package com.techstudio.erp.moneychanger.server.domain;

import com.googlecode.objectify.annotation.Unindexed;

import java.math.BigDecimal;

/**
 * @author Nilson
 */
public class Currency extends MyDatastoreObject {

  @Unindexed
  private String sign = "$";
  @Unindexed
  private String rate = "1";

  public Currency() {
  }

  public String getSign() {
    return sign;
  }

  public void setSign(String sign) {
    this.sign = sign;
  }

  public String getRate() {
    return rate.toString();
  }

  public void setRate(String rate) {
    this.rate = new BigDecimal(rate).setScale(4).toString();
  }

//  @Override
//  public void setName(String name) {
//    super.setName(name.trim().toUpperCase());
//  }

}
