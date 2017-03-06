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

package org.egov.pgr.web.controller.reports;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class AgeingReportHelperAdaptor implements JsonSerializer<AgeingReportResult> {

    @Override
    public JsonElement serialize(final AgeingReportResult ageingReportObject, final Type type,
                                 final JsonSerializationContext jsc) {
        final JsonObject jsonObject = new JsonObject();
        if (ageingReportObject != null) {

            jsonObject.addProperty("complainttype", null != ageingReportObject.getName() ? ageingReportObject.getName() : "Not Available");
            jsonObject.addProperty("grtthn30", null != ageingReportObject.getGrtthn30() ? ageingReportObject
                    .getGrtthn30().toString() : "0");
            jsonObject.addProperty("btw10to30", null != ageingReportObject.getBtw10to30() ? ageingReportObject
                    .getBtw10to30().toString() : "0");
            jsonObject.addProperty("btw5to10", null != ageingReportObject.getBtw5to10() ? ageingReportObject
                    .getBtw5to10().toString() : "0");
            jsonObject.addProperty("btw2to5", null != ageingReportObject.getBtw2to5() ? ageingReportObject
                    .getBtw2to5().toString() : "0");
            jsonObject.addProperty("lsthn2", null != ageingReportObject.getLsthn2() ? ageingReportObject.getLsthn2()
                    .toString() : "0");
            jsonObject.addProperty("total", null != ageingReportObject.getTotal() ? ageingReportObject.getTotal()
                    .toString() : "0");
        }
        return jsonObject;
    }

}
