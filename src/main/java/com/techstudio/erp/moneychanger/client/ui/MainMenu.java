/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.ui;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * A simple menu that can be reused.
 *
 * @author Nilson
 */
public class MainMenu extends Composite {

  public interface Binder extends UiBinder<Widget, MainMenu> {
  }

  @Inject
  public MainMenu(final Binder binder) {
    initWidget(binder.createAndBindUi(this));
  }
}