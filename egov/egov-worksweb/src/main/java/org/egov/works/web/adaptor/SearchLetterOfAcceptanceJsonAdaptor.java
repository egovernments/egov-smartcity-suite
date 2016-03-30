package org.egov.works.web.adaptor;

import java.lang.reflect.Type;

import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.lineestimate.repository.LineEstimateDetailsRepository;
import org.egov.works.models.workorder.WorkOrder;
import org.egov.works.utils.WorksUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

@Component
public class SearchLetterOfAcceptanceJsonAdaptor implements JsonSerializer<WorkOrder>
{
    @Autowired
    private WorksUtils worksUtils;

    @Autowired
    private LineEstimateDetailsRepository lineEstimateDetailsRepository;

    @Override
    public JsonElement serialize(final WorkOrder workOrder, final Type type, final JsonSerializationContext jsc)
    {
        final JsonObject jsonObject = new JsonObject();
        if (workOrder != null)
        {
            if (workOrder.getWorkOrderNumber() != null)
                jsonObject.addProperty("workOrderNumber", workOrder.getWorkOrderNumber());
            else
                jsonObject.addProperty("workOrderNumber", "");
            if (workOrder.getWorkOrderDate() != null)
                jsonObject.addProperty("workOrderDate", workOrder.getWorkOrderDate().toString());
            else
                jsonObject.addProperty("workOrderDate", "");
            if (workOrder.getContractor() != null)
                jsonObject.addProperty("contractor", workOrder.getContractor().getName());
            else
                jsonObject.addProperty("contractor", "");
            if (workOrder.getEgwStatus() != null)
                jsonObject.addProperty("status", workOrder.getEgwStatus().getCode());
            else
                jsonObject.addProperty("status", "");
            if (workOrder.getEstimateNumber() != null) {
                jsonObject.addProperty("estimateNumber", workOrder.getEstimateNumber());
                final LineEstimateDetails led = lineEstimateDetailsRepository.findByEstimateNumber(workOrder.getEstimateNumber());
                final String nameOfWork = led.getNameOfWork();
                jsonObject.addProperty("nameOfWork", nameOfWork);
            }
            else
                jsonObject.addProperty("estimateNumber", "");

            jsonObject.addProperty("workOrderAmount", workOrder.getWorkOrderAmount());

            jsonObject.addProperty("id", workOrder.getId());
        }
        return jsonObject;
    }
}