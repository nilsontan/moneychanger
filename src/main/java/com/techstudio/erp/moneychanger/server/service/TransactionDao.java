/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.server.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Query;
import com.techstudio.erp.moneychanger.server.domain.Transaction;
import com.techstudio.erp.moneychanger.server.domain.TransactionSearchFilter;

import java.util.*;

/**
 * @author Nilson
 */
public class TransactionDao extends MyObjectifyDao<Transaction> {

  @Override
  public List<Transaction> searchByFilter(Transaction transaction) {
    Query<Transaction> q = ofy().query(clazz);
    Query<Transaction> q2 = ofy().query(clazz);
    TransactionSearchFilter search = transaction.getSearchFilter().get(0);
    Date date = search.getDate();
    if (date != null) {
      Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Singapore"));
      calendar.setTime(date);
      calendar.add(Calendar.HOUR, 24);
      q.filter("transactionDate >", date);
      q.filter("transactionDate <", calendar.getTime());
      q2.filter("transactionDate >", date);
      q2.filter("transactionDate <", calendar.getTime());
    }

    String itemCode = search.getItemCode();
    if (itemCode != null && !itemCode.isEmpty()) {
      q.filter("lineItems.itemBuy", itemCode);
      q2.filter("lineItems.itemSell", itemCode);

      Set<Key<Transaction>> set1 = Sets.newHashSet(q.order("-transactionDate").listKeys());
      Set<Key<Transaction>> set2 = Sets.newHashSet(q2.order("-transactionDate").listKeys());

      return Lists.newArrayList(ofy().get(Sets.union(set1, set2)).values());

    } else {
      String categoryCode = search.getCategoryCode();
      if (categoryCode != null && !categoryCode.isEmpty()) {
        q.filter("lineItems.catBuy", categoryCode);
        q2.filter("lineItems.catSell", categoryCode);

        Set<Key<Transaction>> set1 = Sets.newHashSet(q.order("-transactionDate").listKeys());
        Set<Key<Transaction>> set2 = Sets.newHashSet(q2.order("-transactionDate").listKeys());

        return Lists.newArrayList(ofy().get(Sets.union(set1, set2)).values());

      } else {
        return q.order("-transactionDate").list();
      }
    }

  }

}
