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
  @Source("category.txt")
  TextResource categoryText();

  @Source("currency.txt")
  TextResource currencyText();

  @Source("uom.txt")
  TextResource uomText();

  @Source("country.txt")
  TextResource countryText();

  @Source("item.txt")
  TextResource itemText();

  @Source("spotrate.txt")
  TextResource spotRatesText();

  @Source("clean-admin.css")
  StyleAdmin admin();

  @Source("loading.gif")
  ImageResource loadingIcon();

  @Source("logo.png")
  ImageResource logo();

  @Source("images/nia.png")
  ImageResource noImageAvailable();

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

  @Source("clean-pos.css")
  StylePos pos();

  @Source("logo_pos.png")
  ImageResource iLogoPos();

  @Source("images/32/note.png")
  ImageResource iTxNew();

  @Source("images/32/note_add.png")
  ImageResource iTxAdd();

  @Source("images/32/note_delete.png")
  ImageResource iTxDel();

  @Source("images/32/note_go.png")
  ImageResource iTxSav();

  @Source("images/16/money.png")
  ImageResource iCatCur();

  @Source("images/16/hand_point_270.png")
  ImageResource iItemSelect();

  public interface StylePos extends CssResource {
    String centered();

    String mainTitle();

    String mainSubtitle();

    String northBar();

    String north2Bar();

    String eastBar();

    String centerBar();

    String menuTitle();

    String pageTitle();

    String pageDescription();

    String formTitle();

    String formParam();

    String numPad();

    String numPadV();

    String curBtn();

    String btmBorder();
  }

  @Source("images/currency/72/aud.png")
  ImageResource iCurAud();

  @Source("images/currency/72/gbp.png")
  ImageResource iCurGbp();

  @Source("images/currency/72/myr.png")
  ImageResource iCurMyr();

  @Source("images/currency/72/sgd.png")
  ImageResource iCurSgd();

  @Source("images/currency/72/usd.png")
  ImageResource iCurUsd();

  @Source("images/currency/72/eur.png")
  ImageResource iCurEur();

  @Source("images/currency/72/inr.png")
  ImageResource iCurInr();

  @Source("images/currency/72/idr.png")
  ImageResource iCurIdr();

  @Source("images/currency/72/jpy.png")
  ImageResource iCurJpy();

  @Source("images/currency/72/thb.png")
  ImageResource iCurThb();
}
