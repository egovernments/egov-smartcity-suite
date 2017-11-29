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
package org.egov.ptis.client.service;

import org.apache.log4j.Logger;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentDao;
import org.egov.demand.model.EgDemandReasonDetails;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infstr.services.PersistenceService;
import org.egov.ptis.client.model.TaxDetail;
import org.egov.ptis.client.model.calculator.APMiscellaneousTaxDetail;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.BoundaryCategory;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.UnitCalculationDetail;
import org.egov.ptis.domain.model.calculator.MiscellaneousTax;
import org.egov.ptis.domain.model.calculator.MiscellaneousTaxDetail;
import org.egov.ptis.domain.model.calculator.TaxCalculationInfo;
import org.egov.ptis.domain.model.calculator.UnitTaxCalculationInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static java.math.BigDecimal.ZERO;
import static org.egov.ptis.constants.PropertyTaxConstants.*;

// [CODE REVIEW] put javadoc explaining what this new class is for. Is it only used for migrating the XML? If so, 
// "PropertyNoticeService" is not the right name to use
public class PropertyNoticeService {

	private static final String STR_MIGRATED = "Migrated";
	private static final Logger LOGGER = Logger.getLogger(PropertyNoticeService.class);

	private String indexNumber;
	private BasicProperty basicProperty;
	private Map<Date, TaxCalculationInfo> taxCalculations = new TreeMap<Date, TaxCalculationInfo>();
	private List<UnitCalculationDetail> unitCalcDetails;
	private PropertyTaxUtil propertyTaxUtil;
	private PersistenceService<BasicProperty, Long> basicPropertyService;
	private Map<Date, String> occupancyAndPropertyType = new TreeMap<Date, String>();
	@Autowired
	private ModuleService moduleDao;
	@Autowired
	private InstallmentDao installmentDao;

	private static Map<Integer, Map<String, Map<Date, BigDecimal>>> dateAndTotalCalcTaxByTaxForUnit;

	public PropertyNoticeService() {
	}

	public PropertyNoticeService(BasicProperty basicProperty, PropertyTaxUtil propertyTaxUtil,
			PersistenceService<BasicProperty, Long> basicPropertyService) {
		this.basicProperty = basicProperty;
		this.propertyTaxUtil = propertyTaxUtil;
		this.basicPropertyService = basicPropertyService;
	}

	// [CODE REVIEW] can this class be defined as a spring bean instead?
	public static PropertyNoticeService createNoticeService(BasicProperty basicProperty,
			PropertyTaxUtil propertyTaxUtil, PersistenceService<BasicProperty, Long> basicPropertyService) {
		return new PropertyNoticeService(basicProperty, propertyTaxUtil, basicPropertyService);
	}

	public Map<Date, TaxCalculationInfo> getTaxCaluculations() {
		return taxCalculations;
	}

	public void setTaxCaluculations(Map<Date, TaxCalculationInfo> taxCaluculations) {
		this.taxCalculations = taxCaluculations;
	}

	public List<UnitCalculationDetail> getUnitCalcDetails() {
		return unitCalcDetails;
	}

	public void setUnitCalcDetails(List<UnitCalculationDetail> unitCalcDetails) {
		this.unitCalcDetails = unitCalcDetails;
	}

	public void setPropertyTaxUtil(PropertyTaxUtil propertyTaxUtil) {
		this.propertyTaxUtil = propertyTaxUtil;
	}

	public String getIndexNumber() {
		return indexNumber;
	}

	public void setIndexNumber(String indexNumber) {
		this.indexNumber = indexNumber;
	}

	public PropertyTaxUtil getPropertyTaxUtil() {
		return propertyTaxUtil;
	}

	private void preparePropertyTypesByOccupancy(Map<Date, Property> propertyByOccupancy) {
		for (Map.Entry<Date, Property> entry : propertyByOccupancy.entrySet()) {
			occupancyAndPropertyType.put(entry.getKey(), entry.getValue().getPropertyDetail().getPropertyTypeMaster()
					.getCode());
		}
	}

	public void migrateTaxXML() {
		LOGGER.debug("Entered into migrateTaxXML basicProperty.upicNo=" + basicProperty.getUpicNo());

		try {
			Map<Date, Property> propertyByOccupancy = getPropertiesByOccupancy();

			preparePropertyTypesByOccupancy(propertyByOccupancy);
			prepareInstallmentWiseTaxCalcs(propertyByOccupancy);

			initCurrentUnitSlabs();

			// here change can be ALV, guidance value, occupancy date or slab
			// change
			List<UnitCalculationDetail> unitCalculationDetails = getTheRowsForChange();

			persistUnitCalcDetails(unitCalculationDetails);
		} catch (Exception e) {
			String errorMsg = "Error in Tax XML migration for " + basicProperty.getUpicNo();
			LOGGER.error(errorMsg, e);
			throw new ApplicationRuntimeException(errorMsg, e);
		}
		LOGGER.debug("Exiting from migrateTaxXML");
	}

	private void initCurrentUnitSlabs() {
		dateAndTotalCalcTaxByTaxForUnit = new TreeMap<Integer, Map<String, Map<Date, BigDecimal>>>();
	}

