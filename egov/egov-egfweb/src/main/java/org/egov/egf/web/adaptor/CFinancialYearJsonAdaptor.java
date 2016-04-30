package org.egov.egf.web.adaptor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.egov.commons.CFinancialYear;

import java.lang.reflect.Type;

public class CFinancialYearJsonAdaptor implements JsonSerializer<CFinancialYear> {
    @Override
    public JsonElement serialize(final CFinancialYear cFinancialYear, final Type type,
            final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (cFinancialYear != null) {
            if (cFinancialYear.getFinYearRange() != null)
                jsonObject.addProperty("finYearRange", cFinancialYear.getFinYearRange());
            else
                jsonObject.addProperty("finYearRange", "");
            if (cFinancialYear.getStartingDate() != null)
                jsonObject.addProperty("startingDate", cFinancialYear.getStartingDate().toString());
            else
                jsonObject.addProperty("startingDate", "");
            if (cFinancialYear.getEndingDate() != null)
                jsonObject.addProperty("endingDate", cFinancialYear.getEndingDate().toString());
            else
                jsonObject.addProperty("endingDate", "");
            if (cFinancialYear.getIsActive() != null)
                jsonObject.addProperty("isActive", cFinancialYear.getIsActive());
            else
                jsonObject.addProperty("isActive", "");
            if (cFinancialYear.getIsActiveForPosting() != null)
                jsonObject.addProperty("isActiveForPosting", cFinancialYear.getIsActiveForPosting());
            else
                jsonObject.addProperty("isActiveForPosting", "");
            if (cFinancialYear.getIsClosed() != null)
                jsonObject.addProperty("isClosed", cFinancialYear.getIsClosed());
            else
                jsonObject.addProperty("isClosed", "");
            if (cFinancialYear.getTransferClosingBalance() != null)
                jsonObject.addProperty("transferClosingBalance", cFinancialYear.getTransferClosingBalance());
            else
                jsonObject.addProperty("transferClosingBalance", "");
            jsonObject.addProperty("id", cFinancialYear.getId());
        }
        return jsonObject;
    }
}