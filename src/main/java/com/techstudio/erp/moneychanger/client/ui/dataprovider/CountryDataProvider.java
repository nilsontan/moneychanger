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
import com.techstudio.erp.moneychanger.shared.proxy.CountryProxy;
import com.techstudio.erp.moneychanger.shared.service.CountryRequest;

import java.util.List;

/**
 * @author Nilson
 */
public class CountryDataProvider extends AbstractDataProvider<CountryProxy> {

  private static CountryProxy DEFAULT;

  private final Provider<CountryRequest> requestProvider;

  @Inject
  public CountryDataProvider(Provider<CountryRequest> requestProvider) {
    this.requestProvider = requestProvider;
    updateData();
    findDefault();
  }

  @Override
  public void updateData() {
    requestProvider.get()
        .fetchAll()
        .with(CountryProxy.CURRENCY)
        .fire(new Receiver<List<CountryProxy>>() {
          @Override
          public void onSuccess(List<CountryProxy> proxies) {
            updateRowCount(proxies.size(), true);
            updateMap(proxies);
            if (firstLoad) {
              onSuccessfulLoad();
              firstLoad = false;
            }
            for (HasData<CountryProxy> display : getDataDisplays()) {
              onRangeChanged(display);
            }
          }
        });
  }

  @Override
  protected void onRangeChanged(final HasData<CountryProxy> display) {
    final Range range = display.getVisibleRange();
    requestProvider.get()
        .fetchRange(range.getStart(), range.getLength())
        .with(CountryProxy.CURRENCY)
        .fire(new Receiver<List<CountryProxy>>() {
          @Override
          public void onSuccess(List<CountryProxy> proxies) {
            updateRowData(display, range.getStart(), proxies);
          }
        });
  }

  @Override
  public CountryProxy getDefault() {
    if (DEFAULT == null) {
      findDefault();
    }
    return DEFAULT;
  }

  @Override
  protected void onValueChanged(HasSelectedValue<CountryProxy> display) {
  }

  private void findDefault() {
    if (DEFAULT == null) {
      requestProvider.get().fetchAll()
          .fire(new Receiver<List<CountryProxy>>() {
            @Override
            public void onSuccess(List<CountryProxy> responses) {
              if (responses.isEmpty()) {
              } else {
                for (CountryProxy proxy : responses) {
                  if (proxy.getCode().equals("SG")) {
                    DEFAULT = proxy;
                    break;
                  }
                }
                if (DEFAULT == null) {
                  DEFAULT = responses.get(0);
                }
              }
            }
          });
    }
  }
}
