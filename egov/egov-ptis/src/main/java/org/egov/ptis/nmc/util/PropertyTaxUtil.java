package org.egov.ptis.nmc.util;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static java.math.BigDecimal.ZERO;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.egov.ptis.constants.PropertyTaxConstants.ARREAR_REBATE_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURRENT_REBATE_STR;
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
import static org.egov.ptis.nmc.constants.NMCPTISConstants.AREA_CONSTANT_FOR_LARGE_RESIDENTIAL_PREMISES;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.ARREARS_DMD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.BASERENT_FROM_APRIL2008_BUILDINGS;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.BASERENT_FROM_APRIL2008_OPENPLOT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.COMMA_STR;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.CURRENT_DMD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DATE_CONSTANT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_FIRE_SERVICE_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_GENERAL_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_GENERAL_WATER_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_LIGHTINGTAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_MUNICIPAL_EDUCATIONAL_CESS;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_PENALTY_FINES;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_SEWERAGE_BENEFIT_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_SEWERAGE_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_STREET_TAX;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.DEMANDRSN_CODE_WATER_BENEFIT_TAX;
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
import static org.egov.ptis.nmc.constants.NMCPTISConstants.HISTORY_TAX_DETAIL;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.LARGE_RESIDENTIAL_PREMISES_ALV;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.MAX_ADVANCES_ALLOWED;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.NONRESD_FLAT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.NON_HISTORY_TAX_DETAIL;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.NON_RESIDENTIAL_FLOOR_AREA_MAP;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.OCCUPIER_OCC;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.OWNER_OCC;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PENALTY_WATERTAX_EFFECTIVE_DATE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPERTYTYPE_STR_TO_CODE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPERTY_MODIFY_REASON_DATA_ENTRY;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPERTY_MODIFY_REASON_MODIFY;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_CAT_NON_RESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.PROPTYPE_CAT_RESD;
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
import static org.egov.ptis.nmc.constants.NMCPTISConstants.STATUS_MIGRATED;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.STRING_SEPERATOR;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.STR_MIGRATED;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.UNITTYPE_OPEN_PLOT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.USAGES_FOR_NON_RESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.USAGES_FOR_OPENPLOT;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.USAGES_FOR_RESD;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.WFLOW_ACTION_NAME_AMALGAMATE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.WFLOW_ACTION_NAME_BIFURCATE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.WFLOW_ACTION_NAME_CHANGEADDRESS;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.WFLOW_ACTION_NAME_CREATE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.WFLOW_ACTION_NAME_DEACTIVATE;
import static org.egov.ptis.nmc.constants.NMCPTISConstants.WFLOW_ACTION_NAME_MODIFY;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.egov.commons.CFinancialYear;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentDao;
import org.egov.demand.dao.DemandGenericHibDao;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgBillType;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.demand.model.EgDemandReasonDetails;
import org.egov.demand.model.EgDemandReasonMaster;
import org.egov.eis.service.EisCommonService;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infstr.commons.Module;
import org.egov.infstr.commons.dao.ModuleDao;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.config.dao.AppConfigValuesDAO;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.DateUtils;
import org.egov.infstr.utils.EgovUtils;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.pims.commons.DesignationMaster;
import org.egov.pims.commons.Position;
import org.egov.pims.commons.service.EisCommonsService;
import org.egov.pims.model.Assignment;
import org.egov.pims.model.PersonalInformation;
import org.egov.pims.service.EmployeeService;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.dao.property.BoundaryCategoryDao;
import org.egov.ptis.domain.dao.property.PropertyDAO;
import org.egov.ptis.domain.entity.demand.FloorwiseDemandCalculations;
import org.egov.ptis.domain.entity.demand.PTDemandCalculations;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.BoundaryCategory;
import org.egov.ptis.domain.entity.property.Category;
import org.egov.ptis.domain.entity.property.FloorIF;
import org.egov.ptis.domain.entity.property.FloorImpl;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyAddress;
import org.egov.ptis.domain.entity.property.PropertyArrear;
import org.egov.ptis.domain.entity.property.PropertyDetail;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyOwner;
import org.egov.ptis.domain.entity.property.PropertyStatusValues;
import org.egov.ptis.domain.entity.property.WorkflowBean;
import org.egov.ptis.nmc.bill.NMCPropertyTaxBillable;
import org.egov.ptis.nmc.constants.NMCPTISConstants;
import org.egov.ptis.nmc.handler.TaxCalculationInfoXmlHandler;
import org.egov.ptis.nmc.model.ApplicableFactor;
import org.egov.ptis.nmc.model.AreaTaxCalculationInfo;
import org.egov.ptis.nmc.model.ConsolidatedUnitTaxCalReport;
import org.egov.ptis.nmc.model.MiscellaneousTax;
import org.egov.ptis.nmc.model.MiscellaneousTaxDetail;
import org.egov.ptis.nmc.model.PropertyArrearBean;
import org.egov.ptis.nmc.model.PropertyInstTaxBean;
import org.egov.ptis.nmc.model.TaxCalculationInfo;
import org.egov.ptis.nmc.model.UnitTaxCalculationInfo;
import org.egov.ptis.nmc.workflow.ActionAmalgmate;
import org.egov.ptis.nmc.workflow.ActionBifurcate;
import org.egov.ptis.nmc.workflow.ActionChangeAddress;
import org.egov.ptis.nmc.workflow.ActionCreate;
import org.egov.ptis.nmc.workflow.ActionDeactivate;
import org.egov.ptis.nmc.workflow.ActionModify;
import org.egov.ptis.nmc.workflow.ActionNameTransfer;
import org.egov.ptis.nmc.workflow.WorkflowDetails;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class PropertyTaxUtil {
	private static final Logger LOGGER = Logger
			.getLogger(PropertyTaxUtil.class);
	private PersistenceService persistenceService;
	@Autowired
	private UserService userService;
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private EisCommonsService eisCommonsService;
	private static final String HUNDRED = "100";
	private static final SimpleDateFormat dateFormatter = new SimpleDateFormat(
			"dd/MM/yyyy");
	@Autowired
	private AppConfigValuesDAO appConfigValuesDAO;
	@Autowired
	@Qualifier(value = "moduleDAO")
	private static ModuleDao moduleDao;
	@Autowired
	private static InstallmentDao installmentDao;
	@Autowired
	@Qualifier(value = "ptDemandDAO")
	private PtDemandDao ptDemandDao;
	@Autowired
	@Qualifier(value = "boundaryCategoryDAO")
	private BoundaryCategoryDao boundaryCategoryDao;
	
	@Autowired
	@Qualifier(value = "demandGenericHibDAO")
	private DemandGenericHibDao demandGenericHibDao;
	
	@Autowired
	@Qualifier(value = "basicPropertyDAO")
	private BasicPropertyDAO basicPropertyDao;
	@Autowired
	@Qualifier(value = "egBillDAO")
	private EgBillDao egBillDao;
	@Autowired
	@Qualifier(value = "propertyDAO")
	private PropertyDAO propertyDao;
	
	private Map<Date, Property> getPropertiesByCreatedDate(
			Map<Date, Property> historyProperties) {
		Map<Date, Property> historyPropsByCrtdDate = new TreeMap<Date, Property>();

		for (Map.Entry<Date, Property> entry : historyProperties.entrySet()) {
			historyPropsByCrtdDate.put(entry.getValue().getCreatedDate()
					.toDate(), entry.getValue());
		}

		return historyPropsByCrtdDate;
	}

	/**
	 * Calculates applicable taxes
	 *
	 * <p>
	 * This method is used both to calculate taxes on unconsolidated ALV and to
	 * calculate taxes on consolidated ALV's. If taxNameAndALV map is null then
	 * use will be to calculate taxes on unconsolidated ALV else this map
	 * contains the ALV to be used to calculate respective tax
	 * </p>
	 *
	 * @param applicableTaxes
	 *            The DemandReasonMaster codes
	 * @param taxNameAndALV
	 *            a map containing taxName and ALV pair, determines ALV to be
	 *            used in calculating tax taxName
	 * @param unitTaxCalculationInfo
	 *            calculated tax details are stored in
	 *            <code> MiscellaneousTax <code>, this wil be addedd to
	 * 							<code> unitTaxCalculationInfo </code>
	 * @param installment
	 *            The installment for which tax has to be calculated
	 * @param propType
	 *            The property type code
	 * @param propTypeCategory
	 *            The property type category
	 * @param amenities
	 *            Amenties in case of Central Govt. property
	 * @param property
	 *            The property containing the property details
	 *
	 * @return {@link UnitTaxCalculationInfo}
	 */

	public UnitTaxCalculationInfo calculateApplicableTaxes(
			List<String> applicableTaxes,
			Map<String, BigDecimal> taxNameAndALV,
			UnitTaxCalculationInfo unitTaxCalculationInfo,
			Installment installment, String propType, String propTypeCategory,
			String amenities, Property property, BoundaryCategory category,
			TaxCalculationInfo currentTaxCalc) {

		BigDecimal totalTaxPayable = BigDecimal.ZERO;
		BigDecimal totalHistoryTaxPayable = BigDecimal.ZERO;

		BigDecimal alv = unitTaxCalculationInfo.getAnnualRentAfterDeduction();
		LOGGER.debug("calculateApplicableTaxes - ALV: " + alv);
		LOGGER.debug("calculateApplicableTaxes - applicableTaxes: "
				+ applicableTaxes);
		LOGGER.debug("calculateApplicableTaxes - taxNameAndALV: "
				+ taxNameAndALV);

		Date newOccupationDate = unitTaxCalculationInfo.getOccpancyDate();
		Boolean isUnitsMatch = false;
		Boolean isNewUnit = true;

		// Dependent on taxNameAndALV map because this contains the TaxName and
		// ALV pair for the Unit
		Boolean isCallForConsolidation = taxNameAndALV == null
				|| taxNameAndALV.isEmpty() ? false : true;

		if (isCallForConsolidation) {
			alv = null;
		}

		Boolean isPropertyModified = PropertyTaxUtil
				.isPropertyModified(property);
		Map<Date, Property> historyProperties = new TreeMap<Date, Property>();
		List<Date> occupancyDates = new ArrayList<Date>();
		Map<Date, Property> historyPropsByCreatedDate = new TreeMap<Date, Property>();
		Set<Date> propertyCreatedDates = new TreeSet<Date>();

		if (isPropertyModified || isMigrated(property.getBasicProperty())) {
			historyProperties = getPropertiesByOccupancy(property, true);
			occupancyDates = new ArrayList<Date>(historyProperties.keySet());
			historyPropsByCreatedDate = getPropertiesByCreatedDate(historyProperties);
			propertyCreatedDates = new TreeSet<Date>(
					historyPropsByCreatedDate.keySet());
		}

		if (!newOccupationDate.equals(installment.getFromDate())
				&& between(newOccupationDate, installment.getFromDate(),
						installment.getToDate()) && isPropertyModified) {

			for (int i = 0; i < propertyCreatedDates.size(); i++) {

				TaxCalculationInfo taxCalcInfo = null;
				Property historyProperty = historyPropsByCreatedDate
						.get((new ArrayList<Date>(propertyCreatedDates)).get(i));

				Date nextPropOccupancyDate = null;

				if ((i + 1) == propertyCreatedDates.size()) {
					nextPropOccupancyDate = getPropertyOccupancyDate(property);
				} else {
					nextPropOccupancyDate = getPropertyOccupancyDate(historyPropsByCreatedDate
							.get((new ArrayList<Date>(propertyCreatedDates))
									.get(i + 1)));
				}

				taxCalcInfo = getTaxCalInfo(installment, historyProperty);

				Set<String> oldApplicableTaxes = new HashSet<String>();
				Date oldUnitOccupancyDate = null;
				Map<String, BigDecimal> historyTaxNameAndALV = new TreeMap<String, BigDecimal>();

				UnitTaxCalculationInfo historyUnit = null;
				BigDecimal historyALV = null;
				boolean isUseMigratedAlv = isUseMigratedALV(property,
						occupancyDates, installment);

				if (taxCalcInfo == null && isUseMigratedAlv) {

					// Code to make use of migrated alv
					oldApplicableTaxes = new HashSet<String>(
							prepareApplicableTaxes(historyProperty));

					isNewUnit = calculateTaxesOnMigratedALV(
							unitTaxCalculationInfo, installment, amenities,
							property, false, isCallForConsolidation,
							isPropertyModified, historyProperty,
							oldApplicableTaxes, installment.getFromDate(),
							nextPropOccupancyDate);
				} else if (taxCalcInfo != null) {

					List<List<UnitTaxCalculationInfo>> unitTaxCalcInfos = new ArrayList<List<UnitTaxCalculationInfo>>();

					if (isCallForConsolidation) {
						if (taxCalcInfo != null) {
							unitTaxCalcInfos = taxCalcInfo
									.getUnitTaxCalculationInfos();
							// for (List<UnitTaxCalculationInfo> units :
							// taxCalcInfo.getUnitTaxCalculationInfos()) {
							for (UnitTaxCalculationInfo unit : taxCalcInfo
									.getConsolidatedUnitTaxCalculationInfo()) {
								// check with both use cases
								/*
								 * when the property has more than 1 unit with
								 * same unit number and when the property is
								 * modified to add the unit same unit number but
								 * with diff floor *
								 */
								if (getIsUnitsMatch(unitTaxCalculationInfo,
										propType, isCallForConsolidation, unit)) {
									historyALV = unit
											.getAnnualRentAfterDeduction();
									oldUnitOccupancyDate = new Date(unit
											.getOccpancyDate().getTime());
									historyUnit = unit;
									break;
								}

							}
							// }

							if (historyUnit != null) {
								if (taxCalcInfo.getUnitTaxCalculationInfos()
										.get(0) instanceof List) {
									for (List<UnitTaxCalculationInfo> units : taxCalcInfo
											.getUnitTaxCalculationInfos()) {
										for (UnitTaxCalculationInfo unit : units) {
											// calling this with
											// isCallForConsolidation
											// = false bec, to get the
											// tax and respective alv pair
											LOGGER.info("unit.unitNumber = "
													+ unit.getUnitNumber()
													+ ", historyUnit.unitNumber = "
													+ historyUnit
															.getUnitNumber());
											if (getIsUnitsMatch(unit, propType,
													true, historyUnit)) {
												prepareTaxNameAndALV(
														historyTaxNameAndALV,
														unit);
											}
										}
									}
								} else {
									for (int j = 0; j < taxCalcInfo
											.getUnitTaxCalculationInfos()
											.size(); j++) {
										UnitTaxCalculationInfo unit = (UnitTaxCalculationInfo) taxCalcInfo
												.getUnitTaxCalculationInfos()
												.get(j);
										LOGGER.info("unit.unitNumber = "
												+ unit.getUnitNumber()
												+ ", historyUnit.unitNumber = "
												+ historyUnit.getUnitNumber());
										if (getIsUnitsMatch(unit, propType,
												true, historyUnit)) {
											prepareTaxNameAndALV(
													historyTaxNameAndALV, unit);
										}
									}
								}

								oldApplicableTaxes.addAll(historyTaxNameAndALV
										.keySet());
							}

						} else {
							unitTaxCalcInfos = currentTaxCalc
									.getUnitTaxCalculationInfos();
						}

					} else {
						unitTaxCalcInfos = taxCalcInfo
								.getUnitTaxCalculationInfos();

						if (unitTaxCalcInfos.get(0) instanceof List) {
							for (List<UnitTaxCalculationInfo> units : unitTaxCalcInfos) {
								for (UnitTaxCalculationInfo unit : units) {
									isUnitsMatch = getIsUnitsMatch(
											unitTaxCalculationInfo, propType,
											isCallForConsolidation, unit);

									if (!isCallForConsolidation
											&& isUnitsMatch
											&& (unit.getBaseRentEffectiveDate() != null && unitTaxCalculationInfo
													.getBaseRentEffectiveDate() != null) ? unit
											.getBaseRentEffectiveDate()
											.equals(unitTaxCalculationInfo
													.getBaseRentEffectiveDate())
											: isUnitsMatch) {
										historyALV = unit
												.getAnnualRentAfterDeduction();
										oldUnitOccupancyDate = new Date(unit
												.getOccpancyDate().getTime());
										historyUnit = unit;
										oldApplicableTaxes = getNonHistoryTaxes(unit);
									}
								}
							}
						} else {
							for (int j = 0; j < unitTaxCalcInfos.size(); j++) {
								UnitTaxCalculationInfo unit = (UnitTaxCalculationInfo) unitTaxCalcInfos
										.get(j);
								isUnitsMatch = getIsUnitsMatch(
										unitTaxCalculationInfo, propType,
										isCallForConsolidation, unit);

								if (!isCallForConsolidation
										&& isUnitsMatch
										&& (unit.getBaseRentEffectiveDate() != null && unitTaxCalculationInfo
												.getBaseRentEffectiveDate() != null) ? unit
										.getBaseRentEffectiveDate()
										.equals(unitTaxCalculationInfo
												.getBaseRentEffectiveDate())
										: isUnitsMatch) {
									historyALV = unit
											.getAnnualRentAfterDeduction();
									oldUnitOccupancyDate = new Date(unit
											.getOccpancyDate().getTime());
									historyUnit = unit;
									oldApplicableTaxes = getNonHistoryTaxes(unit);
								}
							}
						}
					}

					// for ALV of different base rent for a unit, calculating
					// the
					// taxes independently
					// Calculates the Arrears (only on modification), if
					// oldApplicableTaxes is empty then unitTaxCalculationInfo
					// is
					// new unit
					if (!oldApplicableTaxes.isEmpty()
							&& oldUnitOccupancyDate != null) {

						LOGGER.debug("Calculating Arrears Taxes on Modification, UnitNo: "
								+ unitTaxCalculationInfo.getUnitNumber()
								+ ", historyALV: "
								+ historyALV
								+ ", oldUnitOccupancyDate : "
								+ oldUnitOccupancyDate);

						isNewUnit = false;

						if (oldUnitOccupancyDate.equals(unitTaxCalculationInfo
								.getOccpancyDate())) {
							totalTaxPayable = copyHistoryTaxDetails(
									historyUnit, unitTaxCalculationInfo);
						} else {
							/**
							 * Without this condition if the
							 * oldUnitOccupancyDate = 01-06-2002
							 * unitOccupancyDate = 01-07-2002 base rent
							 * effective date = 01-10-2002
							 *
							 * We don't have to calculate the history tax
							 * details, when the unit's base rent effective date
							 * is 01-10-2002
							 *
							 */
							if (category == null
									|| unitTaxCalculationInfo.getOccpancyDate()
											.after(category.getFromDate())) {

								calculateTaxes(
										isNewUnit,
										new ArrayList<String>(
												oldApplicableTaxes),
										unitTaxCalculationInfo,
										installment,
										propType,
										propTypeCategory,
										amenities,
										historyALV,
										isPropertyModified,
										oldUnitOccupancyDate,
										(isCallForConsolidation ? historyTaxNameAndALV
												: null), property, category,
										nextPropOccupancyDate);
							}
						}
					}
				}
			}
		}

		int noOfDays = getNumberOfDays(installment, null,
				unitTaxCalculationInfo.getOccpancyDate(), category);

		if (noOfDays > 0) {
			calculateTaxes(isNewUnit, applicableTaxes, unitTaxCalculationInfo,
					installment, propType, propTypeCategory, amenities, alv,
					isPropertyModified, null, taxNameAndALV, property,
					category, null);
		}

		calculateTotalTaxPayable(unitTaxCalculationInfo);
		totalTaxPayable = roundHistoryTax(unitTaxCalculationInfo,
				unitTaxCalculationInfo.getTotalTaxPayable());
		totalTaxPayable = totalTaxPayable.setScale(0, BigDecimal.ROUND_HALF_UP);

		unitTaxCalculationInfo.setTotalTaxPayable(totalTaxPayable);

		LOGGER.debug(" Total Tax Payable After Applying Applicable Taxes: "
				+ totalTaxPayable);

		LOGGER.debug("calculateApplicableTaxes - applicableTaxes: "
				+ applicableTaxes);
		return unitTaxCalculationInfo;
	}

	/**
	 * Returns true if the property is migrated property else false
	 *
	 * @param property
	 * @return true if BasicProperty is migrated property
	 */
	private boolean isMigrated(BasicProperty basicProperty) {
		return basicProperty.getIsMigrated().equals(STATUS_MIGRATED);
	}

	/**
	 * Calculates the applicable taxes using migrated alv
	 *
	 * @param applicableTaxes
	 * @param unitTaxCalculationInfo
	 * @param installment
	 * @param amenities
	 * @param property
	 * @param isNewUnit
	 * @param isCallForConsolidation
	 * @param isPropertyModified
	 * @param historyProperty
	 * @param oldApplicableTaxes
	 * @param historyALV
	 * @param oldUnitOccupancyDate
	 * @param historyTaxNameAndALV
	 * @param ptDemand
	 * @return true if the units match
	 */
	@SuppressWarnings("unchecked")
	private boolean calculateTaxesOnMigratedALV(
			UnitTaxCalculationInfo unitTaxCalculationInfo,
			Installment installment, String amenities, Property property,
			Boolean isNewUnit, Boolean isCallForConsolidation,
			Boolean isPropertyModified, Property historyProperty,
			Set<String> oldApplicableTaxes, Date oldUnitOccupancyDate,
			Date nextPropOccupancyDate) {

		LOGGER.debug("Entered into calculateTaxesOnMigratedALV");

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("calculateTaxesOnMigratedALV, installment="
					+ installment + ", amenities=" + amenities + ", property="
					+ property + ", isNewUnit=" + isNewUnit
					+ ", isCallForConsolidation=" + isCallForConsolidation
					+ ", isPropertyModified=" + isPropertyModified
					+ ", oldApplicableTaxes=" + oldApplicableTaxes);
		}

		BigDecimal historyALV = BigDecimal.ZERO;
		boolean isUnitsMatch = false;

		Map<String, BigDecimal> historyTaxNameAndALV = new HashMap<String, BigDecimal>();
		String historyPropType = historyProperty.getPropertyDetail()
				.getPropertyTypeMaster().getCode();
		String historyPropCat = historyProperty.getPropertyDetail()
				.getExtra_field5();
		String propType = property.getPropertyDetail().getPropertyTypeMaster()
				.getCode();

		List<FloorwiseDemandCalculations> floorDemands = null;

		if (!historyPropType
				.equalsIgnoreCase(NMCPTISConstants.PROPTYPE_OPEN_PLOT)) {

			floorDemands = HibernateUtil
					.getCurrentSession()
					.createQuery(
							" from FloorwiseDemandCalculations where floor.id in "
									+ getFloorIdsAsString(historyProperty))
					.list();

			if (floorDemands == null || floorDemands.isEmpty()) {
				LOGGER.debug("calculateTaxesOnMigratedALV - floorDemands are not available");
			} else {

				for (FloorwiseDemandCalculations floorDemand : floorDemands) {

					FloorIF floor = floorDemand.getFloor();

					if (propType.equalsIgnoreCase(PROPTYPE_MIXED)
							&& isNull(floor.getPropertyUsage())) {
						LOGGER.debug(" Migrated mixed property does not have Unit usage details ");
						return true;
					}

					if (getIsUnitsMatch(
							unitTaxCalculationInfo,
							propType,
							isCallForConsolidation,
							getUnitTaxFromMigratedFloorInfo(
									unitTaxCalculationInfo, floor))) {

						isUnitsMatch = true;

						if (floor.getWaterRate().equals(GWR_IMPOSED)) {
							oldApplicableTaxes
									.add(DEMANDRSN_CODE_GENERAL_WATER_TAX);
						}

						if (isNotNull(floor.getPropertyUsage())) {
							prepareApplTaxesOnFloor(historyProperty, floor);
						}

						historyALV = floorDemand.getAlv();

						prepareTaxNameAndALV(historyTaxNameAndALV, floorDemand,
								oldApplicableTaxes);

						calculateTaxes(isNewUnit, new ArrayList<String>(
								oldApplicableTaxes), unitTaxCalculationInfo,
								installment, historyPropType, historyPropCat,
								amenities, historyALV, isPropertyModified,
								oldUnitOccupancyDate,
								(isCallForConsolidation ? historyTaxNameAndALV
										: null), property, null,
								nextPropOccupancyDate);

						oldApplicableTaxes
								.remove(DEMANDRSN_CODE_GENERAL_WATER_TAX);

						if (isNotNull(floor.getPropertyUsage())) {
							removeApplTaxesOnFloor(historyProperty,
									oldApplicableTaxes, floor);
						}
					}
				}
			}
		}

		return !isUnitsMatch;
	}

	/**
	 * @param historyProperty
	 * @param oldApplicableTaxes
	 * @param usageName
	 * @param floor
	 */
	private Set<String> prepareApplTaxesOnFloor(Property historyProperty,
			FloorIF floor) {

		Set<String> oldApplicableTaxes = new HashSet<String>();
		String usageName = floor.getPropertyUsage().getUsageName();

		if (historyProperty.getIsExemptedFromTax() == null
				|| !historyProperty.getIsExemptedFromTax()) {
			if (PropertyTaxUtil.isOpenPlotUnit(usageName)) {
				if (PROPTYPE_CAT_RESD.equalsIgnoreCase(floor
						.getUnitTypeCategory())) {
					oldApplicableTaxes
							.add(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD);
				} else if (PROPTYPE_CAT_NON_RESD.equalsIgnoreCase(floor
						.getUnitTypeCategory())) {
					oldApplicableTaxes
							.add(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD);
					oldApplicableTaxes
							.add(DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX);
				}
			} else if (PropertyTaxUtil.isResidentialUnit(usageName)) {
				oldApplicableTaxes.add(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD);
			} else if (PropertyTaxUtil.isNonResidentialUnit(usageName)) {
				oldApplicableTaxes.add(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD);
				oldApplicableTaxes.add(DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX);
			}
		}

		return oldApplicableTaxes;
	}

	/**
	 * @param historyProperty
	 * @param oldApplicableTaxes
	 * @param usageName
	 * @param floor
	 */
	private Set<String> removeApplTaxesOnFloor(Property historyProperty,
			Set<String> oldApplicableTaxes, FloorIF floor) {

		String usageName = floor.getPropertyUsage().getUsageName();

		if (historyProperty.getIsExemptedFromTax() == null
				|| !historyProperty.getIsExemptedFromTax()) {
			if (PropertyTaxUtil.isOpenPlotUnit(usageName)) {
				if (PROPTYPE_CAT_RESD.equalsIgnoreCase(floor
						.getUnitTypeCategory())) {
					oldApplicableTaxes
							.remove(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD);
				} else if (PROPTYPE_CAT_NON_RESD.equalsIgnoreCase(floor
						.getUnitTypeCategory())) {
					oldApplicableTaxes
							.remove(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD);
					oldApplicableTaxes
							.remove(DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX);
				}
			} else if (PropertyTaxUtil.isResidentialUnit(usageName)) {
				oldApplicableTaxes.remove(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD);
			} else if (PropertyTaxUtil.isNonResidentialUnit(usageName)) {
				oldApplicableTaxes
						.remove(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD);
				oldApplicableTaxes
						.remove(DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX);
			}
		}

		return oldApplicableTaxes;
	}

	/**
	 * @param unitTaxCalculationInfo
	 * @param floor
	 */
	private UnitTaxCalculationInfo getUnitTaxFromMigratedFloorInfo(
			UnitTaxCalculationInfo unitTaxCalculationInfo, FloorIF floor) {

		UnitTaxCalculationInfo historyUnit = new UnitTaxCalculationInfo();

		historyUnit.setUnitNumber(Integer.valueOf(floor.getExtraField1()));
		historyUnit.setFloorNumberInteger(floor.getFloorNo());
		historyUnit.setFloorNumber(getFloorStr(floor.getFloorNo()));

		return historyUnit;
	}

	/**
	 * @param historyProperty
	 * @param floorIdString
	 */
	public static String getFloorIdsAsString(Property historyProperty) {
		LOGGER.debug("Entered into getFloorIdsAsString, historyProperty="
				+ historyProperty);

		StringBuilder floorIdString = new StringBuilder();
		floorIdString.append("(");
		int count = 0;
		for (FloorIF floor : historyProperty.getPropertyDetail()
				.getFloorDetails()) {
			floorIdString.append(floor.getId());
			count++;
			if (count != historyProperty.getPropertyDetail().getFloorDetails()
					.size()) {
				floorIdString.append(", ");
			}
		}

		floorIdString.append(")");

		LOGGER.debug("getFloorIdsAsString, floorIdString=" + floorIdString);
		LOGGER.debug("Exiting from getFloorIdsAsString");

		return floorIdString.toString();
	}

	/**
	 * @param oldApplicableTaxes
	 * @param unit
	 */
	private Set<String> getNonHistoryTaxes(UnitTaxCalculationInfo unit) {
		LOGGER.debug("Entered ino getNonHistoryTaxes");

		Set<String> applicableTaxes = new HashSet<String>();
		Boolean hasNonHisotryTaxDtl = false;

		for (MiscellaneousTax miscTax : unit.getMiscellaneousTaxes()) {
			for (MiscellaneousTaxDetail mtd : miscTax.getTaxDetails()) {
				if (mtd.getIsHistory() == null
						|| mtd.getIsHistory().equals(NON_HISTORY_TAX_DETAIL)) {
					hasNonHisotryTaxDtl = true;
					break;
				}
			}
			if (hasNonHisotryTaxDtl) {
				applicableTaxes.add(miscTax.getTaxName());
			}
		}

		LOGGER.info("getNonHistoryTaxes applicableTaxes=" + applicableTaxes);
		LOGGER.debug("Exiting from getNonHistoryTaxes");
		return applicableTaxes;
	}

	private void calculateTotalTaxPayable(UnitTaxCalculationInfo unitTax) {
		BigDecimal totalTax = BigDecimal.ZERO;

		for (MiscellaneousTax mt : unitTax.getMiscellaneousTaxes()) {
			totalTax = totalTax.add(mt.getTotalCalculatedTax());
		}

		unitTax.setTotalTaxPayable(totalTax.setScale(0,
				BigDecimal.ROUND_HALF_UP));
	}

	public TaxCalculationInfo getTaxCalInfo(Installment installment,
			Property property) {
		LOGGER.debug("Entered into getTaxCalInfo, installment=" + installment
				+ ", property=" + property);

		if (property != null) {
			for (Ptdemand ptDemand : property.getPtDemandSet()) {
				if (ptDemand.getEgInstallmentMaster().equals(installment)) {
					LOGGER.debug("Returned from getTaxCalInfo");
					return getTaxCalInfo(ptDemand);
				}
			}
		}

		LOGGER.debug("Returned with null return value from getTaxCalInfo");
		return null;
	}

	/**
	 *
	 * @param unitTaxThis
	 *            UnitTaxCalculationInfo object to be compared
	 * @param propType
	 * @param isCallForConsolidation
	 * @param unitTaxAgainst
	 *            unitTaxThis.unitNumber will be compared against
	 *            unitTaxAgainst.unitNumber
	 * @return true if units match
	 */

	public static Boolean getIsUnitsMatch(UnitTaxCalculationInfo unitTaxThis,
			String propType, Boolean isCallForConsolidation,
			UnitTaxCalculationInfo unitTaxAgainst) {
		Boolean isUnitsMatch;
		if (PROPTYPE_OPEN_PLOT.equalsIgnoreCase(propType)) {
			if (unitTaxThis.getUnitNumber().equals(
					unitTaxAgainst.getUnitNumber())) {
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
			isUnitsMatch = isCallForConsolidation ? (unitTaxThis
					.getUnitNumber().equals(unitTaxAgainst.getUnitNumber()) ? true
					: false)
					: (unitTaxThis.getUnitNumber().equals(
							unitTaxAgainst.getUnitNumber())
							&& unitTaxThis.getFloorNumberInteger().equals(
									unitTaxAgainst.getFloorNumberInteger()) ? true
							: false);
		}
		return isUnitsMatch;
	}

	private BigDecimal roundHistoryTax(
			UnitTaxCalculationInfo unitTaxCalculationInfo,
			BigDecimal totalTaxPayable) {

		LOGGER.debug("roundHistoryTax - totalTaxPayable : " + totalTaxPayable);

		for (MiscellaneousTax miscTax : unitTaxCalculationInfo
				.getMiscellaneousTaxes()) {
			// LOGGER.info("Tax : " + miscTax.getTaxName());
			Boolean hasOnlyHistoryTaxDetails = false;
			for (MiscellaneousTaxDetail taxDetail : miscTax.getTaxDetails()) {
				// LOGGER.info("isHistory : " + taxDetail.getIsHistory());
				hasOnlyHistoryTaxDetails = taxDetail.getIsHistory() == null
						|| taxDetail.getIsHistory().equals('N') ? false : true;
			}

			if (hasOnlyHistoryTaxDetails) {
				totalTaxPayable = totalTaxPayable.subtract(miscTax
						.getTotalCalculatedTax());
				miscTax.setTotalActualTax(miscTax.getTotalActualTax().setScale(
						0, BigDecimal.ROUND_HALF_UP));
				miscTax.setTotalCalculatedTax(miscTax.getTotalCalculatedTax()
						.setScale(0, BigDecimal.ROUND_HALF_UP));
				totalTaxPayable = totalTaxPayable.add(miscTax
						.getTotalCalculatedTax());
			}
		}

		LOGGER.debug("roundHistoryTax - after rounding totalTaxPayable : "
				+ totalTaxPayable);

		return totalTaxPayable;
	}

	private void calculateTaxes(Boolean isNewUnit,
			List<String> applicableTaxes,
			UnitTaxCalculationInfo unitTaxCalculationInfo,
			Installment installment, String propType, String propTypeCategory,
			String amenities, BigDecimal alv, Boolean isModify,
			Date oldUnitOccupancyDate, Map<String, BigDecimal> taxNameAndALV,
			Property property, BoundaryCategory boundaryCategory,
			Date nextPropOccupancyDate) {

		LOGGER.debug("Entered into calculateTaxes");
		LOGGER.debug("calculateTaxes - applicableTaxes: " + applicableTaxes);

		Date baseRentEffectiveDate = unitTaxCalculationInfo
				.getBaseRentEffectiveDate();

		LOGGER.debug("calculateTaxes - baseRentEffectiveDate="
				+ baseRentEffectiveDate);

		Integer totalNoOfDays = getNumberOfDays(installment.getFromDate(),
				installment.getToDate()).intValue();

		if (taxNameAndALV != null && isModify) {
			LOGGER.debug("calculateTaxes - ALV will be taken from taxNameAndALV  : "
					+ taxNameAndALV);
		}

		BigDecimal localALVValue = alv;
		if (applicableTaxes != null) {
			for (String demandReasonCode : applicableTaxes) {

				LOGGER.debug("demandReasonCode: " + demandReasonCode);
				List<EgDemandReasonDetails> demandReasonDetails = new ArrayList<EgDemandReasonDetails>();
				MiscellaneousTax miscellaneousTax = null;

				if (taxNameAndALV != null && !taxNameAndALV.isEmpty()) {
					localALVValue = taxNameAndALV.get(demandReasonCode);
				}

				// Property being modified is in mid of installment, so Arrears
				// Taxes have already been
				// calculated and have been added to the
				// unitTaxCalculationInfo.miscellaneousTaxes
				// When calculating new Taxes for the same
				// MiscellaneousTax.taxName(DemandReason), just getting
				// the existing MiscellaneousTax object and new calculated taxes
				// details will be added as MiscellaneousTaxDetail
				// this existing MiscellaneousTax
				if (localALVValue == null) {
					LOGGER.debug("calculateTaxes - ALV is null");
				} else {
					/*
					 * if (oldUnitOccupancyDate == null &&
					 * between(unitTaxCalculationInfo.getOccpancyDate(),
					 * installment.getFromDate(), installment.getToDate()) &&
					 * isModify) {
					 */
					miscellaneousTax = createMiscellaneousTax(isNewUnit,
							unitTaxCalculationInfo, demandReasonCode);
					// }

					/*
					 * else { miscellaneousTax =
					 * createMiscellaneousTax(isNewUnit, unitTaxCalculationInfo,
					 * demandReasonCode); miscellaneousTax = new
					 * MiscellaneousTax();
					 * miscellaneousTax.setTaxName(demandReasonCode);
					 * miscellaneousTax.setTotalActualTax(BigDecimal.ZERO);
					 * miscellaneousTax.setTotalCalculatedTax(BigDecimal.ZERO);
					 * }
					 */
					/**
					 * This following logic is to prevent to get multiple Tax %
					 * Example: Unit effective date is 01-07-1982 without
					 * dummyInstallment object the the installment.fromDate =
					 * 01-04-1982 & installment.toDate = 31-03-1983 This will
					 * give 2 DemandReaosnDetails for the 1982-83 for 1.
					 * Sewerage Tax; effective from 01-04-1966 and from
					 * 01-05-1982 2. Water Tax; as above
					 *
					 * as Unit effective date is 01-07-1982, the Tax % effective
					 * from 01-04-1966 must not be applied
					 *
					 */
					if (between(unitTaxCalculationInfo.getOccpancyDate(),
							installment.getFromDate(), installment.getToDate())) {
						Installment dummyInstallment = new Installment();

						if (oldUnitOccupancyDate == null) {
							dummyInstallment.setFromDate(unitTaxCalculationInfo
									.getOccpancyDate());
							dummyInstallment.setToDate(installment.getToDate());
						} else {

							if (oldUnitOccupancyDate.before(installment
									.getFromDate())
									|| oldUnitOccupancyDate.equals(installment
											.getFromDate())) {
								dummyInstallment.setFromDate(installment
										.getFromDate());
							} else if (oldUnitOccupancyDate.after(installment
									.getFromDate())) {
								dummyInstallment
										.setFromDate(oldUnitOccupancyDate);
							}

							dummyInstallment.setToDate(unitTaxCalculationInfo
									.getOccpancyDate());
						}

						demandReasonDetails = this.getDemandReasonDetails(
								demandReasonCode, localALVValue,
								dummyInstallment);
					} else {
						demandReasonDetails = this.getDemandReasonDetails(
								demandReasonCode, localALVValue, installment);
					}
				}

				BigDecimal totalActualTax = BigDecimal.ZERO;
				BigDecimal totalCalculatedTax = BigDecimal.ZERO;

				for (EgDemandReasonDetails drd : demandReasonDetails) {

					BigDecimal calculatedTaxValue = BigDecimal.ZERO;
					BigDecimal actualTaxValue = BigDecimal.ZERO;
					BigDecimal demandRsnDtlPercResult = BigDecimal.ZERO;
					BigDecimal taxPerc = BigDecimal.ZERO;
					Integer noOfDays = 0;
					MiscellaneousTaxDetail miscTaxDetail = new MiscellaneousTaxDetail();

					if (propType != null
							&& propType.equalsIgnoreCase(PROPTYPE_STATE_GOVT)
							&& demandReasonCode
									.equalsIgnoreCase(DEMANDRSN_CODE_GENERAL_TAX)) {

						if (drd != null) {
							if (ZERO.equals(drd.getFlatAmount())) {
								demandRsnDtlPercResult = localALVValue
										.multiply(drd.getPercentage()).divide(
												new BigDecimal(HUNDRED));
								calculatedTaxValue = demandRsnDtlPercResult
										.subtract(demandRsnDtlPercResult
												.multiply(
														new BigDecimal(
																STATEGOVT_BUILDING_GENERALTAX_ADDITIONALDEDUCTION))
												.divide(new BigDecimal(HUNDRED)));
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
							&& propTypeCategory
									.equalsIgnoreCase(PROPTYPE_CAT_RESD_CUM_NON_RESD)
							&& (demandReasonCode
									.equals(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD)
									|| (demandReasonCode
											.equals(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD)) || (demandReasonCode
										.equals(DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX)))) {
						calculatedTaxValue = localALVValue
								.multiply(
										new BigDecimal(
												RESD_CUM_COMMERCIAL_PROP_ALV_PERCENTAGE)
												.divide(new BigDecimal(HUNDRED)))
								.multiply(
										taxPerc.divide(new BigDecimal(HUNDRED)));
					} else if (!taxPerc.equals(ZERO)
							&& ZERO.equals(calculatedTaxValue)) {
						calculatedTaxValue = localALVValue.multiply(taxPerc
								.divide(new BigDecimal(HUNDRED)));
					}

					// calculatedTaxValue = calculatedTaxValue.setScale(0,
					// BigDecimal.ROUND_HALF_UP);
					// LOGGER.info("calculateApplicableTaxes - before applying slab % calculatedTaxValue : "
					// + calculatedTaxValue);

					if (isModify && oldUnitOccupancyDate != null) {
						noOfDays = getNumberOfDaysBeforeOccupancy(installment,
								drd, nextPropOccupancyDate,
								oldUnitOccupancyDate, boundaryCategory);
					} else {
						noOfDays = getNumberOfDays(installment, drd,
								unitTaxCalculationInfo.getOccpancyDate(),
								boundaryCategory);
					}

					/**
					 * Bug #21741, Comment 9 - Considering the annual tax for
					 * comparison to determine the min or max flat amount
					 */
					BigDecimal calculatedFlatAmount = BigDecimal.ZERO;

					if (drd.getFlatAmount().compareTo(ZERO) > 0) {
						calculatedFlatAmount = getMinOrMaxFlatAmount(
								totalNoOfDays, drd, calculatedTaxValue,
								noOfDays);
					}

					LOGGER.debug("calculatedTaxValue after getMinOrMaxFlatAmount: "
							+ calculatedTaxValue);

					if (calculatedFlatAmount.compareTo(BigDecimal.ZERO) == 0) {
						calculatedTaxValue = calculatedTaxValue.multiply(
								new BigDecimal(noOfDays))
								.divide(new BigDecimal(totalNoOfDays), 2,
										ROUND_HALF_UP);
					} else {
						calculatedTaxValue = calculatedFlatAmount;
					}

					LOGGER.debug("calculateApplicableTaxes - calculatedTaxValue : "
							+ calculatedTaxValue);

					if (propType != null
							&& propType.equalsIgnoreCase(PROPTYPE_CENTRAL_GOVT)) {
						actualTaxValue = calculatedTaxValue.setScale(0,
								BigDecimal.ROUND_HALF_UP);
						calculatedTaxValue = calcGovtTaxOnAmenities(amenities,
								calculatedTaxValue);
					}

					if (drd != null && !ZERO.equals(calculatedTaxValue)) {
						miscTaxDetail.setTaxValue(drd.getPercentage());
						if (propType != null
								&& propType
										.equalsIgnoreCase(PROPTYPE_CENTRAL_GOVT)) {
							miscTaxDetail.setActualTaxValue(actualTaxValue);
							totalActualTax = totalActualTax.add(actualTaxValue);
						}

						miscTaxDetail.setCalculatedTaxValue(calculatedTaxValue);
						LOGGER.debug("calculateTaxes - calculatedTaxValue: "
								+ calculatedTaxValue);
						totalCalculatedTax = totalCalculatedTax
								.add(calculatedTaxValue);
						LOGGER.debug("calculateTaxes - totalCalculatedTax: "
								+ totalCalculatedTax);
						miscTaxDetail.setFromDate(drd.getFromDate());
						miscTaxDetail.setNoOfDays(noOfDays);

						if (isModify && oldUnitOccupancyDate != null) {
							miscTaxDetail.setIsHistory('Y');
							miscTaxDetail.setHistoryALV(localALVValue);
						} else {
							miscTaxDetail.setIsHistory('N');
						}

						miscellaneousTax
								.addMiscellaneousTaxDetail(miscTaxDetail);
					}
				}

				if (!demandReasonDetails.isEmpty()) {

					if (isModify && oldUnitOccupancyDate != null) {
						miscellaneousTax.setTotalActualTax(miscellaneousTax
								.getTotalActualTax().add(totalActualTax));
						miscellaneousTax.setTotalCalculatedTax(miscellaneousTax
								.getTotalCalculatedTax()
								.add(totalCalculatedTax));

					} else {
						miscellaneousTax.setTotalActualTax(miscellaneousTax
								.getTotalActualTax().add(totalActualTax)
								.setScale(0, BigDecimal.ROUND_HALF_UP));
						miscellaneousTax.setTotalCalculatedTax(miscellaneousTax
								.getTotalCalculatedTax()
								.add(totalCalculatedTax)
								.setScale(0, BigDecimal.ROUND_HALF_UP));
					}
					LOGGER.debug("calculateTaxes - totalCalculatedTax: "
							+ miscellaneousTax.getTotalCalculatedTax());

					addIfNewTax(unitTaxCalculationInfo, miscellaneousTax);
				}

				if (taxNameAndALV != null && !taxNameAndALV.isEmpty()) {
					if (oldUnitOccupancyDate != null) {
						localALVValue = alv;
					}
				}
			}
		}
	}

	/**
	 * @param unitTaxCalculationInfo
	 * @param miscellaneousTax
	 */
	private void addIfNewTax(UnitTaxCalculationInfo unitTaxCalculationInfo,
			MiscellaneousTax miscellaneousTax) {
		Boolean isTaxExists = false;
		if (unitTaxCalculationInfo.getMiscellaneousTaxes().isEmpty()) {
			unitTaxCalculationInfo.addMiscellaneousTaxes(miscellaneousTax);
		} else {

			for (MiscellaneousTax mt : unitTaxCalculationInfo
					.getMiscellaneousTaxes()) {
				if (mt.getTaxName().equalsIgnoreCase(
						miscellaneousTax.getTaxName())) {
					isTaxExists = true;
					break;
				}
			}

			if (!isTaxExists) {
				unitTaxCalculationInfo.addMiscellaneousTaxes(miscellaneousTax);
			}
		}
	}

	/**
	 * @param isNewUnit
	 * @param unitTaxCalculationInfo
	 * @param demandReasonCode
	 * @param miscellaneousTax
	 * @return
	 */
	private MiscellaneousTax createMiscellaneousTax(Boolean isNewUnit,
			UnitTaxCalculationInfo unitTaxCalculationInfo,
			String demandReasonCode) {
		MiscellaneousTax miscellaneousTax = null;

		Boolean isExistingMiscTax = false;

		for (MiscellaneousTax miscTax : unitTaxCalculationInfo
				.getMiscellaneousTaxes()) {
			if (demandReasonCode.equalsIgnoreCase(miscTax.getTaxName())) {
				miscellaneousTax = miscTax;
				isExistingMiscTax = true;
				miscellaneousTax = miscTax;
				break;
			}
		}

		if (isNewUnit || !isExistingMiscTax) {
			miscellaneousTax = new MiscellaneousTax();
			miscellaneousTax.setTaxName(demandReasonCode);
			miscellaneousTax.setTotalActualTax(BigDecimal.ZERO);
			miscellaneousTax.setTotalCalculatedTax(BigDecimal.ZERO);
		}

		return miscellaneousTax;
	}

	/**
	 * Copies the MiscellaneousTaxDetail if isHistory='Y'
	 *
	 * <p>
	 * invoked when Old modification and new modification date are same, as
	 * history tax calculation for current property is dependent on previous
	 * history property. So when both history modification date and current
	 * modification date are same just copying the calculated history tax
	 * details
	 * </p>
	 *
	 * @param historyUnit
	 * @param currentUnitTax
	 * @return Total Tax of History Taxes
	 */
	private BigDecimal copyHistoryTaxDetails(
			UnitTaxCalculationInfo historyUnit,
			UnitTaxCalculationInfo currentUnitTax) {
		LOGGER.debug("Entered into copyHistoryTaxDetails");

		BigDecimal totalTaxPayable = BigDecimal.ZERO;

		for (MiscellaneousTax miscTax : historyUnit.getMiscellaneousTaxes()) {
			for (MiscellaneousTaxDetail taxDetail : miscTax.getTaxDetails()) {
				if (taxDetail.getIsHistory() != null
						&& taxDetail.getIsHistory().equals('Y')) {

					MiscellaneousTax miscellaneousTax = new MiscellaneousTax();
					miscellaneousTax.setTaxName(miscTax.getTaxName());

					MiscellaneousTaxDetail taxDetailCopy = new MiscellaneousTaxDetail(
							taxDetail);
					miscellaneousTax.setTotalCalculatedTax(taxDetailCopy
							.getCalculatedTaxValue());

					if (taxDetailCopy.getActualTaxValue() == null) {
						miscellaneousTax.setTotalActualTax(BigDecimal.ZERO);
					} else {
						miscellaneousTax.setTotalActualTax(taxDetailCopy
								.getActualTaxValue());
					}

					miscellaneousTax.addMiscellaneousTaxDetail(taxDetailCopy);
					currentUnitTax.addMiscellaneousTaxes(miscellaneousTax);
					totalTaxPayable = totalTaxPayable.add(miscellaneousTax
							.getTotalCalculatedTax());
					break;
				}
			}
		}

		LOGGER.debug("Exiting from copyHistoryTaxDetails");

		return totalTaxPayable;
	}

	public BigDecimal getMinOrMaxFlatAmount(Integer totalNoOfDays,
			EgDemandReasonDetails drd, BigDecimal calculatedTaxValue,
			Integer noOfDays) {

		BigDecimal calculatedFlatAmount = BigDecimal.ZERO;

		// FlatAmount must be the maximum amount
		if (drd.getIsFlatAmntMax().equals(Integer.valueOf(1))
				&& (calculatedTaxValue.compareTo(drd.getFlatAmount()) > 0)) {

			if (noOfDays > 0) {
				calculatedFlatAmount = drd
						.getFlatAmount()
						.multiply(new BigDecimal(noOfDays))
						.divide(new BigDecimal(totalNoOfDays), 2, ROUND_HALF_UP);
			} else {
				calculatedFlatAmount = drd.getFlatAmount();
			}
		}

		// FlatAmount must be the minimum amount
		if (drd.getIsFlatAmntMax().equals(Integer.valueOf(0))
				&& (calculatedTaxValue.compareTo(drd.getFlatAmount()) < 0)) {

			if (noOfDays > 0) {
				calculatedFlatAmount = drd
						.getFlatAmount()
						.multiply(new BigDecimal(noOfDays))
						.divide(new BigDecimal(totalNoOfDays), 2, ROUND_HALF_UP);
			} else {
				calculatedFlatAmount = drd.getFlatAmount();
			}
		}
		return calculatedFlatAmount;
	}

	/**
	 * Return the number of days between dates.
	 *
	 * @param installment
	 * @param drd
	 * @return
	 */

	public Integer getNumberOfDays(Installment installment,
			EgDemandReasonDetails drd, Date unitOccupancyDate,
			BoundaryCategory boundaryCategory) {
		LOGGER.debug("Entered into getNumberOfDays - Installment: "
				+ installment + "DemandReasonDetails: " + drd
				+ ", unitOccupancyDate: " + unitOccupancyDate
				+ ", boundaryCategory=" + boundaryCategory);
		Integer noOfDays = 0;
		Date fromDate = null;
		Date toDate = null;

		Set<Date> dates = new TreeSet<Date>();
		dates.add(unitOccupancyDate);
		dates.add(installment.getFromDate());

		if (drd != null) {
			dates.add(drd.getFromDate());
		}

		if (boundaryCategory != null) {
			dates.add(boundaryCategory.getFromDate());
		}

		fromDate = (Date) dates.toArray()[dates.size() - 1];

		dates.clear();

		dates.add(installment.getToDate());

		if (drd != null) {
			dates.add(drd.getToDate());
		}

		if (boundaryCategory != null) {
			dates.add(boundaryCategory.getToDate());
		}

		toDate = (Date) dates.toArray()[0];

		noOfDays = getNumberOfDays(fromDate, toDate).intValue();

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("getNumberOfDays: Installment - " + installment
					+ ", days between " + fromDate + " and " + toDate + " are "
					+ noOfDays);
		}
		LOGGER.debug("Exiting from getNumberOfDays");
		return noOfDays;
	}

	public Integer getNumberOfDaysBeforeOccupancy(Installment installment,
			EgDemandReasonDetails drd, Date unitOccupancyDate,
			Date oldUnitOccupancyDate, BoundaryCategory boundaryCategory) {
		LOGGER.debug("Entered into getNumberOfDays");

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("getNumberOfDays - Installment: " + installment
					+ "DemandReasonDetails: " + drd + ", unitOccupancyDate: "
					+ unitOccupancyDate + ", oldUnitOccupancyDate: "
					+ oldUnitOccupancyDate);
		}

		Integer noOfDays = 0;
		Date fromDate = null;
		Date toDate = null;

		/**
		 *
		 * Taking the earliest date among oldUnitOccupancyDate,
		 * installment.getFromDate(), DemandReasonDetails.fromDate and Base rent
		 * effective date
		 *
		 * Ex: installment.fromDate = 01-04-2010 DemandReasonDetails.fromDate =
		 * 01-06-1985 oldUnitOccupancyDate = 01-10-2010
		 *
		 * Exepected:
		 *
		 * fromDate = 01-04-2010
		 */
		Set<Date> fromDates = new TreeSet<Date>();
		fromDates.add(oldUnitOccupancyDate);
		fromDates.add(installment.getFromDate());
		fromDates.add(drd.getFromDate());

		if (boundaryCategory != null) {
			fromDates.add(boundaryCategory.getFromDate());
		}

		LOGGER.debug("fromDates: " + fromDates);

		fromDate = (Date) fromDates.toArray()[fromDates.size() - 1];

		LOGGER.debug("fromDate: " + fromDate);

		Set<Date> toDates = new TreeSet<Date>();
		toDates.add(unitOccupancyDate);
		toDates.add(installment.getToDate());
		toDates.add(drd.getToDate());

		if (boundaryCategory != null) {
			toDates.add(boundaryCategory.getToDate());
		}

		LOGGER.debug("toDates: " + toDates);

		toDate = (Date) toDates.toArray()[0];

		LOGGER.debug("toDate: " + toDate);

		noOfDays = getNumberOfDays(fromDate, toDate).intValue();

		if (LOGGER.isInfoEnabled()) {
			LOGGER.debug("getNumberOfDays: Installment - " + installment
					+ ", days between " + fromDate + " and " + toDate + " are "
					+ noOfDays);
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
	public BigDecimal calcGovtTaxOnAmenities(String amenities,
			BigDecimal applicableTaxValue) {
		BigDecimal applicableTaxValueDummy = applicableTaxValue;
		if (amenities.equalsIgnoreCase(AMENITY_TYPE_FULL)) {
			applicableTaxValueDummy = applicableTaxValueDummy
					.multiply(new BigDecimal(AMENITY_PERCENTAGE_FULL)
							.divide(new BigDecimal(HUNDRED)));
		} else if (amenities.equalsIgnoreCase(AMENITY_TYPE_PARTIAL)) {
			applicableTaxValueDummy = applicableTaxValueDummy
					.multiply(new BigDecimal(AMENITY_PERCENTAGE_PARTIAL)
							.divide(new BigDecimal(HUNDRED)));
		} else if (amenities.equalsIgnoreCase(AMENITY_TYPE_NIL)) {
			applicableTaxValueDummy = applicableTaxValueDummy
					.multiply(new BigDecimal(AMENITY_PERCENTAGE_NIL)
							.divide(new BigDecimal(HUNDRED)));
		}
		return applicableTaxValueDummy;
	}

	public String multiplicativeFactorAreaWise(String propertyType,
			FloorImpl floor, String areaFactor, String propCategory) {
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
				unitAreaFactor = RESIDENTIAL_FLOOR_AREA_MAP.get("0"
						+ STRING_SEPERATOR + areaFactor);
			} else {
				if (FLOORNO_WITH_DIFF_MULFACTOR_RESD.contains(floor
						.getFloorNo().toString())) {
					unitAreaFactor = RESIDENTIAL_FLOOR_AREA_MAP.get(floor
							.getFloorNo() + STRING_SEPERATOR + areaFactor);
				} else {
					unitAreaFactor = RESIDENTIAL_FLOOR_AREA_MAP.get("3"
							+ STRING_SEPERATOR + areaFactor);
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
				unitAreaFactor = NON_RESIDENTIAL_FLOOR_AREA_MAP.get("0"
						+ STRING_SEPERATOR + areaFactor);
			} else {
				if (FLOORNO_WITH_DIFF_MULFACTOR_NONRESD.contains(floor
						.getFloorNo().toString())) {
					unitAreaFactor = NON_RESIDENTIAL_FLOOR_AREA_MAP.get(floor
							.getFloorNo() + STRING_SEPERATOR + areaFactor);
				} else {
					unitAreaFactor = NON_RESIDENTIAL_FLOOR_AREA_MAP.get("1"
							+ STRING_SEPERATOR + areaFactor);
				}
			}
		}

		LOGGER.info("propertyType:" + propertyType + "; Floor No:"
				+ floor.getFloorNo() + "; areaFactor:" + areaFactor
				+ "; unitAreaFactor: " + unitAreaFactor);
		return unitAreaFactor;

	}

	public List<ApplicableFactor> getApplicableFactorsForResidentialAndNonResidential(
			FloorImpl floorImpl, Boundary propertyArea,
			Installment installment, Long categoryId) {
		List<ApplicableFactor> applicableFactors = new ArrayList<ApplicableFactor>();

		// Category category = this.getCategoryForBoundary(propertyArea);

		Category category = (Category) persistenceService.findByNamedQuery(
				QUERY_BASERENT_BY_OCCUPANCY_AREA_STRUCTURE,
				propertyArea.getId(), floorImpl.getPropertyUsage().getId(),
				floorImpl.getStructureClassification().getId(),
				installment.getFromDate());

		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		Date dateConstant = null;
		Date floorCompletionOccupation = null;
		try {
			dateConstant = dateFormatter.parse(DATE_CONSTANT);
			floorCompletionOccupation = dateFormatter.parse(floorImpl
					.getExtraField3());
		} catch (ParseException e) {
			LOGGER.error(e.getMessage(), e);
		}

		// Add Structural Factor

		if (floorCompletionOccupation.after(dateConstant)
				|| floorCompletionOccupation.equals(dateConstant)) {
			ApplicableFactor applicableStructuralFactor = new ApplicableFactor();
			applicableStructuralFactor.setFactorName("SF");
			applicableStructuralFactor.setFactorIndex(floorImpl
					.getStructureClassification().getTypeName());
			applicableStructuralFactor.setFactorValue(new BigDecimal(Float
					.toString(floorImpl.getStructureClassification()
							.getFactor())));
			applicableFactors.add(applicableStructuralFactor);
		} else {
			ApplicableFactor applicableStructuralFactor = new ApplicableFactor();
			applicableStructuralFactor.setFactorName("SF");
			applicableStructuralFactor.setFactorIndex(category
					.getCategoryName());
			applicableStructuralFactor.setFactorValue(new BigDecimal(Float
					.toString(category.getCategoryAmount())));
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
			applicableUsageFactor.setFactorIndex(floorImpl.getPropertyUsage()
					.getUsageName());
			applicableUsageFactor.setFactorValue(new BigDecimal(
					Float.toString(floorImpl.getPropertyUsage()
							.getUsagePercentage())));
			applicableFactors.add(applicableUsageFactor);
		}

		// Add Occupancy Factor
		ApplicableFactor applicableOccupancyFactor = new ApplicableFactor();
		applicableOccupancyFactor.setFactorName("OF");
		applicableOccupancyFactor.setFactorIndex(floorImpl
				.getPropertyOccupation().getOccupancyCode());
		applicableOccupancyFactor.setFactorValue(new BigDecimal(Float
				.toString(floorImpl.getPropertyOccupation()
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
				applicableAgeFactor.setFactorIndex(floorImpl
						.getDepreciationMaster().getDepreciationName());
			applicableAgeFactor.setFactorValue(new BigDecimal(Float
					.toString(floorImpl.getDepreciationMaster()
							.getDepreciationPct())));
			applicableFactors.add(applicableAgeFactor);
		}

		// Add Location Factor

		ApplicableFactor applicableLocationFactor = new ApplicableFactor();

		if (installment.getToDate().after(dateConstant)) {
			Category locationFactorCategory = (Category) persistenceService
					.find("from Category c where c.id = ?", categoryId);
			applicableLocationFactor.setFactorName("LF");
			applicableLocationFactor.setFactorIndex(locationFactorCategory
					.getCategoryName());
			applicableLocationFactor.setFactorValue(new BigDecimal(
					locationFactorCategory.getCategoryAmount()).setScale(2,
					BigDecimal.ROUND_HALF_UP));
			applicableFactors.add(applicableLocationFactor);
		} else {
			applicableLocationFactor.setFactorName("LF");
			applicableLocationFactor.setFactorIndex(category.getCategoryName());
			applicableLocationFactor.setFactorValue(new BigDecimal(Float
					.toString(category.getCategoryAmount())));
			applicableFactors.add(applicableLocationFactor);
		}

		return applicableFactors;

	}

	public BigDecimal calculateBaseRentPerSqMtPerMonth(
			List<ApplicableFactor> applicableFactors, BigDecimal baseRent) {

		BigDecimal baseRentPerMonthPerSQMT = baseRent;

		for (ApplicableFactor applicableFactor : applicableFactors) {
			baseRentPerMonthPerSQMT = baseRentPerMonthPerSQMT
					.multiply(applicableFactor.getFactorValue());
		}
		LOGGER.info("Base Rent Per Month Per Square Meter: "
				+ baseRentPerMonthPerSQMT.setScale(2, BigDecimal.ROUND_HALF_UP));
		return baseRentPerMonthPerSQMT.setScale(2, BigDecimal.ROUND_HALF_UP);

	}

	@SuppressWarnings("unchecked")
	public List<BoundaryCategory> getBoundaryCategories(FloorImpl floor,
			Boundary area, Installment installment,
			PropertyDetail propertyDetail) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		Date dateConstant = null;
		Map<Date, BigDecimal> unitWiseBaseRent = new TreeMap<Date, BigDecimal>();
		List<BoundaryCategory> categories = new ArrayList<BoundaryCategory>();
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
		String propType = propertyDetail.getPropertyTypeMaster().getCode();
		if (installment.getToDate().before(dateConstant)) {
			if (propType.equalsIgnoreCase(PROPTYPE_OPEN_PLOT)
					|| (floor.getUnitType() != null && floor.getUnitType()
							.getCode().equalsIgnoreCase(UNITTYPE_OPEN_PLOT))) {
				Long usageId = null;

				if (propType.equalsIgnoreCase(PROPTYPE_OPEN_PLOT)) {
					usageId = propertyDetail.getPropertyUsage().getId();
				} else {
					usageId = floor.getPropertyUsage().getId();
				}

				categories = persistenceService.findAllByNamedQuery(
						QUERY_BASERENT_BY_BOUNDARY_FOR_OPENPLOT, area.getId(),
						usageId, installment.getFromDate(),
						installment.getToDate());
			} else {
				Date dateTogetCategory = null;
				try {
					if (floor.getExtraField3() != null
							&& installment.getFromDate()
									.before(dateFormatter.parse(floor
											.getExtraField3()))) {
						dateTogetCategory = dateFormatter.parse(floor
								.getExtraField3());
					} else {
						dateTogetCategory = installment.getFromDate();
					}

				} catch (ParseException e) {
					LOGGER.error(e.getMessage(), e);
				}
				categories = persistenceService.findAllByNamedQuery(
						QUERY_BASERENT_BY_OCCUPANCY_AREA_STRUCTURE, area
								.getId(), floor.getPropertyUsage().getId(),
						floor.getStructureClassification().getId(),
						dateTogetCategory, installment.getToDate());
			}

		} else {
			if (floor.getUnitType() != null
					&& floor.getUnitType().getCode()
							.equalsIgnoreCase(UNITTYPE_OPEN_PLOT)) {
				unitWiseBaseRent.put(dateConstant,
						BASERENT_FROM_APRIL2008_OPENPLOT);
			} else {
				unitWiseBaseRent.put(dateConstant,
						BASERENT_FROM_APRIL2008_BUILDINGS);
			}
		}

		LOGGER.info("baseRentOfUnit - Installment : " + installment
				+ " Base Rent Unit Wise: " + unitWiseBaseRent);
		return categories;
	}

	public String generateTaxCalculationXML(
			TaxCalculationInfo taxCalculationInfo) {
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
			effDate.append(EFFECTIVE_ASSESSMENT_PERIOD1).append(COMMA_STR)
					.append(year);
		}
		if (month >= 3 && month < 6) {
			effDate.append(EFFECTIVE_ASSESSMENT_PERIOD2).append(COMMA_STR)
					.append(year);
		}
		if (month >= 6 && month < 9) {
			effDate.append(EFFECTIVE_ASSESSMENT_PERIOD3).append(COMMA_STR)
					.append(year);
		}
		if (month >= 9 && month < 12) {
			effDate.append(EFFECTIVE_ASSESSMENT_PERIOD4).append(COMMA_STR)
					.append(year);
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
								+ "cfinancialyear.startingDate and cfinancialyear.endingDate")
				.setDate(0, date).list().get(0);
	}

	public Category getCategoryForBoundary(Boundary boundary) {
		return boundaryCategoryDao.getCategoryForBoundary(boundary);
	}

	public List<Installment> getInstallmentListByStartDate(Date startDate) {
		return persistenceService.findAllByNamedQuery(
				QUERY_INSTALLMENTLISTBY_MODULE_AND_STARTYEAR, startDate,
				startDate, PTMODULENAME);
	}

	public EgDemandReason getDemandReasonByCodeAndInstallment(
			String demandReasonCode, Installment installment) {
		return (EgDemandReason) persistenceService.findByNamedQuery(
				QUERY_DEMANDREASONBY_CODE_AND_INSTALLMENTID, demandReasonCode,
				installment.getId());
	}

	public EgDemandReasonDetails getDemandReasonDetailsByDemandReasonId(
			EgDemandReason demandReason,
			BigDecimal grossAnnualRentAfterDeduction) {
		return (EgDemandReasonDetails) persistenceService.findByNamedQuery(
				QUERY_DEMANDREASONDETAILBY_DEMANDREASONID,
				demandReason.getId(), grossAnnualRentAfterDeduction);
	}

	public EgDemandReasonDetails getDemandReasonDetails(
			String demandReasonCode, BigDecimal grossAnnualRentAfterDeduction,
			Date date) {
		return (EgDemandReasonDetails) persistenceService.findByNamedQuery(
				QUERY_DEMANDREASONDETAILS_BY_DEMANDREASONID_DATE,
				demandReasonCode, grossAnnualRentAfterDeduction, date);
	}

	@SuppressWarnings("unchecked")
	public List<EgDemandReasonDetails> getDemandReasonDetails(
			String demandReasonCode, BigDecimal grossAnnualRentAfterDeduction,
			Installment installment) {
		return persistenceService.findAllByNamedQuery(
				QUERY_DEMANDREASONDETAILS_BY_DEMANDREASON_AND_INSTALLMENT,
				demandReasonCode, grossAnnualRentAfterDeduction,
				installment.getFromDate(), installment.getToDate());
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
			AppConfigValues appConfigValues = appConfigValuesDAO
					.getAppConfigValueByDate(moduleName, key, new Date());
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
	public String getAppConfigValue(String moduleName, String key,
			String defaultValue) {
		AppConfigValues appConfigValues = appConfigValuesDAO
				.getAppConfigValueByDate(moduleName, key, new Date());
		return appConfigValues == null ? defaultValue : appConfigValues
				.getValue();
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
				taxCalInfo.setIndexNo(demand.getEgptProperty()
						.getBasicProperty().getUpicNo());
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
	public UnitTaxCalculationInfo calculateTaxPayableByOccupancyDate(
			UnitTaxCalculationInfo unitTaxCalculationInfo,
			Installment installment) {
		if ((unitTaxCalculationInfo.getOccpancyDate().after(
				installment.getFromDate()) || unitTaxCalculationInfo
				.getOccpancyDate().equals(installment.getFromDate()))
				&& (unitTaxCalculationInfo.getOccpancyDate().before(
						installment.getToDate()) || unitTaxCalculationInfo
						.getOccpancyDate().equals(installment.getFromDate()))) {
			Date occupancyDate = unitTaxCalculationInfo.getOccpancyDate();
			SimpleDateFormat monthFormatter = new SimpleDateFormat("MM");
			String month = monthFormatter.format(occupancyDate);
			BigDecimal totalTaxPayableByOccupancyDate = BigDecimal.ZERO;
			LOGGER.info("occupance date month " + month);

			if (Integer.valueOf(month) >= Integer.valueOf(7)
					&& Integer.valueOf(month) <= Integer.valueOf(9)) {
				for (MiscellaneousTax miscellaneousTax : unitTaxCalculationInfo
						.getMiscellaneousTaxes()) {
					BigDecimal totalTax = BigDecimal.ZERO;
					for (MiscellaneousTaxDetail miscTaxDetail : miscellaneousTax
							.getTaxDetails()) {
						miscTaxDetail.setCalculatedTaxValue(miscTaxDetail
								.getCalculatedTaxValue().multiply(
										new BigDecimal(".75")));
						totalTaxPayableByOccupancyDate = totalTaxPayableByOccupancyDate
								.add(miscTaxDetail.getCalculatedTaxValue());
						totalTax = totalTax.add(miscTaxDetail
								.getCalculatedTaxValue());
						LOGGER.debug("appyling .75% for month from 7 t0 9 "
								+ miscTaxDetail.getCalculatedTaxValue());
					}
					miscellaneousTax.setTotalCalculatedTax(totalTax.setScale(0,
							BigDecimal.ROUND_HALF_UP));
				}
				unitTaxCalculationInfo
						.setTotalTaxPayable(totalTaxPayableByOccupancyDate
								.setScale(2, BigDecimal.ROUND_HALF_UP));
			} else if (Integer.valueOf(month) >= Integer.valueOf(10)
					&& Integer.valueOf(month) <= Integer.valueOf(12)) {
				for (MiscellaneousTax miscellaneousTax : unitTaxCalculationInfo
						.getMiscellaneousTaxes()) {
					BigDecimal totalTax = BigDecimal.ZERO;
					for (MiscellaneousTaxDetail miscTaxDetail : miscellaneousTax
							.getTaxDetails()) {
						miscTaxDetail.setCalculatedTaxValue(miscTaxDetail
								.getCalculatedTaxValue().multiply(
										new BigDecimal(".5")));
						totalTaxPayableByOccupancyDate = totalTaxPayableByOccupancyDate
								.add(miscTaxDetail.getCalculatedTaxValue());
						totalTax = totalTax.add(miscTaxDetail
								.getCalculatedTaxValue());
						LOGGER.debug("appyling .5% for month from 10 t0 12 "
								+ miscTaxDetail.getCalculatedTaxValue());
					}
					miscellaneousTax.setTotalCalculatedTax(totalTax.setScale(0,
							BigDecimal.ROUND_HALF_UP));
				}

				unitTaxCalculationInfo
						.setTotalTaxPayable(totalTaxPayableByOccupancyDate
								.setScale(2, BigDecimal.ROUND_HALF_UP));
			} else if (Integer.valueOf(month) >= Integer.valueOf(1)
					&& Integer.valueOf(month) <= Integer.valueOf(3)) {
				for (MiscellaneousTax miscellaneousTax : unitTaxCalculationInfo
						.getMiscellaneousTaxes()) {
					BigDecimal totalTax = BigDecimal.ZERO;
					for (MiscellaneousTaxDetail miscTaxDetail : miscellaneousTax
							.getTaxDetails()) {
						miscTaxDetail.setCalculatedTaxValue(miscTaxDetail
								.getCalculatedTaxValue().multiply(
										new BigDecimal(".25")));
						totalTaxPayableByOccupancyDate = totalTaxPayableByOccupancyDate
								.add(miscTaxDetail.getCalculatedTaxValue());
						totalTax = totalTax.add(miscTaxDetail
								.getCalculatedTaxValue());
						LOGGER.debug("appyling .25% for month from 1 t0 3 "
								+ miscTaxDetail.getCalculatedTaxValue());
					}
					miscellaneousTax.setTotalCalculatedTax(totalTax.setScale(0,
							BigDecimal.ROUND_HALF_UP));
				}
				unitTaxCalculationInfo
						.setTotalTaxPayable(totalTaxPayableByOccupancyDate
								.setScale(2, BigDecimal.ROUND_HALF_UP));
			}
		}
		LOGGER.debug("tax payable by occupancy road "
				+ unitTaxCalculationInfo.getTotalTaxPayable());
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
		BasicProperty basicProperty = basicPropertyDao
				.getBasicPropertyByPropertyID(propertyId);
		EgDemand egDemand = ptDemandDao
				.getNonHistoryCurrDmdForProperty(basicProperty.getProperty());
		Module module = moduleDao
				.getModuleByName(PropertyTaxConstants.PTMODULENAME);
		Installment currentInstall = installmentDao
				.getInsatllmentByModuleForGivenDate(module, new Date());

		list = demandGenericHibDao.getReasonWiseDCB(egDemand, module);

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

	private Map<String, BigDecimal> populateReasonsSum(Object[] data,
			Map<String, BigDecimal> taxSum) {
		BigDecimal tmpVal;
		if ((data[0].toString()).equals(DEMANDRSN_CODE_GENERAL_TAX)) {
			tmpVal = taxSum.get(DEMANDRSN_CODE_GENERAL_TAX);
			// considering rebate as collection and substracting it.
			taxSum.put(DEMANDRSN_CODE_GENERAL_TAX, tmpVal
					.add(((BigDecimal) data[2])
							.subtract(((BigDecimal) data[3]))));
		} else if ((data[0].toString()).equals(DEMANDRSN_CODE_SEWERAGE_TAX)) {
			tmpVal = taxSum.get(DEMANDRSN_CODE_SEWERAGE_TAX);
			taxSum.put(DEMANDRSN_CODE_SEWERAGE_TAX, tmpVal
					.add(((BigDecimal) data[2])
							.subtract(((BigDecimal) data[3]))));
		} else if ((data[0].toString()).equals(DEMANDRSN_CODE_FIRE_SERVICE_TAX)) {
			tmpVal = taxSum.get(DEMANDRSN_CODE_FIRE_SERVICE_TAX);
			taxSum.put(DEMANDRSN_CODE_FIRE_SERVICE_TAX, tmpVal
					.add(((BigDecimal) data[2])
							.subtract(((BigDecimal) data[3]))));
		} else if ((data[0].toString()).equals(DEMANDRSN_CODE_LIGHTINGTAX)) {
			tmpVal = taxSum.get(DEMANDRSN_CODE_LIGHTINGTAX);
			taxSum.put(DEMANDRSN_CODE_LIGHTINGTAX, tmpVal
					.add(((BigDecimal) data[2])
							.subtract(((BigDecimal) data[3]))));
		} else if ((data[0].toString())
				.equals(DEMANDRSN_CODE_GENERAL_WATER_TAX)) {
			tmpVal = taxSum.get(DEMANDRSN_CODE_GENERAL_WATER_TAX);
			taxSum.put(DEMANDRSN_CODE_GENERAL_WATER_TAX, tmpVal
					.add(((BigDecimal) data[2])
							.subtract(((BigDecimal) data[3]))));
		} else if ((data[0].toString())
				.equals(DEMANDRSN_CODE_SEWERAGE_BENEFIT_TAX)) {
			tmpVal = taxSum.get(DEMANDRSN_CODE_SEWERAGE_BENEFIT_TAX);
			taxSum.put(DEMANDRSN_CODE_SEWERAGE_BENEFIT_TAX, tmpVal
					.add(((BigDecimal) data[2])
							.subtract(((BigDecimal) data[3]))));
		} else if ((data[0].toString())
				.equals(DEMANDRSN_CODE_WATER_BENEFIT_TAX)) {
			tmpVal = taxSum.get(DEMANDRSN_CODE_WATER_BENEFIT_TAX);
			taxSum.put(DEMANDRSN_CODE_WATER_BENEFIT_TAX, tmpVal
					.add(((BigDecimal) data[2])
							.subtract(((BigDecimal) data[3]))));
		} else if ((data[0].toString()).equals(DEMANDRSN_CODE_STREET_TAX)) {
			tmpVal = taxSum.get(DEMANDRSN_CODE_STREET_TAX);
			taxSum.put(DEMANDRSN_CODE_STREET_TAX, tmpVal
					.add(((BigDecimal) data[2])
							.subtract(((BigDecimal) data[3]))));
		} else if ((data[0].toString())
				.equals(DEMANDRSN_CODE_MUNICIPAL_EDUCATIONAL_CESS)) {
			tmpVal = taxSum.get(DEMANDRSN_CODE_MUNICIPAL_EDUCATIONAL_CESS);
			taxSum.put(DEMANDRSN_CODE_MUNICIPAL_EDUCATIONAL_CESS, tmpVal
					.add(((BigDecimal) data[2])
							.subtract(((BigDecimal) data[3]))));
		} else if ((data[0].toString())
				.equals(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD)) {
			tmpVal = taxSum.get(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD);
			taxSum.put(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD, tmpVal
					.add(((BigDecimal) data[2])
							.subtract(((BigDecimal) data[3]))));
		} else if ((data[0].toString())
				.equals(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD)) {
			tmpVal = taxSum.get(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD);
			taxSum.put(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD, tmpVal
					.add(((BigDecimal) data[2])
							.subtract(((BigDecimal) data[3]))));
		} else if ((data[0].toString())
				.equals(DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX)) {
			tmpVal = taxSum.get(DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX);
			taxSum.put(DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX, tmpVal
					.add(((BigDecimal) data[2])
							.subtract(((BigDecimal) data[3]))));
		} else if ((data[0].toString())
				.equals(DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX)) {
			tmpVal = taxSum.get(DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX);
			taxSum.put(DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX, tmpVal
					.add(((BigDecimal) data[2])
							.subtract(((BigDecimal) data[3]))));
		} else if ((data[0].toString()).equals(DEMANDRSN_CODE_PENALTY_FINES)) {
			tmpVal = taxSum.get(DEMANDRSN_CODE_PENALTY_FINES);
			taxSum.put(DEMANDRSN_CODE_PENALTY_FINES, tmpVal
					.add(((BigDecimal) data[2])
							.subtract(((BigDecimal) data[3]))));
		} else if ((data[0].toString())
				.equals(DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY)) {
			tmpVal = taxSum.get(DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY);
			taxSum.put(DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY, tmpVal
					.add(((BigDecimal) data[2])
							.subtract(((BigDecimal) data[3]))));
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
	private Map<String, BigDecimal> initReasonsMap(
			Map<String, BigDecimal> taxSum) {

		taxSum.put(DEMANDRSN_CODE_GENERAL_TAX, BigDecimal.ZERO);
		taxSum.put(DEMANDRSN_CODE_SEWERAGE_TAX, BigDecimal.ZERO);
		taxSum.put(DEMANDRSN_CODE_FIRE_SERVICE_TAX, BigDecimal.ZERO);
		taxSum.put(DEMANDRSN_CODE_LIGHTINGTAX, BigDecimal.ZERO);
		taxSum.put(DEMANDRSN_CODE_GENERAL_WATER_TAX, BigDecimal.ZERO);
		taxSum.put(DEMANDRSN_CODE_SEWERAGE_BENEFIT_TAX, BigDecimal.ZERO);
		taxSum.put(DEMANDRSN_CODE_WATER_BENEFIT_TAX, BigDecimal.ZERO);
		taxSum.put(DEMANDRSN_CODE_STREET_TAX, BigDecimal.ZERO);
		taxSum.put(DEMANDRSN_CODE_MUNICIPAL_EDUCATIONAL_CESS, BigDecimal.ZERO);
		taxSum.put(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD, BigDecimal.ZERO);
		taxSum.put(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD, BigDecimal.ZERO);
		taxSum.put(DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX, BigDecimal.ZERO);
		taxSum.put(DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX, BigDecimal.ZERO);
		taxSum.put(DEMANDRSN_CODE_PENALTY_FINES, BigDecimal.ZERO);
		taxSum.put(DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY, BigDecimal.ZERO);

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
		return userService.getUserByUsername((String) sessionMap
				.get(SESSION_VAR_LOGIN_USER_NAME));
	}

	/**
	 * @param user
	 *            the user whose department is to be returned
	 * @return department of the given user
	 */
	private Department getDepartmentOfUser(User user) {
		return getAssignment(user.getId()).getDeptId();
	}

	/**
	 * @param Integer
	 *            the userId
	 * @return Assignment for current date and for
	 *         <code> PersonalInformation </code>
	 */
	private Assignment getAssignment(Long userId) {
		PersonalInformation empForUserId = employeeService
				.getEmpForUserId(userId);
		Assignment assignmentByEmpAndDate = employeeService
				.getAssignmentByEmpAndDate(new Date(),
						empForUserId.getIdPersonalInformation());
		return assignmentByEmpAndDate;
	}

	public HashMap<String, Integer> generateOrderForDemandDetails(
			Set<EgDemandDetails> demandDetails,
			NMCPropertyTaxBillable nmcBillable) {

		HashMap<Integer, String> instReasonMap = new HashMap<Integer, String>();
		HashMap<String, Integer> orderMap = new HashMap<String, Integer>();
		BigDecimal balance = BigDecimal.ZERO;
		String key = "";
		String reasonMasterCode = null;

		for (EgDemandDetails demandDetail : demandDetails) {
			balance = BigDecimal.ZERO;
			balance = demandDetail.getAmount().subtract(
					demandDetail.getAmtCollected());

			if (balance.compareTo(BigDecimal.ZERO) == 1) {

				EgDemandReason reason = demandDetail.getEgDemandReason();
				Installment installment = reason.getEgInstallmentMaster();

				Calendar cal = Calendar.getInstance();
				cal.setTime(installment.getInstallmentYear());

				reasonMasterCode = reason.getEgDemandReasonMaster().getCode();

				if (reasonMasterCode.equals(DEMANDRSN_CODE_GENERAL_TAX)) {

					key = String.valueOf(getOrder(cal.get(Calendar.YEAR),
							DEMAND_REASON_ORDER_MAP.get(DEMANDRSN_REBATE)));
					instReasonMap.put(new Integer(key), cal.get(Calendar.YEAR)
							+ "-" + DEMANDRSN_REBATE);

					key = String.valueOf(getOrder(cal.get(Calendar.YEAR),
							DEMAND_REASON_ORDER_MAP.get(reasonMasterCode)
									.intValue()));
					instReasonMap.put(new Integer(key), cal.get(Calendar.YEAR)
							+ "-" + reasonMasterCode);

				} else {

					key = String.valueOf(getOrder(cal.get(Calendar.YEAR),
							DEMAND_REASON_ORDER_MAP.get(reasonMasterCode)
									.intValue()));
					instReasonMap.put(new Integer(key), cal.get(Calendar.YEAR)
							+ "-" + reasonMasterCode);
				}
			}
		}

		List<String> advanceYears = PropertyTaxUtil
				.getAdvanceYearsFromCurrentInstallment();
		Integer year = null;

		for (String description : advanceYears) {

			year = Integer.valueOf(description.substring(0, 4));

			/*
			 * key = String.valueOf(getOrder(year,
			 * DEMAND_REASON_ORDER_MAP.get(NMCPTISConstants
			 * .DEMANDRSN_CODE_ADVANCE_REBATE)));
			 * 
			 * instReasonMap.put(new Integer(key), year + "-" +
			 * NMCPTISConstants.DEMANDRSN_CODE_ADVANCE_REBATE);
			 */

			key = String.valueOf(getOrder(year, DEMAND_REASON_ORDER_MAP
					.get(NMCPTISConstants.DEMANDRSN_CODE_ADVANCE)));

			instReasonMap.put(new Integer(key), year + "-"
					+ NMCPTISConstants.DEMANDRSN_CODE_ADVANCE);
		}

		Calendar cal = Calendar.getInstance();

		BigDecimal penaltyAmount = BigDecimal.ZERO;

		for (Map.Entry<Installment, PropertyInstTaxBean> mapEntry : nmcBillable
				.getInstTaxBean().entrySet()) {

			penaltyAmount = mapEntry.getValue().getInstPenaltyAmt();
			boolean thereIsPenalty = (penaltyAmount != null && penaltyAmount
					.compareTo(BigDecimal.ZERO) > 0);

			if (thereIsPenalty) {

				cal.setTime(mapEntry.getKey().getInstallmentYear());

				key = Integer.valueOf(
						getOrder(cal.get(Calendar.YEAR),
								DEMAND_REASON_ORDER_MAP
										.get(DEMANDRSN_CODE_PENALTY_FINES)))
						.toString();
				instReasonMap.put(new Integer(key), cal.get(Calendar.YEAR)
						+ "-" + DEMANDRSN_CODE_PENALTY_FINES);
			}
		}

		int order = 1;
		Map<Integer, Map<String, String>> installmentAndReason = new TreeMap<Integer, Map<String, String>>();

		for (Map.Entry<Integer, String> entry : instReasonMap.entrySet()) {
			String[] split = entry.getValue().split("-");
			if (installmentAndReason.get(Integer.valueOf(split[0])) == null) {
				Map<String, String> reason = new HashMap<String, String>();
				reason.put(split[1], entry.getValue());
				installmentAndReason.put(Integer.valueOf(split[0]), reason);
			} else {
				installmentAndReason.get(Integer.valueOf(split[0])).put(
						split[1], entry.getValue());
			}
		}

		for (Integer installmentYear : installmentAndReason.keySet()) {
			if (installmentAndReason.get(installmentYear).get(
					DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY) != null) {
				orderMap.put(
						installmentAndReason.get(installmentYear).get(
								DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY), order++);
			}

			if (installmentAndReason.get(installmentYear).get(
					DEMANDRSN_CODE_PENALTY_FINES) != null) {
				orderMap.put(
						installmentAndReason.get(installmentYear).get(
								DEMANDRSN_CODE_PENALTY_FINES), order++);
			}
		}

		for (Integer installmentYear : installmentAndReason.keySet()) {
			for (String reasonCode : NMCPTISConstants.ORDERED_DEMAND_RSNS_LIST) {

				if (reasonCode.equalsIgnoreCase(DEMANDRSN_CODE_PENALTY_FINES)
						|| reasonCode
								.equalsIgnoreCase(DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY)) {
					continue;
				}

				if (installmentAndReason.get(installmentYear).get(reasonCode) != null) {
					orderMap.put(
							installmentAndReason.get(installmentYear).get(
									reasonCode), order++);
				}
			}
		}

		return orderMap;

	}

	/**
	 * @param sessionMap
	 *            map of session variables
	 *
	 * @return departments of currently logged in user
	 */
	public List<Department> getDepartmentsForLoggedInUser(
			Map<String, Object> sessionMap) {
		Department dept = getDepartmentOfUser(getLoggedInUser(sessionMap));
		List<Department> departments = persistenceService.findAllByNamedQuery(
				QUERY_DEPARTMENTS_BY_DEPTCODE, dept.getCode());
		return departments;
	}

	public DesignationMaster getDesignationForUser(Integer userId) {
		Position position = null;
		DesignationMaster designation = null;
		if (userId != null && userId.intValue() != 0) {
			position = eisCommonsService.getPositionByUserId(userId);
			designation = position.getDeptDesigId().getDesigId();
		}
		return designation;
	}

	public EgBillType getBillTypeByCode(String typeCode) {
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
	public Map<Installment, BigDecimal> prepareRsnWiseDemandForProp(
			Property property) {
		Installment inst = null;
		Map<Installment, BigDecimal> instAmountMap = new TreeMap<Installment, BigDecimal>();
		EgDemand egDemand = ptDemandDao
				.getNonHistoryCurrDmdForProperty(property);
		String demandReason = "";
		BigDecimal amount = BigDecimal.ZERO;

		List<String> demandReasonExcludeList = Arrays.asList(
				DEMANDRSN_CODE_PENALTY_FINES,
				NMCPTISConstants.DEMANDRSN_CODE_ADVANCE);

		for (EgDemandDetails dmdDet : egDemand.getEgDemandDetails()) {

			amount = BigDecimal.ZERO;
			demandReason = dmdDet.getEgDemandReason().getEgDemandReasonMaster()
					.getCode();

			if (!demandReasonExcludeList.contains(demandReason)) {
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
	public Map<Installment, BigDecimal> prepareRsnWiseCollForProp(
			Property property) {
		Installment inst = null;
		Map<Installment, BigDecimal> instCollMap = new HashMap<Installment, BigDecimal>();
		EgDemand egDemand = ptDemandDao
				.getNonHistoryCurrDmdForProperty(property);
		String demandReason = "";
		BigDecimal amount = BigDecimal.ZERO;
		BigDecimal collection = BigDecimal.ZERO;
		for (EgDemandDetails dmdDet : egDemand.getEgDemandDetails()) {
			amount = BigDecimal.ZERO;
			collection = dmdDet.getAmtCollected();
			demandReason = dmdDet.getEgDemandReason().getEgDemandReasonMaster()
					.getCode();
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

	public void consolidateUnitTaxCalcInfos(TaxCalculationInfo taxCalcInfo,
			Installment installment, String propertyTypeCategory,
			String amenities, Property property,
			List<BoundaryCategory> boundaryCategories) throws ParseException {
		LOGGER.debug("Entered into consolidateUnitTaxCalcInfo");

		taxCalcInfo.setTotalTaxPayable(ZERO);
		taxCalcInfo.setTotalAnnualLettingValue(ZERO);

		// Used to group UnitTaxCalculationInfo's by UnitNo
		Map<Integer, List<UnitTaxCalculationInfo>> unitTaxesByUnitNo = new TreeMap<Integer, List<UnitTaxCalculationInfo>>();
		Map<Integer, List<UnitTaxCalculationInfo>> combinedUnitTaxesByUnitNoForMultipleALV = new TreeMap<Integer, List<UnitTaxCalculationInfo>>();

		// holds the consolidated UnitTaxCalculationInfo for UnitNo
		Map<Integer, UnitTaxCalculationInfo> consolidatedUnitTaxForUnitNo = new TreeMap<Integer, UnitTaxCalculationInfo>();
		List<List<UnitTaxCalculationInfo>> unitTaxes = taxCalcInfo
				.getUnitTaxCalculationInfos();
		// Collections.sort(unitTaxes,
		// UnitTaxInfoComparator.getUnitComparator(UnitTaxInfoComparator.UNIT_SORT));

		// holds the unit numbers which are consolidated
		Set<Integer> consolidatedUnitNos = new TreeSet<Integer>();
		Map<Integer, Map<String, BigDecimal>> unitNoWiseTaxAndALV = new TreeMap<Integer, Map<String, BigDecimal>>();
		Map<Date, BoundaryCategory> boundaryCategoryByDate = new TreeMap<Date, BoundaryCategory>();

		for (BoundaryCategory bc : boundaryCategories) {
			boundaryCategoryByDate.put(bc.getFromDate(), bc);
		}

		// Does the grouping of UnitTaxCalculationInfo's by UnitNo
		for (List<UnitTaxCalculationInfo> unitTaxCalcs : unitTaxes) {

			UnitTaxCalculationInfo unit = null;

			if (unitTaxCalcs.size() > 1) {

				UnitTaxCalculationInfo unitClone = getUnitTaxCalcInfoWhenMultipleALV(unitTaxCalcs);

				if (combinedUnitTaxesByUnitNoForMultipleALV.get(unitClone
						.getUnitNumber()) == null) {
					List<UnitTaxCalculationInfo> singleUnit = new ArrayList<UnitTaxCalculationInfo>();
					singleUnit.add(unitClone);
					combinedUnitTaxesByUnitNoForMultipleALV.put(
							unitClone.getUnitNumber(), singleUnit);
				} else {
					combinedUnitTaxesByUnitNoForMultipleALV.get(
							unitClone.getUnitNumber()).add(unitClone);
				}

			} else {
				unit = unitTaxCalcs.get(0);

				if (unitTaxesByUnitNo.get(unit.getUnitNumber()) == null) {
					List<UnitTaxCalculationInfo> singleUnit = new ArrayList<UnitTaxCalculationInfo>();
					singleUnit.add(unit);
					unitTaxesByUnitNo.put(unit.getUnitNumber(), singleUnit);
				} else {
					unitTaxesByUnitNo.get(unit.getUnitNumber()).add(unit);
				}
			}
		}

		LOGGER.debug("Unique unit numbers: " + unitTaxesByUnitNo.keySet());

		Date unitDate = null;
		Date consolidatedUnitDate = null;
		DateFormat dateFormat = new SimpleDateFormat(
				PropertyTaxConstants.DATE_FORMAT_DDMMYYY);

		for (Integer unitNo : unitTaxesByUnitNo.keySet()) {
			List<UnitTaxCalculationInfo> units = unitTaxesByUnitNo.get(unitNo);

			UnitTaxCalculationInfo consolidating = null;
			Map<String, BigDecimal> taxNameAndALV = new TreeMap<String, BigDecimal>();

			for (UnitTaxCalculationInfo unit : units) {

				// if (!unit.getFloorNumber().equalsIgnoreCase("Open Plot") ||
				// boundaryCategoryByDate.size() == 1) {

				UnitTaxCalculationInfo unitClone = getUnitTaxCalculationInfoClone(unit);

				if (isNull(consolidatedUnitTaxForUnitNo.get(unitNo))) {

					prepareTaxNameAndALV(taxNameAndALV, unitClone);
					if (!taxNameAndALV.isEmpty()) {
						consolidatedUnitTaxForUnitNo.put(unitNo, unitClone);
					}

				} else {
					consolidatedUnitNos.add(unitNo);
					consolidating = consolidatedUnitTaxForUnitNo.get(unitNo);

					unitDate = dateFormat.parse(unit.getInstDate());
					consolidatedUnitDate = dateFormat.parse(consolidating
							.getInstDate());

					if (unitDate.before(consolidatedUnitDate)) {
						consolidating.setInstDate(unit.getInstDate());
					}

					Boolean isUnitsMatch = PropertyTaxUtil.getIsUnitsMatch(
							consolidating, PROPERTYTYPE_STR_TO_CODE
									.get(taxCalcInfo.getPropertyType()),
							Boolean.FALSE, unit);

					/**
					 * getting isUnitsMatch value to prevent summing up of
					 * unitArea when multiple base rents are applied for the
					 * same unit in the installment
					 */
					if (isNotNull(consolidating.getUnitArea()) && !isUnitsMatch) {
						consolidating
								.setUnitArea(isNotNull(unit.getUnitArea()) ? consolidating
										.getUnitArea().add(unit.getUnitArea())
										: consolidating.getUnitArea().add(ZERO));
					} else {
						if (isNull(consolidating.getUnitArea())) {
							consolidating.setUnitArea(unit.getUnitArea());
						}
					}

					if (isNotNull(consolidating.getBaseRent())) {
						consolidating
								.setBaseRent(isNotNull(unit.getBaseRent()) ? consolidating
										.getBaseRent().add(unit.getBaseRent())
										: consolidating.getBaseRent().add(ZERO));
					} else {
						consolidating
								.setBaseRent(isNotNull(unit.getBaseRent()) ? ZERO
										.add(unit.getBaseRent()) : ZERO);
					}

					if (isNotNull(unit.getBaseRentEffectiveDate())
							&& isNotNull(consolidating
									.getBaseRentEffectiveDate())
							&& unit.getBaseRentEffectiveDate().before(
									consolidating.getBaseRentEffectiveDate())) {
						consolidating.setBaseRentEffectiveDate(new Date(unit
								.getBaseRentEffectiveDate().getTime()));
					}

					if (isNotNull(consolidating.getBaseRentPerSqMtPerMonth())) {
						consolidating.setBaseRentPerSqMtPerMonth(isNotNull(unit
								.getBaseRentPerSqMtPerMonth()) ? consolidating
								.getBaseRentPerSqMtPerMonth().add(
										unit.getBaseRentPerSqMtPerMonth())
								: consolidating.getBaseRentPerSqMtPerMonth()
										.add(ZERO));
					} else {
						consolidating.setBaseRentPerSqMtPerMonth(isNotNull(unit
								.getBaseRentPerSqMtPerMonth()) ? ZERO.add(unit
								.getBaseRentPerSqMtPerMonth()) : ZERO);
					}
					if (isNotNull(consolidating.getMonthlyRent())) {
						consolidating.setMonthlyRent(isNotNull(unit
								.getMonthlyRent()) ? consolidating
								.getMonthlyRent().add(unit.getMonthlyRent())
								: consolidating.getMonthlyRent().add(ZERO));
					} else {
						consolidating.setMonthlyRent(isNotNull(unit
								.getMonthlyRent()) ? ZERO.add(unit
								.getMonthlyRent()) : ZERO);
					}
					if (isNotNull(consolidating.getAnnualRentBeforeDeduction())) {
						consolidating
								.setAnnualRentBeforeDeduction(isNotNull(unit
										.getAnnualRentBeforeDeduction()) ? consolidating
										.getAnnualRentBeforeDeduction()
										.add(unit
												.getAnnualRentBeforeDeduction())
										: consolidating
												.getAnnualRentBeforeDeduction()
												.add(ZERO));
					} else {
						consolidating
								.setAnnualRentBeforeDeduction(isNotNull(unit
										.getAnnualRentBeforeDeduction()) ? ZERO.add(unit
										.getAnnualRentBeforeDeduction())
										: ZERO);
					}

					// if (consolidatedALV.compareTo(BigDecimal.ZERO) > 0) {
					consolidating.setAnnualRentAfterDeduction(consolidating
							.getAnnualRentAfterDeduction().add(
									unit.getAnnualRentAfterDeduction()));
					// }

					for (MiscellaneousTax mt : unit.getMiscellaneousTaxes()) {

						if (isNull(taxNameAndALV.get(mt.getTaxName()))) {
							MiscellaneousTax mtx = new MiscellaneousTax(mt);

							for (MiscellaneousTaxDetail mtd : mt
									.getTaxDetails()) {
								MiscellaneousTaxDetail txDetail = new MiscellaneousTaxDetail(
										mtd);
								mtx.getTaxDetails().add(txDetail);
							}

							consolidating.addMiscellaneousTaxes(mtx);
						}
					}

					consolidating.setTaxExemptionReason(unit
							.getTaxExemptionReason());

					prepareTaxNameAndALV(taxNameAndALV, unitClone);

					addAreaTaxCalculationInfosClone(unit, consolidating);
					consolidatedUnitTaxForUnitNo.put(unitNo, consolidating);
				}
				unitNoWiseTaxAndALV.put(unitNo, taxNameAndALV);
				// }
			}
		}

		LOGGER.info("Consolidated Unit Nos: " + consolidatedUnitNos);
		Set<Integer> unitNos = (consolidatedUnitNos.isEmpty()) ? unitTaxesByUnitNo
				.keySet() : consolidatedUnitNos;
		LOGGER.debug("Unit Nos: " + unitNos);
		Set<String> applicableTaxes = new HashSet<String>();

		for (Integer unitNo : unitNos) {

			UnitTaxCalculationInfo unitTaxCalcInfo = consolidatedUnitTaxForUnitNo
					.get(unitNo);

			applicableTaxes.clear();

			for (MiscellaneousTax tax : unitTaxCalcInfo.getMiscellaneousTaxes()) {
				for (MiscellaneousTaxDetail taxDetail : tax.getTaxDetails()) {
					if (isNull(taxDetail.getIsHistory())
							|| taxDetail.getIsHistory().equals('N')) {
						applicableTaxes.add(tax.getTaxName());
					}
				}
			}

			unitTaxCalcInfo.getMiscellaneousTaxes().clear();
			calculateApplicableTaxes(new ArrayList<String>(applicableTaxes),
					unitNoWiseTaxAndALV.get(unitNo), unitTaxCalcInfo,
					installment, property.getPropertyDetail()
							.getPropertyTypeMaster().getCode(),
					propertyTypeCategory, amenities, property, null,
					taxCalcInfo);

			if (combinedUnitTaxesByUnitNoForMultipleALV.get(unitNo) == null) {
				setResdAndNonResdParts(unitNoWiseTaxAndALV.get(unitNo),
						unitTaxCalcInfo);
				taxCalcInfo.setTotalTaxPayable(taxCalcInfo.getTotalTaxPayable()
						.add(unitTaxCalcInfo.getTotalTaxPayable()));
			}

			taxCalcInfo.setTotalAnnualLettingValue(taxCalcInfo
					.getTotalAnnualLettingValue().add(
							unitTaxCalcInfo.getAnnualRentAfterDeduction()));
		}

		Map<Integer, UnitTaxCalculationInfo> combinedUnitTaxByUnitNoForMultipleALV = new TreeMap<Integer, UnitTaxCalculationInfo>();

		BigDecimal alv = BigDecimal.ZERO;

		for (Integer unitNo : combinedUnitTaxesByUnitNoForMultipleALV.keySet()) {
			UnitTaxCalculationInfo unit = getUnitTaxCalcInfoWhenMultipleALV(combinedUnitTaxesByUnitNoForMultipleALV
					.get(unitNo));

			alv = BigDecimal.ZERO;

			for (UnitTaxCalculationInfo un : combinedUnitTaxesByUnitNoForMultipleALV
					.get(unitNo)) {
				alv = alv.add(un.getAnnualRentAfterDeduction());
			}

			unit.setAnnualRentAfterDeduction(alv);
			combinedUnitTaxByUnitNoForMultipleALV.put(unitNo, unit);
		}

		for (Integer unitNo : combinedUnitTaxesByUnitNoForMultipleALV.keySet()) {
			UnitTaxCalculationInfo consolidatedUnit = consolidatedUnitTaxForUnitNo
					.get(unitNo);
			UnitTaxCalculationInfo combinedTaxesUnit = combinedUnitTaxByUnitNoForMultipleALV
					.get(unitNo);

			if (isNull(consolidatedUnit)) {
				consolidatedUnitTaxForUnitNo.put(unitNo, combinedTaxesUnit);
				Map<String, BigDecimal> taxNameAndALV = new TreeMap<String, BigDecimal>();
				prepareTaxNameAndALV(taxNameAndALV, combinedTaxesUnit);
				unitNoWiseTaxAndALV.put(unitNo, taxNameAndALV);
				setResdAndNonResdParts(unitNoWiseTaxAndALV.get(unitNo),
						combinedTaxesUnit);
				taxCalcInfo.setTotalTaxPayable(taxCalcInfo.getTotalTaxPayable()
						.add(combinedTaxesUnit.getTotalTaxPayable()));
			} else {

				consolidatedUnit.setAnnualRentBeforeDeduction(consolidatedUnit
						.getAnnualRentBeforeDeduction().add(
								combinedTaxesUnit
										.getAnnualRentBeforeDeduction()));
				consolidatedUnit.setAnnualRentAfterDeduction(consolidatedUnit
						.getAnnualRentAfterDeduction()
						.add(combinedTaxesUnit.getAnnualRentAfterDeduction()));

				for (MiscellaneousTax miscTax : consolidatedUnit
						.getMiscellaneousTaxes()) {
					for (MiscellaneousTax mt : combinedTaxesUnit
							.getMiscellaneousTaxes()) {
						if (miscTax.getTaxName().equalsIgnoreCase(
								mt.getTaxName())) {
							miscTax.setTotalCalculatedTax(miscTax
									.getTotalCalculatedTax().add(
											mt.getTotalCalculatedTax()));
							for (MiscellaneousTaxDetail mtd : mt
									.getTaxDetails()) {
								MiscellaneousTaxDetail taxDetail = new MiscellaneousTaxDetail(
										mtd);
								miscTax.addMiscellaneousTaxDetail(taxDetail);
							}
							break;
						}
					}
				}

				// Setting this because New Base rent is effective
				consolidatedUnit.setBaseRentEffectiveDate(combinedTaxesUnit
						.getBaseRentEffectiveDate());
				addAreaTaxCalculationInfosClone(combinedTaxesUnit,
						consolidatedUnit);

				prepareTaxNameAndALV(unitNoWiseTaxAndALV.get(unitNo),
						combinedTaxesUnit);
				setResdAndNonResdParts(unitNoWiseTaxAndALV.get(unitNo),
						consolidatedUnit);

				consolidatedUnit.setTotalTaxPayable(consolidatedUnit
						.getTotalTaxPayable().add(
								combinedTaxesUnit.getTotalTaxPayable()));
				taxCalcInfo.setTotalTaxPayable(taxCalcInfo.getTotalTaxPayable()
						.add(consolidatedUnit.getTotalTaxPayable()));
			}

			taxCalcInfo.setTotalAnnualLettingValue(taxCalcInfo
					.getTotalAnnualLettingValue().add(
							combinedTaxesUnit.getAnnualRentAfterDeduction()));
		}

		taxCalcInfo.getConsolidatedUnitTaxCalculationInfo().addAll(
				consolidatedUnitTaxForUnitNo.values());

		LOGGER.debug("Exiting from consolidateUnitTaxCalcInfo");
	}

	/**
	 *
	 * When the unit has multiple Base Rents effective, then there will be a
	 * UniTaxCalculationInfo for each Base Rent, so creating a
	 * UnitTaxCalculationInfo out of multiple UnitTaxCalculationInfo's by
	 * summing up the total calculated taxes.
	 *
	 * @param unitTaxCalcs
	 * @return
	 */
	private UnitTaxCalculationInfo getUnitTaxCalcInfoWhenMultipleALV(
			List<UnitTaxCalculationInfo> unitTaxCalcs) {

		/*
		 * getting the unitTaxCalcs.size() - 1 UnitTaxCalculationInfo in order
		 * to get the latest ALV which is the effect of recent base rent value,
		 * 
		 * Example base rent effective date 01-04-1988, 01-10-2002 so the list
		 * contains the tax calculations for the given dates in order, so
		 * getting the latest base rent effective calculation details
		 */
		UnitTaxCalculationInfo unitClone = getUnitTaxCalculationInfoClone(unitTaxCalcs
				.get(unitTaxCalcs.size() - 1));

		Set<String> unitCloneTaxes = new HashSet<String>();

		unitCloneTaxes = getNonHistoryTaxes(unitClone);

		int i = 0;

		for (UnitTaxCalculationInfo u : unitTaxCalcs) {

			if (i < (unitTaxCalcs.size() - 1)) {

				unitClone.setTotalTaxPayable(unitClone.getTotalTaxPayable()
						.add(u.getTotalTaxPayable()));

				for (MiscellaneousTax miscTax : u.getMiscellaneousTaxes()) {

					/**
					 * This is when tax is not applicable in the recent alv
					 * effective date Ex: Water tax was applicable from
					 * 01-04-2002 and its removed when property modified with
					 * occupancy 01-10-2002
					 *
					 * So for, 2002-03 installment we will be having 2 unittax
					 * info's for 1st unit tax alv effective 01-04-1988 with
					 * water tax details for 2nd unit tax alv effective
					 * 01-10-2002 without water tax details and
					 * <code> unitClone </code> is the clone of 2nd unit tax so
					 * adding the water tax detials
					 */
					if (unitCloneTaxes.contains(miscTax.getTaxName())) {
						for (MiscellaneousTax mt : unitClone
								.getMiscellaneousTaxes()) {

							if (miscTax.getTaxName().equalsIgnoreCase(
									mt.getTaxName())) {
								mt.setTotalCalculatedTax(mt
										.getTotalCalculatedTax()
										.add(miscTax.getTotalCalculatedTax()));
								for (MiscellaneousTaxDetail mtd : miscTax
										.getTaxDetails()) {
									MiscellaneousTaxDetail taxDetail = new MiscellaneousTaxDetail(
											mtd);
									mt.getTaxDetails().add(i, taxDetail);
								}

								break;
							}
						}
					} else {
						MiscellaneousTax miscTaxClone = new MiscellaneousTax(
								miscTax);
						for (MiscellaneousTaxDetail mtd : miscTax
								.getTaxDetails()) {
							MiscellaneousTaxDetail txDetail = new MiscellaneousTaxDetail(
									mtd);
							miscTaxClone.getTaxDetails().add(txDetail);
						}
						unitClone.getMiscellaneousTaxes().add(miscTaxClone);
					}
				}

			}

			i++;
		}

		return unitClone;
	}

	public void prepareTaxNameAndALV(Map<String, BigDecimal> taxNameAndALV,
			UnitTaxCalculationInfo unit) {
		LOGGER.debug("Entered into prepareTaxNameAndALV");
		LOGGER.debug("prepareTaxNameAndALV - Inputs: taxNameAndALV: "
				+ taxNameAndALV);

		for (MiscellaneousTax miscTax : unit.getMiscellaneousTaxes()) {
			for (MiscellaneousTaxDetail taxDetail : miscTax.getTaxDetails()) {
				if (taxDetail.getIsHistory() == null
						|| taxDetail.getIsHistory().equals('N')) {
					if (taxNameAndALV.get(miscTax.getTaxName()) == null) {
						taxNameAndALV.put(miscTax.getTaxName(),
								unit.getAnnualRentAfterDeduction());
					} else {
						taxNameAndALV.put(
								miscTax.getTaxName(),
								taxNameAndALV.get(miscTax.getTaxName()).add(
										unit.getAnnualRentAfterDeduction()));
					}
					break;
				}
			}
		}

		LOGGER.debug("prepareTaxNameAndALV - afterPrepare taxNameAndALV: "
				+ taxNameAndALV);
		LOGGER.debug("Exiting from prepareTaxNameAndALV");
	}

	public Map<String, BigDecimal> prepareTaxNameAndALV(
			Map<String, BigDecimal> taxNameAndALV,
			FloorwiseDemandCalculations floorDmdCalc,
			Set<String> applicableTaxes) {
		LOGGER.debug("Entered into prepareTaxNameAndALV");
		LOGGER.debug("prepareTaxNameAndALV - Inputs: taxNameAndALV: "
				+ taxNameAndALV);

		for (String taxName : applicableTaxes) {
			putInTaxNameAndALV(taxNameAndALV, taxName, floorDmdCalc.getAlv());
		}

		LOGGER.debug("prepareTaxNameAndALV - afterPrepare taxNameAndALV: "
				+ taxNameAndALV);
		LOGGER.debug("Exiting from prepareTaxNameAndALV");
		return taxNameAndALV;
	}

	/**
	 * @param taxNameAndALV
	 */
	private void putInTaxNameAndALV(Map<String, BigDecimal> taxNameAndALV,
			String taxName, BigDecimal alv) {
		if (taxNameAndALV.get(taxName) == null) {
			taxNameAndALV.put(taxName, alv);
		} else {
			taxNameAndALV.put(taxName, taxNameAndALV.get(taxName).add(alv));
		}
	}

	public UnitTaxCalculationInfo getUnitTaxCalculationInfoClone(
			UnitTaxCalculationInfo unit) {
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
		clone.setManualAlv(unit.getManualAlv());
		clone.setBigBuildingTaxALV(unit.getBigBuildingTaxALV());

		if (unit.getBaseRentEffectiveDate() != null) {
			clone.setBaseRentEffectiveDate(new Date(unit
					.getBaseRentEffectiveDate().getTime()));
		}

		if (unit.getHasALVChanged() == null) {
			clone.setHasALVChanged(Boolean.FALSE);
		} else {
			clone.setHasALVChanged(unit.getHasALVChanged());
		}

		clone.setIsHistory(unit.getIsHistory());
		clone.setPropertyCreatedDate(unit.getPropertyCreatedDate());
		clone.setTaxExemptionReason(unit.getTaxExemptionReason());

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

	public TaxCalculationInfo getTaxCalculationInfoClone(
			TaxCalculationInfo taxCalInfo) {
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
		clone.setTotalAnnualLettingValue(taxCalInfo
				.getTotalAnnualLettingValue());
		clone.setTotalTaxPayable(taxCalInfo.getTotalTaxPayable());
		clone.setWard(taxCalInfo.getWard());
		clone.setZone(taxCalInfo.getZone());

		addUnitTaxCalculationInfoClone(taxCalInfo, clone);
		addConsolidatedUnitTaxCalculationInfoClone(taxCalInfo, clone);
		addConsolidatedUnitTaxCalReportClone(taxCalInfo, clone);
		return clone;
	}

	private void addConsolidatedUnitTaxCalReportClone(
			TaxCalculationInfo taxCalInfo, TaxCalculationInfo clone) {
		for (ConsolidatedUnitTaxCalReport unitTaxCalReport : taxCalInfo
				.getConsolidatedUnitTaxCalReportList()) {
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
		newUnitTaxCalReport.setAnnualLettingValue(unitTaxCalReport
				.getAnnualLettingValue());
		newUnitTaxCalReport.setAnnualRentBeforeDeduction(unitTaxCalReport
				.getAnnualRentBeforeDeduction());
		newUnitTaxCalReport.setDeductionAmount(unitTaxCalReport
				.getDeductionAmount());
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
	private void addUnitTaxInfoClone(
			ConsolidatedUnitTaxCalReport unitTaxCalReport,
			ConsolidatedUnitTaxCalReport newUnitTaxCalReport) {
		/*
		 * for (UnitTaxCalculationInfo unitInfo :
		 * unitTaxCalReport.getUnitTaxCalInfo()) { UnitTaxCalculationInfo
		 * newUnitInfo = getUnitTaxCalculationInfoClone(unitInfo);
		 * newUnitTaxCalReport.addUnitTaxCalInfo(newUnitInfo); }
		 */
	}

	/**
	 * Adds the ConsolidatedUnitTaxCalculationInfo clones to clone
	 *
	 * @param taxCalInfo
	 * @param clone
	 */
	private void addConsolidatedUnitTaxCalculationInfoClone(
			TaxCalculationInfo taxCalInfo, TaxCalculationInfo clone) {
		for (UnitTaxCalculationInfo unitInfo : taxCalInfo
				.getConsolidatedUnitTaxCalculationInfo()) {
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
	private void addUnitTaxCalculationInfoClone(TaxCalculationInfo taxCalInfo,
			TaxCalculationInfo clone) {
		List<List<UnitTaxCalculationInfo>> units = new ArrayList<List<UnitTaxCalculationInfo>>();

		for (List<UnitTaxCalculationInfo> unitInfos : taxCalInfo
				.getUnitTaxCalculationInfos()) {
			List<UnitTaxCalculationInfo> unitsByDate = new ArrayList<UnitTaxCalculationInfo>();

			for (UnitTaxCalculationInfo unitInfo : unitInfos) {
				UnitTaxCalculationInfo newUnitInfo = getUnitTaxCalculationInfoClone(unitInfo);
				unitsByDate.add(newUnitInfo);
			}
			units.add(unitsByDate);
		}
		clone.setUnitTaxCalculationInfo(units);
	}

	/**
	 * Adds the AreaTaxCalculationInfo clones to clone
	 *
	 * @param unit
	 * @param clone
	 */
	public void addAreaTaxCalculationInfosClone(UnitTaxCalculationInfo unit,
			UnitTaxCalculationInfo clone) {
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
	public void addMiscellaneousTaxesClone(UnitTaxCalculationInfo unit,
			UnitTaxCalculationInfo clone) {
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
				newMiscTaxDetail.setActualTaxValue(miscTaxDetail
						.getActualTaxValue());
				newMiscTaxDetail.setCalculatedTaxValue(miscTaxDetail
						.getCalculatedTaxValue());
				// newMiscTaxDetail.setHasChanged(miscTaxDetail.getHasChanged());
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(miscTaxDetail.getFromDate());
				newMiscTaxDetail.setFromDate(calendar.getTime());
				newMiscTaxDetail.setNoOfDays(miscTaxDetail.getNoOfDays());
				newMiscTaxDetail.setIsHistory(miscTaxDetail.getIsHistory());
				newMiscTaxDetail.setHistoryALV(miscTaxDetail.getHistoryALV());
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

	private void setResdAndNonResdParts(Map<String, BigDecimal> taxNameAndALV,
			UnitTaxCalculationInfo unitTax) {

		BigDecimal eduCessResd = taxNameAndALV
				.get(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD);
		BigDecimal eduCessNonResd = taxNameAndALV
				.get(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD);

		if (eduCessResd != null) {
			unitTax.setResidentialALV(unitTax.getResidentialALV().add(
					eduCessResd.setScale(2, BigDecimal.ROUND_HALF_UP)));
		}

		if (eduCessNonResd != null) {
			unitTax.setNonResidentialALV(unitTax.getNonResidentialALV().add(
					eduCessNonResd.setScale(2, BigDecimal.ROUND_HALF_UP)));
		}

		for (MiscellaneousTax miscTax : unitTax.getMiscellaneousTaxes()) {
			if (DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD.equals(miscTax
					.getTaxName())) {
				unitTax.setResdEduCess(unitTax.getResdEduCess().add(
						miscTax.getTotalCalculatedTax()));
			} else if (DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD.equals(miscTax
					.getTaxName())) {
				unitTax.setNonResdEduCess(unitTax.getNonResdEduCess().add(
						miscTax.getTotalCalculatedTax()));
			} else if (DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX.equals(miscTax
					.getTaxName())) {
				unitTax.setEgCess(unitTax.getEgCess().add(
						miscTax.getTotalCalculatedTax()));
			}
		}
	}

	/**
	 * Gives the current installment
	 *
	 * @return Installment the current installment for PT module
	 */
	public static Installment getCurrentInstallment() {
		Module module = moduleDao
				.getModuleByName(PropertyTaxConstants.PTMODULENAME);
		return installmentDao.getInsatllmentByModuleForGivenDate(module,
				new Date());
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
		LOGGER.debug("Entered into getNumberOfDays, fromDate=" + fromDate
				+ ", toDate=" + toDate);
		Calendar fromDateCalendar = Calendar.getInstance();
		Calendar toDateCalendar = Calendar.getInstance();
		fromDateCalendar.setTime(fromDate);
		toDateCalendar.setTime(toDate);
		Long days = 0L;
		while (fromDateCalendar.before(toDateCalendar)) {
			fromDateCalendar.add(Calendar.DAY_OF_MONTH, 1);
			days++;
		}
		LOGGER.debug("getNumberOfDays - days: " + days);
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
		return ((date.after(fromDate) || date.equals(fromDate))
				&& date.before(toDate) || date.equals(toDate));
	}

	public Boolean betweenOrBefore(Date date, Date fromDate, Date toDate) {
		Boolean result = between(date, fromDate, toDate)
				|| date.before(fromDate);
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
	public static int getMonthsBetweenDates(final Date fromDate,
			final Date toDate) {
		LOGGER.debug("Entered into getMonthsBetweenDates - fromDate: "
				+ fromDate + ", toDate: " + toDate);
		final Calendar fromDateCalendar = Calendar.getInstance();
		final Calendar toDateCalendar = Calendar.getInstance();
		fromDateCalendar.setTime(fromDate);
		toDateCalendar.setTime(toDate);
		final int yearDiff = toDateCalendar.get(Calendar.YEAR)
				- fromDateCalendar.get(Calendar.YEAR);
		int noOfMonths = yearDiff * 12 + toDateCalendar.get(Calendar.MONTH)
				- fromDateCalendar.get(Calendar.MONTH);
		noOfMonths += 1;
		LOGGER.debug("Exiting from getMonthsBetweenDates - noOfMonths: "
				+ noOfMonths);
		return noOfMonths;
	}

	public List<PropertyArrearBean> getPropertyArrears(
			List<PropertyArrear> arrears) {
		List<PropertyArrearBean> propArrears = new ArrayList<PropertyArrearBean>();
		PropertyArrearBean propArrBean = null;
		for (PropertyArrear pa : arrears) {
			propArrBean = new PropertyArrearBean();
			String key = pa.getFromDate().toString().concat("-")
					.concat(pa.getToDate().toString());
			BigDecimal value = BigDecimal.ZERO;
			value = value.add(pa.getGeneralTax()).add(pa.getSewerageTax())
					.add(pa.getFireServiceTax()).add(pa.getLightingTax())
					.add(pa.getGeneralWaterTax()).add(pa.getEducationCess())
					.add(pa.getEgCess()).add(pa.getBigResidentailTax())
					.setScale(2, ROUND_HALF_UP);
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
	 * Returns true if Water Tax was Imposed before Modification but not after
	 * Modification
	 *
	 * @param installment
	 * @param newProperty
	 * @return
	 * @throws ParseException
	 */
	private Boolean isWaterTaxImposedOnModify(Installment installment,
			Property newProperty, UnitTaxCalculationInfo unitTax)
			throws ParseException {
		LOGGER.debug("Entered into isWaterTaxImposedOnModify");
		LOGGER.debug("isWaterTaxImposedOnModify - newProperty: " + newProperty);

		Property oldProperty = newProperty.getBasicProperty().getProperty();

		LOGGER.debug("isWaterTaxImposedOnModify - oldProperty: " + oldProperty);

		Date oldPropertyEffectiveDate = PropertyTaxUtil
				.getEffectiveDateIfWaterTaxImposedForProperty(oldProperty,
						unitTax);
		Date newPropertyEffectiveDate = null;
		LOGGER.debug("isWaterTaxImposedOnModify - oldPropertyEffectiveDate: "
				+ oldPropertyEffectiveDate);
		if (oldPropertyEffectiveDate != null) {
			newPropertyEffectiveDate = PropertyTaxUtil
					.getEffectiveDateIfWaterTaxImposedForProperty(newProperty,
							unitTax);
			LOGGER.debug("isWaterTaxImposedOnModify - newPropertyEffectiveDate: "
					+ newPropertyEffectiveDate);
			// null here indicates Water Tax is not imposed
			if (newPropertyEffectiveDate == null) {
				return installment.getFromDate().before(
						unitTax.getOccpancyDate());
			}
		}

		return false;
	}

	/**
	 * Gives the Effective Date if Water Tax has been imposed for the property
	 *
	 * @param property
	 * @return Property Occupancy date, if floors exists then minimum date of
	 *         Floors effective date
	 * @throws ParseException
	 */
	private static Date getEffectiveDateIfWaterTaxImposedForProperty(
			Property property, UnitTaxCalculationInfo unitTax)
			throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat(
				PropertyTaxConstants.DATE_FORMAT_DDMMYYY);
		String propTypeCode = property.getPropertyDetail()
				.getPropertyTypeMaster().getCode();
		Boolean isGovtProperty = PROPTYPE_STATE_GOVT
				.equalsIgnoreCase(propTypeCode)
				|| PROPTYPE_CENTRAL_GOVT.equalsIgnoreCase(propTypeCode);
		if (isGovtProperty
				|| property.getPropertyDetail().getFloorDetails().isEmpty()) {
			if (GWR_IMPOSED.equalsIgnoreCase(property.getPropertyDetail()
					.getExtra_field1())) {
				return property.getPropertyDetail().getEffective_date();
			}
		} else {
			Date minEffectiveDate = null;
			Date tempDate = null;
			for (FloorIF floor : property.getPropertyDetail().getFloorDetails()) {
				if (unitTax.getUnitNumber() != null
						&& unitTax.getFloorNumberInteger() != null) {
					if ((unitTax.getUnitNumber().equals(
							Integer.parseInt(floor.getExtraField1())) && unitTax
							.getFloorNumberInteger().equals(floor.getFloorNo()))
							&& (floor.getWaterRate() != null && GWR_IMPOSED
									.equalsIgnoreCase(floor.getWaterRate()))) {

						if (floor.getExtraField3() == null) {
							LOGGER.info("Floor effective date (floor.extraField3) is null");
						} else {
							return dateFormat.parse(floor.getExtraField3());
						}
					}
				} else {
					if (floor.getWaterRate() != null
							&& GWR_IMPOSED.equalsIgnoreCase(floor
									.getWaterRate())) {
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
	public UnitTaxCalculationInfo calculateAnnualTaxes(String propertyType,
			String propertyTypCategory, UnitTaxCalculationInfo unit,
			Installment installment, String amenities,
			Map<Installment, TaxCalculationInfo> taxCalcInfos,
			Boolean isHistoryTaxEffective) {

		LOGGER.debug("Entered into calculateAnnualTaxes");

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("calculateAnnualTaxes - propertyType: " + propertyType
					+ ", propertyTypeCategory: " + propertyTypCategory
					+ ", ALV: " + unit.getAnnualRentAfterDeduction()
					+ ", installment: " + installment + ", amenities: "
					+ amenities);
		}

		BigDecimal totalTax = BigDecimal.ZERO;
		BigDecimal alv = BigDecimal.ZERO;
		Boolean isUseHistoryALV = Boolean.FALSE;

		Calendar calendar = Calendar.getInstance();

		Date taxFromDate = null;
		Date unitTaxDate = null;

		UnitTaxCalculationInfo unitTaxClone = getUnitTaxCalculationInfoClone(unit);
		BigDecimal taxPercentage = BigDecimal.ZERO;

		Boolean hasOnlyHistoryTaxDetails1 = false;
		Boolean isTaxFromDateBetweenInstallment = false;

		try {
			unitTaxDate = new SimpleDateFormat(
					PropertyTaxConstants.DATE_FORMAT_DDMMYYY).parse(unitTaxClone
					.getInstDate());
		} catch (ParseException e) {
			LOGGER.error("Error while parsing unit tax installment date", e);
		}

		if (unit.getIsHistory() == null || !unit.getIsHistory()) {
			for (MiscellaneousTax miscTax : unitTaxClone
					.getMiscellaneousTaxes()) {

				alv = unitTaxClone.getAnnualRentAfterDeduction();
				hasOnlyHistoryTaxDetails1 = false;
				isTaxFromDateBetweenInstallment = false;

				for (MiscellaneousTaxDetail detail : miscTax.getTaxDetails()) {
					hasOnlyHistoryTaxDetails1 = detail.getIsHistory() == null ? false
							: detail.getIsHistory().equals(
									NMCPTISConstants.HISTORY_TAX_DETAIL) ? true
									: false;
				}

				/**
				 *
				 * Show the annual tax if the modified tax is effective in this
				 * installment
				 *
				 */
				if (isHistoryTaxEffective) {
					for (MiscellaneousTaxDetail detail : miscTax
							.getTaxDetails()) {
						if (detail.getIsHistory() != null
								&& detail.getIsHistory().equals(
										HISTORY_TAX_DETAIL)
								&& between(detail.getFromDate(),
										installment.getFromDate(),
										installment.getToDate())) {
							isTaxFromDateBetweenInstallment = Boolean.TRUE;
							calendar.setTime(detail.getFromDate());
							taxFromDate = calendar.getTime();
							miscTax.setHasChanged(true);
						}

						if (isTaxFromDateBetweenInstallment
								&& taxFromDate != null) {
							// && (unitTaxDate.before(taxFromDate) ||
							// unitTaxDate.equals(taxFromDate))) {
							alv = detail.getHistoryALV();
							unitTaxClone.setAnnualRentAfterDeduction(alv);
							unitTaxClone.setInstDate(DateUtils
									.getDefaultFormattedDate(taxFromDate));
							isUseHistoryALV = Boolean.TRUE;
							break;
						}
					}

					if (isUseHistoryALV) {
						break;
					}
				}
			}
		}

		Boolean hasOnlyHistoryTaxDetails = false;

		for (MiscellaneousTax miscTax : unitTaxClone.getMiscellaneousTaxes()) {

			if (isUseHistoryALV && taxFromDate != null) {
				// && (unitTaxDate.before(taxFromDate) ||
				// unitTaxDate.equals(taxFromDate))) {
				for (MiscellaneousTaxDetail mtDtl : miscTax.getTaxDetails()) {
					if (mtDtl.getIsHistory() != null
							&& mtDtl.getIsHistory().equals(HISTORY_TAX_DETAIL)) {
						alv = mtDtl.getHistoryALV();
						break;
					}
				}
			} else {
				alv = unitTaxClone.getAnnualRentAfterDeduction();

				hasOnlyHistoryTaxDetails = false;

				/**
				 * When the property is modified & if that tax is not applicable
				 * for the current occupancy This happens when the property
				 * modified to be between installment period, Say X tax is not
				 * applicable now, so we calculate the tax for X for n days
				 * occupied before the occupancy date(modified date)
				 *
				 */

				for (MiscellaneousTaxDetail detail : miscTax.getTaxDetails()) {
					hasOnlyHistoryTaxDetails = detail.getIsHistory() == null ? false
							: detail.getIsHistory().equals(
									NMCPTISConstants.HISTORY_TAX_DETAIL) ? true
									: false;

				}

			}

			if (hasOnlyHistoryTaxDetails && !isUseHistoryALV) {
				miscTax.setTotalActualTax(BigDecimal.ZERO);
				miscTax.setTotalCalculatedTax(BigDecimal.ZERO);
				miscTax.getTaxDetails().clear();
			} else {
				EgDemandReasonDetails demandReasonDetails = null;
				BigDecimal calculatedAnnualTax = BigDecimal.ZERO;
				BigDecimal calculatedActualTax = BigDecimal.ZERO;

				if (!isUseHistoryALV) {
					if (DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD
							.equalsIgnoreCase(miscTax.getTaxName())) {
						alv = unitTaxClone.getResidentialALV().compareTo(
								BigDecimal.ZERO) == 0 ? alv : unitTaxClone
								.getResidentialALV();
					} else if (DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD
							.equalsIgnoreCase(miscTax.getTaxName())
							|| DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX
									.equalsIgnoreCase(miscTax.getTaxName())) {
						alv = unitTaxClone.getNonResidentialALV().compareTo(
								BigDecimal.ZERO) == 0 ? alv : unitTaxClone
								.getNonResidentialALV();
					} else if (DEMANDRSN_CODE_GENERAL_WATER_TAX
							.equalsIgnoreCase(miscTax.getTaxName())
							&& (taxCalcInfos.get(installment) != null)) {
						alv = getALVForDemandReason(installment, unitTaxClone,
								taxCalcInfos.get(installment),
								DEMANDRSN_CODE_GENERAL_WATER_TAX);
					} else if (DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX
							.equalsIgnoreCase(miscTax.getTaxName())) {
						// && PROPTYPE_RESD.equalsIgnoreCase(propertyType)) {
						alv = getALVForDemandReason(installment, unitTaxClone,
								taxCalcInfos.get(installment),
								DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX);
					}
				}

				if (between(unitTaxClone.getOccpancyDate(),
						installment.getFromDate(), installment.getToDate())) {
					Installment dummyInstallment = new Installment();
					dummyInstallment
							.setFromDate(unitTaxClone.getOccpancyDate());
					dummyInstallment.setToDate(installment.getToDate());

					demandReasonDetails = (EgDemandReasonDetails) HibernateUtil
							.getCurrentSession()
							.getNamedQuery(
									QUERY_DEMANDREASONDETAILS_BY_DEMANDREASON_AND_INSTALLMENT)
							.setString(0, miscTax.getTaxName())
							.setBigDecimal(1, alv)
							.setDate(2, dummyInstallment.getFromDate())
							.setDate(3, dummyInstallment.getToDate()).list()
							.get(0);
				} else {

					demandReasonDetails = (EgDemandReasonDetails) HibernateUtil
							.getCurrentSession()
							.getNamedQuery(
									QUERY_DEMANDREASONDETAILS_BY_DEMANDREASON_AND_INSTALLMENT)
							.setString(0, miscTax.getTaxName())
							.setBigDecimal(1, alv)
							.setDate(2, installment.getFromDate())
							.setDate(3, installment.getToDate()).list().get(0);
				}

				if (propertyType != null
						&& propertyType.equalsIgnoreCase(PROPTYPE_STATE_GOVT)
						&& miscTax.getTaxName().equalsIgnoreCase(
								DEMANDRSN_CODE_GENERAL_TAX)) {

					BigDecimal demandRsnDtlPercResult = BigDecimal.ZERO;

					if (demandReasonDetails != null) {

						if (ZERO.equals(demandReasonDetails.getFlatAmount())) {
							demandRsnDtlPercResult = alv.multiply(
									demandReasonDetails.getPercentage())
									.divide(new BigDecimal(HUNDRED));
							calculatedAnnualTax = demandRsnDtlPercResult
									.subtract(demandRsnDtlPercResult
											.multiply(
													new BigDecimal(
															STATEGOVT_BUILDING_GENERALTAX_ADDITIONALDEDUCTION))
											.divide(new BigDecimal(HUNDRED)));
						} else if (demandReasonDetails.getPercentage() == null) {
							calculatedAnnualTax = demandReasonDetails
									.getFlatAmount();
						} else {
							taxPercentage = demandReasonDetails.getPercentage();
						}

					}

				} else {
					if (demandReasonDetails != null) {
						if (ZERO.equals(demandReasonDetails.getFlatAmount())) {
							taxPercentage = demandReasonDetails.getPercentage();
						} else if (demandReasonDetails.getPercentage() == null) {
							calculatedAnnualTax = demandReasonDetails
									.getFlatAmount();
						} else {
							taxPercentage = demandReasonDetails.getPercentage();
						}
					}
				}

				if (propertyTypCategory != null
						&& propertyTypCategory
								.equalsIgnoreCase(PROPTYPE_CAT_RESD_CUM_NON_RESD)
						&& (miscTax.getTaxName().equals(
								DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD)
								|| (miscTax.getTaxName()
										.equals(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD)) || (miscTax
									.getTaxName()
								.equals(DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX)))) {
					calculatedAnnualTax = alv.multiply(
							new BigDecimal(
									RESD_CUM_COMMERCIAL_PROP_ALV_PERCENTAGE)
									.divide(new BigDecimal(HUNDRED))).multiply(
							taxPercentage.divide(new BigDecimal(HUNDRED)));
				} else if (!taxPercentage.equals(ZERO)
						&& ZERO.equals(calculatedAnnualTax)) {
					calculatedAnnualTax = alv.multiply(taxPercentage
							.divide(new BigDecimal(HUNDRED)));
				}

				if (demandReasonDetails != null
						&& demandReasonDetails.getFlatAmount().compareTo(ZERO) > 0) {

					// FlatAmount must be the maximum amount
					if (demandReasonDetails.getIsFlatAmntMax().equals(
							Integer.valueOf(1))
							&& (calculatedAnnualTax
									.compareTo(demandReasonDetails
											.getFlatAmount()) > 0)) {
						calculatedAnnualTax = demandReasonDetails
								.getFlatAmount();
					}

					// FlatAmount must be the minimum amount
					if (demandReasonDetails.getIsFlatAmntMax().equals(
							Integer.valueOf(0))
							&& (calculatedAnnualTax
									.compareTo(demandReasonDetails
											.getFlatAmount()) < 0)) {
						calculatedAnnualTax = demandReasonDetails
								.getFlatAmount();
					}
				}

				MiscellaneousTaxDetail miscTaxDetail = new MiscellaneousTaxDetail();
				miscTaxDetail.setFromDate(demandReasonDetails.getFromDate());
				miscTaxDetail.setTaxValue(demandReasonDetails.getPercentage());
				miscTaxDetail.setCalculatedTaxValue(calculatedAnnualTax);

				if (propertyType != null
						&& propertyType.equalsIgnoreCase(PROPTYPE_CENTRAL_GOVT)) {
					calculatedActualTax = calculatedAnnualTax.setScale(0,
							ROUND_HALF_UP);
					calculatedAnnualTax = calcGovtTaxOnAmenities(amenities,
							calculatedAnnualTax);
					miscTaxDetail.setCalculatedTaxValue(calculatedAnnualTax);
					miscTaxDetail.setActualTaxValue(calculatedActualTax);
				}

				calculatedAnnualTax = calculatedAnnualTax.setScale(0,
						ROUND_HALF_UP);

				miscTax.setTotalCalculatedTax(calculatedAnnualTax);
				miscTax.setTotalActualTax(calculatedActualTax.setScale(0,
						ROUND_HALF_UP));
				miscTax.getTaxDetails().clear();
				miscTax.getTaxDetails().add(miscTaxDetail);

				totalTax = totalTax.add(calculatedAnnualTax);
			}
		}

		unitTaxClone.setTotalTaxPayable(totalTax.setScale(0, ROUND_HALF_UP));

		LOGGER.debug("calculateAnnualTaxes - totalTaxPayable: "
				+ unitTaxClone.getTotalTaxPayable());
		LOGGER.debug("Exiting from calculateAnnualTaxes");
		return unitTaxClone;
	}

	public static Boolean hasConsolidatedUnits(TaxCalculationInfo taxCalc) {
		return taxCalc.getUnitTaxCalculationInfos().size() == taxCalc
				.getConsolidatedUnitTaxCalculationInfo().size() ? false : true;
	}

	public static BigDecimal getALVForDemandReason(Installment installment,
			UnitTaxCalculationInfo consolidatedUnitTax,
			TaxCalculationInfo taxCalcInfo, String demandReasonCode) {
		LOGGER.debug("Entered into getALVForUnitWithWaterTax, demandReasonCode="
				+ demandReasonCode);

		BigDecimal alv = BigDecimal.ZERO;

		if (taxCalcInfo.getUnitTaxCalculationInfos().get(0) instanceof List) {
			for (List<UnitTaxCalculationInfo> unitTaxes : taxCalcInfo
					.getUnitTaxCalculationInfos()) {
				UnitTaxCalculationInfo unitTax = null;
				if (unitTaxes.size() > 1) {
					unitTax = unitTaxes.get(unitTaxes.size() - 1);
				} else {
					unitTax = unitTaxes.get(0);
				}
				if (unitTax.getUnitNumber().equals(
						consolidatedUnitTax.getUnitNumber())) {
					for (MiscellaneousTax miscTax : unitTax
							.getMiscellaneousTaxes()) {
						if (demandReasonCode.equalsIgnoreCase(miscTax
								.getTaxName())) {
							alv = alv
									.add(unitTax.getAnnualRentAfterDeduction());
							break;
						}
					}
				}
			}
		} else {
			for (int i = 0; i < taxCalcInfo.getUnitTaxCalculationInfos().size(); i++) {
				UnitTaxCalculationInfo unit = (UnitTaxCalculationInfo) taxCalcInfo
						.getUnitTaxCalculationInfos().get(i);
				if (unit.getUnitNumber().equals(
						consolidatedUnitTax.getUnitNumber())) {
					for (MiscellaneousTax miscTax : unit
							.getMiscellaneousTaxes()) {
						if (demandReasonCode.equalsIgnoreCase(miscTax
								.getTaxName())) {
							alv = alv.add(unit.getAnnualRentAfterDeduction());
							break;
						}
					}
				}
			}
		}

		LOGGER.debug("getALVForUnitWithWaterTax - ALV to be used for Water Tax: "
				+ alv);

		return alv;
	}

	/**
	 * Return true if unitTax has Tax taxName details
	 *
	 * @param unitTax
	 * @param taxName
	 * @return
	 */
	public static Boolean isTaxExistsInUnitTaxCalcInfo(
			UnitTaxCalculationInfo unitTax, String taxName) {

		for (MiscellaneousTax miscTax : unitTax.getMiscellaneousTaxes()) {
			for (MiscellaneousTaxDetail detail : miscTax.getTaxDetails()) {
				if ((detail.getIsHistory() == null || detail.getIsHistory()
						.equals('N'))
						&& taxName.equalsIgnoreCase(miscTax.getTaxName())) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Returns Map with below key-value pair CURR_DMD_STR - Current Installment
	 * demand ARR_DMD_STR - Current Installment collection CURR_COLL_STR -
	 * Arrear Installment demand ARR_COLL_STR - Arrear Installment demand
	 *
	 * @param property
	 * @return Map<String, BigDecimal>
	 */
	public Map<String, BigDecimal> getDemandAndCollection(Property property) {
		LOGGER.debug("Entered into getDemandAndCollection");

		Map<String, BigDecimal> demandCollMap = new HashMap<String, BigDecimal>();
		Installment installment = null;
		Integer instId = null;
		BigDecimal currDmd = BigDecimal.ZERO;
		BigDecimal arrDmd = BigDecimal.ZERO;
		BigDecimal currCollection = BigDecimal.ZERO;
		BigDecimal arrColelection = BigDecimal.ZERO;
		BigDecimal currentRebate = BigDecimal.ZERO;
		BigDecimal arrearRebate = BigDecimal.ZERO;

		Ptdemand currDemand = ptDemandDao
				.getNonHistoryCurrDmdForProperty(property);
		List dmdCollList = propertyDao.getDmdCollForAllDmdReasons(currDemand);

		for (Object object : dmdCollList) {
			Object[] listObj = (Object[]) object;
			instId = Integer.valueOf(((BigDecimal) listObj[0]).toString());
			installment = (Installment) installmentDao.findById(instId, false);
			if (currDemand.getEgInstallmentMaster().equals(installment)) {
				if (listObj[2] != null && !listObj[2].equals(BigDecimal.ZERO)) {
					currCollection = currCollection
							.add((BigDecimal) listObj[2]);
				}
				/**
				 * Not adding the rebate amount, as during apportioning
				 * crAmountToBePaid is set
				 */
				/*
				 * if (listObj[3] != null &&
				 * !listObj[3].equals(BigDecimal.ZERO)) { currCollection =
				 * currCollection.add((BigDecimal) listObj[3]); }
				 */

				currentRebate = currentRebate.add((BigDecimal) listObj[3]);
				currDmd = currDmd.add((BigDecimal) listObj[1]);
			} else {
				arrDmd = arrDmd.add((BigDecimal) listObj[1]);
				if (listObj[2] != null && !listObj[2].equals(BigDecimal.ZERO)) {
					arrColelection = arrColelection
							.add((BigDecimal) listObj[2]);
				}

				arrearRebate = arrearRebate.add((BigDecimal) listObj[3]);

				/*
				 * if (listObj[3] != null &&
				 * !listObj[3].equals(BigDecimal.ZERO)) { arrColelection =
				 * arrColelection.add((BigDecimal) listObj[3]); }
				 */
			}
		}
		demandCollMap.put(CURR_DMD_STR, currDmd);
		demandCollMap.put(ARR_DMD_STR, arrDmd);
		demandCollMap.put(CURR_COLL_STR, currCollection);
		demandCollMap.put(ARR_COLL_STR, arrColelection);
		demandCollMap.put(CURRENT_REBATE_STR, currentRebate);
		demandCollMap.put(ARREAR_REBATE_STR, arrearRebate);
		LOGGER.debug("getDemandAndCollection - demandCollMap = "
				+ demandCollMap);
		LOGGER.debug("Exiting from getDemandAndCollection");
		return demandCollMap;
	}

	/**
	 * Tells you whether property is modified or not
	 *
	 * @param property
	 * @return true if the Property is modified
	 */
	public static boolean isPropertyModified(Property property) {

		for (PropertyStatusValues psv : property.getBasicProperty()
				.getPropertyStatusValuesSet()) {
			if (PROPERTY_MODIFY_REASON_MODIFY.equalsIgnoreCase(psv
					.getPropertyStatus().getStatusCode())) {
				return true;
			}
		}

		return false;
	}

	public void makeTheEgBillAsHistory(BasicProperty basicProperty) {
		EgBill egBill = (EgBill) persistenceService
				.find("from EgBill where module = ? and consumerId like ? || '%' and is_history = 'N'",
						moduleDao
								.getModuleByName(PropertyTaxConstants.PTMODULENAME),
						basicProperty.getUpicNo());
		if (egBill != null) {
			egBill.setIs_History("Y");
			egBill.setLastUpdatedTimeStamp(new Date());
			persistenceService.setType(EgBill.class);
			persistenceService.update(egBill);
		}
	}

	// TODO -- Fix me (Commented to Resolve compilation issues)
	/*
	public void setAuditEventService(AuditEventService auditEventService) {
		this.auditEventService = auditEventService;
	}

	public void generateAuditEvent(String action, BasicProperty basicProperty,
			String createAuditDetails1, String createAuditDetails2) {
		final AuditEvent auditEvent = new AuditEvent(AuditModule.PROPERTYTAX,
				AuditEntity.PROPERTYTAX_PROPERTY, action,
				basicProperty.getUpicNo(), createAuditDetails1);
		auditEvent.setPkId(basicProperty.getId());
		auditEvent.setDetails2(createAuditDetails2);
		this.auditEventService.createAuditEvent(auditEvent,
				BasicPropertyImpl.class);
	}*/
	 

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

		strAddress.append(
				(isNotBlank(address.getStreetRoadLine())) ? address
						.getStreetRoadLine() : " ").append("|");
		strAddress.append(
				(isNotBlank(address.getHouseNoBldgApt())) ? address
						.getHouseNoBldgApt() : " ").append("|");
		strAddress.append(
				(isNotBlank(address.getDoorNumOld())) ? address.getDoorNumOld()
						: " ").append("|");

		String tmpPin = address.getPinCode();
		strAddress
				.append((tmpPin != null && !tmpPin.toString().isEmpty()) ? tmpPin
						: " ").append("|");

		strAddress.append(
				(isNotBlank(address.getMobileNo())) ? address.getMobileNo()
						: " ").append("|");
		strAddress.append(
				(isNotBlank(address.getEmailAddress())) ? address
						.getEmailAddress() : " ").append("|");
		strAddress.append(
				isNotBlank(address.getExtraField1()) ? address.getExtraField1()
						: " ").append("|");
		strAddress.append(
				isNotBlank(address.getExtraField2()) ? address.getExtraField2()
						: " ").append("|");
		strAddress.append(
				isNotBlank(address.getExtraField3()) ? address.getExtraField3()
						: " ").append("|");
		strAddress.append(isNotBlank(address.getExtraField4()) ? address
				.getExtraField4() : " ");

		LOGGER.debug("Exit from buildAddress, Address: "
				+ strAddress.toString());

		return strAddress.toString();
	}

	/**
	 * Gives the Owner Address as string
	 *
	 * @param Set
	 *            <Owner> Set of Property Owners
	 * @return String
	 */
	public static String getOwnerAddress(Set<PropertyOwner> ownerSet) {
		LOGGER.debug("Entered into getOwnerAddress");

		String ownerAddress = "";
		for (PropertyOwner owner : ownerSet) {
			// TODO -- Fix me (Commented to Resolve compilation issues)
			/*
			 * Set<Address> addrSet = owner.getAddressSet(); for (Address
			 * address : addrSet) { ownerAddress = new PTISCacheManager()
			 * .buildAddressByImplemetation(address); break; }
			 */
		}

		LOGGER.debug("Exiting from getOwnerAddress");
		return ownerAddress;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Date> getLatestCollRcptDateForProp(String consumerCode) {
		LOGGER.debug("Entered into getLatestCollRcptDateForProp, consumerCode="
				+ consumerCode);

		Map<String, Date> penaltyDates = new HashMap<String, Date>();
		List<Object> rcptHeaderList = HibernateUtil
				.getCurrentSession()
				.createQuery(
						"select substr(rd.description, length(rd.description)-6, length(rd.description)), max(rh.createdDate) "
								+ "from org.egov.erpcollection.models.ReceiptHeader rh "
								+ "left join rh.receiptDetails rd "
								+ "where rh.status.code = 'APPROVED' "
								+ "and rd.description is not null "
								+ "and rd.cramount > 0 "
								+ "and rh.consumerCode like '"
								+ consumerCode
								+ "%' "
								+ "group by substr(rd.description, length(rd.description)-6, length(rd.description))")
				.list();

		if (rcptHeaderList != null && !rcptHeaderList.isEmpty()) {
			String instStr = "";
			Date penaltyCollDate = null;
			for (Object object : rcptHeaderList) {
				Object[] penaltyDet = (Object[]) object;
				instStr = (String) penaltyDet[0];
				penaltyCollDate = (Date) penaltyDet[1];
				penaltyDates.put(instStr, penaltyCollDate);
			}
		}

		LOGGER.debug("penaltyDates==>" + penaltyDates);
		LOGGER.debug("Exiting from getLatestCollRcptDateForProp");
		return penaltyDates;
	}

	/**
	 * Gives the latest Property (Recent property) for the BasicProperty
	 *
	 * <p>
	 * This API is used during Data Entry and Modification to get the recent
	 * property when the Occupancy Date after Data Entry or Modification is in
	 * between installment
	 * </p>
	 *
	 * @param basicProperty
	 * @return
	 */
	public static Property getLatestProperty(BasicProperty basicProperty,
			Character status) {
		LOGGER.debug("Entered into getLatestProperty, basicProperty="
				+ basicProperty);

		Map<Date, Property> propertiesByCreatedDate = new TreeMap<Date, Property>();
		Property latestProperty = null;

		for (Property property : basicProperty.getPropertySet()) {

			if (status == null) {
				propertiesByCreatedDate.put(property.getCreatedDate().toDate(),
						property);
			} else if (property.getStatus().equals(status)) {
				propertiesByCreatedDate.put(property.getCreatedDate().toDate(),
						property);
			}
		}

		if (!propertiesByCreatedDate.isEmpty()) {
			List<Property> properties = new ArrayList<Property>(
					propertiesByCreatedDate.values());
			latestProperty = properties.get(properties.size() - 1);
			LOGGER.debug("getLatestProperty, latestProperty=" + latestProperty);
		}

		LOGGER.debug("Exiting from getLatestHistoryProperty");
		return latestProperty;
	}

	public Map<Installment, TaxCalculationInfo> getTaxCalInfoMap(
			Set<Ptdemand> ptDmdSet) {
		Map<Installment, TaxCalculationInfo> taxCalInfoMap = new TreeMap<Installment, TaxCalculationInfo>();

		for (Ptdemand ptdmd : ptDmdSet) {
			TaxCalculationInfo taxCalcInfo = getTaxCalInfo(ptdmd);
			if (taxCalcInfo != null) {
				taxCalInfoMap.put(ptdmd.getEgInstallmentMaster(), taxCalcInfo);
			}
		}

		return taxCalInfoMap;
	}

	public Map<Date, TaxCalculationInfo> getTaxCalInfoMap(
			Set<Ptdemand> ptDmdSet, Date occupancyDate) {
		Map<Date, TaxCalculationInfo> taxCalInfoMap = new TreeMap<Date, TaxCalculationInfo>();
		Installment installment = null;

		for (Ptdemand ptdmd : ptDmdSet) {
			TaxCalculationInfo taxCalcInfo = getTaxCalInfo(ptdmd);
			if (taxCalcInfo != null) {
				installment = ptdmd.getEgInstallmentMaster();
				if (between(occupancyDate, ptdmd.getEgInstallmentMaster()
						.getFromDate(), ptdmd.getEgInstallmentMaster()
						.getToDate())) {
					taxCalInfoMap.put(occupancyDate, taxCalcInfo);
				} else {
					taxCalInfoMap.put(installment.getFromDate(), taxCalcInfo);
				}
			}
		}

		return taxCalInfoMap;
	}

	public String getDesignationName(Long userId) {
		LOGGER.debug("Entered into getDesignationName, userId=" + userId);
		return getAssignment(userId).getDesigId().getDesignationName();
	}

	public WorkflowDetails initWorkflowAction(PropertyImpl propertyModel,
			WorkflowBean workflowBean, Integer loggedInUserId,
			EisCommonService eisCommonService) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entered into initWorkflowAction");
			LOGGER.debug("initWorkflowAction - propertyModel=" + propertyModel
					+ ", workflowBean=" + workflowBean + ", loggedInUserId="
					+ loggedInUserId);
		}

		String beanActionName[] = null;

		if (isNotNull(workflowBean)) {
			beanActionName = workflowBean.getActionName().split(":");
		}

		WorkflowDetails workflowAction = null;

		if (WFLOW_ACTION_NAME_CREATE.equalsIgnoreCase(beanActionName[0])) {
			workflowAction = new ActionCreate(propertyModel, workflowBean,
					loggedInUserId);
		} else if (WFLOW_ACTION_NAME_MODIFY.equalsIgnoreCase(beanActionName[0])) {
			workflowAction = new ActionModify(propertyModel, workflowBean,
					loggedInUserId);
		} else if (WFLOW_ACTION_NAME_BIFURCATE
				.equalsIgnoreCase(beanActionName[0])) {
			workflowAction = new ActionBifurcate(propertyModel, workflowBean,
					loggedInUserId);
		} else if (WFLOW_ACTION_NAME_AMALGAMATE
				.equalsIgnoreCase(beanActionName[0])) {
			workflowAction = new ActionAmalgmate(propertyModel, workflowBean,
					loggedInUserId);
		} else if (WFLOW_ACTION_NAME_CHANGEADDRESS
				.equalsIgnoreCase(beanActionName[0])) {
			workflowAction = new ActionChangeAddress(propertyModel,
					workflowBean, loggedInUserId);
		} else if (WFLOW_ACTION_NAME_DEACTIVATE
				.equalsIgnoreCase(beanActionName[0])) {
			workflowAction = new ActionDeactivate(propertyModel, workflowBean,
					loggedInUserId);
		} else if (NMCPTISConstants.WFLOW_ACTION_NAME_TRANSFER
				.equalsIgnoreCase(beanActionName[0])) {
			workflowAction = new ActionNameTransfer(propertyModel,
					workflowBean, loggedInUserId);
		}

		workflowAction.setWorkflowActionStep(this, eisCommonsService);

		LOGGER.debug("initWorkflowAction - workflowAction=" + workflowAction);
		LOGGER.debug("Exiting from initWorkflowAction");
		return workflowAction;
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
	public void getTaxSlabs(
			Map<Integer, Map<String, Map<Date, BigDecimal>>> dateAndTotalCalcTaxByTaxForUnit,
			List<UnitTaxCalculationInfo> unitTaxes, List<String> taxNames,
			Installment installment) {
		LOGGER.debug("Entered into getTaxSlabs");
		LOGGER.debug("getTaxSlabs - dateAndPercentageByTaxForUnit: "
				+ dateAndTotalCalcTaxByTaxForUnit);
		LOGGER.debug("getTaxSlabs - taxNames: " + taxNames);

		for (UnitTaxCalculationInfo unitTax : unitTaxes) {
			Map<String, Map<Date, BigDecimal>> dateAndPercentageByTax = (dateAndTotalCalcTaxByTaxForUnit
					.get(unitTax.getUnitNumber()) == null) ? new TreeMap<String, Map<Date, BigDecimal>>()
					: dateAndTotalCalcTaxByTaxForUnit.get(unitTax
							.getUnitNumber());

			if (taxNames.isEmpty()) {
				for (MiscellaneousTax mt1 : unitTax.getMiscellaneousTaxes()) {
					Map<Date, BigDecimal> dateAndPercentage1 = new TreeMap<Date, BigDecimal>();
					for (MiscellaneousTaxDetail mtd : mt1.getTaxDetails()) {
						if (mtd.getIsHistory() == null
								|| mtd.getIsHistory().equals('N')) {
							dateAndPercentage1.put(mtd.getFromDate(),
									mtd.getCalculatedTaxValue());
							dateAndPercentageByTax.put(mt1.getTaxName(),
									dateAndPercentage1);
							break;
						}
					}
				}
			} else {
				for (MiscellaneousTax mt2 : unitTax.getMiscellaneousTaxes()) {
					if (taxNames.contains(mt2.getTaxName())) {
						Map<Date, BigDecimal> dateAndPercentage2 = new TreeMap<Date, BigDecimal>();

						MiscellaneousTaxDetail mtd = mt2.getTaxDetails().size() > 1 ? mt2
								.getTaxDetails().get(1) : mt2.getTaxDetails()
								.get(0);
						// for (MiscellaneousTaxDetail mtd :
						// mt2.getTaxDetails()) {
						if (mtd.getIsHistory() == null
								|| mtd.getIsHistory().equals('N')) {
							dateAndPercentage2.put(mtd.getFromDate(),
									mtd.getCalculatedTaxValue());
							dateAndPercentageByTax.put(mt2.getTaxName(),
									dateAndPercentage2);
							// break;
						}
						// }
					}
				}
			}
			dateAndTotalCalcTaxByTaxForUnit.put(unitTax.getUnitNumber(),
					dateAndPercentageByTax);
		}
		LOGGER.debug("Exiting from getTaxSlabs - dateAndPercentageByTaxForUnit: "
				+ dateAndTotalCalcTaxByTaxForUnit);
	}

	public Map<String, Date> getSlabChangedTaxes(
			Map<Integer, Map<String, Map<Date, BigDecimal>>> dateAndTotalCalcTaxByTaxForUnit,
			UnitTaxCalculationInfo unitTax, Installment installment) {

		LOGGER.debug("Entered into getSlabChangedTaxes");
		LOGGER.debug("getSlabChangedTaxes - dateAndPercentageByTaxForUnit: "
				+ dateAndTotalCalcTaxByTaxForUnit);
		LOGGER.debug("getSlabChangedTaxes - UnitNumber : "
				+ unitTax.getUnitNumber());

		Map<String, Map<Date, BigDecimal>> taxAndListOfMapsOfDateAndPercentage = dateAndTotalCalcTaxByTaxForUnit
				.get(unitTax.getUnitNumber());

		Map<String, Date> taxNames = new HashMap<String, Date>();

		if (taxAndListOfMapsOfDateAndPercentage != null) {
			for (MiscellaneousTax tax : unitTax.getMiscellaneousTaxes()) {

				Map<Date, BigDecimal> taxDateAndPercentages = taxAndListOfMapsOfDateAndPercentage
						.get(tax.getTaxName());
				Map<Date, MiscellaneousTaxDetail> taxDetailAndEffectiveDate = new TreeMap<Date, MiscellaneousTaxDetail>();

				// Getting the slab effective dates in asc order
				for (MiscellaneousTaxDetail mtd : tax.getTaxDetails()) {
					if (mtd.getIsHistory() == null
							|| NON_HISTORY_TAX_DETAIL
									.equals(mtd.getIsHistory())) {
						taxDetailAndEffectiveDate.put(mtd.getFromDate(), mtd);
					}
				}

				// Getting the latest slab effective date,
				// as of now in NMC there can be only 2 slabs in a installment
				// period,
				// have considered this in order to simplify the process else it
				// will be complex
				MiscellaneousTaxDetail mtd = taxDetailAndEffectiveDate
						.get(taxDetailAndEffectiveDate.keySet().toArray()[taxDetailAndEffectiveDate
								.size() - 1]);

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
	public List<UnitTaxCalculationInfo> prepareUnitTaxesForChangedTaxes(
			Installment installment, UnitTaxCalculationInfo prevUnitTax,
			UnitTaxCalculationInfo currentUnitTax,
			Map<String, Date> changedTaxesAndDates, Boolean isPropertyModified) {

		List<MiscellaneousTax> prevMiscTaxes = prevUnitTax
				.getMiscellaneousTaxes();
		List<MiscellaneousTax> currentMiscTaxes = currentUnitTax
				.getMiscellaneousTaxes();
		List<UnitTaxCalculationInfo> unitTaxCalcs = new ArrayList<UnitTaxCalculationInfo>();
		Map<Date, UnitTaxCalculationInfo> unitTaxForChangeInTaxByDate = new TreeMap<Date, UnitTaxCalculationInfo>();
		List<String> prevTaxNames = new ArrayList<String>();
		UnitTaxCalculationInfo currentUnitTaxClone = null;

		for (MiscellaneousTax mt : prevMiscTaxes) {
			prevTaxNames.add(mt.getTaxName());
		}

		Boolean isModificationBetInstallment = between(
				currentUnitTax.getOccpancyDate(), installment.getFromDate(),
				installment.getToDate()) ? true : false;

		Date taxEffectiveDate = null;
		for (MiscellaneousTax currentMiscTax : currentMiscTaxes) {
			Boolean dmdRsnExists = prevTaxNames.contains(currentMiscTax
					.getTaxName()) ? true : false;

			if (dmdRsnExists) {
				if (changedTaxesAndDates.containsKey(currentMiscTax
						.getTaxName())) {
					taxEffectiveDate = changedTaxesAndDates.get(currentMiscTax
							.getTaxName());

					LOGGER.info(currentMiscTax.getTaxName() + " has changed, "
							+ " for installment: " + installment
							+ ", EffecDate: " + taxEffectiveDate);
					currentUnitTaxClone = getUnitTaxCalculationInfoClone(currentUnitTax);
					// To indicate that the change is because of
					// modification, so showing the Occupancy Date
					if (isPropertyModified
							&& isModificationBetInstallment
							&& !taxEffectiveDate.after(currentUnitTax
									.getOccpancyDate())) {
						currentUnitTaxClone.setInstDate(DateUtils
								.getDefaultFormattedDate(currentUnitTax
										.getOccpancyDate()));
					} else {
						currentMiscTax.setHasChanged(true);
						currentUnitTaxClone.setInstDate(DateUtils
								.getDefaultFormattedDate(taxEffectiveDate));
					}

					putUnitTaxInMapByDate(currentUnitTaxClone,
							unitTaxForChangeInTaxByDate, taxEffectiveDate,
							currentMiscTax, isPropertyModified,
							isModificationBetInstallment);
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
				for (MiscellaneousTaxDetail mtd : currentMiscTax
						.getTaxDetails()) {
					taxEffectiveDate = mtd.getFromDate();
					if (between(taxEffectiveDate, installment.getFromDate(),
							installment.getToDate())) {
						currentMiscTax.setHasChanged(true);
						currentUnitTax.setInstDate(DateUtils
								.getDefaultFormattedDate(taxEffectiveDate));
						putUnitTaxInMapByDate(currentUnitTax,
								unitTaxForChangeInTaxByDate, taxEffectiveDate,
								currentMiscTax, isPropertyModified,
								isModificationBetInstallment);
						currentMiscTax.setHasChanged(false);
					}
				}
			}

		}

		unitTaxCalcs.addAll(unitTaxForChangeInTaxByDate.values());

		return unitTaxCalcs;
	}

	private void putUnitTaxInMapByDate(UnitTaxCalculationInfo currentUnitTax,
			Map<Date, UnitTaxCalculationInfo> unitTaxForChangeInTaxByDate,
			Date taxEffectiveDate, MiscellaneousTax currentMiscTax,
			Boolean isPropertyModified, Boolean isModificationBetInstallment) {

		Date mayKey = null;
		try {
			mayKey = dateFormatter.parse(currentUnitTax.getInstDate());
		} catch (ParseException e) {
			LOGGER.error("Error while parsing date", e);
		}

		if (unitTaxForChangeInTaxByDate.get(mayKey) == null) {

			unitTaxForChangeInTaxByDate.put(mayKey,
					getUnitTaxCalculationInfoClone(currentUnitTax));

		} else {
			for (MiscellaneousTax mt : unitTaxForChangeInTaxByDate.get(mayKey)
					.getMiscellaneousTaxes()) {
				if (mt.getTaxName().equals(currentMiscTax.getTaxName())) {
					mt.setHasChanged(true);
					break;
				}
			}
			LOGGER.info("multiple miscTax change for date "
					+ mayKey
					+ unitTaxForChangeInTaxByDate.get(mayKey)
							.getMiscellaneousTaxes());
		}
	}

	public List<Property> getHistoryProperties(BasicProperty basicProperty) {
		List<Property> historyProperties = new ArrayList<Property>();

		for (Property property : basicProperty.getPropertySet()) {
			if (PropertyTaxConstants.STATUS_ISHISTORY.equals(property.getStatus())) {
				historyProperties.add(property);
			}
		}

		return historyProperties;

	}

	/**
	 * Returns <tt> true </tt> if the <code> object <code> is null
	 *
	 * @param object
	 * @return <tt> true </tt> if the <code> object <code> is null
	 */
	public static boolean isNull(Object object) {
		return object == null;
	}

	/**
	 * Returns <tt> true </tt> if the <code> object </code> is not null
	 *
	 * @param object
	 * @return <tt> true </tt> if the <code> object </code> is not null
	 */
	public static boolean isNotNull(Object object) {
		return object != null;
	}

	/**
	 *
	 * @return
	 */
	private Map<Date, Property> getPropertiesByOccupancy(
			Property propertyModel, Boolean isConsiderMigrated) {
		LOGGER.debug("Entered into getPropertiesByOccupancy");

		Map<Date, Property> propertyByCreatedDate = new TreeMap<Date, Property>();
		Map<Date, Property> propertyByOccupancyDate = new TreeMap<Date, Property>();

		Property migratedProperty = null;

		for (Property property : propertyModel.getBasicProperty()
				.getPropertySet()) {
			if (isConsiderMigrated
					|| (property.getRemarks() == null || !property.getRemarks()
							.startsWith(NMCPTISConstants.STR_MIGRATED))) {
				propertyByCreatedDate.put(property.getCreatedDate().toDate(),
						property);

				if (property.getRemarks() != null
						&& property.getRemarks().startsWith(
								NMCPTISConstants.STR_MIGRATED)) {
					migratedProperty = property;
				}
			}
		}

		for (Map.Entry<Date, Property> entry : propertyByCreatedDate.entrySet()) {
			propertyByOccupancyDate.put(
					getPropertyOccupancyDate(entry.getValue()),
					entry.getValue());
		}

		List<Date> occupancyDates = new LinkedList<Date>(
				propertyByOccupancyDate.keySet());

		if (isNotNull(migratedProperty)) {
			occupancyDates.remove(getPropertyOccupancyDate(migratedProperty));
		}

		Set<Date> removalDates = getRemovalOccupancyDates(occupancyDates,
				getPropertyOccupancyDate(propertyModel));

		LOGGER.info("getPropertiesByOccupancy - removalDates=" + removalDates);

		propertyByOccupancyDate.keySet().removeAll(removalDates);

		LOGGER.debug("Exiting from getPropertiesByOccupancy");

		return propertyByOccupancyDate;
	}

	/**
	 * @param occupancyDates
	 */
	private Set<Date> getRemovalOccupancyDates(List<Date> occupancyDates,
			Date propModelOccupDate) {
		Set<Date> removalDates = new HashSet<Date>();

		for (int i = 0; i < occupancyDates.size(); i++) {
			for (int j = (i + 1); j < occupancyDates.size() - 1; j++) {
				if (occupancyDates.get(i).after(occupancyDates.get(j))) {
					removalDates.add(occupancyDates.get(i));
				}
			}

			if (occupancyDates.get(i).after(propModelOccupDate)) {
				removalDates.add(occupancyDates.get(i));
			}
		}

		return removalDates;
	}

	/**
	 * This condition is applied because floor number is not mandatory in case
	 * of Mixed Property.(for UnitType = OPEN_PLOT). So UnitType(mandatory only
	 * in case of Mixed Property) is considered. The main purpose of this
	 * condition is to check each unitTax corresponds to which floor
	 */
	public Boolean isMatch(UnitTaxCalculationInfo unit, FloorIF floor) {
		Boolean result = false;
		try {
			result = (floor.getFloorNo() != null && unit
					.getFloorNumberInteger().equals(floor.getFloorNo()))
					&& unit.getUnitNumber().equals(
							Integer.valueOf(floor.getExtraField1()))
					&& unit.getUnitArea()
							.toString()
							.equals(floor.getBuiltUpArea().getArea().toString())
					&& unit.getUnitUsage().equals(
							floor.getPropertyUsage().getUsageName())
					&& unit.getUnitOccupation().equals(
							floor.getPropertyOccupation().getOccupation())
					&& unit.getOccpancyDate().equals(
							new SimpleDateFormat(
									PropertyTaxConstants.DATE_FORMAT_DDMMYYY)
									.parse(floor.getExtraField3()));
		} catch (NumberFormatException e) {
			LOGGER.error("NumberFormatException TaxUpdateJob", e);
			throw new EGOVRuntimeException(
					"NumberFormatException TaxUpdateJob", e);
		} catch (ParseException e) {
			LOGGER.error("Error in parsing floor effective date", e);
			throw new EGOVRuntimeException(
					"Error in parsing floor effective date", e);
		}
		return result;
	}

	public static boolean isNotZero(BigDecimal value) {
		return value == null ? false : value.compareTo(BigDecimal.ZERO) != 0;
	}

	/**
	 * @param Property
	 * @return Date the occupancy date
	 */
	public Date getPropertyOccupancyDate(Property property) {
		return property.getPropertyDetail().getEffective_date() == null ? property
				.getEffectiveDate() : property.getPropertyDetail()
				.getEffective_date();
	}

	/**
	 * Returns true if migrated alv can be used for tax calculation else false
	 *
	 * @param propertyModel
	 * @param occupancyDates
	 * @param installment
	 * @return true for using the migrated alv for tax calculation
	 */
	public boolean isUseMigratedALV(Property propertyModel,
			List<Date> occupancyDates, Installment installment) {
		Boolean isUseMigrtdALV = false;

		if (isMigrated(propertyModel.getBasicProperty())) {
			/**
			 * if the occupancy date is like 01-04-yyyy format and if its before
			 * the installment.fromDate no need to use migrated alv, for
			 * calculation use the system calculated alv only
			 *
			 * test with 01-04-2005, 01-05-2011, 01-10-2011 & 01-01-2012
			 */

			isUseMigrtdALV = isOccupancyDateLikeInstallmentFromDate(
					occupancyDates, installment);

		}

		return isUseMigrtdALV ? !getPropertyOccupancyDate(propertyModel)
				.equals(installment.getFromDate()) : isUseMigrtdALV;
	}

	/**
	 * @param occupancyDates
	 * @param installment
	 * @param isUseMigrtdALV
	 * @param calendar
	 * @return
	 */
	private Boolean isOccupancyDateLikeInstallmentFromDate(
			List<Date> occupancyDates, Installment installment) {

		for (int i = 0; i < occupancyDates.size(); i++) {
			if (isFirstDayOfMonth(occupancyDates.get(i))
					&& isMonthApril(occupancyDates.get(i))) {
				if (occupancyDates.get(i).before(installment.getFromDate())) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * @param calendar
	 * @return true if the month is April
	 */
	private boolean isMonthApril(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH) == Calendar.APRIL;
	}

	/**
	 * @param calendar
	 * @return true if the first day of the month
	 */
	private boolean isFirstDayOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH) == 1;
	}

	public static boolean isResidentialUnit(String usageName) {
		return USAGES_FOR_RESD.contains(usageName) ? true : false;
	}

	public static boolean isNonResidentialUnit(String usageName) {
		return USAGES_FOR_NON_RESD.contains(usageName) ? true : false;
	}

	public static boolean isOpenPlotUnit(String usageName) {
		return USAGES_FOR_OPENPLOT.contains(usageName) ? true : false;
	}

	/**
	 * Returns true if the amount is equal to 0 else false if amount is null or
	 * if its not 0
	 *
	 * @param amount
	 * @return true if amount is equal to 0
	 */
	public static boolean isZero(BigDecimal amount) {
		return amount.compareTo(BigDecimal.ZERO) == 0 ? true : false;
	}

	public static Installment getPTInstallmentForDate(Date date) {
		Module module = moduleDao
				.getModuleByName(PropertyTaxConstants.PTMODULENAME);
		return installmentDao.getInsatllmentByModuleForGivenDate(module, date);
	}

	public void calcConsolidatedBigBldgTaxs(TaxCalculationInfo taxCalcInfo,
			Installment installment, Boolean isExemptedFromTax) {
		LOGGER.debug("Entered into calcConsolidatedBigBldgTaxs");

		taxCalcInfo.setTotalTaxPayable(ZERO);
		taxCalcInfo.setTotalAnnualLettingValue(ZERO);

		// Used to group UnitTaxCalculationInfo's by UnitNo
		Map<Integer, List<UnitTaxCalculationInfo>> consolidateUnitsByUnitNums = new TreeMap<Integer, List<UnitTaxCalculationInfo>>();
		Map<Integer, BigDecimal> totUnitAreaUnitWise = new HashMap<Integer, BigDecimal>();
		Map<Integer, BigDecimal> totALVUnitWise = new HashMap<Integer, BigDecimal>();
		Map<Integer, Date> occDateUnitWise = new HashMap<Integer, Date>();

		List<List<UnitTaxCalculationInfo>> unitTaxes = taxCalcInfo
				.getUnitTaxCalculationInfos();
		Map<Integer, BigDecimal> consdTotalTaxUnitWise = new HashMap<Integer, BigDecimal>();
		Map<Integer, MiscellaneousTax> consdMiscTaxUnitWise = new HashMap<Integer, MiscellaneousTax>();
		Map<Integer, BigDecimal> consdBigBldgALVUnitWise = new HashMap<Integer, BigDecimal>();

		// holds the unit numbers which are consolidated
		for (List<UnitTaxCalculationInfo> unitTaxCalcs : unitTaxes) {
			UnitTaxCalculationInfo unit = null;
			unit = unitTaxCalcs.get(0);
			if ((OWNER_OCC.equals(unit.getUnitOccupation()) || OCCUPIER_OCC
					.equals(unit.getUnitOccupation()))
					&& USAGES_FOR_RESD.contains(unit.getUnitUsage())) {

				// adding UnitTaxCalculationInfo unit wise to map
				if (consolidateUnitsByUnitNums.get(unit.getUnitNumber()) == null) {
					List<UnitTaxCalculationInfo> singleUnit = new ArrayList<UnitTaxCalculationInfo>();
					singleUnit.add(unit);
					consolidateUnitsByUnitNums.put(unit.getUnitNumber(),
							singleUnit);
				} else {
					consolidateUnitsByUnitNums.get(unit.getUnitNumber()).add(
							unit);
				}

				// adding Unitarea unit wise to map
				if (totUnitAreaUnitWise.get(unit.getUnitNumber()) == null) {
					totUnitAreaUnitWise.put(unit.getUnitNumber(),
							unit.getUnitArea());
				} else {
					BigDecimal unitArea = totUnitAreaUnitWise.get(
							unit.getUnitNumber()).add(unit.getUnitArea());
					totUnitAreaUnitWise.remove(unit.getUnitNumber());
					totUnitAreaUnitWise.put(unit.getUnitNumber(), unitArea);
				}

				// adding ALV unit wise to map
				if (totALVUnitWise.get(unit.getUnitNumber()) == null) {
					totALVUnitWise.put(unit.getUnitNumber(),
							unit.getAnnualRentAfterDeduction());
				} else {
					BigDecimal alv = totALVUnitWise.get(unit.getUnitNumber())
							.add(unit.getAnnualRentAfterDeduction());
					totALVUnitWise.remove(unit.getUnitNumber());
					totALVUnitWise.put(unit.getUnitNumber(), alv);
				}

				// adding least occupancy date unit wise
				if (occDateUnitWise.get(unit.getUnitNumber()) == null) {
					occDateUnitWise.put(unit.getUnitNumber(),
							unit.getOccpancyDate());
				} else {
					if (occDateUnitWise.get(unit.getUnitNumber()).after(
							unit.getOccpancyDate())) {
						occDateUnitWise.get(unit.getUnitNumber()).setTime(
								unit.getOccpancyDate().getTime());
					}
				}
			}
		}

		if (consolidateUnitsByUnitNums.size() > 0) {
			for (Map.Entry<Integer, List<UnitTaxCalculationInfo>> entry1 : consolidateUnitsByUnitNums
					.entrySet()) {
				BigDecimal totalUnitArea = BigDecimal.ZERO;
				BigDecimal totalALV = BigDecimal.ZERO;
				Date occDate = null;

				totalUnitArea = totUnitAreaUnitWise.get(entry1.getKey());
				totalALV = totALVUnitWise.get(entry1.getKey());
				occDate = occDateUnitWise.get(entry1.getKey());

				String taxExemptionReason = entry1.getValue().get(0)
						.getTaxExemptionReason();

				LOGGER.debug("calcConsolidatedBigBldgTaxs - taxExemptionReason="
						+ taxExemptionReason);

				Boolean isBigBuildingTaxExempted = taxExemptionReason == null ? false
						: NMCPTISConstants.exemptedTaxesByReason
								.get(taxExemptionReason)
								.contains(
										NMCPTISConstants.DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX) ? true
								: false;

				if (!isBigBuildingTaxExempted
						&& totalUnitArea.compareTo(new BigDecimal(
								AREA_CONSTANT_FOR_LARGE_RESIDENTIAL_PREMISES)) >= 0
						&& totalALV.compareTo(LARGE_RESIDENTIAL_PREMISES_ALV) >= 0) {

					List<EgDemandReasonDetails> demandReasonDetails = getDemandReasonDetails(
							DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX, totalALV,
							installment);
					if (!demandReasonDetails.isEmpty()) {
						LOGGER.debug("applying large residentail tax");

						BigDecimal totalBigResTax = BigDecimal.ZERO;

						MiscellaneousTax miscellaneousTax = new MiscellaneousTax();
						miscellaneousTax
								.setTaxName(DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX);

						for (EgDemandReasonDetails drd : demandReasonDetails) {
							MiscellaneousTaxDetail taxDetail = new MiscellaneousTaxDetail();
							BigDecimal bigResTax = BigDecimal.ZERO;

							bigResTax = totalALV.multiply(drd.getPercentage()
									.divide(new BigDecimal("100")));

							if (between(occDate, installment.getFromDate(),
									installment.getToDate())
									|| demandReasonDetails.size() > 1) {
								Integer totalNoOfDays = getNumberOfDays(
										installment.getFromDate(),
										installment.getToDate()).intValue();
								Integer noOfDays = getNumberOfDays(installment,
										drd, occDate, null);
								bigResTax = bigResTax.multiply(
										new BigDecimal(noOfDays)).divide(
										new BigDecimal(totalNoOfDays), 2,
										ROUND_HALF_UP);
								taxDetail.setNoOfDays(noOfDays);
								LOGGER.info("Big Residential tax for "
										+ noOfDays + " is " + bigResTax);
							} else {
								LOGGER.info("Big Residential tax is "
										+ bigResTax);
							}

							taxDetail.setTaxValue(drd.getPercentage());
							taxDetail.setFromDate(drd.getFromDate());
							taxDetail.setCalculatedTaxValue(bigResTax.setScale(
									0, BigDecimal.ROUND_HALF_UP));
							totalBigResTax = totalBigResTax.add(bigResTax);
							miscellaneousTax
									.addMiscellaneousTaxDetail(taxDetail);
						}
						/*
						 * unitTaxCalculationInfo.setTotalTaxPayable(
						 * unitTaxCalculationInfo.getTotalTaxPayable()
						 * .add(totalBigResTax).setScale(0,
						 * BigDecimal.ROUND_HALF_UP));
						 */
						miscellaneousTax.setTotalCalculatedTax(totalBigResTax
								.setScale(0, BigDecimal.ROUND_HALF_UP));

						consdTotalTaxUnitWise.put(entry1.getKey(),
								totalBigResTax);
						consdMiscTaxUnitWise.put(entry1.getKey(),
								miscellaneousTax);
						consdBigBldgALVUnitWise.put(entry1.getKey(), totalALV);

						LOGGER.debug("big resd tax " + totalBigResTax
								+ " total tax payable " + totalBigResTax);
					}
				}
			}

			for (UnitTaxCalculationInfo conTaxInfo : taxCalcInfo
					.getConsolidatedUnitTaxCalculationInfo()) {
				BigDecimal bigBldgTax = consdTotalTaxUnitWise.get(conTaxInfo
						.getUnitNumber());
				MiscellaneousTax bigBldgMiscTax = consdMiscTaxUnitWise
						.get(conTaxInfo.getUnitNumber());
				BigDecimal bigBldgALV = consdBigBldgALVUnitWise.get(conTaxInfo
						.getUnitNumber());

				if (bigBldgALV != null) {
					conTaxInfo.setBigBuildingTaxALV(bigBldgALV);
				}
				if (bigBldgTax != null) {
					taxCalcInfo.setTotalTaxPayable(taxCalcInfo
							.getTotalTaxPayable().add(bigBldgTax));
					conTaxInfo.setTotalTaxPayable(conTaxInfo
							.getTotalTaxPayable().add(bigBldgTax));
				}
				if (bigBldgMiscTax != null) {
					conTaxInfo.addMiscellaneousTaxes(bigBldgMiscTax);
				}

			}
		}
	}

	/**
	 * Returns the applicable taxes for the given property
	 *
	 * @param property
	 * @return List of taxes
	 */
	public static List<String> prepareApplicableTaxes(Property property) {
		LOGGER.debug("Entered into prepareApplTaxes");
		LOGGER.debug("prepareApplTaxes: property: " + property);
		List<String> applicableTaxes = new ArrayList<String>();
		String propType = property.getPropertyDetail().getPropertyTypeMaster()
				.getCode();
		String waterRate = property.getPropertyDetail().getExtra_field1();
		String propTypeCategory = property.getPropertyDetail()
				.getExtra_field5();

		LOGGER.debug("prepareApplTaxes: propType: " + propType
				+ ", waterRate: " + waterRate);

		applicableTaxes.add(DEMANDRSN_CODE_FIRE_SERVICE_TAX);
		applicableTaxes.add(DEMANDRSN_CODE_SEWERAGE_TAX);
		applicableTaxes.add(DEMANDRSN_CODE_LIGHTINGTAX);
		applicableTaxes.add(DEMANDRSN_CODE_GENERAL_TAX);

		if (propType.equalsIgnoreCase(PROPTYPE_OPEN_PLOT)
				|| propType.equalsIgnoreCase(PROPTYPE_STATE_GOVT)
				|| propType.equalsIgnoreCase(PROPTYPE_CENTRAL_GOVT)) {
			if (waterRate.equals(GWR_IMPOSED)) {
				applicableTaxes.add(DEMANDRSN_CODE_GENERAL_WATER_TAX);
			}
		}

		if (propType.equalsIgnoreCase(PROPTYPE_NON_RESD)) {
			applicableTaxes.add(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD);
			applicableTaxes.add(DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX);
		}

		if (propType.equalsIgnoreCase(PROPTYPE_RESD)) {
			applicableTaxes.add(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD);
		}

		if (propType.equalsIgnoreCase(PROPTYPE_OPEN_PLOT)) {
			if (NMCPTISConstants.PROPTYPE_CAT_RESD
					.equalsIgnoreCase(propTypeCategory)) {
				applicableTaxes.add(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD);
			} else if (NMCPTISConstants.PROPTYPE_CAT_NON_RESD
					.equalsIgnoreCase(propTypeCategory)) {
				applicableTaxes.add(DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD);
				applicableTaxes.add(DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX);
			}
		}

		if (propType.equalsIgnoreCase(PROPTYPE_OPEN_PLOT)) {
			boolean result = false;
			List<String> exemptedTaxes = null;

			if (property.getIsExemptedFromTax() != null
					&& property.getIsExemptedFromTax()) {
				exemptedTaxes = NMCPTISConstants.exemptedTaxesByReason
						.get(property.getTaxExemptReason());
				result = applicableTaxes.removeAll(exemptedTaxes);
			}

			if (result) {
				LOGGER.debug("calculateUnitTax - Open plot is exempted from taxes "
						+ exemptedTaxes);
			} else {
				LOGGER.debug("calculateUnitTax - Open Plot is not exempted from taxes ");
			}
		}

		LOGGER.debug("prepareApplTaxes: applicableTaxes: " + applicableTaxes);
		LOGGER.debug("Exiting from prepareApplTaxes");
		return applicableTaxes;
	}

	/**
	 * Gives the Inactive demand property
	 *
	 * <p>
	 * Property whose status is I is Inactive Demand property, demand will be
	 * activated either if the citizen pays tax or after 21 days from the date
	 * of notice generation
	 * </p>
	 *
	 * @param basicProperty
	 * @return
	 */
	public static Property getInactiveDemandProperty(BasicProperty basicProperty) {
		LOGGER.debug("Entered into getInactiveDemandProperty");

		for (Property property : basicProperty.getPropertySet()) {
			if (property.getStatus().equals(
					PropertyTaxConstants.STATUS_DEMAND_INACTIVE)
					&& property.getIsDefaultProperty().equals('Y')) {
				return property;
			}
		}

		LOGGER.debug("Exiting from getInactiveDemandProperty");

		return null;
	}

	/**
	 * Gives the lowest year from which demand is effective for the give
	 * property
	 *
	 * @param property
	 * @return
	 */
	public static String getRevisedDemandYear(Property property) {
		LOGGER.debug("Entered into getDemandEffectiveYear, property="
				+ property);
		String demandEffectiveYear = null;

		demandEffectiveYear = new SimpleDateFormat("yyyy").format(property
				.getPropertyDetail().getEffective_date());
		LOGGER.debug("getRevisedDemandYear - demandEffectiveYear="
				+ demandEffectiveYear);

		LOGGER.debug("Exting from getDemandEffectiveYear");
		return demandEffectiveYear;
	}

	/**
	 * Returns the notice days remaining for inactive demand
	 *
	 * @param property
	 * @return
	 * @throws ParseException
	 */
	public static Integer getNoticeDaysForInactiveDemand(Property property)
			throws ParseException {
		String query = "";

		List result = null;
		Integer days = 21;
		Date noticeDate = null;
		String indexNumber = property.getBasicProperty().getUpicNo();

		if (isNoticeGenerated(property)) {
			result = HibernateUtil
					.getCurrentSession()
					.createQuery(
							"select to_char(n.noticeDate, 'dd/mm/yyyy') from PtNotice n "
									+ "where n.basicProperty = :basicProp "
									+ "and n.noticeDate is not null "
									+ "and n.noticeDate >= :propCreatedDate")
					.setEntity("basicProp", property.getBasicProperty())
					.setDate("propCreatedDate",
							property.getCreatedDate().toDate()).list();
			if (result.isEmpty()) {
				LOGGER.debug("Notice generation date is not available for property="
						+ indexNumber);
			} else {
				noticeDate = new SimpleDateFormat(
						PropertyTaxConstants.DATE_FORMAT_DDMMYYY).parse(result.get(
						0).toString());
				if (noticeDate.before(new Date())) {
					days = days
							- getNumberOfDays(noticeDate, new Date())
									.intValue();
					days += 1;
				}
			}

		} else {
			LOGGER.debug("getNoticeDaysForInactiveDemand - Notice is not yet generated for property="
					+ indexNumber);
			LOGGER.debug("getNoticeDaysForInactiveDemand - using defualt notice period days "
					+ days);
		}

		return days;
	}

	public static boolean isNoticeGenerated(Property property) {
		return (property.getExtra_field3() != null && property
				.getExtra_field3().equalsIgnoreCase(
						PropertyTaxConstants.STR_YES))
				|| (property.getExtra_field4() != null && property
						.getExtra_field4().equalsIgnoreCase(
								PropertyTaxConstants.STR_YES)) ? true : false;
	}

	public static List<String> getAdvanceYearsFromCurrentInstallment() {
		LOGGER.debug("Entered into getAdvanceYearsFromCurrentInstallment");

		List<String> advanceYears = new ArrayList<String>();
		Installment currentInstallment = PropertyTaxUtil
				.getCurrentInstallment();
		Integer year = null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentInstallment.getFromDate());
		year = calendar.get(Calendar.YEAR);

		for (int i = 0; i < MAX_ADVANCES_ALLOWED; i++) {

			int fromYear = ++year;
			int toYear = year + 1;

			advanceYears.add(fromYear + "-"
					+ String.valueOf(toYear).substring(2));
		}
		LOGGER.debug("getAdvanceYearsFromCurrentInstallment = " + advanceYears);
		LOGGER.debug("Exiting from getAdvanceYearsFromCurrentInstallment");

		return advanceYears;
	}

	@SuppressWarnings("unchecked")
	public List<Installment> getOrderedInstallmentsFromGivenDate(Date date) {
		return persistenceService
				.findAllBy(
						"select it from org.egov.commons.Installment it "
								+ "where (it.fromDate>=? or ? between it.fromDate and it.toDate) "
								+ "and it.fromDate<=sysdate "
								+ "and it.module.moduleName=? order by installmentYear",
						date, date, PTMODULENAME);
	}

	/**
	 * Returns map of EgDemandDetails and Reason master
	 * 
	 * @param egDemandDetails
	 * @return
	 */
	public Map<String, EgDemandDetails> getEgDemandDetailsAndReasonAsMap(
			Set<EgDemandDetails> egDemandDetails) {

		Map<String, EgDemandDetails> demandDetailAndReason = new HashMap<String, EgDemandDetails>();

		for (EgDemandDetails egDmndDtls : egDemandDetails) {

			EgDemandReasonMaster dmndRsnMstr = egDmndDtls.getEgDemandReason()
					.getEgDemandReasonMaster();

			if (dmndRsnMstr.getCode().equalsIgnoreCase(
					DEMANDRSN_CODE_GENERAL_TAX)) {
				demandDetailAndReason.put(DEMANDRSN_CODE_GENERAL_TAX,
						egDmndDtls);
			} else if (dmndRsnMstr.getCode().equalsIgnoreCase(
					DEMANDRSN_CODE_SEWERAGE_TAX)) {
				demandDetailAndReason.put(DEMANDRSN_CODE_SEWERAGE_TAX,
						egDmndDtls);
			} else if (dmndRsnMstr.getCode().equalsIgnoreCase(
					DEMANDRSN_CODE_FIRE_SERVICE_TAX)) {
				demandDetailAndReason.put(DEMANDRSN_CODE_FIRE_SERVICE_TAX,
						egDmndDtls);
			} else if (dmndRsnMstr.getCode().equalsIgnoreCase(
					DEMANDRSN_CODE_LIGHTINGTAX)) {
				demandDetailAndReason.put(DEMANDRSN_CODE_LIGHTINGTAX,
						egDmndDtls);
			} else if (dmndRsnMstr.getCode().equalsIgnoreCase(
					DEMANDRSN_CODE_GENERAL_WATER_TAX)) {
				demandDetailAndReason.put(DEMANDRSN_CODE_GENERAL_WATER_TAX,
						egDmndDtls);
			} else if (dmndRsnMstr.getCode().equalsIgnoreCase(
					DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD)) {
				demandDetailAndReason.put(DEMANDRSN_CODE_EDUCATIONAL_CESS_RESD,
						egDmndDtls);
			} else if (dmndRsnMstr.getCode().equalsIgnoreCase(
					DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD)) {
				demandDetailAndReason.put(
						DEMANDRSN_CODE_EDUCATIONAL_CESS_NONRESD, egDmndDtls);
			} else if (dmndRsnMstr.getCode().equalsIgnoreCase(
					DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX)) {
				demandDetailAndReason.put(
						DEMANDRSN_CODE_EMPLOYEE_GUARANTEE_TAX, egDmndDtls);
			} else if (dmndRsnMstr.getCode().equalsIgnoreCase(
					DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX)) {
				demandDetailAndReason.put(
						DEMANDRSN_CODE_BIG_RESIDENTIAL_BLDG_TAX, egDmndDtls);
			} else if (dmndRsnMstr.getCode().equalsIgnoreCase(
					DEMANDRSN_CODE_PENALTY_FINES)) {
				demandDetailAndReason.put(DEMANDRSN_CODE_PENALTY_FINES,
						egDmndDtls);
			} else if (dmndRsnMstr.getCode().equalsIgnoreCase(
					DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY)) {
				demandDetailAndReason.put(DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY,
						egDmndDtls);
			} else if (dmndRsnMstr.getCode().equalsIgnoreCase(
					NMCPTISConstants.DEMANDRSN_CODE_ADVANCE)) {
				demandDetailAndReason.put(
						NMCPTISConstants.DEMANDRSN_CODE_ADVANCE, egDmndDtls);
			}
		}

		return demandDetailAndReason;
	}

	/**
	 * Gives the Earliest modification date
	 * 
	 * @param propertyId
	 * @return
	 */
	public Date getEarliestModificationDate(String propertyId) {
		List result = HibernateUtil
				.getCurrentSession()
				.createQuery(
						"select to_char(min(pd.effective_date), 'dd/mm/yyyy') "
								+ "from PropertyImpl p inner join p.propertyDetail pd "
								+ "where p.basicProperty.active = true "
								+ "and p.basicProperty.upicNo = :upicNo "
								+ "and (p.remarks is null or p.remarks <> :propertyMigrationRemarks) "
								+ "and pd.effective_date is not null")
				.setString("upicNo", propertyId)
				.setString("propertyMigrationRemarks",
						NMCPTISConstants.STR_MIGRATED_REMARKS).list();

		Date earliestModificationDate = null;

		if (result.isEmpty()) {
			return null;
		} else {
			if (result.get(0) == null) {
				return null;
			}
		}

		try {
			earliestModificationDate = dateFormatter.parse((String) result
					.get(0));
		} catch (ParseException e) {
			LOGGER.error("Error while parsing effective date", e);
			throw new EGOVRuntimeException(
					"Error while parsing effective date", e);
		}

		return earliestModificationDate;
	}

	/**
	 * @param dateFormat
	 * @param waterTaxEffectiveDate
	 * @return
	 */
	public static Date getWaterTaxEffectiveDateForPenalty() {
		Date waterTaxEffectiveDate = null;
		org.slf4j.Logger LOG = LoggerFactory.getLogger(PropertyTaxUtil.class);

		try {
			waterTaxEffectiveDate = dateFormatter
					.parse(PENALTY_WATERTAX_EFFECTIVE_DATE);
		} catch (ParseException pe) {
			throw new EGOVRuntimeException(
					"Error while parsing Water Tax Effective Date for Penalty Calculation",
					pe);
		}

		LOG.debug(
				"getWaterTaxEffectiveDateForPenalty - waterTaxEffectiveDate = {} ",
				waterTaxEffectiveDate);
		return waterTaxEffectiveDate;
	}

	/**
	 * Gives the properties and occupancy date map
	 * 
	 * @return properties by occupancy dates
	 */
	public Map<Date, Property> getPropertiesByOccupancy(
			BasicProperty basicProperty) {
		LOGGER.debug("Entered into getPropertiesByOccupancy");

		Map<Date, Property> propertyByCreatedDate = new TreeMap<Date, Property>();
		Map<Date, Property> propertyByOccupancyDate = new TreeMap<Date, Property>();

		for (Property property : basicProperty.getPropertySet()) {
			if (inConsider(property)) {
				propertyByCreatedDate.put(property.getCreatedDate().toDate(),
						property);
			}
		}

		for (Map.Entry<Date, Property> entry : propertyByCreatedDate.entrySet()) {
			propertyByOccupancyDate.put(
					getPropertyOccupancyDate(entry.getValue()),
					entry.getValue());
		}

		LOGGER.debug("Exiting from getPropertiesByOccupancy");

		return propertyByOccupancyDate;
	}

	private boolean inConsider(Property property) {
		return (property.getRemarks() == null || !property.getRemarks()
				.startsWith(STR_MIGRATED));
	}

	@SuppressWarnings("unchecked")
	public Map<String, Map<Installment, BigDecimal>> prepareReasonWiseDenandAndCollection(
			Property property, Installment currentInstallment) {
		LOGGER.debug("Entered into prepareReasonWiseDenandAndCollection, property="
				+ property);

		Map<Installment, BigDecimal> installmentWiseDemand = new TreeMap<Installment, BigDecimal>();
		Map<Installment, BigDecimal> installmentWiseCollection = new TreeMap<Installment, BigDecimal>();
		Map<String, Map<Installment, BigDecimal>> demandAndCollection = new HashMap<String, Map<Installment, BigDecimal>>();

		String demandReason = "";
		Installment installment = null;

		List<String> demandReasonExcludeList = Arrays.asList(
				DEMANDRSN_CODE_PENALTY_FINES,
				NMCPTISConstants.DEMANDRSN_CODE_ADVANCE);

		String query = "select ptd from Ptdemand ptd "
				+ "inner join fetch ptd.egDemandDetails dd "
				+ "inner join fetch dd.egDemandReason dr "
				+ "inner join fetch dr.egDemandReasonMaster drm "
				+ "inner join fetch ptd.egptProperty p "
				+ "inner join fetch p.basicProperty bp "
				+ "where bp.active = true "
				+ "and (p.status = 'A' or p.status = 'I') "
				+ "and p = :property "
				+ "and ptd.egInstallmentMaster = :installment";

		Ptdemand ptDemand = (Ptdemand) HibernateUtil.getCurrentSession()
				.createQuery(query).setEntity("property", property)
				.setEntity("installment", currentInstallment).list().get(0);

		for (EgDemandDetails dmdDet : ptDemand.getEgDemandDetails()) {

			demandReason = dmdDet.getEgDemandReason().getEgDemandReasonMaster()
					.getCode();

			if (!demandReasonExcludeList.contains(demandReason)) {
				installment = dmdDet.getEgDemandReason()
						.getEgInstallmentMaster();

				if (installmentWiseDemand.get(installment) == null) {
					installmentWiseDemand.put(installment, dmdDet.getAmount());
				} else {
					installmentWiseDemand.put(
							installment,
							installmentWiseDemand.get(installment).add(
									dmdDet.getAmount()));
				}

				if (installmentWiseCollection.get(installment) == null) {
					installmentWiseCollection.put(installment,
							dmdDet.getAmtCollected());
				} else {
					installmentWiseCollection.put(
							installment,
							installmentWiseCollection.get(installment).add(
									dmdDet.getAmtCollected()));
				}
			}
		}

		demandAndCollection.put("DEMAND", installmentWiseDemand);
		demandAndCollection.put("COLLECTION", installmentWiseCollection);

		LOGGER.debug("prepareReasonWiseDenandAndCollection - demandAndCollection="
				+ demandAndCollection);
		LOGGER.debug("Exiting from prepareReasonWiseDenandAndCollection");
		return demandAndCollection;
	}

	@SuppressWarnings("unchecked")
	public Map<Date, Property> getPropertiesForPenlatyCalculation(
			BasicProperty basicProperty) {

		String query = "select p from PropertyImpl p "
				+ "inner join fetch p.basicProperty bp "
				+ "where bp.upicNo = ? and bp.active = true "
				+ "and (p.remarks = null or p.remarks <> ?) "
				+ "order by p.createdDate";

		List<Property> allProperties = HibernateUtil.getCurrentSession()
				.createQuery(query).setString(0, basicProperty.getUpicNo())
				.setString(1, NMCPTISConstants.STR_MIGRATED_REMARKS).list();

		List<Property> properties = new ArrayList<Property>();

		List<String> mutationsCodes = Arrays.asList("NEW", "MODIFY");
		Property property = null;
		Property prevProperty = null;
		String mutationCode = null;
		String nextMutationCode = null;
		String prevMutationCode = null;
		int noOfProperties = allProperties.size();

		Map<Date, Property> propertyAndEffectiveDate = new TreeMap<Date, Property>();
		Date firstDataEntryEffectiveDate = null;

		for (int i = 0; i < noOfProperties; i++) {
			property = allProperties.get(i);
			prevProperty = i > 0 ? allProperties.get(i - 1) : null;

			prevMutationCode = prevProperty != null ? prevProperty
					.getPropertyDetail().getPropertyMutationMaster().getCode()
					: null;
			mutationCode = property.getPropertyDetail()
					.getPropertyMutationMaster().getCode();
			nextMutationCode = i != (noOfProperties - 1) ? allProperties
					.get(i + 1).getPropertyDetail().getPropertyMutationMaster()
					.getCode() : null;

			if (!mutationCode
					.equalsIgnoreCase(NMCPTISConstants.PROPERTY_MODIFY_REASON_OBJ)) {
				if (mutationsCodes.contains(mutationCode)) {
					propertyAndEffectiveDate.put(
							getPropertyOccupancyDate(property), property);
				} else {

					if (isFirstDataEntry(prevMutationCode, mutationCode,
							prevProperty)) {
						firstDataEntryEffectiveDate = getPropertyOccupancyDate(property);
					}

					if (mutationCode
							.equalsIgnoreCase(PROPERTY_MODIFY_REASON_DATA_ENTRY)
							&& (nextMutationCode == null || areNoticesGenerated(property))) {
						propertyAndEffectiveDate.put(
								firstDataEntryEffectiveDate, property);
					}
				}
			}
		}

		Map<Date, Property> propertyByOccupancyDate = new TreeMap<Date, Property>();

		for (Map.Entry<Date, Property> entry : propertyAndEffectiveDate
				.entrySet()) {

			if (entry.getKey() == null) {
				propertyByOccupancyDate.put(
						getPropertyOccupancyDate(entry.getValue()),
						entry.getValue());
			} else {
				propertyByOccupancyDate.put(entry.getKey(), entry.getValue());
			}
		}

		return propertyByOccupancyDate;
	}

	private boolean areNoticesGenerated(Property property) {
		boolean isNotice134Or127Generated = property.getExtra_field3() != null
				&& property.getExtra_field3().equalsIgnoreCase("Yes") ? true
				: false;
		boolean isNoticePVRGenerated = property.getExtra_field4() != null
				&& property.getExtra_field4().equalsIgnoreCase("Yes") ? true
				: false;
		return isNotice134Or127Generated && isNoticePVRGenerated ? true : false;
	}

	private boolean isFirstDataEntry(String prevPropMutationCode,
			String mutationCode, Property prevProperty) {

		List<String> mutationCodes = Arrays.asList(
				NMCPTISConstants.MUTATION_CODE_NEW,
				NMCPTISConstants.PROPERTY_MODIFY_REASON_OBJ);

		if (prevPropMutationCode != null
				&& mutationCodes.contains(prevPropMutationCode)
				&& mutationCode
						.equalsIgnoreCase(PROPERTY_MODIFY_REASON_DATA_ENTRY)) {
			return true;
		}

		return (prevProperty == null || areNoticesGenerated(prevProperty))
				&& mutationCode
						.equalsIgnoreCase(PROPERTY_MODIFY_REASON_DATA_ENTRY) ? true
				: false;
	}

	private boolean isDataEntryApproved(String prevPropMutationCode) {
		List<String> mutationsCodes = Arrays.asList("NEW", "MODIFY");
		return prevPropMutationCode == null
				|| mutationsCodes.contains(prevPropMutationCode) ? true : false;
	}

	/**
	 * Returns true if the date is later than dateToCompare OR date is same as
	 * dateToCompare
	 * 
	 * @param date
	 * @param dateToCompare
	 * @return true if date is after dateToCompare or date is equal to
	 *         dateToCompare
	 */
	public static boolean afterOrEqual(Date date, Date dateToCompare) {
		return date.after(dateToCompare) || date.equals(dateToCompare);
	}
}
