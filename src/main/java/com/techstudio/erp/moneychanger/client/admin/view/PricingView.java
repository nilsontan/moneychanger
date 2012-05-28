/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.admin.view;

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
import com.techstudio.erp.moneychanger.client.admin.presenter.PricingPresenter;
import com.techstudio.erp.moneychanger.client.ui.LabelInput;
import com.techstudio.erp.moneychanger.client.ui.RangeLabelPager;
import com.techstudio.erp.moneychanger.client.ui.ShowMorePagerPanel;
import com.techstudio.erp.moneychanger.client.ui.cell.ItemPriceCell;
import com.techstudio.erp.moneychanger.shared.proxy.PricingProxy;

/**
 * @author Nilson
 */
public class PricingView
    extends ViewWithUiHandlers<PricingUiHandlers>
    implements PricingPresenter.MyView {

  public interface Binder extends UiBinder<Widget, PricingView> {
  }

  private final Widget widget;

  @UiField
  Anchor ancHome;

  @UiField
  HTMLPanel loadingMessage;

  @UiField
  Label currentStep;

  @UiField
  HTMLPanel mainPanel;

  @UiField(provided = true)
  CellList<PricingProxy> list = new CellList<PricingProxy>(new ItemPriceCell());

  @UiField
  ShowMorePagerPanel pagerPanel = new ShowMorePagerPanel();

  @UiField
  RangeLabelPager pager = new RangeLabelPager();

  @UiField
  Label label;

  @UiField
  LabelInput bid;

  @UiField
  LabelInput ask;

  @UiField
  Button save;

  @Inject
  public PricingView(Binder binder) {
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

  @UiHandler("bid")
  public void onBidChange(ValueChangeEvent<String> event) {
    getUiHandlers().setPricingBidRate(event.getValue());
  }

  @UiHandler("ask")
  public void onAskChange(ValueChangeEvent<String> event) {
    getUiHandlers().setPricingAskRate(event.getValue());
  }

//  @SuppressWarnings("unused")
//  @UiHandler("prAdd")
//  public void onCreate(ClickEvent event) {
//    getUiHandlers().create();
//  }

  @SuppressWarnings("unused")
  @UiHandler("save")
  public void onUpdate(ClickEvent event) {
    getUiHandlers().update();
  }

//  @SuppressWarnings("unused")
//  @UiHandler("prDel")
//  public void onDelete(ClickEvent event) {
//    getUiHandlers().delete();
//  }

  @Override
  public HasData<PricingProxy> getListing() {
    return list;
  }

  @Override
  public void showListPanel() {
    mainPanel.setStyleName("slider show1");
    list.setVisible(true);
  }

  @Override
  public void showDetailPanel() {
    mainPanel.setStyleName("slider show2");
    list.setVisible(false);
  }

  @Override
  public void setPriceTitle(String title) {
    label.setText(title);
  }

  @Override
  public void setPriceAsk(String askRate) {
    ask.setValue(askRate);
  }

  @Override
  public void setPriceBid(String bidRate) {
    bid.setValue(bidRate);
  }

  @Override
  public void showLoading(boolean visible) {
    loadingMessage.setVisible(visible);
    currentStep.setVisible(!visible);
    mainPanel.setVisible(!visible);
  }

  private void setUpListing() {
    final NoSelectionModel<PricingProxy> selectionModel = new NoSelectionModel<PricingProxy>();
    list.setSelectionModel(selectionModel);
    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
      @Override
      public void onSelectionChange(SelectionChangeEvent event) {
        PricingProxy selected = selectionModel.getLastSelectedObject();
        if (selected != null) {
          getUiHandlers().edit(selected.getCode());
        }
      }
    });

    pagerPanel.setDisplay(list);
    pager.setDisplay(list);
  }
}