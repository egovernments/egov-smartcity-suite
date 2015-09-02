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
package org.egov.tradelicense.domain.entity;

import org.egov.infstr.models.BaseModel;
import org.egov.infstr.models.validator.Required;
import org.egov.lib.address.model.Address;
import org.egov.lib.admbndry.Boundary;
import org.hibernate.validator.Length;

public class Licensee extends BaseModel {
	@Required(message = "licensee.name.err.required")
	@Length(min = 1, max = 256, message = "licensee.name.err.maxlength")
	private String applicantName;
	private Address address;
	private String nationality;
	private String fatherOrSpouseName;
	// private String spouseName;
	private String qualification;
	private Integer age;
	private String gender;
	private String panNumber;
	private String phoneNumber;
	private String mobilePhoneNumber;
	private String uid;
	private String emailId;
	private Boundary boundary;
	public Boundary getBoundary() {
		return boundary;
	}

	public void setBoundary(Boundary boundary) {
		this.boundary = boundary;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getMobilePhoneNumber() {
		return mobilePhoneNumber;
	}

	public void setMobilePhoneNumber(String mobilePhoneNumber) {
		this.mobilePhoneNumber = mobilePhoneNumber;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	private License license;

	public License getLicense() {
		return license;
	}

	public void setLicense(License license) {
		this.license = license;
	}

	public String getApplicantName() {
		return applicantName;
	}

	public void setApplicantName(String applicantName) {
		this.applicantName = applicantName;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getFatherOrSpouseName() {
		return fatherOrSpouseName;
	}

	public void setFatherOrSpouseName(String fatherOrSpouseName) {
		this.fatherOrSpouseName = fatherOrSpouseName;
	}

	public String getQualification() {
		return qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("Licensee={");
		str.append("  applicantName=").append(applicantName == null ? "null" : applicantName.toString());
		str.append("  address=").append(address == null ? "null" : address.toString());
		str.append("  nationality=").append(nationality == null ? "null" : nationality.toString());
		str.append("  fatherOrSpouseName=").append(fatherOrSpouseName == null ? "null" : fatherOrSpouseName.toString());
		str.append("  qualification=").append(qualification == null ? "null" : qualification.toString());
		str.append("  age=").append(age == null ? "null" : age.toString());
		str.append("  gender=").append(gender == null ? "null" : gender.toString());
		str.append("  panNumber=").append(panNumber == null ? "null" : panNumber.toString());
		str.append("  phoneNumber=").append(phoneNumber == null ? "null" : phoneNumber.toString());
		str.append("  boundary=").append(boundary == null ? "null" : boundary.toString());
		str.append("}");
		return str.toString();
	}
}
