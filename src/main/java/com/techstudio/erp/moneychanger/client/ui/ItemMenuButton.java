/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.ui;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Image;

/**
 * @author Nilson
 */
public class ItemMenuButton extends Button {
  private String text;
  private Element imgElement;

  public ItemMenuButton() {
    super();
  }

  public void setResource(ImageResource imageResource) {
    Image img = new Image(imageResource);
    setImage(img);
  }

  public void setImage(Image img) {
    if (imgElement != null) {
      DOM.removeChild(getElement(), imgElement);
    }
    imgElement = img.getElement();
    String definedStyles = imgElement.getAttribute("style");
    imgElement.setAttribute("style", definedStyles + "; vertical-align:middle;");
    DOM.insertBefore(getElement(), img.getElement(), DOM.getFirstChild(getElement()));
  }

  @Override
  public void setText(String text) {
    this.text = text;
    Element span = DOM.createElement("span");
    span.setInnerText(text);

    DOM.insertChild(getElement(), span, 0);
  }

  @Override
  public String getText() {
    return this.text;
  }
}
