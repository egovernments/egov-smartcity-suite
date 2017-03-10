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

import org.egov.infra.utils.DateUtils;
import org.egov.infra.utils.StringUtils;
import org.egov.works.contractorbill.entity.enums.BillTypes;
import org.egov.works.reports.entity.WorkProgressRegister;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

@Component
public class WorkProgressRegisterJsonAdaptor implements JsonSerializer<WorkProgressRegister> {

    private static final String DATEFORMAT = "dd/MM/yyyy";
    private static final String BALANCEVALUEOFWORKTOBILL = "balanceValueOfWorkToBill";
    @Autowired
    private WorksUtils worksUtils;

    @Override
    public JsonElement serialize(final WorkProgressRegister workProgressRegister, final Type type,
            final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (workProgressRegister != null) {
            setWardJsonValue(workProgressRegister, jsonObject);

            if (workProgressRegister.getLocation() != null)
                jsonObject.addProperty("location", workProgressRegister.getLocation().getName());
            else
                jsonObject.addProperty("location", StringUtils.EMPTY);
            setWorkJsonValues(workProgressRegister, jsonObject);
            setBeneficiaryJsonValue(workProgressRegister, jsonObject);
            setNameOfWorkJsonValue(workProgressRegister, jsonObject);
            setWINCodeJsonValue(workProgressRegister, jsonObject);
            setBudgetJsonValues(workProgressRegister, jsonObject);
            setTOWJsonValues(workProgressRegister, jsonObject);
            setAdminSanctionJsonValues(workProgressRegister, jsonObject);
            setTechnicalSanctionAuthorityJsonValue(workProgressRegister, jsonObject);
            if (workProgressRegister.getEstimatevalue() != null)
                jsonObject.addProperty("estimateAmount", workProgressRegister.getEstimatevalue());
            else
                jsonObject.addProperty("estimateAmount", StringUtils.EMPTY);
            if (workProgressRegister.getModeOfAllotment() != null)
                jsonObject.addProperty("modeOfAllotment", workProgressRegister.getModeOfAllotment());
            else
                jsonObject.addProperty("modeOfAllotment", StringUtils.EMPTY);
            setAgreementJsonValues(workProgressRegister, jsonObject);
            if (workProgressRegister.getContractor() != null)
                jsonObject.addProperty("contractorCodeName", workProgressRegister.getContractor().getCode() + " - "
                        + workProgressRegister.getContractor().getName());
            else
                jsonObject.addProperty("contractorCodeName", WorksConstants.NA);

            setMBJsonValue(workProgressRegister, jsonObject);
            setBillJsonValues(workProgressRegister, jsonObject);
            setMilestonePercentageJsonValue(workProgressRegister, jsonObject);

            jsonObject.addProperty("createdDate",
                    DateUtils.getFormattedDate(workProgressRegister.getCreatedDate(), DATEFORMAT + " hh:mm a"));
        }
        return jsonObject;
    }

    private void setWINCodeJsonValue(final WorkProgressRegister workProgressRegister, final JsonObject jsonObject) {
        if (workProgressRegister.getWinCode() != null)
            jsonObject.addProperty("winCodeEstimateNumber",
                    workProgressRegister.getWinCode() + ", " + workProgressRegister.getEstimateNumber());
        else
            jsonObject.addProperty("winCodeEstimateNumber", StringUtils.EMPTY);
    }

    private void setNameOfWorkJsonValue(final WorkProgressRegister workProgressRegister, final JsonObject jsonObject) {
        if (workProgressRegister.getNameOfWork() != null)
            jsonObject.addProperty("nameOfWork", workProgressRegister.getNameOfWork());
        else
            jsonObject.addProperty("nameOfWork", StringUtils.EMPTY);
    }

    private void setBeneficiaryJsonValue(final WorkProgressRegister workProgressRegister, final JsonObject jsonObject) {
        if (workProgressRegister.getBeneficiary() != null)
            jsonObject.addProperty("beneficiaries",
                    workProgressRegister.getBeneficiary().toString().replaceAll("_C", " /C").replace("_", " "));
        else
            jsonObject.addProperty("beneficiaries", WorksConstants.NA);
    }

