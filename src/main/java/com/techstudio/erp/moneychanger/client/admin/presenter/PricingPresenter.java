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
import com.techstudio.erp.moneychanger.client.admin.view.PricingUiHandlers;
import com.techstudio.erp.moneychanger.client.gin.DefaultScaleForCosting;
import com.techstudio.erp.moneychanger.client.ui.dataprovider.FirstLoad;
import com.techstudio.erp.moneychanger.client.ui.dataprovider.MyDataProvider;
import com.techstudio.erp.moneychanger.client.ui.dataprovider.PricingDataProvider;
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

    void showDetailPanel();

    void showListPanel();

    void setPriceTitle(String code);

    void setPriceAsk(String askRate);

    void setPriceBid(String bidRate);

    void showLoading(boolean visible);
  }

  private final Provider<PricingRequest> requestProvider;
  private final MyDataProvider<PricingProxy> dataProvider;

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
                          final Provider<PricingRequest> requestProvider,
                          final PricingDataProvider dataProvider) {
    super(eventBus, view, proxy);
    getView().setUiHandlers(this);
    getView().showLoading(true);
    this.requestProvider = requestProvider;
    this.dataProvider = dataProvider;
    this.dataProvider.addOnFirstLoadHandler(onFirstLoad);
    this.dataProvider.addDataDisplay(getView().getListing());
  }

  FirstLoad.OnFirstLoad onFirstLoad = new FirstLoad.OnFirstLoad() {
    @Override
    public void onSuccess(FirstLoad firstLoad) {
      Timer timer = new Timer() {
        @Override
        public void run() {
          getView().showLoading(false);
        }
      };

      timer.schedule(1000);
    }
  };

  @Override
  protected void revealInParent() {
    RevealContentEvent.fire(this, MainPosPresenter.TYPE_SetMainContent, this);
  }

  @Override
  protected void onReset() {
    super.onReset();
    loadEntity();
    getView().showListPanel();
  }

  @Override
  protected void onReveal() {
    super.onReveal();
    loadEntity();
    getView().showListPanel();
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
    this.code = code.trim();
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
    requestProvider.get().fetchByProperty("code", code)
        .fire(new Receiver<List<PricingProxy>>() {
          @Override
          public void onSuccess(List<PricingProxy> response) {
            if (response.isEmpty()) {
              Window.alert("That code " + code + " does not exist!");
              getView().showListPanel();
            } else {
              PricingRequest request = requestProvider.get();
              PricingProxy proxy = response.get(0);
              proxy = request.edit(proxy);
              fillData(proxy);
              request.save(proxy)
                  .fire(new Receiver<PricingProxy>() {
                    @Override
                    public void onSuccess(PricingProxy response) {
                      dataProvider.updateData();
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

  private void loadEntity() {
    if (code != null && !code.isEmpty()) {
      PricingProxy pricingProxy = dataProvider.getByCode(code);
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