	private List<UnitCalculationDetail> getTheRowsForChange() {
		LOGGER.debug("Entered into getTheRowsForChange");

		List<UnitCalculationDetail> unitCalculationDetails = new ArrayList<UnitCalculationDetail>();

		Map<Integer, UnitTaxCalculationInfo> prevInstallmentUnitsByUnitNo = new TreeMap<Integer, UnitTaxCalculationInfo>();
		Map<Integer, UnitTaxCalculationInfo> nextInstallmentUnitsByUnitNo = new TreeMap<Integer, UnitTaxCalculationInfo>();

		InstallmentUnitTax instPrevCurrUnitTax = null;
		List<String> emptyList = Collections.<String> emptyList();
		Boundary propertyArea = null;

		for (Map.Entry<Date, TaxCalculationInfo> taxCalcAndInstallment : taxCalculations.entrySet()) {

			Module module = moduleDao.getModuleByName(PropertyTaxConstants.PTMODULENAME);
			Installment installment = installmentDao.getInsatllmentByModuleForGivenDate(module,
					taxCalcAndInstallment.getKey());
			TaxCalculationInfo taxCalcInfo = taxCalcAndInstallment.getValue();

			LOGGER.info("getTheRowsForChange - Installment =" + installment);

			prevInstallmentUnitsByUnitNo.putAll(nextInstallmentUnitsByUnitNo);

			// taking units freshly for each installment by clearing
			// the
			// map
			nextInstallmentUnitsByUnitNo.clear();

			if (basicProperty.getProperty().getAreaBndry() != null) {
				propertyArea = basicProperty.getProperty().getAreaBndry();
			} else {
				propertyArea = basicProperty.getPropertyID().getArea();
			}

			// checking for open plot, this will not work with Non Open plot
			// property
			// as we getting guidance value for per floor basis
			/*
			 * categories = propertyTaxUtil.getBoundaryCategories(null,
			 * propertyArea, installment,
			 * basicProperty.getProperty().getPropertyDetail());
			 */

			// For the first installment, directly saving the
			// details
			// into table as there is nothing
			// to compare against
			if (isStartingInstallment(prevInstallmentUnitsByUnitNo)) {

				for (Map.Entry<Integer, UnitTaxCalculationInfo> entry : nextInstallmentUnitsByUnitNo.entrySet()) {
					instPrevCurrUnitTax = InstallmentUnitTax.create(installment, null, entry.getValue());

					unitCalculationDetails.addAll(createUnitCalculationDetail(basicProperty.getProperty(), installment,
							taxCalcInfo, instPrevCurrUnitTax.getCurrentUnitAsList(), false));

					setOccupancyDateAsFromDate(unitCalculationDetails);

					instPrevCurrUnitTax.getCurrentUnitTaxSlabs(emptyList);
				}

			} else {
				for (Map.Entry<Integer, UnitTaxCalculationInfo> currentUnitTaxEntry : nextInstallmentUnitsByUnitNo
						.entrySet()) {

					instPrevCurrUnitTax = InstallmentUnitTax.create(installment,
							prevInstallmentUnitsByUnitNo.get(currentUnitTaxEntry.getKey()),
							currentUnitTaxEntry.getValue());

					if (instPrevCurrUnitTax.isCurrentUnitNewUnit()) {

						unitCalculationDetails.addAll(createUnitCalculationDetail(basicProperty.getProperty(),
								installment, taxCalcInfo, instPrevCurrUnitTax.getCurrentUnitAsList(), false));

						instPrevCurrUnitTax.getCurrentUnitTaxSlabs(emptyList);

					} else {

						if ((instPrevCurrUnitTax.isSameALV() && instPrevCurrUnitTax.isSameOccupancy())
								|| !instPrevCurrUnitTax.isSameALV()) {
							// check the occupancy date, if different occupancy
							// then create a row for this
							// check any tax slab is effective when alv is same
							// n occupancy is same

							if (!instPrevCurrUnitTax.isSameALV()) {

								// consider here the
								// isPrevCurrALVSame = false and
								// isPrevCurrOccupancySame = false

								unitCalculationDetails.addAll(createUnitCalculationDetail(basicProperty.getProperty(),
										installment, taxCalcInfo, instPrevCurrUnitTax.getCurrentUnitAsList(), false));
								instPrevCurrUnitTax.getCurrentUnitTaxSlabs(emptyList);
							}

							unitCalculationDetails.addAll(getUnitCalDetailsForSlabChange(basicProperty.getProperty(),
									taxCalcInfo, instPrevCurrUnitTax));

						}

						if (instPrevCurrUnitTax.isSameALV() && !instPrevCurrUnitTax.isSameOccupancy()) {
							// indicates modification

							unitCalculationDetails.addAll(createUnitCalculationDetail(basicProperty.getProperty(),
									installment, taxCalcInfo, instPrevCurrUnitTax.getCurrentUnitAsList(), false));

							instPrevCurrUnitTax.getCurrentUnitTaxSlabs(emptyList);
						}
					}

				}

			}
		}

		LOGGER.debug("Exiting from getTheRowsForChange");
		return unitCalculationDetails;
	}

	private void setOccupancyDateAsFromDate(List<UnitCalculationDetail> unitCalculationDetails) {
		LOGGER.debug("Entered into setOccupancyDateAsFromDate");

		for (UnitCalculationDetail unitCalcDetail : unitCalculationDetails) {
			unitCalcDetail.setFromDate(unitCalcDetail.getOccupancyDate());
		}

		LOGGER.debug("Exiting from setOccupancyDateAsFromDate");
	}

