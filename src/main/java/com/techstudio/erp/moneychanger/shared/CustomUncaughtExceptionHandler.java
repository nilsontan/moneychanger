/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.shared;

import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.Window;
import com.google.web.bindery.event.shared.UmbrellaException;

/**
 * @author Nilson
 */
public class CustomUncaughtExceptionHandler implements UncaughtExceptionHandler {
//  private static final Logger LOG = LoggerFactory.getLogger(CustomUncaughtExceptionHandler.class);

  @Override
  public void onUncaughtException(Throwable e) {
    // Get rid of UmbrellaException
    Throwable exceptionToDisplay = getExceptionToDisplay(e);
    // Replace with your favorite message dialog, e.g. GXT's MessageBox
    Window.alert(exceptionToDisplay.getMessage());
    e.printStackTrace();
    //TODO: Nilson - find a way to make logging work
//    LOG.error("Uncaught Exception", exceptionToDisplay);
  }

  private static Throwable getExceptionToDisplay(Throwable throwable) {
    Throwable result = throwable;
    if (throwable instanceof UmbrellaException && ((UmbrellaException) throwable).getCauses().size() == 1) {
      result = ((UmbrellaException) throwable).getCauses().iterator().next();
    }
    return result;
  }
}
