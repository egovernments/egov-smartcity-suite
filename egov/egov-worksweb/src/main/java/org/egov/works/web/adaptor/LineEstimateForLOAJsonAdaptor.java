package org.egov.works.web.adaptor;

import java.lang.reflect.Type;

import org.egov.works.lineestimate.entity.LineEstimateForLoaSearchResult;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class LineEstimateForLOAJsonAdaptor implements JsonSerializer<LineEstimateForLoaSearchResult> {
    @Override
    public JsonElement serialize(final LineEstimateForLoaSearchResult lineEstimateForLoaSearchResult, final Type type,
            final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (lineEstimateForLoaSearchResult != null) {
            if (lineEstimateForLoaSearchResult.getAdminSanctionNumber() != null)
                jsonObject.addProperty("adminSanctionNumber", lineEstimateForLoaSearchResult.getAdminSanctionNumber());
            else
                jsonObject.addProperty("adminSanctionNumber", "");
            if (lineEstimateForLoaSearchResult.getAdminSanctionBy() != null)
                jsonObject.addProperty("adminSanctionBy", lineEstimateForLoaSearchResult.getAdminSanctionBy());
            else
                jsonObject.addProperty("adminSanctionBy", "");
            if (lineEstimateForLoaSearchResult.getEstimateAmount() != null)
                jsonObject.addProperty("estimateAmount", lineEstimateForLoaSearchResult.getEstimateAmount());
            else
                jsonObject.addProperty("estimateAmount", "");
            if (lineEstimateForLoaSearchResult.getEstimateNumber() != null)
                jsonObject.addProperty("estimateNumber", lineEstimateForLoaSearchResult.getEstimateNumber());
            else
                jsonObject.addProperty("estimateNumber", "");
            if (lineEstimateForLoaSearchResult.getNameOfWork() != null)
                jsonObject.addProperty("nameOfWork", lineEstimateForLoaSearchResult.getNameOfWork());
            else
                jsonObject.addProperty("nameOfWork", "");
            if (lineEstimateForLoaSearchResult.getCreatedBy() != null)
                jsonObject.addProperty("createdBy", lineEstimateForLoaSearchResult.getCreatedBy());
            else
                jsonObject.addProperty("createdBy", "");
            if (lineEstimateForLoaSearchResult.getCurrentOwner() != null)
                jsonObject.addProperty("currentOwner", lineEstimateForLoaSearchResult.getCurrentOwner());
            else
                jsonObject.addProperty("currentOwner", "");
            if (lineEstimateForLoaSearchResult.getActualEstimateAmount() != null)
                jsonObject.addProperty("actualEstimateAmount", lineEstimateForLoaSearchResult.getActualEstimateAmount());
            else
                jsonObject.addProperty("actualEstimateAmount", "");
            if (lineEstimateForLoaSearchResult.getWorkIdentificationNumber() != null)
                jsonObject.addProperty("workIdentificationNumber", lineEstimateForLoaSearchResult.getWorkIdentificationNumber());
            else
                jsonObject.addProperty("workIdentificationNumber", "");
            jsonObject.addProperty("id", lineEstimateForLoaSearchResult.getId());
        }
        return jsonObject;
    }
}