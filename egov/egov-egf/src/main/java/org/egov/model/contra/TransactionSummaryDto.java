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
