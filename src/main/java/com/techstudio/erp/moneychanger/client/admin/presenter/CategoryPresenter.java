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
import com.techstudio.erp.moneychanger.client.admin.view.CategoryUiHandlers;
import com.techstudio.erp.moneychanger.client.ui.CategoryDataProvider;
import com.techstudio.erp.moneychanger.shared.proxy.CategoryProxy;
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
//    HasSelectedValue<CategoryProxy> getList();

    HasData<CategoryProxy> getTable();

    void enableCreateButton(boolean isValid);

    void enableUpdateButton(boolean isValid);

    void setCategoryCode(String code);

    void setCategoryName(String name);
  }

  private final Provider<CategoryRequest> categoryRequestProvider;
  private final CategoryDataProvider categoryDataProvider;

  private Long id;
  private String code;
  private String name;

  @Inject
  public CategoryPresenter(final EventBus eventBus,
                           final MyView view,
                           final MyProxy proxy,
                           final Provider<CategoryRequest> categoryRequestProvider,
                           final CategoryDataProvider categoryDataProvider) {
    super(eventBus, view, proxy);
    getView().setUiHandlers(this);
    this.categoryRequestProvider = categoryRequestProvider;
    this.categoryDataProvider = categoryDataProvider;
    this.categoryDataProvider.addDataDisplay(getView().getTable());
  }

  @Override
  protected void revealInParent() {
    RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetMainContent, this);
  }

  @Override
  protected void onReset() {
    super.onReset();

    if (id == null) {
      code = "";
      name = "";
      updateView();
    } else {
      categoryRequestProvider.get().fetch(id)
          .fire(new Receiver<CategoryProxy>() {
            @Override
            public void onSuccess(CategoryProxy response) {
              code = response.getCode();
              name = response.getName();
              updateView();
            }
          });
    }
  }

  @Override
  public void setCategoryCode(String code) {
    this.code = code.trim().toUpperCase();
    updateView();
  }

  @Override
  public void setCategoryName(String name) {
    this.name = name.trim();
    updateView();
  }

  @Override
  public void createCategory() {
    if (!isFormValid()) {
      return;
    }
    categoryRequestProvider.get().fetchByProperty("code", code)
        .fire(new Receiver<List<CategoryProxy>>() {
          @Override
          public void onSuccess(List<CategoryProxy> response) {
            if (response.isEmpty()) {
              CategoryRequest request = categoryRequestProvider.get();
              CategoryProxy proxy = request.create(CategoryProxy.class);
              fillData(proxy);
              request.save(proxy).fire(new Receiver<CategoryProxy>() {
                @Override
                public void onSuccess(CategoryProxy response) {
                  categoryDataProvider.updateAllData();
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
  public void updateCategory() {
    if (!isFormValid()) {
      return;
    }
    categoryRequestProvider.get().fetchByProperty("code", code)
        .fire(new Receiver<List<CategoryProxy>>() {
          @Override
          public void onSuccess(List<CategoryProxy> response) {
            if (response.isEmpty()) {
              Window.alert("A category with that code does not exist!");
            } else {
              CategoryRequest request = categoryRequestProvider.get();
              CategoryProxy proxy = response.get(0);
              proxy = request.edit(proxy);
              fillData(proxy);
              request.save(proxy).fire(new Receiver<CategoryProxy>() {
                @Override
                public void onSuccess(CategoryProxy response) {
                  categoryDataProvider.updateAllData();
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

  private void fillData(CategoryProxy proxy) {
    proxy.setCode(code);
    proxy.setName(name);
  }

  private void updateView() {
    getView().setCategoryCode(code);
    getView().setCategoryName(name);
    boolean isValid = isFormValid();
    getView().enableCreateButton(isValid);
    getView().enableUpdateButton(isValid);
  }

  private boolean isFormValid() {
    return !Strings.isNullOrEmpty(code)
        && !Strings.isNullOrEmpty(name);
  }
}
