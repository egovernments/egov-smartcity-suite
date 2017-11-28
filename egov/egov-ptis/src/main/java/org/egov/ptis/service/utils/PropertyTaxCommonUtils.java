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
package org.egov.ptis.service.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.egov.collection.constants.CollectionConstants;
import org.egov.collection.entity.ReceiptHeader;
import org.egov.commons.CFinancialYear;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentDao;
import org.egov.commons.entity.Source;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.notification.service.NotificationService;
import org.egov.infra.persistence.entity.enums.GuardianRelation;
import org.egov.infra.persistence.entity.enums.UserType;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.utils.NumberUtil;
import org.egov.infra.workflow.entity.State;
import org.egov.pims.commons.Position;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.objection.RevisionPetition;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyID;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyMutation;
import org.egov.ptis.domain.entity.property.PropertyOwnerInfo;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.ptis.domain.entity.property.SurroundingsAudit;
import org.egov.ptis.domain.entity.property.VacancyRemission;
import org.egov.ptis.domain.model.calculator.MiscellaneousTax;
import org.egov.ptis.domain.model.calculator.TaxCalculationInfo;
import org.egov.ptis.domain.model.calculator.UnitTaxCalculationInfo;
import org.egov.ptis.notice.PtNotice;
import org.egov.ptis.service.DemandBill.DemandBillService;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.egov.collection.constants.CollectionConstants.QUERY_RECEIPTS_BY_RECEIPTNUM;
import static org.egov.ptis.constants.PropertyTaxConstants.*;

