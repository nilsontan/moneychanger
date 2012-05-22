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
public interface PricingUiHandlers extends UiHandlers {
  void setPricingCode(String code);

  void setPricingAskRate(String askRate);

  void setPricingBidRate(String bidRate);

  void edit(String code);

  void create();

  void update();

  void delete();
}
