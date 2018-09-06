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

package org.egov.tl.utils;

import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.DesignationService;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.Module;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.admin.master.service.ModuleService;
import org.egov.infra.persistence.entity.enums.UserType;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.tl.entity.LicenseAppType;
import org.egov.tl.service.LicenseConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.egov.tl.utils.Constants.COMMISSIONER_DESGN;
import static org.egov.tl.utils.Constants.NEW_APPTYPE_CODE;
import static org.egov.tl.utils.Constants.RENEW_APPTYPE_CODE;
import static org.egov.tl.utils.Constants.TRADE_LICENSE;

@Service
public class LicenseUtils {
    @Autowired
    private ModuleService moduleService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DesignationService designationService;

    @Autowired
    private LicenseConfigurationService licenseConfigurationService;

    public Module getModule() {
        return moduleService.getModuleByName(TRADE_LICENSE);
    }

    public List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    public Assignment getCommissionerAssignment() {
        List<Assignment> commissionerAssignments = assignmentService.findPrimaryAssignmentForDesignationName(COMMISSIONER_DESGN);
        if (commissionerAssignments.isEmpty()) {
            commissionerAssignments = assignmentService.getAllActiveAssignments(
                    designationService.getDesignationByName(COMMISSIONER_DESGN).getId());
        }
        return commissionerAssignments.isEmpty() ? null : commissionerAssignments.get(0);
    }

    public Integer getSlaForAppType(LicenseAppType licenseAppType) {
        if (NEW_APPTYPE_CODE.equals(licenseAppType.getCode()))
            return licenseConfigurationService.getNewAppTypeSla();
        else if (RENEW_APPTYPE_CODE.equals(licenseAppType.getCode()))
            return licenseConfigurationService.getRenewAppTypeSla();
        else
            return licenseConfigurationService.getClosureAppTypeSla();
    }

    public String getApplicationSenderName(UserType userType, String userName, String applicantName) {
        return SecurityUtils.currentUserIsAnonymous() || UserType.CITIZEN == userType ? applicantName : userName;
    }
}