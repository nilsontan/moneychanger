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
import com.techstudio.erp.moneychanger.server.service.CompanyClientDao;
import com.techstudio.erp.moneychanger.shared.proxy.CompanyClientProxy;

import java.util.List;

/**
 * @author Nilson
 */
@Service(value = CompanyClientDao.class, locator = DaoServiceLocator.class)
public interface CompanyClientRequest extends RequestContext {
  Request<List<CompanyClientProxy>> fetchAll();

  Request<CompanyClientProxy> save(CompanyClientProxy proxy);

  Request<CompanyClientProxy> fetch(Long id);

  Request<Void> purge(CompanyClientProxy proxy);

  Request<List<CompanyClientProxy>> fetchByProperty(String prop, String value);

  Request<List<CompanyClientProxy>> fetchRange(Integer start, Integer length);

  Request<Integer> getCount();
}
