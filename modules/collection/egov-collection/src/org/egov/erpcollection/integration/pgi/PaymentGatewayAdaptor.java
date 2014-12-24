package org.egov.erpcollection.integration.pgi;

import org.egov.erpcollection.models.ReceiptHeader;
import org.egov.infstr.models.ServiceDetails;

/**
 * Adaptor interface for integrating with third party payment gateway systems.<br>
 * Any new payment gateway can be integrated with collections system by writing
 * a class that implements this interface, and then defining a bean in
 * application context for this class with id as
 * <code>[ServiceCode]PaymentGatewayAdaptor</code> <br>
 * where <code>[ServiceCode]</code> is the service code for that payment gateway
 * service. <br>
 * <br>
 * e.g. If <code>BDPGI</code> is the service code of Bill Desk Payment Gateway
 * service, then the corresponding adaptor bean should have the id as
 * <code>BDPGIPaymentGatewayAdaptor</code>
 */
public interface PaymentGatewayAdaptor {

	/**
	 * Creates payment request object that will be used by the system to invoke
	 * the payment gateway URL with appropriate request parameters.
	 * 
	 * @param service
	 *            The payment gateway service object
	 * @param receipt
	 *            The receipt for which payment is to be done
	 * @return The payment request object
	 * @see PaymentRequest
	 */
	public PaymentRequest createPaymentRequest(ServiceDetails service,
			ReceiptHeader receipt);

	/**
	 * Parses the payment response string received from the payment gateway and
	 * creates the payment response object
	 * 
	 * @param response
	 *            The response string received from payment gateway
	 * 
	 * @return The payment response object
	 * @see PaymentResponse
	 */
	public PaymentResponse parsePaymentResponse(String response);

}
