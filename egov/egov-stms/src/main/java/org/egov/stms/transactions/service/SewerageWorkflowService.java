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

package org.egov.stms.transactions.service;

import static org.apache.commons.lang.StringUtils.upperCase;
import static org.egov.infra.persistence.entity.enums.UserType.EMPLOYEE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.MODULE_NAME;
import static org.egov.stms.utils.constants.SewerageTaxConstants.SEWERAGEROLEFORNONEMPLOYEE;
import static org.egov.stms.utils.constants.SewerageTaxConstants.SEWERAGE_DEPARTEMENT_FOR_REASSIGNMENT;
import static org.egov.stms.utils.constants.SewerageTaxConstants.SEWERAGE_WORKFLOWDEPARTEMENT_FOR_CSCOPERATOR;
import static org.egov.stms.utils.constants.SewerageTaxConstants.SEWERAGE_WORKFLOWDESIGNATION_FOR_CSCOPERATOR;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.BoundaryService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.exception.ApplicationValidationException;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.egov.ptis.domain.model.AssessmentDetails;
import org.egov.ptis.domain.model.enums.BasicPropertyStatus;
import org.egov.ptis.domain.service.property.PropertyExternalService;
import org.egov.ptis.wtms.PropertyIntegrationService;
import org.egov.stms.transactions.entity.SewerageApplicationDetails;
import org.egov.stms.utils.constants.SewerageTaxConstants;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional(readOnly = true)
public class SewerageWorkflowService {

