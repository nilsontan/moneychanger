/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.pos.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.techstudio.erp.moneychanger.client.pos.presenter.MainPosPresenter;
import com.techstudio.erp.moneychanger.client.pos.presenter.MainPosPresenter.MyView;
import com.techstudio.erp.moneychanger.client.ui.ItemMenuButton;
import com.techstudio.erp.moneychanger.client.ui.PosLogo;

/**
 * This is the top-level view of the POS application. Every time another presenter
 * wants to reveal itself, {@link MainPosView} will add its content of the
 * target inside the {@code mainContentContainer}.
 *
 * @author Nilson
 */
public class MainPosView extends ViewImpl implements MyView {

  public interface Binder extends UiBinder<DockLayoutPanel, MainPosView> {
  }

  private final DockLayoutPanel widget;

  @UiField(provided = true)
  PosLogo logo;

  @UiField(provided = true)
  ItemMenuButton homeBtn;

  @UiField
  DecoratorPanel loadingMessage;

  @UiField
  LayoutPanel mainContentContainer;

  @Inject
  public MainPosView(final Binder binder,
                     final ItemMenuButton homeButton,
                     final PosLogo logo) {
    this.logo = logo;
    this.homeBtn = homeButton;
    widget = binder.createAndBindUi(this);
  }

  @Override
  public Widget asWidget() {
    return widget;
  }

  @Override
  public void setInSlot(Object slot, Widget content) {
    if (slot == MainPosPresenter.TYPE_SetTopContent) {
      setTopBarContent(content);
    } else if (slot == MainPosPresenter.TYPE_SetMainContent) {
      setMainContent(content);
    } else {
      super.setInSlot(slot, content);
    }
  }

  @SuppressWarnings("unused")
  @UiHandler("homeBtn")
  public void onTxView(ClickEvent event) {
//    getUiHandlers().switchView();
  }

  private void setTopBarContent(Widget topBarContent) {
//    topBarContainer.clear();
//    if (topBarContent != null) {
//      topBarContainer.add(topBarContent);
//    }
  }

  private void setMainContent(Widget mainContent) {
    mainContentContainer.clear();
    if (mainContent != null) {
      mainContentContainer.add(mainContent);
    }
  }

  @Override
  public void showLoading(boolean visible) {
    loadingMessage.setVisible(visible);
  }
}