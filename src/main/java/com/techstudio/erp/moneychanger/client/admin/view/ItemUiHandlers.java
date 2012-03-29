/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.admin.view;

import com.gwtplatform.mvp.client.UiHandlers;
import com.techstudio.erp.moneychanger.shared.proxy.CategoryProxy;
import com.techstudio.erp.moneychanger.shared.proxy.CurrencyProxy;

/**
 * @author Nilson
 */
public interface ItemUiHandlers extends UiHandlers {
  void setItemCode(String code);

  void setItemName(String name);

  void setItemCategory(CategoryProxy category);

  void setItemCurrency(CurrencyProxy currency);

  void createItem();

  void updateItem();
}
