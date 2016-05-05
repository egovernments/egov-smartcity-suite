
package org.egov.collection.integration.pgi;

import org.egov.collection.entity.CollectionObjectFactory;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * The PaymentRequestAdaptor class frames the request object for the payment service.
 * 
 */

public class DefaultPaymentResponseTest{
	
	CollectionObjectFactory objectFactory = new CollectionObjectFactory();
	
	@Test
	public void testPaymentResponse(){
		
		PaymentResponse paytResponse = objectFactory.createPaytResponse();
		
		assertNotNull(paytResponse.getMerchantId());
		assertNotNull(paytResponse.getMerchantId());
		assertNotNull(paytResponse.getCustomerId());
		assertNotNull(paytResponse.getTxnReferenceNo());
		assertNotNull(paytResponse.getBankReferenceNo());
		assertNotNull(paytResponse.getTxnAmount());
		assertNotNull(paytResponse.getBankId());
		assertNotNull(paytResponse.getBankMerchantId());
		assertNotNull(paytResponse.getTxnType());
		assertNotNull(paytResponse.getCurrencyName());
		assertNotNull(paytResponse.getItemCode());
		assertNotNull(paytResponse.getSecurityType());
		assertNotNull(paytResponse.getSecurityId());
		assertNotNull(paytResponse.getSecurityPassword());
		assertNotNull(paytResponse.getTxnDate());
		assertNotNull(paytResponse.getAuthStatus());
		assertNotNull(paytResponse.getSettlementType());
		assertNotNull(paytResponse.getReceiptId());
		assertNotNull(paytResponse.getAdditionalInfo2());
		assertNotNull(paytResponse.getAdditionalInfo3());
		/*assertNotNull(paytResponse.getAdditionalInfo4());
		assertNotNull(paytResponse.getAdditionalInfo5());*/
		assertNotNull(paytResponse.getPaytGatewayServiceCode());
		assertNotNull(paytResponse.getBillingServiceCode());
		assertNotNull(paytResponse.getAdditionalInfo6());
		assertNotNull(paytResponse.getAdditionalInfo7());
		assertNotNull(paytResponse.getErrorStatus());
		assertNotNull(paytResponse.getErrorDescription());
		assertNotNull(paytResponse.getChecksum());
	}
	
}
