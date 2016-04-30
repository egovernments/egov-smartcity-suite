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

@SuppressWarnings("serial")
public class BpaFeeDetailExtn extends BaseModel {

	private BpaFeeExtn bpafee;
	private BigDecimal fromAreasqmt;
	private BigDecimal toAreasqmt;
	private BigDecimal amount;
	private Long srlNo;
	private String subType;
	private String landUseZone;
	private Long floorNumber;
	private LandBuildingTypesExtn usageType;
	private Date startDate;
	private Date endDate;
	private String additionalType;
	
	public String getAdditionalType() {
		return additionalType;
	}

	public void setAdditionalType(String additionalType) {
		this.additionalType = additionalType;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public String getLandUseZone() {
		return landUseZone;
	}

	public void setLandUseZone(String landUseZone) {
		this.landUseZone = landUseZone;
	}

	public Long getFloorNumber() {
		return floorNumber;
	}

	public void setFloorNumber(Long floorNumber) {
		this.floorNumber = floorNumber;
	}

	public LandBuildingTypesExtn getUsageType() {
		return usageType;
	}

	public void setUsageType(LandBuildingTypesExtn usageType) {
		this.usageType = usageType;
	}

	public BigDecimal getToAreasqmt() {
		return toAreasqmt;
	}

	public void setToAreasqmt(BigDecimal toAreasqmt) {
		this.toAreasqmt = toAreasqmt;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BpaFeeExtn getBpafee() {
		return bpafee;
	}

	public void setBpafee(BpaFeeExtn bpafee) {
		this.bpafee = bpafee;
	}

	public Long getSrlNo() {
		return srlNo;
	}

	public void setSrlNo(Long srlNo) {
		this.srlNo = srlNo;
	}

	public BigDecimal getFromAreasqmt() {
		return fromAreasqmt;
	}

	public void setFromAreasqmt(BigDecimal fromAreasqmt) {
		this.fromAreasqmt = fromAreasqmt;
	}

}
