package org.egov.pgr.web.controller.reports;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class AgeingReportHelperAdaptor implements JsonSerializer<AgeingReportResult> {

    @Override
    public JsonElement serialize(AgeingReportResult ageingReportObject, Type type, JsonSerializationContext jsc) {
        JsonObject jsonObject = new JsonObject();
        if (ageingReportObject != null) {
            
            jsonObject.addProperty("complainttype", null != ageingReportObject.getComplainttype() ? ageingReportObject
                    .getComplainttype().toString(): "Not Available");
             jsonObject.addProperty("grtthn90", null != ageingReportObject.getGrtthn90() ? ageingReportObject
                    .getGrtthn90().toString(): "0");
             jsonObject.addProperty("btw45to90", null != ageingReportObject.getBtw45to90() ? ageingReportObject
                     .getBtw45to90().toString(): "0");
             jsonObject.addProperty("btw15to45", null != ageingReportObject.getBtw15to45() ? ageingReportObject
                     .getBtw15to45().toString(): "0");
             jsonObject.addProperty("lsthn15", null != ageingReportObject.getLsthn15() ? ageingReportObject
                     .getLsthn15().toString(): "0");
             jsonObject.addProperty("total", null != ageingReportObject.getTotal() ? ageingReportObject
                     .getTotal().toString(): "0");
        }
        return jsonObject;
    }

}
