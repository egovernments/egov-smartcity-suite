package org.egov.works.web.adaptor;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;

import org.egov.works.lineestimate.entity.enums.TypeOfSlum;
import org.egov.works.lineestimate.entity.enums.WorkCategory;
import org.egov.works.reports.entity.WorkProgressRegister;
import org.egov.works.utils.WorksUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

@Component
public class WorkProgressRegisterJsonAdaptor implements JsonSerializer<WorkProgressRegister> {
    @Autowired
    private WorksUtils worksUtils;

    @Override
    public JsonElement serialize(final WorkProgressRegister workProgressRegister, final Type type,
            final JsonSerializationContext jsc) {
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        final JsonObject jsonObject = new JsonObject();
        if (workProgressRegister != null) {
            if (workProgressRegister.getWard() != null)
                jsonObject.addProperty("ward", workProgressRegister.getWard().getName());
            else
                jsonObject.addProperty("ward", "");
            if (workProgressRegister.getLocation() != null)
                jsonObject.addProperty("location", workProgressRegister.getLocation().getName());
            else
                jsonObject.addProperty("location", "");
            if (workProgressRegister.getWorkCategory() != null
                    && workProgressRegister.getWorkCategory().toString().equals(WorkCategory.SLUM_WORK.toString())) {
                if (workProgressRegister.getTypeOfSlum() != null
                        && workProgressRegister.getTypeOfSlum().toString().equals(TypeOfSlum.NOTIFIED.toString()))
                    jsonObject.addProperty("typeOfSlum", "Notified Slum");
                else
                    jsonObject.addProperty("typeOfSlum", "Non Notified Slum");
            } else
                jsonObject.addProperty("typeOfSlum", "Non slum work");
            if (workProgressRegister.getBeneficiary() != null)
                jsonObject.addProperty("beneficiaries", workProgressRegister.getBeneficiary().toString());
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
                jsonObject.addProperty("typeOfWork", workProgressRegister.getTypeOfWork().getCode());
            else
                jsonObject.addProperty("typeOfWork", "");
            if (workProgressRegister.getSubTypeOfWork() != null)
                jsonObject.addProperty("subTypeOfWork", workProgressRegister.getSubTypeOfWork().getCode());
            else
                jsonObject.addProperty("subTypeOfWork", "");
            if (workProgressRegister.getAdminSanctionBy() != null)
                jsonObject.addProperty(
                        "adminSanctionAuthorityDate",
                        worksUtils.getUserDesignation(workProgressRegister.getAdminSanctionBy()) + " - "
                                + workProgressRegister.getAdminSanctionBy().getName() + ", "
                                + sdf.format(workProgressRegister.getAdminSanctionDate()));
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
                                + sdf.format(workProgressRegister.getTechnicalSanctionDate()));
            else
                jsonObject.addProperty("technicalSanctionAuthorityDate", "");
            if (workProgressRegister.getEstimateAmount() != null)
                jsonObject.addProperty("estimateAmount", workProgressRegister.getEstimateAmount());
            else
                jsonObject.addProperty("estimateAmount", "");
            if (workProgressRegister.getModeOfAllotment() != null)
                jsonObject.addProperty("modeOfAllotment", workProgressRegister.getModeOfAllotment().toString());
            else
                jsonObject.addProperty("modeOfAllotment", "");
            if (workProgressRegister.getAgreementNumber() != null)
                jsonObject.addProperty("agreementNumberDate",
                        workProgressRegister.getAgreementNumber() + " - " + sdf.format(workProgressRegister.getAgreementDate()));
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
                        workProgressRegister.getLatestMbNumber() + ", " + sdf.format(workProgressRegister.getLatestMbDate()));
            else
                jsonObject.addProperty("latestMbNumberDate", "NA");
            if (workProgressRegister.getLatestBillNumber() != null)
                jsonObject.addProperty("latestBillNumberDate",
                        workProgressRegister.getLatestBillNumber() + " - "
                                + sdf.format(workProgressRegister.getLatestBillDate()));
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
            if (workProgressRegister.getTotalBillPaidSoFar() != null)
                jsonObject.addProperty("totalBillPaidSoFar", workProgressRegister.getTotalBillPaidSoFar());
            else
                jsonObject.addProperty("totalBillPaidSoFar", "");
            if (workProgressRegister.getBalanceValueOfWorkToBill() != null)
                jsonObject.addProperty("balanceValueOfWorkToBill", workProgressRegister.getBalanceValueOfWorkToBill());
            else
                jsonObject.addProperty("balanceValueOfWorkToBill", "");

            jsonObject.addProperty("createdDate", formatter.format(workProgressRegister.getCreatedDate()));
        }
        return jsonObject;
    }
}