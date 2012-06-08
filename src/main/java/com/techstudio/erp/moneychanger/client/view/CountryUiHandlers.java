/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.view;

import com.gwtplatform.mvp.client.UiHandlers;

/**
 * @author Nilson
 */
public interface CountryUiHandlers extends UiHandlers {
  void onBack();

  void onNext();

  void setCode(String code);

  void setName(String name);

  void setFullName(String fullName);

  void edit(String code);

  void create();

  void update();

  void delete();
}
