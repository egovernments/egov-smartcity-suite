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
/*
 * Created on Nov 14, 2006

 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.egov.ptis.domain.entity.property;

import java.util.Date;

/**
 * @author Gayathri
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class OnlineWards {
	private Integer wardId;
	private Date onlineDate;
	private Integer wardNum;
	private Integer id;

	/**
	 * @return Returns the bndryId.
	 */

	/**
	 * @return Returns the onlineDate.
	 */
	public Date getOnlineDate() {
		return onlineDate;
	}

	/**
	 * @param onlineDate
	 *            The onlineDate to set.
	 */
	public void setOnlineDate(Date onlineDate) {
		this.onlineDate = onlineDate;
	}

	/**
	 * @return Returns the wardNum.
	 */
	public Integer getWardNum() {
		return wardNum;
	}

	/**
	 * @param wardNum
	 *            The wardNum to set.
	 */
	public void setWardNum(Integer wardNum) {
		this.wardNum = wardNum;
	}

	/**
	 * @return Returns the id.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return Returns the wardid.
	 */
	public Integer getWardId() {
		return wardId;
	}

	/**
	 * @param wardid
	 *            The wardid to set.
	 */
	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	@Override
	public String toString() {
		StringBuilder objStr = new StringBuilder();

		objStr.append("Id: ").append(getId()).append("|WardId: ").append(getWardId()).append("|WardNum:").append(
				getWardNum()).append("|OnlineDate: ").append(getOnlineDate());

		return objStr.toString();
	}
}
