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
import static org.egov.infstr.utils.EgovUtils.roundOffTwo;
import static org.egov.ptis.client.util.PropertyTaxUtil.isNotNull;
import static org.egov.ptis.client.util.PropertyTaxUtil.isNull;
import static org.egov.ptis.constants.PropertyTaxConstants.CENTRAL_GOVT_SHORTFORM;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_FIRE_SERVICE_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_GENERAL_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_GENERAL_WATER_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_LIGHTINGTAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_SEWERAGE_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.FORMAT_YEAR;
import static org.egov.ptis.constants.PropertyTaxConstants.MIXED_SHORTFORM;
import static org.egov.ptis.constants.PropertyTaxConstants.NONRESD_SHORTFORM;
import static org.egov.ptis.constants.PropertyTaxConstants.NON_HISTORY_TAX_DETAIL;
import static org.egov.ptis.constants.PropertyTaxConstants.OCCUPIER_OCC;
import static org.egov.ptis.constants.PropertyTaxConstants.OPEN_PLOT_SHORTFORM;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNER_OCC;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTYTYPE_CODE_TO_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_CAT_RESD_CUM_NON_RESD;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_CENTRAL_GOVT;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_MIXED;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_NON_RESD;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_OPENPLOT_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_OPEN_PLOT;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_RESD;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_STATE_GOVT;
import static org.egov.ptis.constants.PropertyTaxConstants.RESD_CUM_COMMERCIAL_PROP_ALV_PERCENTAGE;
import static org.egov.ptis.constants.PropertyTaxConstants.RESD_SHORTFORM;
import static org.egov.ptis.constants.PropertyTaxConstants.STATEGOVT_BUILDING_GENERALTAX_ADDITIONALDEDUCTION;
import static org.egov.ptis.constants.PropertyTaxConstants.STATE_GOVT_SHORTFORM;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_FAIL_XML_MIGRATION;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_YES_XML_MIGRATION;
import static org.egov.ptis.constants.PropertyTaxConstants.STR_MIGRATED;
import static org.egov.ptis.constants.PropertyTaxConstants.TENANT_OCC;
import static org.egov.ptis.constants.PropertyTaxConstants.VACANT_OCC;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentDao;
import org.egov.demand.model.EgDemandReasonDetails;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.commons.Module;
import org.egov.infstr.commons.dao.ModuleDao;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.ptis.client.model.AreaTaxCalculationInfo;
import org.egov.ptis.client.model.MiscellaneousTax;
import org.egov.ptis.client.model.MiscellaneousTaxDetail;
import org.egov.ptis.client.model.TaxCalculationInfo;
import org.egov.ptis.client.model.TaxDetail;
import org.egov.ptis.client.model.UnitTaxCalculationInfo;
import org.egov.ptis.client.util.PropertyTaxNumberGenerator;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.client.util.SimpleSequenceGenerator;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.UnitAreaCalculationDetail;
import org.egov.ptis.domain.entity.property.UnitCalculationDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * The class <code> UnitCalculationDetailsConverter </code> contains methods to
 * convert <code> UnitTaxCalculationInfo </code> to
 * <code> UnitCalculationDetail </code>
 *
 * <p>
 *
 * The conversion to <code> UnitCalculationDetails </code> is with respect to
 * the Notice unit details requirement. The class does not convert each
 * <code> UnitTaxCalculationInfo <code>
 * xml of every <code> Instalment </code>
 *
 * </P
 *
 *
 * @author nayeem
 *
 */

// public class UnitCalculationDetailsConverter TaxXMLToDBCoverterService
public class TaxXMLToDBCoverterService {

	private static final Logger LOGGER = Logger
			.getLogger(TaxXMLToDBCoverterService.class);
	private static final Integer OPEN_PLOT_FLOOR_NUMBER = 99;
	private static final String STR_ERROR = "ERROR";

	private String indexNumber;
	private BasicProperty basicProperty;
	private Map<Date, TaxCalculationInfo> taxCalculations = new TreeMap<Date, TaxCalculationInfo>();
	private List<UnitCalculationDetail> unitCalcDetails;
	private PropertyTaxUtil propertyTaxUtil;
	private PersistenceService<BasicProperty, Long> basicPropertyService;
	private Map<Date, String> occupancyAndPropertyType = new TreeMap<Date, String>();
	private String unitIdentifierPrefix;
	private PropertyTaxNumberGenerator ptNumberGenerator;
	private DateFormat dateFormat = new SimpleDateFormat(
			PropertyTaxConstants.DATE_FORMAT_DDMMYYY);
	@Autowired
	@Qualifier(value = "moduleDAO")
	private ModuleDao moduleDao;
	@Autowired
	private InstallmentDao installmentDao;

	private static Map<Integer, Map<String, Map<Date, BigDecimal>>> dateAndTotalCalcTaxByTaxForUnit;

	public TaxXMLToDBCoverterService() {
	}

	public TaxXMLToDBCoverterService(BasicProperty basicProperty,
			PropertyTaxUtil propertyTaxUtil,
			PersistenceService<BasicProperty, Long> basicPropertyService,
			PropertyTaxNumberGenerator ptNumberGenerator) {
		this.basicProperty = basicProperty;
		this.propertyTaxUtil = propertyTaxUtil;
		this.basicPropertyService = basicPropertyService;
		this.ptNumberGenerator = ptNumberGenerator;
	}

	// [CODE REVIEW] can this class be defined as a spring bean instead?
	public static TaxXMLToDBCoverterService createConverter(
			BasicProperty basicProperty, PropertyTaxUtil propertyTaxUtil,
			PersistenceService<BasicProperty, Long> basicPropertyService,
			PropertyTaxNumberGenerator ptNumberGenerator) {
		return new TaxXMLToDBCoverterService(basicProperty, propertyTaxUtil,
				basicPropertyService, ptNumberGenerator);
	}

	public void preparePropertyTypesByOccupancy(
			Map<Date, Property> propertyByOccupancy) {
		for (Map.Entry<Date, Property> entry : propertyByOccupancy.entrySet()) {
			occupancyAndPropertyType.put(entry.getKey(), entry.getValue()
					.getPropertyDetail().getPropertyTypeMaster().getCode());
		}
	}

	/**
	 *
	 */
	public void migrateTaxXML() {
		LOGGER.debug("Entered into migrateTaxXML basicProperty.upicNo="
				+ basicProperty.getUpicNo());

		try {
			Map<Date, Property> propertyByOccupancy = getPropertiesByOccupancy();

			preparePropertyTypesByOccupancy(propertyByOccupancy);
			prepareInstallmentWiseTaxCalcs(propertyByOccupancy);

			initCurrentUnitSlabs();

			unitIdentifierPrefix = generateUnitIdentifierPrefix();

			// here change can be ALV, guidance value, occupancy date or slab
			// change
			Set<UnitCalculationDetail> unitCalculationDetails = getTheRowsForChange(null);

			persistUnitCalcDetails(unitCalculationDetails);
		} catch (Exception e) {

			basicProperty.setIsTaxXMLMigrated(STATUS_FAIL_XML_MIGRATION);
			basicPropertyService.update(basicProperty);

			String errorMsg = "Error in Tax XML migration for "
					+ basicProperty.getUpicNo();
			errorMsg = errorMsg + e.getMessage();
			LOGGER.error(errorMsg, e);
		}
		LOGGER.debug("Exiting from migrateTaxXML");
	}

	public void initCurrentUnitSlabs() {
		dateAndTotalCalcTaxByTaxForUnit = new TreeMap<Integer, Map<String, Map<Date, BigDecimal>>>();
	}

