/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.admin.view;

import com.gwtplatform.mvp.client.UiHandlers;

/**
 * @author Nilson
 */
public interface CountryUiHandlers extends UiHandlers {
  void setCountryCode(String code);

  void setCountryName(String name);

  void setCountryFullName(String fullName);

  void createCountry();

  void updateCountry();
}