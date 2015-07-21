package org.egov.pgr.web.controller.reports;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DrillDownReportHelperAdaptor implements JsonSerializer<DrillDownReportResult> {

    @Override
    public JsonElement serialize(DrillDownReportResult drillDownReportObject, Type type, JsonSerializationContext jsc) {
        JsonObject jsonObject = new JsonObject();
        if (drillDownReportObject != null) {
            
            jsonObject.addProperty("name", null != drillDownReportObject.getName()? drillDownReportObject
                    .getName().toString(): "Not Available");
             jsonObject.addProperty("completed", null != drillDownReportObject.getCompleted() ? drillDownReportObject
                    .getCompleted().toString(): "0");
             jsonObject.addProperty("inprocess", null != drillDownReportObject.getInprocess() ? drillDownReportObject
                     .getInprocess().toString(): "0");
             jsonObject.addProperty("registered", null != drillDownReportObject.getRegistered() ? drillDownReportObject
                     .getRegistered().toString(): "0");
             jsonObject.addProperty("rejected", null != drillDownReportObject.getRejected() ? drillDownReportObject
                     .getRejected().toString(): "0");
             jsonObject.addProperty("total", null != drillDownReportObject.getTotal() ? drillDownReportObject
                     .getTotal().toString(): "0");
        }
        return jsonObject;
    }

}
