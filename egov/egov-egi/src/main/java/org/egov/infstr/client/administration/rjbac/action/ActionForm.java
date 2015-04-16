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
package org.egov.infstr.client.administration.rjbac.action;

import java.io.Serializable;

import org.egov.infstr.client.EgovActionForm;

public class ActionForm extends EgovActionForm implements Serializable {
	
	private static final long serialVersionUID = 1L;
	String moduleId;
	String moduleName;
	String[] actionId;
	String[] actionName;
	String[] baseURL;
	String[] queryParams;
	String[] urlOrderId;
	String[] isEnabled;
	String[] orderNumber;
	String[] displayName;
	String[] helpURL;
	String[] deleteActionSet;

	/**
	 * @return Returns the helpURL.
	 */
	public String[] getHelpURL() {
		return this.helpURL;
	}

	/**
	 * @param helpURL The helpURL to set.
	 */
	public void setHelpURL(final String[] helpURL) {
		this.helpURL = helpURL;
	}

	/**
	 * @return Returns the moduleName.
	 */
	public String getModuleName() {
		return this.moduleName;
	}

	/**
	 * @param moduleName The moduleName to set.
	 */
	public void setModuleName(final String moduleName) {
		this.moduleName = moduleName;
	}

	/**
	 * @return Returns the actionId.
	 */
	public String[] getActionId() {
		return this.actionId;
	}

	/**
	 * @param actionId The actionId to set.
	 */
	public void setActionId(final String[] actionId) {
		this.actionId = actionId;
	}

	/**
	 * @return Returns the actionName.
	 */
	public String[] getActionName() {
		return this.actionName;
	}

	/**
	 * @param actionName The actionName to set.
	 */
	public void setActionName(final String[] actionName) {
		this.actionName = actionName;
	}

	/**
	 * @return Returns the baseURL.
	 */
	public String[] getBaseURL() {
		return this.baseURL;
	}

	/**
	 * @param baseURL The baseURL to set.
	 */
	public void setBaseURL(final String[] baseURL) {
		this.baseURL = baseURL;
	}

	/**
	 * @return Returns the displayName.
	 */
	public String[] getDisplayName() {
		return this.displayName;
	}

	/**
	 * @param displayName The displayName to set.
	 */
	public void setDisplayName(final String[] displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return Returns the isEnabled.
	 */
	public String[] getIsEnabled() {
		return this.isEnabled;
	}

	/**
	 * @param isEnabled The isEnabled to set.
	 */
	public void setIsEnabled(final String[] isEnabled) {
		this.isEnabled = isEnabled;
	}

	/**
	 * @return Returns the moduleId.
	 */
	public String getModuleId() {
		return this.moduleId;
	}

	/**
	 * @param moduleId The moduleId to set.
	 */
	public void setModuleId(final String moduleId) {
		this.moduleId = moduleId;
	}

	/**
	 * @return Returns the orderNumber.
	 */
	public String[] getOrderNumber() {
		return this.orderNumber;
	}

	/**
	 * @param orderNumber The orderNumber to set.
	 */
	public void setOrderNumber(final String[] orderNumber) {
		this.orderNumber = orderNumber;
	}

	/**
	 * @return Returns the queryParams.
	 */
	public String[] getQueryParams() {
		return this.queryParams;
	}

	/**
	 * @param queryParams The queryParams to set.
	 */
	public void setQueryParams(final String[] queryParams) {
		this.queryParams = queryParams;
	}

	/**
	 * @return Returns the urlOrderId.
	 */
	public String[] getUrlOrderId() {
		return this.urlOrderId;
	}

	/**
	 * @param urlOrderId The urlOrderId to set.
	 */
	public void setUrlOrderId(final String[] urlOrderId) {
		this.urlOrderId = urlOrderId;
	}

	/**
	 * @return the deleteActionSet
	 */
	public String[] getDeleteActionSet() {
		return this.deleteActionSet;
	}

	/**
	 * @param deleteActionSet the deleteActionSet to set
	 */
	public void setDeleteActionSet(final String[] deleteActionSet) {
		this.deleteActionSet = deleteActionSet;
	}
}
