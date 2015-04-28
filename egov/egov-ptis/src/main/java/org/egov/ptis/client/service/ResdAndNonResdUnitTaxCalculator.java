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
package org.egov.ptis.client.service;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static java.math.BigDecimal.ZERO;
import static org.egov.ptis.constants.PropertyTaxConstants.AREA_CONSTANT_FOR_RESIDENTIAL_BUILDING;
import static org.egov.ptis.constants.PropertyTaxConstants.BASEMENT;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_GENERAL_WATER_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.FLOOR_TYPES;
import static org.egov.ptis.constants.PropertyTaxConstants.GODOWN;
import static org.egov.ptis.constants.PropertyTaxConstants.GWR_IMPOSED;
import static org.egov.ptis.constants.PropertyTaxConstants.OPEN_PLOT_UNIT_FLOORNUMBER;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_CAT_NON_RESD;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_CAT_RESD;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_NON_RESD;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_RESD;
import static org.egov.ptis.constants.PropertyTaxConstants.STANDARD_DEDUCTION_PERCENTAGE;
import static org.egov.ptis.constants.PropertyTaxConstants.TENANT;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.Installment;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.client.model.ApplicableFactor;
import org.egov.ptis.client.model.AreaTaxCalculationInfo;
import org.egov.ptis.client.model.UnitTaxCalculationInfo;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.domain.entity.property.BoundaryCategory;
import org.egov.ptis.domain.entity.property.FloorImpl;
import org.egov.ptis.domain.entity.property.Property;

/**
 *
 * @author subhash
 *
 */
public class ResdAndNonResdUnitTaxCalculator {

	private PropertyTaxUtil propertyTaxUtil;
	private PersistenceService persistenceService;
	private static final Logger LOGGER = Logger.getLogger(ResdAndNonResdUnitTaxCalculator.class);

