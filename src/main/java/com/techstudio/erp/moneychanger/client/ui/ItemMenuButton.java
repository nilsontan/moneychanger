/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.google.inject.Inject;

/**
 * @author Nilson
 */
public class ItemMenuButton extends Composite
    implements HasText, HasClickHandlers {

  @Override
  public HandlerRegistration addClickHandler(ClickHandler handler) {
    return addDomHandler(handler, ClickEvent.getType());
  }

  public interface Binder extends UiBinder<Widget, ItemMenuButton> {
  }

  @UiField
  SimplePanel pane;

  @UiField
  Label label;

  @UiField
  Image image;

  @Inject
  public ItemMenuButton(final Binder binder) {
    initWidget(binder.createAndBindUi(this));
  }

  public void setResource(ImageResource imageResource) {
    image.setResource(imageResource);
  }

  public void setImageUrl(String imgUrl) {
    image.setUrl(imgUrl);
  }

  @Override
  public void setText(String text) {
    label.setText(text);
  }

  @Override
  public String getText() {
    return label.getText();
  }
}
