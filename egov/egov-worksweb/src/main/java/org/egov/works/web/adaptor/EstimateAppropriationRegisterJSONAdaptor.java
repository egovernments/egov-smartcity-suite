package org.egov.works.web.adaptor;

import java.lang.reflect.Type;

import org.egov.works.models.estimate.BudgetFolioDetail;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

@Component
public class EstimateAppropriationRegisterJSONAdaptor implements JsonSerializer<BudgetFolioDetail>{

    @Override
    public JsonElement serialize(final BudgetFolioDetail budgetFolioDetail, final Type type,
            final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (budgetFolioDetail != null) {
            if (budgetFolioDetail.getBudgetApprNo() != null)
                jsonObject.addProperty("appropriationNumber", budgetFolioDetail.getBudgetApprNo());
            else
                jsonObject.addProperty("appropriationNumber", "");
            if (budgetFolioDetail.getAppDate() != null)
                jsonObject.addProperty("appropriationDate", budgetFolioDetail.getAppDate());
            else
                jsonObject.addProperty("appropriationDate", "");
            if (budgetFolioDetail.getAppropriatedValue() != null)
                jsonObject.addProperty("appropriationValue", budgetFolioDetail.getAppropriatedValue());
            else
                jsonObject.addProperty("appropriationValue", "");
            if (budgetFolioDetail.getEstimateNo() != null)
                jsonObject.addProperty("estimateNumber", budgetFolioDetail.getEstimateNo());
            else
                jsonObject.addProperty("estimateNumber", "");
            if (budgetFolioDetail.getWorkIdentificationNumber() != null)
                jsonObject.addProperty("workIdentificationNumber", budgetFolioDetail.getWorkIdentificationNumber());
            else
                jsonObject.addProperty("workIdentificationNumber", "");
            if (budgetFolioDetail.getNameOfWork() != null)
                jsonObject.addProperty("nameOfWork", budgetFolioDetail.getNameOfWork());
            else
                jsonObject.addProperty("nameOfWork", "");
            if (budgetFolioDetail.getEstimateDate() != null)
                jsonObject.addProperty("estimateDate", budgetFolioDetail.getEstimateDate());
            else
                jsonObject.addProperty("estimateDate", "");
            if (budgetFolioDetail.getWorkValue() != null)
                jsonObject.addProperty("estimateValue", budgetFolioDetail.getWorkValue());
            else
                jsonObject.addProperty("estimateValue", "");
            if(budgetFolioDetail.getCumulativeExpensesIncurred() != null)
                jsonObject.addProperty("cumulativeTotal", budgetFolioDetail.getCumulativeTotal());
            else
                jsonObject.addProperty("cumulativeTotal", "");
            if(budgetFolioDetail.getCumulativeExpensesIncurred() != null)
                jsonObject.addProperty("balanceAvailable", budgetFolioDetail.getBalanceAvailable());
            else
                jsonObject.addProperty("balanceAvailable", "");
            if(budgetFolioDetail.getCumulativeExpensesIncurred() != null)
                jsonObject.addProperty("actualBalanceAvailable", budgetFolioDetail.getActualBalanceAvailable());
            else
                jsonObject.addProperty("actualBalanceAvailable", "");
            
            jsonObject.addProperty("id", budgetFolioDetail.getSrlNo());
        }
        return jsonObject;
    }

}
