/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.admin.presenter;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.base.CharMatcher;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.Timer;
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
import com.techstudio.erp.moneychanger.client.admin.view.PosUiHandlers;
import com.techstudio.erp.moneychanger.client.gin.DefaultCurrency;
import com.techstudio.erp.moneychanger.client.gin.DefaultScaleForRate;
import com.techstudio.erp.moneychanger.client.ui.dataprovider.PricingDataProvider;
import com.techstudio.erp.moneychanger.shared.domain.TransactionType;
import com.techstudio.erp.moneychanger.shared.proxy.CategoryProxy;
import com.techstudio.erp.moneychanger.shared.proxy.ItemProxy;
import com.techstudio.erp.moneychanger.shared.proxy.LineItemProxy;
import com.techstudio.erp.moneychanger.shared.proxy.PricingProxy;
import com.techstudio.erp.moneychanger.shared.service.CategoryRequest;
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
   * {@link com.techstudio.erp.moneychanger.client.admin.presenter.PosPresenter}'s proxy.
   */
  @ProxyCodeSplit
  @NameToken(NameTokens.POS_PAGE)
  public interface MyProxy extends ProxyPlace<PosPresenter> {
  }

  public interface MyView extends View, HasUiHandlers<PosUiHandlers> {
    void showItemPanel(boolean visible);

    void showAmtPanel(boolean visible);

    void showRatePanel(boolean visible);

    void showIconHome(boolean visible);

    void showIconBack(boolean visible);

    void showIconMenu(boolean visible);

    void resetSelections();

    void setStep(String step);

    void addItemMenu(ItemProxy item);

    void setDetailsTitle(String title);

    void setItemBuyCode(String itemCode);

    void setItemSellCode(String itemCode);

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

    void showLoading(boolean visible);
  }

  private final Provider<ItemRequest> itemRequestProvider;
  private final Provider<CategoryRequest> categoryRequestProvider;
  private final Provider<LineItemRequest> lineItemRequestProvider;
  private final PricingDataProvider pricingDataProvider;

  private Step step;
  private List<LineItemProxy> lineItems;
  private LineItemProxy pendingLineItem;
  private int nextLine;

  private List<CategoryProxy> categories;
  private String buyItemCode;

  @Inject
  @DefaultCurrency
  private String dftCurrCode;

  @Inject
  @DefaultScaleForRate
  private int rateScale;

  private BigDecimal buyRate;
  private BigDecimal sellRate;
  private BigDecimal intRate;
  private BigDecimal dealRate;

  @Inject
  public PosPresenter(final EventBus eventBus,
                      final MyView view,
                      final MyProxy proxy,
                      final PricingDataProvider pricingDataProvider,
                      final Provider<ItemRequest> itemRequestProvider,
                      final Provider<CategoryRequest> categoryRequestProvider,
                      final Provider<LineItemRequest> lineItemRequestProvider) {
    super(eventBus, view, proxy);
    getView().setUiHandlers(this);
    getView().showLoading(true);
    this.itemRequestProvider = itemRequestProvider;
    this.categoryRequestProvider = categoryRequestProvider;
    this.lineItemRequestProvider = lineItemRequestProvider;
    this.pricingDataProvider = pricingDataProvider;
    this.pricingDataProvider.updateData();

    this.itemRequestProvider.get()
        .fetchAll()
        .fire(new Receiver<List<ItemProxy>>() {
          @Override
          public void onSuccess(List<ItemProxy> responses) {
            for (ItemProxy itemProxy : responses) {
              getView().addItemMenu(itemProxy);
            }

            PosPresenter.this.categoryRequestProvider.get()
                .fetchAll()
                .with(CategoryProxy.UOM)
                .fire(new Receiver<List<CategoryProxy>>() {
                  @Override
                  public void onSuccess(List<CategoryProxy> response) {
                    categories = response;

                    Timer timer = new Timer() {
                      @Override
                      public void run() {
                        getView().showLoading(false);
                      }
                    };

                    timer.schedule(5000);
                  }
                });
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
    super.onReveal();
    reset();
  }

  @Override
  public void onBack() {
    switch (step) {
      case BUY:
        shiftToStep(Step.CONFIRM);
        break;
      case DETAILS:
        lineItems.remove(pendingLineItem);
      case SELL:
        shiftToStep(Step.BUY);
        break;
    }
  }

  @Override
  public void selectCategories() {

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
            .with(ItemProxy.CATEGORY)
            .fire(new Receiver<List<ItemProxy>>() {
              @Override
              public void onSuccess(final List<ItemProxy> buyItem) {
                assert buyItem.size() > 0 : "No item found for the following code " + buyItemCode;
                itemRequestProvider.get()
                    .fetchByProperty("code", itemCode)
                    .with(ItemProxy.CATEGORY)
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
    if (itemQuantity == null || itemQuantity.trim().isEmpty()) {
      return;
    }
    BigDecimal itemQty = returnAmount(itemQuantity);
    int scale = findItemBuyCategory().getUom().getScale();
    pendingLineItem.setBuyQuantity(itemQty.setScale(scale, RoundingMode.HALF_UP));
    recalculateSellQuantity();
    updateItemDetailsRateView();
  }

  @Override
  public void changeItemSellQuantity(String itemQuantity) {
    if (itemQuantity == null || itemQuantity.trim().isEmpty()) {
      return;
    }
    BigDecimal itemQty = returnAmount(itemQuantity);
    int scale = findItemSellCategory().getUom().getScale();
    pendingLineItem.setSellQuantity(itemQty.setScale(scale, RoundingMode.HALF_UP));
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
    shiftToStep(Step.BUY);
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
    PricingProxy itemToBuyPricing = pricingDataProvider.getByCode(itemToBuy.getCode());
    proxy.setBuyUnitPrice(itemToBuyPricing.getBidRate());
    proxy.setBuyQuantity(BigDecimal.ZERO);

    proxy.setItemSell(itemToSell);
    PricingProxy itemToSellPricing = pricingDataProvider.getByCode(itemToSell.getCode());
    proxy.setSellUnitPrice(itemToSellPricing.getAskRate());
    proxy.setSellQuantity(BigDecimal.ZERO);

    proxy.setLine(nextLine++);

    return proxy;
  }

  private void updateView() {
    // 3 panels to hide/show : 1.TxPanel contains -> 2.ItemPanel 3.RatePanel
    if (step.itemSelecting) {
      getView().showItemPanel(step.itemSelecting);
      getView().showIconMenu(true);
      if (lineItems.isEmpty()) {
        getView().showIconHome(true);
        getView().showIconBack(false);
      } else {
        getView().showIconHome(false);
        getView().showIconBack(true);
      }
    } else if (step.amtEntering) {
      getView().showAmtPanel(step.amtEntering);
      getView().showIconMenu(false);
      getView().showIconHome(false);
      getView().showIconBack(true);
    } else {
      getView().showRatePanel(!(step.amtEntering || step.itemSelecting));
      getView().showIconMenu(false);
      getView().showIconHome(false);
      getView().showIconBack(false);
    }

    getView().setStep(step.inst);

    if (pendingLineItem != null && step.equals(Step.DETAILS))
      updateItemDetailsView();
  }

  private void updateItemDetailsView() {
    ItemProxy itemToBuy = pendingLineItem.getItemBuy();
    ItemProxy itemToSell = pendingLineItem.getItemSell();

    getView().setItemBuyCode(itemToBuy.getCode());
    getView().setItemSellCode(itemToSell.getCode());

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
      int scale = findItemBuyCategory().getUom().getScale();
      pendingLineItem.setBuyQuantity(pendingLineItem.getSellQuantity().divide(dealRate, scale, RoundingMode.HALF_UP));
    }
  }

  private void recalculateSellQuantity() {
    if (pendingLineItem.getBuyQuantity() != null) {
      int scale = findItemSellCategory().getUom().getScale();
      BigDecimal sellQuantity = dealRate.multiply(
          pendingLineItem.getBuyQuantity(), MathContext.UNLIMITED)
          .setScale(scale, RoundingMode.HALF_UP);
      pendingLineItem.setSellQuantity(sellQuantity);
    }
  }

  private BigDecimal inverse(BigDecimal number) {
    return BigDecimal.ONE.divide(number, rateScale, RoundingMode.HALF_UP);
  }

  private CategoryProxy findItemBuyCategory() {
    return findCategory(pendingLineItem.getItemBuy().getCategory().getCode());
  }

  private CategoryProxy findItemSellCategory() {
    return findCategory(pendingLineItem.getItemSell().getCategory().getCode());
  }

  private CategoryProxy findCategory(String code) {
    for (CategoryProxy proxy : categories) {
      if (proxy.getCode().equals(code)) {
        return proxy;
      }
    }
    throw new RuntimeException("No Category found for this code: " + code);
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
}
