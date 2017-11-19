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
import org.egov.infra.utils.DateUtils;
import org.egov.works.contractorbill.entity.enums.BillTypes;
import org.egov.works.reports.entity.WorkProgressRegister;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class WorkProgressRegisterJsonAdaptor implements JsonSerializer<WorkProgressRegister> {
    @Autowired
    private WorksUtils worksUtils;

    @Override
    public JsonElement serialize(final WorkProgressRegister workProgressRegister, final Type type,
            final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (workProgressRegister != null) {
            if (workProgressRegister.getWard() != null) {
                if (workProgressRegister.getWard().getBoundaryType().getName()
                        .equalsIgnoreCase(WorksConstants.BOUNDARY_TYPE_CITY))
                    jsonObject.addProperty("ward", workProgressRegister.getWard().getName());
                else
                    jsonObject.addProperty("ward", workProgressRegister.getWard().getBoundaryNum());
            } else
                jsonObject.addProperty("ward", "");

            if (workProgressRegister.getLocation() != null)
                jsonObject.addProperty("location", workProgressRegister.getLocation().getName());
            else
                jsonObject.addProperty("location", "");
            if (workProgressRegister.getWorkCategory().toString() != null)
                jsonObject.addProperty("workCategory", workProgressRegister.getWorkCategory().toString().replace("_", " "));
            else
                jsonObject.addProperty("workCategory", "NA");
            if (workProgressRegister.getBeneficiary() != null)
                jsonObject.addProperty("beneficiaries",
                        workProgressRegister.getBeneficiary().toString().replaceAll("_C", " /C").replace("_", " "));
            else
                jsonObject.addProperty("beneficiaries", "NA");
            if (workProgressRegister.getNameOfWork() != null)
                jsonObject.addProperty("nameOfWork", workProgressRegister.getNameOfWork());
            else
                jsonObject.addProperty("nameOfWork", "");
            if (workProgressRegister.getWinCode() != null)
                jsonObject.addProperty("winCode", workProgressRegister.getWinCode());
            else
                jsonObject.addProperty("winCode", "");
            if (workProgressRegister.getFund() != null)
                jsonObject.addProperty("fund",
                        workProgressRegister.getFund().getCode() + " - " + workProgressRegister.getFund().getName());
            else
                jsonObject.addProperty("fund", "");
            if (workProgressRegister.getFunction() != null)
                jsonObject.addProperty("function",
                        workProgressRegister.getFunction().getCode() + " - " + workProgressRegister.getFunction().getName());
            else
                jsonObject.addProperty("function", "");
            if (workProgressRegister.getBudgetHead() != null)
                jsonObject.addProperty("budgetHead", workProgressRegister.getBudgetHead().getName());
            else
                jsonObject.addProperty("budgetHead", "");
            if (workProgressRegister.getTypeOfWork() != null)
                jsonObject.addProperty("typeOfWork", workProgressRegister.getTypeOfWork().getDescription());
            else
                jsonObject.addProperty("typeOfWork", "");
            if (workProgressRegister.getSubTypeOfWork() != null)
                jsonObject.addProperty("subTypeOfWork", workProgressRegister.getSubTypeOfWork().getDescription());
            else
                jsonObject.addProperty("subTypeOfWork", "");
            if (workProgressRegister.getAdminSanctionBy() != null)
                jsonObject.addProperty(
                        "adminSanctionAuthorityDate",
                        workProgressRegister.getAdminSanctionBy() + " , "
                                + DateUtils.getFormattedDate(workProgressRegister.getAdminSanctionDate(), "dd/MM/yyyy"));
            else
                jsonObject.addProperty("adminSanctionAuthorityDate", "");
            if (workProgressRegister.getAdminSanctionAmount() != null)
                jsonObject.addProperty("adminSanctionAmount", workProgressRegister.getAdminSanctionAmount());
            else
                jsonObject.addProperty("adminSanctionAmount", "");
            if (workProgressRegister.getTechnicalSanctionBy() != null)
                jsonObject.addProperty("technicalSanctionAuthorityDate",
                        worksUtils.getUserDesignation(workProgressRegister.getTechnicalSanctionBy()) + " - "
                                + workProgressRegister.getTechnicalSanctionBy().getName() + ", "
                                + DateUtils.getFormattedDate(workProgressRegister.getTechnicalSanctionDate(), "dd/MM/yyyy"));
            else
                jsonObject.addProperty("technicalSanctionAuthorityDate", "NA");
            if (workProgressRegister.getEstimateAmount() != null)
                jsonObject.addProperty("estimateAmount", workProgressRegister.getEstimateAmount());
            else
                jsonObject.addProperty("estimateAmount", "");
            if (workProgressRegister.getModeOfAllotment() != null)
                jsonObject.addProperty("modeOfAllotment", workProgressRegister.getModeOfAllotment());
            else
                jsonObject.addProperty("modeOfAllotment", "");
            if (workProgressRegister.getAgreementNumber() != null)
                jsonObject.addProperty("agreementNumberDate",
                        workProgressRegister.getAgreementNumber() + " - "
                                + DateUtils.getFormattedDate(workProgressRegister.getAgreementDate(), "dd/MM/yyyy"));
            else
                jsonObject.addProperty("agreementNumberDate", "");
            if (workProgressRegister.getContractor() != null)
                jsonObject.addProperty("contractorCodeName", workProgressRegister.getContractor().getCode() + " - "
                        + workProgressRegister.getContractor().getName());
            else
                jsonObject.addProperty("contractorCodeName", "");
            if (workProgressRegister.getAgreementAmount() != null)
                jsonObject.addProperty("agreementAmount", workProgressRegister.getAgreementAmount());
            else
                jsonObject.addProperty("agreementAmount", "");
            if (workProgressRegister.getLatestBillNumber() != null && workProgressRegister.getLatestBillDate() != null)
                jsonObject.addProperty("latestMbNumberDate",
                        workProgressRegister.getLatestMbNumber() + ", "
                                + DateUtils.getFormattedDate(workProgressRegister.getLatestMbDate(), "dd/MM/yyyy"));
            else
                jsonObject.addProperty("latestMbNumberDate", "NA");
            if (workProgressRegister.getLatestBillNumber() != null)
                jsonObject.addProperty("latestBillNumberDate",
                        workProgressRegister.getLatestBillNumber() + " - "
                                + DateUtils.getFormattedDate(workProgressRegister.getLatestBillDate(), "dd/MM/yyyy"));
            else
                jsonObject.addProperty("latestBillNumberDate", "NA");
            if (workProgressRegister.getBilltype() != null)
                jsonObject.addProperty("billType", workProgressRegister.getBilltype());
            else
                jsonObject.addProperty("billType", "NA");
            if (workProgressRegister.getBillamount() != null)
                jsonObject.addProperty("billAmount", workProgressRegister.getBillamount());
            else
                jsonObject.addProperty("billAmount", "");
            if (workProgressRegister.getTotalBillAmount() != null)
                jsonObject.addProperty("totalBillAmount", workProgressRegister.getTotalBillAmount());
            else
                jsonObject.addProperty("totalBillAmount", "");
            if (workProgressRegister.getTotalBillPaidSoFar() != null)
                jsonObject.addProperty("totalBillPaidSoFar", workProgressRegister.getTotalBillPaidSoFar());
            else
                jsonObject.addProperty("totalBillPaidSoFar", "");
            if (workProgressRegister.getBalanceValueOfWorkToBill() != null) {
                if (workProgressRegister.getBilltype() != null
                        && workProgressRegister.getBilltype().equalsIgnoreCase(BillTypes.Final_Bill.toString()))
                    jsonObject.addProperty("balanceValueOfWorkToBill", "NA");
                else
                    jsonObject.addProperty("balanceValueOfWorkToBill", workProgressRegister.getBalanceValueOfWorkToBill());
            } else
                jsonObject.addProperty("balanceValueOfWorkToBill", "");
            if (workProgressRegister.getMilestonePercentageCompleted() != null)
                jsonObject.addProperty("milestonePercentageCompleted", workProgressRegister.getMilestonePercentageCompleted());
            else
                jsonObject.addProperty("milestonePercentageCompleted", "NA");

            jsonObject.addProperty("createdDate",
                    DateUtils.getFormattedDate(workProgressRegister.getCreatedDate(), "dd/MM/yyyy hh:mm a"));
        }
        return jsonObject;
    }
}