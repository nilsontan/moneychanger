/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.admin.view;

import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.techstudio.erp.moneychanger.client.admin.presenter.CategoryPresenter;
import com.techstudio.erp.moneychanger.shared.proxy.CategoryProxy;

/**
 * @author Nilson
 */
public class CategoryView
    extends ViewWithUiHandlers<CategoryUiHandlers>
    implements CategoryPresenter.MyView {

  public interface Binder extends UiBinder<Widget, CategoryView> {
  }

  private final Widget widget;

  @UiField
  TextBox categoryCode;

  @UiField
  TextBox categoryName;

  @UiField
  Button categoryCreate;

  @UiField
  Button categoryUpdate;

  @UiField
  CellTable<CategoryProxy> categoryTable = new CellTable<CategoryProxy>();

  @UiField
  SimplePager categoryPager = new SimplePager();

  @Inject
  public CategoryView(final Binder binder) {
    widget = binder.createAndBindUi(this);
    setupCategoryTable();
  }

  @Override
  public Widget asWidget() {
    return widget;
  }

  @UiHandler("categoryCode")
  public void onCategoryCodeChange(ValueChangeEvent<String> event) {
    getUiHandlers().setCategoryCode(event.getValue());
  }

  @UiHandler("categoryName")
  public void onCategoryNameChange(ValueChangeEvent<String> event) {
    getUiHandlers().setCategoryName(event.getValue());
  }

  @SuppressWarnings("unused")
  @UiHandler("categoryCreate")
  public void onCreateCurrency(ClickEvent event) {
    getUiHandlers().createCategory();
  }

  @SuppressWarnings("unused")
  @UiHandler("categoryUpdate")
  public void onUpdateCurrency(ClickEvent event) {
    getUiHandlers().updateCategory();
  }

  @Override
  public HasData<CategoryProxy> getTable() {
    return categoryTable;
  }

  @Override
  public void enableCreateButton(boolean isValid) {
    categoryCreate.setEnabled(isValid);
  }

  @Override
  public void enableUpdateButton(boolean isValid) {
    categoryUpdate.setEnabled(isValid);
  }

  @Override
  public void setCategoryCode(String code) {
    categoryCode.setValue(code);
  }

  @Override
  public void setCategoryName(String name) {
    categoryName.setValue(name);
  }

  private void setupCategoryTable() {
    Column<CategoryProxy, String> categoryCodeColumn = new Column<CategoryProxy, String>(new EditTextCell()) {
      @Override
      public String getValue(CategoryProxy categoryProxy) {
        return categoryProxy.getCode();
      }
    };
    categoryTable.addColumn(categoryCodeColumn, "Code");

    Column<CategoryProxy, String> categoryNameColumn = new Column<CategoryProxy, String>(new EditTextCell()) {
      @Override
      public String getValue(CategoryProxy categoryProxy) {
        return categoryProxy.getName();
      }
    };
    categoryTable.addColumn(categoryNameColumn, "Name");

//    Column<CategoryProxy, Long> linkColumn = new Column<CategoryProxy, Long>(new CategoryLinkCell()) {
//      @Override
//      public Long getValue(CategoryProxy categoryProxy) {
//        return categoryProxy.getId();
//      }
//    };
//    categoryTable.addColumn(linkColumn);

    categoryPager.setDisplay(categoryTable);
  }

}