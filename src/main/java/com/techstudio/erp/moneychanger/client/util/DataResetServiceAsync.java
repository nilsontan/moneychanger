package com.techstudio.erp.moneychanger.client.util;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;

import java.io.IOException;

public interface DataResetServiceAsync {
  void resetData(AsyncCallback<String> async);
}
