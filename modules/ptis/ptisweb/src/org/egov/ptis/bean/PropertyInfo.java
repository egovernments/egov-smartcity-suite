package org.egov.ptis.bean;

import static java.lang.Boolean.FALSE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.CENTRAL_GOVT_SHORTFORM;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_FIRE_SERVICE_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_LIGHTINGTAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.MIXED_SHORTFORM;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.NONRESD_SHORTFORM;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.OPEN_PLOT_SHORTFORM;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.OWNER_OCC;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_CENTGOVT_STR;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_MIXED_STR;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_NONRESD_STR;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_OPENPLOT_STR;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_RESD_STR;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_STATEGOVT_STR;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.RESD_SHORTFORM;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.STATE_GOVT_SHORTFORM;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.TENANT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.TENANT_OCC;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.VACANT_OCC;

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

import org.apache.log4j.Logger;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.commons.Installment;
import org.egov.infstr.commons.Module;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.infstr.utils.StringUtils;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.PropertyDAOFactory;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyMutation;
import org.egov.ptis.nmc.constants.NMCPTISConstants;
import org.egov.ptis.nmc.model.MiscellaneousTax;
import org.egov.ptis.nmc.model.MiscellaneousTaxDetail;
import org.egov.ptis.nmc.model.TaxCalculationInfo;
import org.egov.ptis.nmc.model.UnitTaxCalculationInfo;
import org.egov.ptis.nmc.util.PropertyTaxUtil;
import org.egov.ptis.nmc.util.UnitTaxInfoComparator;
import org.egov.ptis.utils.PTISCacheManager;
import org.egov.ptis.utils.PTISCacheManagerInteface;

/**
 * The property information object
 */
public class PropertyInfo {
	private final PropertyImpl property;
	private TaxCalculationInfo taxCalInfo;
	private String noticeNo;
	// Total Tax payable for Current Installment
	//private BigDecimal totalTax;

	PropertyTaxUtil propertyTaxUtil = new PropertyTaxUtil();
	private static final Logger LOGGER = Logger.getLogger(PropertyInfo.class);
	private static final Character STATUS_HISTORY_PROPERTY = 'H';
	
	private final Set<PropertyFloorDetailsInfo> propertyFloorDetails = new LinkedHashSet<PropertyFloorDetailsInfo>();
	private int isCentralGovtProp = 0;

	private PTISCacheManagerInteface ptisCacheMgr = new PTISCacheManager();

	public Set<PropertyFloorDetailsInfo> getPropertyFloorDetails() {
		return propertyFloorDetails;
	}

	public PropertyInfo(PropertyImpl property, String noticeNo) {
		this.property = property;
		this.noticeNo = noticeNo;

		PtDemandDao ptDemandDao = PropertyDAOFactory.getDAOFactory().getPtDemandDao();
		Ptdemand ptDemand = ptDemandDao.getNonHistoryCurrDmdForProperty(property);
		TaxCalculationInfo taxCalInfo = propertyTaxUtil.getTaxCalInfo(ptDemand);

		this.taxCalInfo = taxCalInfo;
		Map<String, Date> taxAndMinEffDate = getMinEffectiveDateForDmdRsns();

		for (UnitTaxCalculationInfo unit : taxCalInfo.getConsolidatedUnitTaxCalculationInfo()) {
			propertyFloorDetails.add(new PropertyFloorDetailsInfo(unit, taxCalInfo.getPropertyType(), null,
					taxAndMinEffDate));
		}
		if (taxCalInfo.getPropertyType() != null && PROPTYPE_CENTGOVT_STR.equals(taxCalInfo.getPropertyType())) {
			isCentralGovtProp = 1;
		}
	}

	private Date getMinEffectiveDate(String demandReasonCode) throws ParseException {
		Date taxMinEffectiveDate = null;
		DateFormat dateFormatter = new SimpleDateFormat(NMCPTISConstants.DATE_FORMAT_DDMMYYY);
		Module ptModule = GenericDaoFactory.getDAOFactory().getModuleDao()
				.getModuleByName(NMCPTISConstants.PTMODULENAME);
		List minEffDate = HibernateUtil
				.getCurrentSession()
				.createSQLQuery(
						"SELECT min(drd.from_date) "
								+ "FROM EG_DEMAND_REASON_DETAILS drd, EG_DEMAND_REASON dr, EG_DEMAND_REASON_MASTER drm "
								+ "WHERE drd.ID_DEMAND_REASON=dr.ID " + "AND dr.ID_DEMAND_REASON_MASTER=drm.ID "
								+ "AND drm.code = ? " + "AND drm.module_id = ?").setString(0, demandReasonCode)
				.setInteger(1, ptModule.getId()).list();
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
				
		Map<Installment, Map<Integer, List<UnitTaxCalculationInfo>>> unitTaxCalculations = getTaxCalInfoList(taxCalInfoMap, property);
		try {
			addFloorDtls(taxCalInfoMap, unitTaxCalculations, taxAndMinEffDate);
		} catch (ParseException e) {
			LOGGER.error("Error while populating Unit details");
			throw new EGOVRuntimeException("Error while populating Unit details", e);
		}
	}

