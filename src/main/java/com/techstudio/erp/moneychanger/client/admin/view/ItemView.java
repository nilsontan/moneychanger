/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.admin.view;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.techstudio.erp.moneychanger.client.admin.presenter.ItemPresenter.MyView;
import com.techstudio.erp.moneychanger.client.ui.ItemLinkCell;
import com.techstudio.erp.moneychanger.shared.proxy.ItemProxy;

import java.util.Date;

/**
 * @author Nilson
 */
public class ItemView
    extends ViewWithUiHandlers<ItemUiHandlers>
    implements MyView {

  public interface Binder extends UiBinder<Widget, ItemView> {
  }

  private final Widget widget;

  @UiField
  TextBox itemName;

  @UiField
  CellTable<ItemProxy> itemTable = new CellTable<ItemProxy>();

  @UiField
  SimplePager itemPager = new SimplePager();

  @Inject
  public ItemView(Binder binder) {
    widget = binder.createAndBindUi(this);
    setupItemTable();
  }

  @Override
  public Widget asWidget() {
    return widget;
  }

  @UiHandler("itemName")
  void onItemNameChange(ValueChangeEvent<String> event) {
    String name = event.getValue();
    if (name == null || name.isEmpty()) {
      return;
    }
    getUiHandlers().updateItemName(event.getValue());
  }

  @Override
  public HasData<ItemProxy> getTable() {
    return itemTable;
  }

  @Override
  public void showItemName(String name) {
    itemName.setValue(name);
  }

  private void setupItemTable() {
    Column<ItemProxy, String> itemNameColumn = new Column<ItemProxy, String>(new EditTextCell()) {
      @Override
      public String getValue(ItemProxy itemProxy) {
        return itemProxy.getName();
      }
    };
    itemTable.addColumn(itemNameColumn, "Item Name");

    Column<ItemProxy, String> itemCategoryColumn = new Column<ItemProxy, String>(new EditTextCell()) {
      @Override
      public String getValue(ItemProxy itemProxy) {
        return itemProxy.getCategory().getName();
      }
    };
    itemTable.addColumn(itemCategoryColumn, "Category");

    Column<ItemProxy, Date> dateColumn = new Column<ItemProxy, Date>(new DateCell()) {
      @Override
      public Date getValue(ItemProxy itemProxy) {
        return itemProxy.getCreationDate();
      }
    };
    itemTable.addColumn(dateColumn, "Created on");

    Column<ItemProxy, Long> linkColumn = new Column<ItemProxy, Long>(new ItemLinkCell()) {
      @Override
      public Long getValue(ItemProxy itemProxy) {
        return itemProxy.getId();
      }
    };
    itemTable.addColumn(linkColumn);

    itemPager.setDisplay(itemTable);
  }
}