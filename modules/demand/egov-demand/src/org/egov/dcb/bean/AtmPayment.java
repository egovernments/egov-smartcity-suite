package org.egov.dcb.bean;

import java.util.Map;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.Bank;
import org.egov.commons.dao.BankHibernateDAO;
import org.egov.commons.dao.CommonsDaoFactory;

public class AtmPayment extends Payment {
 
	public static final String BANKID = "bankId";
	public static final String ATM_BANK_NAME = "bankName";
	public static final String ATM_BANK_CODE = "bankCode"; 
	public static final String ATM_TRANSACTION_NUMBER = "transactionNumber";
	
	private Long bankId;
	private String bankName;
	private String bankCode;
	private String transactionNumber;
	
	static AtmPayment create(Map<String, String> paymentInfo) {
		return new AtmPayment(paymentInfo);
	}

	private AtmPayment(Map<String, String> paymentInfo) {
		validate(paymentInfo);
		this.setBankName(paymentInfo.get(ATM_BANK_NAME));
		this.setBankCode(paymentInfo.get(ATM_BANK_CODE));
		this.setTransactionNumber(paymentInfo.get(ATM_TRANSACTION_NUMBER));
		setBankIDFromName();
	}

	public String toString() {
		return super.toString() + "bankId=" + getBankId() + " bankName=" + 
		        getBankName() + " bankCode=" + getBankCode() + " transactionNumber=" + getTransactionNumber();
	}

	public void validate(Map<String, String> paymentInfo) {
		if (paymentInfo == null || paymentInfo.isEmpty()) {
			throw new EGOVRuntimeException(" paymentInfo is null.Please check. ");
		}
	}

	private void setBankIDFromName() {
		BankHibernateDAO bankHibDao = CommonsDaoFactory.getDAOFactory()
				.getBankDAO();
		if(this.getBankCode() != null) {
			Bank bank = bankHibDao.getBankByCode(this.getBankCode());
		    this.setBankId(new Long(bank.getId())); 
		}
	}
	
	public Long getBankId() {
		return bankId;
	}

	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}
	
}
