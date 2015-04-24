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
package org.egov.bpa.services.extd.integration.AutoDcr;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ProposalInfo")
public class ProposalInfo {
	private String fileNumber;
	private String caseType;
	private String buildingCategory;
	private String landUseZone;
	private String proposlaType;
	private String inwardDate;
	private String zone;
	private String division;
	private String plotNumber;
	private String roadName;
	private String doorNumber;
	private String surveyNumber;
	private String revenueVillage;
	private String blockNumber;
	private String mobileNumber;
	private String emailId;
	private String uniqueId;
	private String pattaPlotArea;
	private String documentPlotArea;
	private String SitePlotArea;
	private String status;

	@XmlAttribute(name = "Status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@XmlAttribute(name = "SurveyNo")
	public String getSurveyNumber() {
		return surveyNumber;
	}

	public void setSurveyNumber(String surveyNos) {
		surveyNumber = surveyNos;
	}

	@XmlAttribute(name = "RevenueVillage")
	public String getRevenueVillage() {
		return revenueVillage;
	}

	public void setRevenueVillage(String revenueVillages) {
		revenueVillage = revenueVillages;
	}

	@XmlAttribute(name = "BlockNo")
	public String getBlockNubmer() {
		return blockNumber;
	}

	public void setBlockNumber(String blockNos) {
		blockNumber = blockNos;
	}

	@XmlAttribute(name = "BuildingCategory")
	public String getBuildingCategory() {
		return buildingCategory;
	}

	public void setBuildingCategory(String buildingCategorys) {
		buildingCategory = buildingCategorys;
	}

	@XmlAttribute(name = "LandUseZone")
	public String getLandUseZone() {
		return landUseZone;
	}

	public void setLandUseZone(String landUseZones) {
		landUseZone = landUseZones;
	}

	@XmlAttribute(name = "ProposlaType")
	public String getProposlaType() {
		return proposlaType;
	}

	public void setProposlaType(String proposlaTypes) {
		proposlaType = proposlaTypes;
	}

	@XmlAttribute(name = "InwardDate")
	public String getInwardDate() {
		return inwardDate;
	}

	public void setInwardDate(String inwardDates) {
		inwardDate = inwardDates;
	}

	@XmlAttribute(name = "Zone")
	public String getZone() {
		return zone;
	}

	public void setZone(String zones) {
		zone = zones;
	}

	@XmlAttribute(name = "Division")
	public String getDivision() {
		return division;
	}

	public void setDivision(String divisions) {
		division = divisions;
	}

	@XmlAttribute(name = "PlotNO")
	public String getPlotNumber() {
		return plotNumber;
	}

	public void setPlotNumber(String plotNOs) {
		plotNumber = plotNOs;
	}

	@XmlAttribute(name = "RoadName")
	public String getRoadName() {
		return roadName;
	}

	public void setRoadName(String roadNames) {
		roadName = roadNames;
	}

	@XmlAttribute(name = "DoorNo")
	public String getDoorNumber() {
		return doorNumber;
	}

	public void setDoorNumber(String doorNos) {
		doorNumber = doorNos;
	}

	@XmlAttribute(name = "MobileNo")
	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNo) {
		mobileNumber = mobileNo;
	}

	@XmlAttribute(name = "EmailId")
	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailIds) {
		emailId = emailIds;
	}

	@XmlAttribute(name = "UniqueId")
	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueIds) {
		uniqueId = uniqueIds;
	}

	@XmlAttribute(name = "PattaPlotArea")
	public String getPattaPlotArea() {
		return pattaPlotArea;
	}

	public void setPattaPlotArea(String pattaPlotAreas) {
		pattaPlotArea = pattaPlotAreas;
	}

	@XmlAttribute(name = "DocumentPlotArea")
	public String getDocumentPlotArea() {
		return documentPlotArea;
	}

	public void setDocumentPlotArea(String documentPlotAreas) {
		documentPlotArea = documentPlotAreas;
	}

	@XmlAttribute(name = "SitePlotArea")
	public String getSitePlotArea() {
		return SitePlotArea;
	}

	public void setSitePlotArea(String sitePlotArea) {
		SitePlotArea = sitePlotArea;
	}

	@XmlAttribute(name = "FileNO")
	public String getFileNumber() {
		return fileNumber;
	}

	public void setFileNumber(String fileNO) {
		fileNumber = fileNO;
	}

	@XmlAttribute(name = "CaseType")
	public String getCaseType() {
		return caseType;
	}

	public void setCaseType(String caseTypes) {
		caseType = caseTypes;
	}
}
