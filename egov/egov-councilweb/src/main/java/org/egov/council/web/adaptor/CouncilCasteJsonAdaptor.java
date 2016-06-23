package org.egov.council.web.adaptor;

import java.lang.reflect.Type;

import org.egov.council.entity.CouncilCaste;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class CouncilCasteJsonAdaptor implements JsonSerializer<CouncilCaste> {
    @Override
    public JsonElement serialize(final CouncilCaste councilCaste, final Type type, final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (councilCaste != null) {
            if (councilCaste.getName() != null)
                jsonObject.addProperty("name", councilCaste.getName());
            else
                jsonObject.addProperty("name", "");
            if (councilCaste.getIsActive() != null)
                jsonObject.addProperty("isActive", councilCaste.getIsActive());
            else
                jsonObject.addProperty("isActive", "");
            jsonObject.addProperty("id", councilCaste.getId());
        }
        return jsonObject;
    }
}