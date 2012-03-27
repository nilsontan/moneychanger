/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger;

import com.google.web.bindery.requestfactory.server.ExceptionHandler;
import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Nilson
 */
public class MoneychangerRequestFactoryServlet extends RequestFactoryServlet {

  private static final long serialVersionUID = 3337352947331529917L;

  static class LoquaciousExceptionHandler implements ExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(LoquaciousExceptionHandler.class);

    @Override
    public ServerFailure createServerFailure(Throwable throwable) {
      LOG.error("Server error", throwable);
      return new ServerFailure(throwable.getMessage(), throwable.getClass().getName(), null, true);
    }
  }

  public MoneychangerRequestFactoryServlet() {
    super(new LoquaciousExceptionHandler());
  }

}
