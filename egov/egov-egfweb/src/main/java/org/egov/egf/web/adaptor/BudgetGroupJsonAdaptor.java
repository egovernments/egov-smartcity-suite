package org.egov.egf.web.adaptor;

import java.lang.reflect.Type;
import org.egov.model.budget.BudgetGroup;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class BudgetGroupJsonAdaptor implements JsonSerializer<BudgetGroup> {
    @Override
    public JsonElement serialize(final BudgetGroup budgetGroup, final Type type, final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (budgetGroup != null) {
            if (budgetGroup.getName() != null)
                jsonObject.addProperty("name", budgetGroup.getName());
            else
                jsonObject.addProperty("name", "");
            if (budgetGroup.getMajorCode() != null)
                jsonObject.addProperty("majorCode", budgetGroup.getMajorCode().getName());
            else
                jsonObject.addProperty("majorCode", "");
            if (budgetGroup.getMaxCode() != null)
                jsonObject.addProperty("maxCode", budgetGroup.getMaxCode().getName());
            else
                jsonObject.addProperty("maxCode", "");
            if (budgetGroup.getMinCode() != null)
                jsonObject.addProperty("minCode", budgetGroup.getMinCode().getName());
            else
                jsonObject.addProperty("minCode", "");
            if (budgetGroup.getAccountType() != null)
                jsonObject.addProperty("accountType", budgetGroup.getAccountType().toString());
            else
                jsonObject.addProperty("accountType", "");
            if (budgetGroup.getBudgetingType() != null)
                jsonObject.addProperty("budgetingType", budgetGroup.getBudgetingType().toString());
            else
                jsonObject.addProperty("budgetingType", "");
                jsonObject.addProperty("isActive", Boolean.toString(budgetGroup.getIsActive()).toUpperCase());
            jsonObject.addProperty("id", budgetGroup.getId());
        }
        return jsonObject;
    }
}