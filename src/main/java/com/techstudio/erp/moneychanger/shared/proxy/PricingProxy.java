/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.shared.proxy;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.techstudio.erp.moneychanger.server.domain.SpotRate;
import com.techstudio.erp.moneychanger.server.locator.ObjectifyLocator;

import java.math.BigDecimal;

/**
 * @author Nilson
 */
@ProxyFor(value = SpotRate.class, locator = ObjectifyLocator.class)
public interface PricingProxy extends MyEntityProxy {
  BigDecimal getAskRate();

  void setAskRate(BigDecimal askRate);

  BigDecimal getBidRate();

  void setBidRate(BigDecimal bidRate);
}
