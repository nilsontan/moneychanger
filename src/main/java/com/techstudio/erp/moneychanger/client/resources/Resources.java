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
import com.google.gwt.user.cellview.client.CellTable;

/**
 * @author Nilson
 */
public interface Resources extends ClientBundle {
  @Source("clean-admin.css")
  StyleAdmin admin();

  @Source("images/loading.gif")
  ImageResource iLoadingIcon();

  @Source("images/logo.png")
  ImageResource iLogo();

  @Source("images/nia.png")
  ImageResource iNoImageAvailable();

  @Source("images/32/accept.png")
  ImageResource iAccept();

  public interface StyleAdmin extends CssResource {

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

  @Source("images/logo_pos.png")
  ImageResource iLogoPos();

  @Source("images/32/house.png")
  ImageResource iHome();

  @Source("images/32/circulate.png")
  ImageResource iTxViewRate();

  @Source("images/32/add.png")
  ImageResource iTxAdd();

  @Source("images/32/cancel.png")
  ImageResource iTxDel();

  @Source("images/32/accept.png")
  ImageResource iTxSav();

  @Source("images/16/money.png")
  ImageResource iCatCur();

  @Source("images/16/hand_point_270.png")
  ImageResource iItemSelect();

  public interface StylePos extends CssResource {
    String step();

    String centered();

    String formTitle();
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
