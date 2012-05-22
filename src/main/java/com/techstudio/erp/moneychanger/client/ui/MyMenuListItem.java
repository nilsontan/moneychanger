/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

/**
 * @author Nilson
 */
public class MyMenuListItem extends Composite
    implements HasClickHandlers {

  @Override
  public HandlerRegistration addClickHandler(ClickHandler handler) {
    return addDomHandler(handler, ClickEvent.getType());
  }

  public interface Binder extends UiBinder<Widget, MyMenuListItem> {
  }

  private static Binder binder = GWT.create(Binder.class);

  @UiField
  HTMLPanel pane;

  @UiField
  HTML icon;

  @UiField
  Label name;

  @UiField
  Anchor next;

  public MyMenuListItem() {
    initWidget(binder.createAndBindUi(this));
  }

  @UiConstructor
  public MyMenuListItem(String name, String iconName) {
    this();
    this.name.setText(name.trim());
    if (iconName == null || iconName.isEmpty()) {
      hideIcon();
    } else {
      /*Element element = DOM.createSpan();
      element.setClassName("icon-set " + iconName.trim());
      element.setAttribute("style", "position:absolute;");*/
      icon.getElement().getFirstChildElement().setClassName("icon-set " + iconName.trim());
    }
  }

  public void hideIcon() {
    icon.setVisible(false);
  }

  /**
   * Returns where this line item will lead to next
   *
   * @return next page
   */
  public Anchor getNext() {
    return next;
  }
}
