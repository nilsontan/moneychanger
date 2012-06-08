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
import com.techstudio.erp.moneychanger.shared.proxy.ItemProxy;
import com.techstudio.erp.moneychanger.shared.service.ItemRequest;

import java.util.List;

/**
 * @author Nilson
 */
public class ItemDataProvider extends AbstractDataProvider<ItemProxy> {

  private final Provider<ItemRequest> requestProvider;

  @Inject
  public ItemDataProvider(Provider<ItemRequest> requestProvider) {
    this.requestProvider = requestProvider;
    updateData();
  }

  @Override
  public void updateData() {
    requestProvider.get()
        .fetchAll()
        .with(ItemProxy.CATEGORY)
        .with(ItemProxy.CURRENCY)
        .with(ItemProxy.UOM)
        .fire(new Receiver<List<ItemProxy>>() {
          @Override
          public void onSuccess(List<ItemProxy> proxies) {
            updateRowCount(proxies.size(), true);
            updateMap(proxies);
            if (firstLoad) {
              onSuccessfulLoad();
              firstLoad = false;
            }
            for (HasData<ItemProxy> display : getDataDisplays()) {
              onRangeChanged(display);
            }
            updateList(proxies);
          }
        });
  }

  @Override
  protected void onRangeChanged(final HasData<ItemProxy> display) {
    final Range range = display.getVisibleRange();
    requestProvider.get()
        .fetchRange(range.getStart(), range.getLength())
        .fire(new Receiver<List<ItemProxy>>() {
          @Override
          public void onSuccess(List<ItemProxy> proxies) {
            updateRowData(display, range.getStart(), proxies);
          }
        });
  }

  @Override
  protected void onValueChanged(HasSelectedValue<ItemProxy> display) {
  }
}