	private Map<String, Date> getMinEffectiveDateForDmdRsns() {
		LOGGER.debug("Entered into getMinEffectiveDateForDmdRsns");
		Map<String, Date> taxAndMinEffDate = new TreeMap<String, Date>();
		String errorMsg = "Error while parsing tax effective dates";

		try {
			taxAndMinEffDate.put(DEMANDRSN_CODE_FIRE_SERVICE_TAX, getMinEffectiveDate(DEMANDRSN_CODE_FIRE_SERVICE_TAX));
			taxAndMinEffDate.put(DEMANDRSN_CODE_LIGHTINGTAX, getMinEffectiveDate(DEMANDRSN_CODE_LIGHTINGTAX));
			taxAndMinEffDate.put(DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX,
					getMinEffectiveDate(DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX));
		} catch (ParseException pe) {
			LOGGER.error(errorMsg, pe);
			throw new EGOVRuntimeException(errorMsg, pe);
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
		
		for (Map.Entry<Installment, Map<Integer, List<UnitTaxCalculationInfo>>> mapEntryByInstallment : unitTaxCalculations
				.entrySet()) {
			LOGGER.info("addFloorDtls- Installment: " + mapEntryByInstallment.getKey());

			TaxCalculationInfo taxCalc = taxCalcInfos.get(mapEntryByInstallment.getKey());
			taxCalInfo = taxCalc;
			propType = taxCalc.getPropertyType();
			//BigDecimal totalTaxPayable = BigDecimal.ZERO;
			
			if (taxCalInfo.getPropertyType() != null && PROPTYPE_CENTGOVT_STR.equals(taxCalInfo.getPropertyType())) {
				isCentralGovtProp = 1;
			}

			Map<Date, Map<Integer, List<UnitTaxCalculationInfo>>> unitsByDateUnitNo = orderByDate(mapEntryByInstallment.getValue());
			
			for (Map.Entry<Date, Map<Integer, List<UnitTaxCalculationInfo>>> entry : unitsByDateUnitNo.entrySet()) {
				for (Map.Entry<Integer, List<UnitTaxCalculationInfo>> mapEntryByUnitNo : entry.getValue().entrySet()) {

					List<UnitTaxCalculationInfo> unitTaxes = mapEntryByUnitNo.getValue();

					for (UnitTaxCalculationInfo unit : unitTaxes) {
						StringBuilder occupierName = new StringBuilder();
						/**
						 * This change is required to change the way
						 * unitoccupation is been displayed in the 2nd column of
						 * notices and prativrutta. In case of Open Plot if the
						 * occupancy is owner then its "Open Plot". In case of
						 * Open Plot if the occupancy is tenant then its
						 * "OP-Name of Occupier". In case of State govt and
						 * Central govt property's the format is "Prefix-Owner"
						 * (ex. SGovt-Owner) In case of other property types if
						 * the occupancy is owner or vacant then its
						 * "Prefix-Occupancy" (ex. R-Owner/R-Vacant). In case of
						 * other property types if the occupancy is tenant then
						 * its "Prefix-Name of Occupier" (ex. R-Suma).
						 */

						if (PROPTYPE_OPENPLOT_STR.equals(propType)) {
							if (OWNER_OCC.equals(unit.getUnitOccupation())
									|| VACANT_OCC.equals(unit.getUnitOccupation())) {
								occupierName.append(propType);
							} else if (TENANT_OCC.equals(unit.getUnitOccupation())) {
								occupierName.append(OPEN_PLOT_SHORTFORM + "-" + unit.getUnitOccupier());
							}
						}
						if (PROPTYPE_RESD_STR.equals(propType)) {
							occupierName.append(RESD_SHORTFORM);
						}
						if (PROPTYPE_NONRESD_STR.equals(propType)) {
							occupierName.append(NONRESD_SHORTFORM);
						}
						if (PROPTYPE_STATEGOVT_STR.equals(propType)) {
							occupierName.append(STATE_GOVT_SHORTFORM + "-" + OWNER_OCC);
						}
						if (PROPTYPE_CENTGOVT_STR.equals(propType)) {
							occupierName.append(CENTRAL_GOVT_SHORTFORM + "-" + OWNER_OCC);
						}
						if (PROPTYPE_MIXED_STR.equals(propType)) {
							occupierName.append(MIXED_SHORTFORM);
						}
						if (!PROPTYPE_OPENPLOT_STR.equals(propType) && !PROPTYPE_STATEGOVT_STR.equals(propType)
								&& !PROPTYPE_CENTGOVT_STR.equals(propType)) {
							if (TENANT_OCC.equals(unit.getUnitOccupation())) {
								occupierName.append("-" + unit.getUnitOccupier());
							} else if (OWNER_OCC.equals(unit.getUnitOccupation())
									|| VACANT_OCC.equals(unit.getUnitOccupation())) {
								occupierName.append("-" + unit.getUnitOccupation());
							}
						}

						unit.setUnitOccupation(occupierName.toString());

						/**
						 * PVR logic to get the Annual Taxes on the fly....!!
						 */

						UnitTaxCalculationInfo unitTaxWithAnnualTaxes = propertyTaxUtil.calculateAnnualTaxes(property
								.getPropertyDetail().getPropertyTypeMaster().getCode(), this.property
								.getPropertyDetail().getExtra_field5(), unit, mapEntryByInstallment.getKey(),
								this.property.getPropertyDetail().getExtra_field4(), taxCalcInfos);

						// totalTaxPayable =
						// totalTaxPayable.add(unitTaxWithAnnualTaxes.getTotalTaxPayable());

						propertyFloorDetails.add(new PropertyFloorDetailsInfo(unitTaxWithAnnualTaxes, propType,
								mapEntryByInstallment.getKey(), taxAndMinEffDate));
					}
				}
			}
			
			/*if (!mapEntryByInstallment.getValue().isEmpty()) {
				LOGGER.info("addFloorDetails- Installment: " + mapEntryByInstallment.getKey() + ", totalTax: " + totalTaxPayable);
				setTotalTax(totalTaxPayable);
			}*/
		}
	}

	
	private Map<Installment, Map<Integer, List<UnitTaxCalculationInfo>>> getTaxCalInfoList(
			Map<Installment, TaxCalculationInfo> taxCalInfoMap, PropertyImpl property) {

		Map<Installment, Map<Integer, List<UnitTaxCalculationInfo>>> unitTaxCalcsOfInstallment = new TreeMap<Installment, Map<Integer, List<UnitTaxCalculationInfo>>>();
		Map<Integer, UnitTaxCalculationInfo> unitwiseAlVMap = new TreeMap<Integer, UnitTaxCalculationInfo>();
		Map<Integer, Map<String, Map<Date, BigDecimal>>> dateAndTotalCalcTaxByTaxForUnit = new TreeMap<Integer, Map<String, Map<Date, BigDecimal>>>();
		Map<Integer, Map<String, BigDecimal>> miscTaxAndTotalTaxForUnit = new HashMap<Integer, Map<String,BigDecimal>>();
		Boolean isPropertyModified = PropertyTaxUtil.isPropertyModified(property);
		LOGGER.debug("getTaxCalInfoList - isPropertyModified : " + isPropertyModified);
		
		for (Map.Entry<Installment, TaxCalculationInfo> txCalInfo : taxCalInfoMap.entrySet()) {
			Map<Integer, List<UnitTaxCalculationInfo>> unitTaxCalculations = new TreeMap<Integer, List<UnitTaxCalculationInfo>>();
			Map<Integer, UnitTaxCalculationInfo> instUnitwiseAlVMap = new TreeMap<Integer, UnitTaxCalculationInfo>();
			instUnitwiseAlVMap.putAll(unitwiseAlVMap);
			Collections.sort(taxCalInfoMap.get(txCalInfo.getKey()).getConsolidatedUnitTaxCalculationInfo(),
					UnitTaxInfoComparator.getUnitComparator(UnitTaxInfoComparator.UNIT_SORT));

			LOGGER.info("Installment===> " + txCalInfo.getKey());

			for (UnitTaxCalculationInfo unittaxCalInfo : taxCalInfoMap.get(txCalInfo.getKey())
					.getConsolidatedUnitTaxCalculationInfo()) {
				unittaxCalInfo.setHasALVChanged(Boolean.FALSE);
				unitwiseAlVMap.put(unittaxCalInfo.getUnitNumber(), unittaxCalInfo);
			}

			if (instUnitwiseAlVMap != null && !instUnitwiseAlVMap.isEmpty()) {
				
				for (Map.Entry<Integer, UnitTaxCalculationInfo> map : unitwiseAlVMap.entrySet()) {

					
					if (instUnitwiseAlVMap.get(map.getKey()) != null) {

						List<UnitTaxCalculationInfo> units = new ArrayList<UnitTaxCalculationInfo>();
						BigDecimal previousALV = instUnitwiseAlVMap.get(map.getKey()).getAnnualRentAfterDeduction();
						BigDecimal currentALV = map.getValue().getAnnualRentAfterDeduction();

						//&& isUnitHasHistoryTaxDetails(map.getValue())
						if (isPropertyModified) {
							addHistoryUnitTax(property, txCalInfo.getKey(), map.getValue(), units);
						} 
						
						// Adding directly in case of modification 
						//(For modified unit, even in case of no change in unit detail, like area, usage, water tax and date)
						if (isPropertyModified
								&& propertyTaxUtil.between(map.getValue().getOccpancyDate(), txCalInfo.getKey()
										.getFromDate(),
								txCalInfo.getKey().getToDate())) {
							units.add(map.getValue());
						} else if (!(currentALV.compareTo(previousALV) == 0)) {

							LOGGER.info("ALV Change for Installment: " + txCalInfo.getKey());

							if (isPropertyModified
									&& (map.getValue().getOccpancyDate().after(txCalInfo.getKey().getFromDate()) || map
											.getValue().getOccpancyDate().equals(txCalInfo.getKey().getFromDate()))) {
								map.getValue().setInstDate(
										DateUtils.getDefaultFormattedDate(map.getValue().getOccpancyDate()));
							} else {

								if (map.getValue().getBaseRentEffectiveDate() == null) {
									map.getValue().setInstDate(
											DateUtils.getDefaultFormattedDate(txCalInfo.getKey().getFromDate()));
								} else if (map.getValue().getBaseRentEffectiveDate()
										.before(map.getValue().getOccpancyDate())) {
									map.getValue().setInstDate(
											DateUtils.getDefaultFormattedDate(txCalInfo.getKey().getFromDate()));
								} else {
									map.getValue()
											.setInstDate(
													DateUtils.getDefaultFormattedDate(map.getValue()
															.getBaseRentEffectiveDate()));
								}
							}

							map.getValue().setHasALVChanged(Boolean.TRUE);
							units.add(map.getValue());

						} else if (!(map.getValue().getTotalTaxPayable()
								.compareTo(instUnitwiseAlVMap.get(map.getKey()).getTotalTaxPayable()) == 0)
								|| hasAnyTotalTaxChangedForTaxes(miscTaxAndTotalTaxForUnit, map.getValue())) {
							LOGGER.info("Tax Payable dont match / Total Tax of a Misc Tax dont match");
							List<UnitTaxCalculationInfo> singleUnit = new ArrayList<UnitTaxCalculationInfo>();
							singleUnit.add(map.getValue());

							List<String> taxNames = getSlabChangedTaxes(dateAndTotalCalcTaxByTaxForUnit,
									map.getValue(), isPropertyModified);
							if (!taxNames.isEmpty()) {
								LOGGER.info("Tax Change for Installment: " + txCalInfo.getKey());
								map.getValue().setHasALVChanged(Boolean.FALSE);
								units = prepareRsnWiseTaxCalInfoList(txCalInfo.getKey(),
										instUnitwiseAlVMap.get(map.getKey()), map.getValue(), taxNames,
										isPropertyModified);
							}
						}
						
						if (!units.isEmpty()) {
							if (unitTaxCalculations.get(map.getKey()) == null) {
								unitTaxCalculations.put(map.getKey(), units);
							} else {
								unitTaxCalculations.get(map.getKey()).addAll(units);
							}

							List<String> emptyStringList = Collections.emptyList();
							getTaxSlabs(dateAndTotalCalcTaxByTaxForUnit, units, emptyStringList);
							getTotalTaxForMiscTax(miscTaxAndTotalTaxForUnit, units);
						}
						
					} // end if In case of New Unit
					else {
						List<UnitTaxCalculationInfo> tmpUnits = new ArrayList<UnitTaxCalculationInfo>();
						tmpUnits.add(map.getValue());
						List<String> emptyStringList = Collections.emptyList();
						getTaxSlabs(dateAndTotalCalcTaxByTaxForUnit, tmpUnits, emptyStringList);
						getTotalTaxForMiscTax(miscTaxAndTotalTaxForUnit, tmpUnits);
						prepareUnitTaxCalcsOfUnitNo(unitTaxCalculations, tmpUnits);
					}
				} // end for
			} // end if map empty
			else {

				List<UnitTaxCalculationInfo> unitTaxes = taxCalInfoMap.get(txCalInfo.getKey())
						.getConsolidatedUnitTaxCalculationInfo();
				List<String> emptyStringList = Collections.emptyList();
				getTaxSlabs(dateAndTotalCalcTaxByTaxForUnit, unitTaxes, emptyStringList);
				getTotalTaxForMiscTax(miscTaxAndTotalTaxForUnit, unitTaxes);
				
				List<UnitTaxCalculationInfo> unitList = new ArrayList<UnitTaxCalculationInfo>();
				
				if (isPropertyModified) {
					for (UnitTaxCalculationInfo unit : unitTaxes) {
						addHistoryUnitTax(property, txCalInfo.getKey(), unit, unitList);
					}
				}
				
				if (!unitList.isEmpty()) {
					unitTaxes.addAll(0, unitList);
				}
				
				prepareUnitTaxCalcsOfUnitNo(unitTaxCalculations, unitTaxes);
			}

			unitTaxCalcsOfInstallment.put(txCalInfo.getKey(), unitTaxCalculations);
		}
		return unitTaxCalcsOfInstallment;
	}

	/**
	 * Adds the history UnitTaxCalculationInfo if the property is modified
	 * 
	 * @param property
	 * @param installment
	 * @param unit
	 * @param units
	 */
	private void addHistoryUnitTax(PropertyImpl property, Installment installment,
			UnitTaxCalculationInfo unit, List<UnitTaxCalculationInfo> units) {
		
		LOGGER.debug("Entered into addHistoryUnitTax"); 
		LOGGER.debug("addHistoryUnitTax - units : " + units.size());
		
		Map<Date, Property> historyProperties = new TreeMap<Date, Property>();
		
		for (Property historyProperty : property.getBasicProperty().getPropertySet()) {
			if (STATUS_HISTORY_PROPERTY.equals(historyProperty.getStatus())) {
				historyProperties.put(historyProperty.getCreatedDate(), historyProperty);
			}
		}
		
		LOGGER.info("addHistoryUnitTax - No of histories : " + historyProperties.size()); 
		
		for (Map.Entry<Date, Property> historyPropertyByDate : historyProperties.entrySet()) {
			//LOGGER.info("  Property modification date - " + historyPropertyByDate.getKey());
			
			Property historyProperty = historyPropertyByDate.getValue();
			
			for (Ptdemand ptDemand : historyProperty.getPtDemandSet()) {
				if (ptDemand.getEgInstallmentMaster().equals(installment)) {
					TaxCalculationInfo taxCalcInfo = propertyTaxUtil.getTaxCalInfo(ptDemand);
					
					if (taxCalcInfo != null) {
						for (UnitTaxCalculationInfo unitTaxInfo : taxCalcInfo.getConsolidatedUnitTaxCalculationInfo()) {
							if (!unitTaxInfo.getOccpancyDate().equals(unit.getOccpancyDate())) {
								if (PropertyTaxUtil.getIsUnitsMatch(unit, taxCalcInfo.getPropertyType(), true,
										unitTaxInfo)
										&& propertyTaxUtil.between(unitTaxInfo.getOccpancyDate(),
												installment.getFromDate(), installment.getToDate())) {
									unitTaxInfo.setInstDate(DateUtils.getDefaultFormattedDate(unitTaxInfo
											.getOccpancyDate()));
									// LOGGER.info("  Adding history Unit....");
									units.add(unitTaxInfo);
									break;
								}
							}
						}
						break;
					}
				}
			}
		}
		
		LOGGER.debug("addHistoryUnitTax - units : " + units.size());
		LOGGER.debug("Exiting addHistoryUnitTax"); 
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
				unitTaxClone.setInstDate(DateUtils.getDefaultFormattedDate(taxFromDate));

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
	 * Gets the Tax Percentage and Effective Date for the given UnitTaxCalculationInfo
	 * 
	 * @param dateAndTotalCalcTaxByTaxForUnit Stores the effective date-percentage for a particular tax and for a unit
	 * @param unitTaxes used to get the tax info like effective date & percentage
	 * @param taxNames List of taxes which have changed
	 */
	private void getTaxSlabs(Map<Integer, Map<String, Map<Date, BigDecimal>>> dateAndTotalCalcTaxByTaxForUnit, List<UnitTaxCalculationInfo> unitTaxes, List<String> taxNames) {
		LOGGER.info("Entered into getTaxSlabs");
		LOGGER.info("getTaxSlabs - dateAndPercentageByTaxForUnit: " + dateAndTotalCalcTaxByTaxForUnit);
		LOGGER.info("getTaxSlabs - taxNames: " + taxNames);
		for (UnitTaxCalculationInfo unitTax : unitTaxes) {
			Map<String, Map<Date, BigDecimal>> dateAndPercentageByTax = (taxNames.isEmpty()) ? new TreeMap<String, Map<Date, BigDecimal>>()
					: dateAndTotalCalcTaxByTaxForUnit.get(unitTax.getUnitNumber());
			for (MiscellaneousTax mt : unitTax.getMiscellaneousTaxes()) {
				Map<Date, BigDecimal> dateAndPercentage = new TreeMap<Date, BigDecimal>();
				
				if (taxNames.isEmpty()) {
					MiscellaneousTaxDetail mtd = ((mt.getTaxDetails().size() > 1) && (mt.getTaxDetails().get(0)
							.getIsHistory() != null && mt.getTaxDetails().get(0).getIsHistory().equals('Y'))) ? mt
							.getTaxDetails().get(1) : mt.getTaxDetails().get(0);
					dateAndPercentage.put(mtd.getFromDate(), mtd.getCalculatedTaxValue());
					dateAndPercentageByTax.put(mt.getTaxName(), dateAndPercentage);
				} else {
					for (String tax : taxNames) {
						if (mt.getTaxName().equalsIgnoreCase(tax)) {
							MiscellaneousTaxDetail mtd = ((mt.getTaxDetails().size() > 1) && (mt.getTaxDetails().get(0)
									.getIsHistory() != null && mt.getTaxDetails().get(0).getIsHistory().equals('Y'))) ? mt
									.getTaxDetails().get(1) : mt.getTaxDetails().get(0);
							dateAndPercentage.put(mtd.getFromDate(), mtd.getCalculatedTaxValue());
							dateAndPercentageByTax.put(mt.getTaxName(), dateAndPercentage);
						}
					}
				}
			}
			dateAndTotalCalcTaxByTaxForUnit.put(unitTax.getUnitNumber(), dateAndPercentageByTax);
		}
		LOGGER.info("Exiting from getTaxSlabs - dateAndPercentageByTaxForUnit: " + dateAndTotalCalcTaxByTaxForUnit);
	}

	private List<String> getSlabChangedTaxes(
			Map<Integer, Map<String, Map<Date, BigDecimal>>> dateAndTotalCalcTaxByTaxForUnit,
			UnitTaxCalculationInfo unitTax, Boolean isPropertyModified) {
		LOGGER.info("Entered into getSlabChangedTaxes");
		LOGGER.info("getSlabChangedTaxes - dateAndPercentageByTaxForUnit: " + dateAndTotalCalcTaxByTaxForUnit);
		LOGGER.info("getSlabChangedTaxes - UnitNumber : " + unitTax.getUnitNumber());
		
		Map<String, Map<Date, BigDecimal>> taxAndListOfMapsOfDateAndPercentage = dateAndTotalCalcTaxByTaxForUnit
				.get(unitTax.getUnitNumber());
		List<String> taxNames = new ArrayList<String>();
					
		if (taxAndListOfMapsOfDateAndPercentage != null) {
			for (MiscellaneousTax tax : unitTax.getMiscellaneousTaxes()) {
				Map<Date, BigDecimal> taxDateAndPercentages = taxAndListOfMapsOfDateAndPercentage.get(tax.getTaxName());

				if (taxDateAndPercentages == null) {
					taxNames.add(tax.getTaxName());
					continue;
				}

				if (tax.getTaxDetails().size() == 1) {

					MiscellaneousTaxDetail taxDetail = tax.getTaxDetails().get(0);
					BigDecimal existingTaxValue = taxDateAndPercentages.get(taxDetail.getFromDate());
					BigDecimal currentTaxValue = tax.getTotalCalculatedTax();
					
					if (existingTaxValue == null) {
						taxNames.add(tax.getTaxName());
					} else if (isPropertyModified && (taxDetail.getNoOfDays() > 0)
							&& existingTaxValue.compareTo(currentTaxValue) != 0) {
						taxNames.add(tax.getTaxName());
					}

					/*if (existingTaxValue == null && currentTaxValue == null) {
						if (taxDetail.getCalculatedTaxValue() != null) {
							BigDecimal existingCalculatedTaxValue = dateAndFlatAmountByTaxForUnit.get(unitTax.getUnitNumber()).get(tax.getTaxName()).get(taxDetail.getFromDate());
							if (existingCalculatedTaxValue != null && taxDetail.getCalculatedTaxValue().equals(existingCalculatedTaxValue)) {
								continue;
							} else {
								taxNames.add(tax.getTaxName());
							}
						} else {
							continue;
						}
					} else if (existingTaxValue == null && currentTaxValue != null) {
						taxNames.add(tax.getTaxName());
					} else if (existingTaxValue.equals(currentTaxValue)) {
						continue;
					} else {
						taxNames.add(tax.getTaxName());
					}*/
				}
			}
		}
		LOGGER.info("getSlabChangedTaxes - slab changed taxes : " + taxNames);
		LOGGER.info("Exiting from getSlabChangedTaxes");
		return taxNames;
	}

	public void prepareUnitTaxCalcsOfUnitNo(Map<Integer, List<UnitTaxCalculationInfo>> unitTaxesMap,
			List<UnitTaxCalculationInfo> units) {
		LOGGER.info("prepareUnitTaxCalcsOfUnitNo, map keys: " + unitTaxesMap.keySet());
		LOGGER.info("map size: " + unitTaxesMap.size());
		for (UnitTaxCalculationInfo unit : units) {
			if (unitTaxesMap.get(unit.getUnitNumber()) == null) {
				List<UnitTaxCalculationInfo> tmpList = new ArrayList<UnitTaxCalculationInfo>();
				tmpList.add(unit);
				unitTaxesMap.put(unit.getUnitNumber(), tmpList);
			} else {
				unitTaxesMap.get(unit.getUnitNumber()).add(unit);
			}
		}
		LOGGER.info("prepareUnitTaxCalcsOfUnitNo, map keys: " + unitTaxesMap.keySet());
		LOGGER.info("map size: " + unitTaxesMap.size());
		LOGGER.info("Exit from prepareUnitTaxCalcsOfUnitNo");
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
			UnitTaxCalculationInfo prevUnitTax, UnitTaxCalculationInfo currentUnitTax, List<String> changedTaxNames, Boolean isPropertyModified) {

		List<MiscellaneousTax> prevMiscTaxes = prevUnitTax.getMiscellaneousTaxes();
		List<MiscellaneousTax> currentMiscTaxes = currentUnitTax.getMiscellaneousTaxes();
		List<MiscellaneousTax> newMisTaxes = new ArrayList<MiscellaneousTax>();
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
				if (dmdRsnExists) {
					if (changedTaxNames.contains(currentMiscTax.getTaxName())) {
						LOGGER.info(currentMiscTax.getTaxName() + " has changed, " + " for installment: " + installment
								+ ", EffecDate: " + taxEffectiveDate);						
						
						//To indicate that the change is because of modification, so showing the Occupancy Date
						if (isPropertyModified && isModificationBetInstallment) {
							currentUnitTax.setInstDate(DateUtils.getDefaultFormattedDate(currentUnitTax
									.getOccpancyDate()));
						} else {
							currentMiscTax.setHasChanged(true);
							currentUnitTax.setInstDate(DateUtils.getDefaultFormattedDate(taxEffectiveDate));
						}
						
						putUnitTaxInMapByDate(currentUnitTax, unitTaxForChangeInTaxByDate, taxEffectiveDate,
								currentMiscTax, isPropertyModified, isModificationBetInstallment);
						currentMiscTax.setHasChanged(false);
					}
				} else {
					currentMiscTax.setHasChanged(true);
					putUnitTaxInMapByDate(currentUnitTax, unitTaxForChangeInTaxByDate, taxEffectiveDate,
							currentMiscTax, isPropertyModified, isModificationBetInstallment);
					currentMiscTax.setHasChanged(false);
					newMisTaxes.add(currentMiscTax);
				}
			}
		}

		unitTaxCalcs.addAll(unitTaxForChangeInTaxByDate.values());

		return unitTaxCalcs;
	}

