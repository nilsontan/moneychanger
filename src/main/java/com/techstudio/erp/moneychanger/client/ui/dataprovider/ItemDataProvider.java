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
import com.techstudio.erp.moneychanger.shared.proxy.ItemProxy;
import com.techstudio.erp.moneychanger.shared.service.ItemRequest;

import java.util.List;

/**
 * @author Nilson
 */
public class ItemDataProvider extends MultiDataProvider<ItemProxy> {

  private final Provider<ItemRequest> itemRequestProvider;

  @Inject
  public ItemDataProvider(Provider<ItemRequest> itemRequestProvider) {
    this.itemRequestProvider = itemRequestProvider;
  }

  public void updateAllData() {
    itemRequestProvider.get().fetchAll()
        .fire(new Receiver<List<ItemProxy>>() {
          @Override
          public void onSuccess(List<ItemProxy> responses) {
            updateRowCount(responses.size(), true);
            updateRowData(0, responses);
            updateList(responses);
          }
        });
  }

  public void updateTableData() {
    itemRequestProvider.get()
        .fetchAll()
        .with(ItemProxy.CATEGORY)
        .with(ItemProxy.CURRENCY)
        .with(ItemProxy.UOM)
        .fire(new Receiver<List<ItemProxy>>() {
          @Override
          public void onSuccess(List<ItemProxy> responses) {
            updateRowCount(responses.size(), true);
            updateRowData(0, responses);
          }
        });
  }

  public void updateListData() {
    itemRequestProvider.get()
        .fetchAll()
        .with(ItemProxy.CATEGORY)
        .with(ItemProxy.CURRENCY)
        .with(ItemProxy.UOM)
        .fire(new Receiver<List<ItemProxy>>() {
          @Override
          public void onSuccess(List<ItemProxy> responses) {
            updateList(responses);
          }
        });
  }

  @Override
  protected void onRangeChanged(HasData<ItemProxy> itemProxyHasData) {
    updateTableData();
  }

  @Override
  protected void onValueChanged(HasSelectedValue<ItemProxy> display) {
    updateListData();
  }
}
