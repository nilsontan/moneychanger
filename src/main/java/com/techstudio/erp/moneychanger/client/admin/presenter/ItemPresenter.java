/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.admin.presenter;

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
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.techstudio.erp.moneychanger.client.NameTokens;
import com.techstudio.erp.moneychanger.client.admin.view.ItemUiHandlers;
import com.techstudio.erp.moneychanger.client.ui.ItemDataProvider;
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
    HasData<ItemProxy> getTable();

    void showItemName(String name);
  }

  private final Provider<ItemRequest> itemRequestProvider;
  private final ItemDataProvider itemDataProvider;

  private Long id;
  private ItemProxy itemProxy;

  @Inject
  public ItemPresenter(final EventBus eventBus,
                       final MyView view,
                       final MyProxy proxy,
                       final Provider<ItemRequest> itemRequestProvider,
                       final ItemDataProvider itemDataProvider) {
    super(eventBus, view, proxy);
    getView().setUiHandlers(this);
    this.itemRequestProvider = itemRequestProvider;
    this.itemDataProvider = itemDataProvider;
    this.itemDataProvider.addDataDisplay(getView().getTable());
  }

  @Override
  protected void revealInParent() {
    RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetMainContent, this);
  }

  @Override
  protected void onReset() {
    super.onReset();

    itemProxy = null;
    if (id != null) {
      itemRequestProvider.get().fetch(id).with("category").fire(new Receiver<ItemProxy>() {
        @Override
        public void onSuccess(ItemProxy itemProxy) {
          ItemPresenter.this.itemProxy = itemProxy;
          getView().showItemName(itemProxy.getName());
        }
      });
    }
    RangeChangeEvent.fire(getView().getTable(), getView().getTable().getVisibleRange());
  }

  @Override
  public void updateItemName(final String name) {
    if (itemProxy == null || !name.equals(itemProxy.getName())) {

      itemRequestProvider.get().fetchByProperty("name", name).fire(new Receiver<List<ItemProxy>>() {
        @Override
        public void onSuccess(List<ItemProxy> response) {
          if (response.isEmpty()) {
            ItemRequest itemRequest = itemRequestProvider.get();
            itemProxy = getEditableItemProxy(itemRequest);
            itemProxy.setName(name);
            itemRequest.save(itemProxy).with("category").fire(new Receiver<ItemProxy>() {
              @Override
              public void onSuccess(ItemProxy response) {
                itemDataProvider.updateData();
              }
            });
          } else {
            Window.alert("An item with that name already exist!");
          }
        }
      });
    }
  }

//  public void setItemCatName(final String name) {
//    if (name == null || fetching) {
//      return;
//    }
//    if (categoryProxy == null || !name.equals(categoryProxy.getName())) {
//      CategoryRequest categoryRequest = categoryServiceProvider.get();
//      categoryProxy = getEditableCategoryProxy(categoryRequest);
//      categoryProxy.showItemName(name);
//      categoryRequest.save(categoryProxy).to(new Receiver<Void>() {
//        @Override
//        public void onSuccess(Void aVoid) {
//          categoryDataProvider.updateTableData();
////          getView().setItemCatName(name);
////          getView().updateCatTable(categoryProxy);
//          ItemRequest itemRequest = itemRequestProvider.get();
//          itemProxy = getEditableItemProxy(itemRequest);
//          itemProxy.setCategory(categoryProxy);
//          itemRequest.save(itemProxy).with("category").to(new Receiver<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//              itemDataProvider.updateTableData();
//            }
//          }).fire();
//        }
//      }).fire();
//    }
//  }

  private ItemProxy getEditableItemProxy(ItemRequest itemRequest) {
    if (itemProxy == null) {
      itemProxy = itemRequest.create(ItemProxy.class);
    }
    return itemRequest.edit(itemProxy);
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