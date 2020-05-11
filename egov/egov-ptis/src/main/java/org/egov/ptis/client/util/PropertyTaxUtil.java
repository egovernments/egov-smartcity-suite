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
package org.egov.ptis.client.util;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.egov.ptis.constants.PropertyTaxConstants.ADVANCE_COLLECTION_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.AMP_ACTUAL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.AMP_ENCODED_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.APPCONFIG_ISCORPORATION;
import static org.egov.ptis.constants.PropertyTaxConstants.APPCONFIG_ISSEASHORE_ULB;
import static org.egov.ptis.constants.PropertyTaxConstants.APPCONFIG_IS_PRIMARY_SERVICECHARGES_APPLICABLE;
import static org.egov.ptis.constants.PropertyTaxConstants.APPEAL_PETITION;
import static org.egov.ptis.constants.PropertyTaxConstants.APPURTENANT_PROPERTY;
import static org.egov.ptis.constants.PropertyTaxConstants.ARREARS;
import static org.egov.ptis.constants.PropertyTaxConstants.ARREARS_DMD;
import static org.egov.ptis.constants.PropertyTaxConstants.ARREAR_REBATE_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.BIGDECIMAL_100;
import static org.egov.ptis.constants.PropertyTaxConstants.CURRENTYEAR_FIRST_HALF;
import static org.egov.ptis.constants.PropertyTaxConstants.CURRENTYEAR_SECOND_HALF;
import static org.egov.ptis.constants.PropertyTaxConstants.CURRENT_DMD;
import static org.egov.ptis.constants.PropertyTaxConstants.CURRENT_REBATE_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_FIRSTHALF_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_FIRSTHALF_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_SECONDHALF_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_SECONDHALF_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_ADVANCE;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_EDUCATIONAL_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_GENERAL_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_LIBRARY_CESS;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_PENALTY_FINES;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_REBATE;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_UNAUTHORIZED_PENALTY;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMAND_REASONS_FOR_REBATE_CALCULATION;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMAND_REASON_ORDER_MAP;
import static org.egov.ptis.constants.PropertyTaxConstants.GENERAL_REVISION_PETETION_APPTYPE;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_CENTRAL_GOVT;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_COURT_CASE;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_PRIVATE;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_STATE_GOVT;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND;
import static org.egov.ptis.constants.PropertyTaxConstants.PENALTY_WATERTAX_EFFECTIVE_DATE;
import static org.egov.ptis.constants.PropertyTaxConstants.PROPERTY_MODIFY_REASON_DATA_ENTRY;
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_DEMANDREASONBY_CODE_AND_INSTALLMENTID;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_INSTALLMENTLISTBY_MODULE_AND_FINANCIALYYEAR_DESC;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_INSTALLMENTLISTBY_MODULE_AND_STARTYEAR;
import static org.egov.ptis.constants.PropertyTaxConstants.REVISION_PETETION;
import static org.egov.ptis.constants.PropertyTaxConstants.VACANCY_REMISSION;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.RandomStringGenerator;
import org.apache.log4j.Logger;
import org.egov.commons.CFinancialYear;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentHibDao;
import org.egov.commons.repository.CFinancialYearRepository;
import org.egov.demand.dao.DemandGenericHibDao;
import org.egov.demand.dao.DepreciationMasterDao;
import org.egov.demand.dao.EgBillDao;
import org.egov.demand.model.DepreciationMaster;
import org.egov.demand.model.EgBill;
import org.egov.demand.model.EgBillType;
import org.egov.demand.model.EgDemand;
import org.egov.demand.model.EgDemandDetails;
import org.egov.demand.model.EgDemandReason;
import org.egov.demand.model.EgdmCollectedReceipt;
import org.egov.eis.entity.Assignment;
import org.egov.eis.entity.Employee;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.EisCommonService;
import org.egov.eis.service.EmployeeService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.persistence.entity.Address;
import org.egov.infra.reporting.engine.ReportFormat;
import org.egov.infra.reporting.engine.ReportOutput;
import org.egov.infra.reporting.engine.ReportRequest;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.utils.DateUtils;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.instrument.InstrumentType;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.ptis.client.handler.TaxCalculationInfoXmlHandler;
import org.egov.ptis.client.model.PenaltyAndRebate;
import org.egov.ptis.client.model.calculator.APMiscellaneousTax;
import org.egov.ptis.client.model.calculator.APMiscellaneousTaxDetail;
import org.egov.ptis.client.model.calculator.APUnitTaxCalculationInfo;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.bill.PropertyTaxBillable;
import org.egov.ptis.domain.dao.demand.PtDemandDao;
import org.egov.ptis.domain.dao.property.BasicPropertyDAO;
import org.egov.ptis.domain.dao.property.PropertyDAO;
import org.egov.ptis.domain.entity.demand.PTDemandCalculations;
import org.egov.ptis.domain.entity.demand.Ptdemand;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyMutation;
import org.egov.ptis.domain.entity.property.PropertyOwnerInfo;
import org.egov.ptis.domain.entity.property.PropertyStatusValues;
import org.egov.ptis.domain.entity.property.PtApplicationType;
import org.egov.ptis.domain.entity.property.VacancyRemission;
import org.egov.ptis.domain.entity.property.VacancyRemissionDetails;
import org.egov.ptis.domain.model.calculator.MiscellaneousTax;
import org.egov.ptis.domain.model.calculator.MiscellaneousTaxDetail;
import org.egov.ptis.domain.model.calculator.TaxCalculationInfo;
import org.egov.ptis.domain.model.calculator.UnitTaxCalculationInfo;
import org.egov.ptis.domain.repository.vacancyremission.VacancyRemissionRepository;
import org.egov.ptis.domain.service.property.RebateService;
import org.egov.ptis.master.service.TaxRatesService;
import org.hibernate.query.Query;
import org.egov.ptis.service.utils.PropertyTaxCommonUtils;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
/**
 * @author malathi
 */
@Service
public class PropertyTaxUtil {
    private static final String UPIC_NO = "upicNo";
    private static final String DD_MM_YYYY = "dd/MM/yyyy";
    private static final String INSTALLMENT = "installment";
    private static final String PROPERTY = "property";
    private static final String SERVICE_TYPE = "serviceType";
    private static final String ACK_NO = "ackNo";
    private static final String CITY_NAME = "cityName";
    private static final String ULB_NAME = "ulbName";
    private static final String DUE_DATE = "dueDate";
    private static final String AS_ON_DATE = "asOnDate";
    private static final String ELECTION_WARD = "electionWard";
    private static final String APPLICATANT_NAME = "applicatantName";
    private static final String APPLICATANT_ADDR = "applicatantAddr";
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
    private InstallmentHibDao installmentDao;
    @Autowired
    private PtDemandDao ptDemandDAO;
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
    private ModuleService moduleDao;
    @Autowired
    @Qualifier("ptaxApplicationTypeService")
    private PersistenceService<PtApplicationType, Long> ptaxApplicationTypeService;
    @Autowired
    private ReportService reportService;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private RebateService rebateService;
    @Autowired
    private TaxRatesService taxRatesService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private VacancyRemissionRepository vacancyRemissionRepository;
    @Autowired
    private CFinancialYearRepository financialYearRepository;
    @Autowired
    private DepreciationMasterDao depreciationMasterDao;
    @Autowired
    private PropertyTaxCommonUtils propertyTaxCommonUtils;

