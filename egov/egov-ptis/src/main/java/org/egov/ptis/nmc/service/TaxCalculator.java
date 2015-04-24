package org.egov.ptis.nmc.service;

import static org.egov.ptis.nmc.constants.NMCPTISConstants.CENTRALGOVT_BUILDING_ALV_PERCENTAGE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.STATEGOVT_BUILDING_ALV_PERCENTAGE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.dateFormat;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.egov.commons.Installment;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.ptis.domain.entity.property.BoundaryCategory;
import org.egov.ptis.domain.entity.property.FloorIF;
import org.egov.ptis.domain.entity.property.FloorImpl;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyDetail;
import org.egov.ptis.nmc.constants.NMCPTISConstants;
import org.egov.ptis.nmc.model.MiscellaneousTax;
import org.egov.ptis.nmc.model.TaxCalculationInfo;
import org.egov.ptis.nmc.model.UnitTaxCalculationInfo;
import org.egov.ptis.nmc.util.AreaTaxInfoComparator;
import org.egov.ptis.nmc.util.PropertyTaxUtil;
import org.egov.ptis.nmc.util.UnitTaxInfoComparator;
import org.egov.ptis.utils.PTISCacheManager;
import org.egov.ptis.utils.PTISCacheManagerInteface;

public class TaxCalculator {

	private ResidentialUnitTaxCalculator residentialUnitTaxCalculator;
	private NonResidentialUnitTaxCalculator nonResidentialUnitTaxCalculator;
	private OpenPlotUnitTaxCalculator openPlotUnitTaxCalculator;
	private GovtPropertyUnitTaxCalculator govtPropertyUnitTaxCalculator;
	private ResdAndNonResdUnitTaxCalculator resdAndNonResdUnitTaxCalculator;
	private PropertyTaxUtil propertyTaxUtil;
	private BigDecimal totalTaxPayable = BigDecimal.ZERO;
	private static final Logger LOGGER = Logger.getLogger(TaxCalculator.class);
	private HashMap<Installment, TaxCalculationInfo> taxCalculationMap = new HashMap<Installment, TaxCalculationInfo>();
	SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

