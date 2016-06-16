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
package org.egov.lcms.transactions.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
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
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.DateFormat;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.ValidateDate;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.lcms.masters.entity.JudgmentType;
import org.egov.lcms.utils.LcmsConstants;
import org.hibernate.validator.constraints.Length;

/**
 * Judgment entity.
 *
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EGLC_JUDGMENT")
@SequenceGenerator(name = Judgment.SEQ_EGLC_JUDGMENT, sequenceName = Judgment.SEQ_EGLC_JUDGMENT, allocationSize = 1)
public class Judgment extends AbstractAuditable {
    /**
     * Serial version uid
     */
    private static final long serialVersionUID = 1517694643078084884L;
    public static final String SEQ_EGLC_JUDGMENT = "SEQ_EGLC_JUDGMENT";

    // Fields
    @Id
    @GeneratedValue(generator = SEQ_EGLC_JUDGMENT, strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne(fetch=FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "LEGALCASE", nullable = false)
    private Legalcase eglcLegalcase;
    @Required(message = "select.judgmentType")
    @ManyToOne(fetch=FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "JUDGEMENTTYPE", nullable = false)
    private JudgmentType judgmentType;
    @Required(message = "orderDate.null")
    @DateFormat(message = "invalid.fieldvalue.model.orderDate")
    @ValidateDate(allowPast = true, dateFormat = LcmsConstants.DATE_FORMAT, message = "invalid.order.date")
    private Date orderDate;
    @DateFormat(message = "invalid.fieldvalue.model.sentToDeptOn")
    @ValidateDate(allowPast = true, dateFormat = LcmsConstants.DATE_FORMAT, message = "invalid.sentDept.date")
    private Date sentToDeptOn;
    @DateFormat(message = "invalid.fieldvalue.model.implementByDate")
    private Date implementByDate;
    private Long costAwarded;
    private Long compensationAwarded;
    @Required(message = "judgmentDetails.null")
    @Length(max = 1024, message = "judgmentDetails.length")
    private String judgmentDetails;
    private String judgmentDocument;
    private Double advisorFee;
    private Double arbitratorFee;
    @Length(max = 1024, message = "enquirydetails.length")
    private String enquirydetails;
    @DateFormat(message = "invalid.fieldvalue.model.enquirydate")
    private Date enquirydate;
    @DateFormat(message = "invalid.fieldvalue.model.setasidePetitionDate")
    private Date setasidePetitionDate;
    @Length(max = 1024, message = "setasidePetitionDetails.length")
    private String setasidePetitionDetails;
    @OneToMany(mappedBy = "eglcJudgment", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Judgmentimpl> eglcJudgmentimpls = new HashSet<Judgmentimpl>(0);
   /* @OneToMany(mappedBy = "PARENT", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Judgment> children = new LinkedHashSet<Judgment>(0);*/
    @DateFormat(message = "invalid.fieldvalue.model.sapHearingDate")
    private Date sapHearingDate;
    private boolean sapAccepted;
    @ManyToOne(fetch=FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "PARENT")
    private Judgment parent;
    private Long documentNum;
    private boolean isMemoRequired;
    private Date certifiedMemoFwdDate;

    public Long getDocumentNum() {
        return documentNum;
    }

    public void setDocumentNum(final Long documentNum) {
        this.documentNum = documentNum;
    }

    public Legalcase getEglcLegalcase() {
        return eglcLegalcase;
    }

    public void setEglcLegalcase(final Legalcase eglcLegalcase) {
        this.eglcLegalcase = eglcLegalcase;
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

    public Long getCostAwarded() {
        return costAwarded;
    }

    public void setCostAwarded(final Long costAwarded) {
        this.costAwarded = costAwarded;
    }

    public Long getCompensationAwarded() {
        return compensationAwarded;
    }

    public void setCompensationAwarded(final Long compensationAwarded) {
        this.compensationAwarded = compensationAwarded;
    }

    public String getJudgmentDetails() {
        return judgmentDetails;
    }

    public void setJudgmentDetails(final String judgmentDetails) {
        this.judgmentDetails = judgmentDetails;
    }

    public String getJudgmentDocument() {
        return judgmentDocument;
    }

    public void setJudgmentDocument(final String judgmentDocument) {
        this.judgmentDocument = judgmentDocument;
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

    public String getEnquirydetails() {
        return enquirydetails;
    }

    public void setEnquirydetails(final String enquirydetails) {
        this.enquirydetails = enquirydetails;
    }

    public Date getEnquirydate() {
        return enquirydate;
    }

    public void setEnquirydate(final Date enquirydate) {
        this.enquirydate = enquirydate;
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

    public void addJudgmentimpl(final Judgmentimpl judgmentimpl) {
        getEglcJudgmentimpls().add(judgmentimpl);
    }

    @Valid
    public Set<Judgmentimpl> getEglcJudgmentimpls() {
        return eglcJudgmentimpls;
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

    public List<ValidationError> validate() {
        final List<ValidationError> errors = new ArrayList<ValidationError>();
        if (eglcLegalcase != null && !DateUtils.compareDates(getOrderDate(), eglcLegalcase.getCasedate()))
            errors.add(new ValidationError("orderDate", "orderdate.less.casedate"));
        if (!DateUtils.compareDates(getImplementByDate(), getOrderDate()))
            errors.add(new ValidationError("implementByDate", "implementByDate.less.orderDate"));
        if (!DateUtils.compareDates(getSentToDeptOn(), getOrderDate()))
            errors.add(new ValidationError("sentToDeptOn", "sentToDeptOn.less.orderDate"));
        if (!DateUtils.compareDates(getEnquirydate(), getOrderDate()))
            errors.add(new ValidationError("enquirydate", "enquirydate.less.orderDate"));
        if (!DateUtils.compareDates(getSapHearingDate(), getOrderDate()))
            errors.add(new ValidationError("sapHearingDate", "sapHearingDate.less.orderDate"));
        if (!DateUtils.compareDates(getSetasidePetitionDate(), getOrderDate()))
            errors.add(new ValidationError("setasidePetitionDate", "setasidePetitionDate.less.orderDate"));
        for (final Judgmentimpl judgmentimpl : getEglcJudgmentimpls()) {
            final Judgmentimpl element = judgmentimpl;
            errors.addAll(element.validate());
        }
        return errors;
    }

    public void setEglcJudgmentimpls(final Set<Judgmentimpl> eglcJudgmentimpls) {
        this.eglcJudgmentimpls = eglcJudgmentimpls;
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

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

}