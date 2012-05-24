package com.techstudio.erp.moneychanger.client.ui.dataprovider;

import com.google.gwt.view.client.HasData;

import java.util.List;

/**
 * @author Nilson
 */
public interface DataProvider<T> {

  /**
   * Adds a display to the provider
   *
   * @param display view
   */
  void addDataDisplay(HasData<T> display);

  /**
   * Updates data in the provider
   */
  void updateData();

  /**
   * Updates mapping of code to T
   */
  void updateMap(List<T> proxies);

  /**
   * Retrieves T from a given code
   *
   * @param code T code
   * @return T
   */
  T getByCode(String code);

  /**
   * Returns a default T value
   *
   * @return null if none available
   */
  T getDefault();
}
