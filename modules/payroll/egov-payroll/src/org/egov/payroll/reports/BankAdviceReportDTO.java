package org.egov.payroll.reports;

import java.math.BigDecimal;

/**
 * this class used as DTO for BankAdvicePaymentReport/EmpwisePaymentReport
 * @author suhasini
 *
 */
public class BankAdviceReportDTO implements java.io.Serializable{

	private static final long serialVersionUID = 1L;

	String bankName;
	String branchName;
	String branchCode;
	String deptName;
	BigDecimal netAmount;

	String empCode;
	String empName;
	String empType;
	String empBankAcNo;
	Long empCount;

	public BankAdviceReportDTO( )
	{
	}
	public BankAdviceReportDTO(String empcode,String empName,String branchName, String bankName,String empBankAcNo,BigDecimal netpay)
	{
		this.empCode=empcode;
		this.empName=empName;
		this.branchName=branchName;
		this.bankName=bankName;this.empBankAcNo=empBankAcNo;
		this.netAmount=netpay;
	}
	public BankAdviceReportDTO(String empType,String empcode,String empName,String deptName,String branchName, String bankName,String empBankAcNo,BigDecimal netpay)
	{
		this.empType=empType;
		this.empCode=empcode;
		this.empName=empName;
		this.deptName=deptName;
		this.branchName=branchName;
		this.bankName=bankName;
		this.empBankAcNo=empBankAcNo;
		this.netAmount=netpay;
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

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public BigDecimal getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(BigDecimal netAmount) {
		this.netAmount = netAmount;
	}

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getEmpType() {
		return empType;
	}

	public void setEmpType(String empType) {
		this.empType = empType;
	}

	public String getEmpBankAcNo() {
		return empBankAcNo;
	}

	public void setEmpBankAcNo(String empBankAcNo) {
		this.empBankAcNo = empBankAcNo;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	public Long getEmpCount() {
		return empCount;
	}
	public void setEmpCount(Long empCount) {
		this.empCount = empCount;
	}

}
