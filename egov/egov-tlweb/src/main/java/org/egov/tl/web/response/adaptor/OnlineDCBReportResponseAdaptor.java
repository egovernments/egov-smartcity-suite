/*
 * eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) <2017>  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 * 	Further, all user interfaces, including but not limited to citizen facing interfaces,
 *         Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *         derived works should carry eGovernments Foundation logo on the top right corner.
 *
 * 	For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 * 	For any further queries on attribution, including queries on brand guidelines,
 *         please contact contact@egovernments.org
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.tl.web.response.adaptor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.egov.tl.entity.view.DCBReportResult;

import java.lang.reflect.Type;

import static org.egov.infra.utils.StringUtils.defaultIfBlank;

public class OnlineDCBReportResponseAdaptor implements JsonSerializer<DCBReportResult> {

    @Override
    public JsonElement serialize(final DCBReportResult dCBReportObj, final Type type, final JsonSerializationContext jsc) {
        final JsonObject dcbReportResponse = new JsonObject();
        if (dCBReportObj != null) {
            dcbReportResponse.addProperty("licenseid", dCBReportObj.getLicenseid());
            dcbReportResponse.addProperty("licensenumber", defaultIfBlank(dCBReportObj.getLicensenumber()));
            dcbReportResponse.addProperty("username", dCBReportObj.getUsername());
            dcbReportResponse.addProperty("curr_demand", dCBReportObj.getCurrentdemand());
            dcbReportResponse.addProperty("arr_demand", dCBReportObj.getArreardemand());
            dcbReportResponse.addProperty("total_demand", dCBReportObj.getTotaldemand());
            dcbReportResponse.addProperty("curr_coll", dCBReportObj.getCurrentcollection());
            dcbReportResponse.addProperty("arr_coll", dCBReportObj.getArrearcollection());
            dcbReportResponse.addProperty("total_coll", dCBReportObj.getTotalcollection());
            dcbReportResponse.addProperty("curr_balance", dCBReportObj.getCurrentbalance());
            dcbReportResponse.addProperty("arr_balance", dCBReportObj.getArrearbalance());
            dcbReportResponse.addProperty("total_balance", dCBReportObj.getTotalbalance());

        }
        return dcbReportResponse;
    }

}
