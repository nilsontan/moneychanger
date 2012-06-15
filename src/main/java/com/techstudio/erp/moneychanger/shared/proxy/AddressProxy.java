/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.shared.proxy;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.techstudio.erp.moneychanger.shared.domain.Address;

/**
 * @author Nilson
 */
@ProxyFor(value = Address.class)
public interface AddressProxy extends ValueProxy {

  String getLine1();

  void setLine1(String line1);

  String getLine2();

  void setLine2(String line2);

  String getLine3();

  void setLine3(String line3);

  String getPostalCode();

  void setPostalCode(String postalCode);
}
