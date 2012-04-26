package com.techstudio.erp.moneychanger.client.util;

import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * @author Nilson
 */
public interface TestData {

  public interface OnSuccessfulReset extends EventHandler {
    void onSuccess(TestData testData);
  }

  HandlerRegistration addOnSuccessfulResetHandler(final OnSuccessfulReset handler);

  void resetAll();
}
