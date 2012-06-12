package com.techstudio.erp.moneychanger.server.domain;

import com.google.common.collect.Lists;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Subclass;
import com.techstudio.erp.moneychanger.server.service.IndividualClientDao;

import java.util.List;

/**
 * @author Nilson
 */
@Subclass
public class CompanyClient extends Client {

  public static final CompanyClient EMPTY = new CompanyClient();

  private Boolean isMoneyChanger;

  private Boolean isRemitter;

  private String bizRegNo;

  private String licenseNo;

  private String website;

  private List<Key<IndividualClient>> authorizedPersons = Lists.newArrayList();

  public CompanyClient() {
  }

  public Boolean getMoneyChanger() {
    return isMoneyChanger;
  }

  public void setMoneyChanger(Boolean moneyChanger) {
    isMoneyChanger = moneyChanger;
  }

  public Boolean getRemitter() {
    return isRemitter;
  }

  public void setRemitter(Boolean remitter) {
    isRemitter = remitter;
  }

  public String getBizRegNo() {
    return bizRegNo;
  }

  public void setBizRegNo(String bizRegNo) {
    this.bizRegNo = bizRegNo;
  }

  public String getLicenseNo() {
    return licenseNo;
  }

  public void setLicenseNo(String licenseNo) {
    this.licenseNo = licenseNo;
  }

  public String getWebsite() {
    return website;
  }

  public void setWebsite(String website) {
    this.website = website;
  }

  public List<IndividualClient> getAuthorizedPersons() {
    if (authorizedPersons.isEmpty()) {
      return Lists.newArrayList();
    }
    return Lists.newArrayList(new IndividualClientDao().get(authorizedPersons).values());
  }

  public void setAuthorizedPersons(List<IndividualClient> authorizedPersons) {
    if (authorizedPersons == null || authorizedPersons.isEmpty()) {
      return;
    }
    this.authorizedPersons = Lists.newArrayList(new IndividualClientDao().putAll(authorizedPersons).keySet());
  }
}