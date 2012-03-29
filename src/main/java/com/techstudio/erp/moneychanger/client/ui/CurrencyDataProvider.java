/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.ui;

import com.google.common.collect.ImmutableList;
import com.google.gwt.view.client.HasData;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.techstudio.erp.moneychanger.shared.proxy.CurrencyProxy;
import com.techstudio.erp.moneychanger.shared.proxy.NullEntityProxy;
import com.techstudio.erp.moneychanger.shared.service.CurrencyRequest;

import java.util.List;

/**
 * @author Nilson
 */
public class CurrencyDataProvider extends MultiDataProvider<CurrencyProxy> {

  private final Provider<CurrencyRequest> currencyRequestProvider;

  private CurrencyProxy currencyProxy;

  @Inject
  public CurrencyDataProvider(Provider<CurrencyRequest> currencyRequestProvider) {
    this.currencyRequestProvider = currencyRequestProvider;
  }

  public void updateAllData() {
    currencyRequestProvider.get().fetchAll()
        .fire(new Receiver<List<CurrencyProxy>>() {
          @Override
          public void onSuccess(List<CurrencyProxy> currencyProxies) {
            updateRowCount(currencyProxies.size(), true);
            updateRowData(0, currencyProxies);
            ImmutableList<CurrencyProxy> list
                = new ImmutableList.Builder<CurrencyProxy>()
                .add(NullEntityProxy.CURRENCY)
                .addAll(currencyProxies)
                .build();
            updateList(list);
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
          public void onSuccess(List<CurrencyProxy> currencyProxies) {
            currencyProxies.remove(currencyProxy);
            ImmutableList<CurrencyProxy> list
                = new ImmutableList.Builder<CurrencyProxy>()
                .add(NullEntityProxy.CURRENCY)
                .addAll(currencyProxies)
                .build();
            updateList(list);
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

  public void setCurrency(CurrencyProxy currencyProxy) {
    this.currencyProxy = currencyProxy;
    updateListData();
  }

  public void removeCurrency() {
    this.currencyProxy = NullEntityProxy.CURRENCY;
  }
}
