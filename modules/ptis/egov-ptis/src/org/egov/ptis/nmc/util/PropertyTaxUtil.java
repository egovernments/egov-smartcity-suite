package org.egov.ptis.nmc.util;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static java.math.BigDecimal.ZERO;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;
import static org.egov.ptis.constants.PropertyTaxConstants.SESSION_VAR_LOGIN_USER_NAME;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.AMENITY_PERCENTAGE_FULL;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.AMENITY_PERCENTAGE_NIL;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.AMENITY_PERCENTAGE_PARTIAL;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.AMENITY_TYPE_FULL;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.AMENITY_TYPE_NIL;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.AMENITY_TYPE_PARTIAL;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.AMP_ACTUAL_STR;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.AMP_ENCODED_STR;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.ARREARS_DMD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.BASERENT_FROM_APRIL2008_BUILDINGS;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.BASERENT_FROM_APRIL2008_OPENPLOT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.COMMA_STR;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.CURRENT_DMD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DATE_CONSTANT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_FIRE_SERVICE_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_GENERAL_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_GENERAL_WATER_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_LIGHTINGTAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_PENALTY_FINES;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_SEWERAGE_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_REBATE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMAND_REASON_ORDER_MAP;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.EFFECTIVE_ASSESSMENT_PERIOD1;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.EFFECTIVE_ASSESSMENT_PERIOD2;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.EFFECTIVE_ASSESSMENT_PERIOD3;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.EFFECTIVE_ASSESSMENT_PERIOD4;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.FLOORNO_WITH_DIFF_MULFACTOR_NONRESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.FLOORNO_WITH_DIFF_MULFACTOR_RESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.FLOOR_MAP;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.GWR_IMPOSED;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.NONRESD_FLAT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.NON_RESIDENTIAL_FLOOR_AREA_MAP;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPERTY_MODIFY_REASON_MODIFY;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_CAT_RESD_CUM_NON_RESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_CENTRAL_GOVT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_MIXED;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_NON_RESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_OPEN_PLOT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_RESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_STATE_GOVT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.QUERY_BASERENT_BY_BOUNDARY_FOR_OPENPLOT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.QUERY_BASERENT_BY_OCCUPANCY_AREA_STRUCTURE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.QUERY_DEMANDREASONBY_CODE_AND_INSTALLMENTID;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.QUERY_DEMANDREASONDETAILBY_DEMANDREASONID;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.QUERY_DEMANDREASONDETAILS_BY_DEMANDREASONID_DATE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.QUERY_DEMANDREASONDETAILS_BY_DEMANDREASON_AND_INSTALLMENT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.QUERY_DEPARTMENTS_BY_DEPTCODE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.QUERY_INSTALLMENTLISTBY_MODULE_AND_STARTYEAR;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.RESD_CUM_COMMERCIAL_PROP_ALV_PERCENTAGE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.RESIDENTIAL_FLATS;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.RESIDENTIAL_FLOOR_AREA_MAP;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.STATEGOVT_BUILDING_GENERALTAX_ADDITIONALDEDUCTION;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.STRING_SEPERATOR;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.UNITTYPE_OPEN_PLOT;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.egov.commons.CFinancialYear;
import org.egov.commons.Installment;
import org.egov.commons.dao.CommonsDaoFactory;
import org.egov.commons.dao.InstallmentDao;
import org.egov.demand.dao.DCBHibernateDaoFactory;
import org.egov.demand.dao.DemandGenericHibDao;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgBillType;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.demand.model.EgDemandReasonDetails;
import org.egov.infstr.auditing.model.AuditEntity;
import org.egov.infstr.auditing.model.AuditEvent;
import org.egov.infstr.auditing.model.AuditModule;
import org.egov.infstr.auditing.service.AuditEventService;
import org.egov.infstr.commons.Module;
import org.egov.infstr.commons.dao.GenericDaoFactory;
import org.egov.infstr.commons.dao.GenericHibernateDaoFactory;
import org.egov.infstr.commons.dao.ModuleDao;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.config.dao.AppConfigValuesDAO;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.EgovUtils;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.ejb.api.UserService;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.Position;
import org.egov.pims.commons.service.EisCommonsService;
import org.egov.pims.commons.service.EisCommonsServiceImpl;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EisUtilService;
import org.egov.pims.service.EmployeeService;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.dao.property.BoundaryCategoryDao;
import org.egov.ptis.domain.dao.property.PropertyDAO;
import org.egov.ptis.domain.dao.property.PropertyDAOFactory;
import org.egov.ptis.domain.entity.demand.PTDemandCalculations;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.BasicPropertyImpl;
import org.egov.ptis.domain.entity.property.BoundaryCategory;
import org.egov.ptis.domain.entity.property.Category;
import org.egov.ptis.domain.entity.property.FloorIF;
import org.egov.ptis.domain.entity.property.FloorImpl;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyAddress;
import org.egov.ptis.domain.entity.property.PropertyArrear;
import org.egov.ptis.domain.entity.property.PropertyStatusValues;
import org.egov.ptis.nmc.constants.NMCPTISConstants;
import org.egov.ptis.nmc.handler.TaxCalculationInfoXmlHandler;
import org.egov.ptis.nmc.model.ApplicableFactor;
import org.egov.ptis.nmc.model.AreaTaxCalculationInfo;
import org.egov.ptis.nmc.model.ConsolidatedUnitTaxCalReport;
import org.egov.ptis.nmc.model.MiscellaneousTax;
import org.egov.ptis.nmc.model.MiscellaneousTaxDetail;
import org.egov.ptis.nmc.model.PropertyArrearBean;
import org.egov.ptis.nmc.model.TaxCalculationInfo;
import org.egov.ptis.nmc.model.UnitTaxCalculationInfo;

public class PropertyTaxUtil {

	private static final Logger LOGGER = Logger.getLogger(PropertyTaxUtil.class);
	private PersistenceService persistenceService;
	private UserService userService;
	private EmployeeService employeeService;
	private static GenericHibernateDaoFactory genericDao;
	private static final String HUNDRED = "100";
	SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
	private AuditEventService auditEventService;
	
	/**
	 * Calculates applicable taxes
	 * 
	 * <p>
	 * This method is used both to calculate taxes on unconsolidated ALV and to calculate taxes on 
	 * consolidated ALV's. If taxNameAndALV map is null then use will be to calculate taxes on 
	 * unconsolidated ALV else this map contains the ALV to be used to calculate respective tax 
	 * </p>
	 * 
	 * @param applicableTaxes     The DemandReasonMaster codes
	 * @param taxNameAndALV        a map containing taxName and ALV pair, determines ALV to be used in calculating tax taxName
	 * @param unitTaxCalculationInfo calculated tax details are stored in <code> MiscellaneousTax <code>, this wil be addedd to 
	 * 							<code> unitTaxCalculationInfo </code>
	 * @param installment         The installment for which tax has to be calculated
	 * @param propType			   The property type code 
	 * @param propTypeCategory    The property type category
	 * @param amenities           Amenties in case of Central Govt. property
	 * @param property			   The property containing the property details
	 * 
	 * @return {@link UnitTaxCalculationInfo}
	 */
	
	public UnitTaxCalculationInfo calculateApplicableTaxes(List<String> applicableTaxes,
			Map<String, BigDecimal> taxNameAndALV, UnitTaxCalculationInfo unitTaxCalculationInfo,
			Installment installment, String propType, String propTypeCategory, String amenities, Property property) {

		BigDecimal totalTaxPayable = BigDecimal.ZERO;
		BigDecimal alv = unitTaxCalculationInfo.getAnnualRentAfterDeduction();
		LOGGER.debug("calculateApplicableTaxes - ALV: " + alv);
		LOGGER.debug("calculateApplicableTaxes - applicableTaxes: " + applicableTaxes);
		LOGGER.debug("calculateApplicableTaxes - taxNameAndALV: " + taxNameAndALV);
		
		Date newOccupationDate = unitTaxCalculationInfo.getOccpancyDate();
		Boolean isPropertyModified = false;
		Boolean isUnitsMatch = false;
		Boolean isNewUnit = true;
		
		// Dependent on taxNameAndALV map because this contains the TaxName and ALV pair for the Unit
		Boolean isCallForConsolidation = taxNameAndALV == null || taxNameAndALV.isEmpty() ? false : true;
		
		if (isCallForConsolidation) {
			alv = null;
		}
		
		for (PropertyStatusValues psv : property.getBasicProperty().getPropertyStatusValuesSet()) {
			if (PROPERTY_MODIFY_REASON_MODIFY.equalsIgnoreCase(psv.getPropertyStatus().getStatusCode())) {
				isPropertyModified = true;
				break;
			}
		}
		
		if (!newOccupationDate.equals(installment.getFromDate())
				&& between(newOccupationDate, installment.getFromDate(), installment.getToDate()) && isPropertyModified) {
			
			TaxCalculationInfo taxCalcInfo = null;
			List<String> oldApplicableTaxes = new ArrayList<String>();			
			BigDecimal historyALV = null;
			Date oldUnitOccupancyDate = null; 
			
			for (Ptdemand ptDemand : property.getBasicProperty().getProperty().getPtDemandSet()) {
				if (ptDemand.getEgInstallmentMaster().equals(installment)) {
					taxCalcInfo = getTaxCalInfo(ptDemand);
					break;
				}
			}
			
			if (taxCalcInfo != null) {

				for (UnitTaxCalculationInfo unit : isCallForConsolidation ? taxCalcInfo
						.getConsolidatedUnitTaxCalculationInfo() : taxCalcInfo.getUnitTaxCalculationInfo()) {

					isUnitsMatch = getIsUnitsMatch(unitTaxCalculationInfo, propType, isCallForConsolidation, unit);

					if (isUnitsMatch) {
						historyALV = unit.getAnnualRentAfterDeduction();
						oldUnitOccupancyDate = new Date(unit.getOccpancyDate().getTime());
						for (MiscellaneousTax miscTax : unit.getMiscellaneousTaxes()) {
							oldApplicableTaxes.add(miscTax.getTaxName());
						}
						break;
					}
				}
			}
			
			// Calculates the Arrears (only on modification), if oldApplicableTaxes is empty then unitTaxCalculationInfo is new unit
			if (!oldApplicableTaxes.isEmpty() && !oldUnitOccupancyDate.equals(unitTaxCalculationInfo.getOccpancyDate())) {
				LOGGER.debug("Calculating Arrears Taxes on Modification, UnitNo: "
						+ unitTaxCalculationInfo.getUnitNumber() + ", historyALV: " + historyALV
						+ ", oldUnitOccupancyDate : " + oldUnitOccupancyDate);
				isNewUnit = false;
				totalTaxPayable = calculateTaxes(isNewUnit, oldApplicableTaxes, unitTaxCalculationInfo, installment,
						propType, propTypeCategory, amenities, totalTaxPayable, historyALV, isPropertyModified,
						oldUnitOccupancyDate, taxNameAndALV);

			}
			
		}

		totalTaxPayable = calculateTaxes(isNewUnit, applicableTaxes, unitTaxCalculationInfo, installment, propType,
				propTypeCategory, amenities, totalTaxPayable, alv, isPropertyModified, null, taxNameAndALV);
		
		
		
		LOGGER.debug(" Total Tax Payable After Applying Applicable Taxes: "
				+ totalTaxPayable.setScale(2, BigDecimal.ROUND_HALF_UP));
		
		totalTaxPayable = roundHistoryTax(unitTaxCalculationInfo, totalTaxPayable);
		
		unitTaxCalculationInfo.setTotalTaxPayable(totalTaxPayable.setScale(0, BigDecimal.ROUND_HALF_UP));
		LOGGER.debug("calculateApplicableTaxes - applicableTaxes: " + applicableTaxes);
		return unitTaxCalculationInfo;
	}

	/**
	 * 
	 * @param unitTaxThis UnitTaxCalculationInfo object to be compared
	 * @param propType
	 * @param isCallForConsolidation
	 * @param unitTaxAgainst unitTaxThis.unitNumber will be compared against unitTaxAgainst.unitNumber
	 * @return true if units match
	 */
	
	public static Boolean getIsUnitsMatch(UnitTaxCalculationInfo unitTaxThis, String propType,
			Boolean isCallForConsolidation, UnitTaxCalculationInfo unitTaxAgainst) {
		Boolean isUnitsMatch;
		if (PROPTYPE_OPEN_PLOT.equalsIgnoreCase(propType)) {
			if (unitTaxThis.getUnitNumber().equals(unitTaxAgainst.getUnitNumber())) {
				isUnitsMatch = true;
			} else {
				isUnitsMatch = false;
			}
		} else {

			// When calculating taxes for consolidated unit, then
			// using only UnitNumber to say that units match
			// (History unit & Current unit)
			// else using UnitNumber + Floor Number for unit
			// matching.
			isUnitsMatch = isCallForConsolidation ? (unitTaxThis.getUnitNumber().equals(
					unitTaxAgainst.getUnitNumber()) ? true : false)
					: (unitTaxThis.getUnitNumber().equals(unitTaxAgainst.getUnitNumber())
							&& unitTaxThis.getFloorNumberInteger().equals(
									unitTaxAgainst.getFloorNumberInteger()) ? true : false);
		}
		return isUnitsMatch;
	}

	private BigDecimal roundHistoryTax(UnitTaxCalculationInfo unitTaxCalculationInfo, BigDecimal totalTaxPayable) {

		LOGGER.debug("roundHistoryTax - totalTaxPayable : " + totalTaxPayable);

		for (MiscellaneousTax miscTax : unitTaxCalculationInfo.getMiscellaneousTaxes()) {
			//LOGGER.info("Tax : " + miscTax.getTaxName());
			Boolean hasOnlyHistoryTaxDetails = false;
			for (MiscellaneousTaxDetail taxDetail : miscTax.getTaxDetails()) {
				//LOGGER.info("isHistory : " + taxDetail.getIsHistory());
				hasOnlyHistoryTaxDetails = taxDetail.getIsHistory() == null || taxDetail.getIsHistory().equals('N') ? false
						: true;
			}

			if (hasOnlyHistoryTaxDetails) {
				totalTaxPayable = totalTaxPayable.subtract(miscTax.getTotalCalculatedTax());
				miscTax.setTotalActualTax(miscTax.getTotalActualTax().setScale(0, BigDecimal.ROUND_HALF_UP));
				miscTax.setTotalCalculatedTax(miscTax.getTotalCalculatedTax().setScale(0, BigDecimal.ROUND_HALF_UP));
				totalTaxPayable = totalTaxPayable.add(miscTax.getTotalCalculatedTax());
			}
		}

		LOGGER.debug("roundHistoryTax - after rounding totalTaxPayable : " + totalTaxPayable);

		return totalTaxPayable;
	}

