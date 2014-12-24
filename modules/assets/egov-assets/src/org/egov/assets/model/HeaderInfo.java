package org.egov.assets.model;

import java.util.Date;

public class HeaderInfo {
	
	private String voucherName;
	private String voucherType;
	private String description;
	private String voucherNumber;
	private Date voucherDate;
	private String department;							// department code
	private String fund;								// fund code
	private String functionary;							// functionary code 
	private String scheme;								// scheme code
	private String subScheme;							// subscheme code
	private String fundSource;							// fundsource code
	private Integer division;							// division Id
	private String module;
	
	private String status;
	private String originalVoucher;
	private String refVoucher;
	private String voucherSubtype;
	private Boolean budgetCheckReq;
	private Long bill;									// bill Id - nr
	
	
	public String getVoucherName() {
		return voucherName;
	}
	public void setVoucherName(String voucherName) {
		this.voucherName = voucherName;
	}
	public String getVoucherType() {
		return voucherType;
	}
	public void setVoucherType(String voucherType) {
		this.voucherType = voucherType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getVoucherNumber() {
		return voucherNumber;
	}
	public void setVoucherNumber(String voucherNumber) {
		this.voucherNumber = voucherNumber;
	}
	public Date getVoucherDate() {
		return voucherDate;
	}
	public void setVoucherDate(Date voucherDate) {
		this.voucherDate = voucherDate;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getFund() {
		return fund;
	}
	public void setFund(String fund) {
		this.fund = fund;
	}
	public String getFunctionary() {
		return functionary;
	}
	public void setFunctionary(String functionary) {
		this.functionary = functionary;
	}
	public String getScheme() {
		return scheme;
	}
	public void setScheme(String scheme) {
		this.scheme = scheme;
	}
	public String getSubScheme() {
		return subScheme;
	}
	public void setSubScheme(String subScheme) {
		this.subScheme = subScheme;
	}
	public String getFundSource() {
		return fundSource;
	}
	public void setFundSource(String fundSource) {
		this.fundSource = fundSource;
	}
	public Integer getDivision() {
		return division;
	}
	public void setDivision(Integer division) {
		this.division = division;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOriginalVoucher() {
		return originalVoucher;
	}
	public void setOriginalVoucher(String originalVoucher) {
		this.originalVoucher = originalVoucher;
	}
	public String getRefVoucher() {
		return refVoucher;
	}
	public void setRefVoucher(String refVoucher) {
		this.refVoucher = refVoucher;
	}
	public String getVoucherSubtype() {
		return voucherSubtype;
	}
	public void setVoucherSubtype(String voucherSubtype) {
		this.voucherSubtype = voucherSubtype;
	}
	public Boolean getBudgetCheckReq() {
		return budgetCheckReq;
	}
	public void setBudgetCheckReq(Boolean budgetCheckReq) {
		this.budgetCheckReq = budgetCheckReq;
	}
	public Long getBill() {
		return bill;
	}
	public void setBill(Long bill) {
		this.bill = bill;
	}

}
