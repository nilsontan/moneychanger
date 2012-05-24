package com.techstudio.erp.moneychanger.client.ui.cell;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.techstudio.erp.moneychanger.shared.proxy.CountryProxy;

/**
 * @author Nilson
 */
public class CountryCell extends AbstractMenuCell<CountryProxy> {

  interface Template extends SafeHtmlTemplates {
    @Template("<div><span>{0} : {1}</span></div>")
    SafeHtml cell(SafeHtml code, SafeHtml name);
  }

  private static final Template TEMPLATES = GWT.create(Template.class);

  @Override
  SafeHtml content(CountryProxy proxy) {

    SafeHtml code = SafeHtmlUtils.fromString(proxy.getCode());
    SafeHtml name = SafeHtmlUtils.fromString(proxy.getName());

    return TEMPLATES.cell(code, name);
  }
}
