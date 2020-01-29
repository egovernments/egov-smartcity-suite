package org.egov.ptis.actions.reports;

import java.lang.reflect.Type;

import org.egov.ptis.domain.entity.property.RevisionPetitionReport;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class RevisionPetitionReportAdaptor implements JsonSerializer<RevisionPetitionReport> {
    @Override
    public JsonElement serialize(final RevisionPetitionReport rpReport, final Type type, final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (rpReport != null) {
            jsonObject.addProperty("slNo", rpReport.getCount().toString());
            jsonObject.addProperty("ownerName", rpReport.getOwnerName());
            jsonObject.addProperty("assessmentNo", rpReport.getAssessmentNo());
            jsonObject.addProperty("receiptDate", rpReport.getCreatedDate());
            jsonObject.addProperty("noticeDate", rpReport.getNoticeDate());
            jsonObject.addProperty("PropertyType", rpReport.getPropertyType());

            jsonObject.addProperty("prevGenTax", rpReport.getRevisionPetitionReportTax().getPrevGenTax());
            jsonObject.addProperty("prevEduTax", rpReport.getRevisionPetitionReportTax().getPrevEduTax());
            jsonObject.addProperty("prevDrainageTax", rpReport.getRevisionPetitionReportTax().getPrevDrainageTax());
            jsonObject.addProperty("prevScavageTax", rpReport.getRevisionPetitionReportTax().getPrevSacvagTax());
            jsonObject.addProperty("prevLightTax", rpReport.getRevisionPetitionReportTax().getPrevLightTax());
            jsonObject.addProperty("prevWaterTax", rpReport.getRevisionPetitionReportTax().getPrevWaterTax());
            jsonObject.addProperty("prevLibTax", rpReport.getRevisionPetitionReportTax().getPrevLibTax());
            jsonObject.addProperty("prevTotalTax", rpReport.getRevisionPetitionReportTax().getPrevTotalTax());
            jsonObject.addProperty("prevUnauthPenaltyTax", rpReport.getRevisionPetitionReportTax().getPrevUnAuthPenaltyTax());

            jsonObject.addProperty("currentGenTax", rpReport.getRevisionPetitionReportTax().getCurrentGenTax());
            jsonObject.addProperty("currentEduTax", rpReport.getRevisionPetitionReportTax().getCurrentEduTax());
            jsonObject.addProperty("currentDrainageTax", rpReport.getRevisionPetitionReportTax().getCurrentDrainageTax());
            jsonObject.addProperty("currentScavageTax", rpReport.getRevisionPetitionReportTax().getCurrentSacvagTax());
            jsonObject.addProperty("currentLightTax", rpReport.getRevisionPetitionReportTax().getCurrentLightTax());
            jsonObject.addProperty("currentWaterTax", rpReport.getRevisionPetitionReportTax().getCurrentWaterTax());
            jsonObject.addProperty("currentLibTax", rpReport.getRevisionPetitionReportTax().getCurrentLibTax());
            jsonObject.addProperty("currentTotalTax", rpReport.getRevisionPetitionReportTax().getCurrentTotalTax());
            jsonObject.addProperty("currentUnauthPenaltyTax",
                    rpReport.getRevisionPetitionReportTax().getCurrentUnAuthPenaltyTax());
            jsonObject.addProperty("remarks", rpReport.getApproverRemarks());

        }
        return jsonObject;
    }
}