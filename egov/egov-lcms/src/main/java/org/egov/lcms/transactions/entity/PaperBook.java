/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */
package org.egov.lcms.transactions.entity;

import java.util.Date;

import org.egov.infstr.models.BaseModel;

public class PaperBook extends BaseModel {

	private static final long serialVersionUID = 1L;

	private Legalcase legalcase;
	private Date lastDateToDepositAmt;
	private Double depositedAmount;
	private String concernedOfficerName;
	private String remarks;
	private boolean isPaperBookRequired;

	public Legalcase getLegalcase() {
		return legalcase;
	}

	public void setLegalcase(final Legalcase legalcase) {
		this.legalcase = legalcase;
	}

	public Date getLastDateToDepositAmt() {
		return lastDateToDepositAmt;
	}

	public void setLastDateToDepositAmt(final Date lastDateToDepositAmt) {
		this.lastDateToDepositAmt = lastDateToDepositAmt;
	}

	public Double getDepositedAmount() {
		return depositedAmount;
	}

	public void setDepositedAmount(final Double depositedAmount) {
		this.depositedAmount = depositedAmount;
	}

	public String getConcernedOfficerName() {
		return concernedOfficerName;
	}

	public void setConcernedOfficerName(final String concernedOfficerName) {
		this.concernedOfficerName = concernedOfficerName;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(final String remarks) {
		this.remarks = remarks;
	}

	public boolean getIsPaperBookRequired() {
		return isPaperBookRequired;
	}

	public void setIsPaperBookRequired(final boolean isPaperBookRequired) {
		this.isPaperBookRequired = isPaperBookRequired;
	}

}
