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

package org.egov.mrs.web.adaptor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.egov.infra.utils.StringUtils;
import org.egov.mrs.domain.entity.RegistrationReportsSearchResult;

import java.lang.reflect.Type;

public class MarriageRegistrationReportsJsonAdaptor implements JsonSerializer<RegistrationReportsSearchResult> {
    @Override
    public JsonElement serialize(final RegistrationReportsSearchResult reportSearchResult, final Type type,
            final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (reportSearchResult != null) {
            if (reportSearchResult.getRegistrationNo() != null)
                jsonObject.addProperty("registrationNo", reportSearchResult.getRegistrationNo());
            else
                jsonObject.addProperty("registrationNo", StringUtils.EMPTY);
            if (reportSearchResult.getApplicationNo() != null)
                jsonObject.addProperty("applicationNo", reportSearchResult.getApplicationNo());
            else
                jsonObject.addProperty("applicationNo", StringUtils.EMPTY);
            if (reportSearchResult.getRegistrationDate() != null)
                jsonObject.addProperty("registrationDate", reportSearchResult.getRegistrationDate());
            else
                jsonObject.addProperty("registrationDate", StringUtils.EMPTY);

            if (reportSearchResult.getDateOfMarriage() != null)
                jsonObject.addProperty("dateOfMarriage", reportSearchResult.getDateOfMarriage());
            else
                jsonObject.addProperty("dateOfMarriage", StringUtils.EMPTY);

            if (reportSearchResult.getHusbandName() != null)
                jsonObject.addProperty("husbandName", reportSearchResult.getHusbandName());
            else
                jsonObject.addProperty("husbandName", StringUtils.EMPTY);

            if (reportSearchResult.getWifeName() != null)
                jsonObject.addProperty("wifeName", reportSearchResult.getWifeName());
            else
                jsonObject.addProperty("wifeName", StringUtils.EMPTY);
            
            if (reportSearchResult.getStatus() != null)
                jsonObject.addProperty("status", reportSearchResult.getStatus());
            else
                jsonObject.addProperty("status", StringUtils.EMPTY);
            if (reportSearchResult.getFeePaid() != null)
                jsonObject.addProperty("feePaid", reportSearchResult.getFeePaid());
            else
                jsonObject.addProperty("feePaid", StringUtils.EMPTY);
           
            if (reportSearchResult.getZone() != null)
                jsonObject.addProperty("zone", reportSearchResult.getZone());
            else
                jsonObject.addProperty("zone", StringUtils.EMPTY);
            if (reportSearchResult.getUserName() != null)
                jsonObject.addProperty("userName", reportSearchResult.getUserName());
            else
                jsonObject.addProperty("userName", StringUtils.EMPTY);
            if (reportSearchResult.getPendingAction() != null)
                jsonObject.addProperty("pendingAction", reportSearchResult.getPendingAction());
            else
                jsonObject.addProperty("pendingAction", StringUtils.EMPTY);
            
            if (reportSearchResult.getRegistrationUnit() != null)
                    jsonObject.addProperty("marriageRegistrationUnit", reportSearchResult.getRegistrationUnit());
                else
                    jsonObject.addProperty("marriageRegistrationUnit", StringUtils.EMPTY);
            
            if (reportSearchResult.getApplicationType() != null)
                jsonObject.addProperty("applicationType", reportSearchResult.getApplicationType());
            else
                jsonObject.addProperty("applicationType", StringUtils.EMPTY);
            if (reportSearchResult.getPlaceOfMarriage() != null)
                jsonObject.addProperty("placeOfMarriage", reportSearchResult.getPlaceOfMarriage());
            else
                jsonObject.addProperty("placeOfMarriage", StringUtils.EMPTY);
            jsonObject.addProperty("id", reportSearchResult.getRegistrationId());
        }
        return jsonObject;
    }
}