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
package org.egov.ptis.bean;

import org.apache.log4j.Logger;
import org.egov.commons.Installment;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.utils.DateUtils;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyMutation;
import org.egov.ptis.domain.model.calculator.MiscellaneousTax;
import org.egov.ptis.domain.model.calculator.MiscellaneousTaxDetail;
import org.egov.ptis.domain.model.calculator.TaxCalculationInfo;
import org.egov.ptis.domain.model.calculator.UnitTaxCalculationInfo;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_EDUCATIONAL_CESS;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_GENERAL_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_LIBRARY_CESS;
import static org.egov.ptis.constants.PropertyTaxConstants.NON_HISTORY_TAX_DETAIL;
import static org.egov.ptis.constants.PropertyTaxConstants.OCC_COMMERCIAL;
import static org.egov.ptis.constants.PropertyTaxConstants.OCC_OWNER;
import static org.egov.ptis.constants.PropertyTaxConstants.OCC_TENANT;
import static org.egov.ptis.constants.PropertyTaxConstants.OPEN_PLOT_SHORTFORM;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_CENTRAL_GOVT_50;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_STATE_GOVT;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_ISHISTORY;

/**
 * The property information object
 */
public class PropertyInfo {
	private final PropertyImpl property;
	private TaxCalculationInfo taxCalInfo;
	private String noticeNo;
	private ModuleService moduleDao;
	// Total Tax payable for Current Installment
	// private BigDecimal totalTax;

	PropertyTaxUtil propertyTaxUtil = new PropertyTaxUtil();
	private static final Logger LOGGER = Logger.getLogger(PropertyInfo.class);
	private static final String PROPERTY_TYPE = "PROPERTY-TYPE";
	private static final String PROPERTY_TYPE_CATEGORY = "PROPERTY-TYPE-CATEGORY";
	private static final String PROPERTY_AMENITY = "PROPERTY-AMENITY";

	private final Set<PropertyFloorDetailsInfo> propertyFloorDetails = new LinkedHashSet<PropertyFloorDetailsInfo>();
	private DateFormat dateFormatter = new SimpleDateFormat(PropertyTaxConstants.DATE_FORMAT_DDMMYYY);
	private int isCentralGovtProp = 0;

	Map<Date, Map<Installment, TaxCalculationInfo>> installmentAndHistoryTaxCalcsByDate = new TreeMap<Date, Map<Installment, TaxCalculationInfo>>();
	Map<Date, Map<String, String>> propertyInfoByCreatedDate = new TreeMap<Date, Map<String, String>>();
	@Autowired
	private PtDemandDao ptDemandDAO;

	@PersistenceContext
	private EntityManager entityManager;

	public Set<PropertyFloorDetailsInfo> getPropertyFloorDetails() {
		return propertyFloorDetails;
	}

	public PropertyInfo(PropertyImpl property, String noticeNo) {
		this.property = property;
		this.noticeNo = noticeNo;

		Ptdemand ptDemand = ptDemandDAO.getNonHistoryCurrDmdForProperty(property);
		TaxCalculationInfo taxCalInfo = propertyTaxUtil.getTaxCalInfo(ptDemand);

		this.taxCalInfo = taxCalInfo;
		Map<String, Date> taxAndMinEffDate = getMinEffectiveDateForDmdRsns();

		/*
		 * for (UnitTaxCalculationInfo unit :
		 * taxCalInfo.getConsolidatedUnitTaxCalculationInfo()) {
		 * propertyFloorDetails.add(new PropertyFloorDetailsInfo(unit,
		 * taxCalInfo.getPropertyType(), null, taxAndMinEffDate)); }
		 */

		if (taxCalInfo.getPropertyType() != null && OWNERSHIP_TYPE_CENTRAL_GOVT_50.equals(taxCalInfo.getPropertyType())) {
			isCentralGovtProp = 1;
		}
	}

	private Date getMinEffectiveDate(String demandReasonCode) throws ParseException {
		Date taxMinEffectiveDate = null;
		Module ptModule = moduleDao.getModuleByName(PropertyTaxConstants.PTMODULENAME);
		List minEffDate = entityManager.unwrap(Session.class)
				.createSQLQuery(
						"SELECT min(drd.from_date) "
								+ "FROM EG_DEMAND_REASON_DETAILS drd, EG_DEMAND_REASON dr, EG_DEMAND_REASON_MASTER drm "
								+ "WHERE drd.ID_DEMAND_REASON=dr.ID " + "AND dr.ID_DEMAND_REASON_MASTER=drm.ID "
								+ "AND drm.code = ? " + "AND drm.module_id = ?").setString(0, demandReasonCode)
				.setLong(1, ptModule.getId()).list();
		String dateString = minEffDate.get(0).toString();
		int len = dateString.length();
		String dateInmmDDyyyy = dateString.substring(len - 2, len).concat("/")
				.concat(dateString.substring(len - 5, len - 3)).concat("/").concat(dateString.substring(0, 4));
		taxMinEffectiveDate = dateFormatter.parse(dateInmmDDyyyy);
		return taxMinEffectiveDate;
	}

	public PropertyInfo(PropertyImpl property, String noticeNo, Boolean instwiseNoticeReport) {
		this.property = property;
		this.noticeNo = noticeNo;
		Set<Ptdemand> ptDmdSet = property.getPtDemandSet();
		Map<String, Date> taxAndMinEffDate = getMinEffectiveDateForDmdRsns();
		Map<Installment, TaxCalculationInfo> taxCalInfoMap = getTaxCalInfoMap(ptDmdSet);

		Map<Installment, Map<Integer, List<UnitTaxCalculationInfo>>> unitTaxCalculations = getTaxCalInfoList(
				taxCalInfoMap, property);
		try {
			addFloorDtls(taxCalInfoMap, unitTaxCalculations, taxAndMinEffDate);
		} catch (ParseException e) {
			LOGGER.error("Error while populating Unit details");
			throw new ApplicationRuntimeException("Error while populating Unit details", e);
		}
	}

