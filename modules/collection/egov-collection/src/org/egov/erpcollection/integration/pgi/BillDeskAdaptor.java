package org.egov.erpcollection.integration.pgi;


import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.erpcollection.models.ReceiptHeader;
import org.egov.erpcollection.web.constants.CollectionConstants;
import org.egov.infstr.models.ServiceDetails;
import org.egov.infstr.utils.EGovConfig;

import com.billdesk.pgidsk.PGIUtil;

/**
 * The PaymentRequestAdaptor class frames the request object for the payment service.
 * 
 */

public class BillDeskAdaptor implements PaymentGatewayAdaptor {
	
	private static final Logger LOGGER=Logger.getLogger(BillDeskAdaptor.class);
	
	
	/**
	 * This method invokes APIs to frame request object for the payment service passed as parameter   
	 * @param serviceDetails
	 * @param receiptHeader
	 * @return
	 */
	public PaymentRequest createPaymentRequest(ServiceDetails paymentServiceDetails,ReceiptHeader receiptHeader)
	{		
		DefaultPaymentRequest paymentRequest=new DefaultPaymentRequest();
		
		/*paymentRequest.setParameter(CollectionConstants.BILLDESK_TXT_CUSTOMERID,
				receiptHeader.getReferencenumber());
		paymentRequest.setParameter(CollectionConstants.BILLDESK_TXT_TXNAMOUNT,
				receiptHeader.getTotalAmount().setScale(CollectionConstants.AMOUNT_PRECISION_DEFAULT,BigDecimal.ROUND_UP));
		paymentRequest.setParameter(CollectionConstants.BILLDESK_TXT_ADDITIONALINFO1,
				receiptHeader.getId().toString());
		paymentRequest.setParameter(CollectionConstants.BILLDESK_TXT_ADDITIONALINFO2,
				receiptHeader.getTotalAmount().setScale(CollectionConstants.AMOUNT_PRECISION_DEFAULT,BigDecimal.ROUND_UP));
		paymentRequest.setParameter(CollectionConstants.BILLDESK_TXT_ADDITIONALINFO3,
					EGovConfig.getMessage(CollectionConstants.CUSTOMPROPERTIES_FILENAME, 
							CollectionConstants.MESSAGEKEY_BILLDESK_REV_HEAD_+receiptHeader.getService().getCode()));
		paymentRequest.setParameter(CollectionConstants.BILLDESK_TXT_ADDITIONALINFO4,
				paymentServiceDetails.getCode());
		paymentRequest.setParameter(CollectionConstants.BILLDESK_TXT_ADDITIONALINFO5,
				receiptHeader.getService().getCode());
		paymentRequest.setParameter(CollectionConstants.BILLDESK_RU,
				paymentServiceDetails.getCallBackurl()+"?serviceCode="+paymentServiceDetails.getCode());
		paymentRequest.setParameter(CollectionConstants.ONLINEPAYMENT_INVOKE_URL,
				paymentServiceDetails.getServiceUrl());*/
		
		
		StringBuffer paymentReqMsg = new StringBuffer(20);
		paymentReqMsg.append(CollectionConstants.ONLINE_PAYMENT_BILLDESK_MERCHANTID) // MerchantID 
					 .append(CollectionConstants.PIPE_SEPARATOR)
					 .append(receiptHeader.getReferencenumber()) // txtCustomerID
					 .append(CollectionConstants.PIPE_SEPARATOR)
					 .append(CollectionConstants.PAYMENT_REQUEST_MSG_NA)
					 .append(CollectionConstants.PIPE_SEPARATOR)
					 .append(receiptHeader.getTotalAmount().setScale(CollectionConstants.AMOUNT_PRECISION_DEFAULT,BigDecimal.ROUND_UP))// txtTxnAmount
					 .append(CollectionConstants.PIPE_SEPARATOR)
					 .append(CollectionConstants.PAYMENT_REQUEST_MSG_NA)
					 .append(CollectionConstants.PIPE_SEPARATOR)
					 .append(CollectionConstants.PAYMENT_REQUEST_MSG_NA)
					 .append(CollectionConstants.PIPE_SEPARATOR)
					 .append(CollectionConstants.PAYMENT_REQUEST_MSG_NA)
					 .append(CollectionConstants.PIPE_SEPARATOR)
					 .append("INR")
					 .append(CollectionConstants.PIPE_SEPARATOR)
					 .append(CollectionConstants.PAYMENT_REQUEST_MSG_NA)
					 .append(CollectionConstants.PIPE_SEPARATOR)
					 .append('R')
					 .append(CollectionConstants.PIPE_SEPARATOR)
					 .append(CollectionConstants.PAYMENT_REQUEST_SECURITY_ID)
					 .append(CollectionConstants.PIPE_SEPARATOR)
					 .append(CollectionConstants.PAYMENT_REQUEST_MSG_NA)
					 .append(CollectionConstants.PIPE_SEPARATOR)
					 .append(CollectionConstants.PAYMENT_REQUEST_MSG_NA)
					 .append(CollectionConstants.PIPE_SEPARATOR)
					 .append('F')
					 .append(CollectionConstants.PIPE_SEPARATOR)
					 .append(receiptHeader.getId()) // txtAdditionalInfo1
					 .append(CollectionConstants.PIPE_SEPARATOR)
					 .append(receiptHeader.getTotalAmount().setScale(CollectionConstants.AMOUNT_PRECISION_DEFAULT,BigDecimal.ROUND_UP))// txtAdditionalInfo2
					 .append(CollectionConstants.PIPE_SEPARATOR)
					 .append(EGovConfig.getMessage(CollectionConstants.CUSTOMPROPERTIES_FILENAME, 
								CollectionConstants.MESSAGEKEY_BILLDESK_REV_HEAD_+receiptHeader.getService().getCode())) // txtAdditionalInfo3
					 .append(CollectionConstants.PIPE_SEPARATOR)
					 .append(paymentServiceDetails.getCode()) //txtAdditionalInfo4
					 .append(CollectionConstants.PIPE_SEPARATOR)
					 .append(receiptHeader.getService().getCode()) // txtAdditionalInfo5
					 .append(CollectionConstants.PIPE_SEPARATOR)
					 .append(CollectionConstants.PAYMENT_REQUEST_MSG_NA)
					 .append(CollectionConstants.PIPE_SEPARATOR)
					 .append(CollectionConstants.PAYMENT_REQUEST_MSG_NA)
					 .append(CollectionConstants.PIPE_SEPARATOR)
					 .append(paymentServiceDetails.getCallBackurl()+"?serviceCode=").append(paymentServiceDetails.getCode()); //RU
					 
					 String checkSumValue = PGIUtil.doDigest(paymentReqMsg.toString(),CollectionConstants.UNIQUE_CHECKSUM_KEY);
					 paymentReqMsg.append(CollectionConstants.PIPE_SEPARATOR).append(checkSumValue);

					 
		paymentRequest.setParameter(CollectionConstants.PAYMENT_REQUEST_MESSAGE_KEY, paymentReqMsg.toString());
		paymentRequest.setParameter(CollectionConstants.ONLINEPAYMENT_INVOKE_URL, paymentServiceDetails.getServiceUrl());

		LOGGER.info("paymentRequest: "+paymentRequest.toString());
		return paymentRequest;
	}

