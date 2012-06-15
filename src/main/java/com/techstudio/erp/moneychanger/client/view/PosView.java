/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.techstudio.erp.moneychanger.client.NameTokens;
import com.techstudio.erp.moneychanger.client.presenter.PosPresenter.MyView;
import com.techstudio.erp.moneychanger.client.resources.Resources;
import com.techstudio.erp.moneychanger.client.ui.*;
import com.techstudio.erp.moneychanger.client.ui.cell.CategoryCell;
import com.techstudio.erp.moneychanger.shared.proxy.CategoryProxy;
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

  @UiField
  HTMLPanel ancBar;

  @UiField
  Anchor ancHome;

  @UiField
  Anchor ancBack;

  @UiField
  Anchor ancMenu;

  /**
   * Main Panel
   */

  @UiField
  HTMLPanel loadingMessage;

  @UiField
  HTMLPanel mainPanel;

  @UiField
  Label currentStep;

  /**
   * Category Panel
   */

  @UiField
  HTMLPanel categoryPulldown;

  @UiField(provided = true)
  CellList<CategoryProxy> categoryList = new CellList<CategoryProxy>(new CategoryCell());

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
  LabelInput qtpBuyQty;

  @UiField
  LabelInput qtpSellQty;

  @UiField
  LabelInput qtpRate1;

  @UiField
  LabelInput qtpRate2;

  @UiField
  LabelInput qtpRate3;

  @UiField
  LabelInput qtpRate4;

  @UiField
  LabelInput qtpRate5;

  @UiField
  LabelInput qtpRate6;

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
    setUpListing();
  }

  @Override
  public Widget asWidget() {
    return widget;
  }

  @SuppressWarnings("unused")
  @UiHandler("ancHome")
  public void onClickHome(ClickEvent event) {
    History.newItem(NameTokens.getMenuPage());
  }

  @SuppressWarnings("unused")
  @UiHandler("ancBack")
  public void onClickBack(ClickEvent event) {
    getUiHandlers().onBack();
  }

  @SuppressWarnings("unused")
  @UiHandler("ancMenu")
  public void onClickMenu(ClickEvent event) {
    if (mainPanel.getStyleName().contains("showPulldown")) {
      mainPanel.removeStyleName("showPulldown");
    } else {
      mainPanel.addStyleName("showPulldown");
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
  public HasData<CategoryProxy> getListing() {
    return categoryList;
  }

  @Override
  public void showItemPanel(boolean visible) {
    if (visible) {
//      mainPanel.setStyleName("slideshow show1");
      itemPanel.setVisible(true);
//      itemMenu.setVisible(true);
      qtyPanel.setVisible(false);
      transactionPanel.setVisible(false);
    }
  }

  @Override
  public void showQtyPanel(boolean visible) {
    if (visible) {
//      mainPanel.setStyleName("slideshow show2");
      itemPanel.setVisible(false);
//      itemMenu.setVisible(false);
      qtyPanel.setVisible(true);
      transactionPanel.setVisible(false);
    }
  }

  @Override
  public void showTxPanel(boolean visible) {
    if (visible) {
//      mainPanel.setStyleName("slideshow show3");
      itemPanel.setVisible(false);
//      itemMenu.setVisible(false);
      qtyPanel.setVisible(false);
      transactionPanel.setVisible(true);
    }
  }

  @Override
  public void showIconHome(boolean visible) {
    ancHome.setVisible(visible);
  }

  @Override
  public void showIconBack(boolean visible) {
    ancBack.setVisible(visible);
  }

  @Override
  public void showIconMenu(boolean visible) {
    ancMenu.setVisible(visible);
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
  public void clearItemMenu() {
    itemMenu.clear();
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

      myLineItem.addClickHandler(new ClickHandler() {
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

  @Override
  public void showLoading(boolean visible) {
    loadingMessage.setVisible(visible);
    currentStep.setVisible(!visible);
    mainPanel.setVisible(!visible);
    ancBar.setVisible(!visible);
  }

  private void setUpListing() {
    final NoSelectionModel<CategoryProxy> selectionModel = new NoSelectionModel<CategoryProxy>();
    categoryList.setSelectionModel(selectionModel);
    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
      @Override
      public void onSelectionChange(SelectionChangeEvent event) {
        CategoryProxy selected = selectionModel.getLastSelectedObject();
        if (selected != null) {
          getUiHandlers().switchCategory(selected);
          mainPanel.removeStyleName("showPulldown");
        }
      }
    });
  }
}