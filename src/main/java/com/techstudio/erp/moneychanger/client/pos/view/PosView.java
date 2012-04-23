/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.pos.view;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.event.dom.client.ClickEvent;
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
import com.techstudio.erp.moneychanger.client.ui.ItemMenuCell;
import com.techstudio.erp.moneychanger.client.ui.NumberBox;
import com.techstudio.erp.moneychanger.client.ui.PosLogo;
import com.techstudio.erp.moneychanger.client.ui.TxSubTotalCell;
import com.techstudio.erp.moneychanger.shared.domain.TransactionType;
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

  @UiField(provided = true)
  PosLogo logo;

  /**
   * Top Menu Panel
   */

  @UiField
  Image txNew;

  @UiField
  Image txAdd;

  @UiField
  Image txDel;

  @UiField
  Image txSav;

  /**
   * Main Panel
   */

  @UiField
  VerticalPanel mainPanel;

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

  @UiField(provided = true)
  CellTable<List<ItemProxy>> itemTable = new CellTable<List<ItemProxy>>();

  @UiField
  SimplePager itemPager = new SimplePager();

  @UiField
  FlowPanel itemPanel;

  /**
   * Quantity Panel (qtp)
   */

  @UiField
  DecoratorPanel qtyPanel;

  @UiField
  Label qtpItemCode;

  @UiField
  Label qtpItemName;

  @UiField
  Label qtpItemUomRate;

  @UiField
  Label qtpItemUom;

  @UiField
  Label qtpTxType;

  @UiField(provided = true)
  NumberBox qtpItemUnitPrice;

  @UiField(provided = true)
  NumberBox qtpItemQuantity;

  @UiField
  Button qtpOK;

  /**
   * Transaction Panel (txp)
   */

  @UiField
  DecoratorPanel transactionPanel;

  @UiField
  CellTable<LineItemProxy> lineItemTable = new CellTable<LineItemProxy>();

  @Inject
  public PosView(final Binder binder,
                 final Resources res,
                 final NumberBox numBoxUnitPrice,
                 final NumberBox numBoxQuantity,
                 final PosLogo logo) {
    this.logo = logo;
    this.res = res;
    this.qtpItemUnitPrice = numBoxUnitPrice;
    this.qtpItemQuantity = numBoxQuantity;
    this.itemTable = new CellTable<List<ItemProxy>>(4, res.tableResources());
    widget = binder.createAndBindUi(this);
    setupSpotRateTable();
    setupItemTable();
    setupLineItemTable();
  }

  @Override
  public Widget asWidget() {
    return widget;
  }

  @SuppressWarnings("unused")
  @UiHandler("txNew")
  public void onTxNew(ClickEvent event) {
    getUiHandlers().returnToTransaction();
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
    getUiHandlers().viewRates();
  }

  @SuppressWarnings("unused")
  @UiHandler("qtpOK")
  public void onConfirmAmount(ClickEvent event) {
    try {
      getUiHandlers().confirmItemRate(qtpItemUnitPrice.getValue());
      getUiHandlers().confirmItemQuantity(qtpItemQuantity.getValue());
      getUiHandlers().confirmChanges();
    } catch (Exception e) {
      Log.error("Failed to confirm quantity.", e);
    }
  }

  @Override
  public HasData<SpotRateProxy> getSpotRateTable() {
    return spotRateTable;
  }

  @Override
  public HasData<List<ItemProxy>> getItemTable() {
    return itemTable;
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
  public void showTxNew(boolean visible) {
    txNew.setVisible(visible);
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

  @Override
  public void setStep(String step) {
    currentStep.setText(step);
  }

  @Override
  public void setItemCode(String itemCode) {
    qtpItemCode.setText(itemCode);
  }

  @Override
  public void setItemName(String itemName) {
    qtpItemName.setText(itemName);
  }

  @Override
  public void setItemUomRate(String itemUomRate) {
    qtpItemUomRate.setText(itemUomRate);
  }

  @Override
  public void setItemUOM(String itemUom) {
    qtpItemUom.setText(itemUom);
  }

  @Override
  public void setTxType(String txType) {
    this.qtpTxType.setText(txType);
  }

  @Override
  public void setItemUnitPrice(String unitPrice) {
    qtpItemUnitPrice.setValue(unitPrice);
  }

  @Override
  public void setItemQuantity(String quantity) {
    qtpItemQuantity.setValue(quantity);
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

  private void setupItemTable() {
    FieldUpdater<List<ItemProxy>, ItemProxy> itemMenuFieldUpdater = new FieldUpdater<List<ItemProxy>, ItemProxy>() {
      @Override
      public void update(int index, List<ItemProxy> object, ItemProxy value) {
        getUiHandlers().itemSelected(value.getCode());
      }
    };

    Column<List<ItemProxy>, ItemProxy> item1stColumn =
        new Column<List<ItemProxy>, ItemProxy>(new ItemMenuCell()) {
          @Override
          public ItemProxy getValue(List<ItemProxy> list) {
            return list.get(0);
          }
        };
    item1stColumn.setFieldUpdater(itemMenuFieldUpdater);
    itemTable.addColumn(item1stColumn);

    Column<List<ItemProxy>, ItemProxy> item2ndColumn =
        new Column<List<ItemProxy>, ItemProxy>(new ItemMenuCell()) {
          @Override
          public ItemProxy getValue(List<ItemProxy> list) {
            return list.get(1);
          }
        };
    item2ndColumn.setFieldUpdater(itemMenuFieldUpdater);
    itemTable.addColumn(item2ndColumn);

    Column<List<ItemProxy>, ItemProxy> item3rdColumn =
        new Column<List<ItemProxy>, ItemProxy>(new ItemMenuCell()) {
          @Override
          public ItemProxy getValue(List<ItemProxy> list) {
            return list.get(2);
          }
        };
    item3rdColumn.setFieldUpdater(itemMenuFieldUpdater);
    itemTable.addColumn(item3rdColumn);

    Column<List<ItemProxy>, ItemProxy> item4thColumn =
        new Column<List<ItemProxy>, ItemProxy>(new ItemMenuCell()) {
          @Override
          public ItemProxy getValue(List<ItemProxy> list) {
            return list.get(3);
          }
        };
    item4thColumn.setFieldUpdater(itemMenuFieldUpdater);
    itemTable.addColumn(item4thColumn);

    Column<List<ItemProxy>, ItemProxy> item5thColumn =
        new Column<List<ItemProxy>, ItemProxy>(new ItemMenuCell()) {
          @Override
          public ItemProxy getValue(List<ItemProxy> list) {
            return list.get(4);
          }
        };
    item5thColumn.setFieldUpdater(itemMenuFieldUpdater);
    itemTable.addColumn(item5thColumn);
  }

  private void setupLineItemTable() {
    Column<LineItemProxy, String> lineItemNameColumn = new Column<LineItemProxy, String>(new TextCell()) {
      @Override
      public String getValue(LineItemProxy lineItemProxy) {
        return lineItemProxy.getItem().getCode();
      }
    };
    lineItemTable.addColumn(lineItemNameColumn, "Item");

    Column<LineItemProxy, String> lineItemQuantityColumn = new Column<LineItemProxy, String>(new TextCell()) {
      @Override
      public String getValue(LineItemProxy lineItemProxy) {
        return lineItemProxy.getQuantity().toPlainString();
      }
    };
    lineItemTable.addColumn(lineItemQuantityColumn, "Quantity");

    Column<LineItemProxy, String> lineItemPriceColumn = new Column<LineItemProxy, String>(new TextCell()) {
      @Override
      public String getValue(LineItemProxy lineItemProxy) {
        return lineItemProxy.getUnitPrice().toPlainString();
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
            if (lineItem.getTransactionType().equals(TransactionType.SALE)) {
              totalPrice = totalPrice.add(lineItem.getSubTotal());
            } else {
              totalPrice = totalPrice.subtract(lineItem.getSubTotal());
            }
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