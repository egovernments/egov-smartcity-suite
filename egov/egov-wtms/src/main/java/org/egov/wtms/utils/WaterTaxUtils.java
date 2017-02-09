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
package org.egov.wtms.utils;

import static org.egov.ptis.constants.PropertyTaxConstants.MEESEVA_OPERATOR_ROLE;
import static org.egov.ptis.constants.PropertyTaxConstants.PTMODULENAME;
import static org.egov.ptis.constants.PropertyTaxConstants.QUERY_INSTALLMENTLISTBY_MODULE_AND_STARTYEAR;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.egov.commons.EgwStatus;
import org.egov.commons.Installment;
import org.egov.commons.dao.InstallmentDao;
import org.egov.demand.model.EgDemand;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.messaging.MessagingService;
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
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

@Service
public class WaterTaxUtils {

    @Autowired
    private AppConfigValueService appConfigValuesService;

    @Autowired
    private CityService cityService;

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
    private MessagingService messagingService;

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
    @Qualifier("fileStoreService")
    protected FileStoreService fileStoreService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private InstallmentDao installmentDao;

    public List<AppConfigValues> getAppConfigValueByModuleNameAndKeyName(final String moduleName,
            final String keyName) {
        final List<AppConfigValues> appconfigValuesList = appConfigValuesService
                .getConfigValuesByModuleAndKey(moduleName, keyName);
        return appconfigValuesList;
    }

    public String loggedInUserDesignation(final WaterConnectionDetails waterConnectionDetails) {
        String loggedInUserDesignation = "";
        final User user = securityUtils.getCurrentUser();
        List<Assignment> loggedInUserAssign;
        if (waterConnectionDetails.getState() != null && waterConnectionDetails.getState().getOwnerPosition() !=null) {
            loggedInUserAssign = assignmentService.getAssignmentByPositionAndUserAsOnDate(
                    waterConnectionDetails.getState().getOwnerPosition().getId(), user.getId(), new Date());
            loggedInUserDesignation = !loggedInUserAssign.isEmpty()
                    ? loggedInUserAssign.get(0).getDesignation().getName() : null;
        }
        return loggedInUserDesignation;
    }
    public Boolean getCurrentUserRole() {
        Boolean cscUserRole =Boolean.FALSE;
        User currentUser = null;

        if (ApplicationThreadLocals.getUserId() != null)
            currentUser = userService.getUserById(ApplicationThreadLocals.getUserId());
        else
            currentUser = securityUtils.getCurrentUser();

        for (final Role userrole : currentUser.getRoles())
            if (userrole.getName().equals(WaterTaxConstants.ROLE_CSCOPERTAOR)) {
                cscUserRole = Boolean.TRUE;
                break;
            }
        return cscUserRole;
    }

    public Boolean isSmsEnabled() {
        final AppConfigValues appConfigValue = getAppConfigValueByModuleNameAndKeyName(WaterTaxConstants.MODULE_NAME,
                WaterTaxConstants.SENDSMSFORWATERTAX).get(0);
        return WaterTaxConstants.APPCONFIGVALUEOFENABLED.equalsIgnoreCase(appConfigValue.getValue());
    }

    public String getDepartmentForWorkFlow() {
        String department = "";
        final List<AppConfigValues> appConfigValue = getAppConfigValueByModuleNameAndKeyName(
                WaterTaxConstants.MODULE_NAME, WaterTaxConstants.WATERTAXWORKFLOWDEPARTEMENT);
        if (null != appConfigValue && !appConfigValue.isEmpty())
            department = appConfigValue.get(0).getValue();
        return department;
    }

    public String getDesignationForThirdPartyUser() {
        String designation = "";
        final List<AppConfigValues> appConfigValue = getAppConfigValueByModuleNameAndKeyName(
                WaterTaxConstants.MODULE_NAME, WaterTaxConstants.CLERKDESIGNATIONFORCSCOPERATOR);
        if (null != appConfigValue && !appConfigValue.isEmpty())
            designation = appConfigValue.get(0).getValue();
        return designation;
    }

