/*
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
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
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
 */
package org.egov.ptis.service.utils;

import static org.egov.collection.constants.CollectionConstants.QUERY_RECEIPTS_BY_RECEIPTNUM;
import static org.egov.ptis.constants.PropertyTaxConstants.ADDITIONAL_COMMISSIONER_DESIGN;
import static org.egov.ptis.constants.PropertyTaxConstants.APPCONFIG_DIGITAL_SIGNATURE;
import static org.egov.ptis.constants.PropertyTaxConstants.ARREARS;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ARR_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.ASSISTANT_COMMISSIONER_DESIGN;
import static org.egov.ptis.constants.PropertyTaxConstants.CITIZEN_ROLE;
import static org.egov.ptis.constants.PropertyTaxConstants.COMMISSIONER_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.CSC_OPERATOR_ROLE;
import static org.egov.ptis.constants.PropertyTaxConstants.CURRENTYEAR_FIRST_HALF;
import static org.egov.ptis.constants.PropertyTaxConstants.CURRENTYEAR_SECOND_HALF;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_FIRSTHALF_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_FIRSTHALF_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_SECONDHALF_COLL_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.CURR_SECONDHALF_DMD_STR;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_EDUCATIONAL_CESS;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_GENERAL_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_LIBRARY_CESS;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_PRIMARY_SERVICE_CHARGES;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_SEWERAGE_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_UNAUTHORIZED_PENALTY;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_CODE_VACANT_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_STR_EDUCATIONAL_CESS;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_STR_GENERAL_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_STR_LIBRARY_CESS;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_STR_UNAUTHORIZED_PENALTY;
import static org.egov.ptis.constants.PropertyTaxConstants.DEMANDRSN_STR_VACANT_TAX;
import static org.egov.ptis.constants.PropertyTaxConstants.DEPUTY_COMMISSIONER_DESIGN;
import static org.egov.ptis.constants.PropertyTaxConstants.MEESEVA_OPERATOR_ROLE;
import static org.egov.ptis.constants.PropertyTaxConstants.OWNERSHIP_TYPE_VAC_LAND;
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;
import static org.egov.ptis.constants.PropertyTaxConstants.REVENUE_OFFICER_DESGN;
import static org.egov.ptis.constants.PropertyTaxConstants.ZONAL_COMMISSIONER_DESIGN;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.persistence.entity.enums.GuardianRelation;
import org.egov.infra.persistence.entity.enums.UserType;
import org.egov.infra.utils.NumberUtil;
import org.egov.pims.commons.Position;
import org.egov.ptis.client.util.PropertyTaxUtil;
import org.egov.ptis.constants.PropertyTaxConstants;
import org.egov.ptis.domain.entity.objection.RevisionPetition;
import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.Property;
import org.egov.ptis.domain.entity.property.PropertyImpl;
import org.egov.ptis.domain.entity.property.PropertyMutation;
import org.egov.ptis.domain.entity.property.PropertyTypeMaster;
import org.egov.ptis.domain.entity.property.VacancyRemission;
import org.egov.ptis.domain.model.calculator.MiscellaneousTax;
import org.egov.ptis.domain.model.calculator.TaxCalculationInfo;
import org.egov.ptis.domain.model.calculator.UnitTaxCalculationInfo;
import org.egov.ptis.service.DemandBill.DemandBillService;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

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
    
    public Position getPositionForUser(final Long userId) {
        Position position = null;
        if (userId != null && userId.intValue() != 0) {
            position = positionMasterService.getPositionByUserId(userId);
        }
        return position;
    }
}