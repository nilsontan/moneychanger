/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.gin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.impl.SchedulerImpl;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.RootPresenter;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.proxy.ParameterTokenFormatter;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.TokenFormatter;
import com.techstudio.erp.moneychanger.client.MoneychangerPlaceManager;
import com.techstudio.erp.moneychanger.client.NameTokens;
import com.techstudio.erp.moneychanger.client.admin.presenter.*;
import com.techstudio.erp.moneychanger.client.admin.view.*;
import com.techstudio.erp.moneychanger.client.pos.presenter.MainPosPresenter;
import com.techstudio.erp.moneychanger.client.pos.presenter.PosPresenter;
import com.techstudio.erp.moneychanger.client.pos.view.MainPosView;
import com.techstudio.erp.moneychanger.client.pos.view.PosView;
import com.techstudio.erp.moneychanger.client.resources.Resources;
import com.techstudio.erp.moneychanger.client.ui.ItemMenuImageFileUpload;
import com.techstudio.erp.moneychanger.client.util.MoneychangerTestData;
import com.techstudio.erp.moneychanger.client.util.TestData;
import com.techstudio.erp.moneychanger.shared.service.*;
import gwtupload.client.IFileInput;

/**
 * @author Nilson
 */
public class MoneychangerModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
    bind(PlaceManager.class).to(MoneychangerPlaceManager.class).in(Singleton.class);
    bind(MoneychangerPlaceManager.class).in(Singleton.class);
    bind(TokenFormatter.class).to(ParameterTokenFormatter.class).in(Singleton.class);
    bind(RootPresenter.class).asEagerSingleton();
    bind(Scheduler.class).to(SchedulerImpl.class).in(Singleton.class);
    bind(Resources.class).in(Singleton.class);
    bind(TestData.class).to(MoneychangerTestData.class).in(Singleton.class);

    // Constants
    bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.POS_PAGE);
    bindConstant().annotatedWith(DefaultCurrency.class).to("SGD");
    bindConstant().annotatedWith(DefaultScaleForCosting.class).to(4);
    bindConstant().annotatedWith(DefaultScaleForMoney.class).to(2);
    bindConstant().annotatedWith(DefaultScaleForItemQty.class).to(2);
    bindConstant().annotatedWith(DefaultScaleForRate.class).to(8);

    // Presenters
    bindPresenter(MainPagePresenter.class, MainPagePresenter.MyView.class,
        MainPageView.class, MainPagePresenter.MyProxy.class);
    bindPresenter(ItemPresenter.class, ItemPresenter.MyView.class,
        ItemView.class, ItemPresenter.MyProxy.class);
    bindPresenter(CategoryPresenter.class, CategoryPresenter.MyView.class,
        CategoryView.class, CategoryPresenter.MyProxy.class);
    bindPresenter(CountryPresenter.class, CountryPresenter.MyView.class,
        CountryView.class, CountryPresenter.MyProxy.class);
    bindPresenter(CurrencyPresenter.class, CurrencyPresenter.MyView.class,
        CurrencyView.class, CurrencyPresenter.MyProxy.class);
    bindPresenter(SpotRatePresenter.class, SpotRatePresenter.MyView.class,
        SpotRateView.class, SpotRatePresenter.MyProxy.class);
    bindPresenter(UomPresenter.class, UomPresenter.MyView.class,
        UomView.class, UomPresenter.MyProxy.class);
    bindPresenter(TestPresenter.class, TestPresenter.MyView.class,
        TestView.class, TestPresenter.MyProxy.class);

    bindPresenter(MainPosPresenter.class, MainPosPresenter.MyView.class,
        MainPosView.class, MainPosPresenter.MyProxy.class);
    bindPresenter(PosPresenter.class, PosPresenter.MyView.class,
        PosView.class, PosPresenter.MyProxy.class);
  }

  @Provides
  @Singleton
  MoneychangerRequestFactory provideRequestFactory(EventBus eventBus) {
    MoneychangerRequestFactory requestFactory = GWT.create(MoneychangerRequestFactory.class);
    requestFactory.initialize(eventBus);
    return requestFactory;
  }

  @Provides
  CategoryRequest provideCategoryService(MoneychangerRequestFactory requestFactory) {
    return requestFactory.categoryRequest();
  }

  @Provides
  CountryRequest provideCountryService(MoneychangerRequestFactory requestFactory) {
    return requestFactory.countryRequest();
  }

  @Provides
  CurrencyRequest provideCurrencyService(MoneychangerRequestFactory requestFactory) {
    return requestFactory.currencyRequest();
  }

  @Provides
  ItemRequest provideItemService(MoneychangerRequestFactory requestFactory) {
    return requestFactory.itemRequest();
  }

  @Provides
  LineItemRequest provideLineItemService(MoneychangerRequestFactory requestFactory) {
    return requestFactory.lineItemRequest();
  }

  @Provides
  SpotRateRequest provideExchangeRateService(MoneychangerRequestFactory requestFactory) {
    return requestFactory.spotRateRequest();
  }

  @Provides
  TransactionRequest provideTransactionService(MoneychangerRequestFactory requestFactory) {
    return requestFactory.transactionRequest();
  }

  @Provides
  UomRequest provideUomService(MoneychangerRequestFactory requestFactory) {
    return requestFactory.uomRequest();
  }

  @Provides
  IFileInput provideFileUpload(ItemMenuImageFileUpload itemMenuImageFileUpload) {
    return itemMenuImageFileUpload;
  }
}