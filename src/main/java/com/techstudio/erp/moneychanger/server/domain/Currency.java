package com.techstudio.erp.moneychanger.server.domain;

import com.google.common.base.Objects;
import com.googlecode.objectify.annotation.Cached;
import com.googlecode.objectify.annotation.Entity;

/**
 * @author Nilson
 */
@Cached
@Entity
public class Currency extends MyExDatastoreObject {

  public static final Currency EMPTY = new Currency();

  public Currency() {
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
