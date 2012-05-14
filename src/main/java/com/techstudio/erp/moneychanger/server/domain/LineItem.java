/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.server.domain;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.inject.Inject;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Unindexed;
import com.techstudio.erp.moneychanger.client.gin.DefaultCurrency;
import com.techstudio.erp.moneychanger.server.service.ItemDao;
import com.techstudio.erp.moneychanger.shared.domain.TransactionType;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.math.BigDecimal;

/**
 * Represents details about a transacted item in a transaction
 */
@Entity
public class LineItem extends DatastoreObject {

  public static final LineItem EMPTY = new LineItem();

  @Inject
  @DefaultCurrency
  String currency;

  CurrencyUnit currencyUnit = CurrencyUnit.of(java.util.Currency.getInstance(currency));

  TransactionType transactionType;

  @Unindexed
  Money buyUnitPrice = Money.parse(currency + " 1.00");

  @Unindexed
  Money sellUnitPrice = Money.parse(currency + " 1.00");

  @Unindexed
  BigDecimal buyQuantity = BigDecimal.ONE;

  @Unindexed
  BigDecimal sellQuantity = BigDecimal.ONE;

  @Unindexed
  Money subTotal = Money.parse("1.00");

  private Key<Item> itemBuy;
  
  private Key<Item> itemSell;

  Money money;

  @Unindexed
  Integer line = 1;

  public LineItem() {
  }

  public Money getMoney() {
    return money;
  }

  public void setMoney(Money money) {
    this.money = money;
  }

  public TransactionType getTransactionType() {
    return transactionType;
  }

  public void setTransactionType(TransactionType transactionType) {
    this.transactionType = transactionType;
  }

  public Item getItemBuy() {
    if (itemBuy == null) {
      return Item.EMPTY;
    }
    try {
      return new ItemDao().get(itemBuy);
    } catch (EntityNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public void setItemBuy(Item itemBuy) {
    if (itemBuy.equals(Item.EMPTY)) {
      return;
    }
    this.itemBuy = new ItemDao().key(itemBuy);
  }
  
  public Item getItemSell() {
    if (itemSell == null) {
      return Item.EMPTY;
    }
    try {
      return new ItemDao().get(itemSell);
    } catch (EntityNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public void setItemSell(Item itemSell) {
    if (itemSell.equals(Item.EMPTY)) {
      return;
    }
    this.itemSell = new ItemDao().key(itemSell);
  }

  public BigDecimal getBuyUnitPrice() {
    return buyUnitPrice.getAmount();
  }

  public void setBuyUnitPrice(BigDecimal unitPrice) {
    this.buyUnitPrice = Money.of(currencyUnit, unitPrice);
  }

  public BigDecimal getSellUnitPrice() {
    return sellUnitPrice.getAmount();
  }

  public void setSellUnitPrice(BigDecimal unitPrice) {
    this.buyUnitPrice = Money.of(currencyUnit, unitPrice);
  }

  public BigDecimal getBuyQuantity() {
    return buyQuantity;
  }

  public void setBuyQuantity(BigDecimal buyQuantity) {
    this.buyQuantity = buyQuantity;
  }

  public BigDecimal getSellQuantity() {
    return sellQuantity;
  }

  public void setSellQuantity(BigDecimal sellQuantity) {
    this.sellQuantity = sellQuantity;
  }

  public BigDecimal getSubTotal() {
    return subTotal.getAmount();
  }

  public void setSubTotal(BigDecimal subTotal) {
    this.subTotal = Money.of(currencyUnit, subTotal);
  }

  public Integer getLine() {
    return line;
  }

  public void setLine(Integer line) {
    this.line = line;
  }
}
