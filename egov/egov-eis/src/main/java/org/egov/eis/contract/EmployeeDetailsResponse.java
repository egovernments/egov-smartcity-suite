package org.egov.eis.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmployeeDetailsResponse {

    @JsonProperty("Employee")
    private EmployeeResponse employeeResponse;

    @JsonProperty("ResponseInfo")
    private ResponseInfo responseInfo;

    public EmployeeResponse getEmployeeResponse() {
        return employeeResponse;
    }

    public void setEmployeeResponse(final EmployeeResponse employeeResponse) {
        this.employeeResponse = employeeResponse;
    }

    public ResponseInfo getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(final ResponseInfo responseInfo) {
        this.responseInfo = responseInfo;
    }

}
