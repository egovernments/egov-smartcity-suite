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
package org.egov.tl.service;

import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.es.entity.ApplicationIndex;
import org.egov.infra.es.entity.enums.ApprovalStatus;
import org.egov.infra.es.entity.enums.ClosureStatus;
import org.egov.infra.es.service.ApplicationIndexService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.workflow.entity.StateHistory;
import org.egov.tl.entity.License;
import org.egov.tl.utils.Constants;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.String.format;
import static org.egov.commons.entity.Source.SYSTEM;
import static org.egov.infra.es.entity.enums.ApprovalStatus.INPROGRESS;
import static org.egov.infra.es.entity.enums.ClosureStatus.NO;
import static org.egov.tl.utils.Constants.TRADELICENSE_MODULENAME;

@Service
public class TradeLicenseUpdateIndexService {

    private static final String APPLICATION_VIEW_URL = "/tl/viewtradelicense/viewTradeLicense-view.action?applicationNo=%s";

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ApplicationIndexService applicationIndexService;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private UserService userService;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    public void updateTradeLicenseIndexes(final License license) {
        User user = null;

        if (license.getState() != null && license.getState().getOwnerPosition() != null) {
            Assignment assignment = assignmentService.getPrimaryAssignmentForPositionAndDate(license.getState().getOwnerPosition()
                    .getId(), new Date());
            List<Assignment> asignList = null;
            if (assignment != null) {
                asignList = new ArrayList<>();
                asignList.add(assignment);
            } else if (assignment == null)
                asignList = assignmentService.getAssignmentsForPosition(license.getState().getOwnerPosition().getId(), new Date());
            if (!asignList.isEmpty())
                user = userService.getUserById(asignList.get(0).getEmployee().getId());
        } else
            user = securityUtils.getCurrentUser();
        ApplicationIndex applicationIndex = applicationIndexService.findByApplicationNumber(license.getApplicationNumber());
        if (applicationIndex != null) {
            if (applicationIndex != null && null != license.getId() && license.getEgwStatus() != null
                    && license.getEgwStatus() != null
                    && (license.getEgwStatus().getCode().equals(Constants.APPLICATION_STATUS_INSPE_CODE)
                    || license.getEgwStatus().getCode().equals(Constants.APPLICATION_STATUS_APPROVED_CODE)
                    || license.getEgwStatus().getCode().equals(Constants.APPLICATION_STATUS_COLLECTION_CODE)
                    || license.getEgwStatus().getCode().equals(Constants.APPLICATION_STATUS_GENECERT_CODE) || license
                    .getEgwStatus().getCode().equals(Constants.APPLICATION_STATUS_DIGUPDATE_CODE))
                    || license.getStatus().getStatusCode().equals(Constants.STATUS_CANCELLED)
                    || license.getEgwStatus().getCode().equals(Constants.APPLICATION_STATUS_CREATED_CODE)
                    && license.getState().getValue().contains(Constants.WORKFLOW_STATE_REJECTED)) {
                applicationIndex.setStatus(license.getEgwStatus().getDescription());
                applicationIndex.setApplicantAddress(license.getAddress());
                applicationIndex.setOwnername(user.getUsername() + "::" + user.getName());
                if (license.getLicenseNumber() != null)
                    applicationIndex.setConsumerCode(license.getLicenseNumber());
                int noofDays = 0;
                applicationIndex.setClosed(NO);
                applicationIndex.setApproved(INPROGRESS);
                Date endDate = null;
                if (license.getEgwStatus().getCode().equals(Constants.APPLICATION_STATUS_GENECERT_CODE)) {
                    final List<StateHistory> stateHistoryList = license.getStateHistory();
                    for (final StateHistory stateHisObj : stateHistoryList)
                        if (stateHisObj.getValue().equalsIgnoreCase(Constants.WF_STATE_GENERATE_CERTIFICATE))
                            endDate = stateHisObj.getLastModifiedDate();
                    final Date startDate = license.getApplicationDate();
                    if (endDate == null)
                        endDate = license.getLastModifiedDate();
                    noofDays = DateUtils.noOfDays(startDate, endDate);
                    applicationIndex.setElapsedDays(noofDays);
                    applicationIndex.setClosed(ClosureStatus.YES);
                    applicationIndex.setApproved(ApprovalStatus.APPROVED);
                }
                if (license.getStatus().getStatusCode().equals(Constants.STATUS_CANCELLED)) {
                    applicationIndex.setApproved(ApprovalStatus.REJECTED);
                    applicationIndex.setClosed(ClosureStatus.YES);
                }

                applicationIndexService.updateApplicationIndex(applicationIndex);
            }
        } else {
            if (license.getApplicationDate() == null)
                license.setApplicationDate(new Date());
            if (license.getApplicationNumber() == null)
                license.setApplicationNumber(license.getApplicationNumber());
            applicationIndex = ApplicationIndex.builder().withModuleName(TRADELICENSE_MODULENAME)
                    .withApplicationNumber(license.getApplicationNumber()).withApplicationDate(license.getApplicationDate())
                    .withApplicationType(license.getLicenseAppType().getName()).withApplicantName(license.getLicensee().getApplicantName())
                    .withStatus(license.getEgwStatus().getDescription()).withUrl(format(APPLICATION_VIEW_URL, license.getApplicationNumber()))
                    .withApplicantAddress(license.getAddress()).withOwnername(user.getUsername() + "::" + user.getName())
                    .withChannel(SYSTEM.toString()).withMobileNumber(license.getLicensee().getMobilePhoneNumber())
                    .withAadharNumber(license.getLicensee().getUid()).withClosed(NO).withApproved(INPROGRESS)
                    .build();
            if (license.getIsActive())
                applicationIndexService.createApplicationIndex(applicationIndex);
        }
    }

}
