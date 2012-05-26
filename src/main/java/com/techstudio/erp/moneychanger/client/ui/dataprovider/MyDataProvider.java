package com.techstudio.erp.moneychanger.client.ui.dataprovider;

import com.techstudio.erp.moneychanger.client.ui.HasSelectedValue;

/**
 * @author Nilson
 */
public interface MyDataProvider<T>
    extends DataProvider<T>, FirstLoad {

  void addDataListDisplay(HasSelectedValue<T> display);
}
