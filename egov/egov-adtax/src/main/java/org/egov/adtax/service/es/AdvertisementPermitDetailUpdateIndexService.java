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

package org.egov.adtax.service.es;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.adtax.entity.AdvertisementPermitDetail;
import org.egov.adtax.utils.constants.AdvertisementTaxConstants;
import org.egov.commons.entity.Source;
import org.egov.eis.entity.Assignment;
import org.egov.eis.service.AssignmentService;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.UserService;
import org.egov.infra.elasticsearch.entity.ApplicationIndex;
import org.egov.infra.elasticsearch.entity.enums.ApprovalStatus;
import org.egov.infra.elasticsearch.entity.enums.ClosureStatus;
import org.egov.infra.elasticsearch.service.ApplicationIndexService;
import org.egov.infra.security.utils.SecurityUtils;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdvertisementPermitDetailUpdateIndexService {
    private static final String ADTAX_APPLICATION_VIEW = "/adtax/hoarding/view/%s";
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

    @Autowired
    private AdvertisementIndexService advertisementIndexService;

    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    /**
     * @param advertisementPermitDetail
     */
    public void updateAdvertisementPermitDetailIndexes(final AdvertisementPermitDetail advertisementPermitDetail) {

        Assignment assignment = null;
        User user = null;
        List<Assignment> asignList = null;
        if (advertisementPermitDetail.getState() != null && advertisementPermitDetail.getState().getOwnerPosition() != null) {
            assignment = assignmentService
                    .getPrimaryAssignmentForPositionAndDate(advertisementPermitDetail.getState().getOwnerPosition()
                            .getId(), new Date());
            if (assignment != null) {
                asignList = new ArrayList<Assignment>();
                asignList.add(assignment);
            } else if (assignment == null)
                asignList = assignmentService.getAssignmentsForPosition(
                        advertisementPermitDetail.getState().getOwnerPosition().getId(),
                        new Date());
            if (!asignList.isEmpty())
                user = userService.getUserById(asignList.get(0).getEmployee().getId());
        } else
            user = securityUtils.getCurrentUser();

        // For legacy application - create only advertisementIndex
        if (advertisementPermitDetail.getAdvertisement().getLegacy()
                && (null == advertisementPermitDetail.getId() || (null != advertisementPermitDetail.getId()
                        && advertisementPermitDetail.getStatus().getCode()
                                .equalsIgnoreCase(AdvertisementTaxConstants.APPLICATION_STATUS_ADTAXPERMITGENERATED)))) {
            advertisementIndexService.createAdvertisementIndex(advertisementPermitDetail);
            return;
        }

        ApplicationIndex applicationIndex = applicationIndexService.findByApplicationNumber(advertisementPermitDetail
                .getApplicationNumber());
        // update existing application index
        if (applicationIndex != null && null != advertisementPermitDetail.getId() && advertisementPermitDetail.getStatus() != null
                && !advertisementPermitDetail.getStatus().getCode()
                        .equalsIgnoreCase(AdvertisementTaxConstants.APPLICATION_STATUS_CREATED)) {
            if (advertisementPermitDetail.getStatus() != null &&
                    (advertisementPermitDetail.getStatus().getCode()
                            .equalsIgnoreCase(AdvertisementTaxConstants.APPLICATION_STATUS_APPROVED)
                            || advertisementPermitDetail.getStatus().getCode()
                                    .equalsIgnoreCase(AdvertisementTaxConstants.APPLICATION_STATUS_ADTAXAMOUNTPAID)
                            || advertisementPermitDetail.getStatus().getCode()
                                    .equalsIgnoreCase(AdvertisementTaxConstants.APPLICATION_STATUS_ADTAXPERMITGENERATED)
                            || advertisementPermitDetail.getStatus().getCode()
                                    .equalsIgnoreCase(AdvertisementTaxConstants.APPLICATION_STATUS_CANCELLED))) {
                applicationIndex.setStatus(advertisementPermitDetail.getStatus().getDescription());
                applicationIndex.setOwnerName(user != null ? user.getUsername() + "::" + user.getName() : "");

                // Set application index status to approved on advertisement approval
                if (advertisementPermitDetail.getStatus().getCode()
                        .equalsIgnoreCase(AdvertisementTaxConstants.APPLICATION_STATUS_APPROVED)) {
                    String applicantName = advertisementPermitDetail.getAgency() != null
                            ? advertisementPermitDetail.getAgency().getName() : advertisementPermitDetail.getOwnerDetail();
                    String address = advertisementPermitDetail.getAgency() != null
                            ? advertisementPermitDetail.getAgency().getAddress() : advertisementPermitDetail.getOwnerDetail();
                    applicationIndex.setApplicantName(applicantName);
                    applicationIndex.setApplicantAddress(address);
                    applicationIndex.setMobileNumber(advertisementPermitDetail.getAgency() != null
                            ? advertisementPermitDetail.getAgency().getMobileNumber() : "");
                }
                // mark application index as closed on generate permit order
                if (advertisementPermitDetail.getStatus().getCode()
                        .equalsIgnoreCase(AdvertisementTaxConstants.APPLICATION_STATUS_ADTAXPERMITGENERATED)) {
                    applicationIndex.setApproved(ApprovalStatus.APPROVED);
                    applicationIndex.setClosed(ClosureStatus.YES);
                }
                // mark application index as rejected and closed on advertisement cancellation / Deactivation
                if (advertisementPermitDetail.getStatus().getCode()
                        .equalsIgnoreCase(AdvertisementTaxConstants.APPLICATION_STATUS_CANCELLED) ||
                        advertisementPermitDetail.getAdvertisement().getStatus().name().equalsIgnoreCase("INACTIVE")) {
                    applicationIndex.setApproved(ApprovalStatus.REJECTED);
                    applicationIndex.setClosed(ClosureStatus.YES);
                }

                if (advertisementPermitDetail.getPermissionNumber() != null)
                    applicationIndex.setConsumerCode(advertisementPermitDetail.getPermissionNumber());

                applicationIndexService.updateApplicationIndex(applicationIndex);
            }
            // create advertisement index
            advertisementIndexService.createAdvertisementIndex(advertisementPermitDetail);

        } else {

            if (advertisementPermitDetail.getApplicationDate() == null)
                advertisementPermitDetail.setApplicationDate(new Date());
            if (advertisementPermitDetail.getApplicationNumber() == null)
                advertisementPermitDetail.setApplicationNumber(advertisementPermitDetail.getApplicationNumber());
            if (applicationIndex == null) {
                String applicantName = advertisementPermitDetail.getAgency() != null
                        ? advertisementPermitDetail.getAgency().getName() : advertisementPermitDetail.getOwnerDetail();
                String address = advertisementPermitDetail.getAgency() != null
                        ? advertisementPermitDetail.getAgency().getAddress() : advertisementPermitDetail.getOwnerDetail();
                applicationIndex = ApplicationIndex.builder().withModuleName(AdvertisementTaxConstants.MODULE_NAME)
                        .withApplicationNumber(advertisementPermitDetail.getApplicationNumber())
                        .withApplicationDate(advertisementPermitDetail.getApplicationDate())
                        .withApplicationType(advertisementPermitDetail.getState().getNatureOfTask())
                        .withApplicantName(applicantName)
                        .withStatus(advertisementPermitDetail.getStatus().getDescription()).withUrl(
                                String.format(ADTAX_APPLICATION_VIEW, advertisementPermitDetail.getId()))
                        .withApplicantAddress(address).withOwnername(user.getUsername() + "::" + user.getName())
                        .withChannel(advertisementPermitDetail.getSource() == null ? Source.SYSTEM.toString()
                                : advertisementPermitDetail.getSource())
                        .withMobileNumber(advertisementPermitDetail.getAgency() != null
                                ? advertisementPermitDetail.getAgency().getMobileNumber() : EMPTY)
                        .withClosed(ClosureStatus.NO)
                        .withApproved(ApprovalStatus.INPROGRESS).build();
                applicationIndexService.createApplicationIndex(applicationIndex);
            }
        }
    }
}