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
package org.egov.bpa.models.extd;

import org.egov.infstr.models.BaseModel;

import java.util.HashSet;
import java.util.Set;

public class AutoDcrDtlsExtn extends BaseModel {
	
	private static final long serialVersionUID = 1L;
	// File Details
	private String  autoDcrNum;
	private String  fileNumber;
	private String  file_caseType;
	private String  file_buildingCategory;
	private String  file_landUseZone;
	private String 	logicalPath;
	private String  file_proposlaType;
	private String  file_inwardDate;
	private String  file_zone;
	private String  file_division;
	private String  file_plotNumber;
	private String  file_roadName;
	private String  file_doorNumber;
	private String  file_surveyNumber;
	private String  file_revenueVillage;
	private String  file_blockNumber;
	private String  file_mobileNumber;
	private String  file_applicantName;
	private String  file_emailId;
	private String  file_uniqueId;
	private String  file_pattaPlotArea;
	private String  file_documentPlotArea;
	private String  file_SitePlotArea;
	private String  file_status;
	// Plot Information
	private String  plotUse;
	private String  plot_grossPlotArea;
	private String  plot_totalBuildUpArea;
	private String  plot_consumedFsi;
	private String  plot_coveragePercentage;
	private String  plot_netPlotArea;
	private String  plotWidth;
	private String  plot_abuttingRoad;
	private String  plotFrontage;
	private String  plot_compoundWallArea;
	private String  plot_wellOhtSumpTankArea;
	// Building Information
	private String bldg_buildingName;
	private String bldg_buildingHeight;
	private String bldg_marginFrontSide;
	private String bldg_marginRearSide;
	private String bldg_marginSide1;
	private String bldg_marginSide2;
	
	//private List<AutoDcrDtlsFloorInfoExtn> autoDcrDtlsFloorInfoExtnList = new ArrayList<AutoDcrDtlsFloorInfoExtn>();
	private Set<AutoDcrDtlsFloorInfoExtn> autoDcrDtlsFloorInfoExtnList = new HashSet<AutoDcrDtlsFloorInfoExtn>();
	
	
	public void addFloorInfoList(AutoDcrDtlsFloorInfoExtn floorInfo) {
		this.autoDcrDtlsFloorInfoExtnList.add(floorInfo);
	}
	
