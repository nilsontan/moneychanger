/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.server.domain;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cached;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Unindexed;
import com.techstudio.erp.moneychanger.server.service.CategoryDao;
import com.techstudio.erp.moneychanger.server.service.CurrencyDao;

import java.math.BigDecimal;

/**
 * A 'product' or item
 */
@Cached
@Entity
public class Item extends MyExDatastoreObject {

  public static final Item EMPTY = new Item();

  private Key<Category> category;

  private Key<Currency> currency;

  private BigDecimal uomRate;

  @Unindexed
  private String imageUrl = "";

  public Item() {
  }

  public Category getCategory() {
    if (category == null) {
      return Category.EMPTY;
    }
    try {
      return new CategoryDao().get(category);
    } catch (EntityNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public void setCategory(Category category) {
    if (category.equals(Category.EMPTY)) {
      return;
    }
    this.category = new CategoryDao().key(category);
  }

  public Currency getCurrency() {
    if (currency == null) {
      return Currency.EMPTY;
    }
    try {
      return new CurrencyDao().get(currency);
    } catch (EntityNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public void setCurrency(Currency currency) {
    if (currency.equals(Currency.EMPTY)) {
      return;
    }
    this.currency = new CurrencyDao().key(currency);
  }

  public BigDecimal getUomRate() {
    return uomRate;
  }

  public void setUomRate(BigDecimal uomRate) {
    this.uomRate = uomRate;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

}
