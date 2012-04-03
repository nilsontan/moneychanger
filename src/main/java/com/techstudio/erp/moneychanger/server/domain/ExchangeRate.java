package com.techstudio.erp.moneychanger.server.domain;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cached;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Unindexed;
import com.techstudio.erp.moneychanger.server.service.CurrencyDao;

import java.util.ArrayList;

/**
 * @author Nilson
 */
@Cached
@Entity
public class ExchangeRate extends MyDatastoreObject {

  public static final ExchangeRate EMPTY = new ExchangeRate();

  Key<Currency> currency;

  @Unindexed
  Integer unit = 1;

  @Unindexed
  String askRate = "1.0000";

  @Unindexed
  String bidRate = "1.0000";

  public ExchangeRate() {
  }

  public Currency getCurrency() {
    if (currency == null) {
      return Currency.EMPTY;
    }
    try {
      return new CurrencyDao().get(currency);
    } catch (EntityNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public void setCurrency(Currency currency) {
    if (currency == null) {
      return;
    }
    this.currency = new CurrencyDao().key(currency);
  }

  public Integer getUnit() {
    return unit;
  }

  public void setUnit(Integer unit) {
    this.unit = unit;
  }

  public String getAskRate() {
    return askRate;
  }

  public void setAskRate(String askRate) {
    this.askRate = convertToScale(askRate);
  }

  public String getBidRate() {
    return bidRate;
  }

  public void setBidRate(String bidRate) {
    this.bidRate = convertToScale(bidRate);
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj instanceof ExchangeRate) {
      final ExchangeRate other = (ExchangeRate) obj;
      return Objects.equal(getCode(), other.getCode());
    } else {
      return false;
    }
  }

  private String convertToScale(String rate) {
    ArrayList<String> list = Lists.newArrayList(Splitter.on(".").split(rate));
    String decimals = "0000";
    if (list.size() == 2) {
      decimals = Strings.padEnd(list.get(1), 4, '0');
    }
    return Joiner.on(".").join(list.get(0), decimals);
  }

}
