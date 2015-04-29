package org.egov.works.models.estimate;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.egov.commons.EgwStatus;
import org.egov.commons.utils.EntityType;
import org.egov.infstr.models.BaseModel;
import org.hibernate.validator.constraints.Length;

public class ProjectCode extends BaseModel implements EntityType  {

	private String code;
	private Set<AbstractEstimate> estimates = new HashSet<AbstractEstimate>();
	private Boolean isActive;
	@Length(max=1024,message="projectCode.description.length")
	private String description;
	//@Required(message="projectCode.name.null")
	@Length(max=1024,message="projectCode.name.length")
	private String codeName;
	private EgwStatus egwStatus;
	private Double projectValue;
	private Date completionDate;
	private boolean isFinalBill;
	
	public ProjectCode(){
           }
	public ProjectCode(AbstractEstimate abstractEstimate, String code){
		this.estimates.add(abstractEstimate);
		this.code=code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public void addEstimate(AbstractEstimate estimate){
		this.estimates.add(estimate);
	}
	
	public Set<AbstractEstimate> getEstimates() {
		return estimates;
	}
	public void setEstimates(Set<AbstractEstimate> estimates) {
		this.estimates = estimates;
	}

	public String getBankaccount() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getBankname() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getIfsccode() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getModeofpay() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return codeName;
	}

	public String getPanno() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTinno() {
		// TODO Auto-generated method stub
		return null;
	}

	public Integer getEntityId() {
		return Integer.valueOf(id.intValue());
	}
	
	@Override
	public String getEntityDescription() {
		return description;
	}
	/*public AbstractEstimate getAbstractEstimate() {
		return abstractEstimate;
	}

	public void setAbstractEstimate(AbstractEstimate abstractEstimate) {
		this.abstractEstimate = abstractEstimate;
	}*/
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCodeName() {
		return codeName;
	}
	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}
	public EgwStatus getEgwStatus() {
		return egwStatus;
	}
	public void setEgwStatus(EgwStatus egwStatus) {
		this.egwStatus = egwStatus;
	}
	public Double getProjectValue() {
		return projectValue;
	}
	public void setProjectValue(Double projectValue) {
		this.projectValue = projectValue;
	}
	public Date getCompletionDate() {
		return completionDate;
	}
	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}
	
	public boolean getIsFinalBill() {
		return isFinalBill;
	}
	public void setIsFinalBill(boolean isFinalBill) {
		this.isFinalBill = isFinalBill;
	}
	
}
