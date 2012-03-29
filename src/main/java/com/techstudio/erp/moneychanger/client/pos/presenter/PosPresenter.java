/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.pos.presenter;

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
import com.techstudio.erp.moneychanger.client.pos.view.PosUiHandlers;
import com.techstudio.erp.moneychanger.client.ui.CategoryDataProvider;
import com.techstudio.erp.moneychanger.shared.proxy.CategoryProxy;
import com.techstudio.erp.moneychanger.shared.service.CategoryRequest;

/**
 * @author Nilson
 */
public class PosPresenter
    extends Presenter<PosPresenter.MyView, PosPresenter.MyProxy>
    implements PosUiHandlers {

  /**
   * {@link com.techstudio.erp.moneychanger.client.pos.presenter.PosPresenter}'s proxy.
   */
  @ProxyCodeSplit
  @NameToken(NameTokens.POS_PAGE)
  public interface MyProxy extends ProxyPlace<PosPresenter> {
  }

  public interface MyView extends View, HasUiHandlers<PosUiHandlers> {
    HasData<CategoryProxy> getTable();

    void setCategoryName(String name);
  }

  private final Provider<CategoryRequest> categoryRequestProvider;
  private final CategoryDataProvider categoryDataProvider;

  private Long id;
  private CategoryProxy categoryProxy;
//  private List<CategoryProxy> parentCategoryList;

  @Inject
  public PosPresenter(final EventBus eventBus,
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
    RevealContentEvent.fire(this, MainPosPresenter.TYPE_SetMainContent, this);
  }

  @Override
  protected void onReset() {
    super.onReset();

    categoryProxy = null;
    categoryDataProvider.setCategory(categoryProxy);
    if (id != null) {
      categoryRequestProvider.get().fetch(id).with(CategoryProxy.PARENT)
          .fire(new Receiver<CategoryProxy>() {
            @Override
            public void onSuccess(CategoryProxy response) {
              categoryProxy = response;
              categoryDataProvider.setCategory(categoryProxy);
              getView().setCategoryName(response.getName());

//              parentCategoryList.remove(response);
//              CategoryProxy parentCategory = response.getParent();
//              int indexOfParent = 0;
//              if (parentCategory != null) {
//                indexOfParent = parentCategoryList.indexOf(parentCategory);
//              }
//          getView().setParentCategoryIndex(indexOfParent);
            }
          });
    }
    //TODO:Nilson delete this line if not needed
//    RangeChangeEvent.fire(getView().getItemTable(), getView().getItemTable().getVisibleRange());
  }

  @Override
  public void setCategoryName(String name) {
    if (name == null) {
      return;
    }
    if (categoryProxy == null || !name.equals(categoryProxy.getName())) {
      CategoryRequest categoryRequest = categoryRequestProvider.get();
      categoryProxy = getEditableCategoryProxy(categoryRequest);
      categoryProxy.setName(name);
      categoryRequest.save(categoryProxy).with(CategoryProxy.PARENT).fire(new Receiver<CategoryProxy>() {
        @Override
        public void onSuccess(CategoryProxy response) {
          categoryProxy = response;
          categoryDataProvider.updateAllData();
        }
      });
    }
  }

  @Override
  public void setParentCategoryIndex(int index) {
//    CategoryProxy parentCategoryProxy = parentCategoryList.get(index);
//    if (categoryProxy == null || !parentCategoryProxy.equals(categoryProxy.getParent())) {
//      CategoryRequest categoryRequest = categoryRequestProvider.get();
//      categoryProxy = getEditableCategoryProxy(categoryRequest);
//      categoryProxy.setParent(parentCategoryProxy);
//      categoryRequest.save(categoryProxy).with(CategoryProxy.PARENT).fire(new Receiver<CategoryProxy>() {
//        @Override
//        public void onSuccess(CategoryProxy response) {
//          categoryProxy = response;
//          categoryDataProvider.updateTableData();
//        }
//      });
//    }
  }

  private CategoryProxy getEditableCategoryProxy(CategoryRequest categoryRequest) {
    if (categoryProxy == null) {
      categoryProxy = categoryRequest.create(CategoryProxy.class);
    }
    return categoryRequest.edit(categoryProxy);
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
}
