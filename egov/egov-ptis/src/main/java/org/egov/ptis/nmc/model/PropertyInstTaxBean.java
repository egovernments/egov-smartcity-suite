/*******************************************************************************
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
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org
 ******************************************************************************/
/**
 * Class to populate installment wise tax and penalty
 */
package org.egov.ptis.nmc.model;

import java.math.BigDecimal;

import org.egov.commons.Installment;

public class PropertyInstTaxBean {
	private Installment installment;
	private Integer installmentId;
	private String installmentStr;
	private BigDecimal instTaxAmt;
	private BigDecimal instCollAmt;
	private BigDecimal instBalanceAmt;
	private BigDecimal instPenaltyAmt;
	private BigDecimal instRebateAmt;
	private BigDecimal instPenaltyCollAmt = BigDecimal.ZERO;

	public Installment getInstallment() {
		return installment;
	}

	public void setInstallment(Installment installment) {
		this.installment = installment;
	}

	public Integer getInstallmentId() {
		return installmentId;
	}

	public void setInstallmentId(Integer installmentId) {
		this.installmentId = installmentId;
	}

	public String getInstallmentStr() {
		return installmentStr;
	}

	public void setInstallmentStr(String installmentStr) {
		this.installmentStr = installmentStr;
	}

	public BigDecimal getInstTaxAmt() {
		return instTaxAmt;
	}

	public void setInstTaxAmt(BigDecimal instTaxAmt) {
		this.instTaxAmt = instTaxAmt;
	}

	public BigDecimal getInstCollAmt() {
		return instCollAmt;
	}

	public void setInstCollAmt(BigDecimal instCollAmt) {
		this.instCollAmt = instCollAmt;
	}

	public BigDecimal getInstBalanceAmt() {
		return instBalanceAmt;
	}

	public void setInstBalanceAmt(BigDecimal instBalanceAmt) {
		this.instBalanceAmt = instBalanceAmt;
	}

	public BigDecimal getInstPenaltyAmt() {
		return instPenaltyAmt;
	}

	public void setInstPenaltyAmt(BigDecimal instPenaltyAmt) {
		this.instPenaltyAmt = instPenaltyAmt;
	}

	public BigDecimal getInstRebateAmt() {
		return instRebateAmt;
	}

	public void setInstRebateAmt(BigDecimal instRebateAmt) {
		this.instRebateAmt = instRebateAmt;
	}

	public BigDecimal getInstPenaltyCollAmt() {
		return instPenaltyCollAmt;
	}

	public void setInstPenaltyCollAmt(BigDecimal instPenaltyCollAmt) {
		this.instPenaltyCollAmt = instPenaltyCollAmt;
	}
}
