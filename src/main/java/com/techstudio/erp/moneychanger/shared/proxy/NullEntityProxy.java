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
  public static final NullCurrencyProxy CURRENCY = new NullCurrencyProxy();

  public static class NullCategoryProxy
      extends AbstractNullEntityProxy implements CategoryProxy {
    @Override
    public CategoryProxy getParent() {
      return null;
    }

    @Override
    public void setParent(CategoryProxy categoryProxy) {
    }
  }

  public static class NullCurrencyProxy
      extends AbstractNullEntityProxy implements CurrencyProxy {
    @Override
    public String getSign() {
      return "";
    }

    @Override
    public void setSign(String sign) {
    }

    @Override
    public String getRate() {
      return "";
    }

    @Override
    public void setRate(String rate) {
    }
  }
}
