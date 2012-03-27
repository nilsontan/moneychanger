/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.ui;

import com.techstudio.erp.moneychanger.client.NameTokens;

/**
 * @author Nilson
 */
public class ItemLinkCell extends AbstractLinkCell {
  @Override
  String getPageName() {
    return NameTokens.ITEM_PAGE;
  }
}
