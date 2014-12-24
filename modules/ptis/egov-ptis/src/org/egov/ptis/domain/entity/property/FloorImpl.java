/*
 * FloorImpl.java Created on Oct 27, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.ptis.domain.entity.property;

import java.math.BigDecimal;
import java.util.Date;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.Area;
import org.egov.demand.model.DepreciationMaster;
import org.egov.infstr.models.BaseModel;

/**
 * FloorImpl is a implementation of FloorIF interface.
 * 
 * @author Gayathri Joshi
 * @version 2.00
 * @see org.egov.ptis.domain.entity.property.FloorIF
 * @since 1.00
 */

public class FloorImpl extends BaseModel implements FloorIF {
	private ConstructionTypeSet constructionTypeSet = null;
	private StructureClassification structureClassification = null;
	private PropertyUsage propertyUsage = null;
	private PropertyOccupation propertyOccupation = null;
	private DepreciationMaster depreciationMaster = null;
	private Integer floorNo = null;
	private Area builtUpArea = null;
	private Area floorArea = null;
	private String waterMeter = null;
	private String electricMeter = null;
	private PropertyTypeMaster unitType;
	private String unitTypeCategory;
	private Date lastUpdatedTimeStamp = null;
	private Date createdTimeStamp = null;
	private BigDecimal rentPerMonth;
	private BigDecimal manualAlv;
	private String waterRate;
	
	// This field contains Unit No For NMC Impl
	private String extraField1;
	// This field contains Occupier Name For NMC Impl
	private String extraField2;
	// This field contains Occupation Date For NMC Impl
	private String extraField3;
	// This field contains Width For NMC Impl
	private String extraField4;
	// This field contains Length For NMC Impl
	private String extraField5;
	// This field contains Intercepting Wall Area For NMC Impl
	private String extraField6;
	// This field contains floor Type
	private String extraField7;

	public FloorImpl(ConstructionTypeSet constructionTypeSet, StructureClassification structureClassification,
			PropertyUsage propertyUsage, PropertyOccupation propertyOccupation, Integer floorNo,
			DepreciationMaster depreciationMaster, Area builtUpArea, Area floorArea, String waterMeter,
			String electricMeter, Date lastUpdatedTimeStamp, Date createdTimeStamp, BigDecimal rentPerMonth,
			String extraField1, String extraField2, String extraField3, String extraField4, String extraField5,
			String extraField6, String extraField7, BigDecimal manualAlv, PropertyTypeMaster unitType, String unitTypeCategory, String waterRate) {
		super();
		this.constructionTypeSet = constructionTypeSet;
		this.structureClassification = structureClassification;
		this.propertyUsage = propertyUsage;
		this.propertyOccupation = propertyOccupation;
		this.floorNo = floorNo;
		this.depreciationMaster = depreciationMaster;
		this.builtUpArea = builtUpArea;
		this.floorArea = floorArea;
		this.waterMeter = waterMeter;
		this.electricMeter = electricMeter;

		this.lastUpdatedTimeStamp = lastUpdatedTimeStamp;
		this.createdTimeStamp = createdTimeStamp;
		this.rentPerMonth = rentPerMonth;
		this.extraField1 = extraField1;
		this.extraField2 = extraField2;
		this.extraField3 = extraField3;
		this.extraField4 = extraField4;
		this.extraField5 = extraField5;
		this.extraField6 = extraField6;
		this.extraField7 = extraField7;
		this.manualAlv = manualAlv;
		this.unitType = unitType;
		this.unitTypeCategory = unitTypeCategory;
		this.waterRate = waterRate;
	}

	public FloorImpl() {

	}

	public FloorImpl(ConstructionTypeSet constructionTypeSet, StructureClassification structureClassification,
			PropertyUsage propertyUsage, PropertyOccupation propertyOccupation, Integer floorNo,
			DepreciationMaster depreciationMaster, Area builtUpArea, Area floorArea, String waterMeter,
			String electricMeter) {
		this.constructionTypeSet = constructionTypeSet;
		this.structureClassification = structureClassification;
		this.propertyUsage = propertyUsage;
		this.propertyOccupation = propertyOccupation;
		this.floorNo = floorNo;
		this.depreciationMaster = depreciationMaster;
		this.builtUpArea = builtUpArea;
		this.floorArea = floorArea;
		this.waterMeter = waterMeter;
		this.electricMeter = electricMeter;
	}

