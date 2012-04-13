/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.ui;

import com.google.common.collect.Maps;
import com.google.gwt.view.client.HasData;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.techstudio.erp.moneychanger.shared.proxy.SpotRateProxy;
import com.techstudio.erp.moneychanger.shared.service.SpotRateRequest;

import java.util.List;
import java.util.Map;

/**
 * @author Nilson
 */
public class SpotRateDataProvider extends MultiDataProvider<SpotRateProxy> {

  private final Provider<SpotRateRequest> spotRateRequestProvider;

  private Map<String, SpotRateProxy> spotRateMap = Maps.newHashMap();

  @Inject
  public SpotRateDataProvider(Provider<SpotRateRequest> spotRateRequestProvider) {
    this.spotRateRequestProvider = spotRateRequestProvider;
  }

  public void updateAllData() {
    spotRateRequestProvider.get().fetchAll()
        .fire(new Receiver<List<SpotRateProxy>>() {
          @Override
          public void onSuccess(List<SpotRateProxy> responses) {
            updateRowCount(responses.size(), true);
            updateRowData(0, responses);
            updateList(responses);
            updateMap(responses);
          }
        });
  }

  public void updateTableData() {
    spotRateRequestProvider.get()
        .fetchAll()
        .fire(new Receiver<List<SpotRateProxy>>() {
          @Override
          public void onSuccess(List<SpotRateProxy> responses) {
            updateRowCount(responses.size(), true);
            updateRowData(0, responses);
            updateMap(responses);
          }
        });
  }

  public void updateListData() {
    spotRateRequestProvider.get()
        .fetchAll()
        .fire(new Receiver<List<SpotRateProxy>>() {
          @Override
          public void onSuccess(List<SpotRateProxy> responses) {
            updateList(responses);
            updateMap(responses);
          }
        });
  }

  public SpotRateProxy getSpotRateForCode(String itemCode) {
    return spotRateMap.get(itemCode);
  }

  @Override
  protected void onRangeChanged(HasData<SpotRateProxy> rangeDisplay) {
    updateTableData();
  }

  @Override
  protected void onValueChanged(HasSelectedValue<SpotRateProxy> listDisplay) {
    updateListData();
  }

  private void updateMap(List<SpotRateProxy> spotRateProxies) {
    spotRateMap.clear();
    for (SpotRateProxy spotRateProxy : spotRateProxies) {
      spotRateMap.put(spotRateProxy.getCode(), spotRateProxy);
    }
  }
}
