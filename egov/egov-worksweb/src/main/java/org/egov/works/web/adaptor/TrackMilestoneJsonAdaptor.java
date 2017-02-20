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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.egov.infra.utils.StringUtils;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.milestone.entity.Milestone;
import org.egov.works.milestone.entity.MilestoneActivity;
import org.egov.works.milestone.entity.TrackMilestone;
import org.egov.works.milestone.entity.TrackMilestoneActivity;
import org.egov.works.utils.WorksConstants;
import org.egov.works.workorder.entity.WorkOrder;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

@Component
public class TrackMilestoneJsonAdaptor implements JsonSerializer<Milestone> {
    @Override
    public JsonElement serialize(final Milestone milestone, final Type type, final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        final DecimalFormat df = new DecimalFormat("0.00");
        if (milestone != null) {
            setMileStoneJsonAdaptorValues(milestone, jsonObject, sdf, sdf2, df);
        }
        return jsonObject;
    }

    private void setMileStoneJsonAdaptorValues(final Milestone milestone, final JsonObject jsonObject,
            final SimpleDateFormat sdf, final SimpleDateFormat sdf2, final DecimalFormat df) {
        final AbstractEstimate ae = milestone.getWorkOrderEstimate().getEstimate();
        final LineEstimateDetails led = ae.getLineEstimateDetails();
        final WorkOrder workOrder = milestone.getWorkOrderEstimate().getWorkOrder();
        jsonObject.addProperty("estimateNumber", ae.getEstimateNumber());
        if(led != null)
            jsonObject.addProperty("lineEstimateDate", sdf.format(led.getLineEstimate().getLineEstimateDate()));
        jsonObject.addProperty("nameOfWork", ae.getName());
        jsonObject.addProperty("projectCode", ae.getProjectCode().getCode());
        jsonObject.addProperty("typeOfWork", ae.getParentCategory().getName());
        jsonObject.addProperty("subTypeOfWork",
                ae.getCategory() != null ? ae.getCategory().getName() : StringUtils.EMPTY);
        jsonObject.addProperty("lineEstimateCreatedBy", led != null ? led.getLineEstimate().getCreatedBy().getName()
                : StringUtils.EMPTY);
        jsonObject.addProperty("department", ae.getExecutingDepartment().getName());

        jsonObject.addProperty("workOrderNumber",
                workOrder != null ? workOrder.getWorkOrderNumber() : StringUtils.EMPTY);
        jsonObject.addProperty("workOrderId",
                workOrder != null ? workOrder.getId().toString() : StringUtils.EMPTY);
        jsonObject.addProperty("workOrderAmount", workOrder != null ? df.format(workOrder.getWorkOrderAmount())
                : StringUtils.EMPTY);
        jsonObject.addProperty("workOrderDate", workOrder != null ? sdf.format(workOrder.getWorkOrderDate())
                : StringUtils.EMPTY);
        jsonObject.addProperty("contractorName", workOrder != null ? workOrder.getContractor().getName()
                : StringUtils.EMPTY);
        jsonObject.add("activities", !milestone.getActivities().isEmpty()
                ? getMileStoneActivities(milestone, sdf, sdf2) : new JsonArray());
        setTrackMilestoneActivities(milestone, jsonObject, sdf);
        jsonObject.addProperty("id", milestone.getId());
    }

    private void setTrackMilestoneActivities(final Milestone milestone, final JsonObject jsonObject,
            final SimpleDateFormat sdf) {
        if (!milestone.getTrackMilestone().isEmpty()) {
            jsonObject.addProperty(WorksConstants.MODE, "update");
            jsonObject.add("trackMilestoneActivities", getTrackMileStoneActivities(milestone, sdf));
        } else {
            jsonObject.addProperty(WorksConstants.MODE, "create");
            jsonObject.add("trackMilestoneActivities", new JsonArray());
        }
    }

    private JsonArray getTrackMileStoneActivities(final Milestone milestone, final SimpleDateFormat sdf) {
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
        return jsonArray;
    }

    private JsonArray getMileStoneActivities(final Milestone milestone, final SimpleDateFormat sdf,
            final SimpleDateFormat sdf2) {
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
        return jsonArray;
    }
}
