/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.server.domain;

import javax.persistence.Id;
import javax.persistence.PrePersist;

public class DatastoreObject {
  @Id
  private Long id;
  private Integer version = 0;

  /**
   * Auto-increment version # whenever persisted
   */
  @PrePersist
  void onPersist() {
    this.version++;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }
}