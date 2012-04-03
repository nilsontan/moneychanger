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
import com.techstudio.erp.moneychanger.shared.proxy.UomProxy;
import com.techstudio.erp.moneychanger.shared.service.UomRequest;

import java.util.List;

/**
 * @author Nilson
 */
public class UomDataProvider extends MultiDataProvider<UomProxy> {

  private static UomProxy DEFAULT_UOM;

  private final Provider<UomRequest> uomRequestProvider;

  @Inject
  public UomDataProvider(Provider<UomRequest> uomRequestProvider) {
    this.uomRequestProvider = uomRequestProvider;
    findDefault();
  }

  public void updateAllData() {
    uomRequestProvider.get().fetchAll()
        .fire(new Receiver<List<UomProxy>>() {
          @Override
          public void onSuccess(List<UomProxy> responses) {
            updateRowCount(responses.size(), true);
            updateRowData(0, responses);
            updateList(responses);
          }
        });
  }

  public void updateTableData() {
    uomRequestProvider.get().fetchAll()
        .fire(new Receiver<List<UomProxy>>() {
          @Override
          public void onSuccess(List<UomProxy> responses) {
            updateRowCount(responses.size(), true);
            updateRowData(0, responses);
          }
        });
  }

  public void updateListData() {
    uomRequestProvider.get().fetchAll()
        .fire(new Receiver<List<UomProxy>>() {
          @Override
          public void onSuccess(List<UomProxy> responses) {
            updateList(responses);
          }
        });
  }

  @Override
  protected void onRangeChanged(HasData<UomProxy> currencyProxyHasData) {
    updateTableData();
  }

  @Override
  protected void onValueChanged(HasSelectedValue<UomProxy> display) {
    updateListData();
  }

  public UomProxy getDefaultUom() {
    if (DEFAULT_UOM == null) {
      findDefault();
    }
    return DEFAULT_UOM;
  }

  private void findDefault() {
    uomRequestProvider.get().fetchAll()
        .fire(new Receiver<List<UomProxy>>() {
          @Override
          public void onSuccess(List<UomProxy> responses) {
            if (responses.isEmpty()) {
            } else {
              DEFAULT_UOM = responses.get(0);
            }
          }
        });
  }
}
