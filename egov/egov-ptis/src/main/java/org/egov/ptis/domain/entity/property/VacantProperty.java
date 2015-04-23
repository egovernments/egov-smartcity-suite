/*
 * VacantProperty.java Created on Oct 21, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.ptis.domain.entity.property;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.commons.Area;
import org.egov.commons.Installment;
import org.egov.exceptions.InvalidPropertyException;

/**
 * The Implementation Class for the VacantProperty
 * 
 * @author Neetu
 * @version 2.00
 * @see org.egov.ptis.domain.entity.property.PropertyDetail
 *      org.egov.ptis.property.model.AbstractProperty,
 * 
 */
public class VacantProperty extends AbstractProperty {
	private Area sitalArea;
	private Area totalBuiltupArea;
	private Area commBuiltUpArea;
	private Area plinthArea;
	private Area commVacantLand;
	private Area nonResPlotArea;
	private Boolean irregular;
	private String surveyNumber;
	private Character fieldVerified;
	private java.util.Date fieldVerificationDate;
	private java.util.Set<FloorIF> floorDetails = new HashSet<FloorIF>();
	private List<FloorImpl> floorDetailsProxy = new ArrayList<FloorImpl>();
	private Integer propertyDetailsID;
	private String water_Meter_Num;
	private String elec_Meter_Num;
	private Integer no_of_floors;
	private char fieldIrregular = 'N';
	private Date completion_year;
	private Date effective_date;
	private Date dateOfCompletion;
	private Property property;
	private Date updatedTime;
	private PropertyUsage propertyUsage;
	private PropertyCreationReason creationReason;
	private PropertyTypeMaster propertyTypeMaster;
	private String propertyType;
	private Installment installment;
	private PropertyOccupation propertyOccupation;
	private PropertyMutationMaster propertyMutationMaster;
	private Character comZone = 'N';
	private Character cornerPlot = 'N';
	private static final Logger LOGGER = Logger.getLogger(VacantProperty.class);

	public VacantProperty(Area sitalArea, Area totalBuiltupArea, Area commBuiltUpArea, Area plinthArea,
			Area commVacantLand, Area nonResPlotArea, Boolean irregular, String surveyNumber, Character fieldVerified,
			Date fieldVerificationDate, Set<FloorIF> floorDetails, Integer propertyDetailsID, String water_Meter_Num,
			String elec_Meter_Num, Integer no_of_floors, char fieldIrregular, Date completion_year,
			Date effective_date, Date dateOfCompletion, Property property, Date updatedTime,
			PropertyUsage propertyUsage, PropertyCreationReason creationReason, PropertyTypeMaster propertyTypeMaster,
			String propertyType,Installment installment, PropertyOccupation propertyOccupation,
			PropertyMutationMaster propertyMutationMaster, Character comZone, Character cornerPlot) {
		super();
		this.sitalArea = sitalArea;
		this.totalBuiltupArea = totalBuiltupArea;
		this.commBuiltUpArea = commBuiltUpArea;
		this.plinthArea = plinthArea;
		this.commVacantLand = commVacantLand;
		this.nonResPlotArea = nonResPlotArea;
		this.irregular = irregular;
		this.surveyNumber = surveyNumber;
		this.fieldVerified = fieldVerified;
		this.fieldVerificationDate = fieldVerificationDate;
		this.floorDetails = floorDetails;
		this.propertyDetailsID = propertyDetailsID;
		this.water_Meter_Num = water_Meter_Num;
		this.elec_Meter_Num = elec_Meter_Num;
		this.no_of_floors = no_of_floors;
		this.fieldIrregular = fieldIrregular;
		this.completion_year = completion_year;
		this.effective_date = effective_date;
		this.dateOfCompletion = dateOfCompletion;
		this.property = property;
		this.updatedTime = updatedTime;
		this.propertyUsage = propertyUsage;
		this.creationReason = creationReason;
		this.propertyTypeMaster = propertyTypeMaster;
		this.propertyType = propertyType;
		this.installment = installment;
		this.propertyOccupation = propertyOccupation;
		this.propertyMutationMaster = propertyMutationMaster;
		this.comZone = comZone;
		this.cornerPlot = cornerPlot;
	}

	public Date getDateOfCompletion() {
		return dateOfCompletion;
	}

