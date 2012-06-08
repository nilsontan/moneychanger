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
import com.techstudio.erp.moneychanger.client.presenter.ItemPresenter;
import com.techstudio.erp.moneychanger.client.ui.*;
import com.techstudio.erp.moneychanger.client.ui.cell.ItemCell;
import com.techstudio.erp.moneychanger.shared.proxy.CategoryProxy;
import com.techstudio.erp.moneychanger.shared.proxy.ItemProxy;

/**
 * @author Nilson
 */
public class ItemView
    extends ViewWithUiHandlers<ItemUiHandlers>
    implements ItemPresenter.MyView {

  public interface Binder extends UiBinder<Widget, ItemView> {
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

  @UiField
  HTMLPanel listPanel;

  @UiField
  HTMLPanel detailPanel;

  @UiField(provided = true)
  CellList<ItemProxy> list = new CellList<ItemProxy>(new ItemCell());

  @UiField
  ShowMorePagerPanel pagerPanel = new ShowMorePagerPanel();

  @UiField
  RangeLabelPager pager = new RangeLabelPager();

  @UiField
  LabelInput code;

  @UiField
  LabelInput name;

  @UiField
  LabelInput fullname;

  @UiField
  LabelInput uomRate;

  @UiField(provided = true)
  SelectOneListBox<CategoryProxy> categoryList
      = new SelectOneListBox<CategoryProxy>(new SelectOneListBox.OptionFormatter<CategoryProxy>() {
    @Override
    public String getLabel(CategoryProxy option) {
      return option.getName();
    }

    @Override
    public String getValue(CategoryProxy option) {
      return option.getId().toString();
    }
  });

  @UiField
  Button add;

  @UiField
  Button save;

  @UiField
  Button delete;

  @Inject
  public ItemView(final Binder binder) {
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
    getUiHandlers().onBack();
  }

  @SuppressWarnings("unused")
  @UiHandler("ancNext")
  public void onClickNext(ClickEvent event) {
    getUiHandlers().onNext();
  }

  @UiHandler("code")
  public void onCodeChange(ValueChangeEvent<String> event) {
    getUiHandlers().setCode(event.getValue());
  }

  @UiHandler("name")
  public void onNameChange(ValueChangeEvent<String> event) {
    getUiHandlers().setName(event.getValue());
  }

  @UiHandler("fullname")
  public void onFullnameChange(ValueChangeEvent<String> event) {
    getUiHandlers().setFullName(event.getValue());
  }

  @UiHandler("uomRate")
  public void onUomRateChange(ValueChangeEvent<String> event) {
    getUiHandlers().setUomRate(event.getValue());
  }

  @SuppressWarnings("unused")
  @UiHandler("categoryList")
  public void onCategoryChange(ValueChangeEvent<CategoryProxy> event) {
    getUiHandlers().setCategory(categoryList.getSelectedValue());
  }

  @SuppressWarnings("unused")
  @UiHandler("add")
  public void onCreate(ClickEvent event) {
    getUiHandlers().create();
  }

  @SuppressWarnings("unused")
  @UiHandler("save")
  public void onUpdate(ClickEvent event) {
    getUiHandlers().update();
  }

  @SuppressWarnings("unused")
  @UiHandler("delete")
  public void onDelete(ClickEvent event) {
    getUiHandlers().delete();
  }

  @Override
  public HasData<ItemProxy> getListing() {
    return list;
  }

  @Override
  public HasSelectedValue<CategoryProxy> getCategoryList() {
    return categoryList;
  }

  @Override
  public void showListPanel() {
//    mainPanel.setStyleName("slider show1");
//    list.setVisible(true);
    listPanel.setVisible(true);
    detailPanel.setVisible(false);
    ancHome.setVisible(true);
    ancNext.setVisible(true);
    ancBack.setVisible(false);
  }

  @Override
  public void showDetailPanel() {
//    mainPanel.setStyleName("slider show2");
//    list.setVisible(false);
    listPanel.setVisible(false);
    detailPanel.setVisible(true);
    ancHome.setVisible(false);
    ancNext.setVisible(false);
    ancBack.setVisible(true);
  }

  @Override
  public void showAddButtons() {
    add.setVisible(true);
    save.setVisible(false);
    delete.setVisible(false);
  }

  @Override
  public void showEditButtons() {
    add.setVisible(false);
    save.setVisible(true);
    delete.setVisible(true);
  }

  @Override
  public void setCode(String code) {
    this.code.setValue(code);
  }

  @Override
  public void setName(String name) {
    this.name.setValue(name);
  }

  @Override
  public void setFullname(String fullname) {
    this.fullname.setValue(fullname);
  }

  @Override
  public void setCategory(CategoryProxy categoryProxy) {
    this.categoryList.setSelectedValue(categoryProxy);
  }

  @Override
  public void setUomRate(String uomRate) {
    this.uomRate.setValue(uomRate);
  }

  @Override
  public void showLoading(boolean visible) {
    loadingMessage.setVisible(visible);
    currentStep.setVisible(!visible);
    mainPanel.setVisible(!visible);
    ancBar.setVisible(!visible);
  }

  private void setUpListing() {
    final NoSelectionModel<ItemProxy> selectionModel = new NoSelectionModel<ItemProxy>();
    list.setSelectionModel(selectionModel);
    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
      @Override
      public void onSelectionChange(SelectionChangeEvent event) {
        ItemProxy selected = selectionModel.getLastSelectedObject();
        if (selected != null) {
          getUiHandlers().edit(selected.getCode());
        }
      }
    });

    pagerPanel.setDisplay(list);
    pager.setDisplay(list);
  }

  /*final Resources resources;

  @UiField(provided = true)
  ItemMenuImageUploader itemImageUploader;

  @UiField
  TextBox itemCode;

  @UiField
  TextBox itemName;

  @UiField
  TextBox itemFullName;

  @UiField(provided = true)
  SelectOneListBox<CategoryProxy> itemCategoryList
      = new SelectOneListBox<CategoryProxy>(new SelectOneListBox.OptionFormatter<CategoryProxy>() {
    @Override
    public String getLabel(CategoryProxy option) {
      return option.getName();
    }

    @Override
    public String getValue(CategoryProxy option) {
      return option.getId().toString();
    }
  });

  @UiField(provided = true)
  SelectOneListBox<CurrencyProxy> itemCurrencyList
      = new SelectOneListBox<CurrencyProxy>(new SelectOneListBox.OptionFormatter<CurrencyProxy>() {
    @Override
    public String getLabel(CurrencyProxy option) {
      return option.getName();
    }

    @Override
    public String getValue(CurrencyProxy option) {
      return option.getId().toString();
    }
  });

  @UiField(provided = true)
  SelectOneListBox<UomProxy> itemUomList
      = new SelectOneListBox<UomProxy>(new SelectOneListBox.OptionFormatter<UomProxy>() {
    @Override
    public String getLabel(UomProxy option) {
      return option.getName();
    }

    @Override
    public String getValue(UomProxy option) {
      return option.getId().toString();
    }
  });

  @UiField
  TextBox itemUomRate;

  @UiField
  Button itemUpdate;

  @UiField
  Button itemCreate;

  @UiField
  CellTable<ItemProxy> itemTable = new CellTable<ItemProxy>();

  @UiField
  SimplePager itemPager = new SimplePager();

  @Inject
  public ItemView(Binder binder, Resources resources, ItemMenuImageUploader itemMenuImageUploader) {
    this.resources = resources;
    this.itemImageUploader = itemMenuImageUploader;
    widget = binder.createAndBindUi(this);
    setupItemTable();
    IUploader.OnFinishUploaderHandler onFinishUploaderHandler = new IUploader.OnFinishUploaderHandler() {
      public void onFinish(IUploader uploader) {
        if (uploader.getStatus() == IUploadStatus.Status.SUCCESS) {
//          String url = uploader.getServletPath() + "?blob-key=" + uploader.getServerInfo().message;
//          String url = uploader.fileUrl();
//          new PreloadedImage(url, showImage);
          String url = uploader.getServerInfo().message;
          getUiHandlers().setItemImageUrl(url);
        }
      }
    };
    itemImageUploader.addOnFinishUploadHandler(onFinishUploaderHandler);
  }

  @Override
  public Widget asWidget() {
    return widget;
  }

  @UiHandler("itemCode")
  public void onItemCodeChange(ValueChangeEvent<String> event) {
    getUiHandlers().setItemCode(event.getValue());
  }

  @UiHandler("itemName")
  public void onItemNameChange(ValueChangeEvent<String> event) {
    getUiHandlers().setItemName(event.getValue());
  }

  @UiHandler("itemFullName")
  public void onItemFullNameChange(ValueChangeEvent<String> event) {
    getUiHandlers().setItemFullName(event.getValue());
  }

  @SuppressWarnings("unused")
  @UiHandler("itemCategoryList")
  public void onItemCategoryChange(ValueChangeEvent<CategoryProxy> event) {
    getUiHandlers().setItemCategory(itemCategoryList.getSelectedValue());
  }

  @SuppressWarnings("unused")
  @UiHandler("itemCurrencyList")
  public void onItemCurrencyChange(ValueChangeEvent<CurrencyProxy> event) {
    getUiHandlers().setItemCurrency(itemCurrencyList.getSelectedValue());
  }

  @SuppressWarnings("unused")
  @UiHandler("itemUomList")
  public void onItemUomChange(ValueChangeEvent<UomProxy> event) {
    getUiHandlers().setItemUom(itemUomList.getSelectedValue());
  }

  @UiHandler("itemUomRate")
  public void onItemUomRateChange(ValueChangeEvent<String> event) {
    getUiHandlers().setItemUomRate(event.getValue());
  }

  @SuppressWarnings("unused")
  @UiHandler("itemCreate")
  public void onCreateCurrency(ClickEvent event) {
    getUiHandlers().createItem();
  }

  @SuppressWarnings("unused")
  @UiHandler("itemUpdate")
  public void onUpdateCurrency(ClickEvent event) {
    getUiHandlers().updateItem();
  }

  @Override
  public HasData<ItemProxy> getItemTable() {
    return itemTable;
  }

  @Override
  public HasSelectedValue<CategoryProxy> getCategoryList() {
    return itemCategoryList;
  }

  @Override
  public HasSelectedValue<CurrencyProxy> getCurrencyList() {
    return itemCurrencyList;
  }

  @Override
  public HasSelectedValue<UomProxy> getUomList() {
    return itemUomList;
  }

  @Override
  public void enableCreateButton(boolean isValid) {
    itemCreate.setEnabled(isValid);
  }

  @Override
  public void enableUpdateButton(boolean isValid) {
    itemUpdate.setEnabled(isValid);
  }

  @Override
  public void setItemCode(String code) {
    itemCode.setValue(code);
  }

  @Override
  public void setItemName(String name) {
    itemName.setValue(name);
  }

  @Override
  public void setItemFullName(String fullName) {
    itemFullName.setValue(fullName);
  }

  @Override
  public void setItemCategory(CategoryProxy categoryProxy) {
    itemCategoryList.setSelectedValue(categoryProxy);
  }

  //TODO:Nilson problem with ListBox unselecting itself
  @Override
  public void setItemCurrency(CurrencyProxy currencyProxy) {
    itemCurrencyList.setSelectedValue(currencyProxy);
  }

  @Override
  public void setItemUom(UomProxy uomProxy) {
    itemUomList.setSelectedValue(uomProxy);
  }

  @Override
  public void setItemUomRate(String uomRate) {
    itemUomRate.setValue(uomRate);
  }

  @Override
  public void setItemImageUrl(String itemImageUrl) {
    if (itemImageUrl.isEmpty()) {
      itemImageUploader.setImageResource(resources.iNoImageAvailable());
    } else {
      itemImageUploader.setImageUrl(itemImageUrl);
    }
  }

  private void setupItemTable() {
    Column<ItemProxy, String> itemCodeColumn = new Column<ItemProxy, String>(new EditTextCell()) {
      @Override
      public String getValue(ItemProxy itemProxy) {
        return itemProxy.getCode();
      }
    };
    itemTable.addColumn(itemCodeColumn, "Code");

    Column<ItemProxy, String> itemNameColumn = new Column<ItemProxy, String>(new EditTextCell()) {
      @Override
      public String getValue(ItemProxy itemProxy) {
        return itemProxy.getName();
      }
    };
    itemTable.addColumn(itemNameColumn, "Name");

    Column<ItemProxy, String> itemFullNameColumn = new Column<ItemProxy, String>(new EditTextCell()) {
      @Override
      public String getValue(ItemProxy itemProxy) {
        return itemProxy.getFullName();
      }
    };
    itemTable.addColumn(itemFullNameColumn, "Full Name");

    Column<ItemProxy, String> itemCategoryColumn = new Column<ItemProxy, String>(new EditTextCell()) {
      @Override
      public String getValue(ItemProxy itemProxy) {
        return itemProxy.getCategory().getName();
      }
    };
    itemTable.addColumn(itemCategoryColumn, "Category");

    Column<ItemProxy, String> itemCurrencyColumn = new Column<ItemProxy, String>(new EditTextCell()) {
      @Override
      public String getValue(ItemProxy itemProxy) {
        return itemProxy.getCurrency().getName();
      }
    };
    itemTable.addColumn(itemCurrencyColumn, "Currency");

    Column<ItemProxy, String> itemUomColumn = new Column<ItemProxy, String>(new EditTextCell()) {
      @Override
      public String getValue(ItemProxy itemProxy) {
        return itemProxy.getUom().getName();
      }
    };
    itemTable.addColumn(itemUomColumn, "Uom");

    Column<ItemProxy, String> itemUomRateColumn = new Column<ItemProxy, String>(new EditTextCell()) {
      @Override
      public String getValue(ItemProxy itemProxy) {
        return itemProxy.getUomRate().toString();
      }
    };
    itemTable.addColumn(itemUomRateColumn, "Uom Rate");

//    Column<ItemProxy, Long> linkColumn = new Column<ItemProxy, Long>(new ItemLinkCell()) {
//      @Override
//      public Long getValue(ItemProxy itemProxy) {
//        return itemProxy.getId();
//      }
//    };
//    itemTable.addColumn(linkColumn);

    itemTable.setPageSize(10);

    itemPager.setDisplay(itemTable);
  }*/
}