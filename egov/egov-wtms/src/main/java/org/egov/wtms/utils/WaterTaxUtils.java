/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2018  eGovernments Foundation
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
package org.egov.wtms.utils;

import org.apache.commons.lang3.StringUtils;
import org.egov.commons.EgwStatus;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentDao;
import org.egov.commons.entity.Source;
import org.egov.demand.model.EgDemand;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.notification.service.NotificationService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.infstr.services.PersistenceService;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.ptis.domain.entity.property.BasicPropertyImpl;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.enums.BasicPropertyStatus;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.egov.wtms.application.entity.WaterDemandConnection;
import org.egov.wtms.application.service.WaterConnectionDetailsService;
import org.egov.wtms.application.service.WaterDemandConnectionService;
import org.egov.wtms.masters.entity.enums.ConnectionStatus;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Collections.EMPTY_LIST;
import static org.egov.commons.entity.Source.CSC;
import static org.egov.commons.entity.Source.MEESEVA;
import static org.egov.commons.entity.Source.ONLINE;
import static org.egov.infra.config.core.ApplicationThreadLocals.getUserId;
import static org.egov.infra.utils.StringUtils.EMPTY;
import static org.egov.ptis.constants.PropertyTaxConstants.MEESEVA_OPERATOR_ROLE;
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_INSTALLMENTLISTBY_MODULE_AND_STARTYEAR;
import static org.egov.wtms.utils.constants.WaterTaxConstants.ADDITIONALCONNECTIONALLOWEDIFPTDUE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.ADDNLCONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPCONFIGVALUEOFENABLED;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_CLOSERDIGSIGNPENDING;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_CLOSERINPROGRESS;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_DIGITALSIGNPENDING;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_FEEPAID;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_RECONNCTIONINPROGRESS;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPLICATION_STATUS_RECONNDIGSIGNPENDING;
import static org.egov.wtms.utils.constants.WaterTaxConstants.APPROVEWORKFLOWACTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.BOUNDARY_TYPE_CITY;
import static org.egov.wtms.utils.constants.WaterTaxConstants.CHANGEOFUSE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.COMMISSIONER_DESGN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.CONNECTIONALLOWEDIFPTDUE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.DEPUTY_ENGINEER_DESIGN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.DOCUMENTREQUIREDFORBPL;
import static org.egov.wtms.utils.constants.WaterTaxConstants.EXECUTIVE_ENGINEER_DESIGN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.JUNIOR_OR_SENIOR_ASSISTANT_DESIGN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.MODULE_NAME;
import static org.egov.wtms.utils.constants.WaterTaxConstants.MULTIPLENEWCONNECTIONFORPID;
import static org.egov.wtms.utils.constants.WaterTaxConstants.MUNICIPAL_ENGINEER_DESIGN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.NEWCONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.NEWCONNECTIONALLOWEDIFPTDUE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.REASSIGNMENT;
import static org.egov.wtms.utils.constants.WaterTaxConstants.REGULARIZE_CONNECTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.ROLE_ADMIN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.ROLE_BANKCOLLECTOROPERATOR;
import static org.egov.wtms.utils.constants.WaterTaxConstants.ROLE_APPROVERROLE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.ROLE_CITIZEN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.ROLE_CSCOPERTAOR;
import static org.egov.wtms.utils.constants.WaterTaxConstants.ROLE_MEESEVA_OPERATOR;
import static org.egov.wtms.utils.constants.WaterTaxConstants.ROLE_PUBLIC;
import static org.egov.wtms.utils.constants.WaterTaxConstants.ROLE_SUPERUSER;
import static org.egov.wtms.utils.constants.WaterTaxConstants.ROLE_ULBOPERATOR;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SENDEMAILFORWATERTAX;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SENDSMSFORWATERTAX;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SIGNWORKFLOWACTION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SUPERIENTEND_ENGINEER_DESIGN;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SUPERINTENDING_ENGINEER_DESIGNATION;
import static org.egov.wtms.utils.constants.WaterTaxConstants.USERNAME_ANONYMOUS;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WATERTAXWORKFLOWDEPARTEMENT;
import static org.egov.wtms.utils.constants.WaterTaxConstants.WF_PREVIEW_BUTTON;
import static org.springframework.context.i18n.LocaleContextHolder.getLocale;

@Service
public class WaterTaxUtils {