	/**
	 * This method parses the given response string into a bill desk payment response 
	 * object.
	 * 
	 * @param a <code>String</code> representation of the response.
	 * 
	 * @return an instance of <code></code> containing the response information
	 */
	public PaymentResponse parsePaymentResponse(String response) {
			
			String[] messages = response.split("\\|", -1);
			
			String errorKeyPrefix = messages[20]+".pgi."+messages[19];
			if(!isValidChecksum(response.substring(0, response.lastIndexOf('|')), 
					messages[25]))
			{
				LOGGER.error("Error occured due to check sum mismatch");
				throw new EGOVRuntimeException(errorKeyPrefix+".checksum.mismatch");
			}
			
			PaymentResponse billDeskResponse = new DefaultPaymentResponse();
			
			billDeskResponse.setTxnReferenceNo(messages[2]);
			billDeskResponse.setTxnAmount(new BigDecimal(messages[4]));
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
			Date transactionDate = null;
			try {
				transactionDate = sdf.parse(messages[13]);
			} catch (ParseException e) {
				LOGGER.error("Error occured in parsing the transaction date [" + messages[13] + "]", e);
				
				throw new EGOVRuntimeException(errorKeyPrefix+".transactiondate.parse.error",e);
			}
			billDeskResponse.setTxnDate(transactionDate);
			
			billDeskResponse.setAuthStatus(messages[14]);
			billDeskResponse.setReceiptId(messages[16]);
			billDeskResponse.setChecksum(messages[25]);
			
			return billDeskResponse;
	}	
	
	public boolean isValidChecksum(String testString,String testChecksum){
		String actualChecksum= PGIUtil.doDigest(testString,CollectionConstants.UNIQUE_CHECKSUM_KEY);
		return actualChecksum.equals(testChecksum);
	}
}
