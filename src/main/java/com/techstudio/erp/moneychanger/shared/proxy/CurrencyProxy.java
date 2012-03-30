/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.shared.proxy;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.techstudio.erp.moneychanger.server.domain.Currency;
import com.techstudio.erp.moneychanger.server.locator.ObjectifyLocator;

/**
 * @author Nilson
 */
@ProxyFor(value = Currency.class, locator = ObjectifyLocator.class)
public interface CurrencyProxy extends BaseEntityProxy {
  String getSign();

  void setSign(String sign);
}
