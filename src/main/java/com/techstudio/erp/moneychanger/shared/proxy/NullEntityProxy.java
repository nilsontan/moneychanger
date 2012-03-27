/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.shared.proxy;

/**
 * A Null Entity Proxy for various uses.
 *
 * @author Nilson
 */
public class NullEntityProxy {

  public static final NullCategoryProxy CATEGORY = new NullCategoryProxy();

  public static class NullCategoryProxy extends AbstractNullEntityProxy implements CategoryProxy {
    @Override
    public CategoryProxy getParent() {
      return null;
    }

    @Override
    public void setParent(CategoryProxy categoryProxy) {
    }
  }
}
