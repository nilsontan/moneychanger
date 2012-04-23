package com.techstudio.erp.moneychanger.server.gae;

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
    }

    return ret;
  }
}
