/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.presenter;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.techstudio.erp.moneychanger.client.NameTokens;
import com.techstudio.erp.moneychanger.client.view.MenuUiHandlers;

/**
 * @author Nilson
 */
public class MenuPresenter
    extends Presenter<MenuPresenter.MyView, MenuPresenter.MyProxy>
    implements MenuUiHandlers {

  /**
   * {@link com.techstudio.erp.moneychanger.client.presenter.MenuPresenter}'s proxy.
   */
  @ProxyCodeSplit
  @NameToken(NameTokens.MENU_PAGE)
  public interface MyProxy extends ProxyPlace<MenuPresenter> {
  }

  public interface MyView extends View, HasUiHandlers<MenuUiHandlers> {
  }

  @Inject
  public MenuPresenter(final EventBus eventBus,
                       final MyView view,
                       final MyProxy proxy) {
    super(eventBus, view, proxy);
    getView().setUiHandlers(this);
  }

  @Override
  protected void revealInParent() {
    RevealContentEvent.fire(this, MainPosPresenter.TYPE_SetMainContent, this);
  }

  @Override
  protected void onReset() {
    super.onReset();
  }

  protected void onReveal() {
  }

}
