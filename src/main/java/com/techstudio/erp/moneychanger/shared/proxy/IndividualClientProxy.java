/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.shared.proxy;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.techstudio.erp.moneychanger.server.domain.IndividualClient;
import com.techstudio.erp.moneychanger.server.locator.ObjectifyLocator;

import java.util.Date;

/**
 * @author Nilson
 */
@ProxyFor(value = IndividualClient.class, locator = ObjectifyLocator.class)
public interface IndividualClientProxy extends ClientProxy {

  String getNric();

  void setNric(String nric);

  Date getDateOfBirth();

  void setDateOfBirth(Date dateOfBirth);

  CountryProxy getNationality();

  void setNationality(CountryProxy nationality);

  String getJobTitle();

  void setJobTitle(String jobTitle);
}
