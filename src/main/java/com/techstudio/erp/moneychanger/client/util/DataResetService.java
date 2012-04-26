package com.techstudio.erp.moneychanger.client.util;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.io.IOException;

/**
 * @author Nilson
 */
@RemoteServiceRelativePath("resetService")
public interface DataResetService extends RemoteService {
  public String resetData();
}
