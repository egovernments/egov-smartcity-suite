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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/* File Format
 * 
 * <AUTODCRBPSIntegration>
		<GetFileDetails>
	   		<ProposalInfo FileNO=\"COC/14133/14\" CaseType=\"Demolition and Reconstruction\" BuildingCategory=\"NA\" LandUseZone=\"Continuous Bldg Area\" ProposlaType=\"Residential\" InwardDate=\"9/4/2014 12:52:42 PM\" Zone=\"Zone-01\" Division=\"Div - 012\" PlotNO=\"NIL\" RoadName=\"JANDRAVAR STREET\" DoorNo=\"NEW DOOR NO:35, OLD DOOR NO:2A/1\" SurveyNo=\"173/83, 173/1 (PART)\" RevenueVillage=\"SATHANGADU\" BlockNo=\"10\" ApplicantName=\"S.PRIYA\" MobileNo=\"7200055490\" EmailId=\"NIL\" UniqueId=\"COC/1578112990\" PattaPlotArea=\"138\" DocumentPlotArea=\"137.73\" SitePlotArea=\"137.73\" />
		</GetFileDetails>
		<PlotDetails>
			<PlotInfo PLOTUSE=\"Residential\" GROSSPLOT_AREA=\"137.73\" TOTALBUILTUP_AREA=\"158.54\" CONSUMED_FSI=\"1.09\" COVERAGE_PERCENTAGE=\"70.04\" NETPLOT_AREA=\"137.73\" PLOT_WIDTH=\"5.79\" ABUTTING_ROAD=\"9\" PLOT_FRONTAGE=\"5.79\" COMPOUND_WALL_AREA=\"0\" WELL_OHT_SUMP_TANK_AREA=\"0\" />
		</PlotDetails>
		<BuildingDetails>
			<BuildingInfo BUILDINGNAME=\"PROPOSED (BUILDING)\" BUILDING_HEIGHT=\"6.91\" MarginFrontSide=\"2.13\" MarginRearSide=\"1.52\" MarginSide1=\"1\" Marginside2=\"0\" />
		</BuildingDetails>
		<FLOORDETAILS>
			<FLOORInfo FLOORNAME=\"TERRACE FLOOR\" TOTAL_CARPET_AREA=\"0\" TOTAL_BUILTUP_AREA=\"0\" TOTAL_SLAB=\"8.35\" />
			<FLOORInfo FLOORNAME=\"FIRST FLOOR\" TOTAL_CARPET_AREA=\"85.21\" TOTAL_BUILTUP_AREA=\"96.47\" TOTAL_SLAB=\"96.47\" /><FLOORInfo FLOORNAME=\"GROUND FLOOR\" TOTAL_CARPET_AREA=\"37.65\" TOTAL_BUILTUP_AREA=\"53.72\" TOTAL_SLAB=\"53.72\" />
		</FLOORDETAILS>
	</AUTODCRBPSIntegration>
 */

@XmlRootElement(name = "AUTODCRBPSIntegration")
public class AutoDcrBpaIntegration {

	private GetFileDetails getFileDetails;
	private FloorDetails floorDetails;
	private PlotDetails plotDetails;
	private BuildingDetails buildingDetails;
	private GetFileStatus fileStatus;

	@XmlElement(name = "GetFileStatus")
	public GetFileStatus getFileStatus() {
		return fileStatus;
	}

	public void setFileStatus(GetFileStatus fileStatus) {
		this.fileStatus = fileStatus;
	}

	@XmlElement(name = "BuildingDetails")
	public BuildingDetails getBuildingDetails() {
		return buildingDetails;
	}

	public void setBuildingDetails(BuildingDetails buildingDetails) {
		this.buildingDetails = buildingDetails;
	}

	@XmlElement(name = "FLOORDETAILS")
	public FloorDetails getFloorDetails() {
		return floorDetails;
	}

	public void setFloorDetails(FloorDetails floorDetailss) {
		floorDetails = floorDetailss;
	}

	@XmlElement(name = "GetFileDetails")
	public GetFileDetails getGetFileDetails() {
		return getFileDetails;
	}

	public void setGetFileDetails(GetFileDetails getFileDetails) {
		this.getFileDetails = getFileDetails;
	}

	@XmlElement(name = "PlotDetails")
	public PlotDetails getPlotDetails() {
		return plotDetails;
	}

	public void setPlotDetails(PlotDetails plotDetails) {
		this.plotDetails = plotDetails;
	}

}

