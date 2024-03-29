/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.shared.proxy;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.techstudio.erp.moneychanger.server.domain.Category;
import com.techstudio.erp.moneychanger.server.locator.ObjectifyLocator;

/**
 * @author Nilson
 */
@ProxyFor(value = Category.class, locator = ObjectifyLocator.class)
public interface CategoryProxy extends MyEntityProxy {
  public static final String PARENT = "parent";
  public static final String UOM = "uom";

  CategoryProxy getParent();

  void setParent(CategoryProxy categoryProxy);

  UomProxy getUom();

  void setUom(UomProxy uomProxy);
}
