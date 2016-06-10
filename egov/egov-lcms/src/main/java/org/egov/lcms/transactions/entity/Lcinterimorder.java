package org.egov.lcms.transactions.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.commons.EgwStatus;
import org.egov.infra.persistence.validator.annotation.CompareDates;
import org.egov.infra.persistence.validator.annotation.DateFormat;
import org.egov.infra.persistence.validator.annotation.OptionalPattern;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.ValidateDate;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infstr.models.BaseModel;
import org.egov.lcms.masters.entity.InterimOrder;
import org.egov.lcms.utils.LcmsConstants;
import org.hibernate.validator.constraints.Length;

/**
 * Lcinterimorder entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@CompareDates(fromDate = "sendtoStandingCounsel", toDate = "iodate", dateFormat = "dd/MM/yyyy", message = "sendtoStandingCounsel.greaterThan.iodate")
public class Lcinterimorder extends BaseModel {
    /**
     * Serial version uid
     */
    private static final long serialVersionUID = 1L;

    // Fields

    private Legalcase eglcLegalcase;
    @Required(message = "io.select.iotype")
    private InterimOrder interimOrder;
    @DateFormat(message = "invalid.fieldvalue.model.iodate")
    @ValidateDate(allowPast = true, dateFormat = LcmsConstants.DATE_FORMAT, message = "ioDate.notAllow.futureDate")
    @Required(message = "interimorder.date.null")
    private Date iodate;
    @Length(max = 50, message = "io.mpNum.length")
    @OptionalPattern(regex = LcmsConstants.searchMixedCharType1, message = "io.mpnumber.text")
    private String mpnumber;
    @Length(max = 1024, message = "io.notes.length")
    private String notes;
    @DateFormat(message = "invalid.fieldvalue.model.sendtoStandingCounsel")
    private Date sendtoStandingCounsel;
    @DateFormat(message = "invalid.fieldvalue.model.petitionFiledOn")
    private Date petitionFiledOn;
    @DateFormat(message = "invalid.fieldvalue.model.reportFilingDue")
    private Date reportFilingDue;
    @DateFormat(message = "invalid.fieldvalue.model.sendtoDepartment")
    private Date sendtoDepartment;
    @DateFormat(message = "invalid.fieldvalue.model.reportFromHod")
    private Date reportFromHod;
    @DateFormat(message = "invalid.fieldvalue.model.reportSendtoStandingCounsel")
    private Date reportSendtoStandingCounsel;
    @DateFormat(message = "invalid.fieldvalue.model.reportFilingDate")
    private Date reportFilingDate;
    private Long documentNum;
    private EgwStatus egwStatus;
    @Length(max = 50, message = "ti.referencenumber.length")
    @OptionalPattern(regex = LcmsConstants.referenceNumberTIRegx, message = "ti.referencenumber.alphanumeric")
    private String referenceNumber;

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

    public InterimOrder getInterimOrder() {
        return this.interimOrder;
    }

    public void setInterimOrder(InterimOrder interimOrder) {
        this.interimOrder = interimOrder;
    }

    public Date getIodate() {
        return this.iodate;
    }

    public void setIodate(Date iodate) {
        this.iodate = iodate;
    }

    public String getMpnumber() {
        return this.mpnumber;
    }

    public void setMpnumber(String mpnumber) {
        this.mpnumber = mpnumber;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getSendtoStandingCounsel() {
        return sendtoStandingCounsel;
    }

    public void setSendtoStandingCounsel(Date sendtoStandingCounsel) {
        this.sendtoStandingCounsel = sendtoStandingCounsel;
    }

    public Date getPetitionFiledOn() {
        return petitionFiledOn;
    }

    public void setPetitionFiledOn(Date petitionFiledOn) {
        this.petitionFiledOn = petitionFiledOn;
    }

    public Date getReportFilingDue() {
        return reportFilingDue;
    }

    public void setReportFilingDue(Date reportFilingDue) {
        this.reportFilingDue = reportFilingDue;
    }

    public Date getSendtoDepartment() {
        return sendtoDepartment;
    }

    public void setSendtoDepartment(Date sendtoDepartment) {
        this.sendtoDepartment = sendtoDepartment;
    }

    public Date getReportFromHod() {
        return reportFromHod;
    }

    public void setReportFromHod(Date reportFromHod) {
        this.reportFromHod = reportFromHod;
    }

    public Date getReportSendtoStandingCounsel() {
        return reportSendtoStandingCounsel;
    }

    public void setReportSendtoStandingCounsel(Date reportSendtoStandingCounsel) {
        this.reportSendtoStandingCounsel = reportSendtoStandingCounsel;
    }

    public Date getReportFilingDate() {
        return reportFilingDate;
    }

    public void setReportFilingDate(Date reportFilingDate) {
        this.reportFilingDate = reportFilingDate;
    }

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<ValidationError>();

        if (getInterimOrder() != null
                && getInterimOrder().getInterimOrderType()
                        .equals("Report File") && getReportFilingDue() == null) {
            errors.add(new ValidationError("reportFilingDue",
                    "reportFilingDue.required"));
        }

        if (!DateUtils.compareDates(getIodate(), eglcLegalcase.getCasedate()))
            errors.add(new ValidationError("ioDate",
                    "ioDate.greaterThan.caseDate"));

        if (!DateUtils.compareDates(getPetitionFiledOn(),
                getSendtoStandingCounsel()))
            errors.add(new ValidationError("petitionFiledOn",
                    "petitionFiledOn.greaterThan.sendtostandingcounsel"));

        if (!DateUtils.compareDates(getReportFilingDue(), getIodate()))
            errors.add(new ValidationError("iodate",
                    "reportFilingDue.greaterThan.iodate"));

        if (!DateUtils.compareDates(getSendtoDepartment(), getIodate()))
            errors.add(new ValidationError("iodate",
                    "sendtoDepartment.greaterThan.iodate"));

        if (!DateUtils.compareDates(getReportSendtoStandingCounsel(),
                getReportFromHod()))
            errors.add(new ValidationError("reportFromHod",
                    "reportFromHod.greaterThan.reportSendtoStandingCounsel"));

        if (!DateUtils.compareDates(getReportFilingDate(),
                getReportSendtoStandingCounsel()))
            errors
                    .add(new ValidationError("reportFilingDate",
                            "reportSendtoStandingCounsel.greaterThan.reportFilingDate"));

        return errors;
    }

    public EgwStatus getEgwStatus() {
        return egwStatus;
    }

    public void setEgwStatus(EgwStatus egwStatus) {
        this.egwStatus = egwStatus;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public Boolean getReplytoTI() {
        if (this.getEgwStatus().getCode().equals(LcmsConstants.LEGALCASE_STATUS_ORDER_REPLYTOTI)) {
            return Boolean.TRUE;
        }
        else {
            return Boolean.FALSE;
        }
    }

}