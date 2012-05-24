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
public interface CategoryUiHandlers extends UiHandlers {
  void onBack();

  void onNext();

  void setCategoryCode(String code);

  void setCategoryName(String name);

  void edit(String code);

  void create();

  void update();

  void delete();
}
