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
import org.egov.mrs.domain.entity.ReIssue;

import java.lang.reflect.Type;

public class MarriageReIssueJsonAdaptor implements JsonSerializer<ReIssue> {
    @Override
    public JsonElement serialize(final ReIssue reIssue, final Type type,
            final JsonSerializationContext jsc) {
        JsonObject jsonObject = new JsonObject();
        if (reIssue != null) {
            if (reIssue.getApplicationNo() != null)
                jsonObject.addProperty("applicationNo", reIssue.getApplicationNo());
            else
                jsonObject.addProperty("applicationNo", org.apache.commons.lang.StringUtils.EMPTY);
            if (reIssue.getRegistration().getRegistrationNo() != null)
                jsonObject.addProperty("registrationNo", reIssue.getRegistration().getRegistrationNo());
            else
                jsonObject.addProperty("registrationNo", StringUtils.EMPTY);
            if (reIssue.getApplicationDate() != null)
                jsonObject.addProperty("registrationDate", reIssue.getApplicationDate().toString());
            else
                jsonObject.addProperty("registrationDate", StringUtils.EMPTY);

            if (reIssue.getRegistration().getDateOfMarriage() != null)
                jsonObject.addProperty("dateOfMarriage", reIssue.getRegistration().getDateOfMarriage().toString());
            else
                jsonObject.addProperty("dateOfMarriage", StringUtils.EMPTY);

            if (reIssue.getRegistration().getHusband().getFullName() != null)
                jsonObject.addProperty("husbandName", reIssue.getRegistration().getHusband().getFullName());
            else
                jsonObject.addProperty("husbandName", StringUtils.EMPTY);
            if (reIssue.getRegistration().getWife().getFullName() != null)
                jsonObject.addProperty("wifeName", reIssue.getRegistration().getWife().getFullName());
            else
                jsonObject.addProperty("wifeName", StringUtils.EMPTY);

            if (reIssue.getMarriageCertificate().isEmpty())
                jsonObject.addProperty("certificateIssued", "No");
            else if (reIssue.getMarriageCertificate().get(0).isCertificateIssued())
                jsonObject.addProperty("certificateIssued", "Yes");

            if (reIssue.getStatus() != null)
                jsonObject.addProperty("status", reIssue.getStatus().getDescription());
            else
                jsonObject.addProperty("status", StringUtils.EMPTY);
            if (reIssue.getFeePaid() != null)
                jsonObject.addProperty("feePaid", reIssue.getFeePaid());
            else
                jsonObject.addProperty("feePaid", StringUtils.EMPTY);

            if (!reIssue.isFeeCollected()) {
                jsonObject.addProperty("feeCollected", "No");
            } else
                jsonObject.addProperty("feeCollected", "Yes");
            if (reIssue.getZone() != null)
                jsonObject.addProperty("zone", reIssue.getZone().getName());
            else
                jsonObject.addProperty("zone", org.apache.commons.lang.StringUtils.EMPTY);
            if (reIssue.getStateType() != null)
                jsonObject.addProperty("remarks", reIssue.getRejectionReason());
            else
                jsonObject.addProperty("remarks", org.apache.commons.lang.StringUtils.EMPTY);
            if (reIssue.getMarriageRegistrationUnit() != null)
                jsonObject.addProperty("marriageRegistrationUnit", reIssue.getMarriageRegistrationUnit().getName());
            else
                jsonObject.addProperty("marriageRegistrationUnit", org.apache.commons.lang.StringUtils.EMPTY);
            jsonObject.addProperty("id", reIssue.getId());
        }
        return jsonObject;
    }
}