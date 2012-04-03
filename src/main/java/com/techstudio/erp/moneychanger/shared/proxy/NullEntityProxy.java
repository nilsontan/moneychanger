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
  public static final NullItemProxy ITEM = new NullItemProxy();
  public static final NullExchangeRateProxy EXCHANGE_RATE_PROXY = new NullExchangeRateProxy();

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
  }

  public static class NullItemProxy
      extends AbstractNullEntityProxy implements ItemProxy {

    @Override
    public CategoryProxy getCategory() {
      return null;
    }

    @Override
    public void setCategory(CategoryProxy categoryProxy) {
    }

    @Override
    public CurrencyProxy getCurrency() {
      return null;
    }

    @Override
    public void setCurrency(CurrencyProxy currencyProxy) {
    }

    @Override
    public UomProxy getUom() {
      return null;
    }

    @Override
    public void setUom(UomProxy uomProxy) {
    }

    @Override
    public Integer getUomRate() {
      return 1;
    }

    @Override
    public void setUomRate(Integer uomRate) {
    }
  }

  public static class NullExchangeRateProxy
      extends AbstractNullEntityProxy implements ExchangeRateProxy {

    @Override
    public CurrencyProxy getCurrency() {
      return null;
    }

    @Override
    public void setCurrency(CurrencyProxy currencyProxy) {
    }

    @Override
    public Integer getUnit() {
      return 1;
    }

    @Override
    public void setUnit(Integer unit) {
    }

    @Override
    public String getAskRate() {
      return "1";
    }

    @Override
    public void setAskRate(String askRate) {
    }

    @Override
    public String getBidRate() {
      return "1";
    }

    @Override
    public void setBidRate(String bidRate) {
    }
  }
}
