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

    currencies = Lists.newArrayList();
    TextResource textResource = resources.currencyText();
    Iterable<String> split = Splitter.on("\r\n").split(textResource.getText());
    for (String line : split) {
      ArrayList<String> currency = Lists.newArrayList(Splitter.on(",").split(line));
      if (currency.size() != 4) continue;
      MyCurrency myCurrency = new MyCurrency();
      myCurrency.code = currency.get(0);
      myCurrency.name = currency.get(1);
      myCurrency.sign = currency.get(2);
      myCurrency.rate = currency.get(3);
      currencies.add(myCurrency);
    }
  }

  public void repopulateCurrencies(final Provider<CurrencyRequest> currencyRequestProvider,
                                   final CurrencyDataProvider currencyDataProvider) {
    CurrencyRequest request = currencyRequestProvider.get();
    request.fetchAll().fire(new Receiver<List<CurrencyProxy>>() {
      @Override
      public void onSuccess(List<CurrencyProxy> response) {
        CurrencyRequest request1 = currencyRequestProvider.get();
        for (CurrencyProxy currencyProxy : response) {
          request1.purge(currencyProxy);
        }
        request1.fire(new Receiver<Void>() {
          @Override
          public void onSuccess(Void response) {
            CurrencyRequest request2 = currencyRequestProvider.get();
            for (MyCurrency myCurrency : currencies) {
              CurrencyProxy proxy1 = request2.create(CurrencyProxy.class);
              proxy1.setCode(myCurrency.code);
              proxy1.setName(myCurrency.name);
              proxy1.setSign(myCurrency.sign);
              proxy1.setRate(myCurrency.rate);
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
    String sign;
    String rate;

//    MyCurrency(String code, String name, String sign, BigDecimal rate) {
//      this.code = code;
//      this.name = name;
//      this.sign = sign;
//      this.rate = rate;
//    }
  }
}
