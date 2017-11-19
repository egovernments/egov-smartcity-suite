/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
package org.egov.ptis.domain.entity.property;

import static java.lang.Boolean.FALSE;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.commons.Area;
import org.egov.commons.Installment;
import org.egov.exceptions.InvalidPropertyException;
import org.egov.ptis.domain.entity.property.vacantland.LayoutApprovalAuthority;
import org.egov.ptis.domain.entity.property.vacantland.VacantLandPlotArea;

/**
 * The Implementation Class for the VacantProperty
 * 
 * @see org.egov.ptis.domain.entity.property.PropertyDetail
 *      org.egov.ptis.property.model.AbstractProperty,
 */
public class VacantProperty extends AbstractProperty {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(VacantProperty.class);
    private transient Area sitalArea;
    private transient Area totalBuiltupArea;
    private transient Area commBuiltUpArea;
    private transient Area plinthArea;
    private transient Area commVacantLand;
    private transient Area nonResPlotArea;
    private Boolean irregular;
    private String surveyNumber;
    private Character fieldVerified;
    private java.util.Date fieldVerificationDate;
    private java.util.List<Floor> floorDetails = new ArrayList<>();
    private java.util.List<Floor> floorDetailsProxy = new ArrayList<>();
    private Integer propertyDetailsID;
    private String water_Meter_Num;
    private String elec_Meter_Num;
    private Integer noofFloors;
    private char fieldIrregular = 'N';
    private Date dateOfCompletion;
    private transient Property property;
    private Date updatedTime;
    private PropertyUsage propertyUsage;
    private PropertyCreationReason creationReason;
    private PropertyTypeMaster propertyTypeMaster;
    private String propertyType;
    private PropertyOccupation propertyOccupation;
    private transient PropertyMutationMaster propertyMutationMaster;
    private Character comZone = 'N';
    private Character cornerPlot = 'N';
    private boolean lift = false;
    private boolean toilets = false;
    private boolean waterTap = false;
    private boolean structure = false;
    private boolean electricity = false;
    private boolean attachedBathRoom = false;
    private boolean waterHarvesting = false;
    private boolean cable = false;
    private double extentSite;
    private double extentAppartenauntLand;
    private String siteOwner;
    private FloorType floorType;
    private RoofType roofType;
    private WallType wallType;
    private WoodType woodType;
    private Apartment apartment = null;
    private String pattaNumber;
    private Double currentCapitalValue;
    private Double marketValue;
    private String categoryType;
    private String occupancyCertificationNo;
    private Date occupancyCertificationDate;
    private transient Boolean appurtenantLandChecked = FALSE;
    private Boolean corrAddressDiff;
    private PropertyDepartment propertyDepartment;
    private VacantLandPlotArea vacantLandPlotArea;
    private LayoutApprovalAuthority layoutApprovalAuthority;
    private String layoutPermitNo;
    private Date layoutPermitDate;
    private String exemptionDetails;

