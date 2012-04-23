/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.admin.view;

import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.techstudio.erp.moneychanger.client.admin.presenter.ItemPresenter;
import com.techstudio.erp.moneychanger.client.resources.Resources;
import com.techstudio.erp.moneychanger.client.ui.HasSelectedValue;
import com.techstudio.erp.moneychanger.client.ui.ItemLinkCell;
import com.techstudio.erp.moneychanger.client.ui.ItemMenuImageUploader;
import com.techstudio.erp.moneychanger.client.ui.SelectOneListBox;
import com.techstudio.erp.moneychanger.shared.proxy.CategoryProxy;
import com.techstudio.erp.moneychanger.shared.proxy.CurrencyProxy;
import com.techstudio.erp.moneychanger.shared.proxy.ItemProxy;
import com.techstudio.erp.moneychanger.shared.proxy.UomProxy;
import gwtupload.client.IUploadStatus;
import gwtupload.client.IUploader;

/**
 * @author Nilson
 */
public class ItemView
    extends ViewWithUiHandlers<ItemUiHandlers>
    implements ItemPresenter.MyView {

  public interface Binder extends UiBinder<Widget, ItemView> {
  }

  private final Widget widget;

  final Resources resources;

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
          String url = uploader.getServletPath() + "?blob-key=" + uploader.getServerInfo().message;
//          String url = uploader.fileUrl();
//          new PreloadedImage(url, showImage);
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
      itemImageUploader.setImageResource(resources.noImageAvailable());
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

    Column<ItemProxy, Long> linkColumn = new Column<ItemProxy, Long>(new ItemLinkCell()) {
      @Override
      public Long getValue(ItemProxy itemProxy) {
        return itemProxy.getId();
      }
    };
    itemTable.addColumn(linkColumn);

    itemTable.setPageSize(10);

    itemPager.setDisplay(itemTable);
  }
}