	private void putUnitTaxInMapByDate(UnitTaxCalculationInfo currentUnitTax,
			Map<Date, UnitTaxCalculationInfo> unitTaxForChangeInTaxByDate, Date taxEffectiveDate,
			MiscellaneousTax currentMiscTax, Boolean isPropertyModified, Boolean isModificationBetInstallment) {
		if (unitTaxForChangeInTaxByDate.get(taxEffectiveDate) == null) {
			
			if (isPropertyModified && isModificationBetInstallment) {
				currentUnitTax.setInstDate(DateUtils.getDefaultFormattedDate(currentUnitTax.getOccpancyDate()));
				unitTaxForChangeInTaxByDate.put(currentUnitTax.getOccpancyDate(),
						propertyTaxUtil.getUnitTaxCalculationInfoClone(currentUnitTax));
			} else {
				currentUnitTax.setInstDate(DateUtils.getDefaultFormattedDate(taxEffectiveDate));
				unitTaxForChangeInTaxByDate.put(taxEffectiveDate,
						propertyTaxUtil.getUnitTaxCalculationInfoClone(currentUnitTax));
			}			
		} else {
			for (MiscellaneousTax mt : unitTaxForChangeInTaxByDate.get(taxEffectiveDate).getMiscellaneousTaxes()) {
				if (mt.getTaxName().equals(currentMiscTax.getTaxName())) {
					mt.setHasChanged(true);
					break;
				}
			}
			LOGGER.info("multiple miscTax change for date " + taxEffectiveDate
					+ unitTaxForChangeInTaxByDate.get(taxEffectiveDate).getMiscellaneousTaxes());
		}
	}

