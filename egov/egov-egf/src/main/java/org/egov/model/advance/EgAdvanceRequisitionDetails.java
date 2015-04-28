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
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.model.advance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.egov.commons.CChartOfAccounts;
import org.egov.commons.CFunction;

public class EgAdvanceRequisitionDetails implements Serializable{
	private Long id;
	private Date lastupdatedtime;
	private CChartOfAccounts chartofaccounts;
	private EgAdvanceRequisition egAdvanceRequisition;
	private CFunction function;
	private BigDecimal creditamount = BigDecimal.ZERO;
	private BigDecimal debitamount = BigDecimal.ZERO;
	private String narration;
	private Set<EgAdvanceReqPayeeDetails> egAdvanceReqpayeeDetailses = new HashSet<EgAdvanceReqPayeeDetails>(0);
	
	public EgAdvanceRequisitionDetails(Long id, Date lastupdatedtime,
			CChartOfAccounts chartofaccounts,
			EgAdvanceRequisition egAdvanceRequisition, CFunction function,
			BigDecimal creditamount, BigDecimal debitamount, String narration,
			Set<EgAdvanceReqPayeeDetails> egAdvanceReqpayeeDetailses) {
		super();
		this.id = id;
		this.lastupdatedtime = lastupdatedtime;
		this.chartofaccounts = chartofaccounts;
		this.egAdvanceRequisition = egAdvanceRequisition;
		this.function = function;
		this.creditamount = creditamount;
		this.debitamount = debitamount;
		this.narration = narration;
		this.egAdvanceReqpayeeDetailses = egAdvanceReqpayeeDetailses;
	}

	public EgAdvanceRequisitionDetails() {
		super();
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getLastupdatedtime() {
		return lastupdatedtime;
	}

	public void setLastupdatedtime(Date lastupdatedtime) {
		this.lastupdatedtime = lastupdatedtime;
	}

	public CChartOfAccounts getChartofaccounts() {
		return chartofaccounts;
	}

	public void setChartofaccounts(CChartOfAccounts chartofaccounts) {
		this.chartofaccounts = chartofaccounts;
	}

	public EgAdvanceRequisition getEgAdvanceRequisition() {
		return egAdvanceRequisition;
	}

	public void setEgAdvanceRequisition(EgAdvanceRequisition egAdvanceRequisition) {
		this.egAdvanceRequisition = egAdvanceRequisition;
	}

	public CFunction getFunction() {
		return function;
	}

	public void setFunction(CFunction function) {
		this.function = function;
	}

	public BigDecimal getCreditamount() {
		return creditamount;
	}

	public void setCreditamount(BigDecimal creditamount) {
		this.creditamount = creditamount;
	}

	public BigDecimal getDebitamount() {
		return debitamount;
	}

	public void setDebitamount(BigDecimal debitamount) {
		this.debitamount = debitamount;
	}

	public String getNarration() {
		return narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public Set<EgAdvanceReqPayeeDetails> getEgAdvanceReqpayeeDetailses() {
		return egAdvanceReqpayeeDetailses;
	}

	public void setEgAdvanceReqpayeeDetailses(
			Set<EgAdvanceReqPayeeDetails> egAdvanceReqpayeeDetailses) {
		this.egAdvanceReqpayeeDetailses = egAdvanceReqpayeeDetailses;
	}
	
	
}
