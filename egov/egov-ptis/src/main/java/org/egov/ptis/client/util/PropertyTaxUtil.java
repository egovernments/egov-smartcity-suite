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
package org.egov.ptis.client.util;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.egov.ptis.constants.PropertyTaxConstants.AMENITY_PERCENTAGE_FULL;
import static org.egov.ptis.constants.PropertyTaxConstants.AMENITY_PERCENTAGE_NIL;
import static org.egov.ptis.constants.PropertyTaxConstants.AMENITY_PERCENTAGE_PARTIAL;
import static org.egov.ptis.constants.PropertyTaxConstants.AMENITY_TYPE_FULL;
import static org.egov.ptis.constants.PropertyTaxConstants.AMENITY_TYPE_NIL;
import static org.egov.ptis.constants.PropertyTaxConstants.AMENITY_TYPE_PARTIAL;
import static org.egov.ptis.constants.PropertyTaxConstants.AMP_ACTUAL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.AMP_ENCODED_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ARREARS_DMD;
import static org.egov.ptis.constants.PropertyTaxConstants.ARREAR_REBATE_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.BIGDECIMAL_100;
import static org.egov.ptis.constants.PropertyTaxConstants.COMMA_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURRENT_DMD;
import static org.egov.ptis.constants.PropertyTaxConstants.CURRENT_REBATE_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.DATE_CONSTANT;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_EDUCATIONAL_CESS;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_GENERAL_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_LIBRARY_CESS;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_PENALTY_FINES;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_UNAUTHORIZED_PENALTY;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_REBATE;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMAND_REASON_ORDER_MAP;
import static org.egov.ptis.constants.PropertyTaxConstants.EFFECTIVE_ASSESSMENT_PERIOD1;
import static org.egov.ptis.constants.PropertyTaxConstants.EFFECTIVE_ASSESSMENT_PERIOD2;
import static org.egov.ptis.constants.PropertyTaxConstants.EFFECTIVE_ASSESSMENT_PERIOD3;
import static org.egov.ptis.constants.PropertyTaxConstants.EFFECTIVE_ASSESSMENT_PERIOD4;
import static org.egov.ptis.constants.PropertyTaxConstants.FLOORNO_WITH_DIFF_MULFACTOR_NONRESD;
import static org.egov.ptis.constants.PropertyTaxConstants.FLOORNO_WITH_DIFF_MULFACTOR_RESD;
import static org.egov.ptis.constants.PropertyTaxConstants.FLOOR_MAP;
import static org.egov.ptis.constants.PropertyTaxConstants.MAX_ADVANCES_ALLOWED;
import static org.egov.ptis.constants.PropertyTaxConstants.NONRESD_FLAT;
import static org.egov.ptis.constants.PropertyTaxConstants.NON_HISTORY_TAX_DETAIL;
import static org.egov.ptis.constants.PropertyTaxConstants.NON_RESIDENTIAL_FLOOR_AREA_MAP;
import static org.egov.ptis.constants.PropertyTaxConstants.PENALTY_WATERTAX_EFFECTIVE_DATE;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_MODIFY_REASON_DATA_ENTRY;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_MODIFY_REASON_MODIFY;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_NON_RESD;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPTYPE_RESD;
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_BASERATE_BY_OCCUPANCY_ZONE;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_DEMANDREASONBY_CODE_AND_INSTALLMENTID;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_DEMANDREASONDETAILBY_DEMANDREASONID;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_DEMANDREASONDETAILS_BY_DEMANDREASON_AND_INSTALLMENT;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_DEPARTMENTS_BY_DEPTCODE;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_INSTALLMENTLISTBY_MODULE_AND_STARTYEAR;
import static org.egov.ptis.constants.PropertyTaxConstants.RESIDENTIAL_FLATS;
import static org.egov.ptis.constants.PropertyTaxConstants.RESIDENTIAL_FLOOR_AREA_MAP;
import static org.egov.ptis.constants.PropertyTaxConstants.SESSION_VAR_LOGIN_USER_NAME;
import static org.egov.ptis.constants.PropertyTaxConstants.STATUS_MIGRATED;
import static org.egov.ptis.constants.PropertyTaxConstants.STRING_SEPERATOR;
import static org.egov.ptis.constants.PropertyTaxConstants.STR_MIGRATED;
import static org.egov.ptis.constants.PropertyTaxConstants.USAGES_FOR_NON_RESD;
import static org.egov.ptis.constants.PropertyTaxConstants.USAGES_FOR_OPENPLOT;
import static org.egov.ptis.constants.PropertyTaxConstants.USAGES_FOR_RESD;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_NAME_AMALGAMATE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_NAME_BIFURCATE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_NAME_CHANGEADDRESS;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_NAME_CREATE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_NAME_DEACTIVATE;
import static org.egov.ptis.constants.PropertyTaxConstants.WFLOW_ACTION_NAME_MODIFY;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
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
import org.egov.eis.entity.Assignment;
import org.egov.eis.entity.Employee;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.EisCommonService;
import org.egov.eis.service.EmployeeService;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.persistence.entity.Address;
import org.egov.infstr.config.dao.AppConfigValuesDAO;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.infstr.utils.MoneyUtils;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.ptis.client.handler.TaxCalculationInfoXmlHandler;
import org.egov.ptis.client.model.ApplicableFactor;
import org.egov.ptis.client.model.PropertyArrearBean;
import org.egov.ptis.client.model.PropertyInstTaxBean;
import org.egov.ptis.client.model.calculator.APMiscellaneousTax;
import org.egov.ptis.client.model.calculator.APMiscellaneousTaxDetail;
import org.egov.ptis.client.model.calculator.APTaxCalculationInfo;
import org.egov.ptis.client.model.calculator.APUnitTaxCalculationInfo;
import org.egov.ptis.client.workflow.ActionAmalgmate;
import org.egov.ptis.client.workflow.ActionBifurcate;
import org.egov.ptis.client.workflow.ActionChangeAddress;
import org.egov.ptis.client.workflow.ActionCreate;
import org.egov.ptis.client.workflow.ActionDeactivate;
import org.egov.ptis.client.workflow.ActionModify;
import org.egov.ptis.client.workflow.ActionNameTransfer;
import org.egov.ptis.client.workflow.WorkflowDetails;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.bill.PropertyTaxBillable;
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
import org.egov.ptis.domain.entity.property.Floor;
import org.egov.ptis.domain.entity.property.Floor;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyArrear;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyOwnerInfo;
import org.egov.ptis.domain.entity.property.PropertyStatusValues;
import org.egov.ptis.domain.entity.property.WorkflowBean;
import org.egov.ptis.domain.model.calculator.MiscellaneousTax;
import org.egov.ptis.domain.model.calculator.MiscellaneousTaxDetail;
import org.egov.ptis.domain.model.calculator.TaxCalculationInfo;
import org.egov.ptis.domain.model.calculator.UnitTaxCalculationInfo;
import org.egov.ptis.utils.PTISCacheManager;
import org.hibernate.Query;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class PropertyTaxUtil {
	private static final Logger LOGGER = Logger.getLogger(PropertyTaxUtil.class);
	
	@Autowired
	private PersistenceService persistenceService;
	@Autowired
	private UserService userService;
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private AssignmentService assignmentService;
	@Autowired
	private EisCommonService eisCommonService;
	@Autowired
	private AppConfigValuesDAO appConfigValuesDAO;
	@Autowired
	private ModuleService moduleService;
	@Autowired
	private InstallmentDao installmentDao;
	@Autowired
	private PtDemandDao ptDemandDAO;
	@Autowired
	private BoundaryCategoryDao boundaryCategoryDAO;

	@Autowired
	private DemandGenericHibDao demandGenericDAO;

	@Autowired
	private BasicPropertyDAO basicPropertyDAO;
	@Autowired
	private EgBillDao egBillDAO;
	@Autowired
	private PropertyDAO propertyDAO;

	private Map<Date, Property> getPropertiesByCreatedDate(Map<Date, Property> historyProperties) {
		Map<Date, Property> historyPropsByCrtdDate = new TreeMap<Date, Property>();

		for (Map.Entry<Date, Property> entry : historyProperties.entrySet()) {
			historyPropsByCrtdDate.put(entry.getValue().getCreatedDate().toDate(), entry.getValue());
		}

		return historyPropsByCrtdDate;
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
	 * @param historyProperty
	 * @param floorIdString
	 */
	public static String getFloorIdsAsString(Property historyProperty) {
		LOGGER.debug("Entered into getFloorIdsAsString, historyProperty=" + historyProperty);

		StringBuilder floorIdString = new StringBuilder();
		floorIdString.append("(");
		int count = 0;
		for (Floor floor : historyProperty.getPropertyDetail().getFloorDetails()) {
			floorIdString.append(floor.getId());
			count++;
			if (count != historyProperty.getPropertyDetail().getFloorDetails().size()) {
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
				if (mtd.getIsHistory() == null || mtd.getIsHistory().equals(NON_HISTORY_TAX_DETAIL)) {
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

		unitTax.setTotalTaxPayable(totalTax.setScale(0, BigDecimal.ROUND_HALF_UP));
	}

	public TaxCalculationInfo getTaxCalInfo(Installment installment, Property property) {
		LOGGER.debug("Entered into getTaxCalInfo, installment=" + installment + ", property=" + property);

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

	private BigDecimal roundHistoryTax(UnitTaxCalculationInfo unitTaxCalculationInfo, BigDecimal totalTaxPayable) {

		LOGGER.debug("roundHistoryTax - totalTaxPayable : " + totalTaxPayable);

		for (MiscellaneousTax miscTax : unitTaxCalculationInfo.getMiscellaneousTaxes()) {
			// LOGGER.info("Tax : " + miscTax.getTaxName());
			Boolean hasOnlyHistoryTaxDetails = false;
			for (MiscellaneousTaxDetail taxDetail : miscTax.getTaxDetails()) {
				// LOGGER.info("isHistory : " + taxDetail.getIsHistory());
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

	/**
	 * Return the number of days between dates.
	 *
	 * @param installment
	 * @param drd
	 * @return
	 */

	public Integer getNumberOfDays(Installment installment, EgDemandReasonDetails drd, Date unitOccupancyDate,
			BoundaryCategory boundaryCategory) {
		LOGGER.debug("Entered into getNumberOfDays - Installment: " + installment + "DemandReasonDetails: " + drd
				+ ", unitOccupancyDate: " + unitOccupancyDate + ", boundaryCategory=" + boundaryCategory);
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
			LOGGER.debug("getNumberOfDays: Installment - " + installment + ", days between " + fromDate + " and "
					+ toDate + " are " + noOfDays);
		}
		LOGGER.debug("Exiting from getNumberOfDays");
		return noOfDays;
	}

	public Integer getNumberOfDaysBeforeOccupancy(Installment installment, EgDemandReasonDetails drd,
			Date unitOccupancyDate, Date oldUnitOccupancyDate, BoundaryCategory boundaryCategory) {
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
	public BigDecimal calcGovtTaxOnAmenities(String amenities, BigDecimal applicableTaxValue) {
		BigDecimal applicableTaxValueDummy = applicableTaxValue;
		if (amenities.equalsIgnoreCase(AMENITY_TYPE_FULL)) {
			applicableTaxValueDummy = applicableTaxValueDummy.multiply(new BigDecimal(AMENITY_PERCENTAGE_FULL)
					.divide(BIGDECIMAL_100));
		} else if (amenities.equalsIgnoreCase(AMENITY_TYPE_PARTIAL)) {
			applicableTaxValueDummy = applicableTaxValueDummy.multiply(new BigDecimal(AMENITY_PERCENTAGE_PARTIAL)
					.divide(BIGDECIMAL_100));
		} else if (amenities.equalsIgnoreCase(AMENITY_TYPE_NIL)) {
			applicableTaxValueDummy = applicableTaxValueDummy.multiply(new BigDecimal(AMENITY_PERCENTAGE_NIL)
					.divide(BIGDECIMAL_100));
		}
		return applicableTaxValueDummy;
	}

	public String multiplicativeFactorAreaWise(String propertyType, Floor floor, String areaFactor,
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

	public List<ApplicableFactor> getApplicableFactorsForResidentialAndNonResidential(Floor floorImpl,
			Boundary propertyArea, Installment installment, Long categoryId) {
		List<ApplicableFactor> applicableFactors = new ArrayList<ApplicableFactor>();

		Category category = (Category) persistenceService.findByNamedQuery(QUERY_BASERATE_BY_OCCUPANCY_ZONE,
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
			applicableStructuralFactor.setFactorValue(new BigDecimal(Double.toString(category.getCategoryAmount())));
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
			applicableLocationFactor.setFactorValue(new BigDecimal(Double.toString(category.getCategoryAmount())));
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

	@SuppressWarnings("unchecked")
	public BoundaryCategory getBoundaryCategory(Boundary zone, Installment installment, Long usageId, Date occupancyDate) {
		List<BoundaryCategory> categories = new ArrayList<BoundaryCategory>();

		categories = persistenceService.findAllByNamedQuery(QUERY_BASERATE_BY_OCCUPANCY_ZONE, zone.getId(), usageId,
				occupancyDate, installment.getToDate());

		LOGGER.debug("baseRentOfUnit - Installment : " + installment);
		return categories.get(0);
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

	public PersistenceService getPersistenceService() {
		return this.persistenceService;
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
		return boundaryCategoryDAO.getCategoryForBoundary(boundary);
	}

	public List<Installment> getInstallmentListByStartDate(Date startDate) {
		return persistenceService.findAllByNamedQuery(QUERY_INSTALLMENTLISTBY_MODULE_AND_STARTYEAR, startDate,
				startDate, PTMODULENAME);
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
			AppConfigValues appConfigValues = appConfigValuesDAO.getAppConfigValueByDate(moduleName, key, new Date());
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
		AppConfigValues appConfigValues = appConfigValuesDAO.getAppConfigValueByDate(moduleName, key, new Date());
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
			if (taxCalInfo.getPropertyId() == null) {
				taxCalInfo.setPropertyId(demand.getEgptProperty().getBasicProperty().getUpicNo());
			}
		}
		return taxCalInfo;
	}

	public static BigDecimal roundOffTax(BigDecimal tax) {
		return MoneyUtils.roundOff(tax);
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
			// considering rebate as collection and substracting it.
			taxSum.put(DEMANDRSN_CODE_GENERAL_TAX, tmpVal.add(((BigDecimal) data[2]).subtract(((BigDecimal) data[3]))));
		} else if ((data[0].toString()).equals(DEMANDRSN_CODE_LIBRARY_CESS)) {
			tmpVal = taxSum.get(DEMANDRSN_CODE_LIBRARY_CESS);
			taxSum.put(DEMANDRSN_CODE_LIBRARY_CESS, tmpVal.add(((BigDecimal) data[2]).subtract(((BigDecimal) data[3]))));
		} else if ((data[0].toString()).equals(DEMANDRSN_CODE_EDUCATIONAL_CESS)) {
			tmpVal = taxSum.get(DEMANDRSN_CODE_EDUCATIONAL_CESS);
			taxSum.put(DEMANDRSN_CODE_EDUCATIONAL_CESS,
					tmpVal.add(((BigDecimal) data[2]).subtract(((BigDecimal) data[3]))));
		} else if ((data[0].toString()).equals(DEMANDRSN_CODE_UNAUTHORIZED_PENALTY)) {
			tmpVal = taxSum.get(DEMANDRSN_CODE_UNAUTHORIZED_PENALTY);
			taxSum.put(DEMANDRSN_CODE_UNAUTHORIZED_PENALTY,
					tmpVal.add(((BigDecimal) data[2]).subtract(((BigDecimal) data[3]))));
		} else if ((data[0].toString()).equals(DEMANDRSN_CODE_PENALTY_FINES)) {
			tmpVal = taxSum.get(DEMANDRSN_CODE_PENALTY_FINES);
			taxSum.put(DEMANDRSN_CODE_PENALTY_FINES,
					tmpVal.add(((BigDecimal) data[2]).subtract(((BigDecimal) data[3]))));
		} else if ((data[0].toString()).equals(DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY)) {
			tmpVal = taxSum.get(DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY);
			taxSum.put(DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY,
					tmpVal.add(((BigDecimal) data[2]).subtract(((BigDecimal) data[3]))));
		}

		return taxSum;
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
		BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(propertyId);
		EgDemand egDemand = ptDemandDAO.getNonHistoryCurrDmdForProperty(basicProperty.getProperty());
		Module module = moduleService.getModuleByName(PropertyTaxConstants.PTMODULENAME);
		Installment currentInstall = installmentDao.getInsatllmentByModuleForGivenDate(module, new Date());

		list = demandGenericDAO.getReasonWiseDCB(egDemand, module);

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
	 * Called locally to initialize
	 *
	 * @param taxSum
	 *            Map of demand reasons
	 * @return Map with demand reasons initialized
	 */
	private Map<String, BigDecimal> initReasonsMap(Map<String, BigDecimal> taxSum) {

		taxSum.put(DEMANDRSN_CODE_GENERAL_TAX, BigDecimal.ZERO);
		taxSum.put(DEMANDRSN_CODE_LIBRARY_CESS, BigDecimal.ZERO);
		taxSum.put(DEMANDRSN_CODE_EDUCATIONAL_CESS, BigDecimal.ZERO);
		taxSum.put(DEMANDRSN_CODE_UNAUTHORIZED_PENALTY, BigDecimal.ZERO);
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
		return userService.getUserByUsername((String) sessionMap.get(SESSION_VAR_LOGIN_USER_NAME));
	}

	/**
	 * @param user
	 *            the user whose department is to be returned
	 * @return department of the given user
	 */
	private Department getDepartmentOfUser(User user) {
		return getAssignment(user.getId()).getDepartment();
	}

	/**
	 * @param Integer
	 *            the userId
	 * @return Assignment for current date and for
	 *         <code> PersonalInformation </code>
	 */
	private Assignment getAssignment(Long userId) {
		Employee  empForUserId = employeeService.getEmployeeById(userId);
		Assignment assignmentByEmpAndDate = assignmentService.getPrimaryAssignmentForEmployeeByToDate(empForUserId.getId(),	new Date());
		return assignmentByEmpAndDate;
	}

	public HashMap<String, Integer> generateOrderForDemandDetails(Set<EgDemandDetails> demandDetails,
			PropertyTaxBillable nmcBillable) {

		HashMap<Integer, String> instReasonMap = new HashMap<Integer, String>();
		HashMap<String, Integer> orderMap = new HashMap<String, Integer>();
		BigDecimal balance = BigDecimal.ZERO;
		String key = "";
		String reasonMasterCode = null;

		for (EgDemandDetails demandDetail : demandDetails) {
			balance = BigDecimal.ZERO;
			balance = demandDetail.getAmount().subtract(demandDetail.getAmtCollected());

			if (balance.compareTo(BigDecimal.ZERO) == 1) {

				EgDemandReason reason = demandDetail.getEgDemandReason();
				Installment installment = reason.getEgInstallmentMaster();

				Calendar cal = Calendar.getInstance();
				cal.setTime(installment.getInstallmentYear());

				reasonMasterCode = reason.getEgDemandReasonMaster().getCode();

				if (reasonMasterCode.equals(DEMANDRSN_CODE_GENERAL_TAX)) {

					key = String
							.valueOf(getOrder(cal.get(Calendar.YEAR), DEMAND_REASON_ORDER_MAP.get(DEMANDRSN_REBATE)));
					instReasonMap.put(new Integer(key), cal.get(Calendar.YEAR) + "-" + DEMANDRSN_REBATE);

					key = String.valueOf(getOrder(cal.get(Calendar.YEAR), DEMAND_REASON_ORDER_MAP.get(reasonMasterCode)
							.intValue()));
					instReasonMap.put(new Integer(key), cal.get(Calendar.YEAR) + "-" + reasonMasterCode);

				} else {

					key = String.valueOf(getOrder(cal.get(Calendar.YEAR), DEMAND_REASON_ORDER_MAP.get(reasonMasterCode)
							.intValue()));
					instReasonMap.put(new Integer(key), cal.get(Calendar.YEAR) + "-" + reasonMasterCode);
				}
			}
		}

		List<String> advanceYears = getAdvanceYearsFromCurrentInstallment();
		Integer year = null;

		for (String description : advanceYears) {

			year = Integer.valueOf(description.substring(0, 4));

			/*
			 * key = String.valueOf(getOrder(year,
			 * DEMAND_REASON_ORDER_MAP.get(PropertyTaxConstants
			 * .DEMANDRSN_CODE_ADVANCE_REBATE)));
			 * 
			 * instReasonMap.put(new Integer(key), year + "-" +
			 * PropertyTaxConstants.DEMANDRSN_CODE_ADVANCE_REBATE);
			 */

			key = String.valueOf(getOrder(year,
					DEMAND_REASON_ORDER_MAP.get(PropertyTaxConstants.DEMANDRSN_CODE_ADVANCE)));

			instReasonMap.put(new Integer(key), year + "-" + PropertyTaxConstants.DEMANDRSN_CODE_ADVANCE);
		}

		Calendar cal = Calendar.getInstance();

		BigDecimal penaltyAmount = BigDecimal.ZERO;

		for (Map.Entry<Installment, PropertyInstTaxBean> mapEntry : nmcBillable.getInstTaxBean().entrySet()) {

			penaltyAmount = mapEntry.getValue().getInstPenaltyAmt();
			boolean thereIsPenalty = (penaltyAmount != null && penaltyAmount.compareTo(BigDecimal.ZERO) > 0);

			if (thereIsPenalty) {

				cal.setTime(mapEntry.getKey().getInstallmentYear());

				key = Integer.valueOf(
						getOrder(cal.get(Calendar.YEAR), DEMAND_REASON_ORDER_MAP.get(DEMANDRSN_CODE_PENALTY_FINES)))
						.toString();
				instReasonMap.put(new Integer(key), cal.get(Calendar.YEAR) + "-" + DEMANDRSN_CODE_PENALTY_FINES);
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
				installmentAndReason.get(Integer.valueOf(split[0])).put(split[1], entry.getValue());
			}
		}

		for (Integer installmentYear : installmentAndReason.keySet()) {
			if (installmentAndReason.get(installmentYear).get(DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY) != null) {
				orderMap.put(installmentAndReason.get(installmentYear).get(DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY), order++);
			}

			if (installmentAndReason.get(installmentYear).get(DEMANDRSN_CODE_PENALTY_FINES) != null) {
				orderMap.put(installmentAndReason.get(installmentYear).get(DEMANDRSN_CODE_PENALTY_FINES), order++);
			}
		}

		for (Integer installmentYear : installmentAndReason.keySet()) {
			for (String reasonCode : PropertyTaxConstants.ORDERED_DEMAND_RSNS_LIST) {

				if (reasonCode.equalsIgnoreCase(DEMANDRSN_CODE_PENALTY_FINES)
						|| reasonCode.equalsIgnoreCase(DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY)) {
					continue;
				}

				if (installmentAndReason.get(installmentYear).get(reasonCode) != null) {
					orderMap.put(installmentAndReason.get(installmentYear).get(reasonCode), order++);
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
	public List<Department> getDepartmentsForLoggedInUser(User user) {
		Department dept = getDepartmentOfUser(user);
		List<Department> departments = persistenceService.findAllByNamedQuery(QUERY_DEPARTMENTS_BY_DEPTCODE,
				dept.getCode());
		return departments;
	}

	public Designation getDesignationForUser(Long userId) {
		Position position = null;
		Designation designation = null;
		if (userId != null && userId.intValue() != 0) {
			position = eisCommonService.getPositionByUserId(userId);
			designation = position.getDeptDesig().getDesignation();
		}
		return designation;
	}

	public EgBillType getBillTypeByCode(String typeCode) {
		EgBillType billType = egBillDAO.getBillTypeByCode(typeCode);
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
		Map<Installment, BigDecimal> instAmountMap = new TreeMap<Installment, BigDecimal>();
		EgDemand egDemand = ptDemandDAO.getNonHistoryCurrDmdForProperty(property);
		String demandReason = "";
		BigDecimal amount = BigDecimal.ZERO;

		List<String> demandReasonExcludeList = Arrays.asList(DEMANDRSN_CODE_PENALTY_FINES,
				PropertyTaxConstants.DEMANDRSN_CODE_ADVANCE);

		for (EgDemandDetails dmdDet : egDemand.getEgDemandDetails()) {

			amount = BigDecimal.ZERO;
			demandReason = dmdDet.getEgDemandReason().getEgDemandReasonMaster().getCode();

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
	public Map<Installment, BigDecimal> prepareRsnWiseCollForProp(Property property) {
		Installment inst = null;
		Map<Installment, BigDecimal> instCollMap = new HashMap<Installment, BigDecimal>();
		EgDemand egDemand = ptDemandDAO.getNonHistoryCurrDmdForProperty(property);
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

	public Map<String, BigDecimal> prepareTaxNameAndALV(Map<String, BigDecimal> taxNameAndALV,
			FloorwiseDemandCalculations floorDmdCalc, Set<String> applicableTaxes) {
		LOGGER.debug("Entered into prepareTaxNameAndALV");
		LOGGER.debug("prepareTaxNameAndALV - Inputs: taxNameAndALV: " + taxNameAndALV);

		for (String taxName : applicableTaxes) {
			putInTaxNameAndALV(taxNameAndALV, taxName, floorDmdCalc.getAlv());
		}

		LOGGER.debug("prepareTaxNameAndALV - afterPrepare taxNameAndALV: " + taxNameAndALV);
		LOGGER.debug("Exiting from prepareTaxNameAndALV");
		return taxNameAndALV;
	}

	/**
	 * @param taxNameAndALV
	 */
	private void putInTaxNameAndALV(Map<String, BigDecimal> taxNameAndALV, String taxName, BigDecimal alv) {
		if (taxNameAndALV.get(taxName) == null) {
			taxNameAndALV.put(taxName, alv);
		} else {
			taxNameAndALV.put(taxName, taxNameAndALV.get(taxName).add(alv));
		}
	}

	public UnitTaxCalculationInfo getUnitTaxCalculationInfoClone(UnitTaxCalculationInfo unit) {
		LOGGER.debug("Entered into getUnitTaxCalculationInfoClone");
		UnitTaxCalculationInfo clone = new APUnitTaxCalculationInfo();
		clone.setFloorNumber(unit.getFloorNumber());
		clone.setUnitOccupation(unit.getUnitOccupation());
		clone.setUnitUsage(unit.getUnitUsage());
		clone.setBaseRate(unit.getBaseRate());
		clone.setMrv(unit.getMrv());
		clone.setBaseRatePerSqMtPerMonth(unit.getBaseRatePerSqMtPerMonth());
		clone.setBuildingValue(unit.getBuildingValue());
		clone.setTotalTaxPayable(unit.getTotalTaxPayable());
		clone.setSiteValue(unit.getSiteValue());
		clone.setOccpancyDate(new Date(unit.getOccpancyDate().getTime()));
		clone.setEffectiveAssessmentDate(unit.getEffectiveAssessmentDate());
		clone.setUnitOccupier(unit.getUnitOccupier());

		clone.setPropertyCreatedDate(unit.getPropertyCreatedDate());

		addMiscellaneousTaxesClone(unit, clone);

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
		TaxCalculationInfo clone = new APTaxCalculationInfo();
		clone.setBlock(taxCalInfo.getBlock());
		clone.setHouseNumber(taxCalInfo.getHouseNumber());
		clone.setPropertyId(taxCalInfo.getPropertyId());
		clone.setPropertyAddress(taxCalInfo.getPropertyAddress());
		clone.setPropertyArea(taxCalInfo.getPropertyArea());
		clone.setPropertyOwnerName(taxCalInfo.getPropertyOwnerName());
		clone.setPropertyType(taxCalInfo.getPropertyType());
		clone.setTaxCalculationInfoXML(taxCalInfo.getTaxCalculationInfoXML());
		clone.setTotalNetARV(taxCalInfo.getTotalNetARV());
		clone.setTotalTaxPayable(taxCalInfo.getTotalTaxPayable());
		clone.setWard(taxCalInfo.getWard());
		clone.setZone(taxCalInfo.getZone());

		addUnitTaxCalculationInfoClone(taxCalInfo, clone);
		return clone;
	}

	/**
	 * Adds the UnitTaxCalculationInfo clones to clone
	 *
	 * @param taxCalInfo
	 * @param clone
	 */
	private void addUnitTaxCalculationInfoClone(TaxCalculationInfo taxCalInfo, TaxCalculationInfo clone) {
		List<UnitTaxCalculationInfo> units = new ArrayList<UnitTaxCalculationInfo>();

		List<UnitTaxCalculationInfo> unitsByDate = new ArrayList<UnitTaxCalculationInfo>();

		for (UnitTaxCalculationInfo unitInfo : taxCalInfo.getUnitTaxCalculationInfos()) {
			UnitTaxCalculationInfo newUnitInfo = getUnitTaxCalculationInfoClone(unitInfo);
			unitsByDate.add(newUnitInfo);
		}
		clone.setUnitTaxCalculationInfo(units);
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

			MiscellaneousTax newMiscTax = new APMiscellaneousTax();
			newMiscTax.setTaxName(miscTax.getTaxName());
			newMiscTax.setTotalActualTax(miscTax.getTotalActualTax());
			newMiscTax.setTotalCalculatedTax(miscTax.getTotalCalculatedTax());
			newMiscTax.setHasChanged(miscTax.getHasChanged());

			for (MiscellaneousTaxDetail miscTaxDetail : miscTax.getTaxDetails()) {
				MiscellaneousTaxDetail newMiscTaxDetail = new APMiscellaneousTaxDetail();
				newMiscTaxDetail.setTaxValue(miscTaxDetail.getTaxValue());
				newMiscTaxDetail.setActualTaxValue(miscTaxDetail.getActualTaxValue());
				newMiscTaxDetail.setCalculatedTaxValue(miscTaxDetail.getCalculatedTaxValue());
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
						"select min(inst.fromDate) from org.egov.commons.Installment inst where inst.module.name = :moduleName")
				.setString("moduleName", PTMODULENAME).uniqueResult();
	}

	/**
	 * Gives the current installment
	 *
	 * @return Installment the current installment for PT module
	 */
	public static Installment getCurrentInstallment() {
	        Query query = HibernateUtil.getCurrentSession().createQuery("from Installment I where I.module.name = :moduleName and (I.fromDate <= :fromYear and I.toDate >=:toYear)");
                query.setString("moduleName", PropertyTaxConstants.PTMODULENAME);
                query.setDate("fromYear", new Date());
                query.setDate("toYear", new Date());
		return (Installment) query.list().get(0);
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
		LOGGER.debug("Entered into getNumberOfDays, fromDate=" + fromDate + ", toDate=" + toDate);
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

		Ptdemand currDemand = ptDemandDAO.getNonHistoryCurrDmdForProperty(property);
		List dmdCollList = propertyDAO.getDmdCollForAllDmdReasons(currDemand);

		for (Object object : dmdCollList) {
			Object[] listObj = (Object[]) object;
			instId = Integer.valueOf(listObj[0].toString());
			installment = (Installment) installmentDao.findById(instId, false);
			if (currDemand.getEgInstallmentMaster().equals(installment)) {
				if (listObj[2] != null && !listObj[2].equals(BigDecimal.ZERO)) {
					currCollection = currCollection.add(new BigDecimal(listObj[2].toString()));
				}

				currentRebate = currentRebate.add(new BigDecimal(listObj[3].toString()));
				currDmd = currDmd.add(new BigDecimal(listObj[1].toString()));
			} else {
				arrDmd = arrDmd.add((BigDecimal) listObj[1]);
				if (listObj[2] != null && !listObj[2].equals(BigDecimal.ZERO)) {
					arrColelection = arrColelection.add(new BigDecimal(listObj[2].toString()));
				}
				arrearRebate = arrearRebate.add(new BigDecimal(listObj[3].toString()));
			}
		}
		demandCollMap.put(CURR_DMD_STR, currDmd);
		demandCollMap.put(ARR_DMD_STR, arrDmd);
		demandCollMap.put(CURR_COLL_STR, currCollection);
		demandCollMap.put(ARR_COLL_STR, arrColelection);
		demandCollMap.put(CURRENT_REBATE_STR, currentRebate);
		demandCollMap.put(ARREAR_REBATE_STR, arrearRebate);
		LOGGER.debug("getDemandAndCollection - demandCollMap = " + demandCollMap);
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

		for (PropertyStatusValues psv : property.getBasicProperty().getPropertyStatusValuesSet()) {
			if (PROPERTY_MODIFY_REASON_MODIFY.equalsIgnoreCase(psv.getPropertyStatus().getStatusCode())) {
				return true;
			}
		}

		return false;
	}

	public void makeTheEgBillAsHistory(BasicProperty basicProperty) {
		EgBill egBill = (EgBill) persistenceService.find(
				"from EgBill where module = ? and consumerId like ? || '%' and is_history = 'N'",
				moduleService.getModuleByName(PropertyTaxConstants.PTMODULENAME), basicProperty.getUpicNo());
		if (egBill != null) {
			egBill.setIs_History("Y");
			egBill.setModifiedDate(new Date());
			//FIXME UTTERWRONG
			persistenceService.setType(EgBill.class);
			persistenceService.update(egBill);
		}
	}

	/**
	 * Called to get concatenated string from Address fields
	 *
	 * @param address
	 * @return String formed by concatenating the address fields
	 */
	public static String buildAddress(Address address) {

		LOGGER.debug("Entered into buildAddress");

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("buildAddress - Address: " + address);
		}

		StringBuffer strAddress = new StringBuffer();

		strAddress.append(
				(isNotBlank(address.getLandmark())) ? address.getLandmark() : " ")
				.append("|");
		strAddress.append(
				(isNotBlank(address.getHouseNoBldgApt())) ? address.getHouseNoBldgApt() : " ")
				.append("|");
	/*	strAddress.append((isNotBlank(address.getDoorNumOld())) ? address.getDoorNumOld() : " ")
				.append("|");*/

		String tmpPin = address.getPinCode();
		strAddress.append((tmpPin != null && !tmpPin.toString().isEmpty()) ? tmpPin : " ").append(
				"|");

		/*strAddress.append((isNotBlank(address.getMobileNo())) ? address.getMobileNo() : " ")
				.append("|");
		strAddress
				.append((isNotBlank(address.getEmailAddress())) ? address.getEmailAddress() : " ")
				.append("|");*/
		

		LOGGER.debug("Exit from buildAddress, Address: " + strAddress.toString());

		return strAddress.toString();
	}

	/**
	 * Gives the Owner Address as string
	 *
	 * @param Set
	 *            <Owner> Set of Property Owners
	 * @return String
	 */
	public static String getOwnerAddress(List<PropertyOwnerInfo> ownerSet) {
		LOGGER.debug("Entered into getOwnerAddress");

		String ownerAddress = "";
		for (PropertyOwnerInfo owner : ownerSet) {
			List<Address> addresses = owner.getOwner().getAddress();
			for (Address address : addresses) {
				ownerAddress = new PTISCacheManager().buildAddressByImplemetation(address);
				break;
			}

		}

		LOGGER.debug("Exiting from getOwnerAddress");
		return ownerAddress;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Date> getLatestCollRcptDateForProp(String consumerCode) {
		LOGGER.debug("Entered into getLatestCollRcptDateForProp, consumerCode=" + consumerCode);

		Map<String, Date> penaltyDates = new HashMap<String, Date>();
		List<Object> rcptHeaderList = HibernateUtil
				.getCurrentSession()
				.createQuery(
						"select substr(rd.description, length(rd.description)-6, length(rd.description)), max(rh.createdDate) "
								+ "from org.egov.erpcollection.models.ReceiptHeader rh "
								+ "left join rh.receiptDetails rd " + "where rh.status.code = 'APPROVED' "
								+ "and rd.description is not null " + "and rd.cramount > 0 "
								+ "and rh.consumerCode like '" + consumerCode + "%' "
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
	public static Property getLatestProperty(BasicProperty basicProperty, Character status) {
		LOGGER.debug("Entered into getLatestProperty, basicProperty=" + basicProperty);

		Map<Date, Property> propertiesByCreatedDate = new TreeMap<Date, Property>();
		Property latestProperty = null;

		for (Property property : basicProperty.getPropertySet()) {

			if (status == null) {
				propertiesByCreatedDate.put(property.getCreatedDate().toDate(), property);
			} else if (property.getStatus().equals(status)) {
				propertiesByCreatedDate.put(property.getCreatedDate().toDate(), property);
			}
		}

		if (!propertiesByCreatedDate.isEmpty()) {
			List<Property> properties = new ArrayList<Property>(propertiesByCreatedDate.values());
			latestProperty = properties.get(properties.size() - 1);
			LOGGER.debug("getLatestProperty, latestProperty=" + latestProperty);
		}

		LOGGER.debug("Exiting from getLatestHistoryProperty");
		return latestProperty;
	}

	public Map<Installment, TaxCalculationInfo> getTaxCalInfoMap(Set<Ptdemand> ptDmdSet) {
		Map<Installment, TaxCalculationInfo> taxCalInfoMap = new TreeMap<Installment, TaxCalculationInfo>();

		for (Ptdemand ptdmd : ptDmdSet) {
			TaxCalculationInfo taxCalcInfo = getTaxCalInfo(ptdmd);
			if (taxCalcInfo != null) {
				taxCalInfoMap.put(ptdmd.getEgInstallmentMaster(), taxCalcInfo);
			}
		}

		return taxCalInfoMap;
	}

	public Map<Date, TaxCalculationInfo> getTaxCalInfoMap(Set<Ptdemand> ptDmdSet, Date occupancyDate) {
		Map<Date, TaxCalculationInfo> taxCalInfoMap = new TreeMap<Date, TaxCalculationInfo>();
		Installment installment = null;

		for (Ptdemand ptdmd : ptDmdSet) {
			TaxCalculationInfo taxCalcInfo = getTaxCalInfo(ptdmd);
			if (taxCalcInfo != null) {
				installment = ptdmd.getEgInstallmentMaster();
				if (between(occupancyDate, ptdmd.getEgInstallmentMaster().getFromDate(), ptdmd.getEgInstallmentMaster()
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
		return getAssignment(userId).getDesignation().getName();
	}

	public WorkflowDetails initWorkflowAction(PropertyImpl propertyModel, WorkflowBean workflowBean,
			Long loggedInUserId, EisCommonService eisCommonService) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entered into initWorkflowAction");
			LOGGER.debug("initWorkflowAction - propertyModel=" + propertyModel + ", workflowBean=" + workflowBean
					+ ", loggedInUserId=" + loggedInUserId);
		}

		String beanActionName[] = null;

		if (isNotNull(workflowBean)) {
			beanActionName = workflowBean.getActionName().split(":");
		}

		WorkflowDetails workflowAction = null;

		if (WFLOW_ACTION_NAME_CREATE.equalsIgnoreCase(beanActionName[0])) {
			workflowAction = new ActionCreate(propertyModel, workflowBean, loggedInUserId);
		} else if (WFLOW_ACTION_NAME_MODIFY.equalsIgnoreCase(beanActionName[0])) {
			workflowAction = new ActionModify(propertyModel, workflowBean, loggedInUserId);
		} else if (WFLOW_ACTION_NAME_BIFURCATE.equalsIgnoreCase(beanActionName[0])) {
			workflowAction = new ActionBifurcate(propertyModel, workflowBean, loggedInUserId);
		} else if (WFLOW_ACTION_NAME_AMALGAMATE.equalsIgnoreCase(beanActionName[0])) {
			workflowAction = new ActionAmalgmate(propertyModel, workflowBean, loggedInUserId);
		} else if (WFLOW_ACTION_NAME_CHANGEADDRESS.equalsIgnoreCase(beanActionName[0])) {
			workflowAction = new ActionChangeAddress(propertyModel, workflowBean, loggedInUserId);
		} else if (WFLOW_ACTION_NAME_DEACTIVATE.equalsIgnoreCase(beanActionName[0])) {
			workflowAction = new ActionDeactivate(propertyModel, workflowBean, loggedInUserId);
		} else if (PropertyTaxConstants.WFLOW_ACTION_NAME_TRANSFER.equalsIgnoreCase(beanActionName[0])) {
			workflowAction = new ActionNameTransfer(propertyModel, workflowBean, loggedInUserId);
		}

		workflowAction.setWorkflowActionStep(this, eisCommonService);

		LOGGER.debug("initWorkflowAction - workflowAction=" + workflowAction);
		LOGGER.debug("Exiting from initWorkflowAction");
		return workflowAction;
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
	private Map<Date, Property> getPropertiesByOccupancy(Property propertyModel, Boolean isConsiderMigrated) {
		LOGGER.debug("Entered into getPropertiesByOccupancy");

		Map<Date, Property> propertyByCreatedDate = new TreeMap<Date, Property>();
		Map<Date, Property> propertyByOccupancyDate = new TreeMap<Date, Property>();

		Property migratedProperty = null;

		for (Property property : propertyModel.getBasicProperty().getPropertySet()) {
			if (isConsiderMigrated
					|| (property.getRemarks() == null || !property.getRemarks().startsWith(
							PropertyTaxConstants.STR_MIGRATED))) {
				propertyByCreatedDate.put(property.getCreatedDate().toDate(), property);

				if (property.getRemarks() != null
						&& property.getRemarks().startsWith(PropertyTaxConstants.STR_MIGRATED)) {
					migratedProperty = property;
				}
			}
		}

		for (Map.Entry<Date, Property> entry : propertyByCreatedDate.entrySet()) {
			propertyByOccupancyDate.put(getPropertyOccupancyDate(entry.getValue()), entry.getValue());
		}

		List<Date> occupancyDates = new LinkedList<Date>(propertyByOccupancyDate.keySet());

		if (isNotNull(migratedProperty)) {
			occupancyDates.remove(getPropertyOccupancyDate(migratedProperty));
		}

		Set<Date> removalDates = getRemovalOccupancyDates(occupancyDates, getPropertyOccupancyDate(propertyModel));

		LOGGER.info("getPropertiesByOccupancy - removalDates=" + removalDates);

		propertyByOccupancyDate.keySet().removeAll(removalDates);

		LOGGER.debug("Exiting from getPropertiesByOccupancy");

		return propertyByOccupancyDate;
	}

	/**
	 * @param occupancyDates
	 */
	private Set<Date> getRemovalOccupancyDates(List<Date> occupancyDates, Date propModelOccupDate) {
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

	public static boolean isNotZero(BigDecimal value) {
		return value == null ? false : value.compareTo(BigDecimal.ZERO) != 0;
	}

	/**
	 * @param Property
	 * @return Date the occupancy date
	 */
	public Date getPropertyOccupancyDate(Property property) {
		return property.getPropertyDetail().getEffective_date() == null ? property.getEffectiveDate() : property
				.getPropertyDetail().getEffective_date();
	}

	/**
	 * Returns true if migrated alv can be used for tax calculation else false
	 *
	 * @param propertyModel
	 * @param occupancyDates
	 * @param installment
	 * @return true for using the migrated alv for tax calculation
	 */
	public boolean isUseMigratedALV(Property propertyModel, List<Date> occupancyDates, Installment installment) {
		Boolean isUseMigrtdALV = false;

		if (isMigrated(propertyModel.getBasicProperty())) {
			/**
			 * if the occupancy date is like 01-04-yyyy format and if its before
			 * the installment.fromDate no need to use migrated alv, for
			 * calculation use the system calculated alv only
			 *
			 * test with 01-04-2005, 01-05-2011, 01-10-2011 & 01-01-2012
			 */

			isUseMigrtdALV = isOccupancyDateLikeInstallmentFromDate(occupancyDates, installment);

		}

		return isUseMigrtdALV ? !getPropertyOccupancyDate(propertyModel).equals(installment.getFromDate())
				: isUseMigrtdALV;
	}

	/**
	 * @param occupancyDates
	 * @param installment
	 * @param isUseMigrtdALV
	 * @param calendar
	 * @return
	 */
	private Boolean isOccupancyDateLikeInstallmentFromDate(List<Date> occupancyDates, Installment installment) {

		for (int i = 0; i < occupancyDates.size(); i++) {
			if (isFirstDayOfMonth(occupancyDates.get(i)) && isMonthApril(occupancyDates.get(i))) {
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

	public Installment getPTInstallmentForDate(Date date) {
		Module module = moduleService.getModuleByName(PropertyTaxConstants.PTMODULENAME);
		return installmentDao.getInsatllmentByModuleForGivenDate(module, date);
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
			if (property.getStatus().equals(PropertyTaxConstants.STATUS_DEMAND_INACTIVE)
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
		LOGGER.debug("Entered into getDemandEffectiveYear, property=" + property);
		String demandEffectiveYear = null;

		demandEffectiveYear = new SimpleDateFormat("yyyy").format(property.getPropertyDetail().getEffective_date());
		LOGGER.debug("getRevisedDemandYear - demandEffectiveYear=" + demandEffectiveYear);

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
	public static Integer getNoticeDaysForInactiveDemand(Property property) throws ParseException {
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
									+ "where n.basicProperty = :basicProp " + "and n.noticeDate is not null "
									+ "and n.noticeDate >= :propCreatedDate")
					.setEntity("basicProp", property.getBasicProperty())
					.setDate("propCreatedDate", property.getCreatedDate().toDate()).list();
			if (result.isEmpty()) {
				LOGGER.debug("Notice generation date is not available for property=" + indexNumber);
			} else {
				noticeDate = new SimpleDateFormat(PropertyTaxConstants.DATE_FORMAT_DDMMYYY).parse(result.get(0)
						.toString());
				if (noticeDate.before(new Date())) {
					days = days - getNumberOfDays(noticeDate, new Date()).intValue();
					days += 1;
				}
			}

		} else {
			LOGGER.debug("getNoticeDaysForInactiveDemand - Notice is not yet generated for property=" + indexNumber);
			LOGGER.debug("getNoticeDaysForInactiveDemand - using defualt notice period days " + days);
		}

		return days;
	}

	public static boolean isNoticeGenerated(Property property) {
		return (property.getExtra_field3() != null && property.getExtra_field3().equalsIgnoreCase(
				PropertyTaxConstants.STR_YES))
				|| (property.getExtra_field4() != null && property.getExtra_field4().equalsIgnoreCase(
						PropertyTaxConstants.STR_YES)) ? true : false;
	}

	public List<String> getAdvanceYearsFromCurrentInstallment() {
		LOGGER.debug("Entered into getAdvanceYearsFromCurrentInstallment");

		List<String> advanceYears = new ArrayList<String>();
		Installment currentInstallment = getCurrentInstallment();
		Integer year = null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentInstallment.getFromDate());
		year = calendar.get(Calendar.YEAR);

		for (int i = 0; i < MAX_ADVANCES_ALLOWED; i++) {

			int fromYear = ++year;
			int toYear = year + 1;

			advanceYears.add(fromYear + "-" + String.valueOf(toYear).substring(2));
		}
		LOGGER.debug("getAdvanceYearsFromCurrentInstallment = " + advanceYears);
		LOGGER.debug("Exiting from getAdvanceYearsFromCurrentInstallment");

		return Collections.emptyList();
	}

	@SuppressWarnings("unchecked")
	public List<Installment> getOrderedInstallmentsFromGivenDate(Date date) {
		return persistenceService.findAllBy("select it from org.egov.commons.Installment it "
				+ "where (it.fromDate>=? or ? between it.fromDate and it.toDate) " + "and it.fromDate<=sysdate "
				+ "and it.module.moduleName=? order by installmentYear", date, date, PTMODULENAME);
	}

	/**
	 * Returns map of EgDemandDetails and Reason master
	 * 
	 * @param egDemandDetails
	 * @return
	 */
	public Map<String, EgDemandDetails> getEgDemandDetailsAndReasonAsMap(Set<EgDemandDetails> egDemandDetails) {

		Map<String, EgDemandDetails> demandDetailAndReason = new HashMap<String, EgDemandDetails>();

		for (EgDemandDetails egDmndDtls : egDemandDetails) {

			EgDemandReasonMaster dmndRsnMstr = egDmndDtls.getEgDemandReason().getEgDemandReasonMaster();

			if (dmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_GENERAL_TAX)) {
				demandDetailAndReason.put(DEMANDRSN_CODE_GENERAL_TAX, egDmndDtls);
			} else if (dmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_LIBRARY_CESS)) {
				demandDetailAndReason.put(DEMANDRSN_CODE_LIBRARY_CESS, egDmndDtls);
			} else if (dmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_EDUCATIONAL_CESS)) {
				demandDetailAndReason.put(DEMANDRSN_CODE_EDUCATIONAL_CESS, egDmndDtls);
			} else if (dmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_UNAUTHORIZED_PENALTY)) {
				demandDetailAndReason.put(DEMANDRSN_CODE_UNAUTHORIZED_PENALTY, egDmndDtls);
			} else if (dmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_PENALTY_FINES)) {
				demandDetailAndReason.put(DEMANDRSN_CODE_PENALTY_FINES, egDmndDtls);
			} else if (dmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY)) {
				demandDetailAndReason.put(DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY, egDmndDtls);
			} else if (dmndRsnMstr.getCode().equalsIgnoreCase(PropertyTaxConstants.DEMANDRSN_CODE_ADVANCE)) {
				demandDetailAndReason.put(PropertyTaxConstants.DEMANDRSN_CODE_ADVANCE, egDmndDtls);
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
								+ "where p.basicProperty.active = true " + "and p.basicProperty.upicNo = :upicNo "
								+ "and (p.remarks is null or p.remarks <> :propertyMigrationRemarks) "
								+ "and pd.effective_date is not null").setString("upicNo", propertyId)
				.setString("propertyMigrationRemarks", PropertyTaxConstants.STR_MIGRATED_REMARKS).list();

		Date earliestModificationDate = null;

		if (result.isEmpty()) {
			return null;
		} else {
			if (result.get(0) == null) {
				return null;
			}
		}

		try {
			earliestModificationDate = PropertyTaxConstants.DATEFORMATTER_DDMMYYYY.parse((String) result.get(0));
		} catch (ParseException e) {
			LOGGER.error("Error while parsing effective date", e);
			throw new EGOVRuntimeException("Error while parsing effective date", e);
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
			waterTaxEffectiveDate = PropertyTaxConstants.DATEFORMATTER_DDMMYYYY.parse(PENALTY_WATERTAX_EFFECTIVE_DATE);
		} catch (ParseException pe) {
			throw new EGOVRuntimeException("Error while parsing Water Tax Effective Date for Penalty Calculation", pe);
		}

		LOG.debug("getWaterTaxEffectiveDateForPenalty - waterTaxEffectiveDate = {} ", waterTaxEffectiveDate);
		return waterTaxEffectiveDate;
	}

	/**
	 * Gives the properties and occupancy date map
	 * 
	 * @return properties by occupancy dates
	 */
	public Map<Date, Property> getPropertiesByOccupancy(BasicProperty basicProperty) {
		LOGGER.debug("Entered into getPropertiesByOccupancy");

		Map<Date, Property> propertyByCreatedDate = new TreeMap<Date, Property>();
		Map<Date, Property> propertyByOccupancyDate = new TreeMap<Date, Property>();

		for (Property property : basicProperty.getPropertySet()) {
			if (inConsider(property)) {
				propertyByCreatedDate.put(property.getCreatedDate().toDate(), property);
			}
		}

		for (Map.Entry<Date, Property> entry : propertyByCreatedDate.entrySet()) {
			propertyByOccupancyDate.put(getPropertyOccupancyDate(entry.getValue()), entry.getValue());
		}

		LOGGER.debug("Exiting from getPropertiesByOccupancy");

		return propertyByOccupancyDate;
	}

	private boolean inConsider(Property property) {
		return (property.getRemarks() == null || !property.getRemarks().startsWith(STR_MIGRATED));
	}

	@SuppressWarnings("unchecked")
	public Map<String, Map<Installment, BigDecimal>> prepareReasonWiseDenandAndCollection(Property property,
			Installment currentInstallment) {
		LOGGER.debug("Entered into prepareReasonWiseDenandAndCollection, property=" + property);

		Map<Installment, BigDecimal> installmentWiseDemand = new TreeMap<Installment, BigDecimal>();
		Map<Installment, BigDecimal> installmentWiseCollection = new TreeMap<Installment, BigDecimal>();
		Map<String, Map<Installment, BigDecimal>> demandAndCollection = new HashMap<String, Map<Installment, BigDecimal>>();

		String demandReason = "";
		Installment installment = null;

		List<String> demandReasonExcludeList = Arrays.asList(DEMANDRSN_CODE_PENALTY_FINES,
				PropertyTaxConstants.DEMANDRSN_CODE_ADVANCE);

		String query = "select ptd from Ptdemand ptd " + "inner join fetch ptd.egDemandDetails dd "
				+ "inner join fetch dd.egDemandReason dr " + "inner join fetch dr.egDemandReasonMaster drm "
				+ "inner join fetch ptd.egptProperty p " + "inner join fetch p.basicProperty bp "
				+ "where bp.active = true " + "and (p.status = 'A' or p.status = 'I') " + "and p = :property "
				+ "and ptd.egInstallmentMaster = :installment";

		Ptdemand ptDemand = (Ptdemand) HibernateUtil.getCurrentSession().createQuery(query)
				.setEntity("property", property).setEntity("installment", currentInstallment).list().get(0);

		for (EgDemandDetails dmdDet : ptDemand.getEgDemandDetails()) {

			demandReason = dmdDet.getEgDemandReason().getEgDemandReasonMaster().getCode();

			if (!demandReasonExcludeList.contains(demandReason)) {
				installment = dmdDet.getEgDemandReason().getEgInstallmentMaster();

				if (installmentWiseDemand.get(installment) == null) {
					installmentWiseDemand.put(installment, dmdDet.getAmount());
				} else {
					installmentWiseDemand.put(installment,
							installmentWiseDemand.get(installment).add(dmdDet.getAmount()));
				}

				if (installmentWiseCollection.get(installment) == null) {
					installmentWiseCollection.put(installment, dmdDet.getAmtCollected());
				} else {
					installmentWiseCollection.put(installment,
							installmentWiseCollection.get(installment).add(dmdDet.getAmtCollected()));
				}
			}
		}

		demandAndCollection.put("DEMAND", installmentWiseDemand);
		demandAndCollection.put("COLLECTION", installmentWiseCollection);

		LOGGER.debug("prepareReasonWiseDenandAndCollection - demandAndCollection=" + demandAndCollection);
		LOGGER.debug("Exiting from prepareReasonWiseDenandAndCollection");
		return demandAndCollection;
	}

	@SuppressWarnings("unchecked")
	public Map<Date, Property> getPropertiesForPenlatyCalculation(BasicProperty basicProperty) {

		String query = "select p from PropertyImpl p " + "inner join fetch p.basicProperty bp "
				+ "where bp.upicNo = ? and bp.active = true " + "and (p.remarks = null or p.remarks <> ?) "
				+ "order by p.createdDate";

		List<Property> allProperties = HibernateUtil.getCurrentSession().createQuery(query)
				.setString(0, basicProperty.getUpicNo()).setString(1, PropertyTaxConstants.STR_MIGRATED_REMARKS).list();

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

			prevMutationCode = prevProperty != null ? prevProperty.getPropertyDetail().getPropertyMutationMaster()
					.getCode() : null;
			mutationCode = property.getPropertyDetail().getPropertyMutationMaster().getCode();
			nextMutationCode = i != (noOfProperties - 1) ? allProperties.get(i + 1).getPropertyDetail()
					.getPropertyMutationMaster().getCode() : null;

			if (!mutationCode.equalsIgnoreCase(PropertyTaxConstants.PROPERTY_MODIFY_REASON_OBJ)) {
				if (mutationsCodes.contains(mutationCode)) {
					propertyAndEffectiveDate.put(getPropertyOccupancyDate(property), property);
				} else {

					if (isFirstDataEntry(prevMutationCode, mutationCode, prevProperty)) {
						firstDataEntryEffectiveDate = getPropertyOccupancyDate(property);
					}

					if (mutationCode.equalsIgnoreCase(PROPERTY_MODIFY_REASON_DATA_ENTRY)
							&& (nextMutationCode == null || areNoticesGenerated(property))) {
						propertyAndEffectiveDate.put(firstDataEntryEffectiveDate, property);
					}
				}
			}
		}

		Map<Date, Property> propertyByOccupancyDate = new TreeMap<Date, Property>();

		for (Map.Entry<Date, Property> entry : propertyAndEffectiveDate.entrySet()) {

			if (entry.getKey() == null) {
				propertyByOccupancyDate.put(getPropertyOccupancyDate(entry.getValue()), entry.getValue());
			} else {
				propertyByOccupancyDate.put(entry.getKey(), entry.getValue());
			}
		}

		return propertyByOccupancyDate;
	}

	private boolean areNoticesGenerated(Property property) {
		boolean isNotice134Or127Generated = property.getExtra_field3() != null
				&& property.getExtra_field3().equalsIgnoreCase("Yes") ? true : false;
		boolean isNoticePVRGenerated = property.getExtra_field4() != null
				&& property.getExtra_field4().equalsIgnoreCase("Yes") ? true : false;
		return isNotice134Or127Generated && isNoticePVRGenerated ? true : false;
	}

	private boolean isFirstDataEntry(String prevPropMutationCode, String mutationCode, Property prevProperty) {

		List<String> mutationCodes = Arrays.asList(PropertyTaxConstants.MUTATION_CODE_NEW,
				PropertyTaxConstants.PROPERTY_MODIFY_REASON_OBJ);

		if (prevPropMutationCode != null && mutationCodes.contains(prevPropMutationCode)
				&& mutationCode.equalsIgnoreCase(PROPERTY_MODIFY_REASON_DATA_ENTRY)) {
			return true;
		}

		return (prevProperty == null || areNoticesGenerated(prevProperty))
				&& mutationCode.equalsIgnoreCase(PROPERTY_MODIFY_REASON_DATA_ENTRY) ? true : false;
	}

	private boolean isDataEntryApproved(String prevPropMutationCode) {
		List<String> mutationsCodes = Arrays.asList("NEW", "MODIFY");
		return prevPropMutationCode == null || mutationsCodes.contains(prevPropMutationCode) ? true : false;
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
