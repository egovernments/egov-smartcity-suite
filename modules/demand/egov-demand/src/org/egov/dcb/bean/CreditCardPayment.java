package org.egov.dcb.bean;

import java.util.Map;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.erpcollection.integration.models.PaymentInfoCard.CARDTYPE;

public class CreditCardPayment extends Payment {
	public final static String CREDITCARDNO = "creditCardNo";
	public final static String CREDIRCARDEXPIRYMONTH = "creditCardStartMonth";
	public final static String CREDIRCARDEXPIRYYEAR = "crediCardtExpireYear";
	public final static String CVV = "cvv";
	public final static String CARDTYPE = "CardType";
	public final static String CARDTYPE_VISA = "V";
	public final static String CARDTYPE_MASTER = "M";
	public final static String TRANSACTIONNUMBER = "transactionNumber";

	private String creditCardNo;
	private String cvv;
	private String transactionNumber;	
	private String expMonth;
	private String expYear;
	public CARDTYPE cardType;

	static CreditCardPayment create(Map<String, String> paymentInfo) {
		return new CreditCardPayment(paymentInfo);
	}

	private CreditCardPayment(Map<String, String> paymentInfo) {
		validate(paymentInfo);
		this.setCreditCardNo(paymentInfo.get(CREDITCARDNO));
		this.setTransactionNumber(paymentInfo.get(TRANSACTIONNUMBER));
		setCardTypeIfAvailable(paymentInfo);
		setCVVIfAvailable(paymentInfo);
		setExpiryDateIfAvailable(paymentInfo);
	}

	public String toString() {
		return super.toString() + "creditCardNo " + 
		        getCreditCardNo().substring(getCreditCardNo().length() - 4, getCreditCardNo().length())
				+ " Expiry Month " + getExpMonth() + " Expiry Year "
				+ getExpYear();
	}

	public void validate(Map<String, String> paymentInfo) {
		if (paymentInfo == null || paymentInfo.isEmpty()) {
			throw new EGOVRuntimeException(" paymentInfo is null.Please check. ");
		}
	}

	private void setCardTypeIfAvailable(Map<String, String> paymentInfo) {
	    if (paymentInfo.get(CARDTYPE) != null) {
	        if (paymentInfo.get(CARDTYPE).equals(CARDTYPE_MASTER)) {
	            this.setCardType(cardType.M);
	        } else {
	            this.setCardType(cardType.V);
	        }
	    }
	}

	private void setExpiryDateIfAvailable(Map<String, String> paymentInfo) {
	    String expMonth = paymentInfo.get(CREDIRCARDEXPIRYMONTH);
	    String expYear = paymentInfo.get(CREDIRCARDEXPIRYYEAR);
	    if (expMonth != null && !expMonth.isEmpty()) {
	        this.setExpMonth(expMonth);
	    }
	    if (expYear != null && !expYear.isEmpty()) {
	        this.setExpYear(expYear);
	    }
	}

	private void setCVVIfAvailable(Map<String, String> paymentInfo) {
	    String cvv = paymentInfo.get(CVV);
	    if (cvv != null && !cvv.isEmpty()) {
	        this.setCvv(cvv);
	    }
	}


	public String getCreditCardNo() {
		return creditCardNo;
	}

	public void setCreditCardNo(String creditCardNo) {
		this.creditCardNo = creditCardNo;
	}

	public String getCvv() {
		return cvv;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	public String getExpMonth() {
		return expMonth;
	}

	public void setExpMonth(String expMonth) {
		this.expMonth = expMonth;
	}

	public String getExpYear() {
		return expYear;
	}

	public void setExpYear(String expYear) {
		this.expYear = expYear;
	}

	public CARDTYPE getCardType() {
		return cardType;
	}

	public void setCardType(CARDTYPE cardType) {
		this.cardType = cardType;
	}

}
