package org.egov.ptis.nmc.service;

import static java.math.BigDecimal.ZERO;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.BASEMENT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.FLOORNO_WITH_DIFF_MULFACTOR_NONRESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.FLOOR_TYPES;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.GODOWN;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.Installment;
import org.egov.infstr.services.PersistenceService;
import org.egov.lib.admbndry.Boundary;
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

	public UnitTaxCalculationInfo calculateUnitTax(FloorImpl floor, Boundary propertyArea,
			List<String> applicableTaxes, Installment installment, String propCategory, Property property) {

		BigDecimal grossMonthlyRentUnitWise = BigDecimal.ZERO;
		Double remainingArea = Double.valueOf("0");
		BigDecimal unitAreaFactor = BigDecimal.ZERO;
		UnitTaxCalculationInfo unitTaxCalculationInfo = new UnitTaxCalculationInfo();

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

		/**
		 * Get the base Rate of the Unit
		 */
		BigDecimal baseRent = propertyTaxUtil.baseRentOfUnit(floor, propertyArea, installment, unitTaxCalculationInfo);

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
			unitTaxCalculationInfo = this.addResidentialUnitInfo(floor, unitTaxCalculationInfo);
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

			// Add Unit Info
			unitTaxCalculationInfo = this.addResidentialUnitInfo(floor, unitTaxCalculationInfo);

			// Add intermediate calculation info
			unitTaxCalculationInfo.setBaseRentPerSqMtPerMonth(baseRentPerSqMtPerMonth);
			unitTaxCalculationInfo.setMonthlyRent(grossMonthlyRentUnitWise.setScale(0, BigDecimal.ROUND_HALF_UP));
			unitTaxCalculationInfo.setAnnualRentBeforeDeduction(annualRentUnitWiseBeforeDeduction);
			unitTaxCalculationInfo.setAnnualRentAfterDeduction(grossAnnualRentAfterDeduction.setScale(0,
					BigDecimal.ROUND_HALF_UP));
			unitTaxCalculationInfo.setBaseRent(baseRent);
		}
		// Uncommented applicableFactors
		unitTaxCalculationInfo.addApplicableFactor(applicableFactors);

		unitTaxCalculationInfo = propertyTaxUtil.calculateApplicableTaxes(applicableTaxes, null, unitTaxCalculationInfo,
				installment, null, null, null, property);

		/**
		 * Commented out as Tax Calculation is already day based
		 */
		// unitTaxCalculationInfo =
		// propertyTaxUtil.calculateTaxPayableByOccupancyDate(unitTaxCalculationInfo,
		// installment);

		unitTaxCalculationInfo.setInstDate(floor.getExtraField3());

		return unitTaxCalculationInfo;
	}

	private UnitTaxCalculationInfo addResidentialUnitInfo(FloorImpl floorImpl,
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
