/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.presenter;

import com.allen_sauer.gwt.log.client.Log;
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
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.techstudio.erp.moneychanger.client.NameTokens;
import com.techstudio.erp.moneychanger.client.view.CountryUiHandlers;
import com.techstudio.erp.moneychanger.client.ui.dataprovider.CountryDataProvider;
import com.techstudio.erp.moneychanger.client.ui.dataprovider.FirstLoad;
import com.techstudio.erp.moneychanger.client.ui.dataprovider.MyDataProvider;
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
   * {@link com.techstudio.erp.moneychanger.client.presenter.CountryPresenter}'s proxy.
   */
  @ProxyCodeSplit
  @NameToken(NameTokens.COUNTRY_PAGE)
  public interface MyProxy extends ProxyPlace<CountryPresenter> {
  }

  public interface MyView extends View, HasUiHandlers<CountryUiHandlers> {
    HasData<CountryProxy> getListing();

    void showListPanel();

    void showDetailPanel();

    void showAddButtons();

    void showEditButtons();

    void setCode(String code);

    void setName(String name);

    void setFullname(String fullname);

    void showLoading(boolean visible);
  }

  private final Provider<CountryRequest> requestProvider;
  private final MyDataProvider<CountryProxy> dataProvider;

  private String code;
  private String name;
  private String fullName;
  private Step step;

  @Inject
  public CountryPresenter(final EventBus eventBus,
                          final MyView view,
                          final MyProxy proxy,
                          final Provider<CountryRequest> requestProvider,
                          final CountryDataProvider dataProvider) {
    super(eventBus, view, proxy);
    getView().setUiHandlers(this);
    this.requestProvider = requestProvider;
    this.dataProvider = dataProvider;
    this.dataProvider.addOnFirstLoadHandler(onFirstLoad);
    getView().showLoading(true);
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
    reset();
  }

  @Override
  protected void onReveal() {
    reset();
  }

  @Override
  public void onBack() {
    step = Step.LIST;
    updateView();
  }

  @Override
  public void onNext() {
    step = Step.ADD;
    resetFields();
    updateView();
  }

  @Override
  public void setCode(String code) {
    this.code = code.trim().toUpperCase();
    getView().setCode(this.code);
  }

  @Override
  public void setName(String name) {
    this.name = name.trim();
    getView().setName(this.name);
  }

  @Override
  public void setFullName(String fullName) {
    this.fullName = fullName.trim();
    getView().setFullname(this.fullName);
  }

  @Override
  public void edit(String code) {
    this.code = code;
    step = Step.EDIT;
    loadEntity();
    updateView();
  }

  @Override
  public void create() {
    if (!isFormValid()) {
      return;
    }
    requestProvider.get().fetchByProperty("code", code)
        .fire(new Receiver<List<CountryProxy>>() {
          @Override
          public void onSuccess(List<CountryProxy> response) {
            if (response.isEmpty()) {
              CountryRequest request = requestProvider.get();
              CountryProxy proxy = request.create(CountryProxy.class);
              fillData(proxy);
              request.save(proxy).fire(new Receiver<CountryProxy>() {
                @Override
                public void onSuccess(CountryProxy response) {
                  dataProvider.updateData();
                  step = Step.LIST;
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
  public void update() {
    if (!isFormValid()) {
      return;
    }

    requestProvider.get().fetchByProperty("code", code)
        .fire(new Receiver<List<CountryProxy>>() {
          @Override
          public void onSuccess(List<CountryProxy> response) {
            if (response.isEmpty()) {
              Window.alert("The code \"" + code + "\" does not exist!");
              step = Step.LIST;
              updateView();
            } else {
              CountryRequest request = requestProvider.get();
              CountryProxy proxy = response.get(0);
              proxy = request.edit(proxy);
              fillData(proxy);
              request.save(proxy).fire(new Receiver<CountryProxy>() {
                @Override
                public void onSuccess(CountryProxy response) {
                  dataProvider.updateData();
                  Timer timer = new Timer() {
                    @Override
                    public void run() {
                      step = Step.LIST;
                      updateView();
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
    if (!step.equals(Step.EDIT)) {
      return;
    }

    requestProvider.get()
        .fetchByProperty("code", code)
        .fire(new Receiver<List<CountryProxy>>() {
          @Override
          public void onSuccess(List<CountryProxy> response) {
            if (response.isEmpty()) {
              Window.alert("The code \"" + code + "\" does not exist!");
              step = Step.LIST;
              updateView();
            } else {
              CountryProxy proxy = response.get(0);
              requestProvider.get()
                  .purge(proxy)
                  .fire(new Receiver<Void>() {
                    @Override
                    public void onSuccess(Void response) {
                      dataProvider.updateData();
                      step = Step.LIST;
                      updateView();
                    }
                  });
            }
          }
        });
  }

  private void reset() {
    resetFields();
    step = Step.LIST;
    RangeChangeEvent.fire(getView().getListing(), getView().getListing().getVisibleRange());
    loadEntity();
    updateView();
  }

  private void resetFields() {
    code = "";
    name = "";
    fullName = "";
  }

  private void loadEntity() {
    if (code != null && !code.isEmpty()) {
      CountryProxy proxy = dataProvider.getByCode(code);
      if (proxy == null) {
        resetFields();
        Log.error("Code not found: " + code);
        onBack();
      } else {
        code = proxy.getCode();
        name = proxy.getName();
        fullName = proxy.getFullName();
      }
    } else {
      resetFields();
    }
  }

  private void fillData(CountryProxy proxy) {
    proxy.setCode(code);
    proxy.setName(name);
    proxy.setFullName(fullName);
  }

  private void updateView() {
    getView().setCode(code);
    getView().setName(name);
    getView().setFullname(fullName);
    switch (step) {
      case LIST:
        getView().showListPanel();
        break;
      case ADD:
        getView().showDetailPanel();
        getView().showAddButtons();
        break;
      case EDIT:
        getView().showDetailPanel();
        getView().showEditButtons();
        break;
    }
  }

  private boolean isFormValid() {
    return !Strings.isNullOrEmpty(code)
        && !Strings.isNullOrEmpty(name)
        && !Strings.isNullOrEmpty(fullName);
  }

  private enum Step {
    LIST, ADD, EDIT
  }
}
