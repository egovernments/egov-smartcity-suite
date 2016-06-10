package org.egov.lcms.transactions.entity;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.egov.infra.persistence.validator.annotation.OptionalPattern;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.lcms.masters.entity.GovernmentDept;
import org.egov.lcms.utils.LcmsConstants;
import org.hibernate.validator.constraints.Length;

/**
 * LegalcasePetitioner entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BipartisanDetails {

    private Long id;
    private Legalcase eglcLegalcase;
    @Length(max = 128, message = "petitionerName.length")
    @OptionalPattern(regex = LcmsConstants.mixedCharType1, message = "petitionerName.name.mixedChar")
    private String name;
    @Length(max = 256, message = "address.length")
    private String address;
    @OptionalPattern(regex = LcmsConstants.numericiValForPhoneNo, message = "contactNumber.numeric")
    private Long contactNumber;
    private boolean isrepondent;
    private GovernmentDept governmentDept;
    private boolean isrespondentgovernment;
    private Long serialNumber;

    public GovernmentDept getGovernmentDept() {
        return this.governmentDept;
    }

    public void setGovernmentDept(GovernmentDept governmentDept) {
        this.governmentDept = governmentDept;
    }

    public boolean getIsrespondentgovernment() {
        return isrespondentgovernment;
    }

    public void setIsrespondentgovernment(boolean isrespondentgovernment) {
        this.isrespondentgovernment = isrespondentgovernment;
    }

    public boolean getIsrepondent() {
        return isrepondent;
    }

    public void setIsrepondent(boolean isrepondent) {
        this.isrepondent = isrepondent;
    }

    public Legalcase getEglcLegalcase() {
        return this.eglcLegalcase;
    }

    public void setEglcLegalcase(Legalcase eglcLegalcase) {
        this.eglcLegalcase = eglcLegalcase;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getContactNumber() {
        return this.contactNumber;
    }

    public void setContactNumber(Long contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<ValidationError>();
        if (getIsrespondentgovernment() && getGovernmentDept() == null) {
            if (getIsrepondent())
                errors.add(new ValidationError("govtDept",
                        "respondent.govtDept.select"));
            else
                errors.add(new ValidationError("govtDept",
                        "petitioner.govtDept.select"));
        }
        if (!getIsrespondentgovernment()) {
            if (getIsrepondent() && StringUtils.isBlank(getName()))
                errors.add(new ValidationError("respondent",
                        "respondentName.null"));
            if (!getIsrepondent() && StringUtils.isBlank(getName()))
                errors.add(new ValidationError("petitioner",
                        "petitionerName.null"));
        }
        return errors;
    }

    public Long getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Long serialNumber) {
        this.serialNumber = serialNumber;
    }

}