package org.egov.pims.empLeave.model;

import java.text.SimpleDateFormat;

import org.egov.infstr.models.State;
import org.egov.infstr.models.StateAware;
import org.egov.pims.commons.Position;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.model.StatusMaster;

public class CompOff extends StateAware implements java.io.Serializable
{
//private Integer id ;
private org.egov.lib.rjbac.user.User approvedBy ;
private StatusMaster status ;
private org.egov.lib.rjbac.user.User createdBy ;
private org.egov.pims.empLeave.model.Attendence attObj ;
private java.util.Date compOffDate ;
private Position approverPos;

/*public Integer getId() {
	return id;
}*/



public org.egov.lib.rjbac.user.User getApprovedBy() {
	return approvedBy;
}
public void setApprovedBy(org.egov.lib.rjbac.user.User approvedBy) {   
	this.approvedBy = approvedBy;
}
public java.util.Date getCompOffDate() {
	return compOffDate;
}
public void setCompOffDate(java.util.Date compOffDate) {
	this.compOffDate = compOffDate;
}
public StatusMaster getStatus() {
	return status;
}
public void setStatus(StatusMaster status) { 
	this.status = status;
}
public org.egov.lib.rjbac.user.User getCreatedBy() {
	return createdBy;
}
public void setCreatedBy(org.egov.lib.rjbac.user.User createdBy) {
	this.createdBy = createdBy;
}
public org.egov.pims.empLeave.model.Attendence getAttObj() {
	return attObj;
}
public void setAttObj(org.egov.pims.empLeave.model.Attendence attObj) {
	this.attObj = attObj;
}
@Override
public String getStateDetails() {
	String compoffdate="no dateAvail";
	if(compOffDate!=null)
		compoffdate=new SimpleDateFormat("dd/MM/yyyy").format(compOffDate);
	return compoffdate;
}
/*
 * getting position in case of Manual workflow
 */
public Position getApproverPos() {
	return approverPos;
}
/*
 * setting position in case of Manual workflow
 */
public void setApproverPos(Position approverPos) {
	this.approverPos = approverPos;
}
}
