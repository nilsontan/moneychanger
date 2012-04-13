/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.admin.presenter;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
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
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.techstudio.erp.moneychanger.client.NameTokens;
import com.techstudio.erp.moneychanger.client.admin.view.SpotRateUiHandlers;
import com.techstudio.erp.moneychanger.client.gin.DefaultScaleForCosting;
import com.techstudio.erp.moneychanger.client.ui.SpotRateDataProvider;
import com.techstudio.erp.moneychanger.shared.proxy.SpotRateProxy;
import com.techstudio.erp.moneychanger.shared.service.SpotRateRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Nilson
 */
public class SpotRatePresenter
    extends Presenter<SpotRatePresenter.MyView, SpotRatePresenter.MyProxy>
    implements SpotRateUiHandlers {

  @ProxyCodeSplit
  @NameToken(NameTokens.EXCHANGE_RATE_PAGE)
  public interface MyProxy extends ProxyPlace<SpotRatePresenter> {
  }

  public interface MyView extends View, HasUiHandlers<SpotRateUiHandlers> {
    HasData<SpotRateProxy> getSpotRateTable();

    void enableCreateButton(boolean isValid);

    void enableUpdateButton(boolean isValid);

    void setSpotRateCode(String code);

    void setSpotRateName(String name);

    void setSpotRateAsk(String askRate);

    void setSpotRateBid(String bidRate);
  }

  private final Provider<SpotRateRequest> spotRateRequestProvider;
  private final SpotRateDataProvider spotRateDataProvider;

  private Long id;
  private String code;
  private String name;
  private BigDecimal askRate;
  private BigDecimal bidRate;

  @DefaultScaleForCosting
  @Inject
  Integer scale;

  @Inject
  public SpotRatePresenter(final EventBus eventBus,
                           final MyView view,
                           final MyProxy proxy,
                           final Provider<SpotRateRequest> spotRateRequestProvider,
                           final SpotRateDataProvider spotRateDataProvider) {
    super(eventBus, view, proxy);
    getView().setUiHandlers(this);
    this.spotRateRequestProvider = spotRateRequestProvider;
    this.spotRateDataProvider = spotRateDataProvider;
    this.spotRateDataProvider.addDataDisplay(getView().getSpotRateTable());
  }

  @Override
  protected void revealInParent() {
    RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetMainContent, this);
  }

  @Override
  protected void onReset() {
    super.onReset();

    if (id != null) {
      spotRateRequestProvider.get().fetch(id)
          .fire(new Receiver<SpotRateProxy>() {
            @Override
            public void onSuccess(SpotRateProxy response) {
              code = response.getCode();
              name = response.getName();
              askRate = response.getAskRate();
              bidRate = response.getBidRate();
              updateView();
            }
          });
    }
  }

  @Override
  protected void onReveal() {
    super.onReveal();
    code = "";
    name = "";
    askRate = BigDecimal.ONE;
    bidRate = BigDecimal.ONE;
    updateView();
  }

  @Override
  public void setSpotRateCode(final String code) {
    this.code = code.trim().toUpperCase();
    updateView();
  }

  @Override
  public void setSpotRateName(final String name) {
    this.name = name.trim();
    updateView();
  }

  @Override
  public void setSpotRateAskRate(String askRate) {
    this.askRate = returnRate(askRate);
    updateView();
  }

  @Override
  public void setSpotRateBidRate(String bidRate) {
    this.bidRate = returnRate(bidRate);
    updateView();
  }

  @Override
  public void createSpotRate() {
    if (!isFormValid()) {
      return;
    }
    spotRateRequestProvider.get().fetchByProperty("code", code)
        .fire(new Receiver<List<SpotRateProxy>>() {
          @Override
          public void onSuccess(List<SpotRateProxy> response) {
            if (response.isEmpty()) {
              SpotRateRequest request = spotRateRequestProvider.get();
              SpotRateProxy proxy = request.create(SpotRateProxy.class);
              fillData(proxy);
              request.save(proxy)
                  .fire(new Receiver<SpotRateProxy>() {
                    @Override
                    public void onSuccess(SpotRateProxy response) {
                      spotRateDataProvider.updateTableData();
                      updateView();
                    }
                  });
            } else {
              Window.alert("An exchange rate with that code already exist!");
            }
          }
        });
  }

  @Override
  public void updateSpotRate() {
    if (!isFormValid()) {
      return;
    }
    spotRateRequestProvider.get().fetchByProperty("code", code)
        .fire(new Receiver<List<SpotRateProxy>>() {
          @Override
          public void onSuccess(List<SpotRateProxy> response) {
            if (response.isEmpty()) {
              Window.alert("An exchange rate with that code does not exist!");
            } else {
              SpotRateRequest request = spotRateRequestProvider.get();
              SpotRateProxy proxy = response.get(0);
              proxy = request.edit(proxy);
              fillData(proxy);
              request.save(proxy)
                  .fire(new Receiver<SpotRateProxy>() {
                    @Override
                    public void onSuccess(SpotRateProxy response) {
                      spotRateDataProvider.updateTableData();
                      updateView();
                    }
                  });
            }
          }
        });
  }

  @Override
  public void prepareFromRequest(PlaceRequest placeRequest) {
    String idString = placeRequest.getParameter("id", "");
    try {
      id = Long.parseLong(idString);
    } catch (NumberFormatException e) {
      id = null;
    }
  }

  private void fillData(SpotRateProxy proxy) {
    proxy.setCode(code);
    proxy.setName(name);
    proxy.setAskRate(askRate);
    proxy.setBidRate(bidRate);
  }

  private void updateView() {
    getView().setSpotRateCode(code);
    getView().setSpotRateName(name);
    getView().setSpotRateAsk(askRate.toPlainString());
    getView().setSpotRateBid(bidRate.toPlainString());
    boolean isValid = isFormValid();
    getView().enableCreateButton(isValid);
    getView().enableUpdateButton(isValid);
  }

  private boolean isFormValid() {
    return !Strings.isNullOrEmpty(code)
        && !Strings.isNullOrEmpty(name);
//        && !Strings.isNullOrEmpty(askRate)
//        && !Strings.isNullOrEmpty(bidRate);

  }

  private BigDecimal returnRate(String rate) {
    ArrayList<String> list = Lists.newArrayList(Splitter.on(".").split(rate));
//    String decimals = "0000";
//    if (list.size() == 2) {
//      decimals = Strings.padEnd(list.get(1), scale, '0');
//    }
//    return Joiner.on(".").join(list.get(0), decimals);
    BigDecimal bdRate = new BigDecimal(rate);
    bdRate.setScale(scale);
    return bdRate;
  }
}