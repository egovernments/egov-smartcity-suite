package org.egov.payroll.client.advance;

import org.apache.struts.action.ActionForm;
import org.egov.payroll.model.Advance;

public class AdvanceForm extends ActionForm{
	
	String employeeCode ;	
	String employeeName ;
	String salarycode;
	String advAmount;
	String interestPct;
	String interestType;
	String numberOfInstallments;
	String interestAmount;
	String total;
	String monthlyPayment;
	String sanctionNo;
	String sanctionBy;
	String action;
	String employeeCodeId ;
	String salaryadvanceId ;
	String paymentMethod;
	String advanceType;
	String isSaved;
	String sanctionDate;
	String bankAccountId;
	String bankBranchId;
	String sanctionRejectAction;
	String remarks;
	String mode;
	Advance advance;
	String advanceId;
	String pendingAmt;
	String maintainSchedule;
	
	String installmentNo[];
	String principalInstAmount[];
	String interestInstAmount[];
	String recover[];
	String payslipId[];
	String pendingPrevAmt;
	
	//Added for manual workflow
	private String approverDept;
	private String approverDesig;
	private String approverEmpAssignmentId;
	private String ess = "0";
	
	
	public String getIsSaved() {
		return isSaved;
	}

	public void setIsSaved(String isSaved) {
		this.isSaved = isSaved;
	}

	public String getAdvAmount() {
		return advAmount;
	}

	public void setAdvAmount(String advAmount) {
		this.advAmount = advAmount;
	}

	public String getInterestAmount() {
		return interestAmount;
	}

	public void setInterestAmount(String interestAmount) {
		this.interestAmount = interestAmount;
	}

	public String getInterestPct() {
		return interestPct;
	}

	public void setInterestPct(String interestPct) {
		this.interestPct = interestPct;
	}

	public String getInterestType() {
		return interestType;
	}

	public void setInterestType(String interestType) {
		this.interestType = interestType;
	}

	public String getMonthlyPayment() {
		return monthlyPayment;
	}

	public void setMonthlyPayment(String monthlyPayment) {
		this.monthlyPayment = monthlyPayment;
	}

	public String getNumberOfInstallments() {
		return numberOfInstallments;
	}

	public void setNumberOfInstallments(String numberOfInstallments) {
		this.numberOfInstallments = numberOfInstallments;
	}

	

	public String getSanctionNo() {
		return sanctionNo;
	}

	public String getSanctionBy() {
		return sanctionBy;
	}

	public void setSanctionBy(String sanctionBy) {
		this.sanctionBy = sanctionBy;
	}

	public void setSanctionNo(String sanctionNo) {
		this.sanctionNo = sanctionNo;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}	
	
	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getSalarycode() {
		return salarycode;
	}

	public void setSalarycode(String salarycode) {
		this.salarycode = salarycode;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getEmployeeCodeId() {
		return employeeCodeId;
	}

	public void setEmployeeCodeId(String employeeCodeId) {
		this.employeeCodeId = employeeCodeId;
	}
	
	public String getSalaryadvanceId() {
		return salaryadvanceId;
	}

	public void setSalaryadvanceId(String salaryadvanceId) {
		this.salaryadvanceId = salaryadvanceId;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getAdvanceType() {
		return advanceType;
	}

	public void setAdvanceType(String advanceType) {
		this.advanceType = advanceType;
	}

	public String getSanctionDate() {
		return sanctionDate;
	}

	public void setSanctionDate(String sanctionDate) {
		this.sanctionDate = sanctionDate;
	}

	public String getBankAccountId() {
		return bankAccountId;
	}

	public void setBankAccountId(String bankAccountId) {
		this.bankAccountId = bankAccountId;
	}

	public String getBankBranchId() {
		return bankBranchId;
	}

	public void setBankBranchId(String bankBranchId) {
		this.bankBranchId = bankBranchId;
	}

	public String getSanctionRejectAction() {
		return sanctionRejectAction;
	}

	public void setSanctionRejectAction(String sanctionRejectAction) {
		this.sanctionRejectAction = sanctionRejectAction;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public Advance getAdvance() {
		return advance;
	}

	public void setAdvance(Advance advance) {
		this.advance = advance;
	}

	public String getAdvanceId() {
		return advanceId;
	}

	public void setAdvanceId(String advanceId) {
		this.advanceId = advanceId;
	}

	public String getPendingAmt() {
		return pendingAmt;
	}

	public void setPendingAmt(String pendingAmt) {
		this.pendingAmt = pendingAmt;
	}

	public String getMaintainSchedule() {
		return maintainSchedule;
	}

	
	public void setMaintainSchedule(String maintainSchedule) {
		this.maintainSchedule = maintainSchedule;
	}	

	public String[] getInstallmentNo() {
		return installmentNo;
	}

	public void setInstallmentNo(String[] installmentNo) {
		this.installmentNo = installmentNo;
	}

	public String[] getPrincipalInstAmount() {
		return principalInstAmount;
	}

	public void setPrincipalInstAmount(String[] principalInstAmount) {
		this.principalInstAmount = principalInstAmount;
	}

	public String[] getInterestInstAmount() {
		return interestInstAmount;
	}

	public void setInterestInstAmount(String[] interestInstAmount) {
		this.interestInstAmount = interestInstAmount;
	}

	public String[] getRecover() {
		return recover;
	}

	public void setRecover(String[] recover) {
		this.recover = recover;
	}

	public String[] getPayslipId() {
		return payslipId;
	}

	public void setPayslipId(String[] payslipId) {
		this.payslipId = payslipId;
	}

	public String getApproverDept() {
		return approverDept;
	}

	public void setApproverDept(String approverDept) {
		this.approverDept = approverDept;
	}

	public String getApproverDesig() {
		return approverDesig;
	}

	public void setApproverDesig(String approverDesig) {
		this.approverDesig = approverDesig;
	}

	public String getApproverEmpAssignmentId() {
		return approverEmpAssignmentId;
	}

	public void setApproverEmpAssignmentId(String approverEmpAssignmentId) {
		this.approverEmpAssignmentId = approverEmpAssignmentId;
	}

	public String getEss() {
		return ess;
	}

	public void setEss(String ess) {
		this.ess = ess;
	}

	public String getPendingPrevAmt() {
		return pendingPrevAmt;
	}

	public void setPendingPrevAmt(String pendingPrevAmt) {
		this.pendingPrevAmt = pendingPrevAmt;
	}
}
