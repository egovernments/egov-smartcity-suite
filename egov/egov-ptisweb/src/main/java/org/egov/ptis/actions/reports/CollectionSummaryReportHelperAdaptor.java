package org.egov.ptis.actions.reports;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

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
            jsonObject.addProperty(
                    "arrearTotal",
                    collectionSummaryReportObj.getArrearTaxAmount().add(
                            collectionSummaryReportObj.getArrearLibraryCess()));
            jsonObject.addProperty("taxAmount", collectionSummaryReportObj.getTaxAmount());

            jsonObject.addProperty("libraryCess", collectionSummaryReportObj.getLibraryCess());
            jsonObject.addProperty(
                    "currentTotal",
                    collectionSummaryReportObj.getTaxAmount() != null ? collectionSummaryReportObj.getTaxAmount().add(
                            collectionSummaryReportObj.getLibraryCess()) : collectionSummaryReportObj.getLibraryCess());
            jsonObject.addProperty("penalty", collectionSummaryReportObj.getPenalty());
            jsonObject.addProperty("arrearPenalty", collectionSummaryReportObj.getArrearPenalty());
            jsonObject.addProperty("penaltyTotal",
                    collectionSummaryReportObj.getPenalty().add(collectionSummaryReportObj.getArrearPenalty()));
            jsonObject.addProperty("total", collectionSummaryReportObj.getTotal());
        }
        return jsonObject;
    }

}
