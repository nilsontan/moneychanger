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
import com.techstudio.erp.moneychanger.server.service.SpotRateDao;
import com.techstudio.erp.moneychanger.shared.proxy.SpotRateProxy;

import java.util.List;

/**
 * @author Nilson
 */
@Service(value = SpotRateDao.class, locator = DaoServiceLocator.class)
public interface SpotRateRequest extends RequestContext {
  Request<List<SpotRateProxy>> fetchAll();

  Request<SpotRateProxy> save(SpotRateProxy proxy);

  Request<SpotRateProxy> fetch(Long id);

  Request<Void> purge(SpotRateProxy proxy);

  Request<List<SpotRateProxy>> fetchByProperty(String prop, String value);

  Request<List<SpotRateProxy>> fetchRange(Integer start, Integer length);

  Request<Integer> getCount();
}
