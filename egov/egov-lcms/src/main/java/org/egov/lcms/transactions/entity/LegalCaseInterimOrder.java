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

import org.egov.eis.entity.Employee;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.ValidateDate;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.lcms.masters.entity.InterimOrder;
import org.egov.lcms.utils.constants.LcmsConstants;
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
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "EGLC_LCINTERIMORDER")
@SequenceGenerator(name = LegalCaseInterimOrder.SEQ_EGLC_LCINTERIMORDER, sequenceName = LegalCaseInterimOrder.SEQ_EGLC_LCINTERIMORDER, allocationSize = 1)
/*
 * @CompareDates(fromDate = "sendtoStandingCounsel", toDate = "iodate",
 * dateFormat = "dd/MM/yyyy", message =
 * "sendtoStandingCounsel.greaterThan.iodate")
 */
@AuditOverrides({ @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedBy"),
        @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedDate") })
public class LegalCaseInterimOrder extends AbstractAuditable {
    private static final long serialVersionUID = 1517694643078084884L;
    public static final String SEQ_EGLC_LCINTERIMORDER = "SEQ_EGLC_LCINTERIMORDER";

    @Id
    @GeneratedValue(generator = SEQ_EGLC_LCINTERIMORDER, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Valid
    @NotNull
    @JoinColumn(name = "LEGALCASE", nullable = false)
    @ManyToOne
    @Audited
    private LegalCase legalCase;

    @Valid
    @NotNull
    @JoinColumn(name = "interimorder", nullable = false)
    @ManyToOne
    @Audited
    private InterimOrder interimOrder;

    @Temporal(TemporalType.DATE)
    @ValidateDate(allowPast = true, dateFormat = LcmsConstants.DATE_FORMAT)
    @NotNull
    @Column(name = "iodate")
    @Audited
    private Date ioDate;

    @Length(max = 50)
    @Column(name = "mpnumber")
    @Audited
    private String mpNumber;

    @Length(max = 1024)
    @Audited
    private String notes;

    @Temporal(TemporalType.DATE)
    @Column(name = "sendtostandingcounsel")
    @Audited
    private Date sendtoStandingCounsel;

    @Temporal(TemporalType.DATE)
    @Column(name = "petitionfiledon")
    private Date petitionFiledOn;

    @Temporal(TemporalType.DATE)
    @Column(name = "reportfilingdue")
    @Audited
    private Date reportFilingDue;

    @Temporal(TemporalType.DATE)
    @Column(name = "senttodepartment")
    @Audited
    private Date sendtoDepartment;

    @Temporal(TemporalType.DATE)
    @Column(name = "reportfromhod")
    @Audited
    private Date reportFromHod;

    @Temporal(TemporalType.DATE)
    @Column(name = "reportsendtostandingcounsel")
    @Audited
    private Date reportSendtoStandingCounsel;

    @Temporal(TemporalType.DATE)
    @Column(name = "reportfilingdate")
    @Audited
    private Date reportFilingDate;

    @Length(max = 50)
    @Audited
    private String referenceNumber;

    @Audited
    @Column(name = "actionitem")
    private String actionItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee")
    @Audited
    private Employee employee;

    @Temporal(TemporalType.DATE)
    @Column(name = "duedate")
    @Audited
    private Date dueDate;

    @Audited
    @Column(name = "actiontaken")
    private String actionTaken;

    @OneToMany(mappedBy = "legalCaseInterimOrder", fetch = FetchType.LAZY)
    @NotAudited
    private List<LcInterimOrderDocuments> lcInterimOrderDocuments = new ArrayList<LcInterimOrderDocuments>(0);

    @OneToMany(mappedBy = "legalCaseInterimOrder", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Audited
    private List<VacateStay> vacateStay = new ArrayList<VacateStay>(0);

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public InterimOrder getInterimOrder() {
        return interimOrder;
    }

    public void setInterimOrder(final InterimOrder interimOrder) {
        this.interimOrder = interimOrder;
    }

    public List<LcInterimOrderDocuments> getLcInterimOrderDocuments() {
        return lcInterimOrderDocuments;
    }

    public void setLcInterimOrderDocuments(final List<LcInterimOrderDocuments> lcInterimOrderDocuments) {
        this.lcInterimOrderDocuments = lcInterimOrderDocuments;
    }

    public Date getIoDate() {
        return ioDate;
    }

    public void setIoDate(final Date ioDate) {
        this.ioDate = ioDate;
    }

    public String getMpNumber() {
        return mpNumber;
    }

    public void setMpNumber(final String mpNumber) {
        this.mpNumber = mpNumber;
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

        if (!DateUtils.compareDates(getIoDate(), legalCase.getCaseDate()))
            errors.add(new ValidationError("ioDate", "ioDate.greaterThan.caseDate"));

        if (!DateUtils.compareDates(getPetitionFiledOn(), getSendtoStandingCounsel()))
            errors.add(new ValidationError("petitionFiledOn", "petitionFiledOn.greaterThan.sendtostandingcounsel"));

        if (!DateUtils.compareDates(getReportFilingDue(), getIoDate()))
            errors.add(new ValidationError("iodate", "reportFilingDue.greaterThan.iodate"));

        if (!DateUtils.compareDates(getSendtoDepartment(), getIoDate()))
            errors.add(new ValidationError("iodate", "sendtoDepartment.greaterThan.iodate"));

        if (!DateUtils.compareDates(getReportSendtoStandingCounsel(), getReportFromHod()))
            errors.add(new ValidationError("reportFromHod", "reportFromHod.greaterThan.reportSendtoStandingCounsel"));

        if (!DateUtils.compareDates(getReportFilingDate(), getReportSendtoStandingCounsel()))
            errors.add(new ValidationError("reportFilingDate",
                    "reportSendtoStandingCounsel.greaterThan.reportFilingDate"));

        return errors;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(final String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public LegalCase getLegalCase() {
        return legalCase;
    }

    public void setLegalCase(final LegalCase legalCase) {
        this.legalCase = legalCase;
    }

    public List<VacateStay> getVacateStay() {

        return vacateStay;
    }

    public void setVacateStay(final List<VacateStay> vacateStay) {
        this.vacateStay = vacateStay;
    }

    public String getActionItem() {
        return actionItem;
    }

    public void setActionItem(final String actionItem) {
        this.actionItem = actionItem;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(final Employee employee) {
        this.employee = employee;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(final Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getActionTaken() {
        return actionTaken;
    }

    public void setActionTaken(final String actionTaken) {
        this.actionTaken = actionTaken;
    }

}