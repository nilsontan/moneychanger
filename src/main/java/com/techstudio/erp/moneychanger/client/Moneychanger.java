/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Command;
import com.gwtplatform.mvp.client.DelayedBindRegistry;
import com.techstudio.erp.moneychanger.client.gin.MoneychangerGinjector;

/**
 * @author Nilson
 */
public class Moneychanger implements EntryPoint {
  public final MoneychangerGinjector ginjector = GWT.create(MoneychangerGinjector.class);

  @Override
  public final void onModuleLoad() {
    // Wire the request factory nd the event bus
//    ginjector.getRequestFactory().initialize(ginjector.getEventBus());

//    GWT.setUncaughtExceptionHandler(new CustomUncaughtExceptionHandler());
    Log.setUncaughtExceptionHandler();

    Scheduler.get().scheduleDeferred(new Command() {
      @Override
      public void execute() {
        onModuleLoad2();
      }
    });
  }

  private void onModuleLoad2() {

    long startTimeMillis = 0L;

    if (Log.isDebugEnabled()) {
      startTimeMillis = System.currentTimeMillis();
    }

    // This is required for Gwt-Platform proxy's generator.
    DelayedBindRegistry.bind(ginjector);

    ginjector.getResources().admin().ensureInjected();
    ginjector.getResources().pos().ensureInjected();
    ginjector.getResources().cur().ensureInjected();

    ginjector.getPlaceManager().revealCurrentPlace();

    if (Log.isDebugEnabled()) {
      long endTimeMillis = System.currentTimeMillis();
      float durationSeconds = (endTimeMillis - startTimeMillis) / 1000F;
      Log.debug("Duration: " + durationSeconds + " seconds");
    }
  }
}
