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

package org.egov.wtms.application.service;

import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.eis.service.PositionMasterService;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.pims.commons.Position;
import org.egov.wtms.application.entity.WaterConnectionDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.egov.wtms.utils.constants.WaterTaxConstants.ENGINEERING_CODE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.JUNIOR_ASSISTANT_DESIGN_CODE;
import static org.egov.wtms.utils.constants.WaterTaxConstants.SENIOR_ASSISTANT_DESIGN_CODE;

@Service
public class ReassignmentService {

    private static final String REASSIGNMENT = "REASSIGNMENT";

    @Autowired
    private AppConfigValueService appConfigValuesService;

    @Autowired
    private PositionMasterService positionMasterService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private WaterConnectionDetailsService waterConnectionDetailsService;

    public Map<String, String> employeePositionMap() {
        final List<Assignment> assignmentsList = assignmentService.findByDepartmentCodeAndDesignationCode(
                ENGINEERING_CODE,
                Arrays.asList(JUNIOR_ASSISTANT_DESIGN_CODE, SENIOR_ASSISTANT_DESIGN_CODE));

        final Long positionId = positionMasterService.getCurrentPositionForUser(ApplicationThreadLocals.getUserId()).getId();
        assignmentsList.removeIf(assignment -> assignment.getPosition().getId().equals(positionId));

        return assignmentsList
                .stream()
                .collect(Collectors.toMap(assignment -> assignment.getPosition().getId().toString(),
                        assignment -> new StringBuffer().append(assignment.getEmployee().getName())
                                .append('-').append(assignment.getPosition().getName()).toString()));

    }

    @Transactional
    public Boolean updateReassignedWaterChargeApplication(final Long approverPositionId, final Long connectionDetailId) {
        final WaterConnectionDetails waterConnectionDetails = waterConnectionDetailsService.findBy(connectionDetailId);
        final Position position = positionMasterService.getPositionById(approverPositionId);
        if (waterConnectionDetails != null) {
            waterConnectionDetails.changeProcessOwner(position);
            waterConnectionDetails.changeProcessInitiator(position);
            waterConnectionDetailsService.save(waterConnectionDetails);
            waterConnectionDetailsService.updateIndexes(waterConnectionDetails,
                    waterConnectionDetails.getSource() != null ? waterConnectionDetails.getSource().toString() : null);
        }
        return true;
    }


}
