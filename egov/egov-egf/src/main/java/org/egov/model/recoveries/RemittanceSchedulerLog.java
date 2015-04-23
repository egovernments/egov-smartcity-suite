package org.egov.model.recoveries;

import java.util.Date;

public class RemittanceSchedulerLog implements java.io.Serializable
{
	private Long id;
	private String schJobName;
	private Date lastRunDate;
	private Integer createdBy;
	private Date createdDate;
	private Character schType;
	private String glcode;
	private String status;
	private String remarks;
		
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getSchJobName() {
		return schJobName;
	}
	public void setSchJobName(String schJobName) {
		this.schJobName = schJobName;
	}
	
	public Date getLastRunDate() {
		return lastRunDate;
	}
	public void setLastRunDate(Date lastRunDate) {
		this.lastRunDate = lastRunDate;
	}
	
    
    
    public Date getCreatedDate() {
	return createdDate;
    }
    public void setCreatedDate(Date createdDate) {
	this.createdDate = createdDate;
    }
    
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	public String getGlcode() {
		return glcode;
	}
	public void setGlcode(String glcode) {
		this.glcode = glcode;
	}
	public Character getSchType() {
		return schType;
	}
	public void setSchType(Character schType) {
		this.schType = schType;
	}
	
    
}


