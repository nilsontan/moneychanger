/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.ui;

import com.google.common.base.Joiner;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.techstudio.erp.moneychanger.client.NameTokens;
import com.techstudio.erp.moneychanger.shared.domain.TransactionType;
import com.techstudio.erp.moneychanger.shared.proxy.LineItemProxy;

/**
 * @author Nilson
 */
public class TxSubTotalCell extends AbstractCell<LineItemProxy> {

  static final String PART1 = "<span style=\"color:";
  static final String PART2 = ";\">";
  static final String PART3 = "</span>";

  @Override
  public void render(Context context, LineItemProxy value, SafeHtmlBuilder sb) {
    // Always do a null check on the value. Cell widgets can pass null to cells
    // if the underlying data contains a null, or if the data arrives out of order.
    if (value == null) {
      return;
    }

    // Append some HTML that sets the text color.
    sb.appendHtmlConstant(getHtmlLink(value));
  }

  private String getHtmlLink(LineItemProxy value) {
    String color = value.getTransactionType().equals(TransactionType.SALE) ? "green" : "brown";
    return Joiner.on("").join(PART1, color, PART2, value.getSubTotal(), PART3);
  }
}
