/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.shared.proxy;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.techstudio.erp.moneychanger.server.domain.Item;
import com.techstudio.erp.moneychanger.server.locator.ObjectifyLocator;

/**
 * @author Nilson
 */
@ProxyFor(value = Item.class, locator = ObjectifyLocator.class)
public interface ItemProxy extends BaseEntityProxy {
  public static final String CATEGORY = "category";
  public static final String CURRENCY = "currency";

  CategoryProxy getCategory();

  void setCategory(CategoryProxy categoryProxy);

  CurrencyProxy getCurrency();

  void setCurrency(CurrencyProxy currencyProxy);
}