    private void setTechnicalSanctionAuthorityJsonValue(final WorkProgressRegister workProgressRegister,
            final JsonObject jsonObject) {
        if (workProgressRegister.getTechnicalSanctionBy() != null)
            jsonObject.addProperty("technicalSanctionAuthorityDate",
                    worksUtils.getUserDesignation(workProgressRegister.getTechnicalSanctionBy()) + " - "
                            + workProgressRegister.getTechnicalSanctionBy().getName() + ", "
                            + DateUtils.getFormattedDate(workProgressRegister.getTechnicalSanctionDate(),
                                    "dd/MM/yyyy"));
        else
            jsonObject.addProperty("technicalSanctionAuthorityDate", WorksConstants.NA);
    }

    private void setMilestonePercentageJsonValue(final WorkProgressRegister workProgressRegister,
            final JsonObject jsonObject) {
        if (workProgressRegister.getMilestonePercentageCompleted() != null)
            jsonObject.addProperty("milestonePercentageCompleted",
                    workProgressRegister.getMilestonePercentageCompleted());
        else
            jsonObject.addProperty("milestonePercentageCompleted", WorksConstants.NA);
    }

    private void setMBJsonValue(final WorkProgressRegister workProgressRegister, final JsonObject jsonObject) {
        if (workProgressRegister.getLatestMbNumber() != null && workProgressRegister.getLatestMbDate() != null)
            jsonObject.addProperty("latestMbNumberDate", workProgressRegister.getLatestMbNumber() + ", "
                    + DateUtils.getFormattedDate(workProgressRegister.getLatestMbDate(), DATEFORMAT));
        else
            jsonObject.addProperty("latestMbNumberDate", WorksConstants.NA);
    }

    private void setWorkJsonValues(final WorkProgressRegister workProgressRegister, final JsonObject jsonObject) {
        if (workProgressRegister.getWorkCategory().toString() != null)
            jsonObject.addProperty("workCategory", workProgressRegister.getWorkCategory().toString().replace("_", " "));
        else
            jsonObject.addProperty("workCategory", WorksConstants.NA);

        if (workProgressRegister.getWorkvalue() != null)
            jsonObject.addProperty("workValue", workProgressRegister.getWorkvalue());
        else
            jsonObject.addProperty("workValue", StringUtils.EMPTY);
        if (workProgressRegister.getWorkstatus() != null)
            jsonObject.addProperty("workStatus", workProgressRegister.getWorkstatus());
        else
            jsonObject.addProperty("workStatus", StringUtils.EMPTY);
    }

    private void setAgreementJsonValues(final WorkProgressRegister workProgressRegister, final JsonObject jsonObject) {
        if (workProgressRegister.getAgreementNumber() != null)
            jsonObject.addProperty("agreementNumberDate", workProgressRegister.getAgreementNumber() + " - "
                    + DateUtils.getFormattedDate(workProgressRegister.getAgreementDate(), DATEFORMAT));
        else
            jsonObject.addProperty("agreementNumberDate", WorksConstants.NA);

        if (workProgressRegister.getAgreementAmount() != null)
            jsonObject.addProperty("agreementAmount", workProgressRegister.getAgreementAmount());
        else
            jsonObject.addProperty("agreementAmount", StringUtils.EMPTY);
    }

    private void setBudgetJsonValues(final WorkProgressRegister workProgressRegister, final JsonObject jsonObject) {
        if (workProgressRegister.getFund() != null)
            jsonObject.addProperty("fund",
                    workProgressRegister.getFund().getCode() + " - " + workProgressRegister.getFund().getName());
        else
            jsonObject.addProperty("fund", StringUtils.EMPTY);
        if (workProgressRegister.getFunction() != null)
            jsonObject.addProperty("function", workProgressRegister.getFunction().getCode() + " - "
                    + workProgressRegister.getFunction().getName());
        else
            jsonObject.addProperty("function", StringUtils.EMPTY);
        if (workProgressRegister.getBudgetHead() != null)
            jsonObject.addProperty("budgetHead", workProgressRegister.getBudgetHead().getName());
        else
            jsonObject.addProperty("budgetHead", StringUtils.EMPTY);
    }

