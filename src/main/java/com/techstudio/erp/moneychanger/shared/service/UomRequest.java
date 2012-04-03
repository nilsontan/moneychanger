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
import com.techstudio.erp.moneychanger.server.service.UomDao;
import com.techstudio.erp.moneychanger.shared.proxy.UomProxy;

import java.util.List;

/**
 * @author Nilson
 */
@Service(value = UomDao.class, locator = DaoServiceLocator.class)
public interface UomRequest extends RequestContext {
  Request<List<UomProxy>> fetchAll();

  Request<UomProxy> save(UomProxy proxy);

  Request<UomProxy> fetch(Long id);

  Request<Void> purge(UomProxy proxy);

  Request<List<UomProxy>> fetchByProperty(String prop, String value);

  Request<List<UomProxy>> fetchRange(Integer start, Integer length);

  Request<Integer> getCount();
}
