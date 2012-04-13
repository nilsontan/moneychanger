/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.server.domain;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Unindexed;
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

  private TransactionType transactionType;

  @Unindexed
  Money unitPrice = Money.parse("SGD 1.00");

  @Unindexed
  BigDecimal quantity = BigDecimal.ONE;

  @Unindexed
  Money subTotal = Money.parse("SGD 1.00");

  private Key<Item> item;

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

  public Item getItem() {
    if (item == null) {
      return Item.EMPTY;
    }
    try {
      return new ItemDao().get(item);
    } catch (EntityNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public void setItem(Item item) {
    if (item.equals(Item.EMPTY)) {
      return;
    }
    this.item = new ItemDao().key(item);
  }

  public BigDecimal getUnitPrice() {
    return unitPrice.getAmount();
  }

  public void setUnitPrice(BigDecimal unitPrice) {
    this.unitPrice = Money.of(CurrencyUnit.of(java.util.Currency.getInstance("SGD")), unitPrice);
  }

  public BigDecimal getQuantity() {
    return quantity;
  }

  public void setQuantity(BigDecimal quantity) {
    this.quantity = quantity;
  }

  public BigDecimal getSubTotal() {
    return subTotal.getAmount();
  }

  public void setSubTotal(BigDecimal subTotal) {
    this.subTotal = Money.of(CurrencyUnit.of(java.util.Currency.getInstance("SGD")), subTotal);
  }

  public Integer getLine() {
    return line;
  }

  public void setLine(Integer line) {
    this.line = line;
  }
}
