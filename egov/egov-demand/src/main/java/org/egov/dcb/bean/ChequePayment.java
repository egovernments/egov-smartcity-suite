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
import org.egov.infra.exception.ApplicationRuntimeException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

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
	public static final SimpleDateFormat CHEQUE_DATE_FORMAT = new SimpleDateFormat(
			"dd-MM-yyyy");

	public static ChequePayment create(Map<String, String> paymentInfo) {
		return new ChequePayment(paymentInfo);
	}

	private ChequePayment(Map<String, String> paymentInfo) {
		validate(paymentInfo);
		LOGGER.debug("-Cheque Payment -paymentInfo " + paymentInfo);
		this.setPaymentMode(PAYMENTMODE.cheque);
		this.setInstrumentNumber((String) paymentInfo.get(INSTRUMENTNUMBER));
		try {
			this.setInstrumentDate(CHEQUE_DATE_FORMAT.parse(paymentInfo
					.get(INSTRUMENTDATE)));
		} catch (ParseException e) {
			throw new ApplicationRuntimeException(
					"InstrumentDate-Date Format should be dd-MM-yyyy", e);
		}
		if (paymentInfo.get(BANKID) == null) {
			this.setBankName(paymentInfo.get(BANKNAME));
			this.setBankId(Long.valueOf(paymentInfo.get(BANKID)));
		} else {
			this.setBankId(Long.valueOf(paymentInfo.get(BANKID)));
		}

		if (paymentInfo.get(BRANCHNAME) != null) {
			this.setBranchName(paymentInfo.get(BRANCHNAME));
		}
		LOGGER.debug("-Cheque Payment is created " + this);
	}

	public String toString() {
		return super.toString() + "instrumentNumber " + getInstrumentNumber()
				+ " instrumentDate " + getInstrumentDate() + " bankName "
				+ getBankName() + " branchName " + getBranchName();
	}

	public void validate(Map<String, String> paymentInfo) {
		if (paymentInfo != null && paymentInfo.isEmpty()) {
			throw new ApplicationRuntimeException(
					" paymentInfo is null.Please check. ");
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
