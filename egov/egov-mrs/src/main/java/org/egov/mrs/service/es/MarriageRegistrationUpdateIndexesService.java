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

package org.egov.mrs.service.es;

import static org.egov.mrs.application.MarriageConstants.APPL_INDEX_MODULE_NAME;
import static org.egov.mrs.application.MarriageConstants.APPROVED;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.commons.entity.Source;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.elasticsearch.entity.ApplicationIndex;
import org.egov.infra.elasticsearch.entity.enums.ApprovalStatus;
import org.egov.infra.elasticsearch.entity.enums.ClosureStatus;
import org.egov.infra.elasticsearch.service.ApplicationIndexService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.mrs.application.MarriageConstants;
import org.egov.mrs.domain.entity.MarriageRegistration;
import org.egov.mrs.domain.enums.MarriageCertificateType;
import org.egov.mrs.domain.service.MarriageRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MarriageRegistrationUpdateIndexesService {

    private static final Logger LOG = Logger.getLogger(MarriageRegistrationUpdateIndexesService.class);

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private ApplicationIndexService applicationIndexService;

    @Autowired
    private MarriageRegistrationIndexService marriageRegistrationIndexService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private MarriageRegistrationService marriageRegistrationService;

    public void updateIndexes(final MarriageRegistration marriageRegistration) {
        Assignment assignment;
        User user = null;
        List<Assignment> asignList = null;

        if (marriageRegistration.getState() != null && marriageRegistration.getState().getOwnerPosition() != null) {
            assignment = assignmentService.getPrimaryAssignmentForPositionAndDate(
                    marriageRegistration.getState().getOwnerPosition().getId(), new Date());
            if (assignment != null) {
                asignList = new ArrayList<>();
                asignList.add(assignment);
            } else if (assignment == null)
                asignList = assignmentService.getAssignmentsForPosition(
                        marriageRegistration.getState().getOwnerPosition().getId(), new Date());
            if (!asignList.isEmpty())
                user = userService.getUserById(asignList.get(0).getEmployee().getId());
        } else
            user = securityUtils.getCurrentUser();

        final String url = "/mrs/registration/viewapplication/" + marriageRegistration.getApplicationNo();

        ApplicationIndex applicationIndex = applicationIndexService
                .findByApplicationNumber(marriageRegistration.getApplicationNo());
        // update existing application index
        if (applicationIndex != null && null != marriageRegistration.getId()) {
            applicationIndex.setStatus(marriageRegistration.getStatus().getDescription());
            applicationIndex.setOwnerName(user != null ? user.getUsername() + "::" + user.getName() : "");

            // mark application index as closed on Application Approved
            if (APPROVED.equals(marriageRegistration.getStatus().getCode())) {
                applicationIndex.setApproved(ApprovalStatus.APPROVED);
                applicationIndex.setClosed(ClosureStatus.YES);
            }
            // mark application index as rejected and closed on Application
            // cancellation
            else if (MarriageRegistration.RegistrationStatus.REJECTED.toString()
                    .equals(marriageRegistration.getStatus().getCode())
                    || MarriageRegistration.RegistrationStatus.CANCELLED.toString()
                            .equals(marriageRegistration.getStatus().getCode())) {
                applicationIndex.setApproved(ApprovalStatus.REJECTED);
                applicationIndex.setClosed(ClosureStatus.YES);
            }

            if (marriageRegistration.getApplicationNo() != null)
                applicationIndex.setConsumerCode(marriageRegistration.getApplicationNo());
            applicationIndexService.updateApplicationIndex(applicationIndex);

            marriageRegistrationIndexService.createMarriageIndex(marriageRegistration,
                    MarriageCertificateType.REGISTRATION.toString());
        } else {
            // create new application index
            if (LOG.isDebugEnabled())
                LOG.debug("Application Index creation Started... ");

            final AppConfigValues marriageSla = marriageRegistrationService.getSlaAppConfigValuesForMarriageReg(
                    MarriageConstants.MODULE_NAME, MarriageConstants.SLAFORMARRIAGEREGISTRATION);
            applicationIndex = ApplicationIndex.builder().withModuleName(APPL_INDEX_MODULE_NAME)
                    .withApplicationNumber(marriageRegistration.getApplicationNo())
                    .withApplicationDate(marriageRegistration.getApplicationDate())
                    .withApplicationType(MarriageCertificateType.REGISTRATION.toString())
                    .withApplicantName(marriageRegistration.getHusband().getFullName() + "::"
                            + marriageRegistration.getWife().getFullName())
                    .withStatus(marriageRegistration.getStatus().getDescription()).withUrl(
                            String.format(url))
                    .withApplicantAddress(marriageRegistration.getHusband().getContactInfo().getResidenceAddress())
                    .withOwnername(user != null ? user.getUsername() + "::" + user.getName() : "")
                    .withChannel(marriageRegistration.getSource() == null ? Source.SYSTEM.toString()
                            : marriageRegistration.getSource())
                    .withMobileNumber(marriageRegistration.getHusband().getContactInfo().getMobileNo())
                    .withClosed(ClosureStatus.NO)
                    .withSla(marriageSla != null && marriageSla.getValue() != null
                            ? Integer.valueOf(marriageSla.getValue()) : 0)
                    .withApproved(ApprovalStatus.INPROGRESS).build();
            applicationIndexService.createApplicationIndex(applicationIndex);
            if (LOG.isDebugEnabled())
                LOG.debug("Application Index creation completed...");

            marriageRegistrationIndexService.createMarriageIndex(marriageRegistration,
                    MarriageCertificateType.REGISTRATION.toString());
        }
    }
}