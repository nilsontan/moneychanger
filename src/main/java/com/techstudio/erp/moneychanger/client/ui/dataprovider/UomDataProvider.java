/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.ui.dataprovider;

import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.techstudio.erp.moneychanger.client.ui.HasSelectedValue;
import com.techstudio.erp.moneychanger.shared.proxy.UomProxy;
import com.techstudio.erp.moneychanger.shared.service.UomRequest;

import java.util.List;

/**
 * @author Nilson
 */
public class UomDataProvider extends AbstractDataProvider<UomProxy> {

  private static UomProxy DEFAULT;

  private final Provider<UomRequest> requestProvider;

  private boolean firstLoad = true;

  @Inject
  public UomDataProvider(Provider<UomRequest> requestProvider) {
    this.requestProvider = requestProvider;
    updateData();
    findDefault();
  }

  @Override
  public void updateData() {
    requestProvider.get()
        .fetchAll()
        .fire(new Receiver<List<UomProxy>>() {
          @Override
          public void onSuccess(List<UomProxy> proxies) {
            updateRowCount(proxies.size(), true);
            updateMap(proxies);
            if (firstLoad) {
              onSuccessfulLoad();
              firstLoad = false;
            }
            for (HasData<UomProxy> display : getDataDisplays()) {
              onRangeChanged(display);
            }
            updateList(proxies);
          }
        });
  }

  @Override
  protected void onRangeChanged(final HasData<UomProxy> display) {
    final Range range = display.getVisibleRange();
    requestProvider.get()
        .fetchRange(range.getStart(), range.getLength())
        .fire(new Receiver<List<UomProxy>>() {
          @Override
          public void onSuccess(List<UomProxy> proxies) {
            updateRowData(display, range.getStart(), proxies);
          }
        });

  }

  @Override
  public UomProxy getDefault() {
    if (DEFAULT == null) {
      findDefault();
    }
    return DEFAULT;
  }

  @Override
  protected void onValueChanged(HasSelectedValue<UomProxy> display) {
  }

  private void findDefault() {
    if (DEFAULT == null) {
      requestProvider.get().fetchAll()
          .fire(new Receiver<List<UomProxy>>() {
            @Override
            public void onSuccess(List<UomProxy> responses) {
              if (responses.isEmpty()) {
              } else {
                DEFAULT = responses.get(0);
              }
            }
          });
    }
  }
}
