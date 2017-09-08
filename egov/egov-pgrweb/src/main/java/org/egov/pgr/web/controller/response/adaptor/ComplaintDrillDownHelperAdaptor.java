/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
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
 *           Further, all user interfaces, including but not limited to citizen facing interfaces,
 *           Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *           derived works should carry eGovernments Foundation logo on the top right corner.
 *
 * 	       For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 * 	       For any further queries on attribution, including queries on brand guidelines,
 *           please contact contact@egovernments.org
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

package org.egov.pgr.web.controller.response.adaptor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.egov.infra.utils.StringUtils;
import org.egov.infra.web.support.json.adapter.DataTableJsonAdapter;
import org.egov.infra.web.support.ui.DataTable;
import org.egov.pgr.entity.view.DrillDownReports;

import java.lang.reflect.Type;
import java.util.List;

public class ComplaintDrillDownHelperAdaptor implements DataTableJsonAdapter<DrillDownReports> {

    @Override
    public JsonElement serialize(DataTable<DrillDownReports> drillDownReportResponse, final Type type,
                                 final JsonSerializationContext jsc) {
        final List<DrillDownReports> drillDownReportResult = drillDownReportResponse.getData();
        final JsonArray reportData = new JsonArray();
        drillDownReportResult.forEach(reportObject -> {
            final JsonObject jsonObject = new JsonObject();
            if (reportObject.getEmployeeName() != null)
            jsonObject.addProperty("name", StringUtils.defaultIfBlank(reportObject.getEmployeeName(), "Not Available"));
            if (reportObject.getComplaintTypeName() != null)
            jsonObject.addProperty("name", StringUtils.defaultIfBlank(reportObject.getComplaintTypeName(), "Not Available"));
            jsonObject.addProperty("completed", StringUtils.defaultIfBlank(reportObject.getCompleted().toString(), "0"));
            jsonObject.addProperty("inprocess", StringUtils.defaultIfBlank(reportObject.getInprocess().toString(), "0"));
            jsonObject.addProperty("registered", StringUtils.defaultIfBlank(reportObject.getRegistered().toString(), "0"));
            jsonObject.addProperty("rejected", StringUtils.defaultIfBlank(reportObject.getRejected().toString(), "0"));
            jsonObject.addProperty("withinsla", StringUtils.defaultIfBlank(reportObject.getWithinSLA().toString(), "0"));
            jsonObject.addProperty("beyondsla", StringUtils.defaultIfBlank(reportObject.getBeyondSLA().toString(), "0"));
            jsonObject.addProperty("total", StringUtils.defaultIfBlank(reportObject.getTotal().toString(), "0"));
            jsonObject.addProperty("reopened", StringUtils.defaultIfBlank(reportObject.getReopened().toString(), "0"));
            if (reportObject.getEmployeeId() != null)
            jsonObject.addProperty("usrid", StringUtils.defaultIfBlank(reportObject.getEmployeeId().toString(), "0"));
            if (reportObject.getComplaintTypeId() != null)
            jsonObject.addProperty("complaintTyeId",StringUtils.defaultIfBlank(reportObject.getComplaintTypeId().toString(), "0"));
            reportData.add(jsonObject);
        });
        return enhance(reportData, drillDownReportResponse);
    }

}
