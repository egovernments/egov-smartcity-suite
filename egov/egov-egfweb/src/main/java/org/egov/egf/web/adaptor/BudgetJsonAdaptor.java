package org.egov.egf.web.adaptor;

import java.lang.reflect.Type;
import org.egov.model.budget.Budget;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class BudgetJsonAdaptor implements JsonSerializer<Budget> {
    @Override
    public JsonElement serialize(final Budget budget, final Type type, final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (budget != null) {
            if (budget.getName() != null)
                jsonObject.addProperty("name", budget.getName());
            else
                jsonObject.addProperty("name", "");
            if (budget.getIsbere() != null)
                jsonObject.addProperty("isbere", budget.getIsbere());
            else
                jsonObject.addProperty("isbere", "");
            if (budget.getFinancialYear() != null)
                jsonObject.addProperty("financialYear", budget.getFinancialYear().getFinYearRange());
            else
                jsonObject.addProperty("financialYear", "");
            if (budget.getParent() != null){
                jsonObject.addProperty("parent", budget.getParent().getName());
                jsonObject.addProperty("parentId", budget.getParent().getId());
            }
            else{
                jsonObject.addProperty("parent", "");
                jsonObject.addProperty("parentId","");
            }
            if (budget.getReferenceBudget() != null){
                jsonObject.addProperty("reference", budget.getReferenceBudget().getName());
                jsonObject.addProperty("referenceId", budget.getReferenceBudget().getId());
            }
            else
            {
                jsonObject.addProperty("reference", "");
                jsonObject.addProperty("referenceId", "");
            }
            if (budget.getIsActiveBudget())
                jsonObject.addProperty("isActiveBudget", Boolean.toString(true).toUpperCase());
            else
                jsonObject.addProperty("isActiveBudget", Boolean.toString(false).toUpperCase());

            if (budget.getIsPrimaryBudget())
                jsonObject.addProperty("isPrimaryBudget", Boolean.toString(true).toUpperCase());
            else
                jsonObject.addProperty("isPrimaryBudget", Boolean.toString(false).toUpperCase());
            jsonObject.addProperty("id", budget.getId());
        }
        return jsonObject;
    }
}