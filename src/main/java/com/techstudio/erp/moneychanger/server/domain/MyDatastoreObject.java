/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.server.domain;

import java.util.Date;

/**
 * Datastore Object with some common parameters
 * <ol>
 *   <li>Code</li>
 *   <li>Name</li>
 *   <li>Date Created</li>
 * </ol>
 *
 * @author Nilson
 */
public abstract class MyDatastoreObject extends DatastoreObject {
  private String code = "";
  private String name = "";
  private final Date creationDate = new Date();

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code.trim().toUpperCase();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date date) {
  }
}