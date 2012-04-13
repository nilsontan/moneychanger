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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Resets data in the database to a default state based on resources provided. Please note that all existing data
 * in the db will be cleared
 *
 * @author Nilson
 */
//TODO:Nilson move this class to test
public final class MoneychangerTestData implements TestData {

  private List<MyCategory> categories;
  private List<MyCurrency> currencies;
  private List<MyUom> uoms;
  private List<MyCountry> countries;
  private List<MyItem> items;
  private List<MySpotRate> spotRates;

  private Map<String, CategoryProxy> categoryMap;
  private Map<String, CurrencyProxy> currencyMap;
  private Map<String, UomProxy> uomMap;
  private Map<String, ItemProxy> itemMap;

  private static final CharMatcher NEWLINE = CharMatcher.is('\n');

  private final Provider<CategoryRequest> categoryRequestProvider;
  private final Provider<CurrencyRequest> currencyRequestProvider;
  private final Provider<UomRequest> uomRequestProvider;
  private final Provider<CountryRequest> countryRequestProvider;
  private final Provider<ItemRequest> itemRequestProvider;
  private final Provider<SpotRateRequest> spotRateRequestProvider;

  private boolean categorySetupCompleted = false;
  private boolean currencySetupCompleted = false;
  private boolean uomSetupCompleted = false;

  @Inject
  public MoneychangerTestData(
      final Provider<CategoryRequest> categoryRequestProvider,
      final Provider<CurrencyRequest> currencyRequestProvider,
      final Provider<UomRequest> uomRequestProvider,
      final Provider<CountryRequest> countryRequestProvider,
      final Provider<ItemRequest> itemRequestProvider,
      final Provider<SpotRateRequest> spotRateRequestProvider) {
    Resources resources = GWT.create(Resources.class);

    this.categoryRequestProvider = categoryRequestProvider;
    this.currencyRequestProvider = currencyRequestProvider;
    this.uomRequestProvider = uomRequestProvider;
    this.countryRequestProvider = countryRequestProvider;
    this.itemRequestProvider = itemRequestProvider;
    this.spotRateRequestProvider = spotRateRequestProvider;

    categories = Lists.newArrayList();
    for (ArrayList<String> category : readFromResource(resources.categoryText())) {
      assert category.size() == 2 : "Category not of size 2: " + category;
      MyCategory myCategory = new MyCategory();
      myCategory.code = category.get(0);
      myCategory.name = category.get(1);
      categories.add(myCategory);
    }

    currencies = Lists.newArrayList();
    for (ArrayList<String> currency : readFromResource(resources.currencyText())) {
      assert currency.size() == 3 : "Currency not of size 3: " + currency;
      MyCurrency myCurrency = new MyCurrency();
      myCurrency.code = currency.get(0);
      myCurrency.name = currency.get(1);
      myCurrency.fullName = currency.get(2);
      currencies.add(myCurrency);
    }

    uoms = Lists.newArrayList();
    for (ArrayList<String> uom : readFromResource(resources.uomText())) {
      assert uom.size() == 2 : "Uom not of size 2: " + uom;
      MyUom myUom = new MyUom();
      myUom.code = uom.get(0);
      myUom.name = uom.get(1);
      uoms.add(myUom);
    }

    countries = Lists.newArrayList();
    for (ArrayList<String> country : readFromResource(resources.countryText())) {
      assert country.size() == 4 : "Country not of size 4: " + country;
      MyCountry myCountry = new MyCountry();
      myCountry.code = country.get(0);
      myCountry.name = country.get(1);
      myCountry.fullName = country.get(2);
      myCountry.currency = country.get(3);
      countries.add(myCountry);
    }

    items = Lists.newArrayList();
    for (ArrayList<String> item : readFromResource(resources.itemText())) {
      assert item.size() == 6 : "Item not of size 6: " + item;
      MyItem myItem = new MyItem();
      myItem.code = item.get(0);
      myItem.name = item.get(1);
      myItem.fullName = item.get(2);
      myItem.category = item.get(3);
      myItem.uom = item.get(4);
      myItem.uomRate = item.get(5);
      items.add(myItem);
    }

    spotRates = Lists.newArrayList();
    for (ArrayList<String> spotRate : readFromResource(resources.spotRatesText())) {
      assert spotRate.size() == 3 : "Spot Rate not of size 6: " + spotRate;
      MySpotRate mySpotRate = new MySpotRate();
      mySpotRate.code = spotRate.get(0);
      mySpotRate.bid = spotRate.get(1);
      mySpotRate.ask = spotRate.get(2);
      spotRates.add(mySpotRate);
    }
  }

