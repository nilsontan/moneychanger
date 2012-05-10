/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.ui;

import com.google.common.collect.Lists;
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
public class MenuItemDataProvider extends AsyncDataProvider<List<ItemProxy>> {

  private final Provider<ItemRequest> itemRequestProvider;

  @Inject
  public MenuItemDataProvider(Provider<ItemRequest> itemRequestProvider) {
    this.itemRequestProvider = itemRequestProvider;
  }

  public void updateTableData() {
    itemRequestProvider.get()
        .fetchAll()
        .fire(new Receiver<List<ItemProxy>>() {
          @Override
          public void onSuccess(List<ItemProxy> responses) {
            int count = 0;
            List<List<ItemProxy>> itemList = Lists.newArrayList();
            List<ItemProxy> tempList = Lists.newArrayListWithCapacity(5);
            for (ItemProxy itemProxy : responses) {
              if (count++ == 0) {
                tempList = Lists.newArrayListWithCapacity(5);
              }
              tempList.add(itemProxy);
              if (count == 5) {
                itemList.add(tempList);
                count = 0;
              }
            }
            updateRowCount(itemList.size(), true);
            updateRowData(0, itemList);
          }
        });
  }

  @Override
  protected void onRangeChanged(HasData<List<ItemProxy>> rangeDisplay) {
    updateTableData();
  }
}
