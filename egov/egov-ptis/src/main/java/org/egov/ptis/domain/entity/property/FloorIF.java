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
/*
 * FloorIF.java Created on Oct 25, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.ptis.domain.entity.property;

import java.math.BigDecimal;
import java.util.Date;

import org.egov.commons.Area;
//import org.egov.demand.model.DepreciationMaster;
import org.egov.demand.model.DepreciationMaster;

/**
 * <p>
 * This is an Interface which describes the Floor Details of a Property. A
 * Property might have one or more floors. The data for property tax might be
 * given for individual floors or at aggregate level.
 * </p>
 *
 * @author Gayathri Joshi
 * @version 2.00
 * @see org.egov.ptis.domain.entity.property.FloorImpl
 * @since 2.00
 */

public interface FloorIF {

	public Area getBuiltUpArea();

	public void setBuiltUpArea(Area builtUpArea);

	public ConstructionTypeSet getConstructionTypeSet();

	public void setConstructionTypeSet(ConstructionTypeSet constructionTypeSet);

	public String getElectricMeter();

	public void setElectricMeter(String electricMeter);

	public PropertyTypeMaster getUnitType();

	public void setUnitType(PropertyTypeMaster unitType);

	public String getUnitTypeCategory();

	public void setUnitTypeCategory(String unitTypeCategory);

	public Area getFloorArea();

	public void setFloorArea(Area floorArea);

	public Integer getFloorNo();

	public void setFloorNo(Integer floorNo);

	public PropertyOccupation getPropertyOccupation();

	public void setPropertyOccupation(PropertyOccupation propertyOccupation);

	public PropertyUsage getPropertyUsage();

	public void setPropertyUsage(PropertyUsage propertyUsage);

	public StructureClassification getStructureClassification();

	public void setStructureClassification(
			StructureClassification structureClassification);

	public DepreciationMaster getDepreciationMaster();

	public void setDepreciationMaster(DepreciationMaster depreciationMaster);

	public String getWaterMeter();

	public void setWaterMeter(String waterMeter);

	public boolean validateFloor();

	public Date getLastUpdatedTimeStamp();

	public void setLastUpdatedTimeStamp(Date lastUpdatedTimeStamp);

	public Date getCreatedTimeStamp();

	public void setCreatedTimeStamp(Date createdTimeStamp);

	public Long getId();

	public void setId(Long id);

	public void setRentPerMonth(BigDecimal rent);

	public BigDecimal getRentPerMonth();

	public String getExtraField1();

	public void setExtraField1(String extraField1);

	public String getExtraField2();

	public void setExtraField2(String extraField2);

	public String getExtraField3();

	public void setExtraField3(String extraField3);

	public String getExtraField4();

	public void setExtraField4(String extraField4);

	public String getExtraField5();

	public void setExtraField5(String extraField5);

	public String getExtraField6();

	public void setExtraField6(String extraField6);

	public String getExtraField7();

	public void setExtraField7(String extraField7);

	public BigDecimal getManualAlv();

	public void setManualAlv(BigDecimal alv);

	public String getWaterRate();

	public void setWaterRate(String waterRate);

	public BigDecimal getAlv();

	public void setAlv(BigDecimal alv);

	public String getTaxExemptedReason();

	public void setTaxExemptedReason(String taxExemptedReason);

	public UnitRentAgreementDetail getRentAgreementDetail();

	public void setRentAgreementDetail(
			UnitRentAgreementDetail rentAgreementDetail);
}
