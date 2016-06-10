package org.egov.lcms.transactions.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.infra.persistence.validator.annotation.OptionalPattern;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.ValidateDate;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.lcms.utils.LcmsConstants;
import org.hibernate.validator.constraints.Length;

/**
 * Contempt entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Contempt {

    private Long id;
    private Judgmentimpl judgmentimpl;
    @Required(message = "canumber.null")
    @Length(max = 50, message = "canumber.length")
    @OptionalPattern(regex = LcmsConstants.alphaNumeric, message = "canumber.alpha")
    private String canumber;
    @Required(message = "receivingdate.null")
    @ValidateDate(allowPast = true, dateFormat = LcmsConstants.DATE_FORMAT, message = "invalid.contempt.date")
    private Date receivingdate;
    private boolean iscommapprRequired = false;
    private Date commappDate;

    public Judgmentimpl getJudgmentimpl() {
        return judgmentimpl;
    }

    public void setJudgmentimpl(Judgmentimpl judgmentimpl) {
        this.judgmentimpl = judgmentimpl;
    }

    public String getCanumber() {
        return this.canumber;
    }

    public void setCanumber(String canumber) {
        this.canumber = canumber;
    }

    public Date getReceivingdate() {
        return this.receivingdate;
    }

    public void setReceivingdate(Date receivingdate) {
        this.receivingdate = receivingdate;
    }

    public boolean getIscommapprRequired() {
        return this.iscommapprRequired;
    }

    public void setIscommapprRequired(boolean iscommapprRequired) {
        this.iscommapprRequired = iscommapprRequired;
    }

    public Date getCommappDate() {
        return this.commappDate;
    }

    public void setCommappDate(Date commappDate) {
        this.commappDate = commappDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<ValidationError>();
        if (getReceivingdate() != null) {
            if (!DateUtils.compareDates(getReceivingdate(), getJudgmentimpl()
                    .getEglcJudgment().getOrderDate()))
                errors.add(new ValidationError("receivingDate",
                        "receivingDate.less.orderDate"));
            if (!DateUtils.compareDates(getCommappDate(), getReceivingdate()))
                errors.add(new ValidationError("receivingDate",
                        "commappDate.greaterThan.receivingDate"));
        }
        return errors;
    }

}