    private void setWardJsonValue(final WorkProgressRegister workProgressRegister, final JsonObject jsonObject) {
        if (workProgressRegister.getWard() != null) {
            if (workProgressRegister.getWard().getBoundaryType().getName()
                    .equalsIgnoreCase(WorksConstants.BOUNDARY_TYPE_CITY))
                jsonObject.addProperty("ward", workProgressRegister.getWard().getName());
            else
                jsonObject.addProperty("ward", workProgressRegister.getWard().getBoundaryNum());
        } else
            jsonObject.addProperty("ward", StringUtils.EMPTY);
    }

    private void setBillJsonValues(final WorkProgressRegister workProgressRegister, final JsonObject jsonObject) {
        if (workProgressRegister.getLatestBillNumber() != null)
            jsonObject.addProperty("latestBillNumberDate", workProgressRegister.getLatestBillNumber() + " - "
                    + DateUtils.getFormattedDate(workProgressRegister.getLatestBillDate(), DATEFORMAT));
        else
            jsonObject.addProperty("latestBillNumberDate", WorksConstants.NA);
        if (workProgressRegister.getBilltype() != null)
            jsonObject.addProperty("billType", workProgressRegister.getBilltype());
        else
            jsonObject.addProperty("billType", WorksConstants.NA);
        if (workProgressRegister.getBillamount() != null)
            jsonObject.addProperty("billAmount", workProgressRegister.getBillamount());
        else
            jsonObject.addProperty("billAmount", StringUtils.EMPTY);
        if (workProgressRegister.getTotalBillAmount() != null)
            jsonObject.addProperty("totalBillAmount", workProgressRegister.getTotalBillAmount());
        else
            jsonObject.addProperty("totalBillAmount", StringUtils.EMPTY);
        if (workProgressRegister.getTotalBillPaidSoFar() != null)
            jsonObject.addProperty("totalBillPaidSoFar", workProgressRegister.getTotalBillPaidSoFar());
        else
            jsonObject.addProperty("totalBillPaidSoFar", StringUtils.EMPTY);
        setBalanceValueOfWorkToBillJsonValue(workProgressRegister, jsonObject);
    }

    private void setBalanceValueOfWorkToBillJsonValue(final WorkProgressRegister workProgressRegister,
            final JsonObject jsonObject) {
        if (workProgressRegister.getBalanceValueOfWorkToBill() != null) {
            if (workProgressRegister.getBilltype() != null
                    && workProgressRegister.getBilltype().equalsIgnoreCase(BillTypes.Final_Bill.toString()))
                jsonObject.addProperty(BALANCEVALUEOFWORKTOBILL, WorksConstants.NA);
            else
                jsonObject.addProperty(BALANCEVALUEOFWORKTOBILL, workProgressRegister.getBalanceValueOfWorkToBill());
        } else
            jsonObject.addProperty(BALANCEVALUEOFWORKTOBILL, StringUtils.EMPTY);
    }

    private void setAdminSanctionJsonValues(final WorkProgressRegister workProgressRegister,
            final JsonObject jsonObject) {
        if (workProgressRegister.getAdminSanctionBy() != null)
            jsonObject.addProperty("adminSanctionAuthorityDate", workProgressRegister.getAdminSanctionBy() + " , "
                    + DateUtils.getFormattedDate(workProgressRegister.getAdminSanctionDate(), DATEFORMAT));
        else
            jsonObject.addProperty("adminSanctionAuthorityDate", StringUtils.EMPTY);
        if (workProgressRegister.getAdminSanctionAmount() != null)
            jsonObject.addProperty("adminSanctionAmount", workProgressRegister.getAdminSanctionAmount());
        else
            jsonObject.addProperty("adminSanctionAmount", StringUtils.EMPTY);
    }

    private void setTOWJsonValues(final WorkProgressRegister workProgressRegister, final JsonObject jsonObject) {
        if (workProgressRegister.getTypeOfWork() != null)
            jsonObject.addProperty("typeOfWork", workProgressRegister.getTypeOfWork().getName());
        else
            jsonObject.addProperty("typeOfWork", StringUtils.EMPTY);
        if (workProgressRegister.getSubTypeOfWork() != null)
            jsonObject.addProperty("subTypeOfWork", workProgressRegister.getSubTypeOfWork().getName());
        else
            jsonObject.addProperty("subTypeOfWork", StringUtils.EMPTY);
    }
}