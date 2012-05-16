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
import com.techstudio.erp.moneychanger.client.gin.DefaultCurrency;
import com.techstudio.erp.moneychanger.client.gin.DefaultScaleForItemQty;
import com.techstudio.erp.moneychanger.client.gin.DefaultScaleForRate;
import com.techstudio.erp.moneychanger.client.pos.view.PosUiHandlers;
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

    void showTxPanel(boolean visible);

    void showItemPanel(boolean visible);

    void showAmtPanel(boolean visible);

    void showRatePanel(boolean visible);

    void showTxAdd(boolean visible);

    void showTxDel(boolean visible);

    void showTxSav(boolean visible);

    void resetSelections();

    void setStep(String step);

    void addItemMenu(ItemProxy item);

    void setDetailsTitle(String title);

    void setItemImageL(String imageUrl);

    void setItemImageR(String imageUrl);

    void setItemBuyCode(String itemCode);

    void setItemSellCode(String itemCode);

    void setItemBuyUomRate(String itemRate);

    void setItemSellUomRate(String itemRate);

    void setItemBuyUom(String itemUOM);

    void setItemSellUom(String itemUOM);

    void setR1(String unitPrice);

    void setR2(String unitPrice);

    void setR3(String unitPrice);

    void setR4(String unitPrice);

    void setR5(String unitPrice);

    void setR6(String unitPrice);

    void setItemBuyQuantity(String amount);

    void setItemSellQuantity(String amount);

    void setR1Text(String r1);

    void setR2Text(String r2);

    void setR3Text(String r3);

    void setR4Text(String r4);

    void setR5Text(String r5);

    void setR6Text(String r6);

    void showRate1(boolean visible);

    void showRate2(boolean visible);

    void showRate3(boolean visible);

    void showRate4(boolean visible);

    void showRate5(boolean visible);

    void showRate6(boolean visible);

    void updateLineItems(List<LineItemProxy> lineItems);
  }

  private final Provider<ItemRequest> itemRequestProvider;
  private final Provider<LineItemRequest> lineItemRequestProvider;
  private final SpotRateDataProvider spotRateDataProvider;

  private Step step;
  private List<LineItemProxy> lineItems;
  private LineItemProxy pendingLineItem;
  private int nextLine;

  private String buyItemCode;

  @Inject
  @DefaultCurrency
  private String dftCurrCode;

  @Inject
  @DefaultScaleForRate
  private int rateScale;

  @Inject
  @DefaultScaleForItemQty
  private int qtyScale;

  private BigDecimal buyRate;
  private BigDecimal sellRate;
  private BigDecimal intRate;
  private BigDecimal dealRate;

  private Screen screen;

  @Inject
  public PosPresenter(final EventBus eventBus,
                      final MyView view,
                      final MyProxy proxy,
                      final SpotRateDataProvider spotRateDataProvider,
                      final Provider<ItemRequest> itemRequestProvider,
                      final Provider<LineItemRequest> lineItemRequestProvider) {
    super(eventBus, view, proxy);
    getView().setUiHandlers(this);
    this.itemRequestProvider = itemRequestProvider;
    this.lineItemRequestProvider = lineItemRequestProvider;
    this.spotRateDataProvider = spotRateDataProvider;
    this.spotRateDataProvider.addDataDisplay(getView().getSpotRateTable());

    this.itemRequestProvider.get()
        .fetchAll()
        .fire(new Receiver<List<ItemProxy>>() {
          @Override
          public void onSuccess(List<ItemProxy> responses) {
            for (ItemProxy itemProxy : responses) {
              getView().addItemMenu(itemProxy);
            }
          }
        });
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
    shiftToStep(Step.BUY);
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
        shiftToStep(Step.SELL);
        break;
      case SELL:
        shiftToStep(Step.DETAILS);
        break;
    }
  }

  @Override
  public int itemSelected(final String itemCode) {
    if (itemCode.equals(buyItemCode)) {
      buyItemCode = "";
      shiftToStep(Step.BUY);
      return 0;
    }

    Log.debug("Selected item: " + itemCode);
    switch (step) {
      case BUY:
        buyItemCode = itemCode;
        shiftToStep(Step.SELL);
        break;
      case SELL:
        itemRequestProvider.get()
            .fetchByProperty("code", buyItemCode)
            .with(ItemProxy.UOM)
            .fire(new Receiver<List<ItemProxy>>() {
              @Override
              public void onSuccess(final List<ItemProxy> buyItem) {
                assert buyItem.size() > 0 : "No item found for the following code " + buyItemCode;
                itemRequestProvider.get()
                    .fetchByProperty("code", itemCode)
                    .with(ItemProxy.UOM)
                    .fire(new Receiver<List<ItemProxy>>() {
                      @Override
                      public void onSuccess(List<ItemProxy> sellItem) {
                        assert sellItem.size() > 0 : "No item found for the following code " + itemCode;
                        buyItemCode = "";
                        pendingLineItem = createLineItem(buyItem.get(0), sellItem.get(0));
                        lineItems.add(pendingLineItem);
                        recalculateRates();
                        shiftToStep(Step.DETAILS);
                      }
                    });
              }
            });
        break;
      default:
        Log.error("An item is being selected at the incorrect step.");
    }
    return 1;
  }

  @Override
  public void changeItemBuyQuantity(String itemQuantity) {
    pendingLineItem.setBuyQuantity(returnAmount(itemQuantity).setScale(qtyScale));
    recalculateSellQuantity();
    updateItemDetailsRateView();
  }

  @Override
  public void changeItemSellQuantity(String itemQuantity) {
    pendingLineItem.setSellQuantity(returnAmount(itemQuantity).setScale(qtyScale));
    recalculateBuyQuantity();
    updateItemDetailsRateView();
  }

  @Override
  public void changeIntRate(String itemRate) {
    BigDecimal newIntRate = returnAmount(itemRate);
    if (newIntRate.compareTo(BigDecimal.ZERO) > 0) {
      pendingLineItem.setSellUnitPrice(
          pendingLineItem.getBuyUnitPrice().divide(newIntRate, rateScale, RoundingMode.HALF_UP));
      recalculateRates();
      recalculateSellQuantity();
    }
    updateItemDetailsRateView();
  }

  @Override
  public void changeInvIntRate(String itemRate) {
    BigDecimal newInvIntRate = inverse(returnAmount(itemRate));
    if (newInvIntRate.compareTo(BigDecimal.ZERO) > 0) {
      pendingLineItem.setSellUnitPrice(
          pendingLineItem.getBuyUnitPrice().divide(newInvIntRate, rateScale, RoundingMode.HALF_UP));
      recalculateRates();
      recalculateSellQuantity();
    }
    updateItemDetailsRateView();
  }

  @Override
  public void changeBuyRate(String itemRate) {
    BigDecimal newBuyRate = returnAmount(itemRate).setScale(rateScale);
    if (newBuyRate.compareTo(BigDecimal.ZERO) > 0) {
      pendingLineItem.setBuyUnitPrice(newBuyRate);
      recalculateRates();
      recalculateSellQuantity();
    }
    updateItemDetailsRateView();
  }

  @Override
  public void changeInvBuyRate(String itemRate) {
    BigDecimal newBuyRate = inverse(returnAmount(itemRate));
    if (newBuyRate.compareTo(BigDecimal.ZERO) > 0) {
      pendingLineItem.setBuyUnitPrice(newBuyRate);
      recalculateRates();
      recalculateSellQuantity();
    }
    updateItemDetailsRateView();
  }

  @Override
  public void changeSellRate(String itemRate) {
    BigDecimal newSellRate = returnAmount(itemRate).setScale(rateScale);
    if (newSellRate.compareTo(BigDecimal.ZERO) > 0) {
      pendingLineItem.setSellUnitPrice(newSellRate);
      recalculateRates();
      switch (pendingLineItem.getTransactionType()) {
        case SELL:
          recalculateBuyQuantity();
          break;
        default:
          recalculateSellQuantity();
      }
    }
    updateItemDetailsRateView();
  }

  @Override
  public void changeInvSellRate(String itemRate) {
    BigDecimal newSellRate = inverse(returnAmount(itemRate));
    if (newSellRate.compareTo(BigDecimal.ZERO) > 0) {
      pendingLineItem.setSellUnitPrice(newSellRate);
      recalculateRates();
      switch (pendingLineItem.getTransactionType()) {
        case SELL:
          recalculateBuyQuantity();
          break;
        default:
          recalculateSellQuantity();
      }
    }
    updateItemDetailsRateView();
  }

  @Override
  public void confirmChanges() {
    if (pendingLineItem.getBuyQuantity().compareTo(BigDecimal.ZERO) == 0) {
      return;
    }
    shiftToStep(Step.CONFIRM);
  }

  @Override
  public void removeLineItemIndex(int index) {
    lineItems.remove(index);
    if (lineItems.isEmpty()) {
      shiftToStep(Step.BUY);
    } else {
      getView().updateLineItems(lineItems);
    }
  }

  @Override
  public void modifyItem(int index) {
    pendingLineItem = lineItems.get(index);

    shiftToStep(Step.DETAILS);
  }

  private void reset() {
    lineItems = Lists.newArrayList();
    pendingLineItem = null;
    nextLine = 1;
    buyItemCode = "";
    screen = Screen.TRX;
    shiftToStep(Step.BUY);
    flipViews();
  }

  private void shiftToStep(Step nextStep) {
    switch (nextStep) {
      case BUY:
        step = Step.BUY;
        buyItemCode = "";
        pendingLineItem = null;
        getView().resetSelections();
        break;
      case SELL:
        step = Step.SELL;
        break;
      case DETAILS:
        step = Step.DETAILS;
        assert pendingLineItem != null : "Entered into Step.DETAILS with an empty pending line item";
        break;
      case CONFIRM:
        step = Step.CONFIRM;
        pendingLineItem = null;
        getView().updateLineItems(lineItems);
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

  private LineItemProxy createLineItem(ItemProxy itemToBuy, ItemProxy itemToSell) {
    LineItemRequest request = lineItemRequestProvider.get();
    LineItemProxy proxy = request.create(LineItemProxy.class);

    if (itemToBuy.getCode().equals(dftCurrCode)) {
      proxy.setTransactionType(TransactionType.SELL);
    } else if (itemToSell.getCode().equals(dftCurrCode)) {
      proxy.setTransactionType(TransactionType.BUY);
    } else {
      proxy.setTransactionType(TransactionType.BUYSELL);
    }

    proxy.setItemBuy(itemToBuy);
    SpotRateProxy itemToBuySpotRate = spotRateDataProvider.getSpotRateForCode(itemToBuy.getCode());
    proxy.setBuyUnitPrice(itemToBuySpotRate.getBidRate());
    proxy.setBuyQuantity(null);

    proxy.setItemSell(itemToSell);
    SpotRateProxy itemToSellSpotRate = spotRateDataProvider.getSpotRateForCode(itemToSell.getCode());
    proxy.setSellUnitPrice(itemToSellSpotRate.getAskRate());
    proxy.setSellQuantity(null);

    proxy.setLine(nextLine++);

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

    if (pendingLineItem != null && step.equals(Step.DETAILS))
      updateItemDetailsView();
  }

  private void updateItemDetailsView() {
    ItemProxy itemToBuy = pendingLineItem.getItemBuy();
    ItemProxy itemToSell = pendingLineItem.getItemSell();

    getView().setItemBuyCode(itemToBuy.getCode());
    getView().setItemBuyUomRate(itemToBuy.getUomRate().toString());
    getView().setItemBuyUom(itemToBuy.getUom().getName());
    getView().setItemImageL(itemToBuy.getImageUrl());

    getView().setItemSellCode(itemToSell.getCode());
    getView().setItemSellUom(itemToSell.getUom().getName());
    getView().setItemSellUomRate(itemToSell.getUomRate().toString());
    getView().setItemImageR(itemToSell.getImageUrl());

    getView().setDetailsTitle("Buy " + itemToBuy.getCode() + "/Sell " + itemToSell.getCode());

    getView().setR1Text(itemToBuy.getCode() + "/" + itemToSell.getCode());
    getView().setR2Text(itemToSell.getCode() + "/" + itemToBuy.getCode());
    getView().setR3Text(itemToBuy.getCode() + "/" + dftCurrCode);
    getView().setR4Text(dftCurrCode + "/" + itemToBuy.getCode());
    getView().setR5Text(itemToSell.getCode() + "/" + dftCurrCode);
    getView().setR6Text(dftCurrCode + "/" + itemToSell.getCode());

    updateItemDetailsRateView();
  }

  private void updateItemDetailsRateView() {
    getView().setR3(buyRate.toString());
    getView().setR4(inverse(buyRate).toString());
    getView().setR5(sellRate.toString());
    getView().setR6(inverse(sellRate).toString());

    getView().setR1(intRate.toString());
    getView().setR2(inverse(intRate).toString());

    getView().showRate1(true);
    getView().showRate2(true);
    getView().showRate3(true);
    getView().showRate4(true);
    getView().showRate5(true);
    getView().showRate6(true);

    switch (pendingLineItem.getTransactionType()) {
      case BUY:
        getView().showRate1(false);
        getView().showRate2(false);
        getView().showRate3(true);
        getView().showRate4(true);
        getView().showRate5(false);
        getView().showRate6(false);
        break;
      case SELL:
        getView().showRate1(false);
        getView().showRate2(false);
        getView().showRate3(false);
        getView().showRate4(false);
        getView().showRate5(true);
        getView().showRate6(true);
        break;
      case BUYSELL:
        getView().showRate1(true);
        getView().showRate2(true);
        getView().showRate3(true);
        getView().showRate4(true);
        getView().showRate5(true);
        getView().showRate6(true);
        break;
      default:
    }

    String buyQty = pendingLineItem.getBuyQuantity() == null ? "" : pendingLineItem.getBuyQuantity().toString();
    String sellQty = pendingLineItem.getSellQuantity() == null ? "" : pendingLineItem.getSellQuantity().toString();

    getView().setItemBuyQuantity(buyQty);
    getView().setItemSellQuantity(sellQty);
  }

  private void recalculateRates() {
    buyRate = pendingLineItem.getBuyUnitPrice().setScale(rateScale);
    sellRate = pendingLineItem.getSellUnitPrice().setScale(rateScale);
    intRate = buyRate.divide(sellRate, rateScale, RoundingMode.HALF_UP);
    switch (pendingLineItem.getTransactionType()) {
      case BUY:
        dealRate = buyRate;
        break;
      case SELL:
        dealRate = inverse(sellRate);
        break;
      case BUYSELL:
        dealRate = intRate;
        break;
      default:
    }
  }

  private void recalculateBuyQuantity() {
    if (pendingLineItem.getSellQuantity() != null) {
      pendingLineItem.setBuyQuantity(pendingLineItem.getSellQuantity().divide(dealRate, qtyScale, RoundingMode.HALF_UP));
    }
  }

  private void recalculateSellQuantity() {
    if (pendingLineItem.getBuyQuantity() != null) {
      BigDecimal sellQuantity = dealRate.multiply(
          pendingLineItem.getBuyQuantity(), MathContext.UNLIMITED)
          .setScale(qtyScale, RoundingMode.HALF_UP);
      pendingLineItem.setSellQuantity(sellQuantity);
    }
  }

  private BigDecimal inverse(BigDecimal number) {
    return BigDecimal.ONE.divide(number, rateScale, RoundingMode.HALF_UP);
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
