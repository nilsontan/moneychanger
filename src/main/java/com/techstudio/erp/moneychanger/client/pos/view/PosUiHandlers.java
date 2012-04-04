/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.pos.view;

import com.gwtplatform.mvp.client.UiHandlers;

/**
 * @author Nilson
 */
public interface PosUiHandlers extends UiHandlers {
  void createNewTransaction();

  void addToTransaction();

  void deleteTransaction();

  void saveTransaction();

  void itemSelected(String itemCode);
}
