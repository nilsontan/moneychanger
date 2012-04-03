/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.server.locator;

import com.google.web.bindery.requestfactory.shared.Locator;
import com.techstudio.erp.moneychanger.server.domain.DatastoreObject;
import com.techstudio.erp.moneychanger.server.service.MyDAOBase;

/**
 * Generic @Locator for objects that extend DatastoreObject
 */
public class ObjectifyLocator extends Locator<DatastoreObject, Long> {
  @Override
  public DatastoreObject create(Class<? extends DatastoreObject> clazz) {
    try {
      return clazz.newInstance();
    } catch (InstantiationException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public DatastoreObject find(Class<? extends DatastoreObject> clazz, Long id) {
    MyDAOBase daoBase = new MyDAOBase();
    return daoBase.ofy().find(clazz, id);
  }

  @Override
  public Class<DatastoreObject> getDomainType() {
    // Never called
    return null;
  }

  @Override
  public Long getId(DatastoreObject domainObject) {
    return domainObject.getId();
  }

  @Override
  public Class<Long> getIdType() {
    return Long.class;
  }

  @Override
  public Object getVersion(DatastoreObject domainObject) {
    return domainObject.getVersion();
  }
}