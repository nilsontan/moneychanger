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
public interface UomUiHandlers extends UiHandlers {
  void setUomCode(String code);

  void setUomName(String name);

  void createUom();

  void updateUom();
}
