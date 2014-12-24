package org.egov.pims.empLeave.model;

import org.egov.lib.rjbac.user.User;

public class LeaveApproval implements java.io.Serializable {
	private Integer id;
	private User approvedBy;
	private org.egov.pims.empLeave.model.LeaveApplication appId;
	
	private Character payElegible= new Character('0');

	public Character getPayElegible() {
		return payElegible;
	}
	public void setPayElegible(Character payElegible) {
		this.payElegible = payElegible;
	}

	public org.egov.pims.empLeave.model.LeaveApplication getAppId() {
		return appId;
	}
	public void setAppId(org.egov.pims.empLeave.model.LeaveApplication appId) {
		this.appId = appId;
	}
	public User getApprovedBy() {
		return approvedBy;
	}
	public void setApprovedBy(User approvedBy) {
		this.approvedBy = approvedBy;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	

}
