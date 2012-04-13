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
import com.techstudio.erp.moneychanger.shared.proxy.UomProxy;

/**
 * @author Nilson
 */
public interface ItemUiHandlers extends UiHandlers {
  void setItemCode(String code);

  void setItemName(String name);

  void setItemFullName(String fullName);

  void setItemCategory(CategoryProxy category);

  void setItemCurrency(CurrencyProxy currency);

  void setItemUom(UomProxy uomProxy);

  void setItemUomRate(String uomRate);

  void createItem();

  void updateItem();
}
