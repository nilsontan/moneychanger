/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.ui;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * @author Nilson
 */
public class IconCell extends AbstractCell<String> {

  static final String PART1 =
      "<div class=\"gwt-HTML menuListItem-icon menuListItem-icon-l icon-container-b\">" +
          "<span class=\"icon-set icon-star\" style=\"position:absolute;\"></span></div>";

  @Override
  public void render(Context context, String id, SafeHtmlBuilder sb) {

    // Append some HTML that sets the text color.
    sb.appendHtmlConstant(PART1);
  }
}
