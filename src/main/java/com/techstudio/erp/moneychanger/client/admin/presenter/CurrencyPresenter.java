/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.admin.presenter;

import com.google.common.base.Strings;
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
import com.techstudio.erp.moneychanger.client.admin.view.CurrencyUiHandlers;
import com.techstudio.erp.moneychanger.client.ui.CurrencyDataProvider;
import com.techstudio.erp.moneychanger.client.util.TestData;
import com.techstudio.erp.moneychanger.shared.proxy.CurrencyProxy;
import com.techstudio.erp.moneychanger.shared.service.CurrencyRequest;

import java.util.List;

/**
 * @author Nilson
 */
public class CurrencyPresenter
    extends Presenter<CurrencyPresenter.MyView, CurrencyPresenter.MyProxy>
    implements CurrencyUiHandlers {

  /**
   * {@link com.techstudio.erp.moneychanger.client.admin.presenter.CurrencyPresenter}'s proxy.
   */
  @ProxyCodeSplit
  @NameToken(NameTokens.CURRENCY_PAGE)
  public interface MyProxy extends ProxyPlace<CurrencyPresenter> {
  }

  public interface MyView extends View, HasUiHandlers<CurrencyUiHandlers> {
    HasData<CurrencyProxy> getTable();

    void enableCreateButton(boolean isFilled);

    void enableUpdateButton(boolean isFilled);

    void setCurrencyCode(String code);

    void setCurrencyName(String name);
  }

  private final Provider<CurrencyRequest> currencyRequestProvider;
  private final CurrencyDataProvider currencyDataProvider;

  private Long id;
  private String code;
  private String name;

  @Inject
  public CurrencyPresenter(final EventBus eventBus,
                           final MyView view,
                           final MyProxy proxy,
                           final Provider<CurrencyRequest> currencyRequestProvider,
                           final CurrencyDataProvider currencyDataProvider) {
    super(eventBus, view, proxy);
    getView().setUiHandlers(this);
    this.currencyRequestProvider = currencyRequestProvider;
    this.currencyDataProvider = currencyDataProvider;
    this.currencyDataProvider.addDataDisplay(getView().getTable());
  }

  @Override
  protected void revealInParent() {
    RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetMainContent, this);
  }

  @Override
  protected void onReset() {
    super.onReset();

    if (id == null) {
      updateView();
    } else {
      currencyRequestProvider.get().fetch(id)
          .fire(new Receiver<CurrencyProxy>() {
            @Override
            public void onSuccess(CurrencyProxy response) {
              currencyDataProvider.setCurrency(response);
              code = response.getCode();
              name = response.getName();
              updateView();
            }
          });
    }
  }

  @Override
  public void setCurrencyCode(String code) {
    this.code = code.trim().toUpperCase();
    updateView();
  }

  @Override
  public void setCurrencyName(String name) {
    this.name = name.trim();
    updateView();
  }

  @Override
  public void createCurrency() {
    if (!formIsValidated()) {
      return;
    }
    currencyRequestProvider.get().fetchByProperty("code", code)
        .fire(new Receiver<List<CurrencyProxy>>() {
          @Override
          public void onSuccess(List<CurrencyProxy> response) {
            if (response.isEmpty()) {
              CurrencyRequest request = currencyRequestProvider.get();
              CurrencyProxy proxy = request.create(CurrencyProxy.class);
              fillData(proxy);
              request.save(proxy).fire(new Receiver<CurrencyProxy>() {
                @Override
                public void onSuccess(CurrencyProxy response) {
                  currencyDataProvider.updateAllData();
                  updateView();
                }
              });
            } else {
              Window.alert("A currency with that code already exist!");
            }
          }
        });
  }

  @Override
  public void updateCurrency() {
    if (!formIsValidated()) {
      return;
    }
    currencyRequestProvider.get().fetchByProperty("code", code)
        .fire(new Receiver<List<CurrencyProxy>>() {
          @Override
          public void onSuccess(List<CurrencyProxy> response) {
            if (response.isEmpty()) {
              Window.alert("A currency with that code does not exist!");
            } else {
              CurrencyRequest request = currencyRequestProvider.get();
              CurrencyProxy proxy = response.get(0);
              proxy = request.edit(proxy);
              fillData(proxy);
              request.save(proxy).fire(new Receiver<CurrencyProxy>() {
                @Override
                public void onSuccess(CurrencyProxy response) {
                  currencyDataProvider.updateAllData();
                  updateView();
                }
              });
            }
          }
        });
  }

  @Override
  public void repopulateData() {
    TestData testData = new TestData();
    testData.repopulateCurrencies(currencyRequestProvider, currencyDataProvider);
  }

  private void fillData(CurrencyProxy proxy) {
    proxy.setCode(code);
    proxy.setName(name);
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

  private void updateView() {
    getView().setCurrencyCode(code);
    getView().setCurrencyName(name);
    boolean isFilled = formIsValidated();
    getView().enableCreateButton(isFilled);
    getView().enableUpdateButton(isFilled);
  }

  private boolean formIsValidated() {
    return !Strings.isNullOrEmpty(code)
        && !Strings.isNullOrEmpty(name);
  }
}
