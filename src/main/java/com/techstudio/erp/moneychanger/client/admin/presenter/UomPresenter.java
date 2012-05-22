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
import com.techstudio.erp.moneychanger.client.admin.view.UomUiHandlers;
import com.techstudio.erp.moneychanger.client.ui.UomDataProvider;
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
    HasData<UomProxy> getTable();

    void enableCreateButton(boolean isValid);

    void enableUpdateButton(boolean isValid);

    void setUomCode(String code);

    void setUomName(String name);
  }

  private final Provider<UomRequest> uomRequestProvider;
  private final UomDataProvider uomDataProvider;

  private Long id;
  private String code;
  private String name;

  @Inject
  public UomPresenter(final EventBus eventBus,
                      final MyView view,
                      final MyProxy proxy,
                      final Provider<UomRequest> uomRequestProvider,
                      final UomDataProvider uomDataProvider) {
    super(eventBus, view, proxy);
    getView().setUiHandlers(this);
    this.uomRequestProvider = uomRequestProvider;
    this.uomDataProvider = uomDataProvider;
    this.uomDataProvider.addDataDisplay(getView().getTable());
  }

  @Override
  protected void revealInParent() {
    RevealContentEvent.fire(this, MainPosPresenter.TYPE_SetMainContent, this);
  }

  @Override
  protected void onReset() {
    super.onReset();

    if (id == null) {
      updateView();
    } else {
      uomRequestProvider.get().fetch(id)
          .fire(new Receiver<UomProxy>() {
            @Override
            public void onSuccess(UomProxy response) {
              code = response.getCode();
              name = response.getName();
              updateView();
            }
          });
    }
  }

  @Override
  protected void onReveal() {
    super.onReveal();
    uomDataProvider.updateListData();
  }

  @Override
  public void setUomCode(String code) {
    this.code = code.trim().toUpperCase();
    updateView();
  }

  @Override
  public void setUomName(String name) {
    this.name = name.trim();
    updateView();
  }

  @Override
  public void createUom() {
    if (!isFormValid()) {
      return;
    }
    uomRequestProvider.get().fetchByProperty("code", code)
        .fire(new Receiver<List<UomProxy>>() {
          @Override
          public void onSuccess(List<UomProxy> response) {
            if (response.isEmpty()) {
              UomRequest request = uomRequestProvider.get();
              UomProxy proxy = request.create(UomProxy.class);
              fillData(proxy);
              request.save(proxy).fire(new Receiver<UomProxy>() {
                @Override
                public void onSuccess(UomProxy response) {
                  uomDataProvider.updateAllData();
                  updateView();
                }
              });
            } else {
              Window.alert("A uom with that code already exist!");
            }
          }
        });
  }

  @Override
  public void updateUom() {
    if (!isFormValid()) {
      return;
    }
    uomRequestProvider.get().fetchByProperty("code", code)
        .fire(new Receiver<List<UomProxy>>() {
          @Override
          public void onSuccess(List<UomProxy> response) {
            if (response.isEmpty()) {
              Window.alert("A uom with that code does not exist!");
            } else {
              UomRequest request = uomRequestProvider.get();
              UomProxy proxy = response.get(0);
              proxy = request.edit(proxy);
              fillData(proxy);
              request.save(proxy).fire(new Receiver<UomProxy>() {
                @Override
                public void onSuccess(UomProxy response) {
                  uomDataProvider.updateAllData();
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

  private void fillData(UomProxy proxy) {
    proxy.setCode(code);
    proxy.setName(name);
  }

  private void updateView() {
    getView().setUomCode(code);
    getView().setUomName(name);
    boolean isValid = isFormValid();
    getView().enableCreateButton(isValid);
    getView().enableUpdateButton(isValid);
  }

  private boolean isFormValid() {
    return !Strings.isNullOrEmpty(code)
        && !Strings.isNullOrEmpty(name);
  }
}
