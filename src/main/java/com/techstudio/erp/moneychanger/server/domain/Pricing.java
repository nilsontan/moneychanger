package com.techstudio.erp.moneychanger.server.domain;

import com.google.common.base.Objects;
import com.googlecode.objectify.annotation.Cached;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Unindexed;

import java.math.BigDecimal;

/**
 * @author Nilson
 */
@Cached
@Entity
public class Pricing extends MyDatastoreObject {

  public static final Pricing EMPTY = new Pricing();

  @Unindexed
  BigDecimal askRate = BigDecimal.ONE;

  @Unindexed
  BigDecimal bidRate = BigDecimal.ONE;

  public Pricing() {
  }

  public BigDecimal getAskRate() {
    return askRate;
  }

  public void setAskRate(BigDecimal askRate) {
    this.askRate = askRate;
  }

  public BigDecimal getBidRate() {
    return bidRate;
  }

  public void setBidRate(BigDecimal bidRate) {
    this.bidRate = bidRate;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj instanceof Pricing) {
      final Pricing other = (Pricing) obj;
      return Objects.equal(getCode(), other.getCode());
    } else {
      return false;
    }
  }

}
