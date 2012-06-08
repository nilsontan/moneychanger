/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.presenter;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.base.Strings;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.techstudio.erp.moneychanger.client.NameTokens;
import com.techstudio.erp.moneychanger.client.view.ItemUiHandlers;
import com.techstudio.erp.moneychanger.client.ui.HasSelectedValue;
import com.techstudio.erp.moneychanger.client.ui.dataprovider.CategoryDataProvider;
import com.techstudio.erp.moneychanger.client.ui.dataprovider.FirstLoad;
import com.techstudio.erp.moneychanger.client.ui.dataprovider.ItemDataProvider;
import com.techstudio.erp.moneychanger.client.ui.dataprovider.MyDataProvider;
import com.techstudio.erp.moneychanger.shared.proxy.CategoryProxy;
import com.techstudio.erp.moneychanger.shared.proxy.ItemProxy;
import com.techstudio.erp.moneychanger.shared.proxy.PricingProxy;
import com.techstudio.erp.moneychanger.shared.service.ItemRequest;
import com.techstudio.erp.moneychanger.shared.service.PricingRequest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * @author Nilson
 */
public class ItemPresenter
    extends Presenter<ItemPresenter.MyView, ItemPresenter.MyProxy>
    implements ItemUiHandlers {

  @ProxyCodeSplit
  @NameToken(NameTokens.ITEM_PAGE)
  public interface MyProxy extends ProxyPlace<ItemPresenter> {
  }

  public interface MyView extends View, HasUiHandlers<ItemUiHandlers> {
    /*HasData<ItemProxy> getItemTable();

    HasSelectedValue<CategoryProxy> getCategoryList();

    HasSelectedValue<CurrencyProxy> getCurrencyList();

    HasSelectedValue<UomProxy> getUomList();

    void enableCreateButton(boolean isValid);

    void enableUpdateButton(boolean isValid);

    void setItemCode(String code);

    void setItemName(String name);

    void setItemFullName(String fullName);

    void setItemCategory(CategoryProxy categoryProxy);

    void setItemCurrency(CurrencyProxy currencyProxy);

    void setItemUom(UomProxy uomProxy);

    void setItemUomRate(String uomRate);

    void setItemImageUrl(String itemImageUrl);*/

    HasData<ItemProxy> getListing();

    HasSelectedValue<CategoryProxy> getCategoryList();

    void showListPanel();

    void showDetailPanel();

    void showAddButtons();

    void showEditButtons();

    void setCode(String code);

    void setName(String name);

    void setFullname(String fullname);

    void setCategory(CategoryProxy categoryProxy);

    void setUomRate(String uomRate);

    void showLoading(boolean visible);
  }

  private final Provider<ItemRequest> requestProvider;
  private final Provider<PricingRequest> pricingRequestProvider;
  private final MyDataProvider<ItemProxy> dataProvider;
  private final MyDataProvider<CategoryProxy> categoryDataProvider;
//  private final CurrencyDataProvider currencyDataProvider;
//  private final UomDataProvider uomDataProvider;

//  private Long id;
//  private String code;
//  private String name;
//  private String fullName;
//  private CurrencyProxy currency;
//  private UomProxy uom;
//  private BigDecimal uomRate;
//  private String imageUrl;

  private String code;
  private String name;
  private String fullName;
  private BigDecimal uomRate;
  private CategoryProxy category;

  private Step step;

  @Inject
  public ItemPresenter(final EventBus eventBus,
                       final MyView view,
                       final MyProxy proxy,
                       final Provider<ItemRequest> requestProvider,
                       final Provider<PricingRequest> pricingRequestProvider,
                       final ItemDataProvider dataProvider,
                       final CategoryDataProvider categoryDataProvider
//                       final CurrencyDataProvider currencyDataProvider
//                       final UomDataProvider uomDataProvider
  ) {
    super(eventBus, view, proxy);
    getView().setUiHandlers(this);
    getView().showLoading(true);
    this.requestProvider = requestProvider;
    this.pricingRequestProvider = pricingRequestProvider;
    this.dataProvider = dataProvider;
    this.dataProvider.firstLoad();
    this.dataProvider.addOnFirstLoadHandler(onFirstLoad);
    this.categoryDataProvider = categoryDataProvider;
    this.categoryDataProvider.addDataListDisplay(getView().getCategoryList());
    this.dataProvider.addDataDisplay(getView().getListing());
//    this.currencyDataProvider = currencyDataProvider;
//    this.currencyDataProvider.addDataListDisplay(getView().getCurrencyList());
//    this.uomDataProvider = uomDataProvider;
//    this.uomDataProvider.addDataListDisplay(getView().getUomList());
  }

  FirstLoad.OnFirstLoad onFirstLoad = new FirstLoad.OnFirstLoad() {
    @Override
    public void onSuccess(FirstLoad firstLoad) {
      Timer timer = new Timer() {
        @Override
        public void run() {
          getView().showLoading(false);
        }
      };

      timer.schedule(1000);
    }
  };

  @Override
  protected void revealInParent() {
    RevealContentEvent.fire(this, MainPosPresenter.TYPE_SetMainContent, this);
  }

  @Override
  protected void onReset() {
    super.onReset();
    reset();
  }

  @Override
  protected void onReveal() {
    reset();
  }

  @Override
  public void onBack() {
    step = Step.LIST;
    updateView();
  }

  @Override
  public void onNext() {
    step = Step.ADD;
    resetFields();
    updateView();
  }

  @Override
  public void setCode(String code) {
    this.code = code.trim().toUpperCase();
    getView().setCode(this.code);
  }

  @Override
  public void setName(String name) {
    this.name = name.trim();
    getView().setName(this.name);
  }

  @Override
  public void setFullName(String fullName) {
    this.fullName = fullName.trim();
    getView().setFullname(this.fullName);
  }

  @Override
  public void setUomRate(String uomRate) {
    BigDecimal newRate = new BigDecimal(uomRate);
    this.uomRate = newRate.setScale(category.getUom().getScale(), RoundingMode.HALF_UP);
    getView().setUomRate(this.uomRate.toString());
  }

  @Override
  public void setCategory(CategoryProxy selectedValue) {
    this.category = selectedValue;
    getView().setCategory(selectedValue);
  }

  @Override
  public void edit(String code) {
    this.code = code;
    step = Step.EDIT;
    loadEntity();
    updateView();
  }

  @Override
  public void create() {
    if (!isFormValid()) {
      return;
    }
    requestProvider.get().fetchByProperty("code", code)
        .fire(new Receiver<List<ItemProxy>>() {
          @Override
          public void onSuccess(List<ItemProxy> response) {
            if (response.isEmpty()) {
              ItemRequest request = requestProvider.get();
              ItemProxy proxy = request.create(ItemProxy.class);
              fillData(proxy);
              request.save(proxy).fire(new Receiver<ItemProxy>() {
                @Override
                public void onSuccess(ItemProxy response) {
                  dataProvider.updateData();
                  PricingRequest pricingRequest = pricingRequestProvider.get();
                  PricingProxy pricingProxy = pricingRequest.create(PricingProxy.class);
                  pricingProxy.setCode(code);
                  pricingProxy.setName(name);
                  pricingProxy.setBidRate(BigDecimal.ONE);
                  pricingProxy.setAskRate(BigDecimal.ONE);
                  pricingRequest.save(pricingProxy)
                      .fire(new Receiver<PricingProxy>() {
                        @Override
                        public void onSuccess(PricingProxy response) {
                          step = Step.LIST;
                          updateView();
                        }
                      });
                }
              });
            } else {
              Window.alert("An item with that code already exist!");
            }
          }
        });
  }

  @Override
  public void update() {
    if (!isFormValid()) {
      return;
    }

    requestProvider.get().fetchByProperty("code", code)
        .fire(new Receiver<List<ItemProxy>>() {
          @Override
          public void onSuccess(List<ItemProxy> response) {
            if (response.isEmpty()) {
              Window.alert("The code \"" + code + "\" does not exist!");
              step = Step.LIST;
              updateView();
            } else {
              ItemRequest request = requestProvider.get();
              ItemProxy proxy = response.get(0);
              proxy = request.edit(proxy);
              fillData(proxy);
              request.save(proxy).fire(new Receiver<ItemProxy>() {
                @Override
                public void onSuccess(ItemProxy response) {
                  dataProvider.updateData();
                  Timer timer = new Timer() {
                    @Override
                    public void run() {
                      step = Step.LIST;
                      updateView();
                    }
                  };

                  timer.schedule(300);
                }
              });
            }
          }
        });
  }

  @Override
  public void delete() {
    if (!step.equals(Step.EDIT)) {
      return;
    }

    requestProvider.get()
        .fetchByProperty("code", code)
        .fire(new Receiver<List<ItemProxy>>() {
          @Override
          public void onSuccess(List<ItemProxy> response) {
            if (response.isEmpty()) {
              Window.alert("The code \"" + code + "\" does not exist!");
              step = Step.LIST;
              updateView();
            } else {
              ItemProxy proxy = response.get(0);
              requestProvider.get()
                  .purge(proxy)
                  .fire(new Receiver<Void>() {
                    @Override
                    public void onSuccess(Void response) {
                      dataProvider.updateData();
                      pricingRequestProvider.get().fetchByProperty("code", code)
                          .fire(new Receiver<List<PricingProxy>>() {
                            @Override
                            public void onSuccess(List<PricingProxy> response) {
                              if (!response.isEmpty()) {
                                pricingRequestProvider.get()
                                    .purge(response.get(0))
                                    .fire();
                              }
                              step = Step.LIST;
                              updateView();
                            }
                          });
                    }
                  });
            }
          }
        });
  }

  private void reset() {
    resetFields();
    step = Step.LIST;
    RangeChangeEvent.fire(getView().getListing(), getView().getListing().getVisibleRange());
    loadEntity();
    updateView();
  }

  private void resetFields() {
    code = "";
    name = "";
    fullName = "";
    uomRate = BigDecimal.ONE;
    category = categoryDataProvider.getDefault();
    if (category != null) {
      uomRate = uomRate.setScale(category.getUom().getScale(), RoundingMode.HALF_UP);
    }
  }

  private void loadEntity() {
    if (code != null && !code.isEmpty()) {
      ItemProxy proxy = dataProvider.getByCode(code);
      if (proxy == null) {
        resetFields();
        Log.error("Code not found: " + code);
        onBack();
      } else {
        code = proxy.getCode();
        name = proxy.getName();
        fullName = proxy.getFullName();
        uomRate = proxy.getUomRate();
        category = proxy.getCategory();
      }
    } else {
      resetFields();
    }
  }

  private void fillData(ItemProxy proxy) {
    proxy.setCode(code);
    proxy.setName(name);
    proxy.setFullName(fullName);
    proxy.setUomRate(uomRate);
    proxy.setCategory(category);
  }

  private void updateView() {
    getView().setCode(code);
    getView().setName(name);
    getView().setFullname(fullName);
    getView().setUomRate(uomRate.toString());
    getView().setCategory(category);
    switch (step) {
      case LIST:
        getView().showListPanel();
        break;
      case ADD:
        getView().showDetailPanel();
        getView().showAddButtons();
        break;
      case EDIT:
        getView().showDetailPanel();
        getView().showEditButtons();
        break;
    }
  }

  private boolean isFormValid() {
    return !Strings.isNullOrEmpty(code)
        && !Strings.isNullOrEmpty(name)
        && !Strings.isNullOrEmpty(fullName)
        && uomRate != null
        && category != null;
  }

  private enum Step {
    LIST, ADD, EDIT
  }

  /*@Override
  protected void onReset() {
    super.onReset();

    if (id != null) {
      requestProvider.get().fetch(id)
          .with(ItemProxy.CATEGORY)
          .with(ItemProxy.CURRENCY)
          .with(ItemProxy.UOM)
          .fire(new Receiver<ItemProxy>() {
            @Override
            public void onSuccess(ItemProxy response) {
              code = response.getCode();
              name = response.getName();
              fullName = response.getFullName();
              category = response.getCategory();
              currency = response.getCurrency();
              uom = response.getUom();
              uomRate = response.getUomRate();
              imageUrl = response.getImageUrl();
              updateView();
            }
          });
    }
  }

  @Override
  protected void onReveal() {
    super.onReveal();
//    categoryDataProvider.updateListData();
    currencyDataProvider.updateListData();
    uomDataProvider.updateListData();
    if (categoryDataProvider.getDefault() == null) {
      Window.alert("There are no available Categories. Please create a Category first");
    } else if (currencyDataProvider.getDefaultCurrency() == null) {
      Window.alert("There are no available Currencies. Please create a Currency first");
    } else if (uomDataProvider.getDefaultUom() == null) {
      Window.alert("There are no available Uoms. Please create a Uom first");
    } else {
      code = "";
      name = "";
      fullName = "";
      category = categoryDataProvider.getDefault();
      currency = currencyDataProvider.getDefaultCurrency();
      uom = uomDataProvider.getDefaultUom();
      uomRate = BigDecimal.ONE;
      imageUrl = "";
      updateView();
    }

  }

  @Override
  public void setItemCode(final String code) {
    this.code = code.trim().toUpperCase();
    updateView();
  }

  @Override
  public void setItemName(final String name) {
    this.name = name.trim();
    updateView();
  }

  @Override
  public void setItemFullName(String fullName) {
    this.fullName = fullName;
    updateItem();
  }

  @Override
  public void setItemCategory(final CategoryProxy categoryProxy) {
    this.category = categoryProxy;
    updateView();
  }

  @Override
  public void setItemCurrency(final CurrencyProxy currencyProxy) {
    this.currency = currencyProxy;
    updateView();
  }

  @Override
  public void setItemUom(final UomProxy uomProxy) {
    this.uom = uomProxy;
    updateView();
  }

  @Override
  public void setItemUomRate(String uomRate) {
    this.uomRate = returnUomRate(uomRate);
    updateView();
  }

  @Override
  public void setItemImageUrl(String itemImageUrl) {
    this.imageUrl = itemImageUrl;
    updateView();
  }

  @Override
  public void createItem() {
    if (!isFormValid()) {
      return;
    }
    requestProvider.get().fetchByProperty("code", code)
        .fire(new Receiver<List<ItemProxy>>() {
          @Override
          public void onSuccess(List<ItemProxy> response) {
            if (response.isEmpty()) {
              ItemRequest request = requestProvider.get();
              ItemProxy proxy = request.create(ItemProxy.class);
              fillData(proxy);
              request.save(proxy)
                  .with(ItemProxy.CATEGORY)
                  .with(ItemProxy.CURRENCY)
                  .with(ItemProxy.UOM)
                  .fire(new Receiver<ItemProxy>() {
                    @Override
                    public void onSuccess(ItemProxy response) {
                      dataProvider.updateTableData();
                      updateView();
                    }
                  });
            } else {
              Window.alert("An item with that code already exist!");
            }
          }
        });
  }

  @Override
  public void updateItem() {
    if (!isFormValid()) {
      return;
    }
    requestProvider.get().fetchByProperty("code", code)
        .fire(new Receiver<List<ItemProxy>>() {
          @Override
          public void onSuccess(List<ItemProxy> response) {
            if (response.isEmpty()) {
              Window.alert("An item with that code does not exist!");
            } else {
              ItemRequest request = requestProvider.get();
              ItemProxy proxy = response.get(0);
              proxy = request.edit(proxy);
              fillData(proxy);
              request.save(proxy)
                  .with(ItemProxy.CATEGORY)
                  .with(ItemProxy.CURRENCY)
                  .with(ItemProxy.UOM)
                  .fire(new Receiver<ItemProxy>() {
                    @Override
                    public void onSuccess(ItemProxy response) {
                      dataProvider.updateTableData();
                      updateView();
                    }
                  });
            }
          }
        });
  }

  @Override
  public void prepareFromRequest(PlaceRequest placeRequest) {
    String idString = placeRequest.getParameter("id", "");
    try {
      id = Long.parseLong(idString);
    } catch (NumberFormatException e) {
      id = null;
    }
  }

  private void fillData(ItemProxy proxy) {
    proxy.setCode(code);
    proxy.setName(name);
    proxy.setFullName(fullName);
    proxy.setCategory(category);
    proxy.setCurrency(currency);
    proxy.setUom(uom);
    proxy.setUomRate(uomRate);
    proxy.setImageUrl(imageUrl);
  }

  private void updateView() {
    getView().setItemCode(code);
    getView().setItemName(name);
    getView().setItemFullName(fullName);
    getView().setItemCategory(category);
    getView().setItemCurrency(currency);
    getView().setItemUom(uom);
    getView().setItemUomRate(uomRate.toString());
    getView().setItemImageUrl(imageUrl);
    boolean isValid = isFormValid();
    getView().enableCreateButton(isValid);
    getView().enableUpdateButton(isValid);
  }

  private boolean isFormValid() {
    return !Strings.isNullOrEmpty(code)
        && !Strings.isNullOrEmpty(name)
        && !Strings.isNullOrEmpty(fullName)
        && category != null
        && currency != null
        && uom != null
        && (uomRate.compareTo(BigDecimal.ZERO) > 0);
  }

  private BigDecimal returnUomRate(String uomRate) {
    String filterOutNonDigits = CharMatcher.DIGIT.retainFrom(uomRate).trim();
    return new BigDecimal(filterOutNonDigits);
  }*/
}