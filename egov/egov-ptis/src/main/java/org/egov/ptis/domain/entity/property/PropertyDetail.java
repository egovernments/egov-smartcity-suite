/*
 * PropertyDetail.java Created on Oct 21, 2005
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.ptis.domain.entity.property;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.egov.commons.Area;
import org.egov.commons.Installment;

/**
 * This is Interface for the Property Detail which contains all the details;
 * like Area, , length, breadth, floor details etc which gives the complete
 * statistical information about the Individual Property and are required to
 * calculate the Property Tax for ny Property Each PropertyDetail is associated
 * with Property Class
 * 
 * @author Neetu
 * @version 2.00
 * @see org.egov.ptis.property.model.BuitUpPropertyImpl
 *      org.egov.ptis.property.model.VacantPropertyImpl
 *      org.egov.ptis.property.model.Property
 * 
 */
public interface PropertyDetail extends Property {

	public Property property = null;

	public PropertyTypeMaster getPropertyTypeMaster();

	public void setPropertyTypeMaster(PropertyTypeMaster propertyTypeMaster);

	public Date getCompletion_year();

	public void setCompletion_year(Date completion_year);

	public Date getEffective_date();

	public void setEffective_date(Date effective_date);

	public Date getDateOfCompletion();

	public void setDateOfCompletion(Date dateOfCompletion);

	public void addFloor(FloorIF floor);

	/**
	 * This method removes the Floor Object from the Set view of the Floor
	 * 
	 * @param floor
	 *            The floor to set .
	 */
	public void removeFloor(FloorIF floor);

	public Integer getNo_of_floors();

	/**
	 * @param no_of_floors
	 *            The no_of_floors to set.
	 */
	public void setNo_of_floors(Integer no_of_floors);

	/**
	 * @return Returns the Water_Meter_Num
	 */
	public String getWater_Meter_Num();

	/**
	 * @param Water_Meter_Num
	 *            The Water_Meter_Num to set.
	 */
	public void setWater_Meter_Num(String Water_Meter_Num);

	/**
	 * @return Returns the Elec_Meter_Num
	 */
	public String getElec_Meter_Num();

	/**
	 * @param Water_Meter_Num
	 *            The Water_Meter_Num to set.
	 */
	public void setElec_Meter_Num(String Elec_Meter_Num);

	/**
	 * @return Returns the PropertyDetailsID
	 */
	public Integer getPropertyDetailsID();

	/**
	 * @param propertyDetailsID
	 *            The propertyDetailsID to set.
	 */
	public void setPropertyDetailsID(Integer propertyDetailsID);

	/**
	 * @return Returns the Property
	 */
	public Property getProperty();

	/**
	 * @param Property
	 *            The Property
	 */
	public void setProperty(Property property);

	/**
	 * @return Returns the Set for FloorDetails.
	 */
	public Set<FloorIF> getFloorDetails();

	/**
	 * @param floorDetails
	 *            The Set view of floorDetails to set.
	 */
	public void setFloorDetails(Set<FloorIF> floorDetails);

	/**
	 * @return Returns the List for FloorDetailsProxy.
	 */
	public List<FloorImpl> getFloorDetailsProxy();

	/**
	 * @param floorDetails
	 *            The Set view of floorDetailsProxy to List.
	 */
	public void setFloorDetailsProxy(List<FloorImpl> floorDetailsProxy);

	/**
	 * @return Returns the Sital Area.
	 */
	public Area getSitalArea();

	/**
	 * @param sitalArea
	 *            The SitalArea to set.
	 */
	public void setSitalArea(Area sitalArea);

	/**
	 * @param area
	 *            The PlinthArea to set.
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
	 * @param area
	 *            The TotalBuiltUpArea to set.
	 */
	public void setTotalBuiltupArea(Area area);

	/**
	 * @return Returns the CommBuiltUp Area.
	 */

	public Area getCommBuiltUpArea();

	/**
	 * @param area
	 *            The CommBuiltUpArea to set.
	 */

	public void setCommBuiltUpArea(Area area);

	/**
	 * @return Returns the CommVacantLand Area.
	 */

	public Area getCommVacantLand();

	/**
	 * @param area
	 *            The CommVacantLand to set.
	 */

	public void setCommVacantLand(Area area);

	/**
	 * @return Returns SurveyNumber
	 */
	public String getSurveyNumber();

	/**
	 * @param surveyNumber
	 *            The surveyNumber to set.
	 */
	public void setSurveyNumber(String surveyNumber);

	public Character getFieldVerified();

	/**
	 * @param fieldVerified
	 *            The fieldVerified to set.
	 */
	public void setFieldVerified(Character fieldVerified);

	/**
	 * @param Boolean
	 *            fieldVerified The fieldVerified to set.
	 */

	/**
	 * @return Returns Date for FieldVerification
	 */
	public java.util.Date getFieldVerificationDate();

	/**
	 * @param fieldVerificationDate
	 *            The fieldVerificationDate to set.
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
	 * @param Boolean
	 *            propertyUsage The propertyUsage to set.
	 */
	public void setPropertyUsage(PropertyUsage propertyUsage);

	public void setUpdatedTime(Date updatedTime);

	public Date getUpdatedTime();

	public void setPropertyType(String propertyType);

	public String getPropertyType();

	public Installment getInstallment();

	public void setInstallment(Installment installment);

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
	
}
