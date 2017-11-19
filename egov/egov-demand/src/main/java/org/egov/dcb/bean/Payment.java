/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
package org.egov.dcb.bean;

import org.apache.log4j.Logger;
import org.egov.commons.Bank;
import org.egov.commons.dao.BankHibernateDAO;
import org.egov.infra.exception.ApplicationRuntimeException;

import java.math.BigDecimal;
import java.util.Map;

public abstract class Payment {
	public final static String CHEQUE = "cheque";
	public static final String DD = "dd";
	public final static String CREDITCARD = "creditcard";
	public final static String ATMCARD = "atmcard";
    public final static String ONLINE = "online";
    public final static String CASH = "cash";
    public final static String BANK_TO_BANK = "banktobank";
    public static enum PAYMENTMODE {cash, cheque, dd, cc, b2b, atm, online};
    
	public final static String PAIDBY = "paidBy";
    public final static String AMOUNT = "amount";

	private BigDecimal amount;
    private PAYMENTMODE paymentMode;
    private String paidBy;
    private BankHibernateDAO bankHibernateDAO;
	
	// These two fields will be used in the case of walk-in payments from banks
    private String depositedInBankCode;
    private String paymentReferenceNoFromBank;
		
	private static final Logger LOGGER = Logger.getLogger(Payment.class);

	public static Payment create(String type, Map<String, String> paymentInfo) {
		LOGGER.debug("-Creating payment ---" + type + " paymentInfo " + paymentInfo);
		validate(type, paymentInfo);
		Payment payment = null;
		if (type.equals(CHEQUE)) {
			payment = ChequePayment.create(paymentInfo);

		} else if (type.equals(DD)) {
            payment = DDPayment.create(paymentInfo);

		} else if (type.equals(CREDITCARD)) {
			payment = CreditCardPayment.create(paymentInfo);
			
		} else if (type.equals(ATMCARD)) {
			payment = AtmPayment.create(paymentInfo);
			
        } else if (type.equals(ONLINE)) {
            payment = OnlinePayment.create(paymentInfo);
            
        } else if (type.equals(CASH)) {
            payment = CashPayment.create();
        } 
		
		payment.setAmount(new BigDecimal(paymentInfo.get(AMOUNT)));
		payment.setPaidBy(paymentInfo.get(PAIDBY));
		LOGGER.debug("Created payment=" + payment);
		return payment;
	} 

	public String toString() {
		return new StringBuilder()
		.append("amount=").append(getAmount())
        .append(" paymentMode=").append(getPaymentMode())
        .append(" paidBy=").append(getPaidBy())
        .append(" depositedInBankCode=").append(depositedInBankCode)
		.append(" paymentRef=").append(paymentReferenceNoFromBank)
		.toString();
	}

	private static void validate(String type, Map<String, String> paymentInfo) {
		if (type == null || type.isEmpty() || paymentInfo == null) { 
			throw new ApplicationRuntimeException(
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

    protected Long fetchBankIDFromCodeOrName(String bankCodeOrName) {
       
        // Tries by code first
        Bank bank = bankHibernateDAO.getBankByCode(bankCodeOrName);
        if (bank == null) {
            // Tries by name if code not found
            bank = bankHibernateDAO.getBankByCode(bankCodeOrName);
        }
        return new Long(bank.getId());
    }

    public String getPaymentReferenceNoFromBank() {
        return paymentReferenceNoFromBank;
    }

    public void setPaymentReferenceNoFromBank(String paymentReferenceNoFromBank) {
        this.paymentReferenceNoFromBank = paymentReferenceNoFromBank;
    }

    public String getDepositedInBankCode() {
        return depositedInBankCode;
    }

    public void setDepositedInBankCode(String depositedInBankCode) {
        this.depositedInBankCode = depositedInBankCode;
    }

    public PAYMENTMODE getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(PAYMENTMODE paymentMode) {
        this.paymentMode = paymentMode;
    }

	public BankHibernateDAO getBankHibernateDAO() {
		return bankHibernateDAO;
	}

	public void setBankHibernateDAO(BankHibernateDAO bankHibernateDAO) {
		this.bankHibernateDAO = bankHibernateDAO;
	}

	
    
}
