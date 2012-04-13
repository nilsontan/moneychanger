/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.ui;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * A simple HTML5 number box.
 *
 * @author Nilson
 */
public class NumberBox extends Composite {

  public interface Binder extends UiBinder<Widget, NumberBox> {
  }

  @UiField
  HTML numBox;

  JavaScriptObject e;

  @Inject
  public NumberBox(final Binder binder) {
    initWidget(binder.createAndBindUi(this));
    e = numBox.getElement().getChild(0);
  }

  public String getValue() {
    return GetValue(e);
  }

  public void setValue(String value) {
    SetValue(e, value);
  }

  private final native String GetValue(JavaScriptObject elem) /*-{
    return elem.value;
  }-*/;

  private final native String SetValue(JavaScriptObject elem, String value) /*-{
    elem.value = value;
  }-*/;
}