package org.egov.council.web.adaptor;

import java.lang.reflect.Type;

import org.egov.council.entity.CouncilParty;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class CouncilPartyJsonAdaptor implements JsonSerializer<CouncilParty> {
    @Override
    public JsonElement serialize(final CouncilParty councilParty, final Type type, final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (councilParty != null) {
            if (councilParty.getName() != null)
                jsonObject.addProperty("name", councilParty.getName());
            else
                jsonObject.addProperty("name", "");
            if (councilParty.getIsActive() != null)
                jsonObject.addProperty("isActive", councilParty.getIsActive());
            else
                jsonObject.addProperty("isActive", "");
            jsonObject.addProperty("id", councilParty.getId());
        }
        return jsonObject;
    }
}