	public List<UnitTaxCalculationInfo> getUnitTaxesForMultipleTaxSlabs(Installment installment,
			List<UnitTaxCalculationInfo> units) {
		LOGGER.info("Into getUnitTaxesForMultipleTaxSlabs - Installment: " + installment + ", units: " + units.size());

		Boolean hasMultipleTaxSlabs = Boolean.FALSE;
		for (MiscellaneousTax tax : units.get(0).getMiscellaneousTaxes()) {
			if (tax.getTaxDetails().size() > 1) {
				hasMultipleTaxSlabs = Boolean.TRUE;
				break;
			}
		}
		UnitTaxCalculationInfo u = propertyTaxUtil.getUnitTaxCalculationInfoClone(units.get(0));

		if (hasMultipleTaxSlabs) {
			for (UnitTaxCalculationInfo unitInfo : units) {
				for (MiscellaneousTax t : unitInfo.getMiscellaneousTaxes()) {
					if (t.getHasChanged()) {
						for (MiscellaneousTax ut : u.getMiscellaneousTaxes()) {
							if (ut.getTaxName().equals(t.getTaxName())) {
								ut.setHasChanged(t.getHasChanged());
								break;
							}
						}
					}
				}
			}
		} else {
			LOGGER.info("Units doesnt have multiple tax slab");
			for (UnitTaxCalculationInfo unitTaxInfo : units) {
				unitTaxInfo.setInstDate(unitTaxInfo.getInstDate());
			}

			return units;
		}

		LOGGER.info("MiscTaxes: " + u.getMiscellaneousTaxes());

		List<UnitTaxCalculationInfo> unitTaxes = new ArrayList<UnitTaxCalculationInfo>();

		Boolean hasALVChanged = FALSE;

		Map<Date, List<MiscellaneousTax>> miscTaxesByDate = new TreeMap<Date, List<MiscellaneousTax>>();
		Map<Date, UnitTaxCalculationInfo> unitClonesByDate = new TreeMap<Date, UnitTaxCalculationInfo>();
		Map<Date, Map<String, Boolean>> taxNameSlabExistanceByDate = new TreeMap<Date, Map<String, Boolean>>();

		hasALVChanged = u.getHasALVChanged();

		for (MiscellaneousTax miscTax : u.getMiscellaneousTaxes()) {
			LOGGER.info("MiscTaxName: " + miscTax.getTaxName());
			if (miscTax.getTaxDetails().size() > 1) {
				for (MiscellaneousTaxDetail taxDetail : miscTax.getTaxDetails()) {
					LOGGER.info("Tax: " + taxDetail.getCalculatedTaxValue());
					Map<String, Boolean> taxNameBySlabExistance = new TreeMap<String, Boolean>();
					taxNameBySlabExistance.put(miscTax.getTaxName(), true);
					UnitTaxCalculationInfo unitClone = new UnitTaxCalculationInfo(u);
					unitClone.setHasALVChanged(Boolean.FALSE);
					MiscellaneousTax miscTaxClone = new MiscellaneousTax(miscTax);
					MiscellaneousTaxDetail taxDetailClone = new MiscellaneousTaxDetail(taxDetail);

					if (unitClonesByDate.get(taxDetail.getFromDate()) == null) {
						if (miscTaxesByDate.get(taxDetail.getFromDate()) == null) {
							List<MiscellaneousTax> taxes = new ArrayList<MiscellaneousTax>();

							for (MiscellaneousTax tax : u.getMiscellaneousTaxes()) {
								if (!tax.getTaxName().equals(miscTaxClone.getTaxName())) {
									MiscellaneousTax taxClone = new MiscellaneousTax(tax);
									taxClone.getTaxDetails().addAll(
											new ArrayList<MiscellaneousTaxDetail>(tax.getTaxDetails()));
									taxes.add(taxClone);
								}
							}

							miscTaxClone.addMiscellaneousTaxDetail(taxDetailClone);
							taxes.add(miscTaxClone);
							miscTaxesByDate.put(taxDetail.getFromDate(), taxes);

						} else {
							updateTax(miscTaxClone.getTaxName(), taxDetailClone,
									miscTaxesByDate.get(taxDetail.getFromDate()));
						}

						unitClone.getMiscellaneousTaxes().addAll(miscTaxesByDate.get(taxDetail.getFromDate()));
						LOGGER.info("taxDetail.getFromDate(): " + taxDetail.getFromDate());

						if (propertyTaxUtil.between(unitClone.getOccpancyDate(), installment.getFromDate(),
								installment.getToDate())) {
							unitClone.setInstDate(DateUtils.getDefaultFormattedDate(unitClone.getOccpancyDate()));
						} else {
							unitClone.setInstDate(DateUtils.getDefaultFormattedDate(taxDetail.getFromDate()));
						}
						unitClone.setInstDate(unitClone.getInstDate());
						unitClonesByDate.put(taxDetail.getFromDate(), unitClone);

					} else {
						UnitTaxCalculationInfo un = unitClonesByDate.get(taxDetail.getFromDate());
						updateTax(miscTax.getTaxName(), taxDetailClone, un.getMiscellaneousTaxes());
					}

					LOGGER.info("miscTaxClone : " + unitClone.getMiscellaneousTaxes());
					LOGGER.info("taxDetailClone : " + taxDetailClone);

					if (taxNameSlabExistanceByDate.get(taxDetail.getFromDate()) == null) {
						taxNameSlabExistanceByDate.put(taxDetail.getFromDate(), taxNameBySlabExistance);
					} else {
						Map<String, Boolean> map = taxNameSlabExistanceByDate.get(taxDetail.getFromDate());
						map.put(miscTax.getTaxName(), true);
					}
				}
			}
		}

		if (hasMultipleTaxSlabs) {
			/**
			 * 1. get all the UnitTaxCalcInfo except last UnitTaxCalInfo(having
			 * greatest effective date) 2. loop through MiscTaxes 3. if false(no
			 * multi slab present) the update the calculated value with "-"
			 * 
			 * 
			 */
			LOGGER.info("Dates: " + unitClonesByDate.keySet());
			LOGGER.info("slab Existance dates : " + taxNameSlabExistanceByDate.keySet());
			UnitTaxCalculationInfo uni = null;
			Date date = null;
			int count = 0;
			for (Map.Entry<Date, UnitTaxCalculationInfo> entry : unitClonesByDate.entrySet()) {
				LOGGER.info("First key: " + entry.getKey());

				date = entry.getKey();

				uni = propertyTaxUtil.getUnitTaxCalculationInfoClone(entry.getValue());

				if (count + 1 == unitClonesByDate.size()) {
					break;
				}

				if (count == 0 && hasALVChanged) {
					uni.setHasALVChanged(hasALVChanged);
					uni.setInstDate(DateUtils.getDefaultFormattedDate(installment.getFromDate()));
				}

				for (MiscellaneousTax tax : uni.getMiscellaneousTaxes()) {
					Boolean slabStatus = taxNameSlabExistanceByDate.get(date).get(tax.getTaxName());
					if (slabStatus == null) {
						tax.getTaxDetails().get(0).setCalculatedTaxValue(new BigDecimal("-1"));
					}
				}
				LOGGER.info("Updated: " + uni.getMiscellaneousTaxes());
				unitClonesByDate.put(date, uni);
				count++;
			}
			unitTaxes.addAll(unitClonesByDate.values());
			LOGGER.info("After adding unitClonesByDate to unitTaxes: " + unitTaxes.size());
		}

		LOGGER.info("getUnitTaxesForMultipleTaxSlabs - after copy units: " + unitTaxes.size());
		LOGGER.info("Exit from getUnitTaxesForMultipleTaxSlabs");
		return unitTaxes;
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
		if (!property.getBasicProperty().getPropMutationSet().isEmpty())
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
		if (!property.getBasicProperty().getPropMutationSet().isEmpty())
			return ptisCacheMgr.buildOwnerFullName(property.getPropertyOwnerSet());

		return "";
	}

