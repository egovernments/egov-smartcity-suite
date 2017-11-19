/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */

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
