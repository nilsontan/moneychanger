/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.server.service;

import com.techstudio.erp.moneychanger.server.domain.Category;
import com.techstudio.erp.moneychanger.server.domain.Item;

import java.util.List;

/**
 * @author Nilson
 */
public class ItemDao extends MyObjectifyDao<Item> {
  public List<Item> fetchByCategory(String prop, Category value) {
    return listByProperty(prop, value);
  }
}
