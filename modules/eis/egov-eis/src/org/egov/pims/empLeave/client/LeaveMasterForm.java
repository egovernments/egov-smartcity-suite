package org.egov.pims.empLeave.client;


import org.apache.struts.action.ActionForm;

public class LeaveMasterForm extends ActionForm{

	private String[] typeOfLeaveMstr;
	private String[] noOfDays;
	private String[] leaveMstrId;
	
	
	public String[] getNoOfDays() {
		return noOfDays;
	}
	public void setNoOfDays(String[] noOfDays) {
		this.noOfDays = noOfDays;
	}
	
	public String[] getTypeOfLeaveMstr() {
		return typeOfLeaveMstr;
	}
	public void setTypeOfLeaveMstr(String[] typeOfLeaveMstr) {
		this.typeOfLeaveMstr = typeOfLeaveMstr;
	}
	public String[] getLeaveMstrId() {
		return leaveMstrId;
	}
	public void setLeaveMstrId(String[] leaveMstrId) {
		this.leaveMstrId = leaveMstrId;
	}

}
