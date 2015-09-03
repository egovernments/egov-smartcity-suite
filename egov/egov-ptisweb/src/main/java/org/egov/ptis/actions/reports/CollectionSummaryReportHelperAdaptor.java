package org.egov.ptis.actions.reports;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class CollectionSummaryReportHelperAdaptor implements JsonSerializer<CollectionSummaryReportResult> {

    @Override
    public JsonElement serialize(final CollectionSummaryReportResult collectionSummaryReportObj, final Type type,
            final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (collectionSummaryReportObj != null) {

            jsonObject.addProperty("boundaryName", collectionSummaryReportObj.getBoundaryName());
            jsonObject.addProperty("propertyType", collectionSummaryReportObj.getPropertyType());
            jsonObject.addProperty("arrearTaxAmount", collectionSummaryReportObj.getArrearTaxAmount());

            jsonObject.addProperty("arrearLibraryCess", collectionSummaryReportObj.getArrearLibraryCess());
            jsonObject.addProperty("arrearTotal", collectionSummaryReportObj.getArrearTotal());
            jsonObject.addProperty("taxAmount", collectionSummaryReportObj.getTaxAmount());

            jsonObject.addProperty("libraryCess", collectionSummaryReportObj.getLibraryCess());
            jsonObject.addProperty("currentTotal", collectionSummaryReportObj.getCurrentTotal());
            jsonObject.addProperty("penalty", collectionSummaryReportObj.getPenalty());
            jsonObject.addProperty("arrearPenalty", collectionSummaryReportObj.getArrearPenalty());
            jsonObject.addProperty("penaltyTotal", collectionSummaryReportObj.getPenaltyTotal());
            jsonObject.addProperty("total", collectionSummaryReportObj.getTotal());
        }
        return jsonObject;
    }

}
