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

package org.egov.eis.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.eis.entity.Assignment;
import org.egov.eis.entity.EmployeeGrievance;
import org.egov.eis.entity.enums.EmployeeGrievanceStatus;
import org.egov.eis.repository.EmployeeGrievanceRepository;
import org.egov.eis.utils.constants.EisConstants;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.workflow.matrix.entity.WorkFlowMatrix;
import org.egov.infra.workflow.service.SimpleWorkflowService;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class EmployeeGrievanceService {

    private final EmployeeGrievanceRepository employeeGrievanceRepository;
    @Autowired
    @Qualifier("workflowService")
    private SimpleWorkflowService<EmployeeGrievance> employeeGrievanceWorkflowService;
    @Autowired
    private DesignationService designationService;
    @Autowired
    protected AssignmentService assignmentService;
    @Autowired
    private SecurityUtils securityUtils;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public EmployeeGrievanceService(final EmployeeGrievanceRepository employeeGrievanceRepository) {
        this.employeeGrievanceRepository = employeeGrievanceRepository;
    }

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Transactional
    public EmployeeGrievance create(final EmployeeGrievance employeeGrievance) {
        return employeeGrievanceRepository.save(employeeGrievance);
    }

    @Transactional
    public EmployeeGrievance update(final EmployeeGrievance employeeGrievance) {
        return employeeGrievanceRepository.save(employeeGrievance);
    }

    public List<EmployeeGrievance> findAll() {
        return employeeGrievanceRepository.findAll(new Sort(Sort.Direction.ASC, "grievanceNumber"));
    }

    public EmployeeGrievance findOne(Long id) {
        return employeeGrievanceRepository.findOne(id);
    }

    public List<EmployeeGrievance> search(EmployeeGrievance employeeGrievance) {
        final Criteria criteria = getCurrentSession().createCriteria(
                EmployeeGrievance.class);
        criteria.createAlias("employee", "emp");
        if (employeeGrievance.getGrievanceNumber() != null) {
            String number = "%" + employeeGrievance.getGrievanceNumber() + "%";
            criteria.add(Restrictions.ilike("grievanceNumber", number));
        }

        if (employeeGrievance.getStatus() != null)
            criteria.add(Restrictions.eq("status", employeeGrievance.getStatus()));
        if (employeeGrievance.getEmployeeGrievanceType() != null)
            criteria.add(Restrictions.eq("employeeGrievanceType", employeeGrievance.getEmployeeGrievanceType()));
        if (employeeGrievance.getEmployee() != null && employeeGrievance.getEmployee().getCode() != null) {
            String empCode = "%" + employeeGrievance.getEmployee().getCode() + "%";
            criteria.add(Restrictions.ilike("emp.code", empCode));
        }
        if (employeeGrievance.getEmployee() != null && employeeGrievance.getEmployee().getName() != null) {
            String empName = "%" + employeeGrievance.getEmployee().getName() + "%";
            criteria.add(Restrictions.ilike("emp.name", empName));
        }
        return criteria.list();
    }

    public void prepareWorkFlowTransition(EmployeeGrievance employeeGrievance) {

        final User user = securityUtils.getCurrentUser();
        String stateValue;
        WorkFlowMatrix wfmatrix;
        Designation des = designationService.getDesignationByName("Commissioner");
        List<Assignment> assignment = assignmentService.getAllActiveAssignments(des.getId());
        Position assigneePosition = assignment.get(0).getPosition();
        if (employeeGrievance != null)

            if (employeeGrievance.getStatus().equals(EmployeeGrievanceStatus.REGISTERED)) {

                wfmatrix = employeeGrievanceWorkflowService.getWfMatrix(employeeGrievance.getStateType(), null, null, null,
                        "Registered", null);
                employeeGrievance.transition()
                        .start()
                        .withStateValue("Registered")
                        .withOwner(assigneePosition).withNextAction(wfmatrix.getNextAction()).withDateInfo(DateUtils.now())
                        .withNatureOfTask(EisConstants.EMPLOYEE_GRIEVANCE).withInitiator(assigneePosition)
                        .withSenderName(user.getUsername() + ":" + user.getName());
            }

            else if (employeeGrievance.getStatus().equals(EmployeeGrievanceStatus.INPROCESS)) {
                stateValue = EisConstants.WORKFLOW_STATE_INPROCESS;

                wfmatrix = employeeGrievanceWorkflowService.getWfMatrix(employeeGrievance.getStateType(), null,
                        null, null, employeeGrievance.getCurrentState().getValue(), null);

                if (stateValue.isEmpty())
                    stateValue = wfmatrix.getNextState();

                employeeGrievance.transition().progressWithStateCopy().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments("")
                        .withStateValue(stateValue).withDateInfo(DateUtils.now())
                        .withOwner(assigneePosition)
                        .withNextAction(wfmatrix.getNextAction())
                        .withNatureOfTask(EisConstants.EMPLOYEE_GRIEVANCE);

            }

            else if (employeeGrievance.getStatus().equals(EmployeeGrievanceStatus.REDRESSED)) {
                wfmatrix = employeeGrievanceWorkflowService.getWfMatrix(employeeGrievance.getStateType(), null,
                        null, null, employeeGrievance.getCurrentState().getValue(), null);
                stateValue = EisConstants.WORKFLOW_STATE_REDRESSED;
                if (stateValue.isEmpty())
                    stateValue = wfmatrix.getNextState();

                employeeGrievance.transition().end().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments("")
                        .withStateValue(stateValue).withDateInfo(DateUtils.now())
                        .withNextAction(wfmatrix.getNextAction())
                        .withNatureOfTask(EisConstants.EMPLOYEE_GRIEVANCE);
            } else if (employeeGrievance.getStatus().equals(EmployeeGrievanceStatus.REJECTED)) {
                stateValue = EisConstants.WORKFLOW_STATE_CANCELLED;
                employeeGrievance.transition().end().withSenderName(user.getUsername() + "::" + user.getName())
                        .withComments("")
                        .withStateValue(stateValue).withDateInfo(DateUtils.now())
                        .withNextAction("")
                        .withNatureOfTask(EisConstants.EMPLOYEE_GRIEVANCE);
            }
    }

    public String getApproverName(final Long approvalPosition) {
        Assignment assignment = null;
        List<Assignment> asignList;
        if (approvalPosition != null)
            assignment = assignmentService.getPrimaryAssignmentForPositionAndDate(approvalPosition, DateUtils.now());
        if (assignment != null) {
            asignList = new ArrayList<>();
            asignList.add(assignment);
        } else
            asignList = assignmentService.getAssignmentsForPosition(approvalPosition, DateUtils.now());
        return !asignList.isEmpty() ? asignList.get(0).getEmployee().getName() : "";
    }
}