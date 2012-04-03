package com.techstudio.erp.moneychanger.server.domain;

import com.googlecode.objectify.annotation.Cached;
import com.googlecode.objectify.annotation.Entity;

/**
 * @author Nilson
 */
@Cached
@Entity
public class Uom extends MyDatastoreObject {

  public static final Uom EMPTY = new Uom();

}
