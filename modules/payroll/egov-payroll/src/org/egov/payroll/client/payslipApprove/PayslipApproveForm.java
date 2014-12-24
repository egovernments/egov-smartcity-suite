package org.egov.payroll.client.payslipApprove;

import java.math.BigDecimal;
import java.util.List;

import org.apache.struts.action.ActionForm;

public class PayslipApproveForm extends ActionForm{
	
	
	String action;
	List empPayrolls;
	String approveStatus;
	List earningPayheads;
	List deductionpayheads;	
	List otherdeDuctionPayheads;
	String sortedBy;	
//	List earningsTotal;
//	List deductionsTotal;
//	List otherDeductionTotal;
	BigDecimal grossTotal;
	BigDecimal netTotal;	
	BigDecimal deductionTotal;
	String deptName;
	
	String approvedPayslips[];
	String rejectedPayslips[];
	String rejectComments[];
	
	

	public String[] getRejectComments() {
		return rejectComments;
	}

	public void setRejectComments(String[] rejectComments) {
		this.rejectComments = rejectComments;
	}

	public String[] getApprovedPayslips() {
		return approvedPayslips;
	}

	public void setApprovedPayslips(String[] approvedPayslips) {
		this.approvedPayslips = approvedPayslips;
	}

	public String[] getRejectedPayslips() {
		return rejectedPayslips;
	}

	public void setRejectedPayslips(String[] rejectedPayslips) {
		this.rejectedPayslips = rejectedPayslips;
	}

	public BigDecimal getGrossTotal() {
		return grossTotal;
	}

	public void setGrossTotal(BigDecimal grossTotal) {
		this.grossTotal = grossTotal;
	}

	public BigDecimal getNetTotal() {
		return netTotal;
	}

	public void setNetTotal(BigDecimal netTotal) {
		this.netTotal = netTotal;
	}

	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getSortedBy() {
		return sortedBy;
	}

	public void setSortedBy(String sortedBy) {
		this.sortedBy = sortedBy;
	}

	public List getDeductionpayheads() {
		return deductionpayheads;
	}

	public void setDeductionpayheads(List deductionpayheads) {
		this.deductionpayheads = deductionpayheads;
	}

	public List getEarningPayheads() {
		return earningPayheads;
	}

	public void setEarningPayheads(List earningPayheads) {
		this.earningPayheads = earningPayheads;
	}

	public String getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}

	public List getEmpPayrolls() {
		return empPayrolls;
	}

	public void setEmpPayrolls(List empPayrolls) {
		this.empPayrolls = empPayrolls;
	}

	public List getOtherdeDuctionPayheads() {
		return otherdeDuctionPayheads;
	}

	public void setOtherdeDuctionPayheads(List otherdeDuctionPayheads) {
		this.otherdeDuctionPayheads = otherdeDuctionPayheads;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public BigDecimal getDeductionTotal() {
		return deductionTotal;
	}

	public void setDeductionTotal(BigDecimal deductionTotal) {
		this.deductionTotal = deductionTotal;
	}
	

}
