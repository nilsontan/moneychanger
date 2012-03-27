/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.shared.service;

import com.google.web.bindery.requestfactory.shared.RequestFactory;

/**
 * Service stub for static methods
 *
 * @author Nilson
 */
public interface MoneychangerRequestFactory extends RequestFactory {
  ItemRequest itemService();

  CategoryRequest categoryService();
}