	/**
	 *
	 * @param property
	 *            Property Object
	 * @param applicableTaxes
	 *            List of Applicable Taxes
	 * @param occDate
	 *            Minimum Occupancy Date among all the units
	 * @return
	 */
	public HashMap<Installment, TaxCalculationInfo> calculatePropertyTax(Property property, Date occDate) {

		List<Installment> installmentMasters = propertyTaxUtil.getInstallmentListByStartDate(occDate);
		Boundary propertyArea = null;
		boolean isIgnoreBaseRent = false;

		List<String> applicableTaxes = null;

		if (property.getAreaBndry() != null) {
			propertyArea = property.getAreaBndry();
		} else {
			propertyArea = property.getBasicProperty().getPropertyID().getArea();
		}

		String propTypeCode = property.getPropertyDetail().getPropertyTypeMaster().getCode();
		for (Installment installment : installmentMasters) {
			totalTaxPayable = BigDecimal.ZERO;
			TaxCalculationInfo taxCalculationInfo = this.addPropertyInfo(property);

			BigDecimal installmentWiseTaxPayable = BigDecimal.ZERO;
			BigDecimal totalAnnualLettingValue = BigDecimal.ZERO;

			List<BoundaryCategory> categories = new ArrayList<BoundaryCategory>();

			if (propTypeCode.equals(NMCPTISConstants.PROPTYPE_RESD)) {
				LOGGER.info("Calculate Residential Building-Property Tax");
				Set<FloorIF> newFloorSet = property.getPropertyDetail().getFloorDetails();
				for (FloorIF floorIF : newFloorSet) {
					if (floorIF != null) {
						FloorImpl floorImpl = (FloorImpl) floorIF;
						Date occupationDate = null;
						try {
							occupationDate = dateFormatter.parse(floorImpl.getExtraField3());
						} catch (ParseException e) {
							LOGGER.error("ParseException occured in method : calculatePropertyTax", e);
						}

						if (propertyTaxUtil.betweenOrBefore(occupationDate, installment.getFromDate(),
								installment.getToDate())) {

							/**
							 * Getting the applicable Base Rent
							 */
							categories = propertyTaxUtil.getBoundaryCategories(floorImpl, propertyArea, installment,
									property.getPropertyDetail());
							List<UnitTaxCalculationInfo> calculationInfos = new ArrayList<UnitTaxCalculationInfo>();

							if (floorImpl.getManualAlv() != null) {
								UnitTaxCalculationInfo unitTaxCalculationInfo = new UnitTaxCalculationInfo();
								unitTaxCalculationInfo.setBaseRent(BigDecimal.ZERO);
								unitTaxCalculationInfo.setTaxExemptionReason(floorImpl.getTaxExemptedReason());

								unitTaxCalculationInfo = residentialUnitTaxCalculator.calculateUnitTax(
										unitTaxCalculationInfo, floorImpl, installment, property
												.getPropertyDetail().getExtra_field5(), property, null);

								calculationInfos.add(unitTaxCalculationInfo);
								installmentWiseTaxPayable = installmentWiseTaxPayable.add(unitTaxCalculationInfo
										.getTotalTaxPayable());

								totalTaxPayable = totalTaxPayable.add(unitTaxCalculationInfo.getTotalTaxPayable());

							} else {

								for (BoundaryCategory category : categories) {

									UnitTaxCalculationInfo unitTaxCalculationInfo = new UnitTaxCalculationInfo();
									unitTaxCalculationInfo.setBaseRentEffectiveDate(category.getFromDate());
									unitTaxCalculationInfo.setBaseRent(new BigDecimal(category.getCategory()
											.getCategoryAmount().toString()));
									unitTaxCalculationInfo.setTaxExemptionReason(floorImpl.getTaxExemptedReason());

									unitTaxCalculationInfo = residentialUnitTaxCalculator.calculateUnitTax(
											unitTaxCalculationInfo, floorImpl, installment, property
													.getPropertyDetail().getExtra_field5(), property, category);

									calculationInfos.add(unitTaxCalculationInfo);

									installmentWiseTaxPayable = installmentWiseTaxPayable.add(unitTaxCalculationInfo
											.getTotalTaxPayable());

									totalTaxPayable = totalTaxPayable.add(unitTaxCalculationInfo.getTotalTaxPayable());
								}
							}
							taxCalculationInfo.addUnitTaxCalculationInfo(calculationInfos);
						}
					}
				}

			}

			else if (propTypeCode.equals(NMCPTISConstants.PROPTYPE_NON_RESD)) {
				LOGGER.info("Calculate Non-Residential Building-Property Tax");
				Set<FloorIF> newFloorSet = property.getPropertyDetail().getFloorDetails();

				for (FloorIF floorIF : newFloorSet) {
					FloorImpl floorImpl = (FloorImpl) floorIF;

					Date occupationDate = null;
					try {
						occupationDate = dateFormatter.parse(floorImpl.getExtraField3());
					} catch (ParseException e) {
						LOGGER.error(e.getMessage(), e);
					}
					if (propertyTaxUtil.betweenOrBefore(occupationDate, installment.getFromDate(),
							installment.getToDate())) {

						/**
						 * Getting the applicable Base Rent
						 */
						categories = propertyTaxUtil.getBoundaryCategories(floorImpl, propertyArea, installment,
								property.getPropertyDetail());
						List<UnitTaxCalculationInfo> calculationInfos = new ArrayList<UnitTaxCalculationInfo>();

						if (floorImpl.getManualAlv() != null) {
							UnitTaxCalculationInfo unitTaxCalculationInfo = new UnitTaxCalculationInfo();
							unitTaxCalculationInfo.setBaseRent(BigDecimal.ZERO);
							unitTaxCalculationInfo.setTaxExemptionReason(floorImpl.getTaxExemptedReason());

							unitTaxCalculationInfo = nonResidentialUnitTaxCalculator.calculateUnitTax(
									unitTaxCalculationInfo, floorImpl, installment, property
											.getPropertyDetail().getExtra_field5(), property, null);

							calculationInfos.add(unitTaxCalculationInfo);

							installmentWiseTaxPayable = installmentWiseTaxPayable.add(unitTaxCalculationInfo
									.getTotalTaxPayable());

							totalTaxPayable = totalTaxPayable.add(unitTaxCalculationInfo.getTotalTaxPayable());
						} else {

							for (BoundaryCategory category : categories) {

								UnitTaxCalculationInfo unitTaxCalculationInfo = new UnitTaxCalculationInfo();
								unitTaxCalculationInfo.setBaseRentEffectiveDate(category.getFromDate());
								unitTaxCalculationInfo.setBaseRent(new BigDecimal(category.getCategory()
										.getCategoryAmount().toString()));
								unitTaxCalculationInfo.setTaxExemptionReason(floorImpl.getTaxExemptedReason());

								unitTaxCalculationInfo = nonResidentialUnitTaxCalculator.calculateUnitTax(
										unitTaxCalculationInfo, floorImpl, installment, property
												.getPropertyDetail().getExtra_field5(), property, category);

								calculationInfos.add(unitTaxCalculationInfo);

								installmentWiseTaxPayable = installmentWiseTaxPayable.add(unitTaxCalculationInfo
										.getTotalTaxPayable());

								totalTaxPayable = totalTaxPayable.add(unitTaxCalculationInfo.getTotalTaxPayable());
							}
						}
						taxCalculationInfo.addUnitTaxCalculationInfo(calculationInfos);
					}
				}
			}

			else if (propTypeCode.equals(NMCPTISConstants.PROPTYPE_OPEN_PLOT)) {
				LOGGER.info("Calculate Open Plot-Property Tax");
				Date occupationDate = property.getBasicProperty().getPropCreateDate();

				if (propertyTaxUtil.betweenOrBefore(occupationDate, installment.getFromDate(), installment.getToDate())) {

					PropertyDetail propertyDetail = property.getPropertyDetail();
					List<UnitTaxCalculationInfo> calculationInfos = new ArrayList<UnitTaxCalculationInfo>();

					/**
					 * Getting the applicable Base Rent
					 */
					categories = propertyTaxUtil.getBoundaryCategories(null, propertyArea, installment,
							property.getPropertyDetail());

					applicableTaxes = PropertyTaxUtil.prepareApplicableTaxes(property);
					LOGGER.debug("calculatePropertyTax - Open Plot , applicableTaxes = " + applicableTaxes);

					if (propertyDetail.getManualAlv() != null) {
						UnitTaxCalculationInfo unitTaxCalculationInfo = new UnitTaxCalculationInfo();
						unitTaxCalculationInfo.setBaseRent(BigDecimal.ZERO);

						unitTaxCalculationInfo = openPlotUnitTaxCalculator.calculateUnitTax(unitTaxCalculationInfo,
								propertyDetail, applicableTaxes, installment, Long.valueOf(propertyDetail
										.getExtra_field6()), new BigDecimal(propertyDetail.getSitalArea().getArea()),
								property, null);

						calculationInfos.add(unitTaxCalculationInfo);
						totalTaxPayable = totalTaxPayable.add(unitTaxCalculationInfo.getTotalTaxPayable());
						taxCalculationInfo.addUnitTaxCalculationInfo(calculationInfos);
					} else {

						if (propertyDetail.getNonResPlotArea() == null) {

							for (BoundaryCategory category : categories) {

								/**
								 * when occupancy date is second base rent effective date, no need to create unittax for 1st base rent
								 */
								if (!isIgnoreBaseRent && isIgnoreFirstBaseRent(categories, occupationDate)) {
									isIgnoreBaseRent = true;
									continue;
								}

								UnitTaxCalculationInfo unitTaxCalculationInfo = new UnitTaxCalculationInfo();
								unitTaxCalculationInfo.setBaseRentEffectiveDate(category.getFromDate());
								unitTaxCalculationInfo.setBaseRent(new BigDecimal(category.getCategory()
										.getCategoryAmount().toString()));

								unitTaxCalculationInfo = openPlotUnitTaxCalculator.calculateUnitTax(
										unitTaxCalculationInfo, propertyDetail, applicableTaxes, installment, Long
												.valueOf(propertyDetail.getExtra_field6()), new BigDecimal(
												propertyDetail.getSitalArea().getArea()), property, category);

								calculationInfos.add(unitTaxCalculationInfo);
								totalTaxPayable = totalTaxPayable.add(unitTaxCalculationInfo.getTotalTaxPayable());
							}

							taxCalculationInfo.addUnitTaxCalculationInfo(calculationInfos);

						} else {
							// Non-Res Plot Area in Non-Residential way of tax
							// calculation

							if (property.getIsExemptedFromTax() == null || !property.getIsExemptedFromTax()) {
								applicableTaxes.add(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD);
								applicableTaxes.add(DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX);
							}

							for (BoundaryCategory category : categories) {

								if (!isIgnoreBaseRent && isIgnoreFirstBaseRent(categories, occupationDate)) {
									isIgnoreBaseRent = true;
									continue;
								}

								UnitTaxCalculationInfo unitTaxCalculationInfo = new UnitTaxCalculationInfo();
								unitTaxCalculationInfo.setBaseRentEffectiveDate(category.getFromDate());
								unitTaxCalculationInfo.setBaseRent(new BigDecimal(category.getCategory()
										.getCategoryAmount().toString()));

								unitTaxCalculationInfo = openPlotUnitTaxCalculator.calculateUnitTax(
										unitTaxCalculationInfo, propertyDetail, applicableTaxes, installment, Long
												.valueOf(propertyDetail.getExtra_field6()), new BigDecimal(
												propertyDetail.getNonResPlotArea().getArea()), property, category);

								unitTaxCalculationInfo.setFloorNumber(NMCPTISConstants.PROPTYPE_CAT_NON_RESD);
								unitTaxCalculationInfo.setUnitArea(new BigDecimal(propertyDetail.getNonResPlotArea()
										.getArea()));
								applicableTaxes.remove(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD);
								applicableTaxes.remove(DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX);

								List<UnitTaxCalculationInfo> unitNonResd = new ArrayList<UnitTaxCalculationInfo>();
								unitNonResd.add(unitTaxCalculationInfo);
								taxCalculationInfo.addUnitTaxCalculationInfo(unitNonResd);


								// Balance Area in Residential way of tax
								// calculation
								if (property.getIsExemptedFromTax() == null || !property.getIsExemptedFromTax()) {
									applicableTaxes.add(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD);
								}

								BigDecimal balanceArea = new BigDecimal(propertyDetail.getSitalArea().getArea())
										.subtract(new BigDecimal(propertyDetail.getNonResPlotArea().getArea()));

								UnitTaxCalculationInfo balanceAreaUnitTaxCalculationInfo = new UnitTaxCalculationInfo();
								balanceAreaUnitTaxCalculationInfo.setBaseRentEffectiveDate(category.getFromDate());
								balanceAreaUnitTaxCalculationInfo.setBaseRent(new BigDecimal(category.getCategory()
										.getCategoryAmount().toString()));

								balanceAreaUnitTaxCalculationInfo = openPlotUnitTaxCalculator
										.calculateUnitTax(balanceAreaUnitTaxCalculationInfo, propertyDetail, applicableTaxes,
												installment, Long.valueOf(propertyDetail.getExtra_field6()),
												balanceArea, property, category);

								balanceAreaUnitTaxCalculationInfo.setUnitArea(balanceArea);
								balanceAreaUnitTaxCalculationInfo.setFloorNumber(NMCPTISConstants.PROPTYPE_CAT_RESD);
								applicableTaxes.remove(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD);

								installmentWiseTaxPayable = installmentWiseTaxPayable.add(unitTaxCalculationInfo
										.getTotalTaxPayable());

								if (balanceAreaUnitTaxCalculationInfo != null) {
									installmentWiseTaxPayable = installmentWiseTaxPayable
											.add(balanceAreaUnitTaxCalculationInfo.getTotalTaxPayable());
									List<UnitTaxCalculationInfo> unitResd = new ArrayList<UnitTaxCalculationInfo>();
									unitResd.add(balanceAreaUnitTaxCalculationInfo);
									taxCalculationInfo.addUnitTaxCalculationInfo(unitResd);

								}

								totalTaxPayable = totalTaxPayable.add(installmentWiseTaxPayable);
							}
						}
					}
				}
			} else if (propTypeCode.equals(NMCPTISConstants.PROPTYPE_STATE_GOVT)
					|| propTypeCode.equals(NMCPTISConstants.PROPTYPE_CENTRAL_GOVT)) {

				Date occupationDate = property.getBasicProperty().getPropCreateDate();

				if (propertyTaxUtil.betweenOrBefore(occupationDate, installment.getFromDate(), installment.getToDate())) {

					UnitTaxCalculationInfo unitTaxCalculationInfo = govtPropertyUnitTaxCalculator.calculateUnitTax(
							property.getPropertyDetail(), propertyArea, installment,
							Long.valueOf(property.getPropertyDetail().getExtra_field6()), property);

					unitTaxCalculationInfo.setUnitOccupier(taxCalculationInfo.getPropertyOwnerName());

					totalAnnualLettingValue = totalAnnualLettingValue.add(unitTaxCalculationInfo
							.getAnnualRentAfterDeduction());
					taxCalculationInfo.setTotalAnnualLettingValue(totalAnnualLettingValue.setScale(0,
							BigDecimal.ROUND_HALF_UP));
					taxCalculationInfo.setBuildingCost(new BigDecimal(property.getPropertyDetail().getExtra_field3()));
					taxCalculationInfo.setOccupencyDate(occupationDate);

					List<UnitTaxCalculationInfo> unitTaxes = new ArrayList<UnitTaxCalculationInfo>();
					unitTaxes.add(unitTaxCalculationInfo);

					taxCalculationInfo.addUnitTaxCalculationInfo(unitTaxes);

					if (propTypeCode.equals(NMCPTISConstants.PROPTYPE_STATE_GOVT)) {
						taxCalculationInfo.setAlvPercentage(STATEGOVT_BUILDING_ALV_PERCENTAGE);
						taxCalculationInfo.setAmenities("N/A");
					} else {
						taxCalculationInfo.setAlvPercentage(CENTRALGOVT_BUILDING_ALV_PERCENTAGE);
						taxCalculationInfo.setAmenities(property.getPropertyDetail().getExtra_field4());
					}

					installmentWiseTaxPayable = installmentWiseTaxPayable.add(unitTaxCalculationInfo
							.getTotalTaxPayable());

					totalTaxPayable = totalTaxPayable.add(unitTaxCalculationInfo.getTotalTaxPayable());
				}

			} else if (propTypeCode.equals(NMCPTISConstants.PROPTYPE_MIXED)) {
				LOGGER.info("Calculate Residential & Non-Residential Building-Property Tax");
				Set<FloorIF> newFloorSet = property.getPropertyDetail().getFloorDetails();
				for (FloorIF floorIF : newFloorSet) {
					if (floorIF != null) {
						FloorImpl floorImpl = (FloorImpl) floorIF;

						Date occupationDate = null;
						try {
							occupationDate = dateFormatter.parse(floorImpl.getExtraField3());
						} catch (ParseException e) {
							LOGGER.error("ParseException occured in method : calculatePropertyTax", e);
						}
						if (propertyTaxUtil.betweenOrBefore(occupationDate, installment.getFromDate(),
								installment.getToDate())) {
							/**
							 * Getting the applicable Base Rent
							 */
							categories = propertyTaxUtil.getBoundaryCategories(floorImpl, propertyArea, installment,
									property.getPropertyDetail());
							List<UnitTaxCalculationInfo> calculationInfos = new ArrayList<UnitTaxCalculationInfo>();

							if (floorImpl.getManualAlv() != null) {
								UnitTaxCalculationInfo unitTaxCalculationInfo = new UnitTaxCalculationInfo();
								unitTaxCalculationInfo.setBaseRent(BigDecimal.ZERO);
								unitTaxCalculationInfo.setTaxExemptionReason(floorImpl.getTaxExemptedReason());

								unitTaxCalculationInfo = resdAndNonResdUnitTaxCalculator.calculateUnitTax(
										unitTaxCalculationInfo, floorImpl, installment,
										Long.valueOf(property.getPropertyDetail().getExtra_field6()),
										property.getIsExemptedFromTax(), property, null);

								calculationInfos.add(unitTaxCalculationInfo);

								installmentWiseTaxPayable = installmentWiseTaxPayable.add(unitTaxCalculationInfo
										.getTotalTaxPayable());

								totalTaxPayable = totalTaxPayable.add(unitTaxCalculationInfo.getTotalTaxPayable());

							} else {

								for (BoundaryCategory category : categories) {

									UnitTaxCalculationInfo unitTaxCalculationInfo = new UnitTaxCalculationInfo();
									unitTaxCalculationInfo.setBaseRentEffectiveDate(category.getFromDate());
									unitTaxCalculationInfo.setBaseRent(new BigDecimal(category.getCategory()
											.getCategoryAmount().toString()));
									unitTaxCalculationInfo.setTaxExemptionReason(floorImpl.getTaxExemptedReason());

									unitTaxCalculationInfo = resdAndNonResdUnitTaxCalculator.calculateUnitTax(
											unitTaxCalculationInfo, floorImpl, installment,
											Long.valueOf(property.getPropertyDetail().getExtra_field6()),
											property.getIsExemptedFromTax(), property, category);

									calculationInfos.add(unitTaxCalculationInfo);

									installmentWiseTaxPayable = installmentWiseTaxPayable.add(unitTaxCalculationInfo
											.getTotalTaxPayable());

									totalTaxPayable = totalTaxPayable.add(unitTaxCalculationInfo.getTotalTaxPayable());
								}
							}

							taxCalculationInfo.addUnitTaxCalculationInfo(calculationInfos);
						}
					}
				}
				taxCalculationInfo.setTotalAnnualLettingValue(totalAnnualLettingValue.setScale(0,
						BigDecimal.ROUND_HALF_UP));
			}
			LOGGER.debug("totalTaxPayable " + totalTaxPayable);
			LOGGER.info(" INSTALLMENT: " + installment.getDescription() + " AMOUNT:" + installmentWiseTaxPayable);
			// Set total tax payable after rounding up to nearest Rupee
			taxCalculationInfo.setTotalTaxPayable(totalTaxPayable.setScale(0, BigDecimal.ROUND_UP));

			// Sort UnitTaxCalculationInfo Object Floor Wise then Unit Wise
			if (!propTypeCode.equals(NMCPTISConstants.PROPTYPE_OPEN_PLOT)) {
				for (List<UnitTaxCalculationInfo> unitTaxes : taxCalculationInfo.getUnitTaxCalculationInfos()) {
					Collections.sort(unitTaxes, UnitTaxInfoComparator.getUnitComparator(
							UnitTaxInfoComparator.FLOOR_SORT, UnitTaxInfoComparator.UNIT_SORT));
				}
			}

			// Sort AreaTaxCalculationInfo Base Rent Wise
			for (List<UnitTaxCalculationInfo> unitInfos : taxCalculationInfo.getUnitTaxCalculationInfos()) {
				for (UnitTaxCalculationInfo info : unitInfos) {
					if (info.getAreaTaxCalculationInfos() != null) {
						Collections.sort(info.getAreaTaxCalculationInfos(), AreaTaxInfoComparator
								.decending(AreaTaxInfoComparator
										.getAreaTaxComparator(AreaTaxInfoComparator.BASERATE_SORT)));
					}
				}
			}

			try {
				propertyTaxUtil.consolidateUnitTaxCalcInfos(taxCalculationInfo, installment, property
						.getPropertyDetail().getExtra_field5(), property.getPropertyDetail().getExtra_field4(),
						property, categories);

				if (propTypeCode.equals(NMCPTISConstants.PROPTYPE_MIXED) || propTypeCode.equals(NMCPTISConstants.PROPTYPE_RESD)) {
					propertyTaxUtil.calcConsolidatedBigBldgTaxs(taxCalculationInfo, installment, property.getIsExemptedFromTax());
				}
			} catch (ParseException e) {
				LOGGER.error("Error while Parsing Dates during consolidating units..!!", e);
			}

			String taxCalculationInfoXML = propertyTaxUtil.generateTaxCalculationXML(taxCalculationInfo);
			LOGGER.info("Tax Calculation Info XML for installment " + installment + " is : " + taxCalculationInfoXML);
			taxCalculationInfo.setTaxCalculationInfoXML(taxCalculationInfoXML);
			taxCalculationMap.put(installment, taxCalculationInfo);
		}
		return taxCalculationMap;
	}

