package com.techstudio.erp.moneychanger.client.ui;

import gwtupload.client.DecoratedFileUpload;
import gwtupload.client.IFileInput;

/**
 * @author Nilson
 */
public class ItemMenuImageInput extends DecoratedFileUpload implements IFileInput {

  private ItemMenuButton itemMenuButton;

  public ItemMenuImageInput() {
    this(new ItemMenuButton());
  }

  public ItemMenuImageInput(ItemMenuButton itemMenuButton) {
    super(itemMenuButton);
    this.itemMenuButton = itemMenuButton;
  }

  public IFileInput newInstance() {
    return new ItemMenuImageInput();
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
