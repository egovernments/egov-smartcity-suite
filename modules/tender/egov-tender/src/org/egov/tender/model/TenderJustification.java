package org.egov.tender.model;

import java.util.Date;

import org.egov.infstr.models.StateAware;


/**
 * TenderJustification entity. @author MyEclipse Persistence Tools
 */


public class TenderJustification  extends StateAware {

	private static final long serialVersionUID = 1L;
	private GenericTenderResponse tenderResponse;
	private String remarks;
	private Date justifiedDate;
	
	public Date getJustifiedDate() {
		return justifiedDate;
	}

	public void setJustifiedDate(Date justifiedDate) {
		this.justifiedDate = justifiedDate;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public GenericTenderResponse getTenderResponse() {
		return tenderResponse;
	}

	public void setTenderResponse(GenericTenderResponse tenderResponse) {
		this.tenderResponse = tenderResponse;
	}

	@Override
	public String getStateDetails() {
		return null;
	}

}