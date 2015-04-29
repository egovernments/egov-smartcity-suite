package org.egov.works.services;

import org.egov.infstr.services.PersistenceService;
import org.egov.works.models.revisionEstimate.RevisionAbstractEstimate;

public class RevisionEstimateWFService extends PersistenceService<RevisionAbstractEstimate,Long>{
	
	private AbstractEstimateService abstractEstimateService;
	private RevisionEstimateService revisionEstimateService;
	
	public RevisionEstimateWFService(){
		setType(RevisionAbstractEstimate.class);
	}
	public AbstractEstimateService getAbstractEstimateService() {
		return abstractEstimateService;
	}
	public void setAbstractEstimateService(
			AbstractEstimateService abstractEstimateService) {
		this.abstractEstimateService = abstractEstimateService;
	}
	public RevisionEstimateService getRevisionEstimateService() {
		return revisionEstimateService;
	}
	public void setRevisionEstimateService(
			RevisionEstimateService revisionEstimateService) {
		this.revisionEstimateService = revisionEstimateService;
	}
	
}