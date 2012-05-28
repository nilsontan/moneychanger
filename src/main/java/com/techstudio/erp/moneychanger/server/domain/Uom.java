package com.techstudio.erp.moneychanger.server.domain;

import com.googlecode.objectify.annotation.Cached;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Unindexed;

/**
 * @author Nilson
 */
@Cached
@Entity
public class Uom extends MyDatastoreObject {

  public static final Uom EMPTY = new Uom();

  @Unindexed
  private int scale = 0;

  public int getScale() {
    return scale;
  }

  public void setScale(int scale) {
    this.scale = scale;
  }
}
