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
import com.techstudio.erp.moneychanger.server.service.LineItemDao;
import com.techstudio.erp.moneychanger.shared.proxy.LineItemProxy;

import java.util.List;

/**
 * @author Nilson
 */
@Service(value = LineItemDao.class, locator = DaoServiceLocator.class)
public interface LineItemRequest extends RequestContext {
  Request<List<LineItemProxy>> fetchAll();

  Request<LineItemProxy> save(LineItemProxy proxy);

  Request<LineItemProxy> fetch(Long id);

  Request<Void> purge(LineItemProxy proxy);

  Request<List<LineItemProxy>> fetchByProperty(String prop, String value);

  Request<List<LineItemProxy>> fetchRange(Integer start, Integer length);

  Request<Integer> getCount();
}
