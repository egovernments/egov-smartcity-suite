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
 * The Implementation Class for the BuildUpProperty
 * 
 * @author Neetu
 * @version 2.00
 * @see org.egov.ptis.property.model.PropertyDetail,
 *      org.egov.ptis.property.model.AbstractProperty,
 * 
 */
public class BuiltUpProperty extends AbstractProperty {
	private static final Logger LOGGER = Logger.getLogger(BuiltUpProperty.class);
	private Area sitalArea;
	private Area totalBuiltupArea;
	private Area commBuiltUpArea;
	private Area plinthArea;
	private Area commVacantLand;
	private Area nonResPlotArea;
	private Boolean irregular;
	private String surveyNumber;
	private Character fieldVerified;
	private Date fieldVerificationDate;
	private Set<FloorIF> floorDetails = new HashSet<FloorIF>();
	private List<FloorImpl> floorDetailsProxy = new ArrayList<FloorImpl>();
	private Integer propertyDetailsID;
	private String water_Meter_Num;
	private String elec_Meter_Num;
	private Integer no_of_floors;
	private char fieldIrregular = 'N';
	private Date completion_year;
	private Date effective_date;
	private Property property;
	private Date updatedTime;
	private PropertyUsage propertyUsage;
	private Date dateOfCompletion;
	private PropertyCreationReason creationReason;
	private PropertyTypeMaster propertyTypeMaster;
	private String propertyType;
	private Installment installment;
	private PropertyMutationMaster propertyMutationMaster;
	private Character comZone = 'N';
	private Character cornerPlot = 'N';
	private PropertyOccupation propertyOccupation;
	private boolean lift = false;
	private boolean toilets = false;
	private boolean waterTap = false;
	private boolean structure = false;
	private boolean drainage = false;
	private boolean electricity = false;
	private boolean attachedBathRoom = false;
	private boolean waterHarvesting = false;
	private boolean cable = false;
	private double extentSite;
	private double extentAppartenauntLand;
	private String siteOwner;
	private Long floorType;
	private Long roofType;
	private Long wallType;
	private Long woodType;

	public BuiltUpProperty(Area sitalArea, Area totalBuiltupArea, Area commBuiltUpArea, Area plinthArea,
			Area commVacantLand, Area nonResPlotArea, 
			Boolean irregular, String surveyNumber, Character fieldVerified,
			Date fieldVerificationDate, Set<FloorIF> floorDetails, Integer propertyDetailsID, String water_Meter_Num,
			String elec_Meter_Num, Integer no_of_floors, char fieldIrregular, Date completion_year,
			Date effective_date, Property property, Date updatedTime, PropertyUsage propertyUsage,
			Date dateOfCompletion, PropertyCreationReason creationReason, PropertyTypeMaster propertyTypeMaster,
			String propertyType,Installment installment, PropertyMutationMaster propertyMutationMaster, Character comZone,
			Character cornerPlot, PropertyOccupation propertyOccupation) {
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
		this.property = property;
		this.updatedTime = updatedTime;
		this.propertyUsage = propertyUsage;
		this.dateOfCompletion = dateOfCompletion;
		this.creationReason = creationReason;
		this.propertyTypeMaster = propertyTypeMaster;
		this.propertyType = propertyType;
		this.installment = installment;
		this.propertyMutationMaster = propertyMutationMaster;
		this.comZone = comZone;
		this.cornerPlot = cornerPlot;
		this.propertyOccupation = propertyOccupation;
	}

	public BuiltUpProperty() {
		super();
	}

	/**
	 * @return Returns the dateOfCompletion.
	 */
	public Date getDateOfCompletion() {
		return dateOfCompletion;
	}