	private BigDecimal calculateTaxes(Boolean isNewUnit, List<String> applicableTaxes,
			UnitTaxCalculationInfo unitTaxCalculationInfo, Installment installment, String propType,
			String propTypeCategory, String amenities, BigDecimal totalTaxPayable, BigDecimal alv, Boolean isModify,
			Date oldUnitOccupancyDate, Map<String, BigDecimal> taxNameAndALV) {
		
		LOGGER.debug("Entered into calculateTaxes");
		LOGGER.debug("calculateTaxes - applicableTaxes: " + applicableTaxes);
		
		Integer totalNoOfDays = getNumberOfDays(installment.getFromDate(), installment.getToDate()).intValue();
		
		if (taxNameAndALV != null && isModify) {
			LOGGER.debug("calculateTaxes -  calculating for consolidated unit");
			LOGGER.debug("calculateTaxes - alv expected is null : " + alv);
			LOGGER.debug("ALV will be taken from taxNameAndALV  : " + taxNameAndALV);
			//applicableTaxes = new ArrayList<String>();
			//applicableTaxes.addAll(taxNameAndALV.keySet());
		}
		
		for (String demandReasonCode : applicableTaxes) {

			LOGGER.debug("demandReasonCode: " + demandReasonCode);
			List<EgDemandReasonDetails> demandReasonDetails = new ArrayList<EgDemandReasonDetails>();
			MiscellaneousTax miscellaneousTax = null;
			Boolean isExistingMiscTax = false;
			
			if (taxNameAndALV != null && !taxNameAndALV.isEmpty()) {
				alv = taxNameAndALV.get(demandReasonCode);
			}
			
			// Property is being modified is in mid of installment, so Arrears Taxes have already been
			// calculated and have been added to the unitTaxCalculationInfo.miscellaneousTaxes
			// When calculating new Taxes for the same MiscellaneousTax.taxName(DemandReason), just getting
			// the existing MiscellaneousTax object and new calculated taxes details will be added as MiscellaneousTaxDetail
			// this existing MiscellaneousTax
			if (alv == null) {
				LOGGER.debug("calculateTaxes - ALV is null");
			} else {
				if (oldUnitOccupancyDate == null
						&& between(unitTaxCalculationInfo.getOccpancyDate(), installment.getFromDate(),
								installment.getToDate()) && isModify) {
					if (isNewUnit) {
						miscellaneousTax = new MiscellaneousTax();
						miscellaneousTax.setTaxName(demandReasonCode);
						miscellaneousTax.setTotalActualTax(BigDecimal.ZERO);
						miscellaneousTax.setTotalCalculatedTax(BigDecimal.ZERO);
					} else {
						for (MiscellaneousTax miscTax : unitTaxCalculationInfo.getMiscellaneousTaxes()) {
							if (demandReasonCode.equalsIgnoreCase(miscTax.getTaxName())) {
								miscellaneousTax = miscTax;
								isExistingMiscTax = true;
								break;
							}
						}

						if (!isExistingMiscTax) {
							miscellaneousTax = new MiscellaneousTax();
							miscellaneousTax.setTaxName(demandReasonCode);
							miscellaneousTax.setTotalActualTax(BigDecimal.ZERO);
							miscellaneousTax.setTotalCalculatedTax(BigDecimal.ZERO);
						}
					}
				} else {
					miscellaneousTax = new MiscellaneousTax();
					miscellaneousTax.setTaxName(demandReasonCode);
					miscellaneousTax.setTotalActualTax(BigDecimal.ZERO);
					miscellaneousTax.setTotalCalculatedTax(BigDecimal.ZERO);
				}

				/**
				 * This following logic is to prevent to get multiple Tax %
				 * Example: Unit effective date is 01-07-1982 without
				 * dummyInstallment object the the installment.fromDate =
				 * 01-04-1982 & installment.toDate = 31-03-1983 This will give 2
				 * DemandReaosnDetails for the 1982-83 for 1. Sewerage Tax;
				 * effective from 01-04-1966 and from 01-05-1982 2. Water Tax;
				 * as above
				 * 
				 * as Unit effective date is 01-07-1982, the Tax % effective
				 * from 01-04-1966 must not be applied
				 * 
				 */
				if (between(unitTaxCalculationInfo.getOccpancyDate(), installment.getFromDate(),
						installment.getToDate())) {
					Installment dummyInstallment = new Installment();

					if (oldUnitOccupancyDate == null) {
						dummyInstallment.setFromDate(unitTaxCalculationInfo.getOccpancyDate());
						dummyInstallment.setToDate(installment.getToDate());
					} else {

						if (oldUnitOccupancyDate.before(installment.getFromDate())
								|| oldUnitOccupancyDate.equals(installment.getFromDate())) {
							dummyInstallment.setFromDate(installment.getFromDate());
						} else if (oldUnitOccupancyDate.after(installment.getFromDate())) {
							dummyInstallment.setFromDate(oldUnitOccupancyDate);
						}

						dummyInstallment.setToDate(unitTaxCalculationInfo.getOccpancyDate());
					}

					demandReasonDetails = this.getDemandReasonDetails(demandReasonCode, alv, dummyInstallment);
				} else {
					demandReasonDetails = this.getDemandReasonDetails(demandReasonCode, alv, installment);
				}
			}
						
			BigDecimal totalApplicableTaxValue = BigDecimal.ZERO;
			BigDecimal totalActualTax = BigDecimal.ZERO;
			BigDecimal totalCalculatedTax = BigDecimal.ZERO;

			for (EgDemandReasonDetails drd : demandReasonDetails) {

				BigDecimal calculatedTaxValue = BigDecimal.ZERO;
				BigDecimal actualTaxValue = BigDecimal.ZERO;
				BigDecimal demandRsnDtlPercResult = BigDecimal.ZERO;
				BigDecimal taxPerc = BigDecimal.ZERO;
				Integer noOfDays = 0;
				MiscellaneousTaxDetail miscTaxDetail = new MiscellaneousTaxDetail();

				if (propType != null && propType.equalsIgnoreCase(PROPTYPE_STATE_GOVT)
						&& demandReasonCode.equalsIgnoreCase(DEMANDRSN_CODE_GENERAL_TAX)) {

					if (drd != null) {
						if (ZERO.equals(drd.getFlatAmount())) {
							demandRsnDtlPercResult = alv.multiply(drd.getPercentage()).divide(new BigDecimal(HUNDRED));
							calculatedTaxValue = demandRsnDtlPercResult.subtract(demandRsnDtlPercResult.multiply(
									new BigDecimal(STATEGOVT_BUILDING_GENERALTAX_ADDITIONALDEDUCTION)).divide(
									new BigDecimal(HUNDRED)));
						} else if (drd.getPercentage() == null) {
							calculatedTaxValue = drd.getFlatAmount();
						} else {
							taxPerc = drd.getPercentage();
						}
					}
				} else {
					if (drd != null) {
						if (ZERO.equals(drd.getFlatAmount())) {
							taxPerc = drd.getPercentage();
						} else if (drd.getPercentage() == null) {
							calculatedTaxValue = drd.getFlatAmount();
						} else {
							taxPerc = drd.getPercentage();
						}
					}
				}

				if (propTypeCategory != null
						&& propTypeCategory.equalsIgnoreCase(PROPTYPE_CAT_RESD_CUM_NON_RESD)
						&& (demandReasonCode.equals(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD)
								|| (demandReasonCode.equals(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD)) || (demandReasonCode
									.equals(DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX)))) {
					calculatedTaxValue = alv.multiply(
							new BigDecimal(RESD_CUM_COMMERCIAL_PROP_ALV_PERCENTAGE).divide(new BigDecimal(HUNDRED)))
							.multiply(taxPerc.divide(new BigDecimal(HUNDRED)));
				} else if (!taxPerc.equals(ZERO) && ZERO.equals(calculatedTaxValue)) {
					calculatedTaxValue = alv.multiply(taxPerc.divide(new BigDecimal(HUNDRED)));
				}

				//calculatedTaxValue = calculatedTaxValue.setScale(0, BigDecimal.ROUND_HALF_UP);
				//LOGGER.info("calculateApplicableTaxes - before applying slab % calculatedTaxValue : "
					//	+ calculatedTaxValue);

				if (demandReasonDetails.size() > 1
						|| between(unitTaxCalculationInfo.getOccpancyDate(), installment.getFromDate(),
								installment.getToDate())) {
					
					if (isModify && oldUnitOccupancyDate != null) {
						noOfDays = getNumberOfDaysBeforeOccupancy(installment, drd,
								unitTaxCalculationInfo.getOccpancyDate(), oldUnitOccupancyDate);
					} else {
						noOfDays = getNumberOfDays(installment, drd, unitTaxCalculationInfo.getOccpancyDate());
					}
					
					calculatedTaxValue = calculatedTaxValue.multiply(new BigDecimal(noOfDays)).divide(
							new BigDecimal(totalNoOfDays), 2, ROUND_HALF_UP);
				} 
								
				LOGGER.debug("calculateApplicableTaxes - calculatedTaxValue : "
						+ calculatedTaxValue);

				if (drd != null && drd.getFlatAmount().compareTo(ZERO) > 0) {
					calculatedTaxValue = getMinOrMaxFlatAmount(totalNoOfDays, drd, calculatedTaxValue, noOfDays);
				}
				
				LOGGER.debug("calculatedTaxValue after getMinOrMaxFlatAmount: " + calculatedTaxValue);
				
				if (propType != null && propType.equalsIgnoreCase(PROPTYPE_CENTRAL_GOVT)) {
					actualTaxValue = calculatedTaxValue.setScale(0, BigDecimal.ROUND_HALF_UP);
					calculatedTaxValue = calcGovtTaxOnAmenities(amenities, calculatedTaxValue);
				}

				if (drd != null && !ZERO.equals(calculatedTaxValue)) {
					miscTaxDetail.setTaxValue(drd.getPercentage());
					if (propType != null && propType.equalsIgnoreCase(PROPTYPE_CENTRAL_GOVT)) {
						miscTaxDetail.setActualTaxValue(actualTaxValue);
						totalActualTax = totalActualTax.add(actualTaxValue);
					}
					
					miscTaxDetail.setCalculatedTaxValue(calculatedTaxValue);
					LOGGER.debug("calculateTaxes - calculatedTaxValue: " + calculatedTaxValue);
					totalCalculatedTax = totalCalculatedTax.add(calculatedTaxValue);
					LOGGER.debug("calculateTaxes - totalCalculatedTax: " + totalCalculatedTax);
					miscTaxDetail.setFromDate(drd.getFromDate());
					miscTaxDetail.setNoOfDays(noOfDays);
					
					
					// following condition will fail if both old unit occupancy & 
					// new unit occupancy dates are same
					// Clarification required....
					// Can i make use of older occupancy date of this old unit?
					// is this correct?
					
					if (isModify && noOfDays > 0 && oldUnitOccupancyDate != null) {
						miscTaxDetail.setIsHistory('Y');
					} else {
						miscTaxDetail.setIsHistory('N');
					}
					
					miscellaneousTax.addMiscellaneousTaxDetail(miscTaxDetail);
				}
			}					
			
			if (!demandReasonDetails.isEmpty()) {
				
				if (isModify && oldUnitOccupancyDate != null) {
					miscellaneousTax.setTotalActualTax(miscellaneousTax.getTotalActualTax().add(totalActualTax));
					miscellaneousTax.setTotalCalculatedTax(miscellaneousTax.getTotalCalculatedTax().add(totalCalculatedTax));
					
				} else {
					miscellaneousTax.setTotalActualTax(miscellaneousTax.getTotalActualTax().add(
							totalActualTax).setScale(0, BigDecimal.ROUND_HALF_UP));
					miscellaneousTax.setTotalCalculatedTax(miscellaneousTax.getTotalCalculatedTax().add(
							totalCalculatedTax).setScale(0, BigDecimal.ROUND_HALF_UP));
				}
				LOGGER.debug("calculateTaxes - totalCalculatedTax: " + miscellaneousTax.getTotalCalculatedTax());
				totalApplicableTaxValue = totalApplicableTaxValue.add(miscellaneousTax.getTotalCalculatedTax());
				
				if (isNewUnit || !isExistingMiscTax) {
					unitTaxCalculationInfo.addMiscellaneousTaxes(miscellaneousTax);
				}
			}
			totalTaxPayable = totalTaxPayable.add(totalApplicableTaxValue.setScale(0, BigDecimal.ROUND_HALF_UP));
		}
		
		return totalTaxPayable;
	}

	private BigDecimal getMinOrMaxFlatAmount(Integer totalNoOfDays, EgDemandReasonDetails drd,
			BigDecimal calculatedTaxValue, Integer noOfDays) {
			
			// FlatAmount must be the maximum amount
			if (drd.getIsFlatAmntMax().equals(Integer.valueOf(1))
					&& (calculatedTaxValue.compareTo(drd.getFlatAmount()) > 0)) {
				
				if (noOfDays > 0) {
					calculatedTaxValue = drd.getFlatAmount().multiply(new BigDecimal(noOfDays))
							.divide(new BigDecimal(totalNoOfDays));
				} else {
					calculatedTaxValue = drd.getFlatAmount();
				}
			}
			
			//FlatAmount must be the minimum amount
			if (drd.getIsFlatAmntMax().equals(Integer.valueOf(0))
					&& (calculatedTaxValue.compareTo(drd.getFlatAmount()) < 0)) {
					
				if (noOfDays > 0) {
					calculatedTaxValue = drd.getFlatAmount().multiply(new BigDecimal(noOfDays))
							.divide(new BigDecimal(totalNoOfDays), ROUND_HALF_UP);
				} else {
					calculatedTaxValue = drd.getFlatAmount();
				}
			}
		return calculatedTaxValue;
	}

	/**
	 * Return the number of days between dates.
	 * 
	 * @param installment
	 * @param drd
	 * @return
	 */

