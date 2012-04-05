/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.shared.proxy;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.techstudio.erp.moneychanger.server.domain.ExchangeRate;
import com.techstudio.erp.moneychanger.server.locator.ObjectifyLocator;

/**
 * @author Nilson
 */
@ProxyFor(value = ExchangeRate.class, locator = ObjectifyLocator.class)
public interface ExchangeRateProxy extends MyEntityProxy {
  public static final String CURRENCY = "currency";

  CurrencyProxy getCurrency();

  void setCurrency(CurrencyProxy currencyProxy);

  Integer getUnit();

  void setUnit(Integer unit);

  String getAskRate();

  void setAskRate(String askRate);

  String getBidRate();

  void setBidRate(String bidRate);
}
