package org.egov.dcb.bean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;

public class ChequePayment extends Payment {

	private String instrumentNumber;
	private Date instrumentDate;
	private String bankName;
	private String branchName;
	private Long bankId;
	public final static String INSTRUMENTNUMBER = "instrumentNumber";
	public final static String INSTRUMENTDATE = "instrumentDate";
	public final static String BANKNAME = "bankName";
	public final static String BRANCHNAME = "branchName";
	public final static String BANKID = "bankId";
	private static final Logger LOGGER = Logger.getLogger(ChequePayment.class);
	private final SimpleDateFormat CHEQUE_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

	public static ChequePayment create(Map<String, String> paymentInfo) {
		return new ChequePayment(paymentInfo);
	}

	private ChequePayment(Map<String, String> paymentInfo) {
		validate(paymentInfo);
		LOGGER.debug("-Cheque Payment -paymentInfo " + paymentInfo);
		this.setInstrumentNumber((String) paymentInfo.get(INSTRUMENTNUMBER));
		try {
			this.setInstrumentDate(CHEQUE_DATE_FORMAT.parse(paymentInfo.get(INSTRUMENTDATE)));
		} catch (ParseException e) {
			throw new EGOVRuntimeException("InstrumentDate-Date Format should be dd-MM-yyyy", e);
		}
		this.setBankName(paymentInfo.get(BANKNAME));
		setBankIDFromName();
		
//		if (paymentInfo.get(BANKID) != null && !paymentInfo.get(BANKID).isEmpty()) {
//			this.setBankId(Long.valueOf(paymentInfo.get(BANKID)));
//		}
		
		if (paymentInfo.get(BRANCHNAME) != null) {
			this.setBranchName(paymentInfo.get(BRANCHNAME));
		}
		LOGGER.debug("-Cheque Payment is created " + this);
	}

	private void setBankIDFromName() {
	    this.bankId = Long.valueOf(323);  //TODO
	}
	
	public String toString() {
		return super.toString() + "instrumentNumber " + getInstrumentNumber() + " instrumentDate "
				+ getInstrumentDate() + " bankName " + getBankName() + " branchName "
				+ getBranchName();
	}

	public void validate(Map<String, String> paymentInfo) {
		if (paymentInfo != null && paymentInfo.isEmpty()) {
			throw new EGOVRuntimeException(" paymentInfo is null.Please check. ");
		}
	}

	public String getInstrumentNumber() {
		return instrumentNumber;
	}

	public void setInstrumentNumber(String instrumentNumber) {
		this.instrumentNumber = instrumentNumber;
	}

	public Date getInstrumentDate() {
		return instrumentDate;
	}

	public void setInstrumentDate(Date instrumentDate) {
		this.instrumentDate = instrumentDate;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public Long getBankId() {
		return bankId;
	}

	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}

}
