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

import org.egov.commons.EgwTypeOfWork;
import org.egov.infra.utils.StringUtils;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.milestone.entity.Milestone;
import org.egov.works.workorder.entity.WorkOrder;
import org.egov.works.workorder.entity.WorkOrderEstimate;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

@Component
public class SearchMilestoneJsonAdaptor implements JsonSerializer<Milestone> {

    @Override
    public JsonElement serialize(final Milestone milestone, final Type type, final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (milestone != null) {
            final WorkOrderEstimate woe = milestone.getWorkOrderEstimate();
            if (woe != null) {
                final AbstractEstimate ae = woe.getEstimate();
                setAbstractEstimateJsonValues(jsonObject, ae);
                setWorkOrderJsonValues(jsonObject, woe);
            }
            jsonObject.addProperty("status",
                    milestone.getStatus() != null ? milestone.getStatus().getCode() : StringUtils.EMPTY);
            jsonObject.addProperty("id", milestone.getId());
        }
        return jsonObject;
    }

    private void setWorkOrderJsonValues(final JsonObject jsonObject, final WorkOrderEstimate woe) {
        final WorkOrder wo = woe.getWorkOrder();
        if (wo != null) {
            jsonObject.addProperty("agreementAmount", Double.toString(wo.getWorkOrderAmount()));
            jsonObject.addProperty("workOrderNumber", wo.getWorkOrderNumber());
            jsonObject.addProperty("workOrderId", wo.getId().toString());
        } else {
            jsonObject.addProperty("agreementAmount", StringUtils.EMPTY);
            jsonObject.addProperty("workOrderNumber", StringUtils.EMPTY);
            jsonObject.addProperty("workOrderId", StringUtils.EMPTY);
        }
    }

    private void setAbstractEstimateJsonValues(final JsonObject jsonObject, final AbstractEstimate ae) {

        if (ae != null) {
            final EgwTypeOfWork typeOfWork = ae.getParentCategory();
            final EgwTypeOfWork subTypeOfWork = ae.getCategory();
            final LineEstimateDetails led = ae.getLineEstimateDetails();
            jsonObject.addProperty("typeOfWork", typeOfWork != null ? typeOfWork.getName() : StringUtils.EMPTY);
            jsonObject.addProperty("subTypeOfWork",
                    subTypeOfWork != null ? subTypeOfWork.getName() : StringUtils.EMPTY);
            jsonObject.addProperty("lineEstimateId",
                    led != null ? led.getLineEstimate().getId().toString() : StringUtils.EMPTY);
            jsonObject.addProperty("estimateNumber", ae.getEstimateNumber());
            jsonObject.addProperty("workIdentificationNumber", ae.getProjectCode().getCode());
            jsonObject.addProperty("nameOfWork", ae.getName());
            jsonObject.addProperty("department", ae.getExecutingDepartment().getName());
            jsonObject.addProperty("abstractEstimateId", ae.getId().toString());
        } else {
            jsonObject.addProperty("estimateNumber", StringUtils.EMPTY);
            jsonObject.addProperty("workIdentificationNumber", StringUtils.EMPTY);
            jsonObject.addProperty("nameOfWork", StringUtils.EMPTY);
            jsonObject.addProperty("department", StringUtils.EMPTY);
            jsonObject.addProperty("abstractEstimateId", StringUtils.EMPTY);
        }

    }

}
