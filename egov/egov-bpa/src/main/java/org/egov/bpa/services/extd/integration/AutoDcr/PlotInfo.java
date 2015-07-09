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

@XmlRootElement(name = "PlotInfo")
public class PlotInfo {

	private String plotUse;

	private String grossPlotArea;

	private String totalBuildUpArea;

	private String consumedFsi;

	private String coveragePercentage;

	private String netPlotArea;

	private String plotWidth;

	private String abuttingRoad;

	private String plotFrontage;

	private String compoundWallArea;

	private String wellOhtSumpTankArea;

	@XmlAttribute(name = "PLOTUSE")
	public String getPlotUse() {
		return plotUse;
	}

	public void setPlotUse(String plotUse) {
		this.plotUse = plotUse;
	}

	@XmlAttribute(name = "GROSSPLOT_AREA")
	public String getGrossPlotArea() {
		return grossPlotArea;
	}

	public void setGrossPlotArea(String grossPlotArea) {
		this.grossPlotArea = grossPlotArea;
	}

	@XmlAttribute(name = "TOTALBUILTUP_AREA")
	public String getTotalBuildUpArea() {
		return totalBuildUpArea;
	}

	public void setTotalBuildUpArea(String totalBuildUpArea) {
		this.totalBuildUpArea = totalBuildUpArea;
	}

	@XmlAttribute(name = "CONSUMED_FSI")
	public String getConsumedFsi() {
		return consumedFsi;
	}

	public void setConsumedFsi(String consumedFsi) {
		this.consumedFsi = consumedFsi;
	}

	@XmlAttribute(name = "COVERAGE_PERCENTAGE")
	public String getCoveragePercentage() {
		return coveragePercentage;
	}

	public void setCoveragePercentage(String coveragePercentage) {
		this.coveragePercentage = coveragePercentage;
	}

	@XmlAttribute(name = "NETPLOT_AREA")
	public String getNetPlotArea() {
		return netPlotArea;
	}

	public void setNetPlotArea(String netPlotArea) {
		this.netPlotArea = netPlotArea;
	}

	@XmlAttribute(name = "PLOT_WIDTH")
	public String getPlotWidth() {
		return plotWidth;
	}

	public void setPlotWidth(String plotWidth) {
		this.plotWidth = plotWidth;
	}

	@XmlAttribute(name = "ABUTTING_ROAD")
	public String getAbuttingRoad() {
		return abuttingRoad;
	}

	public void setAbuttingRoad(String abuttingRoad) {
		this.abuttingRoad = abuttingRoad;
	}

	@XmlAttribute(name = "PLOT_FRONTAGE")
	public String getPlotFrontage() {
		return plotFrontage;
	}

	public void setPlotFrontage(String plotFrontage) {
		this.plotFrontage = plotFrontage;
	}

	@XmlAttribute(name = "COMPOUND_WALL_AREA")
	public String getCompoundWallArea() {
		return compoundWallArea;
	}

	public void setCompoundWallArea(String compoundWallArea) {
		this.compoundWallArea = compoundWallArea;
	}

	@XmlAttribute(name = "WELL_OHT_SUMP_TANK_AREA")
	public String getWellOhtSumpTankArea() {
		return wellOhtSumpTankArea;
	}

	public void setWellOhtSumpTankArea(String wellOhtSumpTankArea) {
		this.wellOhtSumpTankArea = wellOhtSumpTankArea;
	}

}
