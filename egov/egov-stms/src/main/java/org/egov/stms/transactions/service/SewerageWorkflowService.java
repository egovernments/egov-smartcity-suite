/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2017  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
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
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.stms.transactions.service;

import static org.egov.stms.utils.constants.SewerageTaxConstants.MODULE_NAME;
import static org.egov.stms.utils.constants.SewerageTaxConstants.SEWERAGEROLEFORNONEMPLOYEE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.SEWERAGE_WORKFLOWDEPARTEMENT_FOR_CSCOPERATOR;
import static org.egov.stms.utils.constants.SewerageTaxConstants.SEWERAGE_WORKFLOWDESIGNATION_FOR_CSCOPERATOR;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.pims.commons.Position;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.enums.BasicPropertyStatus;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.utils.constants.SewerageTaxConstants;
import org.egov.wtms.utils.PropertyExtnUtils;
import org.egov.wtms.utils.constants.WaterTaxConstants;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SewerageWorkflowService {

    @Autowired
    private AppConfigValueService appConfigValuesService;
    @Autowired
    private DesignationService designationService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    protected AssignmentService assignmentService;
    @Autowired
    private BoundaryService boundaryService;
    @Autowired
    private PropertyExtnUtils propertyExtnUtils;
    @PersistenceContext
    private EntityManager entityManager;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    /**
     * Checks whether user is an employee or not
     *
     * @param user
     * @return
     */
    public Boolean isEmployee(final User user) {
        for (final Role role : user.getRoles())
            for (final AppConfigValues appconfig : getThirdPartyUserRoles())
                if (role != null && appconfig != null && role.getName().equals(appconfig.getValue()))
                    return false;
        return true;
    }

    /**
     * Checks for Citizen Role
     *
     * @param user
     * @return
     */
    public Boolean isCitizenPortalUser(final User user) {
        for (final Role role : user.getRoles())
            if (role != null && role.getName().equalsIgnoreCase(SewerageTaxConstants.ROLE_CITIZEN))
                return true;
        return false;
    }

    /**
     * Returns third party user roles
     *
     * @return
     */
    public List<AppConfigValues> getThirdPartyUserRoles() {
        final List<AppConfigValues> appConfigValueList = appConfigValuesService.getConfigValuesByModuleAndKey(
                MODULE_NAME, SEWERAGEROLEFORNONEMPLOYEE);
        return !appConfigValueList.isEmpty() ? appConfigValueList : Collections.emptyList();
    }

    public Assignment getMappedAssignmentForCscOperator(final String asessmentNumber) {

        final AssessmentDetails assessmentDetails = propertyExtnUtils.getAssessmentDetailsForFlag(asessmentNumber,
                PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ALL);
        Assignment assignmentObj = null;
        final Boundary boundaryObj = boundaryService
                .getBoundaryById(assessmentDetails.getBoundaryDetails().getAdminWardId());
        assignmentObj = getUserPositionByZone(boundaryObj);

        return assignmentObj;
    }

    private Assignment getUserPositionByZone(final Boundary boundaryObj) {
        final String designationStr = getDesignationForCscOperatorWorkFlow();
        final String departmentStr = getDepartmentForCscOperatorWorkFlow();
        final String[] department = departmentStr.split(",");
        final String[] designation = designationStr.split(",");
        List<Assignment> assignment = new ArrayList<>();
        for (final String dept : department) {
            for (final String desg : designation) {
                assignment = assignmentService.findByDepartmentDesignationAndBoundary(
                        departmentService.getDepartmentByName(dept).getId(),
                        designationService.getDesignationByName(desg).getId(), boundaryObj.getId());
                if (assignment.isEmpty()) {
                    // Ward->Zone
                    if (boundaryObj.getParent() != null && boundaryObj.getParent().getBoundaryType() != null
                            && boundaryObj.getParent().getBoundaryType().equals(WaterTaxConstants.BOUNDARY_TYPE_ZONE)) {
                        assignment = assignmentService.findByDeptDesgnAndParentAndActiveChildBoundaries(
                                departmentService.getDepartmentByName(dept).getId(),
                                designationService.getDesignationByName(desg).getId(), boundaryObj.getParent().getId());
                        if (assignment.isEmpty())
                            // Ward->Zone->City
                            if (boundaryObj.getParent() != null && boundaryObj.getParent().getParent() != null
                                    && boundaryObj.getParent().getParent().getBoundaryType().getName()
                                            .equals(WaterTaxConstants.BOUNDARY_TYPE_CITY))
                            assignment = assignmentService.findByDeptDesgnAndParentAndActiveChildBoundaries(
                                    departmentService.getDepartmentByName(dept).getId(),
                                    designationService.getDesignationByName(desg).getId(),
                                    boundaryObj.getParent().getParent().getId());
                    }
                    // ward->City mapp
                    if (assignment.isEmpty())
                        if (boundaryObj.getParent() != null && boundaryObj.getParent().getBoundaryType().getName()
                                .equals(WaterTaxConstants.BOUNDARY_TYPE_CITY))
                            assignment = assignmentService.findByDeptDesgnAndParentAndActiveChildBoundaries(
                                    departmentService.getDepartmentByName(dept).getId(),
                                    designationService.getDesignationByName(desg).getId(),
                                    boundaryObj.getParent().getId());
                }
                if (!assignment.isEmpty())
                    break;
            }
            if (!assignment.isEmpty())
                break;
        }
        return !assignment.isEmpty() ? assignment.get(0) : null;

    }

    private String getDepartmentForCscOperatorWorkFlow() {
        final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(MODULE_NAME,
                SEWERAGE_WORKFLOWDEPARTEMENT_FOR_CSCOPERATOR);
        return !appConfigValue.isEmpty() ? appConfigValue.get(0).getValue() : null;
    }

    private String getDesignationForCscOperatorWorkFlow() {
        final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(MODULE_NAME,
                SEWERAGE_WORKFLOWDESIGNATION_FOR_CSCOPERATOR);
        return !appConfigValue.isEmpty() ? appConfigValue.get(0).getValue() : null;
    }

    public Assignment getWorkFlowInitiator(final SewerageApplicationDetails sewerageApplicationDetails) {
        Assignment wfInitiator = null;
        List<Assignment> assignment;
        if (sewerageApplicationDetails != null)
            if (sewerageApplicationDetails.getState() != null
                    && sewerageApplicationDetails.getState().getInitiatorPosition() != null) {
                wfInitiator = getUserAssignmentByPassingPositionAndUser(sewerageApplicationDetails
                        .getCreatedBy(), sewerageApplicationDetails.getState().getInitiatorPosition());

                if (wfInitiator == null) {
                    assignment = assignmentService
                            .getAssignmentsForPosition(sewerageApplicationDetails.getState().getInitiatorPosition().getId(),
                                    new Date());
                    wfInitiator = getActiveAssignment(assignment);
                }
            } else if (!isEmployee(sewerageApplicationDetails.getCreatedBy())
                    || SewerageTaxConstants.ANONYMOUS_USER
                            .equalsIgnoreCase(sewerageApplicationDetails.getCreatedBy().getUsername())
                    || isCitizenPortalUser(sewerageApplicationDetails.getCreatedBy()))
                wfInitiator = getUserAssignment(sewerageApplicationDetails.getCreatedBy(), sewerageApplicationDetails);
            else
                wfInitiator = assignmentService.getPrimaryAssignmentForUser(sewerageApplicationDetails
                        .getCreatedBy().getId());
        return wfInitiator;
    }

    private Assignment getUserAssignment(final User user, final SewerageApplicationDetails sewerageApplicationDetails) {
        Assignment assignment;
        if (isCscOperator(user) || user.getUsername().equalsIgnoreCase("anonymous") || isCitizenPortalUser(user))
            assignment = getMappedAssignmentForCscOperator(
                    sewerageApplicationDetails.getConnectionDetail().getPropertyIdentifier());

        else
            assignment = getWorkFlowInitiator(sewerageApplicationDetails);
        return assignment;
    }

    private Assignment getActiveAssignment(final List<Assignment> assignment) {
        Assignment wfInitiator = null;
        for (final Assignment assign : assignment)
            if (assign.getEmployee().isActive()) {
                wfInitiator = assign;
                break;
            }
        return wfInitiator;
    }

    private Assignment getUserAssignmentByPassingPositionAndUser(final User user, final Position position) {

        Assignment wfInitiatorAssignment = null;

        if (user != null && position != null) {
            final List<Assignment> assignmentList = assignmentService.findByEmployeeAndGivenDate(user.getId(), new Date());
            for (final Assignment assignment : assignmentList)
                if (position.getId() == assignment.getPosition().getId())
                    wfInitiatorAssignment = assignment;
        }

        return wfInitiatorAssignment;
    }

    /**
     * Checks whether user is csc operator or not
     *
     * @param user
     * @return
     */
    public Boolean isCscOperator(final User user) {
        final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(MODULE_NAME,
                SEWERAGEROLEFORNONEMPLOYEE);
        final String rolesForNonEmployee = !appConfigValue.isEmpty() ? appConfigValue.get(0).getValue() : null;
        for (final Role role : user.getRoles())
            if (role != null && rolesForNonEmployee != null && role.getName().equalsIgnoreCase(rolesForNonEmployee))
                return true;
        return false;
    }

}
