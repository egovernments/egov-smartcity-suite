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

package org.egov.works.web.adaptor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.egov.works.lineestimate.entity.LineEstimateForLoaSearchResult;

import java.lang.reflect.Type;

public class LineEstimateForLOAJsonAdaptor implements JsonSerializer<LineEstimateForLoaSearchResult> {
    @Override
    public JsonElement serialize(final LineEstimateForLoaSearchResult lineEstimateForLoaSearchResult, final Type type,
            final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (lineEstimateForLoaSearchResult != null) {
            if (lineEstimateForLoaSearchResult.getAdminSanctionNumber() != null)
                jsonObject.addProperty("adminSanctionNumber", lineEstimateForLoaSearchResult.getAdminSanctionNumber());
            else
                jsonObject.addProperty("adminSanctionNumber", "");
            if (lineEstimateForLoaSearchResult.getAdminSanctionBy() != null)
                jsonObject.addProperty("adminSanctionBy", lineEstimateForLoaSearchResult.getAdminSanctionBy());
            else
                jsonObject.addProperty("adminSanctionBy", "");
            if (lineEstimateForLoaSearchResult.getEstimateAmount() != null)
                jsonObject.addProperty("estimateAmount", lineEstimateForLoaSearchResult.getEstimateAmount());
            else
                jsonObject.addProperty("estimateAmount", "");
            if (lineEstimateForLoaSearchResult.getEstimateNumber() != null)
                jsonObject.addProperty("estimateNumber", lineEstimateForLoaSearchResult.getEstimateNumber());
            else
                jsonObject.addProperty("estimateNumber", "");
            if (lineEstimateForLoaSearchResult.getNameOfWork() != null)
                jsonObject.addProperty("nameOfWork", lineEstimateForLoaSearchResult.getNameOfWork());
            else
                jsonObject.addProperty("nameOfWork", "");
            if (lineEstimateForLoaSearchResult.getCreatedBy() != null)
                jsonObject.addProperty("createdBy", lineEstimateForLoaSearchResult.getCreatedBy());
            else
                jsonObject.addProperty("createdBy", "");
            if (lineEstimateForLoaSearchResult.getCurrentOwner() != null)
                jsonObject.addProperty("currentOwner", lineEstimateForLoaSearchResult.getCurrentOwner());
            else
                jsonObject.addProperty("currentOwner", "");
            if (lineEstimateForLoaSearchResult.getActualEstimateAmount() != null)
                jsonObject.addProperty("actualEstimateAmount", lineEstimateForLoaSearchResult.getActualEstimateAmount());
            else
                jsonObject.addProperty("actualEstimateAmount", "");
            if (lineEstimateForLoaSearchResult.getWorkIdentificationNumber() != null)
                jsonObject.addProperty("workIdentificationNumber", lineEstimateForLoaSearchResult.getWorkIdentificationNumber());
            else
                jsonObject.addProperty("workIdentificationNumber", "");
            jsonObject.addProperty("id", lineEstimateForLoaSearchResult.getId());
        }
        return jsonObject;
    }
}