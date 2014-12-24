package org.egov.works.models.tender;

import org.egov.infstr.models.BaseModel;
import org.egov.works.models.estimate.AbstractEstimate;

public class WorksPackageDetails extends BaseModel{
	
	private WorksPackage worksPackage;
	private AbstractEstimate estimate;
	
	public WorksPackage getWorksPackage() {
		return worksPackage;
	}
	public void setWorksPackage(WorksPackage worksPackage) {
		this.worksPackage = worksPackage;
	}
	public AbstractEstimate getEstimate() {
		return estimate;
	}
	public void setEstimate(AbstractEstimate estimate) {
		this.estimate = estimate;
	}
}
