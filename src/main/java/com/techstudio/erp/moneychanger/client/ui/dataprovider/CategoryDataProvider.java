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
import com.techstudio.erp.moneychanger.shared.proxy.CategoryProxy;
import com.techstudio.erp.moneychanger.shared.service.CategoryRequest;

import java.util.List;

/**
 * @author Nilson
 */
@Singleton
public class CategoryDataProvider extends AbstractDataProvider<CategoryProxy> {

  private static CategoryProxy DEFAULT_CATEGORY;

  private final Provider<CategoryRequest> requestProvider;

  private boolean firstLoad = true;

  @Inject
  public CategoryDataProvider(Provider<CategoryRequest> requestProvider) {
    this.requestProvider = requestProvider;
    updateData();
    findDefault();
  }

  /*@Override
  public void addDataDisplay(HasData<CategoryProxy> display) {
    super.addDataDisplay(display);
    load();
  }*/

  @Override
  public void updateData() {
    requestProvider.get()
        .fetchAll()
        .fire(new Receiver<List<CategoryProxy>>() {
          @Override
          public void onSuccess(List<CategoryProxy> proxies) {
            updateMap(proxies);
            if (firstLoad) {
              onSuccessfulLoad();
              firstLoad = false;
            }
            for (HasData<CategoryProxy> display : getDataDisplays()) {
              onRangeChanged(display);
            }
          }
        });
  }

  @Override
  protected void onRangeChanged(HasData<CategoryProxy> display) {
    final Range range = display.getVisibleRange();
    requestProvider.get()
        .fetchRange(range.getStart(), range.getLength())
        .fire(new Receiver<List<CategoryProxy>>() {
          @Override
          public void onSuccess(List<CategoryProxy> proxies) {
            updateRowData(range.getStart(), proxies);
          }
        });
    requestProvider.get()
        .getCount()
        .fire(new Receiver<Integer>() {
          @Override
          public void onSuccess(Integer total) {
            updateRowCount(total, true);
          }
        });
  }

  @Override
  public CategoryProxy getDefault() {
    if (DEFAULT_CATEGORY == null) {
      findDefault();
    }
    return DEFAULT_CATEGORY;
  }

  private void findDefault() {
    if (DEFAULT_CATEGORY == null) {
      requestProvider.get().fetchAll()
          .fire(new Receiver<List<CategoryProxy>>() {
            @Override
            public void onSuccess(List<CategoryProxy> responses) {
              if (responses.isEmpty()) {
              } else {
                DEFAULT_CATEGORY = responses.get(0);
              }
            }
          });
    }
  }
}
