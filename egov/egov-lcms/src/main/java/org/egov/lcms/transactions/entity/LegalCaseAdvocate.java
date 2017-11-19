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

import org.apache.commons.lang.StringUtils;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.lcms.masters.entity.AdvocateMaster;
import org.egov.lcms.masters.entity.CaseStage;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "EGLC_LEGALCASE_ADVOCATE")
@SequenceGenerator(name = LegalCaseAdvocate.SEQ_EGLC_LEGALCASE_ADVOCATE, sequenceName = LegalCaseAdvocate.SEQ_EGLC_LEGALCASE_ADVOCATE, allocationSize = 1)
@AuditOverrides({ @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedBy"),
        @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedDate") })
public class LegalCaseAdvocate extends AbstractAuditable {

    private static final long serialVersionUID = 1517694643078084884L;
    public static final String SEQ_EGLC_LEGALCASE_ADVOCATE = "SEQ_EGLC_LEGALCASE_ADVOCATE";

    @Id
    @GeneratedValue(generator = SEQ_EGLC_LEGALCASE_ADVOCATE, strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "legalcase", nullable = false)
    @Audited
    private LegalCase legalCase;

    @ManyToOne
    @JoinColumn(name = "advocatemaster")
    @Audited
    private AdvocateMaster advocateMaster;

    @Temporal(TemporalType.DATE)
    @Column(name = "assignedtodate")
    @Audited
    private Date assignedToDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "seniorassignedtodate")
    @Audited
    private Date assignedToDateForSenior;

    @Temporal(TemporalType.DATE)
    @Column(name = "vakalatdate")
    @Audited
    private Date vakalatDate;

    @Audited
    private Boolean isActive;

    @Length(max = 32)
    @Audited
    @Column(name = "ordernumber")
    private String orderNumber;

    @Temporal(TemporalType.DATE)
    @Audited
    @Column(name = "orderdate")
    private Date orderDate;

    @ManyToOne
    @JoinColumn(name = "senioradvocate")
    @Audited
    private AdvocateMaster seniorAdvocate;

    @Length(max = 32)
    @JoinColumn(name = "ordernumberjunior")
    @Audited
    private String orderNumberJunior;

    @Temporal(TemporalType.DATE)
    @Column(name = "orderdatejunior")
    @Audited
    private Date orderDateJunior;

    @ManyToOne
    @JoinColumn(name = "JUNIORSTAGE")
    @Audited
    private CaseStage juniorStage;

    @ManyToOne
    @JoinColumn(name = "SENIORSTAGE")
    @Audited
    private CaseStage seniorStage;

    @Length(max = 256)
    @Audited
    @Column(name = "reassignmentreasonjunior")
    private String reassignmentReasonJunior;

    @Length(max = 256)
    @Audited
    @Column(name = "reassignmentreasonsenior")
    private String reassignmentReasonSenior;

    @Audited
    private Boolean changeAdvocate = Boolean.FALSE;

    @Audited
    private Boolean changeSeniorAdvocate = Boolean.FALSE;

    @Transient
    @Audited
    private Boolean isSeniorAdvocate = Boolean.FALSE;

    public Boolean getChangeAdvocate() {
        return changeAdvocate;
    }

    public void setChangeAdvocate(final Boolean changeAdvocate) {
        this.changeAdvocate = changeAdvocate;
    }

    public Boolean getChangeSeniorAdvocate() {
        return changeSeniorAdvocate;
    }

    public void setChangeSeniorAdvocate(final Boolean changeSeniorAdvocate) {
        this.changeSeniorAdvocate = changeSeniorAdvocate;
    }

    public AdvocateMaster getAdvocateMaster() {
        return advocateMaster;
    }

    public void setAdvocateMaster(final AdvocateMaster advocateMaster) {
        this.advocateMaster = advocateMaster;
    }

    public Date getAssignedToDate() {
        return assignedToDate;
    }

    public void setAssignedToDate(final Date assignedToDate) {
        this.assignedToDate = assignedToDate;
    }

    public Date getAssignedToDateForSenior() {
        return assignedToDateForSenior;
    }

    public void setAssignedToDateForSenior(final Date assignedToDateForSenior) {
        this.assignedToDateForSenior = assignedToDateForSenior;
    }

    public Date getVakalatDate() {
        return vakalatDate;
    }

    public void setVakalatDate(final Date vakalatDate) {
        this.vakalatDate = vakalatDate;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(final String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(final Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderNumberJunior() {
        return orderNumberJunior;
    }

    public void setOrderNumberJunior(final String orderNumberJunior) {
        this.orderNumberJunior = orderNumberJunior;
    }

    public Date getOrderDateJunior() {
        return orderDateJunior;
    }

    public void setOrderDateJunior(final Date orderDateJunior) {
        this.orderDateJunior = orderDateJunior;
    }

    public String getReassignmentReasonJunior() {
        return reassignmentReasonJunior;
    }

    public void setReassignmentReasonJunior(final String reassignmentReasonJunior) {
        this.reassignmentReasonJunior = reassignmentReasonJunior;
    }

    public String getReassignmentReasonSenior() {
        return reassignmentReasonSenior;
    }

    public void setReassignmentReasonSenior(final String reassignmentReasonSenior) {
        this.reassignmentReasonSenior = reassignmentReasonSenior;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(final Boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public List<ValidationError> validate() {
        final List<ValidationError> errors = new ArrayList<ValidationError>();
        if (legalCase.getIsSenioradvrequired()) {
            if (getSeniorAdvocate() == null)
                errors.add(new ValidationError("seniorAdvocate", "legalcase.seniorAdvocate.null"));
            if (StringUtils.isBlank(getOrderNumber()))
                errors.add(new ValidationError("orderNumber", "legalcase.ordernumber.null"));
            if (getOrderNumber() == null)
                errors.add(new ValidationError("orderDate", "legalcase.orderdate.null"));
            if (!DateUtils.compareDates(getOrderDate(), legalCase.getCaseReceivingDate()))
                errors.add(new ValidationError("orderDate", "orderdate.less.casereceivingdate"));
            if (!DateUtils.compareDates(getOrderDate(), legalCase.getCaseDate()))
                errors.add(new ValidationError("orderDate", "orderdate.less.casedate"));
            if (!DateUtils.compareDates(getAssignedToDateForSenior(), getOrderDate()))
                errors.add(new ValidationError("assignedToDateSenior", "assignedon.less.orderdate"));
        }

        if (!DateUtils.compareDates(getAssignedToDate(), legalCase.getCaseReceivingDate()))
            errors.add(new ValidationError("assignedon", "assignedon.less.casereceivingdate"));
        if (!DateUtils.compareDates(getAssignedToDate(), legalCase.getCaseDate()))
            errors.add(new ValidationError("assignedon", "assignedon.less.casedate"));
        if (!DateUtils.compareDates(getVakalatDate(), legalCase.getCaseDate()))
            errors.add(new ValidationError("vakalatDate", "vakalatdate.less.casedate"));
        if (!DateUtils.compareDates(getVakalatDate(), legalCase.getCaseReceivingDate()))
            errors.add(new ValidationError("vakalatDate", "vakalatdate.less.caserecdate"));
        if (!DateUtils.compareDates(getVakalatDate(), getAssignedToDate()))
            errors.add(new ValidationError("vakalatDate", "vakalatdate.less.assingedon"));
        return errors;
    }

    public AdvocateMaster getSeniorAdvocate() {
        return seniorAdvocate;
    }

    public void setSeniorAdvocate(final AdvocateMaster seniorAdvocate) {
        this.seniorAdvocate = seniorAdvocate;
    }

    public CaseStage getJuniorStage() {
        return juniorStage;
    }

    public void setJuniorStage(final CaseStage juniorStage) {
        this.juniorStage = juniorStage;
    }

    public CaseStage getSeniorStage() {
        return seniorStage;
    }

    public void setSeniorStage(final CaseStage seniorStage) {
        this.seniorStage = seniorStage;
    }

    public LegalCase getLegalCase() {
        return legalCase;
    }

    public void setLegalCase(final LegalCase legalCase) {
        this.legalCase = legalCase;
    }

    public Boolean getIsSeniorAdvocate() {
        return isSeniorAdvocate;
    }

    public void setIsSeniorAdvocate(final Boolean isSeniorAdvocate) {
        this.isSeniorAdvocate = isSeniorAdvocate;
    }

}