package org.egov.works.models.revisionEstimate;

import java.util.ArrayList;
import java.util.List;

import org.egov.works.models.estimate.AbstractEstimate;

public class RevisionAbstractEstimate extends AbstractEstimate {

	
	private static final long serialVersionUID = 1L;
	private List<String> revisionEstActions = new ArrayList<String>();
	@Override
	public String getStateDetails() {
		return "Revision Estimate : " + getEstimateNumber();
	}
	public List<String> getRevisionEstActions() {
		return revisionEstActions;
	}
	public void setRevisionEstActions(List<String> revisionEstActions) {
		this.revisionEstActions = revisionEstActions;
	}

}