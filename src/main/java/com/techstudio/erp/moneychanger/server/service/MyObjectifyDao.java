package com.techstudio.erp.moneychanger.server.service;

import com.google.appengine.api.datastore.EntityNotFoundException;

import java.util.List;

/**
 * @author Nilson
 */
public class MyObjectifyDao<T> extends ObjectifyDao<T> {
  public List<T> fetchAll() {
    return listAll();
  }

  public T save(T object) {
    put(object);
    return object;
  }

  public T fetch(Long id) throws EntityNotFoundException {
    return get(id);
  }

  public void purge(T object) {
    delete(object);
  }

  public List<T> fetchByProperty(String prop, Object value) {
    return listByProperty(prop, value);
  }

  public List<T> fetchRange(Integer start, Integer length) {
    return listAll(start, length);
  }

  public Integer getCount() {
    return countAll();
  }
}
