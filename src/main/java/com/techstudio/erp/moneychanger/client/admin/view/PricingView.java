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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.techstudio.erp.moneychanger.client.admin.presenter.PricingPresenter;
import com.techstudio.erp.moneychanger.client.resources.Resources;
import com.techstudio.erp.moneychanger.client.ui.ItemPriceCell;
import com.techstudio.erp.moneychanger.client.ui.LabelInput;
import com.techstudio.erp.moneychanger.client.ui.RangeLabelPager;
import com.techstudio.erp.moneychanger.client.ui.ShowMorePagerPanel;
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

  @UiField(provided = true)
  final Resources res;

  @UiField
  HTMLPanel mainPanel;

  @UiField
  HTMLPanel listingPanel;

  @UiField(provided = true)
  CellList<PricingProxy> priceL = new CellList<PricingProxy>(new ItemPriceCell());

  @UiField
  ShowMorePagerPanel pagerPanel = new ShowMorePagerPanel();

  @UiField
  RangeLabelPager pager = new RangeLabelPager();

  @UiField
  Label prLabel;

  @UiField
  LabelInput prBid;

  @UiField
  LabelInput prAsk;

//  @UiField
//  Button prAdd;

  @UiField
  Button prSav;

//  @UiField
//  Button prDel;

  @Inject
  public PricingView(Binder binder, final Resources resources) {
    this.res = resources;
    widget = binder.createAndBindUi(this);
    setUpListing();
  }

  @Override
  public Widget asWidget() {
    return widget;
  }

  @UiHandler("prBid")
  public void onBidChange(ValueChangeEvent<String> event) {
    getUiHandlers().setPricingBidRate(event.getValue());
  }

  @UiHandler("prAsk")
  public void onAskChange(ValueChangeEvent<String> event) {
    getUiHandlers().setPricingAskRate(event.getValue());
  }

//  @SuppressWarnings("unused")
//  @UiHandler("prAdd")
//  public void onCreate(ClickEvent event) {
//    getUiHandlers().create();
//  }

  @SuppressWarnings("unused")
  @UiHandler("prSav")
  public void onUpdate(ClickEvent event) {
    getUiHandlers().update();
  }

//  @SuppressWarnings("unused")
//  @UiHandler("prDel")
//  public void onDelete(ClickEvent event) {
//    getUiHandlers().delete();
//  }

  @Override
  public void showListPanel() {
    mainPanel.setStyleName("slider show1");
    priceL.setVisible(true);
  }

  @Override
  public HasData<PricingProxy> getListing() {
    return priceL;
  }

  @Override
  public void showDetailPanel() {
    mainPanel.setStyleName("slider show2");
    priceL.setVisible(false);
  }

  @Override
  public void setPriceTitle(String title) {
    prLabel.setText(title);
  }

  @Override
  public void setPriceAsk(String askRate) {
    prAsk.setValue(askRate);
  }

  @Override
  public void setPriceBid(String bidRate) {
    prBid.setValue(bidRate);
  }

  private void setUpListing() {
    final NoSelectionModel<PricingProxy> selectionModel = new NoSelectionModel<PricingProxy>();
    priceL.setSelectionModel(selectionModel);
    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
      @Override
      public void onSelectionChange(SelectionChangeEvent event) {
        PricingProxy selected = selectionModel.getLastSelectedObject();
        if (selected != null) {
          getUiHandlers().edit(selected.getCode());
        }
      }
    });

    pagerPanel.setDisplay(priceL);
    pager.setDisplay(priceL);
  }
}