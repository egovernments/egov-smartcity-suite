
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

import org.egov.works.abstractestimate.entity.Activity;
import org.egov.works.abstractestimate.entity.MeasurementSheet;
import org.egov.works.letterofacceptance.service.WorkOrderActivityService;
import org.egov.works.mb.service.MBHeaderService;
import org.egov.works.revisionestimate.service.RevisionEstimateService;
import org.egov.works.workorder.entity.WorkOrderActivity;
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
    private RevisionEstimateService revisionEstimateService;

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

        if (workOrderActivity != null) {
            revisionEstimateService.deriveWorkOrderActivityQuantity(workOrderActivity);
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
                revisionEstimateService.deriveMeasurementSheetQuantity(ms);
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

        Double qty = 0d;
        for (final MeasurementSheet ms : activity.getMeasurementSheetList())
            if (ms.getIdentifier() == 'A')
                qty = qty + ms.getQuantity().doubleValue();
            else
                qty = qty - ms.getQuantity().doubleValue();
        if (!activity.getMeasurementSheetList().isEmpty())
            activity.setQuantity(qty);

        jsonObject.addProperty("approvedQuantity", activity.getQuantity());

        return jsonObject;
    }

}
