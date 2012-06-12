/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client;

/**
 * The central location of all name tokens for the application. All
 * {@link com.gwtplatform.mvp.client.proxy.ProxyPlace} classes get their tokens from here.
 * This class also makes it easy to use name tokens as a resource within UIBinder xml files.
 * <p/>
 * The public static final String is used within the annotation
 * {@link com.gwtplatform.mvp.client.annotations.NameToken}, which can't use a method and
 * the method associated with this field is used within UiBinder which can't access static fields.
 * <p/>
 * Also note the exclamation mark in front of the tokens, this is used for
 * search engine crawling support.
 *
 * @author Nilson
 */
public class NameTokens {
  public static final String MENU_PAGE = "!MENU";

  public static final String POS_PAGE = "!POS";

  public static final String ITEM_PAGE = "!ITEM";

  public static final String CATEGORY_PAGE = "!CATEGORY";

  public static final String COUNTRY_PAGE = "!COUNTRY";

  public static final String CLIENT_PAGE = "!CLIENT";

  public static final String CURRENCY_PAGE = "!CURRENCY";

  public static final String PRICING_PAGE = "!PRICING";

  public static final String TRANSACTION_PAGE = "!TRANSACTION";

  public static final String UOM_PAGE = "!UOM";

  public static final String SETTINGS_PAGE = "!SETTINGS";

  public static String getMenuPage() {
    return MENU_PAGE;
  }

  public static String getPosPage() {
    return POS_PAGE;
  }

  public static String getItemPage() {
    return ITEM_PAGE;
  }

  public static String getCategoryPage() {
    return CATEGORY_PAGE;
  }

  public static String getCountryPage() {
    return COUNTRY_PAGE;
  }

  public static String getClientPage() {
    return CLIENT_PAGE;
  }

  public static String getCurrencyPage() {
    return CURRENCY_PAGE;
  }

  public static String getPricingPage() {
    return PRICING_PAGE;
  }

  public static String getTransactionPage() {
    return TRANSACTION_PAGE;
  }

  public static String getUomPage() {
    return UOM_PAGE;
  }

  public static String getSettingsPage() {
    return SETTINGS_PAGE;
  }
}