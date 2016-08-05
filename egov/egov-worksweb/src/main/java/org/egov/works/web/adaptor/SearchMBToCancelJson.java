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
import java.text.SimpleDateFormat;

import org.egov.works.contractorbill.entity.ContractorBillRegister;
import org.egov.works.contractorbill.entity.enums.BillTypes;
import org.egov.works.contractorbill.service.ContractorBillRegisterService;
import org.egov.works.mb.entity.MBHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

@Component
public class SearchMBToCancelJson implements JsonSerializer<MBHeader> {

    final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @Autowired
    private ContractorBillRegisterService contractorBillRegisterService;

    @Override
    public JsonElement serialize(final MBHeader mbHeader, final Type type, final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (mbHeader != null) {
            if (mbHeader.getWorkOrderEstimate() != null) {
                jsonObject.addProperty("workOrderNumber", mbHeader.getWorkOrderEstimate().getWorkOrder().getWorkOrderNumber());
                jsonObject.addProperty("workOrderId", mbHeader.getWorkOrderEstimate().getWorkOrder().getId());
                jsonObject.addProperty("contractor", mbHeader.getWorkOrderEstimate().getWorkOrder().getContractor().getName());
                jsonObject.addProperty("contractorcode",
                        mbHeader.getWorkOrderEstimate().getWorkOrder().getContractor().getCode());
                jsonObject.addProperty("estimateNumber", mbHeader.getWorkOrderEstimate().getEstimate().getEstimateNumber());
                jsonObject.addProperty("estimateId", mbHeader.getWorkOrderEstimate().getEstimate().getId());
                jsonObject.addProperty("workIdNumber", mbHeader.getWorkOrderEstimate().getEstimate().getProjectCode().getCode());
            } else {
                jsonObject.addProperty("workOrderNumber", "");
                jsonObject.addProperty("workOrderId", "");
                jsonObject.addProperty("contractor", "");
                jsonObject.addProperty("contractorcode", "");
                jsonObject.addProperty("estimateNumber", "");
                jsonObject.addProperty("estimateId", "");
                jsonObject.addProperty("workIdNumber", "");
            }
            if (mbHeader.getMbRefNo() != null)
                jsonObject.addProperty("mbRefNo", mbHeader.getMbRefNo());
            else
                jsonObject.addProperty("mbRefNo", "");
            if (mbHeader.getMbAmount() != null)
                jsonObject.addProperty("mbAmount", mbHeader.getMbAmount());
            else
                jsonObject.addProperty("mbAmount", "");
            if (mbHeader.getMbDate() != null)
                jsonObject.addProperty("mbDate", sdf.format(mbHeader.getMbDate()));
            else
                jsonObject.addProperty("mbDate", "");

            final ContractorBillRegister contractorBillRegister = contractorBillRegisterService
                    .getContratorBillForWorkOrder(mbHeader.getWorkOrderEstimate(),
                            ContractorBillRegister.BillStatus.CANCELLED.toString(),
                            BillTypes.Final_Bill.toString());
            if (contractorBillRegister != null)
                jsonObject.addProperty("billNumber", contractorBillRegister.getBillnumber());
            else
                jsonObject.addProperty("billNumber", "");

            jsonObject.addProperty("id", mbHeader.getId());
        }
        return jsonObject;
    }

}
