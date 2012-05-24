package com.techstudio.erp.moneychanger.client.ui.cell;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.techstudio.erp.moneychanger.shared.proxy.PricingProxy;

/**
 * @author Nilson
 */
public class ItemPriceCell extends AbstractMenuCell<PricingProxy> {

  interface Template extends SafeHtmlTemplates {
    @Template("<div class=\"code\">{0}</div>" +
        "<div class=\"bid\">{1}</div>" +
        "<div class=\"ask\">{2}</div>")
    SafeHtml cell(SafeHtml code, SafeHtml bid, SafeHtml ask);
  }

  private static final Template TEMPLATES = GWT.create(Template.class);

  @Override
  SafeHtml content(PricingProxy pricingProxy) {

    SafeHtml code = SafeHtmlUtils.fromString(pricingProxy.getCode());
    SafeHtml bid = SafeHtmlUtils.fromString(pricingProxy.getBidRate().toString());
    SafeHtml ask = SafeHtmlUtils.fromString(pricingProxy.getAskRate().toString());

    return TEMPLATES.cell(code, bid, ask);
  }
}
