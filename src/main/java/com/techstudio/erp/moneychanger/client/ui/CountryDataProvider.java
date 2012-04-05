/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.ui;

import com.google.gwt.view.client.HasData;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.techstudio.erp.moneychanger.shared.proxy.CountryProxy;
import com.techstudio.erp.moneychanger.shared.service.CountryRequest;

import java.util.List;

/**
 * @author Nilson
 */
public class CountryDataProvider extends MultiDataProvider<CountryProxy> {

  private static CountryProxy DEFAULT_COUNTRY;

  private final Provider<CountryRequest> countryRequestProvider;

  @Inject
  public CountryDataProvider(Provider<CountryRequest> countryRequestProvider) {
    this.countryRequestProvider = countryRequestProvider;
    findDefault();
  }

  public void updateAllData() {
    countryRequestProvider.get().fetchAll()
        .with(CountryProxy.CURRENCY)
        .fire(new Receiver<List<CountryProxy>>() {
          @Override
          public void onSuccess(List<CountryProxy> countryProxies) {
            updateRowCount(countryProxies.size(), true);
            updateRowData(0, countryProxies);
            updateList(countryProxies);
          }
        });
  }

  public void updateTableData() {
    countryRequestProvider.get().fetchAll()
        .with(CountryProxy.CURRENCY)
        .fire(new Receiver<List<CountryProxy>>() {
          @Override
          public void onSuccess(List<CountryProxy> countryProxies) {
            updateRowCount(countryProxies.size(), true);
            updateRowData(0, countryProxies);
          }
        });
  }

  public void updateListData() {
    countryRequestProvider.get().fetchAll()
        .with(CountryProxy.CURRENCY)
        .fire(new Receiver<List<CountryProxy>>() {
          @Override
          public void onSuccess(List<CountryProxy> responses) {
            updateList(responses);
          }
        });
  }

  @Override
  protected void onRangeChanged(HasData<CountryProxy> countryProxyHasData) {
    updateTableData();
  }

  @Override
  protected void onValueChanged(HasSelectedValue<CountryProxy> display) {
    updateListData();
  }

  public CountryProxy getDefaultCountry() {
    if (DEFAULT_COUNTRY == null) {
      findDefault();
    }
    return DEFAULT_COUNTRY;
  }

  private void findDefault() {
    countryRequestProvider.get().fetchAll()
        .fire(new Receiver<List<CountryProxy>>() {
          @Override
          public void onSuccess(List<CountryProxy> responses) {
            if (responses.isEmpty()) {
            } else {
              for (CountryProxy proxy : responses) {
                if (proxy.getCode().equals("SG")) {
                  DEFAULT_COUNTRY = proxy;
                  break;
                }
              }
              if (DEFAULT_COUNTRY == null) {
                DEFAULT_COUNTRY = responses.get(0);
              }
            }
          }
        });
  }
}
