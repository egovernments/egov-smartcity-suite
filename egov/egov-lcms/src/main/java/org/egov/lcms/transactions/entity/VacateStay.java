package org.egov.lcms.transactions.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.infra.persistence.validator.annotation.DateFormat;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.ValidateDate;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infstr.models.BaseModel;
import org.egov.lcms.utils.LcmsConstants;
import org.hibernate.validator.constraints.Length;

public class VacateStay extends BaseModel {
    /**
     * Serial version uid
     */
    private static final long serialVersionUID = 1L;

    private Lcinterimorder lcinterimOrder;
    @DateFormat(message = "invalid.fieldvalue.model.vsReceivedFromStandingCounsel")
    private Date vsReceivedFromStandingCounsel;
    @DateFormat(message = "invalid.fieldvalue.model.vsSendToStandingCounsel")
    private Date vsSendToStandingCounsel;
    @Required(message = "vcpetition.exists")
    @DateFormat(message = "invalid.fieldvalue.model.vsPetitionFiledOn")
    @ValidateDate(allowPast = true, dateFormat = LcmsConstants.DATE_FORMAT, message = "petitionfiledon.notAllow.futureDate")
    private Date vsPetitionFiledOn;
    @Length(max = 1024, message = "io.vcremarks.length")
    private String remarks;

    public Date getVsReceivedFromStandingCounsel() {
        return vsReceivedFromStandingCounsel;
    }

    public void setVsReceivedFromStandingCounsel(
            Date vsReceivedFromStandingCounsel) {
        this.vsReceivedFromStandingCounsel = vsReceivedFromStandingCounsel;
    }

    public Date getVsSendToStandingCounsel() {
        return vsSendToStandingCounsel;
    }

    public void setVsSendToStandingCounsel(Date vsSendToStandingCounsel) {
        this.vsSendToStandingCounsel = vsSendToStandingCounsel;
    }

    public Date getVsPetitionFiledOn() {
        return vsPetitionFiledOn;
    }

    public void setVsPetitionFiledOn(Date vsPetitionFiledOn) {
        this.vsPetitionFiledOn = vsPetitionFiledOn;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Lcinterimorder getLcinterimOrder() {
        return lcinterimOrder;
    }

    public void setLcinterimOrder(Lcinterimorder lcinterimOrder) {
        this.lcinterimOrder = lcinterimOrder;
    }

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<ValidationError>();

        if (!DateUtils.compareDates(getVsReceivedFromStandingCounsel(),
                getLcinterimOrder().getIodate()))
            errors.add(new ValidationError("iodate",
                    "iodate.greaterThan.vsReceivedFromStandingCounsel"));

        if (!DateUtils.compareDates(getVsPetitionFiledOn(), getLcinterimOrder()
                .getIodate()))
            errors.add(new ValidationError("iodate",
                    "iodate.greaterThan.petitionFiledOn"));

        if (!DateUtils.compareDates(getVsSendToStandingCounsel(),
                getLcinterimOrder().getIodate()))
            errors.add(new ValidationError("iodate",
                    "iodate.greaterThan.vsSendToStandingCounsel"));

        if (!DateUtils.compareDates(getVsReceivedFromStandingCounsel(),
                getVsSendToStandingCounsel()))
            errors
                    .add(new ValidationError("vsReceivedFromStandingCounsel",
                            "vsReceivedFromStandingCounsel.greaterThan.vsSendToStandingCounsel"));

        if (!DateUtils.compareDates(getVsSendToStandingCounsel(),
                getVsPetitionFiledOn()))
            errors.add(new ValidationError("petitionFiledOn",
                    "vsSendToStandingCounsel.greaterThan.petitionFiledOn"));

        return errors;
    }

}
