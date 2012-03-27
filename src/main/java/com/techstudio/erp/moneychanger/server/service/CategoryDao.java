/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.server.service;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.googlecode.objectify.Key;
import com.techstudio.erp.moneychanger.server.domain.Category;

import java.util.List;

/**
 * @author Nilson
 */
public class CategoryDao extends ObjectifyDao<Category> {
  public List<Category> listAllCategories() {
    return listAll();
  }

  public Category save(Category category) {
    put(category);
    return category;
  }

  public Category addParentCategory(Category category, Category parentCategory) {
    category.setParent(parentCategory);
    Key<Category> key = this.put(category);
    try {
      return this.get(key);
    } catch (EntityNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public Category fetch(Long id) throws EntityNotFoundException {
    return get(id);
  }

  public List<Category> fetchRange(Integer start, Integer length) {
    return listAll(start, length);
  }

  public Integer getCount() {
    return countAll();
  }
}
