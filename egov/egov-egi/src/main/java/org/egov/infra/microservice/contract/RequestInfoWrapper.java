package org.egov.infra.microservice.contract;

import org.egov.infra.microservice.models.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestInfoWrapper {

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(final RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

}