	/**
	 * The method returns the total number of 'Tenants' in the property.
	 * 
	 * @return an int value representing the number of tenants in the property
	 */
	public int getNoOfTenants() {
		int tenants = 0;
		for (UnitTaxCalculationInfo unit : taxCalInfo.getUnitTaxCalculationInfo()) {
			if (TENANT.equals(unit.getUnitOccupation()))
				tenants++;
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
		Set<PropertyMutation> propMutSet = property.getBasicProperty().getPropMutationSet();

		if (propMutSet.isEmpty()) {
			return null;
		}

		SortedSet<Date> mutationDates = new TreeSet<Date>();
		for (PropertyMutation propertyMutation : propMutSet) {
			mutationDates.add(propertyMutation.getMutationDate());
		}

		return mutationDates.last();
	}

	public BigDecimal getTotalTax() {
		//return totalTax;
		return taxCalInfo.getTotalTaxPayable();
	}

	public BigDecimal getTotalALV() {
		return taxCalInfo.getTotalAnnualLettingValue();
	}

	public int getNoOfUnits() {
		return taxCalInfo.getConsolidatedUnitTaxCalculationInfo().size();
	}

	public String getPropertyNo() {
		return taxCalInfo.getIndexNo();
	}

	public String getHouseNo() {
		return taxCalInfo.getHouseNumber();
	}

	private String getWardNumber() {
		return property.getBasicProperty().getBoundary().getBoundaryNum().toString();
	}

	public String getWardNo() {

		return StringUtils.leftPad(getWardNumber(), 3, '0');
	}

	public String getZoneNo() {

		return StringUtils.leftPad(property.getBasicProperty().getBoundary().getParent().getBoundaryNum().toString(),
				2, '0');
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
		return taxCalInfo.getPropertyAddress().concat(", Nagpur");
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
	
	/*public void setTotalTax(BigDecimal totalTax) {
		this.totalTax = totalTax;
	}*/

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
				miscTaxAndTotalTaxForUnit.put(unit.getUnitNumber(), miscTaxAndTotalTax);
			}

		} else {
			
			for (UnitTaxCalculationInfo unit : unitTaxes) {
				Map<String, BigDecimal> miscTaxAndTotalTax = miscTaxAndTotalTaxForUnit.get(unit.getUnitNumber()) == null ? new HashMap<String, BigDecimal>()
						: miscTaxAndTotalTaxForUnit.get(unit.getUnitNumber());
				for (MiscellaneousTax miscTax : unit.getMiscellaneousTaxes()) {
					miscTaxAndTotalTax.put(miscTax.getTaxName(), miscTax.getTotalCalculatedTax());
				}
				miscTaxAndTotalTaxForUnit.put(unit.getUnitNumber(), miscTaxAndTotalTax);
			}
			
		}

