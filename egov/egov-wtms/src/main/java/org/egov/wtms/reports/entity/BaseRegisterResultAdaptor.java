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
package org.egov.wtms.reports.entity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.egov.wtms.application.entity.BaseRegisterResult;

import java.lang.reflect.Type;

public class BaseRegisterResultAdaptor implements JsonSerializer<BaseRegisterResult> {

    @Override
    public JsonElement serialize(final BaseRegisterResult baseRegisterResultObj, final Type type,
            final JsonSerializationContext jsc) {

        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("consumerNo",
                null != baseRegisterResultObj.getConsumerNo() ? baseRegisterResultObj.getConsumerNo() : "");
        jsonObject.addProperty("oldConsumerNo",
                baseRegisterResultObj.getOldConsumerNo() != null ? baseRegisterResultObj.getOldConsumerNo() : "");
        jsonObject.addProperty("assessmentNo",
                null != baseRegisterResultObj.getAssementNo() ? baseRegisterResultObj.getAssementNo() : "");
        jsonObject.addProperty("ownerName",
                null != baseRegisterResultObj.getOwnerName() ? baseRegisterResultObj.getOwnerName() : "");
        jsonObject.addProperty("doorNo",
                null != baseRegisterResultObj.getDoorNo() ? baseRegisterResultObj.getDoorNo().toString() : "");
        jsonObject.addProperty("categoryType",
                null != baseRegisterResultObj.getCategoryType() ? baseRegisterResultObj.getCategoryType() : "");
        jsonObject.addProperty("waterSource", baseRegisterResultObj.getWaterSource());
        jsonObject.addProperty("usageType",
                null != baseRegisterResultObj.getUsageType() ? baseRegisterResultObj.getUsageType() : "");
        jsonObject.addProperty("pipeSize",
                null != baseRegisterResultObj.getPipeSize() ? baseRegisterResultObj.getPipeSize() : "");
        jsonObject.addProperty("period",
                null != baseRegisterResultObj.getPeriod() && baseRegisterResultObj.getArrears()!=0  ? baseRegisterResultObj.getPeriod() : "");
        jsonObject.addProperty("arrears", baseRegisterResultObj.getArrears());
        jsonObject.addProperty("current", baseRegisterResultObj.getCurrent());
        jsonObject.addProperty("totalDemand", baseRegisterResultObj.getTotalDemand());
        jsonObject.addProperty("arrearsCollection", baseRegisterResultObj.getArrearsCollection());
        jsonObject.addProperty("currentCollection", baseRegisterResultObj.getCurrentCollection());
        jsonObject.addProperty("totalCollection", baseRegisterResultObj.getTotalCollection());
        jsonObject.addProperty("monthlyRate", baseRegisterResultObj.getMonthlyRate());
        return jsonObject;
    }

}
