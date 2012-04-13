/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.shared.proxy;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.techstudio.erp.moneychanger.server.domain.LineItem;
import com.techstudio.erp.moneychanger.server.domain.Transaction;
import com.techstudio.erp.moneychanger.server.locator.ObjectifyLocator;

import java.util.List;

/**
 * @author Nilson
 */
@ProxyFor(value = Transaction.class, locator = ObjectifyLocator.class)
public interface TransactionProxy extends EntityProxy {
  List<LineItemProxy> getLineItems();

  void setLineItems(List<LineItemProxy> lineItems);
}
