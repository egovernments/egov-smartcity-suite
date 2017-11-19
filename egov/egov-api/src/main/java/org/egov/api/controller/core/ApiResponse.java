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

package org.egov.api.controller.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.egov.api.adapter.DataAdapter;
import org.egov.infra.web.support.json.adapter.HibernateProxyTypeAdapter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiResponse {

    private String format = "json";

    private DataAdapter<?> dataAdapter = null;

    private JsonObject status = null;

    public ApiResponse() {
        status = new JsonObject();
    }

    public static ApiResponse newInstance() {
        return new ApiResponse();
    }

    public String getFormat() {
        return format;
    }

    public ApiResponse setFormat(String format) {
        this.format = format;
        return this;
    }

    public DataAdapter<?> getDataAdapter() {
        return dataAdapter;
    }

    public ApiResponse setDataAdapter(DataAdapter<?> dataAdapter) {
        this.dataAdapter = dataAdapter;
        return this;
    }

    public ApiResponse putStatusAttribute(String key, String value) {
        this.status.addProperty(key, value);
        return this;
    }

    public ResponseEntity<String> success(Object data) {
        return this._getResponse(data, "success", "", HttpStatus.OK);

    }

    public ResponseEntity<String> success(Object data, String message) {
        return this._getResponse(data, "success", message, HttpStatus.OK);
    }

    public ResponseEntity<String> error(String message) {
        return this._getResponse("", "error", message, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> error(String message, HttpStatus httpStatus) {
        return this._getResponse("", "error", message, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> _getResponse(Object data, String type, String message, HttpStatus httpStatus) {

        GsonBuilder gsonBuilder = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting();

        if (this.dataAdapter != null) {
            gsonBuilder.registerTypeAdapterFactory(
                    HibernateProxyTypeAdapter.FACTORY).registerTypeAdapter(
                    this.getDataAdapter().getBaseObject(),
                    this.getDataAdapter());
        }

        JsonElement je = null;
        Gson gson = gsonBuilder.create();
        status.addProperty("type", type);
        status.addProperty("message", message);

        JsonObject jo = new JsonObject();
        jo.add("status", status);

        if (this.dataAdapter != null) {
            je = gson.toJsonTree(data, this.dataAdapter.getTypeToken());
        } else {
            je = gson.toJsonTree(data);
        }
        jo.add("result", je);
        return new ResponseEntity<String>(gson.toJson(jo), httpStatus);
    }

}