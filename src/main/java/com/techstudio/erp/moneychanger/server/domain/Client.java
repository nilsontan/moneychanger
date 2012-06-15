/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.server.domain;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.common.base.Objects;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cached;
import com.googlecode.objectify.annotation.Entity;
import com.techstudio.erp.moneychanger.server.service.CountryDao;
import com.techstudio.erp.moneychanger.shared.domain.Address;
import com.techstudio.erp.moneychanger.shared.domain.ClientType;

import javax.persistence.Embedded;

@Entity
@Cached
public class Client extends MyDatastoreObject {

  private ClientType clientType = ClientType.INDIVIDUAL;

  @Embedded
  private Address address;

  private Key<Country> country;

  private String contactNo;

  private String contactNo2;

  private String faxNo;

  private String email;

  public Client() {
  }

  public ClientType getClientType() {
    return clientType;
  }

  public void setClientType(ClientType clientType) {
    this.clientType = clientType;
  }

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public Country getCountry() {
    if (country == null) {
      return Country.EMPTY;
    }
    try {
      return new CountryDao().get(country);
    } catch (EntityNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public void setCountry(Country country) {
    if (country.equals(Country.EMPTY)) {
      return;
    }
    this.country = new CountryDao().key(country);
  }

  public String getContactNo() {
    return contactNo;
  }

  public void setContactNo(String contactNo) {
    this.contactNo = contactNo;
  }

  public String getContactNo2() {
    return contactNo2;
  }

  public void setContactNo2(String contactNo2) {
    this.contactNo2 = contactNo2;
  }

  public String getFaxNo() {
    return faxNo;
  }

  public void setFaxNo(String faxNo) {
    this.faxNo = faxNo;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj instanceof Client) {
      final Client other = (Client) obj;
      return Objects.equal(getCode(), other.getCode());
    } else {
      return false;
    }
  }
}