    @Autowired
    protected AssignmentService assignmentService;
    @Autowired
    private AppConfigValueService appConfigValuesService;
    @Autowired
    private DesignationService designationService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private BoundaryService boundaryService;
    @Autowired
    @Qualifier("propertyIntegrationServiceImpl")
    private PropertyIntegrationService propertyIntegrationService;
    @PersistenceContext
    private EntityManager entityManager;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    public Assignment getWorkFlowInitiator(final SewerageApplicationDetails sewerageApplicationDetails) {
        Assignment wfInitiator = null;
        List<Assignment> assignmentList;
        if (sewerageApplicationDetails != null)
            if (sewerageApplicationDetails.getState() != null
                    && sewerageApplicationDetails.getState().getInitiatorPosition() != null) {
                wfInitiator = getUserAssignmentByPassingPositionAndUser(sewerageApplicationDetails
                        .getCreatedBy(), sewerageApplicationDetails.getState().getInitiatorPosition());

                if (wfInitiator == null) {
                    assignmentList = assignmentService
                            .getAssignmentsForPosition(sewerageApplicationDetails.getState().getInitiatorPosition().getId(),
                                    new Date());
                    wfInitiator = getActiveAssignment(assignmentList);
                }
            } else if (!EMPLOYEE.equals(sewerageApplicationDetails.getCreatedBy().getType()))
                wfInitiator = getUserAssignment(sewerageApplicationDetails);
            else {
                wfInitiator = getWfInitiator(sewerageApplicationDetails.getCreatedBy());
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

    private Assignment getActiveAssignment(final List<Assignment> assignments) {
        return assignments.stream().filter(assignment -> assignment.getEmployee().isActive()).findFirst()
                .orElse(null);
    }

    private Assignment getUserAssignment(final SewerageApplicationDetails sewerageApplicationDetails) {
        return getMappedAssignmentForCscOperator(
                sewerageApplicationDetails.getConnectionDetail().getPropertyIdentifier());
    }

    public Assignment getMappedAssignmentForCscOperator(final String asessmentNumber) {

        final AssessmentDetails assessmentDetails = propertyIntegrationService.getAssessmentDetailsForFlag(asessmentNumber,
                PropertyExternalService.FLAG_FULL_DETAILS, BasicPropertyStatus.ALL);
        final Boundary boundaryObj = boundaryService
                .getBoundaryById(assessmentDetails.getBoundaryDetails().getAdminWardId());
        return getUserPositionByZone(boundaryObj);
    }

    private Assignment getUserPositionByZone(final Boundary boundaryObj) {
        final String[] departmentList = getDepartmentForCscOperatorWorkFlow().split(",");
        final String[] designationList = getDesignationForCscOperatorWorkFlow().split(",");
        List<Assignment> assignmentList = new ArrayList<>();
        for (final String dept : departmentList) {
            for (final String designation : designationList) {
                assignmentList = assignmentService.findByDepartmentDesignationAndBoundary(
                        departmentService.getDepartmentByName(dept).getId(),
                        designationService.getDesignationByName(designation).getId(), boundaryObj.getId());
                if (assignmentList.isEmpty())
                    assignmentList = getAssignmentsByBoundary(boundaryObj, assignmentList, dept, designation);

                if (!assignmentList.isEmpty())
                    break;
            }
            if (!assignmentList.isEmpty())
                break;
        }
        return assignmentList.isEmpty() ? null : assignmentList.get(0);
    }

    private List<Assignment> getAssignmentsByBoundary(Boundary boundaryObj, List<Assignment> assignmentList, String dept, String desg) {

        Boundary zoneBndry = boundaryObj.getParent();
        if (zoneBndry != null && zoneBndry.getBoundaryType().getName().equals("Zone")) {
            assignmentList = getAssignments(dept, desg, zoneBndry);
            if (assignmentList.isEmpty()) {
                Boundary cityBndry = boundaryObj.getParent().getParent();
                if (cityBndry != null && cityBndry.getBoundaryType().getName().equals("City"))
                    assignmentList = getAssignments(dept, desg, cityBndry);
            }
        }
        if (assignmentList.isEmpty()) {
            Boundary city = boundaryObj.getParent();
            if (city != null && city.getBoundaryType().getName().equals("City"))
                assignmentList = getAssignments(dept, desg, city);
        }

        return assignmentList;
    }

    private List<Assignment> getAssignments(String dept, String desg, Boundary zoneBndry) {
        return assignmentService.findByDeptDesgnAndParentAndActiveChildBoundaries(
                departmentService.getDepartmentByName(dept).getId(),
                designationService.getDesignationByName(desg).getId(), zoneBndry.getId());
    }

    private Assignment getWfInitiator(User connectionCreatedBy) {
        List<String> designationNames = Arrays.asList(upperCase(getDesignationForCscOperatorWorkFlow()).split(","));
        Department department = departmentService.getDepartmentByName(getDepartmentForCscOperatorWorkFlow());
        List<Designation> designationList = designationService
                .getDesignationsByNames(designationNames);
        List<Assignment> assignmentList = assignmentService
                .findByDepartmentDesignationsAndGivenDate(department.getId(),
                        designationList.stream().map(Designation::getId).collect(Collectors.toList()),
                        new Date());
        return assignmentList.stream()
                .filter(assignment -> connectionCreatedBy.equals(assignment.getEmployee())).findAny()
                .orElseThrow(() -> new ApplicationValidationException("error.initiator.undefined"));
    }

    public String getDepartmentForCscOperatorWorkFlow() {
        final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(MODULE_NAME,
                SEWERAGE_WORKFLOWDEPARTEMENT_FOR_CSCOPERATOR);
        return appConfigValue.isEmpty() ? null : appConfigValue.get(0).getValue();
    }

    public String getDesignationForCscOperatorWorkFlow() {
        final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(MODULE_NAME,
                SEWERAGE_WORKFLOWDESIGNATION_FOR_CSCOPERATOR);
        return appConfigValue.isEmpty() ? null : appConfigValue.get(0).getValue();
    }

    public Boolean isCscOperator(final User user) {
        final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(MODULE_NAME,
                SEWERAGEROLEFORNONEMPLOYEE);
        final String rolesForNonEmployee = !appConfigValue.isEmpty() ? appConfigValue.get(0).getValue() : null;
        for (final Role role : user.getRoles())
            if (role != null && rolesForNonEmployee != null && role.getName().equalsIgnoreCase(rolesForNonEmployee))
                return true;
        return false;
    }
    
	public boolean isWardSecretaryUser(final User user) {
		for (Role role : user.getRoles())
			if (SewerageTaxConstants.WARDSECRETARY_OPERATOR_ROLE.equalsIgnoreCase(role.getName()))
				return true;
		return false;
	}

    public String getDepartmentForReassignment() {
        final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(MODULE_NAME,
                SEWERAGE_DEPARTEMENT_FOR_REASSIGNMENT);
        return !appConfigValue.isEmpty() ? appConfigValue.get(0).getValue() : null;
    }
}
