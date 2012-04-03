/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.admin.view;

import com.gwtplatform.mvp.client.UiHandlers;
import com.techstudio.erp.moneychanger.shared.proxy.CurrencyProxy;

/**
 * @author Nilson
 */
public interface ExchangeRateUiHandlers extends UiHandlers {
  void setXrCode(String code);

  void setXrName(String name);

  void setXrCurrency(CurrencyProxy currency);

  void setXrUnit(Integer unit);

  void setXrAskRate(String askRate);

  void setXrBidRate(String bidRate);

  void createXr();

  void updateXr();
}
