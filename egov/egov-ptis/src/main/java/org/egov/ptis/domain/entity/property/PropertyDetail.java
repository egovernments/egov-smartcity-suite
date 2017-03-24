/*
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
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
 */


package org.egov.ptis.domain.entity.property;

import org.egov.commons.Area;
import org.egov.ptis.domain.entity.property.vacantland.LayoutApprovalAuthority;
import org.egov.ptis.domain.entity.property.vacantland.VacantLandPlotArea;

import java.util.Date;
import java.util.List;

/**
 * This is Interface for the Property Detail which contains all the details; like Area, , length, breadth, floor details etc which
 * gives the complete statistical information about the Individual Property and are required to calculate the Property Tax for ny
 * Property Each PropertyDetail is associated with Property Class
 * 
 * @author Neetu
 * @version 2.00
 * @see org.egov.ptis.property.model.BuitUpPropertyImpl org.egov.ptis.property.model.VacantPropertyImpl
 * org.egov.ptis.property.model.Property
 * 
 */
public interface PropertyDetail extends Property {

    public Property property = null;

    public PropertyTypeMaster getPropertyTypeMaster();

    public void setPropertyTypeMaster(PropertyTypeMaster propertyTypeMaster);

    public Date getDateOfCompletion();

    public void setDateOfCompletion(Date dateOfCompletion);

    public void addFloor(Floor floor);

    /**
     * This method removes the Floor Object from the Set view of the Floor
     * 
     * @param floor The floor to set .
     */
    public void removeFloor(Floor floor);

    public Integer getNoofFloors();

    public void setNoofFloors(Integer noofFloors);

    /**
     * @return Returns the Water_Meter_Num
     */
    public String getWater_Meter_Num();

    /**
     * @param Water_Meter_Num The Water_Meter_Num to set.
     */
    public void setWater_Meter_Num(String Water_Meter_Num);

    /**
     * @return Returns the Elec_Meter_Num
     */
    public String getElec_Meter_Num();

    /**
     * @param Water_Meter_Num The Water_Meter_Num to set.
     */
    public void setElec_Meter_Num(String Elec_Meter_Num);

    /**
     * @return Returns the PropertyDetailsID
     */
    public Integer getPropertyDetailsID();

    /**
     * @param propertyDetailsID The propertyDetailsID to set.
     */
    public void setPropertyDetailsID(Integer propertyDetailsID);

    /**
     * @return Returns the Property
     */
    public Property getProperty();

    /**
     * @param Property The Property
     */
    public void setProperty(Property property);

    /**
     * @return Returns the Set for FloorDetails.
     */
    public List<Floor> getFloorDetails();

    /**
     * @param floorDetails The Set view of floorDetails to set.
     */
    public void setFloorDetails(List<Floor> floorDetails);

    /**
     * @return Returns the Sital Area.
     */
    public Area getSitalArea();

    /**
     * @param sitalArea The SitalArea to set.
     */
    public void setSitalArea(Area sitalArea);

    /**
     * @param area The PlinthArea to set.
     */

    public void setPlinthArea(Area area);

    /**
     * @return Returns the Plinth Area.
     */

    public Area getPlinthArea();

    /**
     * @return Returns the Total Built Up Area.
     */
    public Area getTotalBuiltupArea();

    /**
     * @param area The TotalBuiltUpArea to set.
     */
    public void setTotalBuiltupArea(Area area);

    /**
     * @return Returns the CommBuiltUp Area.
     */

    public Area getCommBuiltUpArea();

    /**
     * @param area The CommBuiltUpArea to set.
     */

    public void setCommBuiltUpArea(Area area);

    /**
     * @return Returns the CommVacantLand Area.
     */

    public Area getCommVacantLand();

    /**
     * @param area The CommVacantLand to set.
     */

    public void setCommVacantLand(Area area);

    /**
     * @return Returns SurveyNumber
     */
    public String getSurveyNumber();

    /**
     * @param surveyNumber The surveyNumber to set.
     */
    public void setSurveyNumber(String surveyNumber);

    public Character getFieldVerified();

    /**
     * @param fieldVerified The fieldVerified to set.
     */
    public void setFieldVerified(Character fieldVerified);

    /**
     * @param Boolean fieldVerified The fieldVerified to set.
     */

