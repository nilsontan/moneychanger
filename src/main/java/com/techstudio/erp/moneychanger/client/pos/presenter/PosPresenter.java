/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.pos.presenter;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.base.CharMatcher;
import com.google.common.collect.Lists;
import com.google.gwt.view.client.HasData;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.techstudio.erp.moneychanger.client.NameTokens;
import com.techstudio.erp.moneychanger.client.pos.view.PosUiHandlers;
import com.techstudio.erp.moneychanger.client.ui.LineItemDataProvider;
import com.techstudio.erp.moneychanger.client.ui.MenuItemDataProvider;
import com.techstudio.erp.moneychanger.client.ui.SpotRateDataProvider;
import com.techstudio.erp.moneychanger.shared.domain.TransactionType;
import com.techstudio.erp.moneychanger.shared.proxy.ItemProxy;
import com.techstudio.erp.moneychanger.shared.proxy.LineItemProxy;
import com.techstudio.erp.moneychanger.shared.proxy.SpotRateProxy;
import com.techstudio.erp.moneychanger.shared.service.ItemRequest;
import com.techstudio.erp.moneychanger.shared.service.LineItemRequest;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

/**
 * @author Nilson
 */
public class PosPresenter
    extends Presenter<PosPresenter.MyView, PosPresenter.MyProxy>
    implements PosUiHandlers {

  /**
   * {@link com.techstudio.erp.moneychanger.client.pos.presenter.PosPresenter}'s proxy.
   */
  @ProxyCodeSplit
  @NameToken(NameTokens.POS_PAGE)
  public interface MyProxy extends ProxyPlace<PosPresenter> {
  }

  public interface MyView extends View, HasUiHandlers<PosUiHandlers> {
    HasData<SpotRateProxy> getSpotRateTable();

    HasData<List<ItemProxy>> getItemTable();

    HasData<LineItemProxy> getLineItemTable();

    void showTxPanel(boolean visible);

    void showItemPanel(boolean visible);

    void showAmtPanel(boolean visible);

    void showRatePanel(boolean visible);

    void showTxAdd(boolean visible);

    void showTxDel(boolean visible);

    void showTxSav(boolean visible);

    void setStep(String step);

    void setItemImage(String imageUrl);

    void setItemCode(String itemCode);

    void setItemName(String itemName);

    void setItemUomRate(String itemRate);

    void setItemUOM(String itemUOM);

    void setTxType(String itemTx);

    void setItemUnitPrice(String unitPrice);

    void setItemQuantity(String amount);
  }

  private final Provider<ItemRequest> itemRequestProvider;
  private final Provider<LineItemRequest> lineItemRequestProvider;
  private final SpotRateDataProvider spotRateDataProvider;
  private final MenuItemDataProvider menuItemDataProvider;
  private final LineItemDataProvider lineItemDataProvider;

  private Step step;
  private LineItemProxy pendingItem;
  private List<LineItemProxy> lineItems;
  private List<LineItemProxy> pendingList;
  private int nextLine;

  private String buyItemCode;

  private BigDecimal itemRate;
  private BigDecimal itemQuantity;

  private Screen screen;

  @Inject
  public PosPresenter(final EventBus eventBus,
                      final MyView view,
                      final MyProxy proxy,
                      final SpotRateDataProvider spotRateDataProvider,
                      final MenuItemDataProvider menuItemDataProvider,
                      final LineItemDataProvider lineItemDataProvider,
                      final Provider<ItemRequest> itemRequestProvider,
                      final Provider<LineItemRequest> lineItemRequestProvider) {
    super(eventBus, view, proxy);
    getView().setUiHandlers(this);
    this.itemRequestProvider = itemRequestProvider;
    this.lineItemRequestProvider = lineItemRequestProvider;
    this.spotRateDataProvider = spotRateDataProvider;
    this.spotRateDataProvider.addDataDisplay(getView().getSpotRateTable());
    this.menuItemDataProvider = menuItemDataProvider;
    this.menuItemDataProvider.addDataDisplay(getView().getItemTable());
    this.lineItemDataProvider = lineItemDataProvider;
    this.lineItemDataProvider.addDataDisplay(getView().getLineItemTable());
  }

  @Override
  protected void revealInParent() {
    RevealContentEvent.fire(this, MainPosPresenter.TYPE_SetMainContent, this);
  }

  @Override
  protected void onReset() {
    super.onReset();
    reset();
  }

  protected void onReveal() {
  }

  @Override
  public void switchView() {
    Log.debug("Switching views ...");
    flipViews();
  }

  @Override
  public void addToTransaction() {
    Log.debug("Adding to tx ...");
    advanceToStep(Step.BUY);
  }

  @Override
  public void deleteTransaction() {
    Log.debug("Deleting tx ...");
    reset();
  }

  @Override
  public void saveTransaction() {
    Log.debug("Saving tx ...");
    reset();
  }

  @Override
  public void skipStep() {
    Log.debug("Transacting with default currency ...");
    switch (step) {
      case BUY:
        advanceToStep(Step.SELL);
        break;
      case SELL:
        advanceToStep(Step.DETAILS);
        break;
    }
  }

  @Override
  public void itemSelected(final String itemCode) {
    if (itemCode.equals(buyItemCode))
      return;
    else
      buyItemCode = itemCode;

    Log.debug("Selected item: " + itemCode);
    if (itemCode.equals("SGD")) {
      skipStep();
      return;
    }

    switch (step) {
      case BUY:
      case SELL:
        itemRequestProvider.get()
            .fetchByProperty("code", itemCode)
            .with(ItemProxy.UOM)
            .fire(new Receiver<List<ItemProxy>>() {
              @Override
              public void onSuccess(List<ItemProxy> response) {
                assert response.size() > 0 : "No item found for the following code " + itemCode;
                setItemAndMove(response.get(0));
              }
            });
        break;
      default:
        Log.error("An item is being selected at the incorrect step.");
    }
  }

  @Override
  public void modifyItem(LineItemProxy lineItem) {
    pendingList.add(lineItem);
    int line = lineItem.getLine();

    for (LineItemProxy lineItemProxy : lineItems) {
      int curLine = lineItemProxy.getLine();
      if (curLine < line) {
      } else if (curLine == line) {
        if (!lineItemProxy.equals(lineItem)) {
          pendingList.add(lineItemProxy);
          break;
        }
      } else {
        break;
      }
    }

    advanceToStep(Step.DETAILS);
  }

  @Override
  public void confirmItemRate(String itemRate) {
    this.itemRate = returnAmount(itemRate);
  }

  @Override
  public void confirmItemQuantity(String itemQuantity) {
    this.itemQuantity = returnAmount(itemQuantity);
  }

  @Override
  public void confirmChanges() {
    pendingItem.setUnitPrice(itemRate);
    pendingItem.setQuantity(itemQuantity);
    BigDecimal itemUomRate = new BigDecimal(pendingItem.getItem().getUomRate());
    BigDecimal subTotal = pendingItem.getUnitPrice()
        .multiply(pendingItem.getQuantity(), MathContext.UNLIMITED)
        .divide(itemUomRate);
    pendingItem.setSubTotal(subTotal);

    if (!pendingList.isEmpty()) {
      pendingItem = pendingList.remove(0);
      itemUomRate = new BigDecimal(pendingItem.getItem().getUomRate());
      BigDecimal sellQuantity = subTotal
          .multiply(itemUomRate, MathContext.UNLIMITED)
          .divide(pendingItem.getUnitPrice(), 4, RoundingMode.HALF_UP);
      pendingItem.setQuantity(sellQuantity);
      pendingItem.setSubTotal(subTotal);
    }

    advanceToStep(Step.CONFIRM);
  }

  private void reset() {
    pendingItem = null;
    lineItems = Lists.newArrayList();
    pendingList = Lists.newArrayList();
    nextLine = 1;
    buyItemCode = "";
    screen = Screen.TRX;
    advanceToStep(Step.BUY);
    flipViews();
  }

  private void advanceToStep(Step nextStep) {
    switch (nextStep) {
      case BUY:
        step = Step.BUY;
        buyItemCode = "";
        break;
      case SELL:
        step = Step.SELL;
        break;
      case DETAILS:
        step = Step.DETAILS;
        assert !pendingList.isEmpty() : "Entered into Step.DETAILS with an empty pending list";
        pendingItem = pendingList.remove(0);
        break;
      case CONFIRM:
        step = Step.CONFIRM;
        lineItemDataProvider.updateTableData(lineItems);
        break;
      default:
        Log.error("Step not configured: " + step);
    }
    updateView();
  }

  private BigDecimal returnAmount(String amount) {
    String filterOutNonDigitsAndDot = CharMatcher.DIGIT.or(CharMatcher.is('.')).retainFrom(amount).trim();
    return new BigDecimal(filterOutNonDigitsAndDot);
  }

  private void setItemAndMove(ItemProxy itemProxy) {
    switch (step) {
      case BUY:
        LineItemProxy proxy = createLineItem(itemProxy, TransactionType.PURCHASE);
        pendingList.add(proxy);
        lineItems.add(proxy);
        advanceToStep(Step.SELL);
        break;
      case SELL:
        proxy = createLineItem(itemProxy, TransactionType.SALE);
        pendingList.add(proxy);
        lineItems.add(proxy);
        advanceToStep(Step.DETAILS);
        break;
      default:
    }
  }

  private LineItemProxy createLineItem(ItemProxy itemProxy, TransactionType transactionType) {
    boolean isSelling = transactionType.equals(TransactionType.SALE);

    LineItemRequest request = lineItemRequestProvider.get();
    LineItemProxy proxy = request.create(LineItemProxy.class);
    proxy.setTransactionType(transactionType);
    proxy.setItem(itemProxy);
    SpotRateProxy spotRateProxy = spotRateDataProvider.getSpotRateForCode(itemProxy.getCode());
    proxy.setUnitPrice(isSelling ? spotRateProxy.getBidRate() : spotRateProxy.getAskRate());
    proxy.setQuantity(BigDecimal.ONE);
    proxy.setLine(nextLine);
    if (isSelling) nextLine++;

    return proxy;
  }

  private void flipViews() {
    boolean showTxView = true;
    switch (screen) {
      case RATES:
        showTxView = false;
        screen = Screen.TRX;
        break;
      case TRX:
        showTxView = true;
        screen = Screen.RATES;
        break;
    }
    getView().showTxPanel(showTxView);
    getView().showTxAdd(showTxView && (!(step.amtEntering || step.itemSelecting)));
    getView().showTxDel(showTxView && !lineItems.isEmpty());
    getView().showTxSav(showTxView && (!(step.amtEntering || step.itemSelecting)));
  }

  private void updateView() {
    // 3 panels to hide/show : 1.TxPanel contains -> 2.ItemPanel 3.RatePanel
    getView().showItemPanel(step.itemSelecting);
    getView().showAmtPanel(step.amtEntering);
    getView().showRatePanel(!(step.amtEntering || step.itemSelecting));
    getView().showTxAdd(!(step.amtEntering || step.itemSelecting));
    getView().showTxDel(!lineItems.isEmpty());
    getView().showTxSav(!(step.amtEntering || step.itemSelecting));
    getView().setStep(step.inst);

    if (pendingItem != null && step.equals(Step.DETAILS)) {
      ItemProxy item = pendingItem.getItem();
      getView().setItemCode(item.getCode());
      getView().setItemName(item.getName());
      getView().setItemUomRate(item.getUomRate().toString());
      getView().setItemUOM(item.getUom().getName());
      getView().setItemImage(item.getImageUrl());
      getView().setTxType(pendingItem.getTransactionType().print());
      getView().setItemUnitPrice(pendingItem.getUnitPrice().toString());
      getView().setItemQuantity(pendingItem.getQuantity().toString());
    } else {
      getView().setItemImage("");
    }
  }

  /**
   * Flow for a transaction
   * <ol>
   * <li>Buy(Stock Coming In)</li>
   * <li>Sell(Stock Going Out)</li>
   * <li>Show the Rate and Key in amount</li>
   * </ol>
   */
  private enum Step {
    BUY("Buying", true, false, true),
    SELL("Selling", true, false, true),
    DETAILS("Details", true, true, false),
    CONFIRM("CONFIRM", true, false, false);

    Step(String inst, boolean transacting,
         boolean amtEntering, boolean itemSelecting) {
      this.inst = inst;
      this.transacting = transacting;
      this.amtEntering = amtEntering;
      this.itemSelecting = itemSelecting;
    }

    final String inst;
    final boolean transacting;
    final boolean amtEntering;
    final boolean itemSelecting;
  }

  private enum Screen {
    RATES,
    TRX
  }
}
