/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.pos.view;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.HasData;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.techstudio.erp.moneychanger.client.pos.presenter.PosPresenter.MyView;
import com.techstudio.erp.moneychanger.shared.proxy.ExchangeRateProxy;

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
  FlowPanel xrDisplay;

  @UiField
  FlowPanel txPanel;

  @UiField
  Label currentStep;

  @UiField
  Image txNew;

  @UiField
  Image txAdd;

  @UiField
  Image txDel;

  @UiField
  Image txSav;

  @UiField
  Grid curGrid;

  @UiField
  Button btnAud;

  @UiField
  Button btnGbp;

  @UiField
  Button btnMyr;

  @UiField
  Button btnSgd;

  @UiField
  Button btnUsd;

  @UiField
  CellTable<ExchangeRateProxy> xrTable = new CellTable<ExchangeRateProxy>();

  @UiField
  SimplePager xrPager = new SimplePager();

  @Inject
  public PosView(final Binder binder) {
    widget = binder.createAndBindUi(this);
    setupXrTable();
  }

  @Override
  public Widget asWidget() {
    return widget;
  }

  @SuppressWarnings("unused")
  @UiHandler("txNew")
  public void onTxNew(ClickEvent event) {
    getUiHandlers().createNewTransaction();
  }

  @SuppressWarnings("unused")
  @UiHandler("txAdd")
  public void onTxAdd(ClickEvent event) {
    getUiHandlers().addToTransaction();
  }

  @SuppressWarnings("unused")
  @UiHandler("txDel")
  public void onTxDel(ClickEvent event) {
    getUiHandlers().deleteTransaction();
  }

  @SuppressWarnings("unused")
  @UiHandler("txSav")
  public void onTxSav(ClickEvent event) {
    getUiHandlers().saveTransaction();
  }

  @SuppressWarnings("unused")
  @UiHandler("btnAud")
  public void onAud(ClickEvent event) {
    getUiHandlers().itemSelected("AUD");
  }

  @SuppressWarnings("unused")
  @UiHandler("btnGbp")
  public void onGbp(ClickEvent event) {
    getUiHandlers().itemSelected("GBP");
  }

  @SuppressWarnings("unused")
  @UiHandler("btnMyr")
  public void onMyr(ClickEvent event) {
    getUiHandlers().itemSelected("MYR");
  }

  @SuppressWarnings("unused")
  @UiHandler("btnSgd")
  public void onSgd(ClickEvent event) {
    getUiHandlers().itemSelected("SGD");
  }

  @SuppressWarnings("unused")
  @UiHandler("btnUsd")
  public void onUsd(ClickEvent event) {
    getUiHandlers().itemSelected("USD");
  }

  @Override
  public HasData<ExchangeRateProxy> getXrTable() {
    return xrTable;
  }

  @Override
  public void showTxPanel(boolean visible) {
    txPanel.setVisible(visible);
    xrDisplay.setVisible(!visible);
  }

  @Override
  public void showTxNew(boolean visible) {
    txNew.setVisible(visible);
  }

  @Override
  public void showTxAdd(boolean visible) {
    txAdd.setVisible(visible);
  }

  @Override
  public void showTxDel(boolean visible) {
    txDel.setVisible(visible);
  }

  @Override
  public void showTxSav(boolean visible) {
    txSav.setVisible(visible);
  }

  @Override
  public void setStep(String step) {
    currentStep.setText(step);
  }

  private void setupXrTable() {
    Column<ExchangeRateProxy, String> xrNameColumn = new Column<ExchangeRateProxy, String>(new TextCell()) {
      @Override
      public String getValue(ExchangeRateProxy xrProxy) {
        return xrProxy.getName();
      }
    };
    xrTable.addColumn(xrNameColumn, "Name");

    Column<ExchangeRateProxy, String> xrCurrencyColumn = new Column<ExchangeRateProxy, String>(new TextCell()) {
      @Override
      public String getValue(ExchangeRateProxy xrProxy) {
        return xrProxy.getCurrency().getName();
      }
    };
    xrTable.addColumn(xrCurrencyColumn, "Currency");

    Column<ExchangeRateProxy, String> xrUnitColumn = new Column<ExchangeRateProxy, String>(new TextCell()) {
      @Override
      public String getValue(ExchangeRateProxy xrProxy) {
        return xrProxy.getUnit().toString();
      }
    };
    xrTable.addColumn(xrUnitColumn, "Unit");

    Column<ExchangeRateProxy, String> xrAskColumn = new Column<ExchangeRateProxy, String>(new TextCell()) {
      @Override
      public String getValue(ExchangeRateProxy xrProxy) {
        return xrProxy.getAskRate();
      }
    };
    xrTable.addColumn(xrAskColumn, "Ask");

    Column<ExchangeRateProxy, String> xrBidColumn = new Column<ExchangeRateProxy, String>(new TextCell()) {
      @Override
      public String getValue(ExchangeRateProxy xrProxy) {
        return xrProxy.getBidRate();
      }
    };
    xrTable.addColumn(xrBidColumn, "Bid");

    xrTable.setPageSize(20);

    xrPager.setDisplay(xrTable);
  }
}