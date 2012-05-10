/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.ui;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesUtils;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.*;
import com.techstudio.erp.moneychanger.client.resources.Resources;
import com.techstudio.erp.moneychanger.shared.proxy.ItemProxy;

/**
 * @author Nilson
 */
public class ItemMenuCell extends AbstractCell<ItemProxy> {

  interface Template extends SafeHtmlTemplates {
    @Template("<button type=\"button\" class=\"{0}\">" +
        "<img src=\"{1}\" style=\"{2}\" border=\"0\" class=\"gwt-Image\">" +
        "<span>{3}</span></button>")
    SafeHtml cell(String buttonStyle, SafeUri itemImgUrl, SafeStyles imgStyle, SafeHtml itemCode);
  }

  private static final Template TEMPLATES = GWT.create(Template.class);

  private static final Resources RESOURCES = GWT.create(Resources.class);

  private static final String BUTTON_STYLE = "";

  /**
   * Construct a new ItemMenuCell.
   */
  public ItemMenuCell() {
    /*
     * Sink the click event. Handle click event in this class.
     */
    super("click");
  }

  @Override
  public void onBrowserEvent(Context context, Element parent, ItemProxy value,
                             NativeEvent event, ValueUpdater<ItemProxy> valueUpdater) {
    super.onBrowserEvent(context, parent, value, event, valueUpdater);

    if ("click".equals(event.getType())) {
      // Ignore clicks that occur outside of the outermost element
      EventTarget eventTarget = event.getEventTarget();
      if (parent.getFirstChildElement().isOrHasChild(Element.as(eventTarget))) {
        doAction(value, valueUpdater);
      }
    }
  }

  private void doAction(ItemProxy value, ValueUpdater<ItemProxy> valueUpdater) {
    valueUpdater.update(value);
  }

  @Override
  public void render(Context context, ItemProxy item, SafeHtmlBuilder sb) {
    if (item == null) {
      return;
    }

    SafeUri itemImgUrl = item.getImageUrl().isEmpty() ?
        RESOURCES.iNoImageAvailable().getSafeUri() :
        UriUtils.fromTrustedString(item.getImageUrl());
    SafeStyles imgStyle = SafeStylesUtils.fromTrustedString("display:block;margin:0 auto;");
    SafeHtml itemCode = SafeHtmlUtils.fromString(item.getCode());

    sb.append(TEMPLATES.cell(BUTTON_STYLE, itemImgUrl, imgStyle, itemCode));
  }
}
