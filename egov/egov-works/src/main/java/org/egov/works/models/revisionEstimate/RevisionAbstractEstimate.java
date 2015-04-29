package org.egov.works.models.revisionEstimate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.egov.works.models.estimate.AbstractEstimate;
import org.egov.works.models.estimate.Activity;

public class RevisionAbstractEstimate extends AbstractEstimate {

	private static final long serialVersionUID = 1L;
	private List<String> revisionEstActions = new ArrayList<String>();
	private String additionalRule;
	private BigDecimal amountRule;

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

	public void deleteNonSORActivities() {
		if (getActivities() != null && getActivities().size() > 0) {
			for (Activity estActivity : getActivities()) {
				if (estActivity.getNonSor() != null)
					estActivity.setNonSor(null);
			}
		}
	}

	public String getAdditionalRule() {
		return additionalRule;
	}

	public BigDecimal getAmountRule() {
		return amountRule;
	}

	public void setAdditionalRule(String additionalRule) {
		this.additionalRule = additionalRule;
	}

	public void setAmountRule(BigDecimal amountRule) {
		this.amountRule = amountRule;
	}

}