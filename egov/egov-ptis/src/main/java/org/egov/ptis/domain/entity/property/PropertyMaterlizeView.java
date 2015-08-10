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
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org
 ******************************************************************************/
package org.egov.ptis.domain.entity.property;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

import org.egov.infra.admin.master.entity.Boundary;

public class PropertyMaterlizeView implements Serializable {

	private Integer basicPropertyID;
	private String propertyId;
	private String ownerName;
	private String houseNo;
	private String propertyAddress;
	private PropertyTypeMaster propTypeMstrID;
	private Integer propUsageMstrID;
	private Boundary ward;
	private Boundary zone;
	private Boundary street;
	private Boundary block;
	private Boundary locality;
	private Integer sourceID;
	private BigDecimal sitalArea;
	private BigDecimal toalBuiltUpArea;
	private Integer latestStatus;
	private BigDecimal aggrCurrDmd;
	private BigDecimal aggrArrDmd;
	private BigDecimal aggrCurrColl;
	private BigDecimal aggrArrColl;
	private BigDecimal totalDemand;
	private String gisRefNo;
	private Set<InstDmdCollMaterializeView> instDmdColl;
	/*private Set<CurrFloorDmdCalcMaterializeView> currFloorDmdCalc;*/
	private BigDecimal alv;
	
	public Integer getBasicPropertyID() {
		return basicPropertyID;
	}

	public void setBasicPropertyID(Integer tbasicPropertyID) {
		this.basicPropertyID = tbasicPropertyID;
	}

	public String getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public String getPropertyAddress() {
		return propertyAddress;
	}

	public void setPropertyAddress(String propertyAddress) {
		this.propertyAddress = propertyAddress;
	}

	public Boundary getWard() {
		return ward;
	}

	public void setWard(Boundary ward) {
		this.ward = ward;
	}

	public Boundary getZone() {
		return zone;
	}

	public void setZone(Boundary zone) {
		this.zone = zone;
	}

	public Boundary getStreet() {
		return street;
	}

	public void setStreet(Boundary street) {
		this.street = street;
	}

	public Integer getSourceID() {
		return sourceID;
	}

	public void setSourceID(Integer sourceID) {
		this.sourceID = sourceID;
	}

	public BigDecimal getSitalArea() {
		return sitalArea;
	}

	public void setSitalArea(BigDecimal sitalArea) {
		this.sitalArea = sitalArea;
	}

	public BigDecimal getToalBuiltUpArea() {
		return toalBuiltUpArea;
	}

	public void setToalBuiltUpArea(BigDecimal toalBuiltUpArea) {
		this.toalBuiltUpArea = toalBuiltUpArea;
	}

	public Integer getLatestStatus() {
		return latestStatus;
	}

	public void setLatestStatus(Integer latestStatus) {
		this.latestStatus = latestStatus;
	}

	public BigDecimal getAggrCurrDmd() {
		return aggrCurrDmd;
	}

	public void setAggrCurrDmd(BigDecimal aggrCurrDmd) {
		this.aggrCurrDmd = aggrCurrDmd;
	}

	public PropertyTypeMaster getPropTypeMstrID() {
		return propTypeMstrID;
	}

	public void setPropTypeMstrID(PropertyTypeMaster propTypeMstrID) {
		this.propTypeMstrID = propTypeMstrID;
	}

	public Integer getPropUsageMstrID() {
		return propUsageMstrID;
	}

	public void setPropUsageMstrID(Integer propUsageMstrID) {
		this.propUsageMstrID = propUsageMstrID;
	}

	public BigDecimal getAggrArrDmd() {
		return aggrArrDmd;
	}

	public void setAggrArrDmd(BigDecimal aggrArrDmd) {
		this.aggrArrDmd = aggrArrDmd;
	}

	public BigDecimal getAggrCurrColl() {
		return aggrCurrColl;
	}

	public void setAggrCurrColl(BigDecimal aggrCurrColl) {
		this.aggrCurrColl = aggrCurrColl;
	}

	public BigDecimal getAggrArrColl() {
		return aggrArrColl;
	}

	public void setAggrArrColl(BigDecimal aggrArrColl) {
		this.aggrArrColl = aggrArrColl;
	}

	public BigDecimal getTotalDemand() {
		return totalDemand;
	}

	public void setTotalDemand(BigDecimal totalDemand) {
		this.totalDemand = totalDemand;
	}

	public String getGisRefNo() {
		return gisRefNo;
	}

	public void setGisRefNo(String gisRefNo) {
		this.gisRefNo = gisRefNo;
	}

	public Set<InstDmdCollMaterializeView> getInstDmdColl() {
		return instDmdColl;
	}

	public void setInstDmdColl(Set<InstDmdCollMaterializeView> instDmdColl) {
		this.instDmdColl = instDmdColl;
	}

	/*public Set<CurrFloorDmdCalcMaterializeView> getCurrFloorDmdCalc() {
		return currFloorDmdCalc;
	}

	public void setCurrFloorDmdCalc(Set<CurrFloorDmdCalcMaterializeView> currFloorDmdCalc) {
		this.currFloorDmdCalc = currFloorDmdCalc;
	}*/

	public BigDecimal getAlv() {
		return alv;
	}

	public void setAlv(BigDecimal alv) {
		this.alv = alv;
	}

	@Override
	public String toString() {
		StringBuilder objStr = new StringBuilder();

		objStr.append("BasicPropertyId: " + getBasicPropertyID()).append("|PropertyId: ").append(getPropertyId())
				.append("|SitalArea: ").append(getSitalArea()).append("|AggCurrDemand: ").append(getAggrCurrDmd())
				.append("|AggArrDemand: ").append(getAggrArrDmd()).append("|AggCurrColl: ").append(getAggrCurrColl())
				.append("|AggArrColl: ").append(getAggrArrColl()).append("|TotalDemand: ").append(getTotalDemand());
				//.append("|AVAmount: ").append(getAvAmt());

		return objStr.toString();
	}

    public Boundary getBlock() {
        return block;
    }

    public void setBlock(Boundary block) {
        this.block = block;
    }

    public Boundary getLocality() {
        return locality;
    }

    public void setLocality(Boundary locality) {
        this.locality = locality;
    }
}