    @Autowired
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;
    @Autowired
    private AppConfigValueService appConfigValuesService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private DesignationService designationService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private PropertyExtnUtils propertyExtnUtils;

    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource wcmsMessageSource;

    @Autowired
    private UserService userService;

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    @Autowired
    private WaterDemandConnectionService waterDemandConnectionService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private InstallmentDao installmentDao;

    public List<AppConfigValues> getAppConfigValueByModuleNameAndKeyName(String moduleName, String keyName) {
        return appConfigValuesService.getConfigValuesByModuleAndKey(moduleName, keyName);
    }

    public String loggedInUserDesignation(WaterConnectionDetails waterConnectionDetails) {
        String loggedInUserDesignation = EMPTY;
        User user = securityUtils.getCurrentUser();
        List<Assignment> loggedInUserAssign;
        if (waterConnectionDetails.getState() != null && waterConnectionDetails.getState().getOwnerPosition() != null) {
            loggedInUserAssign = assignmentService.getAssignmentByPositionAndUserAsOnDate(
                    waterConnectionDetails.getState().getOwnerPosition().getId(), user.getId(), new Date());
            loggedInUserDesignation = loggedInUserAssign.isEmpty()
                    ? EMPTY : loggedInUserAssign.get(0).getDesignation().getName();
        }
        return loggedInUserDesignation;
    }

    public String currentUserDesignation(Long ownerPosition) {
        String loggedInUserDesignation = EMPTY;
        List<Assignment> loggedInUserAssign;
        loggedInUserAssign = assignmentService.getAssignmentsForPosition(ownerPosition, new Date());
        if (!loggedInUserAssign.isEmpty())
            loggedInUserDesignation = loggedInUserAssign.get(0).getDesignation().getName();
        return loggedInUserDesignation;
    }

    public Boolean getCurrentUserRole() {
        User currentUser = getUserId() == null ? securityUtils.getCurrentUser() : userService.getUserById(getUserId());
        return compareUserRoleWithParameter(currentUser, ROLE_CSCOPERTAOR);


    }

    public Boolean isCSCoperator(User currentUser) {
        return currentUser == null ? false : compareUserRoleWithParameter(currentUser, ROLE_CSCOPERTAOR);

    }

    public Boolean isSmsEnabled() {
        return APPCONFIGVALUEOFENABLED.equalsIgnoreCase(getConfigurationValueByKey(SENDSMSFORWATERTAX));
    }

    public String getDepartmentForWorkFlow() {
        String department = EMPTY;
        List<AppConfigValues> appConfigValue = getAppConfigValueByModuleNameAndKeyName(MODULE_NAME, WATERTAXWORKFLOWDEPARTEMENT);
        if (appConfigValue != null && !appConfigValue.isEmpty())
            department = appConfigValue.get(0).getValue();
        return department;
    }

    public String getDesignationForThirdPartyUser() {
        String designation = EMPTY;
        List<AppConfigValues> appConfigValue = getAppConfigValueByModuleNameAndKeyName(
                MODULE_NAME, WaterTaxConstants.CLERKDESIGNATIONFORCSCOPERATOR);
        if (appConfigValue != null && !appConfigValue.isEmpty())
            designation = appConfigValue.get(0).getValue();
        return designation;
    }

    public List<AppConfigValues> getThirdPartyUserRoles() {

        List<AppConfigValues> appConfigValueList = getAppConfigValueByModuleNameAndKeyName(
                MODULE_NAME, WaterTaxConstants.ROLEFORNONEMPLOYEEINWATERTAX);

        return appConfigValueList.isEmpty() ? EMPTY_LIST : appConfigValueList;

    }

    /**
     * @return appconfigValues List for Keyname='ROLESFORLOGGEDINUSER'
     */
    public List<AppConfigValues> getUserRolesForLoggedInUser() {
        List<AppConfigValues> appConfigValueList = appConfigValuesService.getConfigValuesByModuleAndKeyByValueAsc(
                MODULE_NAME, WaterTaxConstants.ROLESFORLOGGEDINUSER);
        return appConfigValueList.isEmpty() ? EMPTY_LIST : appConfigValueList;
    }

