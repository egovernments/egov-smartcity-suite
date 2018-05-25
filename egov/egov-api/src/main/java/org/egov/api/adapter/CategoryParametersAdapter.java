package org.egov.api.adapter;

import static org.egov.eventnotification.constants.Constants.PARAMETER_ID;
import static org.egov.eventnotification.constants.Constants.PARAMETER_NAME;

import java.lang.reflect.Type;

import org.egov.eventnotification.entity.CategoryParameters;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

public class CategoryParametersAdapter extends DataAdapter<CategoryParameters> {
    @Override
    public JsonElement serialize(CategoryParameters parameter, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObjectEvent = new JsonObject();
        jsonObjectEvent.addProperty(PARAMETER_ID, parameter.getId());
        jsonObjectEvent.addProperty(PARAMETER_NAME, parameter.getName());
        return jsonObjectEvent;
    }
}
