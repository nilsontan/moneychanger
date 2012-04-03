package com.techstudio.erp.moneychanger.client.util;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.TextResource;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.techstudio.erp.moneychanger.client.resources.Resources;
import com.techstudio.erp.moneychanger.shared.proxy.*;
import com.techstudio.erp.moneychanger.shared.service.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Resets data in the database to a default state based on resources provided. Please note that all existing data
 * in the db will be cleared
 *
 * @author Nilson
 */
public final class MoneychangerTestData implements TestData {

  private List<MyCategory> categories;
  private List<MyCurrency> currencies;
  private List<MyUom> uoms;
  private List<MyItem> items;
  private List<MyExchangeRate> exchangeRates;

  private Map<String, CategoryProxy> categoryMap;
  private Map<String, CurrencyProxy> currencyMap;
  private Map<String, UomProxy> uomMap;

  private static final CharMatcher NEWLINE = CharMatcher.is('\n');

  private final Provider<CategoryRequest> categoryRequestProvider;
  private final Provider<CurrencyRequest> currencyRequestProvider;
  private final Provider<UomRequest> uomRequestProvider;
  private final Provider<ItemRequest> itemRequestProvider;
  private final Provider<ExchangeRateRequest> xrRequestProvider;

  private int i = 0;
  private boolean categorySetupCompleted = false;
  private boolean currencySetupCompleted = false;
  private boolean uomSetupCompleted = false;

  @Inject
  public MoneychangerTestData(
      final Provider<CategoryRequest> categoryRequestProvider,
      final Provider<CurrencyRequest> currencyRequestProvider,
      final Provider<UomRequest> uomRequestProvider,
      final Provider<ExchangeRateRequest> xrRequestProvider,
      final Provider<ItemRequest> itemRequestProvider) {
    Resources resources = GWT.create(Resources.class);

    this.categoryRequestProvider = categoryRequestProvider;
    this.currencyRequestProvider = currencyRequestProvider;
    this.uomRequestProvider = uomRequestProvider;
    this.xrRequestProvider = xrRequestProvider;
    this.itemRequestProvider = itemRequestProvider;

    categories = Lists.newArrayList();
    for (ArrayList<String> category : readFromResource(resources.categoryText())) {
      MyCategory myCategory = new MyCategory();
      myCategory.code = category.get(0);
      myCategory.name = category.get(1);
      categories.add(myCategory);
    }

    currencies = Lists.newArrayList();
    for (ArrayList<String> currency : readFromResource(resources.currencyText())) {
      MyCurrency myCurrency = new MyCurrency();
      myCurrency.code = currency.get(0);
      myCurrency.name = currency.get(1);
      myCurrency.sign = currency.get(2);
      currencies.add(myCurrency);
    }

    uoms = Lists.newArrayList();
    for (ArrayList<String> uom : readFromResource(resources.uomText())) {
      MyUom myUom = new MyUom();
      myUom.code = uom.get(0);
      myUom.name = uom.get(1);
      uoms.add(myUom);
    }

    items = Lists.newArrayList();
    for (ArrayList<String> item : readFromResource(resources.itemText())) {
      MyItem myItem = new MyItem();
      myItem.code = item.get(0);
      myItem.name = item.get(1);
      myItem.category = item.get(2);
      myItem.currency = item.get(3);
      myItem.uom = item.get(4);
      myItem.uomRate = item.get(5);
      items.add(myItem);
    }

    exchangeRates = Lists.newArrayList();
    for (ArrayList<String> exchangeRate : readFromResource(resources.xrRatesText())) {
      MyExchangeRate myExchangeRate = new MyExchangeRate();
      myExchangeRate.code = exchangeRate.get(0);
      myExchangeRate.name = exchangeRate.get(1);
      myExchangeRate.currency = exchangeRate.get(2);
      myExchangeRate.unit = exchangeRate.get(3);
      myExchangeRate.bid = exchangeRate.get(4);
      myExchangeRate.ask = exchangeRate.get(5);
      exchangeRates.add(myExchangeRate);
    }
  }

  public void resetAll() {
    resetCategories();
    resetCurrencies();
    resetUoms();
  }

  private void onCategorySetupCompleted() {
    onDomainDataSetupCompleted();
  }

  private void onCurrencySetupCompleted() {
    resetExchangeRates();
    onDomainDataSetupCompleted();
  }

