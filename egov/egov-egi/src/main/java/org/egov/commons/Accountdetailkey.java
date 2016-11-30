/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.commons;

public class Accountdetailkey implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer groupid;
	private String detailname;
	private Integer detailkey;
	private Accountdetailtype accountdetailtype;

	/**
	 * @return Returns the detailkey.
	 */
	public Integer getDetailkey() {
		return detailkey;
	}

	/**
	 * @param detailkey The detailkey to set.
	 */
	public void setDetailkey(Integer detailkey) {
		this.detailkey = detailkey;
	}

	/**
	 * @return Returns the detailname.
	 */
	public String getDetailname() {
		return detailname;
	}

	/**
	 * @param detailname The detailname to set.
	 */
	public void setDetailname(String detailname) {
		this.detailname = detailname;
	}

	/**
	 * @return Returns the groupid.
	 */
	public Integer getGroupid() {
		return groupid;
	}

	/**
	 * @param groupid The groupid to set.
	 */
	public void setGroupid(Integer groupid) {
		this.groupid = groupid;
	}

	/**
	 * @return Returns the id.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return Returns the accountdetailtype.
	 */
	public Accountdetailtype getAccountdetailtype() {
		return accountdetailtype;
	}

	/**
	 * @param accountdetailtype The accountdetailtype to set.
	 */
	public void setAccountdetailtype(Accountdetailtype accountdetailtype) {
		this.accountdetailtype = accountdetailtype;
	}
}
