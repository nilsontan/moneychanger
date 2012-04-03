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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.techstudio.erp.moneychanger.client.admin.presenter.TestPresenter;

/**
 * @author Nilson
 */
public class TestView
    extends ViewWithUiHandlers<TestUiHandlers>
    implements TestPresenter.MyView {

  public interface Binder extends UiBinder<Widget, TestView> {
  }

  private final Widget widget;

  @UiField
  Button testReset;

  @UiField
  Label testStatus;

  @Inject
  public TestView(final Binder binder) {
    widget = binder.createAndBindUi(this);
  }

  @Override
  public Widget asWidget() {
    return widget;
  }

  @SuppressWarnings("unused")
  @UiHandler("testReset")
  void onResetData(ClickEvent event) {
    getUiHandlers().resetData();
  }

  @Override
  public void setStatus(String message) {
    testStatus.setText(message);
  }

}