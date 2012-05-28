/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.admin.presenter;

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
import com.techstudio.erp.moneychanger.client.admin.view.UomUiHandlers;
import com.techstudio.erp.moneychanger.client.ui.dataprovider.FirstLoad;
import com.techstudio.erp.moneychanger.client.ui.dataprovider.MyDataProvider;
import com.techstudio.erp.moneychanger.client.ui.dataprovider.UomDataProvider;
import com.techstudio.erp.moneychanger.shared.proxy.UomProxy;
import com.techstudio.erp.moneychanger.shared.service.UomRequest;

import java.util.List;

/**
 * @author Nilson
 */
public class UomPresenter
    extends Presenter<UomPresenter.MyView, UomPresenter.MyProxy>
    implements UomUiHandlers {

  /**
   * {@link com.techstudio.erp.moneychanger.client.admin.presenter.UomPresenter}'s proxy.
   */
  @ProxyCodeSplit
  @NameToken(NameTokens.UOM_PAGE)
  public interface MyProxy extends ProxyPlace<UomPresenter> {
  }

  public interface MyView extends View, HasUiHandlers<UomUiHandlers> {
    HasData<UomProxy> getListing();

    void showDetailPanel();

    void showListPanel();

    void setCode(String code);

    void setName(String name);

    void setScale(String scale);

    void showLoading(boolean visible);
  }

  private final Provider<UomRequest> requestProvider;
  private final MyDataProvider<UomProxy> dataProvider;

  private String code;
  private String name;
  private Integer scale;

  @Inject
  public UomPresenter(final EventBus eventBus,
                      final MyView view,
                      final MyProxy proxy,
                      final Provider<UomRequest> requestProvider,
                      final UomDataProvider dataProvider) {
    super(eventBus, view, proxy);
    getView().setUiHandlers(this);
    getView().showLoading(true);
    this.requestProvider = requestProvider;
    this.dataProvider = dataProvider;
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
  protected void onReveal() {
    super.onReveal();
    loadEntity();
    getView().showListPanel();
  }

  @Override
  public void setCode(String code) {
    // code cannot be changed
    getView().setCode(this.code);
  }

  @Override
  public void setName(String name) {
    // name cannot be changed
    getView().setName((this.name));
  }

  @Override
  public void setScale(String scale) {
    this.scale = Integer.parseInt(scale.trim());
    getView().setScale(this.scale.toString());
  }

  @Override
  public void edit(String code) {
    this.code = code.trim();
    loadEntity();
    getView().showDetailPanel();
  }

  @Override
  public void create() {
    // Creation of Uom is not allowed
  }

  @Override
  public void update() {
    if (!isFormValid()) {
      return;
    }
    requestProvider.get().fetchByProperty("code", code)
        .fire(new Receiver<List<UomProxy>>() {
          @Override
          public void onSuccess(List<UomProxy> response) {
            if (response.isEmpty()) {
              Window.alert("That code " + code + " does not exist!");
              getView().showListPanel();
            } else {
              UomRequest request = requestProvider.get();
              UomProxy proxy = response.get(0);
              proxy = request.edit(proxy);
              fillData(proxy);
              request.save(proxy)
                  .fire(new Receiver<UomProxy>() {
                    @Override
                    public void onSuccess(UomProxy response) {
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
    // Deletion of Uom is not allowed
  }

  private void loadEntity() {
    if (code != null && !code.isEmpty()) {
      UomProxy proxy = dataProvider.getByCode(code);
      if (proxy == null) {
        code = "";
        name = "";
        scale = 1;
      } else {
        code = proxy.getCode();
        name = proxy.getName();
        scale = proxy.getScale();
      }
    } else {
      code = "";
      name = "";
      scale = 1;
    }
    updateView();
  }

  private void fillData(UomProxy proxy) {
    proxy.setCode(code);
    proxy.setName(name);
    proxy.setScale(scale);
  }

  private void updateView() {
    getView().setCode(code);
    getView().setName(name);
    getView().setScale(scale.toString());
  }

  private boolean isFormValid() {
    return !Strings.isNullOrEmpty(code)
        && !Strings.isNullOrEmpty(name)
        && scale != null;

  }
}
