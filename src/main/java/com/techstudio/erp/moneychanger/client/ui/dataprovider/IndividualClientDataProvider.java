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
import com.techstudio.erp.moneychanger.shared.proxy.IndividualClientProxy;
import com.techstudio.erp.moneychanger.shared.service.IndividualClientRequest;

import java.util.List;

/**
 * @author Nilson
 */
@Singleton
public class IndividualClientDataProvider extends AbstractDataProvider<IndividualClientProxy> {

  private final Provider<IndividualClientRequest> requestProvider;

  @Inject
  public IndividualClientDataProvider(Provider<IndividualClientRequest> requestProvider) {
    this.requestProvider = requestProvider;
    updateData();
  }

  @Override
  public void updateData() {
    requestProvider.get()
        .fetchAll()
        .fire(new Receiver<List<IndividualClientProxy>>() {
          @Override
          public void onSuccess(List<IndividualClientProxy> proxies) {
            updateRowCount(proxies.size(), true);
            updateMap(proxies);
            if (firstLoad) {
              onSuccessfulLoad();
              firstLoad = false;
            }
            for (HasData<IndividualClientProxy> display : getDataDisplays()) {
              onRangeChanged(display);
            }
            updateList(proxies);
          }
        });
  }

  @Override
  protected void onRangeChanged(final HasData<IndividualClientProxy> display) {
    final Range range = display.getVisibleRange();
    requestProvider.get()
        .fetchRange(range.getStart(), range.getLength())
        .fire(new Receiver<List<IndividualClientProxy>>() {
          @Override
          public void onSuccess(List<IndividualClientProxy> proxies) {
            updateRowData(display, range.getStart(), proxies);
          }
        });

  }

  @Override
  protected void onValueChanged(HasSelectedValue<IndividualClientProxy> display) {
  }
}