	public void setPropertyTaxUtil(PropertyTaxUtil propertyTaxUtil) {
		this.propertyTaxUtil = propertyTaxUtil;
	}

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	public UnitTaxCalculationInfo calculateUnitTax(UnitTaxCalculationInfo unitTaxCalculationInfo, FloorImpl floor,
			Installment installment, Long categoryId, Boolean isExemptedFromTax,
			Property property, BoundaryCategory category) {
		LOGGER.debug("Entered into ResdAndNonResdUnitTaxCalculator: calculateUnitTax method");
		LOGGER.debug("Floor : " + floor);

		BigDecimal grossMonthlyRentUnitWise = ZERO;
		Double remainingArea;
		BigDecimal areaTax = ZERO;
		BigDecimal unitAreaFactor = ZERO;
		BigDecimal baseRent = unitTaxCalculationInfo.getBaseRent();
		String usageName = floor.getPropertyUsage().getUsageName();

		Double accessableArea = Double.valueOf(Float.toString(floor.getBuiltUpArea().getArea()));

		Boolean isResidentialUnit = PropertyTaxUtil.isResidentialUnit(usageName);
		Boolean isNonResidentialUnit = PropertyTaxUtil.isNonResidentialUnit(usageName);
		Boolean isOpenPlotUnit = PropertyTaxUtil.isOpenPlotUnit(usageName);

		List<String> applicableTaxes = PropertyTaxUtil.prepareApplicableTaxes(property);

		if (floor.getWaterRate().equals(GWR_IMPOSED)) {
			applicableTaxes.add(DEMANDRSN_CODE_GENERAL_WATER_TAX);
		}

		LOGGER.debug("calculatePropertyTax - unitNo = " + floor.getExtraField1() + ", floor="
				+ floor.getFloorNo() + ", applicableTaxes = " + applicableTaxes);

		unitTaxCalculationInfo = this.addMixedUnitInfo(floor, unitTaxCalculationInfo);

		if (isOpenPlotUnit) {
			if (PROPTYPE_CAT_RESD.equalsIgnoreCase(floor.getUnitTypeCategory())) {
				applicableTaxes.add(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD);
			} else if (PROPTYPE_CAT_NON_RESD.equalsIgnoreCase(floor.getUnitTypeCategory())) {
				applicableTaxes.add(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD);
				applicableTaxes.add(DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX);
			}
		} else if (isResidentialUnit) {
			applicableTaxes.add(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD);
		} else if (isNonResidentialUnit) {
			applicableTaxes.add(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD);
			applicableTaxes.add(DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX);
		}

		boolean result = false;
		List<String> exemptedTaxes = null;

		if (floor.getTaxExemptedReason() != null) {
			exemptedTaxes = PropertyTaxConstants.exemptedTaxesByReason.get(floor.getTaxExemptedReason());
			result = applicableTaxes.removeAll(exemptedTaxes);
		}

		if (result) {
			LOGGER.debug("calculateUnitTax - Unit is exempted from taxes " + exemptedTaxes);
		} else {
			LOGGER.debug("calculateUnitTax - Unit is not exempted from taxes ");
		}

		/*
		 * Commented because we should not apply any factors on guidance value
		 * for rent chart method (dated 29/06/2012)
		 */

		/*
		 * List<ApplicableFactor> applicableFactors =
		 * propertyTaxUtil.getApplicableFactorsForResidentialAndNonResidential(
		 * floor, propertyArea, installment, categoryId);
		 */

		List<ApplicableFactor> applicableFactors = Collections.emptyList();

		LOGGER.debug("baseRent before reducing by 50% : " + baseRent);
		if (FLOOR_TYPES.get(floor.getExtraField7()) != null) {
			baseRent = baseRent.divide(new BigDecimal(2));
		}
		LOGGER.debug("baseRent after reducing by 50%: " + baseRent);

		if (baseRent == ZERO && (floor.getManualAlv() == null || floor.getManualAlv().equals(""))) {
			LOGGER.debug("Base rent value for the installment : " + installment + " is not available in Rent Chart");
			throw new EGOVRuntimeException("Base rent value for the installment : " + installment
					+ " is not available in Rent Chart");
		} else if (floor.getManualAlv() != null && !floor.getManualAlv().equals("")) {
			LOGGER.info("Using Manual ALV in tax calculation for the installment : " + installment);
			unitTaxCalculationInfo.setAnnualRentAfterDeduction(floor.getManualAlv());
			unitTaxCalculationInfo.setManualAlv(floor.getManualAlv().toString());
		} else {

			BigDecimal baseRentPerSqMtPerMonth = propertyTaxUtil.calculateBaseRentPerSqMtPerMonth(applicableFactors,
					baseRent);

			BigDecimal baseRentForCurrentArea = ZERO;

			if (isResidentialUnit) {
				LOGGER.debug("Floor Usage : Residential");

				if (floor.getExtraField7() == null
						|| (floor.getExtraField7() != null && !FLOOR_TYPES.get(floor.getExtraField7()).equals(BASEMENT) && !FLOOR_TYPES
								.get(floor.getExtraField7()).equals(GODOWN))) {
					/**
					 * Case 1: (Residential Building, Ground Floor) If Floor
					 * Area is greater than 46.45 then apply full rate for first
					 * 46.45 sqmt. 80% for next 46.45 sqmt, 70% for next 46.45
					 * sqmt and for remaining area exceeding 139.35 sqmt 60% of
					 * full rate will be charged. (Assuming unit number is
					 * unique for each floor)
					 */

					if (accessableArea != null && accessableArea.intValue() != 0) {
						/**
						 * For first 46.45sqmt, full base rate will be applied
						 * (Step 1)
						 */
						if (accessableArea >= AREA_CONSTANT_FOR_RESIDENTIAL_BUILDING) {

							unitAreaFactor = new BigDecimal(propertyTaxUtil.multiplicativeFactorAreaWise(PROPTYPE_RESD,
									floor, "46.45", null));

							baseRentForCurrentArea = baseRentPerSqMtPerMonth.multiply(unitAreaFactor).setScale(2,
									BigDecimal.ROUND_HALF_UP);

							areaTax = baseRentForCurrentArea.multiply(new BigDecimal(
									AREA_CONSTANT_FOR_RESIDENTIAL_BUILDING));

							areaTax = areaTax.setScale(0, ROUND_HALF_UP);

							AreaTaxCalculationInfo areaTaxCalculationInfo = new AreaTaxCalculationInfo();
							areaTaxCalculationInfo
									.setTaxableArea(new BigDecimal(AREA_CONSTANT_FOR_RESIDENTIAL_BUILDING).setScale(2,
											ROUND_HALF_UP));
							areaTaxCalculationInfo.setMonthlyBaseRent(baseRentForCurrentArea);
							areaTaxCalculationInfo.setCalculatedTax(areaTax);
							unitTaxCalculationInfo.addAreaTaxCalculationInfo(areaTaxCalculationInfo);

							grossMonthlyRentUnitWise = grossMonthlyRentUnitWise.add(areaTax);

							remainingArea = accessableArea - AREA_CONSTANT_FOR_RESIDENTIAL_BUILDING;
							LOGGER.debug("for 1st 46.45 mtrs areaTax " + grossMonthlyRentUnitWise);

							/**
							 * For next 46.45 sqmt, calculate tax @80% of base
							 * rate(Step 2)
							 */
							if (remainingArea != 0 && remainingArea >= AREA_CONSTANT_FOR_RESIDENTIAL_BUILDING) {

								unitAreaFactor = new BigDecimal(propertyTaxUtil.multiplicativeFactorAreaWise(
										PROPTYPE_RESD, floor, "93", null));

								baseRentForCurrentArea = baseRentPerSqMtPerMonth.multiply(unitAreaFactor).setScale(2,
										BigDecimal.ROUND_HALF_UP);

								areaTax = baseRentForCurrentArea.multiply(new BigDecimal(
										AREA_CONSTANT_FOR_RESIDENTIAL_BUILDING));

								areaTax = areaTax.setScale(0, ROUND_HALF_UP);

								AreaTaxCalculationInfo areaTaxCalculationInfo2 = new AreaTaxCalculationInfo();
								areaTaxCalculationInfo2.setTaxableArea(new BigDecimal(
										AREA_CONSTANT_FOR_RESIDENTIAL_BUILDING).setScale(2, ROUND_HALF_UP));
								areaTaxCalculationInfo2.setMonthlyBaseRent(baseRentForCurrentArea);
								areaTaxCalculationInfo2.setCalculatedTax(areaTax);
								unitTaxCalculationInfo.addAreaTaxCalculationInfo(areaTaxCalculationInfo2);

								grossMonthlyRentUnitWise = grossMonthlyRentUnitWise.add(areaTax);

								remainingArea = remainingArea - AREA_CONSTANT_FOR_RESIDENTIAL_BUILDING;
								LOGGER.debug("for 1st 93 mtrs areaTax " + areaTax + " grossMonthlyRentUnitWise "
										+ grossMonthlyRentUnitWise);

							} else if (remainingArea != 0 && remainingArea < AREA_CONSTANT_FOR_RESIDENTIAL_BUILDING) {
								/**
								 * If remaining area less then 46.45 then
								 * calculate tax
								 *
								 * @80% of the base rate for the entire
								 *      remaining area
								 */

								unitAreaFactor = new BigDecimal(propertyTaxUtil.multiplicativeFactorAreaWise(
										PROPTYPE_RESD, floor, "93", null));

								baseRentForCurrentArea = baseRentPerSqMtPerMonth.multiply(unitAreaFactor).setScale(2,
										BigDecimal.ROUND_HALF_UP);

								areaTax = baseRentForCurrentArea.multiply(new BigDecimal(remainingArea));

								areaTax = areaTax.setScale(0, ROUND_HALF_UP);

								AreaTaxCalculationInfo areaTaxCalculationInfo3 = new AreaTaxCalculationInfo();
								areaTaxCalculationInfo3.setTaxableArea(new BigDecimal(remainingArea).setScale(2,
										ROUND_HALF_UP));
								areaTaxCalculationInfo3.setMonthlyBaseRent(baseRentForCurrentArea);
								areaTaxCalculationInfo3.setCalculatedTax(areaTax);
								unitTaxCalculationInfo.addAreaTaxCalculationInfo(areaTaxCalculationInfo3);

								grossMonthlyRentUnitWise = grossMonthlyRentUnitWise.add(areaTax);

								remainingArea = Double.valueOf("0");
							}
							/**
							 * For next 46.45 sqmt, calculate tax @70% of the
							 * base rate(Step 3)
							 */
							if (remainingArea != 0 && remainingArea >= AREA_CONSTANT_FOR_RESIDENTIAL_BUILDING) {

								unitAreaFactor = new BigDecimal(propertyTaxUtil.multiplicativeFactorAreaWise(
										PROPTYPE_RESD, floor, "139.5", null));

								baseRentForCurrentArea = baseRentPerSqMtPerMonth.multiply(unitAreaFactor).setScale(2,
										BigDecimal.ROUND_HALF_UP);

								areaTax = baseRentForCurrentArea.multiply(new BigDecimal(
										AREA_CONSTANT_FOR_RESIDENTIAL_BUILDING));

								areaTax = areaTax.setScale(0, ROUND_HALF_UP);

								AreaTaxCalculationInfo areaTaxCalculationInfo4 = new AreaTaxCalculationInfo();
								areaTaxCalculationInfo4.setTaxableArea(new BigDecimal(
										AREA_CONSTANT_FOR_RESIDENTIAL_BUILDING).setScale(2, ROUND_HALF_UP));
								areaTaxCalculationInfo4.setMonthlyBaseRent(baseRentForCurrentArea);
								areaTaxCalculationInfo4.setCalculatedTax(areaTax);
								unitTaxCalculationInfo.addAreaTaxCalculationInfo(areaTaxCalculationInfo4);

								grossMonthlyRentUnitWise = grossMonthlyRentUnitWise.add(areaTax);

								remainingArea = remainingArea - AREA_CONSTANT_FOR_RESIDENTIAL_BUILDING;
								LOGGER.debug("for 139.5 mtrs areaTax " + areaTax + " grossMonthlyRentUnitWise "
										+ grossMonthlyRentUnitWise);
							} else if (remainingArea != 0 && remainingArea < AREA_CONSTANT_FOR_RESIDENTIAL_BUILDING) {
								/**
								 * If remaining area less then 46.45 then
								 * calculate tax
								 *
								 * @70% of the base rate for the entire
								 *      remaining area
								 */
								unitAreaFactor = new BigDecimal(propertyTaxUtil.multiplicativeFactorAreaWise(
										PROPTYPE_RESD, floor, "139.5", null));

								baseRentForCurrentArea = baseRentPerSqMtPerMonth.multiply(unitAreaFactor).setScale(2,
										BigDecimal.ROUND_HALF_UP);

								areaTax = baseRentForCurrentArea.multiply(new BigDecimal(remainingArea));

								areaTax = areaTax.setScale(0, ROUND_HALF_UP);

								AreaTaxCalculationInfo areaTaxCalculationInfo5 = new AreaTaxCalculationInfo();
								areaTaxCalculationInfo5.setTaxableArea(new BigDecimal(remainingArea).setScale(2,
										ROUND_HALF_UP));
								areaTaxCalculationInfo5.setMonthlyBaseRent(baseRentForCurrentArea);
								areaTaxCalculationInfo5.setCalculatedTax(areaTax);
								unitTaxCalculationInfo.addAreaTaxCalculationInfo(areaTaxCalculationInfo5);

								grossMonthlyRentUnitWise = grossMonthlyRentUnitWise.add(areaTax);

								remainingArea = Double.valueOf("0");
							}
							/**
							 * For the entire remaining property, calculate tax
							 *
							 * @60% of the base rate(Step 4)
							 */
							if (remainingArea != 0) {

								unitAreaFactor = new BigDecimal(propertyTaxUtil.multiplicativeFactorAreaWise(
										PROPTYPE_RESD, floor, "139.6", null));

								baseRentForCurrentArea = baseRentPerSqMtPerMonth.multiply(unitAreaFactor).setScale(2,
										BigDecimal.ROUND_HALF_UP);

								areaTax = baseRentForCurrentArea.multiply(new BigDecimal(remainingArea));

								areaTax = areaTax.setScale(0, ROUND_HALF_UP);

								AreaTaxCalculationInfo areaTaxCalculationInfo6 = new AreaTaxCalculationInfo();
								areaTaxCalculationInfo6.setTaxableArea(new BigDecimal(remainingArea).setScale(2,
										ROUND_HALF_UP));
								areaTaxCalculationInfo6.setMonthlyBaseRent(baseRentForCurrentArea);
								areaTaxCalculationInfo6.setCalculatedTax(areaTax);
								unitTaxCalculationInfo.addAreaTaxCalculationInfo(areaTaxCalculationInfo6);

								grossMonthlyRentUnitWise = grossMonthlyRentUnitWise.add(areaTax);
								LOGGER.debug("for 139.6 mtrs areaTax " + areaTax + " grossMonthlyRentUnitWise "
										+ grossMonthlyRentUnitWise);
							}
						}
						/**
						 * If the area of the property is less than 46.45 then
						 * apply full base rate for the entire property
						 */
						else if (accessableArea != 0 && accessableArea < AREA_CONSTANT_FOR_RESIDENTIAL_BUILDING) {
							unitAreaFactor = new BigDecimal(propertyTaxUtil.multiplicativeFactorAreaWise(PROPTYPE_RESD,
									floor, "46.45", null));

							baseRentForCurrentArea = baseRentPerSqMtPerMonth.multiply(unitAreaFactor).setScale(2,
									BigDecimal.ROUND_HALF_UP);

							areaTax = baseRentForCurrentArea.multiply(new BigDecimal(accessableArea));

							areaTax = areaTax.setScale(0, ROUND_HALF_UP);

							AreaTaxCalculationInfo areaTaxCalculationInfo7 = new AreaTaxCalculationInfo();
							areaTaxCalculationInfo7.setTaxableArea(new BigDecimal(accessableArea).setScale(2,
									ROUND_HALF_UP));
							areaTaxCalculationInfo7.setMonthlyBaseRent(baseRentForCurrentArea);
							areaTaxCalculationInfo7.setCalculatedTax(areaTax);
							unitTaxCalculationInfo.addAreaTaxCalculationInfo(areaTaxCalculationInfo7);

							grossMonthlyRentUnitWise = grossMonthlyRentUnitWise.add(areaTax);
						}
					}
				}
				/**
				 * If the Floor Type(Non Mandatory Field) is either Basement or
				 * Godown, then apply full rate for the total area
				 */
				else if (floor.getExtraField7() != null
						&& (FLOOR_TYPES.get(floor.getExtraField7()).equals(BASEMENT) || FLOOR_TYPES.get(
								floor.getExtraField7()).equals(GODOWN))) {
					grossMonthlyRentUnitWise = baseRentPerSqMtPerMonth.multiply(new BigDecimal(accessableArea));

					AreaTaxCalculationInfo areaTaxCalculationInfo8 = new AreaTaxCalculationInfo();
					areaTaxCalculationInfo8.setTaxableArea(new BigDecimal(accessableArea).setScale(2,
							BigDecimal.ROUND_HALF_UP));
					areaTaxCalculationInfo8.setMonthlyBaseRent(baseRentPerSqMtPerMonth);
					areaTaxCalculationInfo8.setCalculatedTax(grossMonthlyRentUnitWise.setScale(0,
							BigDecimal.ROUND_HALF_UP));
					unitTaxCalculationInfo.addAreaTaxCalculationInfo(areaTaxCalculationInfo8);
				}
			} else if (isNonResidentialUnit) {
				LOGGER.debug("Floor Usage : Non-Residential");

				if (floor.getExtraField7() == null
						|| (floor.getExtraField7() != null && !FLOOR_TYPES.get(floor.getExtraField7()).equals(BASEMENT) && !FLOOR_TYPES
								.get(floor.getExtraField7()).equals(GODOWN))) {
					/**
					 * Ground Floor-Apply Full Rates for the area upto the depth
					 * of first 7.62 meters or first intercepting wall and for
					 * remaining area apply 50% of full rate.
					 */
					Double areaBeforeInterceptingWall = Double.valueOf(floor.getExtraField6());
					if (floor.getFloorNo().compareTo(Integer.valueOf(0)) == 0) {
						// Ground Floor Before First Intercepting Wall
						unitAreaFactor = new BigDecimal(propertyTaxUtil.multiplicativeFactorAreaWise(PROPTYPE_NON_RESD,
								floor, "1", null));

						baseRentForCurrentArea = baseRentPerSqMtPerMonth.multiply(unitAreaFactor).setScale(2,
								BigDecimal.ROUND_HALF_UP);
						;

						BigDecimal groundFloorRentUptoInterceptingWall = baseRentForCurrentArea
								.multiply(new BigDecimal(areaBeforeInterceptingWall));

						groundFloorRentUptoInterceptingWall = groundFloorRentUptoInterceptingWall.setScale(0,
								ROUND_HALF_UP);

						AreaTaxCalculationInfo areaTaxCalculationInfo1 = new AreaTaxCalculationInfo();
						areaTaxCalculationInfo1.setTaxableArea(new BigDecimal(areaBeforeInterceptingWall).setScale(2,
								ROUND_HALF_UP));
						areaTaxCalculationInfo1.getTaxableArea();
						areaTaxCalculationInfo1.setMonthlyBaseRent(baseRentForCurrentArea);
						areaTaxCalculationInfo1.setCalculatedTax(groundFloorRentUptoInterceptingWall);
						unitTaxCalculationInfo.addAreaTaxCalculationInfo(areaTaxCalculationInfo1);

						grossMonthlyRentUnitWise = grossMonthlyRentUnitWise.add(groundFloorRentUptoInterceptingWall);

						remainingArea = accessableArea - areaBeforeInterceptingWall;

						if (remainingArea != 0) {
							// Ground Floor After First Intercepting Wall
							unitAreaFactor = new BigDecimal(propertyTaxUtil.multiplicativeFactorAreaWise(
									PROPTYPE_NON_RESD, floor, "2", null));
							baseRentForCurrentArea = BigDecimal.ZERO;

							baseRentForCurrentArea = baseRentPerSqMtPerMonth.multiply(unitAreaFactor).setScale(2,
									BigDecimal.ROUND_HALF_UP);

							BigDecimal groundFloorRentAfterInterceptingWall = baseRentForCurrentArea
									.multiply(new BigDecimal(remainingArea));

							groundFloorRentAfterInterceptingWall = groundFloorRentAfterInterceptingWall.setScale(0,
									ROUND_HALF_UP);

							AreaTaxCalculationInfo areaTaxCalculationInfo2 = new AreaTaxCalculationInfo();
							areaTaxCalculationInfo2.setTaxableArea(new BigDecimal(remainingArea).setScale(2,
									ROUND_HALF_UP));
							areaTaxCalculationInfo2.setMonthlyBaseRent(baseRentForCurrentArea);
							areaTaxCalculationInfo2.setCalculatedTax(groundFloorRentAfterInterceptingWall);
							unitTaxCalculationInfo.addAreaTaxCalculationInfo(areaTaxCalculationInfo2);

							grossMonthlyRentUnitWise = grossMonthlyRentUnitWise
									.add(groundFloorRentAfterInterceptingWall);
						}
					}
					/**
					 * First Floor onwards-Apply 70% of the full rates for the
					 * area upto the depth of first 7.62 meters or first
					 * intercepting wall and for remaining area apply 35% of
					 * full rate.
					 */
					else {
						remainingArea = Double.valueOf("0");
						// First Floor Onwards Before First Intercepting Wall
						unitAreaFactor = new BigDecimal(propertyTaxUtil.multiplicativeFactorAreaWise(PROPTYPE_NON_RESD,
								floor, "1", null));

						baseRentForCurrentArea = baseRentPerSqMtPerMonth.multiply(unitAreaFactor).setScale(2,
								BigDecimal.ROUND_HALF_UP);

						BigDecimal firstFloorRentUptoInterceptingWall = baseRentForCurrentArea.multiply(new BigDecimal(
								areaBeforeInterceptingWall));

						firstFloorRentUptoInterceptingWall = firstFloorRentUptoInterceptingWall.setScale(0,
								ROUND_HALF_UP);

						AreaTaxCalculationInfo areaTaxCalculationInfo3 = new AreaTaxCalculationInfo();
						areaTaxCalculationInfo3.setTaxableArea(new BigDecimal(areaBeforeInterceptingWall).setScale(2,
								ROUND_HALF_UP));
						areaTaxCalculationInfo3.setMonthlyBaseRent(baseRentForCurrentArea);
						areaTaxCalculationInfo3.setCalculatedTax(firstFloorRentUptoInterceptingWall);
						unitTaxCalculationInfo.addAreaTaxCalculationInfo(areaTaxCalculationInfo3);

						grossMonthlyRentUnitWise = grossMonthlyRentUnitWise.add(firstFloorRentUptoInterceptingWall);

						remainingArea = accessableArea - areaBeforeInterceptingWall;

						if (remainingArea != 0) {
							// First Floor Onwards After First Intercepting Wall
							unitAreaFactor = new BigDecimal(propertyTaxUtil.multiplicativeFactorAreaWise(
									PROPTYPE_NON_RESD, floor, "2", null));
							baseRentForCurrentArea = BigDecimal.ZERO;

							baseRentForCurrentArea = baseRentPerSqMtPerMonth.multiply(unitAreaFactor).setScale(2,
									BigDecimal.ROUND_HALF_UP);

							BigDecimal firstFloorRentAfterInterceptingWall = baseRentForCurrentArea
									.multiply(new BigDecimal(remainingArea));

							firstFloorRentAfterInterceptingWall = firstFloorRentAfterInterceptingWall.setScale(0,
									ROUND_HALF_UP);

							AreaTaxCalculationInfo areaTaxCalculationInfo4 = new AreaTaxCalculationInfo();
							areaTaxCalculationInfo4.setTaxableArea(new BigDecimal(remainingArea).setScale(2,
									ROUND_HALF_UP));
							areaTaxCalculationInfo4.setMonthlyBaseRent(baseRentForCurrentArea);
							areaTaxCalculationInfo4.setCalculatedTax(firstFloorRentAfterInterceptingWall);
							unitTaxCalculationInfo.addAreaTaxCalculationInfo(areaTaxCalculationInfo4);

							grossMonthlyRentUnitWise = grossMonthlyRentUnitWise
									.add(firstFloorRentAfterInterceptingWall);
						}
					}
				}
				/**
				 * If the Floor Type(Non Mandatory Field) is either Basement or
				 * Godown, then apply full rate for the total area
				 */
				else if (floor.getExtraField7() != null
						&& (FLOOR_TYPES.get(floor.getExtraField7()).equals(BASEMENT) || FLOOR_TYPES.get(
								floor.getExtraField7()).equals(GODOWN))) {
					grossMonthlyRentUnitWise = baseRentPerSqMtPerMonth.multiply(new BigDecimal(accessableArea));

					AreaTaxCalculationInfo areaTaxCalculationInfo5 = new AreaTaxCalculationInfo();
					areaTaxCalculationInfo5.setTaxableArea(new BigDecimal(accessableArea).setScale(2,
							BigDecimal.ROUND_HALF_UP));
					areaTaxCalculationInfo5.setMonthlyBaseRent(baseRentPerSqMtPerMonth);
					areaTaxCalculationInfo5.setCalculatedTax(grossMonthlyRentUnitWise.setScale(0,
							BigDecimal.ROUND_HALF_UP));
					unitTaxCalculationInfo.addAreaTaxCalculationInfo(areaTaxCalculationInfo5);
				}
			} else if (isOpenPlotUnit) {
				LOGGER.debug("Floor Usage : Open Plot");

				grossMonthlyRentUnitWise = baseRentPerSqMtPerMonth.multiply(new BigDecimal(accessableArea)).setScale(0,
						BigDecimal.ROUND_HALF_UP);

				AreaTaxCalculationInfo areaTaxCalculationInfo = new AreaTaxCalculationInfo();

				areaTaxCalculationInfo.setTaxableArea(new BigDecimal(accessableArea));
				areaTaxCalculationInfo.setMonthlyBaseRent(baseRentPerSqMtPerMonth);
				areaTaxCalculationInfo.setCalculatedTax(grossMonthlyRentUnitWise);
				unitTaxCalculationInfo.addAreaTaxCalculationInfo(areaTaxCalculationInfo);
				unitTaxCalculationInfo.setFloorNumber("Open Plot");
			}

			/**
			 * If the Unit is tenant occupied then pick the value greater of MRV
			 * and Actual Rent Paid
			 */
			if (floor.getPropertyOccupation().getOccupancyCode().equals(TENANT)) {
				if (floor.getRentPerMonth() != null && grossMonthlyRentUnitWise != null) {
					unitTaxCalculationInfo.setMonthlyRentPaidByTenant(floor.getRentPerMonth());
					if (floor.getRentPerMonth().compareTo(grossMonthlyRentUnitWise) == 1) {
						grossMonthlyRentUnitWise = floor.getRentPerMonth();
					}
				}
			}

			BigDecimal annualRentUnitWiseBeforeDeduction = grossMonthlyRentUnitWise.multiply(new BigDecimal("12"))
					.setScale(0, ROUND_HALF_UP);

			/**
			 * Apply Standard Deduction(10%) on Gross Annual Rent of All Units
			 */
			BigDecimal applicableDeducationAmount = annualRentUnitWiseBeforeDeduction.multiply(new BigDecimal(
					STANDARD_DEDUCTION_PERCENTAGE));

			applicableDeducationAmount.setScale(2, ROUND_HALF_UP);

			// ALV
			BigDecimal grossAnnualRentAfterDeduction = annualRentUnitWiseBeforeDeduction.subtract(applicableDeducationAmount).setScale(
					0, ROUND_HALF_UP);

			LOGGER.debug("grossAnnualRentAfterDeduction " + grossAnnualRentAfterDeduction);
			// Add intermediate calculation info
			unitTaxCalculationInfo.setBaseRentPerSqMtPerMonth(baseRentPerSqMtPerMonth);
			unitTaxCalculationInfo.setMonthlyRent(grossMonthlyRentUnitWise.setScale(0, ROUND_HALF_UP));
			unitTaxCalculationInfo.setAnnualRentBeforeDeduction(annualRentUnitWiseBeforeDeduction);
			unitTaxCalculationInfo.setAnnualRentAfterDeduction(grossAnnualRentAfterDeduction);
			unitTaxCalculationInfo.setBaseRent(baseRent);
			floor.setAlv(unitTaxCalculationInfo.getAnnualRentAfterDeduction());
		}

		// Uncommented applicableFactors
		unitTaxCalculationInfo.addApplicableFactor(applicableFactors);

		unitTaxCalculationInfo = propertyTaxUtil.calculateApplicableTaxes(applicableTaxes, null,
				unitTaxCalculationInfo, installment, null, null, null, property, category, null);

		if (isResidentialUnit) {
			applicableTaxes.remove(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD);
		} else if (isNonResidentialUnit) {
			applicableTaxes.remove(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD);
			applicableTaxes.remove(DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX);
		} else if (isOpenPlotUnit) {
			if (PROPTYPE_CAT_RESD.equalsIgnoreCase(floor.getUnitTypeCategory())) {
				applicableTaxes.remove(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD);
			} else if (PROPTYPE_CAT_NON_RESD.equalsIgnoreCase(floor.getUnitTypeCategory())) {
				applicableTaxes.remove(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD);
				applicableTaxes.remove(DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX);
			}
		}

		applicableTaxes.remove(DEMANDRSN_CODE_GENERAL_WATER_TAX);

		/*
		 * Commented because BIG_RESD_TAX is applicable only for Residential
		 * type of property
		 *
		 * if
		 * (USAGES_FOR_RESD.contains(floor.getPropertyUsage().getUsageName())) {
		 * // Calculate Large Residential Premises Tax if (accessableArea >=
		 * AREA_CONSTANT_FOR_LARGE_RESIDENTIAL_PREMISES &&
		 * unitTaxCalculationInfo
		 * .getAnnualRentAfterDeduction().compareTo(LARGE_RESIDENTIAL_PREMISES_ALV
		 * ) == 1) {
		 *
		 * EgDemandReasonDetails demandReasonDetails =
		 * propertyTaxUtil.getDemandReasonDetails(
		 * DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX,
		 * unitTaxCalculationInfo.getAnnualRentAfterDeduction(),
		 * installment.getFromDate());
		 *
		 * if (demandReasonDetails != null) {
		 * LOGGER.debug("applying large residentail tax"); MiscellaneousTax
		 * miscellaneousTax = new MiscellaneousTax();
		 * miscellaneousTax.setTaxName(DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX);
		 * miscellaneousTax.setTaxValue(demandReasonDetails.getPercentage());
		 * BigDecimal calculateBigResTax =
		 * unitTaxCalculationInfo.getAnnualRentAfterDeduction().multiply(
		 * demandReasonDetails.getPercentage().divide(new BigDecimal("100")));
		 * miscellaneousTax.setCalculatedTaxValue(calculateBigResTax.setScale(0,
		 * ROUND_HALF_UP));
		 * unitTaxCalculationInfo.setTotalTaxPayable(unitTaxCalculationInfo
		 * .getTotalTaxPayable() .add(calculateBigResTax.setScale(0,
		 * ROUND_HALF_UP)).setScale(0, ROUND_HALF_UP));
		 * unitTaxCalculationInfo.addMiscellaneousTaxes(miscellaneousTax);
		 * LOGGER.debug("big resd tax " + calculateBigResTax +
		 * " total tax payable " + unitTaxCalculationInfo.getTotalTaxPayable());
		 * } } }
		 */

		/**
		 * Commented out as Tax Calculation is already day based
		 */
		// unitTaxCalculationInfo =
		// propertyTaxUtil.calculateTaxPayableByOccupancyDate(unitTaxCalculationInfo,
		// installment);

		unitTaxCalculationInfo.setInstDate(floor.getExtraField3());
		LOGGER.debug("Exited from ResdAndNonResdUnitTaxCalculator: calculateUnitTax method");
		return unitTaxCalculationInfo;
	}

