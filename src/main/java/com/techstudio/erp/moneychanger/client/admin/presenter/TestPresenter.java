/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.admin.presenter;

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
import com.techstudio.erp.moneychanger.client.admin.view.TestUiHandlers;
import com.techstudio.erp.moneychanger.client.util.MoneychangerTestData;

/**
 * @author Nilson
 */
public class TestPresenter
    extends Presenter<TestPresenter.MyView, TestPresenter.MyProxy>
    implements TestUiHandlers {

  /**
   * {@link com.techstudio.erp.moneychanger.client.admin.presenter.TestPresenter}'s proxy.
   */
  @ProxyCodeSplit
  @NameToken(NameTokens.TEST_PAGE)
  public interface MyProxy extends ProxyPlace<TestPresenter> {
  }

  public interface MyView extends View, HasUiHandlers<TestUiHandlers> {
    void setStatus(String message);
  }

  private final MoneychangerTestData testData;

  @Inject
  public TestPresenter(final EventBus eventBus,
                       final MyView view,
                       final MyProxy proxy,
                       final MoneychangerTestData testData) {
    super(eventBus, view, proxy);
    getView().setUiHandlers(this);
    this.testData = testData;
  }

  @Override
  protected void revealInParent() {
    RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetMainContent, this);
  }

  @Override
  protected void onReset() {
    super.onReset();
  }

  @Override
  public void resetData() {
    getView().setStatus("Resetting Data. Please wait...");
    testData.resetAll();
    getView().setStatus("Data Reset Complete.");
  }

}
