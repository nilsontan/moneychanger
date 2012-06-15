/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Nilson
 */
public class MyCheckBox extends Composite
    implements HasClickHandlers {

  @UiField
  Anchor check;

  @UiField
  SpanElement icon;

  @UiField
  Label label;

  private boolean isChecked;

  @Override
  public HandlerRegistration addClickHandler(ClickHandler handler) {
    return addDomHandler(handler, ClickEvent.getType());
  }

  public interface Binder extends UiBinder<Widget, MyCheckBox> {
  }

  private static Binder binder = GWT.create(Binder.class);

  public MyCheckBox() {
    isChecked = false;
    initWidget(binder.createAndBindUi(this));
    this.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        clicked();
      }
    });
  }

  @UiConstructor
  public MyCheckBox(String text) {
    this();
    this.label.setText(text);
  }

  public void clicked() {
    setChecked(!isChecked);
  }

  public void setChecked(boolean checked) {
    if (!checked) {
      isNotChecked();
    } else {
      isChecked();
    }
  }

  private void isNotChecked() {
    icon.removeClassName("icon-check");
    icon.addClassName("icon-empty");
    check.removeStyleName("bar-blue");
    isChecked = false;
  }

  private void isChecked() {
    icon.removeClassName("icon-empty");
    icon.addClassName("icon-check");
    check.addStyleName("bar-blue");
    isChecked = true;
  }
}
