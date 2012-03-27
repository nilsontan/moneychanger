/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.ui;

import com.google.gwt.user.client.ui.HasValue;

import java.util.Collection;

/**
 * MVP-friendly interface for use with any widget that can be populated
 * with a Collection of items, one of which may be selected
 *
 * @author Nilson
 */
public interface HasSelectedValue<T> extends HasValue<T> {
  void setOptions(Collection<T> options);

  void setSelectedValue(T selected);

  T getSelectedValue();
}
