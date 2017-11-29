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

package org.egov.model.contra;

import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.Functionary;
import org.egov.commons.Fund;
import org.egov.commons.Fundsource;
import org.egov.infra.admin.master.entity.Department;

import java.util.List;

/**
 * @author manoj
 *
 */
public class TransactionSummaryDto {

	private Long id;
    
    private CFinancialYear financialyear;
    
    private Fundsource fundsource;
    
    private Fund fund;
    
    private Department departmentid;
    
    private Functionary functionaryid;
    
    private CFunction functionid;
    
    private Integer divisionid;
    
    List<TransactionSummary> transactionSummaryList;
    
    public TransactionSummaryDto() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CFinancialYear getFinancialyear() {
		return financialyear;
	}

	public void setFinancialyear(CFinancialYear financialyear) {
		this.financialyear = financialyear;
	}

	public Fundsource getFundsource() {
		return fundsource;
	}

	public void setFundsource(Fundsource fundsource) {
		this.fundsource = fundsource;
	}

	public Fund getFund() {
		return fund;
	}

	public void setFund(Fund fund) {
		this.fund = fund;
	}

	public Department getDepartmentid() {
		return departmentid;
	}

	public void setDepartmentid(Department departmentid) {
		this.departmentid = departmentid;
	}

	public Functionary getFunctionaryid() {
		return functionaryid;
	}

	public void setFunctionaryid(Functionary functionaryid) {
		this.functionaryid = functionaryid;
	}

	public CFunction getFunctionid() {
		return functionid;
	}

	public void setFunctionid(CFunction functionid) {
		this.functionid = functionid;
	}

	public Integer getDivisionid() {
		return divisionid;
	}

	public void setDivisionid(Integer divisionid) {
		this.divisionid = divisionid;
	}

	public List<TransactionSummary> getTransactionSummaryList() {
		return transactionSummaryList;
	}

	public void setTransactionSummaryList(
			List<TransactionSummary> transactionSummaryList) {
		this.transactionSummaryList = transactionSummaryList;
	}
}
