/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.shared.proxy;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.techstudio.erp.moneychanger.server.domain.Transaction;
import com.techstudio.erp.moneychanger.server.locator.ObjectifyLocator;

import java.util.Date;
import java.util.List;

/**
 * @author Nilson
 */
@ProxyFor(value = Transaction.class, locator = ObjectifyLocator.class)
public interface TransactionProxy extends MyEntityProxy {
  Date getTransactionDate();

  void setTransactionDate(Date date);

  List<LineItemProxy> getLineItems();

  void setLineItems(List<LineItemProxy> lineItems);

  List<TransactionSearchFilterProxy> getSearchFilter();

  void setSearchFilter(List<TransactionSearchFilterProxy> searchFilter);
}
