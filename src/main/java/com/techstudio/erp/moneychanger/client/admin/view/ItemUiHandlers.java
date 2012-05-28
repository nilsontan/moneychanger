/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.admin.view;

import com.gwtplatform.mvp.client.UiHandlers;
import com.techstudio.erp.moneychanger.shared.proxy.CategoryProxy;

/**
 * @author Nilson
 */
public interface ItemUiHandlers extends UiHandlers {
  void onBack();

  void onNext();

  void setCode(String code);

  void setName(String name);

  void setFullName(String fullName);

  void setUomRate(String uomRate);

  void setCategory(CategoryProxy selectedValue);

  void edit(String code);

  void create();

  void update();

  void delete();

//  void setItemCode(String code);
//
//  void setItemName(String name);
//
//  void setItemFullName(String fullName);
//
//  void setItemCategory(CategoryProxy category);
//
//  void setItemCurrency(CurrencyProxy currency);
//
//  void setItemUom(UomProxy uomProxy);
//
//  void setItemUomRate(String uomRate);
//
//  void setItemImageUrl(String itemImageUrl);
//
//  void createItem();
//
//  void updateItem();
}
