/* eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.mrs.service.es;

import static org.egov.mrs.application.MarriageConstants.APPL_INDEX_MODULE_NAME;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.egov.commons.entity.Source;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.elasticsearch.entity.ApplicationIndex;
import org.egov.infra.elasticsearch.entity.enums.ApprovalStatus;
import org.egov.infra.elasticsearch.entity.enums.ClosureStatus;
import org.egov.infra.elasticsearch.service.ApplicationIndexService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.mrs.domain.entity.ReIssue;
import org.egov.mrs.domain.enums.MarriageFeeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ReIssueCertificateUpdateIndexesService {

    private static final Logger LOG = Logger.getLogger(ReIssueCertificateUpdateIndexesService.class);
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private ApplicationIndexService applicationIndexService;

    public void createReIssueAppIndex(final ReIssue reIssue) {
        final User user = securityUtils.getCurrentUser();
        if (LOG.isDebugEnabled())
            LOG.debug("Application Index creation Started... ");

        ApplicationIndex applicationIndex = ApplicationIndex.builder().withModuleName(APPL_INDEX_MODULE_NAME)
                .withApplicationNumber(reIssue.getApplicationNo())
                .withApplicationDate(reIssue.getApplicationDate())
                .withApplicationType(MarriageFeeType.CERTIFICATEISSUE.name())
                .withApplicantName(reIssue.getApplicant().getFullName())
                .withStatus(reIssue.getStatus().getDescription()).withUrl(
                        "/mrs/reissue/viewapplication/" + reIssue.getApplicationNo())
                .withApplicantAddress(reIssue.getApplicant().getContactInfo().getResidenceAddress())
                .withOwnername(user != null ? user.getUsername() + "::" + user.getName() : "")
                .withChannel(reIssue.getSource() == null ? Source.SYSTEM.toString() : reIssue.getSource())
                .withMobileNumber(reIssue.getApplicant().getContactInfo().getMobileNo())
                .withClosed(ClosureStatus.NO)
                .withApproved(ApprovalStatus.INPROGRESS).build();
        applicationIndexService.createApplicationIndex(applicationIndex);
        if (LOG.isDebugEnabled())
            LOG.debug("Application Index creation completed...");
    }

    public void updateReIssueAppIndex(final ReIssue reissue) {
        ApplicationIndex applicationIndex = applicationIndexService.findByApplicationNumber(reissue.getApplicationNo());
        Integer elapsedDays;
        if (applicationIndex != null) {
            if (!ReIssue.ReIssueStatus.CREATED.toString().equalsIgnoreCase(reissue.getStatus().getDescription())) {
                applicationIndex.setStatus(reissue.getStatus().getDescription());
                applicationIndex.setApplicantAddress(reissue.getApplicant().getContactInfo().getResidenceAddress());
                applicationIndex.setApplicantName(reissue.getApplicant().getFullName());
                if (ReIssue.ReIssueStatus.APPROVED.toString().equalsIgnoreCase(reissue.getStatus().getCode())) {
                    elapsedDays = (int) TimeUnit.DAYS.convert(
                            new Date().getTime() - reissue.getApplicationDate().getTime(), TimeUnit.MILLISECONDS);
                    applicationIndex.setElapsedDays(elapsedDays);
                    applicationIndex.setApproved(ApprovalStatus.APPROVED);
                    applicationIndex.setClosed(ClosureStatus.YES);
                }
                // mark application index as rejected and closed on Application cancellation
                else if (ReIssue.ReIssueStatus.REJECTED.toString().equalsIgnoreCase(reissue.getStatus().getCode())
                        || ReIssue.ReIssueStatus.CANCELLED.toString().equalsIgnoreCase(reissue.getStatus().getCode())) {
                    elapsedDays = (int) TimeUnit.DAYS.convert(
                            new Date().getTime() - reissue.getApplicationDate().getTime(), TimeUnit.MILLISECONDS);
                    applicationIndex.setElapsedDays(elapsedDays);
                    applicationIndex.setApproved(ApprovalStatus.REJECTED);
                    applicationIndex.setClosed(ClosureStatus.YES);
                }
            }
            applicationIndexService.updateApplicationIndex(applicationIndex);
        } else
            createReIssueAppIndex(reissue);
    }
}