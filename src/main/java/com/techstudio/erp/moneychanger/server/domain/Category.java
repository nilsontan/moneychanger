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
import com.googlecode.objectify.annotation.Entity;
import com.techstudio.erp.moneychanger.server.service.CategoryDao;

@Entity
public class Category extends MyDatastoreObject {

  public static final Category EMPTY = new Category();

  private Key<Category> parent;

  public Category() {
  }

  public Category getParent() {
    if (parent == null) {
      return EMPTY;
    }
    try {
      return new CategoryDao().get(parent);
    } catch (EntityNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public void setParent(Category parent) {
    if (parent.equals(Category.EMPTY)) {
      return;
    }
    this.parent = new CategoryDao().key(parent);
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj instanceof Category) {
      final Category other = (Category) obj;
      return Objects.equal(getName(), other.getName())
          && Objects.equal(parent, other.parent);
    } else {
      return false;
    }
  }
}
