package com.techstudio.erp.moneychanger.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.google.inject.Provider;
import gwtupload.client.DecoratedFileUpload;
import gwtupload.client.IFileInput;

/**
 * @author Nilson
 */
public class ItemMenuImageFileUpload extends DecoratedFileUpload implements IFileInput {

  private ItemMenuButton itemMenuButton;

  Provider<IFileInput> iFileInputProvider;

  @Inject
  public ItemMenuImageFileUpload(ItemMenuButton itemMenuButton, Provider<IFileInput> iFileInputProvider) {
    super(itemMenuButton);
    this.itemMenuButton = itemMenuButton;
    this.iFileInputProvider = iFileInputProvider;
  }

  public IFileInput newInstance() {
    return iFileInputProvider.get();
  }

  public void setLength(int length) {
  }

  public void setText(String text) {
    // no text
  }

  public ItemMenuButton getButton() {
    return itemMenuButton;
  }
}
