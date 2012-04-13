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
public interface SpotRateUiHandlers extends UiHandlers {
  void setSpotRateCode(String code);

  void setSpotRateName(String name);

  void setSpotRateAskRate(String askRate);

  void setSpotRateBidRate(String bidRate);

  void createSpotRate();

  void updateSpotRate();
}
