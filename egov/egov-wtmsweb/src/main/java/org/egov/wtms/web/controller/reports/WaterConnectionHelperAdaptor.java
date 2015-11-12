package org.egov.wtms.web.controller.reports;

import java.lang.reflect.Type;

import org.egov.wtms.application.repository.WaterConnectionDetailsRepository;
import org.egov.wtms.application.repository.WaterConnectionRepository;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class WaterConnectionHelperAdaptor implements JsonSerializer<WaterConnectionReportResult> {

    @Override
    public JsonElement serialize(final WaterConnectionReportResult drillDownReportObject, final Type type,
            final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (drillDownReportObject != null) {

            jsonObject.addProperty("name", null != drillDownReportObject.getName() ? drillDownReportObject.getName()
                    .toString() : "Not Available");
            jsonObject.addProperty("changeofusage", null != drillDownReportObject.getChangeofusage() ? drillDownReportObject
                    .getChangeofusage().toString() : "0");
            jsonObject.addProperty("addconnection", null != drillDownReportObject.getAddconnection() ? drillDownReportObject
                    .getAddconnection().toString() : "0");
            jsonObject.addProperty("newconnection", null != drillDownReportObject.getNewconnection() ? drillDownReportObject
                    .getNewconnection().toString() : "0");
            jsonObject.addProperty("closeconnection", null != drillDownReportObject.getCloseconnection() ? drillDownReportObject
                    .getCloseconnection().toString() : "0");
          jsonObject.addProperty("reconnection", null != drillDownReportObject.getReconnection() ? drillDownReportObject
                    .getReconnection().toString() : "0");
          jsonObject.addProperty("total", null != drillDownReportObject.getTotal() ? drillDownReportObject.getTotal()
                  .toString() : "0");
        }
        return jsonObject;
    }

}
