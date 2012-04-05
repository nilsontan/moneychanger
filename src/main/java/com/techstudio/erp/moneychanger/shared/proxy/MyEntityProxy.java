/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.shared.proxy;

import com.google.web.bindery.requestfactory.shared.EntityProxy;

import java.util.Date;

/**
 * @author Nilson
 */
public interface MyEntityProxy extends EntityProxy {
  Long getId();
  Integer getVersion();

  String getCode();
  void setCode(String code);

  String getName();
  void setName(String name);

  Date getCreationDate();
  void setCreationDate(Date date);
}