	public void persistUnitCalcDetails(List<UnitCalculationDetail> unitCalculationDetails) {
		LOGGER.debug("Entered into persistUnitCalcDetails");

		basicProperty.getProperty().addAllUnitCalculationDetails(
				new LinkedHashSet<UnitCalculationDetail>(unitCalculationDetails));
		basicProperty.setIsTaxXMLMigrated('Y');
		basicPropertyService.update(basicProperty);

		LOGGER.debug("Exiting from persistUnitCalcDetails");
	}

	/**
	 * @param prevInstallmentUnitsByUnitNo
	 * @return
	 */
	private boolean isStartingInstallment(Map<Integer, UnitTaxCalculationInfo> prevInstallmentUnitsByUnitNo) {
		return prevInstallmentUnitsByUnitNo.isEmpty();
	}

	private Map<Date, Property> getPropertiesByOccupancy() {
		LOGGER.debug("Entered into getPropertiesByOccupancy");

		Map<Date, Property> propertyByCreatedDate = new TreeMap<Date, Property>();
		Map<Date, Property> propertyByOccupancyDate = new TreeMap<Date, Property>();

		for (Property property : basicProperty.getPropertySet()) {
			if (property.getRemarks() == null || !property.getRemarks().startsWith(STR_MIGRATED)) {
				propertyByCreatedDate.put(property.getCreatedDate(), property);
			}
		}

		Date effectiveDate = null;

		for (Map.Entry<Date, Property> entry : propertyByCreatedDate.entrySet()) {
			effectiveDate = entry.getValue().getPropertyDetail().getDateOfCompletion() == null ? entry.getValue()
					.getEffectiveDate() : entry.getValue().getPropertyDetail().getDateOfCompletion();

			propertyByOccupancyDate.put(effectiveDate, entry.getValue());
		}

		LOGGER.debug("Exiting from getPropertiesByOccupancy");

		return propertyByOccupancyDate;
	}

	private int getBeginIndex(Map<Date, Property> propertyByOccupancyDate) {
		LOGGER.debug("Entered into getBeginIndex");

		int beginIndex = 0;

		List<Date> occupancyDates = new ArrayList<Date>(propertyByOccupancyDate.keySet());

		if (occupancyDates.size() > 1) {
			Property firstProperty = propertyByOccupancyDate.get(occupancyDates.get(0));
			Property nextProperty = propertyByOccupancyDate.get(occupancyDates.get(1));

			if (firstProperty.getPropertyDetail().getPropertyMutationMaster().getCode()
					.equalsIgnoreCase(PropertyTaxConstants.MUTATION_CODE_NEW)
					&& nextProperty.getPropertyDetail().getPropertyMutationMaster().getCode()
							.equalsIgnoreCase(PropertyTaxConstants.MUTATION_CODE_DATA_ENTRY)) {

				LOGGER.debug("Returning from getBeginIndex with value 1");
				return 1;
			}
		}

		LOGGER.debug("Exiting from getBeginIndex, beginIndex=" + beginIndex);
		return beginIndex;

	}

	private void prepareInstallmentWiseTaxCalcs(Map<Date, Property> propertyByOccupancyDate) {
		LOGGER.info("Entered into prepareInstallmentWiseTaxCalcs occupancyDates=" + propertyByOccupancyDate.keySet());

		Property prevProperty = null;
		Property nextProperty = null;
		List<Date> occupancyDates = new ArrayList<Date>(propertyByOccupancyDate.keySet());

		if (occupancyDates.size() > 1) {
			int first = getBeginIndex(propertyByOccupancyDate);
			int next = (first + 1);

			prevProperty = propertyByOccupancyDate.get(occupancyDates.get(first));
			nextProperty = propertyByOccupancyDate.get(occupancyDates.get(next));

			taxCalculations.putAll(propertyTaxUtil.getTaxCalInfoMap(prevProperty.getPtDemandSet(),
					occupancyDates.get(first)));

			// Consider the installment tax calcs only effective for the
			// prevProperty
			taxCalculations.keySet().removeAll(
					getInstallmentStartDates(
							propertyTaxUtil.getInstallmentListByStartDate(getPropertyOccupancyDate(nextProperty)),
							occupancyDates.get(next)));

			List<Date> retainDates = new ArrayList<Date>();

			for (int i = next; i < propertyByOccupancyDate.size() - 1; i++) {

				prevProperty = propertyByOccupancyDate.get(occupancyDates.get(i));
				nextProperty = propertyByOccupancyDate.get(occupancyDates.get(i + 1));

				taxCalculations.putAll(propertyTaxUtil.getTaxCalInfoMap(prevProperty.getPtDemandSet(),
						occupancyDates.get(i)));

				retainDates.addAll(new ArrayList<Date>(taxCalculations.keySet()));

				retainDates.addAll(getInstallmentStartDates(
						propertyTaxUtil.getInstallmentListByStartDate(getPropertyOccupancyDate(prevProperty)),
						occupancyDates.get(i)));

				taxCalculations.keySet().retainAll(retainDates);

				// Consider the installment tax calcs only effective for the
				// prevProperty
				taxCalculations.keySet().removeAll(
						getInstallmentStartDates(
								propertyTaxUtil.getInstallmentListByStartDate(getPropertyOccupancyDate(nextProperty)),
								occupancyDates.get(i + 1)));
			}
		}
		Date activePropOccupancyDate = occupancyDates.get(occupancyDates.size() - 1);
		Property activeProperty = propertyByOccupancyDate.get(activePropOccupancyDate);

		Map<Date, TaxCalculationInfo> activePropTaxCalcs = propertyTaxUtil.getTaxCalInfoMap(
				activeProperty.getPtDemandSet(), activePropOccupancyDate);

		// Consider the installment tax calcs only effective for the
		// activeProperty
		activePropTaxCalcs.keySet().retainAll(
				getInstallmentStartDates(
						propertyTaxUtil.getInstallmentListByStartDate(getPropertyOccupancyDate(activeProperty)),
						activePropOccupancyDate));

		taxCalculations.putAll(activePropTaxCalcs);

		LOGGER.debug("prepareInstallmentWiseTaxCalcs - installments=" + taxCalculations.keySet());
		LOGGER.debug("Exiting from prepareInstallmentWiseTaxCalcs");
	}

