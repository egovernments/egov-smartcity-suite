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
import org.egov.infra.utils.DateUtils;
import org.egov.works.lineestimate.entity.LineEstimateDetails;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class LineEstimateForEstimatePhotographJsonAdaptor implements JsonSerializer<LineEstimateDetails> {
    @Override
    public JsonElement serialize(final LineEstimateDetails lineEstimateDetails, final Type type,
            final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (lineEstimateDetails != null) {
            if (lineEstimateDetails.getEstimateAmount() != null)
                jsonObject.addProperty("estimateAmount", lineEstimateDetails.getEstimateAmount());
            else
                jsonObject.addProperty("estimateAmount", "");
            if (lineEstimateDetails.getEstimateNumber() != null)
                jsonObject.addProperty("estimateNumber", lineEstimateDetails.getEstimateNumber());
            else
                jsonObject.addProperty("estimateNumber", "");
            if (lineEstimateDetails.getNameOfWork() != null)
                jsonObject.addProperty("nameOfWork", lineEstimateDetails.getNameOfWork());
            else
                jsonObject.addProperty("nameOfWork", "");
            if (lineEstimateDetails.getLineEstimate().getCreatedDate() != null)
                jsonObject.addProperty("estimateDate", DateUtils.getFormattedDate(lineEstimateDetails.getLineEstimate().getCreatedDate(),"dd/MM/yyyy"));
            else
                jsonObject.addProperty("estimateDate", "");
            if (lineEstimateDetails.getLineEstimate().getNatureOfWork() != null)
                jsonObject.addProperty("natureOfWork", lineEstimateDetails.getLineEstimate().getNatureOfWork().getName());
            else
                jsonObject.addProperty("natureOfWork", "");
            jsonObject.addProperty("lineEstimateDetailsId", lineEstimateDetails.getId());
        }
        return jsonObject;
    }
}