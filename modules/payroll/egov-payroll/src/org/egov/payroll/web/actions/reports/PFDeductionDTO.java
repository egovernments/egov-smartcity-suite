package org.egov.payroll.web.actions.reports;

import java.math.BigDecimal;

public class PFDeductionDTO {
	private String PFNum;
	private String  code;
	private String  name;
	private String  salaryCode;
	private BigDecimal  GPFSUbamt;
	private BigDecimal  GPFADVamt;	
	private BigDecimal  noofinst;
	private BigDecimal  instno;
	public String getPFNum() {
		return PFNum;
	}
	public void setPFNum(String pFNum) {
		PFNum = pFNum;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal getGPFSUbamt() {
		return GPFSUbamt;
	}
	public void setGPFSUbamt(BigDecimal gPFSUbamt) {
		GPFSUbamt = gPFSUbamt;
	}
	public BigDecimal getGPFADVamt() {
		return GPFADVamt;
	}
	public void setGPFADVamt(BigDecimal gPFADVamt) {
		GPFADVamt = gPFADVamt;
	}
	public BigDecimal getNoofinst() {
		return noofinst;
	}
	public void setNoofinst(BigDecimal noofinst) {
		this.noofinst = noofinst;
	}
	public BigDecimal getInstno() {
		return instno;
	}
	public void setInstno(BigDecimal instno) { 
		this.instno = instno;
	}
	public String getSalaryCode() {
		return salaryCode;
	}
	public void setSalaryCode(String salaryCode) {
		this.salaryCode = salaryCode;
	}
	

}