	private Map<String, Date> getMinEffectiveDateForDmdRsns() {
		LOGGER.debug("Entered into getMinEffectiveDateForDmdRsns");
		Map<String, Date> taxAndMinEffDate = new TreeMap<String, Date>();
		String errorMsg = "Error while parsing tax effective dates";

		try {
			taxAndMinEffDate.put(DEMANDRSN_CODE_GENERAL_TAX, getMinEffectiveDate(DEMANDRSN_CODE_GENERAL_TAX));
			taxAndMinEffDate.put(DEMANDRSN_CODE_EDUCATIONAL_CESS, getMinEffectiveDate(DEMANDRSN_CODE_EDUCATIONAL_CESS));
			taxAndMinEffDate.put(DEMANDRSN_CODE_LIBRARY_CESS, getMinEffectiveDate(DEMANDRSN_CODE_LIBRARY_CESS));
		} catch (ParseException pe) {
			LOGGER.error(errorMsg, pe);
			throw new ApplicationRuntimeException(errorMsg, pe);
		}

		LOGGER.info("getMinEffectiveDateForDmdRsns: " + taxAndMinEffDate);
		LOGGER.debug("Exiting from getMinEffectiveDateForDmdRsns");
		return taxAndMinEffDate;
	}

	public PropertyInfo(PropertyImpl property) {
		this.property = property;
	}

