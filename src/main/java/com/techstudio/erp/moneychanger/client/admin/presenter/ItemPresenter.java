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
import com.techstudio.erp.moneychanger.client.admin.view.ItemUiHandlers;
import com.techstudio.erp.moneychanger.client.ui.CategoryDataProvider;
import com.techstudio.erp.moneychanger.client.ui.CurrencyDataProvider;
import com.techstudio.erp.moneychanger.client.ui.HasSelectedValue;
import com.techstudio.erp.moneychanger.client.ui.ItemDataProvider;
import com.techstudio.erp.moneychanger.shared.proxy.CategoryProxy;
import com.techstudio.erp.moneychanger.shared.proxy.CurrencyProxy;
import com.techstudio.erp.moneychanger.shared.proxy.ItemProxy;
import com.techstudio.erp.moneychanger.shared.service.ItemRequest;

import java.util.List;

/**
 * @author Nilson
 */
public class ItemPresenter
    extends Presenter<ItemPresenter.MyView, ItemPresenter.MyProxy>
    implements ItemUiHandlers {

  @ProxyCodeSplit
  @NameToken(NameTokens.ITEM_PAGE)
  public interface MyProxy extends ProxyPlace<ItemPresenter> {
  }

  public interface MyView extends View, HasUiHandlers<ItemUiHandlers> {
    HasData<ItemProxy> getItemTable();

    HasSelectedValue<CategoryProxy> getCategoryList();

    HasSelectedValue<CurrencyProxy> getCurrencyList();

    void enableCreateButton(boolean isValid);

    void enableUpdateButton(boolean isValid);

    void setItemCode(String code);

    void setItemName(String name);

    void setItemCategory(CategoryProxy categoryProxy);

    void setItemCurrency(CurrencyProxy currencyProxy);
  }

  private final Provider<ItemRequest> itemRequestProvider;
  private final ItemDataProvider itemDataProvider;
  private final CategoryDataProvider categoryDataProvider;
  private final CurrencyDataProvider currencyDataProvider;

  private Long id;
  private String code;
  private String name;
  private CategoryProxy category;
  private CurrencyProxy currency;

  @Inject
  public ItemPresenter(final EventBus eventBus,
                       final MyView view,
                       final MyProxy proxy,
                       final Provider<ItemRequest> itemRequestProvider,
                       final ItemDataProvider itemDataProvider,
                       final CategoryDataProvider categoryDataProvider,
                       final CurrencyDataProvider currencyDataProvider) {
    super(eventBus, view, proxy);
    getView().setUiHandlers(this);
    this.itemRequestProvider = itemRequestProvider;
    this.itemDataProvider = itemDataProvider;
    this.itemDataProvider.addDataDisplay(getView().getItemTable());
    this.categoryDataProvider = categoryDataProvider;
    this.categoryDataProvider.addDataListDisplay(getView().getCategoryList());
    this.currencyDataProvider = currencyDataProvider;
    this.currencyDataProvider.addDataListDisplay(getView().getCurrencyList());
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
      category = categoryDataProvider.getDefaultCategory();
      currency = currencyDataProvider.getDefaultCurrency();
      itemDataProvider.removeItem();
      updateView();
    } else {
      itemRequestProvider.get().fetch(id)
          .with(ItemProxy.CATEGORY)
          .with(ItemProxy.CURRENCY)
          .fire(new Receiver<ItemProxy>() {
            @Override
            public void onSuccess(ItemProxy response) {
              itemDataProvider.setItem(response);
              code = response.getCode();
              name = response.getName();
              category = response.getCategory();
              currency = response.getCurrency();
              updateView();
            }
          });
    }
  }

  @Override
  public void setItemCode(final String code) {
    this.code = code.trim().toUpperCase();
    updateView();
  }

  @Override
  public void setItemName(final String name) {
    this.name = name.trim();
    updateView();
  }

  @Override
  public void setItemCategory(final CategoryProxy categoryProxy) {
    this.category = categoryProxy;
    updateView();
  }

  @Override
  public void setItemCurrency(final CurrencyProxy currencyProxy) {
    this.currency = currencyProxy;
    updateView();
  }

  @Override
  public void createItem() {
    if (!isFormValid()) {
      return;
    }
    itemRequestProvider.get().fetchByProperty("code", code)
        .fire(new Receiver<List<ItemProxy>>() {
          @Override
          public void onSuccess(List<ItemProxy> response) {
            if (response.isEmpty()) {
              ItemRequest request = itemRequestProvider.get();
              ItemProxy proxy = request.create(ItemProxy.class);
              fillData(proxy);
              request.save(proxy)
                  .with(ItemProxy.CATEGORY)
                  .with(ItemProxy.CURRENCY)
                  .fire(new Receiver<ItemProxy>() {
                    @Override
                    public void onSuccess(ItemProxy response) {
                      itemDataProvider.updateTableData();
                      updateView();
                    }
                  });
            } else {
              Window.alert("An item with that code already exist!");
            }
          }
        });
  }

  @Override
  public void updateItem() {
    if (!isFormValid()) {
      return;
    }
    itemRequestProvider.get().fetchByProperty("code", code)
        .fire(new Receiver<List<ItemProxy>>() {
          @Override
          public void onSuccess(List<ItemProxy> response) {
            if (response.isEmpty()) {
              Window.alert("An item with that code does not exist!");
            } else {
              ItemRequest request = itemRequestProvider.get();
              ItemProxy proxy = response.get(0);
              proxy = request.edit(proxy);
              fillData(proxy);
              request.save(proxy)
                  .with(ItemProxy.CATEGORY)
                  .with(ItemProxy.CURRENCY)
                  .fire(new Receiver<ItemProxy>() {
                    @Override
                    public void onSuccess(ItemProxy response) {
                      itemDataProvider.updateTableData();
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

  private void fillData(ItemProxy proxy) {
    proxy.setCode(code);
    proxy.setName(name);
    proxy.setCategory(category);
    proxy.setCurrency(currency);
  }

  private void updateView() {
    getView().setItemCode(code);
    getView().setItemName(name);
    getView().setItemCategory(category);
    getView().setItemCurrency(currency);
    boolean isValid = isFormValid();
    getView().enableCreateButton(isValid);
    getView().enableUpdateButton(isValid);
  }

  private boolean isFormValid() {
    return !Strings.isNullOrEmpty(code)
        && !Strings.isNullOrEmpty(name)
        && category != null
        && currency != null;
  }
}