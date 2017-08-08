package org.mule.module.apikit.validation.body.form;

import org.mule.module.apikit.ApikitErrorTypes;
import org.mule.module.apikit.api.exception.InvalidFormParameterException;
import org.mule.runtime.api.el.BindingContext;
import org.mule.runtime.api.metadata.DataType;
import org.mule.runtime.api.metadata.TypedValue;
import org.mule.runtime.api.util.MultiMap;
import org.mule.runtime.core.api.el.ExpressionManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataWeaveTransformer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DataWeaveTransformer.class);

    private final DataType multiMapDataType = DataType.builder()
            .mapType(MultiMap.class)
            .keyType(String.class)
            .valueType(String.class)
            .build();

    ExpressionManager expressionManager;

    public DataWeaveTransformer(ExpressionManager expressionManager)
    {
        this.expressionManager = expressionManager;
    }

    public TypedValue runDataWeaveScript(String script, DataType dataType, TypedValue payload)
    {
        BindingContext.Builder bindingContextBuilder = BindingContext.builder();

        bindingContextBuilder.addBinding("payload", payload);
        TypedValue result;
        try
        {
            result = expressionManager.evaluate(script, dataType, bindingContextBuilder.build());
        }
        catch (Exception e)
        {
            LOGGER.error("Url encoded validation could not be performed. Reason: " + e.getMessage());
            throw ApikitErrorTypes.throwErrorType(new InvalidFormParameterException("Invalid form parameter exception"));
        }
        return result;
    }

    public MultiMap<String, String> getMultiMapFromPayload(TypedValue payload)
    {
        String script = "output application/java --- payload";

        return (MultiMap<String,String>) runDataWeaveScript(script, multiMapDataType, payload).getValue();
    }

    public TypedValue getXFormUrlEncodedStream(MultiMap mapToTransform, DataType responseDataType){
        TypedValue<MultiMap<String, String>> modifiedPayload = new TypedValue<>(mapToTransform, multiMapDataType);
        String script = "output application/x-www-form-urlencoded --- payload";
        return runDataWeaveScript(script, responseDataType, modifiedPayload);
    }
}
