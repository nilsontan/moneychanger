/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;

/**
 * @author Nilson
 */
public interface Resources extends ClientBundle {
  @Source("clean-admin.css")
  @CssResource.NotStrict
  StyleAdmin admin();

  @Source("clean-pos.css")
  @CssResource.NotStrict
  StylePos pos();

  @Source("logo.png")
  ImageResource logo();

  @Source("logo_pos.png")
  ImageResource logoPos();

  @Source("category.txt")
  TextResource categoryText();

  @Source("currency.txt")
  TextResource currencyText();

  @Source("uom.txt")
  TextResource uomText();

  @Source("item.txt")
  TextResource itemText();

  @Source("exchange_rates.txt")
  TextResource xrRatesText();

  public interface StyleAdmin extends CssResource {
    String centered();

    String mainTitle();

    String mainSubtitle();

    String northBar();

    String north2Bar();

    String menuTitle();

    String pageTitle();

    String pageDescription();

    String formTitle();

    String formParam();
  }

  public interface StylePos extends CssResource {
    String centered();

    String mainTitle();

    String mainSubtitle();

    String northBar();

    String north2Bar();

    String eastBar();

    String menuTitle();

    String pageTitle();

    String pageDescription();

    String formTitle();

    String formParam();

    String numPad();

    String numPadV();
  }
}
