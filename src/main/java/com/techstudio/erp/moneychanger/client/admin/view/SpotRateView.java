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
import com.techstudio.erp.moneychanger.client.admin.presenter.SpotRatePresenter;
import com.techstudio.erp.moneychanger.client.ui.SpotRateLinkCell;
import com.techstudio.erp.moneychanger.shared.proxy.SpotRateProxy;

/**
 * @author Nilson
 */
public class SpotRateView
    extends ViewWithUiHandlers<SpotRateUiHandlers>
    implements SpotRatePresenter.MyView {

  public interface Binder extends UiBinder<Widget, SpotRateView> {
  }

  private final Widget widget;

  @UiField
  TextBox spotRateCode;

  @UiField
  TextBox spotRateName;

  @UiField
  TextBox spotRateBid;

  @UiField
  TextBox spotRateAsk;

  @UiField
  Button spotRateUpdate;

  @UiField
  Button spotRateCreate;

  @UiField
  CellTable<SpotRateProxy> spotRateTable = new CellTable<SpotRateProxy>();

  @UiField
  SimplePager spotRatePager = new SimplePager();

  @Inject
  public SpotRateView(Binder binder) {
    widget = binder.createAndBindUi(this);
    setupSpotRateTable();
  }

  @Override
  public Widget asWidget() {
    return widget;
  }

  @UiHandler("spotRateCode")
  public void onSpotRateCodeChange(ValueChangeEvent<String> event) {
    getUiHandlers().setSpotRateCode(event.getValue());
  }

  @UiHandler("spotRateName")
  public void onSpotRateNameChange(ValueChangeEvent<String> event) {
    getUiHandlers().setSpotRateName(event.getValue());
  }

  @UiHandler("spotRateBid")
  public void onSpotRateBidChange(ValueChangeEvent<String> event) {
    getUiHandlers().setSpotRateBidRate(event.getValue());
  }

  @UiHandler("spotRateAsk")
  public void onSpotRateAskChange(ValueChangeEvent<String> event) {
    getUiHandlers().setSpotRateAskRate(event.getValue());
  }

  @SuppressWarnings("unused")
  @UiHandler("spotRateCreate")
  public void onCreateCurrency(ClickEvent event) {
    getUiHandlers().createSpotRate();
  }

  @SuppressWarnings("unused")
  @UiHandler("spotRateUpdate")
  public void onUpdateCurrency(ClickEvent event) {
    getUiHandlers().updateSpotRate();
  }

  @Override
  public HasData<SpotRateProxy> getSpotRateTable() {
    return spotRateTable;
  }

  @Override
  public void enableCreateButton(boolean isValid) {
    spotRateCreate.setEnabled(isValid);
  }

  @Override
  public void enableUpdateButton(boolean isValid) {
    spotRateUpdate.setEnabled(isValid);
  }

  @Override
  public void setSpotRateCode(String code) {
    spotRateCode.setValue(code);
  }

  @Override
  public void setSpotRateName(String name) {
    spotRateName.setValue(name);
  }

  @Override
  public void setSpotRateAsk(String askRate) {
    spotRateAsk.setValue(askRate);
  }

  @Override
  public void setSpotRateBid(String bidRate) {
    spotRateBid.setValue(bidRate);
  }

  private void setupSpotRateTable() {
    Column<SpotRateProxy, String> spotRateCodeColumn = new Column<SpotRateProxy, String>(new EditTextCell()) {
      @Override
      public String getValue(SpotRateProxy spotRateProxy) {
        return spotRateProxy.getCode();
      }
    };
    spotRateTable.addColumn(spotRateCodeColumn, "Code");

    Column<SpotRateProxy, String> spotRateNameColumn = new Column<SpotRateProxy, String>(new EditTextCell()) {
      @Override
      public String getValue(SpotRateProxy spotRateProxy) {
        return spotRateProxy.getName();
      }
    };
    spotRateTable.addColumn(spotRateNameColumn, "Name");

    Column<SpotRateProxy, String> spotRateBidColumn = new Column<SpotRateProxy, String>(new EditTextCell()) {
      @Override
      public String getValue(SpotRateProxy spotRateProxy) {
        return spotRateProxy.getBidRate().toPlainString();
      }
    };
    spotRateTable.addColumn(spotRateBidColumn, "Bid");

    Column<SpotRateProxy, String> spotRateAskColumn = new Column<SpotRateProxy, String>(new EditTextCell()) {
      @Override
      public String getValue(SpotRateProxy spotRateProxy) {
        return spotRateProxy.getAskRate().toPlainString();
      }
    };
    spotRateTable.addColumn(spotRateAskColumn, "Ask");

    Column<SpotRateProxy, Long> linkColumn = new Column<SpotRateProxy, Long>(new SpotRateLinkCell()) {
      @Override
      public Long getValue(SpotRateProxy spotRateProxy) {
        return spotRateProxy.getId();
      }
    };
    spotRateTable.addColumn(linkColumn);

    spotRateTable.setPageSize(10);

    spotRatePager.setDisplay(spotRateTable);
  }
}