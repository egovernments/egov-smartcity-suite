package org.egov.works.web.adaptor;

import java.lang.reflect.Type;

import org.egov.works.lineestimate.entity.LineEstimateAppropriation;
import org.egov.works.utils.WorksUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

@Component
public class EstimateAppropriationRegisterJSONAdaptor implements JsonSerializer<LineEstimateAppropriation>{

    @Autowired
    private WorksUtils worksUtils;
    
    @Override
    public JsonElement serialize(final LineEstimateAppropriation lineEstimateAppropriation, final Type type,
            final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (lineEstimateAppropriation != null) {
            if (lineEstimateAppropriation.getBudgetUsage().getAppropriationnumber() != null)
                jsonObject.addProperty("appropriationNumber", lineEstimateAppropriation.getBudgetUsage().getAppropriationnumber());
            else
                jsonObject.addProperty("appropriationNumber", "");
            if (lineEstimateAppropriation.getCreatedDate() != null)
                jsonObject.addProperty("appropriationDate", lineEstimateAppropriation.getBudgetUsage().getUpdatedTime().toString());
            else
                jsonObject.addProperty("appropriationDate", "");
            if (lineEstimateAppropriation.getBudgetUsage().getConsumedAmount() != null && lineEstimateAppropriation.getBudgetUsage().getConsumedAmount() > 0)
                jsonObject.addProperty("appropriationValue", lineEstimateAppropriation.getBudgetUsage().getConsumedAmount());
            else if(lineEstimateAppropriation.getBudgetUsage().getReleasedAmount() != null && lineEstimateAppropriation.getBudgetUsage().getReleasedAmount() < 0)
                jsonObject.addProperty("appropriationValue", lineEstimateAppropriation.getBudgetUsage().getReleasedAmount());
            else
                jsonObject.addProperty("appropriationValue", "");
            if (lineEstimateAppropriation.getLineEstimateDetails().getEstimateNumber() != null)
                jsonObject.addProperty("estimateNumber", lineEstimateAppropriation.getLineEstimateDetails().getEstimateNumber());
            else
                jsonObject.addProperty("estimateNumber", "");
            if (lineEstimateAppropriation.getLineEstimateDetails().getProjectCode().getCode() != null)
                jsonObject.addProperty("workIdentificationNumber", lineEstimateAppropriation.getLineEstimateDetails().getProjectCode().getCode());
            else
                jsonObject.addProperty("workIdentificationNumber", "");
            if (lineEstimateAppropriation.getLineEstimateDetails().getNameOfWork() != null)
                jsonObject.addProperty("nameOfWork", lineEstimateAppropriation.getLineEstimateDetails().getNameOfWork());
            else
                jsonObject.addProperty("nameOfWork", "");
            if (lineEstimateAppropriation.getLineEstimateDetails().getLineEstimate().getCreatedDate() != null)
                jsonObject.addProperty("estimateDate", lineEstimateAppropriation.getLineEstimateDetails().getLineEstimate().getCreatedDate().toString());
            else
                jsonObject.addProperty("estimateDate", "");
            if (lineEstimateAppropriation.getLineEstimateDetails().getEstimateAmount() != null)
                jsonObject.addProperty("estimateValue", lineEstimateAppropriation.getLineEstimateDetails().getEstimateAmount());
            else
                jsonObject.addProperty("estimateValue", "");
            
            jsonObject.addProperty("id", lineEstimateAppropriation.getId());
        }
        return jsonObject;
    }
    
}
