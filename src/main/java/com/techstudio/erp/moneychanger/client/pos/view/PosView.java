/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.pos.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.techstudio.erp.moneychanger.client.pos.presenter.PosPresenter.MyView;
import com.techstudio.erp.moneychanger.client.resources.Resources;
import com.techstudio.erp.moneychanger.client.ui.*;
import com.techstudio.erp.moneychanger.shared.proxy.ItemProxy;
import com.techstudio.erp.moneychanger.shared.proxy.LineItemProxy;

import java.util.List;

/**
 * @author Nilson
 */
public class PosView
    extends ViewWithUiHandlers<PosUiHandlers>
    implements MyView {

  public interface Binder extends UiBinder<Widget, PosView> {
  }

  private final Widget widget;

  @UiField(provided = true)
  final Resources res;

  /**
   * Main Panel
   */

  @UiField
  HTMLPanel mainPanel;

  @UiField
  Anchor ancHome;

  @UiField
  Anchor ancMenu;

  @UiField
  Label currentStep;

  @UiField
  HTMLPanel wow;

  /**
   * Item Panel (itp)
   */

  @UiField
  HTMLPanel itemPanel;

  @UiField
  OrderedList itemMenu;

  /**
   * Quantity Panel (qtp)
   */

  @UiField
  HTMLPanel qtyPanel;

  @UiField
  LabelNumberBox qtpBuyQty;

  @UiField
  LabelNumberBox qtpSellQty;

  @UiField
  LabelNumberBox qtpRate1;

  @UiField
  LabelNumberBox qtpRate2;

  @UiField
  LabelNumberBox qtpRate3;

  @UiField
  LabelNumberBox qtpRate4;

  @UiField
  LabelNumberBox qtpRate5;

  @UiField
  LabelNumberBox qtpRate6;

  @UiField
  Button qtpOK;

  @UiField
  Button txAdd;

  @UiField
  Button txDel;

  @UiField
  Button txSav;

  /**
   * Transaction Panel (txp)
   */

  @UiField
  HTMLPanel transactionPanel;

  @UiField
  OrderedList lineItemTable;

  @Inject
  public PosView(final Binder binder, final Resources res) {
    this.res = res;
    widget = binder.createAndBindUi(this);
  }

  @Override
  public Widget asWidget() {
    return widget;
  }

  int i = 0;

  @UiHandler("ancHome")
  public void onClickHome(ClickEvent event) {
    if (i == 0) {
      mainPanel.setStyleName("slideshow show2");
      i = 1;
    } else if (i == 1) {
      mainPanel.setStyleName("slideshow show3");
      i = 2;
    } else {
      mainPanel.setStyleName("slideshow show1");
      i = 0;
    }
  }

  int x = 0;

  @UiHandler("ancMenu")
  public void onClickMenu(ClickEvent event) {
    if (x == 0) {
      wow.addStyleName("progress-bar");
      x = 1;
    } else {
      wow.removeStyleName("progress-bar");
      x = 0;
    }
  }

  @SuppressWarnings("unused")
  @UiHandler("txAdd")
  public void onTxAdd(ClickEvent event) {
    getUiHandlers().addToTransaction();
  }

  @SuppressWarnings("unused")
  @UiHandler("txDel")
  public void onTxDel(ClickEvent event) {
    getUiHandlers().deleteTransaction();
  }

  @SuppressWarnings("unused")
  @UiHandler("txSav")
  public void onTxSav(ClickEvent event) {
    getUiHandlers().saveTransaction();
  }

  @SuppressWarnings("unused")
  @UiHandler("qtpOK")
  public void onConfirmAmount(ClickEvent event) {
    getUiHandlers().confirmChanges();
  }

  @UiHandler("qtpBuyQty")
  public void onChangeBuyQty(ValueChangeEvent<String> event) {
    getUiHandlers().changeItemBuyQuantity(event.getValue());
  }

  @UiHandler("qtpSellQty")
  public void onChangeSellQty(ValueChangeEvent<String> event) {
    getUiHandlers().changeItemSellQuantity(event.getValue());
  }

  @UiHandler("qtpRate1")
  public void onChangeRate1(ValueChangeEvent<String> event) {
    getUiHandlers().changeIntRate(event.getValue());
  }

  @UiHandler("qtpRate2")
  public void onChangeRate2(ValueChangeEvent<String> event) {
    getUiHandlers().changeInvIntRate(event.getValue());
  }

  @UiHandler("qtpRate3")
  public void onChangeRate3(ValueChangeEvent<String> event) {
    getUiHandlers().changeBuyRate(event.getValue());
  }

  @UiHandler("qtpRate4")
  public void onChangeRate4(ValueChangeEvent<String> event) {
    getUiHandlers().changeInvBuyRate(event.getValue());
  }

  @UiHandler("qtpRate5")
  public void onChangeRate5(ValueChangeEvent<String> event) {
    getUiHandlers().changeSellRate(event.getValue());
  }

  @UiHandler("qtpRate6")
  public void onChangeRate6(ValueChangeEvent<String> event) {
    getUiHandlers().changeInvSellRate(event.getValue());
  }

  @Override
  public void showItemPanel(boolean visible) {
//    itemPanel.setVisible(visible);
    if (visible) {
//      mainPanel.removeStyleName("show2");
//      mainPanel.removeStyleName("show3");
      mainPanel.setStyleName("slideshow show1");
    }
  }

  @Override
  public void showAmtPanel(boolean visible) {
//    qtyPanel.setVisible(visible);
    if (visible) {
//      mainPanel.removeStyleName("show1");
//      mainPanel.removeStyleName("show3");
//      mainPanel.addStyleName("show2");
      mainPanel.setStyleName("slideshow show2");
//      qtpBuyQty.setFocus(true);
    }
  }

  @Override
  public void showRatePanel(boolean visible) {
//    transactionPanel.setVisible(visible);
    if (visible) {
//      mainPanel.removeStyleName("show1");
//      mainPanel.removeStyleName("show2");
//      mainPanel.addStyleName("show3");
      mainPanel.setStyleName("slideshow show3");
    }
  }

  public void resetSelections() {
    for (Widget listItem : itemMenu) {
      listItem.removeStyleName("activated");
    }
  }

  @Override
  public void setStep(String step) {
    currentStep.setText(step);
  }

  @Override
  public void addItemMenu(final ItemProxy item) {
    final ListItem listItem = new ListItem();
    final ItemMenuButton itemMenuButton = new ItemMenuButton(item);
    itemMenuButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        int result = getUiHandlers().itemSelected(item.getCode());
        if (result == 1) {
          listItem.addStyleName("activated");
        } else {
          listItem.removeStyleName("activated");
        }
      }
    });
    listItem.add(itemMenuButton);
    itemMenu.add(listItem);
  }

  @Override
  public void setDetailsTitle(String title) {
    currentStep.setText(title);
  }

  @Override
  public void setItemBuyCode(String itemCode) {
    qtpBuyQty.setText(itemCode);
  }

  @Override
  public void setItemSellCode(String itemCode) {
    qtpSellQty.setText(itemCode);
  }

  @Override
  public void setItemBuyUomRate(String itemUomRate) {
//    qtpItemUomRate.setText(itemUomRate);
  }

  @Override
  public void setItemSellUomRate(String itemRate) {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public void setItemBuyUom(String itemUom) {
//    qtpItemUom.setText(itemUom);
  }

  @Override
  public void setItemSellUom(String itemUOM) {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public void setR1(String unitPrice) {
    qtpRate1.setValue(unitPrice);
  }

  @Override
  public void setR2(String unitPrice) {
    qtpRate2.setValue(unitPrice);
  }

  @Override
  public void setR3(String unitPrice) {
    qtpRate3.setValue(unitPrice);
  }

  @Override
  public void setR4(String unitPrice) {
    qtpRate4.setValue(unitPrice);
  }

  @Override
  public void setR5(String unitPrice) {
    qtpRate5.setValue(unitPrice);
  }

  @Override
  public void setR6(String unitPrice) {
    qtpRate6.setValue(unitPrice);
  }

  @Override
  public void setItemBuyQuantity(String quantity) {
    qtpBuyQty.setValue(quantity);
  }

  @Override
  public void setItemSellQuantity(String amount) {
    qtpSellQty.setValue(amount);
  }

  @Override
  public void setR1Text(String r1) {
    qtpRate1.setText(r1);
  }

  @Override
  public void setR2Text(String r2) {
    qtpRate2.setText(r2);
  }

  @Override
  public void setR3Text(String r3) {
    qtpRate3.setText(r3);
  }

  @Override
  public void setR4Text(String r4) {
    qtpRate4.setText(r4);
  }

  @Override
  public void setR5Text(String r5) {
    qtpRate5.setText(r5);
  }

  @Override
  public void setR6Text(String r6) {
    qtpRate6.setText(r6);
  }

  @Override
  public void showRate1(boolean visible) {
    qtpRate1.setVisible(visible);
  }

  @Override
  public void showRate2(boolean visible) {
    qtpRate2.setVisible(visible);
  }

  @Override
  public void showRate3(boolean visible) {
    qtpRate3.setVisible(visible);
  }

  @Override
  public void showRate4(boolean visible) {
    qtpRate4.setVisible(visible);
  }

  @Override
  public void showRate5(boolean visible) {
    qtpRate5.setVisible(visible);
  }

  @Override
  public void showRate6(boolean visible) {
    qtpRate6.setVisible(visible);
  }

  @Override
  public void updateLineItems(List<LineItemProxy> lineItems) {
    lineItemTable.clear();
    int count = 0;
    int last = lineItems.size() - 1;
    for (LineItemProxy lineItem : lineItems) {
      final ListItem listItem = new ListItem();
      final MyLineItem myLineItem = new MyLineItem(lineItem);

      if (count == 0) {
        myLineItem.addStyleName("first");
      }
      if (count == last) {
        myLineItem.addStyleName("last");
      }

      final int index = count;

      myLineItem.getLineItemRemove().addClickHandler(new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
          getUiHandlers().removeLineItemIndex(index);
        }
      });

      myLineItem.getLineItemEdit().addClickHandler(new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
          getUiHandlers().modifyItem(index);
        }
      });

      count++;

      switch (lineItem.getTransactionType()) {
        case BUY:
          myLineItem.addStyleName("bar-green");
          break;
        case SELL:
          myLineItem.addStyleName("bar-blue");
          break;
        case BUYSELL:
          myLineItem.addStyleName("bar-orange");
          break;
      }

      listItem.add(myLineItem);
      lineItemTable.add(listItem);
    }
  }
}