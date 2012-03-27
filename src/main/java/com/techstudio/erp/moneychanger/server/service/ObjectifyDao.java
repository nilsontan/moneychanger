/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.server.service;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;
import com.googlecode.objectify.util.DAOBase;
import com.techstudio.erp.moneychanger.server.domain.Category;
import com.techstudio.erp.moneychanger.server.domain.Item;

import javax.persistence.Embedded;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Generic DAO for use with Objectify
 *
 * @param <T>
 * @author Nilson
 */
public class ObjectifyDao<T> extends DAOBase {

  static final int BAD_MODIFIERS = Modifier.FINAL | Modifier.STATIC
      | Modifier.TRANSIENT;

  static {
    ObjectifyService.register(Category.class);
    ObjectifyService.register(Item.class);
  }

  protected Class<T> clazz;

  @SuppressWarnings("unchecked")
  public ObjectifyDao() {
    clazz = (Class<T>) ((ParameterizedType) getClass()
        .getGenericSuperclass()).getActualTypeArguments()[0];
  }

  public Key<T> put(T entity) {
    return ofy().put(entity);
  }

  public Map<Key<T>, T> putAll(Iterable<T> entities) {
    return ofy().put(entities);
  }

  public void delete(T entity) {
    ofy().delete(entity);
  }

  public void deleteKey(Key<T> entityKey) {
    ofy().delete(entityKey);
  }

  public void deleteAll(Iterable<T> entities) {
    ofy().delete(entities);
  }

  public void deleteKeys(Iterable<Key<T>> keys) {
    ofy().delete(keys);
  }

  public T get(Long id) throws EntityNotFoundException {
    return ofy().get(this.clazz, id);
  }

  public T get(Key<T> key) throws EntityNotFoundException {
    return ofy().get(key);
  }

  public Map<Key<T>, T> get(Iterable<Key<T>> keys) {
    return ofy().get(keys);
  }

  public List<T> listAll() {
    Query<T> q = ofy().query(this.clazz);
//    List<T> list = q.list();
    return q.list();
  }

  public List<T> listAll(int start, int length) {
    Query<T> q = ofy().query(clazz).offset(start).limit(length);
    return q.list();
  }

  public int countAll() {
    return ofy().query(clazz).count();
  }

  public List<T> listByProperty(String propName, Object propValue) {
    Query<T> q = ofy().query(clazz);
    q.filter(propName, propValue);
    return q.list();
  }

  public List<Key<T>> listKeysByProperty(String propName, Object propValue) {
    Query<T> q = ofy().query(clazz);
    q.filter(propName, propValue);
    return q.listKeys();
  }

  public List<T> listByExample(T exampleObj) {
    Query<T> queryByExample = buildQueryByExample(exampleObj);
    return queryByExample.list();
  }

  public Key<T> getKey(Long id) {
    return new Key<T>(this.clazz, id);
  }

  public Key<T> key(T obj) {
    return ObjectifyService.factory().getKey(obj);
  }

  public List<T> listChildren(Object parent) {
    return ofy().query(clazz).ancestor(parent).list();
  }

  public List<Key<T>> listChildKeys(Object parent) {
    return ofy().query(clazz).ancestor(parent).listKeys();
  }

  protected Query<T> buildQueryByExample(T exampleObj) {
    Query<T> q = ofy().query(clazz);

    // Add all non-null properties to query filter
    for (Field field : clazz.getDeclaredFields()) {
      // Ignore transient, embedded, array, and collection properties
      if (field.isAnnotationPresent(Transient.class)
          || (field.isAnnotationPresent(Embedded.class))
          || (field.getType().isArray())
          || (field.getType().isArray())
          || (Collection.class.isAssignableFrom(field.getType()))
          || ((field.getModifiers() & BAD_MODIFIERS) != 0))
        continue;

      field.setAccessible(true);

      Object value;
      try {
        value = field.get(exampleObj);
      } catch (IllegalArgumentException e) {
        throw new RuntimeException(e);
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }
      if (value != null) {
        q.filter(field.getName(), value);
      }
    }

    return q;
  }
}