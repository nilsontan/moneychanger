package com.techstudio.erp.moneychanger.server.domain;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.NotSaved;
import com.googlecode.objectify.annotation.Subclass;
import com.googlecode.objectify.condition.IfDefault;
import com.techstudio.erp.moneychanger.server.service.CountryDao;

import java.util.Date;

/**
 * @author Nilson
 */
@Subclass
public class IndividualClient extends Client {

  public static final IndividualClient EMPTY = new IndividualClient();

  String nric;

  @NotSaved(IfDefault.class)
  Date dateOfBirth = null;

  Key<Country> nationality;

  String jobTitle;

  public IndividualClient() {
  }

  public String getNric() {
    return nric;
  }

  public void setNric(String nric) {
    this.nric = nric;
  }

  public Date getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(Date dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public Country getNationality() {
    if (nationality == null) {
      return Country.EMPTY;
    }
    try {
      return new CountryDao().get(nationality);
    } catch (EntityNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public void setNationality(Country nationality) {
    if (nationality == null) {
      return;
    }
    this.nationality = new CountryDao().key(nationality);
  }

  public String getJobTitle() {
    return jobTitle;
  }

  public void setJobTitle(String jobTitle) {
    this.jobTitle = jobTitle;
  }
}