    public boolean getAppconfigValueForSchedulearEnabled() {
        boolean schedularEnabled = false;
        AppConfigValues appConfigValueObj = appConfigValuesService.getConfigValuesByModuleAndKeyByValueAsc(
                MODULE_NAME, WaterTaxConstants.ENABLEDEMANEDBILLSCHEDULAR).get(0);
        if (appConfigValueObj != null && appConfigValueObj.getValue() != null
                && appConfigValueObj.getValue().equals(APPCONFIGVALUEOFENABLED))
            schedularEnabled = true;
        return schedularEnabled;

    }

    public Boolean getCurrentUserRole(User currentUser) {
        Boolean applicationByOthers = false;

        for (Role userrole : currentUser.getRoles())
            for (AppConfigValues appconfig : getThirdPartyUserRoles())
                if (userrole != null && userrole.getName().equals(appconfig.getValue())) {
                    applicationByOthers = true;
                    break;
                }

        return applicationByOthers;
    }

    /**
     * Checks whether user is an meeseva operator or not
     *
     * @param user
     * @return
     */
    public Boolean isMeesevaUser(User user) {
        return compareUserRoleWithParameter(user, MEESEVA_OPERATOR_ROLE);
    }

    public boolean isEmailEnabled() {
        return APPCONFIGVALUEOFENABLED.equalsIgnoreCase(getConfigurationValueByKey(SENDEMAILFORWATERTAX));
    }

    public boolean isNewConnectionAllowedIfPTDuePresent() {
        return APPCONFIGVALUEOFENABLED.equalsIgnoreCase(getConfigurationValueByKey(NEWCONNECTIONALLOWEDIFPTDUE));
    }

    public boolean isAdditionalConnectionAllowedIfPTDuePresent() {
        return APPCONFIGVALUEOFENABLED.equalsIgnoreCase(getConfigurationValueByKey(ADDITIONALCONNECTIONALLOWEDIFPTDUE));
    }

    public boolean isConnectionAllowedIfPTDuePresent() {
        return APPCONFIGVALUEOFENABLED.equalsIgnoreCase(getConfigurationValueByKey(CONNECTIONALLOWEDIFPTDUE));
    }

    public boolean isMultipleNewConnectionAllowedForPID() {
        return APPCONFIGVALUEOFENABLED.equalsIgnoreCase(getConfigurationValueByKey(MULTIPLENEWCONNECTIONFORPID));
    }

    public boolean isConnectionAllowedIfWTDuePresent(String connectionType) {
        List<AppConfigValues> appConfigValue = getAppConfigValueByModuleNameAndKeyName(MODULE_NAME, connectionType);
        return appConfigValue.isEmpty()
                ? false : APPCONFIGVALUEOFENABLED.equalsIgnoreCase(appConfigValue.get(0).getValue());
    }

    public String documentRequiredForBPLCategory() {
        List<AppConfigValues> appConfigValue = getAppConfigValueByModuleNameAndKeyName(MODULE_NAME, DOCUMENTREQUIREDFORBPL);
        return appConfigValue.isEmpty() ? EMPTY : appConfigValue.get(0).getValue();
    }

    public String getMunicipalityName() {
        return ApplicationThreadLocals.getMunicipalityName();
    }

    public String getCityCode() {
        return ApplicationThreadLocals.getCityCode();
    }

    public String smsAndEmailBodyByCodeAndArgsForRejection(String code, String approvalComment,
                                                           String applicantName) {
        return wcmsMessageSource.getMessage(code,
                new String[]{applicantName, approvalComment, getMunicipalityName()}, getLocale());
    }

    public String emailBodyforApprovalEmailByCodeAndArgs(String code, WaterConnectionDetails waterConnectionDetails,
                                                         String applicantName) {
        return wcmsMessageSource.getMessage(code,
                new String[]{applicantName, waterConnectionDetails.getApplicationNumber(),
                        waterConnectionDetails.getConnection().getConsumerCode(), getMunicipalityName()},
                getLocale());
    }

    public String emailSubjectforEmailByCodeAndArgs(String code, String applicationNumber) {
        return wcmsMessageSource.getMessage(code, new String[]{applicationNumber}, getLocale());
    }

    public void sendSMSOnWaterConnection(String mobileNumber, String smsBody) {
        notificationService.sendSMS(mobileNumber, smsBody);
    }

    public void sendEmailOnWaterConnection(String email, String emailBody, String emailSubject) {
        notificationService.sendEmail(email, emailSubject, emailBody);
    }

