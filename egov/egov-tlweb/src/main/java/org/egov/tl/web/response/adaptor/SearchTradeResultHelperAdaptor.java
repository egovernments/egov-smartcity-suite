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
import org.egov.tl.entity.contracts.SearchForm;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.egov.infra.utils.StringUtils.defaultIfBlank;

public class SearchTradeResultHelperAdaptor implements DataTableJsonAdapter<SearchForm> {

    @Override
    public JsonElement serialize(DataTable<SearchForm> searchForm, Type type,
                                 JsonSerializationContext jsc) {
        List<SearchForm> searchFormResponse = searchForm.getData();
        JsonArray searchTradeResult = new JsonArray();
        searchFormResponse.forEach(searchResult -> {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("licenseId", searchResult.getLicenseId());
            jsonObject.addProperty("applicationNumber", searchResult.getApplicationNumber());
            jsonObject.addProperty("tlNumber", defaultIfBlank(searchResult.getLicenseNumber()));
            jsonObject.addProperty("category", searchResult.getCategoryName());
            jsonObject.addProperty("subCategory", searchResult.getSubCategoryName());
            jsonObject.addProperty("tradeTitle", searchResult.getTradeTitle());
            jsonObject.addProperty("tradeOwner", searchResult.getTradeOwnerName());
            jsonObject.addProperty("mobileNumber", searchResult.getMobileNo());
            jsonObject.addProperty("status", searchResult.getStatus());
            jsonObject.addProperty("active", searchResult.getActive());
            jsonObject.addProperty("ownerName", searchResult.getOwnerName());
            Gson gson = new Gson();
            ArrayList<JsonObject> list = new ArrayList<>();
            for (String action : searchResult.getActions()) {
                JsonObject objectInList = new JsonObject();
                objectInList.addProperty("key", action);
                list.add(objectInList);
            }
            jsonObject.addProperty("actions", gson.toJson(list));
            searchTradeResult.add(jsonObject);
        });
        return enhance(searchTradeResult, searchForm);
    }
}