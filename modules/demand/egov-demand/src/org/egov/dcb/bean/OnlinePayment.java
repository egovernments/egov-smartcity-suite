package org.egov.dcb.bean;

import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;

public class OnlinePayment extends Payment {

	public static final String TRANSACTION_NUMBER = "transactionNumber";
	
	private String transactionNumber;
	
	private static final Logger LOGGER = Logger.getLogger(OnlinePayment.class);
	
	public static OnlinePayment create(Map<String, String> paymentInfo) {
		return new OnlinePayment(paymentInfo);
	}
	
	private OnlinePayment(Map<String, String> paymentInfo) {
		validate(paymentInfo);
		LOGGER.debug("-Online Payment -paymentInfo " + paymentInfo);
		this.setTransactionNumber(paymentInfo.get(TRANSACTION_NUMBER));
		LOGGER.debug("--- Online Payment is created " + this);
	}
	
	
	public String toString() {
		return super.toString() + " transactionNumber " + getTransactionNumber();
	}

	public void validate(Map<String, String> paymentInfo) {
		if (paymentInfo != null && paymentInfo.isEmpty()) {
			throw new EGOVRuntimeException(" paymentInfo is null.Please check. ");
		}
	}
	
	public String getTransactionNumber() {
		return transactionNumber;
	}
	
	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}
}
