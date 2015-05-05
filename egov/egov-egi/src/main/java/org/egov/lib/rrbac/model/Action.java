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
package org.egov.lib.rrbac.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.egov.exceptions.RBACException;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infstr.commons.Module;

public class Action implements Comparable<Action> {

	private Integer id;
	private String name;
	private String url;
	private String queryParams;
	private Integer urlOrderId;
	private Date updatedTime;
	private Set roles = new HashSet();
	private Module module;
	private Integer orderNumber;
	private String displayName;
	private Integer isEnabled;
	private String helpURL;
	private String contextRoot;

	/**
	 * @return Returns the helpURL.
	 */
	public String getHelpURL() {
		return helpURL;
	}

	/**
	 * @param helpURL The helpURL to set.
	 */
	public void setHelpURL(String helpURL) {
		this.helpURL = helpURL;
	}

	/**
	 * @return Returns the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id The id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return Returns the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the url.
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url The url to set.
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return Returns the queryParams.
	 */
	public String getQueryParams() {
		return queryParams;
	}

	/**
	 * @param queryParams The queryParams to set.
	 */
	public void setQueryParams(String queryParams) {
		this.queryParams = queryParams;
	}

	/**
	 * @return Returns the urlOrderId.
	 */
	public Integer getUrlOrderId() {
		return urlOrderId;
	}

	/**
	 * @param urlOrderId The urlOrderId to set.
	 */
	public void setUrlOrderId(Integer urlOrderId) {
		this.urlOrderId = urlOrderId;
	}

	/**
	 * @return Returns the UpdatedTime.
	 */
	public Date getUpdatedTime() {
		return updatedTime;
	}

	/**
	 * @param UpdatedTime The UpdatedTime to set.
	 */
	public void setUpdatedTime(Date updatedtime) {
		this.updatedTime = updatedtime;
	}


	/**
	 * @return Returns collections of roles associated with the action.
	 */
	public Set getRoles() {
		return roles;
	}

	/**
	 * @param roles The roles to set
	 */
	public void setRoles(Set roles) {
		this.roles = roles;
	}
	// This is commented while rewriting role master screen
        // code must be corrected while rewriting this screen
	/**
	 * @param role The roles to add
	 */
	/*public void addRole(Role role) {
		getRoles().add(role);
		role.addAction(this);
	}
        // This is commented while rewriting role master screen
        // code must be corrected while rewriting this screen
	*//**
	 * @param role The roles to Remove
	 *//*
	public void removeRole(Role role) {

		role.removeAction(this);
		if (getRoles().contains(role))
			getRoles().remove(role);

	}*/

	
	/**
	 * @return Returns the displayName.
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName The displayName to set.
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return Returns the module.
	 */
	public Module getModule() {
		return module;
	}

	/**
	 * @param module The module to set.
	 */
	public void setModule(Module module) {
		this.module = module;
	}

	/**
	 * @return Returns the orderNumber.
	 */
	public Integer getOrderNumber() {
		return orderNumber;
	}

	/**
	 * @param orderNumber The orderNumber to set.
	 */
	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}

	/**
	 * @return Returns the isEnabled.
	 */
	public Integer getIsEnabled() {
		return isEnabled;
	}

	/**
	 * @param isEnabled The isEnabled to set.
	 */
	public void setIsEnabled(Integer isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getContextRoot() {
		return contextRoot;
	}

	public void setContextRoot(String contextRoot) {
		this.contextRoot = contextRoot;
	}

	/**
	 * @return Returns if the given Object is equal to Action
	 */
	public boolean equals(Object obj) {

		if (obj == null)
			return false;

		if (this == obj)
			return true;

		if (!(obj instanceof Action))
			return false;

		final Action other = (Action) obj;

		if (getName().equals(other.getName())) {
			return true;
		} else
			return false;
	}

	/**
	 * @return Returns the hashCode
	 */
	public int hashCode() {
		int hashCode = 0;
		hashCode = hashCode + this.getName().hashCode();

		return hashCode;
	}

	public int compareTo(Action action) throws ClassCastException {
		return (action == null || action.getName() == null) ? -1 : this.name.compareTo(action.getName());
	}


	/**
	 * @return Returns true if action is valid for role
	 */
	public boolean isActionValid(Role role) throws RBACException {
		if (this.roles.contains(role))
			return true;
		else {
			throw new RBACException("Unauthorized to Perform this Action ");
		}
	}

	@Override
	public String toString() {
		StringBuffer actionString = new StringBuffer();
		actionString.append("Action: ( ").append("ID: " + id).append(" name: " + name).append(" URl : " + url).append(" queryparams: " + queryParams + " )");
		return actionString.toString();
	}
}