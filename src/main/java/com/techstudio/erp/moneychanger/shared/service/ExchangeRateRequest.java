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
import com.techstudio.erp.moneychanger.server.service.ExchangeRateDao;
import com.techstudio.erp.moneychanger.shared.proxy.ExchangeRateProxy;

import java.util.List;

/**
 * @author Nilson
 */
@Service(value = ExchangeRateDao.class, locator = DaoServiceLocator.class)
public interface ExchangeRateRequest extends RequestContext {
  Request<List<ExchangeRateProxy>> fetchAll();

  Request<ExchangeRateProxy> save(ExchangeRateProxy proxy);

  Request<ExchangeRateProxy> fetch(Long id);

  Request<Void> purge(ExchangeRateProxy proxy);

  Request<List<ExchangeRateProxy>> fetchByProperty(String prop, String value);

  Request<List<ExchangeRateProxy>> fetchRange(Integer start, Integer length);

  Request<Integer> getCount();
}