	/**
	 * @return Returns the builtUpArea.
	 */
	public Area getBuiltUpArea() {
		return builtUpArea;
	}

	/**
	 * @param builtUpArea
	 *            The builtUpArea to set.
	 */
	public void setBuiltUpArea(Area builtUpArea) {
		this.builtUpArea = builtUpArea;
	}

	/**
	 * @return Returns the constructionTypeSet.
	 */
	public ConstructionTypeSet getConstructionTypeSet() {
		return constructionTypeSet;
	}

	/**
	 * @param constructionTypeSet
	 *            The constructionTypeSet to set.
	 */
	public void setConstructionTypeSet(ConstructionTypeSet constructionTypeSet) {
		this.constructionTypeSet = constructionTypeSet;
	}

	/**
	 * @return Returns the electricMeter.
	 */
	public String getElectricMeter() {
		return electricMeter;
	}

	/**
	 * @param electricMeter
	 *            The electricMeter to set.
	 */
	public void setElectricMeter(String electricMeter) {
		this.electricMeter = electricMeter;
	}	

	public PropertyTypeMaster getUnitType() {
		return unitType;
	}

	public void setUnitType(PropertyTypeMaster unitType) {
		this.unitType = unitType;
	}

	@Override
	public String getUnitTypeCategory() {
		return this.unitTypeCategory;
	}

	@Override
	public void setUnitTypeCategory(String unitTypeCategory) {
		this.unitTypeCategory = unitTypeCategory;
	}
	
	/**
	 * @return Returns the floorArea.
	 */
	public Area getFloorArea() {
		return floorArea;
	}

	/**
	 * @param floorArea
	 *            The floorArea to set.
	 */
	public void setFloorArea(Area floorArea) {
		this.floorArea = floorArea;
	}

	/**
	 * @return Returns the floorNo.
	 */
	public Integer getFloorNo() {
		return floorNo;
	}

	/**
	 * @param floorNo
	 *            The floorNo to set.
	 */
	public void setFloorNo(Integer floorNo) {
		this.floorNo = floorNo;
	}

	/**
	 * @return Returns the propertyOccupation.
	 */
	public PropertyOccupation getPropertyOccupation() {
		return propertyOccupation;
	}

	/**
	 * @param propertyOccupation
	 *            The propertyOccupation to set.
	 */
	public void setPropertyOccupation(PropertyOccupation propertyOccupation) {
		this.propertyOccupation = propertyOccupation;
	}

	/**
	 * @return Returns the propertyUsage.
	 */
	public PropertyUsage getPropertyUsage() {
		return propertyUsage;
	}

	/**
	 * @param propertyUsage
	 *            The propertyUsage to set.
	 */
	public void setPropertyUsage(PropertyUsage propertyUsage) {
		this.propertyUsage = propertyUsage;
	}

	/**
	 * @return Returns the structureClassification.
	 */
	public StructureClassification getStructureClassification() {
		return structureClassification;
	}

	/**
	 * @param structureClassification
	 *            The structureClassification to set.
	 */
	public void setStructureClassification(StructureClassification structureClassification) {
		this.structureClassification = structureClassification;
	}

	/**
	 * @return Returns the waterMeter.
	 */
	public String getWaterMeter() {
		return waterMeter;
	}

	/**
	 * @param waterMeter
	 *            The waterMeter to set.
	 */
	public void setWaterMeter(String waterMeter) {
		this.waterMeter = waterMeter;
	}

	/**
	 * @return Returns the lastUpdatedTimeStamp.
	 */
	public Date getLastUpdatedTimeStamp() {
		return lastUpdatedTimeStamp;
	}

