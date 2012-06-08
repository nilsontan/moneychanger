/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.techstudio.erp.moneychanger.client.NameTokens;
import com.techstudio.erp.moneychanger.client.presenter.UomPresenter;
import com.techstudio.erp.moneychanger.client.ui.LabelInput;
import com.techstudio.erp.moneychanger.client.ui.RangeLabelPager;
import com.techstudio.erp.moneychanger.client.ui.ShowMorePagerPanel;
import com.techstudio.erp.moneychanger.client.ui.cell.UomCell;
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
  Anchor ancHome;

  @UiField
  Anchor ancBack;

  @UiField
  HTMLPanel loadingMessage;

  @UiField
  Label currentStep;

  @UiField
  HTMLPanel mainPanel;

  @UiField
  HTMLPanel listPanel;

  @UiField
  HTMLPanel detailPanel;

  @UiField(provided = true)
  CellList<UomProxy> list = new CellList<UomProxy>(new UomCell());

  @UiField
  ShowMorePagerPanel pagerPanel = new ShowMorePagerPanel();

  @UiField
  RangeLabelPager pager = new RangeLabelPager();

  @UiField
  Label label;

  @UiField
  LabelInput code;

  @UiField
  LabelInput name;

  @UiField
  LabelInput scale;

  @UiField
  Button save;

  @Inject
  public UomView(final Binder binder) {
    widget = binder.createAndBindUi(this);
    setUpListing();
  }

  @Override
  public Widget asWidget() {
    return widget;
  }

  @UiHandler("code")
  void onUomCodeChange(ValueChangeEvent<String> event) {
    getUiHandlers().setCode(event.getValue());
  }

  @UiHandler("name")
  void onUomNameChange(ValueChangeEvent<String> event) {
    getUiHandlers().setName(event.getValue());
  }

  @SuppressWarnings("unused")
  @UiHandler("ancHome")
  public void onClickHome(ClickEvent event) {
    History.newItem(NameTokens.getMenuPage());
  }

  @SuppressWarnings("unused")
  @UiHandler("ancBack")
  public void onClickBack(ClickEvent event) {
    showListPanel();
  }

  @SuppressWarnings("unused")
  @UiHandler("save")
  void onCreateUom(ClickEvent event) {
    getUiHandlers().update();
  }

  @Override
  public HasData<UomProxy> getListing() {
    return list;
  }

  @Override
  public void showListPanel() {
//    mainPanel.setStyleName("slider show1");
//    list.setVisible(true);
    listPanel.setVisible(true);
    detailPanel.setVisible(false);
    ancHome.setVisible(true);
    ancBack.setVisible(false);
  }

  @Override
  public void showDetailPanel() {
//    mainPanel.setStyleName("slider show2");
//    list.setVisible(false);
    listPanel.setVisible(false);
    detailPanel.setVisible(true);
    ancHome.setVisible(false);
    ancBack.setVisible(true);
  }

  @Override
  public void setCode(String code) {
    this.code.setValue(code);
  }

  @Override
  public void setName(String name) {
    this.name.setValue(name);
  }

  @Override
  public void setScale(String scale) {
    this.scale.setValue(scale);
  }

  @Override
  public void showLoading(boolean visible) {
    loadingMessage.setVisible(visible);
    currentStep.setVisible(!visible);
    mainPanel.setVisible(!visible);
  }

  private void setUpListing() {
    final NoSelectionModel<UomProxy> selectionModel = new NoSelectionModel<UomProxy>();
    list.setSelectionModel(selectionModel);
    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
      @Override
      public void onSelectionChange(SelectionChangeEvent event) {
        UomProxy selected = selectionModel.getLastSelectedObject();
        if (selected != null) {
          getUiHandlers().edit(selected.getCode());
        }
      }
    });

    pagerPanel.setDisplay(list);
    pager.setDisplay(list);
  }

}