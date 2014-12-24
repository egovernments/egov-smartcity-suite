package org.egov.payroll.web.actions.reports;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Comparator;

public class EmpDeductionInfo implements Comparator<EmpDeductionInfo>{ 
	private String empCode;
	private String empName;
	private String designation;
	private String empDateOfBirth;
	private String empDateOfRetirement;
	private BigDecimal grossSalary;
	private BigDecimal netSalary;
	private BigDecimal totalPF;
	private BigDecimal totalLIC;
	private BigDecimal totalIncomeTax;
	private BigDecimal profTax; 
	private BigDecimal amount;
	private String installmentNo;
	private String dcpsSubNo;
	private BigDecimal dcpsAmt;
	private BigDecimal dcpsArr;
	private BigDecimal dcpsTotal;
	private String accountNumber;
	private String bank;
	private String branch;
	private LinkedList<LICDetailDTO> licDetails=new LinkedList<LICDetailDTO>();

	public int compare(EmpDeductionInfo emp1, EmpDeductionInfo emp2) {
		  String name1 = ((EmpDeductionInfo)emp1).getEmpName();
		  String name2 = ((EmpDeductionInfo)emp2).getEmpName();

		  if (name1.compareTo(name2)>0) {
		   return 1;
		  }
		  else if (name1.compareTo(name2)<0) {
		   return -1;
		  }
		  else {
		   return 0;
		  }
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
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public BigDecimal getGrossSalary() {
		return grossSalary;
	}
	public void setGrossSalary(BigDecimal grossSalary) {
		this.grossSalary = grossSalary;
	}
	public BigDecimal getNetSalary() {
		return netSalary;
	}
	public void setNetSalary(BigDecimal netSalary) {
		this.netSalary = netSalary;
	}
	public BigDecimal getTotalPF() {
		return totalPF;
	}
	public void setTotalPF(BigDecimal totalPF) {
		this.totalPF = totalPF;
	}
	public BigDecimal getTotalLIC() {
		return totalLIC;
	}
	public void setTotalLIC(BigDecimal totalLIC) {
		this.totalLIC = totalLIC;
	}
	public BigDecimal getTotalIncomeTax() {
		return totalIncomeTax;
	}
	public void setTotalIncomeTax(BigDecimal totalIncomeTax) {
		this.totalIncomeTax = totalIncomeTax;
	}
	public BigDecimal getProfTax() {
		return profTax;
	}
	public void setProfTax(BigDecimal profTax) {
		this.profTax = profTax;
	}

	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getEmpDateOfBirth() {
		return empDateOfBirth;
	}
	public void setEmpDateOfBirth(String empDateOfBirth) {
		this.empDateOfBirth = empDateOfBirth;
	}
	public String getEmpDateOfRetirement() {
		return empDateOfRetirement;
	}
	public void setEmpDateOfRetirement(String empDateOfRetirement) {
		this.empDateOfRetirement = empDateOfRetirement;
	}
	
	public String getInstallmentNo() {
		return installmentNo;
	}
	public void setInstallmentNo(String installmentNo) {
		this.installmentNo = installmentNo;
	}
	public LinkedList<LICDetailDTO> getLicDetails() {
		return licDetails;
	}
	public void setLicDetails(LinkedList<LICDetailDTO> licDetails) {
		this.licDetails = licDetails;
	}

	public String getDcpsSubNo() {
		return dcpsSubNo;
	}

	public void setDcpsSubNo(String dcpsSubNo) {
		this.dcpsSubNo = dcpsSubNo;
	}

	public BigDecimal getDcpsAmt() {
		return dcpsAmt;
	}

	public void setDcpsAmt(BigDecimal dcpsAmt) {
		this.dcpsAmt = dcpsAmt;
	}

	public BigDecimal getDcpsArr() {
		return dcpsArr;
	}

	public void setDcpsArr(BigDecimal dcpsArr) {
		this.dcpsArr = dcpsArr;
	}

	public BigDecimal getDcpsTotal() {
		return dcpsTotal;
	}

	public void setDcpsTotal(BigDecimal dcpsTotal) {
		this.dcpsTotal = dcpsTotal;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}


}
