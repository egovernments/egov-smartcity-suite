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
import org.egov.infra.persistence.validator.annotation.DateFormat;
import org.egov.infra.persistence.validator.annotation.OptionalPattern;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.lcms.utils.constants.LcmsConstants;
import org.hibernate.validator.constraints.Length;

/**
 * Hearings entity.
 *
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EGLC_HEARINGS")
@SequenceGenerator(name = Hearings.SEQ_EGLC_HEARINGS, sequenceName = Hearings.SEQ_EGLC_HEARINGS, allocationSize = 1)
public class Hearings extends AbstractAuditable {
    /**
     * Serial version uid
     */

    private static final long serialVersionUID = 1517694643078084884L;
    public static final String SEQ_EGLC_HEARINGS = "SEQ_EGLC_HEARINGS";

    @Id
    @GeneratedValue(generator = SEQ_EGLC_HEARINGS, strategy = GenerationType.SEQUENCE)
    private Long id;
    @DateFormat(message = "invalid.fieldvalue.model.hearingDate")
    @Required(message = "hearing.date.null")
    private Date hearingDate;
    @ManyToOne
    @NotNull
    @Valid
    @JoinColumn(name = "legalcase", nullable = false)
    private Legalcase legalcase;
    private boolean isStandingcounselpresent;
    @Length(max = 128, message = "hearing.additionalLawyer.length")
    @OptionalPattern(regex = LcmsConstants.mixedChar, message = "hearing.additionalLawyerName.text")
    private String additionalLawyers;
    /*
     * @OneToMany(mappedBy = "EMPLOYEE", fetch = FetchType.LAZY, cascade =
     * CascadeType.ALL, orphanRemoval = true) private Set<PersonalInformation>
     * eglcEmployeehearings = new HashSet<PersonalInformation>(0);* need to
     * check
     */
    @Length(max = 1024, message = "hearing.outcome.length")
    private String hearingOutcome;
    private boolean isSeniorStandingcounselpresent;
    @Length(max = 1024, message = "hearing.purpose.length")
    private String purposeofHearings;
    @ManyToOne
    @NotNull
    @Valid
    @JoinColumn(name = "STATUS", nullable = false)
    private EgwStatus status;
    @Length(max = 50, message = "ti.referencenumber.length")
    @OptionalPattern(regex = LcmsConstants.referenceNumberTIRegx, message = "ti.referencenumber.alphanumeric")
    private String referenceNumber;

    /*
     * public void addEmployee(final PersonalInformation empObj) {
     * eglcEmployeehearings.add(empObj); }
     */

    public Date getHearingDate() {
        return hearingDate;
    }

    public void setHearingDate(final Date hearingDate) {
        this.hearingDate = hearingDate;
    }

    public Legalcase getLegalcase() {
        return legalcase;
    }

    public void setLegalcase(final Legalcase eglcLegalcase) {
        legalcase = eglcLegalcase;
    }

    public String getAdditionalLawyers() {
        return additionalLawyers;
    }

    public void setAdditionalLawyers(final String additionalLawyers) {
        this.additionalLawyers = additionalLawyers;
    }

    public boolean getIsStandingcounselpresent() {
        return isStandingcounselpresent;
    }

    public void setIsStandingcounselpresent(final boolean isStandingcounselpresent) {
        this.isStandingcounselpresent = isStandingcounselpresent;
    }

    /*
     * public Set<PersonalInformation> getEglcEmployeehearings() { return
     * eglcEmployeehearings; } public void setEglcEmployeehearings(final
     * Set<PersonalInformation> eglcEmployeehearings) {
     * this.eglcEmployeehearings = eglcEmployeehearings; }
     */

    public String getHearingOutcome() {
        return hearingOutcome;
    }

    public void setHearingOutcome(final String hearingOutcome) {
        this.hearingOutcome = hearingOutcome;
    }

    public boolean getIsSeniorStandingcounselpresent() {
        return isSeniorStandingcounselpresent;
    }

    public void setIsSeniorStandingcounselpresent(final boolean isSeniorStandingcounselpresent) {
        this.isSeniorStandingcounselpresent = isSeniorStandingcounselpresent;
    }

    public String getPurposeofHearings() {
        return purposeofHearings;
    }

    public void setPurposeofHearings(final String purposeofHearings) {
        this.purposeofHearings = purposeofHearings;
    }

    public Date getCaDueDate() {
        Date caDueDate = null;
        for (final Pwr pwr : getLegalcase().getEglcPwrs())
            caDueDate = pwr.getCaDueDate();
        return caDueDate;
    }

    public List<ValidationError> validate() {
        final List<ValidationError> errors = new ArrayList<ValidationError>();
        if (getHearingDate() != null) {

            if (getCaDueDate() != null && !DateUtils.compareDates(getHearingDate(), getCaDueDate()))
                errors.add(new ValidationError("hearingDate", "hearingDate.greaterThan.caDueDate"));
            if (legalcase.getCaseReceivingDate() != null
                    && !DateUtils.compareDates(getHearingDate(), legalcase.getCaseReceivingDate()))
                errors.add(new ValidationError("hearingDate", "hearingDate.greaterThan.caseReceivingDate"));
            if (!DateUtils.compareDates(getHearingDate(), legalcase.getCasedate()))
                errors.add(new ValidationError("hearingDate", "hearingDate.greaterThan.caseDate"));

        }
        return errors;
    }

    public EgwStatus getStatus() {
        return status;
    }

    public void setStatus(final EgwStatus egwStatus) {
        status = egwStatus;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(final String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public Boolean getReplytoTI() {
        if (getStatus().getCode().equals(LcmsConstants.LEGALCASE_STATUS_HEARING_REPLYTOTI))
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public void setStandingcounselpresent(final boolean isStandingcounselpresent) {
        this.isStandingcounselpresent = isStandingcounselpresent;
    }

    public void setSeniorStandingcounselpresent(final boolean isSeniorStandingcounselpresent) {
        this.isSeniorStandingcounselpresent = isSeniorStandingcounselpresent;
    }

}