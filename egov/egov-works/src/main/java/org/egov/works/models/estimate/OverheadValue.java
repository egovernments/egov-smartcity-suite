package org.egov.works.models.estimate;

import java.util.Date;

import org.egov.commons.Period;
import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.Money;
import org.egov.works.models.masters.Overhead;
import org.egov.works.models.masters.OverheadRate;
import org.joda.time.LocalDate;

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

	public OverheadRate getOverheadRateOn(Date estimateDate) {
		
		for (OverheadRate overheadRate : overhead.getOverheadRates()) {
			if (overheadRate != null && isWithin(overheadRate.getValidity(), estimateDate)) {
				return overheadRate;
			}
		}

		return null;
	}
	public boolean isWithin(Period period, Date dateTime) {
		LocalDate start = new LocalDate(period.getStartDate());
		LocalDate end = new LocalDate(period.getEndDate());
		LocalDate date = new LocalDate(dateTime);
		if (period.getEndDate() == null) {
			return start.compareTo(date) <= 0;
		} else {
			return start.compareTo(date) <= 0 && end.compareTo(date) >= 0;
		}
	}
	

}
