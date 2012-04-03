package com.techstudio.erp.moneychanger.server.domain;

import com.google.common.base.Objects;
import com.googlecode.objectify.annotation.Cached;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Unindexed;

/**
 * @author Nilson
 */
@Cached
@Entity
public class Currency extends MyDatastoreObject {

  public static final Currency EMPTY = new Currency();

  @Unindexed
  private String sign = "$";

  public Currency() {
  }

  public String getSign() {
    return sign;
  }

  public void setSign(String sign) {
    this.sign = sign;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj instanceof Currency) {
      final Currency other = (Currency) obj;
      return Objects.equal(getCode(), other.getCode());
    } else {
      return false;
    }
  }

}
