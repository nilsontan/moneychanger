/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.view;

import com.gwtplatform.mvp.client.UiHandlers;
import com.techstudio.erp.moneychanger.shared.domain.ClientType;
import com.techstudio.erp.moneychanger.shared.proxy.CountryProxy;
import com.techstudio.erp.moneychanger.shared.proxy.IndividualClientProxy;

/**
 * @author Nilson
 */
public interface ClientUiHandlers extends UiHandlers {
  void onBack();

  void onNext();

  void setClientType(ClientType clientType);

  void setName(String name);

  void setAddressLine1(String addressLine1);

  void setAddressLine2(String addressLine2);

  void setAddressLine3(String addressLine3);

  void setCountry(CountryProxy country);

  void setPostalCode(String postalCode);

  void setContactNo(String contactNo);

  void setContactNo2(String contactNo2);

  void setFaxNo(String faxNo);

  void setEmail(String email);

  void setBizReg(String bizReg);

  void setLicenseNo(String licenseNo);

  void setWebsite(String website);

  void setAuthorizedPerson(IndividualClientProxy authorizedPerson);

  void toggleMChanger();

  void toggleRAgent();

  void edit(Long id);

  void create();

  void delete();

  void update();
}
