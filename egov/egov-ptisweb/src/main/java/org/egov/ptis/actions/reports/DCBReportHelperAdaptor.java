package org.egov.ptis.actions.reports;

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
            jsonObject.addProperty("boundaryId", dCBReportObj.getBoundaryId());
            jsonObject.addProperty("boundaryName", dCBReportObj.getBoundaryName());
            jsonObject.addProperty("assessmentNo", dCBReportObj.getAssessmentNo());
            jsonObject.addProperty("houseNo", dCBReportObj.getHouseNo());

            jsonObject.addProperty("dmnd_arrearPT", dCBReportObj.getDmnd_arrearPT());
            jsonObject.addProperty("dmnd_arrearLC", dCBReportObj.getDmnd_arrearLC());
            jsonObject.addProperty("dmnd_arrearTotal", dCBReportObj.getDmnd_arrearTotal());

            jsonObject.addProperty("dmnd_currentPT", dCBReportObj.getDmnd_currentPT());
            jsonObject.addProperty("dmnd_currentLC", dCBReportObj.getDmnd_currentLC());
            jsonObject.addProperty("dmnd_currentTotal", dCBReportObj.getDmnd_currentTotal());
            jsonObject.addProperty("totalDemand", dCBReportObj.getTotalDemand());

            jsonObject.addProperty("clctn_arrearPT", dCBReportObj.getClctn_arrearPT());
            jsonObject.addProperty("clctn_arrearLC", dCBReportObj.getClctn_arrearLC());
            jsonObject.addProperty("clctn_arrearPFT", dCBReportObj.getClctn_arrearPFT());
            jsonObject.addProperty("clctn_arrearTotal", dCBReportObj.getClctn_arrearTotal());

            jsonObject.addProperty("clctn_currentPT", dCBReportObj.getClctn_currentPT());
            jsonObject.addProperty("clctn_currentLC", dCBReportObj.getClctn_currentLC());
            jsonObject.addProperty("clctn_currentPFT", dCBReportObj.getClctn_currentPFT());
            jsonObject.addProperty("clctn_currentTotal", dCBReportObj.getClctn_currentTotal());
            jsonObject.addProperty("totalCollection", dCBReportObj.getTotalCollection());

            jsonObject.addProperty("bal_arrearPT", dCBReportObj.getBal_arrearPT());
            jsonObject.addProperty("bal_currentPT", dCBReportObj.getBal_currentPT());
            jsonObject.addProperty("totalPTBalance", dCBReportObj.getTotalPTBalance());
        }
        return jsonObject;
    }

}
