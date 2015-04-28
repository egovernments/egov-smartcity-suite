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
package org.egov.ptis.nmc.service;

import static java.math.BigDecimal.ZERO;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.BASEMENT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_GENERAL_WATER_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.FLOORNO_WITH_DIFF_MULFACTOR_NONRESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.FLOOR_TYPES;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.GODOWN;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.GWR_IMPOSED;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.Installment;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.domain.entity.property.BoundaryCategory;
import org.egov.ptis.domain.entity.property.FloorImpl;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.nmc.constants.NMCPTISConstants;
import org.egov.ptis.nmc.model.ApplicableFactor;
import org.egov.ptis.nmc.model.AreaTaxCalculationInfo;
import org.egov.ptis.nmc.model.UnitTaxCalculationInfo;
import org.egov.ptis.nmc.util.PropertyTaxUtil;

public class NonResidentialUnitTaxCalculator {

	private PropertyTaxUtil propertyTaxUtil;
	private PersistenceService persistenceService;
	private static final Logger LOGGER = Logger.getLogger(NonResidentialUnitTaxCalculator.class);

	public void setPropertyTaxUtil(PropertyTaxUtil propertyTaxUtil) {
		this.propertyTaxUtil = propertyTaxUtil;
	}

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	public UnitTaxCalculationInfo calculateUnitTax(UnitTaxCalculationInfo unitTaxCalculationInfo, FloorImpl floor,
			Installment installment, String propCategory, Property property, BoundaryCategory category) {

		BigDecimal grossMonthlyRentUnitWise = BigDecimal.ZERO;
		Double remainingArea = Double.valueOf("0");
		BigDecimal unitAreaFactor = BigDecimal.ZERO;
		BigDecimal baseRent = unitTaxCalculationInfo.getBaseRent();

		List<String> applicableTaxes = PropertyTaxUtil.prepareApplicableTaxes(property);

		if (floor.getWaterRate().equals(GWR_IMPOSED)) {
			applicableTaxes.add(DEMANDRSN_CODE_GENERAL_WATER_TAX);
		}

		LOGGER.debug("calculatePropertyTax - unitNo = " + floor.getExtraField1() + ", floor="
				+ floor.getFloorNo() + ", applicableTaxes = " + applicableTaxes);

		boolean result = false;
		List<String> exemptedTaxes = null;

		if (floor.getTaxExemptedReason() != null) {
			exemptedTaxes = NMCPTISConstants.exemptedTaxesByReason.get(floor.getTaxExemptedReason());
			result = applicableTaxes.removeAll(exemptedTaxes);
		}

		if (result) {
			LOGGER.debug("calculateUnitTax - Unit is exempted from taxes " + exemptedTaxes);
		} else {
			LOGGER.debug("calculateUnitTax - Unit is not exempted from taxes ");
		}

		// Add Unit Info
		unitTaxCalculationInfo = this.addNonResidentialUnitInfo(floor, unitTaxCalculationInfo);

		/**
		 * get the applicableFactors
		 */
		/*
		 * Commented because we should not apply any factors on guidance value
		 * for rent chart method (dated 29/06/2012)
		 */
		/*
		 * List<ApplicableFactor> applicableFactors =
		 * propertyTaxUtil.getApplicableFactorsForResidentialAndNonResidential(
		 * floor, propertyArea,installment, categoryId);
		 */

		List<ApplicableFactor> applicableFactors = Collections.EMPTY_LIST;

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

			/**
			 * Get Accessable Floor Area
			 */
			Double totalaccessableArea = Double.valueOf(Float.toString(floor.getBuiltUpArea().getArea()));

			/**
			 * Area before first intercepting wall
			 */
			Double areaBeforeInterceptingWall = Double.valueOf("0");
			if (floor.getExtraField6() != null && !floor.getExtraField6().equals("")) {
				areaBeforeInterceptingWall = Double.valueOf(floor.getExtraField6());
			}

			/**
			 * Calculate base rent per square meter
			 */
			BigDecimal baseRentPerSqMtPerMonth = propertyTaxUtil.calculateBaseRentPerSqMtPerMonth(applicableFactors,
					baseRent);
			/*
			 * Commented as applicable factors need to be passed to cal
			 * baseRentPerSqMtPerMonth BigDecimal baseRentPerSqMtPerMonth =
			 * baseRent;
			 */

			if (floor.getExtraField7() == null
					|| (floor.getExtraField7() != null && !FLOOR_TYPES.get(floor.getExtraField7()).equals(BASEMENT) && !FLOOR_TYPES
							.get(floor.getExtraField7()).equals(GODOWN))) {

				BigDecimal baseRentForCurrentArea = BigDecimal.ZERO;

				/**
				 * (Lower Ground Floor, Upper Ground Floor, Ground Floor) -Apply
				 * Full Rates for the area upto the depth of first 7.62 meters
				 * or first intercepting wall and for remaining area apply 50%
				 * of full rate.
				 */

				if (FLOORNO_WITH_DIFF_MULFACTOR_NONRESD.contains(floor.getFloorNo().toString())) {
					// Ground Floor Before First Intercepting Wall
					unitAreaFactor = new BigDecimal(propertyTaxUtil.multiplicativeFactorAreaWise(
							NMCPTISConstants.PROPTYPE_NON_RESD, floor, "1", propCategory));

					baseRentForCurrentArea = baseRentPerSqMtPerMonth.multiply(unitAreaFactor).setScale(2,
							BigDecimal.ROUND_HALF_UP);

					BigDecimal groundFloorRentUptoInterceptingWall = baseRentForCurrentArea.multiply(new BigDecimal(
							areaBeforeInterceptingWall));

					groundFloorRentUptoInterceptingWall = groundFloorRentUptoInterceptingWall.setScale(0,
							BigDecimal.ROUND_HALF_UP);

					AreaTaxCalculationInfo areaTaxCalculationInfo1 = new AreaTaxCalculationInfo();
					areaTaxCalculationInfo1.setTaxableArea(new BigDecimal(areaBeforeInterceptingWall).setScale(2,
							BigDecimal.ROUND_HALF_UP));
					areaTaxCalculationInfo1.getTaxableArea();
					areaTaxCalculationInfo1.setMonthlyBaseRent(baseRentForCurrentArea);
					areaTaxCalculationInfo1.setCalculatedTax(groundFloorRentUptoInterceptingWall);
					unitTaxCalculationInfo.addAreaTaxCalculationInfo(areaTaxCalculationInfo1);

					grossMonthlyRentUnitWise = grossMonthlyRentUnitWise.add(groundFloorRentUptoInterceptingWall);

					remainingArea = totalaccessableArea - areaBeforeInterceptingWall;

					if (remainingArea != 0) {
						// Ground Floor After First Intercepting Wall
						unitAreaFactor = new BigDecimal(propertyTaxUtil.multiplicativeFactorAreaWise(
								NMCPTISConstants.PROPTYPE_NON_RESD, floor, "2", propCategory));
						baseRentForCurrentArea = BigDecimal.ZERO;

						baseRentForCurrentArea = baseRentPerSqMtPerMonth.multiply(unitAreaFactor).setScale(2,
								BigDecimal.ROUND_HALF_UP);

						BigDecimal groundFloorRentAfterInterceptingWall = baseRentForCurrentArea
								.multiply(new BigDecimal(remainingArea));

						groundFloorRentAfterInterceptingWall = groundFloorRentAfterInterceptingWall.setScale(0,
								BigDecimal.ROUND_HALF_UP);

						AreaTaxCalculationInfo areaTaxCalculationInfo2 = new AreaTaxCalculationInfo();
						areaTaxCalculationInfo2.setTaxableArea(new BigDecimal(remainingArea).setScale(2,
								BigDecimal.ROUND_HALF_UP));
						areaTaxCalculationInfo2.setMonthlyBaseRent(baseRentForCurrentArea);
						areaTaxCalculationInfo2.setCalculatedTax(groundFloorRentAfterInterceptingWall);
						unitTaxCalculationInfo.addAreaTaxCalculationInfo(areaTaxCalculationInfo2);

						grossMonthlyRentUnitWise = grossMonthlyRentUnitWise.add(groundFloorRentAfterInterceptingWall);
					}
				}
				/**
				 * First Floor onwards-Apply 70% of the full rates for the area
				 * upto the depth of first 7.62 meters or first intercepting
				 * wall and for remaining area apply 35% of full rate.
				 */
				else {
					remainingArea = Double.valueOf("0");
					// First Floor Onwards Before First Intercepting Wall
					unitAreaFactor = new BigDecimal(propertyTaxUtil.multiplicativeFactorAreaWise(
							NMCPTISConstants.PROPTYPE_NON_RESD, floor, "1", propCategory));

					baseRentForCurrentArea = baseRentPerSqMtPerMonth.multiply(unitAreaFactor).setScale(2,
							BigDecimal.ROUND_HALF_UP);

					BigDecimal firstFloorRentUptoInterceptingWall = baseRentForCurrentArea.multiply(new BigDecimal(
							areaBeforeInterceptingWall));

					firstFloorRentUptoInterceptingWall = firstFloorRentUptoInterceptingWall.setScale(0,
							BigDecimal.ROUND_HALF_UP);

					AreaTaxCalculationInfo areaTaxCalculationInfo3 = new AreaTaxCalculationInfo();
					areaTaxCalculationInfo3.setTaxableArea(new BigDecimal(areaBeforeInterceptingWall).setScale(2,
							BigDecimal.ROUND_HALF_UP));
					areaTaxCalculationInfo3.setMonthlyBaseRent(baseRentForCurrentArea);
					areaTaxCalculationInfo3.setCalculatedTax(firstFloorRentUptoInterceptingWall);
					unitTaxCalculationInfo.addAreaTaxCalculationInfo(areaTaxCalculationInfo3);

					grossMonthlyRentUnitWise = grossMonthlyRentUnitWise.add(firstFloorRentUptoInterceptingWall);

					remainingArea = totalaccessableArea - areaBeforeInterceptingWall;

					if (remainingArea != 0) {
						// First Floor Onwards After First Intercepting Wall
						unitAreaFactor = new BigDecimal(propertyTaxUtil.multiplicativeFactorAreaWise(
								NMCPTISConstants.PROPTYPE_NON_RESD, floor, "2", propCategory));
						baseRentForCurrentArea = BigDecimal.ZERO;

						baseRentForCurrentArea = baseRentPerSqMtPerMonth.multiply(unitAreaFactor).setScale(2,
								BigDecimal.ROUND_HALF_UP);

						BigDecimal firstFloorRentAfterInterceptingWall = baseRentForCurrentArea.multiply(
								new BigDecimal(remainingArea)).setScale(0, BigDecimal.ROUND_HALF_UP);

						AreaTaxCalculationInfo areaTaxCalculationInfo4 = new AreaTaxCalculationInfo();
						areaTaxCalculationInfo4.setTaxableArea(new BigDecimal(remainingArea).setScale(2,
								BigDecimal.ROUND_HALF_UP));
						areaTaxCalculationInfo4.setMonthlyBaseRent(baseRentForCurrentArea);
						areaTaxCalculationInfo4.setCalculatedTax(firstFloorRentAfterInterceptingWall);
						unitTaxCalculationInfo.addAreaTaxCalculationInfo(areaTaxCalculationInfo4);

						grossMonthlyRentUnitWise = grossMonthlyRentUnitWise.add(firstFloorRentAfterInterceptingWall);
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
				grossMonthlyRentUnitWise = baseRentPerSqMtPerMonth.multiply(new BigDecimal(totalaccessableArea));

				AreaTaxCalculationInfo areaTaxCalculationInfo5 = new AreaTaxCalculationInfo();
				areaTaxCalculationInfo5.setTaxableArea(new BigDecimal(totalaccessableArea).setScale(2,
						BigDecimal.ROUND_HALF_UP));
				areaTaxCalculationInfo5.setMonthlyBaseRent(baseRentPerSqMtPerMonth);
				areaTaxCalculationInfo5
						.setCalculatedTax(grossMonthlyRentUnitWise.setScale(0, BigDecimal.ROUND_HALF_UP));
				unitTaxCalculationInfo.addAreaTaxCalculationInfo(areaTaxCalculationInfo5);
			}

			/**
			 * If the Unit is tenant occupied then pick the value greater of MRV
			 * and Actual Rent Paid
			 */
			if (floor.getPropertyOccupation().getOccupancyCode().equals(NMCPTISConstants.TENANT)) {
				if (floor.getRentPerMonth() != null && grossMonthlyRentUnitWise != null) {
					unitTaxCalculationInfo.setMonthlyRentPaidByTenant(floor.getRentPerMonth());
					if (floor.getRentPerMonth().compareTo(grossMonthlyRentUnitWise) == 1) {
						grossMonthlyRentUnitWise = floor.getRentPerMonth();
					}
				}
			}

			BigDecimal annualRentUnitWiseBeforeDeduction = grossMonthlyRentUnitWise.multiply(new BigDecimal("12"))
					.setScale(0, BigDecimal.ROUND_HALF_UP);
			/**
			 * Apply Standard Deduction(10%) on Gross Annual Rent of All Units
			 */
			BigDecimal applicableDeducationAmount = annualRentUnitWiseBeforeDeduction.multiply(new BigDecimal(
					NMCPTISConstants.STANDARD_DEDUCTION_PERCENTAGE));

			applicableDeducationAmount.setScale(2, BigDecimal.ROUND_HALF_UP);

			// ALV
			BigDecimal grossAnnualRentAfterDeduction = annualRentUnitWiseBeforeDeduction
					.subtract(applicableDeducationAmount);

			// Add intermediate calculation info
			unitTaxCalculationInfo.setBaseRentPerSqMtPerMonth(baseRentPerSqMtPerMonth);
			unitTaxCalculationInfo.setMonthlyRent(grossMonthlyRentUnitWise.setScale(0, BigDecimal.ROUND_HALF_UP));
			unitTaxCalculationInfo.setAnnualRentBeforeDeduction(annualRentUnitWiseBeforeDeduction);
			unitTaxCalculationInfo.setAnnualRentAfterDeduction(grossAnnualRentAfterDeduction.setScale(0,
					BigDecimal.ROUND_HALF_UP));
			unitTaxCalculationInfo.setBaseRent(baseRent);
			floor.setAlv(unitTaxCalculationInfo.getAnnualRentAfterDeduction());
		}
		// Uncommented applicableFactors
		unitTaxCalculationInfo.addApplicableFactor(applicableFactors);

		unitTaxCalculationInfo = propertyTaxUtil.calculateApplicableTaxes(applicableTaxes, null, unitTaxCalculationInfo,
				installment, null, null, null, property, category, null);

		/**
		 * Commented out as Tax Calculation is already day based
		 */
		// unitTaxCalculationInfo =
		// propertyTaxUtil.calculateTaxPayableByOccupancyDate(unitTaxCalculationInfo,
		// installment);

		unitTaxCalculationInfo.setInstDate(floor.getExtraField3());

		applicableTaxes.remove(DEMANDRSN_CODE_GENERAL_WATER_TAX);

		return unitTaxCalculationInfo;
	}

	private UnitTaxCalculationInfo addNonResidentialUnitInfo(FloorImpl floorImpl,
			UnitTaxCalculationInfo unitTaxCalculationInfo) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

		unitTaxCalculationInfo.setFloorNumberInteger(floorImpl.getFloorNo());
		unitTaxCalculationInfo.setFloorNumber(propertyTaxUtil.getFloorStr(floorImpl.getFloorNo()));
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
		return unitTaxCalculationInfo;
	}

}
