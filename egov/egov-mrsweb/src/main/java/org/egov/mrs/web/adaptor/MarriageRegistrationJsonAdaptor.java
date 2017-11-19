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
import org.egov.infra.utils.DateUtils;
import org.egov.mrs.domain.entity.MarriageRegistration;

import java.lang.reflect.Type;

public class MarriageRegistrationJsonAdaptor implements JsonSerializer<MarriageRegistration> {
    @Override
    public JsonElement serialize(final MarriageRegistration registration, final Type type,
            final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (registration != null) {
            if (registration.getRegistrationNo() != null)
                jsonObject.addProperty("registrationNo", registration.getRegistrationNo());
            else
                jsonObject.addProperty("registrationNo", org.apache.commons.lang.StringUtils.EMPTY);
            if (registration.getApplicationNo() != null)
                jsonObject.addProperty("applicationNo", registration.getApplicationNo());
            else
                jsonObject.addProperty("applicationNo", org.apache.commons.lang.StringUtils.EMPTY);
            if (registration.getApplicationDate() != null)
                jsonObject.addProperty("registrationDate", DateUtils.getDefaultFormattedDate(registration.getApplicationDate()));
            else
                jsonObject.addProperty("registrationDate", org.apache.commons.lang.StringUtils.EMPTY);

            if (registration.getDateOfMarriage() != null)
                jsonObject.addProperty("dateOfMarriage", DateUtils
                        .getDefaultFormattedDate(registration.getDateOfMarriage()));
            else
                jsonObject.addProperty("dateOfMarriage", org.apache.commons.lang.StringUtils.EMPTY);

            if (registration.getHusband().getFullName() != null)
                jsonObject.addProperty("husbandName", registration.getHusband().getFullName());
            else
                jsonObject.addProperty("husbandName", org.apache.commons.lang.StringUtils.EMPTY);

            if (registration.getHusband().getReligion() != null)
                jsonObject.addProperty("husbandreligion", registration.getHusband().getReligion().getName());
            else
                jsonObject.addProperty("husbandreligion", org.apache.commons.lang.StringUtils.EMPTY);
            if (registration.getHusband().getMaritalStatus() != null)
                jsonObject.addProperty("husbandMaritalStatus", registration.getHusband().getMaritalStatus().toString());
            else
                jsonObject.addProperty("husbandMaritalStatus", org.apache.commons.lang.StringUtils.EMPTY);
            if (registration.getWife().getFullName() != null)
                jsonObject.addProperty("wifeName", registration.getWife().getFullName());
            else
                jsonObject.addProperty("wifeName", org.apache.commons.lang.StringUtils.EMPTY);
            if (registration.getWife().getReligion() != null)
                jsonObject.addProperty("wifereligion", registration.getWife().getReligion().getName());
            else
                jsonObject.addProperty("wifereligion", org.apache.commons.lang.StringUtils.EMPTY);
            if (registration.getWife().getMaritalStatus() != null)
                jsonObject.addProperty("wifeMaritalStatus", registration.getWife().getMaritalStatus().toString());
            else
                jsonObject.addProperty("wifeMaritalStatus", org.apache.commons.lang.StringUtils.EMPTY);
            if (registration.getMarriageCertificate().isEmpty())
                jsonObject.addProperty("certificateIssued", "No");
            else if (registration.getMarriageCertificate().get(0).isCertificateIssued())
                jsonObject.addProperty("certificateIssued", "Yes");
            if (registration.getStatus() != null)
                jsonObject.addProperty("status", registration.getStatus().getDescription());
            else
                jsonObject.addProperty("status", org.apache.commons.lang.StringUtils.EMPTY);
            if (registration.getFeePaid() != null)
                jsonObject.addProperty("feePaid", registration.getFeePaid());
            else
                jsonObject.addProperty("feePaid", org.apache.commons.lang.StringUtils.EMPTY);

            if (!registration.isFeeCollected()) {
                jsonObject.addProperty("feeCollected", "No");
                jsonObject.addProperty("feeCollectionPending", true);
            } else
                jsonObject.addProperty("feeCollected", "Yes");

            if (registration.getRemarks() != null)
                jsonObject.addProperty("remarks", registration.getRejectionReason());
            else
                jsonObject.addProperty("remarks", org.apache.commons.lang.StringUtils.EMPTY);
            if (registration.getZone() != null)
                jsonObject.addProperty("zone", registration.getZone().getName());
            else
                jsonObject.addProperty("zone", org.apache.commons.lang.StringUtils.EMPTY);
            if (registration.getStateType() != null)
                jsonObject.addProperty("applicationType", registration.getStateType());
            else
                jsonObject.addProperty("applicationType", org.apache.commons.lang.StringUtils.EMPTY);
            if (registration.getMarriageRegistrationUnit() != null)
                jsonObject.addProperty("marriageRegistrationUnit", registration.getMarriageRegistrationUnit().getName());
            else
                jsonObject.addProperty("marriageRegistrationUnit", org.apache.commons.lang.StringUtils.EMPTY);
            jsonObject.addProperty("id", registration.getId());
        }
        return jsonObject;
    }
}