package com.techstudio.erp.moneychanger.server.gae;

import com.allen_sauer.gwt.log.client.Log;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.files.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author Nilson
 */
public class MyBlobstore {

  protected static BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
  protected static DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
  protected static BlobInfoFactory blobInfoFactory = new BlobInfoFactory(datastoreService);

  public void uploadBlob_ByFile(String fileName, String contentType, byte[] filebytes) {
    if (filebytes == null) {
      return;
    }

    Log.info("uploadBlob_ByFile(): filebytes.length=" + filebytes.length);

    // Get a file service
    FileService fileService = FileServiceFactory.getFileService();

    // Create a new Blob file with mime-type
    AppEngineFile file = null;
    try {
      file = fileService.createNewBlobFile(contentType, fileName);
    } catch (IOException e) {
      Log.error("uploadBlob_ByFile(): Error 1:" + e.toString());
      e.printStackTrace();
    }

    if (file == null) {
      return;
    }

    // Open a channel to write to it
    boolean lock = true;
    FileWriteChannel writeChannel = null;
    try {
      writeChannel = fileService.openWriteChannel(file, lock);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      Log.error("uploadBlob_ByFile(): Error 2:" + e.toString());
    } catch (FinalizationException e) {
      e.printStackTrace();
    } catch (LockException e) {
      Log.error("uploadBlob_ByFile(): Error 3:" + e.toString());
      e.printStackTrace();
    } catch (IOException e) {
      Log.error("uploadBlob_ByFile(): Error 4:" + e.toString());
      e.printStackTrace();
    }

    ByteBuffer bb = ByteBuffer.wrap(filebytes);

    // This time we write to the channel using standard Java
    try {
      writeChannel.write(bb);
    } catch (IOException e) {
      Log.error("uploadBlob_ByFile(): Error 5:" + e.toString());
      e.printStackTrace();
    }

    // Now finalize
    try {
      writeChannel.closeFinally();
    } catch (IllegalStateException e) {
      Log.error("uploadBlob_ByFile(): Error 6:" + e.toString());
      e.printStackTrace();
    } catch (IOException e) {
      Log.error("uploadBlob_ByFile(): Error 7:" + e.toString());
      e.printStackTrace();
    }

    BlobKey blobKey = fileService.getBlobKey(file); //fileService.getBlobKey(file);

    if (blobKey == null) {
      Log.error("uploadBlob_ByFile(): Error 8: blobKey is null");
      return;
    }
  }
}
