/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.shared.domain;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

/**
 * @author Nilson
 */
public enum TransactionType implements IsSerializable, Serializable {
  PURCHASE("Buying"),
  SALE("Selling");

  private String print;

  TransactionType() {
  }

  TransactionType(String print) {
    this.print = print;
  }

  public String print() {
    return print;
  }
}
