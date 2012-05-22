/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.ui;

import com.google.common.base.Joiner;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * A custom cell that links to the specified URL.
 */
public abstract class AbstractLinkCell extends AbstractCell<String> {

  static final String PART1 = "<a class=\"gwt-Anchor menuListItem-icon menuListItem-icon-r icon-container-b\" href=\"#";
  static final String PART2 = ";c=";
  static final String PART3 = "\"><span class=\"icon-set icon-right\" style=\"position:absolute;\"></span></a>";
//  <g:Anchor ui:field="next" addStyleNames="menuListItem-icon menuListItem-icon-r icon-container-b">
//  <span style="position:absolute;" class="icon-set icon-right" />
//  </g:Anchor>

  abstract String getPageName();

  @Override
  public void render(Context context, String id, SafeHtmlBuilder sb) {

    // Always do a null check on the value. Cell widgets can pass null to cells
    // if the underlying data contains a null, or if the data arrives out of order.
    if (id == null) {
      return;
    }

    // Append some HTML that sets the text color.
    sb.appendHtmlConstant(getHtmlLink(id));
  }

  private String getHtmlLink(String id) {
    return Joiner.on("").join(PART1, getPageName(), PART2, id, PART3);
  }
}

