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

import org.egov.infra.utils.DateUtils;
import org.egov.works.revisionestimate.entity.SearchRevisionEstimate;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class RevisionEstimateJsonAdaptor implements JsonSerializer<SearchRevisionEstimate> {

    @Override
    public JsonElement serialize(final SearchRevisionEstimate searchRevisionEstimate, final Type type,
            final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (searchRevisionEstimate != null) {
            if (searchRevisionEstimate.getId() != null)
                jsonObject.addProperty("id", searchRevisionEstimate.getId());
            else
                jsonObject.addProperty("id", org.apache.commons.lang.StringUtils.EMPTY);

            if (searchRevisionEstimate.getWoId() != null)
                jsonObject.addProperty("woId", searchRevisionEstimate.getWoId());
            else
                jsonObject.addProperty("woId", org.apache.commons.lang.StringUtils.EMPTY);

            if (searchRevisionEstimate.getAeId() != null)
                jsonObject.addProperty("aeId", searchRevisionEstimate.getAeId());
            else
                jsonObject.addProperty("aeId", org.apache.commons.lang.StringUtils.EMPTY);

            if (searchRevisionEstimate.getEstimateNumber() != null)
                jsonObject.addProperty("estimateNumber", searchRevisionEstimate.getEstimateNumber());
            else
                jsonObject.addProperty("estimateNumber", org.apache.commons.lang.StringUtils.EMPTY);

            if (searchRevisionEstimate.getRevisionEstimateNumber() != null)
                jsonObject.addProperty("revisionEstimateNumber", searchRevisionEstimate.getRevisionEstimateNumber());
            else
                jsonObject.addProperty("revisionEstimateNumber", org.apache.commons.lang.StringUtils.EMPTY);

            if (searchRevisionEstimate.getReDate() != null)
                jsonObject.addProperty("reDate", DateUtils.getDefaultFormattedDate(searchRevisionEstimate.getReDate()));
            else
                jsonObject.addProperty("reDate", org.apache.commons.lang.StringUtils.EMPTY);

            if (searchRevisionEstimate.getLoaNumber() != null)
                jsonObject.addProperty("loaNumber", searchRevisionEstimate.getLoaNumber());
            else
                jsonObject.addProperty("loaNumber", org.apache.commons.lang.StringUtils.EMPTY);

            if (searchRevisionEstimate.getContractorName() != null)
                jsonObject.addProperty("contractorName", searchRevisionEstimate.getContractorName());
            else
                jsonObject.addProperty("contractorName", org.apache.commons.lang.StringUtils.EMPTY);

            if (searchRevisionEstimate.getReValue() != null)
                jsonObject.addProperty("reValue", searchRevisionEstimate.getReValue());
            else
                jsonObject.addProperty("reValue", org.apache.commons.lang.StringUtils.EMPTY);

            if (searchRevisionEstimate.getRevisionEstimateStatus() != null)
                jsonObject.addProperty("status", searchRevisionEstimate.getRevisionEstimateStatus());
            else
                jsonObject.addProperty("status", org.apache.commons.lang.StringUtils.EMPTY);

            if (searchRevisionEstimate.getCurrentOwner() != null)
                jsonObject.addProperty("currentOwner", searchRevisionEstimate.getCurrentOwner());
            else
                jsonObject.addProperty("currentOwner", org.apache.commons.lang.StringUtils.EMPTY);

        }
        return jsonObject;
    }

}