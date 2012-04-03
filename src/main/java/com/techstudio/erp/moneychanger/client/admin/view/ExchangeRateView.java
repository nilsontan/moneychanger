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
import com.techstudio.erp.moneychanger.client.admin.presenter.ExchangeRatePresenter;
import com.techstudio.erp.moneychanger.client.ui.ExchangeRateLinkCell;
import com.techstudio.erp.moneychanger.client.ui.HasSelectedValue;
import com.techstudio.erp.moneychanger.client.ui.SelectOneListBox;
import com.techstudio.erp.moneychanger.shared.proxy.CurrencyProxy;
import com.techstudio.erp.moneychanger.shared.proxy.ExchangeRateProxy;

/**
 * @author Nilson
 */
public class ExchangeRateView
    extends ViewWithUiHandlers<ExchangeRateUiHandlers>
    implements ExchangeRatePresenter.MyView {

  public interface Binder extends UiBinder<Widget, ExchangeRateView> {
  }

  private final Widget widget;

  @UiField
  TextBox xrCode;

  @UiField
  TextBox xrName;

  @UiField(provided = true)
  SelectOneListBox<CurrencyProxy> xrCurrencyList
      = new SelectOneListBox<CurrencyProxy>(new SelectOneListBox.OptionFormatter<CurrencyProxy>() {
    @Override
    public String getLabel(CurrencyProxy option) {
      return option.getName();
    }

    @Override
    public String getValue(CurrencyProxy option) {
      return option.getId().toString();
    }
  });

  @UiField
  TextBox xrUnit;

  @UiField
  TextBox xrAsk;

  @UiField
  TextBox xrBid;

  @UiField
  Button xrUpdate;

  @UiField
  Button xrCreate;

  @UiField
  CellTable<ExchangeRateProxy> xrTable = new CellTable<ExchangeRateProxy>();

  @UiField
  SimplePager xrPager = new SimplePager();

  @Inject
  public ExchangeRateView(Binder binder) {
    widget = binder.createAndBindUi(this);
    setupXrTable();
  }

  @Override
  public Widget asWidget() {
    return widget;
  }

  @UiHandler("xrCode")
  public void onXrCodeChange(ValueChangeEvent<String> event) {
    getUiHandlers().setXrCode(event.getValue());
  }

  @UiHandler("xrName")
  public void onXrNameChange(ValueChangeEvent<String> event) {
    getUiHandlers().setXrName(event.getValue());
  }

  @SuppressWarnings("unused")
  @UiHandler("xrCurrencyList")
  public void onXrCurrencyChange(ValueChangeEvent<CurrencyProxy> event) {
    getUiHandlers().setXrCurrency(xrCurrencyList.getSelectedValue());
  }

  @UiHandler("xrAsk")
  public void onXrAskChange(ValueChangeEvent<String> event) {
    getUiHandlers().setXrAskRate(event.getValue());
  }

  @UiHandler("xrBid")
  public void onXrBidChange(ValueChangeEvent<String> event) {
    getUiHandlers().setXrBidRate(event.getValue());
  }

  @SuppressWarnings("unused")
  @UiHandler("xrCreate")
  public void onCreateCurrency(ClickEvent event) {
    getUiHandlers().createXr();
  }

  @SuppressWarnings("unused")
  @UiHandler("xrUpdate")
  public void onUpdateCurrency(ClickEvent event) {
    getUiHandlers().updateXr();
  }

  @Override
  public HasData<ExchangeRateProxy> getXrTable() {
    return xrTable;
  }

  @Override
  public HasSelectedValue<CurrencyProxy> getCurrencyList() {
    return xrCurrencyList;
  }

  @Override
  public void enableCreateButton(boolean isValid) {
    xrCreate.setEnabled(isValid);
  }

  @Override
  public void enableUpdateButton(boolean isValid) {
    xrUpdate.setEnabled(isValid);
  }

  @Override
  public void setXrCode(String code) {
    xrCode.setValue(code);
  }

  @Override
  public void setXrName(String name) {
    xrName.setValue(name);
  }

  @Override
  public void setXrCurrency(CurrencyProxy currencyProxy) {
    xrCurrencyList.setSelectedValue(currencyProxy);
  }

  @Override
  public void setXrUnit(String unit) {
    xrUnit.setValue(unit);
  }

  @Override
  public void setXrAsk(String askRate) {
    xrAsk.setValue(askRate);
  }

  @Override
  public void setXrBid(String bidRate) {
    xrBid.setValue(bidRate);
  }

  private void setupXrTable() {
    Column<ExchangeRateProxy, String> xrCodeColumn = new Column<ExchangeRateProxy, String>(new EditTextCell()) {
      @Override
      public String getValue(ExchangeRateProxy xrProxy) {
        return xrProxy.getCode();
      }
    };
    xrTable.addColumn(xrCodeColumn, "Code");

    Column<ExchangeRateProxy, String> xrNameColumn = new Column<ExchangeRateProxy, String>(new EditTextCell()) {
      @Override
      public String getValue(ExchangeRateProxy xrProxy) {
        return xrProxy.getName();
      }
    };
    xrTable.addColumn(xrNameColumn, "Name");

    Column<ExchangeRateProxy, String> xrCurrencyColumn = new Column<ExchangeRateProxy, String>(new EditTextCell()) {
      @Override
      public String getValue(ExchangeRateProxy xrProxy) {
        return xrProxy.getCurrency().getName();
      }
    };
    xrTable.addColumn(xrCurrencyColumn, "Currency");

    Column<ExchangeRateProxy, String> xrUnitColumn = new Column<ExchangeRateProxy, String>(new EditTextCell()) {
      @Override
      public String getValue(ExchangeRateProxy xrProxy) {
        return xrProxy.getUnit().toString();
      }
    };
    xrTable.addColumn(xrUnitColumn, "Unit");

    Column<ExchangeRateProxy, String> xrAskColumn = new Column<ExchangeRateProxy, String>(new EditTextCell()) {
      @Override
      public String getValue(ExchangeRateProxy xrProxy) {
        return xrProxy.getAskRate();
      }
    };
    xrTable.addColumn(xrAskColumn, "Ask");

    Column<ExchangeRateProxy, String> xrBidColumn = new Column<ExchangeRateProxy, String>(new EditTextCell()) {
      @Override
      public String getValue(ExchangeRateProxy xrProxy) {
        return xrProxy.getBidRate();
      }
    };
    xrTable.addColumn(xrBidColumn, "Bid");

    Column<ExchangeRateProxy, Long> linkColumn = new Column<ExchangeRateProxy, Long>(new ExchangeRateLinkCell()) {
      @Override
      public Long getValue(ExchangeRateProxy xrProxy) {
        return xrProxy.getId();
      }
    };
    xrTable.addColumn(linkColumn);

    xrTable.setPageSize(10);

    xrPager.setDisplay(xrTable);
  }
}