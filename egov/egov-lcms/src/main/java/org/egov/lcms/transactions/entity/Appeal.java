package org.egov.lcms.transactions.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.commons.EgwStatus;
import org.egov.infra.persistence.validator.annotation.OptionalPattern;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.ValidateDate;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.lcms.utils.LcmsConstants;
import org.hibernate.validator.constraints.Length;

/**
 * Appeal entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class Appeal {

    private Long id;
    private Judgmentimpl judgmentimpl;
    private EgwStatus egwStatus;
    @Required(message = "srnumber.null")
    @Length(max = 50, message = "srnumber.length")
    @OptionalPattern(regex = LcmsConstants.alphaNumeric, message = "srnumber.alpha")
    private String srnumber;
    @Required(message = "appealfiledon.null")
    @ValidateDate(allowPast = true, dateFormat = LcmsConstants.DATE_FORMAT, message = "invalid.appeal.date")
    private Date appealfiledon;
    @Required(message = "appealfiledby.null")
    @Length(max = 100, message = "appealfiledby.length")
    @OptionalPattern(regex = LcmsConstants.alphaNumeric, message = "appealfiledby.alpha")
    private String appealfiledby;

    public EgwStatus getEgwStatus() {
        return this.egwStatus;
    }

    public void setEgwStatus(EgwStatus egwStatus) {
        this.egwStatus = egwStatus;
    }

    public String getSrnumber() {
        return this.srnumber;
    }

    public void setSrnumber(String srnumber) {
        this.srnumber = srnumber;
    }

    public Date getAppealfiledon() {
        return this.appealfiledon;
    }

    public void setAppealfiledon(Date appealfiledon) {
        this.appealfiledon = appealfiledon;
    }

    public String getAppealfiledby() {
        return this.appealfiledby;
    }

    public void setAppealfiledby(String appealfiledby) {
        this.appealfiledby = appealfiledby;
    }

    public Judgmentimpl getJudgmentimpl() {
        return judgmentimpl;
    }

    public void setJudgmentimpl(Judgmentimpl judgmentimpl) {
        this.judgmentimpl = judgmentimpl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<ValidationError>();
        if (getAppealfiledon() != null
                && !DateUtils.compareDates(getAppealfiledon(),
                        getJudgmentimpl().getEglcJudgment().getOrderDate()))
            errors.add(new ValidationError("appealfiledon",
                    "appealfiledon.less.orderDate"));
        return errors;
    }
}