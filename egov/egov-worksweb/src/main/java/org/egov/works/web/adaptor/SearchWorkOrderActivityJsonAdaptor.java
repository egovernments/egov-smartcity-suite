
/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.works.web.adaptor;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.egov.works.abstractestimate.entity.Activity;
import org.egov.works.abstractestimate.service.ActivityService;
import org.egov.works.mb.service.MBHeaderService;
import org.egov.works.revisionestimate.entity.enums.RevisionType;
import org.egov.works.workorder.entity.WorkOrderActivity;
import org.egov.works.workorder.entity.WorkOrderMeasurementSheet;
import org.egov.works.workorder.service.WorkOrderMeasurementSheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

@Component
public class SearchWorkOrderActivityJsonAdaptor implements JsonSerializer<WorkOrderActivity> {

    @Autowired
    private MBHeaderService mbHeaderService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private WorkOrderMeasurementSheetService workOrderMeasurementSheetService;

    @Override
    public JsonElement serialize(final WorkOrderActivity workOrderActivity, final Type typeOfSrc,
            final JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        if (workOrderActivity.getActivity().getSchedule() != null) {
            jsonObject.addProperty("description", workOrderActivity.getActivity().getSchedule().getDescription());
            jsonObject.addProperty("summary", workOrderActivity.getActivity().getSchedule().getSummary());
            jsonObject.addProperty("sorNonSorType", "SOR");
            jsonObject.addProperty("sorCode", workOrderActivity.getActivity().getSchedule().getCode());
            jsonObject.addProperty("categoryType", workOrderActivity.getActivity().getSchedule().getScheduleCategory().getCode());
        } else {
            if (workOrderActivity.getActivity().getNonSor() != null) {
                jsonObject.addProperty("description", workOrderActivity.getActivity().getNonSor().getDescription());
                jsonObject.addProperty("summary", workOrderActivity.getActivity().getNonSor().getDescription());
            } else {
                jsonObject.addProperty("description", "");
                jsonObject.addProperty("summary", "");
            }
            jsonObject.addProperty("sorNonSorType", "Non SOR");
            jsonObject.addProperty("sorCode", "");
            jsonObject.addProperty("categoryType", "");
        }
        if (workOrderActivity.getActivity().getUom() != null)
            jsonObject.addProperty("uom", workOrderActivity.getActivity().getUom().getUom());
        else
            jsonObject.addProperty("uom", "");

        Double quantity = workOrderActivity.getActivity().getQuantity();

        final List<Activity> cqActivities = activityService
                .findApprovedActivitiesByParentId(workOrderActivity.getActivity().getId());

        for (final Activity act : cqActivities)
            if (act.getRevisionType().equals(RevisionType.ADDITIONAL_QUANTITY))
                quantity = quantity + act.getQuantity();
            else
                quantity = quantity - act.getQuantity();

        jsonObject.addProperty("approvedQuantity", quantity);

        jsonObject.addProperty("estimateRate", workOrderActivity.getActivity().getEstimateRate());
        jsonObject.addProperty("approvedRate", workOrderActivity.getApprovedRate());
        jsonObject.addProperty("approvedAmount", workOrderActivity.getApprovedAmount());
        jsonObject.addProperty("activityAmount", workOrderActivity.getActivity().getAmount().getValue());
        jsonObject.addProperty("unitRate", workOrderActivity.getActivity().getRate());
        jsonObject.addProperty("conversionFactor", workOrderActivity.getActivity().getConversionFactor());
        Double cumulativePreviousEntry = 0.0;
        cumulativePreviousEntry = mbHeaderService.getPreviousCumulativeQuantity(workOrderActivity.getMbHeaderId(),
                workOrderActivity.getId());

        if (cumulativePreviousEntry == null)
            jsonObject.addProperty("cumulativePreviousEntry", 0);
        else
            jsonObject.addProperty("cumulativePreviousEntry", cumulativePreviousEntry);

        jsonObject.addProperty("id", workOrderActivity.getId());

        if (!workOrderActivity.getWorkOrderMeasurementSheets().isEmpty()) {
            final JsonArray jsonArray = new JsonArray();
            for (final WorkOrderMeasurementSheet woms : workOrderActivity.getWorkOrderMeasurementSheets()) {
                final JsonObject child = new JsonObject();
                deriveMeasurementSheetQuantity(woms);
                child.addProperty("womsid", woms.getId());
                child.addProperty("slNo", woms.getMeasurementSheet().getSlNo());
                child.addProperty("remarks", woms.getMeasurementSheet().getRemarks());
                child.addProperty("no", woms.getNo());
                child.addProperty("length", woms.getLength());
                child.addProperty("width", woms.getWidth());
                child.addProperty("depthOrHeight", woms.getDepthOrHeight());
                child.addProperty("quantity", woms.getQuantity());
                child.addProperty("identifier", woms.getMeasurementSheet().getIdentifier());
                final Double mbmsCumulativePreviousEntry = mbHeaderService
                        .getMeasurementsPreviousCumulativeQuantity(workOrderActivity.getMbHeaderId(), woms.getId());
                child.addProperty("mbmsPreviousEntry", mbmsCumulativePreviousEntry != null ? mbmsCumulativePreviousEntry : 0);
                jsonArray.add(child);
            }
            jsonObject.add("woms", jsonArray);
        } else
            jsonObject.add("woms", new JsonArray());
        return jsonObject;
    }

