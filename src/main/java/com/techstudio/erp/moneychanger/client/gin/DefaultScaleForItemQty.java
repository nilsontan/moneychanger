/*
 * Copyright (c) 2004-2012 TechStudio Solutions Pte Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of TechStudio
 * Solution Pte Ltd ("Confidential Information").
 */

package com.techstudio.erp.moneychanger.client.gin;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation is used in {@link com.techstudio.erp.moneychanger.client.MoneychangerPlaceManager}
 * and is bind in {@link com.techstudio.erp.moneychanger.client.gin.MoneychangerModule}.
 * It's purpose is to bind the default scale for an exchange rate
 *
 * @author Nilson
 */
@BindingAnnotation
@Target({FIELD, PARAMETER, METHOD})
@Retention(RUNTIME)
public @interface DefaultScaleForItemQty {
}