package org.egov.works.models.revisionEstimate;

import org.egov.works.models.workorder.WorkOrder;

public class RevisionWorkOrder extends WorkOrder{
	
	private static final long serialVersionUID = 1L;
	@Override
	public String getStateDetails() {
		return "Revision WorkOrder : " + getWorkOrderNumber();
	}
}
