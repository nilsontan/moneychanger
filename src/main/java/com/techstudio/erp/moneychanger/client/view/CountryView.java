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
import com.techstudio.erp.moneychanger.client.presenter.CountryPresenter;
import com.techstudio.erp.moneychanger.client.ui.LabelInput;
import com.techstudio.erp.moneychanger.client.ui.RangeLabelPager;
import com.techstudio.erp.moneychanger.client.ui.ShowMorePagerPanel;
import com.techstudio.erp.moneychanger.client.ui.cell.CountryCell;
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
  HTMLPanel ancBar;

  @UiField
  Anchor ancHome;

  @UiField
  Anchor ancBack;

  @UiField
  Anchor ancNext;

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
  CellList<CountryProxy> list = new CellList<CountryProxy>(new CountryCell());

  @UiField
  ShowMorePagerPanel pagerPanel = new ShowMorePagerPanel();

  @UiField
  RangeLabelPager pager = new RangeLabelPager();

  @UiField
  LabelInput code;

  @UiField
  LabelInput name;

  @UiField
  LabelInput fullname;

  @UiField
  Button add;

  @UiField
  Button save;

  @UiField
  Button delete;

  @Inject
  public CountryView(final Binder binder) {
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
    getUiHandlers().onBack();
  }

  @SuppressWarnings("unused")
  @UiHandler("ancNext")
  public void onClickNext(ClickEvent event) {
    getUiHandlers().onNext();
  }

  @UiHandler("code")
  public void onCodeChange(ValueChangeEvent<String> event) {
    getUiHandlers().setCode(event.getValue());
  }

  @UiHandler("name")
  public void onNameChange(ValueChangeEvent<String> event) {
    getUiHandlers().setName(event.getValue());
  }

  @UiHandler("fullname")
  public void onAskChange(ValueChangeEvent<String> event) {
    getUiHandlers().setFullName(event.getValue());
  }

  @SuppressWarnings("unused")
  @UiHandler("add")
  public void onCreate(ClickEvent event) {
    getUiHandlers().create();
  }

  @SuppressWarnings("unused")
  @UiHandler("save")
  public void onUpdate(ClickEvent event) {
    getUiHandlers().update();
  }

  @SuppressWarnings("unused")
  @UiHandler("delete")
  public void onDelete(ClickEvent event) {
    getUiHandlers().delete();
  }

  @Override
  public HasData<CountryProxy> getListing() {
    return list;
  }

  @Override
  public void showListPanel() {
//    mainPanel.setStyleName("slider show1");
//    list.setVisible(true);
    listPanel.setVisible(true);
    detailPanel.setVisible(false);
    ancHome.setVisible(true);
    ancNext.setVisible(true);
    ancBack.setVisible(false);
  }

  @Override
  public void showDetailPanel() {
//    mainPanel.setStyleName("slider show2");
//    list.setVisible(false);
    listPanel.setVisible(false);
    detailPanel.setVisible(true);
    ancHome.setVisible(false);
    ancNext.setVisible(false);
    ancBack.setVisible(true);
  }

  @Override
  public void showAddButtons() {
    add.setVisible(true);
    save.setVisible(false);
    delete.setVisible(false);
  }

  @Override
  public void showEditButtons() {
    add.setVisible(false);
    save.setVisible(true);
    delete.setVisible(true);
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
  public void setFullname(String fullname) {
    this.fullname.setValue(fullname);
  }

  @Override
  public void showLoading(boolean visible) {
    loadingMessage.setVisible(visible);
    currentStep.setVisible(!visible);
    mainPanel.setVisible(!visible);
    ancBar.setVisible(!visible);
  }

  private void setUpListing() {
    final NoSelectionModel<CountryProxy> selectionModel = new NoSelectionModel<CountryProxy>();
    list.setSelectionModel(selectionModel);
    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
      @Override
      public void onSelectionChange(SelectionChangeEvent event) {
        CountryProxy selected = selectionModel.getLastSelectedObject();
        if (selected != null) {
          getUiHandlers().edit(selected.getCode());
        }
      }
    });

    pagerPanel.setDisplay(list);
    pager.setDisplay(list);
  }
}