	public String getAutoDcrNum() {
		return autoDcrNum;
	}
	public void setAutoDcrNum(String autoDcrNum) {
		this.autoDcrNum = autoDcrNum;
	}
	public String getFileNumber() {
		return fileNumber;
	}
	public void setFileNumber(String fileNumber) {
		this.fileNumber = fileNumber;
	}
	public String getFile_caseType() {
		return file_caseType;
	}
	public void setFile_caseType(String file_caseType) {
		this.file_caseType = file_caseType;
	}
	public String getFile_buildingCategory() {
		return file_buildingCategory;
	}
	public void setFile_buildingCategory(String file_buildingCategory) {
		this.file_buildingCategory = file_buildingCategory;
	}
	public String getFile_landUseZone() {
		return file_landUseZone;
	}
	public void setFile_landUseZone(String file_landUseZone) {
		this.file_landUseZone = file_landUseZone;
	}
	public String getFile_proposlaType() {
		return file_proposlaType;
	}
	public void setFile_proposlaType(String file_proposlaType) {
		this.file_proposlaType = file_proposlaType;
	}
	public String getFile_inwardDate() {
		return file_inwardDate;
	}
	public void setFile_inwardDate(String file_inwardDate) {
		this.file_inwardDate = file_inwardDate;
	}
	public String getFile_zone() {
		return file_zone;
	}
	public void setFile_zone(String file_zone) {
		this.file_zone = file_zone;
	}
	public String getFile_division() {
		return file_division;
	}
	public void setFile_division(String file_division) {
		this.file_division = file_division;
	}
	public String getFile_plotNumber() {
		return file_plotNumber;
	}
	public void setFile_plotNumber(String file_plotNumber) {
		this.file_plotNumber = file_plotNumber;
	}
	public String getFile_roadName() {
		return file_roadName;
	}
	public void setFile_roadName(String file_roadName) {
		this.file_roadName = file_roadName;
	}
	public String getFile_doorNumber() {
		return file_doorNumber;
	}
	public void setFile_doorNumber(String file_doorNumber) {
		this.file_doorNumber = file_doorNumber;
	}
	public String getFile_surveyNumber() {
		return file_surveyNumber;
	}
	public void setFile_surveyNumber(String file_surveyNumber) {
		this.file_surveyNumber = file_surveyNumber;
	}
	public String getFile_revenueVillage() {
		return file_revenueVillage;
	}
	public void setFile_revenueVillage(String file_revenueVillage) {
		this.file_revenueVillage = file_revenueVillage;
	}
	public String getFile_blockNumber() {
		return file_blockNumber;
	}
	public void setFile_blockNumber(String file_blockNumber) {
		this.file_blockNumber = file_blockNumber;
	}
	public String getFile_mobileNumber() {
		return file_mobileNumber;
	}
	public void setFile_mobileNumber(String file_mobileNumber) {
		this.file_mobileNumber = file_mobileNumber;
	}
	public String getFile_emailId() {
		return file_emailId;
	}
	public void setFile_emailId(String file_emailId) {
		this.file_emailId = file_emailId;
	}
	public String getFile_uniqueId() {
		return file_uniqueId;
	}
	public void setFile_uniqueId(String file_uniqueId) {
		this.file_uniqueId = file_uniqueId;
	}
	public String getFile_pattaPlotArea() {
		return file_pattaPlotArea;
	}
	public void setFile_pattaPlotArea(String file_pattaPlotArea) {
		this.file_pattaPlotArea = file_pattaPlotArea;
	}
	public String getFile_documentPlotArea() {
		return file_documentPlotArea;
	}
	public void setFile_documentPlotArea(String file_documentPlotArea) {
		this.file_documentPlotArea = file_documentPlotArea;
	}
	public String getFile_SitePlotArea() {
		return file_SitePlotArea;
	}
	public void setFile_SitePlotArea(String file_SitePlotArea) {
		this.file_SitePlotArea = file_SitePlotArea;
	}
	public String getFile_status() {
		return file_status;
	}
	public void setFile_status(String file_status) {
		this.file_status = file_status;
	}
	public String getPlotUse() {
		return plotUse;
	}
	public void setPlotUse(String plotUse) {
		this.plotUse = plotUse;
	}
	public String getPlot_grossPlotArea() {
		return plot_grossPlotArea;
	}
	public void setPlot_grossPlotArea(String plot_grossPlotArea) {
		this.plot_grossPlotArea = plot_grossPlotArea;
	}
	public String getPlot_totalBuildUpArea() {
		return plot_totalBuildUpArea;
	}
	public void setPlot_totalBuildUpArea(String plot_totalBuildUpArea) {
		this.plot_totalBuildUpArea = plot_totalBuildUpArea;
	}
	public String getPlot_consumedFsi() {
		return plot_consumedFsi;
	}
	public void setPlot_consumedFsi(String plot_consumedFsi) {
		this.plot_consumedFsi = plot_consumedFsi;
	}
	public String getPlot_coveragePercentage() {
		return plot_coveragePercentage;
	}
	public void setPlot_coveragePercentage(String plot_coveragePercentage) {
		this.plot_coveragePercentage = plot_coveragePercentage;
	}
	public String getPlot_netPlotArea() {
		return plot_netPlotArea;
	}
	public void setPlot_netPlotArea(String plot_netPlotArea) {
		this.plot_netPlotArea = plot_netPlotArea;
	}
	public String getPlotWidth() {
		return plotWidth;
	}
	public void setPlotWidth(String plotWidth) {
		this.plotWidth = plotWidth;
	}
	public String getPlot_abuttingRoad() {
		return plot_abuttingRoad;
	}
	public void setPlot_abuttingRoad(String plot_abuttingRoad) {
		this.plot_abuttingRoad = plot_abuttingRoad;
	}
	public String getPlotFrontage() {
		return plotFrontage;
	}
	public void setPlotFrontage(String plotFrontage) {
		this.plotFrontage = plotFrontage;
	}
	public String getPlot_compoundWallArea() {
		return plot_compoundWallArea;
	}
	public void setPlot_compoundWallArea(String plot_compoundWallArea) {
		this.plot_compoundWallArea = plot_compoundWallArea;
	}
	public String getPlot_wellOhtSumpTankArea() {
		return plot_wellOhtSumpTankArea;
	}
	public void setPlot_wellOhtSumpTankArea(String plot_wellOhtSumpTankArea) {
		this.plot_wellOhtSumpTankArea = plot_wellOhtSumpTankArea;
	}
	public String getBldg_buildingName() {
		return bldg_buildingName;
	}
	public void setBldg_buildingName(String bldg_buildingName) {
		this.bldg_buildingName = bldg_buildingName;
	}
	public String getBldg_buildingHeight() {
		return bldg_buildingHeight;
	}
	public void setBldg_buildingHeight(String bldg_buildingHeight) {
		this.bldg_buildingHeight = bldg_buildingHeight;
	}
	public String getBldg_marginFrontSide() {
		return bldg_marginFrontSide;
	}
	public void setBldg_marginFrontSide(String bldg_marginFrontSide) {
		this.bldg_marginFrontSide = bldg_marginFrontSide;
	}
	public String getBldg_marginRearSide() {
		return bldg_marginRearSide;
	}
	public void setBldg_marginRearSide(String bldg_marginRearSide) {
		this.bldg_marginRearSide = bldg_marginRearSide;
	}
	public String getBldg_marginSide1() {
		return bldg_marginSide1;
	}
	public void setBldg_marginSide1(String bldg_marginSide1) { 
		this.bldg_marginSide1 = bldg_marginSide1;  
	}
	public String getBldg_marginSide2() {
		return bldg_marginSide2;
	}
	public void setBldg_marginSide2(String bldg_marginSide2) {
		this.bldg_marginSide2 = bldg_marginSide2;
	}
	public Set<AutoDcrDtlsFloorInfoExtn> getAutoDcrDtlsFloorInfoExtnList() {
		return autoDcrDtlsFloorInfoExtnList;
	}

	public void setAutoDcrDtlsFloorInfoExtnList(
			Set<AutoDcrDtlsFloorInfoExtn> autoDcrDtlsFloorInfoExtnList) {
		this.autoDcrDtlsFloorInfoExtnList = autoDcrDtlsFloorInfoExtnList;
	}

	public String getFile_applicantName() {
		return file_applicantName;
	}

	public void setFile_applicantName(String file_applicantName) {
		this.file_applicantName = file_applicantName;
	}

	public String getLogicalPath() {
		return logicalPath;
	}

	public void setLogicalPath(String logicalPath) {
		this.logicalPath = logicalPath;
	}
}