	public Set<UnitCalculationDetail> getTheRowsForChange(Property propertyModel) {
		LOGGER.debug("Entered into getTheRowsForChange");

		Set<UnitCalculationDetail> unitCalculationDetails = new HashSet<UnitCalculationDetail>();

		Map<Integer, UnitTaxCalculationInfo> prevInstallmentUnitsByUnitNo = new TreeMap<Integer, UnitTaxCalculationInfo>();
		Map<Integer, UnitTaxCalculationInfo> nextInstallmentUnitsByUnitNo = new TreeMap<Integer, UnitTaxCalculationInfo>();

		InstallmentUnitTax instPrevCurrUnitTax = null;
		List<String> emptyList = Collections.<String> emptyList();

		Property oldProperty = null;

		oldProperty = basicProperty.getProperty();

		if (oldProperty == null) {

			oldProperty = PropertyTaxUtil.getLatestProperty(basicProperty,
					PropertyTaxConstants.STATUS_ISHISTORY);

			if (oldProperty == null) {
				oldProperty = propertyModel;
				LOGGER.debug("Using the model property, propertyModel="
						+ propertyModel);
			}
		}

		for (Map.Entry<Date, TaxCalculationInfo> taxCalcAndInstallment : taxCalculations
				.entrySet()) {
			Module module = moduleDao
					.getModuleByName(PropertyTaxConstants.PTMODULENAME);

			Installment installment = installmentDao
					.getInsatllmentByModuleForGivenDate(module,
							taxCalcAndInstallment.getKey());

			TaxCalculationInfo taxCalcInfo = taxCalcAndInstallment.getValue();

			LOGGER.debug("getTheRowsForChange - Installment =" + installment);

			prevInstallmentUnitsByUnitNo.putAll(nextInstallmentUnitsByUnitNo);

			/*
			 * taking units freshly for each installment by clearing the map
			 */
			nextInstallmentUnitsByUnitNo.clear();

			for (UnitTaxCalculationInfo unitTax : taxCalcInfo
					.getConsolidatedUnitTaxCalculationInfo()) {
				nextInstallmentUnitsByUnitNo.put(unitTax.getUnitNumber(),
						unitTax);
			}

			/*
			 * For the first installment, directly saving the details into table
			 * as there is nothing to compare against
			 */
			if (isStartingInstallment(prevInstallmentUnitsByUnitNo)) {

				for (Map.Entry<Integer, UnitTaxCalculationInfo> entry : nextInstallmentUnitsByUnitNo
						.entrySet()) {
					instPrevCurrUnitTax = InstallmentUnitTax.create(
							installment, null, entry.getValue());

					unitCalculationDetails.addAll(createUnitCalculationDetail(
							oldProperty, installment, taxCalcInfo,
							instPrevCurrUnitTax.getCurrentUnitAsList(), false,
							true));

					instPrevCurrUnitTax.getCurrentUnitTaxSlabs(emptyList);
				}

			} else {
				for (Map.Entry<Integer, UnitTaxCalculationInfo> currentUnitTaxEntry : nextInstallmentUnitsByUnitNo
						.entrySet()) {

					instPrevCurrUnitTax = InstallmentUnitTax.create(
							installment, prevInstallmentUnitsByUnitNo
									.get(currentUnitTaxEntry.getKey()),
							currentUnitTaxEntry.getValue());

					if (instPrevCurrUnitTax.isCurrentUnitNewUnit()) {

						unitCalculationDetails
								.addAll(createUnitCalculationDetail(
										oldProperty, installment, taxCalcInfo,
										instPrevCurrUnitTax
												.getCurrentUnitAsList(), false,
										false));

						instPrevCurrUnitTax.getCurrentUnitTaxSlabs(emptyList);

					} else {

						if ((instPrevCurrUnitTax.isSameALV() && instPrevCurrUnitTax
								.isSameOccupancy())
								|| instPrevCurrUnitTax.isALVNotSame()) {

							// check the occupancy date, if different occupancy
							// then create a row for this
							// check any tax slab is effective when alv is same
							// n occupancy is same

							if (instPrevCurrUnitTax.isALVNotSame()) {

								// consider here the
								// isPrevCurrALVSame = false and
								// isPrevCurrOccupancySame = false

								unitCalculationDetails
										.addAll(createUnitCalculationDetail(
												oldProperty,
												installment,
												taxCalcInfo,
												instPrevCurrUnitTax
														.getCurrentUnitAsList(),
												false, false));
								instPrevCurrUnitTax
										.getCurrentUnitTaxSlabs(emptyList);
							}

							unitCalculationDetails
									.addAll(getUnitCalDetailsForSlabChange(
											oldProperty, taxCalcInfo,
											instPrevCurrUnitTax));

						}

						if (instPrevCurrUnitTax.isSameALV()
								&& instPrevCurrUnitTax.isOccupancyNotSame()) {
							// indicates modification

							unitCalculationDetails
									.addAll(createUnitCalculationDetail(
											oldProperty, installment,
											taxCalcInfo, instPrevCurrUnitTax
													.getCurrentUnitAsList(),
											false, false));

							instPrevCurrUnitTax
									.getCurrentUnitTaxSlabs(emptyList);
						}
					}

				}

			}
		}

		LOGGER.debug("Exiting from getTheRowsForChange");
		return unitCalculationDetails;
	}

	private void setOccupancyDateAsFromDate(
			Set<UnitCalculationDetail> unitCalculationDetails) {
		LOGGER.debug("Entered into setOccupancyDateAsFromDate");

		for (UnitCalculationDetail unitCalcDetail : unitCalculationDetails) {
			unitCalcDetail.setFromDate(unitCalcDetail.getOccupancyDate());
		}

		LOGGER.debug("Exiting from setOccupancyDateAsFromDate");
	}

	public void persistUnitCalcDetails(
			Set<UnitCalculationDetail> unitCalculationDetails) {
		LOGGER.debug("Entered into persistUnitCalcDetails");

		addUnitCalculationDetails(unitCalculationDetails,
				basicProperty.getProperty());

		basicProperty.setIsTaxXMLMigrated(STATUS_YES_XML_MIGRATION);
		basicPropertyService.update(basicProperty);

		LOGGER.debug("Exiting from persistUnitCalcDetails");
	}

	/**
	 * Adds the new <code> UnitCalculationDetail </code> to model property
	 *
	 * @param unitCalculationDetails
	 */
	public Property addUnitCalculationDetails(
			Set<UnitCalculationDetail> unitCalculationDetails, Property property) {

		property.addAllUnitCalculationDetails(unitCalculationDetails);

		property.getPropertyDetail().setUnitCalculationDetails(
				property.getUnitCalculationDetails());

		return property;
	}

	/**
	 * Returns true if first installment during the calculation else false
	 *
	 * @param prevInstallmentUnitsByUnitNo
	 * @return true if first installment during the calculation
	 */
	private boolean isStartingInstallment(
			Map<Integer, UnitTaxCalculationInfo> prevInstallmentUnitsByUnitNo) {
		return prevInstallmentUnitsByUnitNo.isEmpty();
	}

	private Map<Date, Property> getPropertiesByOccupancy() {
		LOGGER.debug("Entered into getPropertiesByOccupancy");

		Map<Date, Property> propertyByCreatedDate = new TreeMap<Date, Property>();
		Map<Date, Property> propertyByOccupancyDate = new TreeMap<Date, Property>();

		for (Property property : basicProperty.getPropertySet()) {
			if (isValidForXMLMigration(property)) {
				propertyByCreatedDate.put(property.getCreatedDate().toDate(),
						property);
			}
		}

		for (Map.Entry<Date, Property> entry : propertyByCreatedDate.entrySet()) {
			propertyByOccupancyDate.put(
					propertyTaxUtil.getPropertyOccupancyDate(entry.getValue()),
					entry.getValue());
		}

		LOGGER.debug("Exiting from getPropertiesByOccupancy");

		return propertyByOccupancyDate;
	}

	/**
	 * Returns true if property can be used for XML Migration else false
	 *
	 * @param property
	 * @return
	 */
	private boolean isValidForXMLMigration(Property property) {
		return (property.getRemarks() == null || !property.getRemarks()
				.startsWith(STR_MIGRATED))
				&& !property.getStatus().equals(
						PropertyTaxConstants.STATUS_WORKFLOW);
	}

