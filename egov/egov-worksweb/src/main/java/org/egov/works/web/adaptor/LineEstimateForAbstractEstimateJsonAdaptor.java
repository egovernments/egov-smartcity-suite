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

import java.lang.reflect.Type;

import org.egov.works.lineestimate.entity.LineEstimatesForAbstractEstimate;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class LineEstimateForAbstractEstimateJsonAdaptor implements JsonSerializer<LineEstimatesForAbstractEstimate> {
    @Override
    public JsonElement serialize(final LineEstimatesForAbstractEstimate lineEstimatesForAbstractEstimate, final Type type,
            final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (lineEstimatesForAbstractEstimate != null) {
            if (lineEstimatesForAbstractEstimate.getAdminSanctionNumber() != null)
                jsonObject.addProperty("adminSanctionNumber", lineEstimatesForAbstractEstimate.getAdminSanctionNumber());
            else
                jsonObject.addProperty("adminSanctionNumber", "");
            if (lineEstimatesForAbstractEstimate.getAdminSanctionBy() != null)
                jsonObject.addProperty("adminSanctionBy", lineEstimatesForAbstractEstimate.getAdminSanctionBy());
            else
                jsonObject.addProperty("adminSanctionBy", "");
            if (lineEstimatesForAbstractEstimate.getEstimateAmount() != null)
                jsonObject.addProperty("estimateAmount", lineEstimatesForAbstractEstimate.getEstimateAmount());
            else
                jsonObject.addProperty("estimateAmount", "");
            if (lineEstimatesForAbstractEstimate.getEstimateNumber() != null)
                jsonObject.addProperty("estimateNumber", lineEstimatesForAbstractEstimate.getEstimateNumber());
            else
                jsonObject.addProperty("estimateNumber", "");
            if (lineEstimatesForAbstractEstimate.getNameOfWork() != null)
                jsonObject.addProperty("nameOfWork", lineEstimatesForAbstractEstimate.getNameOfWork());
            else
                jsonObject.addProperty("nameOfWork", "");
            if (lineEstimatesForAbstractEstimate.getCreatedBy() != null)
                jsonObject.addProperty("createdBy", lineEstimatesForAbstractEstimate.getCreatedBy());
            else
                jsonObject.addProperty("createdBy", "");
            if (lineEstimatesForAbstractEstimate.getCurrentOwner() != null)
                jsonObject.addProperty("currentOwner", lineEstimatesForAbstractEstimate.getCurrentOwner());
            else
                jsonObject.addProperty("currentOwner", "");
            if (lineEstimatesForAbstractEstimate.getActualEstimateAmount() != null)
                jsonObject.addProperty("actualEstimateAmount", lineEstimatesForAbstractEstimate.getActualEstimateAmount());
            else
                jsonObject.addProperty("actualEstimateAmount", "");
            if (lineEstimatesForAbstractEstimate.getWorkIdentificationNumber() != null)
                jsonObject
                        .addProperty("workIdentificationNumber", lineEstimatesForAbstractEstimate.getWorkIdentificationNumber());
            else
                jsonObject.addProperty("workIdentificationNumber", "");
            jsonObject.addProperty("id", lineEstimatesForAbstractEstimate.getId());
        }
        return jsonObject;
    }
}