    public Position getCityLevelCommissionerPosition(String commissionerDesgn, String assessmentNumber) {
        Designation desgnObj = designationService.getDesignationByName(commissionerDesgn);
        if ("Commissioner".equals(commissionerDesgn)) {
            Department deptObj = departmentService.getDepartmentByName(WaterTaxConstants.ROLE_COMMISSIONERDEPARTEMNT);
            List<Assignment> assignlist = assignmentService.getAssignmentsByDeptDesigAndDates(
                    deptObj.getId(), desgnObj.getId(), new Date(), new Date());
            if (assignlist.isEmpty())
                assignlist = assignmentService.getAllPositionsByDepartmentAndDesignationForGivenRange(null,
                        desgnObj.getId(), new Date());
            if (assignlist.isEmpty())
                assignlist = assignmentService.getAllActiveAssignments(desgnObj.getId());

            return assignlist.get(0).getPosition();
        } else
            return getZonalLevelClerkForLoggedInUser(assessmentNumber);
    }

    public String getApproverUserName(Long approvalPosition) {
        Assignment assignment = null;
        if (approvalPosition != null)
            assignment = assignmentService.getPrimaryAssignmentForPositionAndDate(approvalPosition, new Date());
        return assignment == null ? EMPTY : assignment.getEmployee().getUsername();
    }

    public String getApproverName(Long approvalPosition) {
        Assignment assignment = null;
        if (approvalPosition != null)
            assignment = assignmentService.getPrimaryAssignmentForPositionAndDate(approvalPosition, new Date());
        List<Assignment> asignList = new ArrayList<>();
        if (assignment != null) {
            asignList.add(assignment);
        } else
            asignList = assignmentService.getAssignmentsForPosition(approvalPosition, new Date());
        return asignList.isEmpty() ? EMPTY : asignList.get(0).getEmployee().getName();
    }

    public EgwStatus getStatusByCodeAndModuleType(String code, String moduleName) {
        return (EgwStatus) persistenceService.find("from EgwStatus where moduleType=? and code=?", moduleName, code);
    }

    public String getRevenueWardForConsumerCode(String code, WaterConnectionDetails waterConnectionDetails) {

        BasicPropertyImpl basicPropertyImpl;
        if (waterConnectionDetails != null && waterConnectionDetails.getConnectionStatus().equals(ConnectionStatus.ACTIVE))
            basicPropertyImpl = (BasicPropertyImpl) persistenceService.find(
                    "from BasicPropertyImpl "
                            + "bp where bp.upicNo in(select conn.propertyIdentifier from WaterConnection conn where conn.consumerCode = ?)",
                    code);
        else
            basicPropertyImpl = (BasicPropertyImpl) persistenceService.find(
                    "from BasicPropertyImpl "
                            + "bp where bp.upicNo in(select conn.propertyIdentifier from WaterConnection conn where conn.id in"
                            + "(select conndet.connection from WaterConnectionDetails conndet where conndet.applicationNumber = ?))",
                    code);
        return basicPropertyImpl != null && basicPropertyImpl.getPropertyID() != null
                && basicPropertyImpl.getPropertyID().getWard() != null
                ? basicPropertyImpl.getPropertyID().getWard().getName() : "";
    }

