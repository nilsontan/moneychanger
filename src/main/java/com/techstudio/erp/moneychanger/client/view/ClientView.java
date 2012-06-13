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
import com.techstudio.erp.moneychanger.client.presenter.ClientPresenter;
import com.techstudio.erp.moneychanger.client.ui.LabelInput;
import com.techstudio.erp.moneychanger.client.ui.MyCheckBox;
import com.techstudio.erp.moneychanger.client.ui.RangeLabelPager;
import com.techstudio.erp.moneychanger.client.ui.ShowMorePagerPanel;
import com.techstudio.erp.moneychanger.client.ui.cell.ClientCell;
import com.techstudio.erp.moneychanger.shared.proxy.ClientProxy;

/**
 * @author Nilson
 */
public class ClientView
    extends ViewWithUiHandlers<ClientUiHandlers>
    implements ClientPresenter.MyView {

  public interface Binder extends UiBinder<Widget, ClientView> {
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
  CellList<ClientProxy> list = new CellList<ClientProxy>(new ClientCell());

  @UiField
  ShowMorePagerPanel pagerPanel = new ShowMorePagerPanel();

  @UiField
  RangeLabelPager pager = new RangeLabelPager();

  @UiField
  LabelInput name;

  @UiField
  MyCheckBox cBoxMChanger;

  @UiField
  MyCheckBox cBoxRAgent;

  @UiField
  Button save;

  @Inject
  public ClientView(final Binder binder) {
    widget = binder.createAndBindUi(this);
    setUpListing();
  }

  @Override
  public Widget asWidget() {
    return widget;
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

  @UiHandler("name")
  void onClientNameChange(ValueChangeEvent<String> event) {
    getUiHandlers().setName(event.getValue());
  }

  @SuppressWarnings("unused")
  @UiHandler("cBoxMChanger")
  void onClickMChanger(ClickEvent event) {
    getUiHandlers().toggleMChanger();
  }

  @SuppressWarnings("unused")
  @UiHandler("cBoxRAgent")
  void onClickRAgent(ClickEvent event) {
    getUiHandlers().toggleMChanger();
  }

  @SuppressWarnings("unused")
  @UiHandler("save")
  void onCreateClient(ClickEvent event) {
    getUiHandlers().update();
  }

  @Override
  public HasData<ClientProxy> getListing() {
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
  public void setName(String name) {
    this.name.setValue(name);
  }

  @Override
  public void showLoading(boolean visible) {
    loadingMessage.setVisible(visible);
    currentStep.setVisible(!visible);
    mainPanel.setVisible(!visible);
  }

  @Override
  public void setMChanger(boolean checked) {
    cBoxMChanger.setChecked(checked);
  }

  @Override
  public void setRAgent(boolean checked) {
    cBoxRAgent.setChecked(checked);
  }

  private void setUpListing() {
    final NoSelectionModel<ClientProxy> selectionModel = new NoSelectionModel<ClientProxy>();
    list.setSelectionModel(selectionModel);
    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
      @Override
      public void onSelectionChange(SelectionChangeEvent event) {
        ClientProxy selected = selectionModel.getLastSelectedObject();
        if (selected != null) {
          getUiHandlers().edit(selected.getId());
        }
      }
    });

    pagerPanel.setDisplay(list);
    pager.setDisplay(list);
  }

}