	public Integer getNumberOfDays(Installment installment, EgDemandReasonDetails drd, Date unitOccupancyDate) {
		LOGGER.debug("Entered into getNumberOfDays - Installment: " + installment + "DemandReasonDetails: " + drd
				+ ", unitOccupancyDate: " + unitOccupancyDate);
		Integer noOfDays = 0;
		Date fromDate = null;
		Date toDate = null;

		if (between(unitOccupancyDate, installment.getFromDate(), installment.getToDate())) {
			fromDate = unitOccupancyDate;
		} else if (installment.getFromDate().after(drd.getFromDate())) {
			fromDate = installment.getFromDate();
		} else {
			fromDate = drd.getFromDate();
		}

		if (installment.getToDate().after(drd.getToDate())) {
			toDate = drd.getToDate();
		} else {
			toDate = installment.getToDate();
		}

		noOfDays = getNumberOfDays(fromDate, toDate).intValue();

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("getNumberOfDays: Installment - " + installment + ", days between " + fromDate + " and "
					+ toDate + " are " + noOfDays);
		}
		LOGGER.debug("Exiting from getNumberOfDays");
		return noOfDays;
	}

	
	public Integer getNumberOfDaysBeforeOccupancy(Installment installment, EgDemandReasonDetails drd,
			Date unitOccupancyDate, Date oldUnitOccupancyDate) {
		LOGGER.debug("Entered into getNumberOfDays");
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("getNumberOfDays - Installment: " + installment + "DemandReasonDetails: " + drd
					+ ", unitOccupancyDate: " + unitOccupancyDate + ", oldUnitOccupancyDate: " + oldUnitOccupancyDate);
		}

		Integer noOfDays = 0;
		Date fromDate = null;
		Date toDate = null;

		/**
		 *
		 * Taking the earliest date among oldUnitOccupancyDate, installment.getFromDate() and DemandReasonDetails.fromDate
		 * 
		 * Ex:         installment.fromDate = 01-04-2010
		 *     DemandReasonDetails.fromDate = 01-06-1985
		 *             oldUnitOccupancyDate = 01-10-2010
		 *             
		 *    Exepected:
		 *    
		 *    fromDate = 01-10-2010 
		 */
		Set<Date> fromDates = new TreeSet<Date>();
		fromDates.add(oldUnitOccupancyDate);
		fromDates.add(installment.getFromDate());
		fromDates.add(drd.getFromDate());
		
		LOGGER.debug("fromDates: " + fromDates);
		
		int i = 1;
		for (Date date : fromDates) {
			if (i == fromDates.size()) {
				fromDate = date;
				break;
			}
			i++;
		}
		
		LOGGER.debug("fromDate: " + fromDate);
		
		Set<Date> toDates = new TreeSet<Date>();
		toDates.add(unitOccupancyDate);
		toDates.add(installment.getToDate());
		toDates.add(drd.getToDate());
		
		LOGGER.debug("toDates: " + toDates);
		
		for (Date date : toDates) {
			toDate = date;
			break;
		}
		
		LOGGER.debug("toDate: " + toDate);

		noOfDays = getNumberOfDays(fromDate, toDate).intValue();

		if (LOGGER.isInfoEnabled()) {
			LOGGER.debug("getNumberOfDays: Installment - " + installment + ", days between " + fromDate + " and "
					+ toDate + " are " + noOfDays);
		}
		LOGGER.debug("Exiting from getNumberOfDays");
		return noOfDays;
	}

	/**
	 * Calculate individual taxes for Central Government properties based on
	 * amenities
	 * 
	 * @param amenities
	 *            - type of amenities that Central Govt Properties having
	 * @param applicableTaxValue
	 *            - tax on which this amenities tax changes has to appy
	 * @return {@link BigDecimal}
	 */
	private BigDecimal calcGovtTaxOnAmenities(String amenities, BigDecimal applicableTaxValue) {
		BigDecimal applicableTaxValueDummy = applicableTaxValue;
		if (amenities.equalsIgnoreCase(AMENITY_TYPE_FULL)) {
			applicableTaxValueDummy = applicableTaxValueDummy.multiply(new BigDecimal(AMENITY_PERCENTAGE_FULL)
					.divide(new BigDecimal(HUNDRED)));
		} else if (amenities.equalsIgnoreCase(AMENITY_TYPE_PARTIAL)) {
			applicableTaxValueDummy = applicableTaxValueDummy.multiply(new BigDecimal(AMENITY_PERCENTAGE_PARTIAL)
					.divide(new BigDecimal(HUNDRED)));
		} else if (amenities.equalsIgnoreCase(AMENITY_TYPE_NIL)) {
			applicableTaxValueDummy = applicableTaxValueDummy.multiply(new BigDecimal(AMENITY_PERCENTAGE_NIL)
					.divide(new BigDecimal(HUNDRED)));
		}
		return applicableTaxValueDummy;
	}

	public String multiplicativeFactorAreaWise(String propertyType, FloorImpl floor, String areaFactor,
			String propCategory) {
		String unitAreaFactor = null;

		if (propertyType.equals(PROPTYPE_RESD)) {
			/**
			 * In case of Residential Flats we have to apply the rule as (same
			 * as in case of Ground Floor for Residential House) If Floor Area
			 * is greater than 46.45sqmt apply full rate for first 46.45 sqmt,
			 * 80% for next 46.45 sqmt, 70% for next 46.45 sqmt and for
			 * remaining area exceeding 139.35 sqmt, apply 60% of full rate
			 */
			if (RESIDENTIAL_FLATS.equals(propCategory)) {
				unitAreaFactor = RESIDENTIAL_FLOOR_AREA_MAP.get("0" + STRING_SEPERATOR + areaFactor);
			} else {
				if (FLOORNO_WITH_DIFF_MULFACTOR_RESD.contains(floor.getFloorNo().toString())) {
					unitAreaFactor = RESIDENTIAL_FLOOR_AREA_MAP.get(floor.getFloorNo() + STRING_SEPERATOR + areaFactor);
				} else {
					unitAreaFactor = RESIDENTIAL_FLOOR_AREA_MAP.get("3" + STRING_SEPERATOR + areaFactor);
				}
			}
		} else if (propertyType.equals(PROPTYPE_NON_RESD)) {
			/**
			 * In case of Non Residential Flat we have to apply the rule as
			 * (same as in case of Ground Floor for Other Property Category of
			 * Non Residential Property) If built up area up to depth of first
			 * 7.62 mt. or up to first intercepting wall apply the "Full Rate"
			 * and for the remaining area, apply 50% of the full rate
			 */
			if (NONRESD_FLAT.equals(propCategory)) {
				unitAreaFactor = NON_RESIDENTIAL_FLOOR_AREA_MAP.get("0" + STRING_SEPERATOR + areaFactor);
			} else {
				if (FLOORNO_WITH_DIFF_MULFACTOR_NONRESD.contains(floor.getFloorNo().toString())) {
					unitAreaFactor = NON_RESIDENTIAL_FLOOR_AREA_MAP.get(floor.getFloorNo() + STRING_SEPERATOR
							+ areaFactor);
				} else {
					unitAreaFactor = NON_RESIDENTIAL_FLOOR_AREA_MAP.get("1" + STRING_SEPERATOR + areaFactor);
				}
			}
		}

		LOGGER.info("propertyType:" + propertyType + "; Floor No:" + floor.getFloorNo() + "; areaFactor:" + areaFactor
				+ "; unitAreaFactor: " + unitAreaFactor);
		return unitAreaFactor;

	}

	public List<ApplicableFactor> getApplicableFactorsForResidentialAndNonResidential(FloorImpl floorImpl,
			Boundary propertyArea, Installment installment, Long categoryId) {
		List<ApplicableFactor> applicableFactors = new ArrayList<ApplicableFactor>();

		// Category category = this.getCategoryForBoundary(propertyArea);

		Category category = (Category) persistenceService.findByNamedQuery(QUERY_BASERENT_BY_OCCUPANCY_AREA_STRUCTURE,
				propertyArea.getId(), floorImpl.getPropertyUsage().getId(), floorImpl.getStructureClassification()
						.getId(), installment.getFromDate());

		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		Date dateConstant = null;
		Date floorCompletionOccupation = null;
		try {
			dateConstant = dateFormatter.parse(DATE_CONSTANT);
			floorCompletionOccupation = dateFormatter.parse(floorImpl.getExtraField3());
		} catch (ParseException e) {
			LOGGER.error(e.getMessage(), e);
		}

		// Add Structural Factor

		if (floorCompletionOccupation.after(dateConstant) || floorCompletionOccupation.equals(dateConstant)) {
			ApplicableFactor applicableStructuralFactor = new ApplicableFactor();
			applicableStructuralFactor.setFactorName("SF");
			applicableStructuralFactor.setFactorIndex(floorImpl.getStructureClassification().getTypeName());
			applicableStructuralFactor.setFactorValue(new BigDecimal(Float.toString(floorImpl
					.getStructureClassification().getFactor())));
			applicableFactors.add(applicableStructuralFactor);
		} else {
			ApplicableFactor applicableStructuralFactor = new ApplicableFactor();
			applicableStructuralFactor.setFactorName("SF");
			applicableStructuralFactor.setFactorIndex(category.getCategoryName());
			applicableStructuralFactor.setFactorValue(new BigDecimal(Float.toString(category.getCategoryAmount())));
			applicableFactors.add(applicableStructuralFactor);
		}

		// Add Usage Factor

		/**
		 * if installment date is before 31-Mar-2008 apply base rent from Rent
		 * chart and exclude UF or apply base rent from base rate if floor
		 * installment date is after 1-Apr-2008
		 */
		if (installment.getToDate().after(dateConstant)) {
			ApplicableFactor applicableUsageFactor = new ApplicableFactor();
			applicableUsageFactor.setFactorName("UF");
			applicableUsageFactor.setFactorIndex(floorImpl.getPropertyUsage().getUsageName());
			applicableUsageFactor.setFactorValue(new BigDecimal(Float.toString(floorImpl.getPropertyUsage()
					.getUsagePercentage())));
			applicableFactors.add(applicableUsageFactor);
		}

		// Add Occupancy Factor
		ApplicableFactor applicableOccupancyFactor = new ApplicableFactor();
		applicableOccupancyFactor.setFactorName("OF");
		applicableOccupancyFactor.setFactorIndex(floorImpl.getPropertyOccupation().getOccupancyCode());
		applicableOccupancyFactor.setFactorValue(new BigDecimal(Float.toString(floorImpl.getPropertyOccupation()
				.getOccupancyFactor())));
		applicableFactors.add(applicableOccupancyFactor);

		// Add Age Factor

		/**
		 * if installment date is before 31-Mar-2008 apply base rent from Rent
		 * chart and exclude AF or apply base rent from base rate if floor
		 * installment date is after 1-Apr-2008
		 */
		if (installment.getToDate().after(dateConstant)) {
			ApplicableFactor applicableAgeFactor = new ApplicableFactor();
			applicableAgeFactor.setFactorName("AF");
			if (null != floorImpl.getDepreciationMaster())
				applicableAgeFactor.setFactorIndex(floorImpl.getDepreciationMaster().getDepreciationName());
			applicableAgeFactor.setFactorValue(new BigDecimal(Float.toString(floorImpl.getDepreciationMaster()
					.getDepreciationPct())));
			applicableFactors.add(applicableAgeFactor);
		}

		// Add Location Factor

		ApplicableFactor applicableLocationFactor = new ApplicableFactor();

		if (installment.getToDate().after(dateConstant)) {
			Category locationFactorCategory = (Category) persistenceService.find("from Category c where c.id = ?",
					categoryId);
			applicableLocationFactor.setFactorName("LF");
			applicableLocationFactor.setFactorIndex(locationFactorCategory.getCategoryName());
			applicableLocationFactor.setFactorValue(new BigDecimal(locationFactorCategory.getCategoryAmount())
					.setScale(2, BigDecimal.ROUND_HALF_UP));
			applicableFactors.add(applicableLocationFactor);
		} else {
			applicableLocationFactor.setFactorName("LF");
			applicableLocationFactor.setFactorIndex(category.getCategoryName());
			applicableLocationFactor.setFactorValue(new BigDecimal(Float.toString(category.getCategoryAmount())));
			applicableFactors.add(applicableLocationFactor);
		}

		return applicableFactors;

	}

	public BigDecimal calculateBaseRentPerSqMtPerMonth(List<ApplicableFactor> applicableFactors, BigDecimal baseRent) {

		BigDecimal baseRentPerMonthPerSQMT = baseRent;

		for (ApplicableFactor applicableFactor : applicableFactors) {
			baseRentPerMonthPerSQMT = baseRentPerMonthPerSQMT.multiply(applicableFactor.getFactorValue());
		}
		LOGGER.info("Base Rent Per Month Per Square Meter: "
				+ baseRentPerMonthPerSQMT.setScale(2, BigDecimal.ROUND_HALF_UP));
		return baseRentPerMonthPerSQMT.setScale(2, BigDecimal.ROUND_HALF_UP);

	}

	public BigDecimal baseRentOfUnit(FloorImpl floor, Boundary area, Installment installment,
			UnitTaxCalculationInfo unitTaxCalculationInfo) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		Date dateConstant = null;
		BigDecimal unitWiseBaseRent = BigDecimal.ZERO;
		BoundaryCategory category = null;
		// Boolean taxRuleFlag =
		// Boolean.valueOf((getAppConfigValue(PTMODULENAME,
		// APPCONFIG_TAXCALC_RULE_RENTCHART, "")));

		/**
		 * if taxRuleFlag is true then apply base rate from Rent chart till
		 * today or apply different base rate if floor occupancy date is
		 * before/after 1-Apr-2008
		 */

		/**
		 * if installment date is before 31-Mar-2008 apply base rent from Rent
		 * chart or apply base rent from base rate if floor installment date is
		 * after 1-Apr-2008
		 */
		try {
			dateConstant = dateFormatter.parse(DATE_CONSTANT);
		} catch (ParseException e) {
			LOGGER.error(e.getMessage(), e);
		}
		if (installment.getToDate().before(dateConstant)) {
			if (floor.getUnitType() != null && floor.getUnitType().getCode().equalsIgnoreCase(UNITTYPE_OPEN_PLOT)) {
				category = (BoundaryCategory) persistenceService.findByNamedQuery(QUERY_BASERENT_BY_BOUNDARY_FOR_OPENPLOT,
						area.getId(), floor.getPropertyUsage().getId(), installment.getFromDate());
			} else {
				Date dateTogetCategory = null;
				try {
					if (installment.getFromDate().before(dateFormatter.parse(floor.getExtraField3()))) {
						dateTogetCategory = dateFormatter.parse(floor.getExtraField3());
					} else {
						dateTogetCategory = installment.getToDate();
					}
				} catch (ParseException e) {
					LOGGER.error(e.getMessage(), e);
				}
				category = (BoundaryCategory) persistenceService.findByNamedQuery(QUERY_BASERENT_BY_OCCUPANCY_AREA_STRUCTURE,
						area.getId(), floor.getPropertyUsage().getId(), floor.getStructureClassification().getId(),
						dateTogetCategory);
			}
			if (category != null) {
				unitWiseBaseRent = new BigDecimal(category.getCategory().getCategoryAmount().toString());
				try {
					unitTaxCalculationInfo.setBaseRentEffectiveDate(dateFormatter.parse(dateFormatter.format(category
							.getFromDate())));
				} catch (ParseException e) {
					LOGGER.error("Error while parsing Base Rent Effective Date", e);
				}
			}
		} else {
			if (floor.getUnitType() != null && floor.getUnitType().getCode().equalsIgnoreCase(UNITTYPE_OPEN_PLOT)) {
				unitWiseBaseRent = BASERENT_FROM_APRIL2008_OPENPLOT;
			} else {
				unitWiseBaseRent = BASERENT_FROM_APRIL2008_BUILDINGS;
			}
			unitTaxCalculationInfo.setBaseRentEffectiveDate(dateConstant);
		}

		LOGGER.info("baseRentOfUnit - Installment : " + installment + " Base Rent Unit Wise: " + unitWiseBaseRent);
		return unitWiseBaseRent;
	}

	public String generateTaxCalculationXML(TaxCalculationInfo taxCalculationInfo) {
		TaxCalculationInfoXmlHandler handler = new TaxCalculationInfoXmlHandler();
		String taxCalculationInfoXML = "";

		if (taxCalculationInfo != null) {

			taxCalculationInfoXML = handler.toXML(taxCalculationInfo);

		}

		return taxCalculationInfoXML;

	}

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}
	
	/**
	 * To get effective assessment period as a String
	 * 
	 * @param date
	 *            - date for which assessment period is required
	 * @return assessment period
	 */
	public String getEffectiveAssessmentPeriod(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		StringBuffer effDate = new StringBuffer();

		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);
		if (month < 3) {
			effDate.append(EFFECTIVE_ASSESSMENT_PERIOD1).append(COMMA_STR).append(year);
		}
		if (month >= 3 && month < 6) {
			effDate.append(EFFECTIVE_ASSESSMENT_PERIOD2).append(COMMA_STR).append(year);
		}
		if (month >= 6 && month < 9) {
			effDate.append(EFFECTIVE_ASSESSMENT_PERIOD3).append(COMMA_STR).append(year);
		}
		if (month >= 9 && month < 12) {
			effDate.append(EFFECTIVE_ASSESSMENT_PERIOD4).append(COMMA_STR).append(year);
		}
		return effDate.toString();
	}

	public Date getDateFromString(String str, String format) {
		DateFormat df = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = df.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * This method retrieves the <code>CFinancialYear</code> for the given date.
	 * 
	 * @param date
	 *            an instance of <code>Date</code> for which the financial year
	 *            is to be retrieved.
	 * 
	 * @return an instance of <code></code> representing the financial year for
	 *         the given date
	 */
	public CFinancialYear getFinancialYearforDate(Date date) {
		return (CFinancialYear) persistenceService
				.getSession()
				.createQuery(
						"from CFinancialYear cfinancialyear where ? between "
								+ "cfinancialyear.startingDate and cfinancialyear.endingDate").setDate(0, date).list()
				.get(0);
	}

	public Category getCategoryForBoundary(Boundary boundary) {
		BoundaryCategoryDao boundaryCategoryDao = PropertyDAOFactory.getDAOFactory().getBoundaryCategoryDao();
		return boundaryCategoryDao.getCategoryForBoundary(boundary);

	}

	public List<Installment> getInstallmentListByStartDate(Date startDate) {

		return (List<Installment>) persistenceService.findAllByNamedQuery(QUERY_INSTALLMENTLISTBY_MODULE_AND_STARTYEAR,
				startDate, startDate, PTMODULENAME);
	}

	public EgDemandReason getDemandReasonByCodeAndInstallment(String demandReasonCode, Installment installment) {
		return (EgDemandReason) persistenceService.findByNamedQuery(QUERY_DEMANDREASONBY_CODE_AND_INSTALLMENTID,
				demandReasonCode, installment.getId());
	}

	public EgDemandReasonDetails getDemandReasonDetailsByDemandReasonId(EgDemandReason demandReason,
			BigDecimal grossAnnualRentAfterDeduction) {
		return (EgDemandReasonDetails) persistenceService.findByNamedQuery(QUERY_DEMANDREASONDETAILBY_DEMANDREASONID,
				demandReason.getId(), grossAnnualRentAfterDeduction);
	}

	public EgDemandReasonDetails getDemandReasonDetails(String demandReasonCode,
			BigDecimal grossAnnualRentAfterDeduction, Date date) {
		return (EgDemandReasonDetails) persistenceService
				.findByNamedQuery(QUERY_DEMANDREASONDETAILS_BY_DEMANDREASONID_DATE, demandReasonCode,
						grossAnnualRentAfterDeduction, date);
	}

	@SuppressWarnings("unchecked")
	public List<EgDemandReasonDetails> getDemandReasonDetails(String demandReasonCode,
			BigDecimal grossAnnualRentAfterDeduction, Installment installment) {
		return persistenceService.findAllByNamedQuery(QUERY_DEMANDREASONDETAILS_BY_DEMANDREASON_AND_INSTALLMENT,
				demandReasonCode, grossAnnualRentAfterDeduction, installment.getFromDate(), installment.getToDate());
	}

	/**
	 * Returns AppConfig Value for given key and module.Key needs to exact as in
	 * the Database,otherwise empty string will send
	 * 
	 * @param key
	 *            - Key value for which AppConfig Value is required
	 * @param moduleName
	 *            - Value for the User Id
	 * @return String.
	 */
	public String getAppConfigValue(String key, String moduleName) {
		String value = "";
		if (key != null && moduleName != null) {
			AppConfigValuesDAO appConfValDao = GenericDaoFactory.getDAOFactory().getAppConfigValuesDAO();
			AppConfigValues appConfigValues = appConfValDao.getAppConfigValueByDate(moduleName, key, new Date());
			if (appConfigValues != null) {
				value = appConfigValues.getValue();
			}
		}
		return value;
	}

	/**
	 * This method returns the currently active config value for the given
	 * module name and key
	 * 
	 * @param moduleName
	 *            a <code>String<code> representing the module name
	 * 
	 * @param key
	 *            a <code>String</code> representing the key
	 * 
	 * @param defaultValue
	 *            Default value to be returned in case the key is not defined
	 * 
	 * @return <code>String</code> representing the configuration value
	 */
	public String getAppConfigValue(String moduleName, String key, String defaultValue) {
		AppConfigValuesDAO appConfValDao = genericDao.getAppConfigValuesDAO();
		AppConfigValues appConfigValues = appConfValDao.getAppConfigValueByDate(moduleName, key, new Date());
		return appConfigValues == null ? defaultValue : appConfigValues.getValue();
	}

	public String getFloorStr(Integer flrNo) {
		String flrNoStr = "";
		if (flrNo != null && (flrNo >= -5 && flrNo <= 50)) {
			flrNoStr = FLOOR_MAP.get(flrNo);
		}
		LOGGER.info("Floor Number Text: " + flrNoStr);
		return flrNoStr;
	}

	public TaxCalculationInfo getTaxCalInfo(Ptdemand demand) {
		TaxCalculationInfo taxCalInfo = null;
		TaxCalculationInfoXmlHandler handler = new TaxCalculationInfoXmlHandler();
		PTDemandCalculations ptDmdCalc = demand.getDmdCalculations();
		if (ptDmdCalc.getTaxInfo() != null) {
			String xmlString = new String(ptDmdCalc.getTaxInfo());
			LOGGER.debug("TaxCalculationInfo XML : " + xmlString);
			taxCalInfo = (TaxCalculationInfo) handler.toObject(xmlString);
			if (taxCalInfo.getIndexNo() == null) {
				taxCalInfo.setIndexNo(demand.getEgptProperty().getBasicProperty().getUpicNo());
			}
		}
		return taxCalInfo;
	}

	public static BigDecimal roundOffTax(BigDecimal tax) {
		return EgovUtils.roundOff(tax);
	}

	/**
	 * Returns UnitTaxCalculationInfo with updated montly rent based on the
	 * occupancy date of the unit i.e., if unit is occupied in the first quarter
	 * apply full rate, if occupied in second quarter apply 3/4 rate, if
	 * occupied in third quarter then apply 1/2 rate and in last quarter apply
	 * 1/4 rate
	 * 
	 * 
	 * @param unitTaxCalculationInfo
	 *            UnitTaxCalculationInfo for which the tax has to be calculated
	 * @param installment
	 *            Installment for which tax has to be calculated
	 * @return UnitTaxCalculationInfo UnitTaxCalculationInfo with update Monthly
	 *         Tax
	 */
	public UnitTaxCalculationInfo calculateTaxPayableByOccupancyDate(UnitTaxCalculationInfo unitTaxCalculationInfo,
			Installment installment) {
		if ((unitTaxCalculationInfo.getOccpancyDate().after(installment.getFromDate()) || unitTaxCalculationInfo
				.getOccpancyDate().equals(installment.getFromDate()))
				&& (unitTaxCalculationInfo.getOccpancyDate().before(installment.getToDate()) || unitTaxCalculationInfo
						.getOccpancyDate().equals(installment.getFromDate()))) {
			Date occupancyDate = unitTaxCalculationInfo.getOccpancyDate();
			SimpleDateFormat monthFormatter = new SimpleDateFormat("MM");
			String month = monthFormatter.format(occupancyDate);
			BigDecimal totalTaxPayableByOccupancyDate = BigDecimal.ZERO;
			LOGGER.info("occupance date month " + month);

			if (Integer.valueOf(month) >= Integer.valueOf(7) && Integer.valueOf(month) <= Integer.valueOf(9)) {
				for (MiscellaneousTax miscellaneousTax : unitTaxCalculationInfo.getMiscellaneousTaxes()) {
					BigDecimal totalTax = BigDecimal.ZERO;
					for (MiscellaneousTaxDetail miscTaxDetail : miscellaneousTax.getTaxDetails()) {
						miscTaxDetail.setCalculatedTaxValue(miscTaxDetail.getCalculatedTaxValue().multiply(
								new BigDecimal(".75")));
						totalTaxPayableByOccupancyDate = totalTaxPayableByOccupancyDate.add(miscTaxDetail
								.getCalculatedTaxValue());
						totalTax = totalTax.add(miscTaxDetail.getCalculatedTaxValue());
						LOGGER.debug("appyling .75% for month from 7 t0 9 " + miscTaxDetail.getCalculatedTaxValue());
					}
					miscellaneousTax.setTotalCalculatedTax(totalTax.setScale(0, BigDecimal.ROUND_HALF_UP));
				}
				unitTaxCalculationInfo.setTotalTaxPayable(totalTaxPayableByOccupancyDate.setScale(2,
						BigDecimal.ROUND_HALF_UP));
			} else if (Integer.valueOf(month) >= Integer.valueOf(10) && Integer.valueOf(month) <= Integer.valueOf(12)) {
				for (MiscellaneousTax miscellaneousTax : unitTaxCalculationInfo.getMiscellaneousTaxes()) {
					BigDecimal totalTax = BigDecimal.ZERO;
					for (MiscellaneousTaxDetail miscTaxDetail : miscellaneousTax.getTaxDetails()) {
						miscTaxDetail.setCalculatedTaxValue(miscTaxDetail.getCalculatedTaxValue().multiply(
								new BigDecimal(".5")));
						totalTaxPayableByOccupancyDate = totalTaxPayableByOccupancyDate.add(miscTaxDetail
								.getCalculatedTaxValue());
						totalTax = totalTax.add(miscTaxDetail.getCalculatedTaxValue());
						LOGGER.debug("appyling .5% for month from 10 t0 12 " + miscTaxDetail.getCalculatedTaxValue());
					}
					miscellaneousTax.setTotalCalculatedTax(totalTax.setScale(0, BigDecimal.ROUND_HALF_UP));
				}

				unitTaxCalculationInfo.setTotalTaxPayable(totalTaxPayableByOccupancyDate.setScale(2,
						BigDecimal.ROUND_HALF_UP));
			} else if (Integer.valueOf(month) >= Integer.valueOf(1) && Integer.valueOf(month) <= Integer.valueOf(3)) {
				for (MiscellaneousTax miscellaneousTax : unitTaxCalculationInfo.getMiscellaneousTaxes()) {
					BigDecimal totalTax = BigDecimal.ZERO;
					for (MiscellaneousTaxDetail miscTaxDetail : miscellaneousTax.getTaxDetails()) {
						miscTaxDetail.setCalculatedTaxValue(miscTaxDetail.getCalculatedTaxValue().multiply(
								new BigDecimal(".25")));
						totalTaxPayableByOccupancyDate = totalTaxPayableByOccupancyDate.add(miscTaxDetail
								.getCalculatedTaxValue());
						totalTax = totalTax.add(miscTaxDetail.getCalculatedTaxValue());
						LOGGER.debug("appyling .25% for month from 1 t0 3 " + miscTaxDetail.getCalculatedTaxValue());
					}
					miscellaneousTax.setTotalCalculatedTax(totalTax.setScale(0, BigDecimal.ROUND_HALF_UP));
				}
				unitTaxCalculationInfo.setTotalTaxPayable(totalTaxPayableByOccupancyDate.setScale(2,
						BigDecimal.ROUND_HALF_UP));
			}
		}
		LOGGER.debug("tax payable by occupancy road " + unitTaxCalculationInfo.getTotalTaxPayable());
		return unitTaxCalculationInfo;

	}

	/**
	 * Called to get reason wise demand dues for arrears and current
	 * 
	 * @param propertyId
	 * @return Map of String and Map for reason wise demand dues
	 */

	public Map<String, Map<String, BigDecimal>> getDemandDues(String propertyId) {
		Map<String, Map<String, BigDecimal>> demandDues = new HashMap<String, Map<String, BigDecimal>>();
		List list = new ArrayList();
		DemandGenericHibDao dmdGen = new DemandGenericHibDao();

		ModuleDao moduleDao = GenericDaoFactory.getDAOFactory().getModuleDao();
		InstallmentDao isntalDao = CommonsDaoFactory.getDAOFactory().getInstallmentDao();
		PtDemandDao ptDemandDao = PropertyDAOFactory.getDAOFactory().getPtDemandDao();
		BasicPropertyDAO basicPropertyDAO = PropertyDAOFactory.getDAOFactory().getBasicPropertyDAO();

		BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(propertyId);
		EgDemand egDemand = ptDemandDao.getNonHistoryCurrDmdForProperty(basicProperty.getProperty());
		Module module = moduleDao.getModuleByName(PropertyTaxConstants.PTMODULENAME);
		Installment currentInstall = isntalDao.getInsatllmentByModuleForGivenDate(module, new Date());

		list = dmdGen.getReasonWiseDCB(egDemand, module);

		Map<String, BigDecimal> arrTaxSum = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> currTaxSum = new HashMap<String, BigDecimal>();

		arrTaxSum = initReasonsMap(arrTaxSum);
		currTaxSum = initReasonsMap(currTaxSum);

		for (Object record : list) {
			Object[] data = (Object[]) record;
			if (data[1].toString().compareTo(currentInstall.toString()) < 0) {
				arrTaxSum = populateReasonsSum(data, arrTaxSum);
			} else {
				currTaxSum = populateReasonsSum(data, currTaxSum);
			}
		}

		demandDues.put(ARREARS_DMD, arrTaxSum);
		demandDues.put(CURRENT_DMD, currTaxSum);

		return demandDues;

	}

	/**
	 * Called locally to get sum of demand reasons by reason wise
	 * 
	 * @param data
	 *            a record with reason code, year, amount and amount collected
	 * @param taxSum
	 *            for reason wise sum
	 * @return Map of reason wise sum
	 */

	private Map<String, BigDecimal> populateReasonsSum(Object[] data, Map<String, BigDecimal> taxSum) {
		BigDecimal tmpVal;
		if ((data[0].toString()).equals(DEMANDRSN_CODE_GENERAL_TAX)) {
			tmpVal = taxSum.get(DEMANDRSN_CODE_GENERAL_TAX);
			//considering rebate as collection and substracting it.
			taxSum.put(DEMANDRSN_CODE_GENERAL_TAX,
					tmpVal.add(((BigDecimal) data[2]).subtract(((BigDecimal) data[3])).subtract((BigDecimal) data[4])));
		} else if ((data[0].toString()).equals(DEMANDRSN_CODE_SEWERAGE_TAX)) {
			tmpVal = taxSum.get(DEMANDRSN_CODE_SEWERAGE_TAX);
			taxSum.put(DEMANDRSN_CODE_SEWERAGE_TAX, tmpVal.add(((BigDecimal) data[2]).subtract(((BigDecimal) data[3]))));
		} else if ((data[0].toString()).equals(DEMANDRSN_CODE_FIRE_SERVICE_TAX)) {
			tmpVal = taxSum.get(DEMANDRSN_CODE_FIRE_SERVICE_TAX);
			taxSum.put(DEMANDRSN_CODE_FIRE_SERVICE_TAX,
					tmpVal.add(((BigDecimal) data[2]).subtract(((BigDecimal) data[3]))));
		} else if ((data[0].toString()).equals(DEMANDRSN_CODE_LIGHTINGTAX)) {
			tmpVal = taxSum.get(DEMANDRSN_CODE_LIGHTINGTAX);
			taxSum.put(DEMANDRSN_CODE_LIGHTINGTAX, tmpVal.add(((BigDecimal) data[2]).subtract(((BigDecimal) data[3]))));
		} else if ((data[0].toString()).equals(DEMANDRSN_CODE_GENERAL_WATER_TAX)) {
			tmpVal = taxSum.get(DEMANDRSN_CODE_GENERAL_WATER_TAX);
			taxSum.put(DEMANDRSN_CODE_GENERAL_WATER_TAX,
					tmpVal.add(((BigDecimal) data[2]).subtract(((BigDecimal) data[3]))));
		} else if ((data[0].toString()).equals(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD)) {
			tmpVal = taxSum.get(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD);
			taxSum.put(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD,
					tmpVal.add(((BigDecimal) data[2]).subtract(((BigDecimal) data[3]))));
		} else if ((data[0].toString()).equals(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD)) {
			tmpVal = taxSum.get(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD);
			taxSum.put(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD,
					tmpVal.add(((BigDecimal) data[2]).subtract(((BigDecimal) data[3]))));
		} else if ((data[0].toString()).equals(DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX)) {
			tmpVal = taxSum.get(DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX);
			taxSum.put(DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX,
					tmpVal.add(((BigDecimal) data[2]).subtract(((BigDecimal) data[3]))));
		} else if ((data[0].toString()).equals(DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX)) {
			tmpVal = taxSum.get(DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX);
			taxSum.put(DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX,
					tmpVal.add(((BigDecimal) data[2]).subtract(((BigDecimal) data[3]))));
		}

		return taxSum;
	}

	/**
	 * Called locally to initialize
	 * 
	 * @param taxSum
	 *            Map of demand reasons
	 * @return Map with demand reasons initialized
	 */
	private Map<String, BigDecimal> initReasonsMap(Map<String, BigDecimal> taxSum) {

		taxSum.put(DEMANDRSN_CODE_GENERAL_TAX, BigDecimal.ZERO);
		taxSum.put(DEMANDRSN_CODE_SEWERAGE_TAX, BigDecimal.ZERO);
		taxSum.put(DEMANDRSN_CODE_FIRE_SERVICE_TAX, BigDecimal.ZERO);
		taxSum.put(DEMANDRSN_CODE_LIGHTINGTAX, BigDecimal.ZERO);
		taxSum.put(DEMANDRSN_CODE_GENERAL_WATER_TAX, BigDecimal.ZERO);
		taxSum.put(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD, BigDecimal.ZERO);
		taxSum.put(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD, BigDecimal.ZERO);
		taxSum.put(DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX, BigDecimal.ZERO);
		taxSum.put(DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX, BigDecimal.ZERO);

		return taxSum;
	}

	/**
	 * This method returns the User instance associated with the logged in user
	 * 
	 * @param sessionMap
	 *            Map of session variables
	 * @return the logged in user
	 */
	public User getLoggedInUser(Map<String, Object> sessionMap) {
		return userService.getUserByUserName((String) sessionMap.get(SESSION_VAR_LOGIN_USER_NAME));
	}

	/**
	 * @param user
	 *            the user whose department is to be returned
	 * @return department of the given user
	 */
	public Department getDepartmentOfUser(User user) {
		PersonalInformation empForUserId = employeeService.getEmpForUserId(user.getId());
		Assignment assignmentByEmpAndDate = employeeService.getAssignmentByEmpAndDate(new Date(),
				empForUserId.getIdPersonalInformation());
		return assignmentByEmpAndDate.getDeptId();
	}

	public HashMap<String, Integer> generateOrderForDemandDetails(Set<EgDemandDetails> demandDetails) {

		HashMap<Integer, String> instReasonMap = new HashMap<Integer, String>();
		HashMap<String, Integer> orderMap = new HashMap<String, Integer>();
		BigDecimal balance = BigDecimal.ZERO;
		String key = "";
		for (EgDemandDetails demandDetail : demandDetails) {
			balance = BigDecimal.ZERO;
			balance = demandDetail.getAmount().subtract(demandDetail.getAmtCollected());
			if (balance.compareTo(BigDecimal.ZERO) == 1) {
				EgDemandReason reason = demandDetail.getEgDemandReason();
				Installment installment = reason.getEgInstallmentMaster();
				Calendar cal = Calendar.getInstance();
				cal.setTime(installment.getInstallmentYear());
				if (demandDetail.getEgDemand().getEgInstallmentMaster().equals(installment)
						&& reason.getEgDemandReasonMaster().getCode().equals(DEMANDRSN_CODE_GENERAL_TAX)
						&& demandDetail.getAmtCollected().compareTo(BigDecimal.ZERO) == 0) {

					key = Integer.valueOf(
							getOrder(cal.get(Calendar.YEAR), DEMAND_REASON_ORDER_MAP.get(DEMANDRSN_REBATE))).toString();
					instReasonMap.put(new Integer(key), cal.get(Calendar.YEAR) + "-" + DEMANDRSN_REBATE);
					key = Integer
							.valueOf(
									getOrder(cal.get(Calendar.YEAR),
											DEMAND_REASON_ORDER_MAP.get(reason.getEgDemandReasonMaster().getCode())
													.intValue())).toString();
					instReasonMap.put(new Integer(key), cal.get(Calendar.YEAR) + "-"
							+ reason.getEgDemandReasonMaster().getCode());
				} else {

					key = Integer
							.valueOf(
									getOrder(cal.get(Calendar.YEAR),
											DEMAND_REASON_ORDER_MAP.get(DEMANDRSN_CODE_PENALTY_FINES))).toString();
					instReasonMap.put(new Integer(key), cal.get(Calendar.YEAR) + "-" + DEMANDRSN_CODE_PENALTY_FINES);
					key = Integer
							.valueOf(
									getOrder(cal.get(Calendar.YEAR),
											DEMAND_REASON_ORDER_MAP.get(reason.getEgDemandReasonMaster().getCode())
													.intValue())).toString();
					instReasonMap.put(new Integer(key), cal.get(Calendar.YEAR) + "-"
							+ reason.getEgDemandReasonMaster().getCode());
				}
			}
		}
		SortedSet<Integer> instYrSortedSet = new TreeSet<Integer>(instReasonMap.keySet());
		int order = 1;
		for (Integer i : instYrSortedSet) {
			orderMap.put(instReasonMap.get(i).toString(), order);
			order++;
		}
		return orderMap;

	}

	/**
	 * @param sessionMap
	 *            map of session variables
	 * 
	 * @return departments of currently logged in user
	 */
	public List<Department> getDepartmentsForLoggedInUser(Map<String, Object> sessionMap) {
		Department dept = getDepartmentOfUser(getLoggedInUser(sessionMap));
		List<Department> departments = persistenceService.findAllByNamedQuery(QUERY_DEPARTMENTS_BY_DEPTCODE,
				dept.getDeptCode());
		return departments;
	}

	public DesignationMaster getDesignationForUser(Integer userId) {
		Position position = null;
		DesignationMaster designation = null;
		EisCommonsService eisCommonMgr = new EisCommonsServiceImpl();
		if (userId != null && userId.intValue() != 0) {
			position = eisCommonMgr.getPositionByUserId(userId);
			designation = position.getDeptDesigId().getDesigId();
		}
		return designation;
	}

	public EgBillType getBillTypeByCode(String typeCode) {
		EgBillDao egBillDao = DCBHibernateDaoFactory.getDaoFactory().getEgBillDao();
		EgBillType billType = egBillDao.getBillTypeByCode(typeCode);
		return billType;
	}

	public int getOrder(int year, int reasonOrder) {
		// Reason Order is zero for Cheque Bounce Penalty
		return (reasonOrder == 0) ? (year + (year - 2005) * 20 + reasonOrder)
				: (year + (year - 1990) * 20 + reasonOrder);
	}

	/**
	 * Prepares a map of installment and respective demand for each installment
	 * 
	 * @param property
	 * @return Map of installment and respective reason wise demand for each
	 *         installment
	 */
	public Map<Installment, BigDecimal> prepareRsnWiseDemandForProp(Property property) {
		Installment inst = null;
		Map<Installment, BigDecimal> instAmountMap = new HashMap<Installment, BigDecimal>();
		PtDemandDao ptDemandDao = PropertyDAOFactory.getDAOFactory().getPtDemandDao();
		EgDemand egDemand = ptDemandDao.getNonHistoryCurrDmdForProperty(property);
		String demandReason = "";
		BigDecimal amount = BigDecimal.ZERO;
		for (EgDemandDetails dmdDet : egDemand.getEgDemandDetails()) {
			amount = BigDecimal.ZERO;
			demandReason = dmdDet.getEgDemandReason().getEgDemandReasonMaster().getCode();
			if (!demandReason.equals(DEMANDRSN_CODE_PENALTY_FINES)) {
				inst = dmdDet.getEgDemandReason().getEgInstallmentMaster();
				if (instAmountMap.get(inst) == null) {
					instAmountMap.put(inst, dmdDet.getAmount());
				} else {
					amount = instAmountMap.get(inst);
					amount = amount.add(dmdDet.getAmount());
					instAmountMap.put(inst, amount);
				}
			}
		}
		return instAmountMap;
	}

	/**
	 * Prepares a map of installment and respective collection for each
	 * installment
	 * 
	 * @param property
	 * @return Map of installment and respective reason wise demand for each
	 *         installment
	 */
	public Map<Installment, BigDecimal> prepareRsnWiseCollForProp(Property property) {
		Installment inst = null;
		Map<Installment, BigDecimal> instCollMap = new HashMap<Installment, BigDecimal>();
		PtDemandDao ptDemandDao = PropertyDAOFactory.getDAOFactory().getPtDemandDao();
		EgDemand egDemand = ptDemandDao.getNonHistoryCurrDmdForProperty(property);
		String demandReason = "";
		BigDecimal amount = BigDecimal.ZERO;
		BigDecimal collection = BigDecimal.ZERO;
		for (EgDemandDetails dmdDet : egDemand.getEgDemandDetails()) {
			amount = BigDecimal.ZERO;
			collection = dmdDet.getAmtCollected();
			demandReason = dmdDet.getEgDemandReason().getEgDemandReasonMaster().getCode();
			if (!demandReason.equals(DEMANDRSN_CODE_PENALTY_FINES)) {
				inst = dmdDet.getEgDemandReason().getEgInstallmentMaster();
				if (instCollMap.get(inst) == null) {
					instCollMap.put(inst, collection);
				} else {
					amount = instCollMap.get(inst);
					amount = amount.add(collection);
					instCollMap.put(inst, amount);
				}
			}
		}
		return instCollMap;
	}

	@SuppressWarnings("serial")
	public void consolidateUnitTaxCalcInfos(TaxCalculationInfo taxCalcInfo, Installment installment,
			String propertyTypeCategory, String amenities, Property property, Set<String> applicableTaxes) throws ParseException {
		LOGGER.debug("Entered into consolidateUnitTaxCalcInfo");

		taxCalcInfo.setTotalTaxPayable(ZERO);
		
		// Used to group UnitTaxCalculationInfo's by UnitNo
		Map<Integer, List<UnitTaxCalculationInfo>> unitTaxesByUnitNo = new TreeMap<Integer, List<UnitTaxCalculationInfo>>();

		// holds the consolidated UnitTaxCalculationInfo for UnitNo
		Map<Integer, UnitTaxCalculationInfo> consolidatedUnitTaxForUnitNo = new TreeMap<Integer, UnitTaxCalculationInfo>();
		List<UnitTaxCalculationInfo> unitTaxes = taxCalcInfo.getUnitTaxCalculationInfo();
		Collections.sort(unitTaxes, UnitTaxInfoComparator.getUnitComparator(UnitTaxInfoComparator.UNIT_SORT));

		// holds the unit numbers which are consolidated
		Set<Integer> consolidatedUnitNos = new TreeSet<Integer>();
		Map<Integer, Map<String, BigDecimal>> unitNoWiseTaxAndALV = new TreeMap<Integer, Map<String, BigDecimal>>();

		// Does the grouping of UnitTaxCalculationInfo's by UnitNo
		for (final UnitTaxCalculationInfo unitTax : unitTaxes) {

			if (unitTaxesByUnitNo.get(unitTax.getUnitNumber()) == null) {
				unitTaxesByUnitNo.put(unitTax.getUnitNumber(), new ArrayList<UnitTaxCalculationInfo>() {
					{
						add(unitTax);
					}
				});
			} else {
				unitTaxesByUnitNo.get(unitTax.getUnitNumber()).add(unitTax);
			}
		}

		LOGGER.debug("Unique unit numbers: " + unitTaxesByUnitNo.keySet());

		Date unitDate = null;
		Date consolidatedUnitDate = null;
		DateFormat dateFormat = new SimpleDateFormat(NMCPTISConstants.DATE_FORMAT_DDMMYYY);

		for (Integer unitNo : unitTaxesByUnitNo.keySet()) {
			List<UnitTaxCalculationInfo> units = unitTaxesByUnitNo.get(unitNo);

			UnitTaxCalculationInfo consolidating = null;
			Map<String, BigDecimal> taxNameAndALV = new TreeMap<String, BigDecimal>();

			for (UnitTaxCalculationInfo unit : units) {
				if (consolidatedUnitTaxForUnitNo.get(unitNo) == null) {
					UnitTaxCalculationInfo unitClone = getUnitTaxCalculationInfoClone(unit);
					prepareTaxNameAndALV(taxNameAndALV, unitClone);
					consolidatedUnitTaxForUnitNo.put(unitNo, unitClone);
				} else {
					consolidatedUnitNos.add(unitNo);
					consolidating = consolidatedUnitTaxForUnitNo.get(unitNo);

					unitDate = dateFormat.parse(unit.getInstDate());
					consolidatedUnitDate = dateFormat.parse(consolidating.getInstDate());
					
					if (unitDate.before(consolidatedUnitDate)) {
						consolidating.setInstDate(unit.getInstDate());
					}

					if (consolidating.getUnitArea() != null) {
						consolidating.setUnitArea(unit.getUnitArea() != null ? consolidating.getUnitArea().add(
								unit.getUnitArea()) : consolidating.getUnitArea().add(ZERO));
					} else {
						consolidating.setUnitArea(unit.getUnitArea() != null ? ZERO.add(unit.getUnitArea()) : ZERO);
					}
					if (consolidating.getBaseRent() != null) {
						consolidating.setBaseRent(unit.getBaseRent() != null ? consolidating.getBaseRent().add(
								unit.getBaseRent()) : consolidating.getBaseRent().add(ZERO));
					} else {
						consolidating.setBaseRent(unit.getBaseRent() != null ? ZERO.add(unit.getBaseRent()) : ZERO);
					}

					consolidating.setBaseRentEffectiveDate(new Date(unit.getBaseRentEffectiveDate().getTime()));

					if (consolidating.getBaseRentPerSqMtPerMonth() != null) {
						consolidating
								.setBaseRentPerSqMtPerMonth(unit.getBaseRentPerSqMtPerMonth() != null ? consolidating
										.getBaseRentPerSqMtPerMonth().add(unit.getBaseRentPerSqMtPerMonth())
										: consolidating.getBaseRentPerSqMtPerMonth().add(ZERO));
					} else {
						consolidating.setBaseRentPerSqMtPerMonth(unit.getBaseRentPerSqMtPerMonth() != null ? ZERO
								.add(unit.getBaseRentPerSqMtPerMonth()) : ZERO);
					}
					if (consolidating.getMonthlyRent() != null) {
						consolidating.setMonthlyRent(unit.getMonthlyRent() != null ? consolidating.getMonthlyRent()
								.add(unit.getMonthlyRent()) : consolidating.getMonthlyRent().add(ZERO));
					} else {
						consolidating.setMonthlyRent(unit.getMonthlyRent() != null ? ZERO.add(unit.getMonthlyRent())
								: ZERO);
					}
					if (consolidating.getAnnualRentBeforeDeduction() != null) {
						consolidating
								.setAnnualRentBeforeDeduction(unit.getAnnualRentBeforeDeduction() != null ? consolidating
										.getAnnualRentBeforeDeduction().add(unit.getAnnualRentBeforeDeduction())
										: consolidating.getAnnualRentBeforeDeduction().add(ZERO));
					} else {
						consolidating.setAnnualRentBeforeDeduction(unit.getAnnualRentBeforeDeduction() != null ? ZERO
								.add(unit.getAnnualRentBeforeDeduction()) : ZERO);
					}
					if (consolidating.getTotalTaxPayable() != null) {
						consolidating.setTotalTaxPayable(unit.getTotalTaxPayable() != null ? consolidating
								.getTotalTaxPayable().add(unit.getTotalTaxPayable()) : consolidating
								.getTotalTaxPayable().add(ZERO));
					} else {
						consolidating.setTotalTaxPayable(unit.getTotalTaxPayable() != null ? ZERO.add(unit
								.getTotalTaxPayable()) : ZERO);
					}

					consolidating.setAnnualRentAfterDeduction(consolidating.getAnnualRentAfterDeduction().add(
							unit.getAnnualRentAfterDeduction()));
					
					for (MiscellaneousTax mt : unit.getMiscellaneousTaxes()) {
						if (taxNameAndALV.get(mt.getTaxName()) == null) {
							MiscellaneousTax mtx = new MiscellaneousTax(mt);
							
							for (MiscellaneousTaxDetail mtd : mt.getTaxDetails()) {
								MiscellaneousTaxDetail txDetail = new MiscellaneousTaxDetail(mtd);
								mtx.getTaxDetails().add(txDetail);
							}
							
							consolidating.addMiscellaneousTaxes(mtx);
						}
					}
					
					prepareTaxNameAndALV(taxNameAndALV, unit);

					addAreaTaxCalculationInfosClone(unit, consolidating);
					consolidatedUnitTaxForUnitNo.put(unitNo, consolidating);
				}
				unitNoWiseTaxAndALV.put(unitNo, taxNameAndALV);
			}
		}

		LOGGER.info("Consolidated Unit Nos: " + consolidatedUnitNos);
		Set<Integer> unitNos = (consolidatedUnitNos.isEmpty()) ? unitTaxesByUnitNo.keySet() : consolidatedUnitNos;
		LOGGER.debug("Unit Nos: " + unitNos);

		for (Integer unitNo : unitNos) {

			UnitTaxCalculationInfo unitTaxCalcInfo = consolidatedUnitTaxForUnitNo.get(unitNo);
			
			applicableTaxes.clear();
			
			for (MiscellaneousTax tax : unitTaxCalcInfo.getMiscellaneousTaxes()) {
				for (MiscellaneousTaxDetail taxDetail : tax.getTaxDetails()) {
					if (taxDetail.getIsHistory() == null || taxDetail.getIsHistory().equals('N')) {
						applicableTaxes.add(tax.getTaxName());
					}
				}
			}
			
			unitTaxCalcInfo.getMiscellaneousTaxes().clear();
			calculateApplicableTaxes(new ArrayList<String>(applicableTaxes), unitNoWiseTaxAndALV.get(unitNo), unitTaxCalcInfo, installment,
					property.getPropertyDetail().getPropertyTypeMaster().getCode(), propertyTypeCategory, amenities, property);
			
			//calculateTaxPayableByOccupancyDate(unitTaxCalcInfo, installment);

			if (unitNoWiseTaxAndALV.get(unitNo).containsKey(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD)
					&& unitNoWiseTaxAndALV.get(unitNo).containsKey(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD)) {
				setResdAndNonResdParts(unitNoWiseTaxAndALV.get(unitNo), unitTaxCalcInfo);
			}
			taxCalcInfo.setTotalTaxPayable(taxCalcInfo.getTotalTaxPayable().add(unitTaxCalcInfo.getTotalTaxPayable()));
		}

		taxCalcInfo.getConsolidatedUnitTaxCalculationInfo().addAll(consolidatedUnitTaxForUnitNo.values());

		LOGGER.debug("Exiting from consolidateUnitTaxCalcInfo");
	}

	private void prepareTaxNameAndALV(Map<String, BigDecimal> taxNameAndALV, UnitTaxCalculationInfo unit) {
		LOGGER.debug("Entered into prepareTaxNameAndALV");
		LOGGER.debug("prepareTaxNameAndALV - Inputs: taxNameAndALV: " + taxNameAndALV);

		for (MiscellaneousTax miscTax : unit.getMiscellaneousTaxes()) {
			for (MiscellaneousTaxDetail taxDetail : miscTax.getTaxDetails()) {
				if (taxDetail.getIsHistory() == null || taxDetail.getIsHistory().equals('N')) {
					if (taxNameAndALV.get(miscTax.getTaxName()) == null) {
						taxNameAndALV.put(miscTax.getTaxName(), unit.getAnnualRentAfterDeduction());
					} else {
						taxNameAndALV.put(miscTax.getTaxName(),
								taxNameAndALV.get(miscTax.getTaxName()).add(unit.getAnnualRentAfterDeduction()));
					}
					break;
				}
			}
		}

		LOGGER.debug("prepareTaxNameAndALV - afterPrepare taxNameAndALV: " + taxNameAndALV);
		LOGGER.debug("Exiting from prepareTaxNameAndALV");
	}

	public UnitTaxCalculationInfo getUnitTaxCalculationInfoClone(UnitTaxCalculationInfo unit) {
		LOGGER.debug("Entered into getUnitTaxCalculationInfoClone");
		UnitTaxCalculationInfo clone = new UnitTaxCalculationInfo();

		clone.setFloorNumber(unit.getFloorNumber());
		clone.setUnitNumber(unit.getUnitNumber());
		clone.setUnitArea(unit.getUnitArea());
		clone.setUnitOccupation(unit.getUnitOccupation());
		clone.setUnitUsage(unit.getUnitUsage());
		clone.setBaseRent(unit.getBaseRent());
		clone.setBaseRentPerSqMtPerMonth(unit.getBaseRentPerSqMtPerMonth());
		clone.setMonthlyRent(unit.getMonthlyRent());
		clone.setAnnualRentBeforeDeduction(unit.getAnnualRentBeforeDeduction());
		clone.setAnnualRentAfterDeduction(unit.getAnnualRentAfterDeduction());
		clone.setTotalTaxPayable(unit.getTotalTaxPayable());
		clone.setMonthlyRentPaidByTenant(unit.getMonthlyRentPaidByTenant());
		clone.setOccpancyDate(new Date(unit.getOccpancyDate().getTime()));
		clone.setEffectiveAssessmentDate(unit.getEffectiveAssessmentDate());
		clone.setFloorNumberInteger(unit.getFloorNumberInteger());
		clone.setUnitOccupier(unit.getUnitOccupier());
		clone.setInstDate(unit.getInstDate());
		clone.setResidentialALV(unit.getResidentialALV());
		clone.setNonResidentialALV(unit.getNonResidentialALV());
		clone.setResdEduCess(unit.getResdEduCess());
		clone.setNonResdEduCess(unit.getNonResdEduCess());
		clone.setEgCess(unit.getEgCess());
		
		if (unit.getBaseRentEffectiveDate() != null) {
			clone.setBaseRentEffectiveDate(new Date(unit.getBaseRentEffectiveDate().getTime()));
		}

		if (unit.getHasALVChanged() == null) {
			clone.setHasALVChanged(Boolean.FALSE);
		} else {
			clone.setHasALVChanged(unit.getHasALVChanged());
		}

		addMiscellaneousTaxesClone(unit, clone);

		addAreaTaxCalculationInfosClone(unit, clone);
		LOGGER.debug("Exiting from getUnitTaxCalculationInfoClone");
		return clone;

	}

	/**
	 * Returns TaxCalculationInfo clone
	 * 
	 * @param taxCalInfo
	 * @return
	 */

	public TaxCalculationInfo getTaxCalculationInfoClone(TaxCalculationInfo taxCalInfo) {
		TaxCalculationInfo clone = new TaxCalculationInfo();
		clone.setArea(taxCalInfo.getArea());
		clone.setHouseNumber(taxCalInfo.getHouseNumber());
		clone.setIndexNo(taxCalInfo.getIndexNo());
		clone.setParcelId(taxCalInfo.getParcelId());
		clone.setPropertyAddress(taxCalInfo.getPropertyAddress());
		clone.setPropertyArea(taxCalInfo.getPropertyArea());
		clone.setPropertyOwnerName(taxCalInfo.getPropertyOwnerName());
		clone.setPropertyType(taxCalInfo.getPropertyType());
		clone.setTaxCalculationInfoXML(taxCalInfo.getTaxCalculationInfoXML());
		clone.setTotalAnnualLettingValue(taxCalInfo.getTotalAnnualLettingValue());
		clone.setTotalTaxPayable(taxCalInfo.getTotalTaxPayable());
		clone.setWard(taxCalInfo.getWard());
		clone.setZone(taxCalInfo.getZone());

		addUnitTaxCalculationInfoClone(taxCalInfo, clone);
		addConsolidatedUnitTaxCalculationInfoClone(taxCalInfo, clone);
		addConsolidatedUnitTaxCalReportClone(taxCalInfo, clone);
		return clone;
	}

	private void addConsolidatedUnitTaxCalReportClone(TaxCalculationInfo taxCalInfo, TaxCalculationInfo clone) {
		for (ConsolidatedUnitTaxCalReport unitTaxCalReport : taxCalInfo.getConsolidatedUnitTaxCalReportList()) {
			ConsolidatedUnitTaxCalReport unitTaxCalReportClone = getConsolidatedUnitTaxCalReportColne(unitTaxCalReport);
			clone.addConsolidatedUnitTaxCalReportList(unitTaxCalReportClone);
		}
	}

	/**
	 * Returns the ConsolidatedUnitTaxCalReport clone
	 * 
	 * @param unitTaxCalReport
	 * @return
	 */
	private ConsolidatedUnitTaxCalReport getConsolidatedUnitTaxCalReportColne(
			ConsolidatedUnitTaxCalReport unitTaxCalReport) {
		ConsolidatedUnitTaxCalReport newUnitTaxCalReport = new ConsolidatedUnitTaxCalReport();
		newUnitTaxCalReport.setAnnualLettingValue(unitTaxCalReport.getAnnualLettingValue());
		newUnitTaxCalReport.setAnnualRentBeforeDeduction(unitTaxCalReport.getAnnualRentBeforeDeduction());
		newUnitTaxCalReport.setDeductionAmount(unitTaxCalReport.getDeductionAmount());
		newUnitTaxCalReport.setMonthlyRent(unitTaxCalReport.getMonthlyRent());
		addUnitTaxInfoClone(unitTaxCalReport, newUnitTaxCalReport);
		return newUnitTaxCalReport;
	}

	/**
	 * Adds the UnitTaxInfo clones to newUnitTaxCalReport
	 * 
	 * @param unitTaxCalReport
	 * @param newUnitTaxCalReport
	 */
	private void addUnitTaxInfoClone(ConsolidatedUnitTaxCalReport unitTaxCalReport,
			ConsolidatedUnitTaxCalReport newUnitTaxCalReport) {
		for (UnitTaxCalculationInfo unitInfo : unitTaxCalReport.getUnitTaxCalInfo()) {
			UnitTaxCalculationInfo newUnitInfo = getUnitTaxCalculationInfoClone(unitInfo);
			newUnitTaxCalReport.addUnitTaxCalInfo(newUnitInfo);
		}
	}

	/**
	 * Adds the ConsolidatedUnitTaxCalculationInfo clones to clone
	 * 
	 * @param taxCalInfo
	 * @param clone
	 */
	private void addConsolidatedUnitTaxCalculationInfoClone(TaxCalculationInfo taxCalInfo, TaxCalculationInfo clone) {
		for (UnitTaxCalculationInfo unitInfo : taxCalInfo.getConsolidatedUnitTaxCalculationInfo()) {
			UnitTaxCalculationInfo newUnitInfo = getUnitTaxCalculationInfoClone(unitInfo);
			clone.addConsolidatedUnitTaxCalculationInfo(newUnitInfo);
		}
	}

	/**
	 * Adds the UnitTaxCalculationInfo clones to clone
	 * 
	 * @param taxCalInfo
	 * @param clone
	 */
	private void addUnitTaxCalculationInfoClone(TaxCalculationInfo taxCalInfo, TaxCalculationInfo clone) {
		for (UnitTaxCalculationInfo unitInfo : taxCalInfo.getUnitTaxCalculationInfo()) {
			UnitTaxCalculationInfo newUnitInfo = getUnitTaxCalculationInfoClone(unitInfo);
			clone.addUnitTaxCalculationInfo(newUnitInfo);
		}
	}

	/**
	 * Adds the AreaTaxCalculationInfo clones to clone
	 * 
	 * @param unit
	 * @param clone
	 */
	public void addAreaTaxCalculationInfosClone(UnitTaxCalculationInfo unit, UnitTaxCalculationInfo clone) {
		LOGGER.debug("Entered into addAreaTaxCalculationInfosClone");
		for (AreaTaxCalculationInfo areaTax : unit.getAreaTaxCalculationInfos()) {
			AreaTaxCalculationInfo newAreaTax = new AreaTaxCalculationInfo();
			newAreaTax.setTaxableArea(areaTax.getTaxableArea());
			newAreaTax.setMonthlyBaseRent(areaTax.getMonthlyBaseRent());
			newAreaTax.setCalculatedTax(areaTax.getCalculatedTax());
			clone.addAreaTaxCalculationInfo(newAreaTax);
		}
		LOGGER.debug("Exiting from addAreaTaxCalculationInfosClone");
	}

	/**
	 * Adds the MiscellaneousTaxe Clones to clone
	 * 
	 * @param unit
	 * @param clone
	 */
	public void addMiscellaneousTaxesClone(UnitTaxCalculationInfo unit, UnitTaxCalculationInfo clone) {
		LOGGER.debug("Entered into addMiscellaneousTaxesClone");
		for (MiscellaneousTax miscTax : unit.getMiscellaneousTaxes()) {

			MiscellaneousTax newMiscTax = new MiscellaneousTax();
			newMiscTax.setTaxName(miscTax.getTaxName());
			newMiscTax.setTotalActualTax(miscTax.getTotalActualTax());
			newMiscTax.setTotalCalculatedTax(miscTax.getTotalCalculatedTax());
			newMiscTax.setHasChanged(miscTax.getHasChanged());

			for (MiscellaneousTaxDetail miscTaxDetail : miscTax.getTaxDetails()) {
				MiscellaneousTaxDetail newMiscTaxDetail = new MiscellaneousTaxDetail();
				newMiscTaxDetail.setTaxValue(miscTaxDetail.getTaxValue());
				newMiscTaxDetail.setActualTaxValue(miscTaxDetail.getActualTaxValue());
				newMiscTaxDetail.setCalculatedTaxValue(miscTaxDetail.getCalculatedTaxValue());
				// newMiscTaxDetail.setHasChanged(miscTaxDetail.getHasChanged());
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(miscTaxDetail.getFromDate());
				newMiscTaxDetail.setFromDate(calendar.getTime());
				newMiscTaxDetail.setNoOfDays(miscTaxDetail.getNoOfDays());
				newMiscTaxDetail.setIsHistory(miscTaxDetail.getIsHistory());
				newMiscTax.addMiscellaneousTaxDetail(newMiscTaxDetail);
			}

			clone.addMiscellaneousTaxes(newMiscTax);
		}
		LOGGER.debug("Exiting from addMiscellaneousTaxesClone");
	}

	public Date getStartDateOfLowestInstallment() {
		return (Date) persistenceService
				.getSession()
				.createQuery(
						"select min(inst.fromDate) from org.egov.commons.Installment inst where inst.module.moduleName = :moduleName")
				.setString("moduleName", PTMODULENAME).uniqueResult();
	}

	private void setResdAndNonResdParts(Map<String, BigDecimal> taxNameAndALV, UnitTaxCalculationInfo unitTax) {
		unitTax.setResidentialALV(taxNameAndALV.get(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD).setScale(2,
				BigDecimal.ROUND_HALF_UP));
		unitTax.setNonResidentialALV(taxNameAndALV.get(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD).setScale(2,
				BigDecimal.ROUND_HALF_UP));

		for (MiscellaneousTax miscTax : unitTax.getMiscellaneousTaxes()) {
			for (MiscellaneousTaxDetail miscTaxDetail : miscTax.getTaxDetails()) {
				if (DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD.equals(miscTax.getTaxName())) {
					unitTax.setResdEduCess(miscTaxDetail.getCalculatedTaxValue());
				} else if (DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD.equals(miscTax.getTaxName())) {
					unitTax.setNonResdEduCess(miscTaxDetail.getCalculatedTaxValue());
				} else if (DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX.equals(miscTax.getTaxName())) {
					unitTax.setEgCess(miscTaxDetail.getCalculatedTaxValue());
				}
			}
		}
	}

	/**
	 * Gives the current installment
	 * 
	 * @return Installment the current installment for PT module
	 */
	public static Installment getCurrentInstallment() {
		Module module = GenericDaoFactory.getDAOFactory().getModuleDao().getModuleByName(NMCPTISConstants.PTMODULENAME);
		return CommonsDaoFactory.getDAOFactory().getInstallmentDao()
				.getInsatllmentByModuleForGivenDate(module, new Date());
	}

	/**
	 * Returns the number of days between fromDate and toDate
	 * 
	 * @param fromDate
	 *            the date
	 * @param toDate
	 *            the date
	 * @return Long the number of days
	 */
	public static Long getNumberOfDays(Date fromDate, Date toDate) {
		LOGGER.debug("Entered into getNumberOfDays");
		Calendar fromDateCalendar = Calendar.getInstance();
		Calendar toDateCalendar = Calendar.getInstance();
		fromDateCalendar.setTime(fromDate);
		toDateCalendar.setTime(toDate);
		Long days = 0L;
		while (fromDateCalendar.before(toDateCalendar)) {
			fromDateCalendar.add(Calendar.DAY_OF_MONTH, 1);
			days++;
		}
		LOGGER.debug("getNumberOfDays - days: " + days + " between " + fromDate + " and " + toDate);
		LOGGER.debug("Exiting from getNumberOfDays");
		return days;
	}

	/**
	 * Checks whether date is between fromDate and toDate or not
	 * 
	 * @param date
	 * @param fromDate
	 * @param toDate
	 * @return true if date is between fromDate and toDate else returns false
	 */
	public Boolean between(Date date, Date fromDate, Date toDate) {
		return ((date.after(fromDate) || date.equals(fromDate)) && date.before(toDate) || date.equals(toDate));
	}

	public Boolean betweenOrBefore(Date date, Date fromDate, Date toDate) {
		Boolean result = between(date, fromDate, toDate) || date.before(fromDate);
		return result;
	}

	/**
	 * This method returns the number of months between dates (inclusive of
	 * month).
	 * 
	 * @param fromDate
	 *            the from date
	 * @param toDate
	 *            the to date
	 * @return the number of months
	 * @return
	 */
	public static int getMonthsBetweenDates(final Date fromDate, final Date toDate) {
		LOGGER.debug("Entered into getMonthsBetweenDates - fromDate: " + fromDate + ", toDate: " + toDate);
		final Calendar fromDateCalendar = Calendar.getInstance();
		final Calendar toDateCalendar = Calendar.getInstance();
		fromDateCalendar.setTime(fromDate);
		toDateCalendar.setTime(toDate);
		final int yearDiff = toDateCalendar.get(Calendar.YEAR) - fromDateCalendar.get(Calendar.YEAR);
		int noOfMonths = yearDiff * 12 + toDateCalendar.get(Calendar.MONTH) - fromDateCalendar.get(Calendar.MONTH);
		noOfMonths += 1;
		LOGGER.debug("Exiting from getMonthsBetweenDates - noOfMonths: " + noOfMonths);
		return noOfMonths;
	}

	public List<PropertyArrearBean> getPropertyArrears(List<PropertyArrear> arrears) {
		List<PropertyArrearBean> propArrears = new ArrayList<PropertyArrearBean>();
		PropertyArrearBean propArrBean = null;
		for (PropertyArrear pa : arrears) {
			propArrBean = new PropertyArrearBean();
			String key = pa.getFromDate().toString().concat("-").concat(pa.getToDate().toString());
			BigDecimal value = BigDecimal.ZERO;
			value = value.add(pa.getGeneralTax()).add(pa.getSewerageTax()).add(pa.getFireServiceTax())
					.add(pa.getLightingTax()).add(pa.getGeneralWaterTax()).add(pa.getEducationCess())
					.add(pa.getEgCess()).add(pa.getBigResidentailTax()).setScale(2, ROUND_HALF_UP);
			propArrBean.setYear(key);
			propArrBean.setTaxAmount(value);
			propArrears.add(propArrBean);
		}
		return propArrears;
	}

	/*
	 * antisamy filter encodes '&' to '&amp;' and the decode is not happening
	 * so, manually replacing the text
	 */
	public String antisamyHackReplace(String str) {
		String replacedStr;
		replacedStr = str.replaceAll(AMP_ENCODED_STR, AMP_ACTUAL_STR);
		return replacedStr;
	}
	
	/**
	 * Returns true if Water Tax was Imposed before Modification but not after Modification
	 * 
	 * @param installment
	 * @param newProperty
	 * @return
	 * @throws ParseException 
	 */
	private Boolean isWaterTaxImposedOnModify(Installment installment, Property newProperty, UnitTaxCalculationInfo unitTax) throws ParseException {
		LOGGER.debug("Entered into isWaterTaxImposedOnModify");
		LOGGER.debug("isWaterTaxImposedOnModify - newProperty: " + newProperty);
		
		Property oldProperty = newProperty.getBasicProperty().getProperty();
		
		LOGGER.debug("isWaterTaxImposedOnModify - oldProperty: " + oldProperty);

		Date oldPropertyEffectiveDate = PropertyTaxUtil.getEffectiveDateIfWaterTaxImposedForProperty(oldProperty, unitTax);
		Date newPropertyEffectiveDate = null;
		LOGGER.debug("isWaterTaxImposedOnModify - oldPropertyEffectiveDate: " + oldPropertyEffectiveDate);
		if (oldPropertyEffectiveDate != null) {
			newPropertyEffectiveDate = PropertyTaxUtil.getEffectiveDateIfWaterTaxImposedForProperty(newProperty, unitTax);
			LOGGER.debug("isWaterTaxImposedOnModify - newPropertyEffectiveDate: " + newPropertyEffectiveDate);
			// null here indicates Water Tax is not imposed
			if (newPropertyEffectiveDate == null) {
				return installment.getFromDate().before(unitTax.getOccpancyDate());
			}
		}

		return false;
	}

	/**
	 * Gives the Effective Date if Water Tax has been imposed for the property
	 * 
	 * @param property
	 * @return Property Occupancy date, if floors exists then minimum date of Floors effective date
	 * @throws ParseException 
	 */
	private static Date getEffectiveDateIfWaterTaxImposedForProperty(Property property, UnitTaxCalculationInfo unitTax) throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat(NMCPTISConstants.DATE_FORMAT_DDMMYYY);
		String propTypeCode = property.getPropertyDetail().getPropertyTypeMaster().getCode();
		Boolean isGovtProperty = PROPTYPE_STATE_GOVT.equalsIgnoreCase(propTypeCode) || PROPTYPE_CENTRAL_GOVT.equalsIgnoreCase(propTypeCode);
		if (isGovtProperty || property.getPropertyDetail().getFloorDetails().isEmpty()) {
			if (GWR_IMPOSED.equalsIgnoreCase(property.getPropertyDetail().getExtra_field1())) {
				return property.getPropertyDetail().getEffective_date();
			}
		} else {
			Date minEffectiveDate = null;
			Date tempDate = null;
			for (FloorIF floor : property.getPropertyDetail().getFloorDetails()) {
				if (unitTax.getUnitNumber() != null && unitTax.getFloorNumberInteger() != null) {
					if ((unitTax.getUnitNumber().equals(Integer.parseInt(floor.getExtraField1())) && unitTax
							.getFloorNumberInteger().equals(floor.getFloorNo()))
							&& (floor.getWaterRate() != null && GWR_IMPOSED.equalsIgnoreCase(floor.getWaterRate()))) {
						
							if (floor.getExtraField3() == null) {
								LOGGER.info("Floor effective date (floor.extraField3) is null");
							} else {
								return dateFormat.parse(floor.getExtraField3());
							}	
					}
				} else {
					if (floor.getWaterRate() != null && GWR_IMPOSED.equalsIgnoreCase(floor.getWaterRate())) {
						if (floor.getExtraField3() == null) {
							LOGGER.info("Floor effective date (floor.extraField3) is null");
						} else {
							tempDate = dateFormat.parse(floor.getExtraField3());
							if (minEffectiveDate == null) {
								minEffectiveDate = tempDate;
							} else if (tempDate.before(minEffectiveDate)) {
								minEffectiveDate = tempDate;
							}
						}
					}
					
				}
			}
			return minEffectiveDate;
		}
		return null;
	}
	
	/**
	 * Calculated Annual Taxes (Used for PVR Notice)
	 * 
	 */
	public UnitTaxCalculationInfo calculateAnnualTaxes(String propertyType, String propertyTypCategory,
			UnitTaxCalculationInfo unit, Installment installment, String amenities,
			Map<Installment, TaxCalculationInfo> taxCalcInfos) {
		
		LOGGER.debug("Entered into calculateAnnualTaxes");
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("calculateAnnualTaxes - propertyType: " + propertyType + ", propertyTypeCategory: "
					+ propertyTypCategory + ", ALV: " + unit.getAnnualRentAfterDeduction() + ", installment: "
					+ installment + ", amenities: " + amenities);
		}
		
		BigDecimal totalTax = BigDecimal.ZERO;
				
		UnitTaxCalculationInfo unitTaxClone = getUnitTaxCalculationInfoClone(unit);
		BigDecimal alv = unitTaxClone.getAnnualRentAfterDeduction();
		BigDecimal taxPercentage = null;
		
		for (MiscellaneousTax miscTax : unitTaxClone.getMiscellaneousTaxes()) {

			Boolean hasOnlyHistoryTaxDetails = false;
			
			for (MiscellaneousTaxDetail detail : miscTax.getTaxDetails()) {
				hasOnlyHistoryTaxDetails = detail.getIsHistory() == null ? false : detail.getIsHistory().equals('Y') ? true
						: false;
			}
			
			if (hasOnlyHistoryTaxDetails) {
				miscTax.setTotalActualTax(BigDecimal.ZERO);
				miscTax.setTotalCalculatedTax(BigDecimal.ZERO);
				miscTax.getTaxDetails().clear();
			} else {
				EgDemandReasonDetails demandReasonDetails = null;
				BigDecimal calculatedAnnualTax = BigDecimal.ZERO;
				BigDecimal calculatedActualTax = BigDecimal.ZERO;

				// For Mixed and Open Plot (R+NR Category) property, the Edu
				// Cess Resd, Edu Cess NonResd &
				// EGC are applied only on the respective ALV's
				if (PROPTYPE_MIXED.equalsIgnoreCase(propertyType)
						|| PROPTYPE_OPEN_PLOT.equalsIgnoreCase(propertyType)) {
					if (DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD.equalsIgnoreCase(miscTax.getTaxName())) {
						alv = unitTaxClone.getResidentialALV().compareTo(BigDecimal.ZERO) == 0 ? alv : unitTaxClone
								.getResidentialALV();
					} else if (DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD.equalsIgnoreCase(miscTax.getTaxName())
							|| DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX.equalsIgnoreCase(miscTax.getTaxName())) {
						alv = unitTaxClone.getNonResidentialALV().compareTo(BigDecimal.ZERO) == 0 ? alv : unitTaxClone
								.getNonResidentialALV();
					} else if (DEMANDRSN_CODE_GENERAL_WATER_TAX.equalsIgnoreCase(miscTax.getTaxName())
							&& hasConsolidatedUnits(taxCalcInfos.get(installment))) {
						alv = getALVForUnitWithWaterTax(installment, unitTaxClone, taxCalcInfos);
					} else {
						alv = unitTaxClone.getAnnualRentAfterDeduction();
					}
				}

				if (between(unitTaxClone.getOccpancyDate(), installment.getFromDate(), installment.getToDate())) {
					Installment dummyInstallment = new Installment();
					dummyInstallment.setFromDate(unitTaxClone.getOccpancyDate());
					dummyInstallment.setToDate(installment.getToDate());

					demandReasonDetails = (EgDemandReasonDetails) HibernateUtil.getCurrentSession()
							.getNamedQuery(QUERY_DEMANDREASONDETAILS_BY_DEMANDREASON_AND_INSTALLMENT)
							.setString(0, miscTax.getTaxName()).setBigDecimal(1, alv)
							.setDate(2, dummyInstallment.getFromDate()).setDate(3, dummyInstallment.getToDate()).list()
							.get(0);
				} else {
					demandReasonDetails = (EgDemandReasonDetails) HibernateUtil.getCurrentSession()
							.getNamedQuery(QUERY_DEMANDREASONDETAILS_BY_DEMANDREASON_AND_INSTALLMENT)
							.setString(0, miscTax.getTaxName()).setBigDecimal(1, alv)
							.setDate(2, installment.getFromDate()).setDate(3, installment.getToDate()).list().get(0);
				}

				if (propertyType != null && propertyType.equalsIgnoreCase(PROPTYPE_STATE_GOVT)
						&& miscTax.getTaxName().equalsIgnoreCase(DEMANDRSN_CODE_GENERAL_TAX)) {

					BigDecimal demandRsnDtlPercResult = BigDecimal.ZERO;

					if (demandReasonDetails != null) {

						if (ZERO.equals(demandReasonDetails.getFlatAmount())) {
							demandRsnDtlPercResult = alv.multiply(demandReasonDetails.getPercentage()).divide(
									new BigDecimal(HUNDRED));
							calculatedAnnualTax = demandRsnDtlPercResult.subtract(demandRsnDtlPercResult.multiply(
									new BigDecimal(STATEGOVT_BUILDING_GENERALTAX_ADDITIONALDEDUCTION)).divide(
									new BigDecimal(HUNDRED)));
						} else if (demandReasonDetails.getPercentage() == null) {
							calculatedAnnualTax = demandReasonDetails.getFlatAmount();
						} else {
							taxPercentage = demandReasonDetails.getPercentage();
						}

					}

				} else {
					if (demandReasonDetails != null) {
						if (ZERO.equals(demandReasonDetails.getFlatAmount())) {
							taxPercentage = demandReasonDetails.getPercentage();
						} else if (demandReasonDetails.getPercentage() == null) {
							calculatedAnnualTax = demandReasonDetails.getFlatAmount();
						} else {
							taxPercentage = demandReasonDetails.getPercentage();
						}
					}
				}

				if (propertyTypCategory != null
						&& propertyTypCategory.equalsIgnoreCase(PROPTYPE_CAT_RESD_CUM_NON_RESD)
						&& (miscTax.getTaxName().equals(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD)
								|| (miscTax.getTaxName().equals(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD)) || (miscTax
									.getTaxName().equals(DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX)))) {
					calculatedAnnualTax = alv.multiply(
							new BigDecimal(RESD_CUM_COMMERCIAL_PROP_ALV_PERCENTAGE).divide(new BigDecimal(HUNDRED)))
							.multiply(taxPercentage.divide(new BigDecimal(HUNDRED)));
				} else if (!taxPercentage.equals(ZERO) && ZERO.equals(calculatedAnnualTax)) {
					calculatedAnnualTax = alv.multiply(taxPercentage.divide(new BigDecimal(HUNDRED)));
				}

				if (demandReasonDetails != null && demandReasonDetails.getFlatAmount().compareTo(ZERO) > 0) {
					calculatedAnnualTax = getMinOrMaxFlatAmount(0, demandReasonDetails, calculatedAnnualTax, 0);
				}

				MiscellaneousTaxDetail miscTaxDetail = new MiscellaneousTaxDetail();
				miscTaxDetail.setFromDate(demandReasonDetails.getFromDate());
				miscTaxDetail.setTaxValue(demandReasonDetails.getPercentage());
				miscTaxDetail.setCalculatedTaxValue(calculatedAnnualTax);

				if (propertyType != null && propertyType.equalsIgnoreCase(PROPTYPE_CENTRAL_GOVT)) {
					calculatedActualTax = calculatedAnnualTax.setScale(0, ROUND_HALF_UP);
					calculatedAnnualTax = calcGovtTaxOnAmenities(amenities, calculatedAnnualTax);
					miscTaxDetail.setCalculatedTaxValue(calculatedAnnualTax);
					miscTaxDetail.setActualTaxValue(calculatedActualTax);
				}

				calculatedAnnualTax = calculatedAnnualTax.setScale(0, ROUND_HALF_UP);

				miscTax.setTotalCalculatedTax(calculatedAnnualTax);
				miscTax.setTotalActualTax(calculatedActualTax.setScale(0, ROUND_HALF_UP));
				miscTax.getTaxDetails().clear();
				miscTax.getTaxDetails().add(miscTaxDetail);

				totalTax = totalTax.add(calculatedAnnualTax);
			}
		}
		
		unitTaxClone.setTotalTaxPayable(totalTax.setScale(0, ROUND_HALF_UP));
		
		LOGGER.debug("calculateAnnualTaxes - totalTaxPayable: " + unitTaxClone.getTotalTaxPayable());
		LOGGER.debug("Exiting from calculateAnnualTaxes");
		return unitTaxClone;
	}
	
	public static Boolean hasConsolidatedUnits(TaxCalculationInfo taxCalc) {
		return taxCalc.getUnitTaxCalculationInfo().size() == taxCalc.getConsolidatedUnitTaxCalculationInfo().size() ? false : true; 
	}

	public static BigDecimal getALVForUnitWithWaterTax(Installment installment,
			UnitTaxCalculationInfo consolidatedUnitTax, Map<Installment, TaxCalculationInfo> taxCalcInfos) {

		BigDecimal alv = BigDecimal.ZERO;

		for (UnitTaxCalculationInfo unitTax : taxCalcInfos.get(installment).getUnitTaxCalculationInfo()) {
			if (unitTax.getUnitNumber().equals(consolidatedUnitTax.getUnitNumber())) {
				for (MiscellaneousTax miscTax : unitTax.getMiscellaneousTaxes()) {
					if (DEMANDRSN_CODE_GENERAL_WATER_TAX.equalsIgnoreCase(miscTax.getTaxName())) {
						alv = alv.add(unitTax.getAnnualRentAfterDeduction());
						break;
					}
				}
			}
		}

		LOGGER.debug("getALVForUnitWithWaterTax - ALV to be used for Water Tax: " + alv);

		return alv;
	}
	
	/**
	 * Return true if unitTax has Tax taxName details
	 * @param unitTax
	 * @param taxName
	 * @return
	 */
	public static Boolean isTaxExistsInUnitTaxCalcInfo(UnitTaxCalculationInfo unitTax, String taxName) {

		for (MiscellaneousTax miscTax : unitTax.getMiscellaneousTaxes()) {
			for (MiscellaneousTaxDetail detail : miscTax.getTaxDetails()) {
				if ((detail.getIsHistory() == null || detail.getIsHistory().equals('N'))
						&& taxName.equalsIgnoreCase(miscTax.getTaxName())) {
					return true;
				}
			}
		}
		
		return false;
	}
	

	/**
	 * Returns Map with below key-value pair 
	 * CURR_DMD_STR - Current Installment demand 
	 * ARR_DMD_STR - Current Installment collection 
	 * CURR_COLL_STR - Arrear Installment demand 
	 * ARR_COLL_STR - Arrear Installment demand
	 * 
	 * @param property
	 * @return Map<String, BigDecimal>
	 */
	public Map<String, BigDecimal> getDemandAndCollection(Property property) {
		Map<String, BigDecimal> demandCollMap = new HashMap<String, BigDecimal>();
		Installment installment = null;
		Integer instId = null;
		BigDecimal currDmd = BigDecimal.ZERO;
		BigDecimal arrDmd = BigDecimal.ZERO;
		BigDecimal currCollection = BigDecimal.ZERO;
		BigDecimal arrColelection = BigDecimal.ZERO;
		PropertyDAO propertyDao = PropertyDAOFactory.getDAOFactory().getPropertyDAO();
		InstallmentDao installmentDao = CommonsDaoFactory.getDAOFactory().getInstallmentDao();
		PtDemandDao eGPTDemandDao = PropertyDAOFactory.getDAOFactory().getPtDemandDao();
		Ptdemand currDemand = eGPTDemandDao.getNonHistoryCurrDmdForProperty(property);
		List dmdCollList = propertyDao.getDmdCollForAllDmdReasons(currDemand);
		for (Object object : dmdCollList) {
			Object[] listObj = (Object[]) object;
			instId = Integer.valueOf(((BigDecimal) listObj[0]).toString());
			installment = (Installment) installmentDao.findById(instId, false);
			if (property.getInstallment().equals(installment)) {
				if (listObj[2] != null && !listObj[2].equals(BigDecimal.ZERO)) {
					currCollection = currCollection.add((BigDecimal) listObj[2]);
				}
				if (listObj[3] != null && !listObj[3].equals(BigDecimal.ZERO)) {
					currCollection = currCollection.add((BigDecimal) listObj[3]);
				}
				currDmd = currDmd.add((BigDecimal) listObj[1]);
			} else {
				arrDmd = arrDmd.add((BigDecimal) listObj[1]);
				if (listObj[2] != null && !listObj[2].equals(BigDecimal.ZERO)) {
					arrColelection = arrColelection.add((BigDecimal) listObj[2]);
				}
				if (listObj[3] != null && !listObj[3].equals(BigDecimal.ZERO)) {
					arrColelection = arrColelection.add((BigDecimal) listObj[3]);
				}
			}
		}
		demandCollMap.put(CURR_DMD_STR, currDmd);
		demandCollMap.put(ARR_DMD_STR, arrDmd);
		demandCollMap.put(CURR_COLL_STR, currCollection);
		demandCollMap.put(ARR_COLL_STR, arrColelection);
		return demandCollMap;
	}
	
	/**
	 * Tells you whether property is modified or not
	 * 
	 * @param property
	 * @return true if the Property is modified
	 */
	public static boolean isPropertyModified(Property property) {
		
		for (PropertyStatusValues psv : property.getBasicProperty().getPropertyStatusValuesSet()) {
			if (PROPERTY_MODIFY_REASON_MODIFY.equalsIgnoreCase(psv.getPropertyStatus().getStatusCode())) {
				return true;
			}
		}
		
		return false;
	}

	public void setUserService(UserService userService) {
		this.userService= userService;
	}

	public void setEmployeeService(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public void setGenericDao(GenericHibernateDaoFactory genericDao) {
		this.genericDao = genericDao;
	}
	
	public void makeTheEgBillAsHistory(BasicProperty basicProperty) {
		EgBill egBill = (EgBill) persistenceService.find(
				"from EgBill where module = ? and consumerId like ? || '%' and is_history = 'N'", GenericDaoFactory
						.getDAOFactory().getModuleDao().getModuleByName(NMCPTISConstants.PTMODULENAME),
				basicProperty.getUpicNo());
		if (egBill != null) {
			egBill.setIs_History("Y");
			egBill.setLastUpdatedTimeStamp(new Date());
			persistenceService.setType(EgBill.class);
			persistenceService.update(egBill);
		}
	}

	public void setAuditEventService(AuditEventService auditEventService) {
		this.auditEventService = auditEventService;
	}

	public void generateAuditEvent(String action, BasicProperty basicProperty, 
			String createAuditDetails1, String createAuditDetails2) {
		final AuditEvent auditEvent = new AuditEvent(AuditModule.PROPERTYTAX, AuditEntity.PROPERTYTAX_PROPERTY, 
				action, basicProperty.getUpicNo(), createAuditDetails1);
		auditEvent.setPkId(basicProperty.getId());
		auditEvent.setDetails2(createAuditDetails2);
		this.auditEventService.createAuditEvent(auditEvent, BasicPropertyImpl.class);
	}	
	
	/**
	 * Called to get concatenated string from Address fields
	 * 
	 * @param address
	 * @return String formed by concatenating the address fields
	 */
	public static String buildAddress(PropertyAddress address) {

		LOGGER.debug("Entered into buildAddress");
		
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("buildAddress - Address: " + address);
		}

		StringBuffer strAddress = new StringBuffer();

		strAddress.append((isNotBlank(address.getStreetAddress1())) ? address.getStreetAddress1() : " ").append("|");
		strAddress.append((isNotBlank(address.getHouseNo())) ? address.getHouseNo() : " ").append("|");
		strAddress.append((isNotBlank(address.getDoorNumOld())) ? address.getDoorNumOld() : " ").append("|");

		Integer tmpPin = address.getPinCode();
		strAddress.append((tmpPin != null && !tmpPin.toString().isEmpty()) ? tmpPin : " ").append("|");

		strAddress.append((isNotBlank(address.getMobileNo())) ? address.getMobileNo() : " ").append("|");
		strAddress.append((isNotBlank(address.getEmailAddress())) ? address.getEmailAddress() : " ").append("|");
		strAddress.append(isNotBlank(address.getExtraField1()) ? address.getExtraField1() : " ").append("|");
		strAddress.append(isNotBlank(address.getExtraField2()) ? address.getExtraField2() : " ").append("|");
		strAddress.append(isNotBlank(address.getExtraField3()) ? address.getExtraField3() : " ").append("|");
		strAddress.append(isNotBlank(address.getExtraField4()) ? address.getExtraField4() : " ");

		LOGGER.debug("Exit from buildAddress, Address: " + strAddress.toString());

		return strAddress.toString();
	}
}