    public Long getApproverPosition(String designationName, WaterConnectionDetails waterConnectionDetails) {

        final List<StateHistory<Position>> stateHistoryList = waterConnectionDetails.getStateHistory();
        Long approverPosition = 0l;
        String[] desgnArray = new String[100];
        if (designationName != null)
            desgnArray = designationName.split(",");
        User currentUser = null;
        waterConnectionDetails.getState().getValue();

        if (MEESEVA.equals(waterConnectionDetails.getSource()) &&
                (APPLICATION_STATUS_CLOSERDIGSIGNPENDING.equals(waterConnectionDetails.getStatus().getCode()) ||
                        APPLICATION_STATUS_RECONNDIGSIGNPENDING.equals(waterConnectionDetails.getStatus().getCode()) ||
                        APPLICATION_STATUS_DIGITALSIGNPENDING.equals(waterConnectionDetails.getStatus().getCode()))) {
            desgnArray = JUNIOR_OR_SENIOR_ASSISTANT_DESIGN.split(",");

            approverPosition = getApprovalPositionFromStateHistory(waterConnectionDetails, desgnArray);

        } else if (stateHistoryList != null && !stateHistoryList.isEmpty()) {
            currentUser = userService.getUserById(waterConnectionDetails.getCreatedBy().getId());
            if (currentUser != null && waterConnectionDetails.getLegacy().equals(true)) {
                for (Role userrole : currentUser.getRoles())
                    if (userrole.getName().equals(WaterTaxConstants.ROLE_SUPERUSER) ||
                            ROLE_APPROVERROLE.equalsIgnoreCase(userrole.getName()) ||
                            ROLE_MEESEVA_OPERATOR.equalsIgnoreCase(userrole.getName())) {
                        Position positionuser = getZonalLevelClerkForLoggedInUser(
                                waterConnectionDetails.getConnection().getPropertyIdentifier());
                        if (positionuser != null) {
                            approverPosition = positionuser.getId();
                            break;
                        }
                    }
            } else
                approverPosition = getApprovalPositionFromStateHistory(waterConnectionDetails, desgnArray);

        } else {
            currentUser = userService.getUserById(waterConnectionDetails.getCreatedBy().getId());
            if (currentUser != null && waterConnectionDetails.getLegacy().equals(true)) {
                for (Role userrole : currentUser.getRoles())
                    if (userrole.getName().equals(WaterTaxConstants.ROLE_SUPERUSER)) {
                        Position positionuser = getZonalLevelClerkForLoggedInUser(
                                waterConnectionDetails.getConnection().getPropertyIdentifier());
                        if (positionuser != null) {
                            approverPosition = positionuser.getId();
                            break;
                        }
                    }
            } else {
                Position posObjToClerk = positionMasterService.getCurrentPositionForUser(waterConnectionDetails.getCreatedBy().getId());
                if (posObjToClerk != null)
                    approverPosition = posObjToClerk.getId();
            }
        }
        return approverPosition;

    }

    @Transactional(readOnly = true)
    public Position getZonalLevelClerkForLoggedInUser(String asessmentNumber) {
        AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(asessmentNumber,
                PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ALL);
        Boundary boundaryObj = boundaryService.getBoundaryById(assessmentDetails.getBoundaryDetails().getAdminWardId());
        Assignment assignmentObj = getUserPositionByZone(boundaryObj);

        return assignmentObj == null ? null : assignmentObj.getPosition();
    }

    /**
     * Getting User assignment based on designation ,department and zone boundary Reading Designation and Department from
     * appconfig values and Values should be 'Senior Assistant,Junior Assistant' for designation and
     * 'Revenue,Accounts,Administration' for department
     *
     * @param asessmentNumber ,
     * @param boundaryObj
     * @return Assignment
     * @Param assessmentDetails
     */
    @Transactional(readOnly = true)
    public Assignment getUserPositionByZone(Boundary boundaryObj) {

        String designationStr = getDesignationForThirdPartyUser();
        String departmentStr = getDepartmentForWorkFlow();
        String[] department = departmentStr.split(",");
        String[] designation = designationStr.split(",");
        List<Assignment> assignment = new ArrayList<>();
        for (String dept : department) {
            for (String desg : designation) {
                assignment = assignmentService.findByDepartmentDesignationAndBoundary(
                        departmentService.getDepartmentByName(dept).getId(),
                        designationService.getDesignationByName(desg).getId(), boundaryObj.getId());
                if (assignment.isEmpty()) {
                    // Ward->Zone
                    if (boundaryObj.getParent() != null && boundaryObj.getParent().getBoundaryType() != null
                            && boundaryObj.getParent().getBoundaryType().getName().equals(WaterTaxConstants.BOUNDARY_TYPE_ZONE)) {
                        assignment = assignmentService.findByDeptDesgnAndParentAndActiveChildBoundaries(
                                departmentService.getDepartmentByName(dept).getId(),
                                designationService.getDesignationByName(desg).getId(), boundaryObj.getParent().getId());
                        // Ward->Zone->City
                        if (assignment.isEmpty() && boundaryObj.getParent() != null
                                && boundaryObj.getParent().getParent() != null
                                && boundaryObj.getParent().getParent().getBoundaryType().getName().equals(BOUNDARY_TYPE_CITY)) {
                            assignment = assignmentService.findByDeptDesgnAndParentAndActiveChildBoundaries(
                                    departmentService.getDepartmentByName(dept).getId(),
                                    designationService.getDesignationByName(desg).getId(),
                                    boundaryObj.getParent().getParent().getId());
                        }
                    }
                    // ward->City mapp
                    if (assignment.isEmpty() && boundaryObj.getParent() != null
                            && boundaryObj.getParent().getBoundaryType().getName().equals(BOUNDARY_TYPE_CITY)) {
                        assignment = assignmentService.findByDeptDesgnAndParentAndActiveChildBoundaries(
                                departmentService.getDepartmentByName(dept).getId(),
                                designationService.getDesignationByName(desg).getId(),
                                boundaryObj.getParent().getId());
                    }
                }
                if (!assignment.isEmpty())
                    break;
            }
            if (!assignment.isEmpty())
                break;
        }
        return assignment.isEmpty() ? null : assignment.get(0);
    }

