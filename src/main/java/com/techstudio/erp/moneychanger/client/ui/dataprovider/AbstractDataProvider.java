/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.ui.dataprovider;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.techstudio.erp.moneychanger.shared.proxy.MyEntityProxy;

import java.util.List;
import java.util.Map;

/**
 * @author Nilson
 */
public abstract class AbstractDataProvider<T extends MyEntityProxy>
    extends AsyncDataProvider<T>
    implements MyDataProvider<T> {

  private List<OnFirstLoad> onFirstLoadList = Lists.newArrayList();

  Map<String, T> map = Maps.newHashMap();

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
  public void load() {
    updateData();
  }

  protected void onSuccessfulLoad() {
    for (OnFirstLoad handler : onFirstLoadList) {
      handler.onSuccess(this);
    }
  }
}
