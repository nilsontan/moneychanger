package com.techstudio.erp.moneychanger.client.ui;

import com.google.gwt.user.client.ui.Image;
import gwtupload.client.IFileInput;
import gwtupload.client.SingleUploader;

/**
 * @author Nilson
 */
public class ItemMenuImageUploader extends SingleUploader {

  private ItemMenuImageInput itemMenuImageInput;

  public ItemMenuImageUploader() {
    super(IFileInput.FileInputType.BUTTON);
  }

  @Override
  public void setFileInput(IFileInput input) {
    if (getFileInput() != null) {
      getFileInput().getWidget().removeFromParent();
    }
    itemMenuImageInput = new ItemMenuImageInput();
    super.setFileInput(itemMenuImageInput);
  }

  public void setImage(Image image) {
    itemMenuImageInput.getButton().setImage(image);
  }
}
