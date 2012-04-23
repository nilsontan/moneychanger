/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.user.cellview.client.CellTable;

/**
 * @author Nilson
 */
public interface Resources extends ClientBundle {
  @Source("category.txt")
  TextResource categoryText();

  @Source("currency.txt")
  TextResource currencyText();

  @Source("uom.txt")
  TextResource uomText();

  @Source("country.txt")
  TextResource countryText();

  @Source("item.txt")
  TextResource itemText();

  @Source("spotrate.txt")
  TextResource spotRatesText();

  @Source("clean-admin.css")
  StyleAdmin admin();

  @Source("loading.gif")
  ImageResource loadingIcon();

  @Source("logo.png")
  ImageResource logo();

  @Source("images/nia.png")
  ImageResource noImageAvailable();

  public interface StyleAdmin extends CssResource {

    String centered();

    String mainTitle();

    String mainSubtitle();

    String northBar();

    String north2Bar();

    String menuTitle();

    String pageTitle();

    String pageDescription();

    String formTitle();

    String formParam();

  }

  @Source("clean-pos.css")
  StylePos pos();

  @Source("logo_pos.png")
  ImageResource iLogoPos();

  @Source("images/32/note.png")
  ImageResource iTxNew();

  @Source("images/32/note_add.png")
  ImageResource iTxAdd();

  @Source("images/32/note_delete.png")
  ImageResource iTxDel();

  @Source("images/32/note_go.png")
  ImageResource iTxSav();

  @Source("images/16/money.png")
  ImageResource iCatCur();

  @Source("images/16/hand_point_270.png")
  ImageResource iItemSelect();

  public interface StylePos extends CssResource {
    String centered();

    String northBar();

    String formTitle();

    String formParam();

    String curBtn();

    String btmBorder();
  }

  TableResources tableResources();

  public interface TableResources extends CellTable.Resources {
    @Override
    @Source("clean-custom.css")
    TableStyle cellTableStyle();
  }

  public interface TableStyle extends CellTable.Style {
    /**
     * Applied to every cell.
     */
    String cellTableCell();

    /**
     * Applied to even rows.
     */
    String cellTableEvenRow();

    /**
     * Applied to cells in even rows.
     */
    String cellTableEvenRowCell();

    /**
     * Applied to the first column.
     */
    String cellTableFirstColumn();

    /**
     * Applied to the first column footers.
     */
    String cellTableFirstColumnFooter();

    /**
     * Applied to the first column headers.
     */
    String cellTableFirstColumnHeader();

    /**
     * Applied to footers cells.
     */
    String cellTableFooter();

    /**
     * Applied to headers cells.
     */
    String cellTableHeader();

    /**
     * Applied to the hovered row.
     */
    String cellTableHoveredRow();

    /**
     * Applied to the cells in the hovered row.
     */
    String cellTableHoveredRowCell();

    /**
     * Applied to the keyboard selected cell.
     */
    String cellTableKeyboardSelectedCell();

    /**
     * Applied to the keyboard selected row.
     */
    String cellTableKeyboardSelectedRow();

    /**
     * Applied to the cells in the keyboard selected row.
     */
    String cellTableKeyboardSelectedRowCell();

    /**
     * Applied to the last column.
     */
    String cellTableLastColumn();

    /**
     * Applied to the last column footers.
     */
    String cellTableLastColumnFooter();

    /**
     * Applied to the last column headers.
     */
    String cellTableLastColumnHeader();

    /**
     * Applied to the loading indicator.
     */
    String cellTableLoading();

    /**
     * Applied to odd rows.
     */
    String cellTableOddRow();

    /**
     * Applied to cells in odd rows.
     */
    String cellTableOddRowCell();

    /**
     * Applied to selected rows.
     */
    String cellTableSelectedRow();

    /**
     * Applied to cells in selected rows.
     */
    String cellTableSelectedRowCell();

    /**
     * Applied to header cells that are sortable.
     */
    String cellTableSortableHeader();

    /**
     * Applied to header cells that are sorted in ascending order.
     */
    String cellTableSortedHeaderAscending();

    /**
     * Applied to header cells that are sorted in descending order.
     */
    String cellTableSortedHeaderDescending();

    /**
     * Applied to the table.
     */
    String cellTableWidget();
  }
}
