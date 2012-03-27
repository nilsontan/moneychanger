/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.gwtplatform.mvp.client.DelayedBindRegistry;
import com.techstudio.erp.moneychanger.client.gin.MoneychangerGinjector;
import com.techstudio.erp.moneychanger.shared.CustomUncaughtExceptionHandler;

/**
 * @author Nilson
 */
public class Moneychanger implements EntryPoint {
  public final MoneychangerGinjector ginjector = GWT.create(MoneychangerGinjector.class);

  @Override
  public final void onModuleLoad() {
    // Wire the request factory nd the event bus
//    ginjector.getRequestFactory().initialize(ginjector.getEventBus());

    GWT.setUncaughtExceptionHandler(new CustomUncaughtExceptionHandler());

    // This is required for Gwt-Platform proxy's generator.
    DelayedBindRegistry.bind(ginjector);

    ginjector.getResources().admin().ensureInjected();
    ginjector.getResources().pos().ensureInjected();
    ginjector.getPlaceManager().revealCurrentPlace();
  }
}
