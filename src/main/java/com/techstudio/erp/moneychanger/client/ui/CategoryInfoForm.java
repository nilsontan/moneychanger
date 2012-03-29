/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.ui;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.google.inject.Inject;

/**
 * A form for filling out category details
 *
 * @author Nilson
 */
public class CategoryInfoForm extends Composite {

  public interface Binder extends UiBinder<Widget, CategoryInfoForm> {
  }

  @UiField
  Grid catGrid;

  @UiField
  TextBox categoryName;

  @UiField
  ListBox categoryParent;

  @Inject
  public CategoryInfoForm(final Binder binder) {
    initWidget(binder.createAndBindUi(this));
  }

//  @Override
//  public void setItemName(String name) {
//    categoryName.setItemName(name);
//  }
//
//  @Override
//  public void setCategoryList(List<CategoryProxy> categoryList) {
//    categoryParent.clear();
//    categoryParent.addItem("None", "0");
//    for (CategoryProxy categoryProxy : categoryList) {
//      categoryParent.addItem(categoryProxy.getName());
//    }
//  }
}
