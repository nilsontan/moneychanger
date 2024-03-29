/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.view;

import com.gwtplatform.mvp.client.UiHandlers;
import com.techstudio.erp.moneychanger.shared.proxy.CategoryProxy;

/**
 * @author Nilson
 */
public interface PosUiHandlers extends UiHandlers {
  void onBack();

  void selectCategories();

  void addToTransaction();

  void deleteTransaction();

  void saveTransaction();

  int itemSelected(String itemCode);

  void changeItemBuyQuantity(String itemQuantity);

  void changeItemSellQuantity(String itemQuantity);

  void changeIntRate(String itemRate);

  void changeInvIntRate(String itemRate);

  void changeBuyRate(String itemRate);

  void changeInvBuyRate(String itemRate);

  void changeSellRate(String itemRate);

  void changeInvSellRate(String itemRate);

  void confirmChanges();

  void removeLineItemIndex(int index);

  void modifyItem(int index);

  void switchCategory(CategoryProxy categoryProxy);
}
