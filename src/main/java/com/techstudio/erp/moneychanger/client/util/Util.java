/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.util;

import com.google.common.base.Function;
import com.techstudio.erp.moneychanger.shared.proxy.BaseEntityProxy;

import javax.annotation.Nullable;

/**
 * Contains utility methods for various parts of the application
 *
 * @author Nilson
 */
public final class Util {

  public static Function<BaseEntityProxy, String> entityProxyNameFunction = new Function<BaseEntityProxy, String>() {
    @Override
    public String apply(@Nullable BaseEntityProxy baseEntityProxy) {
      return baseEntityProxy.getName();
    }
  };
}