		LOGGER.info("getTotalTaxForMiscTax - miscTaxAndTotalTaxForUnit: " + miscTaxAndTotalTaxForUnit);
		LOGGER.info("Exiting from getTotalTaxForMiscTax");
	}
	
	private Boolean hasAnyTotalTaxChangedForTaxes(Map<Integer, Map<String, BigDecimal>> miscTaxAndTotalTaxForUnit, UnitTaxCalculationInfo unitTax) {
		LOGGER.info("Entered into hasAnyTotalTaxChangedForTaxes");
		Map<String, BigDecimal> miscTaxAndTotalTax  = miscTaxAndTotalTaxForUnit.get(unitTax.getUnitNumber());
		
		if (miscTaxAndTotalTax == null) {
			return true;
		} else {
			
			for (MiscellaneousTax miscTax : unitTax.getMiscellaneousTaxes()) {
				if (!miscTax.getTotalCalculatedTax().equals(miscTaxAndTotalTax.get(miscTax.getTaxName()))) {
					LOGGER.info("hasAnyTotalTaxChangedForTaxes - " + miscTax.getTaxName() + "'s Total Tax is not matching...");
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
	
	private Map<Date, Map<Integer, List<UnitTaxCalculationInfo>>> orderByDate(Map<Integer, List<UnitTaxCalculationInfo>> unitsByUnitNo) throws ParseException {
		LOGGER.debug("Entered into orderByDate");
		
		Map<Date, Map<Integer, List<UnitTaxCalculationInfo>>> unitTaxesByDateUnitNo = new TreeMap<Date, Map<Integer, List<UnitTaxCalculationInfo>>>();
		DateFormat dateFormat = new SimpleDateFormat(NMCPTISConstants.DATE_FORMAT_DDMMYYY);
		
		for (Map.Entry<Integer, List<UnitTaxCalculationInfo>> mapEntry : unitsByUnitNo.entrySet()) {

			for (UnitTaxCalculationInfo unit : mapEntry.getValue()) {
				Date date = dateFormat.parse(unit.getInstDate());

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
	
}
