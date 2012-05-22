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
import com.techstudio.erp.moneychanger.client.admin.presenter.UomPresenter;
import com.techstudio.erp.moneychanger.shared.proxy.UomProxy;

/**
 * @author Nilson
 */
public class UomView
    extends ViewWithUiHandlers<UomUiHandlers>
    implements UomPresenter.MyView {

  public interface Binder extends UiBinder<Widget, UomView> {
  }

  private final Widget widget;

  @UiField
  TextBox uomCode;

  @UiField
  TextBox uomName;

  @UiField
  Button uomUpdate;

  @UiField
  Button uomCreate;

  @UiField
  CellTable<UomProxy> uomTable = new CellTable<UomProxy>();

  @UiField
  SimplePager uomPager = new SimplePager();

  @Inject
  public UomView(final Binder binder) {
    widget = binder.createAndBindUi(this);
    setupUomTable();
  }

  @Override
  public Widget asWidget() {
    return widget;
  }

  @UiHandler("uomCode")
  void onUomCodeChange(ValueChangeEvent<String> event) {
    getUiHandlers().setUomCode(event.getValue());
  }

  @UiHandler("uomName")
  void onUomNameChange(ValueChangeEvent<String> event) {
    getUiHandlers().setUomName(event.getValue());
  }

  @SuppressWarnings("unused")
  @UiHandler("uomCreate")
  void onCreateUom(ClickEvent event) {
    getUiHandlers().createUom();
  }

  @SuppressWarnings("unused")
  @UiHandler("uomUpdate")
  void onUpdateUom(ClickEvent event) {
    getUiHandlers().updateUom();
  }

  @Override
  public HasData<UomProxy> getTable() {
    return uomTable;
  }

  @Override
  public void enableCreateButton(boolean enabled) {
    uomCreate.setEnabled(enabled);
  }

  @Override
  public void enableUpdateButton(boolean enabled) {
    uomUpdate.setEnabled(enabled);
  }

  @Override
  public void setUomCode(String code) {
    uomCode.setValue(code);
  }

  @Override
  public void setUomName(String name) {
    uomName.setValue(name);
  }

  private void setupUomTable() {
    Column<UomProxy, String> uomCodeColumn = new Column<UomProxy, String>(new EditTextCell()) {
      @Override
      public String getValue(UomProxy uomProxy) {
        return uomProxy.getCode();
      }
    };
    uomTable.addColumn(uomCodeColumn, "Code");

    Column<UomProxy, String> uomNameColumn = new Column<UomProxy, String>(new EditTextCell()) {
      @Override
      public String getValue(UomProxy uomProxy) {
        return uomProxy.getName();
      }
    };
    uomTable.addColumn(uomNameColumn, "Name");

//    Column<UomProxy, Long> linkColumn = new Column<UomProxy, Long>(new UomLinkCell()) {
//      @Override
//      public Long getValue(UomProxy uomProxy) {
//        return uomProxy.getId();
//      }
//    };
//    uomTable.addColumn(linkColumn);

    uomTable.setPageSize(10);

    uomPager.setDisplay(uomTable);
  }

}