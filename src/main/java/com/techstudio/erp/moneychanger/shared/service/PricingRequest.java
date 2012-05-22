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
import com.techstudio.erp.moneychanger.shared.proxy.PricingProxy;

import java.util.List;

/**
 * @author Nilson
 */
@Service(value = SpotRateDao.class, locator = DaoServiceLocator.class)
public interface PricingRequest extends RequestContext {
  Request<List<PricingProxy>> fetchAll();

  Request<PricingProxy> save(PricingProxy proxy);

  Request<PricingProxy> fetch(Long id);

  Request<Void> purge(PricingProxy proxy);

  Request<List<PricingProxy>> fetchByProperty(String prop, String value);

  Request<List<PricingProxy>> fetchRange(Integer start, Integer length);

  Request<Integer> getCount();
}
