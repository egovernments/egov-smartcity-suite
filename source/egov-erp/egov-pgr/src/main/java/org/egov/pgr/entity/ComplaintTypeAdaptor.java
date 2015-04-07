package org.egov.pgr.entity;

import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class ComplaintTypeAdaptor implements JsonSerializer<ComplaintType> {

    @Override
    public JsonElement serialize(ComplaintType compaintType, Type type, JsonSerializationContext jsc) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", compaintType.getName());
        jsonObject.addProperty("department", compaintType.getDepartment().getName());
        jsonObject.addProperty("locationRequired", compaintType.isLocationRequired());
        return jsonObject;      
    }

}