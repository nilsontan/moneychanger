package com.techstudio.erp.moneychanger.client.ui.cell;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.techstudio.erp.moneychanger.shared.domain.ClientType;

/**
 * @author Nilson
 */
public class ClientTypeCell extends AbstractMenuCell<ClientType> {

  interface Template extends SafeHtmlTemplates {
    @Template("<div>{0}</div>")
    SafeHtml cell(SafeHtml code);
  }

  private static final Template TEMPLATES = GWT.create(Template.class);

  @Override
  SafeHtml content(ClientType clientType) {

    SafeHtml code = SafeHtmlUtils.fromString(clientType.name());

    return TEMPLATES.cell(code);
  }
}
