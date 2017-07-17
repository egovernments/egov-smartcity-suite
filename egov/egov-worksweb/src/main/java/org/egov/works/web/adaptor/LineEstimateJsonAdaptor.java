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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.egov.works.lineestimate.entity.LineEstimate;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.egov.works.utils.WorksUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class LineEstimateJsonAdaptor implements JsonSerializer<LineEstimate> {
    @Autowired
    private WorksUtils worksUtils;

    @Override
    public JsonElement serialize(final LineEstimate lineEstimate, final Type type, final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (lineEstimate != null) {
            if (lineEstimate.getLineEstimateNumber() != null)
                jsonObject.addProperty("adminSanctionNumber", lineEstimate.getAdminSanctionNumber());
            else
                jsonObject.addProperty("adminSanctionNumber", "");
            if (lineEstimate.getLineEstimateNumber() != null)
                jsonObject.addProperty("lineEstimateNumber", lineEstimate.getLineEstimateNumber());
            else
                jsonObject.addProperty("lineEstimateNumber", "");
            if (lineEstimate.getFund() != null)
                jsonObject.addProperty("fund", lineEstimate.getFund().getName());
            else
                jsonObject.addProperty("fund", "");
            if (lineEstimate.getFunction() != null)
                jsonObject.addProperty("function", lineEstimate.getFunction().getName());
            else
                jsonObject.addProperty("function", "");
            if (lineEstimate.getBudgetHead() != null)
                jsonObject.addProperty("budgetHead", lineEstimate.getBudgetHead().getName());
            else
                jsonObject.addProperty("budgetHead", "");
            if (lineEstimate.getExecutingDepartment() != null)
                jsonObject.addProperty("executingDepartment", lineEstimate.getExecutingDepartment().getName());
            else
                jsonObject.addProperty("executingDepartment", "");
            if (lineEstimate.getCreatedBy() != null)
                jsonObject.addProperty("createdBy", lineEstimate.getCreatedBy().getName());
            else
                jsonObject.addProperty("createdBy", "");
            if (lineEstimate.getAdminSanctionBy() != null)
                jsonObject.addProperty("approvedBy", lineEstimate.getAdminSanctionBy());
            else
                jsonObject.addProperty("approvedBy", "");
            if (lineEstimate.getStatus() != null)
                jsonObject.addProperty("status", lineEstimate.getStatus().getDescription());
            else
                jsonObject.addProperty("status", "");
            if (lineEstimate.getState() != null && lineEstimate.getState().getOwnerPosition() != null)
                jsonObject.addProperty("owner", worksUtils.getApproverName(lineEstimate.getState().getOwnerPosition().getId()));
            else
                jsonObject.addProperty("owner", "");
            Long totalAmount = 0L;
            for (final LineEstimateDetails led : lineEstimate.getLineEstimateDetails())
                totalAmount += led.getEstimateAmount().longValue();
            jsonObject.addProperty("totalAmount", totalAmount);
            jsonObject.addProperty("id", lineEstimate.getId());
        }
        return jsonObject;
    }
}