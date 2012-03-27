/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.admin.view;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.techstudio.erp.moneychanger.client.admin.presenter.MainPagePresenter;
import com.techstudio.erp.moneychanger.client.admin.presenter.MainPagePresenter.MyView;
import com.techstudio.erp.moneychanger.client.ui.MainMenu;

/**
 * This is the top-level view of the application. Every time another presenter
 * wants to reveal itself, {@link MainPageView} will add its content of the
 * target inside the {@code mainContentContainer}.
 *
 * @author Nilson
 */
public class MainPageView extends ViewImpl implements MyView {

  public interface Binder extends UiBinder<DockLayoutPanel, MainPageView> {
  }

//  private static Binder uiBinder = GWT.create(Binder.class);

  private final DockLayoutPanel widget;

  @UiField
  LayoutPanel mainContentContainer;

//  @UiField
//  Element loadingMessage;

  @UiField(provided = true)
  MainMenu mainMenuTop;

  @UiField(provided = true)
  MainMenu mainMenuBtm;

  @Inject
  public MainPageView(final Binder binder,
                      final MainMenu mainMenuTop,
                      final MainMenu mainMenuBtm) {
    this.mainMenuTop = mainMenuTop;
    this.mainMenuBtm = mainMenuBtm;
    widget = binder.createAndBindUi(this);
  }

  @Override
  public Widget asWidget() {
    return widget;
  }

  @Override
  public void setInSlot(Object slot, Widget content) {
    if (slot == MainPagePresenter.TYPE_SetTopContent) {
      setTopBarContent(content);
    } else if (slot == MainPagePresenter.TYPE_SetMainContent) {
      setMainContent(content);
    } else {
      super.setInSlot(slot, content);
    }
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
//    loadingMessage.getStyle().setVisibility(
//        visible ? Visibility.VISIBLE : Visibility.HIDDEN);
  }
}