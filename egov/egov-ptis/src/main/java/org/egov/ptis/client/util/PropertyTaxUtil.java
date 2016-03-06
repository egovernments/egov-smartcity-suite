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
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org
 ******************************************************************************/
package org.egov.ptis.client.util;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.egov.ptis.constants.PropertyTaxConstants.AMP_ACTUAL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.AMP_ENCODED_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.APPCONFIG_ISCORPORATION;
import static org.egov.ptis.constants.PropertyTaxConstants.APPCONFIG_ISSEASHORE_ULB;
import static org.egov.ptis.constants.PropertyTaxConstants.APPCONFIG_IS_PRIMARY_SERVICECHARGES_APPLICABLE;
import static org.egov.ptis.constants.PropertyTaxConstants.ARREARS_DMD;
import static org.egov.ptis.constants.PropertyTaxConstants.ARREAR_REBATE_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURRENT_DMD;
import static org.egov.ptis.constants.PropertyTaxConstants.CURRENT_REBATE_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_EDUCATIONAL_CESS;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_GENERAL_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_LIBRARY_CESS;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_PENALTY_FINES;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_REBATE;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_UNAUTHORIZED_PENALTY;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMAND_REASON_ORDER_MAP;
import static org.egov.ptis.constants.PropertyTaxConstants.MAX_ADVANCES_ALLOWED;
import static org.egov.ptis.constants.PropertyTaxConstants.PENALTY_WATERTAX_EFFECTIVE_DATE;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_MODIFY_REASON_ADD_OR_ALTER;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_MODIFY_REASON_DATA_ENTRY;
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_DEMANDREASONBY_CODE_AND_INSTALLMENTID;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_DEMANDREASONDETAILBY_DEMANDREASONID;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_DEMANDREASONDETAILS_BY_DEMANDREASON_AND_INSTALLMENT;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_DEPARTMENTS_BY_DEPTCODE;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_INSTALLMENTLISTBY_MODULE_AND_STARTYEAR;
import static org.egov.ptis.constants.PropertyTaxConstants.SESSION_VAR_LOGIN_USER_NAME;
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

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.egov.commons.CFinancialYear;
import org.egov.commons.Installment;
import org.egov.commons.dao.FinancialYearDAO;
import org.egov.commons.dao.InstallmentDao;
import org.egov.demand.dao.DemandGenericHibDao;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.model.DepreciationMaster;
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
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.persistence.entity.Address;
import org.egov.infra.reporting.util.ReportUtil;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.web.utils.WebUtils;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.infstr.utils.MoneyUtils;
import org.egov.model.instrument.InstrumentType;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.ptis.client.handler.TaxCalculationInfoXmlHandler;
import org.egov.ptis.client.model.PenaltyAndRebate;
import org.egov.ptis.client.model.PropertyArrearBean;
import org.egov.ptis.client.model.calculator.APMiscellaneousTax;
import org.egov.ptis.client.model.calculator.APMiscellaneousTaxDetail;
import org.egov.ptis.client.model.calculator.APTaxCalculationInfo;
import org.egov.ptis.client.model.calculator.APUnitTaxCalculationInfo;
import org.egov.ptis.client.model.calculator.DemandNoticeDetailsInfo;
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
import org.egov.ptis.domain.entity.property.Category;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyArrear;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyMaterlizeView;
import org.egov.ptis.domain.entity.property.PropertyMutation;
import org.egov.ptis.domain.entity.property.PropertyOwnerInfo;
import org.egov.ptis.domain.entity.property.PropertyStatusValues;
import org.egov.ptis.domain.entity.property.VacancyRemission;
import org.egov.ptis.domain.entity.property.VacancyRemissionDetails;
import org.egov.ptis.domain.entity.property.WorkflowBean;
import org.egov.ptis.domain.model.calculator.MiscellaneousTax;
import org.egov.ptis.domain.model.calculator.MiscellaneousTaxDetail;
import org.egov.ptis.domain.model.calculator.TaxCalculationInfo;
import org.egov.ptis.domain.model.calculator.UnitTaxCalculationInfo;
import org.egov.ptis.wtms.ConsumerConsumption;
import org.egov.ptis.wtms.PropertyWiseConsumptions;
import org.egov.ptis.wtms.WaterChargesIntegrationService;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.joda.time.DateTime;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @author malathi
 */
public class PropertyTaxUtil {
    private static final Logger LOGGER = Logger.getLogger(PropertyTaxUtil.class);