	public void setDateOfCompletion(Date dateOfCompletion) {
		this.dateOfCompletion = dateOfCompletion;
	}

	public void addFloor(FloorIF floor) {
		LOGGER.debug("BuildUpFloor.addFloor");
		getFloorDetails().add(floor);
		no_of_floors = getFloorDetails().size();
	}

	/**
	 * This method removes the Floor Object from the Set view of the Floor
	 * 
	 * @param floor
	 *            The floor to set .
	 */
	public void removeFloor(FloorIF floor) {
		LOGGER.debug("BuildUpFloor.removeFloor");
		getFloorDetails().remove(floor);
		no_of_floors = getFloorDetails().size();
	}

	/**
	 * @return Returns the commBuiltUpArea.
	 */
	public Area getCommBuiltUpArea() {
		return commBuiltUpArea;
	}

	/**
	 * @param commBuiltUpArea
	 *            The commBuiltUpArea to set.
	 */
	public void setCommBuiltUpArea(Area commBuiltUpArea) {
		this.commBuiltUpArea = commBuiltUpArea;
	}

	/**
	 * @return Returns the commVacantLand.
	 */
	public Area getCommVacantLand() {
		return commVacantLand;
	}

	/**
	 * @param commVacantLand
	 *            The commVacantLand to set.
	 */
	public void setCommVacantLand(Area commVacantLand) {
		this.commVacantLand = commVacantLand;
	}

	/**
	 * @return Returns the completion_year.
	 */
	public Date getCompletion_year() {
		return completion_year;
	}

	/**
	 * @param completion_year
	 *            The completion_year to set.
	 */
	public void setCompletion_year(Date completion_year) {
		this.completion_year = completion_year;
	}

	/**
	 * @return Returns the creationReason.
	 */
	public PropertyCreationReason getCreationReason() {
		return creationReason;
	}

	/**
	 * @param creationReason
	 *            The creationReason to set.
	 */
	public void setCreationReason(PropertyCreationReason creationReason) {
		this.creationReason = creationReason;
	}

	/**
	 * @return Returns the effective_date.
	 */
	public Date getEffective_date() {
		return effective_date;
	}

	/**
	 * @param effective_date
	 *            The effective_date to set.
	 */
	public void setEffective_date(Date effective_date) {
		this.effective_date = effective_date;
	}

	/**
	 * @return Returns the elec_Meter_Num.
	 */
	public String getElec_Meter_Num() {
		return elec_Meter_Num;
	}

	/**
	 * @param elec_Meter_Num
	 *            The elec_Meter_Num to set.
	 */
	public void setElec_Meter_Num(String elec_Meter_Num) {
		this.elec_Meter_Num = elec_Meter_Num;
	}

	/**
	 * @return Returns the fieldIrregular.
	 */
	public char getFieldIrregular() {
		return fieldIrregular;
	}

	/**
	 * @param fieldIrregular
	 *            The fieldIrregular to set.
	 */
	public void setFieldIrregular(char fieldIrregular) {
		this.fieldIrregular = fieldIrregular;
	}

	/**
	 * @return Returns the fieldVerificationDate.
	 */
	public java.util.Date getFieldVerificationDate() {
		return fieldVerificationDate;
	}

	/**
	 * @param fieldVerificationDate
	 *            The fieldVerificationDate to set.
	 */
	public void setFieldVerificationDate(java.util.Date fieldVerificationDate) {
		this.fieldVerificationDate = fieldVerificationDate;
	}

	/**
	 * @return Returns the fieldVerified.
	 */
	public Character getFieldVerified() {
		return fieldVerified;
	}

	/**
	 * @param fieldVerified
	 *            The fieldVerified to set.
	 */
	public void setFieldVerified(Character fieldVerified) {
		this.fieldVerified = fieldVerified;
	}

	/**
	 * @return Returns the floorDetails.
	 */
	public java.util.Set<FloorIF> getFloorDetails() {
		return floorDetails;
	}

	/**
	 * @param floorDetails
	 *            The floorDetails to set.
	 */
	public void setFloorDetails(java.util.Set<FloorIF> floorDetails) {
		this.floorDetails = floorDetails;
	}

	/**
	 * @return Returns the irregular.
	 */
	public Boolean getIrregular() {
		return irregular;
	}

	/**
	 * @param irregular
	 *            The irregular to set.
	 */
	public void setIrregular(Boolean irregular) {
		this.irregular = irregular;
	}

