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

import javax.persistence.Column;
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

import org.apache.commons.lang.StringUtils;
import org.egov.infra.persistence.entity.AbstractPersistable;
import org.egov.infra.persistence.validator.annotation.DateFormat;
import org.egov.infra.persistence.validator.annotation.OptionalPattern;
import org.egov.infra.persistence.validator.annotation.ValidateDate;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.lcms.masters.entity.AdvocateMaster;
import org.egov.lcms.masters.entity.CaseStage;
import org.egov.lcms.utils.constants.LcmsConstants;
import org.hibernate.validator.constraints.Length;

/**
 * LegalcaseAdvocate entity.
 *
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EGLC_LEGALCASE_ADVOCATE")
@SequenceGenerator(name = LegalcaseAdvocate.SEQ_EGLC_LEGALCASE_ADVOCATE, sequenceName = LegalcaseAdvocate.SEQ_EGLC_LEGALCASE_ADVOCATE, allocationSize = 1)
public class LegalcaseAdvocate extends AbstractPersistable<Long> {

    private static final long serialVersionUID = 1517694643078084884L;
    public static final String SEQ_EGLC_LEGALCASE_ADVOCATE = "SEQ_EGLC_LEGALCASE_ADVOCATE";

    @Id
    @GeneratedValue(generator = SEQ_EGLC_LEGALCASE_ADVOCATE, strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne
    @NotNull
    @JoinColumn(name = "legalcase", nullable = false)
    private Legalcase legalcase;
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "advocatemaster", nullable = false)
    private AdvocateMaster advocateMaster;

    @DateFormat(message = "invalid.fieldvalue.assignedOnDate")
    @ValidateDate(allowPast = true, dateFormat = LcmsConstants.DATE_FORMAT, message = "invalid.assignedtodate.date")
    private Date assignedtodate;
    @DateFormat(message = "invalid.fieldvalue.assignedOnForSeniorAdv")
    // @ValidateDate(allowPast = true, dateFormat = LcmsConstants.DATE_FORMAT,
    // message = "invalid.assignedtodateForsenior.date")
    @Column(name = "seniorassignedtodate")
    private Date assignedtodateForsenior;
    @DateFormat(message = "invalid.fieldvalue.vakalaatDate")
    @ValidateDate(allowPast = true, dateFormat = LcmsConstants.DATE_FORMAT, message = "invalid.vakalatdate.date")
    private Date vakalatdate;
    private Boolean isActive;
    @Length(max = 32, message = "ordernumber.length")
    @OptionalPattern(regex = LcmsConstants.orderNumberFormat, message = "orderNumber.format")
    private String ordernumber;
    @DateFormat(message = "invalid.fieldvalue.orderDate")
    @ValidateDate(allowPast = true, dateFormat = LcmsConstants.DATE_FORMAT, message = "invalid.orderdate.date")
    private Date orderdate;
    @ManyToOne
    @JoinColumn(name = "senioradvocate")
    private AdvocateMaster eglcSeniorAdvocateMaster;
    @Length(max = 32, message = "ordernumberJunior.length")
    @JoinColumn(name = "ordernumberjunior")
    private String ordernumberJunior;
    @DateFormat(message = "invalid.fieldvalue.juniororderDate")
    @Column(name = "orderdatejunior")
    private Date orderdateJunior;
    @ManyToOne
    @JoinColumn(name = "JUNIORSTAGE")
    private CaseStage juniorStage;
    @ManyToOne
    @JoinColumn(name = "SENIORSTAGE")
    private CaseStage seniorStage;
    @Length(max = 256, message = "reassignmentJunior.length")
    private String reassignmentreasonjunior;
    @Length(max = 256, message = "reassignmentSenior.length")
    private String reassignmentreasonsenior;
    private Boolean changeAdvocate = Boolean.FALSE;
    private Boolean changeSeniorAdvocate = Boolean.FALSE;

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

    public Legalcase getLegalcase() {
        return legalcase;
    }

    public void setLegalcase(final Legalcase legalcase) {
        this.legalcase = legalcase;
    }

    public AdvocateMaster getAdvocateMaster() {
        return advocateMaster;
    }

    public void setAdvocateMaster(final AdvocateMaster advocateMaster) {
        this.advocateMaster = advocateMaster;
    }

    public Date getAssignedtodate() {
        return assignedtodate;
    }

    public void setAssignedtodate(final Date assignedtodate) {
        this.assignedtodate = assignedtodate;
    }

    public Date getVakalatdate() {
        return vakalatdate;
    }

    public void setVakalatdate(final Date vakalatdate) {
        this.vakalatdate = vakalatdate;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(final Boolean isActive) {
        this.isActive = isActive;
    }

    public String getOrdernumber() {
        return ordernumber;
    }

    public void setOrdernumber(final String ordernumber) {
        this.ordernumber = ordernumber;
    }

    public Date getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(final Date orderdate) {
        this.orderdate = orderdate;
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
        if (legalcase.getIsSenioradvrequired()) {
            if (getEglcSeniorAdvocateMaster() == null)
                errors.add(new ValidationError("eglcSeniorAdvocateMaster", "legalcase.eglcSeniorAdvocateMaster.null"));
            if (StringUtils.isBlank(getOrdernumber()))
                errors.add(new ValidationError("ordernumber", "legalcase.ordernumber.null"));
            if (getOrderdate() == null)
                errors.add(new ValidationError("orderDate", "legalcase.orderdate.null"));
            if (!DateUtils.compareDates(getOrderdate(), legalcase.getCaseReceivingDate()))
                errors.add(new ValidationError("orderDate", "orderdate.less.casereceivingdate"));
            if (!DateUtils.compareDates(getOrderdate(), legalcase.getCasedate()))
                errors.add(new ValidationError("orderDate", "orderdate.less.casedate"));
            if (!DateUtils.compareDates(getAssignedtodateForsenior(), getOrderdate()))
                errors.add(new ValidationError("assignedtodatesenior", "assignedon.less.orderdate"));
        }

        if (!DateUtils.compareDates(getAssignedtodate(), legalcase.getCaseReceivingDate()))
            errors.add(new ValidationError("assignedon", "assignedon.less.casereceivingdate"));
        if (!DateUtils.compareDates(getAssignedtodate(), legalcase.getCasedate()))
            errors.add(new ValidationError("assignedon", "assignedon.less.casedate"));
        if (!DateUtils.compareDates(getVakalatdate(), legalcase.getCasedate()))
            errors.add(new ValidationError("vakalatdate", "vakalatdate.less.casedate"));
        if (!DateUtils.compareDates(getVakalatdate(), legalcase.getCaseReceivingDate()))
            errors.add(new ValidationError("vakalatdate", "vakalatdate.less.caserecdate"));
        if (!DateUtils.compareDates(getVakalatdate(), getAssignedtodate()))
            errors.add(new ValidationError("vakalatdate", "vakalatdate.less.assingedon"));
        return errors;
    }

    public AdvocateMaster getEglcSeniorAdvocateMaster() {
        return eglcSeniorAdvocateMaster;
    }

    public void setEglcSeniorAdvocateMaster(final AdvocateMaster eglcSeniorAdvocateMaster) {
        this.eglcSeniorAdvocateMaster = eglcSeniorAdvocateMaster;
    }

    public Date getAssignedtodateForsenior() {
        return assignedtodateForsenior;
    }

    public void setAssignedtodateForsenior(final Date assignedtodateForsenior) {
        this.assignedtodateForsenior = assignedtodateForsenior;
    }

    public String getOrdernumberJunior() {
        return ordernumberJunior;
    }

    public void setOrdernumberJunior(final String ordernumberJunior) {
        this.ordernumberJunior = ordernumberJunior;
    }

    public Date getOrderdateJunior() {
        return orderdateJunior;
    }

    public void setOrderdateJunior(final Date orderdateJunior) {
        this.orderdateJunior = orderdateJunior;
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

    public String getReassignmentreasonjunior() {
        return reassignmentreasonjunior;
    }

    public void setReassignmentreasonjunior(final String reassignmentreasonjunior) {
        this.reassignmentreasonjunior = reassignmentreasonjunior;
    }

    public String getReassignmentreasonsenior() {
        return reassignmentreasonsenior;
    }

    public void setReassignmentreasonsenior(final String reassignmentreasonsenior) {
        this.reassignmentreasonsenior = reassignmentreasonsenior;
    }

}