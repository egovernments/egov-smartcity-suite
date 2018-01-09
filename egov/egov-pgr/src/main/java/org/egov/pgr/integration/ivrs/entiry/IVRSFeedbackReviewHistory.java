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

package org.egov.pgr.integration.ivrs.entiry;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.pgr.entity.Complaint;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import static org.egov.pgr.integration.ivrs.entiry.IVRSFeedbackReviewHistory.SEQ_IVRS_FEEDBACKREVIEW_HISTORY;


@Entity
@Table(name = "EGPGR_IVRS_FEEDBACK_REVIEW_HISTORY")
@SequenceGenerator(name = SEQ_IVRS_FEEDBACKREVIEW_HISTORY, sequenceName = SEQ_IVRS_FEEDBACKREVIEW_HISTORY, allocationSize = 1)
public class IVRSFeedbackReviewHistory extends AbstractAuditable {

    protected static final String SEQ_IVRS_FEEDBACKREVIEW_HISTORY = "SEQ_IVRS_FEEDBACK_REVIEW_HISTORY";
    private static final long serialVersionUID = 1176499350876549092L;

    @Id
    @GeneratedValue(generator = SEQ_IVRS_FEEDBACKREVIEW_HISTORY, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(targetEntity = IVRSFeedbackReview.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "feedbackReview")
    private IVRSFeedbackReview feedbackReview;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "complaint")
    private Complaint complaint;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "rating")
    private IVRSRating rating;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "feedbackReason")
    private IVRSFeedbackReason feedbackReason;

    private String detail;

    public IVRSFeedbackReviewHistory() {
        //default for mvc
    }

    public IVRSFeedbackReviewHistory(IVRSFeedbackReview feedbackReview) {
        setFeedbackReason(feedbackReview.getFeedbackReason());
        setComplaint(feedbackReview.getComplaint());
        setDetail(feedbackReview.getDetail());
        setRating(feedbackReview.getRating());
        setFeedbackReview(feedbackReview);
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public IVRSFeedbackReview getFeedbackReview() {
        return feedbackReview;
    }

    public void setFeedbackReview(IVRSFeedbackReview feedbackReview) {
        this.feedbackReview = feedbackReview;
    }

    public Complaint getComplaint() {
        return complaint;
    }

    public void setComplaint(Complaint complaint) {
        this.complaint = complaint;
    }

    public IVRSRating getRating() {
        return rating;
    }

    public void setRating(IVRSRating rating) {
        this.rating = rating;
    }

    public IVRSFeedbackReason getFeedbackReason() {
        return feedbackReason;
    }

    public void setFeedbackReason(IVRSFeedbackReason feedbackReason) {
        this.feedbackReason = feedbackReason;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}