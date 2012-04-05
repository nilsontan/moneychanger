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
import com.techstudio.erp.moneychanger.server.service.CountryDao;
import com.techstudio.erp.moneychanger.shared.proxy.CountryProxy;

import java.util.List;

/**
 * @author Nilson
 */
@Service(value = CountryDao.class, locator = DaoServiceLocator.class)
public interface CountryRequest extends RequestContext {
  Request<List<CountryProxy>> fetchAll();

  Request<CountryProxy> save(CountryProxy proxy);

  Request<CountryProxy> fetch(Long id);

  Request<Void> purge(CountryProxy proxy);

  Request<List<CountryProxy>> fetchByProperty(String prop, String value);

  Request<List<CountryProxy>> fetchRange(Integer start, Integer length);

  Request<Integer> getCount();
}
