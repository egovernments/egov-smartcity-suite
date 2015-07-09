/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency, 
 *    accountability and the service delivery of the government  organizations.
 * 
 *     Copyright (C) <2015>  eGovernments Foundation
 * 
 *     The updated version of eGov suite of products as by eGovernments Foundation 
 *     is available at http://www.egovernments.org
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or 
 *     http://www.gnu.org/licenses/gpl.html .
 * 
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 * 
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.eb.domain.master.entity;

import org.egov.commons.EgwStatus;
import org.egov.commons.utils.EntityType;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infstr.models.BaseModel;

public class EBConsumer extends BaseModel implements java.io.Serializable, EntityType {

	private String code;
	private String name;
	private String address;
	private String region;
	private String oddOrEvenBilling;
	private String location;
	private Boolean isActive;
	private Boundary ward;
	private String targetArea;

	public EBConsumer() {
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getOddOrEvenBilling() {
		return oddOrEvenBilling;
	}

	public void setOddOrEvenBilling(String oddOrEvenBilling) {
		this.oddOrEvenBilling = oddOrEvenBilling;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Boundary getWard() {
		return ward;
	}

	public void setWard(Boundary ward) {
		this.ward = ward;
	}

	public String getTargetArea() {
		return targetArea;
	}

	public void setTargetArea(String targetArea) {
		this.targetArea = targetArea;
	}

	public EBConsumer(Long id, String code, String name, String region, String oddOrEvenBilling, String location,
			String address, String accountNo, Boolean isActive, String targetArea, Boundary ward) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.region = region;
		this.oddOrEvenBilling = oddOrEvenBilling;
		this.location = location;
		this.address = address;
		this.isActive = isActive;
		this.ward = ward;
		this.targetArea = targetArea;
	}

	// TODO should use stringbuilder
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("id:" + id);
		str.append(",Code:" + code);
		str.append(",Nmae:" + name);
		str.append(",Region:" + region);
		str.append(",OddOrEvenBilling:" + oddOrEvenBilling);
		str.append(",Address:" + address);
		str.append(",Location:" + location);
		str.append(",Ward:" + ward);
		str.append(",TargetArea:" + targetArea);
		str.append(",IsActive:" + isActive);

		return str.toString();
	}

	@Override
	public String getBankname() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBankaccount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPanno() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTinno() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getIfsccode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getModeofpay() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getEntityId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEntityDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EgwStatus getEgwStatus() {
		// TODO Auto-generated method stub
		return null;
	}


}
