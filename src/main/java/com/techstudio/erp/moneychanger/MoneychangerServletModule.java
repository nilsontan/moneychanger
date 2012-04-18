/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger;

import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;
import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;
import com.gwtplatform.dispatch.server.guice.DispatchServiceImpl;
import com.gwtplatform.dispatch.shared.ActionImpl;
import gwtupload.server.UploadServlet;
import gwtupload.server.gae.FilesApiUploadAction;

/**
 * Guice module used to bind guice-injected servlets and filters with their URL.
 *
 * @author Nilson
 */
public class MoneychangerServletModule extends ServletModule {
  @Override
  public void configureServlets() {

//    // AppStats filter and servlet
//    bind(AppstatsFilter.class).in(Singleton.class);
//    bind(AppstatsServlet.class).in(Singleton.class);
//
//    Map<String, String> appstatsFilterParams = new HashMap<String, String>();
//    appstatsFilterParams.put("logMessage", "Appstats available: /appstats/details?time={ID}");
//    filter("/*").through(AppstatsFilter.class, appstatsFilterParams);
//    serve("/appstats/*").with(AppstatsServlet.class);

    // RequestFactory servlet
    bind(RequestFactoryServlet.class).to(MoneychangerRequestFactoryServlet.class).in(Singleton.class);
    bind(MoneychangerRequestFactoryServlet.class).in(Singleton.class);
    serve("/gwtRequest").with(RequestFactoryServlet.class);

    // GWT-platform commands servlet
    serve("/" + ActionImpl.DEFAULT_SERVICE_NAME + "*").with(DispatchServiceImpl.class);

    // gwtUpload
    bind(UploadServlet.class).to(FilesApiUploadAction.class).in(Singleton.class);
    bind(FilesApiUploadAction.class).in(Singleton.class);
    serve("*.gupld").with(FilesApiUploadAction.class);
    serve("/upload").with(FilesApiUploadAction.class);
  }
}
