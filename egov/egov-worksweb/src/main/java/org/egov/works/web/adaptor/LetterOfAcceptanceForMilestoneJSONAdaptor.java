package org.egov.works.web.adaptor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.lineestimate.service.LineEstimateService;
import org.egov.works.models.workorder.WorkOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class LetterOfAcceptanceForMilestoneJSONAdaptor implements JsonSerializer<WorkOrder> {

    @Autowired
    private LineEstimateService lineEstimateService;

    @Override
    public JsonElement serialize(final WorkOrder workOrder, final Type type,
            final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (workOrder != null)
            if (workOrder.getEstimateNumber() != null) {
                jsonObject.addProperty("estimateNumber", workOrder.getEstimateNumber());
                final LineEstimateDetails led = lineEstimateService.findByEstimateNumber(workOrder.getEstimateNumber());
                jsonObject.addProperty("typeOfWork", led.getLineEstimate().getTypeOfWork().getCode());
                if (led.getLineEstimate().getSubTypeOfWork() != null)
                    jsonObject.addProperty("subTypeOfWork", led.getLineEstimate().getSubTypeOfWork().getCode());
                else
                    jsonObject.addProperty("subTypeOfWork", "");
                jsonObject.addProperty("estimateDate", led.getLineEstimate().getLineEstimateDate().toString());
                jsonObject.addProperty("nameOfTheWork", led.getNameOfWork());
                jsonObject.addProperty("workIdentificationNumber", led.getProjectCode().getCode());
                jsonObject.addProperty("workOrderNumber", workOrder.getWorkOrderNumber());
                jsonObject.addProperty("workOrderAmount", workOrder.getWorkOrderAmount());
                jsonObject.addProperty("workOrderDate", workOrder.getWorkOrderDate().toString());

                jsonObject.addProperty("id", workOrder.getId());
            }
        return jsonObject;
    }
}