	/**
	 * Skipping the first Guidance Value when its not applicable for occupationDate
	 * @param categories
	 * @param occupationDate
	 * @return
	 */
	private boolean isIgnoreFirstBaseRent(List<BoundaryCategory> categories, Date occupationDate) {
		boolean result = false;
		if (categories.size() > 1) {
			result = PropertyTaxUtil.afterOrEqual(occupationDate, categories.get(categories.size() - 1).getFromDate());
		}
		return result;
	}

	private TaxCalculationInfo addPropertyInfo(Property property) {
		TaxCalculationInfo taxCalculationInfo = new TaxCalculationInfo();
		PTISCacheManagerInteface ptisCachMgr = new PTISCacheManager();
		// Add Property Info
		taxCalculationInfo.setPropertyOwnerName(ptisCachMgr.buildOwnerFullName(property.getPropertyOwnerSet()));
		taxCalculationInfo.setPropertyAddress(ptisCachMgr.buildAddressByImplemetation(property.getBasicProperty()
				.getAddress()));
		taxCalculationInfo.setHouseNumber(property.getBasicProperty().getAddress().getHouseNoBldgApt());
		taxCalculationInfo.setArea(property.getBasicProperty().getPropertyID().getArea().getName());
		taxCalculationInfo.setZone(property.getBasicProperty().getPropertyID().getZone().getName());
		taxCalculationInfo.setWard(property.getBasicProperty().getPropertyID().getWard().getName());
		if (property.getPropertyDetail().getSitalArea() != null) {
			taxCalculationInfo.setPropertyArea(new BigDecimal(property.getPropertyDetail().getSitalArea().getArea()
					.toString()));
		}
		taxCalculationInfo.setPropertyType(property.getPropertyDetail().getPropertyTypeMaster().getType());
		taxCalculationInfo.setParcelId(property.getBasicProperty().getGisReferenceNo());
		taxCalculationInfo.setIndexNo(property.getBasicProperty().getUpicNo());
		return taxCalculationInfo;
	}

