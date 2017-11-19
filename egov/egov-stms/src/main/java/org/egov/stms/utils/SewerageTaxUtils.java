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
package org.egov.stms.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.egov.commons.EgwStatus;
import org.egov.commons.Installment;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.dao.InstallmentDao;
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
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.enums.BasicPropertyStatus;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.ptis.wtms.PropertyIntegrationService;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.utils.constants.SewerageTaxConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SewerageTaxUtils {

    @Autowired
    private AppConfigValueService appConfigValuesService;

    @Autowired
    @Qualifier("propertyIntegrationServiceImpl")
    private PropertyIntegrationService propertyIntegrationService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private BoundaryService boundaryService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DesignationService designationService;

    @Autowired
    private UserService userService;

    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    private CityService cityService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private EgwStatusHibernateDAO egwStatusHibernateDAO;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private InstallmentDao installmentDao;

    @Autowired
    @Qualifier("fileStoreService")
    private FileStoreService fileStoreService;

    /**
     * @return false by default. If configuration value is Yes, then returns true.
     */
    public Boolean isNewConnectionAllowedIfPTDuePresent() {

        final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(
                SewerageTaxConstants.MODULE_NAME, SewerageTaxConstants.NEWCONNECTIONALLOWEDIFPTDUE);

        if (appConfigValue != null && !appConfigValue.isEmpty())
            return "YES".equalsIgnoreCase(appConfigValue.get(0).getValue());

        return false;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public EgwStatus getStatusByCodeAndModuleType(final String code, final String moduleName) {
        return egwStatusHibernateDAO.getStatusByModuleAndCode(moduleName, code);
    }

    public AssessmentDetails getAssessmentDetailsForFlag(final String asessmentNumber, final Integer flagDetail) {
        return propertyIntegrationService.getAssessmentDetailsForFlag(
                asessmentNumber, flagDetail, BasicPropertyStatus.ACTIVE);
    }

    public Boolean getCurrentUserRole(final User currentUser) {
        /*
         * Boolean applicationByOthers = false; for (final Role userrole : currentUser.getRoles()) for (final AppConfigValues
         * appconfig : getThirdPartyUserRoles()) if (userrole != null && userrole.getName().equals(appconfig.getValue())) {
         * applicationByOthers = true; break; } return applicationByOthers;
         */
        return true;
    }

    public String getApproverName(final Long approvalPosition) {
        Assignment assignment = null;
        List<Assignment> asignList;
        if (approvalPosition != null)
            assignment = assignmentService.getPrimaryAssignmentForPositionAndDate(approvalPosition, new Date());
        if (assignment != null) {
            asignList = new ArrayList<>();
            asignList.add(assignment);
        } else
            asignList = assignmentService.getAssignmentsForPosition(approvalPosition, new Date());
        return !asignList.isEmpty() ? asignList.get(0).getEmployee().getName() : "";
    }

    public Position getZonalLevelClerkForLoggedInUser(final String asessmentNumber) {
        final AssessmentDetails assessmentDetails = getAssessmentDetailsForFlag(asessmentNumber,
                PropertyExternalService.FLAG_FULL_DETAILS);
        Assignment assignmentObj = null;
        final Boundary boundaryObj = boundaryService.getBoundaryById(assessmentDetails.getBoundaryDetails()
                .getAdminWardId());
        assignmentObj = getUserPositionByZone(asessmentNumber, assessmentDetails, boundaryObj);

        return assignmentObj != null ? assignmentObj.getPosition() : null;
    }

    /**
     * Getting User assignment based on designation ,department and zone boundary Reading Designation and Department from
     * appconfig values and Values should be 'Senior Assistant,Junior Assistant' for designation and
     * 'Revenue,Accounts,Administration' for department
     *
     * @param asessmentNumber ,
     * @Param assessmentDetails
     * @param boundaryObj
     * @return Assignment
     */
    public Assignment getUserPositionByZone(final String asessmentNumber, final AssessmentDetails assessmentDetails,
                                            final Boundary boundaryObj) {
        final String designationStr = getDesignationForThirdPartyUser();
        final String departmentStr = getDepartmentForWorkFlow();
        final String[] department = departmentStr.split(",");
        final String[] designation = designationStr.split(",");
        List<Assignment> assignment = new ArrayList<>();
        for (final String dept : department) {
            for (final String desg : designation) {
                assignment = assignmentService.findByDepartmentDesignationAndBoundary(departmentService
                                .getDepartmentByName(dept).getId(), designationService.getDesignationByName(desg).getId(),
                        boundaryObj.getId());
                if (!assignment.isEmpty())
                    break;
            }
            if (!assignment.isEmpty())
                break;
        }
        return !assignment.isEmpty() ? assignment.get(0) : null;
    }

    public String getDesignationForThirdPartyUser() {
        String designation = "";
        final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(
                SewerageTaxConstants.MODULE_NAME, SewerageTaxConstants.CLERKDESIGNATIONFORCSCOPERATOR);
        if (null != appConfigValue && !appConfigValue.isEmpty())
            designation = appConfigValue.get(0).getValue();
        return designation;
    }

    public String getDepartmentForWorkFlow() {
        String department = "";
        final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(
                SewerageTaxConstants.MODULE_NAME, SewerageTaxConstants.SEWERAGETAXWORKFLOWDEPARTEMENT);
        if (null != appConfigValue && !appConfigValue.isEmpty())
            department = appConfigValue.get(0).getValue();
        return department;
    }

    public Long getApproverPosition(final String designationName,
                                    final SewerageApplicationDetails sewerageApplicationDetails) {
        final Set<StateHistory<Position>> stateHistoryList = sewerageApplicationDetails.getState().getHistory();
        Long approverPosition = 0l;
        final String[] desgnArray = designationName.split(",");
        User currentUser = null;
        if (stateHistoryList != null && !stateHistoryList.isEmpty()) {
            currentUser = userService.getUserById(sewerageApplicationDetails.getCreatedBy().getId());
            if (currentUser != null && sewerageApplicationDetails.getConnection().getLegacy()) {
                for (final Role userrole : currentUser.getRoles())
                    if (userrole.getName().equals(SewerageTaxConstants.ROLE_SUPERUSER)) {
                        final Position positionuser = getZonalLevelClerkForLoggedInUser(sewerageApplicationDetails
                                .getConnectionDetail().getPropertyIdentifier());
                        approverPosition = positionuser.getId();
                        break;
                    }
            } else {
                for (final StateHistory stateHistory : stateHistoryList)
                    if (stateHistory.getOwnerPosition() != null) {
                        final List<Assignment> assignmentList = assignmentService.getAssignmentsForPosition(
                                stateHistory.getOwnerPosition().getId(), new Date());
                        for (final Assignment assgn : assignmentList)
                            for (final String str : desgnArray)
                                if (assgn.getDesignation().getName().equalsIgnoreCase(str)) {
                                    approverPosition = stateHistory.getOwnerPosition().getId();
                                    break;
                                }

                    }
                if (approverPosition == 0) {
                    final State stateObj = sewerageApplicationDetails.getState();
                    final List<Assignment> assignmentList = assignmentService.getAssignmentsForPosition(stateObj
                            .getOwnerPosition().getId(), new Date());
                    for (final Assignment assgn : assignmentList)
                        if (assgn.getDesignation().getName().equalsIgnoreCase(designationName)) {
                            approverPosition = stateObj.getOwnerPosition().getId();
                            break;
                        }
                }

            }

        } else {
            currentUser = userService.getUserById(sewerageApplicationDetails.getCreatedBy().getId());
            if (currentUser != null && sewerageApplicationDetails.getConnection().getLegacy()) {
                for (final Role userrole : currentUser.getRoles())
                    if (userrole.getName().equals(SewerageTaxConstants.ROLE_SUPERUSER)) {
                        final Position positionuser = getZonalLevelClerkForLoggedInUser(sewerageApplicationDetails
                                .getConnectionDetail().getPropertyIdentifier());
                        approverPosition = positionuser.getId();
                        break;
                    }
            } else {
                final Position posObjToClerk = positionMasterService
                        .getCurrentPositionForUser(sewerageApplicationDetails.getCreatedBy().getId());
                approverPosition = posObjToClerk.getId();
            }
        }
        return approverPosition;
    }

    public Position getCityLevelExecutiveEngineerPosition(final String execEnggDesgn) {
        String execEnggDesgnName = "";
        final String[] degnName = execEnggDesgn.split(",");
        if (degnName.length > 1)
            execEnggDesgnName = degnName[0];
        else
            execEnggDesgnName = execEnggDesgn;
        final Designation desgnObj = designationService.getDesignationByName(execEnggDesgn);
        if (execEnggDesgn.equals("Executive engineer")) {
            final Department deptObj = departmentService
                    .getDepartmentByName(SewerageTaxConstants.ROLE_EXECUTIVEDEPARTEMNT);
            List<Assignment> assignlist = null;
            assignlist = assignmentService.getAssignmentsByDeptDesigAndDates(deptObj.getId(), desgnObj.getId(),
                    new Date(), new Date());
            if (assignlist.isEmpty())
                assignlist = assignmentService.getAllPositionsByDepartmentAndDesignationForGivenRange(null,
                        desgnObj.getId(), new Date());
            if (assignlist.isEmpty())
                assignlist = assignmentService.getAllActiveAssignments(desgnObj.getId());

            return assignlist.get(0).getPosition();
        } else
            return !assignmentService.findPrimaryAssignmentForDesignationName(execEnggDesgnName).isEmpty() ? assignmentService
                    .findPrimaryAssignmentForDesignationName(execEnggDesgnName).get(0).getPosition()
                    : null;
    }

    public Position getCityLevelDeputyEngineerPosition(final String deputyEngineerDesgn) {
        String deputydesgnname = "";
        final String[] degnName = deputyEngineerDesgn.split(",");
        if (degnName.length > 1)
            deputydesgnname = degnName[0];
        else
            deputydesgnname = deputyEngineerDesgn;
        final Designation desgnObj = designationService.getDesignationByName(deputyEngineerDesgn);
        if (deputyEngineerDesgn.equalsIgnoreCase("Deputy executive engineer")) {
            final Department deptObj = departmentService
                    .getDepartmentByName(SewerageTaxConstants.ROLE_DEPUTYDEPARTEMNT);
            List<Assignment> assignlist = null;
            assignlist = assignmentService.getAssignmentsByDeptDesigAndDates(deptObj.getId(), desgnObj.getId(),
                    new Date(), new Date());
            if (assignlist.isEmpty())
                assignlist = assignmentService.getAllPositionsByDepartmentAndDesignationForGivenRange(null,
                        desgnObj.getId(), new Date());
            if (assignlist.isEmpty())
                assignlist = assignmentService.getAllActiveAssignments(desgnObj.getId());

            return assignlist.get(0).getPosition();
        } else
            return !assignmentService.findPrimaryAssignmentForDesignationName(deputydesgnname).isEmpty() ? assignmentService
                    .findPrimaryAssignmentForDesignationName(deputydesgnname).get(0).getPosition()
                    : null;
    }

    public String getCityCode() {
        return cityService.getCityByURL(ApplicationThreadLocals.getDomainName()).getCode();
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
                    if (role != null && role.getName().contains(SewerageTaxConstants.ROLE_COLLECTIONOPERATOR)) {
                        isCSCOperator = true;
                        break;
                    }
        }
        return isCSCOperator;
    }

    public Boolean getCitizenUserRole() {
        Boolean citizenrole = Boolean.FALSE;
        if (ApplicationThreadLocals.getUserId() != null) {
            final User currentUser = userService.getUserById(ApplicationThreadLocals.getUserId());
            if (currentUser.getRoles().isEmpty() && securityUtils.getCurrentUser().getUsername().equals("anonymous"))
                citizenrole = Boolean.TRUE;
            for (final Role userrole : currentUser.getRoles())
                if (userrole != null && userrole.getName().equals(SewerageTaxConstants.ROLE_CITIZEN)) {
                    citizenrole = Boolean.TRUE;
                    break;
                }
        } else
            citizenrole = Boolean.TRUE;
        return citizenrole;
    }

    public boolean isInspectionFeeCollectionRequired() {
        final AppConfigValues inspectionFeeCollectionRqd = appConfigValuesService.getConfigValuesByModuleAndKey(
                SewerageTaxConstants.MODULE_NAME, SewerageTaxConstants.APPCONFIG_COLLECT_INSPECTIONFEE).get(0);
        return inspectionFeeCollectionRqd != null && inspectionFeeCollectionRqd.getValue() != null
                && inspectionFeeCollectionRqd.getValue().equals("YES");
    }

    public List<Role> getLoginUserRoles() {

        final List<Role> roleList = new ArrayList<>();
        if (ApplicationThreadLocals.getUserId() != null) {
            final User currentUser = userService.getUserById(ApplicationThreadLocals.getUserId());
            for (final Role userrole : currentUser.getRoles())
                roleList.add(userrole);
        }
        return roleList;
    }

    public String getMunicipalityName() {
        return ApplicationThreadLocals.getMunicipalityName();
    }

    /*
     * public SewerageDemandConnection getCurrentDemand(final SewerageApplicationDetails sewerageApplicationDetails) {
     * SewerageDemandConnection seweragedemandConnection = null; for (final SewerageDemandConnection sdc :
     * sewerageApplicationDetails.getDemandConnections()) if
     * (sdc.getDemand().getIsHistory().equalsIgnoreCase(SewerageTaxConstants.DEMANDISHISTORY)) { seweragedemandConnection = sdc;
     * break; } return seweragedemandConnection; }
     */

    /**
     * @description returns list of installments from the given date to till date
     * @param currDate
     * @return
     */
    public List<Installment> getInstallmentsForCurrYear(final Date currDate) {
        final Module module = moduleService.getModuleByName(SewerageTaxConstants.MODULE_NAME);
        return installmentDao.getAllInstallmentsByModuleAndStartDate(module, currDate);
    }

    public List<Installment> getInstallmentsByModuledescendingorder(final Module module, final int year) {
        return installmentDao
                .getInstallmentsByModuleAndInstallmentYearOrderByInstallmentYearDescending(
                        moduleService.getModuleByName(SewerageTaxConstants.MODULE_NAME), year);
    }

    public Set<FileStoreMapper> addToFileStore(final MultipartFile[] files) {
        if (ArrayUtils.isNotEmpty(files))
            return Arrays
                    .asList(files)
                    .stream()
                    .filter(file -> !file.isEmpty())
                    .map(file -> {
                        try {
                            return fileStoreService.store(file.getInputStream(), file.getOriginalFilename(),
                                    file.getContentType(), SewerageTaxConstants.FILESTORE_MODULECODE);
                        } catch (final Exception e) {
                            throw new ApplicationRuntimeException("Error occurred while getting inputstream", e);
                        }
                    }).collect(Collectors.toSet());
        else
            return null;
    }

    public boolean isDonationChargeCollectionRequiredForLegacy() {
        final AppConfigValues donationChargeConfig = appConfigValuesService.getConfigValuesByModuleAndKey(
                SewerageTaxConstants.MODULE_NAME, SewerageTaxConstants.APPCONFIG_COLLECT_LEGACY_DONATIONCHARGE).get(0);
        return donationChargeConfig != null && donationChargeConfig.getValue() != null
                && "YES".equals(donationChargeConfig.getValue());
    }
}