	/**
	 * @return Returns the no_of_floors.
	 */
	public Integer getNo_of_floors() {
		return no_of_floors;
	}

	/**
	 * @param no_of_floors
	 *            The no_of_floors to set.
	 */
	public void setNo_of_floors(Integer no_of_floors) {
		this.no_of_floors = no_of_floors;
	}

	/**
	 * @return Returns the plinthArea.
	 */
	public Area getPlinthArea() {
		return plinthArea;
	}

	/**
	 * @param plinthArea
	 *            The plinthArea to set.
	 */
	public void setPlinthArea(Area plinthArea) {
		this.plinthArea = plinthArea;
	}

	/**
	 * @return Returns the property.
	 */
	public Property getProperty() {
		return property;
	}

	/**
	 * @param property
	 *            The property to set.
	 */
	public void setProperty(Property property) {
		this.property = property;
	}

	/**
	 * @return Returns the propertyDetailsID.
	 */
	public Integer getPropertyDetailsID() {
		return propertyDetailsID;
	}

	/**
	 * @param propertyDetailsID
	 *            The propertyDetailsID to set.
	 */
	public void setPropertyDetailsID(Integer propertyDetailsID) {
		this.propertyDetailsID = propertyDetailsID;
	}

	/**
	 * @return Returns the propertyTypeMaster.
	 */
	public PropertyTypeMaster getPropertyTypeMaster() {
		return propertyTypeMaster;
	}

	/**
	 * @param propertyTypeMaster
	 *            The propertyTypeMaster to set.
	 */
	public void setPropertyTypeMaster(PropertyTypeMaster propertyTypeMaster) {
		this.propertyTypeMaster = propertyTypeMaster;
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
	 * @return Returns the sitalArea.
	 */
	public Area getSitalArea() {
		return sitalArea;
	}

	/**
	 * @param sitalArea
	 *            The sitalArea to set.
	 */
	public void setSitalArea(Area sitalArea) {
		this.sitalArea = sitalArea;
	}

	/**
	 * @return Returns the surveyNumber.
	 */
	public String getSurveyNumber() {
		return surveyNumber;
	}

	/**
	 * @param surveyNumber
	 *            The surveyNumber to set.
	 */
	public void setSurveyNumber(String surveyNumber) {
		this.surveyNumber = surveyNumber;
	}

	/**
	 * @return Returns the totalBuiltupArea.
	 */
	public Area getTotalBuiltupArea() {
		return totalBuiltupArea;
	}

	/**
	 * @param totalBuiltupArea
	 *            The totalBuiltupArea to set.
	 */
	public void setTotalBuiltupArea(Area totalBuiltupArea) {
		this.totalBuiltupArea = totalBuiltupArea;
	}

	/**
	 * @return Returns the updatedTime.
	 */
	public Date getUpdatedTime() {
		return updatedTime;
	}

	/**
	 * @param updatedTime
	 *            The updatedTime to set.
	 */
	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	/**
	 * @return Returns the water_Meter_Num.
	 */
	public String getWater_Meter_Num() {
		return water_Meter_Num;
	}

	/**
	 * @param water_Meter_Num
	 *            The water_Meter_Num to set.
	 */
	public void setWater_Meter_Num(String water_Meter_Num) {
		this.water_Meter_Num = water_Meter_Num;
	}

	/**
	 * @return Returns the propertyType.
	 */
	public String getPropertyType() {
		return propertyType;
	}

	/**
	 * @param propertyType
	 *            The propertyType to set.
	 */
	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}

	/**
	 * @return Returns the installment.
	 */
	public Installment getInstallment() {
		return installment;
	}

	/**
	 * @param installment
	 *            The installment to set.
	 */
	public void setInstallment(Installment installment) {
		this.installment = installment;
	}

	/**
	 * @return Returns if the given Object is equal to PropertyImpl
	 */
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (this == obj)
			return true;

		if (!(obj instanceof VacantProperty))
			return false;

		final VacantProperty other = (VacantProperty) obj;

		if (getId() != null || other.getId() != null) {
			if (getId().equals(other.getId())) {
				return true;
			}
			return false;
		} else if (getProperty() != null || other.getProperty() != null) {
			if (getProperty().equals(other.getProperty())) {
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
			hashCode = hashCode + this.getId().hashCode();
		} else if ((getProperty() != null)) {
			hashCode = hashCode + this.getProperty().hashCode();
		}
		return hashCode;
	}

