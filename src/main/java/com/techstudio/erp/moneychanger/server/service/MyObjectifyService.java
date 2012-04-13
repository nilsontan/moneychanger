package com.techstudio.erp.moneychanger.server.service;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyOpts;
import com.googlecode.objectify.impl.conv.Converter;
import com.googlecode.objectify.impl.conv.ConverterLoadContext;
import com.googlecode.objectify.impl.conv.ConverterSaveContext;
import org.joda.money.Money;

import java.math.BigDecimal;

/**
 * Modification of ObjectifyService
 *
 * @author Nilson
 */
public class MyObjectifyService {

  /**
   * Singleton instance
   */
  protected static ObjectifyFactory factory = getFactory();

  private static ObjectifyFactory getFactory() {
    ObjectifyFactory objectifyFactory = new ObjectifyFactory();
    objectifyFactory.getConversions().add(new Converter() {
      @Override
      public Object forPojo(Object value, Class<?> fieldType, ConverterLoadContext ctx, Object onPojo) {
        if (fieldType == BigDecimal.class && value instanceof String) {
          return new BigDecimal((String) value);
        } else {
          return null;
        }
      }

      @Override
      public Object forDatastore(Object value, ConverterSaveContext ctx) {
        if (value instanceof BigDecimal)
          return value.toString();
        else
          return null;
      }
    });
    objectifyFactory.getConversions().add(new Converter() {
      @Override
      public Object forDatastore(Object value, ConverterSaveContext ctx) {
        if (value instanceof Money)
          return (value).toString();
        else
          return null;
      }

      @Override
      public Object forPojo(Object value, Class<?> fieldType, ConverterLoadContext ctx, Object onPojo) {
        if (value instanceof String && Money.class.isAssignableFrom(fieldType))
          return Money.parse((String)value);
        else
          return null;
      }
    });
    return objectifyFactory;
  }

  /**
   * Call this to get the instance
   */
  public static ObjectifyFactory factory() {
    return factory;
  }

  /**
   * @see ObjectifyFactory#begin()
   */
  public static Objectify begin() {
    return factory().begin();
  }

  /**
   * @see ObjectifyFactory#beginTransaction()
   */
  public static Objectify beginTransaction() {
    return factory().beginTransaction();
  }

  /**
   * @see ObjectifyFactory#begin(com.googlecode.objectify.ObjectifyOpts)
   */
  public static Objectify begin(ObjectifyOpts opts) {
    return factory().begin(opts);
  }

  /**
   * @see ObjectifyFactory#register(Class)
   */
  public static void register(Class<?> clazz) {
    factory().register(clazz);
  }
}
