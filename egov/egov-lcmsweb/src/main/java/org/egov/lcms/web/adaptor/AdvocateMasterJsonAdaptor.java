package org.egov.lcms.web.adaptor;

import java.lang.reflect.Type;

import org.egov.lcms.masters.entity.AdvocateMaster;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class AdvocateMasterJsonAdaptor implements JsonSerializer<AdvocateMaster> {
    @Override
    public JsonElement serialize(final AdvocateMaster advocateMaster, final Type type,
            final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (advocateMaster != null) {
            if (advocateMaster.getName() != null)
                jsonObject.addProperty("name", advocateMaster.getName());
            else
                jsonObject.addProperty("name", "");
            if (advocateMaster.getMobileNumber() != null)
                jsonObject.addProperty("mobileNumber", advocateMaster.getMobileNumber());
            else
                jsonObject.addProperty("mobileNumber", "");
            if (advocateMaster.getEmail() != null)
                jsonObject.addProperty("email", advocateMaster.getEmail());
            else
                jsonObject.addProperty("email", "");
            jsonObject.addProperty("id", advocateMaster.getId());
        }
        return jsonObject;
    }
}