/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.server.domain;

/**
 * An extension of the basic {@link MyDatastoreObject}
 * which includes some extra common fields
 *
 * <ol>
 *   <li>Full Name</li>
 * </ol>
 * @author Nilson
 */
public class MyExDatastoreObject extends MyDatastoreObject {

  private String fullName = "";

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }
}