	/**
	 * @param dateOfCompletion
	 *            The dateOfCompletion to set.
	 */
	public void setDateOfCompletion(Date dateOfCompletion) {
		this.dateOfCompletion = dateOfCompletion;
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
	 * @return Returns the fieldVerified.
	 */
	public Character getFieldVerified() {
		return fieldVerified;
	}

	/**
	 * @return Returns the irregular.
	 */
	public Boolean getIrregular() {
		return irregular;
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
	 * @return Returns the Water_Meter_Num
	 */
	public String getWater_Meter_Num() {
		return water_Meter_Num;
	}

	/**
	 * @param Water_Meter_Num
	 *            The Water_Meter_Num to set.
	 */
	public void setWater_Meter_Num(String water_Meter_Num) {
		this.water_Meter_Num = water_Meter_Num;
	}

	/**
	 * @return Returns the Elec_Meter_Num
	 */
	public String getElec_Meter_Num() {
		return elec_Meter_Num;
	}

	/**
	 * @param Water_Meter_Num
	 *            The Water_Meter_Num to set.
	 */
	public void setElec_Meter_Num(String elec_Meter_Num) {
		this.elec_Meter_Num = elec_Meter_Num;
	}

	/**
	 * @return Returns the PropertyDetailsID
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
	 * @return Returns the Property
	 */
	public Property getProperty() {
		return property;
	}

	/**
	 * @param Property
	 *            The Property
	 */

	public void setProperty(Property property) {
		this.property = property;
	}

	/**
	 * @return Returns the Set view for the FloorDetails.
	 */
	public Set<FloorIF> getFloorDetails() {
		return floorDetails;
	}

	/**
	 * @param Set
	 *            The Set view of floorDetails.
	 */
	public void setFloorDetails(Set<FloorIF> floorDetails) {
		this.floorDetails = floorDetails;
	}

	/**
	 * @return Returns the Sital area .
	 */
	public Area getSitalArea() {
		return sitalArea;
	}

	/**
	 * @param sitalArea
	 *            The sitalArea to set .
	 */
	public void setSitalArea(Area sitalArea) {
		this.sitalArea = sitalArea;
	}

	/**
	 * This method adds the Floor Object to the Set view of the Floor
	 * 
	 * @param floor
	 *            The floor to set .
	 */
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
	 * @param plinthArea
	 *            The plinthArea to set .
	 */
	public void setPlinthArea(Area area) {
		this.plinthArea = area;
	}

	/**
	 * @return Returns the Plinth Area.
	 */
	public Area getPlinthArea() {
		return plinthArea;
	}

	/**
	 * @return Returns the Total Built Up Area.
	 */
	public Area getTotalBuiltupArea() {
		return totalBuiltupArea;
	}

	/**
	 * @param totalBuiltupArea
	 *            The totalBuiltupArea to set .
	 */
	public void setTotalBuiltupArea(Area area) {
		this.totalBuiltupArea = area;
	}

	/**
	 * @return Returns the Common Built Up Area.
	 */
	public Area getCommBuiltUpArea() {
		return commBuiltUpArea;
	}

	/**
	 * @param commBuiltUpArea
	 *            The commBuiltUpArea to set .
	 */
	public void setCommBuiltUpArea(Area area) {
		this.commBuiltUpArea = area;
	}

	/**
	 * @return Returns the Common Vacant Land Area.
	 */
	public Area getCommVacantLand() {
		return commVacantLand;
	}

	/**
	 * @param commVacantLand
	 *            The commVacantLand to set .
	 */
	public void setCommVacantLand(Area area) {
		this.commVacantLand = area;
	}

	/**
	 * @return Returns the Boolean - isIrregular.
	 */
	public Boolean isIrregular() {
		return irregular;
	}

	/**
	 * @param Boolean
	 *            irregular The irregular to set .
	 */
	public void setIrregular(Boolean irregular) {
		this.irregular = irregular;
	}

	/**
	 * @return Returns the Survey Number.
	 */
	public String getSurveyNumber() {
		return surveyNumber;
	}

	/**
	 * @param surveyNumber
	 *            The surveyNumber to set .
	 */
	public void setSurveyNumber(String surveyNumber) {
		this.surveyNumber = surveyNumber;
	}

	/**
	 * @param fieldVerified
	 *            The fieldVerified to set .
	 */
	public void setFieldVerified(Character fieldVerified) {
		this.fieldVerified = fieldVerified;
	}

	/**
	 * @return Returns the fieldVerificationDate.
	 */
	public java.util.Date getFieldVerificationDate() {
		return fieldVerificationDate;
	}

	/**
	 * @param fieldVerificationDate
	 *            The fieldVerificationDate to set .
	 */
	public void setFieldVerificationDate(java.util.Date fieldVerificationDate) {
		this.fieldVerificationDate = fieldVerificationDate;
	}

	/**
	 * @return Returns PropertyUsage
	 */
	public PropertyUsage getPropertyUsage() {
		return propertyUsage;
	}

	/**
	 * @param Boolean
	 *            propertyUsage The propertyUsage to set.
	 */
	public void setPropertyUsage(PropertyUsage propertyUsage) {
		this.propertyUsage = propertyUsage;
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
	 * @return Returns if the given Object is equal to PropertyImpl
	 */
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (this == obj)
			return true;

		if (!(obj instanceof BuiltUpProperty))
			return false;

		final BuiltUpProperty other = (BuiltUpProperty) obj;

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
			hashCode = hashCode + getId().hashCode();
		} else if ((getProperty() != null)) {
			hashCode = hashCode + this.getProperty().hashCode();
		}
		return hashCode;
	}

	/**
	 * @return Returns the boolean after validating the current object
	 * @throws
	 */
	public boolean validateProperty() throws InvalidPropertyException {
		/*
		 * if(getSitalArea() == null) throw newEGOVRuntimeException(
		 * "BuiltUpProperty.validate : SitalArea is NULL, Please Check !!");
		 */
		/*
		 * if(getPlinthArea() == null) throw newEGOVRuntimeException(
		 * "BuiltUpProperty.validate : PlinthArea is NULL, Please Check !!");
		 */
		/*
		 * if(getId_Adm_Bndry() == null) throw newEGOVRuntimeException(
		 * "BuiltUpProperty.validate : Id_Adm_Bndry is NULL, Please Check !!");
		 */
		/*
		 * if(getTotalBuiltupArea() == null) throw newEGOVRuntimeException(
		 * "BuiltUpProperty.validate : TotalBuiltUpArea is NULL, Please Check
		 * !!" );
		 */
		/*
		 * if(getPropertyAddress() == null) throw newEGOVRuntimeException(
		 * "BuiltUpProperty.validate : PropertyAddress is NULL, Please Check !!"
		 * ); else if(getPropertyAddress().validate() == false) throw new
		 * EGOVRuntimeException( "BuiltUpProperty.validate : PropertyAddress
		 * Validate() failed, Please Check !!" );
		 */
		if (getProperty() == null)
			throw new InvalidPropertyException("BuiltUpProperty.validate : Property is NULL, Please Check !!");
		else if (getProperty().validateProperty() == false)
			throw new InvalidPropertyException("BuiltUpProperty.validate : Property Validate() failed, Please Check !!");

		// can't use validate, not implemented
		/*
		 * if(getBoundary() == null) throw newEGOVRuntimeException(
		 * "BuiltUpProperty.validate : Boundary is NULL, Please Check !!");
		 */
		/*
		 * if(getAddress() == null) throw newEGOVRuntimeException(
		 * "BuiltUpProperty.validate : Address is NULL, Please Check !!"); else
		 * if(getAddress().validate() == false) throw newEGOVRuntimeException(
		 * "BuiltUpProperty.validate : Address Validate() failed, Please Check
		 * !!" );
		 */
		if (getPropertySource() == null)
			throw new InvalidPropertyException("BuiltUpProperty.validate : PropertySource is NULL, Please Check !!");
		else if (getPropertySource().validate() == false)
			throw new InvalidPropertyException(
					"BuiltUpProperty.validate : PropertySource Validate() failed, Please Check !!");
		/*
		 * if(getPropertyUsage() == null) throw newEGOVRuntimeException(
		 * "BuiltUpProperty.validate : PropertyUsage is NULL, Please Check !!");
		 * else if(getPropertyUsage().validate() == false) throw new
		 * EGOVRuntimeException( "BuiltUpProperty.validate : PropertyUsage
		 * Validate() failed, Please Check !!" );
		 */
		return true;
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

	@Override
	public String toString() {
		StringBuilder objStr = new StringBuilder();

		objStr.append("Id: ").append(getId()).append("|").append("Sital Area: ");
		objStr = (getSitalArea() != null) ? objStr.append(getSitalArea().getArea()) : objStr.append("NULL").append(
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

	@Override
	public boolean isLift() {
		return lift;
	}

	@Override
	public void setLift(boolean lift) {
		this.lift = lift;
	}

	@Override
	public boolean isToilets() {
		return toilets;
	}

	@Override
	public void setToilets(boolean toilets) {
		this.toilets = toilets;
	}

	@Override
	public boolean isWaterTap() {
		return waterTap;
	}

	@Override
	public void setWaterTap(boolean waterTap) {
		this.waterTap = waterTap;
	}

	@Override
	public boolean isStructure() {
		return structure;
	}

	@Override
	public void setStructure(boolean structure) {
		this.structure = structure;
	}

	@Override
	public boolean isDrainage() {
		return drainage;
	}

	@Override
	public void setDrainage(boolean drainage) {
		this.drainage = drainage;
	}

	@Override
	public boolean isElectricity() {
		return electricity;
	}

	@Override
	public void setElectricity(boolean electricity) {
		this.electricity = electricity;
	}

	@Override
	public boolean isAttachedBathRoom() {
		return attachedBathRoom;
	}

	@Override
	public void setAttachedBathRoom(boolean attachedBathRoom) {
		this.attachedBathRoom = attachedBathRoom;
	}

	@Override
	public boolean isWaterHarvesting() {
		return waterHarvesting;
	}

	@Override
	public void setWaterHarvesting(boolean waterHarvesting) {
		this.waterHarvesting = waterHarvesting;
	}

	@Override
	public boolean isCable() {
		return cable;
	}

	@Override
	public void setCable(boolean cable) {
		this.cable = cable;
	}

	@Override
	public double getExtentSite() {
		return extentSite;
	}

	@Override
	public void setExtentSite(double extentSite) {
		this.extentSite = extentSite;
	}

	public double getExtentAppartenauntLand() {
		return extentAppartenauntLand;
	}

	@Override
	public void setExtentAppartenauntLand(double extentAppartenauntLand) {
		this.extentAppartenauntLand = extentAppartenauntLand;
	}
	
	@Override
	public String getSiteOwner() {
		return siteOwner;
	}

	@Override
	public void setSiteOwner(String siteOwner) {
		this.siteOwner = siteOwner;
	}

	public Long getFloorType() {
		return floorType;
	}

	public void setFloorType(Long floorType) {
		this.floorType = floorType;
	}

	public Long getRoofType() {
		return roofType;
	}

	public void setRoofType(Long roofType) {
		this.roofType = roofType;
	}

	public Long getWallType() {
		return wallType;
	}

	public void setWallType(Long wallType) {
		this.wallType = wallType;
	}

	public Long getWoodType() {
		return woodType;
	}

	public void setWoodType(Long woodType) {
		this.woodType = woodType;
	}
	
	
}
