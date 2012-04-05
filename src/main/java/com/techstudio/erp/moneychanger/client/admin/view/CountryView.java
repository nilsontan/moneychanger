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
import com.techstudio.erp.moneychanger.client.admin.presenter.CountryPresenter;
import com.techstudio.erp.moneychanger.client.ui.CountryLinkCell;
import com.techstudio.erp.moneychanger.shared.proxy.CountryProxy;

/**
 * @author Nilson
 */
public class CountryView
    extends ViewWithUiHandlers<CountryUiHandlers>
    implements CountryPresenter.MyView {

  public interface Binder extends UiBinder<Widget, CountryView> {
  }

  private final Widget widget;

  @UiField
  TextBox countryCode;

  @UiField
  TextBox countryName;

  @UiField
  TextBox countryFullName;

  @UiField
  Button countryUpdate;

  @UiField
  Button countryCreate;

  @UiField
  CellTable<CountryProxy> countryTable = new CellTable<CountryProxy>();

  @UiField
  SimplePager countryPager = new SimplePager();

  @Inject
  public CountryView(final Binder binder) {
    widget = binder.createAndBindUi(this);
    setupCountryTable();
  }

  @Override
  public Widget asWidget() {
    return widget;
  }

  @UiHandler("countryCode")
  void onCountryCodeChange(ValueChangeEvent<String> event) {
    getUiHandlers().setCountryCode(event.getValue());
  }

  @UiHandler("countryName")
  void onCountryNameChange(ValueChangeEvent<String> event) {
    getUiHandlers().setCountryName(event.getValue());
  }

  @UiHandler("countryFullName")
  void onCountryFullNameChange(ValueChangeEvent<String> event) {
    getUiHandlers().setCountryFullName(event.getValue());
  }

  @SuppressWarnings("unused")
  @UiHandler("countryCreate")
  void onCreateCountry(ClickEvent event) {
    getUiHandlers().createCountry();
  }

  @SuppressWarnings("unused")
  @UiHandler("countryUpdate")
  void onUpdateCountry(ClickEvent event) {
    getUiHandlers().updateCountry();
  }

  @Override
  public HasData<CountryProxy> getTable() {
    return countryTable;
  }

  @Override
  public void enableCreateButton(boolean enabled) {
    countryCreate.setEnabled(enabled);
  }

  @Override
  public void enableUpdateButton(boolean enabled) {
    countryUpdate.setEnabled(enabled);
  }

  @Override
  public void setCountryCode(String code) {
    countryCode.setValue(code);
  }

  @Override
  public void setCountryName(String name) {
    countryName.setValue(name);
  }

  @Override
  public void setCountryFullName(String fullName) {
    countryFullName.setValue(fullName);
  }

  private void setupCountryTable() {
    Column<CountryProxy, String> countryCodeColumn = new Column<CountryProxy, String>(new EditTextCell()) {
      @Override
      public String getValue(CountryProxy countryProxy) {
        return countryProxy.getCode();
      }
    };
    countryTable.addColumn(countryCodeColumn, "Code");

    Column<CountryProxy, String> countryNameColumn = new Column<CountryProxy, String>(new EditTextCell()) {
      @Override
      public String getValue(CountryProxy countryProxy) {
        return countryProxy.getName();
      }
    };
    countryTable.addColumn(countryNameColumn, "Name");

    Column<CountryProxy, String> countryFullNameColumn = new Column<CountryProxy, String>(new EditTextCell()) {
      @Override
      public String getValue(CountryProxy countryProxy) {
        return countryProxy.getFullName();
      }
    };
    countryTable.addColumn(countryFullNameColumn, "Full Name");

    Column<CountryProxy, String> countryCurrencyColumn = new Column<CountryProxy, String>(new EditTextCell()) {
      @Override
      public String getValue(CountryProxy countryProxy) {
        return countryProxy.getCurrency().getCode();
      }
    };
    countryTable.addColumn(countryCurrencyColumn, "Currency");

    Column<CountryProxy, Long> linkColumn = new Column<CountryProxy, Long>(new CountryLinkCell()) {
      @Override
      public Long getValue(CountryProxy countryProxy) {
        return countryProxy.getId();
      }
    };
    countryTable.addColumn(linkColumn);

    countryTable.setPageSize(10);

    countryPager.setDisplay(countryTable);
  }

}