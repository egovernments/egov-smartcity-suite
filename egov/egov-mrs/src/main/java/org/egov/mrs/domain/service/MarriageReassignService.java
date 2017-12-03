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
package org.egov.mrs.domain.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.mrs.application.MarriageConstants;
import org.egov.mrs.application.service.workflow.RegistrationWorkflowService;
import org.egov.mrs.domain.entity.MarriageReassignInfo;
import org.egov.mrs.domain.entity.MarriageRegistration;
import org.egov.mrs.domain.entity.ReIssue;
import org.egov.mrs.service.es.MarriageRegistrationUpdateIndexesService;
import org.egov.mrs.service.es.ReIssueCertificateUpdateIndexesService;
import org.egov.pims.commons.Designation;
import org.egov.pims.commons.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MarriageReassignService {

    @Autowired
    private MarriageRegistrationService marriageRegistrationService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private MarriageRegistrationUpdateIndexesService marriageRegistrationUpdateIndexesService;

    @Autowired
    private ReIssueCertificateUpdateIndexesService reiSsueUpdateIndexesService;

    @Autowired
    private ReIssueService reIssueService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private RegistrationWorkflowService registrationWorkFlowService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DesignationService designationService;

    public User getLoggedInUser() {
        return securityUtils.getCurrentUser();
    }

    public String getStateObject(final MarriageReassignInfo marriageReassignInfo, final Position position) {
        MarriageRegistration registration;
        ReIssue reissue;
        String applicationType = marriageReassignInfo.getStateType();
        if (MarriageConstants.STATETYPE_REGISTRATION.equalsIgnoreCase(applicationType)) {
            registration = marriageRegistrationService.findById(marriageReassignInfo.getApplicationId());
            registration.transition().progressWithStateCopy().withOwner(position).withInitiator(position);
            marriageRegistrationService.update(registration);
            marriageRegistrationUpdateIndexesService.updateIndexes(registration);
            return registration.getApplicationNo();
        } else if (MarriageConstants.STATETYPE_REISSUE.equalsIgnoreCase(applicationType)) {
            reissue = reIssueService.get(marriageReassignInfo.getApplicationId());
            reissue.transition().progressWithStateCopy().withOwner(position).withInitiator(position);
            reIssueService.update(reissue);
            reiSsueUpdateIndexesService.updateReIssueAppIndex(reissue);
            return reissue.getApplicationNo();
        } else {
            return StringUtils.EMPTY;
        }
    }

    public Map<String, String> employeePositionMap() {
        final String designationStr = registrationWorkFlowService.getDesignationForCscOperatorWorkFlow();
        final String departmentStr = registrationWorkFlowService.getDepartmentForCscOperatorWorkFlow();
        Department dept = departmentService.getDepartmentByName(departmentStr.split(",")[0]);
        List<Long> desigList = designationService.getDesignationsByNames(Arrays.asList(designationStr.toUpperCase().split(",")))
                .stream()
                .map(Designation::getId).collect(Collectors.toList());
        List<Assignment> assignments = assignmentService.findByDepartmentDesignationsAndGivenDate(dept.getId(),
                desigList, new Date());
        assignments.removeAll(assignmentService.getAllAssignmentsByEmpId(ApplicationThreadLocals.getUserId()));
        return assignments
                .stream()
                .collect(Collectors.toMap(assignment -> assignment.getPosition().getId().toString(),
                        assignment -> new StringBuffer().append(assignment.getEmployee().getName())
                                .append("-").append(assignment.getPosition().getName()).toString(),
                        (posId1, posId2) -> posId1));
    }
}
