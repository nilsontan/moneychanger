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
import com.techstudio.erp.moneychanger.server.service.CategoryDao;
import com.techstudio.erp.moneychanger.server.service.CurrencyDao;

/**
 * A 'product' or item
 */
@Entity
public class Item extends MyDatastoreObject {

  public static final Item EMPTY = new Item();

  private Key<Category> category;

  private Key<Currency> currency;

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

}
