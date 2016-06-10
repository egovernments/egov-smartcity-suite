package org.egov.lcms.transactions.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.egov.infra.persistence.validator.annotation.DateFormat;
import org.egov.infra.persistence.validator.annotation.OptionalPattern;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.ValidateDate;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.lcms.masters.entity.AdvocateMaster;
import org.egov.lcms.masters.entity.CaseStage;
import org.egov.lcms.utils.LcmsConstants;
import org.hibernate.validator.constraints.Length;

/**
 * LegalcaseAdvocate entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class LegalcaseAdvocate {

    private Long id;
    private Legalcase eglcLegalcase;
    @Required(message = "advocate.legalcase.null")
    private AdvocateMaster eglcAdvocateMaster;
    @DateFormat(message = "invalid.fieldvalue.assignedOnDate")
    @ValidateDate(allowPast = true, dateFormat = LcmsConstants.DATE_FORMAT, message = "invalid.assignedtodate.date")
    private Date assignedtodate;
    @DateFormat(message = "invalid.fieldvalue.assignedOnForSeniorAdv")
    @ValidateDate(allowPast = true, dateFormat = LcmsConstants.DATE_FORMAT, message = "invalid.assignedtodateForsenior.date")
    private Date assignedtodateForsenior;
    @DateFormat(message = "invalid.fieldvalue.vakalaatDate")
    @ValidateDate(allowPast = true, dateFormat = LcmsConstants.DATE_FORMAT, message = "invalid.vakalatdate.date")
    private Date vakalatdate;
    private Long isActive;
    @Length(max = 32, message = "ordernumber.length")
    @OptionalPattern(regex = LcmsConstants.orderNumberFormat, message = "orderNumber.format")
    private String ordernumber;
    @DateFormat(message = "invalid.fieldvalue.orderDate")
    @ValidateDate(allowPast = true, dateFormat = LcmsConstants.DATE_FORMAT, message = "invalid.orderdate.date")
    private Date orderdate;
    private AdvocateMaster eglcSeniorAdvocateMaster;
    @Length(max = 32, message = "ordernumberJunior.length")
    private String ordernumberJunior;
    @DateFormat(message = "invalid.fieldvalue.juniororderDate")
    private Date orderdateJunior;
    private CaseStage juniorStage;
    private CaseStage seniorStage;
    @Length(max = 256, message = "reassignmentJunior.length")
    private String reassignmentJuniorReason;
    @Length(max = 256, message = "reassignmentSenior.length")
    private String reassignmentSeniorReason;
    private long changeAdvocate, changeSeniorAdvocate;

    public long getChangeAdvocate() {
        return changeAdvocate;
    }

    public void setChangeAdvocate(long changeAdvocate) {
        this.changeAdvocate = changeAdvocate;
    }

    public long getChangeSeniorAdvocate() {
        return changeSeniorAdvocate;
    }

    public void setChangeSeniorAdvocate(long changeSeniorAdvocate) {
        this.changeSeniorAdvocate = changeSeniorAdvocate;
    }

    public Legalcase getEglcLegalcase() {
        return this.eglcLegalcase;
    }

    public void setEglcLegalcase(Legalcase eglcLegalcase) {
        this.eglcLegalcase = eglcLegalcase;
    }

    public AdvocateMaster getEglcAdvocateMaster() {
        return this.eglcAdvocateMaster;
    }

    public void setEglcAdvocateMaster(AdvocateMaster eglcAdvocateMaster) {
        this.eglcAdvocateMaster = eglcAdvocateMaster;
    }

    public Date getAssignedtodate() {
        return this.assignedtodate;
    }

    public void setAssignedtodate(Date assignedtodate) {
        this.assignedtodate = assignedtodate;
    }

    public Date getVakalatdate() {
        return this.vakalatdate;
    }

    public void setVakalatdate(Date vakalatdate) {
        this.vakalatdate = vakalatdate;
    }

    public Long getIsActive() {
        return this.isActive;
    }

    public void setIsActive(Long isActive) {
        this.isActive = isActive;
    }

    public String getOrdernumber() {
        return this.ordernumber;
    }

    public void setOrdernumber(String ordernumber) {
        this.ordernumber = ordernumber;
    }

    public Date getOrderdate() {
        return this.orderdate;
    }

    public void setOrderdate(Date orderdate) {
        this.orderdate = orderdate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<ValidationError>();
        if (eglcLegalcase.getIsSenioradvrequired()) {
            if (getEglcSeniorAdvocateMaster() == null) {
                errors.add(new ValidationError("eglcSeniorAdvocateMaster",
                        "legalcase.eglcSeniorAdvocateMaster.null"));
            }
            if (StringUtils.isBlank(getOrdernumber())) {
                errors.add(new ValidationError("ordernumber",
                        "legalcase.ordernumber.null"));
            }
            if (getOrderdate() == null) {
                errors.add(new ValidationError("orderDate",
                        "legalcase.orderdate.null"));
            }
            if (!DateUtils.compareDates(getOrderdate(), eglcLegalcase
                    .getCaseReceivingDate())) {
                errors.add(new ValidationError("orderDate",
                        "orderdate.less.casereceivingdate"));
            }
            if (!DateUtils.compareDates(getOrderdate(), eglcLegalcase
                    .getCasedate())) {
                errors.add(new ValidationError("orderDate",
                        "orderdate.less.casedate"));
            }
            if (!DateUtils.compareDates(getAssignedtodateForsenior(),
                    getOrderdate())) {
                errors.add(new ValidationError("assignedtodatesenior",
                        "assignedon.less.orderdate"));
            }
        }

        if (!DateUtils.compareDates(getAssignedtodate(), eglcLegalcase
                .getCaseReceivingDate())) {
            errors.add(new ValidationError("assignedon",
                    "assignedon.less.casereceivingdate"));
        }
        if (!DateUtils.compareDates(getAssignedtodate(), eglcLegalcase
                .getCasedate())) {
            errors.add(new ValidationError("assignedon",
                    "assignedon.less.casedate"));
        }
        if (!DateUtils.compareDates(getVakalatdate(), eglcLegalcase
                .getCasedate())) {
            errors.add(new ValidationError("vakalatdate",
                    "vakalatdate.less.casedate"));
        }
        if (!DateUtils.compareDates(getVakalatdate(), eglcLegalcase
                .getCaseReceivingDate())) {
            errors.add(new ValidationError("vakalatdate",
                    "vakalatdate.less.caserecdate"));
        }
        if (!DateUtils.compareDates(getVakalatdate(), getAssignedtodate())) {
            errors.add(new ValidationError("vakalatdate",
                    "vakalatdate.less.assingedon"));
        }
        return errors;
    }

    public AdvocateMaster getEglcSeniorAdvocateMaster() {
        return eglcSeniorAdvocateMaster;
    }

    public void setEglcSeniorAdvocateMaster(
            AdvocateMaster eglcSeniorAdvocateMaster) {
        this.eglcSeniorAdvocateMaster = eglcSeniorAdvocateMaster;
    }

    public Date getAssignedtodateForsenior() {
        return assignedtodateForsenior;
    }

    public void setAssignedtodateForsenior(Date assignedtodateForsenior) {
        this.assignedtodateForsenior = assignedtodateForsenior;
    }

    public String getOrdernumberJunior() {
        return ordernumberJunior;
    }

    public void setOrdernumberJunior(String ordernumberJunior) {
        this.ordernumberJunior = ordernumberJunior;
    }

    public Date getOrderdateJunior() {
        return orderdateJunior;
    }

    public void setOrderdateJunior(Date orderdateJunior) {
        this.orderdateJunior = orderdateJunior;
    }

    public CaseStage getJuniorStage() {
        return juniorStage;
    }

    public void setJuniorStage(CaseStage juniorStage) {
        this.juniorStage = juniorStage;
    }

    public CaseStage getSeniorStage() {
        return seniorStage;
    }

    public void setSeniorStage(CaseStage seniorStage) {
        this.seniorStage = seniorStage;
    }

    public String getReassignmentJuniorReason() {
        return reassignmentJuniorReason;
    }

    public void setReassignmentJuniorReason(String reassignmentJuniorReason) {
        this.reassignmentJuniorReason = reassignmentJuniorReason;
    }

    public String getReassignmentSeniorReason() {
        return reassignmentSeniorReason;
    }

    public void setReassignmentSeniorReason(String reassignmentSeniorReason) {
        this.reassignmentSeniorReason = reassignmentSeniorReason;
    }
}