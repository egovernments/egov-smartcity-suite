package org.egov.works.web.controller.letterofacceptance;

import java.lang.reflect.Type;

import org.egov.works.models.milestone.Milestone;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class SearchLetterOfAcceptanceJsonAdaptor implements JsonSerializer<Milestone>
{
    @Override
    public JsonElement serialize(final Milestone milestone, final Type type, final JsonSerializationContext jsc)
    {
        
        final JsonObject jsonObject = new JsonObject();
     /*   if (milestone != null)
            if (milestone.get() != null)
                jsonObject.addProperty("workOrderNumber", workOrder.getWorkOrderNumber());
            else
                jsonObject.addProperty("workOrderNumber", "");
            if (mil.getEstimateNumber() != null)
                jsonObject.addProperty("estimateNumber", milestoneSearchRequest.getEstimateNumber());
            else
                jsonObject.addProperty("estimateNumber", "");
            if (milestoneSearchRequest.getProjectCode() != null)
                jsonObject.addProperty("projectCode", milestoneSearchRequest.getProjectCode().getName());
            else
                jsonObject.addProperty("projectCode", "");
            if (milestoneSearchRequest.getWorkOrder() != null)
                jsonObject.addProperty("workOrder", milestoneSearchRequest.getWorkOrder().getWorkOrderDetails());
            else
                jsonObject.addProperty("workOrder", "");
            if (milestoneSearchRequest.getNatureOfWork() != null)
                jsonObject.addProperty("natureOfWork", milestoneSearchRequest.getNatureOfWork().getName());
            else
                jsonObject.addProperty("natureOfWork", "");
            if (milestoneSearchRequest.getEstimateDate() != null)
                jsonObject.addProperty("estimateDate", milestoneSearchRequest.getEstimateDate());
            else
                jsonObject.addProperty("estimateDate", "");
            if (milestoneSearchRequest.getWorkValue() != null)
                jsonObject.addProperty("workValue", milestoneSearchRequest.getWorkValue().getValue());
            else
                jsonObject.addProperty("workValue", "");
            if (milestoneSearchRequest.getWorkOrderNumber() != null)
                jsonObject.addProperty("workOrderNumber", milestoneSearchRequest.getWorkOrderNumber().getWorkOrderNumber());
            else
                jsonObject.addProperty("workOrderNumber", "");
            if (milestoneSearchRequest.getWorkOrderDate() != null)
                jsonObject.addProperty("workOrderDate", milestoneSearchRequest.getWorkOrderDate().getWorkOrderDate().getDate());
            else
                jsonObject.addProperty("workOrderDate", "");
         // TODO: check  id thing need need to change JSONSerilizer object
            // jsonObject.addProperty("id", Milestone.getId()); 
        }
 */       return jsonObject;
    }
}