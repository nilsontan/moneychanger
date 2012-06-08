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
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.techstudio.erp.moneychanger.client.ui.HasSelectedValue;
import com.techstudio.erp.moneychanger.shared.proxy.PricingProxy;
import com.techstudio.erp.moneychanger.shared.service.PricingRequest;

import java.util.List;

/**
 * @author Nilson
 */
@Singleton
public class PricingDataProvider extends AbstractDataProvider<PricingProxy> {

  private final Provider<PricingRequest> requestProvider;

  @Inject
  public PricingDataProvider(Provider<PricingRequest> requestProvider) {
    this.requestProvider = requestProvider;
  }

  @Override
  public void updateData() {
    requestProvider.get()
        .fetchAll()
        .fire(new Receiver<List<PricingProxy>>() {
          @Override
          public void onSuccess(List<PricingProxy> proxies) {
            updateRowCount(proxies.size(), true);
            updateMap(proxies);
            if (firstLoad) {
              onSuccessfulLoad();
              firstLoad = false;
            }
            for (HasData<PricingProxy> display : getDataDisplays()) {
              onRangeChanged(display);
            }
          }
        });
  }

  @Override
  protected void onRangeChanged(final HasData<PricingProxy> display) {
    final Range range = display.getVisibleRange();
    requestProvider.get()
        .fetchRange(range.getStart(), range.getLength())
        .fire(new Receiver<List<PricingProxy>>() {
          @Override
          public void onSuccess(List<PricingProxy> proxies) {
            updateRowData(display, range.getStart(), proxies);
          }
        });
  }

  @Override
  protected void onValueChanged(HasSelectedValue<PricingProxy> display) {
  }
}
