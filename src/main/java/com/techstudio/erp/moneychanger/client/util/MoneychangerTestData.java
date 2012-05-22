package com.techstudio.erp.moneychanger.client.util;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.HandlerRegistration;

import java.util.List;

/**
 * Resets data in the database to a default state based on resources provided. Please note that all existing data
 * in the db will be cleared
 *
 * @author Nilson
 */
public final class MoneychangerTestData implements TestData {

  private List<OnSuccessfulReset> successfulResetList = Lists.newArrayList();

  @Override
  public HandlerRegistration addOnSuccessfulResetHandler(final OnSuccessfulReset handler) {
    assert handler != null;
    successfulResetList.add(handler);
    return new HandlerRegistration() {
      @Override
      public void removeHandler() {
        successfulResetList.remove(handler);
      }
    };
  }

  protected void onSuccessfulReset() {
    for (OnSuccessfulReset handler : successfulResetList) {
      handler.onSuccess(this);
    }
  }

  public MoneychangerTestData() {
  }

  @Override
  public void resetAll() {
    myService.resetData(callback);
  }

  DataResetServiceAsync myService = (DataResetServiceAsync) GWT.create(DataResetService.class);

  AsyncCallback<String> callback = new AsyncCallback<String>() {
    @Override
    public void onFailure(Throwable caught) {
      Log.debug("RESET FAIL");
    }

    @Override
    public void onSuccess(String result) {
      onSuccessfulReset();
      Log.debug("RESET SUCCESSFUL");
    }
  };
}