    // allowing only for CollectionOperator to collect Fees
    @ModelAttribute(value = "checkOperator")
    public Boolean checkCollectionOperatorRole() {
        User currentUser = getUserId() == null ? null : userService.getUserById(getUserId());
        return currentUser == null ? false : compareUserRoleWithParameter(currentUser, WaterTaxConstants.ROLE_BILLCOLLECTOR);
    }

    public List<Installment> getInstallmentListByStartDate(Date startDate) {
        return persistenceService.findAllByNamedQuery(QUERY_INSTALLMENTLISTBY_MODULE_AND_STARTYEAR, startDate, startDate, PTMODULENAME);
    }

    public List<Installment> getInstallmentsForCurrYear(Date currDate) {
        return installmentDao.getAllInstallmentsByModuleAndStartDate(moduleService.getModuleByName(PTMODULENAME), currDate);
    }

    public List<Installment> getMonthlyInstallments(Date executionDate) {
        return installmentDao.getInstallmentsByModuleAndFromDateAndInstallmentType(
                moduleService.getModuleByName(MODULE_NAME), executionDate, new Date(), WaterTaxConstants.MONTHLY);
    }

    public Double waterConnectionDue(long parentId) {
        BigDecimal waterTaxDueforParent = BigDecimal.ZERO;
        List<WaterConnectionDetails> waterConnectionDetails = waterConnectionDetailsService
                .getAllConnectionDetailsByParentConnection(parentId);
        for (WaterConnectionDetails waterconnectiondetails : waterConnectionDetails)
            waterTaxDueforParent = waterTaxDueforParent.add(waterConnectionDetailsService.getTotalAmount(waterconnectiondetails));
        return waterTaxDueforParent.doubleValue();
    }

    public WaterDemandConnection getCurrentDemand(WaterConnectionDetails waterConnectionDetails) {
        WaterDemandConnection waterdemandConnection = new WaterDemandConnection();

        List<WaterDemandConnection> waterDemandConnectionList = waterDemandConnectionService
                .findByWaterConnectionDetails(waterConnectionDetails);
        for (WaterDemandConnection waterDemandConnection : waterDemandConnectionList)
            if (waterDemandConnection.getDemand().getIsHistory().equalsIgnoreCase(WaterTaxConstants.DEMAND_ISHISTORY_N)) {
                waterdemandConnection = waterDemandConnection;
                break;
            }

        return waterdemandConnection;
    }

    public List<EgDemand> getAllDemand(WaterConnectionDetails waterConnectionDetails) {
        List<EgDemand> demandList = new ArrayList<>();
        List<WaterDemandConnection> waterDemandConnectionList = waterDemandConnectionService
                .findByWaterConnectionDetails(waterConnectionDetails);
        for (WaterDemandConnection waterDemandConnection : waterDemandConnectionList)
            demandList.add(waterDemandConnection.getDemand());

        return demandList;
    }

    public Boolean getCitizenUserRole() {
        Boolean citizenrole = FALSE;
        if (getUserId() != null) {
            User currentUser = userService.getUserById(getUserId());
            if (currentUser.getUsername().equals(WaterTaxConstants.USERNAME_ANONYMOUS))
                citizenrole = TRUE;
            for (Role userrole : currentUser.getRoles())
                if (userrole != null && userrole.getName().equals(ROLE_CITIZEN)) {
                    citizenrole = TRUE;
                    break;
                }
        } else
            citizenrole = TRUE;
        return citizenrole;
    }

    public Boolean isDigitalSignatureEnabled() {
        List<AppConfigValues> appConfigValue = getAppConfigValueByModuleNameAndKeyName(
                WaterTaxConstants.MODULE_NAME, WaterTaxConstants.ENABLEDIGITALSIGNATURE);
        return appConfigValue.isEmpty() ? false : APPCONFIGVALUEOFENABLED.equalsIgnoreCase(appConfigValue.get(0).getValue());
    }

