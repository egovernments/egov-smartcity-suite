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
package org.egov.infstr.client.administration.rjbac.role;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.egov.infstr.client.EgovActionForm;

public class RoleForm extends EgovActionForm implements Serializable {

	private static final long serialVersionUID = 1L;
	private String roleName = "";
	private String roleDesc = "";
	private Integer roleId = null;
	private String roleNameLocal = "";
	private String roleDescLocal = "";

	/**
	 * @return Returns the roleDescLocal.
	 */
	public String getRoleDescLocal() {
		return this.roleDescLocal;
	}

	/**
	 * @param roleDescLocal The roleDescLocal to set.
	 */
	public void setRoleDescLocal(final String roleDescLocal) {
		this.roleDescLocal = roleDescLocal;
	}

	/**
	 * @return Returns the roleNameLocal.
	 */
	public String getRoleNameLocal() {
		return this.roleNameLocal;
	}

	/**
	 * @param roleNameLocal The roleNameLocal to set.
	 */
	public void setRoleNameLocal(final String roleNameLocal) {
		this.roleNameLocal = roleNameLocal;
	}

	/**
	 * @return Returns the roleName.
	 */
	public String getRoleName() {
		return this.roleName;
	}

	/**
	 * @param roleName The roleName to set.
	 */
	public void setRoleName(final String roleName) {
		this.roleName = roleName;
	}

	/**
	 * @return Returns the roleDesc.
	 */
	public String getRoleDesc() {
		return this.roleDesc;
	}

	/**
	 * @param roleDesc The roleDesc to set.
	 */
	public void setRoleDesc(final String roleDesc) {
		this.roleDesc = roleDesc;
	}

	/**
	 * @return Returns the roleId.
	 */
	public Integer getRoleId() {
		return this.roleId;
	}

	/**
	 * @param roleId The roleId to set.
	 */
	public void setRoleId(final Integer roleId) {
		this.roleId = roleId;
	}

	/**
	 * resets properties of this form
	 */
	@Override
	public void reset(final ActionMapping mapping, final HttpServletRequest req) {
		this.roleName = "";
		this.roleDesc = "";
		this.roleId = null;
		this.roleDescLocal = "";
		this.roleNameLocal = "";

	}

}
