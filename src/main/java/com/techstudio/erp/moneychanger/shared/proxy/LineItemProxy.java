/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.shared.proxy;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.techstudio.erp.moneychanger.server.domain.LineItem;
import com.techstudio.erp.moneychanger.server.locator.ObjectifyLocator;
import com.techstudio.erp.moneychanger.shared.domain.TransactionType;
import org.joda.money.Money;

import java.math.BigDecimal;

/**
 * @author Nilson
 */
@ProxyFor(value = LineItem.class, locator = ObjectifyLocator.class)
public interface LineItemProxy extends EntityProxy {
  TransactionType getTransactionType();

  void setTransactionType(TransactionType transactionType);

  ItemProxy getItemBuy();

  void setItemBuy(ItemProxy item);

  ItemProxy getItemSell();

  void setItemSell(ItemProxy item);

  BigDecimal getBuyUnitPrice();

  void setBuyUnitPrice(BigDecimal unitPrice);

  BigDecimal getSellUnitPrice();

  void setSellUnitPrice(BigDecimal unitPrice);

  BigDecimal getBuyQuantity();

  void setBuyQuantity(BigDecimal quantity);

  BigDecimal getSellQuantity();

  void setSellQuantity(BigDecimal quantity);

  BigDecimal getSubTotal();

  void setSubTotal(BigDecimal subTotal);

  Integer getLine();

  void setLine(Integer line);
}
