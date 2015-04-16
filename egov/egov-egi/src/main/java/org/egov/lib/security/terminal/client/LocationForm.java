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

public class LocationForm extends ActionForm {

	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String desc;
	private String createdDate;
	private String lastModifiedDate;
	private String forward;
	private String loginType;
	private String counterId[];
	private String ipaddress[];
	private String counter[];
	private String ipaddr[];
	private String description[];
	private String isActive;
	private String locIPMapID[];
	private String deleteIPSet[];

	public String[] getDescription() {
		return this.description;
	}

	public void setDescription(final String[] description) {
		this.description = description;
	}

	public String[] getCounter() {
		return this.counter;
	}

	public void setCounter(final String[] counter) {
		this.counter = counter;
	}

	public String[] getIpaddr() {
		return this.ipaddr;
	}

	public void setIpaddr(final String[] ipaddr) {
		this.ipaddr = ipaddr;
	}

	public String getLoginType() {
		return this.loginType;
	}

	public void setLoginType(final String loginType) {
		this.loginType = loginType;
	}

	public String getForward() {
		return this.forward;
	}

	public void setForward(final String forward) {
		this.forward = forward;
	}

	public String getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(final String createdDate) {
		this.createdDate = createdDate;
	}

	public String getDesc() {
		return this.desc;
	}

	public void setDesc(final String desc) {
		this.desc = desc;
	}

	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(final String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String[] getCounterId() {
		return this.counterId;
	}

	public void setCounterId(final String[] counterId) {
		this.counterId = counterId;
	}

	/**
	 * @return the isActive
	 */
	public String getIsActive() {
		return this.isActive;
	}

	/**
	 * @param isActive the isActive to set
	 */
	public void setIsActive(final String isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return the ipaddress
	 */
	public String[] getIpaddress() {
		return this.ipaddress;
	}

	/**
	 * @param ipaddress the ipaddress to set
	 */
	public void setIpaddress(final String[] ipaddress) {
		this.ipaddress = ipaddress;
	}

	/**
	 * @return the locIPMapID
	 */
	public String[] getLocIPMapID() {
		return this.locIPMapID;
	}

	/**
	 * @param locIPMapID the locIPMapID to set
	 */
	public void setLocIPMapID(final String[] locIPMapID) {
		this.locIPMapID = locIPMapID;
	}

	/**
	 * @return the deleteIPSet
	 */
	public String[] getDeleteIPSet() {
		return this.deleteIPSet;
	}

	/**
	 * @param deleteIPSet the deleteIPSet to set
	 */
	public void setDeleteIPSet(final String[] deleteIPSet) {
		this.deleteIPSet = deleteIPSet;
	}

}
