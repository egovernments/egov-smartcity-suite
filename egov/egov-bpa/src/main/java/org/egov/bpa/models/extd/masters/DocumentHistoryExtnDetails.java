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
package org.egov.bpa.models.extd.masters;

import org.egov.infstr.models.BaseModel;

import java.math.BigDecimal;
import java.util.Date;

public class DocumentHistoryExtnDetails extends BaseModel{
	private Long id;
	private DocumentHistoryExtn docHistoryId;
	private String surveyNumber;
	private String vendor;
	private String purchaser;
	private BigDecimal extentInsqmt;
	private Long srlNo;
	private String natureOfDeed;
	private String remarks;
	private Date registartionDate;
	private String referenceNumber;
	private String northBoundary;
	private String southBoundary;
	private String westBoundary;
	private String eastBoundary;
	
	
	public DocumentHistoryExtn getDocHistoryId() {
		return docHistoryId;
	}
	public void setDocHistoryId(DocumentHistoryExtn docHistoryId) {
		this.docHistoryId = docHistoryId;
	}
	public Long getSrlNo() {
		return srlNo;
	}
	public void setSrlNo(Long srlNo) {
		this.srlNo = srlNo;
	}
	
	public String getSurveyNumber() {
		return surveyNumber;
	}
	public void setSurveyNumber(String surveyNumber) {
		this.surveyNumber = surveyNumber;
	}
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	public String getPurchaser() {
		return purchaser;
	}
	public void setPurchaser(String purchaser) {
		this.purchaser = purchaser;
	}
	public BigDecimal getExtentInsqmt() {
		return extentInsqmt;
	}
	public void setExtentInsqmt(BigDecimal extentInsqmt) {
		this.extentInsqmt = extentInsqmt;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getNatureOfDeed() {
		return natureOfDeed;
	}
	public void setNatureOfDeed(String natureOfDeed) {
		this.natureOfDeed = natureOfDeed;
	}
	public Date getRegistartionDate() {
		return registartionDate;
	}
	public void setRegistartionDate(Date registartionDate) {
		this.registartionDate = registartionDate;
	}
	public String getReferenceNumber() {
		return referenceNumber;
	}
	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}
	public String getNorthBoundary() {
		return northBoundary;
	}
	public void setNorthBoundary(String northBoundary) {
		this.northBoundary = northBoundary;
	}
	public String getSouthBoundary() {
		return southBoundary;
	}
	public void setSouthBoundary(String southBoundary) {
		this.southBoundary = southBoundary;
	}
	public String getWestBoundary() {
		return westBoundary;
	}
	public void setWestBoundary(String westBoundary) {
		this.westBoundary = westBoundary;
	}
	public String getEastBoundary() {
		return eastBoundary;
	}
	public void setEastBoundary(String eastBoundary) {
		this.eastBoundary = eastBoundary;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
