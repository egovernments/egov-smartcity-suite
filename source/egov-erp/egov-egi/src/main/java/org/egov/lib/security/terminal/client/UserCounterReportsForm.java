/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.lib.security.terminal.client;

import org.apache.struts.action.ActionForm;

public class UserCounterReportsForm extends ActionForm {

	private static final long serialVersionUID = 1L;
	private String isLoc;
	private String fromDate;
	private String toDate;

	/**
	 * @return the fromDate
	 */
	public String getFromDate() {
		return this.fromDate;
	}

	/**
	 * @param fromDate the fromDate to set
	 */
	public void setFromDate(final String fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * @return the isLoc
	 */
	public String getIsLoc() {
		return this.isLoc;
	}

	/**
	 * @param isLoc the isLoc to set
	 */
	public void setIsLoc(final String isLoc) {
		this.isLoc = isLoc;
	}

	/**
	 * @return the toDate
	 */
	public String getToDate() {
		return this.toDate;
	}

	/**
	 * @param toDate the toDate to set
	 */
	public void setToDate(final String toDate) {
		this.toDate = toDate;
	}

}
