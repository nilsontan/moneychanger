/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.ui;

import com.google.common.collect.Maps;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.techstudio.erp.moneychanger.shared.proxy.PricingProxy;
import com.techstudio.erp.moneychanger.shared.service.PricingRequest;

import java.util.List;
import java.util.Map;

/**
 * @author Nilson
 */
@Singleton
public class PricingDataProvider extends AsyncDataProvider<PricingProxy> {

  private final Provider<PricingRequest> pricingRequestProvider;

  private Map<String, PricingProxy> itemPriceMap = Maps.newHashMap();

  @Inject
  public PricingDataProvider(Provider<PricingRequest> pricingRequestProvider) {
    this.pricingRequestProvider = pricingRequestProvider;
    updateData();
  }

  public void updateData() {
    pricingRequestProvider.get()
        .fetchAll()
        .fire(new Receiver<List<PricingProxy>>() {
          @Override
          public void onSuccess(List<PricingProxy> pricingProxies) {
            updateMap(pricingProxies);
          }
        });

    for (HasData<PricingProxy> display : getDataDisplays()) {
      onRangeChanged(display);
    }
  }

  public PricingProxy getSpotRateForCode(String itemCode) {
    return itemPriceMap.get(itemCode);
  }

  @Override
  protected void onRangeChanged(HasData<PricingProxy> display) {
    final Range range = display.getVisibleRange();
    pricingRequestProvider.get()
        .fetchRange(range.getStart(), range.getLength())
        .fire(new Receiver<List<PricingProxy>>() {
          @Override
          public void onSuccess(List<PricingProxy> pricingProxies) {
            updateRowData(range.getStart(), pricingProxies);
          }
        });
    pricingRequestProvider.get()
        .getCount()
        .fire(new Receiver<Integer>() {
          @Override
          public void onSuccess(Integer total) {
            updateRowCount(total, true);
          }
        });
  }

  private void updateMap(List<PricingProxy> pricingProxies) {
    itemPriceMap.clear();
    for (PricingProxy pricingProxy : pricingProxies) {
      itemPriceMap.put(pricingProxy.getCode(), pricingProxy);
    }
  }
}
