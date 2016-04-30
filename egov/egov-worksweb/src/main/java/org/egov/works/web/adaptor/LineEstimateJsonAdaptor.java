package org.egov.works.web.adaptor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.egov.works.lineestimate.entity.LineEstimate;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.utils.WorksUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class LineEstimateJsonAdaptor implements JsonSerializer<LineEstimate> {
    @Autowired
    private WorksUtils worksUtils;

    @Override
    public JsonElement serialize(final LineEstimate lineEstimate, final Type type, final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (lineEstimate != null) {
            if (lineEstimate.getLineEstimateNumber() != null)
                jsonObject.addProperty("adminSanctionNumber", lineEstimate.getAdminSanctionNumber());
            else
                jsonObject.addProperty("adminSanctionNumber", "");
            if (lineEstimate.getLineEstimateNumber() != null)
                jsonObject.addProperty("lineEstimateNumber", lineEstimate.getLineEstimateNumber());
            else
                jsonObject.addProperty("lineEstimateNumber", "");
            if (lineEstimate.getFund() != null)
                jsonObject.addProperty("fund", lineEstimate.getFund().getName());
            else
                jsonObject.addProperty("fund", "");
            if (lineEstimate.getFunction() != null)
                jsonObject.addProperty("function", lineEstimate.getFunction().getName());
            else
                jsonObject.addProperty("function", "");
            if (lineEstimate.getBudgetHead() != null)
                jsonObject.addProperty("budgetHead", lineEstimate.getBudgetHead().getName());
            else
                jsonObject.addProperty("budgetHead", "");
            if (lineEstimate.getExecutingDepartment() != null)
                jsonObject.addProperty("executingDepartment", lineEstimate.getExecutingDepartment().getName());
            else
                jsonObject.addProperty("executingDepartment", "");
            if (lineEstimate.getCreatedBy() != null)
                jsonObject.addProperty("createdBy", lineEstimate.getCreatedBy().getName());
            else
                jsonObject.addProperty("createdBy", "");
            if (lineEstimate.getAdminSanctionBy() != null)
                jsonObject.addProperty("approvedBy", lineEstimate.getAdminSanctionBy().getName());
            else
                jsonObject.addProperty("approvedBy", "");
            if (lineEstimate.getStatus() != null)
                jsonObject.addProperty("status", lineEstimate.getStatus().getDescription());
            else
                jsonObject.addProperty("status", "");
            if (lineEstimate.getState() != null && lineEstimate.getState().getOwnerPosition() != null)
                jsonObject.addProperty("owner", worksUtils.getApproverName(lineEstimate.getState().getOwnerPosition().getId()));
            else
                jsonObject.addProperty("owner", "");
            Long totalAmount = 0L;
            for (final LineEstimateDetails led : lineEstimate.getLineEstimateDetails())
                totalAmount += led.getEstimateAmount().longValue();
            jsonObject.addProperty("totalAmount", totalAmount);
            jsonObject.addProperty("id", lineEstimate.getId());
        }
        return jsonObject;
    }
}