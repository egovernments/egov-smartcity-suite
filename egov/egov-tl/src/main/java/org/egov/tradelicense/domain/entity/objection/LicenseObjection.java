/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency, 
 *     accountability and the service delivery of the government  organizations.
 *  
 *      Copyright (C) <2015>  eGovernments Foundation
 *  
 *      The updated version of eGov suite of products as by eGovernments Foundation 
 *      is available at http://www.egovernments.org
 *  
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *  
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *  
 *      You should have received a copy of the GNU General Public License
 *      along with this program. If not, see http://www.gnu.org/licenses/ or 
 *      http://www.gnu.org/licenses/gpl.html .
 *  
 *      In addition to the terms of the GPL license to be adhered to in using this
 *      program, the following additional terms are to be complied with:
 *  
 *  	1) All versions of this program, verbatim or modified must carry this 
 *  	   Legal Notice.
 *  
 *  	2) Any misrepresentation of the origin of the material is prohibited. It 
 *  	   is required that all modified versions of this material be marked in 
 *  	   reasonable ways as different from the original version.
 *  
 *  	3) This license does not grant any rights to any user of the program 
 *  	   with regards to rights under trademark law for use of the trade names 
 *  	   or trademarks of eGovernments Foundation.
 *  
 *    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.tradelicense.domain.entity.objection;

import java.util.Date;
import java.util.List;

import org.egov.infstr.models.StateAware;
import org.egov.tradelicense.domain.entity.License;

public class LicenseObjection extends StateAware {
	private static final long serialVersionUID = 1L;
	public static final String BY_ID = "LISENSEOBJECTION_BY_ID"; 
	private License license;
	private String number;
	private Integer reason;
	private String name; // Name of the Objectioner
	private String address;// address of the Objectioner
	private String details;
	private Date objectionDate;
	private String docNumber;
	private List<Activity> activities;
	private List<Notice> notices;

	
	public List<Notice> getNotices() {
		return notices;
	}

	public void setNotices(List<Notice> notices) {
		this.notices = notices;
	}

	public License getLicense() {
		return this.license;
	}

	public void setLicense(License license) {
		this.license = license;
	}

	public String getNumber() {
		return this.number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDetails() {
		return this.details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Date getObjectionDate() {
		return this.objectionDate;
	}

	public void setObjectionDate(Date objectionDate) {
		this.objectionDate = objectionDate;
	}

	public String getDocNumber() {
		return this.docNumber;
	}

	public void setDocNumber(String docNumber) {
		this.docNumber = docNumber;
	}

	@Override
	public String getStateDetails() {
		return this.getState().getText1();
	}

	public Integer getReason() {
		return this.reason;
	}

	public void setReason(Integer reason) {
		this.reason = reason;
	}

	public List<Activity> getActivities() {
		return this.activities;
	}

	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}

	public String generateNumber(String runningNumber) {
		this.number = "OBJ" + runningNumber;
		return this.number;
	}
	
	public String myLinkId() {
		return this.getCurrentState().getText2()+"/web/objection/objection!showForApproval.action?model.id="+this.getId();
	}
	@Override
	public String toString() {
		 final StringBuilder str = new StringBuilder();
		str.append("LicenseObjection={");
		str.append("number=").append(number == null ? "null" : number.toString());
		str.append("name=").append(name == null ? "null" : name.toString());
		str.append("address=").append(address == null ? "null" : address.toString());
		str.append("details=").append(details == null ? "null" : details.toString());
		str.append("objectionDate=").append(objectionDate == null ? "null" : objectionDate.toString());
		str.append("docNumber=").append(docNumber == null ? "null" : docNumber.toString());
		str.append("}");
		return str.toString();
	}
}
