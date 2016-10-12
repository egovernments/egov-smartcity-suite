/*
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.ptis.elasticsearch.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document(indexName = "waterconncharges", type = "watercharges_details")
public class WaterTaxIndex {

	@JsonFormat (shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd'T'HH:mm'Z'")
	@Field(type = FieldType.Date, index = FieldIndex.not_analyzed, format = DateFormat.date_optional_time, pattern = "yyyy-MM-dd'T'hh:mm'Z'")
	private Date createdDate;
	
	/*//To be changed to GeoPoint later
	private String wardlocation;*/
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String watersource;
	
	@Field(type = FieldType.Boolean)
	private boolean islegacy; 
	
	@Field(type = FieldType.Long)
	private Long sumpcapacity;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String mobilenumber;
	
	@Field(type = FieldType.Long)
	private Long numberofperson;
	
	@Field(type = FieldType.Long)
	private Long totaldue;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String usage;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String propertytype;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String ulbname;
	
	@Id
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String consumercode;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String ward;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String applicationcode;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String districtname;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String zone;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String adminward;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String grade;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String regionname;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String pipesize;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String doorno;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String category;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String connectiontype;

	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String propertyid;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String status;
	
	@Field(type = FieldType.Long)
	private Long monthlyRate;
	
	//Check for other properties given in json
	private String aadhaarnumber;
	
	@Field(type = FieldType.Long)
	private Long waterTaxDue;
	
	//Check for other properties given in json
	private String locality;
	
	@Field(type = FieldType.Long)
	private Long arrearsDue;
	
	//Check for other properties given in json
	private String consumername;
	
	@Field(type = FieldType.Long)
	private Long currentDue;
	
	@Field(type = FieldType.Long)
	private Long arrearsDemand;
	
	@Field(type = FieldType.Long)
	private Long currentDemand;

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/*public String getWardlocation() {
		return wardlocation;
	}

	public void setWardlocation(String wardlocation) {
		this.wardlocation = wardlocation;
	}*/

	public String getWatersource() {
		return watersource;
	}

	public void setWatersource(String watersource) {
		this.watersource = watersource;
	}

	public boolean isIslegacy() {
		return islegacy;
	}

	public void setIslegacy(boolean islegacy) {
		this.islegacy = islegacy;
	}

	public Long getSumpcapacity() {
		return sumpcapacity;
	}

	public void setSumpcapacity(Long sumpcapacity) {
		this.sumpcapacity = sumpcapacity;
	}

	public String getMobilenumber() {
		return mobilenumber;
	}

	public void setMobilenumber(String mobilenumber) {
		this.mobilenumber = mobilenumber;
	}

	public Long getNumberofperson() {
		return numberofperson;
	}

	public void setNumberofperson(Long numberofperson) {
		this.numberofperson = numberofperson;
	}

	public Long getTotaldue() {
		return totaldue;
	}

	public void setTotaldue(Long totaldue) {
		this.totaldue = totaldue;
	}

	public String getUsage() {
		return usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}

	public String getPropertytype() {
		return propertytype;
	}

	public void setPropertytype(String propertytype) {
		this.propertytype = propertytype;
	}

	public String getUlbname() {
		return ulbname;
	}

	public void setUlbname(String ulbname) {
		this.ulbname = ulbname;
	}

	public String getConsumercode() {
		return consumercode;
	}

	public void setConsumercode(String consumercode) {
		this.consumercode = consumercode;
	}

	public String getWard() {
		return ward;
	}

	public void setWard(String ward) {
		this.ward = ward;
	}

	public String getApplicationcode() {
		return applicationcode;
	}

	public void setApplicationcode(String applicationcode) {
		this.applicationcode = applicationcode;
	}

	public String getDistrictname() {
		return districtname;
	}

	public void setDistrictname(String districtname) {
		this.districtname = districtname;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getAdminward() {
		return adminward;
	}

	public void setAdminward(String adminward) {
		this.adminward = adminward;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getRegionname() {
		return regionname;
	}

	public void setRegionname(String regionname) {
		this.regionname = regionname;
	}

	public String getPipesize() {
		return pipesize;
	}

	public void setPipesize(String pipesize) {
		this.pipesize = pipesize;
	}

	public String getDoorno() {
		return doorno;
	}

	public void setDoorno(String doorno) {
		this.doorno = doorno;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getConnectiontype() {
		return connectiontype;
	}

	public void setConnectiontype(String connectiontype) {
		this.connectiontype = connectiontype;
	}

	public String getPropertyid() {
		return propertyid;
	}

	public void setPropertyid(String propertyid) {
		this.propertyid = propertyid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getMonthlyRate() {
		return monthlyRate;
	}

	public void setMonthlyRate(Long monthlyRate) {
		this.monthlyRate = monthlyRate;
	}

	public String getAadhaarnumber() {
		return aadhaarnumber;
	}

	public void setAadhaarnumber(String aadhaarnumber) {
		this.aadhaarnumber = aadhaarnumber;
	}

	public Long getWaterTaxDue() {
		return waterTaxDue;
	}

	public void setWaterTaxDue(Long waterTaxDue) {
		this.waterTaxDue = waterTaxDue;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public Long getArrearsDue() {
		return arrearsDue;
	}

	public void setArrearsDue(Long arrearsDue) {
		this.arrearsDue = arrearsDue;
	}

	public String getConsumername() {
		return consumername;
	}

	public void setConsumername(String consumername) {
		this.consumername = consumername;
	}

	public Long getCurrentDue() {
		return currentDue;
	}

	public void setCurrentDue(Long currentDue) {
		this.currentDue = currentDue;
	}

	public Long getArrearsDemand() {
		return arrearsDemand;
	}

	public void setArrearsDemand(Long arrearsDemand) {
		this.arrearsDemand = arrearsDemand;
	}

	public Long getCurrentDemand() {
		return currentDemand;
	}

	public void setCurrentDemand(Long currentDemand) {
		this.currentDemand = currentDemand;
	}
	
}
