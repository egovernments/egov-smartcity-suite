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
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.egov.commons.EgwStatus;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.CompareDates;
import org.egov.infra.persistence.validator.annotation.DateFormat;
import org.egov.infra.persistence.validator.annotation.OptionalPattern;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.ValidateDate;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.lcms.masters.entity.InterimOrder;
import org.egov.lcms.utils.LcmsConstants;
import org.hibernate.validator.constraints.Length;

/**
 * Lcinterimorder entity.
 *
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EGLC_LCINTERIMORDER")
@SequenceGenerator(name = Lcinterimorder.SEQ_EGLC_LCINTERIMORDER, sequenceName = Lcinterimorder.SEQ_EGLC_LCINTERIMORDER, allocationSize = 1)
@CompareDates(fromDate = "sendtoStandingCounsel", toDate = "iodate", dateFormat = "dd/MM/yyyy", message = "sendtoStandingCounsel.greaterThan.iodate")
public class Lcinterimorder extends AbstractAuditable {
    private static final long serialVersionUID = 1517694643078084884L;
    public static final String SEQ_EGLC_LCINTERIMORDER = "SEQ_EGLC_LCINTERIMORDER";

    @Id
    @GeneratedValue(generator = SEQ_EGLC_LCINTERIMORDER, strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @Valid
    @NotNull
    @JoinColumn(name = "LEGALCASE", nullable = false)
    private Legalcase eglcLegalcase;
    @Required(message = "io.select.iotype")
    @ManyToOne(cascade = CascadeType.ALL)
    @Valid
    @NotNull
    @JoinColumn(name = "intordertypeid", nullable = false)
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
    @ManyToOne(cascade = CascadeType.ALL)
    @Valid
    @NotNull
    @JoinColumn(name = "STATUS", nullable = false)
    private EgwStatus egwStatus;
    @Length(max = 50, message = "ti.referencenumber.length")
    @OptionalPattern(regex = LcmsConstants.referenceNumberTIRegx, message = "ti.referencenumber.alphanumeric")
    private String referenceNumber;

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

    public InterimOrder getInterimOrder() {
        return interimOrder;
    }

    public void setInterimOrder(final InterimOrder interimOrder) {
        this.interimOrder = interimOrder;
    }

    public Date getIodate() {
        return iodate;
    }

    public void setIodate(final Date iodate) {
        this.iodate = iodate;
    }

    public String getMpnumber() {
        return mpnumber;
    }

    public void setMpnumber(final String mpnumber) {
        this.mpnumber = mpnumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(final String notes) {
        this.notes = notes;
    }

    public Date getSendtoStandingCounsel() {
        return sendtoStandingCounsel;
    }

    public void setSendtoStandingCounsel(final Date sendtoStandingCounsel) {
        this.sendtoStandingCounsel = sendtoStandingCounsel;
    }

    public Date getPetitionFiledOn() {
        return petitionFiledOn;
    }

    public void setPetitionFiledOn(final Date petitionFiledOn) {
        this.petitionFiledOn = petitionFiledOn;
    }

    public Date getReportFilingDue() {
        return reportFilingDue;
    }

    public void setReportFilingDue(final Date reportFilingDue) {
        this.reportFilingDue = reportFilingDue;
    }

    public Date getSendtoDepartment() {
        return sendtoDepartment;
    }

    public void setSendtoDepartment(final Date sendtoDepartment) {
        this.sendtoDepartment = sendtoDepartment;
    }

    public Date getReportFromHod() {
        return reportFromHod;
    }

    public void setReportFromHod(final Date reportFromHod) {
        this.reportFromHod = reportFromHod;
    }

    public Date getReportSendtoStandingCounsel() {
        return reportSendtoStandingCounsel;
    }

    public void setReportSendtoStandingCounsel(final Date reportSendtoStandingCounsel) {
        this.reportSendtoStandingCounsel = reportSendtoStandingCounsel;
    }

    public Date getReportFilingDate() {
        return reportFilingDate;
    }

    public void setReportFilingDate(final Date reportFilingDate) {
        this.reportFilingDate = reportFilingDate;
    }

    public List<ValidationError> validate() {
        final List<ValidationError> errors = new ArrayList<ValidationError>();

        if (getInterimOrder() != null && getInterimOrder().getInterimOrderType().equals("Report File")
                && getReportFilingDue() == null)
            errors.add(new ValidationError("reportFilingDue", "reportFilingDue.required"));

        if (!DateUtils.compareDates(getIodate(), eglcLegalcase.getCasedate()))
            errors.add(new ValidationError("ioDate", "ioDate.greaterThan.caseDate"));

        if (!DateUtils.compareDates(getPetitionFiledOn(), getSendtoStandingCounsel()))
            errors.add(new ValidationError("petitionFiledOn", "petitionFiledOn.greaterThan.sendtostandingcounsel"));

        if (!DateUtils.compareDates(getReportFilingDue(), getIodate()))
            errors.add(new ValidationError("iodate", "reportFilingDue.greaterThan.iodate"));

        if (!DateUtils.compareDates(getSendtoDepartment(), getIodate()))
            errors.add(new ValidationError("iodate", "sendtoDepartment.greaterThan.iodate"));

        if (!DateUtils.compareDates(getReportSendtoStandingCounsel(), getReportFromHod()))
            errors.add(new ValidationError("reportFromHod", "reportFromHod.greaterThan.reportSendtoStandingCounsel"));

        if (!DateUtils.compareDates(getReportFilingDate(), getReportSendtoStandingCounsel()))
            errors.add(new ValidationError("reportFilingDate",
                    "reportSendtoStandingCounsel.greaterThan.reportFilingDate"));

        return errors;
    }

    public EgwStatus getEgwStatus() {
        return egwStatus;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public void setEgwStatus(final EgwStatus egwStatus) {
        this.egwStatus = egwStatus;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(final String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public Boolean getReplytoTI() {
        if (getEgwStatus().getCode().equals(LcmsConstants.LEGALCASE_STATUS_ORDER_REPLYTOTI))
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }

}