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

import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.egov.mrs.application.MarriageConstants.N_A;

import java.lang.reflect.Type;

import org.egov.infra.utils.DateUtils;
import org.egov.mrs.domain.entity.MarriageRegistration;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class MarriageRegistrationJsonAdaptor implements JsonSerializer<MarriageRegistration> {

    @Override
    public JsonElement serialize(final MarriageRegistration registration, final Type type,
            final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (registration != null) {
            jsonObject.addProperty("registrationNo", defaultIfBlank(registration.getRegistrationNo(), N_A));
            jsonObject.addProperty("applicationNo", defaultIfBlank(registration.getApplicationNo(), N_A));
            jsonObject.addProperty("registrationDate",
                    defaultIfBlank(DateUtils.toDefaultDateFormat(registration.getApplicationDate()), N_A));
            jsonObject.addProperty("dateOfMarriage",
                    defaultIfBlank(DateUtils.toDefaultDateFormat(registration.getDateOfMarriage()), N_A));
            jsonObject.addProperty("remarks", defaultIfBlank(registration.getRejectionReason(), N_A));
            jsonObject.addProperty("applicationType", defaultIfBlank(registration.getStateType(), N_A));
            jsonObject.addProperty("husbandName", defaultIfBlank(registration.getHusband().getFullName(), N_A));
            jsonObject.addProperty("husbandreligion",
                    defaultIfBlank(registration.getHusband().getReligion().getName(), N_A));
            jsonObject.addProperty("husbandMaritalStatus",
                    defaultIfBlank(registration.getHusband().getMaritalStatus().toString(), N_A));
            jsonObject.addProperty("wifeName", defaultIfBlank(registration.getWife().getFullName(), N_A));
            jsonObject.addProperty("wifereligion", defaultIfBlank(registration.getWife().getReligion().getName(), N_A));
            jsonObject.addProperty("wifeMaritalStatus",
                    defaultIfBlank(registration.getWife().getMaritalStatus().toString(), N_A));
            jsonObject.addProperty("zone", defaultIfBlank(registration.getZone().getName(), N_A));
            jsonObject.addProperty("marriageRegistrationUnit",
                    defaultIfBlank(registration.getMarriageRegistrationUnit().getName(), N_A));
            if (registration.getMarriageCertificate().isEmpty())
                jsonObject.addProperty("certificateIssued", "No");
            else if (registration.getMarriageCertificate().get(0).isCertificateIssued())
                jsonObject.addProperty("certificateIssued", "Yes");
            if (registration.getStatus() != null)
                jsonObject.addProperty("status", registration.getStatus().getDescription());
            else
                jsonObject.addProperty("status", N_A);
            if (registration.getFeePaid() != null)
                jsonObject.addProperty("feePaid", registration.getFeePaid());
            else
                jsonObject.addProperty("feePaid", N_A);

            if (!registration.isFeeCollected()) {
                jsonObject.addProperty("feeCollected", "No");
                jsonObject.addProperty("feeCollectionPending", true);
            } else
                jsonObject.addProperty("feeCollected", "Yes");

            jsonObject.addProperty("id", registration.getId());
        }
        return jsonObject;
    }
}