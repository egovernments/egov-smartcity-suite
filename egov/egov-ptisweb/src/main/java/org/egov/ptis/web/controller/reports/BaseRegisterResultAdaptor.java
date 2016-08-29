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
package org.egov.ptis.web.controller.reports;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.egov.ptis.domain.entity.property.BaseRegisterResult;

import java.lang.reflect.Type;
import java.math.BigDecimal;

public class BaseRegisterResultAdaptor implements JsonSerializer<BaseRegisterResult> {

    @Override
    public JsonElement serialize(BaseRegisterResult baseRegisterResultObj, Type type, JsonSerializationContext jsc) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("assessmentNo", baseRegisterResultObj.getAssessmentNo());
        jsonObject.addProperty("ownerName", baseRegisterResultObj.getOwnerName());
        jsonObject.addProperty("doorNo", baseRegisterResultObj.getDoorNO());
        jsonObject.addProperty("natureOfUsage", baseRegisterResultObj.getNatureOfUsage());
        if (baseRegisterResultObj.getIsExempted() == null)
            baseRegisterResultObj.setIsExempted("");
        jsonObject.addProperty("exemption", baseRegisterResultObj.getIsExempted());
        jsonObject.addProperty("courtCase", baseRegisterResultObj.getCourtCase());
        jsonObject.addProperty("arrearPeriod", baseRegisterResultObj.getArrearPeriod());
        jsonObject.addProperty("generalTax", (null != baseRegisterResultObj.getPropertyTax() ? baseRegisterResultObj
                .getPropertyTax().toString() : ""));
        jsonObject.addProperty("libraryCessTax",(null != baseRegisterResultObj.getLibraryCessTax() ? baseRegisterResultObj.getLibraryCessTax().toString() : ""));
        jsonObject.addProperty("eduCessTax", (null != baseRegisterResultObj.getEduCessTax() ? baseRegisterResultObj.getEduCessTax().toString() : ""));
        jsonObject.addProperty("penaltyFinesTax",(null != baseRegisterResultObj.getPenaltyFines() ? baseRegisterResultObj.getPenaltyFines().toString() : ""));
        jsonObject.addProperty("currTotal", (null != baseRegisterResultObj.getCurrTotal() ? baseRegisterResultObj.getCurrTotal().toString() : ""));
        jsonObject.addProperty("arrearPropertyTax",(null != baseRegisterResultObj.getArrearPropertyTax() ? baseRegisterResultObj.getArrearPropertyTax().toString() : ""));
        jsonObject.addProperty("arrearlibCess",(null != baseRegisterResultObj.getArrearLibraryTax() ? baseRegisterResultObj.getArrearLibraryTax().toString() : ""));
        jsonObject.addProperty("arrearEduCess", (null != baseRegisterResultObj.getArrearEduCess() ? baseRegisterResultObj.getArrearEduCess().toString() : ""));
        jsonObject.addProperty("arrearTotal", (null != baseRegisterResultObj.getArrearTotal() ? baseRegisterResultObj.getArrearTotal().toString() : ""));
        jsonObject.addProperty("arrearPenaltyFines",(null != baseRegisterResultObj.getArrearPenaltyFines() ? baseRegisterResultObj.getArrearPenaltyFines().toString() : ""));
        jsonObject.addProperty("arrearColl", (null != baseRegisterResultObj.getArrearColl() ? baseRegisterResultObj.getArrearColl().toString() : ""));
        jsonObject.addProperty("currentColl", (null != baseRegisterResultObj.getCurrentColl() ? baseRegisterResultObj.getCurrentColl().toString() : ""));
        jsonObject.addProperty("totalColl", (null != baseRegisterResultObj.getTotalColl() ? baseRegisterResultObj.getTotalColl().toString() : ""));
        jsonObject.addProperty("propertyUsage",(null != baseRegisterResultObj.getPropertyUsage()) ? baseRegisterResultObj.getPropertyUsage().toString() : "");
        jsonObject.addProperty("classification",(null != baseRegisterResultObj.getClassificationOfBuilding()) ? baseRegisterResultObj.getClassificationOfBuilding().toString() : "");
        jsonObject.addProperty("area", (null != baseRegisterResultObj.getPlinthArea()) ? baseRegisterResultObj
                .getPlinthArea().setScale(2, BigDecimal.ROUND_HALF_UP).toString() : "");
        return jsonObject;
    }

}