    @Autowired
    @Qualifier("persistenceService")
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
    private AppConfigValueService appConfigValuesService;
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
    @Autowired
    private PositionMasterService positionMasterService;
    @Autowired
    private FinancialYearDAO financialYearDAO;
    @Autowired
    @Qualifier("waterChargesIntegrationServiceImpl")
    private WaterChargesIntegrationService waterChargesIntegrationService;
    @Autowired
    private BoundaryService boundaryService;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public void setPersistenceService(final PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    /**
     * This method retrieves the <code>CFinancialYear</code> for the given date.
     *
     * @param date
     *            an instance of <code>Date</code> for which the financial year
     *            is to be retrieved.
     * @return an instance of <code></code> representing the financial year for
     *         the given date
     */
    public CFinancialYear getFinancialYearforDate(final Date date) {
        return (CFinancialYear) persistenceService
                .getSession()
                .createQuery(
                        "from CFinancialYear cfinancialyear where ? between "
                                + "cfinancialyear.startingDate and cfinancialyear.endingDate").setDate(0, date).list()
                .get(0);
    }

    public Category getCategoryForBoundary(final Boundary boundary) {
        return boundaryCategoryDAO.getCategoryForBoundary(boundary);
    }

    public List<Installment> getInstallmentListByStartDate(final Date startDate) {
        return persistenceService.findAllByNamedQuery(QUERY_INSTALLMENTLISTBY_MODULE_AND_STARTYEAR, startDate,
                startDate, PTMODULENAME);
    }

    public EgDemandReason getDemandReasonByCodeAndInstallment(final String demandReasonCode,
            final Installment installment) {
        return (EgDemandReason) persistenceService.findByNamedQuery(QUERY_DEMANDREASONBY_CODE_AND_INSTALLMENTID,
                demandReasonCode, installment.getId());
    }

    public EgDemandReasonDetails getDemandReasonDetailsByDemandReasonId(final EgDemandReason demandReason,
            final BigDecimal grossAnnualRentAfterDeduction) {
        return (EgDemandReasonDetails) persistenceService.findByNamedQuery(QUERY_DEMANDREASONDETAILBY_DEMANDREASONID,
                demandReason.getId(), grossAnnualRentAfterDeduction);
    }

    public List<EgDemandReasonDetails> getDemandReasonDetails(final String demandReasonCode,
            final BigDecimal grossAnnualRentAfterDeduction, final Installment installment) {
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
    public String getAppConfigValue(final String key, final String moduleName) {
        String value = "";
        if (key != null && moduleName != null) {
            final AppConfigValues appConfigValues = appConfigValuesService.getAppConfigValueByDate(moduleName, key,
                    new Date());
            if (appConfigValues != null)
                value = appConfigValues.getValue();
        }
        return value;
    }

    /**
     * This method returns the currently active config value for the given
     * module name and key
     *
     * @param moduleName
     *            a <code>String<code> representing the module name
     * @param key
     *            a <code>String</code> representing the key
     * @param defaultValue
     *            Default value to be returned in case the key is not defined
     * @return <code>String</code> representing the configuration value
     */
    public String getAppConfigValue(final String moduleName, final String key, final String defaultValue) {
        final AppConfigValues appConfigValues = appConfigValuesService.getAppConfigValueByDate(moduleName, key,
                new Date());
        return appConfigValues == null ? defaultValue : appConfigValues.getValue();
    }

    public TaxCalculationInfo getTaxCalInfo(final Ptdemand demand) {
        TaxCalculationInfo taxCalInfo = null;
        final TaxCalculationInfoXmlHandler handler = new TaxCalculationInfoXmlHandler();
        final PTDemandCalculations ptDmdCalc = demand.getDmdCalculations();
        if (ptDmdCalc.getTaxInfo() != null) {
            final String xmlString = new String(ptDmdCalc.getTaxInfo());
            LOGGER.debug("TaxCalculationInfo XML : " + xmlString);
            taxCalInfo = (TaxCalculationInfo) handler.toObject(xmlString);
            if (taxCalInfo.getPropertyId() == null)
                taxCalInfo.setPropertyId(demand.getEgptProperty().getBasicProperty().getUpicNo());
        }
        return taxCalInfo;
    }

    public static BigDecimal roundOffTax(final BigDecimal tax) {
        return MoneyUtils.roundOff(tax);
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

    private Map<String, BigDecimal> populateReasonsSum(final Object[] data, final Map<String, BigDecimal> taxSum) {
        BigDecimal tmpVal;
        if (data[0].toString().equals(DEMANDRSN_CODE_GENERAL_TAX)) {
            tmpVal = taxSum.get(DEMANDRSN_CODE_GENERAL_TAX);
            // considering rebate as collection and substracting it.
            taxSum.put(DEMANDRSN_CODE_GENERAL_TAX, tmpVal.add(((BigDecimal) data[2]).subtract((BigDecimal) data[3])));
        } else if (data[0].toString().equals(DEMANDRSN_CODE_LIBRARY_CESS)) {
            tmpVal = taxSum.get(DEMANDRSN_CODE_LIBRARY_CESS);
            taxSum.put(DEMANDRSN_CODE_LIBRARY_CESS, tmpVal.add(((BigDecimal) data[2]).subtract((BigDecimal) data[3])));
        } else if (data[0].toString().equals(DEMANDRSN_CODE_EDUCATIONAL_CESS)) {
            tmpVal = taxSum.get(DEMANDRSN_CODE_EDUCATIONAL_CESS);
            taxSum.put(DEMANDRSN_CODE_EDUCATIONAL_CESS,
                    tmpVal.add(((BigDecimal) data[2]).subtract((BigDecimal) data[3])));
        } else if (data[0].toString().equals(DEMANDRSN_CODE_UNAUTHORIZED_PENALTY)) {
            tmpVal = taxSum.get(DEMANDRSN_CODE_UNAUTHORIZED_PENALTY);
            taxSum.put(DEMANDRSN_CODE_UNAUTHORIZED_PENALTY,
                    tmpVal.add(((BigDecimal) data[2]).subtract((BigDecimal) data[3])));
        } else if (data[0].toString().equals(DEMANDRSN_CODE_PENALTY_FINES)) {
            tmpVal = taxSum.get(DEMANDRSN_CODE_PENALTY_FINES);
            taxSum.put(DEMANDRSN_CODE_PENALTY_FINES, tmpVal.add(((BigDecimal) data[2]).subtract((BigDecimal) data[3])));
        } else if (data[0].toString().equals(DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY)) {
            tmpVal = taxSum.get(DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY);
            taxSum.put(DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY,
                    tmpVal.add(((BigDecimal) data[2]).subtract((BigDecimal) data[3])));
        }

        return taxSum;
    }

    /**
     * Called to get reason wise demand dues for arrears and current
     *
     * @param propertyId
     * @return Map of String and Map for reason wise demand dues
     */

    public Map<String, Map<String, BigDecimal>> getDemandDues(final String propertyId) {
        final Map<String, Map<String, BigDecimal>> demandDues = new HashMap<String, Map<String, BigDecimal>>();
        List list = new ArrayList();
        final BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(propertyId);
        final EgDemand egDemand = ptDemandDAO.getNonHistoryCurrDmdForProperty(basicProperty.getProperty());
        final Module module = moduleService.getModuleByName(PropertyTaxConstants.PTMODULENAME);
        final Installment currentInstall = installmentDao.getInsatllmentByModuleForGivenDate(module, new Date());

        list = demandGenericDAO.getReasonWiseDCB(egDemand, module);

        Map<String, BigDecimal> arrTaxSum = new HashMap<String, BigDecimal>();
        Map<String, BigDecimal> currTaxSum = new HashMap<String, BigDecimal>();

        arrTaxSum = initReasonsMap(arrTaxSum);
        currTaxSum = initReasonsMap(currTaxSum);

        for (final Object record : list) {
            final Object[] data = (Object[]) record;
            if (data[1].toString().compareTo(currentInstall.toString()) < 0)
                arrTaxSum = populateReasonsSum(data, arrTaxSum);
            else
                currTaxSum = populateReasonsSum(data, currTaxSum);
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
    private Map<String, BigDecimal> initReasonsMap(final Map<String, BigDecimal> taxSum) {

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
    public User getLoggedInUser(final Map<String, Object> sessionMap) {
        return userService.getUserByUsername((String) sessionMap.get(SESSION_VAR_LOGIN_USER_NAME));
    }

    /**
     * @param user
     *            the user whose department is to be returned
     * @return department of the given user
     */
    private Department getDepartmentOfUser(final User user) {
        return getAssignment(user.getId()).getDepartment();
    }

    /**
     * @param Integer
     *            the userId
     * @return Assignment for current date and for
     *         <code> PersonalInformation </code>
     */
    private Assignment getAssignment(final Long userId) {
        final Employee empForUserId = employeeService.getEmployeeById(userId);
        final Assignment assignmentByEmpAndDate = assignmentService.getPrimaryAssignmentForEmployeeByToDate(
                empForUserId.getId(), new Date());
        return assignmentByEmpAndDate;
    }

    public HashMap<String, Integer> generateOrderForDemandDetails(final Set<EgDemandDetails> demandDetails,
            final PropertyTaxBillable billable) {

        final Map<Date, String> instReasonMap = new TreeMap<Date, String>();
        final HashMap<String, Integer> orderMap = new HashMap<String, Integer>();
        BigDecimal balance = BigDecimal.ZERO;
        Date key = null;
        String reasonMasterCode = null;

        for (final EgDemandDetails demandDetail : demandDetails) {
            balance = BigDecimal.ZERO;
            balance = demandDetail.getAmount().subtract(demandDetail.getAmtCollected());

            if (balance.compareTo(BigDecimal.ZERO) == 1) {
                final EgDemandReason reason = demandDetail.getEgDemandReason();
                final Installment installment = reason.getEgInstallmentMaster();
                final DateTime dateTime = new DateTime(installment.getInstallmentYear());
                reasonMasterCode = reason.getEgDemandReasonMaster().getCode();

                if (reasonMasterCode.equals(DEMANDRSN_CODE_GENERAL_TAX)) {

                    key = getOrder(installment.getInstallmentYear(), DEMAND_REASON_ORDER_MAP.get(DEMANDRSN_CODE_REBATE));
                    instReasonMap.put(key, dateTime.getMonthOfYear() + "/" + dateTime.getYear() + "-"
                            + DEMANDRSN_CODE_REBATE);

                    key = getOrder(installment.getInstallmentYear(), DEMAND_REASON_ORDER_MAP.get(reasonMasterCode)
                            .intValue());
                    instReasonMap.put(key, dateTime.getMonthOfYear() + "/" + dateTime.getYear() + "-"
                            + reasonMasterCode);

                } else {
                    LOGGER.info(reasonMasterCode);
                    key = getOrder(installment.getInstallmentYear(), DEMAND_REASON_ORDER_MAP.get(reasonMasterCode)
                            .intValue());
                    instReasonMap.put(key, dateTime.getMonthOfYear() + "/" + dateTime.getYear() + "-"
                            + reasonMasterCode);
                }
            }
        }

        DateTime dateTime = null;
        BigDecimal penaltyAmount = BigDecimal.ZERO;

        for (final Map.Entry<Installment, PenaltyAndRebate> mapEntry : billable.getInstTaxBean().entrySet()) {

            penaltyAmount = mapEntry.getValue().getPenalty();
            final boolean thereIsPenalty = penaltyAmount != null && penaltyAmount.compareTo(BigDecimal.ZERO) > 0;

            if (thereIsPenalty) {

                dateTime = new DateTime(mapEntry.getKey().getInstallmentYear());

                key = getOrder(mapEntry.getKey().getInstallmentYear(),
                        DEMAND_REASON_ORDER_MAP.get(DEMANDRSN_CODE_PENALTY_FINES));
                instReasonMap.put(key, dateTime.getMonthOfYear() + "/" + dateTime.getYear() + "-"
                        + DEMANDRSN_CODE_PENALTY_FINES);
            }
        }

        int order = 1;
        final Map<String, Map<String, String>> installmentAndReason = new LinkedHashMap<String, Map<String, String>>();

        for (final Map.Entry<Date, String> entry : instReasonMap.entrySet()) {
            final String[] split = entry.getValue().split("-");
            if (installmentAndReason.get(split[0]) == null) {
                final Map<String, String> reason = new HashMap<String, String>();
                reason.put(split[1], entry.getValue());
                installmentAndReason.put(split[0], reason);
            } else
                installmentAndReason.get(split[0]).put(split[1], entry.getValue());
        }

        for (final String installmentYear : installmentAndReason.keySet()) {
            if (installmentAndReason.get(installmentYear).get(DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY) != null)
                orderMap.put(installmentAndReason.get(installmentYear).get(DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY), order++);

            if (installmentAndReason.get(installmentYear).get(DEMANDRSN_CODE_PENALTY_FINES) != null)
                orderMap.put(installmentAndReason.get(installmentYear).get(DEMANDRSN_CODE_PENALTY_FINES), order++);
        }

        for (final String installmentYear : installmentAndReason.keySet())
            for (final String reasonCode : PropertyTaxConstants.ORDERED_DEMAND_RSNS_LIST) {

                if (reasonCode.equalsIgnoreCase(DEMANDRSN_CODE_PENALTY_FINES)
                        || reasonCode.equalsIgnoreCase(DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY))
                    continue;

                if (installmentAndReason.get(installmentYear).get(reasonCode) != null)
                    orderMap.put(installmentAndReason.get(installmentYear).get(reasonCode), order++);
            }

        return orderMap;

    }

    /**
     * @param sessionMap
     *            map of session variables
     * @return departments of currently logged in user
     */
    public List<Department> getDepartmentsForLoggedInUser(final User user) {
        final Department dept = getDepartmentOfUser(user);
        final List<Department> departments = persistenceService.findAllByNamedQuery(QUERY_DEPARTMENTS_BY_DEPTCODE,
                dept.getCode());
        return departments;
    }

    public Designation getDesignationForUser(final Long userId) {
        Position position = null;
        Designation designation = null;
        if (userId != null && userId.intValue() != 0) {
            position = positionMasterService.getPositionByUserId(userId);
            if (position != null)
                designation = position.getDeptDesig().getDesignation();
        }
        return designation;
    }

    public EgBillType getBillTypeByCode(final String typeCode) {
        final EgBillType billType = egBillDAO.getBillTypeByCode(typeCode);
        return billType;
    }

    public Date getOrder(final Date date, final int reasonOrder) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, reasonOrder);
        return calendar.getTime();
    }

    /**
     * Prepares a map of installment and respective demand for each installment
     *
     * @param property
     * @return Map of installment and respective reason wise demand for each
     *         installment
     */
    public Map<Installment, BigDecimal> prepareRsnWiseDemandForProp(final Property property) {
        Installment inst = null;
        final Map<Installment, BigDecimal> instAmountMap = new TreeMap<Installment, BigDecimal>();
        final EgDemand egDemand = ptDemandDAO.getNonHistoryCurrDmdForProperty(property);
        String demandReason = "";
        BigDecimal amount = BigDecimal.ZERO;

        final List<String> demandReasonExcludeList = Arrays.asList(DEMANDRSN_CODE_PENALTY_FINES,
                PropertyTaxConstants.DEMANDRSN_CODE_ADVANCE);

        for (final EgDemandDetails dmdDet : egDemand.getEgDemandDetails()) {

            amount = BigDecimal.ZERO;
            demandReason = dmdDet.getEgDemandReason().getEgDemandReasonMaster().getCode();

            if (!demandReasonExcludeList.contains(demandReason)) {
                inst = dmdDet.getEgDemandReason().getEgInstallmentMaster();
                if (instAmountMap.get(inst) == null)
                    instAmountMap.put(inst, dmdDet.getAmount());
                else {
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
    public Map<Installment, BigDecimal> prepareRsnWiseCollForProp(final Property property) {
        Installment inst = null;
        final Map<Installment, BigDecimal> instCollMap = new HashMap<Installment, BigDecimal>();
        final EgDemand egDemand = ptDemandDAO.getNonHistoryCurrDmdForProperty(property);
        String demandReason = "";
        BigDecimal amount = BigDecimal.ZERO;
        BigDecimal collection = BigDecimal.ZERO;
        for (final EgDemandDetails dmdDet : egDemand.getEgDemandDetails()) {
            amount = BigDecimal.ZERO;
            collection = dmdDet.getAmtCollected();
            demandReason = dmdDet.getEgDemandReason().getEgDemandReasonMaster().getCode();
            if (!demandReason.equals(DEMANDRSN_CODE_PENALTY_FINES)) {
                inst = dmdDet.getEgDemandReason().getEgInstallmentMaster();
                if (instCollMap.get(inst) == null)
                    instCollMap.put(inst, collection);
                else {
                    amount = instCollMap.get(inst);
                    amount = amount.add(collection);
                    instCollMap.put(inst, amount);
                }
            }
        }
        return instCollMap;
    }

    public Map<String, BigDecimal> prepareTaxNameAndALV(final Map<String, BigDecimal> taxNameAndALV,
            final FloorwiseDemandCalculations floorDmdCalc, final Set<String> applicableTaxes) {
        LOGGER.debug("Entered into prepareTaxNameAndALV");
        LOGGER.debug("prepareTaxNameAndALV - Inputs: taxNameAndALV: " + taxNameAndALV);

        for (final String taxName : applicableTaxes)
            putInTaxNameAndALV(taxNameAndALV, taxName, floorDmdCalc.getAlv());

        LOGGER.debug("prepareTaxNameAndALV - afterPrepare taxNameAndALV: " + taxNameAndALV);
        LOGGER.debug("Exiting from prepareTaxNameAndALV");
        return taxNameAndALV;
    }

    /**
     * @param taxNameAndALV
     */
    private void putInTaxNameAndALV(final Map<String, BigDecimal> taxNameAndALV, final String taxName,
            final BigDecimal alv) {
        if (taxNameAndALV.get(taxName) == null)
            taxNameAndALV.put(taxName, alv);
        else
            taxNameAndALV.put(taxName, taxNameAndALV.get(taxName).add(alv));
    }

    public UnitTaxCalculationInfo getUnitTaxCalculationInfoClone(final UnitTaxCalculationInfo unit) {
        LOGGER.debug("Entered into getUnitTaxCalculationInfoClone");
        final UnitTaxCalculationInfo clone = new APUnitTaxCalculationInfo();
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

    public TaxCalculationInfo getTaxCalculationInfoClone(final TaxCalculationInfo taxCalInfo) {
        final TaxCalculationInfo clone = new APTaxCalculationInfo();
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
    private void addUnitTaxCalculationInfoClone(final TaxCalculationInfo taxCalInfo, final TaxCalculationInfo clone) {
        final List<UnitTaxCalculationInfo> units = new ArrayList<UnitTaxCalculationInfo>();

        final List<UnitTaxCalculationInfo> unitsByDate = new ArrayList<UnitTaxCalculationInfo>();

        for (final UnitTaxCalculationInfo unitInfo : taxCalInfo.getUnitTaxCalculationInfos()) {
            final UnitTaxCalculationInfo newUnitInfo = getUnitTaxCalculationInfoClone(unitInfo);
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
    public void addMiscellaneousTaxesClone(final UnitTaxCalculationInfo unit, final UnitTaxCalculationInfo clone) {
        LOGGER.debug("Entered into addMiscellaneousTaxesClone");
        for (final MiscellaneousTax miscTax : unit.getMiscellaneousTaxes()) {

            final MiscellaneousTax newMiscTax = new APMiscellaneousTax();
            newMiscTax.setTaxName(miscTax.getTaxName());
            newMiscTax.setTotalActualTax(miscTax.getTotalActualTax());
            newMiscTax.setTotalCalculatedTax(miscTax.getTotalCalculatedTax());
            newMiscTax.setHasChanged(miscTax.getHasChanged());

            for (final MiscellaneousTaxDetail miscTaxDetail : miscTax.getTaxDetails()) {
                final MiscellaneousTaxDetail newMiscTaxDetail = new APMiscellaneousTaxDetail();
                newMiscTaxDetail.setTaxValue(miscTaxDetail.getTaxValue());
                newMiscTaxDetail.setActualTaxValue(miscTaxDetail.getActualTaxValue());
                newMiscTaxDetail.setCalculatedTaxValue(miscTaxDetail.getCalculatedTaxValue());
                // newMiscTaxDetail.setHasChanged(miscTaxDetail.getHasChanged());
                final Calendar calendar = Calendar.getInstance();
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
        final Query query = HibernateUtil
                .getCurrentSession()
                .createQuery(
                        "from Installment I where I.module.name = :moduleName and (I.fromDate <= :fromYear and I.toDate >=:toYear)");
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
    public static Long getNumberOfDays(final Date fromDate, final Date toDate) {
        LOGGER.debug("Entered into getNumberOfDays, fromDate=" + fromDate + ", toDate=" + toDate);
        final Calendar fromDateCalendar = Calendar.getInstance();
        final Calendar toDateCalendar = Calendar.getInstance();
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
    public Boolean between(final Date date, final Date fromDate, final Date toDate) {
        return (date.after(fromDate) || date.equals(fromDate)) && date.before(toDate) || date.equals(toDate);
    }

    public Boolean betweenOrBefore(final Date date, final Date fromDate, final Date toDate) {
        final Boolean result = between(date, fromDate, toDate) || date.before(fromDate);
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

    public List<PropertyArrearBean> getPropertyArrears(final List<PropertyArrear> arrears) {
        final List<PropertyArrearBean> propArrears = new ArrayList<PropertyArrearBean>();
        PropertyArrearBean propArrBean = null;
        for (final PropertyArrear pa : arrears) {
            propArrBean = new PropertyArrearBean();
            final String key = pa.getFromDate().toString().concat("-").concat(pa.getToDate().toString());
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
    public String antisamyHackReplace(final String str) {
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
    public Map<String, BigDecimal> getDemandAndCollection(final Property property) {
        LOGGER.debug("Entered into getDemandAndCollection");

        final Map<String, BigDecimal> demandCollMap = new HashMap<String, BigDecimal>();
        Installment installment = null;
        Integer instId = null;
        BigDecimal currDmd = BigDecimal.ZERO;
        BigDecimal arrDmd = BigDecimal.ZERO;
        BigDecimal currCollection = BigDecimal.ZERO;
        BigDecimal arrColelection = BigDecimal.ZERO;
        BigDecimal currentRebate = BigDecimal.ZERO;
        BigDecimal arrearRebate = BigDecimal.ZERO;

        final Ptdemand currDemand = ptDemandDAO.getNonHistoryCurrDmdForProperty(property);
        final List dmdCollList = propertyDAO.getDmdCollForAllDmdReasons(currDemand);

        for (final Object object : dmdCollList) {
            final Object[] listObj = (Object[]) object;
            instId = Integer.valueOf(listObj[0].toString());
            installment = (Installment) installmentDao.findById(instId, false);
            if (currDemand.getEgInstallmentMaster().equals(installment)) {
                if (listObj[2] != null && !listObj[2].equals(BigDecimal.ZERO))
                    currCollection = currCollection.add(new BigDecimal(listObj[2].toString()));

                currentRebate = currentRebate.add(new BigDecimal(listObj[3].toString()));
                currDmd = currDmd.add(new BigDecimal(listObj[1].toString()));
            } else {
                arrDmd = arrDmd.add(new BigDecimal((Double) listObj[1]));
                if (listObj[2] != null && !listObj[2].equals(BigDecimal.ZERO))
                    arrColelection = arrColelection.add(new BigDecimal(listObj[2].toString()));
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
    public static boolean isPropertyModified(final Property property) {

        for (final PropertyStatusValues psv : property.getBasicProperty().getPropertyStatusValuesSet())
            if (PROPERTY_MODIFY_REASON_ADD_OR_ALTER.equalsIgnoreCase(psv.getPropertyStatus().getStatusCode()))
                return true;

        return false;
    }

    public void makeTheEgBillAsHistory(final BasicProperty basicProperty) {
        final EgBill egBill = (EgBill) persistenceService.find(
                "from EgBill where module = ? and consumerId like ? || '%' and is_history = 'N'",
                moduleService.getModuleByName(PropertyTaxConstants.PTMODULENAME), basicProperty.getUpicNo());
        if (egBill != null) {
            egBill.setIs_History("Y");
            egBill.setModifiedDate(new Date());
            persistenceService.update(egBill);
        }
    }

    /**
     * Called to get concatenated string from Address fields
     *
     * @param address
     * @return String formed by concatenating the address fields
     */
    public static String buildAddress(final Address address) {

        LOGGER.debug("Entered into buildAddress");

        if (LOGGER.isInfoEnabled())
            LOGGER.info("buildAddress - Address: " + address);

        final StringBuffer strAddress = new StringBuffer();

        strAddress.append(isNotBlank(address.getLandmark()) ? address.getLandmark() : " ").append("|");
        strAddress.append(isNotBlank(address.getHouseNoBldgApt()) ? address.getHouseNoBldgApt() : " ").append("|");
        /*
         * strAddress.append((isNotBlank(address.getDoorNumOld())) ?
         * address.getDoorNumOld() : " ") .append("|");
         */

        final String tmpPin = address.getPinCode();
        strAddress.append(tmpPin != null && !tmpPin.toString().isEmpty() ? tmpPin : " ").append("|");

        /*
         * strAddress.append((isNotBlank(address.getMobileNo())) ?
         * address.getMobileNo() : " ") .append("|"); strAddress
         * .append((isNotBlank(address.getEmailAddress())) ?
         * address.getEmailAddress() : " ") .append("|");
         */

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
    public static String getOwnerAddress(final List<PropertyOwnerInfo> ownerSet) {
        LOGGER.debug("Entered into getOwnerAddress");

        String ownerAddress = "";
        for (final PropertyOwnerInfo owner : ownerSet) {
            final List<Address> addresses = owner.getOwner().getAddress();
            for (final Address address : addresses) {
                ownerAddress = address.toString();
                break;
            }
        }
        LOGGER.debug("Exiting from getOwnerAddress");
        return ownerAddress;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Date> getLatestCollRcptDateForProp(final String consumerCode) {
        LOGGER.debug("Entered into getLatestCollRcptDateForProp, consumerCode=" + consumerCode);

        final Map<String, Date> penaltyDates = new HashMap<String, Date>();
        final List<Object> rcptHeaderList = HibernateUtil
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
            for (final Object object : rcptHeaderList) {
                final Object[] penaltyDet = (Object[]) object;
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
     * <p>
     * This API is used during Data Entry and Modification to get the recent
     * property when the Occupancy Date after Data Entry or Modification is in
     * between installment
     * </p>
     *
     * @param basicProperty
     * @return
     */
    public static Property getLatestProperty(final BasicProperty basicProperty, final Character status) {
        LOGGER.debug("Entered into getLatestProperty, basicProperty=" + basicProperty);

        final Map<Date, Property> propertiesByCreatedDate = new TreeMap<Date, Property>();
        Property latestProperty = null;

        for (final Property property : basicProperty.getPropertySet())
            if (status == null)
                propertiesByCreatedDate.put(property.getCreatedDate(), property);
            else if (property.getStatus().equals(status))
                propertiesByCreatedDate.put(property.getCreatedDate(), property);

        if (!propertiesByCreatedDate.isEmpty()) {
            final List<Property> properties = new ArrayList<Property>(propertiesByCreatedDate.values());
            latestProperty = properties.get(properties.size() - 1);
            LOGGER.debug("getLatestProperty, latestProperty=" + latestProperty);
        }

        LOGGER.debug("Exiting from getLatestHistoryProperty");
        return latestProperty;
    }

    public Map<Installment, TaxCalculationInfo> getTaxCalInfoMap(final Set<Ptdemand> ptDmdSet) {
        final Map<Installment, TaxCalculationInfo> taxCalInfoMap = new TreeMap<Installment, TaxCalculationInfo>();

        for (final Ptdemand ptdmd : ptDmdSet) {
            final TaxCalculationInfo taxCalcInfo = getTaxCalInfo(ptdmd);
            if (taxCalcInfo != null)
                taxCalInfoMap.put(ptdmd.getEgInstallmentMaster(), taxCalcInfo);
        }

        return taxCalInfoMap;
    }

    public Map<Date, TaxCalculationInfo> getTaxCalInfoMap(final Set<Ptdemand> ptDmdSet, final Date occupancyDate) {
        final Map<Date, TaxCalculationInfo> taxCalInfoMap = new TreeMap<Date, TaxCalculationInfo>();
        Installment installment = null;

        for (final Ptdemand ptdmd : ptDmdSet) {
            final TaxCalculationInfo taxCalcInfo = getTaxCalInfo(ptdmd);
            if (taxCalcInfo != null) {
                installment = ptdmd.getEgInstallmentMaster();
                if (between(occupancyDate, ptdmd.getEgInstallmentMaster().getFromDate(), ptdmd.getEgInstallmentMaster()
                        .getToDate()))
                    taxCalInfoMap.put(occupancyDate, taxCalcInfo);
                else
                    taxCalInfoMap.put(installment.getFromDate(), taxCalcInfo);
            }
        }

        return taxCalInfoMap;
    }

    public String getDesignationName(final Long userId) {
        LOGGER.debug("Entered into getDesignationName, userId=" + userId);
        return getAssignment(userId).getDesignation().getName();
    }

    public WorkflowDetails initWorkflowAction(final PropertyImpl propertyModel, final WorkflowBean workflowBean,
            final Long loggedInUserId, final EisCommonService eisCommonService) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Entered into initWorkflowAction");
            LOGGER.debug("initWorkflowAction - propertyModel=" + propertyModel + ", workflowBean=" + workflowBean
                    + ", loggedInUserId=" + loggedInUserId);
        }

        String beanActionName[] = null;

        if (isNotNull(workflowBean))
            beanActionName = workflowBean.getActionName().split(":");

        WorkflowDetails workflowAction = null;

        if (WFLOW_ACTION_NAME_CREATE.equalsIgnoreCase(beanActionName[0]))
            workflowAction = new ActionCreate(propertyModel, workflowBean, loggedInUserId);
        else if (WFLOW_ACTION_NAME_MODIFY.equalsIgnoreCase(beanActionName[0]))
            workflowAction = new ActionModify(propertyModel, workflowBean, loggedInUserId);
        else if (WFLOW_ACTION_NAME_BIFURCATE.equalsIgnoreCase(beanActionName[0]))
            workflowAction = new ActionBifurcate(propertyModel, workflowBean, loggedInUserId);
        else if (WFLOW_ACTION_NAME_AMALGAMATE.equalsIgnoreCase(beanActionName[0]))
            workflowAction = new ActionAmalgmate(propertyModel, workflowBean, loggedInUserId);
        else if (WFLOW_ACTION_NAME_CHANGEADDRESS.equalsIgnoreCase(beanActionName[0]))
            workflowAction = new ActionChangeAddress(propertyModel, workflowBean, loggedInUserId);
        else if (WFLOW_ACTION_NAME_DEACTIVATE.equalsIgnoreCase(beanActionName[0]))
            workflowAction = new ActionDeactivate(propertyModel, workflowBean, loggedInUserId);
        else if (PropertyTaxConstants.WFLOW_ACTION_NAME_TRANSFER.equalsIgnoreCase(beanActionName[0]))
            workflowAction = new ActionNameTransfer(propertyModel, workflowBean, loggedInUserId);

        workflowAction.setWorkflowActionStep(this, eisCommonService);

        LOGGER.debug("initWorkflowAction - workflowAction=" + workflowAction);
        LOGGER.debug("Exiting from initWorkflowAction");
        return workflowAction;
    }

    public List<Property> getHistoryProperties(final BasicProperty basicProperty) {
        final List<Property> historyProperties = new ArrayList<Property>();

        for (final Property property : basicProperty.getPropertySet())
            if (PropertyTaxConstants.STATUS_ISHISTORY.equals(property.getStatus()))
                historyProperties.add(property);

        return historyProperties;

    }

    /**
     * Returns <tt> true </tt> if the <code> object <code> is null
     *
     * @param object
     * @return <tt> true </tt> if the <code> object <code> is null
     */
    public static boolean isNull(final Object object) {
        return object == null;
    }

    /**
     * Returns <tt> true </tt> if the <code> object </code> is not null
     *
     * @param object
     * @return <tt> true </tt> if the <code> object </code> is not null
     */
    public static boolean isNotNull(final Object object) {
        return object != null;
    }

    public static boolean isNotZero(final BigDecimal value) {
        return value == null ? false : value.compareTo(BigDecimal.ZERO) != 0;
    }

    /**
     * @param Property
     * @return Date the occupancy date
     */
    public Date getPropertyOccupancyDate(final Property property) {
        return property.getPropertyDetail().getDateOfCompletion() == null ? property.getEffectiveDate() : property
                .getPropertyDetail().getDateOfCompletion();
    }

    public static boolean isResidentialUnit(final String usageName) {
        return USAGES_FOR_RESD.contains(usageName) ? true : false;
    }

    public static boolean isNonResidentialUnit(final String usageName) {
        return USAGES_FOR_NON_RESD.contains(usageName) ? true : false;
    }

    public static boolean isOpenPlotUnit(final String usageName) {
        return USAGES_FOR_OPENPLOT.contains(usageName) ? true : false;
    }

    /**
     * Returns true if the amount is equal to 0 else false if amount is null or
     * if its not 0
     *
     * @param amount
     * @return true if amount is equal to 0
     */
    public static boolean isZero(final BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) == 0 ? true : false;
    }

    public Installment getPTInstallmentForDate(final Date date) {
        final Module module = moduleService.getModuleByName(PropertyTaxConstants.PTMODULENAME);
        return installmentDao.getInsatllmentByModuleForGivenDate(module, date);
    }

    /**
     * Gives the Inactive demand property
     * <p>
     * Property whose status is I is Inactive Demand property, demand will be
     * activated either if the citizen pays tax or after 21 days from the date
     * of notice generation
     * </p>
     *
     * @param basicProperty
     * @return
     */
    public static Property getInactiveDemandProperty(final BasicProperty basicProperty) {
        LOGGER.debug("Entered into getInactiveDemandProperty");

        for (final Property property : basicProperty.getPropertySet())
            if (property.getStatus().equals(PropertyTaxConstants.STATUS_DEMAND_INACTIVE)
                    && property.getIsDefaultProperty().equals('Y'))
                return property;

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
    public static String getRevisedDemandYear(final Property property) {
        LOGGER.debug("Entered into getDemandEffectiveYear, property=" + property);
        String demandEffectiveYear = null;

        demandEffectiveYear = new SimpleDateFormat("yyyy").format(property.getPropertyDetail().getDateOfCompletion());
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
    /*
     * public static Integer getNoticeDaysForInactiveDemand(Property property)
     * throws ParseException { String query = ""; List result = null; Integer
     * days = 21; Date noticeDate = null; String indexNumber =
     * property.getBasicProperty().getUpicNo(); if (isNoticeGenerated(property))
     * { result = HibernateUtil .getCurrentSession() .createQuery(
     * "select to_char(n.noticeDate, 'dd/mm/yyyy') from PtNotice n " +
     * "where n.basicProperty = :basicProp " + "and n.noticeDate is not null " +
     * "and n.noticeDate >= :propCreatedDate") .setEntity("basicProp",
     * property.getBasicProperty()) .setDate("propCreatedDate",
     * property.getCreatedDate().toDate()).list(); if (result.isEmpty()) {
     * LOGGER.debug("Notice generation date is not available for property=" +
     * indexNumber); } else { noticeDate = new
     * SimpleDateFormat(PropertyTaxConstants
     * .DATE_FORMAT_DDMMYYY).parse(result.get(0) .toString()); if
     * (noticeDate.before(new Date())) { days = days -
     * getNumberOfDays(noticeDate, new Date()).intValue(); days += 1; } } } else
     * { LOGGER.debug(
     * "getNoticeDaysForInactiveDemand - Notice is not yet generated for property="
     * + indexNumber); LOGGER.debug(
     * "getNoticeDaysForInactiveDemand - using defualt notice period days " +
     * days); } return days; }
     */
    /*
     * public static boolean isNoticeGenerated(Property property) { return
     * (property.getExtra_field3() != null &&
     * property.getExtra_field3().equalsIgnoreCase(
     * PropertyTaxConstants.STR_YES)) || (property.getExtra_field4() != null &&
     * property.getExtra_field4().equalsIgnoreCase(
     * PropertyTaxConstants.STR_YES)) ? true : false; }
     */

    public List<String> getAdvanceYearsFromCurrentInstallment() {
        LOGGER.debug("Entered into getAdvanceYearsFromCurrentInstallment");

        final List<String> advanceYears = new ArrayList<String>();
        final Installment currentInstallment = getCurrentInstallment();
        Integer year = null;
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentInstallment.getFromDate());
        year = calendar.get(Calendar.YEAR);

        for (int i = 0; i < MAX_ADVANCES_ALLOWED; i++) {

            final int fromYear = ++year;
            final int toYear = year + 1;

            advanceYears.add(fromYear + "-" + String.valueOf(toYear).substring(2));
        }
        LOGGER.debug("getAdvanceYearsFromCurrentInstallment = " + advanceYears);
        LOGGER.debug("Exiting from getAdvanceYearsFromCurrentInstallment");

        return Collections.emptyList();
    }

    @SuppressWarnings("unchecked")
    public List<Installment> getOrderedInstallmentsFromGivenDate(final Date date) {
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
    public Map<String, EgDemandDetails> getEgDemandDetailsAndReasonAsMap(final Set<EgDemandDetails> egDemandDetails) {

        final Map<String, EgDemandDetails> demandDetailAndReason = new HashMap<String, EgDemandDetails>();

        for (final EgDemandDetails egDmndDtls : egDemandDetails) {

            final EgDemandReasonMaster dmndRsnMstr = egDmndDtls.getEgDemandReason().getEgDemandReasonMaster();

            if (dmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_GENERAL_TAX))
                demandDetailAndReason.put(DEMANDRSN_CODE_GENERAL_TAX, egDmndDtls);
            else if (dmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_LIBRARY_CESS))
                demandDetailAndReason.put(DEMANDRSN_CODE_LIBRARY_CESS, egDmndDtls);
            else if (dmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_EDUCATIONAL_CESS))
                demandDetailAndReason.put(DEMANDRSN_CODE_EDUCATIONAL_CESS, egDmndDtls);
            else if (dmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_UNAUTHORIZED_PENALTY))
                demandDetailAndReason.put(DEMANDRSN_CODE_UNAUTHORIZED_PENALTY, egDmndDtls);
            else if (dmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_PENALTY_FINES))
                demandDetailAndReason.put(DEMANDRSN_CODE_PENALTY_FINES, egDmndDtls);
            else if (dmndRsnMstr.getCode().equalsIgnoreCase(DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY))
                demandDetailAndReason.put(DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY, egDmndDtls);
            else if (dmndRsnMstr.getCode().equalsIgnoreCase(PropertyTaxConstants.DEMANDRSN_CODE_ADVANCE))
                demandDetailAndReason.put(PropertyTaxConstants.DEMANDRSN_CODE_ADVANCE, egDmndDtls);
        }

        return demandDetailAndReason;
    }

    /**
     * Gives the Earliest modification date
     *
     * @param propertyId
     * @return
     */
    public Date getEarliestModificationDate(final String propertyId) {
        final List result = HibernateUtil
                .getCurrentSession()
                .createQuery(
                        "select to_char(min(pd.effective_date), 'dd/mm/yyyy') "
                                + "from PropertyImpl p inner join p.propertyDetail pd "
                                + "where p.basicProperty.active = true " + "and p.basicProperty.upicNo = :upicNo "
                                + "and (p.remarks is null or p.remarks <> :propertyMigrationRemarks) "
                                + "and pd.effective_date is not null").setString("upicNo", propertyId)
                .setString("propertyMigrationRemarks", PropertyTaxConstants.STR_MIGRATED_REMARKS).list();

        Date earliestModificationDate = null;

        if (result.isEmpty())
            return null;
        else if (result.get(0) == null)
            return null;

        try {
            earliestModificationDate = PropertyTaxConstants.DATEFORMATTER_DDMMYYYY.parse((String) result.get(0));
        } catch (final ParseException e) {
            LOGGER.error("Error while parsing effective date", e);
            throw new ApplicationRuntimeException("Error while parsing effective date", e);
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
        final org.slf4j.Logger LOG = LoggerFactory.getLogger(PropertyTaxUtil.class);

        try {
            waterTaxEffectiveDate = PropertyTaxConstants.DATEFORMATTER_DDMMYYYY.parse(PENALTY_WATERTAX_EFFECTIVE_DATE);
        } catch (final ParseException pe) {
            throw new ApplicationRuntimeException(
                    "Error while parsing Water Tax Effective Date for Penalty Calculation", pe);
        }

        LOG.debug("getWaterTaxEffectiveDateForPenalty - waterTaxEffectiveDate = {} ", waterTaxEffectiveDate);
        return waterTaxEffectiveDate;
    }

    /**
     * Gives the properties and occupancy date map
     *
     * @return properties by occupancy dates
     */
    public Map<Date, Property> getPropertiesByOccupancy(final BasicProperty basicProperty) {
        LOGGER.debug("Entered into getPropertiesByOccupancy");

        final Map<Date, Property> propertyByCreatedDate = new TreeMap<Date, Property>();
        final Map<Date, Property> propertyByOccupancyDate = new TreeMap<Date, Property>();

        for (final Property property : basicProperty.getPropertySet())
            if (inConsider(property))
                propertyByCreatedDate.put(property.getCreatedDate(), property);

        for (final Map.Entry<Date, Property> entry : propertyByCreatedDate.entrySet())
            propertyByOccupancyDate.put(getPropertyOccupancyDate(entry.getValue()), entry.getValue());

        LOGGER.debug("Exiting from getPropertiesByOccupancy");

        return propertyByOccupancyDate;
    }

    private boolean inConsider(final Property property) {
        return property.getRemarks() == null || !property.getRemarks().startsWith(STR_MIGRATED);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Map<Installment, BigDecimal>> prepareReasonWiseDenandAndCollection(final Property property,
            final Installment currentInstallment) {
        LOGGER.debug("Entered into prepareReasonWiseDenandAndCollection, property=" + property);

        final Map<Installment, BigDecimal> installmentWiseDemand = new TreeMap<Installment, BigDecimal>();
        final Map<Installment, BigDecimal> installmentWiseCollection = new TreeMap<Installment, BigDecimal>();
        final Map<String, Map<Installment, BigDecimal>> demandAndCollection = new HashMap<String, Map<Installment, BigDecimal>>();

        String demandReason = "";
        Installment installment = null;

        final List<String> demandReasonExcludeList = Arrays.asList(DEMANDRSN_CODE_PENALTY_FINES,
                PropertyTaxConstants.DEMANDRSN_CODE_ADVANCE);

        final String query = "select ptd from Ptdemand ptd " + "inner join fetch ptd.egDemandDetails dd "
                + "inner join fetch dd.egDemandReason dr " + "inner join fetch dr.egDemandReasonMaster drm "
                + "inner join fetch ptd.egptProperty p " + "inner join fetch p.basicProperty bp "
                + "where bp.active = true " + "and (p.status = 'A' or p.status = 'I') " + "and p = :property "
                + "and ptd.egInstallmentMaster = :installment";

        final Ptdemand ptDemand = (Ptdemand) HibernateUtil.getCurrentSession().createQuery(query)
                .setEntity("property", property).setEntity("installment", currentInstallment).list().get(0);

        for (final EgDemandDetails dmdDet : ptDemand.getEgDemandDetails()) {

            demandReason = dmdDet.getEgDemandReason().getEgDemandReasonMaster().getCode();

            if (!demandReasonExcludeList.contains(demandReason)) {
                installment = dmdDet.getEgDemandReason().getEgInstallmentMaster();

                if (installmentWiseDemand.get(installment) == null)
                    installmentWiseDemand.put(installment, dmdDet.getAmount());
                else
                    installmentWiseDemand.put(installment,
                            installmentWiseDemand.get(installment).add(dmdDet.getAmount()));

                if (installmentWiseCollection.get(installment) == null)
                    installmentWiseCollection.put(installment, dmdDet.getAmtCollected());
                else
                    installmentWiseCollection.put(installment,
                            installmentWiseCollection.get(installment).add(dmdDet.getAmtCollected()));
            }
        }

        demandAndCollection.put("DEMAND", installmentWiseDemand);
        demandAndCollection.put("COLLECTION", installmentWiseCollection);

        LOGGER.debug("prepareReasonWiseDenandAndCollection - demandAndCollection=" + demandAndCollection);
        LOGGER.debug("Exiting from prepareReasonWiseDenandAndCollection");
        return demandAndCollection;
    }

    /*
     * Return the map with required demand and collection details required for
     * view page.
     */
    @SuppressWarnings("unchecked")
    public Map<String, BigDecimal> prepareDemandDetForView(final Property property, final Installment currentInstallment) {
        LOGGER.debug("Entered into prepareDemandDetForView, property=" + property);

        Map<String, BigDecimal> DCBDetails = new HashMap<String, BigDecimal>();
        String demandReason = "";
        Installment installment = null;
        BigDecimal totalArrearDemand = BigDecimal.ZERO;
        BigDecimal totalCurrentDemand = BigDecimal.ZERO;
        BigDecimal totalArrearCollection = BigDecimal.ZERO;
        BigDecimal totalCurrentCollection = BigDecimal.ZERO;

        final List<String> demandReasonExcludeList = Arrays.asList(DEMANDRSN_CODE_PENALTY_FINES,
                PropertyTaxConstants.DEMANDRSN_CODE_ADVANCE);

        final String query = "select ptd from Ptdemand ptd " + "inner join fetch ptd.egDemandDetails dd "
                + "inner join fetch dd.egDemandReason dr " + "inner join fetch dr.egDemandReasonMaster drm "
                + "inner join fetch ptd.egptProperty p " + "inner join fetch p.basicProperty bp "
                + "where bp.active = true " + "and (p.status = 'A' or p.status = 'I' or p.status = 'W') "
                + "and p = :property " + "and ptd.egInstallmentMaster = :installment";

        final Ptdemand ptDemand = (Ptdemand) HibernateUtil.getCurrentSession().createQuery(query)
                .setEntity("property", property).setEntity("installment", currentInstallment).list().get(0);

        for (final EgDemandDetails dmdDet : ptDemand.getEgDemandDetails()) {

            demandReason = dmdDet.getEgDemandReason().getEgDemandReasonMaster().getCode();

            if (!demandReasonExcludeList.contains(demandReason)) {
                installment = dmdDet.getEgDemandReason().getEgInstallmentMaster();
                if (installment.equals(currentInstallment)) {
                    totalCurrentDemand = totalCurrentDemand.add(dmdDet.getAmount());
                    totalCurrentCollection = totalCurrentCollection.add(dmdDet.getAmtCollected());
                    DCBDetails.put(dmdDet.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster(),
                            dmdDet.getAmount());
                } else {
                    totalArrearDemand = totalArrearDemand.add(dmdDet.getAmount());
                    totalArrearCollection = totalArrearCollection.add(dmdDet.getAmtCollected());
                }
            }
        }
        DCBDetails.put(CURR_DMD_STR, totalCurrentDemand);
        DCBDetails.put(ARR_DMD_STR, totalArrearDemand);
        DCBDetails.put(CURR_COLL_STR, totalCurrentCollection);
        DCBDetails.put(ARR_COLL_STR, totalArrearCollection);
        LOGGER.debug("prepareDemandDetForView - demands=" + DCBDetails);
        LOGGER.debug("Exiting from prepareDemandDetForView");
        return DCBDetails;
    }

    @SuppressWarnings("unchecked")
    public Map<Date, Property> getPropertiesForPenlatyCalculation(final BasicProperty basicProperty) {

        final String query = "select p from PropertyImpl p " + "inner join fetch p.basicProperty bp "
                + "where bp.upicNo = ? and bp.active = true " + "and (p.remarks = null or p.remarks <> ?) "
                + "order by p.createdDate";

        final List<Property> allProperties = HibernateUtil.getCurrentSession().createQuery(query)
                .setString(0, basicProperty.getUpicNo()).setString(1, PropertyTaxConstants.STR_MIGRATED_REMARKS).list();

        new ArrayList<Property>();

        final List<String> mutationsCodes = Arrays.asList("NEW", "MODIFY");
        Property property = null;
        Property prevProperty = null;
        String mutationCode = null;
        String nextMutationCode = null;
        String prevMutationCode = null;
        final int noOfProperties = allProperties.size();

        final Map<Date, Property> propertyAndEffectiveDate = new TreeMap<Date, Property>();
        Date firstDataEntryEffectiveDate = null;

        for (int i = 0; i < noOfProperties; i++) {
            property = allProperties.get(i);
            prevProperty = i > 0 ? allProperties.get(i - 1) : null;

            prevMutationCode = prevProperty != null ? prevProperty.getPropertyDetail().getPropertyMutationMaster()
                    .getCode() : null;
            mutationCode = property.getPropertyDetail().getPropertyMutationMaster().getCode();
            nextMutationCode = i != noOfProperties - 1 ? allProperties.get(i + 1).getPropertyDetail()
                    .getPropertyMutationMaster().getCode() : null;

            if (!mutationCode.equalsIgnoreCase(PropertyTaxConstants.PROPERTY_MODIFY_REASON_OBJ))
                if (mutationsCodes.contains(mutationCode))
                    propertyAndEffectiveDate.put(getPropertyOccupancyDate(property), property);
                else {

                    if (isFirstDataEntry(prevMutationCode, mutationCode, prevProperty))
                        firstDataEntryEffectiveDate = getPropertyOccupancyDate(property);

                    if (mutationCode.equalsIgnoreCase(PROPERTY_MODIFY_REASON_DATA_ENTRY) && nextMutationCode == null)
                        propertyAndEffectiveDate.put(firstDataEntryEffectiveDate, property);
                }
        }

        final Map<Date, Property> propertyByOccupancyDate = new TreeMap<Date, Property>();

        for (final Map.Entry<Date, Property> entry : propertyAndEffectiveDate.entrySet())
            if (entry.getKey() == null)
                propertyByOccupancyDate.put(getPropertyOccupancyDate(entry.getValue()), entry.getValue());
            else
                propertyByOccupancyDate.put(entry.getKey(), entry.getValue());

        return propertyByOccupancyDate;
    }

    /*
     * private boolean areNoticesGenerated(Property property) { boolean
     * isNotice134Or127Generated = property.getExtra_field3() != null &&
     * property.getExtra_field3().equalsIgnoreCase("Yes") ? true : false;
     * boolean isNoticePVRGenerated = property.getExtra_field4() != null &&
     * property.getExtra_field4().equalsIgnoreCase("Yes") ? true : false; return
     * isNotice134Or127Generated && isNoticePVRGenerated ? true : false; }
     */

    private boolean isFirstDataEntry(final String prevPropMutationCode, final String mutationCode,
            final Property prevProperty) {

        final List<String> mutationCodes = Arrays.asList(PropertyTaxConstants.MUTATION_CODE_NEW,
                PropertyTaxConstants.PROPERTY_MODIFY_REASON_OBJ);

        if (prevPropMutationCode != null && mutationCodes.contains(prevPropMutationCode)
                && mutationCode.equalsIgnoreCase(PROPERTY_MODIFY_REASON_DATA_ENTRY))
            return true;

        return prevProperty == null && mutationCode.equalsIgnoreCase(PROPERTY_MODIFY_REASON_DATA_ENTRY) ? true : false;
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
    public static boolean afterOrEqual(final Date date, final Date dateToCompare) {
        return date.after(dateToCompare) || date.equals(dateToCompare);
    }

    /**
     * @param basicProperty
     * @param propertyWiseConsumptions
     * @return list of DemandNoticeDetails having aggregated arrear and current
     *         demand tax amounts
     */
    public List<DemandNoticeDetailsInfo> getDemandNoticeDetailsInfo(final BasicProperty basicProperty,
            final PropertyWiseConsumptions propertyWiseConsumptions) {
        final List<DemandNoticeDetailsInfo> demandNoticeDetailsInfo = new LinkedList<DemandNoticeDetailsInfo>();
        final EgDemand egDemand = ptDemandDAO.getNonHistoryCurrDmdForProperty(basicProperty.getProperty());
        final Module module = moduleService.getModuleByName(PropertyTaxConstants.PTMODULENAME);
        final CFinancialYear finYear = financialYearDAO.getFinancialYearByDate(new Date());
        List<DemandNoticeDetailsInfo> tempList = new LinkedList<DemandNoticeDetailsInfo>();
        // General Tax and Penalty
        tempList = getArrearCurrentDemandbyReasonCode(egDemand, module, finYear);
        if (tempList != null && !tempList.isEmpty())
            demandNoticeDetailsInfo.addAll(tempList);
        // Water Tax
        if (propertyWiseConsumptions != null) {
            tempList = new LinkedList<DemandNoticeDetailsInfo>();
            tempList = getArrearCurrentDemandforWaterTax(propertyWiseConsumptions);
            if (tempList != null && !tempList.isEmpty())
                demandNoticeDetailsInfo.addAll(tempList);
        }
        return demandNoticeDetailsInfo;
    }

    /**
     * @Description Returns Aggregated list of arrear and current demand amount
     *              for water tax
     * @param propertyWiseConsumptions
     * @return
     */
    private List<DemandNoticeDetailsInfo> getArrearCurrentDemandforWaterTax(
            final PropertyWiseConsumptions propertyWiseConsumptions) {
        final List<DemandNoticeDetailsInfo> demandNoticeDetailsInfo = new LinkedList<DemandNoticeDetailsInfo>();
        String arrearFromDate = "";
        String arrearToDate = "";
        BigDecimal arrearAmount = BigDecimal.ZERO;
        String currentFromDate = "";
        String currentToDate = "";
        DemandNoticeDetailsInfo dndi;
        BigDecimal currentAmount = BigDecimal.ZERO;
        if (propertyWiseConsumptions.getConsumerConsumptions() != null
                && propertyWiseConsumptions.getConsumerConsumptions().size() > 0) {
            for (final ConsumerConsumption cc : propertyWiseConsumptions.getConsumerConsumptions())
                if (cc != null) {
                    if (cc.getArrearDue() != null && cc.getArrearDue().compareTo(BigDecimal.ZERO) > 0) {
                        if (arrearFromDate == "")
                            arrearFromDate = sdf.format(cc.getArrearFromDate().toDate());
                        arrearToDate = sdf.format(cc.getArrearToDate().toDate());
                        arrearAmount = arrearAmount.add(cc.getArrearDue());

                    }
                    if (cc.getCurrentDue() != null && cc.getCurrentDue().compareTo(BigDecimal.ZERO) > 0) {
                        if (currentFromDate == "")
                            currentFromDate = sdf.format(cc.getCurrentFromDate().toDate());
                        currentToDate = sdf.format(cc.getCurentToDate().toDate());
                        currentAmount = currentAmount.add(cc.getCurrentDue());
                    }
                }
            if (arrearFromDate != "") {
                dndi = new DemandNoticeDetailsInfo();
                dndi.setFromDate(arrearFromDate);
                dndi.setToDate(arrearToDate);
                dndi.setWaterTax(arrearAmount);
                demandNoticeDetailsInfo.add(dndi);
            }
            if (currentFromDate != "") {
                dndi = new DemandNoticeDetailsInfo();
                dndi.setFromDate(currentFromDate);
                dndi.setToDate(currentToDate);
                dndi.setWaterTax(currentAmount);
                demandNoticeDetailsInfo.add(dndi);
            }
            return demandNoticeDetailsInfo;
        }
        return demandNoticeDetailsInfo;
    }

    /**
     * @Description Returns Aggregated list of arrear and current demand amount
     *              for all reasoncodes
     * @param egDemand
     * @param module
     * @param reasonCode
     * @param finYear
     * @return
     */
    private List<DemandNoticeDetailsInfo> getArrearCurrentDemandbyReasonCode(final EgDemand egDemand,
            final Module module, final CFinancialYear finYear) {
        List list = new LinkedList();
        String arrearFromDate = "";
        String arrearToDate = "";
        BigDecimal arrearAmount = BigDecimal.ZERO;
        BigDecimal pnltyArrearAmount = BigDecimal.ZERO;
        String currentFromDate = "";
        String currentToDate = "";
        Integer instId = null;
        BigDecimal currentAmount = BigDecimal.ZERO;
        BigDecimal pnltyCurrentAmount = BigDecimal.ZERO;
        Installment installment;
        final List<DemandNoticeDetailsInfo> demandNoticeDetailsInfo = new LinkedList<DemandNoticeDetailsInfo>();
        DemandNoticeDetailsInfo dndi;
        list = demandGenericDAO.getReasonWiseDCB(egDemand, module);
        for (final Object record : list) {
            final Object[] data = (Object[]) record;
            instId = Integer.valueOf(data[5].toString());
            installment = (Installment) installmentDao.findById(instId, false);
            if (installment.getFromDate().compareTo(finYear.getStartingDate()) < 0) {
                if (arrearFromDate == "")
                    arrearFromDate = sdf.format(installment.getFromDate());
                arrearToDate = sdf.format(installment.getToDate());
                if (!data[0].toString().equalsIgnoreCase(DEMANDRSN_CODE_PENALTY_FINES))
                    arrearAmount = arrearAmount.add(new BigDecimal(data[2].toString()));
                else
                    pnltyArrearAmount = pnltyArrearAmount.add(new BigDecimal(data[2].toString()));
            } else {
                if (currentFromDate == "")
                    currentFromDate = sdf.format(installment.getFromDate());
                currentToDate = sdf.format(installment.getToDate());
                if (!data[0].toString().equalsIgnoreCase(DEMANDRSN_CODE_PENALTY_FINES))
                    currentAmount = currentAmount.add(new BigDecimal(data[2].toString()));
                else
                    pnltyCurrentAmount = pnltyCurrentAmount.add(new BigDecimal(data[2].toString()));
            }
        }
        if (arrearFromDate != "") {
            dndi = new DemandNoticeDetailsInfo();
            dndi.setFromDate(arrearFromDate);
            dndi.setToDate(arrearToDate);
            dndi.setPropertyTax(arrearAmount);
            dndi.setPenalty(pnltyArrearAmount);
            demandNoticeDetailsInfo.add(dndi);
        }
        if (currentFromDate != "") {
            dndi = new DemandNoticeDetailsInfo();
            dndi.setFromDate(currentFromDate);
            dndi.setToDate(currentToDate);
            dndi.setPropertyTax(currentAmount);
            dndi.setPenalty(pnltyCurrentAmount);
            demandNoticeDetailsInfo.add(dndi);
        }
        return demandNoticeDetailsInfo;
    }

    public String logoBasePath() {
        final HttpServletRequest request = ServletActionContext.getRequest();
        final String url = WebUtils.extractRequestDomainURL(request, false);
        final String imagePath = url.concat(PropertyTaxConstants.IMAGES_BASE_PATH).concat(ReportUtil.fetchLogo());
        return imagePath;
    }

    public DepreciationMaster getDepreciationByDate(final Date depreciationDate) {
        String depreciationYear = null;
        final int years = DateUtils.getNumberOfYearPassesed(depreciationDate, new Date());
        if (years >= 0 && years <= 25)
            depreciationYear = "0-25";
        else if (years > 25 && years <= 40)
            depreciationYear = "26-40";
        else
            depreciationYear = "Above 40";
        return (DepreciationMaster) persistenceService.getSession()
                .createQuery("from DepreciationMaster where depreciationName = :depreName")
                .setString("depreName", depreciationYear).uniqueResult();
    }

    public List<InstrumentType> prepareInstrumentTypeList() {
        return persistenceService.findAllBy("from InstrumentType order by type");
    }

    public Boolean isCorporation() {
        Boolean isCorporation = Boolean.FALSE;
        final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(PTMODULENAME,
                APPCONFIG_ISCORPORATION);
        if (appConfigValue != null && !appConfigValue.isEmpty())
            isCorporation = Boolean.valueOf(appConfigValue.get(0).getValue());
        return isCorporation;
    }

    public Boolean isSeaShoreULB() {
        Boolean isSeaShoreULB = Boolean.FALSE;
        final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(PTMODULENAME,
                APPCONFIG_ISSEASHORE_ULB);
        if (appConfigValue != null && !appConfigValue.isEmpty())
            isSeaShoreULB = Boolean.valueOf(appConfigValue.get(0).getValue());
        return isSeaShoreULB;
    }

    public Boolean isPrimaryServiceApplicable() {
        Boolean isCorporation = Boolean.FALSE;
        final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(PTMODULENAME,
                APPCONFIG_IS_PRIMARY_SERVICECHARGES_APPLICABLE);
        if (appConfigValue != null && !appConfigValue.isEmpty())
            isCorporation = Boolean.valueOf(appConfigValue.get(0).getValue());
        return isCorporation;
    }

    public String getRolesForUserId(final Long userId) {
        LOGGER.debug("Entered into method getRolesForUserId " + userId);
        String roleName;
        final List<String> roleNameList = new ArrayList<String>();
        final User user = userService.getUserById(userId);
        for (final Role role : user.getRoles()) {
            roleName = role.getName() != null ? role.getName() : "";
            roleNameList.add(roleName);
        }
        LOGGER.debug("Exit from method getRolesForUserId with return value : " + roleNameList.toString().toUpperCase());
        return roleNameList.toString().toUpperCase();
    }

    public String generateUserName(final String name) {
        final StringBuilder userNameBuilder = new StringBuilder();
        String userName = "";
        if (name.length() < 6)
            userName = String.format("%-6s", name).replace(' ', '0');
        else
            userName = name.substring(0, 6).replace(' ', '0');
        userNameBuilder.append(userName).append(RandomStringUtils.randomNumeric(4));
        return userNameBuilder.toString();
    }

    /**
     * @param basicPropertyId
     * @return PropertyWiseConsumptions object having water tax details for a
     *         given property
     */
    public PropertyWiseConsumptions getPropertyWiseConsumptions(final String basicPropertyId) {
        final PropertyWiseConsumptions propertyWiseConsumptions = waterChargesIntegrationService
                .getPropertyWiseConsumptionsForWaterCharges(basicPropertyId);
        return propertyWiseConsumptions;
    }

    public Properties loadTaxRates() {
        final Properties taxRates = new Properties();
        final String s = appConfigValuesService.getAppConfigValueByDate(PTMODULENAME, "PT_TAXRATES", new Date())
                .getValue();
        final StringReader sr = new StringReader(s);
        try {
            taxRates.load(sr);
        } catch (final IOException e) {
            throw new ApplicationRuntimeException("Could not decipher Tax rates from string" + s, e);
        }
        sr.close();
        return taxRates;
    }

    /**
     * @param fromDate
     * @param toDate
     * @param collMode
     * @param transMode
     * @param mode
     * @param boundaryId
     *            (used in case of collection summary report other than
     *            usagewise)
     * @param propTypeCategoryId
     * @param zoneId
     * @param wardId
     * @param areaId
     * @return
     */
    public Query prepareQueryforCollectionSummaryReport(final String fromDate, final String toDate,
            final String collMode, final String transMode, final String mode, final String boundaryId,
            final String propTypeCategoryId, final Long zoneId, final Long wardId, final Long blockId) {
        String srchQryStr = "";
        String baseQry = "", orderbyQry = "";
        final String ZONEWISE = "zoneWise";
        final String WARDWISE = "wardWise";
        final String BLOCKWISE = "blockWise";
        final String LOCALITYWISE = "localityWise";
        final String USAGEWISE = "usageWise";
        new ArrayList<Object>();
        try {
            baseQry = "select cs from CollectionSummary cs where ";
            if (fromDate != null && !fromDate.equals("DD/MM/YYYY") && !fromDate.equals(""))
                srchQryStr = "(cast(cs.receiptDate as date)) >= to_date('" + fromDate + "', 'DD/MM/YYYY') ";
            if (toDate != null && !toDate.equals("DD/MM/YYYY") && !toDate.equals(""))
                srchQryStr = srchQryStr + "and (cast(cs.receiptDate as date)) <= to_date('" + toDate
                        + "', 'DD/MM/YYYY') ";
            if (collMode != null && !collMode.equals("") && !collMode.equals("-1")) {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Collection Mode = " + collMode);
                srchQryStr = srchQryStr + "and cs.collectionType ='" + collMode + "' ";
            }
            if (transMode != null && !transMode.equals("") && !transMode.equals("-1")) {
                if (LOGGER.isDebugEnabled())
                    LOGGER.debug("Transaction Mode = " + transMode);
                srchQryStr = srchQryStr + "and (cs.paymentMode ='" + transMode + "' OR cs.paymentMode like '%' || '"+transMode+"' || '%')";
            }
            if (mode.equals(USAGEWISE)) {
                if (propTypeCategoryId != null && !propTypeCategoryId.equals("") && !propTypeCategoryId.equals("-1")) {
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug("Transaction Mode = " + transMode);
                    srchQryStr = srchQryStr
                            + "and cs.property.propertyDetail in (select floor.propertyDetail from Floor floor where floor.propertyUsage = '"
                            + propTypeCategoryId + "')) ";
                }
                if (zoneId != null && !zoneId.equals("") && zoneId != -1)
                    srchQryStr = srchQryStr + " and cs.zoneId.id =" + zoneId;
                if (wardId != null && !wardId.equals("") && wardId != -1)
                    srchQryStr = srchQryStr + " and cs.wardId.id =" + wardId;
                if (blockId != null && !blockId.equals("") && blockId != -1)
                    srchQryStr = srchQryStr + " and cs.areaId.id =" + blockId;
                orderbyQry = "order by cs.property.propertyDetail.categoryType";
            }
            if (mode.equals(ZONEWISE)) {
                if (boundaryId != null && !boundaryId.equals("") && !boundaryId.equals("-1")) {
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug("zoneNo = " + boundaryId);
                    srchQryStr = srchQryStr + "and cs.zoneId.id =" + boundaryId;
                }
                orderbyQry = " and cs.zoneId.boundaryType.name = 'Zone' order by cs.zoneId.boundaryNum";
            } else if (mode.equals(WARDWISE)) {
                if (boundaryId != null && !boundaryId.equals("") && !boundaryId.equals("-1")) {
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug("wardNo = " + boundaryId);
                    srchQryStr = srchQryStr + "and cs.wardId.id =" + boundaryId;
                }
                orderbyQry = " and cs.wardId.boundaryType.name = 'Ward' order by cs.wardId.boundaryNum";
            } else if (mode.equals(BLOCKWISE)) {
                if (boundaryId != null && !boundaryId.equals("") && !boundaryId.equals("-1")) {
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug("blockNo = " + boundaryId);
                    srchQryStr = srchQryStr + "and cs.areaId.id =" + boundaryId;
                }
                orderbyQry = " and cs.areaId.boundaryType.name = 'Block' order by cs.areaId.boundaryNum";
            } else if (mode.equals(LOCALITYWISE)) {
                if (boundaryId != null && !boundaryId.equals("") && !boundaryId.equals("-1")) {
                    if (LOGGER.isDebugEnabled())
                        LOGGER.debug("localityNo = " + boundaryId);
                    srchQryStr = srchQryStr + "and cs.localityId.id =" + boundaryId;
                }
                orderbyQry = "  order by cs.localityId.boundaryNum";
            }
            srchQryStr = baseQry + srchQryStr + orderbyQry;

        } catch (final Exception e) {
            e.printStackTrace();
            LOGGER.error("Error occured in Class : CollectionSummaryReportAction  Method : list", e);
            throw new ApplicationRuntimeException(
                    "Error occured in Class : CollectionSummaryReportAction  Method : list " + e.getMessage());
        }
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("Exit from prepareQueryforCollectionSummaryReport method");
        final Query qry = persistenceService.getSession().createQuery(srchQryStr.toString());
        return qry;
    }

    /**
     * @ Description - Returns query that retrieves zone/ward/block/propertywise
     * Arrear, Current Demand and Collection Details Final Query Form : select
     * boundary,arrear,current from (select boundary,arrear,0 as collection
     * group by boundary union select boundary,0 as arrear, collection group by
     * boundary) group by boundary
     *
     * @param boundaryId
     * @return
     */
    public SQLQuery prepareQueryForDCBReport(final Long boundaryId, final String mode) {

        final String WARDWISE = "ward";
        final String BLOCKWISE = "block";
        final String PROPERTY = "property";

        final StringBuffer queryStr = new StringBuffer("");
        final StringBuffer unionQueryStr = new StringBuffer("");
        String arrear_innerCommonQry0 = "", arrear_innerCommonQry1 = "", current_innerCommonQry0 = "", current_innerCommonQry1 = "";
        String finalCommonQry = "", finalSelectQry = "", finalGrpQry = "", finalWhereQry = "", finalFrmQry = "";
        String innerSelectQry0 = "", innerSelectQry1 = "", arrearGroupBy = "", whereQry = "", collGroupBy = "";
        Long param = null;

        if (boundaryId != -1 && boundaryId != null)
            param = boundaryId;
        // To retreive Arrear Demand and Collection Details
        arrear_innerCommonQry0 = "idc.* from egpt_mv_inst_dem_coll idc, egpt_mv_propertyinfo pi,  eg_installment_master im "
                + "where idc.id_basic_property=pi.basicpropertyid and im.id=idc.id_installment and pi.isactive = true "
                + "and im.start_date not between (select STARTINGDATE from financialyear where now() between STARTINGDATE and ENDINGDATE) "
                + "and  (select ENDINGDATE from financialyear where now() between STARTINGDATE and ENDINGDATE)";

        arrear_innerCommonQry1 = "sum(GeneralTax) as arrearGT, sum(LibCessTax) as arrearLC, sum(EduCessTax) as arrearEC,"
                + "sum(UnauthPenaltyTax) as arrearUPT,sum(PenaltyFinesTax) as arrearPFT,sum(SewTax) as arrearST,"
                + "sum(VacantLandTax) as arrearVLT,sum(PubSerChrgTax) as arrearPSCT,sum(GeneralTaxColl) as arrearGTColl, "
                + "sum(LibCessTaxColl) as arrearLCColl, sum(EduCessTaxColl) as arrearECColl,sum(UnauthPenaltyTaxColl) as arrearUPTColl,"
                + "sum(PenaltyFinesTaxColl) as arrearPFTColl,sum(SewTaxColl) as arrearSTColl,"
                + "sum(VacantLandTaxColl) as arrearVLTColl,sum(PubSerChrgTaxColl) as arrearPSCTColl,"
                + "0 as curGT, 0 as curLC, 0 as curEC,0 as curUPT,0 as curPFT,0 as curST,"
                + "0 as curVLT,0 as curPSCT,0 as curGTColl,0 as curLCColl,0 as curECColl,0 as curUPTColl,"
                + "0 as curPFTColl,0 as curSTColl, 0 as curVLTColl,0 as curPSCTColl from (";

        // To retreive Current Demand and Collection Details
        current_innerCommonQry0 = "idc.* from egpt_mv_inst_dem_coll idc, egpt_mv_propertyinfo pi,  eg_installment_master im "
                + "where idc.id_basic_property=pi.basicpropertyid and im.id=idc.id_installment and pi.isactive = true "
                + "and im.start_date between (select STARTINGDATE from financialyear where now() between STARTINGDATE and ENDINGDATE) "
                + "and  (select ENDINGDATE from financialyear where now() between STARTINGDATE and ENDINGDATE)";

        current_innerCommonQry1 = "0 as arrearGT, 0 as arrearLC, 0 as arrearEC,0 as arrearUPT,0 as arrearPFT,0 as arrearST,"
                + "0 as arrearVLT,0 as arrearPSCT,0 as arrearGTColl,0 as arrearLCColl,0 as arrearECColl,0 as arrearUPTColl,"
                + "0 as arrearPFTColl,0 as arrearSTColl, 0 as arrearVLTColl,0 as arrearPSCTColl,"
                + "sum(GeneralTax) as curGT, sum(LibCessTax) as curLC, sum(EduCessTax) as curEC,"
                + "sum(UnauthPenaltyTax) as curUPT,sum(PenaltyFinesTax) as curPFT,sum(SewTax) as curST,"
                + "sum(VacantLandTax) as curVLT,sum(PubSerChrgTax) as curPSCT,sum(GeneralTaxColl) as curGTColl, "
                + "sum(LibCessTaxColl) as curLCColl, sum(EduCessTaxColl) as curECColl,sum(UnauthPenaltyTaxColl) as curUPTColl,"
                + "sum(PenaltyFinesTaxColl) as curPFTColl,sum(SewTaxColl) as curSTColl,"
                + "sum(VacantLandTaxColl) as curVLTColl,sum(PubSerChrgTaxColl) as curPSCTColl from (";

        // Final query that retreives both Arrear and Current details from the
        // other two inner queries
        finalCommonQry = "cast(sum(arrearGT) AS numeric) as \"dmnd_arrearPT\", cast(sum(arrearLC)  AS numeric) as \"dmnd_arrearLC\", cast(sum(arrearEC) AS numeric) as \"dmnd_arrearEC\","
                + "cast(sum(arrearUPT) AS numeric) as \"dmnd_arrearUPT\",cast(sum(arrearPFT) AS numeric) as \"dmnd_arrearPFT\",cast(sum(arrearST) AS numeric) as \"dmnd_arrearST\","
                + "cast(sum(arrearVLT) AS numeric) as \"dmnd_arrearVLT\",cast(sum(arrearPSCT) AS numeric) as \"dmnd_arrearPSCT\",cast(SUM(arrearGTColl) AS numeric)  AS \"clctn_arrearPT\", "
                + "cast(sum(arrearLCColl) AS numeric) as \"clctn_arrearLC\", cast(sum(arrearECColl) AS numeric) as \"clctn_arrearEC\",cast(sum(arrearUPTColl) AS numeric) as \"clctn_arrearUPT\","
                + "cast(sum(arrearPFTColl) AS numeric) as \"clctn_arrearPFT\",cast(sum(arrearSTColl) AS numeric) as \"clctn_arrearST\","
                + "cast(sum(arrearVLTColl) AS numeric) as \"clctn_arrearVLT\",cast(sum(arrearPSCTColl) AS numeric) as \"clctn_arrearPSCT\","
                + "cast(sum(curGT) AS numeric) as \"dmnd_currentPT\", cast(sum(curLC) AS numeric) as \"dmnd_currentLC\", cast(sum(curEC) AS numeric) as \"dmnd_currentEC\","
                + "cast(sum(curUPT) AS numeric) as \"dmnd_currentUPT\",cast(sum(curPFT) AS numeric) as \"dmnd_currentPFT\",cast(sum(curST) AS numeric) as \"dmnd_currentST\","
                + "cast(sum(curVLT) AS numeric) as \"dmnd_currentVLT\",CAST(sum(curPSCT) AS numeric) as \"dmnd_currentPSCT\",CAST(sum(curGTColl) AS numeric) as \"clctn_currentPT\", "
                + "cast(sum(curLCColl) AS numeric) as \"clctn_currentLC\", cast(sum(curECColl) AS numeric) as \"clctn_currentEC\",cast(sum(curUPTColl) AS numeric) as \"clctn_currentUPT\","
                + "cast(sum(curPFTColl) AS numeric) as \"clctn_currentPFT\",cast(sum(curSTColl) AS numeric) as \"clctn_currentST\","
                + "cast(sum(curVLTColl) AS numeric) as \"clctn_currentVLT\",cast(sum(curPSCTColl) AS numeric) as \"clctn_currentPSCT\" from (";

        // Conditions to Retrieve data based on selected boundary types
        if (!mode.equalsIgnoreCase(PROPERTY)) {
            finalSelectQry = "select cast(id as integer) as \"boundaryId\",boundary.name as \"boundaryName\", ";
            finalGrpQry = " group by boundary.id,boundary.name order by boundary.name";
            finalFrmQry = " )as dcbinfo,eg_boundary boundary ";
        }
       if (mode.equalsIgnoreCase(WARDWISE)) {
            innerSelectQry0 = "select distinct pi.wardid as ward,";
            innerSelectQry1 = "select ward as ward,";
            arrearGroupBy = ") as arrear group by ward ";
            collGroupBy = ") as collection  group by ward ";
            if (param != 0)
              whereQry = " and pi.WARDID = " + param;
            finalWhereQry = " where dcbinfo.ward=boundary.id ";
        } else if (mode.equalsIgnoreCase(BLOCKWISE)) {
            innerSelectQry0 = "select distinct pi.blockid as block,";
            innerSelectQry1 = "select block as block,";
            arrearGroupBy = ") as arrear group by block ";
            collGroupBy = ") as collection  group by block ";
            whereQry = " and pi.wardid = " + param;
            finalWhereQry = " where dcbinfo.block=boundary.id ";
        } else if (mode.equalsIgnoreCase(PROPERTY)) {
            innerSelectQry0 = "select distinct pi.upicno as upicno, pi.houseno as doorno,";
            innerSelectQry1 = "select upicno as upicno,doorno as doorno,";
            arrearGroupBy = ") as arrear group by upicno,doorno ";
            collGroupBy = ") as collection  group by upicno,doorno ";
            whereQry = " and pi.blockid = " + param;
            finalSelectQry = "select COALESCE(upicno,null,'',upicno) as \"assessmentNo\", doorno as \"houseNo\", ";
            finalFrmQry = " )as dcbinfo ";
            finalWhereQry = "";
            finalGrpQry = " group by dcbinfo.upicno,dcbinfo.doorno order by dcbinfo.upicno ";
        }
        // Arrear Demand query union Current Demand query
        unionQueryStr.append(innerSelectQry1).append(arrear_innerCommonQry1).append(innerSelectQry0)
                .append(arrear_innerCommonQry0).append(whereQry).append(arrearGroupBy).append(" union ")
                .append(innerSelectQry1).append(current_innerCommonQry1).append(innerSelectQry0)
                .append(current_innerCommonQry0).append(whereQry).append(collGroupBy);
        // Final Query : Retrieves arrear and current for the selected boundary.
        queryStr.append(finalSelectQry).append(finalCommonQry).append(unionQueryStr).append(finalFrmQry)
                .append(finalWhereQry).append(finalGrpQry);

        final SQLQuery query = persistenceService.getSession().createSQLQuery(queryStr.toString());
        return query;
    }

    /**
     * @param zoneId
     * @param wardId
     * @param areaId
     * @param localityId
     * @return
     */
    public List<PropertyMaterlizeView> prepareQueryforArrearRegisterReport(final Long zoneId, final Long wardId,
            final Long areaId, final Long localityId) {
        // Get current installment
        final Installment currentInst = getCurrentInstallment();
        final StringBuffer query = new StringBuffer(300);

        // Query that retrieves all the properties that has arrears.
        query.append("select distinct pmv from PropertyMaterlizeView pmv,InstDmdCollMaterializeView idc where "
                + "pmv.basicPropertyID = idc.propMatView.basicPropertyID and pmv.isActive = true and idc.installment.fromDate not between  ('"
                + currentInst.getFromDate() + "') and ('" + currentInst.getToDate() + "') ");

        if (localityId != null && localityId != -1)
            query.append(" and pmv.locality.id= :localityId ");
        if (zoneId != null && zoneId != -1)
            query.append(" and pmv.zone.id= :zoneId ");
        if (wardId != null && wardId != -1)
            query.append("  and pmv.ward.id= :wardId ");
        if (areaId != null && areaId != -1)
            query.append("  and pmv.block.id= :areaId ");

        query.append(" order by pmv.basicPropertyID ");
        final Query qry = persistenceService.getSession().createQuery(query.toString());
        
        if (localityId != null && localityId != -1)
            qry.setParameter("localityId", localityId);
        if (zoneId != null && zoneId != -1)
            qry.setParameter("zoneId", zoneId);
        if (wardId != null && wardId != -1)
            qry.setParameter("wardId", wardId);
        if (areaId != null && areaId != -1)
            qry.setParameter("areaId", areaId);
        final List<PropertyMaterlizeView> propertyViewList = qry.setResultTransformer(
                CriteriaSpecification.DISTINCT_ROOT_ENTITY).list();
        return propertyViewList;
    }

    /**
     * @param zoneId
     * @param wardId
     * @param areaId
     * @param fromDate
     * @param toDate
     * @return
     */
    public Query prepareQueryforTitleTransferReport(final Long zoneId, final Long wardId, final Long areaId,
            final String fromDate, final String toDate) {
        final StringBuffer query = new StringBuffer(300);
        new PropertyMutation();
        String boundaryCond = "";
        String boundaryWhrCond = "";

        if (zoneId != null && zoneId != -1)
            boundaryCond = " and pi.zone.id= " + zoneId;
        if (wardId != null && wardId != -1)
            boundaryCond = boundaryCond + " and pi.ward.id= " + wardId;
        if (areaId != null && areaId != -1)
            boundaryCond = boundaryCond + " and pi.area.id= " + areaId;
        if (boundaryCond != "")
            boundaryWhrCond = ",PropertyID pi where pm.basicProperty.id=pi.basicProperty.id "
                    + " and pm.state.value in ('" + PropertyTaxConstants.WF_STATE_COMMISSIONER_APPROVED + "','"
                    + PropertyTaxConstants.WF_STATE_CLOSED + "') ";
        else
            boundaryWhrCond = " where pm.state.value in ('" + PropertyTaxConstants.WF_STATE_COMMISSIONER_APPROVED
                    + "','" + PropertyTaxConstants.WF_STATE_CLOSED + "') ";
        // Query that retrieves all the properties that has Transfer of owners
        // details.
        query.append("select pm from PropertyMutation pm").append(boundaryWhrCond).append(boundaryCond);
        if (fromDate != null && !fromDate.isEmpty())
            if (toDate != null && !toDate.isEmpty())
                query.append(" and (cast(pm.createdDate as date)) between to_date('" + fromDate
                        + "', 'DD/MM/YYYY') and to_date('" + toDate + "','DD/MM/YYYY') ");
            else
                query.append(" and (cast(pm.createdDate as date)) between to_date('" + fromDate
                        + "', 'DD/MM/YYYY') and to_date('" + sdf.format(new Date()) + "','DD/MM/YYYY')  ");

        query.append(" order by pm.basicProperty.id,pm.mutationDate ");
        final Query qry = persistenceService.getSession().createQuery(query.toString());
        return qry;
    }

    /**
     * @ Description : gets the property tax arrear amount for a property
     * 
     * @param basicPropId
     * @param finyear
     * @return
     */
    public BigDecimal getPropertyTaxDetails(final Long basicPropId, final CFinancialYear finyear) {
        List<Object> list = new ArrayList<Object>();

        final String selectQuery = " select sum(amount) as amount from ("
                + " select distinct inst.description,dd.amount as amount from egpt_basic_property bp, egpt_property prop, "
                + " egpt_ptdemand ptd, eg_demand d, "
                + " eg_demand_details dd, eg_demand_reason dr, eg_demand_reason_master drm, eg_installment_master inst "
                + " where bp.id = prop.id_basic_property and prop.status = 'A' "
                + " and prop.id = ptd.id_property and ptd.id_demand = d.id " + " and d.id = dd.id_demand "
                + " and dd.id_demand_reason = dr.id and drm.id = dr.id_demand_reason_master "
                + " and dr.id_installment = inst.id and bp.id =:basicPropId " + " and inst.start_date between '"
                + finyear.getStartingDate() + "' and '" + finyear.getEndingDate() + "'" + " and drm.code = '"
                + PropertyTaxConstants.GEN_TAX + "') as genTaxDtls";
        final Query qry = persistenceService.getSession().createSQLQuery(selectQuery)
                .setLong("basicPropId", basicPropId);
        list = qry.list();
        return (null != list && !list.contains(null)) ? new BigDecimal((Double) list.get(0)) : null;
    }

    public Map<String, BigDecimal> prepareDemandDetForWorkflowProperty(final Property property,
            final Installment currentInstallment) {
        LOGGER.debug("Entered into prepareDemandDetForWorkflowProperty, property=" + property);

        Map<String, BigDecimal> DCBDetails = new HashMap<String, BigDecimal>();
        String demandReason = "";
        Installment installment = null;
        BigDecimal totalCurrentDemand = BigDecimal.ZERO;
        BigDecimal totalCurrentCollection = BigDecimal.ZERO;

        final List<String> demandReasonExcludeList = Arrays.asList(DEMANDRSN_CODE_PENALTY_FINES,
                PropertyTaxConstants.DEMANDRSN_CODE_ADVANCE);

        final String query = "select ptd from Ptdemand ptd " + "inner join fetch ptd.egDemandDetails dd "
                + "inner join fetch dd.egDemandReason dr " + "inner join fetch dr.egDemandReasonMaster drm "
                + "inner join fetch ptd.egptProperty p " + "inner join fetch p.basicProperty bp "
                + "where bp.active = true " + "and (p.status = 'W' or p.status = 'I' or p.status = 'A') "
                + "and p = :property " + "and ptd.egInstallmentMaster = :installment";

        final Ptdemand ptDemand = (Ptdemand) HibernateUtil.getCurrentSession().createQuery(query)
                .setEntity("property", property).setEntity("installment", currentInstallment).list().get(0);

        for (final EgDemandDetails dmdDet : ptDemand.getEgDemandDetails()) {

            demandReason = dmdDet.getEgDemandReason().getEgDemandReasonMaster().getCode();

            if (!demandReasonExcludeList.contains(demandReason)) {
                installment = dmdDet.getEgDemandReason().getEgInstallmentMaster();
                if (installment.equals(currentInstallment)) {
                    totalCurrentDemand = totalCurrentDemand.add(dmdDet.getAmount());
                    totalCurrentCollection = totalCurrentCollection.add(dmdDet.getAmtCollected());
                    DCBDetails.put(dmdDet.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster(),
                            dmdDet.getAmount());
                }
            }
        }
        LOGGER.debug("prepareDemandDetForWorkflowProperty - demands=" + DCBDetails);
        LOGGER.debug("Exiting from prepareDemandDetForWorkflowProperty");
        return DCBDetails;
    }

    public String getApproverUserName(final Long approvalPosition) {
        Assignment assignment = null;
        if (approvalPosition != null)
            assignment = assignmentService.getPrimaryAssignmentForPositon(approvalPosition);
        return assignment != null ? assignment.getEmployee().getName().concat("~").concat(assignment.getPosition().getName()) : "";
    }

    public boolean enableVacancyRemission(String upicNo) {
        boolean vrFlag = false;
        List<VacancyRemission> remissionList = persistenceService.findAllBy(
                "select vr from VacancyRemission vr where vr.basicProperty.upicNo=? order by id desc", upicNo);
        if (remissionList.isEmpty()) {
            vrFlag = true;
        } else {
            VacancyRemission vacancyRemission = remissionList.get(remissionList.size() - 1);
            if (vacancyRemission != null) {
                if (vacancyRemission.getStatus().equalsIgnoreCase(PropertyTaxConstants.VR_STATUS_APPROVED)) {
                    if (org.egov.infstr.utils.DateUtils.isSameDay(vacancyRemission.getVacancyToDate(), new Date())) {
                        vrFlag = true;
                    } else if (vacancyRemission.getVacancyToDate().compareTo(new Date()) < 0) {
                        vrFlag = true;
                    }
                } else if (vacancyRemission.getStatus().equalsIgnoreCase(
                        PropertyTaxConstants.VR_STATUS_REJECTION_ACK_GENERATED)) {
                    vrFlag = true;
                }
            }
        }
        return vrFlag;
    }

    public boolean enableMonthlyUpdate(String upicNo) {
        boolean monthlyUpdateFlag = false;
        VacancyRemission vacancyRemission = (VacancyRemission) persistenceService
                .find("select vr from VacancyRemission vr where vr.basicProperty.upicNo=? and vr.status = 'APPROVED'",
                        upicNo);
        if (vacancyRemission != null) {
            if (vacancyRemission.getVacancyRemissionDetails().isEmpty()) {
                monthlyUpdateFlag = true;
            } else {
                VacancyRemissionDetails vrd = vacancyRemission.getVacancyRemissionDetails().get(
                        vacancyRemission.getVacancyRemissionDetails().size() - 1);
                int noOfMonths = DateUtils.noOfMonths(vrd.getCheckinDate(), new Date());
                if (noOfMonths != 0) {
                    monthlyUpdateFlag = true;
                }
            }
        }
        return monthlyUpdateFlag;
    }

    public boolean enableVRApproval(String upicNo) {
        boolean vrApprovalFlag = false;
        VacancyRemission vacancyRemission = (VacancyRemission) persistenceService
                .find("select vr from VacancyRemission vr where vr.basicProperty.upicNo=? and vr.status = 'APPROVED'",
                        upicNo);
        if (vacancyRemission != null) {
            if (!vacancyRemission.getVacancyRemissionDetails().isEmpty()) {
                int detailsSize = vacancyRemission.getVacancyRemissionDetails().size();
                if (detailsSize % 5 == 0) {
                    vrApprovalFlag = true;
                }
            }
        }
        return vrApprovalFlag;
    }

    /**
     * @Description : checks if the parent property has any child which is in
     *              workflow
     * @param upicNo
     *            of the parent property
     * @return boolean
     */
    public boolean checkForParentUsedInBifurcation(String upicNo) {
        boolean isChildUnderWorkflow = false;
        PropertyStatusValues statusValues = (PropertyStatusValues) persistenceService
                .find("select psv from PropertyStatusValues psv where psv.referenceBasicProperty.upicNo=? and psv.basicProperty.underWorkflow = 't' ",
                        upicNo);
        if (statusValues != null) {
            isChildUnderWorkflow = true;
        }
        return isChildUnderWorkflow;
    }

    /**
     * Method to get lowest installment for property
     *
     * @param property
     * @return Lowest installment from date
     */
    public Date getLowestInstallmentForProperty(Property property) {
        final String query = "select demandDetails.egDemandReason from Ptdemand ptd,EgDemandDetails demandDetails where ptd.egptProperty = :property "
                + " and ptd.id = demandDetails.egDemand.id ";
        List<EgDemandReason> egDemandReason = persistenceService.getSession().createQuery(query.toString())
                .setEntity("property", property).list();
        return (null != egDemandReason && !egDemandReason.isEmpty()) ? egDemandReason.get(0)
                .getEgInstallmentMaster().getFromDate() : null;

    }

    /**
     * Method to check for Nagar Panchayats as Grade
     * @return boolean
     */
    public boolean checkIsNagarPanchayat() {
        String grade = (String) persistenceService.findAllBy("select grade from City").get(0);
        return PropertyTaxConstants.GRADE_NAGAR_PANCHAYAT.equalsIgnoreCase(grade);
    }
    
    /**
     * Prepare query for Defaulters report
     * @param wardId
     * @param fromDemand
     * @param toDemand
     * @param limit
     * @return
     */
    public Query prepareQueryforDefaultersReport(final Long wardId, final String fromDemand,
            final String toDemand, final Integer limit) {
        final StringBuffer query = new StringBuffer(300);
        query.append("select pmv from PropertyMaterlizeView pmv where pmv.propertyId is not null and pmv.isActive = true and pmv.isExempted=false ");
        String arrearBalanceCond = " ((pmv.aggrArrDmd - pmv.aggrArrColl) + (pmv.aggrCurrDmd - pmv.aggrCurrColl)) ";
        String arrearBalanceNotZeroCond = " and ((pmv.aggrArrDmd - pmv.aggrArrColl) + (pmv.aggrCurrDmd - pmv.aggrCurrColl))!=0 ";
        String orderByClause = " order by ";
        query.append(arrearBalanceNotZeroCond);
        if(StringUtils.isNotBlank(fromDemand) && StringUtils.isBlank(toDemand)){
            query.append(" and "+arrearBalanceCond+" >= ").append(fromDemand);
        } else if(StringUtils.isNotBlank(fromDemand) && StringUtils.isNotBlank(toDemand)){
                query.append(" and "+arrearBalanceCond+" >= ").append(fromDemand);
                query.append(" and "+arrearBalanceCond+" <= ").append(toDemand);
        }
        if(wardId != null && wardId != -1){
                query.append(" and pmv.ward.id = ").append(wardId); 
        }
        orderByClause = orderByClause.concat(arrearBalanceCond+" desc, pmv.ward.id asc ");
        query.append(orderByClause);

        final Query qry = persistenceService.getSession().createQuery(query.toString());
        if(limit != null && limit != -1)
                qry.setMaxResults(limit);
        return qry;
    }
    
    @SuppressWarnings("unchecked")
    public List<Installment> getInstallments(PropertyImpl property) {
        final EgDemand egDemand = ptDemandDAO.getNonHistoryCurrDmdForProperty(property);
        List<Installment> installments = (List<Installment>) persistenceService
                .findAllBy(
                        "select distinct(dd.egDemandReason.egInstallmentMaster) from EgDemandDetails dd where dd.egDemand = ? order by dd.egDemandReason.egInstallmentMaster.fromDate",
                        egDemand);
        return installments;
    }
}