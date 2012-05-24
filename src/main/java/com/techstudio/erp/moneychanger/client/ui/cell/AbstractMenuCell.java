package com.techstudio.erp.moneychanger.client.ui.cell;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * @author Nilson
 */
public abstract class AbstractMenuCell<T> extends AbstractCell<T> {

  interface Template extends SafeHtmlTemplates {
    @Template("<div class=\"centered f-width menuListItem-container soft-bg\">" +
        "<div class=\"gwt-HTML menuListItem-icon menuListItem-icon-l icon-container-b\">" +
        "<span class=\"icon-set icon-star\" style=\"position:absolute;\"></span></div>" +
        "<div class=\"menuListItem-item\"><div class=\"menuListItem-box\"><div class=\"menuListItem-content\">{0}" +
        "</div></div></div><div class=\"menuListItem-icon menuListItem-icon-r icon-container-b\">" +
        "<span class=\"icon-set icon-right\" style=\"position:absolute;\"></span></div></div>")
    SafeHtml cell(SafeHtml safeHtml);
  }

  private static final Template TEMPLATES = GWT.create(Template.class);

  /**
   * The content inside the menu
   *
   * @return content
   */
  abstract SafeHtml content(T value);

  @Override
  public void render(Context context, T value, SafeHtmlBuilder sb) {
    // Value can be null, so do a null check..
    if (value == null) {
      return;
    }

    sb.append(TEMPLATES.cell(content(value)));
  }
}
