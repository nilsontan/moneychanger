/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.pos.presenter;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.view.client.HasData;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.techstudio.erp.moneychanger.client.NameTokens;
import com.techstudio.erp.moneychanger.client.pos.view.PosUiHandlers;
import com.techstudio.erp.moneychanger.client.resources.Resources;
import com.techstudio.erp.moneychanger.client.ui.ExchangeRateDataProvider;
import com.techstudio.erp.moneychanger.shared.proxy.ExchangeRateProxy;
import com.techstudio.erp.moneychanger.shared.proxy.ItemProxy;

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
    HasData<ExchangeRateProxy> getXrTable();
    void showTxPanel(boolean visible);
    void showTxNew(boolean visible);
    void showTxAdd(boolean visible);
    void showTxDel(boolean visible);
    void showTxSav(boolean visible);
    void setStep(String step);
  }

  private final ExchangeRateDataProvider xrDataProvider;

  private Step step;
  private final Resources resources;
  //TODO:Nilson isTransacting correlated with step?
  private boolean isTransacting;
  private ItemProxy tempItemProxy;
  private ItemProxy buyItemProxy;
  private ItemProxy sellItemProxy;

  @Inject
  public PosPresenter(final EventBus eventBus,
                      final MyView view,
                      final MyProxy proxy,
                      final ExchangeRateDataProvider xrDataProvider,
                      final Resources resources) {
    super(eventBus, view, proxy);
    getView().setUiHandlers(this);
    this.xrDataProvider = xrDataProvider;
    this.xrDataProvider.addDataDisplay(getView().getXrTable());
    this.resources = resources;
  }

  @Override
  protected void revealInParent() {
    RevealContentEvent.fire(this, MainPosPresenter.TYPE_SetMainContent, this);
  }

  @Override
  protected void onReset() {
    super.onReset();
  }

  protected void onReveal() {
    step = Step.NONE;
    isTransacting = false;
    updateView();
  }

  @Override
  public void createNewTransaction() {
    Log.debug("Creating a new tx ...");
    step = Step.ITEMSELECT;
    isTransacting = true;
    updateView();
  }

  @Override
  public void addToTransaction() {
    Log.debug("Adding to tx ...");
    step = Step.ITEMSELECT;
    isTransacting = true;
    updateView();
  }

  @Override
  public void deleteTransaction() {
    Log.debug("Deleting tx ...");
    isTransacting = false;
    updateView();
  }

  @Override
  public void saveTransaction() {
    Log.debug("Saving tx ...");
    step = Step.NONE;
    isTransacting = false;
    updateView();
  }

  @Override
  public void itemSelected(String itemCode) {
    if (buyItemProxy == null && sellItemProxy == null) {
    }
  }

  private void updateView() {
    getView().showTxPanel(isTransacting);
    getView().showTxNew(!isTransacting);
    getView().showTxAdd(isTransacting);
    getView().showTxDel(isTransacting);
    getView().showTxSav(isTransacting);
    //TODO:Nilson merge guide into Step and add an accompanying image
    String guide = "";
    ImageResource imageResource = resources.iCatCur();
    switch (step) {
      case ITEMSELECT:
        guide = "Select Item";
        break;
      default:
    }
    getView().setStep(guide);
  }

  /**
   * Flow for a transaction
   * <ol>
   *  <li>Buy(Stock Coming In)</li>
   *  <li>Sell(Stock Going Out)</li>
   *  <li>Show the Rate and Ask to confirm</li>
   *  <li>Key in amount to </li>
   * </ol>
   */
  private enum Step {
    NONE,
    ITEMSELECT,
    SELL
  }
}
