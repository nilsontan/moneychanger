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
import com.techstudio.erp.moneychanger.server.service.CategoryDao;
import com.techstudio.erp.moneychanger.server.service.UomDao;

@Cached
@Entity
public class Category extends MyDatastoreObject {

  public static final Category EMPTY = new Category();

  private Key<Category> parent;

  private Key<Uom> uom;

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

  public Uom getUom() {
    if (uom == null) {
      return Uom.EMPTY;
    }
    try {
      return new UomDao().get(uom);
    } catch (EntityNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public void setUom(Uom uom) {
    if (uom.equals(Uom.EMPTY)) {
      return;
    }
    this.uom = new UomDao().key(uom);
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj instanceof Category) {
      final Category other = (Category) obj;
      return Objects.equal(getCode(), other.getCode())
          && Objects.equal(getUom(), other.getUom())
          && Objects.equal(parent, other.parent);
    } else {
      return false;
    }
  }
}
