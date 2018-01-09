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

package org.egov.pgr.integration.ivrs.service;

import org.egov.pgr.entity.Complaint;
import org.egov.pgr.integration.ivrs.entiry.IVRSFeedbackReview;
import org.egov.pgr.integration.ivrs.entiry.IVRSFeedbackReviewHistory;
import org.egov.pgr.integration.ivrs.repository.IVRSFeedbackReviewRepository;
import org.egov.pgr.service.ComplaintService;
import org.egov.pgr.service.ComplaintStatusService;
import org.egov.pgr.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

import static org.egov.pgr.utils.constants.PGRConstants.COMPLAINT_REOPENED;

@Service
@Transactional(readOnly = true)
public class IVRSFeedbackReviewService {

    @Autowired
    private ComplaintService complaintService;

    @Autowired
    private ComplaintStatusService complaintStatusService;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private IVRSFeedbackReviewRepository ivrsFeedbackReviewRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void createFeedbackReview(IVRSFeedbackReview feedbackReview) {
        if (feedbackReview.getFeedbackReason().isToBeReopened()) {
            reopenComplaintOnFeedbackReview(feedbackReview);
            feedbackReview.setReopenCount(1);
        }
        feedbackReview.setReviewCount(1);
        ivrsFeedbackReviewRepository.save(feedbackReview);
    }

    @Transactional
    public void updateFeedbackReview(IVRSFeedbackReview feedbackReview) {
        if (feedbackReview.getFeedbackReason().isToBeReopened()
                && feedbackReview.getReopenCount() < Long.valueOf(configurationService.getValueByKey("IVRS_REOPEN_COUNT"))) {
            reopenComplaintOnFeedbackReview(feedbackReview);
            feedbackReview.setReopenCount(feedbackReview.getReopenCount() + 1);
        }
        feedbackReview.setReviewCount(feedbackReview.getReviewCount() + 1);
        entityManager.detach(feedbackReview);
        IVRSFeedbackReviewHistory history = new IVRSFeedbackReviewHistory(ivrsFeedbackReviewRepository
                .findByComplaintCrn(feedbackReview.getComplaint().getCrn()));
        feedbackReview = entityManager.merge(feedbackReview);
        feedbackReview.getHistory().add(history);
        ivrsFeedbackReviewRepository.save(feedbackReview);
    }

    public void reopenComplaintOnFeedbackReview(IVRSFeedbackReview feedbackReview) {
        Complaint complaint = feedbackReview.getComplaint();
        complaint.setStatus(complaintStatusService.getByName(COMPLAINT_REOPENED));
        complaintService.updateComplaint(complaint);
    }

    public Optional<IVRSFeedbackReview> getExistingFeedbackReviewByCRN(String crn) {
        return Optional.
                ofNullable(ivrsFeedbackReviewRepository.findByComplaintCrn(crn));
    }

}
