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
package org.egov.lcms.transactions.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.ValidateDate;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.lcms.masters.entity.JudgmentType;
import org.egov.lcms.utils.constants.LcmsConstants;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.validator.constraints.Length;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "EGLC_JUDGMENT")
@SequenceGenerator(name = Judgment.SEQ_EGLC_JUDGMENT, sequenceName = Judgment.SEQ_EGLC_JUDGMENT, allocationSize = 1)
@AuditOverrides({ @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedBy"),
        @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedDate") })
public class Judgment extends AbstractAuditable {

    private static final long serialVersionUID = 1517694643078084884L;
    public static final String SEQ_EGLC_JUDGMENT = "SEQ_EGLC_JUDGMENT";

    @Id
    @GeneratedValue(generator = SEQ_EGLC_JUDGMENT, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "legalcase", nullable = false)
    @Audited
    private LegalCase legalCase;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "judgmenttype", nullable = false)
    @Audited
    private JudgmentType judgmentType;

    @NotNull
    @Temporal(TemporalType.DATE)
    @ValidateDate(allowPast = true, dateFormat = LcmsConstants.DATE_FORMAT)
    @Column(name = "orderdate")
    @Audited
    private Date orderDate;

    @NotNull
    @Temporal(TemporalType.DATE)
    @ValidateDate(allowPast = true, dateFormat = LcmsConstants.DATE_FORMAT)
    @Column(name = "senttodepton")
    @Audited
    private Date sentToDeptOn;

    @Column(name = "implementbydate")
    @Temporal(TemporalType.DATE)
    @Audited
    private Date implementByDate;

    @Column(name = "costawarded")
    @Audited
    private double costAwarded;

    @Column(name = "compensationAwarded")
    @Audited
    private double compensationAwarded;

    @NotNull
    @Length(max = 1024)
    @Column(name = "judgmentdetails")
    @Audited
    private String judgmentDetails;

    @Column(name = "advisorfee")
    @Audited
    private Double advisorFee;

    @Column(name = "arbitratorfee")
    @Audited
    private Double arbitratorFee;

    @Length(max = 1024)
    @Column(name = "enquirydetails")
    @Audited
    private String enquiryDetails;

    @Temporal(TemporalType.DATE)
    @Column(name = "enquirydate")
    private Date enquiryDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "setasidepetitiondate")
    @Audited
    private Date setasidePetitionDate;

    @Length(max = 1024)
    @Column(name = "setasidepetitiondetails")
    @Audited
    private String setasidePetitionDetails;

    @Temporal(TemporalType.DATE)
    @Column(name = "saphearingdate")
    @Audited
    private Date sapHearingDate;

    @Column(name = "issapaccepted")
    @Audited
    private boolean sapAccepted;

    @ManyToOne
    @JoinColumn(name = "parent")
    @Fetch(value = FetchMode.SELECT)
    private Judgment parent;

    @OneToMany(mappedBy = "judgment", fetch = FetchType.LAZY)
    @NotAudited
    private List<JudgmentDocuments> judgmentDocuments = new ArrayList<JudgmentDocuments>(0);

    @OneToMany(mappedBy = "judgment", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Audited
    private List<JudgmentImpl> judgmentImpl = new ArrayList<JudgmentImpl>(0);

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "parent")
    @Fetch(value = FetchMode.SELECT)
    @JsonIgnore
    private Set<Judgment> children = new HashSet<Judgment>();

    @Column(name = "ismemorequired")
    @Audited
    private boolean isMemoRequired;

    @Column(name = "certifiedmemofwddate")
    @Audited
    private Date certifiedMemoFwdDate;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public List<JudgmentDocuments> getJudgmentDocuments() {
        return judgmentDocuments;
    }

    public void setJudgmentDocuments(final List<JudgmentDocuments> judgmentDocuments) {
        this.judgmentDocuments = judgmentDocuments;
    }

    public LegalCase getLegalCase() {
        return legalCase;
    }

    public void setLegalCase(final LegalCase legalCase) {
        this.legalCase = legalCase;
    }

    public JudgmentType getJudgmentType() {
        return judgmentType;
    }

    public void setJudgmentType(final JudgmentType newJudgmentType) {
        judgmentType = newJudgmentType;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(final Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getSentToDeptOn() {
        return sentToDeptOn;
    }

    public void setSentToDeptOn(final Date sentToDeptOn) {
        this.sentToDeptOn = sentToDeptOn;
    }

    public Date getImplementByDate() {
        return implementByDate;
    }

    public void setImplementByDate(final Date implementByDate) {
        this.implementByDate = implementByDate;
    }

    public double getCostAwarded() {
        return costAwarded;
    }

    public void setCostAwarded(final double costAwarded) {
        this.costAwarded = costAwarded;
    }

    public double getCompensationAwarded() {
        return compensationAwarded;
    }

    public void setCompensationAwarded(final double compensationAwarded) {
        this.compensationAwarded = compensationAwarded;
    }

    public Set<Judgment> getChildren() {
        return children;
    }

    public void setChildren(final Set<Judgment> children) {
        this.children = children;
    }

    public void setMemoRequired(final boolean isMemoRequired) {
        this.isMemoRequired = isMemoRequired;
    }

    public String getJudgmentDetails() {
        return judgmentDetails;
    }

    public void setJudgmentDetails(final String judgmentDetails) {
        this.judgmentDetails = judgmentDetails;
    }

    public Double getAdvisorFee() {
        return advisorFee;
    }

    public void setAdvisorFee(final Double advisorFee) {
        this.advisorFee = advisorFee;
    }

    public Double getArbitratorFee() {
        return arbitratorFee;
    }

    public void setArbitratorFee(final Double arbitratorFee) {
        this.arbitratorFee = arbitratorFee;
    }

    public Date getSetasidePetitionDate() {
        return setasidePetitionDate;
    }

    public void setSetasidePetitionDate(final Date setasidePetitionDate) {
        this.setasidePetitionDate = setasidePetitionDate;
    }

    public String getSetasidePetitionDetails() {
        return setasidePetitionDetails;
    }

    public void setSetasidePetitionDetails(final String setasidePetitionDetails) {
        this.setasidePetitionDetails = setasidePetitionDetails;
    }

    public Date getSapHearingDate() {
        return sapHearingDate;
    }

    public boolean getSapAccepted() {
        return sapAccepted;
    }

    public void setSapHearingDate(final Date sapHearingDate) {
        this.sapHearingDate = sapHearingDate;
    }

    public void setSapAccepted(final boolean sapAccepted) {
        this.sapAccepted = sapAccepted;
    }

    public List<JudgmentImpl> getJudgmentImpl() {
        return judgmentImpl;
    }

    public void setJudgmentImpl(final List<JudgmentImpl> judgmentImpl) {
        this.judgmentImpl = judgmentImpl;
    }

    public void addJudgmentimpl(final JudgmentImpl judgmentimpl) {
        getJudgmentImpl().add(judgmentimpl);
    }

    public Judgment getParent() {
        return parent;
    }

    public void setParent(final Judgment parent) {
        this.parent = parent;
    }

    public void setIsMemoRequired(final boolean isMemoRequired) {
        this.isMemoRequired = isMemoRequired;
    }

    public boolean getIsMemoRequired() {
        return isMemoRequired;
    }

    public Date getCertifiedMemoFwdDate() {
        return certifiedMemoFwdDate;
    }

    public void setCertifiedMemoFwdDate(final Date certifiedMemoFwdDate) {
        this.certifiedMemoFwdDate = certifiedMemoFwdDate;
    }

    public String getEnquiryDetails() {
        return enquiryDetails;
    }

    public void setEnquiryDetails(final String enquiryDetails) {
        this.enquiryDetails = enquiryDetails;
    }

    public Date getEnquiryDate() {
        return enquiryDate;
    }

    public void setEnquiryDate(final Date enquiryDate) {
        this.enquiryDate = enquiryDate;
    }

    public List<ValidationError> validate() {
        final List<ValidationError> errors = new ArrayList<ValidationError>();
        if (legalCase != null && !DateUtils.compareDates(getOrderDate(), legalCase.getCaseDate()))
            errors.add(new ValidationError("orderDate", "orderdate.less.casedate"));
        if (!DateUtils.compareDates(getImplementByDate(), getOrderDate()))
            errors.add(new ValidationError("implementByDate", "implementByDate.less.orderDate"));
        if (!DateUtils.compareDates(getSentToDeptOn(), getOrderDate()))
            errors.add(new ValidationError("sentToDeptOn", "sentToDeptOn.less.orderDate"));
        if (!DateUtils.compareDates(getEnquiryDate(), getOrderDate()))
            errors.add(new ValidationError("enquirydate", "enquirydate.less.orderDate"));
        if (!DateUtils.compareDates(getSapHearingDate(), getOrderDate()))
            errors.add(new ValidationError("sapHearingDate", "sapHearingDate.less.orderDate"));
        if (!DateUtils.compareDates(getSetasidePetitionDate(), getOrderDate()))
            errors.add(new ValidationError("setasidePetitionDate", "setasidePetitionDate.less.orderDate"));
        for (final JudgmentImpl judgmentimpl : getJudgmentImpl()) {
            final JudgmentImpl element = judgmentimpl;
            errors.addAll(element.validate());
        }
        return errors;
    }

}