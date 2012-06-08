package com.techstudio.erp.moneychanger.shared.proxy;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.techstudio.erp.moneychanger.server.domain.TransactionSearchFilter;

import java.util.Date;

/**
 * @author Nilson
 */
@ProxyFor(value = TransactionSearchFilter.class)
public interface TransactionSearchFilterProxy extends ValueProxy {

  Date getDate();

  void setDate(Date date);

  String getCategoryCode();

  void setCategoryCode(String categoryCode);

  String getItemCode();

  void setItemCode(String itemCode);
}