	private List<Date> getInstallmentStartDates(List<Installment> installments, Date occupancyDate) {
		LOGGER.debug("Entered into getInstallmentStartDates installments=" + installments + ", occupancyDate="
				+ occupancyDate);
		List<Date> installmentStartDates = new ArrayList<Date>();

		for (Installment installment : installments) {
			if (propertyTaxUtil.between(occupancyDate, installment.getFromDate(), installment.getToDate())) {
				installmentStartDates.add(occupancyDate);
			} else {
				installmentStartDates.add(installment.getFromDate());
			}
		}

		LOGGER.debug("Exiting from getInstallmentStartDates - installmentStartDates=" + installmentStartDates);
		return installmentStartDates;
	}

	/**
	 * @param Property
	 * @return Date the occupancy date
	 */
	private Date getPropertyOccupancyDate(Property property) {
		return property.getPropertyDetail().getDateOfCompletion() == null ? property.getEffectiveDate() : property
				.getPropertyDetail().getDateOfCompletion();
	}

	/**
	 * @param basicProperty
	 * @param unitCalculationDetails
	 * @param dateAndTotalCalcTaxByTaxForUnit
	 * @param installment
	 * @param taxCalcInfo
	 * @param currentUnitTax
	 * @param previousUnitTax
	 * @param units
	 */
	private List<UnitCalculationDetail> getUnitCalDetailsForSlabChange(Property property,
			TaxCalculationInfo taxCalcInfo, InstallmentUnitTax instUnitTax) {

		Map<String, Date> slabChangedTaxes = instUnitTax.getSlabChangedTaxes();

		List<UnitCalculationDetail> unitCalculationDetails = new ArrayList<UnitCalculationDetail>();

		if (slabChangedTaxes.isEmpty()) {
			LOGGER.debug("slabChangedTaxes - No tax slabs have changed");
		} else {
			LOGGER.debug("slabChangedTaxes -" + slabChangedTaxes);

			instUnitTax.getCurrentUnitTaxSlabs(new ArrayList<String>(slabChangedTaxes.keySet()));
		}

		return unitCalculationDetails;
	}

	private List<UnitCalculationDetail> createUnitCalculationDetail(Property property, Installment installment,
			TaxCalculationInfo taxCalcInfo, List<UnitTaxCalculationInfo> unitTaxes, Boolean isTaxSlabChange) {
		LOGGER.debug("Entered into createUnitCalculationDetail");
		LOGGER.debug("createUnitCalculationDetail - property=" + property + ", installment=" + installment
				+ ", unitTaxes.size=" + unitTaxes.size() + ", isTaxSlabChange=" + isTaxSlabChange);

		UnitCalculationDetail unitCalculationDetail = null;
		List<UnitCalculationDetail> unitCalculationDetails = new ArrayList<UnitCalculationDetail>();

		try {
			for (UnitTaxCalculationInfo unitTax : unitTaxes) {
				unitCalculationDetail = new UnitCalculationDetail();
				unitCalculationDetail.setCreatedTimeStamp(new Date());
				unitCalculationDetail.setLastUpdatedTimeStamp(new Date());
				unitCalculationDetail.setUnitNumber(Integer.valueOf(unitTax.getFloorNumber()));
				unitCalculationDetail.setUnitArea(unitTax.getFloorArea());
				unitCalculationDetail.setOccupancyDate(unitTax.getOccpancyDate());

				unitCalculationDetail.setGuidanceValue(unitTax.getBaseRate() == null ? ZERO : unitTax.getBaseRate());
				unitCalculationDetail.setGuidValEffectiveDate(unitTax.getBaseRateEffectiveDate() == null ? unitTax
						.getOccpancyDate() : unitTax.getBaseRateEffectiveDate());
				unitCalculationDetail.setUnitOccupation(buildUnitOccupation(
						occupancyAndPropertyType.get(unitTax.getOccpancyDate()), unitTax));

				unitCalculationDetail.setInstallmentFromDate(installment.getFromDate());
				unitCalculationDetail.setMonthlyRent(unitTax.getMrv() == null ? ZERO : unitTax.getMrv());

				if (isTaxSlabChange) {
					unitCalculationDetail.setFromDate(unitTax.getOccpancyDate());
				}

				setAnnualLettingValues(property, installment, unitCalculationDetail, taxCalcInfo);
				unitCalculationDetails.addAll(setMiscellaneousTaxDetails(property, installment, unitCalculationDetail,
						unitTax));
				setUnitAreaCalculationDetails(property, installment, unitCalculationDetail, unitTax);
			}
		} catch (Exception e) {
			LOGGER.error("Error while parsing unit tax instDate", e);
			throw new ApplicationRuntimeException("Error while parsing unit tax instDate", e);
		}

		LOGGER.debug("createUnitCalculationDetail - unitCalculationDetails=" + unitCalculationDetails);
		LOGGER.debug("Exiting from createUnitCalculationDetail");

		return unitCalculationDetails;
	}