    private void deriveMeasurementSheetQuantity(final WorkOrderMeasurementSheet workOrderMeasurementSheet) {
        List<WorkOrderMeasurementSheet> remsList = new ArrayList<>();
        remsList = workOrderMeasurementSheetService
                .findByMeasurementSheetParentId(workOrderMeasurementSheet.getMeasurementSheet().getId());
        Double no = workOrderMeasurementSheet.getNo() == null ? 0 : workOrderMeasurementSheet.getNo().doubleValue();
        Double length = workOrderMeasurementSheet.getLength() == null ? 0 : workOrderMeasurementSheet.getLength().doubleValue();
        Double width = workOrderMeasurementSheet.getWidth() == null ? 0 : workOrderMeasurementSheet.getWidth().doubleValue();
        Double depthOrHeight = workOrderMeasurementSheet.getDepthOrHeight() == null ? 0
                : workOrderMeasurementSheet.getDepthOrHeight().doubleValue();
        for (final WorkOrderMeasurementSheet rems : remsList) {
            if (rems.getNo() != null)
                no = no + rems.getNo().doubleValue();
            if (rems.getLength() != null)
                length = length + rems.getLength().doubleValue();
            if (rems.getWidth() != null)
                width = width + rems.getWidth().doubleValue();
            if (rems.getDepthOrHeight() != null)
                depthOrHeight = depthOrHeight + rems.getDepthOrHeight().doubleValue();
        }
        if (no != null && no != 0)
            workOrderMeasurementSheet.setNo(new BigDecimal(no.toString()));
        if (length != null && length != 0)
            workOrderMeasurementSheet.setLength(new BigDecimal(length.toString()));
        if (width != null && width != 0)
            workOrderMeasurementSheet.setWidth(new BigDecimal(width.toString()));
        if (depthOrHeight != null && depthOrHeight != 0)
            workOrderMeasurementSheet.setDepthOrHeight(new BigDecimal(depthOrHeight.toString()));

        workOrderMeasurementSheet.setQuantity(new BigDecimal(
                (no == null || no == 0 ? 1 : no.doubleValue()) * (length == null || length == 0 ? 1 : length.doubleValue())
                        * (width == null || width == 0 ? 1 : width.doubleValue())
                        * (depthOrHeight == null || depthOrHeight == 0 ? 1 : depthOrHeight.doubleValue())));
    }
}
