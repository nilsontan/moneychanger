/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.techstudio.erp.moneychanger.shared.proxy.ItemProxy;
import com.techstudio.erp.moneychanger.shared.proxy.LineItemProxy;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * @author Nilson
 */
public class MyLineObject extends Composite
    implements HasClickHandlers {

  @Override
  public HandlerRegistration addClickHandler(ClickHandler handler) {
    return addDomHandler(handler, ClickEvent.getType());
  }

  public interface Binder extends UiBinder<Widget, MyLineObject> {
  }

  private static Binder binder = GWT.create(Binder.class);

  @UiField
  HTMLPanel pane;

  @UiField
  Anchor lineItemRemove;

  @UiField
  Anchor lineItemEdit;

  @UiField
  Label itemBuy;

  @UiField
  Label itemBuyQty;

  @UiField
  Label itemSell;

  @UiField
  Label itemSellQty;


  public MyLineObject() {
    initWidget(binder.createAndBindUi(this));
  }

  public MyLineObject(LineItemProxy lineItemProxy) {
    this();
    ItemProxy itemToBuy = lineItemProxy.getItemBuy();
    BigDecimal buyQty = lineItemProxy.getBuyQuantity().multiply(itemToBuy.getUomRate(), MathContext.UNLIMITED);
    itemBuy.setText(lineItemProxy.getItemBuy().getCode());
    itemBuyQty.setText(formatValue(buyQty));

    ItemProxy itemToSell = lineItemProxy.getItemSell();
    BigDecimal sellQty = lineItemProxy.getSellQuantity().multiply(itemToSell.getUomRate(), MathContext.UNLIMITED);
    itemSell.setText(lineItemProxy.getItemSell().getCode());
    itemSellQty.setText(formatValue(sellQty));
  }

  public Anchor getLineItemRemove() {
    return lineItemRemove;
  }

  public Anchor getLineItemEdit() {
    return lineItemEdit;
  }

  private String formatValue(BigDecimal number) {
    NumberFormat numberFormat = NumberFormat.getFormat("###,###,###,##0.00");
    return numberFormat.format(number.doubleValue());
  }
}
