package org.egov.wtms.web.controller.reports;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DCBReportHelperAdaptor implements JsonSerializer<DCBReportResult> {

    @Override
    public JsonElement serialize(final DCBReportResult dCBReportObj, final Type type, final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (dCBReportObj != null) {
            jsonObject.addProperty("boundaryName", dCBReportObj.getBoundaryName());
            jsonObject.addProperty("id", dCBReportObj.getId());
            jsonObject.addProperty("boundaryId", dCBReportObj.getBoundaryId());
            jsonObject.addProperty("propertyid", dCBReportObj.getPropertyid());
            jsonObject.addProperty("address", dCBReportObj.getAddress());
            jsonObject.addProperty("hscno", dCBReportObj.getHscno());
            jsonObject.addProperty("username", dCBReportObj.getUsername());
            jsonObject.addProperty("zoneid", dCBReportObj.getZoneid());
            jsonObject.addProperty("wardid", dCBReportObj.getWardid());
            jsonObject.addProperty("block", dCBReportObj.getBlock());
            jsonObject.addProperty("locality", dCBReportObj.getLocality());
            jsonObject.addProperty("street", dCBReportObj.getStreet());
            jsonObject.addProperty("connectiontype", dCBReportObj.getConnectiontype());

            jsonObject.addProperty("curr_demand", dCBReportObj.getCurr_demand());
            jsonObject.addProperty("arr_demand", dCBReportObj.getArr_demand());
            jsonObject.addProperty("total_demand", dCBReportObj.getTotal_demand());

            jsonObject.addProperty("curr_coll", dCBReportObj.getCurr_coll());
            jsonObject.addProperty("arr_coll", dCBReportObj.getArr_coll());
            jsonObject.addProperty("total_coll", dCBReportObj.getTotal_coll());

            jsonObject.addProperty("curr_balance", dCBReportObj.getCurr_balance());
            jsonObject.addProperty("arr_balance", dCBReportObj.getArr_balance());
            jsonObject.addProperty("total_balance", dCBReportObj.getTotal_balance());

        }
        return jsonObject;
    }

}
