/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.ui;

import com.google.common.collect.Sets;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.view.client.AsyncDataProvider;

import java.util.Collection;
import java.util.Set;

/**
 * @author Nilson
 */
public abstract class MultiDataProvider<T> extends AsyncDataProvider<T> {

  private Set<HasSelectedValue<T>> displays = Sets.newHashSet();

  /**
   * Adds a data list display to this adapter. The current range of interest of the
   * display will be populated with data.
   *
   * @param display a {@link HasSelectedValue}.
   */
  public void addDataListDisplay(final HasSelectedValue<T> display) {
    if (display == null) {
      throw new IllegalArgumentException("display cannot be null");
    }

    // Add a handler to the display.
    display.addValueChangeHandler(
        new ValueChangeHandler<T>() {
          @Override
          public void onValueChange(ValueChangeEvent<T> event) {
            MultiDataProvider.this.onValueChanged(display);
          }
        }
    );

    // Add to displays
    displays.add(display);

    // Initialize the display with the current list.
    onValueChanged(display);
  }

  protected abstract void onValueChanged(HasSelectedValue<T> display);

  /**
   * Inform the displays of the updated list
   *
   * @param collection the new list
   */
  protected void updateList(Collection<T> collection) {
    for (HasSelectedValue<T> display : displays) {
      display.setOptions(collection);
    }
  }
}