    public List<AppConfigValues> getThirdPartyUserRoles() {

        final List<AppConfigValues> appConfigValueList = getAppConfigValueByModuleNameAndKeyName(
                WaterTaxConstants.MODULE_NAME, WaterTaxConstants.ROLEFORNONEMPLOYEEINWATERTAX);

        return !appConfigValueList.isEmpty() ? appConfigValueList : null;

    }

    /**
     * @return appconfigValues List for Keyname='ROLESFORLOGGEDINUSER'
     */
    public List<AppConfigValues> getUserRolesForLoggedInUser() {
        final List<AppConfigValues> appConfigValueList = appConfigValuesService.getConfigValuesByModuleAndKeyByValueAsc(
                WaterTaxConstants.MODULE_NAME, WaterTaxConstants.ROLESFORLOGGEDINUSER);
        // TODO: this method getting Values by Order By value Asc and based on
        // that returning LoggedInRoles
        return !appConfigValueList.isEmpty() ? appConfigValueList : null;
    }

    public Boolean getAppconfigValueForSchedulearEnabled() {
        Boolean schedularEnabled = Boolean.FALSE;
        final AppConfigValues appConfigValueObj = appConfigValuesService.getConfigValuesByModuleAndKeyByValueAsc(
                WaterTaxConstants.MODULE_NAME, WaterTaxConstants.ENABLEDEMANEDBILLSCHEDULAR).get(0);
        if (appConfigValueObj != null && appConfigValueObj.getValue() != null)
            if (appConfigValueObj.getValue().equals(WaterTaxConstants.APPCONFIGVALUEOFENABLED))
                schedularEnabled = Boolean.TRUE;
        return schedularEnabled;

    }

