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
  public static final String CATEGORY_PAGE = "!CATEGORY_PAGE";

  public static final String ITEM_PAGE = "!ITEM_PAGE";

  public static final String POS_PAGE = "!POS_PAGE";

  public static String getCategoryPage() {
    return CATEGORY_PAGE;
  }

  public static String getItemPage() {
    return ITEM_PAGE;
  }

  public static String getPosPage() {
    return POS_PAGE;
  }
}