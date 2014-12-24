package org.egov.works.models.estimate;

import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.Money;
import org.egov.works.models.masters.Overhead;

public class OverheadValue extends BaseModel {
	private Overhead overhead;
	private Money amount;
	private AbstractEstimate abstractEstimate;
	public AbstractEstimate getAbstractEstimate() {
		return abstractEstimate;
	}
	public void setAbstractEstimate(AbstractEstimate abstractEstimate) {
		this.abstractEstimate = abstractEstimate;
	}
	public OverheadValue() {
	}
	public OverheadValue(Money amount, Overhead overhead) {
		super();
		this.amount = amount;
		this.overhead = overhead;
	}
	public Overhead getOverhead() {
		return overhead;
	}
	public void setOverhead(Overhead overhead) {
		this.overhead = overhead;
	}
	public Money getAmount() {
		return amount;
	}
	public void setAmount(Money amount) {
		this.amount = amount;
	}
}
