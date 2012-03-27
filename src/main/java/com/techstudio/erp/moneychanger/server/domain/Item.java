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

/**
 * A 'product' or item
 */
@Entity
public class Item extends DatedDatastoreObject {

  private Key<Category> category;

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

}
