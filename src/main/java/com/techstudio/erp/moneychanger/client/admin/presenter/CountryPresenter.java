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
import com.techstudio.erp.moneychanger.client.admin.view.CountryUiHandlers;
import com.techstudio.erp.moneychanger.client.ui.CountryDataProvider;
import com.techstudio.erp.moneychanger.shared.proxy.CountryProxy;
import com.techstudio.erp.moneychanger.shared.service.CountryRequest;

import java.util.List;

/**
 * @author Nilson
 */
public class CountryPresenter
    extends Presenter<CountryPresenter.MyView, CountryPresenter.MyProxy>
    implements CountryUiHandlers {

  /**
   * {@link com.techstudio.erp.moneychanger.client.admin.presenter.CountryPresenter}'s proxy.
   */
  @ProxyCodeSplit
  @NameToken(NameTokens.COUNTRY_PAGE)
  public interface MyProxy extends ProxyPlace<CountryPresenter> {
  }

  public interface MyView extends View, HasUiHandlers<CountryUiHandlers> {
    HasData<CountryProxy> getTable();

    void enableCreateButton(boolean isValid);

    void enableUpdateButton(boolean isValid);

    void setCountryCode(String code);

    void setCountryName(String name);

    void setCountryFullName(String fullName);
  }

  private final Provider<CountryRequest> countryRequestProvider;
  private final CountryDataProvider countryDataProvider;

  private Long id;
  private String code;
  private String name;
  private String fullName;

  @Inject
  public CountryPresenter(final EventBus eventBus,
                          final MyView view,
                          final MyProxy proxy,
                          final Provider<CountryRequest> countryRequestProvider,
                          final CountryDataProvider countryDataProvider) {
    super(eventBus, view, proxy);
    getView().setUiHandlers(this);
    this.countryRequestProvider = countryRequestProvider;
    this.countryDataProvider = countryDataProvider;
    this.countryDataProvider.addDataDisplay(getView().getTable());
  }

  @Override
  protected void revealInParent() {
    RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetMainContent, this);
  }

  @Override
  protected void onReset() {
    super.onReset();

    if (id != null) {
      countryRequestProvider.get().fetch(id)
          .fire(new Receiver<CountryProxy>() {
            @Override
            public void onSuccess(CountryProxy response) {
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
  public void setCountryCode(String code) {
    this.code = code.trim().toUpperCase();
    updateView();
  }

  @Override
  public void setCountryName(String name) {
    this.name = name.trim();
    updateView();
  }

  @Override
  public void setCountryFullName(String fullName) {
    this.fullName = fullName.trim();
    updateView();
  }

  @Override
  public void createCountry() {
    if (!isFormValid()) {
      return;
    }
    countryRequestProvider.get().fetchByProperty("code", code)
        .fire(new Receiver<List<CountryProxy>>() {
          @Override
          public void onSuccess(List<CountryProxy> response) {
            if (response.isEmpty()) {
              CountryRequest request = countryRequestProvider.get();
              CountryProxy proxy = request.create(CountryProxy.class);
              fillData(proxy);
              request.save(proxy).fire(new Receiver<CountryProxy>() {
                @Override
                public void onSuccess(CountryProxy response) {
                  countryDataProvider.updateAllData();
                  updateView();
                }
              });
            } else {
              Window.alert("A country with that code already exist!");
            }
          }
        });
  }

  @Override
  public void updateCountry() {
    if (!isFormValid()) {
      return;
    }
    countryRequestProvider.get().fetchByProperty("code", code)
        .fire(new Receiver<List<CountryProxy>>() {
          @Override
          public void onSuccess(List<CountryProxy> response) {
            if (response.isEmpty()) {
              Window.alert("A country with that code does not exist!");
            } else {
              CountryRequest request = countryRequestProvider.get();
              CountryProxy proxy = response.get(0);
              proxy = request.edit(proxy);
              fillData(proxy);
              request.save(proxy).fire(new Receiver<CountryProxy>() {
                @Override
                public void onSuccess(CountryProxy response) {
                  countryDataProvider.updateAllData();
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

  private void fillData(CountryProxy proxy) {
    proxy.setCode(code);
    proxy.setName(name);
    proxy.setFullName(fullName);
  }

  private void updateView() {
    getView().setCountryCode(code);
    getView().setCountryName(name);
    getView().setCountryFullName(fullName);
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
