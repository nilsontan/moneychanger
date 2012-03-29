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
import com.techstudio.erp.moneychanger.server.service.CurrencyDao;
import com.techstudio.erp.moneychanger.shared.proxy.CurrencyProxy;

import java.util.List;

/**
 * @author Nilson
 */
@Service(value = CurrencyDao.class, locator = DaoServiceLocator.class)
public interface CurrencyRequest extends RequestContext {
  Request<List<CurrencyProxy>> fetchAll();

  Request<CurrencyProxy> save(CurrencyProxy editable);

  Request<CurrencyProxy> fetch(Long id);

  Request<Void> purge(CurrencyProxy currencyProxy);

  Request<List<CurrencyProxy>> fetchByProperty(String prop, String value);

  Request<List<CurrencyProxy>> fetchRange(Integer start, Integer length);

  Request<Integer> getCount();
}
