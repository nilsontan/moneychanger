/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.shared.proxy;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.techstudio.erp.moneychanger.server.domain.Client;
import com.techstudio.erp.moneychanger.server.locator.ObjectifyLocator;
import com.techstudio.erp.moneychanger.shared.domain.ClientType;

/**
 * @author Nilson
 */
@ProxyFor(value = Client.class, locator = ObjectifyLocator.class)
public interface ClientProxy extends MyEntityProxy {
  public static final String ADDRESS = "address";

  ClientType getClientType();

  void setClientType(ClientType clientType);

  AddressProxy getAddress();

  void setAddress(AddressProxy address);

  CountryProxy getCountry();

  void setCountry(CountryProxy country);

  String getContactNo();

  void setContactNo(String contactNo);

  String getContactNo2();

  void setContactNo2(String contactNo2);

  String getFaxNo();

  void setFaxNo(String faxNo);

  String getEmail();

  void setEmail(String email);
}
