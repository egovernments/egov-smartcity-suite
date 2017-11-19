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
package org.egov.tl.service.es;

import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.elasticsearch.entity.ApplicationIndex;
import org.egov.infra.elasticsearch.entity.enums.ApprovalStatus;
import org.egov.infra.elasticsearch.entity.enums.ClosureStatus;
import org.egov.infra.elasticsearch.service.ApplicationIndexService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.tl.entity.License;
import org.egov.tl.utils.LicenseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.egov.commons.entity.Source.CSC;
import static org.egov.commons.entity.Source.SYSTEM;
import static org.egov.infra.elasticsearch.entity.enums.ApprovalStatus.INPROGRESS;
import static org.egov.infra.elasticsearch.entity.enums.ClosureStatus.NO;
import static org.egov.infra.utils.ApplicationConstant.NA;
import static org.egov.tl.utils.Constants.APPLICATION_STATUS_GENECERT_CODE;
import static org.egov.tl.utils.Constants.CSCOPERATOR;
import static org.egov.tl.utils.Constants.DELIMITER_COLON;
import static org.egov.tl.utils.Constants.STATUS_CANCELLED;
import static org.egov.tl.utils.Constants.TRADE_LICENSE;

@Service
public class LicenseApplicationIndexService {

    private static final String APPLICATION_VIEW_URL = "/tl/viewtradelicense/viewTradeLicense-view.action?applicationNo=%s";

    @Autowired
    private ApplicationIndexService applicationIndexService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private LicenseUtils licenseUtils;

    public void createOrUpdateLicenseApplicationIndex(final License license) {
        ApplicationIndex applicationIndex = applicationIndexService.findByApplicationNumber(license.getApplicationNumber());

        if (applicationIndex != null) {
            updateLicenseApplicationIndex(license, applicationIndex);
        } else {
            createLicenseApplicationIndex(license);
        }
    }

    private void createLicenseApplicationIndex(License license) {
        Optional<User> user = getApplicationCurrentOwner(license);
        if (license.getApplicationDate() == null)
            license.setApplicationDate(new Date());
        Integer slaConfig = licenseUtils.getSlaForAppType(license.getLicenseAppType());

        applicationIndexService.createApplicationIndex(ApplicationIndex.builder().withModuleName(TRADE_LICENSE)
                .withApplicationNumber(license.getApplicationNumber()).withApplicationDate(license.getApplicationDate())
                .withApplicationType(license.getLicenseAppType().getName()).withApplicantName(license.getLicensee().getApplicantName())
                .withStatus(license.getEgwStatus() != null ? license.getEgwStatus().getDescription() : license.getCurrentState().getValue())
                .withUrl(format(APPLICATION_VIEW_URL, license.getApplicationNumber()))
                .withApplicantAddress(license.getAddress()).withOwnername(user.isPresent() ?
                        user.get().getUsername() + DELIMITER_COLON + user.get().getName() : NA)
                .withChannel(getChannel())
                .withMobileNumber(license.getLicensee().getMobilePhoneNumber())
                .withAadharNumber(license.getLicensee().getUid()).withClosed(NO).withApproved(INPROGRESS)
                .withSla(slaConfig != null ? slaConfig : 0)
                .build());
    }

    private String getChannel() {
        return securityUtils.currentUserIsEmployee() ?
                SYSTEM.toString() :
                securityUtils.getCurrentUser().hasRole(CSCOPERATOR) ? CSC.toString() : "ONLINE";
    }


    private void updateLicenseApplicationIndex(License license, ApplicationIndex applicationIndex) {
        Optional<User> user = getApplicationCurrentOwner(license);
        applicationIndex.setStatus(license.getEgwStatus() != null ? license.getEgwStatus().getDescription() : license.getCurrentState().getValue());
        applicationIndex.setApplicantAddress(license.getAddress());
        applicationIndex
                .setOwnerName(user.isPresent() ? user.get().getUsername() + DELIMITER_COLON + user.get().getName() : EMPTY);
        applicationIndex.setConsumerCode(license.getLicenseNumber());
        applicationIndex.setClosed(NO);
        applicationIndex.setApproved(INPROGRESS);
        if (license.getIsActive() || (license.getEgwStatus() != null && license.getEgwStatus().getCode().equals(APPLICATION_STATUS_GENECERT_CODE))) {
            applicationIndex.setClosed(ClosureStatus.YES);
            applicationIndex.setApproved(ApprovalStatus.APPROVED);
        }
        if (license.getStatus().getStatusCode().equals(STATUS_CANCELLED)) {
            applicationIndex.setApproved(ApprovalStatus.REJECTED);
            applicationIndex.setClosed(ClosureStatus.YES);
        }

        applicationIndexService.updateApplicationIndex(applicationIndex);

    }

    private Optional<User> getApplicationCurrentOwner(final License license) {
        User user = null;

        if (license.hasState() && license.getState().getOwnerPosition() != null) {
            Assignment assignment = assignmentService.getPrimaryAssignmentForPositionAndDate(license.getState().getOwnerPosition()
                    .getId(), new Date());
            if (assignment == null) {
                List<Assignment> assignments = assignmentService.getAssignmentsForPosition(license.getState().getOwnerPosition().getId(), new Date());
                if (!assignments.isEmpty())
                    user = assignments.get(0).getEmployee();

            } else {
                user = assignment.getEmployee();
            }
        } else {
            user = securityUtils.getCurrentUser();
        }
        return Optional.ofNullable(user);
    }

}
