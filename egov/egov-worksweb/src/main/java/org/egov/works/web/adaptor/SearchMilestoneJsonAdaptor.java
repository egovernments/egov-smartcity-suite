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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.lineestimate.service.LineEstimateService;
import org.egov.works.milestone.entity.Milestone;
import org.egov.works.utils.WorksUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class SearchMilestoneJsonAdaptor implements JsonSerializer<Milestone> {

    @Autowired
    private LineEstimateService lineEstimateService;

    @Autowired
    private WorksUtils worksUtils;

    @Override
    public JsonElement serialize(final Milestone milestone, final Type type,
            final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (milestone != null) {
            if (milestone.getWorkOrderEstimate().getWorkOrder().getEstimateNumber() != null) {
                final LineEstimateDetails led = lineEstimateService.findByEstimateNumber(milestone.getWorkOrderEstimate()
                        .getWorkOrder()
                        .getEstimateNumber());
                jsonObject.addProperty("estimateNumber", led.getEstimateNumber());
                jsonObject.addProperty("workIdentificationNumber", led.getProjectCode().getCode());
                jsonObject.addProperty("nameOfWork", led.getNameOfWork());
                jsonObject.addProperty("department", led.getLineEstimate().getExecutingDepartment().getName());
                if(led.getLineEstimate().getTypeOfWork() != null){
                jsonObject.addProperty("typeOfWork", led.getLineEstimate().getTypeOfWork().getDescription());
                }
                if(led.getLineEstimate().getSubTypeOfWork() != null){
                    jsonObject.addProperty("subTypeOfWork", led.getLineEstimate().getSubTypeOfWork().getDescription());
                }
                jsonObject.addProperty("lineEstimateId", led.getLineEstimate().getId());
            }
            else {
                jsonObject.addProperty("estimateNumber", "");
                jsonObject.addProperty("workIdentificationNumber", "");
                jsonObject.addProperty("nameOfWork", "");
                jsonObject.addProperty("department", "");
                jsonObject.addProperty("typeOfWork", "");
                jsonObject.addProperty("subTypeOfWork", "");
                jsonObject.addProperty("lineEstimateId", "");
            }
            if (milestone.getWorkOrderEstimate().getWorkOrder() != null) {
                jsonObject.addProperty("agreementAmount", milestone.getWorkOrderEstimate().getWorkOrder().getWorkOrderAmount());
                jsonObject.addProperty("workOrderNumber", milestone.getWorkOrderEstimate().getWorkOrder().getWorkOrderNumber());
                jsonObject.addProperty("workOrderId", milestone.getWorkOrderEstimate().getWorkOrder().getId());
            }
            else {
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
