package com.techstudio.erp.moneychanger.server.gae;

import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import gwtupload.server.exceptions.UploadActionException;
import gwtupload.server.gae.FilesApiFileItemFactory;
import gwtupload.server.gae.FilesApiUploadAction;
import org.apache.commons.fileupload.FileItem;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Nilson
 */
public class MyUploadAction extends FilesApiUploadAction {

  @Override
  public String executeAction(HttpServletRequest request,
                              List<FileItem> sessionFiles) throws UploadActionException {
    String ret = "";
    for (FileItem i : sessionFiles) {
      ret = ((FilesApiFileItemFactory.FilesAPIFileItem) i).getKey().getKeyString();
      logger.info("Received new file, stored in blobstore with the key: " + ret);

      ImagesService imagesService = ImagesServiceFactory.getImagesService();
      ret = imagesService.getServingUrl(((FilesApiFileItemFactory.FilesAPIFileItem) i).getKey());
    }

    return ret;
  }
}
