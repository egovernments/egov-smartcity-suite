package org.egov.works.models.revisionEstimate;

import org.egov.works.models.workorder.WorkOrder;

public class RevisionWorkOrder extends WorkOrder{
	
	private static final long serialVersionUID = 1L;
	private RevisionWOCreationType creationType;
	@Override
	public String getStateDetails() {
		return "Revision WorkOrder : " + getWorkOrderNumber();
	}
	public RevisionWOCreationType getCreationType() {
		return creationType;
	}
	public void setCreationType(RevisionWOCreationType creationType) {
		this.creationType = creationType;
	}
}
