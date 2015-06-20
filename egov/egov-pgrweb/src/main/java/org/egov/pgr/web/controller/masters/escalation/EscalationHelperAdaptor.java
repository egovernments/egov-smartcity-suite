package org.egov.pgr.web.controller.masters.escalation;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class EscalationHelperAdaptor implements JsonSerializer<EscalationHelper> {

    @Override
    public JsonElement serialize(EscalationHelper escalationHelper, Type type, JsonSerializationContext jsc) {
        JsonObject jsonObject = new JsonObject();
        if (escalationHelper != null) {
        //    jsonObject.addProperty("id", escalationHelper.getId());
            jsonObject.addProperty("positionFrom", null != escalationHelper.getFromPosition() ? escalationHelper
                    .getFromPosition().getName() : "NA");
            jsonObject.addProperty("objectSubType", null!= escalationHelper.getComplaintType()?escalationHelper.getComplaintType().getName():"");
         //   jsonObject.addProperty("objectType", escalationHelper.getObjectType().getDescription());
            jsonObject.addProperty("positionTo", null != escalationHelper.getToPosition() ? escalationHelper
                    .getToPosition().getName() : "");

        }
        return jsonObject;
    }

}