	private UnitTaxCalculationInfo addMixedUnitInfo(FloorImpl floorImpl,
			UnitTaxCalculationInfo unitTaxCalculationInfo) {
		LOGGER.debug("Entered into ResdAndNonResdUnitTaxCalculator : addResidentialUnitInfo method");
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

		if (floorImpl.getUnitType() != null
				& floorImpl.getUnitType().getCode().equalsIgnoreCase(PropertyTaxConstants.UNITTYPE_OPEN_PLOT)) {
			unitTaxCalculationInfo.setFloorNumberInteger(OPEN_PLOT_UNIT_FLOORNUMBER);
			unitTaxCalculationInfo.setFloorNumber(propertyTaxUtil.getFloorStr(OPEN_PLOT_UNIT_FLOORNUMBER));
		} else {
			unitTaxCalculationInfo.setFloorNumberInteger(floorImpl.getFloorNo());
			unitTaxCalculationInfo.setFloorNumber(propertyTaxUtil.getFloorStr(floorImpl.getFloorNo()));
		}

		unitTaxCalculationInfo.setUnitNumber(Integer.parseInt(floorImpl.getExtraField1()));
		unitTaxCalculationInfo.setUnitOccupation(floorImpl.getPropertyOccupation().getOccupation());
		unitTaxCalculationInfo.setUnitArea(new BigDecimal(floorImpl.getBuiltUpArea().getArea().toString()));
		unitTaxCalculationInfo.setUnitUsage(floorImpl.getPropertyUsage().getUsageName());
		unitTaxCalculationInfo.setUnitOccupier(floorImpl.getExtraField2());

		try {
			unitTaxCalculationInfo.setOccpancyDate(dateFormatter.parse(floorImpl.getExtraField3()));
		} catch (ParseException e) {
			LOGGER.error(e.getMessage(), e);
		}
		LOGGER.debug("Exit from ResdAndNonResdUnitTaxCalculator : addResidentialUnitInfo method");
		return unitTaxCalculationInfo;
	}
}