    public VacantProperty(Area sitalArea, Area totalBuiltupArea, Area commBuiltUpArea, Area plinthArea,
            Area commVacantLand, Area nonResPlotArea, Boolean irregular, String surveyNumber, Character fieldVerified,
            Date fieldVerificationDate, List<Floor> floorDetails, Integer propertyDetailsID, String water_Meter_Num,
            String elec_Meter_Num, Integer no_of_floors, char fieldIrregular, Date dateOfCompletion, Property property,
            Date updatedTime, PropertyUsage propertyUsage, PropertyCreationReason creationReason,
            PropertyTypeMaster propertyTypeMaster, String propertyType, Installment installment,
            PropertyOccupation propertyOccupation, PropertyMutationMaster propertyMutationMaster, Character comZone,
            Character cornerPlot, Double extentSite, Double extentAppartenauntLand, FloorType floorType,
            RoofType roofType, WallType wallType, WoodType woodType, boolean lift, boolean toilets, boolean waterTap,
            boolean structure, boolean electricity, boolean attachedBathRoom, boolean waterHarvesting, boolean cable,
            String siteOwner, String pattaNumber, Double currentCapitalValue, Double marketValue, String categoryType,
            String occupancyCertificationNo,Date occupancyCertificationDate, Boolean appurtenantLandChecked, Boolean corrAddressDiff,
            PropertyDepartment propertyDepartment, VacantLandPlotArea vacantLandPlotArea,
            LayoutApprovalAuthority layoutApprovalAuthority, String layoutPermitNo, Date layoutPermitDate) {
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
        this.noofFloors = noofFloors;
        this.fieldIrregular = fieldIrregular;
        this.dateOfCompletion = dateOfCompletion;
        this.property = property;
        this.updatedTime = updatedTime;
        this.propertyUsage = propertyUsage;
        this.creationReason = creationReason;
        this.propertyTypeMaster = propertyTypeMaster;
        this.propertyType = propertyType;
        this.propertyOccupation = propertyOccupation;
        this.propertyMutationMaster = propertyMutationMaster;
        this.comZone = comZone;
        this.cornerPlot = cornerPlot;
        this.extentSite = extentSite;
        this.extentAppartenauntLand = extentAppartenauntLand;
        this.wallType = wallType;
        this.roofType = roofType;
        this.woodType = woodType;
        this.floorType = floorType;
        this.lift = lift;
        this.toilets = toilets;
        this.waterTap = waterTap;
        this.structure = structure;
        this.electricity = electricity;
        this.attachedBathRoom = attachedBathRoom;
        this.waterHarvesting = waterHarvesting;
        this.cable = cable;
        this.siteOwner = siteOwner;
        this.pattaNumber = pattaNumber;
        this.currentCapitalValue = currentCapitalValue;
        this.marketValue = marketValue;
        this.categoryType = categoryType;
        this.occupancyCertificationNo = occupancyCertificationNo;
        this.occupancyCertificationDate= occupancyCertificationDate;
        this.appurtenantLandChecked = appurtenantLandChecked;
        this.corrAddressDiff = corrAddressDiff;
        this.propertyDepartment = propertyDepartment;
        this.vacantLandPlotArea = vacantLandPlotArea;
        this.layoutApprovalAuthority = layoutApprovalAuthority;
        this.layoutPermitNo = layoutPermitNo;
        this.layoutPermitDate = layoutPermitDate;
    }

    @Override
    public Date getDateOfCompletion() {
        return dateOfCompletion;
    }

    @Override
    public void setDateOfCompletion(Date dateOfCompletion) {
        this.dateOfCompletion = dateOfCompletion;
    }

    @Override
    public void addFloor(Floor floor) {
        LOGGER.debug("BuildUpFloor.addFloor");
        if (floor != null) {
            getFloorDetails().add(floor);
            noofFloors = getFloorDetails().size();
        }
    }

    /**
     * This method removes the Floor Object from the Set view of the Floor
     * 
     * @param floor
     *            The floor to set .
     */
    @Override
    public void removeFloor(Floor floor) {
        LOGGER.debug("BuildUpFloor.removeFloor");
        getFloorDetails().remove(floor);
        noofFloors = getFloorDetails().size();
    }

    /**
     * @return Returns the commBuiltUpArea.
     */
    @Override
    public Area getCommBuiltUpArea() {
        return commBuiltUpArea;
    }

    /**
     * @param commBuiltUpArea
     *            The commBuiltUpArea to set.
     */
    @Override
    public void setCommBuiltUpArea(Area commBuiltUpArea) {
        this.commBuiltUpArea = commBuiltUpArea;
    }

    /**
     * @return Returns the commVacantLand.
     */
    @Override
    public Area getCommVacantLand() {
        return commVacantLand;
    }

