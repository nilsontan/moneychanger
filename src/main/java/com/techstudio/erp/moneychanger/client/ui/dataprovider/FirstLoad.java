package com.techstudio.erp.moneychanger.client.ui.dataprovider;

import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * @author Nilson
 */
public interface FirstLoad {

  public interface OnFirstLoad extends EventHandler {
    void onSuccess(FirstLoad firstLoad);
  }

  HandlerRegistration addOnFirstLoadHandler(final OnFirstLoad handler);

  void firstLoad();

  void load();
}
