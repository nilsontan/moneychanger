/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.pos.view;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.techstudio.erp.moneychanger.client.pos.presenter.PosPresenter.MyView;
import com.techstudio.erp.moneychanger.client.ui.CategoryLinkCell;
import com.techstudio.erp.moneychanger.shared.proxy.CategoryProxy;

import java.util.Date;

/**
 * @author Nilson
 */
public class PosView
    extends ViewWithUiHandlers<PosUiHandlers>
    implements MyView {

  public interface Binder extends UiBinder<Widget, PosView> {
  }

  private final Widget widget;

  @UiField
  Grid catGrid;

  @UiField
  TextBox categoryName;

  @UiField
  CellTable<CategoryProxy> categoryTable = new CellTable<CategoryProxy>();

  @UiField
  SimplePager categoryPager = new SimplePager();

  @Inject
  public PosView(final Binder binder) {
    widget = binder.createAndBindUi(this);
    setupCategoryTable();
  }

  @Override
  public Widget asWidget() {
    return widget;
  }

  @UiHandler("categoryName")
  void onCategoryNameChange(ValueChangeEvent<String> event) {
    getUiHandlers().setCategoryName(event.getValue());
  }

//  @UiHandler("categoryParent")
//  void onCategoryParentChange(ChangeEvent event) {
//    getUiHandlers().setParentCategoryIndex(categoryParent.getSelectedIndex());
//  }

  @Override
  public HasData<CategoryProxy> getTable() {
    return categoryTable;
  }

  @Override
  public void setCategoryName(String name) {
    categoryName.setValue(name);
  }

//  @Override
//  public void setParentCategoryIndex(int index) {
//    categoryParent.setSelectedIndex(index);
//  }
//
//  @Override
//  public void setParentCategoryList(List<String> names) {
//    categoryParent.clear();
//    for (String name : names) {
//      categoryParent.addItem(name);
//    }
//  }

  private void setupCategoryTable() {
    Column<CategoryProxy, String> categoryNameColumn = new Column<CategoryProxy, String>(new EditTextCell()) {
      @Override
      public String getValue(CategoryProxy categoryProxy) {
        return categoryProxy.getName();
      }
    };
    categoryTable.addColumn(categoryNameColumn, "Name");

    Column<CategoryProxy, String> categoryParentNameColumn = new Column<CategoryProxy, String>(new EditTextCell()) {
      @Override
      public String getValue(CategoryProxy categoryProxy) {
        return categoryProxy.getParent().getName();
      }
    };
    categoryTable.addColumn(categoryParentNameColumn, "Parent");

    Column<CategoryProxy, Date> dateColumn = new Column<CategoryProxy, Date>(new DateCell()) {
      @Override
      public Date getValue(CategoryProxy categoryProxy) {
        return categoryProxy.getCreationDate();
      }
    };
    categoryTable.addColumn(dateColumn, "Created on");

    Column<CategoryProxy, Long> linkColumn = new Column<CategoryProxy, Long>(new CategoryLinkCell()) {
      @Override
      public Long getValue(CategoryProxy categoryProxy) {
        return categoryProxy.getId();
      }
    };
    categoryTable.addColumn(linkColumn);

    categoryPager.setDisplay(categoryTable);
  }
}