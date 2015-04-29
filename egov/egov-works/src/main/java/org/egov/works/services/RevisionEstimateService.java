package org.egov.works.services;

import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.revisionEstimate.RevisionAbstractEstimate;


/**
 * This class will have all business logic related to Revision Estimate.
 * @author Julian
 *
 */

public interface RevisionEstimateService extends BaseService<RevisionAbstractEstimate,Long> {

	public void consumeBudget( RevisionAbstractEstimate revisionEstimate) ;
	public boolean isDepositWorksType(AbstractEstimate estimate);
	public void releaseBudget( RevisionAbstractEstimate revisionEstimate);
	boolean getShowBudgetFolio(AbstractEstimate revisionEstimate);
	boolean getShowDepositFolio(AbstractEstimate revisionEstimate);
	
}