	private void setAnnualLettingValues(Property property, Installment installment,
			UnitCalculationDetail unitCalculationDetail, TaxCalculationInfo taxCalcInfo) {
		LOGGER.debug("Entered into setAnnualLettingValues");
		LOGGER.debug("setAnnualLettingValues - property=" + property + ", installment=" + installment
				+ ", unitCalculationDetail=" + unitCalculationDetail);

		Map<String, BigDecimal> taxNameAndALV = new TreeMap<String, BigDecimal>();

		List<UnitTaxCalculationInfo> unitTaxes = taxCalcInfo.getUnitTaxCalculationInfos();

		LOGGER.debug("setAnnualLettingValues - unitCalculationDetail=" + unitCalculationDetail);
		LOGGER.debug("Exiting from setAnnualLettingValues");
	}

	private List<UnitCalculationDetail> setMiscellaneousTaxDetails(Property property, Installment installment,
			UnitCalculationDetail unitCalculationDetail, UnitTaxCalculationInfo consolidatedUnitTax) {

		LOGGER.debug("Entered into setMiscellaneousTaxDetails");
		LOGGER.debug("setMiscellaneousTaxDetails - property=" + property + ", installment=" + installment
				+ ", unitCalculationDetail=" + unitCalculationDetail);

		BigDecimal totalCalculatedTax = BigDecimal.ZERO;

		Integer totalNoOfDays = PropertyTaxUtil.getNumberOfDays(installment.getFromDate(), installment.getToDate())
				.intValue();

		List<EgDemandReasonDetails> demandReasonDetails = new ArrayList<EgDemandReasonDetails>();

		String propertyType = property.getPropertyDetail().getPropertyTypeMaster().getCode();
		/*
		 * String amenities = property.getPropertyDetail().getExtra_field4();
		 * String propertyTypeCategory =
		 * property.getPropertyDetail().getExtra_field5();
		 */
		String amenities = "";
		String propertyTypeCategory = "";

		List<UnitCalculationDetail> unitCalculationDetails = new ArrayList<UnitCalculationDetail>();
		Map<String, TaxDetail> taxDetailAndTaxName = new HashMap<String, TaxDetail>();

		BoundaryCategory boundaryCategory = null;

		Integer noOfDaysForNewTaxSlab = 0;

		for (MiscellaneousTax miscTax : consolidatedUnitTax.getMiscellaneousTaxes()) {

			if (hasNonHistoryTaxDetails(miscTax.getTaxDetails())) {

				String demandReasonCode = miscTax.getTaxName();

				BigDecimal alv = BigDecimal.ZERO;
				BigDecimal taxPercentage = BigDecimal.ZERO;
				BigDecimal calculatedAnnualTax = BigDecimal.ZERO;
				BigDecimal calculatedActualTax = BigDecimal.ZERO;
				BigDecimal demandRsnDtlPercResult = BigDecimal.ZERO;

				LOGGER.debug("setMiscellaneousTaxDetails - demandReasonCode=" + demandReasonCode + ", alv = " + alv);

				demandReasonDetails = propertyTaxUtil.getDemandReasonDetails(demandReasonCode, alv, installment);
				EgDemandReasonDetails demandReasonDetail = demandReasonDetails.get(demandReasonDetails.size() - 1);

				if (propertyType != null && propertyType.equalsIgnoreCase(OWNERSHIP_TYPE_STATE_GOVT)
						&& miscTax.getTaxName().equalsIgnoreCase(DEMANDRSN_CODE_GENERAL_TAX)) {

					demandRsnDtlPercResult = BigDecimal.ZERO;

					if (demandReasonDetail != null) {

						if (ZERO.equals(demandReasonDetail.getFlatAmount())) {
							Amount amount = new Amount(demandReasonDetail.getPercentage());
							demandRsnDtlPercResult = amount.percentOf(alv);
							amount.setValue(new BigDecimal(STATEGOVT_BUILDING_GENERALTAX_ADDITIONALDEDUCTION));
							calculatedAnnualTax = demandRsnDtlPercResult.subtract(amount
									.percentOf(demandRsnDtlPercResult));
						} else if (demandReasonDetail.getPercentage() == null) {
							calculatedAnnualTax = demandReasonDetail.getFlatAmount();
						} else {
							taxPercentage = demandReasonDetail.getPercentage();
						}

					}

				} else {
					if (demandReasonDetails != null) {
						if (ZERO.equals(demandReasonDetail.getFlatAmount())) {
							taxPercentage = demandReasonDetail.getPercentage();
						} else if (demandReasonDetail.getPercentage() == null) {
							calculatedAnnualTax = demandReasonDetail.getFlatAmount();
						} else {
							taxPercentage = demandReasonDetail.getPercentage();
						}
					}
				}

				if (demandReasonDetail != null && demandReasonDetail.getFlatAmount().compareTo(ZERO) > 0) {

					// FlatAmount must be the maximum amount
					if (demandReasonDetail.getIsFlatAmntMax().equals(Integer.valueOf(1))
							&& (calculatedAnnualTax.compareTo(demandReasonDetail.getFlatAmount()) > 0)) {
						calculatedAnnualTax = demandReasonDetail.getFlatAmount();
					}

					// FlatAmount must be the minimum amount
					if (demandReasonDetail.getIsFlatAmntMax().equals(Integer.valueOf(0))
							&& (calculatedAnnualTax.compareTo(demandReasonDetail.getFlatAmount()) < 0)) {
						calculatedAnnualTax = demandReasonDetail.getFlatAmount();
					}
				}

				MiscellaneousTaxDetail miscTaxDetail = new APMiscellaneousTaxDetail();
				miscTaxDetail.setFromDate(demandReasonDetail.getFromDate());
				miscTaxDetail.setTaxValue(demandReasonDetail.getPercentage());
				miscTaxDetail.setCalculatedTaxValue(calculatedAnnualTax);

				if (propertyType != null && propertyType.equalsIgnoreCase(OWNERSHIP_TYPE_CENTRAL_GOVT_50)) {
					calculatedActualTax = calculatedAnnualTax.setScale(0, ROUND_HALF_UP);
					// TODO need write business logic to get a Govt prop tax.
					calculatedAnnualTax = BigDecimal.ZERO;
					miscTaxDetail.setCalculatedTaxValue(calculatedAnnualTax);
					miscTaxDetail.setActualTaxValue(calculatedActualTax);
				}

				calculatedAnnualTax = calculatedAnnualTax.setScale(0, ROUND_HALF_UP);

				miscTax.setTotalCalculatedTax(calculatedAnnualTax);
				miscTax.setTotalActualTax(calculatedActualTax.setScale(0, ROUND_HALF_UP));
				miscTax.getTaxDetails().clear();

				if (!ZERO.equals(calculatedAnnualTax)) {

					/*
					 * if (propertyType != null &&
					 * propertyType.equalsIgnoreCase(PROPTYPE_CENTRAL_GOVT)) {
					 * totalActualTax = totalActualTax.add(actualTaxValue); }
					 * 
					 * totalCalculatedTax =
					 * totalCalculatedTax.add(calculatedTaxValue);
					 */

					if (demandReasonDetail != null) {
						TaxDetail taxDetail = new TaxDetail();
						taxDetail.setTaxName(demandReasonCode);
						taxDetail.setCalculatedTax(calculatedAnnualTax);
						taxDetail.setFromDate(demandReasonDetail.getFromDate());
						taxDetailAndTaxName.put(demandReasonCode, taxDetail);
					}
				}
			}
		}

		unitCalculationDetail.setTaxPayable(totalCalculatedTax);

		if (noOfDaysForNewTaxSlab > 0) {
			unitCalculationDetail.setTaxDays(noOfDaysForNewTaxSlab);
		} else {
			unitCalculationDetail.setTaxDays(totalNoOfDays);
		}

		unitCalculationDetails.add(unitCalculationDetail);

		LOGGER.debug("unitCalculationDetails= " + unitCalculationDetails + ", Exiting from setMiscellaneousTaxDetails");
		return unitCalculationDetails;
	}

