package org.egov.commons.entity;

import org.egov.commons.Installment;
import org.egov.infra.admin.master.entity.Module;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Ramki
 */
public class InstallmentBuilder {
	private final Installment installment;

	public InstallmentBuilder() {
		installment = new Installment();
	}

	public Installment build() {
		return installment;
	}

	public InstallmentBuilder withModule(final Module module) {
		installment.setModule(module);
		return this;
	}

	public InstallmentBuilder withDescription(final String description) {
		installment.setDescription(description);
		return this;
	}

	public InstallmentBuilder withFromDate(final Date fromDate) {
		installment.setFromDate(fromDate);
		return this;
	}

	public InstallmentBuilder withToDate(final Date toDate) {
		installment.setToDate(toDate);
		return this;
	}

	public InstallmentBuilder withCurrentHalfPeriod(Module module) {
		Calendar today = Calendar.getInstance();
		Calendar fromDate = Calendar.getInstance();
		Calendar toDate = Calendar.getInstance();
		int month = today.get(Calendar.MONTH) + 1;
		int year = today.get(Calendar.YEAR);

		fromDate.set(Calendar.HOUR_OF_DAY, 0);
		fromDate.set(Calendar.MINUTE, 0);
		fromDate.set(Calendar.SECOND, 0);

		toDate.set(Calendar.HOUR_OF_DAY, 23);
		toDate.set(Calendar.MINUTE, 59);
		toDate.set(Calendar.SECOND, 59);

		if (month >= 4 && month <= 9) {
			fromDate.set(Calendar.DATE, 1);
			fromDate.set(Calendar.MONTH, 3);
			fromDate.set(Calendar.YEAR, year);

			toDate.set(Calendar.DATE, 30);
			toDate.set(Calendar.MONTH, 8);
			toDate.set(Calendar.YEAR, year);

		} else {
			fromDate.set(Calendar.DATE, 1);
			fromDate.set(Calendar.MONTH, 9);
			fromDate.set(Calendar.YEAR, year-1);

			toDate.set(Calendar.DATE, 31);
			toDate.set(Calendar.MONTH, 2);
			toDate.set(Calendar.YEAR, year);
		}

		withFromDate(fromDate.getTime());
		withToDate(toDate.getTime());
		withModule(module);
		return this;
	}
}
