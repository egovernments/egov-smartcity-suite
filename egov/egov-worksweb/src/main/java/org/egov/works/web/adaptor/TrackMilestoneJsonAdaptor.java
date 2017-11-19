/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */

package org.egov.works.web.adaptor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.milestone.entity.Milestone;
import org.egov.works.milestone.entity.MilestoneActivity;
import org.egov.works.milestone.entity.TrackMilestone;
import org.egov.works.milestone.entity.TrackMilestoneActivity;
import org.egov.works.models.workorder.WorkOrder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

@Component
public class TrackMilestoneJsonAdaptor implements JsonSerializer<Milestone> {
    @Override
    public JsonElement serialize(final Milestone milestone, final Type type, final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        final DecimalFormat df = new DecimalFormat("0.00");
        if (milestone != null) {
            if (milestone.getWorkOrderEstimate().getEstimate().getLineEstimateDetails() != null) {
                final LineEstimateDetails led = milestone.getWorkOrderEstimate().getEstimate().getLineEstimateDetails();
                jsonObject.addProperty("estimateNumber", led.getEstimateNumber());
                jsonObject.addProperty("lineEstimateDate", sdf.format(led.getLineEstimate().getLineEstimateDate()));
                jsonObject.addProperty("nameOfWork", led.getNameOfWork());
                jsonObject.addProperty("projectCode", led.getProjectCode().getCode());
                jsonObject.addProperty("typeOfWork", led.getLineEstimate().getTypeOfWork().getDescription().toString());
                if (led.getLineEstimate().getSubTypeOfWork() != null)
                    jsonObject.addProperty("subTypeOfWork", led.getLineEstimate().getSubTypeOfWork().getDescription().toString());
                jsonObject.addProperty("lineEstimateCreatedBy", led.getLineEstimate().getCreatedBy().getName());
                jsonObject.addProperty("department", led.getLineEstimate().getExecutingDepartment().getName());
            } else {
                jsonObject.addProperty("estimateNumber", "");
                jsonObject.addProperty("lineEstimateDate", "");
                jsonObject.addProperty("nameOfWork", "");
                jsonObject.addProperty("projectCode", "");
                jsonObject.addProperty("typeOfWork", "");
                jsonObject.addProperty("subTypeOfWork", "");
                jsonObject.addProperty("lineEstimateCreatedBy", "");
                jsonObject.addProperty("department", "");
            }
            if (milestone.getWorkOrderEstimate().getWorkOrder() != null) {
                final WorkOrder workOrder = milestone.getWorkOrderEstimate().getWorkOrder();
                jsonObject.addProperty("workOrderNumber", workOrder.getWorkOrderNumber());
                jsonObject.addProperty("workOrderId", workOrder.getId());
                jsonObject.addProperty("workOrderAmount", df.format(workOrder.getWorkOrderAmount()));
                jsonObject.addProperty("workOrderDate", sdf.format(workOrder.getWorkOrderDate()));
                jsonObject.addProperty("contractorName", workOrder.getContractor().getName());
            } else {
                jsonObject.addProperty("workOrderNumber", "");
                jsonObject.addProperty("workOrderId", "");
                jsonObject.addProperty("workOrderAmount", "");
                jsonObject.addProperty("workOrderDate", "");
                jsonObject.addProperty("contractorName", "");
            }
            if (!milestone.getActivities().isEmpty()) {
                final JsonArray jsonArray = new JsonArray();
                for (final MilestoneActivity ma : milestone.getActivities()) {
                    final JsonObject child = new JsonObject();
                    child.addProperty("stageOrderNumber", ma.getStageOrderNo());
                    child.addProperty("description", ma.getDescription());
                    child.addProperty("percentage", ma.getPercentage());
                    child.addProperty("scheduleStartDate", sdf.format(ma.getScheduleStartDate()));
                    child.addProperty("scheduleEndDate", sdf.format(ma.getScheduleEndDate()));
                    child.addProperty("hiddenScheduleStartDate", sdf2.format(ma.getScheduleStartDate()));
                    child.addProperty("hiddenScheduleEndDate", sdf2.format(ma.getScheduleEndDate()));
                    jsonArray.add(child);
                }
                jsonObject.add("activities", jsonArray);
            } else
                jsonObject.add("activities", new JsonArray());
            if (!milestone.getTrackMilestone().isEmpty()) {
                jsonObject.addProperty("mode", "update");
                final JsonArray jsonArray = new JsonArray();
                for (final TrackMilestone ma : milestone.getTrackMilestone())
                    for (final TrackMilestoneActivity tma : ma.getActivities()) {
                        final JsonObject child = new JsonObject();
                        child.addProperty("currentStatus", tma.getStatus());
                        child.addProperty("completedPercentage", tma.getCompletedPercentage());
                        if (tma.getCompletionDate() != null)
                            child.addProperty("completionDate", sdf.format(tma.getCompletionDate()));
                        child.addProperty("reasonForDelay", tma.getRemarks());
                        jsonArray.add(child);
                    }
                jsonObject.add("trackMilestoneActivities", jsonArray);
            } else {
                jsonObject.addProperty("mode", "create");
                jsonObject.add("trackMilestoneActivities", new JsonArray());
            }

            jsonObject.addProperty("id", milestone.getId());
        }
        return jsonObject;
    }
}
