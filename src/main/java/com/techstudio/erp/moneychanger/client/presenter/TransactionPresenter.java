/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.presenter;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gwt.i18n.shared.DateTimeFormat;
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
import com.techstudio.erp.moneychanger.client.gin.DefaultScaleForCosting;
import com.techstudio.erp.moneychanger.client.ui.HasSelectedValue;
import com.techstudio.erp.moneychanger.client.ui.dataprovider.*;
import com.techstudio.erp.moneychanger.client.view.TransactionUiHandlers;
import com.techstudio.erp.moneychanger.shared.proxy.*;
import com.techstudio.erp.moneychanger.shared.service.TransactionRequest;

import java.util.Date;
import java.util.List;

/**
 * @author Nilson
 */
public class TransactionPresenter
    extends Presenter<TransactionPresenter.MyView, TransactionPresenter.MyProxy>
    implements TransactionUiHandlers {

  @ProxyCodeSplit
  @NameToken(NameTokens.TRANSACTION_PAGE)
  public interface MyProxy extends ProxyPlace<TransactionPresenter> {
  }

  public interface MyView extends View, HasUiHandlers<TransactionUiHandlers> {
    HasSelectedValue<CategoryProxy> getCategoryList();

    HasSelectedValue<ItemProxy> getItemList();

//    HasSelectedValue<CategoryProxy> getCategoryList();

    HasData<TransactionProxy> getListing();

    void showSearchPanel();

    void showListPanel();

    void showDetailPanel();

    void setDate(String date);

    void setCategory(CategoryProxy category);

    void setItem(ItemProxy item);

    void setPending(Boolean pending);

    void updateLineItems(List<LineItemProxy> lineItems);

    void showLoading(boolean visible);
  }

  private static final DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy-MM-dd");

  private final Provider<TransactionRequest> requestProvider;
  private final TransactionDataProvider dataProvider;
  private final MyDataProvider<CategoryProxy> categoryDataProvider;
  private final MyDataProvider<ItemProxy> itemDataProvider;

  private Long id;

  private Date date;
  private CategoryProxy category;
  private ItemProxy item;
  private Boolean pending;

  private boolean categoryLoaded = false;
  private boolean itemLoaded = false;

  @DefaultScaleForCosting
  @Inject
  Integer scale;

  @Inject
  public TransactionPresenter(final EventBus eventBus,
                              final MyView view,
                              final MyProxy proxy,
                              final Provider<TransactionRequest> requestProvider,
                              final TransactionDataProvider dataProvider,
                              final CategoryDataProvider categoryDataProvider,
                              final ItemDataProvider itemDataProvider) {
    super(eventBus, view, proxy);
    getView().setUiHandlers(this);
    getView().showLoading(true);
    categoryLoaded = false;
    itemLoaded = false;
    this.requestProvider = requestProvider;
    this.categoryDataProvider = categoryDataProvider;
    this.itemDataProvider = itemDataProvider;
    this.dataProvider = dataProvider;
    this.categoryDataProvider.firstLoad();
    this.itemDataProvider.firstLoad();
    this.categoryDataProvider.addOnFirstLoadHandler(onCategoryFirstLoad);
    this.categoryDataProvider.addDataListDisplay(getView().getCategoryList());
    this.itemDataProvider.addOnFirstLoadHandler(onItemFirstLoad);
    this.itemDataProvider.addDataListDisplay(getView().getItemList());
    this.dataProvider.addOnSearchHandler(onSearch);
    this.dataProvider.addDataDisplay(getView().getListing());
  }

  FirstLoad.OnFirstLoad onCategoryFirstLoad = new FirstLoad.OnFirstLoad() {
    @Override
    public void onSuccess(FirstLoad firstLoad) {
      categoryLoaded = true;
      onDataLoaded();
    }
  };

  FirstLoad.OnFirstLoad onItemFirstLoad = new FirstLoad.OnFirstLoad() {
    @Override
    public void onSuccess(FirstLoad firstLoad) {
      itemLoaded = true;
      onDataLoaded();
    }
  };

  private void onDataLoaded() {
    if (categoryLoaded && itemLoaded) {
      Timer timer = new Timer() {
        @Override
        public void run() {
          getView().showLoading(false);
          getView().showSearchPanel();
        }
      };

      timer.schedule(1000);
    }
  }

  SearchFilter.OnSearch onSearch = new SearchFilter.OnSearch() {
    @Override
    public void onSuccess(SearchFilter searchFilter) {
      Timer timer = new Timer() {
        @Override
        public void run() {
          getView().showLoading(false);
          getView().showListPanel();
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
    resetFields();
    loadEntity();
    getView().showSearchPanel();
  }

  @Override
  protected void onReveal() {
    super.onReveal();
  }

  @Override
  public void search() {
    getView().showLoading(true);
    TransactionRequest request = requestProvider.get();
    TransactionSearchFilterProxy searchFilter = request.create(TransactionSearchFilterProxy.class);
    searchFilter.setDate(date);
    searchFilter.setCategoryCode(category == null ? "" : category.getCode());
    searchFilter.setItemCode(item == null ? "" : item.getCode());
    List<TransactionSearchFilterProxy> list = Lists.newArrayList();
    list.add(searchFilter);
    TransactionProxy proxy = request.create(TransactionProxy.class);
    proxy.setSearchFilter(list);
    request.searchByFilter(proxy)
        .with("lineItems")
        .fire(new Receiver<List<TransactionProxy>>() {
          @Override
          public void onSuccess(List<TransactionProxy> proxies) {
            dataProvider.updateSearchList(proxies);
          }
        });
  }

  @Override
  public void setSearchDate(String value) {
    if (value == null || value.isEmpty()) {
      date = null;
    } else {
      date = dtf.parse(value);
    }
  }

  @Override
  public void setSearchCategory(CategoryProxy selectedValue) {
    category = selectedValue;
  }

  @Override
  public void setSearchItem(ItemProxy selectedValue) {
    item = selectedValue;
  }

  @Override
  public void setSearchPending(Boolean selectedValue) {
    pending = selectedValue;
  }

  @Override
  public void edit(Long id) {
    this.id = id;
    loadEntity();
    getView().showDetailPanel();
  }

  @Override
  public void create() {
    // Creation of Transaction is not allowed
  }

  @Override
  public void update() {
    requestProvider.get().fetch(id)
        .fire(new Receiver<TransactionProxy>() {
          @Override
          public void onSuccess(TransactionProxy response) {
            if (response == null) {
              Window.alert("That id " + id + " does not exist!");
              getView().showListPanel();
            } else {
              TransactionRequest request = requestProvider.get();
              TransactionProxy proxy = request.edit(response);
              request.save(proxy)
                  .fire(new Receiver<TransactionProxy>() {
                    @Override
                    public void onSuccess(TransactionProxy response) {
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
    // Deletion of Transaction is not allowed
  }

  private void resetFields() {
    id = null;
    date = new Date();
    category = null;
    item = null;
    pending = null;
  }

  private void loadEntity() {
    if (id != null) {
      requestProvider.get().fetch(id)
          .with("lineItems")
          .fire(new Receiver<TransactionProxy>() {
            @Override
            public void onSuccess(TransactionProxy response) {
              getView().updateLineItems(response.getLineItems());
            }
          });
    }
    updateView();
  }

  private void updateView() {
    if (date == null) {
      getView().setDate("");
    } else {
      getView().setDate(dtf.format(date));
    }
    getView().setCategory(category);
    getView().setItem(item);
    getView().setPending(pending);
  }
}