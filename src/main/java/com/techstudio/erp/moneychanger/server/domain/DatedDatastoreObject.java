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
 *
 * @author Nilson
 */
public abstract class DatedDatastoreObject extends DatastoreObject {
  private String name = "-";
  private final Date creationDate = new Date();

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