	/**
	 *
	 * @param taxDetails
	 * @return true if there is a non history tax details, false if it only has
	 *         history tax detail
	 */
	private boolean hasNonHistoryTaxDetails(List<MiscellaneousTaxDetail> taxDetails) {

		for (MiscellaneousTaxDetail taxDetail : taxDetails) {
			if (isNonHistoryTaxDetail(taxDetail)) {
				return true;
			}
		}

		return false;

	}

	/**
	 * @param taxDetail
	 * @return
	 */
	private boolean isNonHistoryTaxDetail(MiscellaneousTaxDetail taxDetail) {
		return taxDetail.getIsHistory() == null
				|| taxDetail.getIsHistory().equals(PropertyTaxConstants.NON_HISTORY_TAX_DETAIL);
	}

	private static class Amount {
		private BigDecimal value;
		private static final BigDecimal HUNDRED = new BigDecimal(100);

		Amount(BigDecimal value) {
			this.value = value;
		}

		public void setValue(BigDecimal value) {
			this.value = value;
		}

		public BigDecimal percentOf(BigDecimal amount) {
			return amount.multiply(value).divide(HUNDRED);
		}
	}

	public void setUnitAreaCalculationDetails(Property property, Installment installment,
			UnitCalculationDetail unitCalculationDetail, UnitTaxCalculationInfo consolidatedUnitTax) {
		LOGGER.debug("Entered into setUnitAreaCalculationDetails");
		LOGGER.debug("setUnitAreaCalculationDetails - property=" + property + ", installment=" + installment
				+ ", unitCalculationDetail=" + unitCalculationDetail);

		LOGGER.debug("setUnitAreaCalculationDetails - unitCalculationDetail=" + unitCalculationDetail);
		LOGGER.debug("Exiting from setUnitAreaCalculationDetails");
	}

