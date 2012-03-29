package com.techstudio.erp.moneychanger.client.util;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.TextResource;
import com.google.inject.Provider;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.techstudio.erp.moneychanger.client.resources.Resources;
import com.techstudio.erp.moneychanger.client.ui.CurrencyDataProvider;
import com.techstudio.erp.moneychanger.shared.proxy.CurrencyProxy;
import com.techstudio.erp.moneychanger.shared.service.CurrencyRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nilson
 */
public final class TestData {

  private final Resources resources;
  private List<MyCurrency> currencies;

  public TestData() {
    resources = GWT.create(Resources.class);
  }

  public void repopulateCurrencies(final Provider<CurrencyRequest> currencyRequestProvider,
                                          final CurrencyDataProvider currencyDataProvider) {
    CurrencyRequest request = currencyRequestProvider.get();
    request.fetchAll().fire(new Receiver<List<CurrencyProxy>>() {
      @Override
      public void onSuccess(List<CurrencyProxy> response) {
        CurrencyRequest request1 = currencyRequestProvider.get();
        for (CurrencyProxy currencyProxy: response) {
          request1.purge(currencyProxy);
        }
        request1.fire(new Receiver<Void>() {
          @Override
          public void onSuccess(Void response) {
            currencies = Lists.newArrayList();
            TextResource textResource = resources.currencyText();
            Iterable<String> split = Splitter.on("\n").split(textResource.getText());
            for (String line: split) {
              ArrayList<String> currency = Lists.newArrayList(Splitter.on(",").split(line));
              if(currency.size() != 2) continue;
              currencies.add(new MyCurrency(currency.get(0), currency.get(1)));
            }
            CurrencyRequest request2 = currencyRequestProvider.get();
            for (MyCurrency myCurrency: currencies) {
              CurrencyProxy proxy1 = request2.create(CurrencyProxy.class);
              proxy1.setCode(myCurrency.code);
              proxy1.setName(myCurrency.name);
              request2.save(proxy1);
            }
            request2.fire(new Receiver<Void>() {
              @Override
              public void onSuccess(Void response) {
                currencyDataProvider.updateAllData();
              }
            });
          }
        });
      }
    });

  }

  private class MyCurrency {
    String code;
    String name;

    MyCurrency(String code, String name) {
      this.code = code;
      this.name = name;
    }
  }
}