public class PropertyTaxCommonUtils {
    private static final Logger LOGGER = Logger.getLogger(PropertyTaxCommonUtils.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private AppConfigValueService appConfigValuesService;

    @Autowired
    private PropertyTaxUtil propertyTaxUtil;

    @Autowired
    private ApplicationContext beanProvider;

    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private InstallmentDao installmentDao;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private DesignationService designationService;

    @Autowired
    private DepartmentService departmentService;
        
    @Autowired
    private NotificationService notificationService;

    /**
     * Gives the first half of the current financial year
     *
     * @return Installment - the first half of the current financial year for PT module
     */
    public Installment getCurrentInstallment() {
        final Query query = getSession().createQuery(
                "select installment from Installment installment,CFinancialYear finYear where installment.module.name =:moduleName  and (cast(:currDate as date)) between finYear.startingDate and finYear.endingDate "
                        + " and cast(installment.fromDate as date) >= cast(finYear.startingDate as date) and cast(installment.toDate as date) <= cast(finYear.endingDate as date) order by installment.fromDate asc ");
        query.setString("moduleName", PropertyTaxConstants.PTMODULENAME);
        query.setDate("currDate", new Date());
        final List<Installment> installments = query.list();
        return installments.get(0);
    }

    /**
     * Returns AppConfig Value for given key and module.Key needs to exact as in the Database,otherwise empty string will send
     *
     * @param key - Key value for which AppConfig Value is required
     * @param moduleName - Value for the User Id
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
     * Fetches the tax details for workflow property
     * @param basicProperty
     * @return Map<String, Object>
     */
    public Map<String, Object> getTaxDetailsForWorkflowProperty(final BasicProperty basicProperty) {
        Map<String, Map<String, BigDecimal>> demandCollMap = new TreeMap<String, Map<String, BigDecimal>>();
        final Map<String, Object> wfPropTaxDetailsMap = new HashMap<String, Object>();
        final Property property = basicProperty.getWFProperty();
        try {
            demandCollMap = propertyTaxUtil.prepareDemandDetForView(property, getCurrentInstallment());
            if (!demandCollMap.isEmpty())
                for (final Entry<String, Map<String, BigDecimal>> entry : demandCollMap.entrySet()) {
                    final String key = entry.getKey();
                    final Map<String, BigDecimal> reasonDmd = entry.getValue();
                    if (key.equals(CURRENTYEAR_FIRST_HALF)) {
                        wfPropTaxDetailsMap.put("firstHalf", CURRENTYEAR_FIRST_HALF);
                        wfPropTaxDetailsMap.put("firstHalfGT",
                                reasonDmd.get(DEMANDRSN_STR_GENERAL_TAX) != null
                                        ? reasonDmd.get(DEMANDRSN_STR_GENERAL_TAX)
                                        : demandCollMap.get(DEMANDRSN_STR_VACANT_TAX));
                        wfPropTaxDetailsMap.put("firstHalfEC", reasonDmd.get(DEMANDRSN_STR_EDUCATIONAL_CESS) != null
                                ? reasonDmd.get(DEMANDRSN_STR_EDUCATIONAL_CESS) : BigDecimal.ZERO);
                        wfPropTaxDetailsMap.put("firstHalfLC", reasonDmd.get(DEMANDRSN_STR_LIBRARY_CESS));
                        wfPropTaxDetailsMap.put("firstHalfUAP",
                                reasonDmd.get(DEMANDRSN_STR_UNAUTHORIZED_PENALTY) != null
                                        ? reasonDmd.get(DEMANDRSN_STR_UNAUTHORIZED_PENALTY) : BigDecimal.ZERO);
                        wfPropTaxDetailsMap.put("firstHalfTotal", reasonDmd.get(CURR_FIRSTHALF_DMD_STR) != null
                                ? reasonDmd.get(CURR_FIRSTHALF_DMD_STR) : BigDecimal.ZERO);
                        wfPropTaxDetailsMap
                                .put("firstHalfTaxDue",
                                        (reasonDmd.get(CURR_FIRSTHALF_DMD_STR) != null
                                                ? reasonDmd.get(CURR_FIRSTHALF_DMD_STR) : BigDecimal.ZERO)
                                                .subtract(reasonDmd.get(CURR_FIRSTHALF_COLL_STR)));

                    } else if (key.equals(CURRENTYEAR_SECOND_HALF)) {
                        wfPropTaxDetailsMap.put("secondHalf", CURRENTYEAR_SECOND_HALF);
                        wfPropTaxDetailsMap.put("secondHalfGT",
                                reasonDmd.get(DEMANDRSN_STR_GENERAL_TAX) != null
                                        ? reasonDmd.get(DEMANDRSN_STR_GENERAL_TAX)
                                        : demandCollMap.get(DEMANDRSN_STR_VACANT_TAX));
                        wfPropTaxDetailsMap.put("secondHalfEC", reasonDmd.get(DEMANDRSN_STR_EDUCATIONAL_CESS) != null
                                ? reasonDmd.get(DEMANDRSN_STR_EDUCATIONAL_CESS) : BigDecimal.ZERO);
                        wfPropTaxDetailsMap.put("secondHalfLC", reasonDmd.get(DEMANDRSN_STR_LIBRARY_CESS));
                        wfPropTaxDetailsMap.put("secondHalfUAP",
                                reasonDmd.get(DEMANDRSN_STR_UNAUTHORIZED_PENALTY) != null
                                        ? reasonDmd.get(DEMANDRSN_STR_UNAUTHORIZED_PENALTY) : BigDecimal.ZERO);
                        wfPropTaxDetailsMap
                                .put("secondHalfTotal",
                                        reasonDmd.get(CURR_SECONDHALF_DMD_STR) != null
                                                ? reasonDmd.get(CURR_SECONDHALF_DMD_STR) : BigDecimal.ZERO);
                        wfPropTaxDetailsMap.put("secondHalfTaxDue",
                                (reasonDmd.get(CURR_SECONDHALF_DMD_STR) != null
                                        ? reasonDmd.get(CURR_SECONDHALF_DMD_STR) : BigDecimal.ZERO)
                                        .subtract(reasonDmd.get(CURR_SECONDHALF_COLL_STR)));

                    } else {
                        wfPropTaxDetailsMap.put("arrears", ARREARS);
                        wfPropTaxDetailsMap.put("arrearTax",
                                reasonDmd.get(ARR_DMD_STR) != null ? reasonDmd.get(ARR_DMD_STR) : BigDecimal.ZERO);
                        wfPropTaxDetailsMap.put("totalArrDue",
                                (reasonDmd.get(ARR_DMD_STR) != null ? reasonDmd.get(ARR_DMD_STR) : BigDecimal.ZERO)
                                        .subtract(reasonDmd.get(ARR_COLL_STR)));
                    }
                }
        } catch (final ParseException e) {
            LOGGER.error("Exception in getTaxDetailsForWorkflowProperty: ", e);
            throw new ApplicationRuntimeException("Exception in getTaxDetailsForWorkflowProperty : " + e);
        }
        return wfPropTaxDetailsMap;
    }

    public boolean isDigitalSignatureEnabled() {
        final List<AppConfigValues> appConfigValues = appConfigValuesService.getConfigValuesByModuleAndKey(PTMODULENAME,
                APPCONFIG_DIGITAL_SIGNATURE);
        return !appConfigValues.isEmpty() && "Y".equals(appConfigValues.get(0).getValue()) ? true : false;
    }
    
    public boolean isMuadIntegrationRequired() {
        final List<AppConfigValues> appConfigValues = appConfigValuesService.getConfigValuesByModuleAndKey(PTMODULENAME,
                APPCONFIG_MAUD_INTEGRATION_REQUIRED);
        return !appConfigValues.isEmpty() && "Y".equals(appConfigValues.get(0).getValue()) ? true : false;
    }

    /**
     * Fetches the list of installments for advance collections
     * @param startDate
     * @return List of Installment
     */
    public List<Installment> getAdvanceInstallmentsList(final Date startDate) {
        List<Installment> advanceInstallments = new ArrayList<Installment>();
        final String query = "select inst from Installment inst where inst.module.name = '" + PTMODULENAME
                + "' and inst.fromDate >= :startdate order by inst.fromDate asc ";
        advanceInstallments = getSession().createQuery(query)
                .setParameter("startdate", startDate)
                .setMaxResults(PropertyTaxConstants.MAX_ADVANCES_ALLOWED).list();
        return advanceInstallments;
    }

    /**
     * API to make the existing DemandBill inactive
     * @param assessmentNo
     */
    public void makeExistingDemandBillInactive(final String assessmentNo) {
        final DemandBillService demandBillService = (DemandBillService) beanProvider.getBean("demandBillService");
        demandBillService.makeDemandBillInactive(assessmentNo);
    }

    /**
     * Fetches a list of all the designations for the given userId
     *
     * @param userId
     * @return designations for the given userId
     */
    public String getAllDesignationsForUser(final Long userId) {
        List<Position> positions = null;
        final List<String> designationList = new ArrayList<String>();
        final StringBuilder listString = new StringBuilder();
        if (userId != null && userId.intValue() != 0) {
            positions = positionMasterService.getPositionsForEmployee(userId);
            if (positions != null) {
                for (final Position position : positions)
                    designationList.add(position.getDeptDesig().getDesignation().getName());

                for (final String s : designationList)
                    listString.append(s + ", ");
            }
        }

        return listString.toString();
    }

    /**
     * API to check if a receipt is cancelled or not
     * @param receiptNumber
     * @return boolean
     */
    public boolean isReceiptCanceled(final String receiptNumber) {
        final javax.persistence.Query qry = entityManager.createNamedQuery(QUERY_RECEIPTS_BY_RECEIPTNUM);
        qry.setParameter(1, receiptNumber);
        final ReceiptHeader receiptHeader = (ReceiptHeader) qry.getSingleResult();
        return receiptHeader.getStatus().getCode().equals(CollectionConstants.RECEIPT_STATUS_CODE_CANCELLED)
                ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * API to get the current installment period
     *
     * @return installment
     */
    public Installment getCurrentPeriodInstallment() {
        final Module module = moduleService.getModuleByName(PTMODULENAME);
        return installmentDao.getInsatllmentByModuleForGivenDate(module, new Date());
    }

    /**
     * API to get user assignment by passing user and position
     *
     * @return assignment
     */
    public Assignment getUserAssignmentByPassingPositionAndUser(final User user, final Position position) {

        Assignment userAssignment = null;

        if (user != null && position != null) {
            final List<Assignment> assignmentList = assignmentService.findByEmployeeAndGivenDate(user.getId(), new Date());
            for (final Assignment assignment : assignmentList)
                if (position.getId() == assignment.getPosition().getId() && assignment.getEmployee().isActive()) {
                    userAssignment = assignment;
                    break;
                }
        }
        return userAssignment;
    }

    /**
     * API to get workflow initiator assignment in Property Tax.
     *
     * @return assignment
     */
    public Assignment getWorkflowInitiatorAssignment(final Long userId) {
        Assignment wfInitiatorAssignment = null;
        if (userId != null) {
            final List<Assignment> assignmentList = assignmentService.getAllActiveEmployeeAssignmentsByEmpId(userId);
            for (final Assignment assignment : assignmentList)
                if ((assignment.getDesignation().getName().equals(PropertyTaxConstants.JUNIOR_ASSISTANT)
                        || assignment.getDesignation().getName().equals(PropertyTaxConstants.SENIOR_ASSISTANT))
                        && assignment.getEmployee().isActive()) {
                    wfInitiatorAssignment = assignment;
                    break;
                }
        }
        return wfInitiatorAssignment;
    }

    public String getCurrentHalfyearTax(final HashMap<Installment, TaxCalculationInfo> instTaxMap,
                                        final PropertyTypeMaster propTypeMstr) {
        final Installment currentInstall = getCurrentPeriodInstallment();
        final TaxCalculationInfo calculationInfo = instTaxMap.get(currentInstall);
        final BigDecimal annualValue = calculationInfo.getTotalNetARV();
        final BigDecimal totalPropertyTax = calculationInfo.getTotalTaxPayable();
        BigDecimal genTax = BigDecimal.ZERO;
        BigDecimal libCess = BigDecimal.ZERO;
        BigDecimal eduTax = BigDecimal.ZERO;
        BigDecimal unAuthPenalty = BigDecimal.ZERO;
        BigDecimal vacLandTax = BigDecimal.ZERO;
        BigDecimal sewrageTax = BigDecimal.ZERO;
        BigDecimal serviceCharges = BigDecimal.ZERO;
        for (final UnitTaxCalculationInfo unitTaxCalcInfo : calculationInfo.getUnitTaxCalculationInfos())
            for (final MiscellaneousTax miscTax : unitTaxCalcInfo.getMiscellaneousTaxes())
                if (miscTax.getTaxName() == DEMANDRSN_CODE_GENERAL_TAX)
                    genTax = genTax.add(miscTax.getTotalCalculatedTax());
                else if (miscTax.getTaxName() == DEMANDRSN_CODE_UNAUTHORIZED_PENALTY)
                    unAuthPenalty = unAuthPenalty.add(miscTax.getTotalCalculatedTax());
                else if (miscTax.getTaxName() == DEMANDRSN_CODE_EDUCATIONAL_CESS)
                    eduTax = eduTax.add(miscTax.getTotalCalculatedTax());
                else if (miscTax.getTaxName() == DEMANDRSN_CODE_VACANT_TAX)
                    vacLandTax = vacLandTax.add(miscTax.getTotalCalculatedTax());
                else if (miscTax.getTaxName() == DEMANDRSN_CODE_LIBRARY_CESS)
                    libCess = libCess.add(miscTax.getTotalCalculatedTax());
                else if (miscTax.getTaxName() == DEMANDRSN_CODE_SEWERAGE_TAX)
                    sewrageTax = sewrageTax.add(miscTax.getTotalCalculatedTax());
                else if (miscTax.getTaxName() == DEMANDRSN_CODE_PRIMARY_SERVICE_CHARGES)
                    serviceCharges = serviceCharges.add(miscTax.getTotalCalculatedTax());
        final String resultString = preparResponeString(propTypeMstr, annualValue, totalPropertyTax, vacLandTax, genTax,
                unAuthPenalty,
                eduTax, libCess, sewrageTax, serviceCharges);
        return resultString;
    }

    private String preparResponeString(final PropertyTypeMaster propTypeMstr, final BigDecimal annualValue,
                                       final BigDecimal totalPropertyTax,
                                       final BigDecimal vacLandTax, final BigDecimal genTax, final BigDecimal unAuthPenalty, final BigDecimal eduTax,
                                       final BigDecimal libCess,
                                       final BigDecimal sewrageTax, final BigDecimal serviceCharges) {
        final StringBuilder resultString = new StringBuilder(200);
        resultString.append(
                "Annual Rental Value=" + formatAmount(annualValue) + "~Total Tax=" + formatAmount(totalPropertyTax));
        if (OWNERSHIP_TYPE_VAC_LAND.equalsIgnoreCase(propTypeMstr.getCode()))
            resultString.append("~Vacant Land Tax=" + formatAmount(vacLandTax));
        else
            resultString.append("~Property Tax=" + formatAmount(genTax) + "~Education Cess=" + formatAmount(eduTax));
        resultString.append("~Library Cess=" + formatAmount(libCess));
        final Boolean isCorporation = propertyTaxUtil.isCorporation();
        if (isCorporation)
            resultString.append("~Sewrage Tax=" + formatAmount(sewrageTax));
        if (isCorporation && propertyTaxUtil.isPrimaryServiceApplicable())
            resultString.append("~Service Charges=" + formatAmount(serviceCharges));
        if (!OWNERSHIP_TYPE_VAC_LAND.equalsIgnoreCase(propTypeMstr.getCode()))
            resultString.append("~Unauthorized Penalty=" + formatAmount(unAuthPenalty));
        return resultString.toString();
    }

    public String formatAmount(BigDecimal tax) {
        tax = tax.setScale(0, RoundingMode.CEILING);
        return NumberUtil.formatNumber(tax);
    }

    /**
     * Returns the day of month along with suffix for the last digit (1st, 2nd, .. , 13th, .. , 23rd)
     */
    public String getDateWithSufix(final int dayOfMonth) {
        switch (dayOfMonth < 20 ? dayOfMonth : dayOfMonth % 10) {
            case 1:
                return dayOfMonth + "st";
            case 2:
                return dayOfMonth + "nd";
            case 3:
                return dayOfMonth + "rd";
            default:
                return dayOfMonth + "th";
        }
    }

    /**
     * Returns whether the ULB is Corporation or not
     *
     * @return boolean
     */
    public Boolean isCorporation() {
        final City city = (City) entityManager.createQuery("from City").getSingleResult();
        return city.getGrade().equals(PropertyTaxConstants.CITY_GRADE_CORPORATION) ? true : false;
    }

    /**
     * Returns whether the logged in User is eligible to initiate an application or not
     *
     * @return boolean
     */
    public Boolean isEligibleInitiator(final Long userId) {
        return getAllDesignationsForUser(userId).contains(PropertyTaxConstants.JUNIOR_ASSISTANT)
                || getAllDesignationsForUser(userId).contains(PropertyTaxConstants.SENIOR_ASSISTANT) ? true : false;

    }

    public String setSourceOfProperty(final User user, final Boolean isOnline) {
        String source;
        if (checkCscUserAndType(user))
            source = PropertyTaxConstants.SOURCE_CSC;
        else if (isMeesevaUser(user))
            source = PropertyTaxConstants.SOURCE_MEESEVA;
        else if (isCitizenPortalUser(user))
            source = Source.CITIZENPORTAL.toString();
        else if (isOnline)
            source = PropertyTaxConstants.SOURCE_ONLINE;
        else
            source = PropertyTaxConstants.SOURCE_SYSTEM;
        return source;
    }

    public Boolean checkCscUserAndType(final User user) {
        if (user.getType() != null)
            return isCscOperator(user) && UserType.BUSINESS.equals(user.getType());
        else
            return false;
    }

    public String getPropertySource(final PropertyImpl property) {
        return property.getSource() != null ? property.getSource() : null;
    }

    public String getMutationSource(final PropertyMutation propertyMutation) {
        return propertyMutation.getSource() != null ? propertyMutation.getSource() : null;
    }

    public String getObjectionSource(final RevisionPetition objection) {
        return objection.getSource() != null ? objection.getSource() : null;
    }

    public String getVRSource(final VacancyRemission vacancyRemission) {
        return vacancyRemission.getSource() != null ? vacancyRemission.getSource() : null;
    }

    public Boolean isCscOperator(final User user) {
        for (final Role role : user.getRoles())
            if (role != null && role.getName().equalsIgnoreCase(CSC_OPERATOR_ROLE))
                return true;
        return false;
    }

    public Boolean isMeesevaUser(final User user) {
        for (final Role role : user.getRoles())
            if (role != null && role.getName().equalsIgnoreCase(MEESEVA_OPERATOR_ROLE))
                return true;
        return false;
    }

    public Boolean isCitizenPortalUser(final User user) {
        for (final Role role : user.getRoles())
            if (role != null && role.getName().equalsIgnoreCase(CITIZEN_ROLE))
                return true;
        return false;
    }

    public String getDesgnForThirdPartyFullTransferWF() {
        final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(PTMODULENAME,
                PropertyTaxConstants.DESIGNATION_FOR_THIRDPARTY_FULLTRANSFER_WF);
        return null != appConfigValue ? appConfigValue.get(0).getValue() : null;

    }

    public String getDeptForThirdPartyFullTransferWF() {
        final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(PTMODULENAME,
                PropertyTaxConstants.DEPARTMENT_FOR_THIRDPARTY_FULLTRANSFER_WF);
        return null != appConfigValue ? appConfigValue.get(0).getValue() : null;

    }

    public Assignment getCommissionerAsgnForFullTransfer() {
        final String designationStr = getDesgnForThirdPartyFullTransferWF();
        final String departmentStr = getDeptForThirdPartyFullTransferWF();
        final String[] department = departmentStr.split(",");
        final String[] designation = designationStr.split(",");
        List<Assignment> assignment = new ArrayList<>();
        for (final String dept : department) {
            for (final String desg : designation) {
                final Long deptId = departmentService.getDepartmentByName(dept).getId();
                final Long desgId = designationService.getDesignationByName(desg).getId();
                assignment = assignmentService.findByDepartmentAndDesignation(deptId, desgId);
                if (!assignment.isEmpty())
                    break;
            }
            if (!assignment.isEmpty())
                break;
        }
        return !assignment.isEmpty() ? assignment.get(0) : null;
    }

    public Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    @SuppressWarnings("unchecked")
    public List<CFinancialYear> getAllFinancialYearsBetweenDates(final Date fromDate, final Date toDate) {
        final Query query = getSession().createQuery(
                " from CFinancialYear cfinancialyear where cfinancialyear.startingDate <:eDate and cfinancialyear.endingDate >=:sDate order by finYearRange asc ");
        query.setDate("sDate", fromDate);
        query.setDate("eDate", toDate);
        return query.list();
    }

    public boolean isRoOrCommissioner(final String designation) {
        return isCommissioner(designation) || isRevenueOfficer(designation);
    }

    public boolean isRevenueOfficer(final String designation) {
        return REVENUE_OFFICER_DESGN.equalsIgnoreCase(designation);
    }

    public boolean isCommissioner(final String designation) {
        return commissionerDesginations().contains(designation);
    }

    public List<String> commissionerDesginations() {
        final List<String> designations = new ArrayList<>();
        designations.add(ASSISTANT_COMMISSIONER_DESIGN);
        designations.add(ADDITIONAL_COMMISSIONER_DESIGN);
        designations.add(DEPUTY_COMMISSIONER_DESIGN);
        designations.add(COMMISSIONER_DESGN);
        designations.add(ZONAL_COMMISSIONER_DESIGN);
        return designations;
    }

    public List<String> getGuardianRelations() {
        return Stream.of(GuardianRelation.values()).map(GuardianRelation::name).collect(Collectors.toList());
    }
    
    public List<Long> getPositionForUser(final Long userId) {
        List<Long> positionIds = new ArrayList<>();
        if (userId != null && userId.intValue() != 0) {
            for(Position position : positionMasterService.getPositionsForEmployee(ApplicationThreadLocals.getUserId()))
                positionIds.add(position.getId());
        }
        return positionIds;
    }
    
    public void buildMailAndSMS(final Property property) {
        String transactionType;
        String modifyReason = property.getPropertyModifyReason();
        if (modifyReason.equalsIgnoreCase(TRANSACTION_TYPE_CREATE))
            transactionType = NATURE_NEW_ASSESSMENT;
        else if (modifyReason.equalsIgnoreCase(PROPERTY_MODIFY_REASON_ADD_OR_ALTER))
            transactionType = NATURE_ALTERATION;
        else if (modifyReason.equalsIgnoreCase(PROPERTY_MODIFY_REASON_BIFURCATE))
            transactionType = NATURE_BIFURCATION;
        else if (modifyReason.equalsIgnoreCase(APPLICATION_TYPE_DEMOLITION))
            transactionType = NATURE_DEMOLITION;
        else if (modifyReason.equalsIgnoreCase(PROPERTY_MODIFY_REASON_AMALG))
            transactionType = NATURE_AMALGAMATION;
        else
            transactionType = NATURE_TAX_EXEMPTION;
        for (final PropertyOwnerInfo ownerInfo : property.getBasicProperty().getPropertyOwnerInfo())
            if (StringUtils.isNotBlank(ownerInfo.getOwner().getMobileNumber()))
                buildSmsAndMail(property.getApplicationNo(), ownerInfo.getOwner(), transactionType);
    }

    public void buildMailAndSMS(final RevisionPetition revisionPetition) {
        for (final PropertyOwnerInfo ownerInfo : revisionPetition.getBasicProperty().getPropertyOwnerInfo())
            if (StringUtils.isNotBlank(ownerInfo.getOwner().getMobileNumber()))
                buildSmsAndMail(revisionPetition.getObjectionNumber(), ownerInfo.getOwner(),
                        revisionPetition.getProperty().getPropertyModifyReason());
    }

    public void buildMailAndSMS(final PropertyMutation propertyMutation) {
        for (final PropertyOwnerInfo ownerInfo : propertyMutation.getBasicProperty().getPropertyOwnerInfo())
            if (StringUtils.isNotBlank(ownerInfo.getOwner().getMobileNumber()))
                buildSmsAndMail(propertyMutation.getApplicationNo(), ownerInfo.getOwner(), NATURE_TITLE_TRANSFER);
    }

    public void buildMailAndSMS(final VacancyRemission vacancyRemission) {
        for (final PropertyOwnerInfo ownerInfo : vacancyRemission.getBasicProperty().getPropertyOwnerInfo())
            if (StringUtils.isNotBlank(ownerInfo.getOwner().getMobileNumber()))
                buildSmsAndMail(vacancyRemission.getApplicationNumber(), ownerInfo.getOwner(), NATURE_VACANCY_REMISSION);
    }

    private void buildSmsAndMail(final String applicationNumber, final User user, final String workFlowAction) {
        final String mobileNumber = user.getMobileNumber();
        String smsMsg = "";
        final String emailid = user.getEmailId();
        String emailSubject = "";
        String emailBody = "";
        String noticeNumber = getNoticeNumber(applicationNumber, workFlowAction);
        if (mobileNumber != null)
            smsMsg = "Your application " + applicationNumber + ", for " + workFlowAction
                    + " is approved. Download your digitally signed copy of Special Notice/ Proceedings from the below "
                    + ApplicationThreadLocals.getDomainURL() + "/ptis/reports/searchNotices-showNotice.action?noticeNumber="
                    + noticeNumber + "&moduleName=PTIS   or approach to Puraseva Center "
                    + ApplicationThreadLocals.getMunicipalityName() + " to obtain the same.";
        if (emailid != null) {
            emailSubject = workFlowAction + " application request with acknowledgement No: " + applicationNumber
                    + " is approved and digitally signed.";
            emailBody = "Dear " + user.getName() + ",\n\n" + "Your application " + applicationNumber + ", for " + workFlowAction
                    + " is approved. Download your digitally signed copy of Special Notice/ Proceedings from the below "
                    + ApplicationThreadLocals.getDomainURL() + "/ptis/reports/searchNotices-showNotice.action?noticeNumber="
                    + noticeNumber + "&moduleName=PTIS   or approach to Puraseva Center "
                    + ApplicationThreadLocals.getMunicipalityName() + " to obtain the same.\n\nThanks,\n"
                    + ApplicationThreadLocals.getMunicipalityName();
        }
        if (StringUtils.isNotBlank(emailid) && StringUtils.isNotBlank(emailSubject) && StringUtils.isNotBlank(emailBody))
            notificationService.sendEmail(emailid, emailSubject, emailBody);
        if (StringUtils.isNotBlank(mobileNumber) && StringUtils.isNotBlank(smsMsg))
            notificationService.sendSMS(mobileNumber, smsMsg);
    }

    public String getNoticeNumber(final String applicationNo, final String workFlowAction) {
        String noticeType = NOTICE_TYPE_SPECIAL_NOTICE;
        if (workFlowAction.equalsIgnoreCase(NATURE_OF_WORK_RP))
            noticeType = NOTICE_TYPE_RPPROCEEDINGS;
        else if (workFlowAction.equalsIgnoreCase(NATURE_OF_WORK_GRP))
            noticeType = NOTICE_TYPE_GRPPROCEEDINGS;
        else if (workFlowAction.equalsIgnoreCase(NATURE_TITLE_TRANSFER))
            noticeType = NOTICE_TYPE_MUTATION_CERTIFICATE;
        else if (workFlowAction.equalsIgnoreCase(NATURE_TAX_EXEMPTION))
            noticeType = NOTICE_TYPE_EXEMPTION;
        else if (workFlowAction.equalsIgnoreCase(NATURE_VACANCY_REMISSION))
            noticeType = NOTICE_TYPE_VRPROCEEDINGS;
        final javax.persistence.Query qry = entityManager
                .createQuery("from PtNotice notice where applicationNumber=? and noticeType=?");
        qry.setParameter(1, applicationNo);
        qry.setParameter(2, noticeType);
        PtNotice notice = (PtNotice) qry.getSingleResult();
        return notice.getNoticeNo();
    }

    @SuppressWarnings("unchecked")
    public List<PtNotice> getEndorsementNotices(final String applicationNo) {
        final javax.persistence.Query qry = entityManager
                .createQuery("from PtNotice notice where applicationNumber=? and noticeType='Endorsement Notice'");
        qry.setParameter(1, applicationNo);
        return (List<PtNotice>) qry.getResultList();
    }

    public Boolean getEndorsementGenerate(final Long userId, final State state) {
        String loggedInUserDesignation;
        final List<Assignment> loggedInUserAssign = assignmentService.getAssignmentByPositionAndUserAsOnDate(
                state.getOwnerPosition().getId(), userId,
                new Date());
        loggedInUserDesignation = !loggedInUserAssign.isEmpty()
                ? loggedInUserAssign.get(0).getDesignation().getName() : null;
        return !StringUtils.isBlank(loggedInUserDesignation) && isValidDesignationForEndorsement(loggedInUserDesignation, state)
                && isPrintPendingAction(state) && isEndorsementEnabled();
    }

    private Boolean isValidDesignationForEndorsement(String loggedInUserDesignation, State state) {
        return loggedInUserDesignation.equalsIgnoreCase(REVENUE_INSPECTOR_DESGN) ||
                ((loggedInUserDesignation.equalsIgnoreCase(JUNIOR_ASSISTANT) ||
                        loggedInUserDesignation.equalsIgnoreCase(SENIOR_ASSISTANT))
                        && (state.getOwnerPosition() != null));
    }

    private Boolean isPrintPendingAction(State state) {
        return !(state.getNextAction()).equalsIgnoreCase(WF_STATE_NOTICE_PRINT_PENDING) &&
                !(state.getNextAction()).equalsIgnoreCase(WFLOW_ACTION_STEP_PRINT_NOTICE);
    }
    
    public boolean isEndorsementEnabled() {
        final List<AppConfigValues> appConfigValues = appConfigValuesService.getConfigValuesByModuleAndKey(PTMODULENAME,
                PropertyTaxConstants.APPCONFIG_ENDORSEMENT);
        return !appConfigValues.isEmpty() && "Y".equals(appConfigValues.get(0).getValue()) ? true : false;
    }
    
    /**
     * Returns whether the logged in user is the current owner of the application
     *
     * @return boolean
     */
    public Boolean isOwnerOfApplication(PropertyImpl property, User user, Long approverPositionId) {
        return !positionMasterService.getPositionsForEmployee(user.getId())
                .contains(property.getCurrentState().getOwnerPosition()) ? Boolean.TRUE
                        : validateApproverPosition(approverPositionId,
                                property.getCurrentState().getOwnerPosition().getId());

    }

    /**
     * Returns whether the approver user position is same as the current owner position of the application
     *
     * @return boolean
     */
    public Boolean validateApproverPosition(Long approverPositionId, Long currentPosition) {

        if (null != approverPositionId && approverPositionId != -1 && currentPosition != null) {
            Boolean b = approverPositionId.longValue() == currentPosition.longValue();
            return b ? Boolean.TRUE : Boolean.FALSE;
        } else
            return Boolean.FALSE;
    }
    
    public List<String> validationForInactiveProperty(BasicProperty basicProperty) {
        String noOfDays;
        String reason = null;
        List<String> list = new ArrayList<>();
        if (isCorporation())
            noOfDays = "30";
        else
            noOfDays = "15";
        list.add(noOfDays);
        if (basicProperty.getProperty().getStatus() == 'I') {
            if ("CREATE".equals(basicProperty.getProperty().getPropertyModifyReason()))
                reason = "New Assessment";
            else if ("ADD_OR_ALTER".equals(basicProperty.getProperty().getPropertyModifyReason()))
                reason = "Addition/Alteration";
            list.add(reason);
            list.add(DateUtils.getFormattedDate(basicProperty.getModifiedDate(), "dd/MM/yyyy"));
        }
        return list;
    }
    
    public SurroundingsAudit setSurroundingDetails(final BasicProperty basicProperty) {
        SurroundingsAudit oldSurroundings = new SurroundingsAudit();
        PropertyID propertyId = basicProperty.getPropertyID();
        oldSurroundings.setBasicproperty(basicProperty.getId());
        oldSurroundings.setEastBoundary(propertyId.getEastBoundary() != null ? propertyId.getEastBoundary() : null);
        oldSurroundings.setNorthBoundary(propertyId.getNorthBoundary() != null ? propertyId.getNorthBoundary() : null);
        oldSurroundings.setSouthBoundary(propertyId.getSouthBoundary() != null ? propertyId.getSouthBoundary() : null);
        oldSurroundings.setWestBoundary(propertyId.getWestBoundary() != null ? propertyId.getWestBoundary() : null);
        return oldSurroundings;
    }

}