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
import com.techstudio.erp.moneychanger.server.service.ItemDao;
import com.techstudio.erp.moneychanger.shared.proxy.ItemProxy;

import java.util.List;

/**
 * @author Nilson
 */
@Service(value = ItemDao.class, locator = DaoServiceLocator.class)
public interface ItemRequest extends RequestContext {
  Request<List<ItemProxy>> fetchAll();

  Request<ItemProxy> save(ItemProxy editable);

  Request<ItemProxy> fetch(Long id);

  Request<List<ItemProxy>> fetchRange(Integer start, Integer length);

  Request<Integer> getCount();
}