	private int getBeginIndex(Map<Date, Property> propertyByOccupancyDate) {
		LOGGER.debug("Entered into getBeginIndex");

		int beginIndex = 0;

		List<Date> occupancyDates = new ArrayList<Date>(
				propertyByOccupancyDate.keySet());

		if (occupancyDates.size() > 1) {
			Property firstProperty = propertyByOccupancyDate.get(occupancyDates
					.get(0));
			Property nextProperty = propertyByOccupancyDate.get(occupancyDates
					.get(1));

			if (firstProperty.getPropertyDetail().getPropertyMutationMaster()
					.getCode()
					.equalsIgnoreCase(PropertyTaxConstants.MUTATION_CODE_NEW)
					&& nextProperty
							.getPropertyDetail()
							.getPropertyMutationMaster()
							.getCode()
							.equalsIgnoreCase(
									PropertyTaxConstants.MUTATION_CODE_DATA_ENTRY)) {

				LOGGER.debug("Returning from getBeginIndex with value 1");
				return 1;
			}
		}

		LOGGER.debug("Exiting from getBeginIndex, beginIndex=" + beginIndex);
		return beginIndex;

	}

	private void prepareInstallmentWiseTaxCalcs(
			Map<Date, Property> propertyByOccupancyDate) {
		LOGGER.debug("Entered into prepareInstallmentWiseTaxCalcs occupancyDates="
				+ propertyByOccupancyDate.keySet());

		Property prevProperty = null;
		List<Date> occupancyDates = new ArrayList<Date>(
				propertyByOccupancyDate.keySet());

		for (int i = 0; i < propertyByOccupancyDate.size() - 1; i++) {

			prevProperty = propertyByOccupancyDate.get(occupancyDates.get(i));

			Map<Date, TaxCalculationInfo> prevPropTaxCalculations = propertyTaxUtil
					.getTaxCalInfoMap(prevProperty.getPtDemandSet(),
							occupancyDates.get(i));

			// just keep the tax calculations of prevProperty
			// that is if prevProperty.effectiveDate = 01-04-1980 then
			// from 01-04-1980 to currentInstallment.fromDate
			prevPropTaxCalculations.keySet().retainAll(
					getInstallmentStartDates(propertyTaxUtil
							.getInstallmentListByStartDate(propertyTaxUtil
									.getPropertyOccupancyDate(prevProperty)),
							occupancyDates.get(i)));

			taxCalculations.putAll(prevPropTaxCalculations);
		}

		Date activePropOccupancyDate = occupancyDates
				.get(occupancyDates.size() - 1);
		Property activeProperty = propertyByOccupancyDate
				.get(activePropOccupancyDate);

		getActivePropTaxCalc(activePropOccupancyDate, activeProperty);

		LOGGER.debug("prepareInstallmentWiseTaxCalcs - installments="
				+ taxCalculations.keySet());
		LOGGER.debug("Exiting from prepareInstallmentWiseTaxCalcs");
	}

	/**
	 * @param activePropOccupancyDate
	 * @param activeProperty
	 */
	public void getActivePropTaxCalc(Date activePropOccupancyDate,
			Property activeProperty) {

		Map<Date, TaxCalculationInfo> activePropTaxCalcs = propertyTaxUtil
				.getTaxCalInfoMap(activeProperty.getPtDemandSet(),
						activePropOccupancyDate);

		// Consider the installment tax calcs only effective for the
		// activeProperty
		activePropTaxCalcs.keySet().retainAll(
				getInstallmentStartDates(propertyTaxUtil
						.getInstallmentListByStartDate(propertyTaxUtil
								.getPropertyOccupancyDate(activeProperty)),
						activePropOccupancyDate));

		taxCalculations.putAll(activePropTaxCalcs);
	}

