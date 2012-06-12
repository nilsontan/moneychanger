/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.presenter;

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
import com.techstudio.erp.moneychanger.client.ui.dataprovider.ClientDataProvider;
import com.techstudio.erp.moneychanger.client.ui.dataprovider.FirstLoad;
import com.techstudio.erp.moneychanger.client.ui.dataprovider.MyDataProvider;
import com.techstudio.erp.moneychanger.client.view.ClientUiHandlers;
import com.techstudio.erp.moneychanger.shared.proxy.ClientProxy;
import com.techstudio.erp.moneychanger.shared.service.ClientRequest;

import java.util.List;

/**
 * @author Nilson
 */
public class ClientPresenter
    extends Presenter<ClientPresenter.MyView, ClientPresenter.MyProxy>
    implements ClientUiHandlers {

  /**
   * {@link com.techstudio.erp.moneychanger.client.presenter.ClientPresenter}'s proxy.
   */
  @ProxyCodeSplit
  @NameToken(NameTokens.CLIENT_PAGE)
  public interface MyProxy extends ProxyPlace<ClientPresenter> {
  }

  public interface MyView extends View, HasUiHandlers<ClientUiHandlers> {
    HasData<ClientProxy> getListing();

    void showDetailPanel();

    void showListPanel();

    void setName(String name);

    void showLoading(boolean visible);
  }

  private final Provider<ClientRequest> requestProvider;
  private final MyDataProvider<ClientProxy> dataProvider;

  private String code;
  private String name;

  @Inject
  public ClientPresenter(final EventBus eventBus,
                         final MyView view,
                         final MyProxy proxy,
                         final Provider<ClientRequest> requestProvider,
                         final ClientDataProvider dataProvider) {
    super(eventBus, view, proxy);
    getView().setUiHandlers(this);
    getView().showLoading(true);
    this.requestProvider = requestProvider;
    this.dataProvider = dataProvider;
    this.dataProvider.firstLoad();
    this.dataProvider.addOnFirstLoadHandler(onFirstLoad);
    this.dataProvider.addDataDisplay(getView().getListing());
  }

  @Override
  protected void revealInParent() {
    RevealContentEvent.fire(this, MainPosPresenter.TYPE_SetMainContent, this);
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
  protected void onReset() {
    super.onReset();
    loadEntity();
    getView().showListPanel();
  }

  @Override
  public void setName(String name) {
    // name cannot be changed
    getView().setName((this.name));
  }

  @Override
  public void edit(Long code) {
    loadEntity();
    getView().showDetailPanel();
  }

  @Override
  public void create() {
    // Creation of Client is not allowed
  }

  @Override
  public void update() {
    if (!isFormValid()) {
      return;
    }
    requestProvider.get().fetchByProperty("code", code)
        .fire(new Receiver<List<ClientProxy>>() {
          @Override
          public void onSuccess(List<ClientProxy> response) {
            if (response.isEmpty()) {
              Window.alert("That code " + code + " does not exist!");
              getView().showListPanel();
            } else {
              ClientRequest request = requestProvider.get();
              ClientProxy proxy = response.get(0);
              proxy = request.edit(proxy);
              fillData(proxy);
              request.save(proxy)
                  .fire(new Receiver<ClientProxy>() {
                    @Override
                    public void onSuccess(ClientProxy response) {
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
    // Deletion of Client is not allowed
  }

  private void loadEntity() {
    if (code != null && !code.isEmpty()) {
      ClientProxy proxy = dataProvider.getByCode(code);
      if (proxy == null) {
        code = "";
        name = "";
      } else {
        code = proxy.getCode();
        name = proxy.getName();
      }
    } else {
      code = "";
      name = "";
    }
    updateView();
  }

  private void fillData(ClientProxy proxy) {
    proxy.setCode(code);
    proxy.setName(name);
  }

  private void updateView() {
    getView().setName(name);
  }

  private boolean isFormValid() {
    return !Strings.isNullOrEmpty(code)
        && !Strings.isNullOrEmpty(name);

  }
}
