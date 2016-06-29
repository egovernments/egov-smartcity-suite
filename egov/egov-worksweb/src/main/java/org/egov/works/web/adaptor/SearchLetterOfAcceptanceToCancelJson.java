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

import org.egov.works.letterofacceptance.service.LetterOfAcceptanceService;
import org.egov.works.workorder.entity.WorkOrder;
import org.egov.works.workorder.entity.WorkOrderEstimate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

@Component
public class SearchLetterOfAcceptanceToCancelJson implements JsonSerializer<WorkOrder> {

    @Autowired
    private LetterOfAcceptanceService letterOfAcceptanceService;

    @Override
    public JsonElement serialize(final WorkOrder workOrder, final Type type, final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (workOrder != null) {
            if (workOrder.getWorkOrderNumber() != null)
                jsonObject.addProperty("workOrderNumber", workOrder.getWorkOrderNumber());
            else
                jsonObject.addProperty("workOrderNumber", "");
            if (workOrder.getWorkOrderDate() != null)
                jsonObject.addProperty("workOrderDate", workOrder.getWorkOrderDate().toString());
            else
                jsonObject.addProperty("workOrderDate", "");
            if (workOrder.getContractor() != null) {
                jsonObject.addProperty("contractor", workOrder.getContractor().getName());
                jsonObject.addProperty("contractorcode", workOrder.getContractor().getCode());
            } else {
                jsonObject.addProperty("contractor", "");
                jsonObject.addProperty("contractorcode", "");
            }

            if (!workOrder.getWorkOrderEstimates().isEmpty()) {
                final WorkOrderEstimate woe = workOrder.getWorkOrderEstimates().get(0);
                jsonObject.addProperty("estimateNumber", woe.getEstimate().getEstimateNumber());
                jsonObject.addProperty("nameOfWork", woe.getEstimate().getName());
                jsonObject.addProperty("workIdentificationNumber", woe.getEstimate().getProjectCode().getCode());
            }

            jsonObject.addProperty("isMileStoneCreated", letterOfAcceptanceService.checkIfMileStonesCreated(workOrder));
            jsonObject.addProperty("workOrderAmount", workOrder.getWorkOrderAmount());

            jsonObject.addProperty("id", workOrder.getId());
        }
        return jsonObject;
    }

}
