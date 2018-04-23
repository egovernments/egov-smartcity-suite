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

import org.egov.infra.config.persistence.datasource.routing.annotation.ReadOnly;
import org.egov.pgr.elasticsearch.service.ComplaintIndexService;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.entity.enums.CitizenFeedback;
import org.egov.pgr.integration.ivrs.entity.IVRSFeedback;
import org.egov.pgr.integration.ivrs.entity.IVRSRating;
import org.egov.pgr.integration.ivrs.entity.contract.IVRSFeedbackSearchRequest;
import org.egov.pgr.integration.ivrs.entity.contract.IVRSFeedbackUpdateRequest;
import org.egov.pgr.integration.ivrs.repository.IVRSFeedbackRepository;
import org.egov.pgr.integration.ivrs.repository.IVRSRatingRepository;
import org.egov.pgr.integration.ivrs.repository.specs.IVRSFeedbackReviewSearchSpec;
import org.egov.pgr.repository.ComplaintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class IVRSFeedbackService {

    @Autowired
    private IVRSRatingRepository ivrsRatingRepository;

    @Autowired
    private IVRSFeedbackRepository ivrsFeedbackRepository;

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private ComplaintIndexService complaintIndexService;

    @Transactional
    public IVRSFeedback createFeedback(IVRSFeedbackUpdateRequest request) {
        Complaint complaint = complaintRepository.findByCrn(request.getCrn());
        IVRSFeedback ivrsFeedback = Optional
                .ofNullable(ivrsFeedbackRepository.findByComplaintCrn(complaint.getCrn()))
                .orElse(new IVRSFeedback());
        if (ivrsFeedback.getId() == null)
            ivrsFeedback.setComplaint(complaint);
        IVRSRating rating = ivrsRatingRepository.findByName(request.getRating());
        ivrsFeedback.setIvrsRating(rating);
        ivrsFeedback = ivrsFeedbackRepository.saveAndFlush(ivrsFeedback);
        complaint.setCitizenFeedback(CitizenFeedback.value(rating.getWeight()));
        complaintRepository.saveAndFlush(complaint);
        complaintIndexService.updateComplaintIndex(complaint);
        return ivrsFeedback;
    }

    public IVRSFeedback getComplaintByCRN(String crn) {
        return ivrsFeedbackRepository.findByComplaintCrn(crn);
    }

    @ReadOnly
    public Page<IVRSFeedback> getComplaintForReview(IVRSFeedbackSearchRequest searchRequest) {
        return ivrsFeedbackRepository.findAll(IVRSFeedbackReviewSearchSpec.search(searchRequest),
                new PageRequest(searchRequest.pageNumber(), searchRequest.pageSize()));
    }
}
