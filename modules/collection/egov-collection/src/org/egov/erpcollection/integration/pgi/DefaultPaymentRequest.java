package org.egov.erpcollection.integration.pgi;

import java.util.HashMap;
import java.util.Map;


public class DefaultPaymentRequest implements PaymentRequest {
	Map<String, Object> requestParameters = new HashMap<String, Object>(0);

	public DefaultPaymentRequest() {

	}

	public void setParameter(String paramName, Object paramValue) {
		requestParameters.put(paramName, paramValue);
	}

	public Map getRequestParameters() {
		return requestParameters;
	}
}