	private Boolean isZero(BigDecimal value) {
		return BigDecimal.ZERO.compareTo(value) == 0;
	}

	/**
	 * unit occupancy in the 2nd column of the Notice/Prativrutta will be like
	 * for Open Plot if the occupancy is owner then its "Open Plot". In case of
	 * Open Plot if the occupancy is tenant then its "OP-Name of Occupier". In
	 * case of State govt and Central govt property's the format is
	 * "Prefix-Owner" (ex. SGovt-Owner) In case of other property types if the
	 * occupancy is owner or vacant then its "Prefix-Occupancy" (ex.
	 * R-Owner/R-Vacant). In case of other property types if the occupancy is
	 * tenant then its "Prefix-Name of Occupier" (ex. R-Suma).
	 */

	private String buildUnitOccupation(String propType, UnitTaxCalculationInfo unit) {
		LOGGER.debug("Entered into buildUnitOccupation, propType=" + propType);

		StringBuilder occupierName = new StringBuilder();

		if (OWNERSHIP_TYPE_VAC_LAND.equals(propType)) {
			if (OCC_OWNER.equals(unit.getUnitOccupation()) || OCC_COMMERCIAL.equals(unit.getUnitOccupation())) {
				occupierName.append(propType);
			} else if (OCC_TENANT.equals(unit.getUnitOccupation())) {
				occupierName.append(OPEN_PLOT_SHORTFORM + "-" + unit.getUnitOccupier());
			}
		} else if (PROPTYPE_RESD.equals(propType)) {
			occupierName.append(RESD_SHORTFORM);
		} else if (PROPTYPE_NON_RESD.equals(propType)) {
			occupierName.append(NONRESD_SHORTFORM);
		} else if (OWNERSHIP_TYPE_STATE_GOVT.equals(propType)) {
			occupierName.append(STATE_GOVT_SHORTFORM + "-" + OCC_OWNER);
		} else if (OWNERSHIP_TYPE_CENTRAL_GOVT_50.equals(propType)) {
			occupierName.append(CENTRAL_GOVT_SHORTFORM + "-" + OCC_OWNER);
		} else if (PROPTYPE_MIXED.equals(propType)) {
			occupierName.append(MIXED_SHORTFORM);
		}

		if (!OWNERSHIP_TYPE_VAC_LAND.equals(propType) && !OWNERSHIP_TYPE_STATE_GOVT.equals(propType)
				&& !OWNERSHIP_TYPE_CENTRAL_GOVT_50.equals(propType)) {
			if (OCC_TENANT.equals(unit.getUnitOccupation())) {
				occupierName.append("-" + unit.getUnitOccupier());
			} else if (OCC_OWNER.equals(unit.getUnitOccupation()) || OCC_COMMERCIAL.equals(unit.getUnitOccupation())) {
				occupierName.append("-" + unit.getUnitOccupation());
			}
		}

		LOGGER.debug("occupierName=" + occupierName.toString() + "\nExiting from buildUnitOccupation");

		return occupierName.toString();
	}

	private static class InstallmentUnitTax {

		private UnitTaxCalculationInfo prevUnitTax;
		private UnitTaxCalculationInfo currentUnitTax;
		private Installment installment;

		public InstallmentUnitTax() {
		}

		public InstallmentUnitTax(Installment installment, UnitTaxCalculationInfo prevUnitTax,
				UnitTaxCalculationInfo currentUnitTax) {
			this.installment = installment;
			this.prevUnitTax = prevUnitTax;
			this.currentUnitTax = currentUnitTax;
		}

		public static InstallmentUnitTax create(Installment installment, UnitTaxCalculationInfo prevUnitTax,
				UnitTaxCalculationInfo currentUnitTax) {
			return new InstallmentUnitTax(installment, prevUnitTax, currentUnitTax);
		}

		public boolean isCurrentUnitNewUnit() {
			return prevUnitTax == null ? currentUnitTax == null ? false : true : false;
		}

		public boolean isSameALV() {
			return prevUnitTax.getNetARV().compareTo(currentUnitTax.getNetARV()) == 0;
		}

		public boolean isSameOccupancy() {
			return prevUnitTax.getOccpancyDate().equals(currentUnitTax.getOccpancyDate());
		}

		public boolean isCurrentUnitSlabChanged() {
			return true;
		}

		public UnitTaxCalculationInfo getPrevUnitTax() {
			return prevUnitTax;
		}

		public UnitTaxCalculationInfo getCurrentUnitTax() {
			return currentUnitTax;
		}

		public Installment getInstallment() {
			return installment;
		}

		public List<UnitTaxCalculationInfo> getCurrentUnitAsList() {
			List<UnitTaxCalculationInfo> units = new ArrayList<UnitTaxCalculationInfo>();
			units.add(currentUnitTax);
			return units;
		}

