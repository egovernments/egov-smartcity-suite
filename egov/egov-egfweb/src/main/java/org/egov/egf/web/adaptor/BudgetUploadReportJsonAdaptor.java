package org.egov.egf.web.adaptor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.egov.model.budget.BudgetUploadReport;

import java.lang.reflect.Type;
import java.math.BigDecimal;

public class BudgetUploadReportJsonAdaptor implements JsonSerializer<BudgetUploadReport>
{
    @Override
    public JsonElement serialize(final BudgetUploadReport budgetUploadReport, final Type type, final JsonSerializationContext jsc)
    { 
        final JsonObject jsonObject = new JsonObject();
        if (budgetUploadReport != null)
        { 
            jsonObject.addProperty("fundCode", budgetUploadReport.getFundCode()!=null?budgetUploadReport.getFundCode():"");
            jsonObject.addProperty("deptCode", budgetUploadReport.getDeptCode()!=null?budgetUploadReport.getDeptCode():"");
            jsonObject.addProperty("functionCode", budgetUploadReport.getFunctionCode()!=null?budgetUploadReport.getFunctionCode():"");
            jsonObject.addProperty("glCode", budgetUploadReport.getGlCode()!=null?budgetUploadReport.getGlCode():"");
            jsonObject.addProperty("approvedReAmount", budgetUploadReport.getApprovedReAmount()!=null?budgetUploadReport.getApprovedReAmount():BigDecimal.ZERO);
            jsonObject.addProperty("planningReAmount", budgetUploadReport.getPlanningReAmount()!=null?budgetUploadReport.getPlanningReAmount():BigDecimal.ZERO);
            jsonObject.addProperty("approvedBeAmount", budgetUploadReport.getApprovedBeAmount()!=null?budgetUploadReport.getApprovedBeAmount():BigDecimal.ZERO);
            jsonObject.addProperty("planningBeAmount", budgetUploadReport.getPlanningBeAmount()!=null?budgetUploadReport.getPlanningBeAmount():BigDecimal.ZERO);
        }
        return jsonObject;
    }
}