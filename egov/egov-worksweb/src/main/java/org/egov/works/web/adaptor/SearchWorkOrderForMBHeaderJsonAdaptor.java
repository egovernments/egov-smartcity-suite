package org.egov.works.web.adaptor;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;

import org.egov.works.workorder.entity.WorkOrder;
import org.egov.works.workorder.entity.WorkOrderEstimate;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

@Component
public class SearchWorkOrderForMBHeaderJsonAdaptor implements JsonSerializer<WorkOrderEstimate> {

    @Override
    public JsonElement serialize(WorkOrderEstimate workOrderEstimate, Type typeOfSrc,
            JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        jsonObject.addProperty("workOrderDate", sdf.format(workOrderEstimate.getWorkOrder().getWorkOrderDate()));
        jsonObject.addProperty("workOrderNumber", workOrderEstimate.getWorkOrder().getWorkOrderNumber());
        jsonObject.addProperty("agreementAmount", workOrderEstimate.getWorkOrder().getWorkOrderAmount());
        jsonObject.addProperty("contractorName", workOrderEstimate.getWorkOrder().getContractor().getName());
        jsonObject.addProperty("workIdentificationNumber", workOrderEstimate.getEstimate().getProjectCode().getCode());
        jsonObject.addProperty("estimateId", workOrderEstimate.getEstimate().getId());
        jsonObject.addProperty("id", workOrderEstimate.getWorkOrder().getId());
        return jsonObject;
    }

}
