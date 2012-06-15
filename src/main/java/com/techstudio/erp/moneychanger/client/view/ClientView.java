/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.techstudio.erp.moneychanger.client.NameTokens;
import com.techstudio.erp.moneychanger.client.presenter.ClientPresenter;
import com.techstudio.erp.moneychanger.client.ui.*;
import com.techstudio.erp.moneychanger.client.ui.cell.ClientCell;
import com.techstudio.erp.moneychanger.client.ui.cell.ClientTypeCell;
import com.techstudio.erp.moneychanger.shared.domain.ClientType;
import com.techstudio.erp.moneychanger.shared.proxy.ClientProxy;
import com.techstudio.erp.moneychanger.shared.proxy.CountryProxy;
import com.techstudio.erp.moneychanger.shared.proxy.IndividualClientProxy;

/**
 * @author Nilson
 */
public class ClientView
    extends ViewWithUiHandlers<ClientUiHandlers>
    implements ClientPresenter.MyView {

  public interface Binder extends UiBinder<Widget, ClientView> {
  }

  private final Widget widget;

  @UiField
  HTMLPanel ancBar;

  @UiField
  Anchor ancHome;

  @UiField
  Anchor ancBack;

  @UiField
  Anchor ancNext;

  @UiField
  HTMLPanel loadingMessage;

  @UiField
  Label currentStep;

  @UiField
  HTMLPanel mainPanel;

  /**
   * Category Panel
   */

  @UiField
  HTMLPanel clientPulldown;

  @UiField(provided = true)
  CellList<ClientType> clientList = new CellList<ClientType>(new ClientTypeCell());

  @UiField
  HTMLPanel listPanel;

  @UiField
  HTMLPanel detailPanel;

  @UiField(provided = true)
  CellList<ClientProxy> list = new CellList<ClientProxy>(new ClientCell());

  @UiField
  ShowMorePagerPanel pagerPanel = new ShowMorePagerPanel();

  @UiField
  RangeLabelPager pager = new RangeLabelPager();

  @UiField
  LabelInput name;

  @UiField
  MyCheckBox cBoxMChanger;

  @UiField
  MyCheckBox cBoxRAgent;

  @UiField
  LabelInput bizReg;

  @UiField
  LabelInput licenseNo;

  @UiField
  LabelInput addressLine1;

  @UiField
  TextBox addressLine2;

  @UiField
  TextBox addressLine3;

  @UiField
  LabelInput postalCode;

  @UiField
  LabelInput contactNo1;

  @UiField
  LabelInput contactNo2;

  @UiField
  LabelInput contactFax;

  @UiField
  LabelInput contactOther;

  @UiField
  LabelInput email;

  @UiField
  LabelInput website;

  @UiField(provided = true)
  SelectOneListBox<CountryProxy> countryList
      = new SelectOneListBox<CountryProxy>(new SelectOneListBox.OptionFormatter<CountryProxy>() {
    @Override
    public String getLabel(CountryProxy option) {
      return option.getName();
    }

    @Override
    public String getValue(CountryProxy option) {
      return option.getId().toString();
    }
  });

  @UiField(provided = true)
  SelectOneListBox<IndividualClientProxy> authorizedList
      = new SelectOneListBox<IndividualClientProxy>(new SelectOneListBox.OptionFormatter<IndividualClientProxy>() {
    @Override
    public String getLabel(IndividualClientProxy option) {
      return option.getName();
    }

    @Override
    public String getValue(IndividualClientProxy option) {
      return option.getId().toString();
    }
  });

  @UiField
  Button save;

  @Inject
  public ClientView(final Binder binder) {
    widget = binder.createAndBindUi(this);
    setUpListing();
  }

  @Override
  public Widget asWidget() {
    return widget;
  }

  @SuppressWarnings("unused")
  @UiHandler("ancHome")
  public void onClickHome(ClickEvent event) {
    History.newItem(NameTokens.getMenuPage());
  }

  @SuppressWarnings("unused")
  @UiHandler("ancBack")
  public void onClickBack(ClickEvent event) {
    showListPanel();
  }

//  @SuppressWarnings("unused")
//  @UiHandler("ancNext")
//  public void onClickNext(ClickEvent event) {
//    getUiHandlers().create();
//  }

  @SuppressWarnings("unused")
  @UiHandler("ancNext")
  public void onClickMenu(ClickEvent event) {
    if (mainPanel.getStyleName().contains("showPulldown")) {
      mainPanel.removeStyleName("showPulldown");
    } else {
      mainPanel.addStyleName("showPulldown");
    }
  }

  @UiHandler("name")
  void onClientNameChange(ValueChangeEvent<String> event) {
    getUiHandlers().setName(event.getValue());
  }

  @SuppressWarnings("unused")
  @UiHandler("cBoxMChanger")
  void onClickMChanger(ClickEvent event) {
    getUiHandlers().toggleMChanger();
  }

  @SuppressWarnings("unused")
  @UiHandler("cBoxRAgent")
  void onClickRAgent(ClickEvent event) {
    getUiHandlers().toggleRAgent();
  }

  @UiHandler("bizReg")
  void onChangeBizReg(ValueChangeEvent<String> event) {
    getUiHandlers().setBizReg(event.getValue());
  }

  @UiHandler("licenseNo")
  void onChangeLicenseNo(ValueChangeEvent<String> event) {
    getUiHandlers().setLicenseNo(event.getValue());
  }

  @UiHandler("addressLine1")
  void onChangeAddressLine1(ValueChangeEvent<String> event) {
    getUiHandlers().setAddressLine1(event.getValue());
  }

  @UiHandler("addressLine2")
  void onChangeAddressLine2(ValueChangeEvent<String> event) {
    getUiHandlers().setAddressLine2(event.getValue());
  }

  @UiHandler("addressLine3")
  void onChangeAddressLine3(ValueChangeEvent<String> event) {
    getUiHandlers().setAddressLine3(event.getValue());
  }

  @SuppressWarnings("unused")
  @UiHandler("countryList")
  public void onChangeCountry(ValueChangeEvent<CountryProxy> event) {
    getUiHandlers().setCountry(countryList.getSelectedValue());
  }

  @UiHandler("postalCode")
  void onChangePostalCode(ValueChangeEvent<String> event) {
    getUiHandlers().setPostalCode(event.getValue());
  }

  @SuppressWarnings("unused")
  @UiHandler("authorizedList")
  public void onChangeAuthorizedPerson(ValueChangeEvent<IndividualClientProxy> event) {
    getUiHandlers().setAuthorizedPerson(authorizedList.getSelectedValue());
  }

  @UiHandler("email")
  void onChangeEmail(ValueChangeEvent<String> event) {
    getUiHandlers().setEmail(event.getValue());
  }

  @UiHandler("website")
  void onChangeWebsite(ValueChangeEvent<String> event) {
    getUiHandlers().setWebsite(event.getValue());
  }

  @SuppressWarnings("unused")
  @UiHandler("save")
  void onCreateClient(ClickEvent event) {
    getUiHandlers().update();
  }

  @Override
  public HasData<ClientProxy> getListing() {
    return list;
  }

  @Override
  public HasSelectedValue<CountryProxy> getCountryListing() {
    return countryList;
  }

  @Override
  public HasSelectedValue<IndividualClientProxy> getAuthorizedPersonListing() {
    return authorizedList;
  }

  @Override
  public void showListPanel() {
//    mainPanel.setStyleName("slider show1");
//    list.setVisible(true);
    listPanel.setVisible(true);
    detailPanel.setVisible(false);
    ancHome.setVisible(true);
    ancBack.setVisible(false);
    ancNext.setVisible(true);
  }

  @Override
  public void showDetailPanel() {
//    mainPanel.setStyleName("slider show2");
//    list.setVisible(false);
    listPanel.setVisible(false);
    detailPanel.setVisible(true);
    ancHome.setVisible(false);
    ancBack.setVisible(true);
    ancNext.setVisible(false);
  }

  @Override
  public void setName(String name) {
    this.name.setValue(name);
  }

  @Override
  public void setAddressLine1(String addressLine1) {
    this.addressLine1.setValue(addressLine1);
  }

  @Override
  public void setAddressLine2(String addressLine2) {
    this.addressLine2.setValue(addressLine2);
  }

  @Override
  public void setAddressLine3(String addressLine3) {
    this.addressLine3.setValue(addressLine3);
  }

  @Override
  public void setCountry(CountryProxy country) {
    this.countryList.setSelectedValue(country);
  }

  @Override
  public void setPostalCode(String postalCode) {
    this.postalCode.setValue(postalCode);
  }

  @Override
  public void setContactNo(String contactNo) {
    this.contactNo1.setValue(contactNo);
  }

  @Override
  public void setContactNo2(String contactNo2) {
    this.contactNo2.setValue(contactNo2);
  }

  @Override
  public void setFaxNo(String faxNo) {
    this.contactFax.setValue(faxNo);
  }

  @Override
  public void setEmail(String email) {
    this.email.setValue(email);
  }

  @Override
  public void showLoading(boolean visible) {
    loadingMessage.setVisible(visible);
    mainPanel.setVisible(!visible);
    ancBar.setVisible(!visible);
  }

  @Override
  public void setMChanger(boolean checked) {
    cBoxMChanger.setChecked(checked);
  }

  @Override
  public void setRAgent(boolean checked) {
    cBoxRAgent.setChecked(checked);
  }

  @Override
  public void setBizReg(String bizReg) {
    this.bizReg.setValue(bizReg);
  }

  @Override
  public void setLicenseNo(String licenseNo) {
    this.licenseNo.setValue(licenseNo);
  }

  @Override
  public void setWebsite(String website) {
    this.website.setValue(website);
  }

  @Override
  public void setAuthorizedPerson(IndividualClientProxy authorizedPerson) {
    this.authorizedList.setSelectedValue(authorizedPerson);
  }

  private void setUpListing() {
    final NoSelectionModel<ClientProxy> selectionModel = new NoSelectionModel<ClientProxy>();
    list.setSelectionModel(selectionModel);
    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
      @Override
      public void onSelectionChange(SelectionChangeEvent event) {
        ClientProxy selected = selectionModel.getLastSelectedObject();
        if (selected != null) {
          getUiHandlers().edit(selected.getId());
        }
      }
    });

    pagerPanel.setDisplay(list);
    pager.setDisplay(list);

    final NoSelectionModel<ClientType> clientSelectionModel = new NoSelectionModel<ClientType>();
    clientList.setSelectionModel(clientSelectionModel);
    clientSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
      @Override
      public void onSelectionChange(SelectionChangeEvent event) {
        ClientType selected = clientSelectionModel.getLastSelectedObject();
        if (selected != null) {
          getUiHandlers().setClientType(selected);
          getUiHandlers().create();
          mainPanel.removeStyleName("showPulldown");
        }
      }
    });
  }

}