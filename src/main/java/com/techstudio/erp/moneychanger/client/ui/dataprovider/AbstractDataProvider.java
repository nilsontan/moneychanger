/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.ui.dataprovider;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.techstudio.erp.moneychanger.client.ui.HasSelectedValue;
import com.techstudio.erp.moneychanger.shared.proxy.MyEntityProxy;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Nilson
 */
public abstract class AbstractDataProvider<T extends MyEntityProxy>
    extends AsyncDataProvider<T>
    implements MyDataProvider<T> {

  private List<OnFirstLoad> onFirstLoadList = Lists.newArrayList();

  private Set<HasSelectedValue<T>> displays = Sets.newHashSet();

  Map<String, T> map = Maps.newHashMap();

  protected boolean firstLoad = true;

  @Override
  public void addDataDisplay(HasData<T> display) {
    super.addDataDisplay(display);
    load();
  }

  @Override
  public void updateMap(List<T> proxies) {
    map.clear();
    for (T proxy : proxies) {
      map.put(proxy.getCode(), proxy);
    }
  }

  @Override
  public T getByCode(String code) {
    return map.get(code);
  }

  @Override
  public T getDefault() {
    return null;
  }

  @Override
  public HandlerRegistration addOnFirstLoadHandler(final OnFirstLoad handler) {
    assert handler != null;
    onFirstLoadList.add(handler);
    return new HandlerRegistration() {
      @Override
      public void removeHandler() {
        onFirstLoadList.remove(handler);
      }
    };
  }

  @Override
  public void firstLoad() {
    firstLoad = true;
  }

  @Override
  public void load() {
    updateData();
  }

  protected void onSuccessfulLoad() {
    for (OnFirstLoad handler : onFirstLoadList) {
      handler.onSuccess(this);
    }
  }

  /**
   * Adds a data list display to this adapter. The current range of interest of the
   * display will be populated with data.
   *
   * @param display a {@link HasSelectedValue}.
   */
  @Override
  public void addDataListDisplay(final HasSelectedValue<T> display) {
    if (display == null) {
      throw new IllegalArgumentException("display cannot be null");
    }

    // Add a handler to the display.
    display.addValueChangeHandler(
        new ValueChangeHandler<T>() {
          @Override
          public void onValueChange(ValueChangeEvent<T> event) {
            AbstractDataProvider.this.onValueChanged(display);
          }
        }
    );

    // Add to displays
    displays.add(display);

    // Initialize the display with the current list.
    onValueChanged(display);

    load();
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
