/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.admin.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.techstudio.erp.moneychanger.client.NameTokens;
import com.techstudio.erp.moneychanger.client.admin.presenter.MenuPresenter.MyView;
import com.techstudio.erp.moneychanger.client.ui.MyMenuListItem;

/**
 * @author Nilson
 */
public class MenuView
    extends ViewWithUiHandlers<MenuUiHandlers>
    implements MyView {

  public interface Binder extends UiBinder<Widget, MenuView> {
  }

  private final Widget widget;

  @UiField
  MyMenuListItem bPos;

  @UiField
  MyMenuListItem bPricing;

  @UiField
  MyMenuListItem bSettings;

  @Inject
  public MenuView(final Binder binder) {
    widget = binder.createAndBindUi(this);
  }

  @Override
  public Widget asWidget() {
    return widget;
  }

  @SuppressWarnings("unused")
  @UiHandler("bPos")
  public void onClickPos(ClickEvent event) {
    History.newItem(NameTokens.getPosPage());
  }

  @SuppressWarnings("unused")
  @UiHandler("bPricing")
  public void onClickPricing(ClickEvent event) {
    History.newItem(NameTokens.getPricingPage());
  }

  @SuppressWarnings("unused")
  @UiHandler("bSettings")
  public void onClickSettings(ClickEvent event) {
    History.newItem(NameTokens.getSettingsPage());
  }

  @SuppressWarnings("unused")
  @UiHandler("bItem")
  public void onClickItem(ClickEvent event) {
    History.newItem(NameTokens.getItemPage());
  }

  @SuppressWarnings("unused")
  @UiHandler("bCategory")
  public void onClickCategory(ClickEvent event) {
    History.newItem(NameTokens.getCategoryPage());
  }

  @SuppressWarnings("unused")
  @UiHandler("bCountry")
  public void onClickCountry(ClickEvent event) {
    History.newItem(NameTokens.getCountryPage());
  }

  @SuppressWarnings("unused")
  @UiHandler("bUom")
  public void onClickUom(ClickEvent event) {
    History.newItem(NameTokens.getUomPage());
  }

}