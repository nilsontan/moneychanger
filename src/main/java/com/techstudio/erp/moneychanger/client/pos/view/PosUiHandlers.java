/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.pos.view;

import com.gwtplatform.mvp.client.UiHandlers;
import com.techstudio.erp.moneychanger.shared.proxy.LineItemProxy;

/**
 * @author Nilson
 */
public interface PosUiHandlers extends UiHandlers {
  void switchView();

  void addToTransaction();

  void deleteTransaction();

  void saveTransaction();

  void skipStep();

  void itemSelected(String itemCode);

  void modifyItem(LineItemProxy lineItem);

  void confirmItemRate(String itemRate);

  void confirmItemQuantity(String itemQuantity);

  void confirmChanges();
}
