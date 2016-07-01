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
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

import org.egov.works.letterofacceptance.service.LetterOfAcceptanceService;
import org.egov.works.mb.entity.MBHeader;
import org.egov.works.mb.service.MBHeaderService;
import org.egov.works.workorder.entity.WorkOrderEstimate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

@Component
public class SearchLetterOfAcceptanceToCreateContractorBillJson implements JsonSerializer<WorkOrderEstimate> {

    @Autowired
    private LetterOfAcceptanceService letterOfAcceptanceService;

    @Autowired
    private MBHeaderService mBHeaderService;

    @Override
    public JsonElement serialize(final WorkOrderEstimate workOrderEstimate, final Type type, final JsonSerializationContext jsc) {
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        final JsonObject jsonObject = new JsonObject();
        if (workOrderEstimate != null) {
            if (workOrderEstimate.getWorkOrder().getWorkOrderNumber() != null)
                jsonObject.addProperty("workOrderNumber", workOrderEstimate.getWorkOrder().getWorkOrderNumber());
            else
                jsonObject.addProperty("workOrderNumber", "");
            if (workOrderEstimate.getWorkOrder().getWorkOrderDate() != null)
                jsonObject.addProperty("workOrderDate", formatter.format(workOrderEstimate.getWorkOrder().getWorkOrderDate()));
            else
                jsonObject.addProperty("workOrderDate", "");
            if (workOrderEstimate.getWorkOrder().getContractor() != null) {
                jsonObject.addProperty("contractor", workOrderEstimate.getWorkOrder().getContractor().getName());
                jsonObject.addProperty("contractorcode", workOrderEstimate.getWorkOrder().getContractor().getCode());
            } else {
                jsonObject.addProperty("contractor", "");
                jsonObject.addProperty("contractorcode", "");
            }

            if (workOrderEstimate.getEstimate().getEstimateNumber() != null) {
                jsonObject.addProperty("estimateNumber", workOrderEstimate.getEstimate().getEstimateNumber());
                jsonObject.addProperty("nameOfWork", workOrderEstimate.getEstimate().getName());
                jsonObject.addProperty("workIdentificationNumber", workOrderEstimate.getEstimate().getProjectCode().getCode());
            } else
                jsonObject.addProperty("estimateNumber", "");

            jsonObject.addProperty("isMileStoneCreated",
                    letterOfAcceptanceService.checkIfMileStonesCreated(workOrderEstimate.getWorkOrder()));
            jsonObject.addProperty("workOrderAmount", workOrderEstimate.getWorkOrder().getWorkOrderAmount());

            jsonObject.addProperty("id", workOrderEstimate.getWorkOrder().getId());
            if (workOrderEstimate.getWorkOrder().getWorkOrderEstimates() != null) {
                final List<MBHeader> mbHeaders = mBHeaderService
                        .getApprovedMBsForContractorBillByWorkOrderEstimateId(workOrderEstimate.getId());
                if (!mbHeaders.isEmpty()) {
                    String mbRefNumbers = "";
                    BigDecimal mbAmount = BigDecimal.ZERO;
                    for (final MBHeader header : mbHeaders) {
                        mbRefNumbers = mbRefNumbers + header.getMbRefNo() + ",";
                        mbAmount = mbAmount.add(header.getMbAmount());
                    }
                    jsonObject.addProperty("mbRefNumbers",
                            !mbRefNumbers.equalsIgnoreCase("") ? mbRefNumbers.substring(0, mbRefNumbers.length() - 1) : "");
                    jsonObject.addProperty("mbAmount", mbAmount.setScale(2, BigDecimal.ROUND_HALF_EVEN));
                } else {
                    jsonObject.addProperty("mbRefNumbers", "NA");
                    jsonObject.addProperty("mbAmount", "NA");
                }
            }
            if (workOrderEstimate.getEstimate() != null)
                jsonObject.addProperty("aeId", workOrderEstimate.getEstimate().getId());

            jsonObject.addProperty("woeId", workOrderEstimate.getId());
        }
        return jsonObject;
    }

}
