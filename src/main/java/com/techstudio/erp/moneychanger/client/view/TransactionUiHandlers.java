/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.view;

import com.gwtplatform.mvp.client.UiHandlers;
import com.techstudio.erp.moneychanger.shared.proxy.CategoryProxy;
import com.techstudio.erp.moneychanger.shared.proxy.ItemProxy;

/**
 * @author Nilson
 */
public interface TransactionUiHandlers extends UiHandlers {
  void search();

  void setSearchDate(String value);

  void setSearchCategory(CategoryProxy selectedValue);

  void setSearchItem(ItemProxy selectedValue);

  void setSearchPending(Boolean selectedValue);

  void edit(Long id);

  void create();

  void update();

  void delete();
}
