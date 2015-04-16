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
package org.egov.infstr.client.administration.rjbac.dept;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.egov.infstr.client.EgovActionForm;

public class DepartmentForm extends EgovActionForm implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String deptid = "";
	private String deptName = "";
	private String deptDetails = "";
	private String deptCode = "";
	private String billingLocation = "";
	private String deptAddress = "";

	/**
	 * @return Returns the deptCode.
	 */
	public String getCode() {
		return this.deptCode;
	}

	/**
	 * @param deptCode The deptCode to set.
	 */
	public void setDeptCode(final String deptCode) {
		this.deptCode = deptCode;
	}

	/**
	 * @return Returns the deptid.
	 */
	public String getDeptid() {
		return this.deptid;
	}

	/**
	 * @param deptid The deptid to set.
	 */
	public void setDeptid(final String deptid) {
		this.deptid = deptid;
	}

	/**
	 * @return Returns the deptDetails.
	 */
	public String getDeptDetails() {
		return this.deptDetails;
	}

	/**
	 * @param deptDetails The deptDetails to set.
	 */

	public void setDeptDetails(final String deptDetails) {
		this.deptDetails = deptDetails;
	}

	/**
	 * @return Returns the deptName.
	 */
	public String getName() {
		return this.deptName;
	}

	/**
	 * @param deptName The deptName to set.
	 */
	public void setDeptName(final String deptName) {
		this.deptName = deptName;
	}

	/**
	 * @return Returns the deptAddress.
	 */
	public String getDeptAddress() {
		return this.deptAddress;
	}

	/**
	 * @param deptAddress The deptAddress to set.
	 */
	public void setDeptAddress(final String deptAddress) {
		this.deptAddress = deptAddress;
	}

	/**
	 * @return Returns the billingLocation.
	 */
	public String getBillingLocation() {
		return this.billingLocation;
	}

	/**
	 * @param billingLocation The billingLocation to set.
	 */
	public void setBillingLocation(final String billingLocation) {
		this.billingLocation = billingLocation;
	}

	/**
	 * resets the properties of the form
	 */
	@Override
	public void reset(final ActionMapping mapping, final HttpServletRequest req) {
		this.deptid = "";
		this.deptName = "";
		this.deptDetails = "";
		this.deptCode = "";
		// this.parentId = "";
		// this.isLeaf = "";
		this.billingLocation = "";
		this.deptAddress = "";
	}

}
