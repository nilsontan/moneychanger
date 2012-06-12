/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.ui.dataprovider;

import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.techstudio.erp.moneychanger.client.ui.HasSelectedValue;
import com.techstudio.erp.moneychanger.shared.proxy.ClientProxy;
import com.techstudio.erp.moneychanger.shared.service.ClientRequest;

import java.util.List;

/**
 * @author Nilson
 */
@Singleton
public class ClientDataProvider extends AbstractDataProvider<ClientProxy> {

  private static ClientProxy DEFAULT;

  private final Provider<ClientRequest> requestProvider;

  @Inject
  public ClientDataProvider(Provider<ClientRequest> requestProvider) {
    this.requestProvider = requestProvider;
    updateData();
    findDefault();
  }

  @Override
  public void updateData() {
    requestProvider.get()
        .fetchAll()
        .fire(new Receiver<List<ClientProxy>>() {
          @Override
          public void onSuccess(List<ClientProxy> proxies) {
            updateRowCount(proxies.size(), true);
            updateMap(proxies);
            if (firstLoad) {
              onSuccessfulLoad();
              firstLoad = false;
            }
            for (HasData<ClientProxy> display : getDataDisplays()) {
              onRangeChanged(display);
            }
            updateList(proxies);
          }
        });
  }

  @Override
  protected void onRangeChanged(final HasData<ClientProxy> display) {
    final Range range = display.getVisibleRange();
    requestProvider.get()
        .fetchRange(range.getStart(), range.getLength())
        .fire(new Receiver<List<ClientProxy>>() {
          @Override
          public void onSuccess(List<ClientProxy> proxies) {
            updateRowData(display, range.getStart(), proxies);
          }
        });

  }

  @Override
  public ClientProxy getDefault() {
    if (DEFAULT == null) {
      findDefault();
    }
    return DEFAULT;
  }

  @Override
  protected void onValueChanged(HasSelectedValue<ClientProxy> display) {
  }

  private void findDefault() {
    if (DEFAULT == null) {
      requestProvider.get()
          .fetchAll()
          .fire(new Receiver<List<ClientProxy>>() {
            @Override
            public void onSuccess(List<ClientProxy> responses) {
              if (responses.isEmpty()) {
              } else {
                DEFAULT = responses.get(0);
              }
            }
          });
    }
  }
}
