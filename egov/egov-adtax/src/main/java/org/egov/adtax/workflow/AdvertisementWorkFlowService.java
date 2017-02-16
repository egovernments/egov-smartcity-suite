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

package org.egov.adtax.workflow;

import static org.egov.adtax.utils.constants.AdvertisementTaxConstants.ADTAX_ROLEFORNONEMPLOYEE;
import static org.egov.adtax.utils.constants.AdvertisementTaxConstants.ADTAX_WORKFLOWDEPARTEMENT;
import static org.egov.adtax.utils.constants.AdvertisementTaxConstants.ADTAX_WORKFLOWDEPARTEMENT_FOR_CSCOPERATOR;
import static org.egov.adtax.utils.constants.AdvertisementTaxConstants.ADTAX_WORKFLOWDESIGNATION;
import static org.egov.adtax.utils.constants.AdvertisementTaxConstants.ADTAX_WORKFLOWDESIGNATION_FOR_CSCOPERATOR;
import static org.egov.adtax.utils.constants.AdvertisementTaxConstants.MODULE_NAME;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.adtax.entity.AdvertisementPermitDetail;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.pims.commons.Position;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AdvertisementWorkFlowService {

    @Autowired
    private AppConfigValueService appConfigValuesService;
    @Autowired
    private DesignationService designationService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    protected AssignmentService assignmentService;

    @PersistenceContext
    private EntityManager entityManager;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    /**
     * Returns Designation for third party user
     *
     * @return
     */
    public String getDesignationForThirdPartyUser() {
        String appConfigKey = ADTAX_WORKFLOWDESIGNATION;
        final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(MODULE_NAME,
                appConfigKey);
        return !appConfigValue.isEmpty() ? appConfigValue.get(0).getValue() : null;
    }

    /**
     * Returns Department for property tax workflow
     *
     * @return
     */
    public String getDepartmentForWorkFlow() {
        final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(MODULE_NAME,
                ADTAX_WORKFLOWDEPARTEMENT);
        return !appConfigValue.isEmpty() ? appConfigValue.get(0).getValue() : null;
    }

    /**
     * Checks whether user is an employee or not
     *
     * @param user
     * @return
     */
    public Boolean isEmployee(final User user) {
        for (final Role role : user.getRoles()) {
            for (final AppConfigValues appconfig : getThirdPartyUserRoles()) {
                if (role != null && appconfig != null && role.getName().equals(appconfig.getValue()))
                    return false;
            }
        }
        return true;
    }

    /**
     * Returns third party user roles
     *
     * @return
     */
    public List<AppConfigValues> getThirdPartyUserRoles() {
        final List<AppConfigValues> appConfigValueList = appConfigValuesService.getConfigValuesByModuleAndKey(
                MODULE_NAME, ADTAX_ROLEFORNONEMPLOYEE);
        return !appConfigValueList.isEmpty() ? appConfigValueList : Collections.emptyList();
    }

    /**
     * Checks whether user is csc operator or not
     *
     * @param user
     * @return
     */
    public Boolean isCscOperator(final User user) {
        final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(MODULE_NAME,
                ADTAX_ROLEFORNONEMPLOYEE);
        String rolesForNonEmployee = !appConfigValue.isEmpty() ? appConfigValue.get(0).getValue() : null;
        for (final Role role : user.getRoles()) {
            if (role != null && rolesForNonEmployee != null && role.getName().equalsIgnoreCase(rolesForNonEmployee))
                return true;
        }
        return false;
    }

    /**
     * Returns Designation for property tax csc operator workflow
     *
     * @return
     */
    public String getDesignationForCscOperatorWorkFlow() {
        final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(MODULE_NAME,
                ADTAX_WORKFLOWDESIGNATION_FOR_CSCOPERATOR);
        return !appConfigValue.isEmpty() ? appConfigValue.get(0).getValue() : null;
    }

    /**
     * Returns Department for property tax csc operator workflow
     *
     * @return
     */
    public String getDepartmentForCscOperatorWorkFlow() {
        final List<AppConfigValues> appConfigValue = appConfigValuesService.getConfigValuesByModuleAndKey(MODULE_NAME,
                ADTAX_WORKFLOWDEPARTEMENT_FOR_CSCOPERATOR);
        return !appConfigValue.isEmpty() ? appConfigValue.get(0).getValue() : null;
    }

    public Assignment getAssignmentByDeptDesigElecWard(final AdvertisementPermitDetail advertisementPermitDetail) {
        final String designationStr = getDesignationForCscOperatorWorkFlow();
        final String departmentStr = getDepartmentForCscOperatorWorkFlow();
        final String[] department = departmentStr.split(",");
        final String[] designation = designationStr.split(",");
        List<Assignment> assignment = new ArrayList<>();
        for (final String dept : department) {
            for (final String desg : designation) {
                Long deptId = departmentService.getDepartmentByName(dept).getId();
                Long desgId = designationService.getDesignationByName(desg).getId();
                Long boundaryId = advertisementPermitDetail.getAdvertisement() != null
                        && advertisementPermitDetail.getAdvertisement().getElectionWard() != null
                                ? advertisementPermitDetail.getAdvertisement().getElectionWard().getId() : null;

                if (boundaryId == null)
                    assignment = assignmentService.findByDepartmentAndDesignation(deptId, desgId);
                else

                    assignment = assignmentService.findAssignmentByDepartmentDesignationAndBoundary(deptId, desgId,
                            boundaryId);
                if (!assignment.isEmpty())
                    break;
            }
            if (!assignment.isEmpty())
                break;
        }
        return !assignment.isEmpty() ? assignment.get(0) : null;
    }

    /**
     * Getting User assignment based on designation ,department and zone boundary Reading Designation and Department from
     * appconfig values and Values should be 'Senior Assistant,Junior Assistant' for designation and
     * 'Revenue,Accounts,Administration' for department
     *
     * @param basicProperty
     * @return
     */
    public Assignment getUserPositionByZone(final AdvertisementPermitDetail advertisementPermitDetail) {
        final String designationStr = getDesignationForThirdPartyUser();
        final String departmentStr = getDepartmentForWorkFlow();
        final String[] department = departmentStr.split(",");
        final String[] designation = designationStr.split(",");
        List<Assignment> assignment = new ArrayList<>();
        for (final String dept : department) {
            for (final String desg : designation) {
                assignment = assignmentService.findByDepartmentAndDesignation(departmentService
                        .getDepartmentByName(dept).getId(), designationService.getDesignationByName(desg).getId());
                if (!assignment.isEmpty())
                    break;
            }
            if (!assignment.isEmpty())
                break;
        }
        return !assignment.isEmpty() ? assignment.get(0) : null;
    }

    public Assignment getMappedAssignmentForCscOperator(final AdvertisementPermitDetail advertisementPermitDetail) {
        Assignment assignment;
        assignment = getAssignmentByDeptDesigElecWard(advertisementPermitDetail);

        if (assignment == null)
            assignment = getUserPositionByZone(advertisementPermitDetail);
        return assignment;
    }

    public Assignment getWorkFlowInitiator(final AdvertisementPermitDetail advertisementPermitDetail) {
        Assignment wfInitiator = null;
        List<Assignment> assignment;
        if (advertisementPermitDetail != null) {
            if (advertisementPermitDetail.getState() != null
                    && advertisementPermitDetail.getState().getInitiatorPosition() != null) {
                wfInitiator = getUserAssignmentByPassingPositionAndUser(advertisementPermitDetail
                        .getCreatedBy(), advertisementPermitDetail.getState().getInitiatorPosition());

                if (wfInitiator == null) {
                    assignment = assignmentService
                            .getAssignmentsForPosition(advertisementPermitDetail.getState().getInitiatorPosition().getId(),
                                    new Date());
                    wfInitiator = getActiveAssignment(assignment);
                }
            } else {

                if (!isEmployee(advertisementPermitDetail.getCreatedBy())) {
                    wfInitiator = getUserAssignment(advertisementPermitDetail.getCreatedBy(), advertisementPermitDetail);
                } else {
                    wfInitiator = assignmentService.getPrimaryAssignmentForUser(advertisementPermitDetail
                            .getCreatedBy().getId());
                }
            }
        }
        return wfInitiator;
    }

    private Assignment getUserAssignmentByPassingPositionAndUser(final User user, Position position) {

        Assignment wfInitiatorAssignment = null;

        if (user != null && position != null) {
            List<Assignment> assignmentList = assignmentService.findByEmployeeAndGivenDate(user.getId(), new Date());
            for (final Assignment assignment : assignmentList) {
                if (position.getId() == assignment.getPosition().getId()) {
                    wfInitiatorAssignment = assignment;
                }
            }
        }

        return wfInitiatorAssignment;
    }

    public Boolean validateUserHasSamePositionAsInitiator(final Long userId, Position position) {

        Boolean userHasSamePosition = false;

        if (userId != null && position != null) {
            List<Assignment> assignmentList = assignmentService.findByEmployeeAndGivenDate(userId, new Date());
            for (final Assignment assignment : assignmentList) {
                if (position.getId() == assignment.getPosition().getId()) {
                    userHasSamePosition = true;
                }
            }
        }
        return userHasSamePosition;
    }

    public Assignment getUserAssignment(User user, AdvertisementPermitDetail advertisementPermitDetail) {
        Assignment assignment;
        if (isCscOperator(user))
            assignment = getMappedAssignmentForCscOperator(advertisementPermitDetail);
        else
            assignment = getWorkFlowInitiator(advertisementPermitDetail);
        return assignment;
    }

    private Assignment getActiveAssignment(List<Assignment> assignment) {
        Assignment wfInitiator = null;
        for (final Assignment assign : assignment)
            if (assign.getEmployee().isActive()) {
                wfInitiator = assign;
                break;
            }
        return wfInitiator;
    }

}
