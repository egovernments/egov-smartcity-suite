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
package org.egov.infstr.commons;

import java.util.Date;
import java.util.Set;

import org.egov.exceptions.EGOVRuntimeException;
 
public class Module {

	private Integer id;
	private String moduleName;
	private Date lastUpdatedTimeStamp;
	private Boolean isEnabled = false;
	private String moduleNameLocal;
	private String baseUrl;
	private Module parent;
	private String moduleDescription;
	private Integer orderNumber;
	private Set actions;
	private String contextRoot;

	/**
	 * @return the contextRoot
	 */
	public String getContextRoot() {
		return contextRoot;
	}

	/**
	 * @param contextRoot the contextRoot to set
	 */
	public void setContextRoot(String contextRoot) {
		this.contextRoot = contextRoot;
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
	 * @return Returns the moduleName.
	 */
	public String getModuleName() {
		return moduleName;
	}

	/**
	 * @param moduleName The moduleName to set.
	 */
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	/**
	 * @return Returns the lastUpdatedTimeStamp.
	 */
	public Date getLastUpdatedTimeStamp() {
		return lastUpdatedTimeStamp;
	}

	/**
	 * @param lastUpdatedTimeStamp The lastUpdatedTimeStamp to set.
	 */
	public void setLastUpdatedTimeStamp(Date lastUpdatedTimeStamp) {
		this.lastUpdatedTimeStamp = lastUpdatedTimeStamp;
	}

	/**
	 * @return Returns the baseUrl.
	 */
	public String getBaseUrl() {
		return baseUrl;
	}

	/**
	 * @param baseUrl The baseUrl to set.
	 */
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	/**
	 * @return Returns the enabled.
	 */
	public Boolean getIsEnabled() {
		if (isEnabled == null)
			isEnabled = false;
		return isEnabled;
	}

	/**
	 * @param enabled The enabled to set.
	 */
	public void setIsEnabled(Boolean enabled) {
		this.isEnabled = enabled;
	}

	/**
	 * @return Returns the moduleNameLocal.
	 */
	public String getModuleNameLocal() {
		return moduleNameLocal;
	}

	/**
	 * @param moduleNameLocal The moduleNameLocal to set.
	 */
	public void setModuleNameLocal(String moduleNameLocal) {
		this.moduleNameLocal = moduleNameLocal;
	}

	/**
	 * @return Returns the parent.
	 */
	public Module getParent() {
		return parent;
	}

	/**
	 * @param parent The parent to set.
	 */
	public void setParent(Module parent) {
		this.parent = parent;
	}

	/**
	 * @return Returns the moduleDescription.
	 */
	public String getModuleDescription() {
		return moduleDescription;
	}

	/**
	 * @param moduleDescription The moduleDescription to set.
	 */
	public void setModuleDescription(String moduleDescription) {
		this.moduleDescription = moduleDescription;
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
	 * @return Returns the actions.
	 */
	public Set getActions() {
		return actions;
	}

	/**
	 * @param actions The actions to set.
	 */
	public void setActions(Set actions) {
		this.actions = actions;
	}

	public Boolean validate() {
		if (this.moduleName == null || this.moduleName.trim().equals(""))
			throw new EGOVRuntimeException("Module name is not specified.");
		else
			return true;
	}

	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof Module))
			return false;
		Module mod = (Module) obj;

		if (this == mod)
			return true;

		if (this.getId() != null && mod.getId() != null) {
			if (this.getId().equals(mod.getId()))
				return true;
			else
				return false;

		}

		if (this.moduleName == null || mod.moduleName == null)
			return false;

		if (this.moduleName.trim().equalsIgnoreCase(mod.getModuleName())) {
			return true;
		} else
			return false;
	}

	public int hashCode() {
		int hashcode = 0;
		if (this.moduleName != null)
			hashcode = this.moduleName.hashCode();

		return hashcode;
	}

}