    public Boolean isLoggedInUserJuniorOrSeniorAssistant(Long userid) {
        Boolean isJrAsstOrSrAsst = FALSE;
        String designationStr = getDesignationForAppInitiator();
        String[] desgnArray = designationStr.split(",");
        if (desgnArray != null) {
            List<Assignment> assignmentList = assignmentService.getAllActiveEmployeeAssignmentsByEmpId(userid);
            for (Assignment assignment : assignmentList)
                for (String str : desgnArray)
                    if (assignment.getDesignation().getName().equalsIgnoreCase(str)) {
                        isJrAsstOrSrAsst = TRUE;
                        break;
                    }
        }
        return isJrAsstOrSrAsst;
    }

    public String getDesignationForAppInitiator() {
        List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(
                WaterTaxConstants.MODULE_NAME, WaterTaxConstants.APPLICATIONINITIATORROLE);
        return appConfigValue.isEmpty() ? EMPTY : appConfigValue.get(0).getValue();
    }

    public Boolean isCitizenPortalUser(User user) {
        return compareUserRoleWithParameter(user, ROLE_CITIZEN);
    }

    public Source setSourceOfConnection(User user) {
        return isCitizenPortalUser(user) ? Source.CITIZENPORTAL : null;
    }

    public Boolean isAnonymousUser(User user) {
        return USERNAME_ANONYMOUS.equalsIgnoreCase(user.getName());
    }

    public Boolean isSuperUser(User user) {
        return compareUserRoleWithParameter(user, ROLE_SUPERUSER);

    }

    public Boolean isCurrentUserCitizenRole() {
        User currentUser = getUserId() == null ? securityUtils.getCurrentUser() : userService.getUserById(getUserId());
        return compareUserRoleWithParameter(currentUser, ROLE_CITIZEN);
    }

    public boolean isUlbOperator() {
        User currentUser = getUserId() == null ? securityUtils.getCurrentUser() : userService.getUserById(getUserId());
        return compareUserRoleWithParameter(currentUser, ROLE_ULBOPERATOR);
    }

    public boolean isPublicRole() {
        User currentUser = userService.getUserById(getUserId());
        return currentUser == null ? true : compareUserRoleWithParameter(currentUser, ROLE_PUBLIC);
    }

    public Long getApprovalPositionFromStateHistory(final WaterConnectionDetails waterConnectionDetails,
                                                    final String[] desgnArray) {
        Long approverPosition = 0l;
        final List<StateHistory<Position>> stateHistoryList = waterConnectionDetails.getStateHistory();
        for (final StateHistory stateHistory : stateHistoryList)
            if (stateHistory.getOwnerPosition() != null) {
                final List<Assignment> assignmentList = assignmentService
                        .getAssignmentsForPosition(stateHistory.getOwnerPosition().getId(), new Date());
                for (final Assignment assgn : assignmentList)
                    for (final String str : desgnArray)
                        if (assgn.getDesignation().getName().equalsIgnoreCase(str)) {
                            approverPosition = stateHistory.getOwnerPosition().getId();
                            break;
                        }
                if (approverPosition != 0)
                    break;
            }
        if (approverPosition == 0) {
            final State stateObj = waterConnectionDetails.getState();
            final List<Assignment> assignmentList = assignmentService
                    .getAssignmentsForPosition(stateObj.getOwnerPosition().getId(), new Date());
            for (final Assignment assgn : assignmentList)
                for (final String str : desgnArray)
                    if (assgn.getDesignation().getName().equalsIgnoreCase(str)) {
                        approverPosition = stateObj.getOwnerPosition().getId();
                        break;
                    }
        }
        return approverPosition;
    }

    public boolean reassignEnabled() {
        final List<AppConfigValues> appConfigValues = appConfigValuesService.getConfigValuesByModuleAndKey(MODULE_NAME,
                REASSIGNMENT);
        return !appConfigValues.isEmpty() && "Yes".equalsIgnoreCase(appConfigValues.get(0).getValue());

    }

    public Boolean isRoleAdmin(final User user) {
        return compareUserRoleWithParameter(user, ROLE_ADMIN);
    }
    
