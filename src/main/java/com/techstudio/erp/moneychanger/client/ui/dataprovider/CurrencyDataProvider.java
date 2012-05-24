/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.ui.dataprovider;

import com.google.gwt.view.client.HasData;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.techstudio.erp.moneychanger.client.ui.HasSelectedValue;
import com.techstudio.erp.moneychanger.client.ui.MultiDataProvider;
import com.techstudio.erp.moneychanger.shared.proxy.CurrencyProxy;
import com.techstudio.erp.moneychanger.shared.service.CurrencyRequest;

import java.util.List;

/**
 * @author Nilson
 */
public class CurrencyDataProvider extends MultiDataProvider<CurrencyProxy> {

  private static CurrencyProxy DEFAULT_CURRENCY;

  private final Provider<CurrencyRequest> currencyRequestProvider;

  @Inject
  public CurrencyDataProvider(Provider<CurrencyRequest> currencyRequestProvider) {
    this.currencyRequestProvider = currencyRequestProvider;
    findDefault();
  }

  public void updateAllData() {
    currencyRequestProvider.get().fetchAll()
        .fire(new Receiver<List<CurrencyProxy>>() {
          @Override
          public void onSuccess(List<CurrencyProxy> currencyProxies) {
            updateRowCount(currencyProxies.size(), true);
            updateRowData(0, currencyProxies);
            updateList(currencyProxies);
          }
        });
  }

  public void updateTableData() {
    currencyRequestProvider.get().fetchAll()
        .fire(new Receiver<List<CurrencyProxy>>() {
          @Override
          public void onSuccess(List<CurrencyProxy> currencyProxies) {
            updateRowCount(currencyProxies.size(), true);
            updateRowData(0, currencyProxies);
          }
        });
  }

  public void updateListData() {
    currencyRequestProvider.get().fetchAll()
        .fire(new Receiver<List<CurrencyProxy>>() {
          @Override
          public void onSuccess(List<CurrencyProxy> responses) {
            updateList(responses);
          }
        });
  }

  @Override
  protected void onRangeChanged(HasData<CurrencyProxy> currencyProxyHasData) {
    updateTableData();
  }

  @Override
  protected void onValueChanged(HasSelectedValue<CurrencyProxy> display) {
    updateListData();
  }

  public CurrencyProxy getDefaultCurrency() {
    if (DEFAULT_CURRENCY == null) {
      findDefault();
    }
    return DEFAULT_CURRENCY;
  }

  private void findDefault() {
    currencyRequestProvider.get().fetchAll()
        .fire(new Receiver<List<CurrencyProxy>>() {
          @Override
          public void onSuccess(List<CurrencyProxy> responses) {
            if (responses.isEmpty()) {
            } else {
              for (CurrencyProxy proxy : responses) {
                if (proxy.getCode().equals("SGD")) {
                  DEFAULT_CURRENCY = proxy;
                  break;
                }
              }
              if (DEFAULT_CURRENCY == null) {
                DEFAULT_CURRENCY = responses.get(0);
              }
            }
          }
        });
  }
}
