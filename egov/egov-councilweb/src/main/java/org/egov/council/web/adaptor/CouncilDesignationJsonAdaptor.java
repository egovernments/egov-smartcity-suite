package org.egov.council.web.adaptor;

import java.lang.reflect.Type;

import org.egov.council.entity.CouncilDesignation;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class CouncilDesignationJsonAdaptor implements JsonSerializer<CouncilDesignation> {
    @Override
    public JsonElement serialize(final CouncilDesignation councilDesignation, final Type type,
            final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (councilDesignation != null) {
            if (councilDesignation.getName() != null)
                jsonObject.addProperty("name", councilDesignation.getName());
            else
                jsonObject.addProperty("name", "");
            if (councilDesignation.getIsActive() != null)
                jsonObject.addProperty("isActive", councilDesignation.getIsActive());
            else
                jsonObject.addProperty("isActive", "");
            jsonObject.addProperty("id", councilDesignation.getId());
        }
        return jsonObject;
    }
}