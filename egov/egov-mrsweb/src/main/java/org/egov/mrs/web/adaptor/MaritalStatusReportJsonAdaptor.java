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
import org.egov.mrs.domain.entity.MaritalStatusReport;

import java.lang.reflect.Type;

public class MaritalStatusReportJsonAdaptor implements JsonSerializer<MaritalStatusReport> {
    @Override
    public JsonElement serialize(final MaritalStatusReport marital, final Type type,
            final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (marital != null) {
            if (marital.getMonth() != null)
                jsonObject.addProperty("month", marital.getMonth());
            else
                jsonObject.addProperty("name", StringUtils.EMPTY);
            if (marital.getApplicantType() != null)
                jsonObject.addProperty("applicantType", marital.getApplicantType());
            else
                jsonObject.addProperty("applicantType", StringUtils.EMPTY);

            if (marital.getMarried() != null)
                jsonObject.addProperty("married", marital.getMarried());
            else
                jsonObject.addProperty("married", "0");

            if (marital.getUnmarried() != null)
                jsonObject.addProperty("unmarried", marital.getUnmarried());
            else
                jsonObject.addProperty("unmarried", "0");
            if (marital.getWidower() != null)
                jsonObject.addProperty("widower", marital.getWidower());
            else
                jsonObject.addProperty("widower", "0");
            if (marital.getDivorced() != null)
                jsonObject.addProperty("divorced", marital.getDivorced());
            else
                jsonObject.addProperty("divorced", "0");

            if (marital.getTotal() != null)
                jsonObject.addProperty("total", marital.getTotal());
            else
                jsonObject.addProperty("total", "0");
        }
        return jsonObject;
    }
}
