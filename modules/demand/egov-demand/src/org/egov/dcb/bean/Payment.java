package org.egov.dcb.bean;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;

public abstract class Payment {
	public final static String CHEQUE = "cheque";
	public final static String CREDITCARD = "creditcard";
	public final static String ATMCARD = "atmcard";
	public final static String ONLINE = "online";
	public final static String PAIDBY = "paidBy";
	public final static String AMOUNT = "amount";
	private BigDecimal amount;
	private String paidBy;
	private static final Logger LOGGER = Logger.getLogger(Payment.class);

	public static Payment create(String type, Map<String, String> paymentInfo) {
		LOGGER.debug("-Creating payment ---" + type + " paymentInfo " + paymentInfo);
		validate(type, paymentInfo);
		Payment payment = null;
		if (type.equals(CHEQUE)) {
			payment = ChequePayment.create(paymentInfo);
		} else if (type.equals(CREDITCARD)) {
			payment = CreditCardPayment.create(paymentInfo);
		} else if (type.equals(ATMCARD)) {
			payment = AtmPayment.create(paymentInfo);
		} else if (type.equals(ONLINE)) {
			payment = OnlinePayment.create(paymentInfo);
		}
		
		payment.setAmount(new BigDecimal(paymentInfo.get(AMOUNT)));
		payment.setPaidBy(paymentInfo.get(PAIDBY));
		LOGGER.debug("Created payment=" + payment);
		return payment;
	} 

	public String toString() {
		return "amount=" + getAmount() + " paidBy=" + getPaidBy();
	}

	private static void validate(String type, Map<String, String> paymentInfo) {
		if (type == null || type.isEmpty() || paymentInfo == null || paymentInfo.isEmpty()) { 
			throw new EGOVRuntimeException(
				" Either the type or PaymentInfo Map is null .Please Check " + type + " "
						+ paymentInfo); }

	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getPaidBy() {
		return paidBy;
	}

	public void setPaidBy(String paidBy) {
		this.paidBy = paidBy;
	}

}
