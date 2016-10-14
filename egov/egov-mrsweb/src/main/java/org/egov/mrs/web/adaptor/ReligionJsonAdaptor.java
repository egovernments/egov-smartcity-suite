package org.egov.mrs.web.adaptor;

import java.lang.reflect.Type;

import org.egov.infra.utils.StringUtils;
import org.egov.mrs.masters.entity.MarriageReligion;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class ReligionJsonAdaptor implements JsonSerializer<MarriageReligion> {
    @Override
    public JsonElement serialize(final MarriageReligion religion, final Type type, final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (religion != null) {
            if (religion.getName() != null)
                jsonObject.addProperty("name", religion.getName());
            else
                jsonObject.addProperty("name", StringUtils.EMPTY);
            if (religion.getDescription() != null)
                jsonObject.addProperty("description", religion.getDescription());
            else
                jsonObject.addProperty("description", StringUtils.EMPTY);
            
            if (religion.getCreatedDate() != null)
                jsonObject.addProperty("createdDate", religion.getCreatedDate().toString());
            else
                jsonObject.addProperty("createdDate", StringUtils.EMPTY);
            
            jsonObject.addProperty("id", religion.getId());
        }
        return jsonObject;
    }
}