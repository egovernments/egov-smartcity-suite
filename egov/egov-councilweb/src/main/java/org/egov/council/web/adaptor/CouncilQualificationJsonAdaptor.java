package org.egov.council.web.adaptor;

import java.lang.reflect.Type;

import org.egov.council.entity.CouncilQualification;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class CouncilQualificationJsonAdaptor implements JsonSerializer<CouncilQualification> {
    @Override
    public JsonElement serialize(final CouncilQualification councilQualification, final Type type,
            final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (councilQualification != null) {
            if (councilQualification.getName() != null)
                jsonObject.addProperty("name", councilQualification.getName());
            else
                jsonObject.addProperty("name", "");
            if (councilQualification.getIsActive() != null)
                jsonObject.addProperty("isActive", councilQualification.getIsActive());
            else
                jsonObject.addProperty("isActive", "");
            jsonObject.addProperty("id", councilQualification.getId());
        }
        return jsonObject;
    }
}