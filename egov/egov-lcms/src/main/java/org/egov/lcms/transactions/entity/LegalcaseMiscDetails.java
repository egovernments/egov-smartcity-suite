package org.egov.lcms.transactions.entity;

import java.util.Date;

import org.egov.commons.EgwStatus;
import org.egov.infra.persistence.validator.annotation.DateFormat;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infstr.models.BaseModel;
import org.hibernate.validator.constraints.Length;

public class LegalcaseMiscDetails extends BaseModel {
    /**
     * Serial version uid
     */
    private static final long serialVersionUID = 1L;

    @Required(message = "miscDetails.date.null")
    @DateFormat(message = "invalid.fieldvalue.model.miscDate")
    private Date miscDate;
    @Length(max = 50, message = "miscDetails.referencenumber.maxlength")
    private String referenceNumber;
    @Length(max = 1024, message = "miscDetails.remarks.maxlength")
    private String remarks;

    private Legalcase legalcase;
    private EgwStatus egwStatus;

    public Legalcase getLegalcase() {
        return legalcase;
    }

    public void setLegalcase(Legalcase legalcase) {
        this.legalcase = legalcase;
    }

    public Date getMiscDate() {
        return miscDate;
    }

    public void setMiscDate(Date miscDate) {
        this.miscDate = miscDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

}