	/**
	 * @param lastUpdatedTimeStamp
	 *            The lastUpdatedTimeStamp to set.
	 */
	public void setLastUpdatedTimeStamp(Date lastUpdatedTimeStamp) {
		this.lastUpdatedTimeStamp = lastUpdatedTimeStamp;
	}

	/**
	 * @return true if the given Object is equal to FloorImpl this will get
	 *         invoke when more than one object is adding to
	 *         collection(ex:Set,Map.)
	 */
	public boolean equals(Object that) {
		if (that == null)
			return false;

		if (this == that)
			return true;
		if (that.getClass() != this.getClass())
			return false;

		final FloorImpl thatFlrImpl = (FloorImpl) that;

		if (this.getId() != null && thatFlrImpl.getId() != null) {
			if (getId().equals(thatFlrImpl.getId())) {
				return true;
			} else
				return false;
		} else
			return false;
	}

	/**
	 * @return Returns the hashCode
	 */
	public int hashCode() {
		int hashCode = 0;
		if (getId() != null) {
			hashCode += this.getId().hashCode();
		}
		return hashCode;
	}

	/**
	 * @return Returns the boolean after validating the current object
	 */
	public boolean validateFloor() {
		if (getFloorNo() == null)
			throw new EGOVRuntimeException("In FloorImpl Validate : FloorNumber is Not Set, Please Check !!");

		return true;
	}

	/**
	 * @return Returns the createdTimeStamp.
	 */
	public Date getCreatedTimeStamp() {
		return createdTimeStamp;
	}

	/**
	 * @param createdTimeStamp
	 *            The createdTimeStamp to set.
	 */
	public void setCreatedTimeStamp(Date createdTimeStamp) {
		this.createdTimeStamp = createdTimeStamp;
	}

	public BigDecimal getRentPerMonth() {
		return rentPerMonth;
	}

	public void setRentPerMonth(BigDecimal rentPerMonth) {
		this.rentPerMonth = rentPerMonth;
	}

	public String getExtraField1() {
		return extraField1;
	}

	public void setExtraField1(String extraField1) {
		this.extraField1 = extraField1;
	}

	public String getExtraField2() {
		return extraField2;
	}

	public void setExtraField2(String extraField2) {
		this.extraField2 = extraField2;
	}

	public String getExtraField3() {
		return extraField3;
	}

	public void setExtraField3(String extraField3) {
		this.extraField3 = extraField3;
	}

	public String getExtraField4() {
		return extraField4;
	}

	public void setExtraField4(String extraField4) {
		this.extraField4 = extraField4;
	}

	public String getExtraField5() {
		return extraField5;
	}

	public void setExtraField5(String extraField5) {
		this.extraField5 = extraField5;
	}

	public String getExtraField6() {
		return extraField6;
	}

	public void setExtraField6(String extraField6) {
		this.extraField6 = extraField6;
	}
	
	public String getExtraField7() {
		return extraField7;
	}

	public void setExtraField7(String extraField7) {
		this.extraField7 = extraField7;
	}

	public DepreciationMaster getDepreciationMaster() {
		return depreciationMaster;
	}

	public void setDepreciationMaster(DepreciationMaster depreciationMaster) {
		this.depreciationMaster = depreciationMaster;
	}
	
	public BigDecimal getManualAlv() {
		return manualAlv;
	}

	public void setManualAlv(BigDecimal manualAlv) {
		this.manualAlv = manualAlv;
	}	

	public String getWaterRate() {
		return waterRate;
	}

	public void setWaterRate(String waterRate) {
		this.waterRate = waterRate;
	}

	@Override
	public String toString() {
		return new StringBuilder(256)
		           .append("[Id: ").append(getId())
		           .append("|FloorNo: ").append(getFloorNo())
		           .append("|FloorArea:").append(getFloorArea() != null ? getFloorArea().getArea() : "null")
		           .append("|PropertyUsage:").append(getPropertyUsage())
		           .append("|StructCl:").append(getStructureClassification())
				   .append("|Occupancy: ").append(getPropertyOccupation())
				   .append("|Depreciation: ").append(getDepreciationMaster())
				   .append("|WaterRate: ").append(getWaterRate())
				   .append("]").toString();

	}
}
