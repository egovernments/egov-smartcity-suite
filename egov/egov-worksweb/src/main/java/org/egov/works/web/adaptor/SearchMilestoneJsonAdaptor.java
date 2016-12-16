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

import org.egov.works.milestone.entity.Milestone;
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
            if (milestone.getWorkOrderEstimate().getWorkOrder().getEstimateNumber() != null) {
                jsonObject.addProperty("estimateNumber",
                        milestone.getWorkOrderEstimate().getEstimate().getEstimateNumber());
                jsonObject.addProperty("workIdentificationNumber",
                        milestone.getWorkOrderEstimate().getEstimate().getProjectCode().getCode());
                jsonObject.addProperty("nameOfWork", milestone.getWorkOrderEstimate().getEstimate().getName());
                jsonObject.addProperty("department",
                        milestone.getWorkOrderEstimate().getEstimate().getExecutingDepartment().getName());
                if (milestone.getWorkOrderEstimate().getEstimate().getParentCategory() != null)
                    jsonObject.addProperty("typeOfWork",
                            milestone.getWorkOrderEstimate().getEstimate().getParentCategory().getName());
                if (milestone.getWorkOrderEstimate().getEstimate().getCategory() != null)
                    jsonObject.addProperty("subTypeOfWork",
                            milestone.getWorkOrderEstimate().getEstimate().getCategory().getName());
                if (milestone.getWorkOrderEstimate().getEstimate().getLineEstimateDetails() != null)
                    jsonObject.addProperty("lineEstimateId", milestone.getWorkOrderEstimate().getEstimate()
                            .getLineEstimateDetails().getLineEstimate().getId());
            } else {
                jsonObject.addProperty("estimateNumber", "");
                jsonObject.addProperty("workIdentificationNumber", "");
                jsonObject.addProperty("nameOfWork", "");
                jsonObject.addProperty("department", "");
                jsonObject.addProperty("typeOfWork", "");
                jsonObject.addProperty("subTypeOfWork", "");
                jsonObject.addProperty("lineEstimateId", "");
            }
            if (milestone.getWorkOrderEstimate().getWorkOrder() != null) {
                jsonObject.addProperty("agreementAmount",
                        milestone.getWorkOrderEstimate().getWorkOrder().getWorkOrderAmount());
                jsonObject.addProperty("workOrderNumber",
                        milestone.getWorkOrderEstimate().getWorkOrder().getWorkOrderNumber());
                jsonObject.addProperty("workOrderId", milestone.getWorkOrderEstimate().getWorkOrder().getId());
            } else {
                jsonObject.addProperty("agreementAmount", "");
                jsonObject.addProperty("workOrderNumber", "");
                jsonObject.addProperty("workOrderId", "");
            }
            if (milestone.getStatus() != null)
                jsonObject.addProperty("status", milestone.getStatus().getCode());
            else
                jsonObject.addProperty("status", "");

            jsonObject.addProperty("id", milestone.getId());

        }
        return jsonObject;
    }
}
