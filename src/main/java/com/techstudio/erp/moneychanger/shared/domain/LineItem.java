/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.shared.domain;

import com.google.common.base.Objects;
import com.googlecode.objectify.annotation.Unindexed;

import java.math.BigDecimal;

/**
 * Represents details about a transacted item in a transaction
 */
public class LineItem {

  public static final LineItem EMPTY = new LineItem();

  TransactionType transactionType = TransactionType.BUY;

  @Unindexed
  BigDecimal buyUnitPrice = BigDecimal.ONE;

  @Unindexed
  BigDecimal sellUnitPrice = BigDecimal.ONE;

  @Unindexed
  BigDecimal buyQuantity = BigDecimal.ONE;

  @Unindexed
  BigDecimal sellQuantity = BigDecimal.ONE;

  String itemBuy = "";

  String catBuy = "";
  
  String itemSell = "";

  String catSell = "";

  @Unindexed
  Integer line = 1;

  public LineItem() {
  }

  public TransactionType getTransactionType() {
    return transactionType;
  }

  public void setTransactionType(TransactionType transactionType) {
    this.transactionType = transactionType;
  }

  public String getItemBuy() {
    return itemBuy;
  }

  public void setItemBuy(String itemBuy) {
    this.itemBuy = itemBuy;
  }

  public String getCatBuy() {
    return catBuy;
  }

  public void setCatBuy(String catBuy) {
    this.catBuy = catBuy;
  }

  public String getItemSell() {
    return  itemSell;
  }

  public void setItemSell(String itemSell) {
    this.itemSell = itemSell;
  }

  public String getCatSell() {
    return catSell;
  }

  public void setCatSell(String catSell) {
    this.catSell = catSell;
  }

  public BigDecimal getBuyUnitPrice() {
    return buyUnitPrice;
  }

  public void setBuyUnitPrice(BigDecimal unitPrice) {
    this.buyUnitPrice = unitPrice;
  }

  public BigDecimal getSellUnitPrice() {
    return sellUnitPrice;
  }

  public void setSellUnitPrice(BigDecimal unitPrice) {
    this.buyUnitPrice = unitPrice;
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

  public Integer getLine() {
    return line;
  }

  public void setLine(Integer line) {
    this.line = line;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("transaction type", transactionType)
        .add("buy price", buyUnitPrice)
        .add("sell price", sellUnitPrice)
        .add("buy amount", buyQuantity)
        .add("sell amount", sellQuantity)
        .add("buying", itemBuy)
        .add("selling", itemSell)
        .add("buy category", catBuy)
        .add("sell category", catSell)
        .toString();
  }
}
