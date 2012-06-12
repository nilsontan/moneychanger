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
import com.techstudio.erp.moneychanger.server.service.IndividualClientDao;
import com.techstudio.erp.moneychanger.shared.proxy.IndividualClientProxy;

import java.util.List;

/**
 * @author Nilson
 */
@Service(value = IndividualClientDao.class, locator = DaoServiceLocator.class)
public interface IndividualClientRequest extends RequestContext {
  Request<List<IndividualClientProxy>> fetchAll();

  Request<IndividualClientProxy> save(IndividualClientProxy proxy);

  Request<IndividualClientProxy> fetch(Long id);

  Request<Void> purge(IndividualClientProxy proxy);

  Request<List<IndividualClientProxy>> fetchByProperty(String prop, String value);

  Request<List<IndividualClientProxy>> fetchRange(Integer start, Integer length);

  Request<Integer> getCount();
}
