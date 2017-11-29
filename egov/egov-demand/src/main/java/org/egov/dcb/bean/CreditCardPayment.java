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

import org.egov.collection.integration.models.PaymentInfoCard.CARDTYPE;
import org.egov.infra.exception.ApplicationRuntimeException;

import java.util.Map;

public class CreditCardPayment extends Payment {
	public final static String CREDITCARDNO = "creditCardNo";
	public final static String CREDIRCARDEXPIRYMONTH = "creditCardStartMonth";
	public final static String CREDIRCARDEXPIRYYEAR = "crediCardtExpireYear";
	public final static String CVV = "cvv";
	public final static String CARDTYPE = "CardType";
	public final static String CARDTYPE_VISA = "V";
	public final static String CARDTYPE_MASTER = "M";
	public final static String CARDTYPE_OTHER = "O";
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
		this.setPaymentMode(PAYMENTMODE.cc);
		this.setCreditCardNo(paymentInfo.get(CREDITCARDNO));
		this.setTransactionNumber(paymentInfo.get(TRANSACTIONNUMBER));
		setCardTypeIfAvailable(paymentInfo);
		setCVVIfAvailable(paymentInfo);
		setExpiryDateIfAvailable(paymentInfo);
	}

	public String toString() {
		return super.toString()
				+ "creditCardNo "
				+ getCreditCardNo().substring(getCreditCardNo().length() - 4,
						getCreditCardNo().length()) + " Expiry Month "
				+ getExpMonth() + " Expiry Year " + getExpYear();
	}

	public void validate(Map<String, String> paymentInfo) {
		if (paymentInfo == null || paymentInfo.isEmpty()) {
			throw new ApplicationRuntimeException(
					" paymentInfo is null.Please check. ");
		}
	}

	private void setCardTypeIfAvailable(Map<String, String> paymentInfo) {
		if (paymentInfo.get(CARDTYPE) != null) {
			if (paymentInfo.get(CARDTYPE).equals(CARDTYPE_MASTER)) {
				this.setCardType(cardType.M);

			} else if (paymentInfo.get(CARDTYPE).equals(CARDTYPE_VISA)) {
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
