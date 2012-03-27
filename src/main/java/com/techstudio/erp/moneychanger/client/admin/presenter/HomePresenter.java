/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.admin.presenter;

import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealRootLayoutContentEvent;
import com.techstudio.erp.moneychanger.client.NameTokens;
import com.techstudio.erp.moneychanger.client.ui.ItemDataProvider;
import com.techstudio.erp.moneychanger.shared.proxy.ItemProxy;

/**
 * @author Nilson
 */
public class HomePresenter extends
    Presenter<HomePresenter.MyView, HomePresenter.MyProxy> {
  /**
   * {@link HomePresenter}'s proxy.
   */
  @ProxyCodeSplit
  @NameToken(NameTokens.POS_PAGE)
  public interface MyProxy extends ProxyPlace<HomePresenter> {
  }

  /**
   * {@link HomePresenter}'s view.
   */
  public interface MyView extends View {
    HasData<ItemProxy> getItemTable();

    void setPresenter(HomePresenter presenter);

    Column<ItemProxy, String> getNameColumn();
  }

  @Inject
  public HomePresenter(final EventBus eventBus, final MyView view,
                       final MyProxy proxy, final ItemDataProvider itemDataProvider) {
    super(eventBus, view, proxy);
    view.setPresenter(this);
    itemDataProvider.addDataDisplay(getView().getItemTable());
  }

  @Override
  protected void revealInParent() {
    RevealRootLayoutContentEvent.fire(this, this);
  }

  @Override
  protected void onReset() {
    super.onReset();
    RangeChangeEvent.fire(getView().getItemTable(), getView().getItemTable().getVisibleRange());
  }
}