	public Map<String, BigDecimal> getMiscTaxesForProp(List<UnitTaxCalculationInfo> unitTaxCalcInfos) {

		Map<String, BigDecimal> taxMap = new HashMap<String, BigDecimal>();
		for (UnitTaxCalculationInfo unitTaxCalcInfo : unitTaxCalcInfos) {
			for (MiscellaneousTax miscTax : unitTaxCalcInfo.getMiscellaneousTaxes()) {
				if (taxMap.get(miscTax.getTaxName()) == null) {
					taxMap.put(miscTax.getTaxName(), miscTax.getTotalCalculatedTax());
				} else {
					taxMap.put(miscTax.getTaxName(),
							taxMap.get(miscTax.getTaxName()).add(miscTax.getTotalCalculatedTax()));
				}
			}
		}

		return taxMap;
	}

	public void setResidentialUnitTaxCalculator(ResidentialUnitTaxCalculator residentialUnitTaxCalculator) {
		this.residentialUnitTaxCalculator = residentialUnitTaxCalculator;
	}

	public void setNonResidentialUnitTaxCalculator(NonResidentialUnitTaxCalculator nonResidentialUnitTaxCalculator) {
		this.nonResidentialUnitTaxCalculator = nonResidentialUnitTaxCalculator;
	}

	public void setOpenPlotUnitTaxCalculator(OpenPlotUnitTaxCalculator openPlotUnitTaxCalculator) {
		this.openPlotUnitTaxCalculator = openPlotUnitTaxCalculator;
	}

	public void setPropertyTaxUtil(PropertyTaxUtil propertyTaxUtil) {
		this.propertyTaxUtil = propertyTaxUtil;
	}

	public void setGovtPropertyUnitTaxCalculator(GovtPropertyUnitTaxCalculator govtPropertyUnitTaxCalculator) {
		this.govtPropertyUnitTaxCalculator = govtPropertyUnitTaxCalculator;
	}

	public void setResdAndNonResdUnitTaxCalculator(ResdAndNonResdUnitTaxCalculator resdAndNonResdUnitTaxCalculator) {
		this.resdAndNonResdUnitTaxCalculator = resdAndNonResdUnitTaxCalculator;
	}

}
