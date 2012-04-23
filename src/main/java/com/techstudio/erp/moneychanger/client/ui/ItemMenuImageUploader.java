package com.techstudio.erp.moneychanger.client.ui;

import com.google.gwt.resources.client.ImageResource;
import com.google.inject.Inject;
import com.google.inject.Provider;
import gwtupload.client.IFileInput;
import gwtupload.client.SingleUploader;

/**
 * @author Nilson
 */
public class ItemMenuImageUploader extends SingleUploader {

  private ItemMenuImageFileUpload itemMenuImageFileUpload;

  Provider<IFileInput> provider;

  @Inject
  public ItemMenuImageUploader(Provider<IFileInput> iFileInputProvider) {
    super(IFileInput.FileInputType.BUTTON);
    this.provider = iFileInputProvider;
    itemMenuImageFileUpload = (ItemMenuImageFileUpload) provider.get();
    setFileInput(itemMenuImageFileUpload);
  }

  @Override
  public void setFileInput(IFileInput input) {
    if (provider == null) {
      return;
    }

    if (getFileInput() != null) {
      getFileInput().getWidget().removeFromParent();
    }
    itemMenuImageFileUpload = (ItemMenuImageFileUpload) provider.get();
    super.setFileInput(itemMenuImageFileUpload);
  }

  public void setImageUrl(String url) {
    itemMenuImageFileUpload.getButton().setImageUrl(url);
  }

  public void setImageResource(ImageResource imageResource) {
    itemMenuImageFileUpload.getButton().setResource(imageResource);
  }
}
