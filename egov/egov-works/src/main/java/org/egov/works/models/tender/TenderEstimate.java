package org.egov.works.models.tender;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infstr.models.BaseModel;
import org.egov.works.models.estimate.AbstractEstimate;

public class TenderEstimate extends BaseModel{
	
	private AbstractEstimate abstractEstimate;
	
	@Valid
	private TenderHeader tenderHeader;
	
	private WorksPackage worksPackage;
		
	private Set<TenderResponse> tenderResponseSet = new HashSet<TenderResponse>();
	
	@Required(message="tenderEstimate.tenderType.null")
	private String tenderType;

	public AbstractEstimate getAbstractEstimate() {
		return abstractEstimate;
	}

	public void setAbstractEstimate(AbstractEstimate abstractEstimate) {
		this.abstractEstimate = abstractEstimate;
	}

	public TenderHeader getTenderHeader() {
		return tenderHeader;
	}

	public void setTenderHeader(TenderHeader tenderHeader) {
		this.tenderHeader = tenderHeader;
	}

	public String getTenderType() {
		return tenderType;
	}

	public void setTenderType(String tenderType) {
		this.tenderType = tenderType;
	}

	public WorksPackage getWorksPackage() {
		return worksPackage;
	}

	public void setWorksPackage(WorksPackage worksPackage) {
		this.worksPackage = worksPackage;
	}

	public Set<TenderResponse> getTenderResponseSet() {
		return tenderResponseSet;
	}

	public void setTenderResponseSet(Set<TenderResponse> tenderResponseSet) {
		this.tenderResponseSet = tenderResponseSet;
	}
	
	/*public List<ValidationError> validate()	{
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		if(tenderType!=null && tenderType.equals("")){
			validationErrors.add(new ValidationError("tenderType", "tenderEstimate.tenderType.null"));
		}
		return validationErrors;
	}*/
}
