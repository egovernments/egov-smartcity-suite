package org.egov.pims.model;

import java.util.Date;

import org.egov.commons.EgwStatus;
import org.egov.infstr.models.StateAware;
import org.egov.infstr.models.validator.Required;

public class PowerDelegation extends StateAware {
	
	
	private String remarks;
	private EgwStatus status;
	private Long docNo;
	
	@Required(message="powerdelegation.effdate.required")
	private Date effectiveDate;
	
	@Required(message="powerdelegation.ordernumber.required")
	private String orderNumber;
	
	@Required(message="powerdelegation.orderBy.required")
	private String orderBy;

	
	public String getRemarks() {
		return remarks;
	}


	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}


	public EgwStatus getStatus() {
		return status;
	}


	public void setStatus(EgwStatus status) {
		this.status = status;
	}


	public Long getDocNo() {
		return docNo;
	}


	public void setDocNo(Long docNo) {
		this.docNo = docNo;
	}


	public Date getEffectiveDate() {
		return effectiveDate;
	}


	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}


	public String getOrderNumber() {
		return orderNumber;
	}


	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}


	public String getOrderBy() {
		return orderBy;
	}


	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}


	@Override
	public String getStateDetails() {
		// TODO Auto-generated method stub
		return null;
	}

}
