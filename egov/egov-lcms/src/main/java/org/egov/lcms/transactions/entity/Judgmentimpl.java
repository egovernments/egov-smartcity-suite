package org.egov.lcms.transactions.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.egov.infra.persistence.validator.annotation.DateFormat;
import org.egov.infra.persistence.validator.annotation.ValidateDate;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infstr.models.BaseModel;
import org.egov.lcms.utils.LcmsConstants;
import org.hibernate.validator.constraints.Length;

/**
 * Judgmentimpl entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Judgmentimpl extends BaseModel {
    /**
     * Serial version uid
     */
    private static final long serialVersionUID = 1L;

    // Fields
    private Judgment eglcJudgment;
    private Long isCompiled;
    @DateFormat(message = "invalid.fieldvalue.model.dateofcompliance")
    @ValidateDate(allowPast = true, dateFormat = LcmsConstants.DATE_FORMAT, message = "invalid.compliance.date")
    private Date dateofcompliance;
    @Length(max = 1024, message = "compliancereport.maxlength")
    private String compliancereport;
    private String reason;
    @Length(max = 128, message = "details.maxlength")
    private String details;
    private Set<Contempt> contempt = new HashSet<Contempt>();
    private Set<Appeal> appeal = new HashSet<Appeal>();

    public void addContempt(Contempt contempt) {
        getContempt().add(contempt);
    }

    public void addAppeal(Appeal appeal) {
        getAppeal().add(appeal);
    }

    @Valid
    public Set<Contempt> getContempt() {
        return contempt;
    }

    public void setContempt(Set<Contempt> contempt) {
        this.contempt = contempt;
    }

    @Valid
    public Set<Appeal> getAppeal() {
        return appeal;
    }

    public void setAppeal(Set<Appeal> appeal) {
        this.appeal = appeal;
    }

    public Judgment getEglcJudgment() {
        return this.eglcJudgment;
    }

    public void setEglcJudgment(Judgment eglcJudgment) {
        this.eglcJudgment = eglcJudgment;
    }

    public Long getIsCompiled() {
        return this.isCompiled;
    }

    public void setIsCompiled(Long isCompiled) {
        this.isCompiled = isCompiled;
    }

    public Date getDateofcompliance() {
        return this.dateofcompliance;
    }

    public void setDateofcompliance(Date dateofcompliance) {
        this.dateofcompliance = dateofcompliance;
    }

    public String getCompliancereport() {
        return this.compliancereport;
    }

    public void setCompliancereport(String compliancereport) {
        this.compliancereport = compliancereport;
    }

    /**
     * @return the reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * @param reason the reason to set
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<ValidationError>();
        if (getDateofcompliance() != null
                && !DateUtils.compareDates(getDateofcompliance(), eglcJudgment
                        .getOrderDate())) {
            errors.add(new ValidationError("dateofcompliance",
                    "dateofcompliance.less.orderDate"));
        }
        for (Contempt contempt : getContempt())
            errors.addAll(contempt.validate());
        for (Appeal appeal : getAppeal())
            errors.addAll(appeal.validate());
        return errors;
    }

    /*
     * public String getImplementationdetails() { return implementationdetails; } public void setImplementationdetails(String
     * implementationdetails) { this.implementationdetails = implementationdetails; }
     */

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

}