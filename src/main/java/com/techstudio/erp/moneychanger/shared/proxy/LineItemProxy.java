/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.shared.proxy;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.techstudio.erp.moneychanger.shared.domain.LineItem;
import com.techstudio.erp.moneychanger.shared.domain.TransactionType;

import java.math.BigDecimal;

/**
 * @author Nilson
 */
@ProxyFor(value = LineItem.class)
public interface LineItemProxy extends ValueProxy {
  TransactionType getTransactionType();

  void setTransactionType(TransactionType transactionType);

  String getItemBuy();

  void setItemBuy(String itemBuy);

  String getCatBuy();

  void setCatBuy(String catBuy);

  String getItemSell();

  void setItemSell(String itemSell);

  String getCatSell();

  void setCatSell(String catSell);

  BigDecimal getBuyUnitPrice();

  void setBuyUnitPrice(BigDecimal unitPrice);

  BigDecimal getSellUnitPrice();

  void setSellUnitPrice(BigDecimal unitPrice);

  BigDecimal getBuyQuantity();

  void setBuyQuantity(BigDecimal quantity);

  BigDecimal getSellQuantity();

  void setSellQuantity(BigDecimal quantity);

  Integer getLine();

  void setLine(Integer line);
}
