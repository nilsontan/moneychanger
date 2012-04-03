/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.gin;

import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.client.gin.DispatchAsyncModule;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.techstudio.erp.moneychanger.client.admin.presenter.*;
import com.techstudio.erp.moneychanger.client.pos.presenter.MainPosPresenter;
import com.techstudio.erp.moneychanger.client.pos.presenter.PosPresenter;
import com.techstudio.erp.moneychanger.client.resources.Resources;
import com.techstudio.erp.moneychanger.client.util.TestData;
import com.techstudio.erp.moneychanger.shared.service.MoneychangerRequestFactory;

/**
 * @author Nilson
 */
@GinModules({DispatchAsyncModule.class, MoneychangerModule.class})
public interface MoneychangerGinjector extends Ginjector {

  EventBus getEventBus();

  PlaceManager getPlaceManager();

  Resources getResources();

  TestData getDataReset();

  MoneychangerRequestFactory getRequestFactory();

  AsyncProvider<MainPagePresenter> getMainPagePresenter();

  AsyncProvider<ItemPresenter> getItemPresenter();

  AsyncProvider<CategoryPresenter> getCategoryPresenter();

  AsyncProvider<CurrencyPresenter> getCurrencyPresenter();

  AsyncProvider<ExchangeRatePresenter> getExchangeRatePresenter();

  AsyncProvider<UomPresenter> getUomPresenter();

  AsyncProvider<TestPresenter> getTestPresenter();

  AsyncProvider<MainPosPresenter> getMainPosPresenter();

  AsyncProvider<PosPresenter> getPosPresenter();

}