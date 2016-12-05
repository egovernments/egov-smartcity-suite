
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
import org.egov.works.abstractestimate.entity.MeasurementSheet;
import org.egov.works.abstractestimate.service.ActivityService;
import org.egov.works.abstractestimate.service.MeasurementSheetService;
import org.egov.works.letterofacceptance.service.WorkOrderActivityService;
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
public class SearchActivityJsonAdaptor implements JsonSerializer<Activity> {

    @Autowired
    private WorkOrderActivityService workOrderActivityService;

    @Autowired
    private MBHeaderService mbHeaderService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private WorkOrderMeasurementSheetService workOrderMeasurementSheetService;

    @Autowired
    private MeasurementSheetService measurementSheetService;

    @Override
    public JsonElement serialize(final Activity activity, final Type typeOfSrc,
            final JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        final WorkOrderActivity workOrderActivity = workOrderActivityService.getWorkOrderActivityByActivity(activity.getId());
        if (activity.getSchedule() != null) {
            jsonObject.addProperty("description", activity.getSchedule().getDescription());
            jsonObject.addProperty("summary", activity.getSchedule().getSummary());
            jsonObject.addProperty("sorNonSorType", "SOR");
            jsonObject.addProperty("sorCode", activity.getSchedule().getCode());
            jsonObject.addProperty("categoryType", activity.getSchedule().getScheduleCategory().getCode());
        } else {
            if (activity.getNonSor() != null) {
                jsonObject.addProperty("description", activity.getNonSor().getDescription());
                jsonObject.addProperty("summary", activity.getNonSor().getDescription());
            } else {
                jsonObject.addProperty("description", "");
                jsonObject.addProperty("summary", "");
            }
            jsonObject.addProperty("sorNonSorType", "Non SOR");
            jsonObject.addProperty("sorCode", "");
            jsonObject.addProperty("categoryType", "");
        }

        if (activity.getUom() != null)
            jsonObject.addProperty("uom", activity.getUom().getUom());
        else
            jsonObject.addProperty("uom", "");

        Double quantity = activity.getQuantity();

        final List<Activity> cqActivities = activityService.findApprovedActivitiesByParentId(activity.getId());

        for (final Activity act : cqActivities)
            if (act.getRevisionType().equals(RevisionType.ADDITIONAL_QUANTITY))
                quantity = quantity + act.getQuantity();
            else
                quantity = quantity - act.getQuantity();

        jsonObject.addProperty("approvedQuantity", quantity);

        if (workOrderActivity != null) {
            deriveWorkOrderActivityQuantity(workOrderActivity);
            jsonObject.addProperty("estimateQuantity", workOrderActivity.getApprovedQuantity());
            final Double consumedQuantity = mbHeaderService.getPreviousCumulativeQuantity(-1L, workOrderActivity.getId());
            jsonObject.addProperty("consumedQuantity", consumedQuantity == null ? "0" : consumedQuantity.toString());
        } else {
            jsonObject.addProperty("estimateQuantity", 0);
            jsonObject.addProperty("consumedQuantity", 0);
        }

        if (activity.getRate() > 0)
            jsonObject.addProperty("rate", activity.getRate());
        else
            jsonObject.addProperty("rate", "");

        jsonObject.addProperty("estimateRate", activity.getEstimateRate());
        jsonObject.addProperty("id", activity.getId());

        if (!activity.getMeasurementSheetList().isEmpty()) {
            final JsonArray jsonArray = new JsonArray();
            for (final MeasurementSheet ms : activity.getMeasurementSheetList()) {
                final JsonObject child = new JsonObject();
                deriveMeasurementSheetQuantity(ms);
                child.addProperty("parent", ms.getId());
                child.addProperty("slNo", ms.getSlNo());
                child.addProperty("remarks", ms.getRemarks());
                child.addProperty("no", ms.getNo());
                child.addProperty("length", ms.getLength());
                child.addProperty("width", ms.getWidth());
                child.addProperty("depthOrHeight", ms.getDepthOrHeight());
                child.addProperty("quantity", ms.getQuantity());
                child.addProperty("identifier", ms.getIdentifier());
                jsonArray.add(child);
            }
            jsonObject.add("ms", jsonArray);
        } else
            jsonObject.add("ms", new JsonArray());

        return jsonObject;
    }

