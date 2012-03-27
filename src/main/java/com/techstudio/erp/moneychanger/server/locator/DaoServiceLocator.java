/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.server.locator;

import com.google.web.bindery.requestfactory.shared.ServiceLocator;

/**
 * Generic locator service that can be referenced in the @Service annotation
 * for any RequestFactory service stub
 */
public class DaoServiceLocator implements ServiceLocator {

  @Override
  public Object getInstance(Class<?> clazz) {
    try {
      return clazz.newInstance();
    } catch (InstantiationException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

}