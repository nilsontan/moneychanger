/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import com.techstudio.erp.moneychanger.client.presenter.TransactionPresenter;
import com.techstudio.erp.moneychanger.client.ui.*;
import com.techstudio.erp.moneychanger.client.ui.cell.TransactionCell;
import com.techstudio.erp.moneychanger.shared.proxy.CategoryProxy;
import com.techstudio.erp.moneychanger.shared.proxy.ItemProxy;
import com.techstudio.erp.moneychanger.shared.proxy.LineItemProxy;
import com.techstudio.erp.moneychanger.shared.proxy.TransactionProxy;

import java.util.Arrays;
import java.util.List;

/**
 * @author Nilson
 */
public class TransactionView
    extends ViewWithUiHandlers<TransactionUiHandlers>
    implements TransactionPresenter.MyView {

  public interface Binder extends UiBinder<Widget, TransactionView> {
  }

  private final Widget widget;

  @UiField
  HTMLPanel ancBar;

  @UiField
  Anchor ancHome;

  @UiField
  Anchor ancBack;

  @UiField
  HTMLPanel loadingMessage;

  @UiField
  Label currentStep;

  @UiField
  HTMLPanel mainPanel;

  @UiField
  HTMLPanel searchPanel;

  @UiField
  Button search;

  @UiField
  LabelInput sDate;

  @UiField(provided = true)
  SelectOneListBox<CategoryProxy> sCategory
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
  SelectOneListBox<ItemProxy> sItem
      = new SelectOneListBox<ItemProxy>(new SelectOneListBox.OptionFormatter<ItemProxy>() {
    @Override
    public String getLabel(ItemProxy option) {
      return option.getCode();
    }

    @Override
    public String getValue(ItemProxy option) {
      return option.getId().toString();
    }
  });

//  @UiField(provided = true)
//  SelectOneListBox<CategoryProxy> sClient
//      = new SelectOneListBox<CategoryProxy>(new SelectOneListBox.OptionFormatter<CategoryProxy>() {
//    @Override
//    public String getLabel(CategoryProxy option) {
//      return option.getName();
//    }
//
//    @Override
//    public String getValue(CategoryProxy option) {
//      return option.getId().toString();
//    }
//  });

  @UiField(provided = true)
  SelectOneListBox<Boolean> sPending
      = new SelectOneListBox<Boolean>(new SelectOneListBox.OptionFormatter<Boolean>() {
    @Override
    public String getLabel(Boolean option) {
      if (option) {
        return "Yes";
      } else {
        return "No";
      }
    }

    @Override
    public String getValue(Boolean option) {
      return option.toString();
    }
  });

  @UiField
  HTMLPanel listPanel;

  @UiField(provided = true)
  CellList<TransactionProxy> list = new CellList<TransactionProxy>(new TransactionCell());

  @UiField
  ShowMorePagerPanel pagerPanel = new ShowMorePagerPanel();

  @UiField
  RangeLabelPager pager = new RangeLabelPager();

  @UiField
  HTMLPanel detailPanel;

  @UiField
  OrderedList lineItemTable;

  @Inject
  public TransactionView(Binder binder) {
    widget = binder.createAndBindUi(this);
    sCategory.includesAll(true);
    sItem.includesAll(true);
    sPending.includesAll(true);
    sPending.setOptions(Arrays.asList(Boolean.TRUE, Boolean.FALSE));
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
    if (listPanel.isVisible()) {
      showSearchPanel();
    } else if (detailPanel.isVisible()) {
      showListPanel();
    }
  }

  @SuppressWarnings("unused")
  @UiHandler("search")
  public void onSearch(ClickEvent event) {
    getUiHandlers().search();
  }

  @UiHandler("sDate")
  public void onDateChange(ValueChangeEvent<String> event) {
    getUiHandlers().setSearchDate(event.getValue());
  }

  @SuppressWarnings("unused")
  @UiHandler("sCategory")
  public void onCategoryChange(ValueChangeEvent<CategoryProxy> event) {
    getUiHandlers().setSearchCategory(sCategory.getSelectedValue());
  }

  @SuppressWarnings("unused")
  @UiHandler("sItem")
  public void onItemChange(ValueChangeEvent<ItemProxy> event) {
    getUiHandlers().setSearchItem(sItem.getSelectedValue());
  }

//  @SuppressWarnings("unused")
//  @UiHandler("sClient")
//  public void onCategoryChange(ValueChangeEvent<CategoryProxy> event) {
//    getUiHandlers().setCategory(sCategory.getSelectedValue());
//  }

  @SuppressWarnings("unused")
  @UiHandler("sPending")
  public void onPendingChange(ValueChangeEvent<Boolean> event) {
    getUiHandlers().setSearchPending(sPending.getSelectedValue());
  }

//  @SuppressWarnings("unused")
//  @UiHandler("prAdd")
//  public void onCreate(ClickEvent event) {
//    getUiHandlers().create();
//  }

//  @SuppressWarnings("unused")
//  @UiHandler("save")
//  public void onUpdate(ClickEvent event) {
//    getUiHandlers().update();
//  }

//  @SuppressWarnings("unused")
//  @UiHandler("prDel")
//  public void onDelete(ClickEvent event) {
//    getUiHandlers().delete();
//  }

  @Override
  public HasSelectedValue<CategoryProxy> getCategoryList() {
    return sCategory;
  }

  @Override
  public HasSelectedValue<ItemProxy> getItemList() {
    return sItem;
  }

  @Override
  public HasData<TransactionProxy> getListing() {
    return list;
  }

  @Override
  public void showSearchPanel() {
    searchPanel.setVisible(true);
    listPanel.setVisible(false);
    detailPanel.setVisible(false);
    ancHome.setVisible(true);
    ancBack.setVisible(false);
  }

  @Override
  public void showListPanel() {
//    mainPanel.setStyleName("slider show1");
//    list.setVisible(true);
    searchPanel.setVisible(false);
    listPanel.setVisible(true);
    detailPanel.setVisible(false);
    ancHome.setVisible(false);
    ancBack.setVisible(true);
  }

  @Override
  public void showDetailPanel() {
//    mainPanel.setStyleName("slider show2");
//    list.setVisible(false);
    searchPanel.setVisible(false);
    listPanel.setVisible(false);
    detailPanel.setVisible(true);
    ancHome.setVisible(false);
    ancBack.setVisible(true);
  }

  @Override
  public void setDate(String date) {
    sDate.setValue(date);
  }

  @Override
  public void setCategory(CategoryProxy category) {
    sCategory.setSelectedValue(category);
  }

  @Override
  public void setItem(ItemProxy item) {
    sItem.setSelectedValue(item);
  }

  @Override
  public void setPending(Boolean pending) {
    sPending.setSelectedValue(pending);
  }

  @Override
  public void updateLineItems(List<LineItemProxy> lineItems) {
    lineItemTable.clear();
    int count = 0;
    int last = lineItems.size() - 1;
    for (LineItemProxy lineItem : lineItems) {
      final ListItem listItem = new ListItem();
      final MyLineItem myLineItem = new MyLineItem(lineItem);

      if (count == 0) {
        myLineItem.addStyleName("first");
      }
      if (count == last) {
        myLineItem.addStyleName("last");
      }

      final int index = count;

      myLineItem.getLineItemRemove().addClickHandler(new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
//          getUiHandlers().removeLineItemIndex(index);
        }
      });

      myLineItem.getLineItemEdit().addClickHandler(new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
//          getUiHandlers().modifyItem(index);
        }
      });

      myLineItem.addClickHandler(new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
//          getUiHandlers().modifyItem(index);
        }
      });

      count++;

      switch (lineItem.getTransactionType()) {
        case BUY:
          myLineItem.addStyleName("bar-green");
          break;
        case SELL:
          myLineItem.addStyleName("bar-blue");
          break;
        case BUYSELL:
          myLineItem.addStyleName("bar-orange");
          break;
      }

      listItem.add(myLineItem);
      lineItemTable.add(listItem);
    }
  }

  @Override
  public void showLoading(boolean visible) {
    loadingMessage.setVisible(visible);
    currentStep.setVisible(!visible);
    mainPanel.setVisible(!visible);
    ancBar.setVisible(!visible);
  }

  private void setUpListing() {
    final NoSelectionModel<TransactionProxy> selectionModel = new NoSelectionModel<TransactionProxy>();
    list.setSelectionModel(selectionModel);
    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
      @Override
      public void onSelectionChange(SelectionChangeEvent event) {
        TransactionProxy selected = selectionModel.getLastSelectedObject();
        if (selected != null) {
          getUiHandlers().edit(selected.getId());
        }
      }
    });

    pagerPanel.setDisplay(list);
    pager.setDisplay(list);
  }
}