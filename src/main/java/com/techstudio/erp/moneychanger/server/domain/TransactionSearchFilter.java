package com.techstudio.erp.moneychanger.server.domain;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author Nilson
 */
public class TransactionSearchFilter {

  private Date date;

  private String categoryCode;

  private String itemCode;

  public TransactionSearchFilter() {
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Singapore"));
    calendar.setTime(date);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    this.date =calendar.getTime();
  }

  public String getCategoryCode() {
    return categoryCode;
  }

  public void setCategoryCode(String categoryCode) {
    this.categoryCode = categoryCode;
  }

  public String getItemCode() {
    return itemCode;
  }

  public void setItemCode(String itemCode) {
    this.itemCode = itemCode;
  }
}
