/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.server.service;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.techstudio.erp.moneychanger.server.domain.Currency;

import java.util.List;

/**
 * @author Nilson
 */
public class CurrencyDao extends ObjectifyDao<Currency> {
  public List<Currency> fetchAll() {
    return listAll();
  }

  public Currency save(Currency currency) {
    put(currency);
    return currency;
  }

  public Currency fetch(Long id) throws EntityNotFoundException {
    return get(id);
  }

  public void purge(Currency currency) {
    delete(currency);
  }

  public List<Currency> fetchByProperty(String prop, String value) {
    return listByProperty(prop, value);
  }

  public List<Currency> fetchRange(Integer start, Integer length) {
    return listAll(start, length);
  }

  public Integer getCount() {
    return countAll();
  }
}
