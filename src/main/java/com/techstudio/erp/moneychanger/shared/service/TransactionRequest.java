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
import com.techstudio.erp.moneychanger.server.service.TransactionDao;
import com.techstudio.erp.moneychanger.shared.proxy.TransactionProxy;

import java.util.List;

/**
 * @author Nilson
 */
@Service(value = TransactionDao.class, locator = DaoServiceLocator.class)
public interface TransactionRequest extends RequestContext {
  Request<List<TransactionProxy>> fetchAll();

  Request<TransactionProxy> save(TransactionProxy proxy);

  Request<TransactionProxy> fetch(Long id);

  Request<Void> purge(TransactionProxy proxy);

  Request<List<TransactionProxy>> fetchByProperty(String prop, String value);

  Request<List<TransactionProxy>> fetchRange(Integer start, Integer length);

  Request<Integer> getCount();
}
