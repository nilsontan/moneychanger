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
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.techstudio.erp.moneychanger.client.resources.Resources;
import com.techstudio.erp.moneychanger.shared.proxy.ItemProxy;

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

  private static Binder binder = GWT.create(Binder.class);

  private final Resources resources = GWT.create(Resources.class);

  @UiField
  HTMLPanel pane;

  @UiField
  Label label;

  @UiField
  Image image;


  public ItemMenuButton() {
    initWidget(binder.createAndBindUi(this));
  }

  public ItemMenuButton(ItemProxy itemProxy) {
    this();
    if (itemProxy.getImageUrl() == null || itemProxy.getImageUrl().isEmpty()) {
      setImageUrl("moneychanger/images/nia.png");
    } else {
      setImageUrl(itemProxy.getImageUrl());
    }
    setText(itemProxy.getCode());
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
