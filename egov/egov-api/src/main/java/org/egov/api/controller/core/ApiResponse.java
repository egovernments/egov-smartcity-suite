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