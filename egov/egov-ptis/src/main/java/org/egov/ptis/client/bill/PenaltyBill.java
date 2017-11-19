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
package org.egov.ptis.client.bill;

import java.util.Date;

public class PenaltyBill implements Comparable<PenaltyBill> {
	private Date createdDate;
	private Date occupancyDate;
	private Date billDate;
	private boolean isBillGeneratedAfterRollover;

	public PenaltyBill(Date createdDate, Date occupancyDate, Date billDate,
			boolean isBillGeneratedAfterRollover) {
		this.createdDate = createdDate;
		this.occupancyDate = occupancyDate;
		this.billDate = billDate;
		this.isBillGeneratedAfterRollover = isBillGeneratedAfterRollover;
	}

	@Override
	public String toString() {
		return new StringBuilder(200).append("PenaltyBill [").append("createdDate=")
				.append(createdDate).append(", occupancyDate=").append(occupancyDate)
				.append(", billDate=").append(billDate).append(", isBillGeneratedAfterRollover=")
				.append(isBillGeneratedAfterRollover).append("] ").toString();
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getOccupancyDate() {
		return occupancyDate;
	}

	public void setOccupancyDate(Date occupancyDate) {
		this.occupancyDate = occupancyDate;
	}

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public boolean getIsBillGeneratedAfterRollover() {
		return isBillGeneratedAfterRollover;
	}

	public void setIsBillGeneratedAfterRollover(boolean isBillGeneratedAfterRollover) {
		this.isBillGeneratedAfterRollover = isBillGeneratedAfterRollover;
	}

	@Override
	public int compareTo(PenaltyBill o) {
		return this.billDate.compareTo(o.getBillDate());
	}
}
