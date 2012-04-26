package com.techstudio.erp.moneychanger.client.ui;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.ImageResourceRenderer;
import com.techstudio.erp.moneychanger.client.resources.Resources;

/**
 * <p>
 * An {@link com.google.gwt.cell.client.AbstractCell} used to render an image. The String value is the url
 * of the image.
 * </p>
 *
 * @author Nilson
 */
public class MyImageCell extends AbstractCell<String> {

  interface Template extends SafeHtmlTemplates {
    @Template("<img src=\"{0}\"/>")
    SafeHtml img(String url);
  }

  private static final Resources resources = GWT.create(Resources.class);

  private static Template template;

  private static ImageResourceRenderer renderer;

  /**
   * Construct a new ImageCell.
   */
  public MyImageCell() {
    if (template == null) {
      template = GWT.create(Template.class);
    }
    if (renderer == null) {
      renderer = new ImageResourceRenderer();
    }
  }

  @Override
  public void render(Context context, String value, SafeHtmlBuilder sb) {
    if (value != null && !value.isEmpty()) {
      // The template will sanitize the URI.
      sb.append(template.img(value));
    } else {
      sb.append(renderer.render(resources.iNoImageAvailable()));
    }
  }
}
