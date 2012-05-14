/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.pos.view;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.*;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.techstudio.erp.moneychanger.client.pos.presenter.PosPresenter.MyView;
import com.techstudio.erp.moneychanger.client.resources.Resources;
import com.techstudio.erp.moneychanger.client.ui.*;
import com.techstudio.erp.moneychanger.shared.proxy.ItemProxy;
import com.techstudio.erp.moneychanger.shared.proxy.LineItemProxy;
import com.techstudio.erp.moneychanger.shared.proxy.SpotRateProxy;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
   * Top Menu Panel
   */

  @UiField(provided = true)
  MyButton txAdd;

  @UiField(provided = true)
  MyButton txDel;

  @UiField(provided = true)
  MyButton txSav;

  /**
   * Main Panel
   */

  @UiField
  HTMLPanel mainPanel;

  @UiField
  Label currentStep;

  /**
   * Spot Rate
   */

  @UiField
  FlowPanel spotRateDisplay;

  @UiField
  CellTable<SpotRateProxy> spotRateTable = new CellTable<SpotRateProxy>();

  @UiField
  SimplePager spotRatePager = new SimplePager();

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
/*
  @UiField
  Label qtpTitle;*/

  @UiField
  HTMLPanel qtyPanel;

  @UiField
  Image qtpItemImageL;

  @UiField
  Image qtpItemImageR;

  /*@UiField
  Label qtpItemCode;

  @UiField
  Label qtpItemName;

  @UiField
  Label qtpItemUomRate;

  @UiField
  Label qtpItemUom;

  @UiField
  Label qtpItemBuy;

  @UiField
  Label qtpItemSell;*/

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

  /**
   * Transaction Panel (txp)
   */

  @UiField
  HTMLPanel transactionPanel;

  @UiField
  CellTable<LineItemProxy> lineItemTable = new CellTable<LineItemProxy>();

  @Inject
  public PosView(final Binder binder,
                 final Resources res,
                 final MyButton addBtn,
                 final MyButton savBtn,
                 final MyButton delBtn) {
    this.res = res;
    this.txAdd = addBtn;
    this.txSav = savBtn;
    this.txDel = delBtn;
    widget = binder.createAndBindUi(this);
    setupSpotRateTable();
    setupLineItemTable();
    qtpBuyQty.setAutoFocus();
  }

  @Override
  public Widget asWidget() {
    return widget;
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
  public HasData<SpotRateProxy> getSpotRateTable() {
    return spotRateTable;
  }

  @Override
  public HasData<LineItemProxy> getLineItemTable() {
    return lineItemTable;
  }

  @Override
  public void showTxPanel(boolean visible) {
    mainPanel.setVisible(visible);
    spotRateDisplay.setVisible(!visible);
  }

  @Override
  public void showItemPanel(boolean visible) {
    itemPanel.setVisible(visible);
  }

  @Override
  public void showAmtPanel(boolean visible) {
    qtyPanel.setVisible(visible);
  }

  @Override
  public void showRatePanel(boolean visible) {
    transactionPanel.setVisible(visible);
  }

  @Override
  public void showTxAdd(boolean visible) {
    txAdd.setVisible(visible);
  }

  @Override
  public void showTxDel(boolean visible) {
    txDel.setVisible(visible);
  }

  @Override
  public void showTxSav(boolean visible) {
    txSav.setVisible(visible);
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
  public void setItemImageL(String imageUrl) {
    if (imageUrl.isEmpty()) {
      qtpItemImageL.setResource(res.iNoImageAvailable());
    } else {
      qtpItemImageL.setUrl(imageUrl);
    }
  }

  @Override
  public void setItemImageR(String imageUrl) {
    if (imageUrl.isEmpty()) {
      qtpItemImageR.setResource(res.iNoImageAvailable());
    } else {
      qtpItemImageR.setUrl(imageUrl);
    }
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

  private void setupSpotRateTable() {
    Column<SpotRateProxy, String> spotRateNameColumn = new Column<SpotRateProxy, String>(new TextCell()) {
      @Override
      public String getValue(SpotRateProxy spotRateProxy) {
        return spotRateProxy.getName();
      }
    };
    spotRateTable.addColumn(spotRateNameColumn, "Item");

    Column<SpotRateProxy, String> spotRateBidColumn = new Column<SpotRateProxy, String>(new TextCell()) {
      @Override
      public String getValue(SpotRateProxy spotRateProxy) {
        return spotRateProxy.getBidRate().toPlainString();
      }
    };
    spotRateTable.addColumn(spotRateBidColumn, "Bid");

    Column<SpotRateProxy, String> spotRateAskColumn = new Column<SpotRateProxy, String>(new TextCell()) {
      @Override
      public String getValue(SpotRateProxy spotRateProxy) {
        return spotRateProxy.getAskRate().toPlainString();
      }
    };
    spotRateTable.addColumn(spotRateAskColumn, "Ask");

    spotRatePager.setDisplay(spotRateTable);
  }

  private void setupLineItemTable() {
    Column<LineItemProxy, String> lineItemImageColumn = new Column<LineItemProxy, String>(new MyImageCell()) {
      @Override
      public String getValue(LineItemProxy lineItemProxy) {
        String imgUrl = lineItemProxy.getItemBuy().getImageUrl();
        if (imgUrl.isEmpty()) {
          return imgUrl;
        }
        return imgUrl + "=s32";
      }
    };
    lineItemTable.addColumn(lineItemImageColumn, "");

    Column<LineItemProxy, String> lineItemNameColumn = new Column<LineItemProxy, String>(new TextCell()) {
      @Override
      public String getValue(LineItemProxy lineItemProxy) {
        return lineItemProxy.getItemBuy().getCode();
      }
    };
    lineItemTable.addColumn(lineItemNameColumn, "Item");

    Column<LineItemProxy, String> lineItemQuantityColumn = new Column<LineItemProxy, String>(new TextCell()) {
      @Override
      public String getValue(LineItemProxy lineItemProxy) {
        return lineItemProxy.getBuyQuantity().toPlainString();
      }
    };
    lineItemTable.addColumn(lineItemQuantityColumn, "Quantity");

    Column<LineItemProxy, String> lineItemPriceColumn = new Column<LineItemProxy, String>(new TextCell()) {
      @Override
      public String getValue(LineItemProxy lineItemProxy) {
        return lineItemProxy.getBuyUnitPrice().toPlainString();
      }
    };
    lineItemTable.addColumn(lineItemPriceColumn, "Price");

    Column<LineItemProxy, LineItemProxy> lineItemSubTotalColumn =
        new Column<LineItemProxy, LineItemProxy>(new TxSubTotalCell()) {
          @Override
          public LineItemProxy getValue(LineItemProxy lineItemProxy) {
            return lineItemProxy;
          }
        };
    Header<String> totalFooter = new Header<String>(new TextCell()) {
      @Override
      public String getValue() {
        List<LineItemProxy> lineItems = lineItemTable.getVisibleItems();
        if (lineItems.isEmpty()) {
          return "0.00";
        } else {
          BigDecimal totalPrice = BigDecimal.ZERO;
          for (LineItemProxy lineItem : lineItems) {
            /*if (lineItem.getTransactionType().equals(TransactionType.SALE)) {
              totalPrice = totalPrice.add(lineItem.getSubTotal());
            } else {
              totalPrice = totalPrice.subtract(lineItem.getSubTotal());
            }*/
          }
          totalPrice = totalPrice.setScale(2, RoundingMode.HALF_UP);
          return totalPrice.toString();
        }
      }
    };
    lineItemTable.addColumn(lineItemSubTotalColumn,
        new SafeHtmlHeader(SafeHtmlUtils.fromString("SubTotal")), totalFooter);


    final NoSelectionModel<LineItemProxy> selectionModel = new NoSelectionModel<LineItemProxy>();
    lineItemTable.setSelectionModel(selectionModel);
    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
      @Override
      public void onSelectionChange(SelectionChangeEvent event) {
        LineItemProxy selected = selectionModel.getLastSelectedObject();
        if (selected != null) {
          getUiHandlers().modifyItem(selected);
        }
      }
    });
  }

}