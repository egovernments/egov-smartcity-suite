package org.egov.pgr.web.controller.masters.escalationTime;


import java.lang.reflect.Type;

import org.egov.pgr.entity.Escalation;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class EscalationTimeAdaptor implements JsonSerializer<Escalation> {
	@Override
    public JsonElement serialize(final Escalation escalation, final Type type,
            final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if(escalation!=null ){
	        jsonObject.addProperty("complaintType", null != escalation.getComplaintType() ? escalation.getComplaintType().getName() : "NA");
	        jsonObject.addProperty("designation",  null != escalation.getDesignation() ? escalation.getDesignation().getName() : "NA");
	        jsonObject.addProperty("noOfHours", null !=escalation.getNoOfHrs()?escalation.getNoOfHrs().toString():"NA");
		}else
		{
			jsonObject.addProperty("complaintType", "NA");
	        jsonObject.addProperty("designation", "NA");
	        jsonObject.addProperty("noOfHours", "NA");
		}
        return jsonObject;
    }
}
