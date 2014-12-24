package org.egov.pims.model;

import java.util.Date;

import org.egov.commons.Bankbranch;
import org.egov.commons.DisbursementMode;
import org.egov.commons.EgwStatus;
import org.egov.commons.utils.EntityType;
import org.egov.infstr.models.BaseModel;




public class EmployeeNomineeMaster extends BaseModel implements java.io.Serializable,EntityType 
{
 
  private PersonalInformation employeeId;
  
  private String nomineeName;
  
  private int nomineeAge;
  
  private Date nomineeDob;
  
  private Integer maritalStatus;
  
  private Integer isActive;
  private Bankbranch bankBranch;
  
  private EisRelationType relationType;
  private String accountNumber;
  
  private Integer isWorking;
  
  private String guardianName;
  
  private String guardianRelationship;
  
  private String nomineeAddress;
  
  private DisbursementMode disburseType;
  
public DisbursementMode getDisburseType() {
	return disburseType;
}

public void setDisburseType(DisbursementMode disburseType) {
	this.disburseType = disburseType;
}

public String getNomineeAddress() {
	return nomineeAddress;
}

public void setNomineeAddress(String nomineeAddress) {
	this.nomineeAddress = nomineeAddress;
}

public String getGuardianName() {
	return guardianName;
}

public void setGuardianName(String guardianName) {
	this.guardianName = guardianName;
}

public String getGuardianRelationship() {
	return guardianRelationship;
}

public void setGuardianRelationship(String guardianRelationship) {
	this.guardianRelationship = guardianRelationship;
}

public EmployeeNomineeMaster()
  {
	  
  }

public PersonalInformation getEmployeeId() {
	return employeeId;
}
public void setEmployeeId(PersonalInformation employeeId) {
	this.employeeId = employeeId;
}
public String getNomineeName() {
	return nomineeName;
}
public void setNomineeName(String nomineeName) {
	this.nomineeName = nomineeName;
}
public int getNomineeAge() {
	return nomineeAge;
}
public void setNomineeAge(int nomineeAge) {
	this.nomineeAge = nomineeAge;
}
public Integer getMaritalStatus() {
	return maritalStatus;
}
public void setMaritalStatus(Integer maritalStatus) {
	this.maritalStatus = maritalStatus;
}
public Integer getIsActive() {
	return isActive;
}
public void setIsActive(Integer isActive) {
	this.isActive = isActive;
}
public Bankbranch getBankBranch() {
	return bankBranch;
}
public void setBankBranch(Bankbranch bankBranch) {
	this.bankBranch = bankBranch;
}

public String getAccountNumber() {
	return accountNumber;
}
public void setAccountNumber(String accountNumber) {
	this.accountNumber = accountNumber;
}
public EisRelationType getRelationType() {
	return relationType;
}
public void setRelationType(EisRelationType relationType) {
	this.relationType = relationType;
}
public Integer getIsWorking() {
	return isWorking;
}
public void setIsWorking(Integer isWorking) {
	this.isWorking = isWorking;
}

public Date getNomineeDob() {
	return nomineeDob;
}
public void setNomineeDob(Date nomineeDob) {
	this.nomineeDob = nomineeDob;
}

@Override
public String getBankaccount() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getBankname() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getCode() {	
	return employeeId.getCode();
}

@Override
public String getEntityDescription() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public Integer getEntityId() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getIfsccode() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getModeofpay() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getName() {
	return nomineeName;
}

@Override
public String getPanno() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public String getTinno() {
	// TODO Auto-generated method stub
	return null;
}

}
