/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.apikit.validation.body.form;

import org.mule.module.apikit.ApikitErrorTypes;
import org.mule.module.apikit.api.exception.BadRequestException;
import org.mule.module.apikit.api.exception.InvalidFormParameterException;
import org.mule.raml.interfaces.model.parameter.IParameter;
import org.mule.runtime.api.el.BindingContext;
import org.mule.runtime.api.metadata.TypedValue;
import org.mule.runtime.core.api.el.ExpressionManager;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UrlencodedFormValidator implements FormValidatorStrategy<TypedValue> {

  Map<String, List<IParameter>> formParameters;
  ExpressionManager expressionManager;

  public UrlencodedFormValidator(Map<String, List<IParameter>> formParameters, ExpressionManager expressionManager) {
    this.formParameters = formParameters;
    this.expressionManager = expressionManager;
  }

  private static final Logger LOGGER = LoggerFactory.getLogger(UrlencodedFormValidator.class);

  @Override
  public TypedValue validate(TypedValue payload) throws BadRequestException
  {

    BindingContext.Builder bindingContextBuilder = BindingContext.builder();

    String script = "output application/java --- payload groupBy validateProperty($$ as String, $))";

    bindingContextBuilder.addBinding("payload", payload);
    TypedValue result;
    try
    {
       result = expressionManager.evaluate(script, bindingContextBuilder.build());
    }
    catch (Exception e)
    {
      LOGGER.error("Url encoded validation could not be performed. Reason: " + e.getMessage());
      throw ApikitErrorTypes.throwErrorType(new InvalidFormParameterException("Invalid form parameter exception"));

    }
    return result;
  }

  public boolean validateProperty(String key, String value)
  {
    return true;
  }
    //for (String expectedKey : formParameters.keySet())
    //{
    //  if (formParameters.get(expectedKey).size() != 1)
    //  {
    //    //do not perform validation when multi-type parameters are used
    //    continue;
    //  }
    //
    //  IParameter expected = formParameters.get(expectedKey).get(0);
    //
    //  Object actual = payload.get(expectedKey);
    //
    //  if (actual == null && expected.isRequired())
    //  {
    //    throw ApikitErrorTypes.throwErrorType(new InvalidFormParameterException("Required form parameter " + expectedKey + " not specified"));
    //  }
    //
    //  if (actual == null && expected.getDefaultValue() != null)
    //  {
    //    payload.put(expectedKey, expected.getDefaultValue());
    //  }
    //
    //  if (actual != null && actual instanceof String)
    //  {
    //    if (!expected.validate((String) actual))
    //    {
    //      String msg = String.format("Invalid value '%s' for form parameter %s. %s",
    //          actual, expectedKey, expected.message((String) actual));
    //      throw ApikitErrorTypes.throwErrorType(new InvalidFormParameterException(msg));
    //    }
    //  }
    //}

 //   return payload;
 // }
  //
  //public boolean validateProperty(String key, String value)
  //{
  //  if (formParameters.get(expectedKey).size() != 1)
  //  {
  //    //do not perform validation when multi-type parameters are used
  //    return true;
  //  }
  //
  //  IParameter expected = formParameters.get(key).get(0);
  //
  //  if (expected)
  //  Object actual = payload.get(expectedKey);
  //
  //}
}