	private List<Date> getInstallmentStartDates(List<Installment> installments,
			Date occupancyDate) {
		LOGGER.debug("Entered into getInstallmentStartDates installments="
				+ installments + ", occupancyDate=" + occupancyDate);
		List<Date> installmentStartDates = new ArrayList<Date>();

		for (Installment installment : installments) {
			if (propertyTaxUtil.between(occupancyDate,
					installment.getFromDate(), installment.getToDate())) {
				installmentStartDates.add(occupancyDate);
			} else {
				installmentStartDates.add(installment.getFromDate());
			}
		}

		LOGGER.debug("Exiting from getInstallmentStartDates - installmentStartDates="
				+ installmentStartDates);
		return installmentStartDates;
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
	private List<UnitCalculationDetail> getUnitCalDetailsForSlabChange(
			Property property, TaxCalculationInfo taxCalcInfo,
			InstallmentUnitTax instUnitTax) {

		Map<String, Date> slabChangedTaxes = instUnitTax.getSlabChangedTaxes();

		List<UnitCalculationDetail> unitCalculationDetails = new ArrayList<UnitCalculationDetail>();

		if (slabChangedTaxes.isEmpty()) {
			LOGGER.debug("slabChangedTaxes - No tax slabs have changed");
		} else {
			LOGGER.debug("slabChangedTaxes -" + slabChangedTaxes);

			List<UnitTaxCalculationInfo> unitsForTaxChange = propertyTaxUtil
					.prepareUnitTaxesForChangedTaxes(
							instUnitTax.getInstallment(),
							instUnitTax.getPrevUnitTax(),
							instUnitTax.getCurrentUnitTax(), slabChangedTaxes,
							PropertyTaxUtil.isPropertyModified(property));

			unitCalculationDetails.addAll(createUnitCalculationDetail(property,
					instUnitTax.getInstallment(), taxCalcInfo,
					unitsForTaxChange, true, false));

			instUnitTax.getCurrentUnitTaxSlabs(new ArrayList<String>(
					slabChangedTaxes.keySet()));
		}

		return unitCalculationDetails;
	}

	private List<UnitCalculationDetail> createUnitCalculationDetail(
			Property property, Installment installment,
			TaxCalculationInfo taxCalcInfo,
			List<UnitTaxCalculationInfo> unitTaxes, Boolean isTaxSlabChange,
			Boolean isFirstInstallment) {
		LOGGER.debug("Entered into createUnitCalculationDetail");
		LOGGER.debug("createUnitCalculationDetail - property=" + property
				+ ", installment=" + installment + ", unitTaxes.size="
				+ unitTaxes.size() + ", isTaxSlabChange=" + isTaxSlabChange);

		List<UnitCalculationDetail> unitCalculationDetails = new ArrayList<UnitCalculationDetail>();
		UnitCalculationDetail unitCalculationDetail = null;
		UnitTaxCalculationInfo unit = null;
		Boolean isMultipleALVEffective = false;

		try {
			for (UnitTaxCalculationInfo unitTax : unitTaxes) {
				if (isFirstInstallment) {
					if (taxCalcInfo.getUnitTaxCalculationInfos().get(0) instanceof List) {
						for (List<UnitTaxCalculationInfo> units : taxCalcInfo
								.getUnitTaxCalculationInfos()) {

							if (units.size() > 1
									&& unitTax.getUnitNumber().equals(
											units.get(0).getUnitNumber())) {

								unitCalculationDetail = setUnitCalculationDetailProps(
										installment, taxCalcInfo,
										isTaxSlabChange, units.get(0), true);

								isMultipleALVEffective = true;
								unitCalculationDetails
										.addAll(setMiscellaneousTaxDetails(
												property,
												installment,
												unitCalculationDetail,
												taxCalcInfo,
												propertyTaxUtil
														.getUnitTaxCalculationInfoClone(unitTax)));

								break;

							}
						}
					} else {
						for (int j = 0; j < taxCalcInfo
								.getUnitTaxCalculationInfos().size(); j++) {

							unit = (UnitTaxCalculationInfo) taxCalcInfo
									.getUnitTaxCalculationInfos().get(j);

							if (unitTax.getUnitNumber().equals(
									unit.getUnitNumber())) {

								unitCalculationDetail = setUnitCalculationDetailProps(
										installment, taxCalcInfo,
										isTaxSlabChange, unit, true);

								isMultipleALVEffective = false;
								unitCalculationDetails
										.addAll(setMiscellaneousTaxDetails(
												property,
												installment,
												unitCalculationDetail,
												taxCalcInfo,
												propertyTaxUtil
														.getUnitTaxCalculationInfoClone(unitTax)));

								break;

							}
						}
					}
				}

				unitCalculationDetail = setUnitCalculationDetailProps(
						installment, taxCalcInfo, isTaxSlabChange, unitTax,
						false);

				if (isMultipleALVEffective) {
					unitCalculationDetail.setFromDate(null);
				}

				unitCalculationDetails
						.addAll(setMiscellaneousTaxDetails(
								property,
								installment,
								unitCalculationDetail,
								taxCalcInfo,
								propertyTaxUtil
										.getUnitTaxCalculationInfoClone(unitTax)));

			}
		} catch (ParseException e) {
			LOGGER.error("Error while parsing unit tax instDate", e);
			throw new EGOVRuntimeException(
					"Error while parsing unit tax instDate", e);
		}

		LOGGER.debug("createUnitCalculationDetail - unitCalculationDetails="
				+ unitCalculationDetails);
		LOGGER.debug("Exiting from createUnitCalculationDetail");

		return unitCalculationDetails;
	}

	/**
	 * @param property
	 * @param installment
	 * @param taxCalcInfo
	 * @param isTaxSlabChange
	 * @param unitCalculationDetails
	 * @param buildingCost
	 * @param unitTax
	 * @return
	 * @throws ParseException
	 */
	private UnitCalculationDetail setUnitCalculationDetailProps(
			Installment installment, TaxCalculationInfo taxCalcInfo,
			Boolean isTaxSlabChange, UnitTaxCalculationInfo unitTax,
			Boolean isFirstInstallment) throws ParseException {

		BigDecimal buildingCost = isNull(taxCalcInfo.getBuildingCost()) ? ZERO
				: taxCalcInfo.getBuildingCost();
		UnitCalculationDetail unitCalculationDetail = null;
		String propertyType = null;
		Date fromDate = null;

		unitCalculationDetail = new UnitCalculationDetail();
		unitCalculationDetail.setCreatedTimeStamp(new Date());
		unitCalculationDetail.setLastUpdatedTimeStamp(new Date());
		unitCalculationDetail.setUnitNumber(unitTax.getUnitNumber());
		unitCalculationDetail.setUnitArea(isNull(unitTax.getUnitArea()) ? ZERO
				: unitTax.getUnitArea());
		unitCalculationDetail.setOccupancyDate(unitTax.getOccpancyDate());

		unitCalculationDetail
				.setGuidanceValue(isNull(unitTax.getBaseRent()) ? ZERO
						: unitTax.getBaseRent());
		unitCalculationDetail.setGuidValEffectiveDate(isNull(unitTax
				.getBaseRentEffectiveDate()) ? unitTax.getOccpancyDate()
				: unitTax.getBaseRentEffectiveDate());

		propertyType = occupancyAndPropertyType.get(unitTax.getOccpancyDate());

		fromDate = dateFormat.parse(unitTax.getInstDate());

		if (isNull(propertyType)) {
			propertyType = occupancyAndPropertyType.get(fromDate);
			if (isNull(propertyType)) {
				propertyType = STR_ERROR;
			}
		}

		unitCalculationDetail.setUnitOccupation(buildUnitOccupation(
				propertyType, unitTax));

		unitCalculationDetail.setInstallmentFromDate(installment.getFromDate());
		unitCalculationDetail
				.setMonthlyRent(isNull(unitTax.getMonthlyRent()) ? ZERO
						: unitTax.getMonthlyRent());
		unitCalculationDetail.setMonthlyRentTenant(isNull(unitTax
				.getMonthlyRentPaidByTenant()) ? ZERO : unitTax
				.getMonthlyRentPaidByTenant());
		unitCalculationDetail.setBuildingCost(buildingCost);

		if (isTaxSlabChange || isFirstInstallment) {
			unitCalculationDetail.setFromDate(fromDate);
		}

		setAnnualLettingValues(installment, unitCalculationDetail, taxCalcInfo,
				isFirstInstallment);
		return unitCalculationDetail;
	}

	private void setAnnualLettingValues(Installment installment,
			UnitCalculationDetail unitCalculationDetail,
			TaxCalculationInfo taxCalcInfo, Boolean isFirstInstallment) {
		LOGGER.debug("Entered into setAnnualLettingValues");
		LOGGER.debug("setAnnualLettingValues - installment=" + installment
				+ ", unitCalculationDetail=" + unitCalculationDetail);

		Map<String, BigDecimal> taxNameAndALV = new TreeMap<String, BigDecimal>();
		BigDecimal alv = BigDecimal.ZERO;

		List<List<UnitTaxCalculationInfo>> unitTaxes = taxCalcInfo
				.getUnitTaxCalculationInfos();

		for (UnitTaxCalculationInfo consolidatedUnitTax : taxCalcInfo
				.getConsolidatedUnitTaxCalculationInfo()) {
			if (consolidatedUnitTax.getBigBuildingTaxALV() != null
					&& consolidatedUnitTax.getBigBuildingTaxALV().compareTo(
							BigDecimal.ZERO) == 1) {
				unitCalculationDetail.setBigBuildingTaxALV(consolidatedUnitTax
						.getBigBuildingTaxALV());
			}
		}

		for (UnitTaxCalculationInfo consolidatedUnitTax : taxCalcInfo
				.getConsolidatedUnitTaxCalculationInfo()) {
			if (unitCalculationDetail.getUnitNumber().equals(
					consolidatedUnitTax.getUnitNumber())) {
				unitCalculationDetail.setAlv(consolidatedUnitTax
						.getAnnualRentAfterDeduction());
				break;
			}
		}

		for (int k = 0; k < unitTaxes.size(); k++) {

			if (unitTaxes.get(k) instanceof List) {
				if (unitTaxes.get(k).get(0).getUnitNumber()
						.equals(unitCalculationDetail.getUnitNumber())) {
					if (unitTaxes.get(k).size() > 1 && !isFirstInstallment) {
						// here size > 1 indicates there are 2 base rents
						// effective,
						// in such cases taking the alv calculated using 2nd
						// base rent,
						// as the 1st BR is used in prvious case
						propertyTaxUtil.prepareTaxNameAndALV(taxNameAndALV,
								unitTaxes.get(k).get(1));
					} else {
						alv = alv.add(unitTaxes.get(k).get(0)
								.getAnnualRentAfterDeduction());
						propertyTaxUtil.prepareTaxNameAndALV(taxNameAndALV,
								unitTaxes.get(k).get(0));
					}
				}
			} else {
				UnitTaxCalculationInfo unitTax = (UnitTaxCalculationInfo) unitTaxes
						.get(k);
				if (unitTax.getUnitNumber().equals(
						unitCalculationDetail.getUnitNumber())) {
					propertyTaxUtil
							.prepareTaxNameAndALV(taxNameAndALV, unitTax);
				}
			}
		}

		if (alv.compareTo(BigDecimal.ZERO) > 0) {
			unitCalculationDetail.setAlv(alv);
		}

		unitCalculationDetail.setResidentialALV(isNull(taxNameAndALV
				.get(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD)) ? BigDecimal.ZERO
				: taxNameAndALV.get(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD));
		unitCalculationDetail
				.setNonResidentialALV(isNull(taxNameAndALV
						.get(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD)) ? BigDecimal.ZERO
						: taxNameAndALV
								.get(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD));
		unitCalculationDetail.setWaterTaxALV(isNull(taxNameAndALV
				.get(DEMANDRSN_CODE_GENERAL_WATER_TAX)) ? BigDecimal.ZERO
				: taxNameAndALV.get(DEMANDRSN_CODE_GENERAL_WATER_TAX));

		LOGGER.debug("setAnnualLettingValues - unitCalculationDetail="
				+ unitCalculationDetail);
		LOGGER.debug("Exiting from setAnnualLettingValues");
	}

	private List<UnitCalculationDetail> setMiscellaneousTaxDetails(
			Property property, Installment installment,
			UnitCalculationDetail unitCalculationDetail,
			TaxCalculationInfo taxCalcInfo,
			UnitTaxCalculationInfo consolidatedUnitTax) {

		LOGGER.debug("Entered into setMiscellaneousTaxDetails");
		LOGGER.debug("setMiscellaneousTaxDetails - property=" + property
				+ ", installment=" + installment + ", unitCalculationDetail="
				+ unitCalculationDetail);

		BigDecimal totalCalculatedTax = BigDecimal.ZERO;
		BigDecimal totalServiceCharge = BigDecimal.ZERO;

		Integer totalNoOfDays = PropertyTaxUtil.getNumberOfDays(
				installment.getFromDate(), installment.getToDate()).intValue();

		List<EgDemandReasonDetails> demandReasonDetails = new ArrayList<EgDemandReasonDetails>();

		String propertyType = property.getPropertyDetail()
				.getPropertyTypeMaster().getCode();
		String amenities = property.getPropertyDetail().getExtra_field4();
		String propertyTypeCategory = property.getPropertyDetail()
				.getExtra_field5();

		List<UnitCalculationDetail> unitCalculationDetails = new ArrayList<UnitCalculationDetail>();
		Map<String, TaxDetail> taxDetailAndTaxName = new HashMap<String, TaxDetail>();

		Integer noOfDaysForNewTaxSlab = 0;

		for (MiscellaneousTax miscTax : consolidatedUnitTax
				.getMiscellaneousTaxes()) {

			if (hasNonHistoryTaxDetails(miscTax.getTaxDetails())) {

				String demandReasonCode = miscTax.getTaxName();

				BigDecimal alv = BigDecimal.ZERO;
				BigDecimal taxPercentage = BigDecimal.ZERO;
				BigDecimal calculatedAnnualTax = BigDecimal.ZERO;
				BigDecimal demandRsnDtlPercResult = BigDecimal.ZERO;

				alv = getApplicableALV(unitCalculationDetail, demandReasonCode);

				if (alv.compareTo(BigDecimal.ZERO) == 0) {
					LOGGER.info("setMiscellaneousTaxDetails - installment="
							+ installment + ", demandReasonCode="
							+ demandReasonCode + ", alv = " + alv);
				}

				demandReasonDetails = propertyTaxUtil.getDemandReasonDetails(
						demandReasonCode, alv, installment);
				EgDemandReasonDetails demandReasonDetail = null;

				if (isUseFirstSlab(unitCalculationDetail.getFromDate(),
						demandReasonDetails.size())) {
					demandReasonDetail = demandReasonDetails.get(0);
				} else {
					demandReasonDetail = demandReasonDetails
							.get(demandReasonDetails.size() - 1);
				}

				if (propertyType != null
						&& propertyType.equalsIgnoreCase(PROPTYPE_STATE_GOVT)
						&& demandReasonCode
								.equalsIgnoreCase(DEMANDRSN_CODE_GENERAL_TAX)) {

					demandRsnDtlPercResult = BigDecimal.ZERO;

					if (isNotNull(demandReasonDetail)) {

						if (ZERO.equals(demandReasonDetail.getFlatAmount())) {
							Amount amount = new Amount(
									demandReasonDetail.getPercentage());
							demandRsnDtlPercResult = amount.percentOf(alv);
							amount.setValue(new BigDecimal(
									STATEGOVT_BUILDING_GENERALTAX_ADDITIONALDEDUCTION));
							calculatedAnnualTax = demandRsnDtlPercResult
									.subtract(amount
											.percentOf(demandRsnDtlPercResult));
						} else if (demandReasonDetail.getPercentage() == null) {
							calculatedAnnualTax = demandReasonDetail
									.getFlatAmount();
						} else {
							taxPercentage = demandReasonDetail.getPercentage();
						}

					}

				} else {
					if (isNotNull(demandReasonDetails)) {
						if (ZERO.equals(demandReasonDetail.getFlatAmount())) {
							taxPercentage = demandReasonDetail.getPercentage();
						} else if (demandReasonDetail.getPercentage() == null) {
							calculatedAnnualTax = demandReasonDetail
									.getFlatAmount();
						} else {
							taxPercentage = demandReasonDetail.getPercentage();
						}
					}
				}

				if (isNotNull(propertyTypeCategory)
						&& propertyTypeCategory
								.equalsIgnoreCase(PROPTYPE_CAT_RESD_CUM_NON_RESD)
						&& (demandReasonCode
								.equals(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD)
								|| (demandReasonCode
										.equals(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD)) || (demandReasonCode
									.equals(DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX)))) {

					Amount amount = new Amount(new BigDecimal(
							RESD_CUM_COMMERCIAL_PROP_ALV_PERCENTAGE));
					calculatedAnnualTax = amount.percentOf(alv);
					amount.setValue(taxPercentage);
					calculatedAnnualTax = amount.percentOf(calculatedAnnualTax);

				} else if (!taxPercentage.equals(ZERO)
						&& ZERO.equals(calculatedAnnualTax)) {
					calculatedAnnualTax = new Amount(taxPercentage)
							.percentOf(alv);
				}

				if (isNotNull(demandReasonDetail)
						&& demandReasonDetail.getFlatAmount().compareTo(ZERO) > 0) {

					// FlatAmount must be the maximum amount
					if (demandReasonDetail.getIsFlatAmntMax().equals(
							Integer.valueOf(1))
							&& (calculatedAnnualTax
									.compareTo(demandReasonDetail
											.getFlatAmount()) > 0)) {
						calculatedAnnualTax = demandReasonDetail
								.getFlatAmount();
					}

					// FlatAmount must be the minimum amount
					if (demandReasonDetail.getIsFlatAmntMax().equals(
							Integer.valueOf(0))
							&& (calculatedAnnualTax
									.compareTo(demandReasonDetail
											.getFlatAmount()) < 0)) {
						calculatedAnnualTax = demandReasonDetail
								.getFlatAmount();
					}
				}

				if (isNotNull(propertyType)
						&& propertyType.equalsIgnoreCase(PROPTYPE_CENTRAL_GOVT)) {
					totalServiceCharge = totalServiceCharge.add(propertyTaxUtil
							.calcGovtTaxOnAmenities(amenities,
									calculatedAnnualTax));
				}

				calculatedAnnualTax = calculatedAnnualTax.setScale(0,
						ROUND_HALF_UP);

				if (!ZERO.equals(calculatedAnnualTax)) {

					totalCalculatedTax = totalCalculatedTax
							.add(calculatedAnnualTax);

					if (isNotNull(demandReasonDetail)) {
						TaxDetail taxDetail = new TaxDetail();
						taxDetail.setTaxName(demandReasonCode);
						taxDetail.setCalculatedTax(calculatedAnnualTax);
						taxDetail.setFromDate(demandReasonDetail.getFromDate());
						taxDetailAndTaxName.put(demandReasonCode, taxDetail);
					}
				}
			}
		}

		setTaxDetails(unitCalculationDetail, taxDetailAndTaxName);
		unitCalculationDetail.setTaxPayable(totalCalculatedTax);
		unitCalculationDetail.setServiceCharge(totalServiceCharge.setScale(0,
				ROUND_HALF_UP));

		if (noOfDaysForNewTaxSlab > 0) {
			unitCalculationDetail.setTaxDays(noOfDaysForNewTaxSlab);
		} else {
			unitCalculationDetail.setTaxDays(totalNoOfDays);
		}

		setUnitAreaCalculationDetails(property, installment,
				unitCalculationDetail, taxCalcInfo, consolidatedUnitTax);

		unitCalculationDetails.add(unitCalculationDetail);

		LOGGER.debug("unitCalculationDetails= " + unitCalculationDetails
				+ ", Exiting from setMiscellaneousTaxDetails");
		return unitCalculationDetails;
	}

	/**
	 * Return true if the first slab value to be used else false
	 *
	 * @param instFromDate
	 *            The Installment from date
	 * @param guidValEffectiveDate
	 *            The guidance value effective date
	 * @param noOfDemandDetails
	 *            The number of slabs effective
	 * @return true if the first slab value to be used
	 */
	private boolean isUseFirstSlab(Date slabEffectiveDate, int noOfDemandDetails) {
		return isNull(slabEffectiveDate) && noOfDemandDetails > 1;
	}

	/**
	 *
	 * @param taxDetails
	 * @return true if there is a non history tax details, false if it only has
	 *         history tax detail
	 */
	private boolean hasNonHistoryTaxDetails(
			List<MiscellaneousTaxDetail> taxDetails) {

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
		return isNull(taxDetail.getIsHistory())
				|| taxDetail.getIsHistory().equals(
						PropertyTaxConstants.NON_HISTORY_TAX_DETAIL);
	}

	/**
	 * @param unitCalculationDetail
	 * @param demandReasonCode
	 * @return ALV
	 */
	private BigDecimal getApplicableALV(
			UnitCalculationDetail unitCalculationDetail, String demandReasonCode) {
		BigDecimal alv;

		if (demandReasonCode.equalsIgnoreCase(DEMANDRSN_CODE_GENERAL_WATER_TAX)) {
			alv = unitCalculationDetail.getWaterTaxALV();
		} else if (demandReasonCode
				.equalsIgnoreCase(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD)) {
			alv = unitCalculationDetail.getResidentialALV();
		} else if (demandReasonCode
				.equalsIgnoreCase(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD)
				|| demandReasonCode
						.equalsIgnoreCase(DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX)) {
			alv = unitCalculationDetail.getNonResidentialALV();
		} else if (demandReasonCode
				.equalsIgnoreCase(DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX)) {
			alv = unitCalculationDetail.getBigBuildingTaxALV();
		} else {
			alv = unitCalculationDetail.getAlv();
		}

		return alv;
	}

	/**
	 * @param propertyTypeCategory
	 * @param demandReasonCode
	 * @return
	 */
	private boolean isPropTypeCatResdCumNonResd(String propertyTypeCategory,
			String demandReasonCode) {
		return propertyTypeCategory != null
				&& propertyTypeCategory
						.equalsIgnoreCase(PROPTYPE_CAT_RESD_CUM_NON_RESD)
				&& (demandReasonCode
						.equals(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD)
						|| (demandReasonCode
								.equals(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD)) || (demandReasonCode
							.equals(DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX)));
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

	private void setTaxDetails(UnitCalculationDetail unitCalcDetail,
			Map<String, TaxDetail> taxDetailAndTaxName) {
		LOGGER.debug("Entered into setTaxDetails");
		LOGGER.debug("setTaxDetails - unitCalcDetail=" + unitCalcDetail
				+ ", taxDetailAndTaxName" + taxDetailAndTaxName);

		if (isNotNull(taxDetailAndTaxName.get(DEMANDRSN_CODE_SEWERAGE_TAX))) {
			unitCalcDetail.setSewerageTax(taxDetailAndTaxName.get(
					DEMANDRSN_CODE_SEWERAGE_TAX).getCalculatedTax());
			unitCalcDetail.setSewerageTaxFromDate(taxDetailAndTaxName.get(
					DEMANDRSN_CODE_SEWERAGE_TAX).getFromDate());
		}

		if (isNotNull(taxDetailAndTaxName.get(DEMANDRSN_CODE_GENERAL_WATER_TAX))) {
			unitCalcDetail.setWaterTax(taxDetailAndTaxName.get(
					DEMANDRSN_CODE_GENERAL_WATER_TAX).getCalculatedTax());
			unitCalcDetail.setWaterTaxFromDate(taxDetailAndTaxName.get(
					DEMANDRSN_CODE_GENERAL_WATER_TAX).getFromDate());
		}

		if (isNotNull(taxDetailAndTaxName.get(DEMANDRSN_CODE_GENERAL_TAX))) {
			unitCalcDetail.setGeneralTax(taxDetailAndTaxName.get(
					DEMANDRSN_CODE_GENERAL_TAX).getCalculatedTax());
			unitCalcDetail.setGeneralTaxFromDate(taxDetailAndTaxName.get(
					DEMANDRSN_CODE_GENERAL_TAX).getFromDate());
		}

		if (isNotNull(taxDetailAndTaxName.get(DEMANDRSN_CODE_LIGHTINGTAX))) {
			unitCalcDetail.setLightTax(taxDetailAndTaxName.get(
					DEMANDRSN_CODE_LIGHTINGTAX).getCalculatedTax());
			unitCalcDetail.setLightTaxFromDate(taxDetailAndTaxName.get(
					DEMANDRSN_CODE_LIGHTINGTAX).getFromDate());
		}

		if (isNotNull(taxDetailAndTaxName.get(DEMANDRSN_CODE_FIRE_SERVICE_TAX))) {
			unitCalcDetail.setFireTax(taxDetailAndTaxName.get(
					DEMANDRSN_CODE_FIRE_SERVICE_TAX).getCalculatedTax());
			unitCalcDetail.setLightTaxFromDate(taxDetailAndTaxName.get(
					DEMANDRSN_CODE_FIRE_SERVICE_TAX).getFromDate());
		}

		if (isNotNull(taxDetailAndTaxName
				.get(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD))) {
			unitCalcDetail.setEduCessResd(taxDetailAndTaxName.get(
					DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD).getCalculatedTax());
			unitCalcDetail.setEduCessResdFromDate(taxDetailAndTaxName.get(
					DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD).getFromDate());
		}

		if (isNotNull(taxDetailAndTaxName
				.get(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD))) {
			unitCalcDetail
					.setEduCessNonResd(taxDetailAndTaxName.get(
							DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD)
							.getCalculatedTax());
			unitCalcDetail.setEduCessNonResdFromDate(taxDetailAndTaxName.get(
					DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD).getFromDate());
			TaxDetail egcTaxDetail = taxDetailAndTaxName
					.get(DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX);
			unitCalcDetail
					.setEmpGrntCess(isNull(egcTaxDetail) ? BigDecimal.ZERO
							: egcTaxDetail.getCalculatedTax());
			unitCalcDetail.setEmpGrntCessFromDate(isNull(egcTaxDetail) ? null
					: egcTaxDetail.getFromDate());
		}

		if (isNotNull(taxDetailAndTaxName
				.get(DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX))) {
			unitCalcDetail
					.setBigBuildingTax(taxDetailAndTaxName.get(
							DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX)
							.getCalculatedTax());
			unitCalcDetail.setBigBuildingTaxFromDate(taxDetailAndTaxName.get(
					DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX).getFromDate());
		}

		LOGGER.debug("setTaxDetails - unitCalcDetail=" + unitCalcDetail
				+ ", Exiting from setTaxDetails");
	}

	public void setUnitAreaCalculationDetails(Property property,
			Installment installment,
			UnitCalculationDetail unitCalculationDetail,
			TaxCalculationInfo taxCalcInfo,
			UnitTaxCalculationInfo consolidatedUnitTax) {

		LOGGER.debug("Entered into setUnitAreaCalculationDetails");
		LOGGER.debug("setUnitAreaCalculationDetails - property=" + property
				+ ", installment=" + installment + ", unitCalculationDetail="
				+ unitCalculationDetail);

		UnitAreaCalculationDetail unitAreaCalcDetail = null;
		UnitTaxCalculationInfo unitTax = null;

		SimpleSequenceGenerator unitIdentifier = new SimpleSequenceGenerator(
				unitIdentifierPrefix, 0,
				new SimpleDateFormat(FORMAT_YEAR).format(installment
						.getFromDate()));

		for (int i = 0; i < taxCalcInfo.getUnitTaxCalculationInfos().size(); i++) {

			if (taxCalcInfo.getUnitTaxCalculationInfos().get(i) instanceof List) {
				unitTax = taxCalcInfo.getUnitTaxCalculationInfos().get(i)
						.size() == 1 ? taxCalcInfo.getUnitTaxCalculationInfos()
						.get(i).get(0) : taxCalcInfo
						.getUnitTaxCalculationInfos().get(i).get(1);
			} else {
				unitTax = (UnitTaxCalculationInfo) taxCalcInfo
						.getUnitTaxCalculationInfos().get(i);
			}

			if (unitTax.getUnitNumber().equals(
					unitCalculationDetail.getUnitNumber())) {

				unitIdentifier.setRunningNumber(i);

				if (consolidatedUnitTax.getAreaTaxCalculationInfos().isEmpty()) {
					unitAreaCalcDetail = createUnitAreaCalcDetail(unitTax,
							unitIdentifier, null);
					unitCalculationDetail
							.addUnitAreaCalculationDetail(unitAreaCalcDetail);
				} else {
					for (AreaTaxCalculationInfo areaTaxCalc : unitTax
							.getAreaTaxCalculationInfos()) {
						unitAreaCalcDetail = createUnitAreaCalcDetail(unitTax,
								unitIdentifier, areaTaxCalc);
						unitCalculationDetail
								.addUnitAreaCalculationDetail(unitAreaCalcDetail);
					}
				}
			}
		}

		LOGGER.debug("setUnitAreaCalculationDetails - unitCalculationDetail="
				+ unitCalculationDetail);
		LOGGER.debug("Exiting from setUnitAreaCalculationDetails");
	}

	/**
	 * @param unitTax
	 * @param unitIdentifier
	 * @param areaTaxCalc
	 * @return
	 */
	private UnitAreaCalculationDetail createUnitAreaCalcDetail(
			UnitTaxCalculationInfo unitTax,
			SimpleSequenceGenerator unitIdentifier,
			AreaTaxCalculationInfo areaTaxCalc) {

		LOGGER.debug("Entered into createUnitAreaCalcDetail areaTaxCalc="
				+ areaTaxCalc);

		UnitAreaCalculationDetail unitAreaCalcDetail = new UnitAreaCalculationDetail();

		if (isNotNull(areaTaxCalc)) {
			unitAreaCalcDetail.setMonthlyBaseRent(areaTaxCalc
					.getMonthlyBaseRent() == null ? ZERO
					: roundOffTwo(areaTaxCalc.getMonthlyBaseRent()));
			unitAreaCalcDetail.setMonthlyRentalValue(areaTaxCalc
					.getCalculatedTax() == null ? ZERO : areaTaxCalc
					.getCalculatedTax());
			unitAreaCalcDetail
					.setTaxableArea(areaTaxCalc.getTaxableArea() == null ? ZERO
							: roundOffTwo(areaTaxCalc.getTaxableArea()));
		}

		if (isNotNull(unitTax.getBaseRent())) {
			unitAreaCalcDetail.setBaseRentPerSqMtr(roundOffTwo(unitTax
					.getBaseRent()));
		}

		String floorNo = isNull(unitTax.getFloorNumberInteger()) ? "0"
				.equalsIgnoreCase(unitTax.getFloorNumber()) ? PROPTYPE_OPENPLOT_STR
				: unitTax.getFloorNumber()
				: unitTax.getFloorNumberInteger().toString();

		unitAreaCalcDetail.setFloorNumber(floorNo);

		unitAreaCalcDetail.setUnitOccupation(unitTax.getUnitOccupation());
		unitAreaCalcDetail
				.setUnitUsage(isNull(unitTax.getUnitUsage()) ? unitTax
						.getUsageFactorIndex() : unitTax.getUnitUsage());

		unitAreaCalcDetail.setManualALV(isNull(unitTax.getManualAlv()) ? ZERO
				: new BigDecimal(unitTax.getManualAlv()));
		unitAreaCalcDetail.setMonthlyRentPaidByTenanted(isNull(unitTax
				.getMonthlyRentPaidByTenant()) ? ZERO : unitTax
				.getMonthlyRentPaidByTenant());

		unitAreaCalcDetail.setUnitIdentifier(unitIdentifier.generateSequence());

		LOGGER.debug("createUnitAreaCalcDetail - unitAreaCalcDetail="
				+ unitAreaCalcDetail);
		LOGGER.debug("Exiting from createUnitAreaCalcDetail");
		return unitAreaCalcDetail;
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

	private String buildUnitOccupation(String propType,
			UnitTaxCalculationInfo unit) {
		LOGGER.debug("Entered into buildUnitOccupation, propType=" + propType);

		StringBuilder occupierName = new StringBuilder();

		if (PROPTYPE_OPEN_PLOT.equals(propType)) {
			if (OWNER_OCC.equals(unit.getUnitOccupation())
					|| VACANT_OCC.equals(unit.getUnitOccupation())) {
				occupierName.append(PROPERTYTYPE_CODE_TO_STR.get(propType));
			} else if (TENANT_OCC.equals(unit.getUnitOccupation())
					|| OCCUPIER_OCC.equalsIgnoreCase(unit.getUnitOccupation())) {
				occupierName.append(OPEN_PLOT_SHORTFORM + "-"
						+ unit.getUnitOccupier());
			}
		} else if (PROPTYPE_RESD.equals(propType)) {
			occupierName.append(RESD_SHORTFORM);
		} else if (PROPTYPE_NON_RESD.equals(propType)) {
			occupierName.append(NONRESD_SHORTFORM);
		} else if (PROPTYPE_STATE_GOVT.equals(propType)) {
			occupierName.append(STATE_GOVT_SHORTFORM + "-" + OWNER_OCC);
		} else if (PROPTYPE_CENTRAL_GOVT.equals(propType)) {
			occupierName.append(CENTRAL_GOVT_SHORTFORM + "-" + OWNER_OCC);
		} else if (PROPTYPE_MIXED.equals(propType)) {
			occupierName.append(MIXED_SHORTFORM);
		}

		if (!PROPTYPE_OPEN_PLOT.equals(propType)
				&& !PROPTYPE_STATE_GOVT.equals(propType)
				&& !PROPTYPE_CENTRAL_GOVT.equals(propType)) {
			if (TENANT_OCC.equals(unit.getUnitOccupation())
					|| OCCUPIER_OCC.equalsIgnoreCase(unit.getUnitOccupation())) {
				occupierName.append("-" + unit.getUnitOccupier());
			} else if (OWNER_OCC.equals(unit.getUnitOccupation())
					|| VACANT_OCC.equals(unit.getUnitOccupation())) {
				occupierName.append("-" + unit.getUnitOccupation());
			}
		}

		LOGGER.debug("occupierName=" + occupierName.toString()
				+ "\nExiting from buildUnitOccupation");

		return occupierName.toString();
	}

	private static class InstallmentUnitTax {

		private UnitTaxCalculationInfo prevUnitTax;
		private UnitTaxCalculationInfo currentUnitTax;
		private Installment installment;

		public InstallmentUnitTax() {
		}

		public InstallmentUnitTax(Installment installment,
				UnitTaxCalculationInfo prevUnitTax,
				UnitTaxCalculationInfo currentUnitTax) {
			this.installment = installment;
			this.prevUnitTax = prevUnitTax;
			this.currentUnitTax = currentUnitTax;
		}

		public static InstallmentUnitTax create(Installment installment,
				UnitTaxCalculationInfo prevUnitTax,
				UnitTaxCalculationInfo currentUnitTax) {
			return new InstallmentUnitTax(installment, prevUnitTax,
					currentUnitTax);
		}

		public boolean isCurrentUnitNewUnit() {
			return prevUnitTax == null ? currentUnitTax == null ? false : true
					: false;
		}

		public boolean isSameALV() {
			return prevUnitTax.getAnnualRentAfterDeduction().compareTo(
					currentUnitTax.getAnnualRentAfterDeduction()) == 0;
		}

		public boolean isALVNotSame() {
			return !isSameALV();
		}

		public boolean isSameOccupancy() {
			return prevUnitTax.getOccpancyDate().equals(
					currentUnitTax.getOccpancyDate());
		}

		public boolean isOccupancyNotSame() {
			return !isSameOccupancy();
		}

		public boolean isCurrentUnitSlabChanged() {
			return true;
		}

		public List<UnitTaxCalculationInfo> getCurrentUnitAsList() {
			List<UnitTaxCalculationInfo> units = new ArrayList<UnitTaxCalculationInfo>();
			units.add(currentUnitTax);
			return units;
		}

		public void getCurrentUnitTaxSlabs(List<String> taxNames) {
			LOGGER.debug("Entered into getCurrentUnitTaxSlabs");
			LOGGER.debug("getCurrentUnitTaxSlabs - dateAndPercentageByTaxForUnit: "
					+ dateAndTotalCalcTaxByTaxForUnit);
			LOGGER.debug("getCurrentUnitTaxSlabs - taxNames: " + taxNames);

			Map<String, Map<Date, BigDecimal>> dateAndPercentageByTax = (dateAndTotalCalcTaxByTaxForUnit
					.get(currentUnitTax.getUnitNumber()) == null) ? new TreeMap<String, Map<Date, BigDecimal>>()
					: dateAndTotalCalcTaxByTaxForUnit.get(currentUnitTax
							.getUnitNumber());

			if (taxNames.isEmpty()) {
				for (MiscellaneousTax mt1 : currentUnitTax
						.getMiscellaneousTaxes()) {
					Map<Date, BigDecimal> dateAndPercentage1 = new TreeMap<Date, BigDecimal>();
					for (MiscellaneousTaxDetail mtd : mt1.getTaxDetails()) {
						if (isNonHistory(mtd)) {
							dateAndPercentage1.put(mtd.getFromDate(),
									mtd.getCalculatedTaxValue());
							dateAndPercentageByTax.put(mt1.getTaxName(),
									dateAndPercentage1);
							break;
						}
					}
				}
			} else {
				for (MiscellaneousTax mt2 : currentUnitTax
						.getMiscellaneousTaxes()) {
					if (taxNames.contains(mt2.getTaxName())) {
						Map<Date, BigDecimal> dateAndPercentage2 = new TreeMap<Date, BigDecimal>();

						MiscellaneousTaxDetail mtd = mt2.getTaxDetails().size() > 1 ? mt2
								.getTaxDetails().get(1) : mt2.getTaxDetails()
								.get(0);

						if (isNonHistory(mtd)) {
							dateAndPercentage2.put(mtd.getFromDate(),
									mtd.getCalculatedTaxValue());
							dateAndPercentageByTax.put(mt2.getTaxName(),
									dateAndPercentage2);
						}
					}
				}
			}

			dateAndTotalCalcTaxByTaxForUnit.put(currentUnitTax.getUnitNumber(),
					dateAndPercentageByTax);

			LOGGER.debug("Exiting from getCurrentUnitTaxSlabs - dateAndPercentageByTaxForUnit: "
					+ dateAndTotalCalcTaxByTaxForUnit);
		}

		private Map<String, Date> getSlabChangedTaxes() {
			LOGGER.debug("Entered into getSlabChangedTaxes");
			LOGGER.debug("getSlabChangedTaxes - dateAndPercentageByTaxForUnit: "
					+ dateAndTotalCalcTaxByTaxForUnit);
			LOGGER.debug("getSlabChangedTaxes - UnitNumber : "
					+ currentUnitTax.getUnitNumber());

			Map<String, Map<Date, BigDecimal>> taxAndListOfMapsOfDateAndPercentage = dateAndTotalCalcTaxByTaxForUnit
					.get(currentUnitTax.getUnitNumber());

			Map<String, Date> taxNames = new HashMap<String, Date>();

			if (taxAndListOfMapsOfDateAndPercentage != null
					&& !taxAndListOfMapsOfDateAndPercentage.isEmpty()) {
				for (MiscellaneousTax tax : currentUnitTax
						.getMiscellaneousTaxes()) {

					Map<Date, BigDecimal> taxDateAndPercentages = taxAndListOfMapsOfDateAndPercentage
							.get(tax.getTaxName());
					Map<Date, MiscellaneousTaxDetail> taxDetailAndEffectiveDate = new TreeMap<Date, MiscellaneousTaxDetail>();

					// Getting the slab effective dates in asc order
					for (MiscellaneousTaxDetail mtd : tax.getTaxDetails()) {
						if (mtd.getIsHistory() == null
								|| NON_HISTORY_TAX_DETAIL.equals(mtd
										.getIsHistory())) {
							taxDetailAndEffectiveDate.put(mtd.getFromDate(),
									mtd);
						}
					}

					// Getting the latest slab effective date,
					// as of now in NMC there can be only 2 slabs in a
					// installment period,
					// have considered this in order to simplify the process
					// else it will become complex
					if (!taxDetailAndEffectiveDate.isEmpty()) {
						MiscellaneousTaxDetail mtd = taxDetailAndEffectiveDate
								.get(taxDetailAndEffectiveDate.keySet()
										.toArray()[taxDetailAndEffectiveDate
										.size() - 1]);

						if (taxDateAndPercentages != null) {
							if (taxDateAndPercentages.get(mtd.getFromDate()) == null) {
								taxNames.put(tax.getTaxName(),
										mtd.getFromDate());
							}
						} else {
							taxNames.put(tax.getTaxName(), mtd.getFromDate());
						}
					}
				}
			}
			LOGGER.debug("getSlabChangedTaxes - slab changed taxes : "
					+ taxNames);
			LOGGER.debug("Exiting from getSlabChangedTaxes");
			return taxNames;
		}

		/**
		 * @param mtd
		 * @return true if the tax detail is history details else false
		 */
		private boolean isNonHistory(MiscellaneousTaxDetail mtd) {
			return mtd.getIsHistory() == null || mtd.getIsHistory().equals('N');
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
	}

	@SuppressWarnings("unchecked")
	private List<Installment> getInstallmentsByStartDate(Date startDate) {

		String hqlInstallments = "select it from org.egov.commons.Installment it "
				+ "where (it.fromDate >= ? or ? between it.fromDate and it.toDate) "
				+ "and it.fromDate <= sysdate and it.module.moduleName = ? ";

		return HibernateUtil.getCurrentSession().createQuery(hqlInstallments)
				.setDate(0, startDate).setDate(1, startDate)
				.setString(2, PropertyTaxConstants.PTMODULENAME).list();

	}

	public String generateUnitIdentifierPrefix() {
		return ptNumberGenerator.generateUnitIdentifierPrefix();
	}

	public BasicProperty getBasicProperty() {
		return basicProperty;
	}

	public void setBasicProperty(BasicProperty basicProperty) {
		this.basicProperty = basicProperty;
	}

	public Map<Date, TaxCalculationInfo> getTaxCaluculations() {
		return taxCalculations;
	}

	public void setTaxCaluculations(
			Map<Date, TaxCalculationInfo> taxCaluculations) {
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

	public String getUnitIdentifierPrefix() {
		return unitIdentifierPrefix;
	}

	public void setUnitIdentifierPrefix(String unitIdentifierPrefix) {
		this.unitIdentifierPrefix = unitIdentifierPrefix;
	}
}
