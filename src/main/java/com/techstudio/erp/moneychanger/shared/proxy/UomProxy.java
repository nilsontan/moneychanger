/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.shared.proxy;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.techstudio.erp.moneychanger.server.domain.Uom;
import com.techstudio.erp.moneychanger.server.locator.ObjectifyLocator;

/**
 * @author Nilson
 */
@ProxyFor(value = Uom.class, locator = ObjectifyLocator.class)
public interface UomProxy extends MyEntityProxy {
  int getScale();

  void setScale(int scale);
}
