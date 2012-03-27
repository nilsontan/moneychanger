/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.proxy.PlaceManagerImpl;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.TokenFormatter;
import com.techstudio.erp.moneychanger.client.gin.DefaultPlace;

/**
 * @author Nilson
 */
public class MoneychangerPlaceManager extends PlaceManagerImpl {
  private final PlaceRequest defaultPlaceRequest;

  @Inject
  public MoneychangerPlaceManager(final EventBus eventBus,
                                  final TokenFormatter tokenFormatter,
                                  @DefaultPlace String defaultNameToken) {
    super(eventBus, tokenFormatter);

    this.defaultPlaceRequest = new PlaceRequest(defaultNameToken);
  }

  @Override
  public void revealDefaultPlace() {
    revealPlace(defaultPlaceRequest);
  }
}