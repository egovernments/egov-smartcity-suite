package org.egov.payroll.model.providentfund;

import org.egov.commons.CFinancialYear;
import org.egov.infstr.models.BaseModel;

public class PFTriggerDetail extends BaseModel implements java.io.Serializable
{
	private Long id = null;
	private String pfType=null;
	private Integer month=null;
	private CFinancialYear financialYear =null;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPfType() {
		return pfType;
	}
	public void setPfType(String pfType) {
		this.pfType = pfType;
	}
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
	public CFinancialYear getFinancialYear() {
		return financialYear;
	}
	public void setFinancialYear(CFinancialYear financialYear) {
		this.financialYear = financialYear;
	}
	
	
}
