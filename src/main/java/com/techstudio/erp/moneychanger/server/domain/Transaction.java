/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.server.domain;

import com.googlecode.objectify.annotation.Entity;

import javax.persistence.Embedded;
import java.util.Date;
import java.util.List;

/**
 * Represents details about a transacted item in a transaction
 */
@Entity
public class Transaction extends DatastoreObject {

  public static final Transaction EMPTY = new Transaction();

  final Date creationDate = new Date();

  @Embedded
  List<LineItem> lineItems;

  public Transaction() {
  }

  public List<LineItem> getLineItems() {
    return lineItems;
  }

  public void setLineItems(List<LineItem> lineItems) {
    this.lineItems = lineItems;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date date) {
  }

}