    private void deriveWorkOrderActivityQuantity(final WorkOrderActivity workOrderActivity) {
        if (!workOrderActivity.getWorkOrderMeasurementSheets().isEmpty())
            for (final WorkOrderMeasurementSheet woms : workOrderActivity.getWorkOrderMeasurementSheets()) {
                final List<WorkOrderMeasurementSheet> rewomsList = workOrderMeasurementSheetService
                        .findByMeasurementSheetParentId(woms.getMeasurementSheet().getId());
                Double no = woms.getNo() == null ? 0 : woms.getNo().doubleValue();
                Double length = woms.getLength() == null ? 0 : woms.getLength().doubleValue();
                Double width = woms.getWidth() == null ? 0 : woms.getWidth().doubleValue();
                Double depthOrHeight = woms.getDepthOrHeight() == null ? 0 : woms.getDepthOrHeight().doubleValue();
                Double quantity = woms.getQuantity() == null ? 0 : woms.getQuantity().doubleValue();
                for (final WorkOrderMeasurementSheet rems : rewomsList) {
                    if (rems.getNo() != null)
                        no = no + rems.getNo().doubleValue();
                    if (rems.getLength() != null)
                        length = length + rems.getLength().doubleValue();
                    if (rems.getWidth() != null)
                        width = width + rems.getWidth().doubleValue();
                    if (rems.getDepthOrHeight() != null)
                        depthOrHeight = depthOrHeight + rems.getDepthOrHeight().doubleValue();

                    quantity = quantity + rems.getQuantity().doubleValue();
                }
                if (no != null && no != 0)
                    woms.setNo(new BigDecimal(no));
                if (length != null && length != 0)
                    woms.setLength(new BigDecimal(length));
                if (width != null && width != 0)
                    woms.setWidth(new BigDecimal(width));
                if (depthOrHeight != null && depthOrHeight != 0)
                    woms.setDepthOrHeight(new BigDecimal(depthOrHeight));

                woms.setQuantity(new BigDecimal(quantity.toString()));
            }
        Double qty = 0d;
        for (final WorkOrderMeasurementSheet woms : workOrderActivity.getWorkOrderMeasurementSheets())
            if (woms.getMeasurementSheet().getIdentifier() == 'A')
                qty = qty + woms.getQuantity().doubleValue();
            else
                qty = qty - woms.getQuantity().doubleValue();
        if (!workOrderActivity.getWorkOrderMeasurementSheets().isEmpty())
            workOrderActivity.setApprovedQuantity(qty);
    }

    private void deriveMeasurementSheetQuantity(final MeasurementSheet measurementSheet) {
        List<MeasurementSheet> remsList = new ArrayList<>();
        remsList = measurementSheetService.findByParentId(measurementSheet.getId());
        Double no = measurementSheet.getNo() == null ? 0 : measurementSheet.getNo().doubleValue();
        Double length = measurementSheet.getLength() == null ? 0 : measurementSheet.getLength().doubleValue();
        Double width = measurementSheet.getWidth() == null ? 0 : measurementSheet.getWidth().doubleValue();
        Double depthOrHeight = measurementSheet.getDepthOrHeight() == null ? 0
                : measurementSheet.getDepthOrHeight().doubleValue();
        Double quantity = measurementSheet.getQuantity() == null ? 0 : measurementSheet.getQuantity().doubleValue();
        for (final MeasurementSheet rems : remsList) {
            if (rems.getNo() != null)
                no = no + rems.getNo().doubleValue();
            if (rems.getLength() != null)
                length = length + rems.getLength().doubleValue();
            if (rems.getWidth() != null)
                width = width + rems.getWidth().doubleValue();
            if (rems.getDepthOrHeight() != null)
                depthOrHeight = depthOrHeight + rems.getDepthOrHeight().doubleValue();

            quantity = quantity + rems.getQuantity().doubleValue();
        }

        if (no != null && no != 0)
            measurementSheet.setNo(new BigDecimal(no));
        if (length != null && length != 0)
            measurementSheet.setLength(new BigDecimal(length));
        if (width != null && width != 0)
            measurementSheet.setWidth(new BigDecimal(width));
        if (depthOrHeight != null && depthOrHeight != 0)
            measurementSheet.setDepthOrHeight(new BigDecimal(depthOrHeight));

        measurementSheet.setQuantity(new BigDecimal(quantity.toString()));
    }
}