  public void resetAll() {
    resetCategories();
    resetCurrencies();
    resetUoms();
  }

  private void onCategorySetupCompleted() {
    categorySetupCompleted = true;
    onDomainDataSetupCompleted();
  }

  private void onCurrencySetupCompleted() {
    currencySetupCompleted = true;
    resetCountries();
    onDomainDataSetupCompleted();
  }

  private void onUomSetupCompleted() {
    uomSetupCompleted = true;
    onDomainDataSetupCompleted();
  }

  private void onDomainDataSetupCompleted() {
    if (categorySetupCompleted
        && currencySetupCompleted
        && uomSetupCompleted) {
      resetItems();
    }
  }

  private void onCountrySetupCompleted() {
  }

  private void onItemSetupCompleted() {
    resetSpotRates();
  }

  private void onSpotRateSetupCompleted() {
    Log.debug("Test data setup completed");
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
    Log.debug("Categories fetchAll");
    categoryRequestProvider.get().fetchAll().fire(new Receiver<List<CategoryProxy>>() {
      @Override
      public void onSuccess(List<CategoryProxy> response) {
        CategoryRequest request1 = categoryRequestProvider.get();
        for (CategoryProxy categoryProxy : response) {
          request1.purge(categoryProxy);
        }
        Log.debug("Categories purgeAll");
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
            Log.debug("Categories save And map");
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
                    Log.debug("Categories setup done");
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
    Log.debug("Currencies fetchAll");
    currencyRequestProvider.get().fetchAll().fire(new Receiver<List<CurrencyProxy>>() {
      @Override
      public void onSuccess(List<CurrencyProxy> response) {
        CurrencyRequest request1 = currencyRequestProvider.get();
        for (CurrencyProxy currencyProxy : response) {
          request1.purge(currencyProxy);
        }
        Log.debug("Currencies purgeAll");
        request1.fire(new Receiver<Void>() {
          @Override
          public void onSuccess(Void response) {
            CurrencyRequest request2 = currencyRequestProvider.get();
            for (MyCurrency myCurrency : currencies) {
              CurrencyProxy proxy1 = request2.create(CurrencyProxy.class);
              proxy1.setCode(myCurrency.code);
              proxy1.setName(myCurrency.name);
              proxy1.setFullName(myCurrency.fullName);
              request2.save(proxy1);
            }
            Log.debug("Currencies save And map");
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
                    Log.debug("Currencies setup done");
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
    Log.debug("Uoms fetchAll");
    uomRequestProvider.get().fetchAll().fire(new Receiver<List<UomProxy>>() {
      @Override
      public void onSuccess(List<UomProxy> response) {
        UomRequest request1 = uomRequestProvider.get();
        for (UomProxy uomProxy : response) {
          request1.purge(uomProxy);
        }
        Log.debug("Uoms purgeAll");
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
            Log.debug("Uoms save And map");
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
                    Log.debug("Uoms setup done");
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

  private void resetCountries() {
    Log.debug("Countries fetchAll");
    countryRequestProvider.get().fetchAll().fire(new Receiver<List<CountryProxy>>() {
      @Override
      public void onSuccess(List<CountryProxy> response) {
        CountryRequest request1 = countryRequestProvider.get();
        for (CountryProxy countryProxy : response) {
          request1.purge(countryProxy);
        }
        Log.debug("Countries purgeAll");
        request1.fire(new Receiver<Void>() {
          @Override
          public void onSuccess(Void response) {
            CountryRequest request2 = countryRequestProvider.get();
            for (MyCountry myCountry : countries) {
              CountryProxy proxy1 = request2.create(CountryProxy.class);
              proxy1.setCode(myCountry.code);
              proxy1.setName(myCountry.name);
              proxy1.setFullName(myCountry.fullName);
              proxy1.setCurrency(currencyMap.get(myCountry.currency));
              request2.save(proxy1);
            }
            Log.debug("Countries save And map");
            request2.fire(new Receiver<Void>() {
              @Override
              public void onSuccess(Void response) {
                Log.debug("Countries setup done");
                onCountrySetupCompleted();
              }
            });
          }
        });
      }
    });
  }

  private void resetItems() {
    Log.debug("Items fetchAll");
    itemRequestProvider.get().fetchAll().fire(new Receiver<List<ItemProxy>>() {
      @Override
      public void onSuccess(List<ItemProxy> response) {
        ItemRequest request1 = itemRequestProvider.get();
        for (ItemProxy itemProxy : response) {
          request1.purge(itemProxy);
        }
        Log.debug("Items purgeAll");
        request1.fire(new Receiver<Void>() {
          @Override
          public void onSuccess(Void response) {
            ItemRequest request2 = itemRequestProvider.get();
            for (MyItem myItem : items) {
              ItemProxy proxy1 = request2.create(ItemProxy.class);
              proxy1.setCode(myItem.code);
              proxy1.setName(myItem.name);
              proxy1.setFullName(myItem.fullName);
              proxy1.setCategory(categoryMap.get(myItem.category));
              proxy1.setCurrency(currencyMap.get("SGD"));
              proxy1.setUom(uomMap.get(myItem.uom));
              proxy1.setUomRate(Integer.valueOf(myItem.uomRate));
              request2.save(proxy1);
            }
            Log.debug("Items saveAll");
            request2.fire(new Receiver<Void>() {
              @Override
              public void onSuccess(Void response) {
                itemRequestProvider.get().fetchAll().fire(new Receiver<List<ItemProxy>>() {
                  @Override
                  public void onSuccess(List<ItemProxy> response) {
                    itemMap = Maps.newHashMap();
                    for (ItemProxy proxy : response) {
                      itemMap.put(proxy.getCode(), proxy);
                    }
                    Log.debug("Items setup done");
                    onItemSetupCompleted();
                  }
                });
              }
            });
          }
        });
      }
    });
  }

  private void resetSpotRates() {
    Log.debug("Spot Rates fetchAll");
    spotRateRequestProvider.get().fetchAll().fire(new Receiver<List<SpotRateProxy>>() {
      @Override
      public void onSuccess(List<SpotRateProxy> response) {
        SpotRateRequest request1 = spotRateRequestProvider.get();
        for (SpotRateProxy spotRateProxy : response) {
          request1.purge(spotRateProxy);
        }
        Log.debug("Spot Rates purgeAll");
        request1.fire(new Receiver<Void>() {
          @Override
          public void onSuccess(Void response) {
            SpotRateRequest request2 = spotRateRequestProvider.get();
            for (MySpotRate mySpotRate : spotRates) {
              SpotRateProxy proxy1 = request2.create(SpotRateProxy.class);
              proxy1.setCode(mySpotRate.code);
              proxy1.setName(itemMap.get(mySpotRate.code).getName());
              proxy1.setAskRate(new BigDecimal(mySpotRate.ask));
              proxy1.setBidRate(new BigDecimal(mySpotRate.bid));
              request2.save(proxy1);
            }
            Log.debug("Spot Rates saveAll");
            request2.fire(new Receiver<Void>() {
              @Override
              public void onSuccess(Void response) {
                spotRateRequestProvider.get().fetchAll().fire(new Receiver<List<SpotRateProxy>>() {
                  @Override
                  public void onSuccess(List<SpotRateProxy> response) {
                    Log.debug("Spot Rates setup done");
                    onSpotRateSetupCompleted();
                  }
                });
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
    String fullName;
  }

  private class MyUom {
    String code;
    String name;
  }

  private class MyCountry {
    String code;
    String name;
    String fullName;
    String currency;
  }

  private class MySpotRate {
    String code;
    String bid;
    String ask;
  }

  private class MyItem {
    String code;
    String name;
    String fullName;
    String category;
    String uom;
    String uomRate;

  }
}
