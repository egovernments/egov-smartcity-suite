package org.egov.pgr.web.controller.reports;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DrillDownReportWithcompTypeAdaptor  implements JsonSerializer<DrillDownReportResult> {

    @Override
    public JsonElement serialize(DrillDownReportResult drillDownReportObject, Type type, JsonSerializationContext jsc) {
        JsonObject jsonObject = new JsonObject();
        if (drillDownReportObject != null) {
            
            jsonObject.addProperty("crn", null != drillDownReportObject.getCrn()? drillDownReportObject
                    .getCrn().toString(): "Not Available");
             jsonObject.addProperty("createddate", null != drillDownReportObject.getCreateddate() ? drillDownReportObject
                    .getCreateddate().toString(): "Not Available");
             jsonObject.addProperty("complainantname", null != drillDownReportObject.getComplaintname() ? drillDownReportObject
                     .getComplaintname().toString(): "Not Available");
             jsonObject.addProperty("details", null != drillDownReportObject.getDetails() ? drillDownReportObject
                     .getDetails().toString(): "Not Available");
             jsonObject.addProperty("boundaryname", null != drillDownReportObject.getBoundaryname() ? drillDownReportObject
                     .getBoundaryname().toString(): "Not Available");
             jsonObject.addProperty("status", null != drillDownReportObject.getStatus() ? drillDownReportObject
                     .getStatus().toString(): "Not Available");
             jsonObject.addProperty("complaintId", null != drillDownReportObject.getComplaintid() ? drillDownReportObject
                     .getComplaintid().toString(): "Not Available");
        }
        return jsonObject;
    }

}

