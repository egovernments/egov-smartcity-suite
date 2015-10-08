package org.egov.ptis.web.controller.reports;

import java.lang.reflect.Type;

import org.egov.ptis.domain.entity.property.BaseRegisterResult;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class BaseRegisterResultAdaptor implements JsonSerializer<BaseRegisterResult> {
    
    @Override
    public JsonElement serialize(BaseRegisterResult baseRegisterResultObj, Type type, JsonSerializationContext jsc) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("assessmentNo", baseRegisterResultObj.getAssessmentNo());
        jsonObject.addProperty("ownerName", baseRegisterResultObj.getOwnerName());
        jsonObject.addProperty("doorNo", baseRegisterResultObj.getDoorNO());
        jsonObject.addProperty("natureOfUsage", baseRegisterResultObj.getNatureOfUsage());
        jsonObject.addProperty("exemption", baseRegisterResultObj.getIsExempted() ? "Yes": "No");
        jsonObject.addProperty("courtCase", baseRegisterResultObj.getCourtCase() ? "Yes": "No");
        jsonObject.addProperty("arrearPeriod", baseRegisterResultObj.getArrearPeriod());
        jsonObject.addProperty("generalTax", baseRegisterResultObj.getGeneralTax());
        jsonObject.addProperty("libraryCessTax", baseRegisterResultObj.getLibraryCessTax());
        jsonObject.addProperty("eduCessTax", baseRegisterResultObj.getEduCessTax());
        jsonObject.addProperty("penaltyFinesTax", baseRegisterResultObj.getPenaltyFines());
        jsonObject.addProperty("currTotal", baseRegisterResultObj.getCurrTotal());
        jsonObject.addProperty("arrearPropertyTax", baseRegisterResultObj.getGeneralTax());
        jsonObject.addProperty("arrearlibCess", baseRegisterResultObj.getLibraryCessTax());
        jsonObject.addProperty("arrearEduCess", baseRegisterResultObj.getEduCessTax());
        jsonObject.addProperty("arrearTotal", baseRegisterResultObj.getArrearTotal());
        jsonObject.addProperty("arrearPenaltyFines", baseRegisterResultObj.getArrearPenaltyFines());
        return jsonObject;
    }

}