    /**
     * @param commVacantLand
     *            The commVacantLand to set.
     */
    @Override
    public void setCommVacantLand(Area commVacantLand) {
        this.commVacantLand = commVacantLand;
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
     * @return Returns the elec_Meter_Num.
     */
    @Override
    public String getElec_Meter_Num() {
        return elec_Meter_Num;
    }

    /**
     * @param elec_Meter_Num
     *            The elec_Meter_Num to set.
     */
    @Override
    public void setElec_Meter_Num(String elec_Meter_Num) {
        this.elec_Meter_Num = elec_Meter_Num;
    }

    /**
     * @return Returns the fieldIrregular.
     */
    @Override
    public char getFieldIrregular() {
        return fieldIrregular;
    }

    /**
     * @param fieldIrregular
     *            The fieldIrregular to set.
     */
    @Override
    public void setFieldIrregular(char fieldIrregular) {
        this.fieldIrregular = fieldIrregular;
    }

    /**
     * @return Returns the fieldVerificationDate.
     */
    @Override
    public java.util.Date getFieldVerificationDate() {
        return fieldVerificationDate;
    }

    /**
     * @param fieldVerificationDate
     *            The fieldVerificationDate to set.
     */
    @Override
    public void setFieldVerificationDate(java.util.Date fieldVerificationDate) {
        this.fieldVerificationDate = fieldVerificationDate;
    }

    /**
     * @return Returns the fieldVerified.
     */
    @Override
    public Character getFieldVerified() {
        return fieldVerified;
    }

    /**
     * @param fieldVerified
     *            The fieldVerified to set.
     */
    @Override
    public void setFieldVerified(Character fieldVerified) {
        this.fieldVerified = fieldVerified;
    }

    /**
     * @return Returns the floorDetails.
     */
    @Override
    public java.util.List<Floor> getFloorDetails() {
        return floorDetails;
    }

    /**
     * @param floorDetails
     *            The floorDetails to set.
     */
    @Override
    public void setFloorDetails(java.util.List<Floor> floorDetails) {
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

    @Override
    public Integer getNoofFloors() {
        return noofFloors;
    }

    @Override
    public void setNoofFloors(Integer noofFloors) {
        this.noofFloors = noofFloors;
    }

    /**
     * @return Returns the plinthArea.
     */
    @Override
    public Area getPlinthArea() {
        return plinthArea;
    }

    /**
     * @param plinthArea
     *            The plinthArea to set.
     */
    @Override
    public void setPlinthArea(Area plinthArea) {
        this.plinthArea = plinthArea;
    }

    /**
     * @return Returns the property.
     */
    @Override
    public Property getProperty() {
        return property;
    }

    /**
     * @param property
     *            The property to set.
     */
    @Override
    public void setProperty(Property property) {
        this.property = property;
    }

    /**
     * @return Returns the propertyDetailsID.
     */
    @Override
    public Integer getPropertyDetailsID() {
        return propertyDetailsID;
    }

    /**
     * @param propertyDetailsID
     *            The propertyDetailsID to set.
     */
    @Override
    public void setPropertyDetailsID(Integer propertyDetailsID) {
        this.propertyDetailsID = propertyDetailsID;
    }

    /**
     * @return Returns the propertyTypeMaster.
     */
    @Override
    public PropertyTypeMaster getPropertyTypeMaster() {
        return propertyTypeMaster;
    }

    /**
     * @param propertyTypeMaster
     *            The propertyTypeMaster to set.
     */
    @Override
    public void setPropertyTypeMaster(PropertyTypeMaster propertyTypeMaster) {
        this.propertyTypeMaster = propertyTypeMaster;
    }

    /**
     * @return Returns the propertyUsage.
     */
    @Override
    public PropertyUsage getPropertyUsage() {
        return propertyUsage;
    }

    /**
     * @param propertyUsage
     *            The propertyUsage to set.
     */
    @Override
    public void setPropertyUsage(PropertyUsage propertyUsage) {
        this.propertyUsage = propertyUsage;
    }

    /**
     * @return Returns the sitalArea.
     */
    @Override
    public Area getSitalArea() {
        return sitalArea;
    }

    /**
     * @param sitalArea
     *            The sitalArea to set.
     */
    @Override
    public void setSitalArea(Area sitalArea) {
        this.sitalArea = sitalArea;
    }

    /**
     * @return Returns the surveyNumber.
     */
    @Override
    public String getSurveyNumber() {
        return surveyNumber;
    }

    /**
     * @param surveyNumber
     *            The surveyNumber to set.
     */
    @Override
    public void setSurveyNumber(String surveyNumber) {
        this.surveyNumber = surveyNumber;
    }

    /**
     * @return Returns the totalBuiltupArea.
     */
    @Override
    public Area getTotalBuiltupArea() {
        return totalBuiltupArea;
    }

    /**
     * @param totalBuiltupArea
     *            The totalBuiltupArea to set.
     */
    @Override
    public void setTotalBuiltupArea(Area totalBuiltupArea) {
        this.totalBuiltupArea = totalBuiltupArea;
    }

    /**
     * @return Returns the updatedTime.
     */
    @Override
    public Date getUpdatedTime() {
        return updatedTime;
    }

    /**
     * @param updatedTime
     *            The updatedTime to set.
     */
    @Override
    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    /**
     * @return Returns the water_Meter_Num.
     */
    @Override
    public String getWater_Meter_Num() {
        return water_Meter_Num;
    }

    /**
     * @param water_Meter_Num
     *            The water_Meter_Num to set.
     */
    @Override
    public void setWater_Meter_Num(String water_Meter_Num) {
        this.water_Meter_Num = water_Meter_Num;
    }

    /**
     * @return Returns the propertyType.
     */
    @Override
    public String getPropertyType() {
        return propertyType;
    }

    /**
     * @param propertyType
     *            The propertyType to set.
     */
    @Override
    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    /**
     * @return Returns if the given Object is equal to PropertyImpl
     */
    @Override
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
    @Override
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
         * ApplicationRuntimeException(
         * "VacantProperty.validate : PropertyUsage Validate() failed, Please Check !!"
         * );
         */
        return true;
    }

    /**
     * @return Returns the propertyMutationMaster.
     */
    @Override
    public PropertyMutationMaster getPropertyMutationMaster() {
        return propertyMutationMaster;
    }

    /**
     * @param propertyMutationMaster
     *            The propertyMutationMaster to set.
     */
    @Override
    public void setPropertyMutationMaster(PropertyMutationMaster propertyMutationMaster) {
        this.propertyMutationMaster = propertyMutationMaster;
    }

    @Override
    public Character getComZone() {
        return comZone;
    }

    @Override
    public void setComZone(Character comZone) {
        this.comZone = comZone;
    }

    @Override
    public Character getCornerPlot() {
        return cornerPlot;
    }

    @Override
    public void setCornerPlot(Character cornerPlot) {
        this.cornerPlot = cornerPlot;
    }

    @Override
    public PropertyOccupation getPropertyOccupation() {
        return propertyOccupation;
    }

    @Override
    public void setPropertyOccupation(PropertyOccupation propertyOccupation) {
        this.propertyOccupation = propertyOccupation;
    }

    public VacantProperty() {
        super();
    }

    @Override
    public String toString() {
        StringBuilder objStr = new StringBuilder();

        objStr.append("Id: ").append(getId()).append("|Sital Area: ").append(getSitalArea().getArea())
                .append("|NoOfFloors: ").append(getNoofFloors());

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
    public Double getExtentSite() {
        return extentSite;
    }

    @Override
    public void setExtentSite(Double extentSite) {
        this.extentSite = extentSite;
    }

    @Override
    public Double getExtentAppartenauntLand() {
        return extentAppartenauntLand;
    }

    @Override
    public void setExtentAppartenauntLand(Double extentAppartenauntLand) {
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

    @Override
    public FloorType getFloorType() {
        return floorType;
    }

    @Override
    public void setFloorType(FloorType floorType) {
        this.floorType = floorType;
    }

    @Override
    public RoofType getRoofType() {
        return roofType;
    }

    @Override
    public void setRoofType(RoofType roofType) {
        this.roofType = roofType;
    }

    @Override
    public WallType getWallType() {
        return wallType;
    }

    @Override
    public void setWallType(WallType wallType) {
        this.wallType = wallType;
    }

    @Override
    public WoodType getWoodType() {
        return woodType;
    }

    @Override
    public void setWoodType(WoodType woodType) {
        this.woodType = woodType;
    }

    @Override
    public Apartment getApartment() {
        return apartment;
    }

    @Override
    public void setApartment(Apartment apartment) {
        this.apartment = apartment;
    }

    @Override
    public String getPattaNumber() {
        return pattaNumber;
    }

    @Override
    public void setPattaNumber(String pattaNumber) {
        this.pattaNumber = pattaNumber;
    }

    @Override
    public Double getCurrentCapitalValue() {
        return currentCapitalValue;
    }

    @Override
    public void setCurrentCapitalValue(Double currentCapitalValue) {
        this.currentCapitalValue = currentCapitalValue;
    }

    @Override
    public Double getMarketValue() {
        return marketValue;
    }

    @Override
    public void setMarketValue(Double marketValue) {
        this.marketValue = marketValue;
    }

    @Override
    public String getCategoryType() {
        return categoryType;
    }

    @Override
    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    @Override
    public String getOccupancyCertificationNo() {
        return occupancyCertificationNo;
    }

    @Override
    public void setOccupancyCertificationNo(String occupancyCertificationNo) {
        this.occupancyCertificationNo = occupancyCertificationNo;
    }
    
    @Override
    public Date getOccupancyCertificationDate() {
        return occupancyCertificationDate;
    }

    @Override
    public void setOccupancyCertificationDate(Date occupancyCertificationDate) {
        this.occupancyCertificationDate = occupancyCertificationDate;
    }

    @Override
    public Boolean isAppurtenantLandChecked() {
        return appurtenantLandChecked;
    }

    @Override
    public void setAppurtenantLandChecked(Boolean appurtenantLandChecked) {
        this.appurtenantLandChecked = appurtenantLandChecked;
    }

    @Override
    public java.util.List<Floor> getFloorDetailsProxy() {
        return floorDetailsProxy;
    }

    @Override
    public void setFloorDetailsProxy(java.util.List<Floor> floorDetailsProxy) {
        this.floorDetailsProxy = floorDetailsProxy;
    }

    @Override
    public Boolean isCorrAddressDiff() {
        return corrAddressDiff;
    }

    @Override
    public void setCorrAddressDiff(Boolean corrAddressDiff) {
        this.corrAddressDiff = corrAddressDiff;
    }

    @Override
    public PropertyDepartment getPropertyDepartment() {
        return propertyDepartment;
    }

    @Override
    public void setPropertyDepartment(PropertyDepartment propertyDepartment) {
        this.propertyDepartment = propertyDepartment;
    }

    @Override
    public VacantLandPlotArea getVacantLandPlotArea() {
        return vacantLandPlotArea;
    }

    @Override
    public void setVacantLandPlotArea(VacantLandPlotArea vacantLandPlotArea) {
        this.vacantLandPlotArea = vacantLandPlotArea;
    }

    @Override
    public LayoutApprovalAuthority getLayoutApprovalAuthority() {
        return layoutApprovalAuthority;
    }

    @Override
    public void setLayoutApprovalAuthority(LayoutApprovalAuthority layoutApprovalAuthority) {
        this.layoutApprovalAuthority = layoutApprovalAuthority;
    }

    @Override
    public String getLayoutPermitNo() {
        return layoutPermitNo;
    }

    @Override
    public void setLayoutPermitNo(String layoutPermitNo) {
        this.layoutPermitNo = layoutPermitNo;
    }

    @Override
    public Date getLayoutPermitDate() {
        return layoutPermitDate;
    }

    @Override
    public void setLayoutPermitDate(Date layoutPermitDate) {
        this.layoutPermitDate = layoutPermitDate;
    }

    public String getExemptionDetails() {
        return exemptionDetails;
    }

    public void setExemptionDetails(String exemptionDetails) {
        this.exemptionDetails = exemptionDetails;
    }

}