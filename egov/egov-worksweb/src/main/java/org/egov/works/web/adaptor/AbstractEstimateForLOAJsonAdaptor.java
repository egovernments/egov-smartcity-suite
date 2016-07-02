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

import org.egov.works.abstractestimate.entity.AbstractEstimateForLoaSearchResult;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class AbstractEstimateForLOAJsonAdaptor implements JsonSerializer<AbstractEstimateForLoaSearchResult> {
    @Override
    public JsonElement serialize(final AbstractEstimateForLoaSearchResult abstractEstimateForLoaSearchResult, final Type type,
            final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (abstractEstimateForLoaSearchResult != null) {
            if (abstractEstimateForLoaSearchResult.getAdminSanctionNumber() != null)
                jsonObject.addProperty("adminSanctionNumber", abstractEstimateForLoaSearchResult.getAdminSanctionNumber());
            else
                jsonObject.addProperty("adminSanctionNumber", "");
            if (abstractEstimateForLoaSearchResult.getAdminSanctionBy() != null)
                jsonObject.addProperty("adminSanctionBy", abstractEstimateForLoaSearchResult.getAdminSanctionBy());
            else
                jsonObject.addProperty("adminSanctionBy", "");
            if (abstractEstimateForLoaSearchResult.getEstimateAmount() != null)
                jsonObject.addProperty("estimateAmount", abstractEstimateForLoaSearchResult.getEstimateAmount());
            else
                jsonObject.addProperty("estimateAmount", "");
            if (abstractEstimateForLoaSearchResult.getEstimateNumber() != null)
                jsonObject.addProperty("estimateNumber", abstractEstimateForLoaSearchResult.getEstimateNumber());
            else
                jsonObject.addProperty("estimateNumber", "");
            if (abstractEstimateForLoaSearchResult.getNameOfWork() != null)
                jsonObject.addProperty("nameOfWork", abstractEstimateForLoaSearchResult.getNameOfWork());
            else
                jsonObject.addProperty("nameOfWork", "");
            if (abstractEstimateForLoaSearchResult.getCreatedBy() != null)
                jsonObject.addProperty("createdBy", abstractEstimateForLoaSearchResult.getCreatedBy());
            else
                jsonObject.addProperty("createdBy", "");
            if (abstractEstimateForLoaSearchResult.getCurrentOwner() != null)
                jsonObject.addProperty("currentOwner", abstractEstimateForLoaSearchResult.getCurrentOwner());
            else
                jsonObject.addProperty("currentOwner", "");
            if (abstractEstimateForLoaSearchResult.getWorkIdentificationNumber() != null)
                jsonObject.addProperty("workIdentificationNumber",
                        abstractEstimateForLoaSearchResult.getWorkIdentificationNumber());
            else
                jsonObject.addProperty("workIdentificationNumber", "");
            jsonObject.addProperty("leId", abstractEstimateForLoaSearchResult.getLeId());
            jsonObject.addProperty("aeId", abstractEstimateForLoaSearchResult.getAeId());
        }
        return jsonObject;
    }
}