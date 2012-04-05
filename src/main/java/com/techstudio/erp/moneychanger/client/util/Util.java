/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.util;

import com.google.common.base.Function;
import com.techstudio.erp.moneychanger.shared.proxy.MyEntityProxy;

import javax.annotation.Nullable;

/**
 * Contains utility methods for various parts of the application
 *
 * @author Nilson
 */
public final class Util {

  public static Function<MyEntityProxy, String> entityProxyNameFunction = new Function<MyEntityProxy, String>() {
    @Override
    public String apply(@Nullable MyEntityProxy myEntityProxy) {
      return myEntityProxy.getName();
    }
  };
}
