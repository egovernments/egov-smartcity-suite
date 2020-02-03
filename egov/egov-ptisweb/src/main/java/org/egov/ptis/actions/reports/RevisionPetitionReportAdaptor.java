package org.egov.ptis.actions.reports;

import java.lang.reflect.Type;
import java.math.BigDecimal;

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

            jsonObject.addProperty("prevGenTax", rpReport.getRevisionPetitionReportTax().getPrevGenTax() == null ? BigDecimal.ZERO
                    : rpReport.getRevisionPetitionReportTax().getPrevGenTax());
            jsonObject.addProperty("prevEduTax", rpReport.getRevisionPetitionReportTax().getPrevEduTax() == null ? BigDecimal.ZERO
                    : rpReport.getRevisionPetitionReportTax().getPrevEduTax());
            jsonObject.addProperty("prevDrainageTax", rpReport.getRevisionPetitionReportTax().getPrevDrainageTax() == null
                    ? BigDecimal.ZERO : rpReport.getRevisionPetitionReportTax().getPrevDrainageTax());
            jsonObject.addProperty("prevScavageTax", rpReport.getRevisionPetitionReportTax().getPrevSacvagTax() == null
                    ? BigDecimal.ZERO : rpReport.getRevisionPetitionReportTax().getPrevSacvagTax());
            jsonObject.addProperty("prevLightTax", rpReport.getRevisionPetitionReportTax().getPrevLightTax() == null
                    ? BigDecimal.ZERO : rpReport.getRevisionPetitionReportTax().getPrevLightTax());
            jsonObject.addProperty("prevWaterTax", rpReport.getRevisionPetitionReportTax().getPrevWaterTax() == null
                    ? BigDecimal.ZERO : rpReport.getRevisionPetitionReportTax().getPrevWaterTax());
            jsonObject.addProperty("prevLibTax", rpReport.getRevisionPetitionReportTax().getPrevLibTax() == null ? BigDecimal.ZERO
                    : rpReport.getRevisionPetitionReportTax().getPrevLibTax());
            jsonObject.addProperty("prevTotalTax", rpReport.getRevisionPetitionReportTax().getPrevTotalTax() == null
                    ? BigDecimal.ZERO : rpReport.getRevisionPetitionReportTax().getPrevTotalTax());
            jsonObject.addProperty("prevUnauthPenaltyTax",
                    rpReport.getRevisionPetitionReportTax().getPrevUnAuthPenaltyTax() == null ? BigDecimal.ZERO
                            : rpReport.getRevisionPetitionReportTax().getPrevUnAuthPenaltyTax());

            jsonObject.addProperty("currentGenTax", rpReport.getRevisionPetitionReportTax().getCurrentGenTax() == null
                    ? BigDecimal.ZERO : rpReport.getRevisionPetitionReportTax().getCurrentGenTax());
            jsonObject.addProperty("currentEduTax", rpReport.getRevisionPetitionReportTax().getCurrentEduTax() == null
                    ? BigDecimal.ZERO : rpReport.getRevisionPetitionReportTax().getCurrentEduTax());
            jsonObject.addProperty("currentDrainageTax", rpReport.getRevisionPetitionReportTax().getCurrentDrainageTax() == null
                    ? BigDecimal.ZERO : rpReport.getRevisionPetitionReportTax().getCurrentDrainageTax());
            jsonObject.addProperty("currentScavageTax", rpReport.getRevisionPetitionReportTax().getCurrentSacvagTax() == null
                    ? BigDecimal.ZERO : rpReport.getRevisionPetitionReportTax().getCurrentSacvagTax());
            jsonObject.addProperty("currentLightTax", rpReport.getRevisionPetitionReportTax().getCurrentLightTax() == null
                    ? BigDecimal.ZERO : rpReport.getRevisionPetitionReportTax().getCurrentLightTax());
            jsonObject.addProperty("currentWaterTax", rpReport.getRevisionPetitionReportTax().getCurrentWaterTax() == null
                    ? BigDecimal.ZERO : rpReport.getRevisionPetitionReportTax().getCurrentWaterTax());
            jsonObject.addProperty("currentLibTax", rpReport.getRevisionPetitionReportTax().getCurrentLibTax() == null
                    ? BigDecimal.ZERO : rpReport.getRevisionPetitionReportTax().getCurrentLibTax());
            jsonObject.addProperty("currentTotalTax", rpReport.getRevisionPetitionReportTax().getCurrentTotalTax() == null
                    ? BigDecimal.ZERO : rpReport.getRevisionPetitionReportTax().getCurrentTotalTax());
            jsonObject.addProperty("currentUnauthPenaltyTax",
                    rpReport.getRevisionPetitionReportTax().getCurrentUnAuthPenaltyTax() == null ? BigDecimal.ZERO
                            : rpReport.getRevisionPetitionReportTax().getCurrentUnAuthPenaltyTax());
            jsonObject.addProperty("remarks", rpReport.getApproverRemarks());

        }
        return jsonObject;
    }
}