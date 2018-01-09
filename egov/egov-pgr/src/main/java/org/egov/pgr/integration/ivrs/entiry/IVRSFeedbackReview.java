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
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

import static org.egov.pgr.integration.ivrs.entiry.IVRSFeedbackReview.SEQ_IVRS_FEEDBACKREVIEW;

@Entity
@Table(name = "EGPGR_IVRS_FEEDBACK_REVIEW")
@SequenceGenerator(name = SEQ_IVRS_FEEDBACKREVIEW, sequenceName = SEQ_IVRS_FEEDBACKREVIEW, allocationSize = 1)
public class IVRSFeedbackReview extends AbstractAuditable {

    protected static final String SEQ_IVRS_FEEDBACKREVIEW = "SEQ_EGPGR_IVRS_FEEDBACK_REVIEW";
    private static final long serialVersionUID = -1375433581373197712L;

    @Id
    @GeneratedValue(generator = SEQ_IVRS_FEEDBACKREVIEW, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "feedbackReason")
    private IVRSFeedbackReason feedbackReason;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "rating")
    private IVRSRating rating;

    @SafeHtml
    @Length(max = 128)
    private String detail;

    @NotNull
    @OneToOne
    @JoinColumn(name = "complaint")
    private Complaint complaint;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY,
            mappedBy = "feedbackReview", targetEntity = IVRSFeedbackReviewHistory.class)
    @OrderBy("id")
    private Set<IVRSFeedbackReviewHistory> history = new HashSet<>();

    private Integer reopenCount = 0;

    private Integer reviewCount = 0;

    @Transient
    private boolean existing;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IVRSFeedbackReason getFeedbackReason() {
        return feedbackReason;
    }

    public void setFeedbackReason(IVRSFeedbackReason feedbackReason) {
        this.feedbackReason = feedbackReason;
    }

    public IVRSRating getRating() {
        return rating;
    }

    public void setRating(IVRSRating rating) {
        this.rating = rating;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Complaint getComplaint() {
        return complaint;
    }

    public void setComplaint(Complaint complaint) {
        this.complaint = complaint;
    }

    public Integer getReopenCount() {
        return reopenCount;
    }

    public void setReopenCount(Integer reopenCount) {
        this.reopenCount = reopenCount;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public boolean isExisting() {
        return existing;
    }

    public void setExisting(boolean existing) {
        this.existing = existing;
    }

    public Set<IVRSFeedbackReviewHistory> getHistory() {
        return history;
    }

    public void setHistory(Set<IVRSFeedbackReviewHistory> history) {
        this.history = history;
    }

    public void addHistory(IVRSFeedbackReviewHistory history) {
        this.history.add(history);
    }
}