    public void setPersistenceService(final PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    /**
     * This method retrieves the <code>CFinancialYear</code> for the given date.
     *
     * @param date an instance of <code>Date</code> for which the financial year is to be retrieved.
     * @return an instance of <code></code> representing the financial year for the given date
     */
    public CFinancialYear getFinancialYearforDate(final Date date) {
        return financialYearRepository.getFinancialYearByDate(date);
    }

    public List<Installment> getInstallmentListByStartDate(final Date startDate) {
        return persistenceService.findAllByNamedQuery(QUERY_INSTALLMENTLISTBY_MODULE_AND_STARTYEAR, startDate,
                startDate, PTMODULENAME);
    }

    public List<Installment> getInstallmentListByStartDateToCurrFinYearDesc(final Date startDate) {
        return persistenceService.findAllByNamedQuery(QUERY_INSTALLMENTLISTBY_MODULE_AND_FINANCIALYYEAR_DESC, PTMODULENAME,
                PTMODULENAME, startDate);
    }

    public EgDemandReason getDemandReasonByCodeAndInstallment(final String demandReasonCode,
            final Installment installment) {
        return (EgDemandReason) persistenceService.findByNamedQuery(QUERY_DEMANDREASONBY_CODE_AND_INSTALLMENTID,
                demandReasonCode, installment.getId());
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

    /**
     * Called locally to get sum of demand reasons by reason wise
     *
     * @param data a record with reason code, year, amount and amount collected
     * @param taxSum for reason wise sum
     * @return Map of reason wise sum
     */

    private Map<String, BigDecimal> populateReasonsSum(final Object[] data, final Map<String, BigDecimal> taxSum) {
        BigDecimal tmpVal;
        if (data[0].toString().equals(DEMANDRSN_CODE_GENERAL_TAX)) {
            tmpVal = taxSum.get(DEMANDRSN_CODE_GENERAL_TAX);
            taxSum.put(DEMANDRSN_CODE_GENERAL_TAX, tmpVal.add(((BigDecimal) data[2]).subtract((BigDecimal) data[3])));
        } else if (data[0].toString().equals(DEMANDRSN_CODE_LIBRARY_CESS)) {
            tmpVal = taxSum.get(DEMANDRSN_CODE_LIBRARY_CESS);
            taxSum.put(DEMANDRSN_CODE_LIBRARY_CESS, tmpVal.add(((BigDecimal) data[2]).subtract((BigDecimal) data[3])));
        } else if (data[0].toString().equals(DEMANDRSN_CODE_EDUCATIONAL_TAX)) {
            tmpVal = taxSum.get(DEMANDRSN_CODE_EDUCATIONAL_TAX);
            taxSum.put(DEMANDRSN_CODE_EDUCATIONAL_TAX,
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
        final Map<String, Map<String, BigDecimal>> demandDues = new HashMap<>();
        final BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(propertyId);
        final EgDemand egDemand = ptDemandDAO.getNonHistoryCurrDmdForProperty(basicProperty.getProperty());
        final Module module = moduleService.getModuleByName(PropertyTaxConstants.PTMODULENAME);
        final Installment currentInstall = installmentDao.getInsatllmentByModuleForGivenDate(module, new Date());
        List list = demandGenericDAO.getReasonWiseDCB(egDemand, module);
        Map<String, BigDecimal> arrTaxSum = new HashMap<>();
        Map<String, BigDecimal> currTaxSum = new HashMap<>();
        for (final Object record : list) {
            final Object[] data = (Object[]) record;
            if (data[1].toString().compareTo(currentInstall.toString()) < 0)
                arrTaxSum = populateReasonsSum(data, initReasonsMap());
            else
                currTaxSum = populateReasonsSum(data, initReasonsMap());
        }
        demandDues.put(ARREARS_DMD, arrTaxSum);
        demandDues.put(CURRENT_DMD, currTaxSum);
        return demandDues;
    }

    /**
     * Called locally to initialize
     *
     * @param taxSum Map of demand reasons
     * @return Map with demand reasons initialized
     */
    private Map<String, BigDecimal> initReasonsMap() {
        Map<String, BigDecimal> taxSum = new HashMap<>();
        taxSum.put(DEMANDRSN_CODE_GENERAL_TAX, BigDecimal.ZERO);
        taxSum.put(DEMANDRSN_CODE_LIBRARY_CESS, BigDecimal.ZERO);
        taxSum.put(DEMANDRSN_CODE_EDUCATIONAL_TAX, BigDecimal.ZERO);
        taxSum.put(DEMANDRSN_CODE_UNAUTHORIZED_PENALTY, BigDecimal.ZERO);
        taxSum.put(DEMANDRSN_CODE_PENALTY_FINES, BigDecimal.ZERO);
        taxSum.put(DEMANDRSN_CODE_CHQ_BOUNCE_PENALTY, BigDecimal.ZERO);
        return taxSum;
    }

    /**
     * @param user the user whose department is to be returned
     * @return department of the given user
     */
    private Department getDepartmentOfUser(final User user) {
        return getAssignment(user.getId()).getDepartment();
    }

    /**
     * @param Integer the userId
     * @return Assignment for current date and for <code> PersonalInformation </code>
     */
    private Assignment getAssignment(final Long userId) {
        final Employee empForUserId = employeeService.getEmployeeById(userId);
        return assignmentService.getPrimaryAssignmentForEmployeeByToDate(
                empForUserId.getId(), new Date());
    }

    public HashMap<String, Integer> generateOrderForDemandDetails(final Set<EgDemandDetails> demandDetails,
            final PropertyTaxBillable billable, final List<Installment> advanceInstallments) {

        final Map<Date, String> instReasonMap = new TreeMap<>();
        final HashMap<String, Integer> orderMap = new HashMap<>();
        BigDecimal balance;
        Date key = null;
        String reasonMasterCode = null;
        final Map<String, Installment> currYearInstMap = getInstallmentsForCurrYear(new Date());

        for (final EgDemandDetails demandDetail : demandDetails) {
            balance = demandDetail.getAmount().subtract(demandDetail.getAmtCollected());

            if (balance.compareTo(BigDecimal.ZERO) > 0) {
                final EgDemandReason reason = demandDetail.getEgDemandReason();
                final Installment installment = reason.getEgInstallmentMaster();
                final DateTime dateTime = new DateTime(installment.getInstallmentYear());
                reasonMasterCode = reason.getEgDemandReasonMaster().getCode();

                LOGGER.info(reasonMasterCode);
                key = getOrder(installment.getInstallmentYear(), DEMAND_REASON_ORDER_MAP.get(reasonMasterCode)
                        .intValue());
                instReasonMap.put(key, dateTime.getMonthOfYear() + "/" + dateTime.getYear() + "-" + reasonMasterCode);
            }
        }
        if (rebateService.isEarlyPayRebateActive(billable.getReceiptDate() != null ? billable.getReceiptDate() : new Date())) {
            final Installment currFirstHalf = currYearInstMap.get(CURRENTYEAR_FIRST_HALF);
            final DateTime dateTime = new DateTime(currFirstHalf.getInstallmentYear());
            key = getOrder(currFirstHalf.getInstallmentYear(), DEMAND_REASON_ORDER_MAP.get(DEMANDRSN_CODE_REBATE));
            instReasonMap.put(key, dateTime.getMonthOfYear() + "/" + dateTime.getYear() + "-" + DEMANDRSN_CODE_REBATE);
        }

        DateTime dateTime = null;
        for (final Installment inst : advanceInstallments) {
            dateTime = new DateTime(inst.getInstallmentYear());

            key = getOrder(inst.getInstallmentYear(), DEMAND_REASON_ORDER_MAP.get(DEMANDRSN_CODE_ADVANCE));

            instReasonMap.put(key, dateTime.getMonthOfYear() + "/" + dateTime.getYear() + "-"
                    + DEMANDRSN_CODE_ADVANCE);
        }

        BigDecimal penaltyAmount;
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
        final Map<String, Map<String, String>> installmentAndReason = new LinkedHashMap<>();

        for (final Map.Entry<Date, String> entry : instReasonMap.entrySet()) {
            final String[] split = entry.getValue().split("-");
            if (installmentAndReason.get(split[0]) == null) {
                final Map<String, String> reason = new HashMap<>();
                reason.put(split[1], entry.getValue());
                installmentAndReason.put(split[0], reason);
            } else
                installmentAndReason.get(split[0]).put(split[1], entry.getValue());
        }

        for (final String installmentYear : installmentAndReason.keySet())
            for (final String reasonCode : PropertyTaxConstants.ORDERED_DEMAND_RSNS_LIST)
                if (installmentAndReason.get(installmentYear).get(reasonCode) != null)
                    orderMap.put(installmentAndReason.get(installmentYear).get(reasonCode), order++);

        return orderMap;
    }

    /**
     * @param sessionMap map of session variables
     * @return departments of currently logged in user
     */
    public List<Department> getDepartmentsForLoggedInUser(final User user) {
        final Department dept = getDepartmentOfUser(user);
        return Arrays.asList(departmentService.getDepartmentByCode(dept.getCode()));
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
        return egBillDAO.getBillTypeByCode(typeCode);
    }

    public Date getOrder(final Date date, final int reasonOrder) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, reasonOrder);
        return calendar.getTime();
    }

    public UnitTaxCalculationInfo getUnitTaxCalculationInfoClone(final UnitTaxCalculationInfo unit) {
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
        return clone;
    }

    /**
     * Adds the MiscellaneousTaxe Clones to clone
     *
     * @param unit
     * @param clone
     */
    public void addMiscellaneousTaxesClone(final UnitTaxCalculationInfo unit, final UnitTaxCalculationInfo clone) {
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
    }

    /**
     * Returns the number of days between fromDate and toDate
     *
     * @param fromDate the date
     * @param toDate the date
     * @return Long the number of days
     */
    public static Long getNumberOfDays(final Date fromDate, final Date toDate) {
        LOGGER.debug("Entered into getNumberOfDays, fromDate=" + fromDate + ", toDate=" + toDate);
        final DateTime startDate = new DateTime(fromDate);
        final DateTime endDate = new DateTime(toDate);
        Integer days = Days.daysBetween(startDate, endDate).getDays();
        days = days < 0 ? 0 : days;
        return Long.valueOf(days.longValue());
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

    /**
     * This method returns the number of months between dates (inclusive of month).
     *
     * @param fromDate the from date
     * @param toDate the to date
     * @return
     */
    public static int getMonthsBetweenDates(final Date fromDate, final Date toDate) {
        final Calendar fromDateCalendar = Calendar.getInstance();
        final Calendar toDateCalendar = Calendar.getInstance();
        fromDateCalendar.setTime(fromDate);
        toDateCalendar.setTime(toDate);
        final int yearDiff = toDateCalendar.get(Calendar.YEAR) - fromDateCalendar.get(Calendar.YEAR);
        int noOfMonths = yearDiff * 12 + toDateCalendar.get(Calendar.MONTH) - fromDateCalendar.get(Calendar.MONTH);
        noOfMonths += 1;
        return noOfMonths;
    }

    /*
     * antisamy filter encodes '&' to '&amp;' and the decode is not happening so, manually replacing the text
     */
    public String antisamyHackReplace(final String str) {
        String replacedStr;
        replacedStr = str.replaceAll(AMP_ENCODED_STR, AMP_ACTUAL_STR);
        return replacedStr;
    }

    /**
     * Returns Map with below key-value pair CURR_DMD_STR - Current Installment demand ARR_DMD_STR - Current Installment
     * collection CURR_COLL_STR - Arrear Installment demand ARR_COLL_STR - Arrear Installment demand
     *
     * @param property
     * @return Map<String , BigDecimal>
     */
    public Map<String, BigDecimal> getDemandAndCollection(final Property property) {
        final Map<String, BigDecimal> demandCollMap = new HashMap<>();
        Installment installment = null;
        Integer instId = null;
        BigDecimal currDmd = BigDecimal.ZERO;
        BigDecimal arrDmd = BigDecimal.ZERO;
        BigDecimal currCollection = BigDecimal.ZERO;
        BigDecimal arrColelection = BigDecimal.ZERO;
        BigDecimal currentRebate = BigDecimal.ZERO;
        BigDecimal arrearRebate = BigDecimal.ZERO;
        String reason = "";
        final Ptdemand currDemand = ptDemandDAO.getNonHistoryCurrDmdForProperty(property);
        final List dmdCollList = propertyDAO.getDmdCollForAllDmdReasons(currDemand);
        BigDecimal advanceCollection = BigDecimal.ZERO;
        BigDecimal secondHalfTax = BigDecimal.ZERO;
        final Map<String, Installment> currYearInstallments = getInstallmentsForCurrYear(new Date());
        for (final Object object : dmdCollList) {
            final Object[] listObj = (Object[]) object;
            instId = Integer.valueOf(listObj[0].toString());
            installment = installmentDao.findById(instId, false);
            reason = listObj[5].toString();
            if (currDemand.getEgInstallmentMaster().equals(installment)) {
                if (listObj[2] != null && new BigDecimal((Double) listObj[2]).compareTo(BigDecimal.ZERO) != 0)
                    currCollection = currCollection.add(new BigDecimal(listObj[2].toString()));
                currentRebate = currentRebate.add(new BigDecimal(listObj[3].toString()));
                currDmd = currDmd.add(new BigDecimal(listObj[1].toString()));
            } else if (currYearInstallments.get(CURRENTYEAR_SECOND_HALF).equals(installment))
                secondHalfTax = secondHalfTax.add(new BigDecimal(listObj[1].toString()));
            else {
                arrDmd = arrDmd.add(new BigDecimal((Double) listObj[1]));
                if (listObj[2] != null && new BigDecimal((Double) listObj[2]).compareTo(BigDecimal.ZERO) != 0)
                    arrColelection = arrColelection.add(new BigDecimal(listObj[2].toString()));
                arrearRebate = arrearRebate.add(new BigDecimal(listObj[3].toString()));
            }
            if (reason.equalsIgnoreCase(DEMANDRSN_CODE_ADVANCE))
                advanceCollection = new BigDecimal(listObj[2].toString());
        }
        demandCollMap.put(CURR_DMD_STR, currDmd);
        demandCollMap.put(ARR_DMD_STR, arrDmd);
        demandCollMap.put(CURR_COLL_STR, currCollection);
        demandCollMap.put(ARR_COLL_STR, arrColelection);
        demandCollMap.put(CURRENT_REBATE_STR, currentRebate);
        demandCollMap.put(ARREAR_REBATE_STR, arrearRebate);
        demandCollMap.put(CURR_SECONDHALF_DMD_STR, secondHalfTax);
        demandCollMap.put(ADVANCE_COLLECTION_STR, advanceCollection);
        return demandCollMap;
    }

    public void makeTheEgBillAsHistory(final BasicProperty basicProperty) {
        EgBill egBill = null;
        try {
            egBill = (EgBill) entityManager.unwrap(Session.class)
                    .createQuery(
                            "from EgBill where module =:module and consumerId like :upicNo || '%' and is_history = 'N' and egBillType.code =: billType")
                    .setParameter("module", moduleService.getModuleByName(PropertyTaxConstants.PTMODULENAME))
                    .setParameter("upicNo", basicProperty.getUpicNo())
                    .setParameter("billType", PropertyTaxConstants.BILLTYPE_MANUAL).getSingleResult();
        } catch (Exception e) {
            LOGGER.error("No manual bill present for property");
        }
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
        final StringBuilder strAddress = new StringBuilder();
        strAddress.append(isNotBlank(address.getLandmark()) ? address.getLandmark() : " ").append("|");
        strAddress.append(isNotBlank(address.getHouseNoBldgApt()) ? address.getHouseNoBldgApt() : " ").append("|");
        final String tmpPin = address.getPinCode();
        strAddress.append(tmpPin != null && !tmpPin.isEmpty() ? tmpPin : " ").append("|");
        return strAddress.toString();
    }

    /**
     * Gives the Owner Address as string
     *
     * @param Set <Owner> Set of Property Owners
     * @return String
     */
    public static String getOwnerAddress(final List<PropertyOwnerInfo> ownerSet) {
        String ownerAddress = "";
        for (final PropertyOwnerInfo owner : ownerSet) {
            final List<Address> addresses = owner.getOwner().getAddress();
            for (final Address address : addresses)
                ownerAddress = address.toString();
        }
        return ownerAddress;
    }

    public String getDesignationName(final Long userId) {
        return getAssignment(userId).getDesignation().getName();
    }

    /**
     * @param Property
     * @return Date the occupancy date
     */
    public Date getPropertyOccupancyDate(final Property property) {
        return property.getPropertyDetail().getDateOfCompletion() == null ? property.getEffectiveDate()
                : property
                        .getPropertyDetail().getDateOfCompletion();
    }

    public Installment getPTInstallmentForDate(final Date date) {
        final Module module = moduleService.getModuleByName(PropertyTaxConstants.PTMODULENAME);
        return installmentDao.getInsatllmentByModuleForGivenDate(module, date);
    }

    /**
     * Gives the Earliest modification date
     *
     * @param propertyId
     * @return
     */
    public Date getEarliestModificationDate(final String propertyId) {
        final List result = entityManager
                .unwrap(Session.class)
                .createQuery(
                        "select to_char(min(pd.effective_date), 'dd/mm/yyyy') "
                                + "from PropertyImpl p inner join p.propertyDetail pd "
                                + "where p.basicProperty.active = true " + "and p.basicProperty.upicNo = :upicNo "
                                + "and (p.remarks is null or p.remarks <> :propertyMigrationRemarks) "
                                + "and pd.effective_date is not null")
                .setParameter(UPIC_NO, propertyId)
                .setParameter("propertyMigrationRemarks", PropertyTaxConstants.STR_MIGRATED_REMARKS).list();

        Date earliestModificationDate = null;
        if (result.isEmpty())
            return null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(DD_MM_YYYY);
        try {
            earliestModificationDate = dateFormat.parse((String) result.get(0));
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
        SimpleDateFormat dateFormat = new SimpleDateFormat(DD_MM_YYYY);
        try {
            waterTaxEffectiveDate = dateFormat.parse(PENALTY_WATERTAX_EFFECTIVE_DATE);
        } catch (final ParseException pe) {
            throw new ApplicationRuntimeException(
                    "Error while parsing Water Tax Effective Date for Penalty Calculation", pe);
        }
        return waterTaxEffectiveDate;
    }

    public Map<String, Map<Installment, BigDecimal>> prepareReasonWiseDenandAndCollection(final Property property,
            final Installment currentInstallment) {
        final Map<Installment, BigDecimal> installmentWiseDemand = new TreeMap<>();
        final Map<Installment, BigDecimal> installmentWiseCollection = new TreeMap<>();
        final Map<String, Map<Installment, BigDecimal>> demandAndCollection = new HashMap<>();

        String demandReason = "";
        Installment installment = null;

        final List<String> demandReasonExcludeList = Arrays
                .asList(DEMANDRSN_CODE_PENALTY_FINES, DEMANDRSN_CODE_ADVANCE);

        final String query = "select ptd from Ptdemand ptd " + "inner join fetch ptd.egDemandDetails dd "
                + "inner join fetch dd.egDemandReason dr " + "inner join fetch dr.egDemandReasonMaster drm "
                + "inner join fetch ptd.egptProperty p " + "inner join fetch p.basicProperty bp "
                + "where bp.active = true " + "and p.status in ('A', 'I') " + "and p = :property "
                + "and ptd.egInstallmentMaster = :installment";

        Query queryRes = entityManager.unwrap(Session.class).createQuery(query).setEntity(PROPERTY, property)
                .setEntity(INSTALLMENT, currentInstallment);

        if (!queryRes.list().isEmpty()) {
            Ptdemand ptDemand = (Ptdemand) queryRes.list().get(0);

            for (final EgDemandDetails dmdDet : ptDemand.getEgDemandDetails()) {

                demandReason = dmdDet.getEgDemandReason().getEgDemandReasonMaster().getCode();

                if (!demandReasonExcludeList.isEmpty() && !demandReasonExcludeList.contains(demandReason)) {
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
        }
        return demandAndCollection;
    }

    /*
     * Return the map with required demand and collection details required for view page.
     */
    @SuppressWarnings("unchecked")
    public Map<String, Map<String, BigDecimal>> prepareDemandDetForView(final Property property,
            final Installment currentInstallment) {
        final Map<String, Map<String, BigDecimal>> dCBDetails = new TreeMap<>();
        final Map<String, BigDecimal> firstHalfReasonDemandDetails = new HashMap<>();
        final Map<String, BigDecimal> secondHalfReasonDemandDetails = new HashMap<>();
        final Map<String, BigDecimal> arrearDemandDetails = new HashMap<>();
        String demandReason = "";
        Installment installment = null;
        BigDecimal totalArrearDemand = BigDecimal.ZERO;
        BigDecimal totalCurrentDemand = BigDecimal.ZERO;
        BigDecimal totalArrearCollection = BigDecimal.ZERO;
        BigDecimal totalCurrentCollection = BigDecimal.ZERO;
        BigDecimal totalNextInstCollection = BigDecimal.ZERO;
        BigDecimal totalNextInstDemand = BigDecimal.ZERO;
        final Map<String, Installment> currYearInstMap = getInstallmentsForCurrYear(new Date());

        final List<String> demandReasonExcludeList = Arrays
                .asList(DEMANDRSN_CODE_PENALTY_FINES, DEMANDRSN_CODE_ADVANCE);

        final String query = "select ptd from Ptdemand ptd " + "inner join fetch ptd.egDemandDetails dd "
                + "inner join fetch dd.egDemandReason dr " + "inner join fetch dr.egDemandReasonMaster drm "
                + "inner join fetch ptd.egptProperty p " + "inner join fetch p.basicProperty bp "
                + "where bp.active = true " + "and p.status in ('A', 'I', 'W') "
                + "and p = :property " + "and ptd.egInstallmentMaster = :installment";

        final List<Ptdemand> ptDemandList = entityManager.unwrap(Session.class).createQuery(query)
                .setParameter(PROPERTY, property).setParameter(INSTALLMENT, currentInstallment).list();
        if (!ptDemandList.isEmpty()) {
            final Ptdemand ptDemand = ptDemandList.get(0);
            for (final EgDemandDetails dmdDet : ptDemand.getEgDemandDetails()) {
                demandReason = dmdDet.getEgDemandReason().getEgDemandReasonMaster().getCode();
                if (!demandReasonExcludeList.contains(demandReason)) {
                    installment = dmdDet.getEgDemandReason().getEgInstallmentMaster();
                    if (installment.equals(currYearInstMap.get(CURRENTYEAR_FIRST_HALF))) {
                        totalCurrentDemand = totalCurrentDemand.add(dmdDet.getAmount());
                        totalCurrentCollection = totalCurrentCollection.add(dmdDet.getAmtCollected());
                        firstHalfReasonDemandDetails.put(dmdDet.getEgDemandReason().getEgDemandReasonMaster()
                                .getReasonMaster(), dmdDet.getAmount());
                    } else if (installment.equals(currYearInstMap.get(CURRENTYEAR_SECOND_HALF))) {
                        totalNextInstDemand = totalNextInstDemand.add(dmdDet.getAmount());
                        totalNextInstCollection = totalNextInstCollection.add(dmdDet.getAmtCollected());
                        secondHalfReasonDemandDetails.put(dmdDet.getEgDemandReason().getEgDemandReasonMaster()
                                .getReasonMaster(), dmdDet.getAmount());
                    } else {
                        totalArrearDemand = totalArrearDemand.add(dmdDet.getAmount());
                        totalArrearCollection = totalArrearCollection.add(dmdDet.getAmtCollected());
                    }
                }
            }
        }
        arrearDemandDetails.put(ARR_DMD_STR, totalArrearDemand);
        arrearDemandDetails.put(ARR_COLL_STR, totalArrearCollection);
        firstHalfReasonDemandDetails.put(CURR_FIRSTHALF_DMD_STR, totalCurrentDemand);
        firstHalfReasonDemandDetails.put(CURR_FIRSTHALF_COLL_STR, totalCurrentCollection);
        secondHalfReasonDemandDetails.put(CURR_SECONDHALF_DMD_STR, totalNextInstDemand);
        secondHalfReasonDemandDetails.put(CURR_SECONDHALF_COLL_STR, totalNextInstCollection);
        dCBDetails.put(CURRENTYEAR_FIRST_HALF, firstHalfReasonDemandDetails);
        dCBDetails.put(CURRENTYEAR_SECOND_HALF, secondHalfReasonDemandDetails);
        dCBDetails.put(ARREARS, arrearDemandDetails);
        return dCBDetails;
    }

    @SuppressWarnings("unchecked")
    public Map<Date, Property> getPropertiesForPenlatyCalculation(final BasicProperty basicProperty) {
        final String query = "select p from PropertyImpl p " + "inner join fetch p.basicProperty bp "
                + "where bp.upicNo = :upicNo and bp.active = true " + "and (p.remarks = null or p.remarks <> :remarks) "
                + "order by p.createdDate";
        final List<Property> allProperties = entityManager.unwrap(Session.class).createQuery(query)
                .setParameter(UPIC_NO, basicProperty.getUpicNo()).setParameter("remarks", PropertyTaxConstants.STR_MIGRATED_REMARKS)
                .list();
        new ArrayList<Property>();
        final List<String> mutationsCodes = Arrays.asList("NEW", "MODIFY");
        Property property = null;
        Property prevProperty = null;
        String mutationCode = null;
        String nextMutationCode = null;
        String prevMutationCode = null;
        final int noOfProperties = allProperties.size();
        final Map<Date, Property> propertyAndEffectiveDate = new TreeMap<>();
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
        final Map<Date, Property> propertyByOccupancyDate = new TreeMap<>();

        for (final Map.Entry<Date, Property> entry : propertyAndEffectiveDate.entrySet())
            if (entry.getKey() == null)
                propertyByOccupancyDate.put(getPropertyOccupancyDate(entry.getValue()), entry.getValue());
            else
                propertyByOccupancyDate.put(entry.getKey(), entry.getValue());

        return propertyByOccupancyDate;
    }

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
     * Returns true if the date is later than dateToCompare OR date is same as dateToCompare
     *
     * @param date
     * @param dateToCompare
     * @return true if date is after dateToCompare or date is equal to dateToCompare
     */
    public static boolean afterOrEqual(final Date date, final Date dateToCompare) {
        return date.after(dateToCompare) || date.equals(dateToCompare);
    }

    public DepreciationMaster getDepreciationByDate(final Date constructionDate, final Date effectiveDate) {
        String depreciationYear = null;
        final int years = DateUtils.noOfYearsBetween(constructionDate, effectiveDate);
        if (years >= 0 && years <= 25)
            depreciationYear = "0-25";
        else if (years > 25 && years <= 40)
            depreciationYear = "26-40";
        else
            depreciationYear = "Above 40";
        return depreciationMasterDao.findByName(depreciationYear);
    }

    public List<InstrumentType> prepareInstrumentTypeList() {
		return entityManager.unwrap(Session.class).createQuery("from InstrumentType order by type").getResultList();
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
        String roleName;
        final List<String> roleNameList = new ArrayList<>();
        final User user = userService.getUserById(userId);
        for (final Role role : user.getRoles()) {
            roleName = role.getName() != null ? role.getName() : "";
            roleNameList.add(roleName);
        }
        return roleNameList.toString().toUpperCase();
    }

    public String generateUserName(final String mobileNumber) {
        final StringBuilder userNameBuilder = new StringBuilder();
        String userName;
        if (mobileNumber.length() < 10)
            userName = String.format("%-10s", mobileNumber).replace(' ', '0');
        else
            userName = mobileNumber.substring(0, 10).replace(' ', '0');
        RandomStringGenerator generator = new RandomStringGenerator.Builder().withinRange('0', '9').build();
        userNameBuilder.append(userName).append(generator.generate(5));
        return userNameBuilder.toString();
    }

    /**
     * @param fromDate
     * @param toDate
     * @param collMode
     * @param transMode
     * @param mode
     * @param boundaryId (used in case of collection summary report other than usagewise)
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
        String baseQry = "";
        String orderbyQry = "";
        final String ZONEWISE = "zoneWise";
        final String WARDWISE = "wardWise";
        final String BLOCKWISE = "blockWise";
        final String LOCALITYWISE = "localityWise";
        final String USAGEWISE = "usageWise";
        new ArrayList<Object>();
        try {
            baseQry = "select cs from CollectionSummary cs where ";
            if (isNotBlank(fromDate) && !fromDate.equals("DD/MM/YYYY"))
                srchQryStr = "(cast(cs.receiptDate as date)) >= to_date('" + fromDate + "', 'DD/MM/YYYY') ";
            if (isNotBlank(toDate) && !toDate.equals("DD/MM/YYYY"))
                srchQryStr = srchQryStr + "and (cast(cs.receiptDate as date)) <= to_date('" + toDate
                        + "', 'DD/MM/YYYY') ";
            if (isNotBlank(collMode) && !collMode.equals("-1"))
                srchQryStr = srchQryStr + "and cs.collectionType ='" + collMode + "' ";
            if (isNotBlank(transMode) && !transMode.equals("-1"))
                srchQryStr = srchQryStr + "and (cs.paymentMode ='" + transMode + "' OR cs.paymentMode like '%' || '"
                        + transMode + "' || '%')";
            if (mode.equals(USAGEWISE)) {
                if (isNotBlank(propTypeCategoryId) && !propTypeCategoryId.equals("-1"))
                    srchQryStr = srchQryStr
                            + "and cs.property.propertyDetail in (select floor.propertyDetail from Floor floor where floor.propertyUsage = '"
                            + propTypeCategoryId + "')) ";
                if (zoneId != null && zoneId != -1)
                    srchQryStr = srchQryStr + " and cs.zoneId.id =" + zoneId;
                if (wardId != null && wardId != -1)
                    srchQryStr = srchQryStr + " and cs.wardId.id =" + wardId;
                if (blockId != null && blockId != -1)
                    srchQryStr = srchQryStr + " and cs.areaId.id =" + blockId;
                orderbyQry = "order by cs.property.propertyDetail.categoryType";
            }
            if (mode.equals(ZONEWISE)) {
                if (isNotBlank(boundaryId) && !boundaryId.equals("-1"))
                    srchQryStr = srchQryStr + "and cs.zoneId.id =" + boundaryId;
                orderbyQry = " and cs.zoneId.boundaryType.name = 'Zone' order by cs.zoneId.boundaryNum";
            } else if (mode.equals(WARDWISE)) {
                if (isNotBlank(boundaryId) && !boundaryId.equals("-1"))
                    srchQryStr = srchQryStr + "and cs.wardId.id =" + boundaryId;
                orderbyQry = " and cs.wardId.boundaryType.name = 'Ward' order by cs.wardId.boundaryNum";
            } else if (mode.equals(BLOCKWISE)) {
                if (isNotBlank(boundaryId) && !boundaryId.equals("-1"))
                    srchQryStr = srchQryStr + "and cs.areaId.id =" + boundaryId;
                orderbyQry = " and cs.areaId.boundaryType.name = 'Block' order by cs.areaId.boundaryNum";
            } else if (mode.equals(LOCALITYWISE)) {
                if (isNotBlank(boundaryId) && !boundaryId.equals("-1"))
                    srchQryStr = srchQryStr + "and cs.localityId.id =" + boundaryId;
                orderbyQry = "  order by cs.localityId.boundaryNum";
            }
            srchQryStr = baseQry + srchQryStr + orderbyQry;

        } catch (final Exception e) {
            throw new ApplicationRuntimeException(
                    "Error occured in Class : CollectionSummaryReportAction  Method : list " + e.getMessage());
        }
        return entityManager.unwrap(Session.class).createQuery(srchQryStr);
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
        final SimpleDateFormat sdf = new SimpleDateFormat(DD_MM_YYYY);
        final StringBuilder query = new StringBuilder();
        new PropertyMutation();
        String boundaryCond = "";
        String boundaryWhrCond = "";

        if (isWard(zoneId))
            boundaryCond = " and pi.zone.id= " + zoneId;
        if (isWard(wardId))
            boundaryCond = boundaryCond + " and pi.ward.id= " + wardId;
        if (isWard(areaId))
            boundaryCond = boundaryCond + " and pi.area.id= " + areaId;
        if (isNotBlank(boundaryCond))
            boundaryWhrCond = ",PropertyID pi where pm.basicProperty.id=pi.basicProperty.id "
                    + " and pm.state.value in ('" + PropertyTaxConstants.WF_STATE_COMMISSIONER_APPROVED + "','"
                    + PropertyTaxConstants.WF_STATE_CLOSED + "') ";
        else
            boundaryWhrCond = " where pm.state.value in ('" + PropertyTaxConstants.WF_STATE_COMMISSIONER_APPROVED
                    + "','" + PropertyTaxConstants.WF_STATE_CLOSED + "') ";
        query.append("select pm from PropertyMutation pm").append(boundaryWhrCond).append(boundaryCond);
        if (isNotBlank(fromDate))
            if (isNotBlank(toDate))
                query.append(" and (cast(pm.createdDate as date)) between to_date('" + fromDate
                        + "', 'DD/MM/YYYY') and to_date('" + toDate + "','DD/MM/YYYY') ");
            else
                query.append(" and (cast(pm.createdDate as date)) between to_date('" + fromDate
                        + "', 'DD/MM/YYYY') and to_date('" + sdf.format(new Date()) + "','DD/MM/YYYY')  ");

        query.append(" order by pm.basicProperty.id,pm.mutationDate ");
        return entityManager.unwrap(Session.class).createQuery(query.toString());
    }

    /**
     * @param basicPropId
     * @param finyear
     * @return @ Description : gets the property tax arrear amount for a property
     */
    @SuppressWarnings("unchecked")
    public BigDecimal getPropertyTaxDetails(final Long basicPropId, final CFinancialYear finyear) {
        List<Object> list;
        final String selectQuery = " select sum(amount) as amount from ("
                + " select distinct inst.description,dd.amount as amount from egpt_basic_property bp, egpt_property prop, "
                + " egpt_ptdemand ptd, eg_demand d, "
                + " eg_demand_details dd, eg_demand_reason dr, eg_demand_reason_master drm, eg_installment_master inst "
                + " where bp.id = prop.id_basic_property and prop.status = 'A' "
                + " and prop.id = ptd.id_property and ptd.id_demand = d.id " + " and d.id = dd.id_demand "
                + " and dd.id_demand_reason = dr.id and drm.id = dr.id_demand_reason_master "
                + " and dr.id_installment = inst.id and bp.id =:basicPropId " + " and inst.start_date between '"
                + finyear.getStartingDate() + "' and '" + finyear.getEndingDate() + "'"
                + " and drm.code in (:demandReasonList)) as genTaxDtls";
        final Query qry = entityManager.unwrap(Session.class).createNativeQuery(selectQuery)
                .setParameter("basicPropId", basicPropId);
        qry.setParameterList("demandReasonList", PropertyTaxConstants.NON_VACANT_TAX_DEMAND_CODES);
        list = qry.list();
        return null != list && !list.contains(null) ? new BigDecimal((Double) list.get(0)) : null;
    }

    public Map<String, BigDecimal> prepareDemandDetForWorkflowProperty(final Property property,
            final Installment dmdInstallment, final Installment dmdDetInstallment) {
        final Map<String, BigDecimal> dCBDetails = new HashMap<>();
        String demandReason = "";
        Installment installment = null;
        BigDecimal totalCurrentDemand = BigDecimal.ZERO;
        BigDecimal totalCurrentCollection = BigDecimal.ZERO;

        final List<String> demandReasonExcludeList = Arrays
                .asList(DEMANDRSN_CODE_PENALTY_FINES, DEMANDRSN_CODE_ADVANCE);

        final String query = "select ptd from Ptdemand ptd " + "inner join fetch ptd.egDemandDetails dd "
                + "inner join fetch dd.egDemandReason dr " + "inner join fetch dr.egDemandReasonMaster drm "
                + "inner join fetch ptd.egptProperty p " + "inner join fetch p.basicProperty bp "
                + "where bp.active = true " + "and p.status in ('A', 'I', 'W') "
                + "and p = :property " + "and ptd.egInstallmentMaster = :installment";

        List<Ptdemand> ptDemandList = entityManager.unwrap(Session.class).createQuery(query).setParameter("property", property)
                .setParameter("installment", dmdInstallment).list();
        Ptdemand ptDemand = new Ptdemand();
        if (!ptDemandList.isEmpty())
            ptDemand = ptDemandList.get(0);

        for (final EgDemandDetails dmdDet : ptDemand.getEgDemandDetails()) {

            demandReason = dmdDet.getEgDemandReason().getEgDemandReasonMaster().getCode();

            if (!demandReasonExcludeList.contains(demandReason)) {
                installment = dmdDet.getEgDemandReason().getEgInstallmentMaster();
                if (installment.equals(dmdDetInstallment)) {
                    totalCurrentDemand = totalCurrentDemand.add(dmdDet.getAmount());
                    totalCurrentCollection = totalCurrentCollection.add(dmdDet.getAmtCollected());
                    dCBDetails.put(dmdDet.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster(),
                            dmdDet.getAmount());
                }
            }
        }
        return dCBDetails;
    }

    public String getApproverUserName(final Long approvalPosition) {
        Position approverPosition = null;
        User approverUser = null;
        if (approvalPosition != null) {

            approverPosition = positionMasterService.getPositionById(approvalPosition);
            approverUser = eisCommonService.getUserForPosition(approvalPosition, new Date());
        }
        return approverUser != null ? approverUser.getName().concat("~")
                .concat(approverPosition.getName()) : "";
    }

    public boolean enableVacancyRemission(final String upicNo) {
        boolean vrFlag = false;
        final List<VacancyRemission> remissionList = vacancyRemissionRepository.getAllVacancyRemissionByUpicNo(upicNo);
        if (remissionList.isEmpty())
            vrFlag = true;
        else {
            final VacancyRemission vacancyRemission = remissionList.get(0);
            if (vacancyRemission != null)
                if (vacancyRemission.getStatus().equalsIgnoreCase(PropertyTaxConstants.VR_STATUS_APPROVED))
                    vrFlag = true;
                else if (vacancyRemission.getStatus().equalsIgnoreCase(
                        PropertyTaxConstants.VR_STATUS_REJECTION_ACK_GENERATED))
                    vrFlag = true;
        }
        return vrFlag;
    }

    public boolean enableMonthlyUpdate(final String upicNo) {
        boolean monthlyUpdateFlag = false;
        final VacancyRemission vacancyRemission = getLatestApprovedVR(upicNo);
        if (vacancyRemission != null)
            if (vacancyRemission.getVacancyRemissionDetails().isEmpty()
                    && DateUtils.noOfMonthsBetween(vacancyRemission.getVacancyFromDate(), new Date()) == 1)
                monthlyUpdateFlag = true;
            else if (!vacancyRemission.getVacancyRemissionDetails().isEmpty()) {
                final VacancyRemissionDetails vrd = vacancyRemission.getVacancyRemissionDetails().get(
                        vacancyRemission.getVacancyRemissionDetails().size() - 1);
                final int noOfMonths = DateUtils.noOfMonthsBetween(vrd.getCheckinDate(), new Date());
                final int detailsSize = vacancyRemission.getVacancyRemissionDetails().size();
                if (noOfMonths != 0)
                    monthlyUpdateFlag = true;
                if (detailsSize % 5 == 0 && vacancyRemission.getVacancyToDate().compareTo(new Date()) > 0)
                    monthlyUpdateFlag = false;
            }
        return monthlyUpdateFlag;
    }

    private VacancyRemission getLatestApprovedVR(final String upicNo) {
        final String query = "from VacancyRemission vr where vr.basicProperty.upicNo= :upicNo and vr.status = 'APPROVED' order by createdDate desc";
        final List<VacancyRemission> vacancyRemissions = entityManager.unwrap(Session.class).createQuery(query)
                .setParameter(UPIC_NO, upicNo).list();
        return vacancyRemissions != null && !vacancyRemissions.isEmpty() ? vacancyRemissions.get(0) : null;
    }

    public boolean enableVRApproval(final String upicNo) {
        boolean vrApprovalFlag = false;
        VacancyRemission vacancyRemission = getLatestApprovedVR(upicNo);
        if (vacancyRemission != null && !vacancyRemission.getVacancyRemissionDetails().isEmpty()) {
            final int detailsSize = vacancyRemission.getVacancyRemissionDetails().size();
            if (detailsSize % 6 == 0)
                vrApprovalFlag = true;
        }
        return vrApprovalFlag;
    }

    /**
     * @param upicNo of the parent property
     * @return boolean
     * @Description : checks if the parent property has any child which is in workflow
     */
    public boolean checkForParentUsedInBifurcation(final String upicNo) {
        boolean isChildUnderWorkflow = false;
		@SuppressWarnings("unchecked")
        final List<PropertyStatusValues> statusValues = (List<PropertyStatusValues>) entityManager.unwrap(Session.class)
				.createQuery(
						"select psv from PropertyStatusValues psv,PropertyImpl p where p.basicProperty.id=psv.basicProperty.id and psv.referenceBasicProperty.upicNo=:upicNo and p.propertyModifyReason='CREATE' and psv.basicProperty.underWorkflow = 't' and p.status='W' and (psv.remarks is null or psv.remarks !=:remarks)")
				.setParameter("upicNo", upicNo).setParameter("remarks", APPURTENANT_PROPERTY).getResultList();
        if (!statusValues.isEmpty())
            isChildUnderWorkflow = true;
        return isChildUnderWorkflow;
    }

    /**
     * Method to get lowest installment for property
     *
     * @param property
     * @return Lowest installment from date
     */
    public Date getLowestInstallmentForProperty(final Property property) {
        final String query = "select demandDetails.egDemandReason from Ptdemand ptd,EgDemandDetails demandDetails where ptd.egptProperty = :property "
                + " and ptd.id = demandDetails.egDemand.id order by demandDetails.egDemandReason.egInstallmentMaster.fromDate";
		final List<EgDemandReason> egDemandReason = entityManager.unwrap(Session.class).createQuery(query.toString())
				.setParameter(PROPERTY, property).getResultList();
        return null != egDemandReason && !egDemandReason.isEmpty() ? egDemandReason.get(0).getEgInstallmentMaster()
                .getFromDate() : null;

    }

    /**
     * Method to check for Nagar Panchayats as Grade
     *
     * @return boolean
     */
    public boolean checkIsNagarPanchayat() {
		final String grade = (String) entityManager.unwrap(Session.class).createQuery("select grade from City")
				.getResultList().get(0);
        return PropertyTaxConstants.GRADE_NAGAR_PANCHAYAT.equalsIgnoreCase(grade);
    }

    /**
     * Prepare query for Defaulters report
     *
     * @param wardId
     * @param fromDemand
     * @param toDemand
     * @param limit
     * @return
     */
    public Query prepareQueryforDefaultersReport(final Long wardId, final String fromDemand, final String toDemand,
            final Integer limit, final String ownerShipType, final String proptype) {
        final StringBuilder query = new StringBuilder(300);
        final Map<String, Object> params = new HashMap<>();
        query.append(
                "select pmv from PropertyMaterlizeView pmv where pmv.propertyId is not null and pmv.isActive = true and pmv.isExempted=false ");
        final String arrearBalanceCond = " ((pmv.aggrArrDmd - pmv.aggrArrColl) + ((pmv.aggrCurrFirstHalfDmd + pmv.aggrCurrSecondHalfDmd) - (pmv.aggrCurrFirstHalfColl + pmv.aggrCurrSecondHalfColl)) - pmv.waivedoffAmount) ";
        final String arrearBalanceNotZeroCond = " and ((pmv.aggrArrDmd - pmv.aggrArrColl) + ((pmv.aggrCurrFirstHalfDmd + pmv.aggrCurrSecondHalfDmd) - (pmv.aggrCurrFirstHalfColl + pmv.aggrCurrSecondHalfColl)) - pmv.waivedoffAmount)>0 ";
        String orderByClause = " order by ";
        final String and = " and ";
        final String ownerType = "ownerType";
        query.append(arrearBalanceNotZeroCond);
        appendFromToDmd(fromDemand, toDemand, query, params, arrearBalanceCond, and);
        if (isWard(wardId)) {
            query.append(" and pmv.ward.id = :wardId");
            params.put("wardId", wardId);
        }

        if (isOwnerShipType(ownerShipType)) {
            if (isOwnershipPrivate(ownerShipType)) {
                query.append(
                        " and (pmv.propTypeMstrID.code = :ownerType or pmv.propTypeMstrID.code = 'EWSHS') and pmv.isUnderCourtCase = false ");
                params.put(ownerType, ownerShipType);
            } else if (isOwnerShipTypeStateOrVacLand(ownerShipType)) {
                query.append(" and (pmv.propTypeMstrID.code = :ownerType) and  pmv.isUnderCourtCase = false ");
                params.put(ownerType, ownerShipType);
            } else if (isOwnerShipCentral(ownerShipType)) {
                query.append(" and (pmv.propTypeMstrID.code like :ownerType) and pmv.isUnderCourtCase = false ");
                params.put(ownerType, ownerShipType + "%");
            } else if (isOwnerShipCourtCase(ownerShipType))
                query.append(" and pmv.isUnderCourtCase = true ");
        } else if (isNonVacantLand(proptype)) {
            query.append(" and (pmv.propTypeMstrID.code <> :ownerType)");
            params.put(ownerType, OWNERSHIP_TYPE_VAC_LAND);
        }

        orderByClause = orderByClause.concat(arrearBalanceCond + " desc, pmv.ward.id asc ");
        query.append(orderByClause);

        final Query qry = entityManager.unwrap(Session.class).createQuery(query.toString());

        for (final Entry<String, Object> entry : params.entrySet())
            qry.setParameter(entry.getKey(), entry.getValue());
        if (limit != null && limit != -1)
            qry.setMaxResults(limit);
        return qry;
    }

    private void appendFromToDmd(final String fromDemand, final String toDemand, final StringBuilder query,
            final Map<String, Object> params, final String arrearBalanceCond, final String and) {
        if (isFromDemand(fromDemand, toDemand)) {
            query.append(and + arrearBalanceCond + " >= :fromDemand");
            params.put("fromDemand", BigDecimal.valueOf(Long.valueOf(isNotBlank(fromDemand) ? fromDemand : "0")));
        } else if (isFromDmdToDmd(fromDemand, toDemand)) {
            query.append(and + arrearBalanceCond + " >= :fromDemand");
            params.put("fromDemand", BigDecimal.valueOf(Long.valueOf(isNotBlank(fromDemand) ? fromDemand : "0")));
            query.append(and + arrearBalanceCond + " <= :toDemand");
            params.put("toDemand", BigDecimal.valueOf(Long.valueOf(isNotBlank(toDemand) ? toDemand : "0")));
        }
    }

    private boolean isOwnerShipCourtCase(final String ownerShipType) {
        return ownerShipType.equals(OWNERSHIP_TYPE_COURT_CASE);
    }

    private boolean isOwnerShipCentral(final String ownerShipType) {
        return ownerShipType.equals(OWNERSHIP_TYPE_CENTRAL_GOVT);
    }

    private boolean isOwnerShipTypeStateOrVacLand(final String ownerShipType) {
        return ownerShipType.equals(OWNERSHIP_TYPE_STATE_GOVT) || ownerShipType.equals(OWNERSHIP_TYPE_VAC_LAND);
    }

    private boolean isOwnershipPrivate(final String ownerShipType) {
        return ownerShipType.equals(OWNERSHIP_TYPE_PRIVATE);
    }

    private boolean isNonVacantLand(final String proptype) {
        return isOwnerShipType(proptype) && proptype.equals(PropertyTaxConstants.CATEGORY_TYPE_PROPERTY_TAX);
    }

    private boolean isOwnerShipType(final String ownerShipType) {
        return StringUtils.isNotBlank(ownerShipType);
    }

    public boolean isWard(final Long wardId) {
        return wardId != null && wardId != -1;
    }

    private boolean isFromDmdToDmd(final String fromDemand, final String toDemand) {
        return isOwnerShipType(fromDemand) && isOwnerShipType(toDemand);
    }

    private boolean isFromDemand(final String fromDemand, final String toDemand) {
        return isOwnerShipType(fromDemand) && StringUtils.isBlank(toDemand);
    }

    public List<Installment> getInstallments(final PropertyImpl property) {
        final EgDemand egDemand = ptDemandDAO.getNonHistoryCurrDmdForProperty(property);
        return entityManager.unwrap(Session.class).createQuery(
				"select distinct(dd.egDemandReason.egInstallmentMaster) from EgDemandDetails dd where dd.egDemand =:demand order by dd.egDemandReason.egInstallmentMaster.fromDate")
				.setParameter("demand", egDemand).getResultList();
    }

    public Map<String, Installment> getInstallmentsForCurrYear(final Date currDate) {
        final Map<String, Installment> currYearInstMap = new HashMap<>();
        final String query = "select installment from Installment installment,CFinancialYear finYear where installment.module.name = '"
                + PTMODULENAME
                + "'  and (cast(:currDate as date)) between finYear.startingDate and finYear.endingDate "
                + " and cast(installment.fromDate as date) >= cast(finYear.startingDate as date) and cast(installment.toDate as date) <= cast(finYear.endingDate as date) order by installment.id ";
        final Query qry = entityManager.unwrap(Session.class).createQuery(query);
        qry.setParameter("currDate", currDate);
        final List<Installment> installments = qry.list();
        currYearInstMap.put(CURRENTYEAR_FIRST_HALF, installments.get(0));
        currYearInstMap.put(CURRENTYEAR_SECOND_HALF, installments.get(1));
        return currYearInstMap;
    }

    public Date getEffectiveDateForProperty(Property property) {
        final Module module = moduleDao.getModuleByName(PTMODULENAME);
        final Date currInstToDate = installmentDao.getInsatllmentByModuleForGivenDate(module,
                property.getCreatedDate() == null ? new Date() : property.getCreatedDate())
                .getToDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currInstToDate);
        calendar.set(calendar.get(Calendar.YEAR) - 3, calendar.get(Calendar.MONTH) + 1, 1, 0, 0, 0);
        return calendar.getTime();
    }

    /**
     * Returns map containing tax amount for demand reasons other than Penalty and Advance
     *
     * @param property
     * @param effectiveInstallment
     * @param demandInstallment
     * @return Map<String , BigDecimal>
     */
    public Map<String, BigDecimal> getTaxDetailsForInstallment(final Property property, final Installment effectiveInstallment,
            final Installment demandInstallment) {
        final Map<String, BigDecimal> taxDetailsMap = new HashMap<>();
        final String query = "select ptd from Ptdemand ptd " + "inner join fetch ptd.egDemandDetails dd "
                + "inner join fetch dd.egDemandReason dr " + "inner join fetch dr.egDemandReasonMaster drm "
                + "inner join fetch ptd.egptProperty p " + "inner join fetch p.basicProperty bp "
                + "where bp.active = true " + "and p.status in ('A', 'I', 'W') "
                + "and p = :property " + "and ptd.egInstallmentMaster = :demandInstallment ";

        final Ptdemand ptDemand = (Ptdemand) entityManager.unwrap(Session.class).createQuery(query)
                .setParameter(PROPERTY, property).setParameter("demandInstallment", demandInstallment).list().get(0);

        for (final EgDemandDetails dmdDet : ptDemand.getEgDemandDetails())
            if (dmdDet.getInstallmentStartDate().equals(effectiveInstallment.getFromDate())
                    && !dmdDet.getEgDemandReason().getEgDemandReasonMaster().getCode()
                            .equalsIgnoreCase(DEMANDRSN_CODE_PENALTY_FINES)
                    && !dmdDet.getEgDemandReason().getEgDemandReasonMaster().getCode()
                            .equalsIgnoreCase(DEMANDRSN_CODE_ADVANCE))
                taxDetailsMap.put(dmdDet.getEgDemandReason().getEgDemandReasonMaster().getReasonMaster(),
                        dmdDet.getAmount());
        return taxDetailsMap;
    }

    /**
     * Returns a list of Installments for tax calculation, based on the effective date
     *
     * @param effectiveDate
     * @return List of Installments
     */
    public List<Installment> getInstallmentsListByEffectiveDate(final Date effectiveDate) {
        final Installment effectiveInstallment = getPTInstallmentForDate(effectiveDate);
        String query = "";
        List<Installment> installmentList = new ArrayList<>();
        final Map<String, Installment> installmentMap = getInstallmentsForCurrYear(new Date());
        final Installment installmentFirstHalf = installmentMap.get(PropertyTaxConstants.CURRENTYEAR_FIRST_HALF);
        final Installment installmentSecondHalf = installmentMap.get(PropertyTaxConstants.CURRENTYEAR_SECOND_HALF);

        /*
         * If effective date is before the current financial year, fetch all installments from the effective installment till the
         * 2nd half of current financial year.
         */
        if (!effectiveInstallment.equals(installmentFirstHalf) && !effectiveInstallment.equals(installmentSecondHalf)
                && effectiveDate.before(installmentFirstHalf.getFromDate())) {
            query = "select inst from Installment inst where inst.module.name = '" + PTMODULENAME
                    + "' and inst.fromDate between :startdate and :enddate order by inst.fromDate";
            installmentList = entityManager.unwrap(Session.class).createQuery(query)
                    .setParameter("startdate", effectiveInstallment.getFromDate())
                    .setParameter("enddate", installmentSecondHalf.getFromDate()).list();
        } else if (effectiveInstallment.equals(installmentFirstHalf)) {
            // If effective date is in 1st half of current financial year, fetch both installments
            installmentList.add(installmentFirstHalf);
            installmentList.add(installmentSecondHalf);
        } else if (effectiveInstallment.equals(installmentSecondHalf))
            // If effective date is in 2nd half of current financial year, fetch only 2nd half installment
            installmentList.add(installmentSecondHalf);
        else if (effectiveDate.after(installmentSecondHalf.getToDate())) {
            /*
             * This use case is applicable for Demolition done in 2nd half of current financial year. In such case, we must fetch
             * the 2 installments of the next financial year and calculate vacant land tax for them. Here, the effective date will
             * be the starting date of the next financial year
             */
            query = "select inst from Installment inst where inst.module.name = '"
                    + PTMODULENAME
                    + "' and exists (select inst2.finYearRange from Installment inst2 where inst.finYearRange = inst2.finYearRange "
                    + "and inst2.module.name = '" + PTMODULENAME + "' and inst2.fromDate = :startdate ) order by inst.fromDate";
            installmentList = entityManager.unwrap(Session.class).createQuery(query)
                    .setParameter("startdate", effectiveInstallment.getFromDate()).list();
        }
        return installmentList;
    }

    public int getNoOfYears(final Date fromDate, final Date toDate) {

        final DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        final int d1 = Integer.parseInt(formatter.format(fromDate));
        final int d2 = Integer.parseInt(formatter.format(toDate));
        return (d2 - d1) / 10000;
    }

    public ReportOutput generateCitizenCharterAcknowledgement(final String propertyId, final String applicationType,
            final String serviceType, final String applicationNo) {
        final SimpleDateFormat sdf = new SimpleDateFormat(DD_MM_YYYY);
        ReportRequest reportInput;
        Long resolutionTime;
        final Map<String, Object> reportParams = new HashMap<>();
        resolutionTime = ((PtApplicationType) entityManager.createNamedQuery(PtApplicationType.BY_CODE)
				.setParameter("code", applicationType).getSingleResult()).getResolutionTime();
        reportParams.put(DUE_DATE, sdf.format(DateUtils.add(new Date(), Calendar.DAY_OF_MONTH, resolutionTime.intValue())));
        final BasicProperty basicProperty = basicPropertyDAO.getBasicPropertyByPropertyID(propertyId);
        final StringBuilder queryString = new StringBuilder("from City");
        final City city = (City) entityManager.createQuery(queryString.toString()).getSingleResult();
        reportParams.put(APPLICATANT_ADDR, basicProperty.getAddress().toString());
        reportParams.put(APPLICATANT_NAME, basicProperty.getFullOwnerName());
        reportParams.put(ELECTION_WARD, basicProperty.getPropertyID().getElectionBoundary().getName());
        reportParams.put(AS_ON_DATE, sdf.format(new Date()));
        reportParams.put(ULB_NAME, city.getPreferences().getMunicipalityName());
        reportParams.put(CITY_NAME, city.getName());
        if (!Arrays.asList(REVISION_PETETION, VACANCY_REMISSION, GENERAL_REVISION_PETETION_APPTYPE, APPEAL_PETITION)
                .contains(applicationType))
            reportParams.put(ACK_NO, basicProperty.getWFProperty().getApplicationNo());
        else
            reportParams.put(ACK_NO, applicationNo);
        reportParams.put(SERVICE_TYPE, serviceType);
        reportInput = new ReportRequest("MainCitizenCharterAcknowledgement", reportParams, reportParams);
        reportInput.setReportFormat(ReportFormat.PDF);
        reportInput.setPrintDialogOnOpenReport(true);
        return reportService.createReport(reportInput);
    }

    public BigDecimal getRebateAmount(EgDemand currentDemand) {
        Object rebateAmt;
        Map<String, Installment> currInstallments = getInstallmentsForCurrYear(new Date());
        Installment currentFirstHalf = currInstallments.get(CURRENTYEAR_FIRST_HALF);

        final String selectQuery = " select sum(dd.amt_rebate) as rebateamount from eg_demand_details dd, eg_demand_reason dr,"
                + " eg_demand_reason_master drm, eg_installment_master inst "
                + " where dd.id_demand_reason = dr.id and drm.id = dr.id_demand_reason_master "
                + " and dr.id_installment = inst.id and dd.id_demand =:currentDemandId and inst.start_date between "
                + ":firstHlfFromdt and :firstHlfTodt and drm.code in (:codelist)";

        final Query qry = entityManager.unwrap(Session.class).createNativeQuery(selectQuery)
                .setParameter("currentDemandId", currentDemand.getId())
                .setParameter("firstHlfFromdt", currentFirstHalf.getFromDate())
                .setParameter("firstHlfTodt", currentFirstHalf.getToDate())
                .setParameterList("codelist", Arrays.asList(DEMANDRSN_CODE_GENERAL_TAX,
                        PropertyTaxConstants.DEMANDRSN_CODE_DRAINAGE_TAX, PropertyTaxConstants.DEMANDRSN_CODE_LIGHT_TAX,
                        PropertyTaxConstants.DEMANDRSN_CODE_SCAVENGE_TAX, PropertyTaxConstants.DEMANDRSN_CODE_WATER_TAX,
                        PropertyTaxConstants.DEMANDRSN_CODE_VACANT_TAX));
        rebateAmt = qry.uniqueResult();
        return rebateAmt != null ? new BigDecimal((Double) rebateAmt) : BigDecimal.ZERO;
    }

    public BigDecimal getCurrentDemandForRebateCalculation(BasicProperty basicProperty) {
        final EgDemand currentDemand = ptDemandDAO.getNonHistoryCurrDmdForProperty(basicProperty.getProperty());
        final Map<String, Installment> currInstallments = getInstallmentsForCurrYear(new Date());
        final Installment currentFirstHalf = currInstallments.get(CURRENTYEAR_FIRST_HALF);
        final Installment currentSecondHalf = currInstallments.get(CURRENTYEAR_SECOND_HALF);

        final String selectQuery = " select sum(dd.amount) amount from eg_demand_details dd, eg_demand_reason dr,"
                + " eg_demand_reason_master drm, eg_installment_master inst "
                + " where dd.id_demand_reason = dr.id and drm.id = dr.id_demand_reason_master "
                + " and dr.id_installment = inst.id and dd.id_demand =:currentDemandId and inst.id in (:installments) and drm.code in (:codes)";

        Object amount = Double.valueOf(0);
        if (currentDemand != null)
            amount = entityManager.unwrap(Session.class).createNativeQuery(selectQuery)
                    .setLong("currentDemandId", currentDemand.getId())
                    .setParameterList("installments", Arrays.asList(currentFirstHalf.getId(), currentSecondHalf.getId()))
                    .setParameterList("codes", DEMAND_REASONS_FOR_REBATE_CALCULATION).uniqueResult();
        return amount != null ? BigDecimal.valueOf((Double) amount) : BigDecimal.ZERO;
    }

    /**
     * API returns the percentage of tax difference between GIS survey tax and application tax Current second half taxes,
     * excluding UAC penalty will be considered for the comparison
     *
     * @param propertyImpl
     * @return BigDecimal
     */
    public BigDecimal getTaxDifferenceForGIS(PropertyImpl propertyImpl) {
        BigDecimal taxDiffPerc = BigDecimal.ZERO;
        BigDecimal gisHalfYrTax = BigDecimal.ZERO;
        BigDecimal applHalfYrTax = BigDecimal.ZERO;
        Map<String, Installment> currYearInstMap = getInstallmentsForCurrYear(new Date());
        PropertyImpl gisProperty = (PropertyImpl) propertyDAO
                .getLatestGISPropertyForBasicProperty(propertyImpl.getBasicProperty());
        if (gisProperty != null) {
            Ptdemand gisPtdemand = gisProperty.getPtDemandSet().iterator().next();
            if (gisPtdemand != null) {
                Map<String, Installment> gisPropYearInstMap = getInstallmentsForCurrYear(
                        gisPtdemand.getEgInstallmentMaster().getFromDate());
                for (EgDemandDetails demandDetails : gisPtdemand.getEgDemandDetails())
                    if (gisPropYearInstMap.get(CURRENTYEAR_SECOND_HALF).getFromDate()
                            .equals(demandDetails.getInstallmentStartDate())
                            &&
                            !PropertyTaxConstants.DEMANDRSN_CODE_UNAUTHORIZED_PENALTY
                                    .equalsIgnoreCase(demandDetails.getEgDemandReason().getEgDemandReasonMaster().getCode()))
                        gisHalfYrTax = gisHalfYrTax.add(demandDetails.getAmount());
            }
        }
        Ptdemand ptdemand = propertyImpl.getPtDemandSet().iterator().next();
        if (ptdemand != null)
            for (EgDemandDetails demandDetails : ptdemand.getEgDemandDetails())
                if (currYearInstMap.get(CURRENTYEAR_SECOND_HALF).getFromDate().equals(demandDetails.getInstallmentStartDate()) &&
                        !PropertyTaxConstants.DEMANDRSN_CODE_UNAUTHORIZED_PENALTY
                                .equalsIgnoreCase(demandDetails.getEgDemandReason().getEgDemandReasonMaster().getCode()))
                    applHalfYrTax = applHalfYrTax.add(demandDetails.getAmount());
        if (gisHalfYrTax.compareTo(BigDecimal.ZERO) > 0)
            taxDiffPerc = gisHalfYrTax.subtract(applHalfYrTax).multiply(BIGDECIMAL_100).divide(gisHalfYrTax,
                    BigDecimal.ROUND_HALF_UP);

        if (gisProperty != null)
            gisProperty.setSurveyVariance(taxDiffPerc);
        return taxDiffPerc;
    }

    public BigDecimal getTaxRates() {
        return taxRatesService.getTaxRateByDemandReasonCode(PropertyTaxConstants.DEMANDRSN_CODE_VACANT_TAX);
    }

    public boolean isEligibleforWaiver(Boolean paymentTypeFullPayment, String consumerCode) {
        Date cutOffDate = new Date();
        boolean hasReceiptAfterCutoffDate = false;
        boolean configPenaltyWaiverEnabled = isEligibleforWaiver(cutOffDate) && paymentTypeFullPayment.booleanValue();
        if (!configPenaltyWaiverEnabled)
            return configPenaltyWaiverEnabled;

        Ptdemand pd = ptDemandDAO
                .getNonHistoryCurrDmdForProperty(basicPropertyDAO.getBasicPropertyByPropertyID(consumerCode).getProperty());
        for (EgDemandDetails dd : pd.getEgDemandDetails()) {
            for (EgdmCollectedReceipt cr : dd.getEgdmCollectedReceipts())
                if (!cr.isCancelled() && cr.getReceiptDate().compareTo(cutOffDate) >= 0) {
                    hasReceiptAfterCutoffDate = true;
                    break;
                }
            if (hasReceiptAfterCutoffDate)
                break;
        }

        return paymentTypeFullPayment.booleanValue() && !hasReceiptAfterCutoffDate;
    }

    /**
     * If receipt is eligible for property tax waiver Based on AppConfigValue APPCONFIG_PENALTY_WAIVER_ENABLED is YES and current
     * date > APPCONFIG_PENALTY_WAIVER_CUTOFF_DATE
     */
    public boolean isEligibleforWaiver(Date outCutOffDate) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(DD_MM_YYYY);

        String penaltyWaiverEnabledConfig = propertyTaxCommonUtils.getAppConfigValue(
                PropertyTaxConstants.APPCONFIG_PENALTY_WAIVER_ENABLED,
                PropertyTaxConstants.PTMODULENAME).toUpperCase();
        String penaltyWaiverCutoffConfig = propertyTaxCommonUtils.getAppConfigValue(
                PropertyTaxConstants.APPCONFIG_PENALTY_WAIVER_CUTOFF_DATE,
                PropertyTaxConstants.PTMODULENAME);
        Date cutOffDate;
        Date today = new Date();
        try {
            cutOffDate = dateFormat.parse(penaltyWaiverCutoffConfig);
        } catch (ParseException e) {
            LOGGER.error(String.format("Invalid app config, %s=%s, %s=%s",
                    PropertyTaxConstants.APPCONFIG_PENALTY_WAIVER_ENABLED, penaltyWaiverEnabledConfig,
                    PropertyTaxConstants.APPCONFIG_PENALTY_WAIVER_CUTOFF_DATE, penaltyWaiverCutoffConfig));
            throw new ApplicationRuntimeException(String.format("Invalid app_config %s, value: %s, it must be of the form %s",
                    PropertyTaxConstants.APPCONFIG_PENALTY_WAIVER_CUTOFF_DATE, penaltyWaiverCutoffConfig, DD_MM_YYYY));
        }
        if (outCutOffDate != null)
            outCutOffDate.setTime(cutOffDate.getTime());

        boolean isEligible = penaltyWaiverEnabledConfig.equalsIgnoreCase(PropertyTaxConstants.STR_YES)
                && today.compareTo(cutOffDate) >= 0;
        return isEligible;
    }

}
