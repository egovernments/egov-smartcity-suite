package org.egov.erpcollection.integration.pgi;

import java.util.Map;

/**
 * The payment request interface. This is used by the system to invoke the
 * payment gateway URL by passing appropriate request parameters
 */
public interface PaymentRequest {
	/**
	 * @return Map of request parameters to be submitted to the payment gateway
	 *         URL. Key = parameter name, Value = parameter value
	 */
	public Map getRequestParameters();
}
