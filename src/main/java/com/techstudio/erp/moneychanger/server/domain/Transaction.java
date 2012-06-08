/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.server.domain;

import com.google.common.collect.Lists;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.NotSaved;
import com.techstudio.erp.moneychanger.shared.domain.LineItem;

import javax.persistence.Embedded;
import java.util.Date;
import java.util.List;

/**
 * Represents details about a transacted item in a transaction
 */
@Entity
public class Transaction extends MyDatastoreObject {

  public static final Transaction EMPTY = new Transaction();

  private Date transactionDate = new Date();

  @Embedded
  List<LineItem> lineItems;

  @NotSaved
  @Embedded
  List<TransactionSearchFilter> searchFilter;

  public Transaction() {
    lineItems = Lists.newArrayList();
    searchFilter = Lists.newArrayList();
  }

  public Date getTransactionDate() {
    return transactionDate;
  }

  public void setTransactionDate(Date transactionDate) {
    this.transactionDate = transactionDate;
  }

  public List<LineItem> getLineItems() {
    return lineItems;
  }

  public void setLineItems(List<LineItem> lineItems) {
    this.lineItems = lineItems;
  }

  public List<TransactionSearchFilter> getSearchFilter() {
    return searchFilter;
  }

  public void setSearchFilter(List<TransactionSearchFilter> searchFilter) {
    this.searchFilter = searchFilter;
  }
}