    public Boolean isRoleBankCollectorOperator(final User user) {
        return compareUserRoleWithParameter(user, ROLE_BANKCOLLECTOROPERATOR);
    }

    public boolean compareUserRoleWithParameter(User user, String... userRole) {
        Boolean roleCheck = Boolean.FALSE;
        for(String roleCompare : userRole){
            roleCheck=user.getRoles().stream().anyMatch(role -> role != null && role.getName().equalsIgnoreCase(roleCompare));
            if (roleCheck)
                break;
        }
        return roleCheck;
    }

    private String getConfigurationValueByKey(String key) {
        return getAppConfigValueByModuleNameAndKeyName(MODULE_NAME, key).get(0).getValue();
    }

    public String getUserRole(String userRole) {
        User user = getUserId() == null ? securityUtils.getCurrentUser() : userService.getUserById(getUserId());
        Optional<Role> roles = user.getRoles().stream().filter(role -> userRole.equals(role.getName())).findFirst();
        return roles.isPresent() ? roles.get().getName() : EMPTY;
    }

    public boolean currentUserIsApprover(String currentUserDesignation) {
        return COMMISSIONER_DESGN.equalsIgnoreCase(currentUserDesignation)
                || DEPUTY_ENGINEER_DESIGN.equalsIgnoreCase(currentUserDesignation)
                || EXECUTIVE_ENGINEER_DESIGN.equalsIgnoreCase(currentUserDesignation)
                || MUNICIPAL_ENGINEER_DESIGN.equalsIgnoreCase(currentUserDesignation)
                || SUPERIENTEND_ENGINEER_DESIGN.equalsIgnoreCase(currentUserDesignation)
                || SUPERINTENDING_ENGINEER_DESIGNATION.equalsIgnoreCase(currentUserDesignation);
    }

    public boolean isConnectionInProgress(String connectionStatus) {
        return Arrays.asList(APPLICATION_STATUS_FEEPAID, APPLICATION_STATUS_DIGITALSIGNPENDING, APPLICATION_STATUS_CLOSERDIGSIGNPENDING,
                APPLICATION_STATUS_RECONNDIGSIGNPENDING, APPLICATION_STATUS_RECONNCTIONINPROGRESS, APPLICATION_STATUS_CLOSERINPROGRESS)
                .contains(connectionStatus);
    }

    public boolean currentUserIsCommissionerAndConnectionInProgress(WaterConnectionDetails waterConnectionDetails) {
        return COMMISSIONER_DESGN.equals(loggedInUserDesignation(waterConnectionDetails))
                && isConnectionInProgress(waterConnectionDetails.getStatus().getCode());
    }

    public boolean isSanctionDetailsEnabled(WaterConnectionDetails waterConnectionDetails) {
        return currentUserIsApprover(loggedInUserDesignation(waterConnectionDetails))
                && (waterConnectionDetails.getApprovalNumber() == null
                || !APPLICATION_STATUS_DIGITALSIGNPENDING.equalsIgnoreCase(waterConnectionDetails.getStatus().getCode()));
    }

    public void setConnectionSource(WaterConnectionDetails connectionDetails) {
        User currentUser = securityUtils.getCurrentUser();

        if (isAnonymousUser(currentUser))
            connectionDetails.setSource(ONLINE);
        else if (isCSCoperator(currentUser))
            connectionDetails.setSource(CSC);
        else if (isCitizenPortalUser(currentUser) && (connectionDetails.getSource() == null
                || StringUtils.isBlank(connectionDetails.getSource().toString())))
            connectionDetails.setSource(setSourceOfConnection(currentUser));
        else if (isMeesevaUser(currentUser)) {
            connectionDetails.setSource(MEESEVA);
            if (connectionDetails.getMeesevaApplicationNumber() != null)
                connectionDetails.setApplicationNumber(connectionDetails.getMeesevaApplicationNumber());
        } else
            connectionDetails.setSource(Source.SYSTEM);
    }

    public boolean validateWorkflow(String workFlowAction) {
        return Arrays.asList(APPROVEWORKFLOWACTION, SIGNWORKFLOWACTION, WF_PREVIEW_BUTTON).contains(workFlowAction);
    }

    public boolean checkWithApplicationType(String appTypeCode) {
        return Arrays.asList(NEWCONNECTION, ADDNLCONNECTION, CHANGEOFUSE, REGULARIZE_CONNECTION).contains(appTypeCode);
    }
}
