/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.admin.presenter;

import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.LockInteractionEvent;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.client.proxy.RevealRootLayoutContentEvent;

/**
 * This is the top-level presenter of the hierarchy. Other presenters reveal
 * themselves within this presenter.
 * <p/>
 * The goal of this sample is to show how to use nested presenters. These can
 * be useful to decouple multiple presenters that need to be displayed on the
 * screen simultaneously.
 *
 * @author Nilson
 */
public class MainPagePresenter extends
    Presenter<MainPagePresenter.MyView, MainPagePresenter.MyProxy> {
  /**
   * {@link MainPagePresenter}'s proxy.
   */
  @ProxyCodeSplit
  public interface MyProxy extends Proxy<MainPagePresenter> {
  }

  /**
   * {@link MainPagePresenter}'s view.
   */
  public interface MyView extends View {
    void showLoading(boolean visible);
  }

  /**
   * Use this in leaf presenters, inside their {@link #revealInParent} method.
   */
  @ContentSlot
  public static final Type<RevealContentHandler<?>> TYPE_SetTopContent = new Type<RevealContentHandler<?>>();
  @ContentSlot
  public static final Type<RevealContentHandler<?>> TYPE_SetMainContent = new Type<RevealContentHandler<?>>();

  @Inject
  public MainPagePresenter(final EventBus eventBus, final MyView view, final MyProxy proxy) {
    super(eventBus, view, proxy);
  }

  @Override
  protected void revealInParent() {
    RevealRootLayoutContentEvent.fire(this, this);
  }

  /**
   * We display a short lock message whenever navigation is in progress.
   *
   * @param event The {@link LockInteractionEvent}.
   */
  @ProxyEvent
  public void onLockInteraction(LockInteractionEvent event) {
    getView().showLoading(event.shouldLock());
  }

}
