package com.techstudio.erp.moneychanger.client.ui.dataprovider;

import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.HandlerRegistration;

import java.util.List;

/**
 * @author Nilson
 */
public interface SearchFilter<T> {

  public interface OnSearch extends EventHandler {
    void onSuccess(SearchFilter searchFilter);
  }

  HandlerRegistration addOnSearchHandler(final OnSearch handler);

  void updateSearchList(List<T> proxies);
}
