/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.ui.dataprovider;

import com.google.common.collect.Lists;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.techstudio.erp.moneychanger.client.ui.HasSelectedValue;
import com.techstudio.erp.moneychanger.shared.proxy.TransactionProxy;
import com.techstudio.erp.moneychanger.shared.service.TransactionRequest;

import java.util.List;

/**
 * @author Nilson
 */
@Singleton
public class TransactionDataProvider
    extends AbstractDataProvider<TransactionProxy>
    implements SearchFilter<TransactionProxy> {

  private final Provider<TransactionRequest> requestProvider;

  private List<OnSearch> onSearchFilterList = Lists.newArrayList();

  private boolean firstLoad;

  @Inject
  public TransactionDataProvider(Provider<TransactionRequest> requestProvider) {
    this.requestProvider = requestProvider;
  }

  @Override
  public void updateData() {
    requestProvider.get()
        .fetchAll()
        .with("lineItems")
        .fire(new Receiver<List<TransactionProxy>>() {
          @Override
          public void onSuccess(List<TransactionProxy> proxies) {
            updateMap(proxies);
            updateToTable(proxies);
          }
        });
  }

  private void updateToTable(List<TransactionProxy> proxies) {
    updateRowCount(proxies.size(), true);

    if (firstLoad) {
      onSuccessfulLoad();
      firstLoad = false;
    } else {
      for (OnSearch handler : onSearchFilterList) {
        handler.onSuccess(this);
      }
    }

    for (HasData<TransactionProxy> display : getDataDisplays()) {
//      onRangeChanged(display);
      final Range range = display.getVisibleRange();
      updateRowData(display, range.getStart(), proxies);
    }
  }

  @Override
  protected void onRangeChanged(final HasData<TransactionProxy> display) {
    final Range range = display.getVisibleRange();
    requestProvider.get()
        .fetchRange(range.getStart(), range.getLength())
        .with("lineItems")
        .fire(new Receiver<List<TransactionProxy>>() {
          @Override
          public void onSuccess(List<TransactionProxy> proxies) {
            updateRowData(display, range.getStart(), proxies);
          }
        });
  }

  @Override
  protected void onValueChanged(HasSelectedValue<TransactionProxy> display) {
  }

  @Override
  public HandlerRegistration addOnSearchHandler(final OnSearch handler) {
    assert handler != null;
    onSearchFilterList.add(handler);
    return new HandlerRegistration() {
      @Override
      public void removeHandler() {
        onSearchFilterList.remove(handler);
      }
    };
  }

  @Override
  public void updateSearchList(List<TransactionProxy> proxies) {
    updateToTable(proxies);
  }
}
