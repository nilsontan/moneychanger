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
import com.techstudio.erp.moneychanger.client.admin.presenter.CategoryPresenter;
import com.techstudio.erp.moneychanger.client.admin.presenter.CurrencyPresenter;
import com.techstudio.erp.moneychanger.client.admin.presenter.ItemPresenter;
import com.techstudio.erp.moneychanger.client.admin.presenter.MainPagePresenter;
import com.techstudio.erp.moneychanger.client.admin.view.CategoryView;
import com.techstudio.erp.moneychanger.client.admin.view.CurrencyView;
import com.techstudio.erp.moneychanger.client.admin.view.ItemView;
import com.techstudio.erp.moneychanger.client.admin.view.MainPageView;
import com.techstudio.erp.moneychanger.client.pos.presenter.MainPosPresenter;
import com.techstudio.erp.moneychanger.client.pos.presenter.PosPresenter;
import com.techstudio.erp.moneychanger.client.pos.view.MainPosView;
import com.techstudio.erp.moneychanger.client.pos.view.PosView;
import com.techstudio.erp.moneychanger.client.resources.Resources;
import com.techstudio.erp.moneychanger.shared.service.CategoryRequest;
import com.techstudio.erp.moneychanger.shared.service.CurrencyRequest;
import com.techstudio.erp.moneychanger.shared.service.ItemRequest;
import com.techstudio.erp.moneychanger.shared.service.MoneychangerRequestFactory;

/**
 * @author Nilson
 */
public class MoneychangerModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    // Default implementation of standard resources
//    install(new DefaultModule(MoneychangerPlaceManager.class));

    bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
    bind(PlaceManager.class).to(MoneychangerPlaceManager.class).in(Singleton.class);
    bind(MoneychangerPlaceManager.class).in(Singleton.class);
    bind(TokenFormatter.class).to(ParameterTokenFormatter.class).in(Singleton.class);
    bind(RootPresenter.class).asEagerSingleton();
    bind(Scheduler.class).to(SchedulerImpl.class).in(Singleton.class);
    bind(Resources.class).in(Singleton.class);

    // Constants
    bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.ITEM_PAGE);

    // Presenters
    bindPresenter(MainPagePresenter.class, MainPagePresenter.MyView.class,
        MainPageView.class, MainPagePresenter.MyProxy.class);
    bindPresenter(ItemPresenter.class, ItemPresenter.MyView.class,
        ItemView.class, ItemPresenter.MyProxy.class);
    bindPresenter(CategoryPresenter.class, CategoryPresenter.MyView.class,
        CategoryView.class, CategoryPresenter.MyProxy.class);
    bindPresenter(CurrencyPresenter.class, CurrencyPresenter.MyView.class,
        CurrencyView.class, CurrencyPresenter.MyProxy.class);

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
  ItemRequest provideItemService(MoneychangerRequestFactory requestFactory) {
    return requestFactory.itemRequest();
  }

  @Provides
  CategoryRequest provideCategoryService(MoneychangerRequestFactory requestFactory) {
    return requestFactory.categoryRequest();
  }

  @Provides
  CurrencyRequest provideCurrencyService(MoneychangerRequestFactory requestFactory) {
    return requestFactory.currencyRequest();
  }
}