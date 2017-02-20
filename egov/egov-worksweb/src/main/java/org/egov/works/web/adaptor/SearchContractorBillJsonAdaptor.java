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

import org.egov.infra.utils.StringUtils;
import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.contractorbill.entity.ContractorBillRegister;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.utils.WorksUtils;
import org.egov.works.workorder.entity.WorkOrder;
import org.egov.works.workorder.entity.WorkOrderEstimate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

@Component
public class SearchContractorBillJsonAdaptor implements JsonSerializer<ContractorBillRegister> {

    @Autowired
    private WorksUtils worksUtils;

    @Override
    public JsonElement serialize(final ContractorBillRegister contractorBillRegister, final Type type,
            final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (contractorBillRegister != null) {
            final WorkOrderEstimate woe = contractorBillRegister.getWorkOrderEstimate();
            final WorkOrder wo = woe.getWorkOrder();
            final AbstractEstimate ae = woe.getEstimate();
            final LineEstimateDetails led = ae.getLineEstimateDetails();
            setContractorBillRegisterJsonValues(contractorBillRegister, jsonObject, ae);
            jsonObject.addProperty("workIdentificationNumber",
                    ae.getProjectCode() != null ? ae.getProjectCode().getCode() : StringUtils.EMPTY);
            setWorkOrderJsonValues(jsonObject, wo);
            jsonObject.addProperty("lineEstimateId",
                    led != null ? led.getLineEstimate().getId().toString() : StringUtils.EMPTY);
            jsonObject.addProperty("workActivitySize", woe.getWorkOrderActivities().size());
            jsonObject.addProperty("abstractEstimateId", ae.getId());

        }
        return jsonObject;
    }

    private void setContractorBillRegisterJsonValues(final ContractorBillRegister contractorBillRegister,
            final JsonObject jsonObject, final AbstractEstimate ae) {
        jsonObject.addProperty("id", contractorBillRegister.getId());
        if (contractorBillRegister.getBillnumber() != null)
            jsonObject.addProperty("billNumber", contractorBillRegister.getBillnumber());
        else
            jsonObject.addProperty("billNumber", StringUtils.EMPTY);
        if (contractorBillRegister.getBilldate() != null)
            jsonObject.addProperty("billDate", contractorBillRegister.getBilldate().toString());
        else
            jsonObject.addProperty("billDate", StringUtils.EMPTY);
        if (contractorBillRegister.getBilltype() != null)
            jsonObject.addProperty("billType", contractorBillRegister.getBilltype());
        else
            jsonObject.addProperty("billType", StringUtils.EMPTY);
        if (contractorBillRegister.getBillamount() != null)
            jsonObject.addProperty("billValue", contractorBillRegister.getBillamount().toString());
        else
            jsonObject.addProperty("billValue", StringUtils.EMPTY);
        if (contractorBillRegister.getBillstatus() != null)
            jsonObject.addProperty("billStatus", contractorBillRegister.getBillstatus());
        else
            jsonObject.addProperty("billStatus", StringUtils.EMPTY);
        if (contractorBillRegister.getState().getOwnerPosition() != null)
            jsonObject.addProperty("owner",
                    worksUtils.getApproverName(contractorBillRegister.getState().getOwnerPosition().getId()));
        else
            jsonObject.addProperty("owner", StringUtils.EMPTY);
        jsonObject.addProperty("adminSanctionNumber", ae != null ? ae.getEstimateNumber() : StringUtils.EMPTY);
    }

    private void setWorkOrderJsonValues(final JsonObject jsonObject, final WorkOrder wo) {
        jsonObject.addProperty("workOrderNumber", wo != null ? wo.getWorkOrderNumber() : StringUtils.EMPTY);
        if (wo != null) {
            jsonObject.addProperty("contractorName",
                    wo.getContractor() != null ? wo.getContractor().getName() : StringUtils.EMPTY);
            jsonObject.addProperty("contractorCode",
                    wo.getContractor() != null ? wo.getContractor().getCode() : StringUtils.EMPTY);
            jsonObject.addProperty("workOrderId", wo.getId());
        }
    }
}
