/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */

package org.egov.infstr.utils;

import java.sql.Date;

public class FinancialYearImpl implements FinancialYear {

	private transient final Date startOnDate;
	private transient final Date endOnDate;
	private transient final Integer currentYear;

	/**
	 * Instantiates a new financial year impl.
	 * @param startOnDate the start on date
	 * @param endOnDate the end on on date
	 * @param currentYear the current year
	 */
	public FinancialYearImpl(final Date startOnDate, final Date endOnDate, final Integer currentYear) {
		this.startOnDate = startOnDate;
		this.endOnDate = endOnDate;
		this.currentYear = currentYear;
	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.infstr.utils.FinancialYear#getCurrentYear()
	 */
	@Override
	public Integer getCurrentYear() {
		return this.currentYear;
	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.infstr.utils.FinancialYear#getEndOnOnDate()
	 */
	@Override
	public Date getEndOnOnDate() {
		return this.endOnDate;
	}

	/*
	 * (non-Javadoc)
	 * @see org.egov.infstr.utils.FinancialYear#getStartOnDate()
	 */
	@Override
	public Date getStartOnDate() {
		return this.startOnDate;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder strBuf = new StringBuilder();
		strBuf.append("startOnDate:").append(this.startOnDate + ",");
		strBuf.append("endOnOnDate:").append(this.endOnDate + ",");
		strBuf.append("currentYear:").append(this.currentYear + ".");
		return strBuf.toString();
	}
}
