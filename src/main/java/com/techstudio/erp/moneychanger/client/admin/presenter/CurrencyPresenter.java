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
import com.techstudio.erp.moneychanger.client.ui.dataprovider.CurrencyDataProvider;
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

    void enableCreateButton(boolean isValid);

    void enableUpdateButton(boolean isValid);

    void setCurrencyCode(String code);

    void setCurrencyName(String name);

    void setCurrencyFullName(String fullName);
  }

  private final Provider<CurrencyRequest> currencyRequestProvider;
  private final CurrencyDataProvider currencyDataProvider;

  private Long id;
  private String code;
  private String name;
  private String fullName;

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
    RevealContentEvent.fire(this, MainPosPresenter.TYPE_SetMainContent, this);
  }

  @Override
  protected void onReset() {
    super.onReset();

    if (id != null) {
      currencyRequestProvider.get().fetch(id)
          .fire(new Receiver<CurrencyProxy>() {
            @Override
            public void onSuccess(CurrencyProxy response) {
              code = response.getCode();
              name = response.getName();
              fullName = response.getFullName();
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
    fullName = "";
    updateView();
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
  public void setCurrencyFullName(String fullName) {
    this.fullName = fullName.trim();
    updateView();
  }

  @Override
  public void createCurrency() {
    if (!isFormValid()) {
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
    if (!isFormValid()) {
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
  public void prepareFromRequest(PlaceRequest placeRequest) {
    String idString = placeRequest.getParameter("id", "");
    try {
      id = Long.parseLong(idString);
    } catch (NumberFormatException e) {
      id = null;
    }
  }

  private void fillData(CurrencyProxy proxy) {
    proxy.setCode(code);
    proxy.setName(name);
    proxy.setFullName(fullName);
  }

  private void updateView() {
    getView().setCurrencyCode(code);
    getView().setCurrencyName(name);
    getView().setCurrencyFullName(fullName);
    boolean isValid = isFormValid();
    getView().enableCreateButton(isValid);
    getView().enableUpdateButton(isValid);
  }

  private boolean isFormValid() {
    return !Strings.isNullOrEmpty(code)
        && !Strings.isNullOrEmpty(name)
        && !Strings.isNullOrEmpty(fullName);
  }
}
