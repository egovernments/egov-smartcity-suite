package org.egov.works.services;

import java.util.Collection;
import java.util.List;

import org.egov.commons.CFinancialYear;
import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.tender.EstimateLineItemsForWP;
import org.egov.works.models.tender.WorksPackage;

public interface WorksPackageService extends BaseService<WorksPackage,Long> {
	public void setWorksPackageNumber(WorksPackage entity,CFinancialYear finYear);
	public List<AbstractEstimate> getAbStractEstimateListByWorksPackage(WorksPackage entity);
	public Collection<EstimateLineItemsForWP> getActivitiesForEstimate(WorksPackage wpObj);
	public double getTotalAmount(Collection<EstimateLineItemsForWP> actList);
}