    /**
     * @return Returns Date for FieldVerification
     */
    public java.util.Date getFieldVerificationDate();

    /**
     * @param fieldVerificationDate The fieldVerificationDate to set.
     */
    public void setFieldVerificationDate(java.util.Date fieldVerificationDate);

    /**
     * @return Returns char FieldIrregular
     */
    public char getFieldIrregular();

    /**
     * @param char fieldIrregular The fieldIrregular to set.
     */
    public void setFieldIrregular(char fieldIrregular);

    /**
     * @return Returns PropertyUsage
     */
    public PropertyUsage getPropertyUsage();

    /**
     * @param Boolean propertyUsage The propertyUsage to set.
     */
    public void setPropertyUsage(PropertyUsage propertyUsage);

    public void setUpdatedTime(Date updatedTime);

    public Date getUpdatedTime();

    public void setPropertyType(String propertyType);

    public String getPropertyType();

    public void setPropertyMutationMaster(PropertyMutationMaster propertyMutationMaster);

    public PropertyMutationMaster getPropertyMutationMaster();

    public Character getComZone();

    public void setComZone(Character comZone);

    public Character getCornerPlot();

    public void setCornerPlot(Character cornerPlot);

    public PropertyOccupation getPropertyOccupation();

    public void setPropertyOccupation(PropertyOccupation propertyOccupation);

    public Area getNonResPlotArea();

    public void setNonResPlotArea(Area nonResPlotArea);

    public boolean isLift();

    public void setLift(boolean lift);

    public boolean isToilets();

    public void setToilets(boolean toilets);

    public boolean isWaterTap();

    public void setWaterTap(boolean waterTap);

    public boolean isStructure();

    public void setStructure(boolean structure);

    public boolean isElectricity();

    public void setElectricity(boolean electricity);

    public boolean isAttachedBathRoom();

    public void setAttachedBathRoom(boolean attachedBathRoom);

    public boolean isWaterHarvesting();

    public void setWaterHarvesting(boolean waterHarvesting);

    public boolean isCable();

    public void setCable(boolean cable);

    public Double getExtentSite();

    public void setExtentSite(Double extentSite);

    public Double getExtentAppartenauntLand();

    public void setExtentAppartenauntLand(Double extentAppartenauntLand);

    public String getSiteOwner();

    public void setSiteOwner(String siteOwner);

    public FloorType getFloorType();

    public void setFloorType(FloorType floorType);

    public RoofType getRoofType();

    public void setRoofType(RoofType roofType);

    public WallType getWallType();

    public void setWallType(WallType wallType);

    public WoodType getWoodType();

    public void setWoodType(WoodType woodType);

    public Apartment getApartment();

    public void setApartment(Apartment apartment);

    public String getPattaNumber();

    public void setPattaNumber(String pattaNumber);

    public Double getCurrentCapitalValue();

    public void setCurrentCapitalValue(Double currentCapitalValue);

    public Double getMarketValue();

    public void setMarketValue(Double marketValue);

    public String getCategoryType();

    public void setCategoryType(String categoryType);

    public String getOccupancyCertificationNo();

    public void setOccupancyCertificationNo(String occupancyCertificationNo);

    public Boolean isAppurtenantLandChecked();

    public void setAppurtenantLandChecked(Boolean appurtenantLandChecked);

    public List<Floor> getFloorDetailsProxy();

    public void setFloorDetailsProxy(List<Floor> floorDetailsProxy);

    public Boolean isCorrAddressDiff();

    public void setCorrAddressDiff(Boolean corrAddressDiff);
    
    public PropertyDepartment getPropertyDepartment();
    
    public void setPropertyDepartment(PropertyDepartment propertyDepartment);
    
    public VacantLandPlotArea getVacantLandPlotArea();
    
    public void setVacantLandPlotArea(VacantLandPlotArea vacantLandPlotArea);
    
    public LayoutApprovalAuthority getLayoutApprovalAuthority();
    
    public void setLayoutApprovalAuthority(LayoutApprovalAuthority layoutApprovalAuthority);
    
    public String getLayoutPermitNo();
    
    public void setLayoutPermitNo(String layoutPermitNo);
    
    public Date getLayoutPermitDate();
    
    public void setLayoutPermitDate(Date layoutPermitDate);
    
    public String getExemptionDetails();
    
    public void setExemptionDetails(String exemptionDetails);
}
