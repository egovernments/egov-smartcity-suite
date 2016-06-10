package org.egov.lcms.transactions.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.egov.infra.persistence.validator.annotation.DateFormat;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.ValidateDate;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infstr.models.BaseModel;
import org.egov.lcms.masters.entity.JudgmentType;
import org.egov.lcms.utils.LcmsConstants;
import org.hibernate.validator.constraints.Length;

/**
 * Judgment entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Judgment extends BaseModel {
    /**
     * Serial version uid
     */
    private static final long serialVersionUID = 1L;

    // Fields
    private Legalcase eglcLegalcase;
    @Required(message = "select.judgmentType")
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
    private Set<Judgmentimpl> eglcJudgmentimpls = new HashSet<Judgmentimpl>(0);
    private Set<Judgment> children = new LinkedHashSet<Judgment>(0);
    @DateFormat(message = "invalid.fieldvalue.model.sapHearingDate")
    private Date sapHearingDate;
    private boolean sapAccepted;
    private Judgment parent;
    private Long documentNum;
    private boolean isMemoRequired;
    private Date certifiedMemoFwdDate;

    public Long getDocumentNum() {
        return documentNum;
    }

    public void setDocumentNum(Long documentNum) {
        this.documentNum = documentNum;
    }

    public Legalcase getEglcLegalcase() {
        return this.eglcLegalcase;
    }

    public void setEglcLegalcase(Legalcase eglcLegalcase) {
        this.eglcLegalcase = eglcLegalcase;
    }

    public JudgmentType getJudgmentType() {
        return this.judgmentType;
    }

    public void setJudgmentType(JudgmentType newJudgmentType) {
        this.judgmentType = newJudgmentType;
    }

    public Date getOrderDate() {
        return this.orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getSentToDeptOn() {
        return this.sentToDeptOn;
    }

    public void setSentToDeptOn(Date sentToDeptOn) {
        this.sentToDeptOn = sentToDeptOn;
    }

    public Date getImplementByDate() {
        return this.implementByDate;
    }

    public void setImplementByDate(Date implementByDate) {
        this.implementByDate = implementByDate;
    }

    public Long getCostAwarded() {
        return this.costAwarded;
    }

    public void setCostAwarded(Long costAwarded) {
        this.costAwarded = costAwarded;
    }

    public Long getCompensationAwarded() {
        return this.compensationAwarded;
    }

    public void setCompensationAwarded(Long compensationAwarded) {
        this.compensationAwarded = compensationAwarded;
    }

    public String getJudgmentDetails() {
        return this.judgmentDetails;
    }

    public void setJudgmentDetails(String judgmentDetails) {
        this.judgmentDetails = judgmentDetails;
    }

    public String getJudgmentDocument() {
        return this.judgmentDocument;
    }

    public void setJudgmentDocument(String judgmentDocument) {
        this.judgmentDocument = judgmentDocument;
    }

    public Double getAdvisorFee() {
        return this.advisorFee;
    }

    public void setAdvisorFee(Double advisorFee) {
        this.advisorFee = advisorFee;
    }

    public Double getArbitratorFee() {
        return this.arbitratorFee;
    }

    public void setArbitratorFee(Double arbitratorFee) {
        this.arbitratorFee = arbitratorFee;
    }

    public String getEnquirydetails() {
        return this.enquirydetails;
    }

    public void setEnquirydetails(String enquirydetails) {
        this.enquirydetails = enquirydetails;
    }

    public Date getEnquirydate() {
        return this.enquirydate;
    }

    public void setEnquirydate(Date enquirydate) {
        this.enquirydate = enquirydate;
    }

    public Date getSetasidePetitionDate() {
        return this.setasidePetitionDate;
    }

    public void setSetasidePetitionDate(Date setasidePetitionDate) {
        this.setasidePetitionDate = setasidePetitionDate;
    }

    public String getSetasidePetitionDetails() {
        return setasidePetitionDetails;
    }

    public void setSetasidePetitionDetails(String setasidePetitionDetails) {
        this.setasidePetitionDetails = setasidePetitionDetails;
    }

    public void addJudgmentimpl(Judgmentimpl judgmentimpl) {
        getEglcJudgmentimpls().add(judgmentimpl);
    }

    @Valid
    public Set<Judgmentimpl> getEglcJudgmentimpls() {
        return this.eglcJudgmentimpls;
    }

    public Date getSapHearingDate() {
        return sapHearingDate;
    }

    public boolean getSapAccepted() {
        return sapAccepted;
    }

    public void setSapHearingDate(Date sapHearingDate) {
        this.sapHearingDate = sapHearingDate;
    }

    public void setSapAccepted(boolean sapAccepted) {
        this.sapAccepted = sapAccepted;
    }

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<ValidationError>();
        if (eglcLegalcase != null
                && !DateUtils.compareDates(getOrderDate(), eglcLegalcase
                        .getCasedate())) {
            errors.add(new ValidationError("orderDate",
                    "orderdate.less.casedate"));
        }
        if (!DateUtils.compareDates(getImplementByDate(), getOrderDate())) {
            errors.add(new ValidationError("implementByDate",
                    "implementByDate.less.orderDate"));
        }
        if (!DateUtils.compareDates(getSentToDeptOn(), getOrderDate())) {
            errors.add(new ValidationError("sentToDeptOn",
                    "sentToDeptOn.less.orderDate"));
        }
        if (!DateUtils.compareDates(getEnquirydate(), getOrderDate())) {
            errors.add(new ValidationError("enquirydate",
                    "enquirydate.less.orderDate"));
        }
        if (!DateUtils.compareDates(getSapHearingDate(), getOrderDate())) {
            errors.add(new ValidationError("sapHearingDate",
                    "sapHearingDate.less.orderDate"));
        }
        if (!DateUtils.compareDates(getSetasidePetitionDate(), getOrderDate())) {
            errors.add(new ValidationError("setasidePetitionDate",
                    "setasidePetitionDate.less.orderDate"));
        }
        for (Iterator<Judgmentimpl> iter = getEglcJudgmentimpls().iterator(); iter
                .hasNext();) {
            Judgmentimpl element = (Judgmentimpl) iter.next();
            errors.addAll(element.validate());
        }
        return errors;
    }

    public void setEglcJudgmentimpls(Set<Judgmentimpl> eglcJudgmentimpls) {
        this.eglcJudgmentimpls = eglcJudgmentimpls;
    }

    public Set<Judgment> getChildren() {
        return children;
    }

    public void setChildren(Set<Judgment> children) {
        this.children = children;
    }

    public Judgment getParent() {
        return parent;
    }

    public void setParent(Judgment parent) {
        this.parent = parent;
    }

    public void addChild(Judgment arg1) {
        children.add(arg1);
    }

    public void setIsMemoRequired(boolean isMemoRequired) {
        this.isMemoRequired = isMemoRequired;
    }

    public boolean getIsMemoRequired() {
        return isMemoRequired;
    }

    public Date getCertifiedMemoFwdDate() {
        return certifiedMemoFwdDate;
    }

    public void setCertifiedMemoFwdDate(Date certifiedMemoFwdDate) {
        this.certifiedMemoFwdDate = certifiedMemoFwdDate;
    }

}