  private void onUomSetupCompleted() {
    onDomainDataSetupCompleted();
  }

  private void onDomainDataSetupCompleted() {
    if (categorySetupCompleted
        && currencySetupCompleted
        && uomSetupCompleted) {
      resetItems();
    }
  }

  private void onXrSetupCompleted() {
  }

  private void onItemSetupCompleted() {
  }

  private List<ArrayList<String>> readFromResource(TextResource textResource) {
    Iterable<String> split =
        Splitter.on(NEWLINE)
            .omitEmptyStrings()
            .trimResults()
            .split(textResource.getText());
    List<ArrayList<String>> list = Lists.newArrayList();
    for (String line : split) {
      list.add(Lists.newArrayList(Splitter.on(",").split(line)));
    }
    return list;
  }

  private void resetCategories() {
    categorySetupCompleted = false;
    Log.debug(i++ + " - Categories fetchAll");
    categoryRequestProvider.get().fetchAll().fire(new Receiver<List<CategoryProxy>>() {
      @Override
      public void onSuccess(List<CategoryProxy> response) {
        CategoryRequest request1 = categoryRequestProvider.get();
        for (CategoryProxy categoryProxy : response) {
          request1.purge(categoryProxy);
        }
        Log.debug(i++ + " - Categories purgeAll");
        request1.fire(new Receiver<Void>() {
          @Override
          public void onSuccess(Void response) {
            CategoryRequest request2 = categoryRequestProvider.get();
            for (MyCategory myCategory : categories) {
              CategoryProxy proxy1 = request2.create(CategoryProxy.class);
              proxy1.setCode(myCategory.code);
              proxy1.setName(myCategory.name);
              request2.save(proxy1);
            }
            Log.debug(i++ + " - Categories save And map");
            request2.fire(new Receiver<Void>() {
              @Override
              public void onSuccess(Void response) {
                categoryRequestProvider.get().fetchAll().fire(new Receiver<List<CategoryProxy>>() {
                  @Override
                  public void onSuccess(List<CategoryProxy> response) {
                    categoryMap = Maps.newHashMap();
                    for (CategoryProxy proxy : response) {
                      categoryMap.put(proxy.getCode(), proxy);
                    }
                    categorySetupCompleted = true;
                    onCategorySetupCompleted();
                  }
                });
              }
            });
          }
        });
      }
    });
  }

  private void resetCurrencies() {
    currencySetupCompleted = false;
    Log.debug(i++ + " - Currencies fetchAll");
    currencyRequestProvider.get().fetchAll().fire(new Receiver<List<CurrencyProxy>>() {
      @Override
      public void onSuccess(List<CurrencyProxy> response) {
        CurrencyRequest request1 = currencyRequestProvider.get();
        for (CurrencyProxy currencyProxy : response) {
          request1.purge(currencyProxy);
        }
        Log.debug(i++ + " - Currencies purgeAll");
        request1.fire(new Receiver<Void>() {
          @Override
          public void onSuccess(Void response) {
            CurrencyRequest request2 = currencyRequestProvider.get();
            for (MyCurrency myCurrency : currencies) {
              CurrencyProxy proxy1 = request2.create(CurrencyProxy.class);
              proxy1.setCode(myCurrency.code);
              proxy1.setName(myCurrency.name);
              proxy1.setSign(myCurrency.sign);
              request2.save(proxy1);
            }
            Log.debug(i++ + " - Currencies save And map");
            request2.fire(new Receiver<Void>() {
              @Override
              public void onSuccess(Void response) {
                currencyRequestProvider.get().fetchAll().fire(new Receiver<List<CurrencyProxy>>() {
                  @Override
                  public void onSuccess(List<CurrencyProxy> response) {
                    currencyMap = Maps.newHashMap();
                    for (CurrencyProxy proxy : response) {
                      currencyMap.put(proxy.getCode(), proxy);
                    }
                    currencySetupCompleted = true;
                    onCurrencySetupCompleted();
                  }
                });
              }
            });
          }
        });
      }
    });
  }

