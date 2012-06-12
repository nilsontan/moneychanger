package com.techstudio.erp.moneychanger.client.ui.cell;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.techstudio.erp.moneychanger.shared.proxy.ClientProxy;

/**
 * @author Nilson
 */
public class ClientCell extends AbstractMenuCell<ClientProxy> {

  interface Template extends SafeHtmlTemplates {
    @Template("<div><span>{0}</span></div>")
    SafeHtml cell(SafeHtml name);
  }

  private static final Template TEMPLATES = GWT.create(Template.class);

  @Override
  SafeHtml content(ClientProxy proxy) {

    SafeHtml name = SafeHtmlUtils.fromString(proxy.getName());

    return TEMPLATES.cell(name);
  }
}
