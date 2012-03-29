/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.ui;

import com.google.common.collect.ImmutableList;
import com.google.gwt.view.client.HasData;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.techstudio.erp.moneychanger.shared.proxy.CategoryProxy;
import com.techstudio.erp.moneychanger.shared.proxy.NullEntityProxy;
import com.techstudio.erp.moneychanger.shared.service.CategoryRequest;

import java.util.List;

/**
 * @author Nilson
 */
public class CategoryDataProvider extends MultiDataProvider<CategoryProxy> {

  private final Provider<CategoryRequest> categoryRequestProvider;

  private CategoryProxy categoryProxy;

  @Inject
  public CategoryDataProvider(Provider<CategoryRequest> categoryRequestProvider) {
    this.categoryRequestProvider = categoryRequestProvider;
  }

  public void updateAllData() {
    categoryRequestProvider.get().fetchAll().with(CategoryProxy.PARENT)
        .fire(new Receiver<List<CategoryProxy>>() {
          @Override
          public void onSuccess(List<CategoryProxy> categoryProxies) {
            updateRowCount(categoryProxies.size(), true);
            updateRowData(0, categoryProxies);
            ImmutableList<CategoryProxy> list
                = new ImmutableList.Builder<CategoryProxy>()
                .add(NullEntityProxy.CATEGORY)
                .addAll(categoryProxies)
                .build();
            updateList(list);
          }
        });
  }

  public void updateTableData() {
    categoryRequestProvider.get().fetchAll().with(CategoryProxy.PARENT)
        .fire(new Receiver<List<CategoryProxy>>() {
          @Override
          public void onSuccess(List<CategoryProxy> categoryProxies) {
            updateRowCount(categoryProxies.size(), true);
            updateRowData(0, categoryProxies);
          }
        });
  }

  public void updateListData() {
    categoryRequestProvider.get().fetchAll().with(CategoryProxy.PARENT)
        .fire(new Receiver<List<CategoryProxy>>() {
          @Override
          public void onSuccess(List<CategoryProxy> categoryProxies) {
            categoryProxies.remove(categoryProxy);
            ImmutableList<CategoryProxy> list
                = new ImmutableList.Builder<CategoryProxy>()
                .add(NullEntityProxy.CATEGORY)
                .addAll(categoryProxies)
                .build();
            updateList(list);
          }
        });
  }

  @Override
  protected void onRangeChanged(HasData<CategoryProxy> categoryProxyHasData) {
    updateTableData();
  }

  @Override
  protected void onValueChanged(HasSelectedValue<CategoryProxy> display) {
    updateListData();
  }

  public void setCategory(CategoryProxy categoryProxy) {
    this.categoryProxy = categoryProxy;
    updateListData();
  }

  public void removeCategory() {
    this.categoryProxy = null;
  }
}
