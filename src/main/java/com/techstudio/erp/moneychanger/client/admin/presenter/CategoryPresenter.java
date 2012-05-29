/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.admin.presenter;

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
import com.techstudio.erp.moneychanger.client.admin.view.CategoryUiHandlers;
import com.techstudio.erp.moneychanger.client.ui.HasSelectedValue;
import com.techstudio.erp.moneychanger.client.ui.dataprovider.CategoryDataProvider;
import com.techstudio.erp.moneychanger.client.ui.dataprovider.FirstLoad;
import com.techstudio.erp.moneychanger.client.ui.dataprovider.MyDataProvider;
import com.techstudio.erp.moneychanger.client.ui.dataprovider.UomDataProvider;
import com.techstudio.erp.moneychanger.shared.proxy.CategoryProxy;
import com.techstudio.erp.moneychanger.shared.proxy.UomProxy;
import com.techstudio.erp.moneychanger.shared.service.CategoryRequest;

import java.util.List;

/**
 * @author Nilson
 */
public class CategoryPresenter
    extends Presenter<CategoryPresenter.MyView, CategoryPresenter.MyProxy>
    implements CategoryUiHandlers {

  /**
   * {@link CategoryPresenter}'s proxy.
   */
  @ProxyCodeSplit
  @NameToken(NameTokens.CATEGORY_PAGE)
  public interface MyProxy extends ProxyPlace<CategoryPresenter> {
  }

  public interface MyView extends View, HasUiHandlers<CategoryUiHandlers> {
    HasData<CategoryProxy> getListing();

    HasSelectedValue<UomProxy> getUomListing();

    void showListPanel();

    void showDetailPanel();

    void showAddButtons();

    void showEditButtons();

    void setCode(String code);

    void setName(String name);

    void setUom(UomProxy uom);

    void showLoading(boolean visible);
  }

  private final Provider<CategoryRequest> requestProvider;
  private final MyDataProvider<CategoryProxy> dataProvider;
  private final MyDataProvider<UomProxy> uomDataProvider;

  private String code;
  private String name;
  private UomProxy uom;
  private Step step;

  @Inject
  public CategoryPresenter(final EventBus eventBus,
                           final MyView view,
                           final MyProxy proxy,
                           final Provider<CategoryRequest> requestProvider,
                           final CategoryDataProvider dataProvider,
                           final UomDataProvider uomDataProvider) {
    super(eventBus, view, proxy);
    getView().setUiHandlers(this);
    getView().showLoading(true);
    this.requestProvider = requestProvider;
    this.uomDataProvider = uomDataProvider;
    this.uomDataProvider.addDataListDisplay(getView().getUomListing());
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
    reset();
  }

  @Override
  protected void onReveal() {
    super.onReveal();
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
  public void setUom(UomProxy uom) {
    this.uom = uom;
    getView().setUom(this.uom);
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
        .fire(new Receiver<List<CategoryProxy>>() {
          @Override
          public void onSuccess(List<CategoryProxy> response) {
            if (response.isEmpty()) {
              CategoryRequest request = requestProvider.get();
              CategoryProxy proxy = request.create(CategoryProxy.class);
              fillData(proxy);
              request.save(proxy).fire(new Receiver<CategoryProxy>() {
                @Override
                public void onSuccess(CategoryProxy response) {
                  dataProvider.updateData();
                  step = Step.LIST;
                  updateView();
                }
              });
            } else {
              Window.alert("A category with that code already exist!");
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
        .fire(new Receiver<List<CategoryProxy>>() {
          @Override
          public void onSuccess(List<CategoryProxy> response) {
            if (response.isEmpty()) {
              Window.alert("The code \"" + code + "\" does not exist!");
              step = Step.LIST;
              updateView();
            } else {
              CategoryRequest request = requestProvider.get();
              CategoryProxy proxy = response.get(0);
              proxy = request.edit(proxy);
              fillData(proxy);
              request.save(proxy).fire(new Receiver<CategoryProxy>() {
                @Override
                public void onSuccess(CategoryProxy response) {
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
        .fire(new Receiver<List<CategoryProxy>>() {
          @Override
          public void onSuccess(List<CategoryProxy> response) {
            if (response.isEmpty()) {
              Window.alert("The code \"" + code + "\" does not exist!");
              step = Step.LIST;
              updateView();
            } else {
              CategoryProxy proxy = response.get(0);
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
    uom = uomDataProvider.getDefault();
  }

  private void loadEntity() {
    if (code != null && !code.isEmpty()) {
      CategoryProxy proxy = dataProvider.getByCode(code);
      if (proxy == null) {
        resetFields();
        Log.error("Code not found: " + code);
        onBack();
      } else {
        code = proxy.getCode();
        name = proxy.getName();
        uom = proxy.getUom();
      }
    } else {
      resetFields();
    }
  }

  private void fillData(CategoryProxy proxy) {
    proxy.setCode(code);
    proxy.setName(name);
    proxy.setUom(uom);
  }

  private void updateView() {
    getView().setCode(code);
    getView().setName(name);
    getView().setUom(uom);
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
        && uom != null;
  }

  private enum Step {
    LIST, ADD, EDIT
  }
}
