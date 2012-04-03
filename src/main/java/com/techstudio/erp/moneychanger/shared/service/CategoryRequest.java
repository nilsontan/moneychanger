/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.shared.service;

import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;
import com.techstudio.erp.moneychanger.server.locator.DaoServiceLocator;
import com.techstudio.erp.moneychanger.server.service.CategoryDao;
import com.techstudio.erp.moneychanger.shared.proxy.CategoryProxy;

import java.util.List;

/**
 * @author Nilson
 */
@Service(value = CategoryDao.class, locator = DaoServiceLocator.class)
public interface CategoryRequest extends RequestContext {
  Request<List<CategoryProxy>> fetchAll();

  Request<CategoryProxy> save(CategoryProxy proxy);

  Request<CategoryProxy> fetch(Long id);

  Request<Void> purge(CategoryProxy proxy);

  Request<List<CategoryProxy>> fetchByProperty(String prop, String value);

  Request<List<CategoryProxy>> fetchRange(Integer start, Integer length);

  Request<Integer> getCount();
}