  private void resetUoms() {
    uomSetupCompleted = false;
    Log.debug(i++ + " - Uoms fetchAll");
    uomRequestProvider.get().fetchAll().fire(new Receiver<List<UomProxy>>() {
      @Override
      public void onSuccess(List<UomProxy> response) {
        UomRequest request1 = uomRequestProvider.get();
        for (UomProxy uomProxy : response) {
          request1.purge(uomProxy);
        }
        Log.debug(i++ + " - Uoms purgeAll");
        request1.fire(new Receiver<Void>() {
          @Override
          public void onSuccess(Void response) {
            UomRequest request2 = uomRequestProvider.get();
            for (MyUom myUom : uoms) {
              UomProxy proxy1 = request2.create(UomProxy.class);
              proxy1.setCode(myUom.code);
              proxy1.setName(myUom.name);
              request2.save(proxy1);
            }
            Log.debug(i++ + " - Uoms save And map");
            request2.fire(new Receiver<Void>() {
              @Override
              public void onSuccess(Void response) {
                uomRequestProvider.get().fetchAll().fire(new Receiver<List<UomProxy>>() {
                  @Override
                  public void onSuccess(List<UomProxy> response) {
                    uomMap = Maps.newHashMap();
                    for (UomProxy proxy : response) {
                      uomMap.put(proxy.getCode(), proxy);
                    }
                    uomSetupCompleted = true;
                    onUomSetupCompleted();
                  }
                });
              }
            });
          }
        });
      }
    });
  }

  private void resetExchangeRates() {
    Log.debug(i++ + " - Xrs fetchAll");
    xrRequestProvider.get().fetchAll().fire(new Receiver<List<ExchangeRateProxy>>() {
      @Override
      public void onSuccess(List<ExchangeRateProxy> response) {
        ExchangeRateRequest request1 = xrRequestProvider.get();
        for (ExchangeRateProxy xrProxy : response) {
          request1.purge(xrProxy);
        }
        Log.debug(i++ + " - Xrs purgeAll");
        request1.fire(new Receiver<Void>() {
          @Override
          public void onSuccess(Void response) {
            ExchangeRateRequest request2 = xrRequestProvider.get();
            for (MyExchangeRate myXr : exchangeRates) {
              ExchangeRateProxy proxy1 = request2.create(ExchangeRateProxy.class);
              proxy1.setCode(myXr.code);
              proxy1.setName(myXr.name);
              proxy1.setCurrency(currencyMap.get(myXr.currency));
              proxy1.setUnit(Integer.valueOf(myXr.unit));
              proxy1.setAskRate(myXr.ask);
              proxy1.setBidRate(myXr.bid);
              request2.save(proxy1);
            }
            Log.debug(i++ + " - Xrs saveAll");
            request2.fire(new Receiver<Void>() {
              @Override
              public void onSuccess(Void response) {
                onXrSetupCompleted();
              }
            });
          }
        });
      }
    });
  }

  private void resetItems() {
    Log.debug(i++ + " - Items fetchAll");
    itemRequestProvider.get().fetchAll().fire(new Receiver<List<ItemProxy>>() {
      @Override
      public void onSuccess(List<ItemProxy> response) {
        ItemRequest request1 = itemRequestProvider.get();
        for (ItemProxy itemProxy : response) {
          request1.purge(itemProxy);
        }
        Log.debug(i++ + " - Items purgeAll");
        request1.fire(new Receiver<Void>() {
          @Override
          public void onSuccess(Void response) {
            ItemRequest request2 = itemRequestProvider.get();
            for (MyItem myItem : items) {
              ItemProxy proxy1 = request2.create(ItemProxy.class);
              proxy1.setCode(myItem.code);
              proxy1.setName(myItem.name);
              proxy1.setCategory(categoryMap.get(myItem.category));
              proxy1.setCurrency(currencyMap.get(myItem.currency));
              proxy1.setUom(uomMap.get(myItem.uom));
              proxy1.setUomRate(Integer.valueOf(myItem.uomRate));
              request2.save(proxy1);
            }
            Log.debug(i++ + " - Items saveAll");
            request2.fire(new Receiver<Void>() {
              @Override
              public void onSuccess(Void response) {
                onItemSetupCompleted();
              }
            });
          }
        });
      }
    });
  }

  private class MyCategory {
    String code;
    String name;
  }

  private class MyCurrency {
    String code;
    String name;
    String sign;
  }

  private class MyUom {
    String code;
    String name;
  }

  private class MyExchangeRate {
    String code;
    String name;
    String currency;
    String unit;
    String ask;
    String bid;
  }

  private class MyItem {
    String code;
    String name;
    String category;
    String currency;
    String uom;
    String uomRate;

  }
}