	private void addFloorDtls(Map<Installment, TaxCalculationInfo> taxCalcInfos,
			Map<Installment, Map<Integer, List<UnitTaxCalculationInfo>>> unitTaxCalculations,
			Map<String, Date> taxAndMinEffDate) throws ParseException {
		String propType = null;
		Boolean isHistoryTaxEffective = false;

		for (Map.Entry<Installment, Map<Integer, List<UnitTaxCalculationInfo>>> mapEntryByInstallment : unitTaxCalculations
				.entrySet()) {
			LOGGER.info("addFloorDtls- Installment: " + mapEntryByInstallment.getKey());

			TaxCalculationInfo taxCalc = taxCalcInfos.get(mapEntryByInstallment.getKey());
			taxCalInfo = taxCalc;
			propType = taxCalc.getPropertyType();
			// BigDecimal totalTaxPayable = BigDecimal.ZERO;

			if (taxCalInfo.getPropertyType() != null
					&& OWNERSHIP_TYPE_CENTRAL_GOVT_50.equals(taxCalInfo.getPropertyType())) {
				isCentralGovtProp = 1;
			}

			Map<Date, Map<Integer, List<UnitTaxCalculationInfo>>> unitsByDateUnitNo = orderByDate(mapEntryByInstallment
					.getValue());

			for (Map.Entry<Date, Map<Integer, List<UnitTaxCalculationInfo>>> entry : unitsByDateUnitNo.entrySet()) {
				for (Map.Entry<Integer, List<UnitTaxCalculationInfo>> mapEntryByUnitNo : entry.getValue().entrySet()) {

					for (UnitTaxCalculationInfo unit : mapEntryByUnitNo.getValue()) {

						isHistoryTaxEffective = false;

						/**
						 * PVR logic to get the Annual Taxes on the fly....!!
						 */

						UnitTaxCalculationInfo unitTaxWithAnnualTaxes = null;

						for (MiscellaneousTax mt : unit.getMiscellaneousTaxes()) {
							for (MiscellaneousTaxDetail detail : mt.getTaxDetails()) {
								if (detail.getIsHistory() != null
										&& detail.getIsHistory().equals(PropertyTaxConstants.HISTORY_TAX_DETAIL)
										&& propertyTaxUtil.between(detail.getFromDate(), mapEntryByInstallment.getKey()
												.getFromDate(), mapEntryByInstallment.getKey().getToDate())) {
								}
							}
						}
					}
				}
			}
		}
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

	private void buildUnitOccupation(String propType, UnitTaxCalculationInfo unit) {
		LOGGER.debug("Entered into buildUnitOccupation, propType=" + propType);

		StringBuilder occupierName = new StringBuilder();

		if (OWNERSHIP_TYPE_VAC_LAND.equals(propType)) {
			if (OCC_OWNER.equals(unit.getUnitOccupation()) || OCC_COMMERCIAL.equals(unit.getUnitOccupation())) {
				occupierName.append(propType);
			} else if (OCC_TENANT.equals(unit.getUnitOccupation())) {
				occupierName.append(OPEN_PLOT_SHORTFORM + "-" + unit.getUnitOccupier());
			}
		}

		if (!OWNERSHIP_TYPE_VAC_LAND_STR.equals(propType) && !OWNERSHIP_TYPE_STATE_GOVT.equals(propType)
				&& !OWNERSHIP_TYPE_CENTRAL_GOVT_50.equals(propType)) {
			if (OCC_TENANT.equals(unit.getUnitOccupation()) || OCC_OWNER.equals(unit.getUnitOccupation())) {
				occupierName.append("-" + unit.getUnitOccupier());
			} else if (OCC_OWNER.equals(unit.getUnitOccupation()) || OCC_OWNER.equals(unit.getUnitOccupation())) {
				occupierName.append("-" + unit.getUnitOccupation());
			}
		}

		unit.setUnitOccupation(occupierName.toString());

		LOGGER.debug("occupierName=" + occupierName + "\nExiting from buildUnitOccupation");
	}

	private Map<Installment, Map<Integer, List<UnitTaxCalculationInfo>>> getTaxCalInfoList(
			Map<Installment, TaxCalculationInfo> taxCalInfoMap, PropertyImpl property) {

		Map<Installment, Map<Integer, List<UnitTaxCalculationInfo>>> unitTaxCalcsOfInstallment = new TreeMap<Installment, Map<Integer, List<UnitTaxCalculationInfo>>>();
		Map<Integer, UnitTaxCalculationInfo> unitwiseAlVMap = new TreeMap<Integer, UnitTaxCalculationInfo>();

		Map<Integer, Map<String, Map<Date, BigDecimal>>> dateAndTotalCalcTaxByTaxForUnit = new TreeMap<Integer, Map<String, Map<Date, BigDecimal>>>();

		Boolean isPropertyModified = PropertyTaxUtil.isPropertyModified(property);
		LOGGER.debug("getTaxCalInfoList - isPropertyModified : " + isPropertyModified);

		int i = 0;
		Set<Integer> removalMappingKeys = new HashSet<Integer>();
		Set<Integer> oldRemovalMappingKeys = new HashSet<Integer>();
		for (Map.Entry<Installment, TaxCalculationInfo> txCalInfo : taxCalInfoMap.entrySet()) {
			Map<Integer, List<UnitTaxCalculationInfo>> unitTaxCalculations = new TreeMap<Integer, List<UnitTaxCalculationInfo>>();
			Map<Integer, UnitTaxCalculationInfo> instUnitwiseAlVMap = new TreeMap<Integer, UnitTaxCalculationInfo>();
			oldRemovalMappingKeys = new HashSet<Integer>();

			for (Integer key : removalMappingKeys) {
				unitwiseAlVMap.remove(key);
			}
			oldRemovalMappingKeys.addAll(removalMappingKeys);

			instUnitwiseAlVMap.putAll(unitwiseAlVMap);

			LOGGER.info("Installment===> " + txCalInfo.getKey());
			unitwiseAlVMap.clear();
			removalMappingKeys.clear();

			if (!instUnitwiseAlVMap.isEmpty()) {

				List<UnitTaxCalculationInfo> units = new ArrayList<UnitTaxCalculationInfo>();

				/**
				 * When the property is modified and if the unit occupancy date
				 * falls between the installment adding the unit to show in PVR
				 */
				if (isPropertyModified) {
					addHistoryUnitTax(property, txCalInfo.getKey(), null, units, unitwiseAlVMap);
					if (!units.isEmpty()) {
						prepareUnitTaxCalcsOfUnitNo(unitTaxCalculations, units);
						units.clear();
					}
				}

				for (Map.Entry<Integer, UnitTaxCalculationInfo> map : unitwiseAlVMap.entrySet()) {

					if (instUnitwiseAlVMap.get(map.getKey()) != null) {

						BigDecimal previousALV = instUnitwiseAlVMap.get(map.getKey()).getNetARV();
						BigDecimal currentALV = map.getValue().getNetARV();
						units.clear();
						List<String> taxNames = new ArrayList<String>();
						// && isUnitHasHistoryTaxDetails(map.getValue())
						// Adding directly in case of modification
						// (For modified unit, even in case of no change in unit
						// detail, like area, usage, water tax and date)
						if (isPropertyModified
								&& propertyTaxUtil.between(map.getValue().getOccpancyDate(), txCalInfo.getKey()
										.getFromDate(), txCalInfo.getKey().getToDate())) {

							map.getValue().setOccpancyDate(map.getValue().getOccpancyDate());

							units.add(propertyTaxUtil.getUnitTaxCalculationInfoClone(map.getValue()));

							if (map.getValue().getOccpancyDate().after(txCalInfo.getKey().getFromDate())
									&& map.getValue().getOccpancyDate().before(txCalInfo.getKey().getToDate())) {

								if (!(map.getValue().getTotalTaxPayable()
										.compareTo(instUnitwiseAlVMap.get(map.getKey()).getTotalTaxPayable()) == 0)) {

									LOGGER.info("Tax Payable dont match");
									List<UnitTaxCalculationInfo> singleUnit = new ArrayList<UnitTaxCalculationInfo>();
									singleUnit.add(map.getValue());

									taxNames = getSlabChangedTaxes(dateAndTotalCalcTaxByTaxForUnit, map.getValue(),
											isPropertyModified, txCalInfo.getKey());

									if (!taxNames.isEmpty()) {
										LOGGER.info("Tax Change for Installment: " + txCalInfo.getKey());
										List<UnitTaxCalculationInfo> localUnits = new ArrayList<UnitTaxCalculationInfo>();
										localUnits = prepareRsnWiseTaxCalInfoList(txCalInfo.getKey(),
												instUnitwiseAlVMap.get(map.getKey()), map.getValue(), taxNames,
												isPropertyModified);
										prepareUnitTaxCalcsOfUnitNo(unitTaxCalculations, localUnits);
									}

									taxNames.clear();
								}
							}

							/*
							 * skipping the previous installment unit and next
							 * installment unit comparison when occupancy date
							 * and base rent effective dates match example when
							 * unit occupancy and base rent effective date is
							 * 01-10-2002. Skipping only if there is base rent.
							 */
							if (map.getValue().getBaseRateEffectiveDate() != null) {
								if (!map.getValue().getOccpancyDate().equals(txCalInfo.getKey().getFromDate())
										&& map.getValue().getBaseRateEffectiveDate()
												.equals(map.getValue().getOccpancyDate())) {
									removalMappingKeys.add(map.getKey());
								}
							}

						} else if (!(currentALV.compareTo(previousALV) == 0)) {

							LOGGER.info("ALV Change for Installment: " + txCalInfo.getKey());

							if (isPropertyModified
									&& (map.getValue().getOccpancyDate().after(txCalInfo.getKey().getFromDate()) || map
											.getValue().getOccpancyDate().equals(txCalInfo.getKey().getFromDate()))) {
								map.getValue().setOccpancyDate(map.getValue().getOccpancyDate());
								if (map.getValue().getBaseRateEffectiveDate().equals(map.getValue().getOccpancyDate())) {
									removalMappingKeys.add(map.getKey());
								}
							} else {

								if (map.getValue().getBaseRateEffectiveDate() == null) {
									map.getValue().setOccpancyDate(txCalInfo.getKey().getFromDate());
								} else if (map.getValue().getBaseRateEffectiveDate()
										.before(map.getValue().getOccpancyDate())) {
									map.getValue().setOccpancyDate(txCalInfo.getKey().getFromDate());
								} else {

									map.getValue().setOccpancyDate(map.getValue().getBaseRateEffectiveDate());

									if (map.getValue().getBaseRateEffectiveDate()
											.after(txCalInfo.getKey().getFromDate())
											&& map.getValue().getBaseRateEffectiveDate()
													.before(txCalInfo.getKey().getToDate())) {
										removalMappingKeys.add(map.getKey());
									}
								}
							}
							units.add(map.getValue());

						} else if (!(map.getValue().getTotalTaxPayable()
								.compareTo(instUnitwiseAlVMap.get(map.getKey()).getTotalTaxPayable()) == 0)) {
							LOGGER.info("Tax Payable dont match / Total Tax of a Misc Tax dont match");

							taxNames = getSlabChangedTaxes(dateAndTotalCalcTaxByTaxForUnit, map.getValue(),
									isPropertyModified, txCalInfo.getKey());

							if (!taxNames.isEmpty()) {
								LOGGER.info("Tax Change for Installment: " + txCalInfo.getKey());
								units.add(map.getValue());
								List<UnitTaxCalculationInfo> localUnits = new ArrayList<UnitTaxCalculationInfo>();
								localUnits = prepareRsnWiseTaxCalInfoList(txCalInfo.getKey(),
										instUnitwiseAlVMap.get(map.getKey()), map.getValue(), taxNames,
										isPropertyModified);
								prepareUnitTaxCalcsOfUnitNo(unitTaxCalculations, localUnits);
							}
						}

						if (!units.isEmpty()) {
							prepareUnitTaxCalcsOfUnitNo(unitTaxCalculations, units);
							getTaxSlabs(dateAndTotalCalcTaxByTaxForUnit, units, taxNames, txCalInfo.getKey());
							// getTotalTaxForMiscTax(miscTaxAndTotalTaxForUnit,
							// units);
						}

					} // end if In case of New Unit
					else {
						List<UnitTaxCalculationInfo> tmpUnits = new ArrayList<UnitTaxCalculationInfo>();
						if (!oldRemovalMappingKeys.contains(map.getKey())) {
							tmpUnits.add(map.getValue());
							List<String> emptyStringList = Collections.emptyList();
							getTaxSlabs(dateAndTotalCalcTaxByTaxForUnit, tmpUnits, emptyStringList, txCalInfo.getKey());
							// getTotalTaxForMiscTax(miscTaxAndTotalTaxForUnit,
							// tmpUnits);
							prepareUnitTaxCalcsOfUnitNo(unitTaxCalculations, tmpUnits);
						}
					}
				} // end for
			} // end if map empty
			LOGGER.info("no of units addedd to " + txCalInfo.getKey() + "are " + unitTaxCalculations.size());
			unitTaxCalcsOfInstallment.put(txCalInfo.getKey(), unitTaxCalculations);
			i++;
		}
		return unitTaxCalcsOfInstallment;
	}

	/**
	 * Adds the history UnitTaxCalculationInfo if the property is modified
	 * 
	 * @param property
	 *            The active property
	 * @param installment
	 *            The installment to be which unit is being shown
	 * @param unit
	 *            The active property UnitTaxCalculationInfo
	 * @param units
	 *            The history UnitTaxCalculationInfo's
	 */
	private void addHistoryUnitTax(PropertyImpl property, Installment installment, UnitTaxCalculationInfo unit,
			List<UnitTaxCalculationInfo> units, Map<Integer, UnitTaxCalculationInfo> unitsByUnitNo) {

		LOGGER.debug("Entered into addHistoryUnitTax");
		LOGGER.debug("addHistoryUnitTax - units : " + units.size() + ", installment=" + installment);

		Map<Date, Property> historyProperties = new TreeMap<Date, Property>();

		for (Property historyProperty : property.getBasicProperty().getPropertySet()) {

			Date effectiveDate = historyProperty.getPropertyDetail().getDateOfCompletion() == null ? historyProperty
					.getEffectiveDate() : historyProperty.getPropertyDetail().getDateOfCompletion();

			if (STATUS_ISHISTORY.equals(historyProperty.getStatus())
					&& propertyTaxUtil.between(effectiveDate, installment.getFromDate(), installment.getToDate())) {
				historyProperties.put(historyProperty.getCreatedDate(), historyProperty);
			}
		}

		LOGGER.info("addHistoryUnitTax - No of histories : " + historyProperties.size());
		Map<Installment, TaxCalculationInfo> taxCalcInfoAndInstallment = null;
		Boolean isAddUnit = Boolean.FALSE;

		for (Map.Entry<Date, Property> historyPropertyByDate : historyProperties.entrySet()) {
			// LOGGER.info("  Property modification date - " +
			// historyPropertyByDate.getKey());
			taxCalcInfoAndInstallment = new TreeMap<Installment, TaxCalculationInfo>();
			Property historyProperty = historyPropertyByDate.getValue();

			for (Ptdemand ptDemand : historyProperty.getPtDemandSet()) {
				if (ptDemand.getEgInstallmentMaster().equals(installment)) {
					TaxCalculationInfo taxCalcInfo = propertyTaxUtil.getTaxCalInfo(ptDemand);

				}
			}
		}

		LOGGER.info("addHistoryUnitTax - units : " + units.size());
		LOGGER.debug("Exiting addHistoryUnitTax");
	}

	private void getPropertyInfo(Property historyProperty, Installment installment, TaxCalculationInfo taxCalcInfo,
			Date propCreatedDate, Map<Installment, TaxCalculationInfo> taxCalcInfoAndInstallment) {
		Map<String, String> propertyInfo = new HashMap<String, String>();
		propertyInfo.put(PROPERTY_TYPE, historyProperty.getPropertyDetail().getPropertyTypeMaster().getCode());
		/*propertyInfo.put(PROPERTY_TYPE_CATEGORY, historyProperty.getPropertyDetail().getExtra_field5());
		propertyInfo.put(PROPERTY_AMENITY, historyProperty.getPropertyDetail().getExtra_field4());*/
		taxCalcInfoAndInstallment.put(installment, taxCalcInfo);
		installmentAndHistoryTaxCalcsByDate.put(propCreatedDate, taxCalcInfoAndInstallment);
		propertyInfoByCreatedDate.put(propCreatedDate, propertyInfo);
	}

	@SuppressWarnings("unused")
	private boolean isNewTaxExists(UnitTaxCalculationInfo prevUnitTax, UnitTaxCalculationInfo currentUnitTax) {

		if (currentUnitTax.getMiscellaneousTaxes().size() > prevUnitTax.getMiscellaneousTaxes().size()) {
			return true;
		}

		return false;
	}

	@SuppressWarnings("unused")
	private boolean checkIsUnitHasMultpleTaxSlab(UnitTaxCalculationInfo unittaxCalInfo) {
		for (MiscellaneousTax tax : unittaxCalInfo.getMiscellaneousTaxes()) {
			if (tax.getTaxDetails().size() > 1) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unused")
	private List<UnitTaxCalculationInfo> getNewTaxDetails(UnitTaxCalculationInfo prevUnitTax,
			UnitTaxCalculationInfo currentUnitTax) {
		Set<String> prevDemandReasons = new HashSet<String>();
		Map<Date, UnitTaxCalculationInfo> unitsByNewTaxDate = new HashMap<Date, UnitTaxCalculationInfo>();

		for (MiscellaneousTax miscTax : prevUnitTax.getMiscellaneousTaxes()) {
			prevDemandReasons.add(miscTax.getTaxName());
		}

		for (MiscellaneousTax miscTax : currentUnitTax.getMiscellaneousTaxes()) {
			if (!prevDemandReasons.contains(miscTax.getTaxName())) {
				Date taxFromDate = miscTax.getTaxDetails().get(0).getFromDate();
				miscTax.setHasChanged(true);
				UnitTaxCalculationInfo unitTaxClone = propertyTaxUtil.getUnitTaxCalculationInfoClone(currentUnitTax);
				miscTax.setHasChanged(false);
				unitTaxClone.setOccpancyDate(taxFromDate);

				if (unitsByNewTaxDate.get(taxFromDate) == null) {
					unitsByNewTaxDate.put(taxFromDate, unitTaxClone);
				} else {
					UnitTaxCalculationInfo unit = unitsByNewTaxDate.get(taxFromDate);
					for (MiscellaneousTax tax : unit.getMiscellaneousTaxes()) {
						if (miscTax.getTaxName().equals(tax.getTaxName())) {
							tax.setHasChanged(true);
							break;
						}
					}
				}
			}
		}
		return new ArrayList<UnitTaxCalculationInfo>(unitsByNewTaxDate.values());
	}

	/**
	 * Gets the Tax Percentage and Effective Date for the given
	 * UnitTaxCalculationInfo
	 * 
	 * @param dateAndTotalCalcTaxByTaxForUnit
	 *            Stores the effective date-percentage for a particular tax and
	 *            for a unit
	 * @param unitTaxes
	 *            used to get the tax info like effective date & percentage
	 * @param taxNames
	 *            List of taxes which have changed
	 */
	private void getTaxSlabs(Map<Integer, Map<String, Map<Date, BigDecimal>>> dateAndTotalCalcTaxByTaxForUnit,
			List<UnitTaxCalculationInfo> unitTaxes, List<String> taxNames, Installment installment) {
		LOGGER.debug("Entered into getTaxSlabs");
		LOGGER.debug("getTaxSlabs - dateAndPercentageByTaxForUnit: " + dateAndTotalCalcTaxByTaxForUnit);
		LOGGER.debug("getTaxSlabs - taxNames: " + taxNames);

		for (UnitTaxCalculationInfo unitTax : unitTaxes) {
			Map<String, Map<Date, BigDecimal>> dateAndPercentageByTax = (dateAndTotalCalcTaxByTaxForUnit.get(unitTax
					.getFloorNumber()) == null) ? new TreeMap<String, Map<Date, BigDecimal>>()
					: dateAndTotalCalcTaxByTaxForUnit.get(unitTax.getFloorNumber());

			if (taxNames.isEmpty()) {
				for (MiscellaneousTax mt1 : unitTax.getMiscellaneousTaxes()) {
					Map<Date, BigDecimal> dateAndPercentage1 = new TreeMap<Date, BigDecimal>();
					for (MiscellaneousTaxDetail mtd : mt1.getTaxDetails()) {
						if (mtd.getIsHistory() == null || mtd.getIsHistory().equals('N')) {
							dateAndPercentage1.put(mtd.getFromDate(), mtd.getCalculatedTaxValue());
							dateAndPercentageByTax.put(mt1.getTaxName(), dateAndPercentage1);
							break;
						}
					}
				}
			} else {
				for (MiscellaneousTax mt2 : unitTax.getMiscellaneousTaxes()) {
					if (taxNames.contains(mt2.getTaxName())) {
						Map<Date, BigDecimal> dateAndPercentage2 = new TreeMap<Date, BigDecimal>();

						for (MiscellaneousTaxDetail mtd : mt2.getTaxDetails()) {
							if (mtd.getIsHistory() == null || mtd.getIsHistory().equals('N')) {
								dateAndPercentage2.put(mtd.getFromDate(), mtd.getCalculatedTaxValue());
								dateAndPercentageByTax.put(mt2.getTaxName(), dateAndPercentage2);
								break;
							}
						}
					}
				}
			}
			dateAndTotalCalcTaxByTaxForUnit.put(Integer.valueOf(unitTax.getFloorNumber()), dateAndPercentageByTax);
		}
		LOGGER.debug("Exiting from getTaxSlabs - dateAndPercentageByTaxForUnit: " + dateAndTotalCalcTaxByTaxForUnit);
	}

	private List<String> getSlabChangedTaxes(
			Map<Integer, Map<String, Map<Date, BigDecimal>>> dateAndTotalCalcTaxByTaxForUnit,
			UnitTaxCalculationInfo unitTax, Boolean isPropertyModified, Installment installment) {
		LOGGER.debug("Entered into getSlabChangedTaxes");
		LOGGER.debug("getSlabChangedTaxes - dateAndPercentageByTaxForUnit: " + dateAndTotalCalcTaxByTaxForUnit);
		LOGGER.debug("getSlabChangedTaxes - UnitNumber : " + unitTax.getFloorNumber());

		Map<String, Map<Date, BigDecimal>> taxAndListOfMapsOfDateAndPercentage = dateAndTotalCalcTaxByTaxForUnit
				.get(unitTax.getFloorNumber());
		List<String> taxNames = new ArrayList<String>();

		if (taxAndListOfMapsOfDateAndPercentage != null) {
			for (MiscellaneousTax tax : unitTax.getMiscellaneousTaxes()) {

				Map<Date, BigDecimal> taxDateAndPercentages = taxAndListOfMapsOfDateAndPercentage.get(tax.getTaxName());

				if (taxDateAndPercentages != null) {
					for (MiscellaneousTaxDetail mtd : tax.getTaxDetails()) {
						if (mtd.getIsHistory() == null || NON_HISTORY_TAX_DETAIL.equals(mtd.getIsHistory())) {
							if (taxDateAndPercentages.get(tax.getTaxDetails().get(0).getFromDate()) == null) {
								taxNames.add(tax.getTaxName());
							}
						}
					}
				}
			}
		}
		LOGGER.debug("getSlabChangedTaxes - slab changed taxes : " + taxNames);
		LOGGER.debug("Exiting from getSlabChangedTaxes");
		return taxNames;
	}

	public void prepareUnitTaxCalcsOfUnitNo(Map<Integer, List<UnitTaxCalculationInfo>> unitTaxesMap,
			List<UnitTaxCalculationInfo> units) {
		LOGGER.debug("prepareUnitTaxCalcsOfUnitNo, map keys: " + unitTaxesMap.keySet());
		LOGGER.debug("map size: " + unitTaxesMap.size());

		for (UnitTaxCalculationInfo unit : units) {
			if (unitTaxesMap.get(unit.getFloorNumber()) == null) {
				List<UnitTaxCalculationInfo> tmpList = new ArrayList<UnitTaxCalculationInfo>();
				tmpList.add(unit);
				unitTaxesMap.put(Integer.valueOf(unit.getFloorNumber()), tmpList);
			} else {
				unitTaxesMap.get(unit.getFloorNumber()).add(unit);
			}
		}

		LOGGER.debug("prepareUnitTaxCalcsOfUnitNo, map keys: " + unitTaxesMap.keySet());
		LOGGER.debug("map size: " + unitTaxesMap.size());
		LOGGER.debug("Exit from prepareUnitTaxCalcsOfUnitNo");
	}

	public String getLeastEffectiveDate(UnitTaxCalculationInfo unitTax, Installment installment) {

		Date tempDate = null;
		for (MiscellaneousTax misTax : unitTax.getMiscellaneousTaxes()) {
			Date taxEffectiveDate = getLeastEffectiveDate(misTax);
			if (tempDate == null || taxEffectiveDate.before(tempDate)) {
				tempDate = taxEffectiveDate;
			}
		}

		return DateUtils.getDefaultFormattedDate(tempDate);
	}

	private Date getLeastEffectiveDate(MiscellaneousTax misTax) {
		Date taxEffectiveDate = null;
		for (MiscellaneousTaxDetail taxDetail : misTax.getTaxDetails()) {
			if (taxEffectiveDate == null || taxDetail.getFromDate().before(taxEffectiveDate)) {
				taxEffectiveDate = taxDetail.getFromDate();
			}
		}
		return taxEffectiveDate;
	}

	@SuppressWarnings("unused")
	private Date getGreatestEffectiveDate(MiscellaneousTax misTax) {
		Date taxEffectiveDate = null;
		for (MiscellaneousTaxDetail taxDetail : misTax.getTaxDetails()) {
			if (taxEffectiveDate == null || taxDetail.getFromDate().after(taxEffectiveDate)) {
				taxEffectiveDate = taxDetail.getFromDate();
			}
		}
		return taxEffectiveDate;
	}

	/**
	 * Compares Current installment's ReasonWise taxes with previous
	 * installment's ReasonWise taxes and prepares TaxCalculationInfo list if
	 * any tax value changes
	 * 
	 * @param taxCaltInfoList
	 * @param taxCalInfoMap
	 * @param txCalInfo
	 * @param instUnitwiseAlVMap
	 * @param map
	 */
	private List<UnitTaxCalculationInfo> prepareRsnWiseTaxCalInfoList(Installment installment,
			UnitTaxCalculationInfo prevUnitTax, UnitTaxCalculationInfo currentUnitTax, List<String> changedTaxNames,
			Boolean isPropertyModified) {

		List<MiscellaneousTax> prevMiscTaxes = prevUnitTax.getMiscellaneousTaxes();
		List<MiscellaneousTax> currentMiscTaxes = currentUnitTax.getMiscellaneousTaxes();
		List<UnitTaxCalculationInfo> unitTaxCalcs = new ArrayList<UnitTaxCalculationInfo>();
		Map<Date, UnitTaxCalculationInfo> unitTaxForChangeInTaxByDate = new TreeMap<Date, UnitTaxCalculationInfo>();
		List<String> prevTaxNames = new ArrayList<String>();

		for (MiscellaneousTax mt : prevMiscTaxes) {
			prevTaxNames.add(mt.getTaxName());
		}

		Boolean isModificationBetInstallment = propertyTaxUtil.between(currentUnitTax.getOccpancyDate(),
				installment.getFromDate(), installment.getToDate()) ? true : false;

		Date taxEffectiveDate = null;
		for (MiscellaneousTax currentMiscTax : currentMiscTaxes) {
			Boolean dmdRsnExists = prevTaxNames.contains(currentMiscTax.getTaxName()) ? true : false;
			if (currentMiscTax.getTaxDetails().size() == 1) {
				taxEffectiveDate = currentMiscTax.getTaxDetails().get(0).getFromDate();
				// if (propertyTaxUtil.between(taxEffectiveDate,
				// installment.getFromDate(), installment.getToDate())) {
				if (dmdRsnExists) {
					if (changedTaxNames.contains(currentMiscTax.getTaxName())) {
						LOGGER.info(currentMiscTax.getTaxName() + " has changed, " + " for installment: " + installment
								+ ", EffecDate: " + taxEffectiveDate);

						// To indicate that the change is because of
						// modification, so showing the Occupancy Date
						if (isPropertyModified && isModificationBetInstallment
								&& currentUnitTax.getOccpancyDate().before(taxEffectiveDate)) {
							currentUnitTax.setOccpancyDate(currentUnitTax.getOccpancyDate());
						} else {
							currentMiscTax.setHasChanged(true);
							currentUnitTax.setOccpancyDate(taxEffectiveDate);
						}

						putUnitTaxInMapByDate(currentUnitTax, unitTaxForChangeInTaxByDate, taxEffectiveDate,
								currentMiscTax, isPropertyModified, isModificationBetInstallment);
						currentMiscTax.setHasChanged(false);
					}
				} else {
					// checking whehter tax effective data is effective between
					// this installment
					// else when the property is modified from tax not imposed
					// to imposed
					// simply adding will result in showing up off a row for
					// this effective date
					// Ex. GWR Not to be imposed to GWR imposed from 01-10-2002
					if (propertyTaxUtil.between(taxEffectiveDate, installment.getFromDate(), installment.getToDate())) {
						currentMiscTax.setHasChanged(true);
						currentUnitTax.setOccpancyDate(taxEffectiveDate);
						putUnitTaxInMapByDate(currentUnitTax, unitTaxForChangeInTaxByDate, taxEffectiveDate,
								currentMiscTax, isPropertyModified, isModificationBetInstallment);
						currentMiscTax.setHasChanged(false);
					}
				}
				// }
			}
		}

		unitTaxCalcs.addAll(unitTaxForChangeInTaxByDate.values());

		return unitTaxCalcs;
	}

	private void putUnitTaxInMapByDate(UnitTaxCalculationInfo currentUnitTax,
			Map<Date, UnitTaxCalculationInfo> unitTaxForChangeInTaxByDate, Date taxEffectiveDate,
			MiscellaneousTax currentMiscTax, Boolean isPropertyModified, Boolean isModificationBetInstallment) {

		Date mayKey = null;
		mayKey = currentUnitTax.getOccpancyDate();

		if (unitTaxForChangeInTaxByDate.get(mayKey) == null) {

			unitTaxForChangeInTaxByDate.put(mayKey, propertyTaxUtil.getUnitTaxCalculationInfoClone(currentUnitTax));

		} else {
			for (MiscellaneousTax mt : unitTaxForChangeInTaxByDate.get(mayKey).getMiscellaneousTaxes()) {
				if (mt.getTaxName().equals(currentMiscTax.getTaxName())) {
					mt.setHasChanged(true);
					break;
				}
			}
			LOGGER.info("multiple miscTax change for date " + mayKey
					+ unitTaxForChangeInTaxByDate.get(mayKey).getMiscellaneousTaxes());
		}
	}

	private void updateTax(String taxName, MiscellaneousTaxDetail taxDetail, List<MiscellaneousTax> miscTaxes) {
		LOGGER.info("miscTaxes: " + miscTaxes);
		for (MiscellaneousTax tax : miscTaxes) {
			if (tax.getTaxName().equals(taxName)) {
				tax.getTaxDetails().clear();
				tax.addMiscellaneousTaxDetail(taxDetail);
			}
		}
	}

	public Map<Installment, TaxCalculationInfo> getTaxCalInfoMap(Set<Ptdemand> ptDmdSet) {
		Map<Installment, TaxCalculationInfo> taxCalInfoMap = new TreeMap<Installment, TaxCalculationInfo>();
		for (Ptdemand ptdmd : ptDmdSet) {
			TaxCalculationInfo taxCalcInfo = propertyTaxUtil.getTaxCalInfo(ptdmd);
			if (taxCalcInfo != null) {
				taxCalInfoMap.put(ptdmd.getEgInstallmentMaster(), taxCalcInfo);
			}
		}
		return taxCalInfoMap;
	}

	/**
	 * The method returns the date when the property was approved in case of a
	 * transfer.
	 * 
	 * @return
	 */
	public Date getApprovalDate() {
		// Given property would have been transfered and created as a new
		// property.
		// so date of approval of transfer will be the date this property was
		// created.
		if (!property.getBasicProperty().getPropertyMutations().isEmpty())
			return property.getCreatedDate();

		return null;
	}

	/**
	 * The method returns the name of the most recent owner of the property in
	 * case the property has been transfered.
	 * 
	 * @return a <code>String</code> representing the name of the current owner
	 */
	public String getNewOwnerName() {
		if (!property.getBasicProperty().getPropertyMutations().isEmpty())
			return property.getBasicProperty().getFullOwnerName();

		return "";
	}

	/**
	 * The method returns the total number of 'Tenants' in the property.
	 * 
	 * @return an int value representing the number of tenants in the property
	 */
	public int getNoOfTenants() {
		int tenants = 0;

		if (taxCalInfo.getUnitTaxCalculationInfos().get(0) instanceof List) {
			for (UnitTaxCalculationInfo unit : taxCalInfo.getUnitTaxCalculationInfos()) {
				if (OCC_TENANT.equals(unit.getUnitOccupation()))
					tenants++;
			}
		} else {
			/**
			 * As the TaxCalculationInfo.unitTaxCalculationInfos is changed to
			 * List<List<UnitTaxCalculatioInfo>> because of the ProRating ALV,
			 * the following logic of iteration is required
			 */
			for (int i = 0; i < taxCalInfo.getUnitTaxCalculationInfos().size(); i++) {
				UnitTaxCalculationInfo unit = (UnitTaxCalculationInfo) taxCalInfo.getUnitTaxCalculationInfos().get(i);
				if (OCC_TENANT.equals(unit.getUnitOccupation()))
					tenants++;
			}
		}

		return tenants;
	}

	/**
	 * The method returns the date the property was transfered last. If the
	 * property has not been transfered, transfer date is null.
	 * 
	 * @return a <code>Date</code> instance indicating the date when the
	 *         property was transfered
	 */
	public Date getTransferDate() {
		Set<PropertyMutation> propMutSet = property.getBasicProperty().getPropertyMutations();

		if (propMutSet.isEmpty()) {
			return null;
		}

		SortedSet<Date> mutationDates = new TreeSet<Date>();
		for (PropertyMutation propertyMutation : propMutSet) {
			mutationDates.add(propertyMutation.getCreatedDate());
		}

		return mutationDates.last();
	}

	public BigDecimal getTotalTax() {
		// return totalTax;
		return taxCalInfo.getTotalTaxPayable();
	}

	public BigDecimal getTotalALV() {
		return taxCalInfo.getTotalNetARV();
	}

	public String getPropertyNo() {
		return taxCalInfo.getPropertyId();
	}

	public String getHouseNo() {
		return taxCalInfo.getHouseNumber();
	}

	private String getWardNumber() {
		return property.getBasicProperty().getBoundary().getBoundaryNum().toString();
	}

	public String getWardNo() {

		return org.apache.commons.lang.StringUtils.leftPad(getWardNumber(), 3, '0');
	}

	public String getZoneNo() {

		return org.apache.commons.lang.StringUtils.leftPad(property.getBasicProperty().getBoundary().getParent()
				.getBoundaryNum().toString(), 2, '0');
	}

	public String getWardName() {
		return this.getWardNumber() + "-" + taxCalInfo.getWard();
	}

	public String getZoneName() {
		return taxCalInfo.getZone();
	}

	public String getStreet() {
		return getOwnerAddress();
	}

	public String getOwnerName() {
		return taxCalInfo.getPropertyOwnerName();
	}

	public String getOwnerAddress() {
		return taxCalInfo.getPropertyAddress().concat(", Chennai");
	}

	public String getCompleteAddress() {
		StringBuilder completeAddr = new StringBuilder();
		completeAddr.append(getOwnerName());
		completeAddr.append("\n");
		completeAddr.append(getOwnerAddress());
		return completeAddr.toString();
	}

	/**
	 * TO DO : Should be changed after Notice-BasicProperty relationship is
	 * confirmed
	 * 
	 * @return
	 * 
	 */
	public String getNoticeNo() {

		return this.noticeNo;
	}

	public String getNoticeDate() {

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		return sdf.format(new Date());

	}

	public String getPropertyType() {
		return taxCalInfo.getPropertyType();
	}

	public int getIsCentralGovtProp() {
		return isCentralGovtProp;
	}

	public void setIsCentralGovtProp(int isCentralGovtProp) {
		this.isCentralGovtProp = isCentralGovtProp;
	}

	private void getTotalTaxForMiscTax(Map<Integer, Map<String, BigDecimal>> miscTaxAndTotalTaxForUnit,
			List<UnitTaxCalculationInfo> unitTaxes) {
		LOGGER.info("Entered into getTotalTaxForMiscTax");
		LOGGER.info("getTotalTaxForMiscTax - miscTaxAndTotalTaxForUnit: " + miscTaxAndTotalTaxForUnit);

		if (miscTaxAndTotalTaxForUnit.isEmpty()) {

			for (UnitTaxCalculationInfo unit : unitTaxes) {
				Map<String, BigDecimal> miscTaxAndTotalTax = new HashMap<String, BigDecimal>();
				for (MiscellaneousTax miscTax : unit.getMiscellaneousTaxes()) {
					miscTaxAndTotalTax.put(miscTax.getTaxName(), miscTax.getTotalCalculatedTax());
				}
				miscTaxAndTotalTaxForUnit.put(Integer.valueOf(unit.getFloorNumber()), miscTaxAndTotalTax);
			}

		} else {

			for (UnitTaxCalculationInfo unit : unitTaxes) {
				Map<String, BigDecimal> miscTaxAndTotalTax = miscTaxAndTotalTaxForUnit.get(unit.getFloorNumber()) == null ? new HashMap<String, BigDecimal>()
						: miscTaxAndTotalTaxForUnit.get(unit.getFloorNumber());
				for (MiscellaneousTax miscTax : unit.getMiscellaneousTaxes()) {
					miscTaxAndTotalTax.put(miscTax.getTaxName(), miscTax.getTotalCalculatedTax());
				}
				miscTaxAndTotalTaxForUnit.put(Integer.valueOf(unit.getFloorNumber()), miscTaxAndTotalTax);
			}

		}

		LOGGER.info("getTotalTaxForMiscTax - miscTaxAndTotalTaxForUnit: " + miscTaxAndTotalTaxForUnit);
		LOGGER.info("Exiting from getTotalTaxForMiscTax");
	}

	private Boolean hasAnyTotalTaxChangedForTaxes(Map<Integer, Map<String, BigDecimal>> miscTaxAndTotalTaxForUnit,
			UnitTaxCalculationInfo unitTax) {
		LOGGER.info("Entered into hasAnyTotalTaxChangedForTaxes");
		Map<String, BigDecimal> miscTaxAndTotalTax = miscTaxAndTotalTaxForUnit.get(unitTax.getFloorNumber());

		if (miscTaxAndTotalTax == null) {
			return true;
		} else {

			for (MiscellaneousTax miscTax : unitTax.getMiscellaneousTaxes()) {
				if (!miscTax.getTotalCalculatedTax().equals(miscTaxAndTotalTax.get(miscTax.getTaxName()))) {
					LOGGER.info("hasAnyTotalTaxChangedForTaxes - " + miscTax.getTaxName()
							+ "'s Total Tax is not matching...");
					return true;
				}
			}
		}

		return false;
	}

	private Boolean isUnitHasHistoryTaxDetails(UnitTaxCalculationInfo unitTax) {
		LOGGER.debug("Entered into isUnitHasHistoryTaxDetails");

		for (MiscellaneousTax miscTax : unitTax.getMiscellaneousTaxes()) {
			for (MiscellaneousTaxDetail miscTaxDetail : miscTax.getTaxDetails()) {
				if (miscTaxDetail.getIsHistory() == null) {
					return false;
				} else if (miscTaxDetail.getIsHistory().equals('Y') || miscTaxDetail.getIsHistory().equals('1')) {
					return true;
				}
			}
		}

		LOGGER.debug("Exiting from isUnitHasHistoryTaxDetails");

		return false;
	}

	private Map<Date, Map<Integer, List<UnitTaxCalculationInfo>>> orderByDate(
			Map<Integer, List<UnitTaxCalculationInfo>> unitsByUnitNo) throws ParseException {
		LOGGER.debug("Entered into orderByDate");

		Map<Date, Map<Integer, List<UnitTaxCalculationInfo>>> unitTaxesByDateUnitNo = new TreeMap<Date, Map<Integer, List<UnitTaxCalculationInfo>>>();

		for (Map.Entry<Integer, List<UnitTaxCalculationInfo>> mapEntry : unitsByUnitNo.entrySet()) {

			for (UnitTaxCalculationInfo unit : mapEntry.getValue()) {
				Date date = unit.getOccpancyDate();

				if (unitTaxesByDateUnitNo.get(date) == null) {
					Map<Integer, List<UnitTaxCalculationInfo>> unByUnitNo = new TreeMap<Integer, List<UnitTaxCalculationInfo>>();
					List<UnitTaxCalculationInfo> units = new ArrayList<UnitTaxCalculationInfo>();
					units.add(unit);
					unByUnitNo.put(mapEntry.getKey(), units);
					unitTaxesByDateUnitNo.put(date, unByUnitNo);
				} else {
					if (unitTaxesByDateUnitNo.get(date).get(mapEntry.getKey()) == null) {
						List<UnitTaxCalculationInfo> units = new ArrayList<UnitTaxCalculationInfo>();
						units.add(unit);
						unitTaxesByDateUnitNo.get(date).put(mapEntry.getKey(), units);
					} else {
						unitTaxesByDateUnitNo.get(date).get(mapEntry.getKey()).add(unit);
					}
				}
			}
		}
		LOGGER.debug("Entered into orderByDate");

		return unitTaxesByDateUnitNo;
	}

	/**
	 * Gives the Property Assessment Date
	 * 
	 * @return assessment date (property created date)
	 */
	public String getAssessmentDate() {
		return dateFormatter.format(this.property.getBasicProperty().getPropOccupationDate());
	}

}
