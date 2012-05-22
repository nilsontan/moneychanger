/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.admin.presenter;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.techstudio.erp.moneychanger.client.NameTokens;
import com.techstudio.erp.moneychanger.client.admin.view.PricingUiHandlers;
import com.techstudio.erp.moneychanger.client.gin.DefaultScaleForCosting;
import com.techstudio.erp.moneychanger.client.ui.PricingDataProvider;
import com.techstudio.erp.moneychanger.shared.proxy.PricingProxy;
import com.techstudio.erp.moneychanger.shared.service.PricingRequest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * @author Nilson
 */
public class PricingPresenter
    extends Presenter<PricingPresenter.MyView, PricingPresenter.MyProxy>
    implements PricingUiHandlers {

  @ProxyCodeSplit
  @NameToken(NameTokens.PRICING_PAGE)
  public interface MyProxy extends ProxyPlace<PricingPresenter> {
  }

  public interface MyView extends View, HasUiHandlers<PricingUiHandlers> {
    HasData<PricingProxy> getListing();

//    HasData<PricingProxy> getItemPriceListing();

    void showDetailPanel();

    void showListPanel();

    void setPriceTitle(String code);

    void setPriceAsk(String askRate);

    void setPriceBid(String bidRate);
  }

  private final Provider<PricingRequest> pricingRequestProvider;
  private final PricingDataProvider pricingDataProvider;

  private String code;
  private String name;
  private BigDecimal askRate;
  private BigDecimal bidRate;

  @DefaultScaleForCosting
  @Inject
  Integer scale;

  @Inject
  public PricingPresenter(final EventBus eventBus,
                          final MyView view,
                          final MyProxy proxy,
                          final Provider<PricingRequest> pricingRequestProvider,
                          final PricingDataProvider pricingDataProvider) {
    super(eventBus, view, proxy);
    getView().setUiHandlers(this);
    this.pricingRequestProvider = pricingRequestProvider;
    this.pricingDataProvider = pricingDataProvider;
    this.pricingDataProvider.addDataDisplay(getView().getListing());
  }

  @Override
  protected void revealInParent() {
    RevealContentEvent.fire(this, MainPosPresenter.TYPE_SetMainContent, this);
  }

  @Override
  protected void onReset() {
    super.onReset();
    loadEntity();
    RangeChangeEvent.fire(getView().getListing(), getView().getListing().getVisibleRange());
    getView().showListPanel();
  }

  private void loadEntity() {
    if (code != null && !code.isEmpty()) {
      PricingProxy pricingProxy = pricingDataProvider.getSpotRateForCode(code);
      if (pricingProxy == null) {
        code = "";
        name = "";
        bidRate = BigDecimal.ONE;
        askRate = BigDecimal.ONE;
      } else {
        code = pricingProxy.getCode();
        name = pricingProxy.getName();
        bidRate = pricingProxy.getBidRate();
        askRate = pricingProxy.getAskRate();
      }
    } else {
      code = "";
      name = "";
      bidRate = BigDecimal.ONE;
      askRate = BigDecimal.ONE;
    }
    updateView();
  }

  @Override
  protected void onReveal() {
    super.onReveal();
    loadEntity();
    getView().showListPanel();
  }

  @Override
  public void setPricingCode(final String code) {
    this.code = code.trim().toUpperCase();
    updateView();
  }

  @Override
  public void setPricingAskRate(String askRate) {
    this.askRate = returnRate(askRate);
    updateView();
  }

  @Override
  public void setPricingBidRate(String bidRate) {
    this.bidRate = returnRate(bidRate);
    updateView();
  }

  @Override
  public void edit(String code) {
    this.code = code;
    loadEntity();
    getView().showDetailPanel();
  }

  @Override
  public void create() {
    // Creation of Pricing is not allowed
  }

  @Override
  public void update() {
    if (!isFormValid()) {
      return;
    }
    pricingRequestProvider.get().fetchByProperty("code", code)
        .fire(new Receiver<List<PricingProxy>>() {
          @Override
          public void onSuccess(List<PricingProxy> response) {
            if (response.isEmpty()) {
              Window.alert("An exchange rate with that code does not exist!");
            } else {
              PricingRequest request = pricingRequestProvider.get();
              PricingProxy proxy = response.get(0);
              proxy = request.edit(proxy);
              fillData(proxy);
              request.save(proxy)
                  .fire(new Receiver<PricingProxy>() {
                    @Override
                    public void onSuccess(PricingProxy response) {
                      pricingDataProvider.updateData();
                      Timer timer = new Timer() {
                        @Override
                        public void run() {
                          getView().showListPanel();
                        }
                      };

                      timer.schedule(300);
                    }
                  });
            }
          }
        });
  }

  @Override
  public void delete() {
    // Deletion of Pricing is not allowed
  }

  @Override
  public void prepareFromRequest(PlaceRequest placeRequest) {
    code = placeRequest.getParameter("c", "");
  }

  private void fillData(PricingProxy proxy) {
    proxy.setCode(code);
    proxy.setName(name);
    proxy.setAskRate(askRate);
    proxy.setBidRate(bidRate);
  }

  private void updateView() {
    getView().setPriceTitle(getTitle());
    getView().setPriceAsk(askRate.toPlainString());
    getView().setPriceBid(bidRate.toPlainString());
  }

  private String getTitle() {
    return Joiner.on(" | ").join(code, name);
  }

  private boolean isFormValid() {
    return !Strings.isNullOrEmpty(code)
        && askRate.compareTo(BigDecimal.ZERO) > 0
        && bidRate.compareTo(BigDecimal.ZERO) > 0;

  }

  private BigDecimal returnRate(String rate) {
    BigDecimal bdRate = new BigDecimal(rate);
    return bdRate.setScale(scale, RoundingMode.HALF_UP);
  }
}