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
import com.techstudio.erp.moneychanger.shared.proxy.ExchangeRateProxy;
import com.techstudio.erp.moneychanger.shared.service.ExchangeRateRequest;

import java.util.List;

/**
 * @author Nilson
 */
public class ExchangeRateDataProvider extends MultiDataProvider<ExchangeRateProxy> {

  private final Provider<ExchangeRateRequest> xrRequestProvider;

  @Inject
  public ExchangeRateDataProvider(Provider<ExchangeRateRequest> xrRequestProvider) {
    this.xrRequestProvider = xrRequestProvider;
  }

  public void updateAllData() {
    xrRequestProvider.get().fetchAll()
        .fire(new Receiver<List<ExchangeRateProxy>>() {
          @Override
          public void onSuccess(List<ExchangeRateProxy> responses) {
            updateRowCount(responses.size(), true);
            updateRowData(0, responses);
            updateList(responses);
          }
        });
  }

  public void updateTableData() {
    xrRequestProvider.get()
        .fetchAll()
        .with(ExchangeRateProxy.CURRENCY)
        .fire(new Receiver<List<ExchangeRateProxy>>() {
          @Override
          public void onSuccess(List<ExchangeRateProxy> responses) {
            updateRowCount(responses.size(), true);
            updateRowData(0, responses);
          }
        });
  }

  public void updateListData() {
    xrRequestProvider.get()
        .fetchAll()
        .with(ExchangeRateProxy.CURRENCY)
        .fire(new Receiver<List<ExchangeRateProxy>>() {
          @Override
          public void onSuccess(List<ExchangeRateProxy> responses) {
            updateList(responses);
          }
        });
  }

  @Override
  protected void onRangeChanged(HasData<ExchangeRateProxy> itemProxyHasData) {
    updateTableData();
  }

  @Override
  protected void onValueChanged(HasSelectedValue<ExchangeRateProxy> display) {
    updateListData();
  }
}
