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
import org.apache.commons.lang3.StringUtils;
import org.egov.commons.EgwStatus;
import org.egov.commons.Installment;
import org.egov.commons.dao.EgwStatusHibernateDAO;
import org.egov.commons.dao.InstallmentDao;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.filestore.entity.FileStoreMapper;
import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.enums.BasicPropertyStatus;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.ptis.wtms.PropertyIntegrationService;
import org.egov.stms.masters.entity.SewerageApplicationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.emptySet;
import static org.egov.stms.utils.constants.SewerageTaxConstants.*;

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
                MODULE_NAME, NEWCONNECTIONALLOWEDIFPTDUE);
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

    public String getApproverName(final Long approvalPosition) {
        Assignment assignment = null;
        if (approvalPosition != null)
            assignment = assignmentService.getPrimaryAssignmentForPositionAndDate(approvalPosition, new Date());
        List<Assignment> assignmentList = new ArrayList<>();
        if (assignment == null) {
            assignmentList = assignmentService.getAssignmentsForPosition(approvalPosition, new Date());
        } else
            assignmentList.add(assignment);
        return assignmentList.isEmpty() ? "" : assignmentList.get(0).getEmployee().getName();
    }

    public Position getZonalLevelClerkForLoggedInUser(final String asessmentNumber) {
        final AssessmentDetails assessmentDetails = getAssessmentDetailsForFlag(asessmentNumber,
                PropertyExternalService.FLAG_FULL_DETAILS);
        Assignment assignmentObj = null;
        final Boundary boundaryObj = boundaryService.getBoundaryById(assessmentDetails.getBoundaryDetails()
                .getAdminWardId());
        assignmentObj = getUserPositionByZone(boundaryObj);
        return assignmentObj == null ? null : assignmentObj.getPosition();
    }

    /**
     * Getting User assignment based on designation ,department and zone boundary Reading Designation and Department from
     * appconfig values and Values should be 'Senior Assistant,Junior Assistant' for designation and
     * 'Revenue,Accounts,Administration' for department
     *
     * @param asessmentNumber   ,
     * @param assessmentDetails
     * @param boundaryObj
     * @return Assignment
     */
    public Assignment getUserPositionByZone(final Boundary boundaryObj) {
        List<Designation> designationList = getDesignationForThirdPartyUser();
        List<Department> departmentList = getDepartmentForWorkFlow();
        List<Assignment> assignment = new ArrayList<>();

        for (final Department dept : departmentList)
            for (final Designation desg : designationList) {
                assignment = assignmentService.
                        findByDepartmentDesignationAndBoundary(dept.getId(), desg.getId(), boundaryObj.getId());
                if (!assignment.isEmpty())
                    break;
            }
        return !assignment.isEmpty() ? assignment.get(0) : null;
    }

    public List<Designation> getDesignationForThirdPartyUser() {
        String designation = "";
        final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(
                MODULE_NAME, CLERKDESIGNATIONFORCSCOPERATOR);
        if (appConfigValue != null && !appConfigValue.isEmpty())
            designation = appConfigValue.get(0).getValue();
        return designationService.
                getDesignationsByNames(Arrays.asList(StringUtils.upperCase(designation).split(",")));
    }

    public List<Department> getDepartmentForWorkFlow() {
        String department = "";
        final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(
                MODULE_NAME, SEWERAGETAXWORKFLOWDEPARTEMENT);
        if (appConfigValue != null && !appConfigValue.isEmpty())
            department = appConfigValue.get(0).getValue();
        return departmentService.
                getDepartmentsByNames(Arrays.asList(StringUtils.upperCase(department).split(",")));
    }

    public String getCityCode() {
        return ApplicationThreadLocals.getCityCode();
    }

    public Boolean checkCollectionOperatorRole() {
        return securityUtils.getCurrentUser().hasRole(ROLE_COLLECTIONOPERATOR);
    }

    public Boolean hasCitizenRole() {
        return securityUtils.currentUserIsCitizen() || SecurityUtils.currentUserIsAnonymous();
    }

    public boolean isInspectionFeeCollectionRequired() {
        final AppConfigValues inspectionFeeCollectionRqd = appConfigValuesService.getConfigValuesByModuleAndKey(
                MODULE_NAME, APPCONFIG_COLLECT_INSPECTIONFEE).get(0);
        return inspectionFeeCollectionRqd != null && inspectionFeeCollectionRqd.getValue() != null
                && inspectionFeeCollectionRqd.getValue().equals("YES");
    }

    public Set<Role> getLoginUserRoles() {
        return securityUtils.getCurrentUser().getRoles();
    }

    public String getMunicipalityName() {
        return ApplicationThreadLocals.getMunicipalityName();
    }

    public List<Installment> getInstallmentsForCurrYear(final Date currDate) {
        return installmentDao.getAllInstallmentsByModuleAndStartDate(getModule(), currDate);
    }

    public List<Installment> getInstallmentsByModuleDesc(final int year) {
        return installmentDao
                .getInstallmentsByModuleAndInstallmentYearOrderByInstallmentYearDescending(
                        getModule(), year);
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
                                    file.getContentType(), FILESTORE_MODULECODE);
                        } catch (final Exception e) {
                            throw new ApplicationRuntimeException("Error occurred while getting inputstream", e);
                        }
                    }).collect(Collectors.toSet());
        else
            return emptySet();
    }

    public boolean isDonationChargeCollectionRequiredForLegacy() {
        final AppConfigValues donationChargeConfig = appConfigValuesService.getConfigValuesByModuleAndKey(
                MODULE_NAME, APPCONFIG_COLLECT_LEGACY_DONATIONCHARGE).get(0);
        return donationChargeConfig != null && donationChargeConfig.getValue() != null
                && "YES".equals(donationChargeConfig.getValue());
    }

    public Integer getSlaAppConfigValues(SewerageApplicationType applicationType) {
        String keyName = getSlaKeyName(applicationType);
        final List<AppConfigValues> appConfigValues = appConfigValuesService.getConfigValuesByModuleAndKey(MODULE_NAME, keyName);
        return appConfigValues.isEmpty() ? 0 : Integer.valueOf(appConfigValues.get(0).getValue());
    }

    private String getSlaKeyName(SewerageApplicationType applicationType) {
        if (applicationType.getCode() == NEWSEWERAGECONNECTION)
            return SLAFORNEWSEWERAGECONNECTION;
        else
            return applicationType.getCode() == CHANGEINCLOSETS ? SLAFORCHANGEINCLOSET : SLAFORCLOSURECONNECTION;
    }

    public AppConfigValues getAppConfigValues(final String keyName) {
        final List<AppConfigValues> appConfigValues = appConfigValuesService.getConfigValuesByModuleAndKey(MODULE_NAME, keyName);
        return !appConfigValues.isEmpty() ? appConfigValues.get(0) : null;
    }

    public Module getModule() {
        return moduleService.getModuleByName(MODULE_NAME);
    }

    public Boolean isEmployee(final User user) {
        List<String> appConfigValueList = getThirdPartyUserRoles().stream().map(AppConfigValues::getValue)
                .collect(Collectors.toList());
        return !user.hasAnyRole(Arrays.toString(appConfigValueList.toArray()));
    }

    public Boolean isCitizenPortalUser(final User user) {
        return user.hasRole(ROLE_CITIZEN);
    }

    public List<AppConfigValues> getThirdPartyUserRoles() {
        final List<AppConfigValues> appConfigValueList = appConfigValuesService.getConfigValuesByModuleAndKey(
                MODULE_NAME, SEWERAGEROLEFORNONEMPLOYEE);
        return appConfigValueList.isEmpty() ? Collections.emptyList() : appConfigValueList;
    }

    public boolean isReassignEnabled() {
        final List<AppConfigValues> appConfigValues = appConfigValuesService.getConfigValuesByModuleAndKey(
                MODULE_NAME,
                APPCONFKEY_REASSIGN_BUTTONENABLED);
        return !appConfigValues.isEmpty() && "YES".equals(appConfigValues.get(0).getValue());
    }
}
