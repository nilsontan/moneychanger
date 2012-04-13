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
import com.techstudio.erp.moneychanger.shared.proxy.LineItemProxy;
import com.techstudio.erp.moneychanger.shared.service.LineItemRequest;

import java.util.List;

/**
 * @author Nilson
 */
public class LineItemDataProvider extends MultiDataProvider<LineItemProxy> {

  private final Provider<LineItemRequest> lineItemRequestProvider;

  @Inject
  public LineItemDataProvider(Provider<LineItemRequest> lineItemRequestProvider) {
    this.lineItemRequestProvider = lineItemRequestProvider;
  }

  public void updateAllData() {
    lineItemRequestProvider.get().fetchAll()
        .fire(new Receiver<List<LineItemProxy>>() {
          @Override
          public void onSuccess(List<LineItemProxy> responses) {
            updateRowCount(responses.size(), true);
            updateRowData(0, responses);
            updateList(responses);
          }
        });
  }

  public void updateTableData() {
    lineItemRequestProvider.get()
        .fetchAll()
        .fire(new Receiver<List<LineItemProxy>>() {
          @Override
          public void onSuccess(List<LineItemProxy> responses) {
            updateRowCount(responses.size(), true);
            updateRowData(0, responses);
          }
        });
  }

  public void updateTableData(List<LineItemProxy> lineItemProxies) {
    updateRowCount(lineItemProxies.size(), true);
    updateRowData(0, lineItemProxies);
  }

  public void updateListData() {
    lineItemRequestProvider.get()
        .fetchAll()
        .fire(new Receiver<List<LineItemProxy>>() {
          @Override
          public void onSuccess(List<LineItemProxy> responses) {
            updateList(responses);
          }
        });
  }

  @Override
  protected void onRangeChanged(HasData<LineItemProxy> rangeDisplay) {
    updateTableData();
  }

  @Override
  protected void onValueChanged(HasSelectedValue<LineItemProxy> listDisplay) {
    updateListData();
  }
}
