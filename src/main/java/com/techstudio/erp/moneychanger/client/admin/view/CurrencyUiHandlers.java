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
public interface CurrencyUiHandlers extends UiHandlers {
  void setCurrencyCode(String code);
  void setCurrencyName(String name);

  void createCurrency();
  void updateCurrency();

  void repopulateData();
}
