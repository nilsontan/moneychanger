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
public abstract class AbstractLinkCell extends AbstractCell<Long> {

  static final String PART1 = "<a href=\"#";
  static final String PART2 = ";id=";
  static final String PART3 = "\">view</a>";

  abstract String getPageName();

  @Override
  public void render(Context context, Long id, SafeHtmlBuilder sb) {

    // Always do a null check on the value. Cell widgets can pass null to cells
    // if the underlying data contains a null, or if the data arrives out of order.
    if (id == null) {
      return;
    }

    // Append some HTML that sets the text color.
    sb.appendHtmlConstant(getHtmlLink(id));
  }

  private String getHtmlLink(Long id) {
    return Joiner.on("").join(PART1, getPageName(), PART2, id.toString(), PART3);
  }
}

