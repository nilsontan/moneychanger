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
import com.techstudio.erp.moneychanger.client.admin.view.ExchangeRateUiHandlers;
import com.techstudio.erp.moneychanger.client.ui.CurrencyDataProvider;
import com.techstudio.erp.moneychanger.client.ui.ExchangeRateDataProvider;
import com.techstudio.erp.moneychanger.client.ui.HasSelectedValue;
import com.techstudio.erp.moneychanger.shared.proxy.CurrencyProxy;
import com.techstudio.erp.moneychanger.shared.proxy.ExchangeRateProxy;
import com.techstudio.erp.moneychanger.shared.service.ExchangeRateRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nilson
 */
public class ExchangeRatePresenter
    extends Presenter<ExchangeRatePresenter.MyView, ExchangeRatePresenter.MyProxy>
    implements ExchangeRateUiHandlers {

  @ProxyCodeSplit
  @NameToken(NameTokens.EXCHANGE_RATE_PAGE)
  public interface MyProxy extends ProxyPlace<ExchangeRatePresenter> {
  }

  public interface MyView extends View, HasUiHandlers<ExchangeRateUiHandlers> {
    HasData<ExchangeRateProxy> getXrTable();

    HasSelectedValue<CurrencyProxy> getCurrencyList();

    void enableCreateButton(boolean isValid);

    void enableUpdateButton(boolean isValid);

    void setXrCode(String code);

    void setXrName(String name);

    void setXrCurrency(CurrencyProxy currencyProxy);

    void setXrUnit(String unit);

    void setXrAsk(String askRate);

    void setXrBid(String bidRate);
  }

  private final Provider<ExchangeRateRequest> xrRequestProvider;
  private final ExchangeRateDataProvider itemDataProvider;
  private final CurrencyDataProvider currencyDataProvider;

  private Long id;
  private String code;
  private String name;
  private CurrencyProxy currency;
  private Integer unit;
  private String askRate;
  private String bidRate;

  @Inject
  public ExchangeRatePresenter(final EventBus eventBus,
                               final MyView view,
                               final MyProxy proxy,
                               final Provider<ExchangeRateRequest> xrRequestProvider,
                               final ExchangeRateDataProvider itemDataProvider,
                               final CurrencyDataProvider currencyDataProvider) {
    super(eventBus, view, proxy);
    getView().setUiHandlers(this);
    this.xrRequestProvider = xrRequestProvider;
    this.itemDataProvider = itemDataProvider;
    this.itemDataProvider.addDataDisplay(getView().getXrTable());
    this.currencyDataProvider = currencyDataProvider;
    this.currencyDataProvider.addDataListDisplay(getView().getCurrencyList());
  }

  @Override
  protected void revealInParent() {
    RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetMainContent, this);
  }

  @Override
  protected void onReset() {
    super.onReset();

    if (id != null) {
      xrRequestProvider.get().fetch(id)
          .fire(new Receiver<ExchangeRateProxy>() {
            @Override
            public void onSuccess(ExchangeRateProxy response) {
              code = response.getCode();
              name = response.getName();
              currency = response.getCurrency();
              unit = response.getUnit();
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
    currencyDataProvider.updateListData();
    code = "";
    name = "";
    currency = currencyDataProvider.getDefaultCurrency();
    unit = 1;
    askRate = returnRate("1");
    bidRate = returnRate("1");
    updateView();
  }

  @Override
  public void setXrCode(final String code) {
    this.code = code.trim().toUpperCase();
    updateView();
  }

  @Override
  public void setXrName(final String name) {
    this.name = name.trim();
    updateView();
  }

  @Override
  public void setXrCurrency(final CurrencyProxy currencyProxy) {
    this.currency = currencyProxy;
    updateView();
  }

  @Override
  public void setXrUnit(Integer unit) {
    this.unit = unit;
  }

  @Override
  public void setXrAskRate(String askRate) {
    this.askRate = returnRate(askRate);
    updateView();
  }

  @Override
  public void setXrBidRate(String bidRate) {
    this.bidRate = returnRate(bidRate);
    updateView();
  }

  @Override
  public void createXr() {
    if (!isFormValid()) {
      return;
    }
    xrRequestProvider.get().fetchByProperty("code", code)
        .fire(new Receiver<List<ExchangeRateProxy>>() {
          @Override
          public void onSuccess(List<ExchangeRateProxy> response) {
            if (response.isEmpty()) {
              ExchangeRateRequest request = xrRequestProvider.get();
              ExchangeRateProxy proxy = request.create(ExchangeRateProxy.class);
              fillData(proxy);
              request.save(proxy)
                  .fire(new Receiver<ExchangeRateProxy>() {
                    @Override
                    public void onSuccess(ExchangeRateProxy response) {
                      itemDataProvider.updateTableData();
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
  public void updateXr() {
    if (!isFormValid()) {
      return;
    }
    xrRequestProvider.get().fetchByProperty("code", code)
        .fire(new Receiver<List<ExchangeRateProxy>>() {
          @Override
          public void onSuccess(List<ExchangeRateProxy> response) {
            if (response.isEmpty()) {
              Window.alert("An exchange rate with that code does not exist!");
            } else {
              ExchangeRateRequest request = xrRequestProvider.get();
              ExchangeRateProxy proxy = response.get(0);
              proxy = request.edit(proxy);
              fillData(proxy);
              request.save(proxy)
                  .fire(new Receiver<ExchangeRateProxy>() {
                    @Override
                    public void onSuccess(ExchangeRateProxy response) {
                      itemDataProvider.updateTableData();
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

  private void fillData(ExchangeRateProxy proxy) {
    proxy.setCode(code);
    proxy.setName(name);
    proxy.setCurrency(currency);
    proxy.setAskRate(askRate);
    proxy.setBidRate(bidRate);
  }

  private void updateView() {
    getView().setXrCode(code);
    getView().setXrName(name);
    getView().setXrCurrency(currency);
    getView().setXrUnit(unit.toString());
    getView().setXrAsk(askRate);
    getView().setXrBid(bidRate);
    boolean isValid = isFormValid();
    getView().enableCreateButton(isValid);
    getView().enableUpdateButton(isValid);
  }

  private boolean isFormValid() {
    return !Strings.isNullOrEmpty(code)
        && !Strings.isNullOrEmpty(name)
        && currency != null
        && unit > 0
        && !Strings.isNullOrEmpty(askRate)
        && !Strings.isNullOrEmpty(bidRate);

  }

  private String returnRate(String rate) {
    ArrayList<String> list = Lists.newArrayList(Splitter.on(".").split(rate));
    String decimals = "0000";
    if (list.size() == 2) {
      decimals = Strings.padEnd(list.get(1), 4, '0');
    }
    return Joiner.on(".").join(list.get(0), decimals);
  }
}