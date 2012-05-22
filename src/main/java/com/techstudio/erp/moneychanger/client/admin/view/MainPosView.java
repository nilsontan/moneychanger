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
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.techstudio.erp.moneychanger.client.NameTokens;
import com.techstudio.erp.moneychanger.client.admin.presenter.MainPosPresenter;
import com.techstudio.erp.moneychanger.client.admin.presenter.MainPosPresenter.MyView;

/**
 * This is the top-level view of the POS application. Every time another presenter
 * wants to reveal itself, {@link MainPosView} will add its content of the
 * target inside the {@code mainContentContainer}.
 *
 * @author Nilson
 */
public class MainPosView
    extends ViewImpl implements MyView {

  public interface Binder extends UiBinder<Widget, MainPosView> {
  }

  private final Widget widget;

  @UiField
  HTMLPanel loadingMessage;

  @UiField
  Anchor ancHome;

  @UiField
  Anchor ancMenu;

  @UiField
  HTMLPanel mainContentContainer;

  @Inject
  public MainPosView(final Binder binder) {
    widget = binder.createAndBindUi(this);
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
  @UiHandler("ancMenu")
  public void onClickMenu(ClickEvent event) {
  }

  @Override
  public void setInSlot(Object slot, Widget content) {
    if (slot == MainPosPresenter.TYPE_SetMainContent) {
      setMainContent(content);
    } else {
      super.setInSlot(slot, content);
    }
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