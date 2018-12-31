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

package org.egov.tl.web.response.adaptor;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.egov.infra.web.support.json.adapter.DataTableJsonAdapter;
import org.egov.infra.web.support.ui.DataTable;
import org.egov.tl.entity.contracts.LicenseSearchRequest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.egov.infra.utils.ApplicationConstant.NA;
import static org.egov.infra.utils.DateUtils.toDefaultDateFormat;
import static org.egov.infra.utils.StringUtils.defaultIfBlank;

public class LicenseSearchResponseAdaptor implements DataTableJsonAdapter<LicenseSearchRequest> {

    @Override
    public JsonElement serialize(DataTable<LicenseSearchRequest> licenseSearchRequest, Type type,
                                 JsonSerializationContext jsc) {
        List<LicenseSearchRequest> searchResults = licenseSearchRequest.getData();
        JsonArray licenseSearchResponse = new JsonArray();
        searchResults.forEach(searchResult -> {
            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("licenseId", searchResult.getLicenseId());
            responseJson.addProperty("uid", searchResult.getUid());
            responseJson.addProperty("applicationNumber", searchResult.getApplicationNumber());
            responseJson.addProperty("tlNumber", defaultIfBlank(searchResult.getLicenseNumber()));
            responseJson.addProperty("oldLicenseNumber", defaultIfBlank(searchResult.getOldLicenseNumber()));
            responseJson.addProperty("expiryDate", searchResult.getExpiryDate() == null ? NA :
                    toDefaultDateFormat(searchResult.getExpiryDate()));
            responseJson.addProperty("category", searchResult.getCategoryName());
            responseJson.addProperty("subCategory", searchResult.getSubCategoryName());
            responseJson.addProperty("tradeTitle", searchResult.getTradeTitle());
            responseJson.addProperty("tradeOwner", searchResult.getTradeOwnerName());
            responseJson.addProperty("mobileNumber", searchResult.getMobileNo());
            responseJson.addProperty("status", searchResult.getStatus());
            responseJson.addProperty("active", searchResult.getActive());
            responseJson.addProperty("ownerName", searchResult.getOwnerName());
            Gson gson = new Gson();
            ArrayList<JsonObject> list = new ArrayList<>();
            for (String action : searchResult.getActions()) {
                JsonObject objectInList = new JsonObject();
                objectInList.addProperty("key", action);
                list.add(objectInList);
            }
            responseJson.addProperty("actions", gson.toJson(list));
            licenseSearchResponse.add(responseJson);
        });
        return enhance(licenseSearchResponse, licenseSearchRequest);
    }
}