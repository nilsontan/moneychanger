/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.server.domain;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.common.base.Objects;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cached;
import com.googlecode.objectify.annotation.Entity;
import com.techstudio.erp.moneychanger.server.service.CurrencyDao;

/**
 * @author Nilson
 */
@Cached
@Entity
public class Country extends MyExDatastoreObject {

  public static final Country EMPTY = new Country();

  private Key<Currency> currency;

  public Country() {
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

  @Override
  public boolean equals(final Object obj) {
    if (obj instanceof Country) {
      final Country other = (Country) obj;
      return Objects.equal(getCode(), other.getCode());
    } else {
      return false;
    }
  }

}
