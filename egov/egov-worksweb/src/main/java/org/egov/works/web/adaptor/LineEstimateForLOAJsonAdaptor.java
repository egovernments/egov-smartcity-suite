package org.egov.works.web.adaptor;

import java.lang.reflect.Type;
import org.egov.works.lineestimate.entity.LineEstimateSearchResult;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class LineEstimateForLOAJsonAdaptor implements JsonSerializer<LineEstimateSearchResult>
{
    @Override
    public JsonElement serialize(final LineEstimateSearchResult lineEstimateSearchResult, final Type type,
            final JsonSerializationContext jsc)
    {
        final JsonObject jsonObject = new JsonObject();
        if (lineEstimateSearchResult != null)
        {
            if (lineEstimateSearchResult.getAdminSanctionNumber() != null)
                jsonObject.addProperty("adminSanctionNumber", lineEstimateSearchResult.getAdminSanctionNumber());
            else
                jsonObject.addProperty("adminSanctionNumber", "");
            if (lineEstimateSearchResult.getApprovedBy() != null)
                jsonObject.addProperty("approvedBy", lineEstimateSearchResult.getApprovedBy());
            else
                jsonObject.addProperty("approvedBy", "");
            if (lineEstimateSearchResult.getEstimateAmount() != null)
                jsonObject.addProperty("estimateAmount", lineEstimateSearchResult.getEstimateAmount());
            else
                jsonObject.addProperty("estimateAmount", "");
            if (lineEstimateSearchResult.getEstimateNumber() != null)
                jsonObject.addProperty("estimateNumber", lineEstimateSearchResult.getEstimateNumber());
            else
                jsonObject.addProperty("estimateNumber", "");
            if (lineEstimateSearchResult.getNameOfWork() != null)
                jsonObject.addProperty("nameOfWork", lineEstimateSearchResult.getNameOfWork());
            else
                jsonObject.addProperty("nameOfWork", "");
            if (lineEstimateSearchResult.getCreatedBy() != null)
                jsonObject.addProperty("createdBy", lineEstimateSearchResult.getCreatedBy());
            else
                jsonObject.addProperty("createdBy", "");
            jsonObject.addProperty("id", lineEstimateSearchResult.getId());
        }
        return jsonObject;
    }
}