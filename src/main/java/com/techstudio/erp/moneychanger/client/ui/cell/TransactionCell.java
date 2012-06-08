package com.techstudio.erp.moneychanger.client.ui.cell;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.techstudio.erp.moneychanger.shared.proxy.TransactionProxy;

/**
 * @author Nilson
 */
public class TransactionCell extends AbstractMenuCell<TransactionProxy> {

  interface Template extends SafeHtmlTemplates {
    @Template("<div class=\"code\">{0}</div>" +
        "<div class=\"bid\">{1}</div>" +
        "<div class=\"ask\">{2}</div>")
    SafeHtml cell(SafeHtml code, SafeHtml date, SafeHtml id);
  }

  private static final Template TEMPLATES = GWT.create(Template.class);

  @Override
  SafeHtml content(TransactionProxy proxy) {

    SafeHtml id = SafeHtmlUtils.fromString(proxy.getLineItems().get(0).getItemBuy() + " | "
        + proxy.getLineItems().get(0).getItemSell());
    SafeHtml date = SafeHtmlUtils.fromString(proxy.getTransactionDate().toString());
    SafeHtml code = SafeHtmlUtils.fromString(proxy.getId().toString());

    return TEMPLATES.cell(code, date, id);
  }
}
