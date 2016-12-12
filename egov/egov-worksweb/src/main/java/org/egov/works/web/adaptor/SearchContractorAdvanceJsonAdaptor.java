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

import org.egov.works.contractoradvance.entity.ContractorAdvanceRequisition;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

@Component
public class SearchContractorAdvanceJsonAdaptor implements JsonSerializer<ContractorAdvanceRequisition> {

    @Autowired
    private WorksUtils worksUtils;

    @Override
    public JsonElement serialize(final ContractorAdvanceRequisition contractorAdvanceRequisition, final Type typeOfSrc,
            final JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("workOrderNumber",
                contractorAdvanceRequisition.getWorkOrderEstimate().getWorkOrder().getWorkOrderNumber());
        jsonObject.addProperty("advanceRequisitionNumber", contractorAdvanceRequisition.getAdvanceRequisitionNumber());
        jsonObject.addProperty("nameOfWork", contractorAdvanceRequisition.getWorkOrderEstimate().getEstimate().getName());
        jsonObject.addProperty("contractorName",
                contractorAdvanceRequisition.getWorkOrderEstimate().getWorkOrder().getContractor().getName() + " - "
                        + contractorAdvanceRequisition.getWorkOrderEstimate().getWorkOrder().getContractor().getCode());
        jsonObject.addProperty("requisitionAmount", contractorAdvanceRequisition.getAdvanceRequisitionAmount());
        jsonObject.addProperty("status", contractorAdvanceRequisition.getStatus().getDescription());

        if (contractorAdvanceRequisition.getState() != null) {
            if (contractorAdvanceRequisition.getStatus() != null
                    && (contractorAdvanceRequisition.getStatus().getCode().equalsIgnoreCase(WorksConstants.APPROVED)
                            || contractorAdvanceRequisition.getStatus().getCode()
                                    .equalsIgnoreCase(WorksConstants.CANCELLED_STATUS)))
                jsonObject.addProperty("currentowner", "NA");
            else
                jsonObject.addProperty("currentowner",
                        worksUtils.getApproverName(contractorAdvanceRequisition.getState().getOwnerPosition().getId()));
        } else
            jsonObject.addProperty("currentowner", "NA");

        jsonObject.addProperty("advanceBillNumber",
                contractorAdvanceRequisition.getEgAdvanceReqMises().getEgBillregister() != null
                        ? contractorAdvanceRequisition.getEgAdvanceReqMises().getEgBillregister().getBillnumber() : "NA");

        jsonObject.addProperty("contractorRequisitionId", contractorAdvanceRequisition.getId());
        return jsonObject;
    }

}
