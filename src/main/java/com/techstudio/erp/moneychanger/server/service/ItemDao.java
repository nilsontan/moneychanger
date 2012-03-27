/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.server.service;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.techstudio.erp.moneychanger.server.domain.Item;

import java.util.List;

/**
 * @author Nilson
 */
public class ItemDao extends ObjectifyDao<Item> {
  public List<Item> fetchAll() {
    return listAll();
  }

  public Item save(Item item) {
    put(item);
    return item;
  }

  public Item fetch(Long id) throws EntityNotFoundException {
    return get(id);
  }

  public List<Item> fetchRange(Integer start, Integer length) {
    return listAll(start, length);
  }

  public Integer getCount() {
    return countAll();
  }
}
