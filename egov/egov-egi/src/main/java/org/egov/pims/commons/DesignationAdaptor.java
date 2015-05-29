package org.egov.pims.commons;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DesignationAdaptor implements JsonSerializer<Designation> {

	@Override
    public JsonElement serialize(Designation designation, Type type, JsonSerializationContext jsc) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", designation.getName());
        jsonObject.addProperty("description", null != designation.getDescription() ? designation.getDescription() : "NA");
        return jsonObject;
    }
}
