package org.egov.works.services;

import org.egov.infstr.services.PersistenceService;
import org.egov.works.models.revisionEstimate.RevisionAbstractEstimate;
import org.egov.works.models.revisionEstimate.RevisionWorkOrder;

public class RevisionWorkOrderWFService extends PersistenceService<RevisionWorkOrder,Long>{ 
	
	private AbstractEstimateService abstractEstimateService;
	public RevisionWorkOrderWFService(){
		setType(RevisionWorkOrder.class);
	}
	public AbstractEstimateService getAbstractEstimateService() {
		return abstractEstimateService;
	}
	public void setAbstractEstimateService(
			AbstractEstimateService abstractEstimateService) {
		this.abstractEstimateService = abstractEstimateService;
	}
	
}