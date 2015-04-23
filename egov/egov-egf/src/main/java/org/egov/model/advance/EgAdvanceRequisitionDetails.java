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