    public Boolean getCurrentUserRole(final User currentUser) {
        Boolean applicationByOthers = false;

        for (final Role userrole : currentUser.getRoles())
            for (final AppConfigValues appconfig : getThirdPartyUserRoles())
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
    public Boolean isMeesevaUser(final User user) {
        for (final Role role : user.getRoles())
            if (role != null && role.getName().equalsIgnoreCase(MEESEVA_OPERATOR_ROLE))
                return true;
        return false;
    }

    public Boolean isEmailEnabled() {
        final AppConfigValues appConfigValue = getAppConfigValueByModuleNameAndKeyName(WaterTaxConstants.MODULE_NAME,
                WaterTaxConstants.SENDEMAILFORWATERTAX).get(0);
        return WaterTaxConstants.APPCONFIGVALUEOFENABLED.equalsIgnoreCase(appConfigValue.getValue());
    }

    public Boolean isNewConnectionAllowedIfPTDuePresent() {
        final AppConfigValues appConfigValue = getAppConfigValueByModuleNameAndKeyName(WaterTaxConstants.MODULE_NAME,
                WaterTaxConstants.NEWCONNECTIONALLOWEDIFPTDUE).get(0);
        return WaterTaxConstants.APPCONFIGVALUEOFENABLED.equalsIgnoreCase(appConfigValue.getValue());
    }

    public Boolean isMultipleNewConnectionAllowedForPID() {
        final AppConfigValues appConfigValue = getAppConfigValueByModuleNameAndKeyName(WaterTaxConstants.MODULE_NAME,
                WaterTaxConstants.MULTIPLENEWCONNECTIONFORPID).get(0);
        return WaterTaxConstants.APPCONFIGVALUEOFENABLED.equalsIgnoreCase(appConfigValue.getValue());
    }

    public Boolean isConnectionAllowedIfWTDuePresent(final String connectionType) {
        final Boolean isAllowed = false;
        final List<AppConfigValues> appConfigValue = getAppConfigValueByModuleNameAndKeyName(
                WaterTaxConstants.MODULE_NAME, connectionType);
        if (null != appConfigValue && !appConfigValue.isEmpty())
            return WaterTaxConstants.APPCONFIGVALUEOFENABLED.equalsIgnoreCase(appConfigValue.get(0).getValue());

        return isAllowed;
    }

    public String documentRequiredForBPLCategory() {
        String documentName = null;
        final List<AppConfigValues> appConfigValue = getAppConfigValueByModuleNameAndKeyName(
                WaterTaxConstants.MODULE_NAME, WaterTaxConstants.DOCUMENTREQUIREDFORBPL);
        if (appConfigValue != null && !appConfigValue.isEmpty())
            documentName = appConfigValue.get(0).getValue();
        return documentName;
    }

    public String getMunicipalityName() {
        return ApplicationThreadLocals.getMunicipalityName();
    }

    public String getCityCode() {
        return cityService.getCityByURL(ApplicationThreadLocals.getDomainName()).getCode();
    }

    public String smsAndEmailBodyByCodeAndArgsForRejection(final String code, final String approvalComment,
            final String applicantName) {
        final Locale locale = LocaleContextHolder.getLocale();
        final String smsMsg = wcmsMessageSource.getMessage(code,
                new String[] { applicantName, approvalComment, getMunicipalityName() }, locale);
        return smsMsg;
    }

    public String emailBodyforApprovalEmailByCodeAndArgs(final String code,
            final WaterConnectionDetails waterConnectionDetails, final String applicantName) {
        final Locale locale = LocaleContextHolder.getLocale();
        final String smsMsg = wcmsMessageSource
                .getMessage(code,
                        new String[] { applicantName, waterConnectionDetails.getApplicationNumber(),
                                waterConnectionDetails.getConnection().getConsumerCode(), getMunicipalityName() },
                        locale);
        return smsMsg;
    }

    public String emailSubjectforEmailByCodeAndArgs(final String code, final String applicationNumber) {
        final Locale locale = LocaleContextHolder.getLocale();
        final String emailSubject = wcmsMessageSource.getMessage(code, new String[] { applicationNumber }, locale);
        return emailSubject;
    }

    public void sendSMSOnWaterConnection(final String mobileNumber, final String smsBody) {
        messagingService.sendSMS(mobileNumber, smsBody);
    }

    public void sendEmailOnWaterConnection(final String email, final String emailBody, final String emailSubject) {
        messagingService.sendEmail(email, emailSubject, emailBody);
    }

    public Position getCityLevelCommissionerPosition(final String commissionerDesgn, final String assessmentNumber) {
        final String[] degnName = commissionerDesgn.split(",");
        if (degnName.length > 1) {
        } else {
        }
        final Designation desgnObj = designationService.getDesignationByName(commissionerDesgn);
        if ("Commissioner".equals(commissionerDesgn)) {
            final Department deptObj = departmentService
                    .getDepartmentByName(WaterTaxConstants.ROLE_COMMISSIONERDEPARTEMNT);
            List<Assignment> assignlist = null;
            assignlist = assignmentService.getAssignmentsByDeptDesigAndDates(deptObj.getId(), desgnObj.getId(),
                    new Date(), new Date());
            if (assignlist.isEmpty())
                assignlist = assignmentService.getAllPositionsByDepartmentAndDesignationForGivenRange(null,
                        desgnObj.getId(), new Date());
            if (assignlist.isEmpty())
                assignlist = assignmentService.getAllActiveAssignments(desgnObj.getId());

            return assignlist.get(0).getPosition();
        } else {
            final Position userPosition = getZonalLevelClerkForLoggedInUser(assessmentNumber);
            return userPosition;
        }
    }

    public String getApproverUserName(final Long approvalPosition) {
        Assignment assignment = null;
        if (approvalPosition != null)
            assignment = assignmentService.getPrimaryAssignmentForPositionAndDate(approvalPosition, new Date());
        return assignment != null ? assignment.getEmployee().getUsername() : "";
    }

    public String getApproverName(final Long approvalPosition) {
        Assignment assignment = null;
        List<Assignment> asignList = null;
        if (approvalPosition != null)
            assignment = assignmentService.getPrimaryAssignmentForPositionAndDate(approvalPosition, new Date());
        if (assignment != null) {
            asignList = new ArrayList<Assignment>();
            asignList.add(assignment);
        } else if (assignment == null)
            asignList = assignmentService.getAssignmentsForPosition(approvalPosition, new Date());
        return !asignList.isEmpty() ? asignList.get(0).getEmployee().getName() : "";
    }

    public EgwStatus getStatusByCodeAndModuleType(final String code, final String moduleName) {
        return (EgwStatus) persistenceService.find("from EgwStatus where moduleType=? and code=?", moduleName, code);
    }

	public String getRevenueWardForConsumerCode(final String code,WaterConnectionDetails waterConnectionDetails) {
		BasicPropertyImpl basicPropertyImpl=null;
		if(waterConnectionDetails!=null && waterConnectionDetails.getConnectionStatus().equals(ConnectionStatus.ACTIVE))
			basicPropertyImpl=(BasicPropertyImpl) persistenceService.find("from BasicPropertyImpl "
						+ "bp where bp.upicNo in(select conn.propertyIdentifier from WaterConnection conn where conn.consumerCode = ?)",
				code);
		else 
			basicPropertyImpl=(BasicPropertyImpl) persistenceService.find(
					"from BasicPropertyImpl "
							+ "bp where bp.upicNo in(select conn.propertyIdentifier from WaterConnection conn where conn.id in"
							+ "(select conndet.connection from WaterConnectionDetails conndet where conndet.applicationNumber = ?))",
					code);
			return (basicPropertyImpl!=null && basicPropertyImpl.getPropertyID()!=null && basicPropertyImpl.getPropertyID().getWard()!=null?basicPropertyImpl.getPropertyID().getWard().getName():"");
	}
    public Long getApproverPosition(final String designationName, final WaterConnectionDetails waterConnectionDetails) {

        final List<StateHistory> stateHistoryList = waterConnectionDetails.getStateHistory();
        Long approverPosition = 0l;
        final String[] desgnArray = designationName.split(",");
        User currentUser = null;
        waterConnectionDetails.getState().getValue();
        if (stateHistoryList != null && !stateHistoryList.isEmpty()) {
            currentUser = userService.getUserById(waterConnectionDetails.getCreatedBy().getId());
            if (currentUser != null && waterConnectionDetails.getLegacy().equals(true)) {
                for (final Role userrole : currentUser.getRoles())
                    if (userrole.getName().equals(WaterTaxConstants.ROLE_SUPERUSER)) {
                        final Position positionuser = getZonalLevelClerkForLoggedInUser(
                                waterConnectionDetails.getConnection().getPropertyIdentifier());
                        if(positionuser!=null){
                        approverPosition = positionuser.getId();
                        break;
                        }
                    }
            } else {
                for (final StateHistory stateHistory : stateHistoryList)
                    if (stateHistory.getOwnerPosition() != null) {
                        final List<Assignment> assignmentList = assignmentService
                                .getAssignmentsForPosition(stateHistory.getOwnerPosition().getId(), new Date());
                        for (final Assignment assgn : assignmentList)
                            for (final String str : desgnArray) {
                               if (assgn.getDesignation().getName().equalsIgnoreCase(str)) {
                                    approverPosition = stateHistory.getOwnerPosition().getId();
                                    break;
                                }
                            }
                        if (approverPosition != 0)
                            break;
                    }
                if (approverPosition == 0) {
                    final State stateObj = waterConnectionDetails.getState();
                    final List<Assignment> assignmentList = assignmentService
                            .getAssignmentsForPosition(stateObj.getOwnerPosition().getId(), new Date());
                    for (final Assignment assgn : assignmentList)
                        for (final String str : desgnArray) {
                          if (assgn.getDesignation().getName().equalsIgnoreCase(str)) {
                                approverPosition = stateObj.getOwnerPosition().getId();
                                break;
                            }
                        }
                }
            }

        } else {
            currentUser = userService.getUserById(waterConnectionDetails.getCreatedBy().getId());
            if (currentUser != null && waterConnectionDetails.getLegacy().equals(true)) {
                for (final Role userrole : currentUser.getRoles())
                    if (userrole.getName().equals(WaterTaxConstants.ROLE_SUPERUSER)) {
                        final Position positionuser = getZonalLevelClerkForLoggedInUser(
                                waterConnectionDetails.getConnection().getPropertyIdentifier());
                        if(positionuser!=null){
                        approverPosition = positionuser.getId();
                        break;
                        }
                    }
            } else {
                final Position posObjToClerk = positionMasterService
                        .getCurrentPositionForUser(waterConnectionDetails.getCreatedBy().getId());
                if(posObjToClerk!=null)
                approverPosition = posObjToClerk.getId();
            }
        }
        return approverPosition;

    }
    @Transactional(readOnly=true)
    public Position getZonalLevelClerkForLoggedInUser(final String asessmentNumber) {
        final AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(asessmentNumber,
                PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ALL);
        Assignment assignmentObj = null;
        /*
         * final HierarchyType hierarchy =
         * hierarchyTypeService.getHierarchyTypeByName
         * (WaterTaxConstants.HIERARCHYNAME_ADMIN); final BoundaryType
         * boundaryTypeObj =
         * boundaryTypeService.getBoundaryTypeByNameAndHierarchyType(
         * assessmentDetails.getBoundaryDetails().getWardBoundaryType(),
         * hierarchy); final Boundary boundaryObj =
         * boundaryService.getBoundaryByTypeAndNo(boundaryTypeObj,
         * assessmentDetails .getBoundaryDetails().getAdminWardNumber());
         */
        // TODO: check whether adminward always mandatory
        final Boundary boundaryObj = boundaryService
                .getBoundaryById(assessmentDetails.getBoundaryDetails().getAdminWardId());
        assignmentObj = getUserPositionByZone(asessmentNumber, assessmentDetails, boundaryObj);

        return assignmentObj != null ? assignmentObj.getPosition() : null;
    }

    /**
     * Getting User assignment based on designation ,department and zone
     * boundary Reading Designation and Department from appconfig values and
     * Values should be 'Senior Assistant,Junior Assistant' for designation and
     * 'Revenue,Accounts,Administration' for department
     *
     * @param asessmentNumber
     *            ,
     * @Param assessmentDetails
     * @param boundaryObj
     * @return Assignment
     */
    @Transactional(readOnly=true)
    public Assignment getUserPositionByZone(final String asessmentNumber, final AssessmentDetails assessmentDetails,
            final Boundary boundaryObj) {
        final String designationStr = getDesignationForThirdPartyUser();
        final String departmentStr = getDepartmentForWorkFlow();
        final String[] department = departmentStr.split(",");
        final String[] designation = designationStr.split(",");
        List<Assignment> assignment = new ArrayList<Assignment>();
        for (final String dept : department) {
            for (final String desg : designation) {
                assignment = assignmentService.findByDepartmentDesignationAndBoundary(
                        departmentService.getDepartmentByName(dept).getId(),
                        designationService.getDesignationByName(desg).getId(), boundaryObj.getId());
                if (!assignment.isEmpty())
                    break;
            }
            if (!assignment.isEmpty())
                break;
        }
        return !assignment.isEmpty() ? assignment.get(0) : null;
    }

    // allowing only for CollectionOperator to collect Fees
    @ModelAttribute(value = "checkOperator")
    public Boolean checkCollectionOperatorRole() {
        Boolean isCSCOperator = false;
        // as per Adoni allowing collection for ULB Operator
        if (ApplicationThreadLocals.getUserId() != null) {
            final User userObj = userService.getUserById(ApplicationThreadLocals.getUserId());
            if (userObj != null)
                for (final Role role : userObj.getRoles())
                    if (role != null && role.getName().contains(WaterTaxConstants.ROLE_BILLCOLLECTOR)) {
                        isCSCOperator = true;
                        break;
                    }
        }
        return isCSCOperator;
    }

    public List<Installment> getInstallmentListByStartDate(final Date startDate) {
        return persistenceService.findAllByNamedQuery(QUERY_INSTALLMENTLISTBY_MODULE_AND_STARTYEAR, startDate,
                startDate, PTMODULENAME);
    }

    public List<Installment> getInstallmentsForCurrYear(final Date currDate) {
        final Module module = moduleService.getModuleByName(PTMODULENAME);
        final List<Installment> installments = installmentDao.getAllInstallmentsByModuleAndStartDate(module, currDate);
        return installments;
    }

    public Double waterConnectionDue(final long parentId) {
        BigDecimal waterTaxDueforParent = BigDecimal.ZERO;
        final List<WaterConnectionDetails> waterConnectionDetails = waterConnectionDetailsService
                .getAllConnectionDetailsByParentConnection(parentId);
        for (final WaterConnectionDetails waterconnectiondetails : waterConnectionDetails)
            waterTaxDueforParent = waterTaxDueforParent
                    .add(waterConnectionDetailsService.getTotalAmount(waterconnectiondetails));
        return waterTaxDueforParent.doubleValue();
    }

    public WaterDemandConnection getCurrentDemand(final WaterConnectionDetails waterConnectionDetails) {
        WaterDemandConnection waterdemandConnection = new WaterDemandConnection();

        final List<WaterDemandConnection> waterDemandConnectionList = waterDemandConnectionService
                .findByWaterConnectionDetails(waterConnectionDetails);
        for (final WaterDemandConnection waterDemandConnection : waterDemandConnectionList)
            if (waterDemandConnection.getDemand().getIsHistory().equalsIgnoreCase(WaterTaxConstants.DEMANDISHISTORY)) {
                waterdemandConnection = waterDemandConnection;
                break;
            }

        return waterdemandConnection;
    }

    public List<EgDemand> getAllDemand(final WaterConnectionDetails waterConnectionDetails) {
        final List<EgDemand> demandList = new ArrayList<EgDemand>();
        final List<WaterDemandConnection> waterDemandConnectionList = waterDemandConnectionService
                .findByWaterConnectionDetails(waterConnectionDetails);
        for (final WaterDemandConnection waterDemandConnection : waterDemandConnectionList)
            demandList.add(waterDemandConnection.getDemand());

        return demandList;
    }

    public Boolean getCitizenUserRole() {
        Boolean citizenrole = Boolean.FALSE;
        if (ApplicationThreadLocals.getUserId() != null) {
            final User currentUser = userService.getUserById(ApplicationThreadLocals.getUserId());
            if (currentUser.getRoles().isEmpty()
                    && securityUtils.getCurrentUser().getUsername().equals(WaterTaxConstants.USERNAME_ANONYMOUS))
                citizenrole = Boolean.TRUE;
            for (final Role userrole : currentUser.getRoles())
                if (userrole != null && userrole.getName().equals(WaterTaxConstants.ROLE_CITIZEN)) {
                    citizenrole = Boolean.TRUE;
                    break;
                }
        } else
            citizenrole = Boolean.TRUE;
        return citizenrole;
    }

    public Boolean isDigitalSignatureEnabled() {
        final List<AppConfigValues> appConfigValue = getAppConfigValueByModuleNameAndKeyName(
                WaterTaxConstants.MODULE_NAME, WaterTaxConstants.ENABLEDIGITALSIGNATURE);
        if (null != appConfigValue && !appConfigValue.isEmpty())
            return WaterTaxConstants.APPCONFIGVALUEOFENABLED.equalsIgnoreCase(appConfigValue.get(0).getValue());
        else
            return false;
    }

}
