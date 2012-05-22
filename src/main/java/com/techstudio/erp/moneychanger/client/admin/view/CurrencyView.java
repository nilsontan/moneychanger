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
import com.techstudio.erp.moneychanger.client.admin.presenter.CurrencyPresenter;
import com.techstudio.erp.moneychanger.shared.proxy.CurrencyProxy;

/**
 * @author Nilson
 */
public class CurrencyView
    extends ViewWithUiHandlers<CurrencyUiHandlers>
    implements CurrencyPresenter.MyView {

  public interface Binder extends UiBinder<Widget, CurrencyView> {
  }

  private final Widget widget;

  @UiField
  TextBox currencyCode;

  @UiField
  TextBox currencyName;

  @UiField
  TextBox currencyFullName;

  @UiField
  Button currencyUpdate;

  @UiField
  Button currencyCreate;

  @UiField
  CellTable<CurrencyProxy> currencyTable = new CellTable<CurrencyProxy>();

  @UiField
  SimplePager currencyPager = new SimplePager();

  @Inject
  public CurrencyView(final Binder binder) {
    widget = binder.createAndBindUi(this);
    setupCurrencyTable();
  }

  @Override
  public Widget asWidget() {
    return widget;
  }

  @UiHandler("currencyCode")
  void onCurrencyCodeChange(ValueChangeEvent<String> event) {
    getUiHandlers().setCurrencyCode(event.getValue());
  }

  @UiHandler("currencyName")
  void onCurrencyNameChange(ValueChangeEvent<String> event) {
    getUiHandlers().setCurrencyName(event.getValue());
  }

  @UiHandler("currencyFullName")
  void onCurrencyFullNameChange(ValueChangeEvent<String> event) {
    getUiHandlers().setCurrencyFullName(event.getValue());
  }

  @SuppressWarnings("unused")
  @UiHandler("currencyCreate")
  void onCreateCurrency(ClickEvent event) {
    getUiHandlers().createCurrency();
  }

  @SuppressWarnings("unused")
  @UiHandler("currencyUpdate")
  void onUpdateCurrency(ClickEvent event) {
    getUiHandlers().updateCurrency();
  }

  @Override
  public HasData<CurrencyProxy> getTable() {
    return currencyTable;
  }

  @Override
  public void enableCreateButton(boolean enabled) {
    currencyCreate.setEnabled(enabled);
  }

  @Override
  public void enableUpdateButton(boolean enabled) {
    currencyUpdate.setEnabled(enabled);
  }

  @Override
  public void setCurrencyCode(String code) {
    currencyCode.setValue(code);
  }

  @Override
  public void setCurrencyName(String name) {
    currencyName.setValue(name);
  }

  @Override
  public void setCurrencyFullName(String fullName) {
    currencyFullName.setValue(fullName);
  }

  private void setupCurrencyTable() {
    Column<CurrencyProxy, String> currencyCodeColumn = new Column<CurrencyProxy, String>(new EditTextCell()) {
      @Override
      public String getValue(CurrencyProxy currencyProxy) {
        return currencyProxy.getCode();
      }
    };
    currencyTable.addColumn(currencyCodeColumn, "Code");

    Column<CurrencyProxy, String> currencyNameColumn = new Column<CurrencyProxy, String>(new EditTextCell()) {
      @Override
      public String getValue(CurrencyProxy currencyProxy) {
        return currencyProxy.getName();
      }
    };
    currencyTable.addColumn(currencyNameColumn, "Name");

    Column<CurrencyProxy, String> currencyFullNameColumn = new Column<CurrencyProxy, String>(new EditTextCell()) {
      @Override
      public String getValue(CurrencyProxy currencyProxy) {
        return currencyProxy.getFullName();
      }
    };
    currencyTable.addColumn(currencyFullNameColumn, "Full Name");

//    Column<CurrencyProxy, Long> linkColumn = new Column<CurrencyProxy, Long>(new CurrencyLinkCell()) {
//      @Override
//      public Long getValue(CurrencyProxy currencyProxy) {
//        return currencyProxy.getId();
//      }
//    };
//    currencyTable.addColumn(linkColumn);

    currencyTable.setPageSize(10);

    currencyPager.setDisplay(currencyTable);
  }

}