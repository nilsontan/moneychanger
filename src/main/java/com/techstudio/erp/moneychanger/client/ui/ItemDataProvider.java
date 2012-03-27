/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.ui;

import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.techstudio.erp.moneychanger.shared.proxy.ItemProxy;
import com.techstudio.erp.moneychanger.shared.service.ItemRequest;

import java.util.List;

/**
 * @author Nilson
 */
public class ItemDataProvider extends AsyncDataProvider<ItemProxy> {

  private final Provider<ItemRequest> itemServiceProvider;

  @Inject
  public ItemDataProvider(Provider<ItemRequest> itemServiceProvider) {
    this.itemServiceProvider = itemServiceProvider;
  }

  @Override
  protected void onRangeChanged(HasData<ItemProxy> itemProxyHasData) {
    updateData();
  }

  public void updateData() {
    itemServiceProvider.get().fetchAll().with("category")
        .fire(new Receiver<List<ItemProxy>>() {
          @Override
          public void onSuccess(List<ItemProxy> itemProxies) {
            updateRowCount(itemProxies.size(), true);
            updateRowData(0, itemProxies);
          }
        });
  }
}
