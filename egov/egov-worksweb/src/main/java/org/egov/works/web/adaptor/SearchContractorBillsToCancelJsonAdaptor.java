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
import org.egov.works.contractorbill.entity.ContractorBillRegister;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.lineestimate.service.LineEstimateService;
import org.egov.works.utils.WorksUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class SearchContractorBillsToCancelJsonAdaptor implements JsonSerializer<ContractorBillRegister> {

    @Autowired
    private LineEstimateService lineEstimateService;

    @Autowired
    private WorksUtils worksUtils;

    @Override
    public JsonElement serialize(final ContractorBillRegister contractorBillRegister, final Type type,
            final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (contractorBillRegister != null) {
            if (contractorBillRegister.getBillnumber() != null)
                jsonObject.addProperty("billNumber", contractorBillRegister.getBillnumber());
            else
                jsonObject.addProperty("billNumber", "");
            if (contractorBillRegister.getBilldate() != null)
                jsonObject.addProperty("billDate", contractorBillRegister.getBilldate().toString());
            else
                jsonObject.addProperty("billDate", "");
            if (contractorBillRegister.getWorkOrder().getEstimateNumber() != null) {
                final LineEstimateDetails led = lineEstimateService.findByEstimateNumber(contractorBillRegister.getWorkOrder()
                        .getEstimateNumber());
                jsonObject.addProperty("estimateNumber", led.getEstimateNumber());
                final String workIdentificationNumber = led.getProjectCode().getCode();
                jsonObject.addProperty("workIdentificationNumber", workIdentificationNumber);
            }
            else {
                jsonObject.addProperty("estimateNumber", "");
                jsonObject.addProperty("workIdentificationNumber", "");
            }
            if (contractorBillRegister.getWorkOrder() != null)
                jsonObject.addProperty("workOrderNumber", contractorBillRegister.getWorkOrder().getWorkOrderNumber());
            else
                jsonObject.addProperty("workOrderNumber", "");
            if (contractorBillRegister.getWorkOrder().getContractor() != null)
                jsonObject.addProperty("contractorName", contractorBillRegister.getWorkOrder().getContractor().getName());
            else
                jsonObject.addProperty("contractorName", "");
            if (contractorBillRegister.getWorkOrder().getContractor() != null)
                jsonObject.addProperty("contractorCode", contractorBillRegister.getWorkOrder().getContractor().getCode());
            else
                jsonObject.addProperty("contractorCode", "");
            if (contractorBillRegister.getEgBillregistermis() != null
                    && contractorBillRegister.getEgBillregistermis().getVoucherHeader() != null
                    && contractorBillRegister.getEgBillregistermis().getVoucherHeader().getStatus() != 4)
                jsonObject.addProperty("voucherNumber", contractorBillRegister.getEgBillregistermis().getVoucherHeader()
                        .getVoucherNumber());
            else
                jsonObject.addProperty("voucherNumber", "");

            jsonObject.addProperty("id", contractorBillRegister.getId());

        }
        return jsonObject;
    }
}
