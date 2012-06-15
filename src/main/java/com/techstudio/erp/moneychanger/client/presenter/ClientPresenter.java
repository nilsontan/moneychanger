/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.presenter;

import com.allen_sauer.gwt.log.client.Log;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.view.client.HasData;
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
import com.techstudio.erp.moneychanger.client.ui.HasSelectedValue;
import com.techstudio.erp.moneychanger.client.ui.dataprovider.*;
import com.techstudio.erp.moneychanger.client.view.ClientUiHandlers;
import com.techstudio.erp.moneychanger.shared.domain.ClientType;
import com.techstudio.erp.moneychanger.shared.proxy.*;
import com.techstudio.erp.moneychanger.shared.service.ClientRequest;
import com.techstudio.erp.moneychanger.shared.service.CompanyClientRequest;
import com.techstudio.erp.moneychanger.shared.service.IndividualClientRequest;

import java.util.List;

/**
 * @author Nilson
 */
public class ClientPresenter
    extends Presenter<ClientPresenter.MyView, ClientPresenter.MyProxy>
    implements ClientUiHandlers {

  /**
   * {@link com.techstudio.erp.moneychanger.client.presenter.ClientPresenter}'s proxy.
   */
  @ProxyCodeSplit
  @NameToken(NameTokens.CLIENT_PAGE)
  public interface MyProxy extends ProxyPlace<ClientPresenter> {
  }

  public interface MyView extends View, HasUiHandlers<ClientUiHandlers>,
      CompanyClientMyView {
    HasData<ClientProxy> getListing();

    HasSelectedValue<CountryProxy> getCountryListing();

    HasSelectedValue<IndividualClientProxy> getAuthorizedPersonListing();

    void showDetailPanel();

    void showListPanel();

    void showLoading(boolean visible);
  }

  public interface CompanyClientMyView extends ClientMyView {
    void setMChanger(boolean checked);

    void setRAgent(boolean checked);

    void setBizReg(String bizReg);

    void setLicenseNo(String licenseNo);

    void setWebsite(String website);

    void setAuthorizedPerson(IndividualClientProxy authorizedPerson);
  }

  public interface ClientMyView {
    void setName(String name);

    void setAddressLine1(String addressLine1);

    void setAddressLine2(String addressLine2);

    void setAddressLine3(String addressLine3);

    void setCountry(CountryProxy country);

    void setPostalCode(String postalCode);

    void setContactNo(String contactNo);

    void setContactNo2(String contactNo2);

    void setFaxNo(String faxNo);

    void setEmail(String email);
  }

  private final Provider<ClientRequest> requestProvider;
  private final Provider<IndividualClientRequest> individualClientRequestProvider;
  private final Provider<CompanyClientRequest> companyClientRequestProvider;
  private final MyDataProvider<ClientProxy> dataProvider;
  private final MyDataProvider<CountryProxy> countryDataProvider;
  private final MyDataProvider<IndividualClientProxy> individualClientDataProvider;

  private Action action;
  private ClientProxy proxy;
  private Long id;

  // shared
  private ClientType clientType;
  private String name;
  private String addressLine1;
  private String addressLine2;
  private String addressLine3;
  private CountryProxy country;
  private String postalCode;
  private String contactNo;
  private String contactNo2;
  private String faxNo;
  private String email;

  // company
  private boolean isMChanger;
  private boolean isRAgent;
  private String bizReg;
  private String licenseNo;
  private String website;
  private IndividualClientProxy authorizedPerson;

  @Inject
  public ClientPresenter(final EventBus eventBus,
                         final MyView view,
                         final MyProxy proxy,
                         final Provider<ClientRequest> requestProvider,
                         final Provider<IndividualClientRequest> individualClientRequestProvider,
                         final Provider<CompanyClientRequest> companyClientRequestProvider,
                         final ClientDataProvider dataProvider,
                         final CountryDataProvider countryDataProvider,
                         final IndividualClientDataProvider individualClientDataProvider) {
    super(eventBus, view, proxy);
    getView().setUiHandlers(this);
    getView().showLoading(true);
    this.requestProvider = requestProvider;
    this.individualClientRequestProvider = individualClientRequestProvider;
    this.companyClientRequestProvider = companyClientRequestProvider;
    this.dataProvider = dataProvider;
    this.countryDataProvider = countryDataProvider;
    this.individualClientDataProvider = individualClientDataProvider;
    this.dataProvider.firstLoad();
    this.dataProvider.addOnFirstLoadHandler(onFirstLoad);
    this.dataProvider.addDataDisplay(getView().getListing());
    this.countryDataProvider.addDataListDisplay(getView().getCountryListing());
    this.individualClientDataProvider.addDataListDisplay(getView().getAuthorizedPersonListing());
  }

  @Override
  protected void revealInParent() {
    RevealContentEvent.fire(this, MainPosPresenter.TYPE_SetMainContent, this);
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
  protected void onReset() {
    super.onReset();
    loadEntity();
    getView().showListPanel();
  }

  @Override
  public void onBack() {
    getView().showListPanel();
  }

  @Override
  public void onNext() {
    resetFields();
    loadEntity();
    getView().showDetailPanel();
  }

  @Override
  public void setClientType(ClientType clientType) {
    this.clientType = clientType;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public void setAddressLine1(String addressLine1) {
    this.addressLine1 = addressLine1;
  }

  @Override
  public void setAddressLine2(String addressLine2) {
    this.addressLine2 = addressLine2;
  }

  @Override
  public void setAddressLine3(String addressLine3) {
    this.addressLine3 = addressLine3;
  }

  @Override
  public void setCountry(CountryProxy country) {
    this.country = country;
  }

  @Override
  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  @Override
  public void setContactNo(String contactNo) {
    this.contactNo = contactNo;
  }

  @Override
  public void setContactNo2(String contactNo2) {
    this.contactNo2 = contactNo2;
  }

  @Override
  public void setFaxNo(String faxNo) {
    this.faxNo = faxNo;
  }

  @Override
  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public void setBizReg(String bizReg) {
    this.bizReg = bizReg;
  }

  @Override
  public void setLicenseNo(String licenseNo) {
    this.licenseNo = licenseNo;
  }

  @Override
  public void setWebsite(String website) {
    this.website = website;
  }

  @Override
  public void setAuthorizedPerson(IndividualClientProxy authorizedPerson) {
    this.authorizedPerson = authorizedPerson;
  }

  @Override
  public void toggleMChanger() {
    isMChanger = !isMChanger;
  }

  @Override
  public void toggleRAgent() {
    isRAgent = !isRAgent;
  }

  @Override
  public void edit(Long id) {
    this.id = id;
    loadEntity();
    action = Action.UPDATE;
    getView().showDetailPanel();
  }

  @Override
  public void create() {
    resetFields();
    action = Action.CREATE;
    getView().showDetailPanel();
  }

  @Override
  public void update() {
    if (!isFormValid()) {
      return;
    }
    requestProvider.get().fetch(id)
        .fire(new Receiver<ClientProxy>() {
          @Override
          public void onSuccess(ClientProxy response) {
            if (response == null) {
              Window.alert("That id " + id + " does not exist!");
              onBack();
            } else {
              doAction();
            }
          }
        });
  }

  @Override
  public void delete() {
    // Deletion of Client is not allowed
  }

  private void loadEntity() {
    if (id != null) {
      requestProvider.get()
          .fetch(id)
          .fire(new Receiver<ClientProxy>() {
            @Override
            public void onSuccess(ClientProxy client) {
              clientType = client.getClientType();
              switch (clientType) {
                case INDIVIDUAL:
                  individualClientRequestProvider.get()
                      .fetch(id)
                      .with(IndividualClientProxy.ADDRESS)
                      .with(IndividualClientProxy.NATIONALITY)
                      .fire(new Receiver<IndividualClientProxy>() {
                        @Override
                        public void onSuccess(IndividualClientProxy client) {
                          proxy = client;
                          name = client.getName();
                          addressLine1 = client.getAddress().getLine1();
                          addressLine2 = client.getAddress().getLine2();
                          addressLine3 = client.getAddress().getLine3();
                          country = countryDataProvider.getDefault();
                          postalCode = client.getAddress().getPostalCode();
                          contactNo = client.getContactNo();
                          contactNo2 = client.getContactNo2();
                          faxNo = client.getFaxNo();
                          email = client.getEmail();
                          updateView();
                        }
                      });
                  break;

                case COMPANY:
                  companyClientRequestProvider.get()
                      .fetch(id)
                      .with(CompanyClientProxy.ADDRESS)
                      .with(CompanyClientProxy.AUTHORIZED_PERSONS)
                      .fire(new Receiver<CompanyClientProxy>() {
                        @Override
                        public void onSuccess(CompanyClientProxy client) {
                          proxy = client;
                          name = client.getName();
                          addressLine1 = client.getAddress().getLine1();
                          addressLine2 = client.getAddress().getLine2();
                          addressLine3 = client.getAddress().getLine3();
                          country = countryDataProvider.getDefault();
                          postalCode = client.getAddress().getPostalCode();
                          contactNo = client.getContactNo();
                          contactNo2 = client.getContactNo2();
                          faxNo = client.getFaxNo();
                          email = client.getEmail();
                          isMChanger = client.getMoneyChanger();
                          isRAgent = client.getRemitter();
                          bizReg = client.getBizRegNo();
                          licenseNo = client.getLicenseNo();
                          website = client.getWebsite();
                          authorizedPerson = client.getAuthorizedPersons().isEmpty() ?
                              null : client.getAuthorizedPersons().get(0);
                          updateView();
                        }
                      });
                  break;
              }
            }
          });
    } else {
      resetFields();
      updateView();
    }

  }

  private void resetFields() {
    id = null;
    clientType = null;
    name = "";
    addressLine1 = "";
    addressLine2 = "";
    addressLine3 = "";
    country = countryDataProvider.getDefault();
    postalCode = "";
    contactNo = "";
    contactNo2 = "";
    faxNo = "";
    email = "";
    isMChanger = false;
    isRAgent = false;
    bizReg = "";
    licenseNo = "";
    website = "";
    authorizedPerson = null;
  }

  private void doAction() {
    switch (clientType) {
      case INDIVIDUAL:
        IndividualClientRequest iRequest = individualClientRequestProvider.get();
        IndividualClientProxy iProxy;
        if (action.equals(Action.CREATE)) {
          iProxy = iRequest.create(IndividualClientProxy.class);
        } else if (action.equals(Action.UPDATE)) {
          iProxy = (IndividualClientProxy) iRequest.edit(proxy);
        } else {
          Log.debug("Error at action: " + action);
          return;
        }
        iProxy.setName(name);

        iRequest.save(iProxy)
            .with(IndividualClientProxy.ADDRESS)
            .with(IndividualClientProxy.NATIONALITY)
            .fire(new Receiver<IndividualClientProxy>() {
              @Override
              public void onSuccess(IndividualClientProxy proxy) {
                Log.info("Client saved: " + proxy);
                dataProvider.updateData();
                resetFields();
                Timer timer = new Timer() {
                  @Override
                  public void run() {
                    getView().showListPanel();
                  }
                };

                timer.schedule(300);
              }
            });
        break;

      case COMPANY:
        CompanyClientRequest cRequest = companyClientRequestProvider.get();
        CompanyClientProxy cProxy;
        if (action.equals(Action.CREATE)) {
          cProxy = cRequest.create(CompanyClientProxy.class);
        } else if (action.equals(Action.UPDATE)) {
          cProxy = (CompanyClientProxy) cRequest.edit(proxy);
        } else {
          Log.debug("Error at action: " + action);
          return;
        }
        cProxy.setName(name);

        AddressProxy addressProxy = cRequest.create(AddressProxy.class);
        addressProxy.setLine1(addressLine1);
        addressProxy.setLine2(addressLine2);
        addressProxy.setLine3(addressLine3);
        addressProxy.setPostalCode(postalCode);
        cProxy.setAddress(addressProxy);

        cProxy.setCountry(country);
        cProxy.setContactNo(contactNo);
        cProxy.setContactNo2(contactNo2);
        cProxy.setFaxNo(faxNo);
        cProxy.setEmail(email);
        cProxy.setMoneyChanger(isMChanger);
        cProxy.setRemitter(isRAgent);
        cProxy.setBizRegNo(bizReg);
        cProxy.setLicenseNo(licenseNo);
        cProxy.setWebsite(website);
        List<IndividualClientProxy> list = Lists.newArrayList();
        list.add(authorizedPerson);
        cProxy.setAuthorizedPersons(list);

        cRequest.save(cProxy)
            .with(CompanyClientProxy.ADDRESS)
            .with(CompanyClientProxy.AUTHORIZED_PERSONS)
            .fire(new Receiver<CompanyClientProxy>() {
              @Override
              public void onSuccess(CompanyClientProxy proxy) {
                String printout =
                    Objects.toStringHelper(proxy)
                        .add("id", proxy.getId())
                        .add("name", proxy.getName())
                        .add("type", proxy.getClientType())
                        .toString();
                Log.info("Client saved: " + printout);
                dataProvider.updateData();
                resetFields();
                Timer timer = new Timer() {
                  @Override
                  public void run() {
                    getView().showListPanel();
                  }
                };

                timer.schedule(300);
              }
            });
        break;

      default:
        throw new RuntimeException("clientType is not set " + clientType);
    }
  }

  private void updateView() {
    getView().setName(name);
    getView().setAddressLine1(addressLine1);
    getView().setAddressLine2(addressLine2);
    getView().setAddressLine3(addressLine3);
    getView().setCountry(country);
    getView().setPostalCode(postalCode);
    getView().setContactNo(contactNo);
    getView().setContactNo2(contactNo2);
    getView().setFaxNo(faxNo);
    getView().setEmail(email);
    getView().setMChanger(isMChanger);
    getView().setRAgent(isRAgent);
    getView().setBizReg(bizReg);
    getView().setLicenseNo(licenseNo);
    getView().setWebsite(website);
    getView().setAuthorizedPerson(authorizedPerson);
  }

  private boolean isFormValid() {
    return !Strings.isNullOrEmpty(name);
  }

  private enum Action {
    CREATE, UPDATE
  }
}
