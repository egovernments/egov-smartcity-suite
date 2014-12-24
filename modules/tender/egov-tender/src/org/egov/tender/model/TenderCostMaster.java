package org.egov.tender.model;

import java.math.BigDecimal;
import java.util.Date;

import org.egov.commons.CFinancialYear;
import org.egov.infstr.utils.FinancialYear;
import org.egov.lib.rjbac.user.UserImpl;

public class TenderCostMaster implements java.io.Serializable{

	private Long id;
	private CFinancialYear financialyear;
	private BigDecimal minamount;
	private BigDecimal maxamount;
	private BigDecimal percentage;
	private BigDecimal flatrate;
	private UserImpl createdby;
	private Date createddate;
	private Date lastmodifieddate;
	
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
	public BigDecimal getMinamount() {
		return minamount;
	}
	public void setMinamount(BigDecimal minamount) {
		this.minamount = minamount;
	}
	public BigDecimal getMaxamount() {
		return maxamount;
	}
	public void setMaxamount(BigDecimal maxamount) {
		this.maxamount = maxamount;
	}
	public BigDecimal getPercentage() {
		return percentage;
	}
	public void setPercentage(BigDecimal percentage) {
		this.percentage = percentage;
	}
	public BigDecimal getFlatrate() {
		return flatrate;
	}
	public void setFlatrate(BigDecimal flatrate) {
		this.flatrate = flatrate;
	}
	public UserImpl getCreatedby() {
		return createdby;
	}
	public void setCreatedby(UserImpl createdby) {
		this.createdby = createdby;
	}
	public Date getCreateddate() {
		return createddate;
	}
	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}
	public Date getLastmodifieddate() {
		return lastmodifieddate;
	}
	public void setLastmodifieddate(Date lastmodifieddate) {
		this.lastmodifieddate = lastmodifieddate;
	}
}
