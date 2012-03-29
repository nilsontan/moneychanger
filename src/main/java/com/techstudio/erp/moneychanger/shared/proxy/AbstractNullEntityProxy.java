/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.shared.proxy;

import com.google.web.bindery.requestfactory.shared.EntityProxyId;

import java.util.Date;

/**
 * @author Nilson
 */
public abstract class AbstractNullEntityProxy implements BaseEntityProxy {
  @Override
  public Long getId() {
    return -1L;
  }

  @Override
  public Integer getVersion() {
    return null;
  }

  @Override
  public String getCode() {
    return "";
  }

  @Override
  public void setCode(String code) {
  }

  @Override
  public String getName() {
    return "";
  }

  @Override
  public void setName(String name) {
  }

  @Override
  public Date getCreationDate() {
    return null;
  }

  @Override
  public void setCreationDate(Date date) {
  }

  @Override
  public EntityProxyId<?> stableId() {
    return null;
  }
}
