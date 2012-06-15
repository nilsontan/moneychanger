/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.shared.proxy;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.techstudio.erp.moneychanger.server.domain.CompanyClient;
import com.techstudio.erp.moneychanger.server.locator.ObjectifyLocator;

import java.util.List;

/**
 * @author Nilson
 */
@ProxyFor(value = CompanyClient.class, locator = ObjectifyLocator.class)
public interface CompanyClientProxy extends ClientProxy {
  public static final String AUTHORIZED_PERSONS = "authorizedPersons";

  Boolean getMoneyChanger();

  void setMoneyChanger(Boolean moneyChanger);

  Boolean getRemitter();

  void setRemitter(Boolean remitter);

  String getBizRegNo();

  void setBizRegNo(String bizRegNo);

  String getLicenseNo();

  void setLicenseNo(String licenseNo);

  String getWebsite();

  void setWebsite(String website);

  List<IndividualClientProxy> getAuthorizedPersons();

  void setAuthorizedPersons(List<IndividualClientProxy> authorizedPersons);
}
