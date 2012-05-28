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

import java.math.BigDecimal;

/**
 * @author Nilson
 */
@ProxyFor(value = Item.class, locator = ObjectifyLocator.class)
public interface ItemProxy extends MyExEntityProxy {
  public static final String CATEGORY = "category";
  public static final String CURRENCY = "currency";
  public static final String UOM = "uom";

  CategoryProxy getCategory();

  void setCategory(CategoryProxy categoryProxy);

  CurrencyProxy getCurrency();

  void setCurrency(CurrencyProxy currencyProxy);

  BigDecimal getUomRate();

  void setUomRate(BigDecimal uomRate);

  String getImageUrl();

  void setImageUrl(String imageUrl);
}