		public void getCurrentUnitTaxSlabs(List<String> taxNames) {
			LOGGER.debug("Entered into getCurrentUnitTaxSlabs");
			LOGGER.debug("getCurrentUnitTaxSlabs - dateAndPercentageByTaxForUnit: " + dateAndTotalCalcTaxByTaxForUnit);
			LOGGER.debug("getCurrentUnitTaxSlabs - taxNames: " + taxNames);

			Map<String, Map<Date, BigDecimal>> dateAndPercentageByTax = (dateAndTotalCalcTaxByTaxForUnit
					.get(currentUnitTax.getFloorNumber()) == null) ? new TreeMap<String, Map<Date, BigDecimal>>()
					: dateAndTotalCalcTaxByTaxForUnit.get(currentUnitTax.getFloorNumber());

			if (taxNames.isEmpty()) {
				for (MiscellaneousTax mt1 : currentUnitTax.getMiscellaneousTaxes()) {
					Map<Date, BigDecimal> dateAndPercentage1 = new TreeMap<Date, BigDecimal>();
					for (MiscellaneousTaxDetail mtd : mt1.getTaxDetails()) {
						if (isHistory(mtd)) {
							dateAndPercentage1.put(mtd.getFromDate(), mtd.getCalculatedTaxValue());
							dateAndPercentageByTax.put(mt1.getTaxName(), dateAndPercentage1);
							break;
						}
					}
				}
			} else {
				for (MiscellaneousTax mt2 : currentUnitTax.getMiscellaneousTaxes()) {
					if (taxNames.contains(mt2.getTaxName())) {
						Map<Date, BigDecimal> dateAndPercentage2 = new TreeMap<Date, BigDecimal>();

						MiscellaneousTaxDetail mtd = mt2.getTaxDetails().size() > 1 ? mt2.getTaxDetails().get(1) : mt2
								.getTaxDetails().get(0);

						if (isHistory(mtd)) {
							dateAndPercentage2.put(mtd.getFromDate(), mtd.getCalculatedTaxValue());
							dateAndPercentageByTax.put(mt2.getTaxName(), dateAndPercentage2);
						}
					}
				}
			}

			dateAndTotalCalcTaxByTaxForUnit.put(Integer.valueOf(currentUnitTax.getFloorNumber()),
					dateAndPercentageByTax);

			LOGGER.debug("Exiting from getCurrentUnitTaxSlabs - dateAndPercentageByTaxForUnit: "
					+ dateAndTotalCalcTaxByTaxForUnit);
		}

		// [CODE REVIEW] why is this public?
		public Map<String, Date> getSlabChangedTaxes() {
			LOGGER.debug("Entered into getSlabChangedTaxes");
			LOGGER.debug("getSlabChangedTaxes - dateAndPercentageByTaxForUnit: " + dateAndTotalCalcTaxByTaxForUnit);
			LOGGER.debug("getSlabChangedTaxes - UnitNumber : " + currentUnitTax.getFloorNumber());

			Map<String, Map<Date, BigDecimal>> taxAndListOfMapsOfDateAndPercentage = dateAndTotalCalcTaxByTaxForUnit
					.get(currentUnitTax.getFloorNumber());

			Map<String, Date> taxNames = new HashMap<String, Date>();

			if (taxAndListOfMapsOfDateAndPercentage != null && !taxAndListOfMapsOfDateAndPercentage.isEmpty()) {
				for (MiscellaneousTax tax : currentUnitTax.getMiscellaneousTaxes()) {

					Map<Date, BigDecimal> taxDateAndPercentages = taxAndListOfMapsOfDateAndPercentage.get(tax
							.getTaxName());
					Map<Date, MiscellaneousTaxDetail> taxDetailAndEffectiveDate = new TreeMap<Date, MiscellaneousTaxDetail>();

					// Getting the slab effective dates in asc order
					for (MiscellaneousTaxDetail mtd : tax.getTaxDetails()) {
						if (mtd.getIsHistory() == null || NON_HISTORY_TAX_DETAIL.equals(mtd.getIsHistory())) {
							taxDetailAndEffectiveDate.put(mtd.getFromDate(), mtd);
						}
					}

					// Getting the latest slab effective date,
					// as of now in NMC there can be only 2 slabs in a
					// installment period,
					// have considered this in order to simplify the process
					// else it will become complex
					MiscellaneousTaxDetail mtd = taxDetailAndEffectiveDate.get(taxDetailAndEffectiveDate.keySet()
							.toArray()[taxDetailAndEffectiveDate.size() - 1]);

					LOGGER.info("getSlabChangedTaxes - " + mtd);

					if (taxDateAndPercentages != null) {
						if (taxDateAndPercentages.get(mtd.getFromDate()) == null) {
							taxNames.put(tax.getTaxName(), mtd.getFromDate());
						}
					} else {
						taxNames.put(tax.getTaxName(), mtd.getFromDate());
					}
				}
			}
			LOGGER.debug("getSlabChangedTaxes - slab changed taxes : " + taxNames);
			LOGGER.debug("Exiting from getSlabChangedTaxes");
			return taxNames;
		}

		/**
		 * @param mtd
		 * @return true if the tax detail is history details else false
		 */
		private boolean isHistory(MiscellaneousTaxDetail mtd) {
			return mtd.getIsHistory() == null || mtd.getIsHistory().equals('N');
		}
	}

	public BasicProperty getBasicProperty() {
		return basicProperty;
	}

	public void setBasicProperty(BasicProperty basicProperty) {
		this.basicProperty = basicProperty;
	}
}