	/**
	 * @return Returns the boolean after validating the current object
	 */
	public boolean validateProperty() throws InvalidPropertyException {
		if (getSitalArea() == null) {
			throw new InvalidPropertyException("VacantProperty.validate : SitalArea Data is NULL, Please Check !!");
		}
		/*
		 * if(getTotalBuiltupArea() == null) throw newEGOVRuntimeException(
		 * "VacantProperty.validate : TotalBuiltUpArea is NULL, Please Check !!"
		 * );
		 */

		if (getPropertyAddress() == null) {
			throw new InvalidPropertyException("VacantProperty.validate : PropertyAddress is NULL, Please Check !!");
		} 
		if (getProperty() == null) {
			throw new InvalidPropertyException("VacantProperty.validate : Property is NULL, Please Check !!");
		} else if (!getProperty().validateProperty()) {
			throw new InvalidPropertyException("VacantProperty.validate : Property Validate() failed, Please Check !!");
		}
		// can't use validate, not implemented
		/*
		 * if(getBoundary() == null) throw newEGOVRuntimeException(
		 * "VacantProperty.validate : Boundary is NULL, Please Check !!");
		 */
		/*
		 * if(getAddress() == null) throw newEGOVRuntimeException(
		 * "VacantProperty.validate : Address is NULL, Please Check !!"); else
		 * if(getAddress().validate() == false) throw newEGOVRuntimeException(
		 * "VacantProperty.validate : Address Validate() failed, Please Check
		 * !!" );
		 */
		if (getPropertySource() == null) {
			throw new InvalidPropertyException("VacantProperty.validate : PropertySource is NULL, Please Check !!");
		} else if (!getPropertySource().validate()) {
			throw new InvalidPropertyException(
					"VacantProperty.validate : PropertySource Validate() failed, Please Check !!");
		}
		/*
		 * if(getPropertyUsage() == null) throw newEGOVRuntimeException(
		 * "VacantProperty.validate : PropertyUsage is NULL, Please Check !!");
		 * else if(getPropertyUsage().validate() == false) throw new
		 * EGOVRuntimeException( "VacantProperty.validate : PropertyUsage
		 * Validate() failed, Please Check !!" );
		 */
		return true;
	}

	/**
	 * @return Returns the propertyMutationMaster.
	 */
	public PropertyMutationMaster getPropertyMutationMaster() {
		return propertyMutationMaster;
	}

	/**
	 * @param propertyMutationMaster
	 *            The propertyMutationMaster to set.
	 */
	public void setPropertyMutationMaster(PropertyMutationMaster propertyMutationMaster) {
		this.propertyMutationMaster = propertyMutationMaster;
	}

	public Character getComZone() {
		return comZone;
	}

	public void setComZone(Character comZone) {
		this.comZone = comZone;
	}

	public Character getCornerPlot() {
		return cornerPlot;
	}

	public void setCornerPlot(Character cornerPlot) {
		this.cornerPlot = cornerPlot;
	}

	public PropertyOccupation getPropertyOccupation() {
		return propertyOccupation;
	}

	public void setPropertyOccupation(PropertyOccupation propertyOccupation) {
		this.propertyOccupation = propertyOccupation;
	}

	public List<FloorImpl> getFloorDetailsProxy() {
		getFloorDetails().addAll(floorDetailsProxy);
		return floorDetailsProxy;
	}

	public void setFloorDetailsProxy(List<FloorImpl> floorDetailsProxy) {
		this.floorDetailsProxy = floorDetailsProxy;
		getFloorDetails().addAll(floorDetailsProxy);
	}

	public VacantProperty() {
		super();
	}

	@Override
	public String toString() {
		StringBuilder objStr = new StringBuilder();

		objStr.append("Id: ").append(getId()).append("|Sital Area: ").append(getSitalArea().getArea()).append(
				"|NoOfFloors: ").append(getNo_of_floors());

		return objStr.toString();
	}

	@Override
	public Area getNonResPlotArea() {
		return nonResPlotArea;
	}

	@Override
	public void setNonResPlotArea(Area nonResPlotArea) {
		this.nonResPlotArea = nonResPlotArea;
	}
}
