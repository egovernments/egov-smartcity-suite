package org.egov.lcms.transactions.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.egov.commons.EgwStatus;
import org.egov.infra.persistence.validator.annotation.DateFormat;
import org.egov.infra.persistence.validator.annotation.OptionalPattern;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infstr.models.BaseModel;
import org.egov.lcms.utils.LcmsConstants;
import org.egov.pims.model.PersonalInformation;
import org.hibernate.validator.constraints.Length;

/**
 * Hearings entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Hearings extends BaseModel {
    /**
     * Serial version uid
     */
    private static final long serialVersionUID = 1L;
    // Fields
    @DateFormat(message = "invalid.fieldvalue.model.hearingDate")
    @Required(message = "hearing.date.null")
    private Date hearingDate;
    private Legalcase legalcase;
    private boolean isStandingcounselpresent;
    @Length(max = 128, message = "hearing.additionalLawyer.length")
    @OptionalPattern(regex = LcmsConstants.mixedChar, message = "hearing.additionalLawyerName.text")
    private String additionalLawyers;
    private Set<PersonalInformation> eglcEmployeehearings = new HashSet<PersonalInformation>(
            0);
    @Length(max = 1024, message = "hearing.outcome.length")
    private String hearingOutcome;
    private boolean isSeniorStandingcounselpresent;
    @Length(max = 1024, message = "hearing.purpose.length")
    private String purposeofHearings;
    private EgwStatus egwStatus;
    @Length(max = 50, message = "ti.referencenumber.length")
    @OptionalPattern(regex = LcmsConstants.referenceNumberTIRegx, message = "ti.referencenumber.alphanumeric")
    private String referenceNumber;

    public void addEmployee(PersonalInformation empObj) {
        this.eglcEmployeehearings.add(empObj);
    }

    public Date getHearingDate() {
        return this.hearingDate;
    }

    public void setHearingDate(Date hearingDate) {
        this.hearingDate = hearingDate;
    }

    public Legalcase getLegalcase() {
        return legalcase;
    }

    public void setLegalcase(Legalcase legalcase) {
        this.legalcase = legalcase;
    }

    public String getAdditionalLawyers() {
        return this.additionalLawyers;
    }

    public void setAdditionalLawyers(String additionalLawyers) {
        this.additionalLawyers = additionalLawyers;
    }

    public boolean getIsStandingcounselpresent() {
        return isStandingcounselpresent;
    }

    public void setIsStandingcounselpresent(boolean isStandingcounselpresent) {
        this.isStandingcounselpresent = isStandingcounselpresent;
    }

    public Set<PersonalInformation> getEglcEmployeehearings() {
        return eglcEmployeehearings;
    }

    public void setEglcEmployeehearings(
            Set<PersonalInformation> eglcEmployeehearings) {
        this.eglcEmployeehearings = eglcEmployeehearings;
    }

    public String getHearingOutcome() {
        return hearingOutcome;
    }

    public void setHearingOutcome(String hearingOutcome) {
        this.hearingOutcome = hearingOutcome;
    }

    public boolean getIsSeniorStandingcounselpresent() {
        return isSeniorStandingcounselpresent;
    }

    public void setIsSeniorStandingcounselpresent(
            boolean isSeniorStandingcounselpresent) {
        this.isSeniorStandingcounselpresent = isSeniorStandingcounselpresent;
    }

    public String getPurposeofHearings() {
        return purposeofHearings;
    }

    public void setPurposeofHearings(String purposeofHearings) {
        this.purposeofHearings = purposeofHearings;
    }

    public Date getCaDueDate() {
        Date caDueDate = null;
        for (Pwr pwr : getLegalcase().getEglcPwrs())
            caDueDate = pwr.getCaDueDate();
        return caDueDate;
    }

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<ValidationError>();
        if (getHearingDate() != null) {

            if (getCaDueDate() != null
                    && !DateUtils
                            .compareDates(getHearingDate(), getCaDueDate())) {
                errors.add(new ValidationError("hearingDate",
                        "hearingDate.greaterThan.caDueDate"));
            }
            if (legalcase.getCaseReceivingDate() != null
                    && !DateUtils.compareDates(getHearingDate(), legalcase
                            .getCaseReceivingDate())) {
                errors.add(new ValidationError("hearingDate",
                        "hearingDate.greaterThan.caseReceivingDate"));
            }
            if (!DateUtils.compareDates(getHearingDate(), legalcase
                    .getCasedate())) {
                errors.add(new ValidationError("hearingDate",
                        "hearingDate.greaterThan.caseDate"));
            }

        }
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
        if (this.getEgwStatus().getCode().equals(LcmsConstants.LEGALCASE_STATUS_HEARING_REPLYTOTI)) {
            return Boolean.TRUE;
        }
        else {
            return Boolean.FALSE;
        }
    }

}