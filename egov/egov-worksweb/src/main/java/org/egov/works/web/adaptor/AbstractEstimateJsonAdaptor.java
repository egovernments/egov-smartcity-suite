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
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import org.egov.works.abstractestimate.entity.AbstractEstimate;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

@Component
public class AbstractEstimateJsonAdaptor implements JsonSerializer<AbstractEstimate> {

    final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @Autowired
    private WorksUtils worksUtils;

    @Override
    public JsonElement serialize(final AbstractEstimate abstractEstimate, final Type type,
            final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (abstractEstimate != null) {
            if (abstractEstimate.getId() != null)
                jsonObject.addProperty("id", abstractEstimate.getId());
            else
                jsonObject.addProperty("id", "");

            if (abstractEstimate.getLineEstimateDetails() != null)
                jsonObject.addProperty("leId", abstractEstimate.getLineEstimateDetails().getLineEstimate().getId());
            else
                jsonObject.addProperty("leId", "");

            if (abstractEstimate.getEstimateNumber() != null)
                jsonObject.addProperty("estimateNumber", abstractEstimate.getEstimateNumber());
            else
                jsonObject.addProperty("estimateNumber", "");

            if (abstractEstimate.getLineEstimateDetails() != null
                    && abstractEstimate.getLineEstimateDetails().getLineEstimate().getLineEstimateNumber() != null)
                jsonObject.addProperty("lineestimateNumber",
                        abstractEstimate.getLineEstimateDetails().getLineEstimate().getLineEstimateNumber());
            else
                jsonObject.addProperty("lineestimateNumber", "");

            if (abstractEstimate.getEstimateNumber() != null
                    && abstractEstimate.getEstimateDate() != null)
                jsonObject.addProperty("estimateNumberAndDate",
                        abstractEstimate.getEstimateNumber() + " - " + sdf.format(abstractEstimate.getEstimateDate()));
            else
                jsonObject.addProperty("estimateNumberAndDate", "");

            if (abstractEstimate.getProjectCode() != null)
                jsonObject.addProperty("workIdentificationNumber",
                        abstractEstimate.getProjectCode().getCode());
            else
                jsonObject.addProperty("workIdentificationNumber", "");

            if (abstractEstimate.getEstimateValue() != null)
                jsonObject.addProperty("estimateAmount", abstractEstimate.getEstimateValue().setScale(2,
                        BigDecimal.ROUND_HALF_EVEN).toString());
            else
                jsonObject.addProperty("estimateAmount", "");

            if (abstractEstimate.getExecutingDepartment() != null)
                jsonObject.addProperty("departmentName", abstractEstimate.getExecutingDepartment().getName());
            else
                jsonObject.addProperty("departmentName", "");

            if (abstractEstimate.getWard() != null) {
                if (abstractEstimate.getWard().getBoundaryType().getName()
                        .equalsIgnoreCase(WorksConstants.BOUNDARY_TYPE_CITY))
                    jsonObject.addProperty("ward", abstractEstimate.getWard().getName());
                else
                    jsonObject.addProperty("ward", abstractEstimate.getWard().getBoundaryNum());
            } else
                jsonObject.addProperty("ward", "");

            if (abstractEstimate.getEgwStatus() != null)
                jsonObject.addProperty("status", abstractEstimate.getEgwStatus().getDescription());
            else
                jsonObject.addProperty("status", "");

            if (abstractEstimate.getState() != null) {
                if (abstractEstimate.getEgwStatus() != null
                        && (abstractEstimate.getEgwStatus().getCode().equalsIgnoreCase(WorksConstants.APPROVED)
                                || abstractEstimate.getEgwStatus().getCode().equalsIgnoreCase(WorksConstants.CANCELLED_STATUS)))
                    jsonObject.addProperty("currentowner", "NA");
                else
                    jsonObject.addProperty("currentowner",
                            worksUtils.getApproverName(abstractEstimate.getState().getOwnerPosition().getId()));
            } else
                jsonObject.addProperty("currentowner", "NA");

            if (abstractEstimate.getName() != null)
                jsonObject.addProperty("nameofwork", abstractEstimate.getName());
            else
                jsonObject.addProperty("nameofwork", "");

        }
        return jsonObject;
    }
}