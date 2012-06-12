/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.shared.service;

import com.google.web.bindery.requestfactory.shared.ExtraTypes;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;
import com.techstudio.erp.moneychanger.server.locator.DaoServiceLocator;
import com.techstudio.erp.moneychanger.server.service.ClientDao;
import com.techstudio.erp.moneychanger.shared.proxy.ClientProxy;
import com.techstudio.erp.moneychanger.shared.proxy.CompanyClientProxy;
import com.techstudio.erp.moneychanger.shared.proxy.IndividualClientProxy;

import java.util.List;

/**
 * @author Nilson
 */
@ExtraTypes({IndividualClientProxy.class, CompanyClientProxy.class})
@Service(value = ClientDao.class, locator = DaoServiceLocator.class)
public interface ClientRequest extends RequestContext {
  Request<List<ClientProxy>> fetchAll();

  Request<ClientProxy> save(ClientProxy proxy);

  Request<ClientProxy> fetch(Long id);

  Request<Void> purge(ClientProxy proxy);

  Request<List<ClientProxy>> fetchByProperty(String prop, String value);

  Request<List<ClientProxy>> fetchRange(Integer start, Integer length);

  Request<Integer> getCount();
}
