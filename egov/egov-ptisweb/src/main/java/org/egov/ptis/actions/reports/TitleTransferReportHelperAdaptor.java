package org.egov.ptis.actions.reports;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class TitleTransferReportHelperAdaptor implements JsonSerializer<TitleTransferReportResult> {

    @Override
    public JsonElement serialize(final TitleTransferReportResult titleTransferReportObj, final Type type,
            final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (titleTransferReportObj != null) {
            jsonObject.addProperty("assessmentNo", titleTransferReportObj.getAssessmentNo());
            jsonObject.addProperty("ownerName", titleTransferReportObj.getOwnerName());
            jsonObject.addProperty("doorNo", titleTransferReportObj.getDoorNo());

            jsonObject.addProperty("location", titleTransferReportObj.getLocation());
            jsonObject.addProperty("propertyTax", titleTransferReportObj.getPropertyTax());
            jsonObject.addProperty("oldTitle", titleTransferReportObj.getOldTitle());

            jsonObject.addProperty("changedTitle", titleTransferReportObj.getChangedTitle());
            jsonObject.addProperty("dateOfTransfer", titleTransferReportObj.getDateOfTransfer());
            jsonObject.addProperty("commissionerOrder", titleTransferReportObj.getCommissionerOrder());
            jsonObject.addProperty("mutationFee", titleTransferReportObj.getMutationFee());
        }
        return jsonObject;
    }

}
