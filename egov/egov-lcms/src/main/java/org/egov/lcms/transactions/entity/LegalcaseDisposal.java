package org.egov.lcms.transactions.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.infra.persistence.validator.annotation.CompareDates;
import org.egov.infra.persistence.validator.annotation.DateFormat;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.ValidateDate;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.infstr.models.BaseModel;
import org.egov.lcms.utils.LcmsConstants;
import org.hibernate.validator.constraints.Length;

@CompareDates(fromDate = "consignmentDate", toDate = LcmsConstants.DISPOSAL_DATE, dateFormat = "dd/MM/yyyy", message = "consignmentDate.greaterThan.disposalDate")
public class LegalcaseDisposal extends BaseModel {
    /**
     * Serial version uid
     */
    private static final long serialVersionUID = 1L;

    @Required(message = "disposalDate.null")
    @DateFormat(message = "invalid.fieldvalue.model.disposalDate")
    @ValidateDate(allowPast = true, dateFormat = LcmsConstants.DATE_FORMAT, message = "disposalDate.lessthan.currentDate")
    private Date disposalDate;
    @Length(max = 1024, message = "io.disposalDetails.length")
    private String disposalDetails;
    @DateFormat(message = "invalid.fieldvalue.model.consignmentDate")
    private Date consignmentDate;
    private Legalcase legalcase;

    public Date getDisposalDate() {
        return disposalDate;
    }

    public void setDisposalDate(Date disposalDate) {
        this.disposalDate = disposalDate;
    }

    public String getDisposalDetails() {
        return disposalDetails;
    }

    public void setDisposalDetails(String disposalDetails) {
        this.disposalDetails = disposalDetails;
    }

    public Date getConsignmentDate() {
        return consignmentDate;
    }

    public void setConsignmentDate(Date consignmentDate) {
        this.consignmentDate = consignmentDate;
    }

    public Legalcase getLegalcase() {
        return legalcase;
    }

    public void setLegalcase(Legalcase legalcase) {
        this.legalcase = legalcase;
    }

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<ValidationError>();

        if (!DateUtils.compareDates(getDisposalDate(), getLegalcase()
                .getCasedate())) {
            errors.add(new ValidationError(LcmsConstants.DISPOSAL_DATE,
                    "disposalDate.greaterthan.caseDate"));
        }

        for (Hearings hearingsObj : legalcase.getHearings()) {
            int i = 0;
            if (!DateUtils.compareDates(getDisposalDate(), hearingsObj
                    .getHearingDate())) {
                errors.add(new ValidationError(LcmsConstants.DISPOSAL_DATE,
                        "disposalDate.greaterthan.hearingDate"));
                i++;
            }
            if (i > 0)
                break;
        }

        for (Judgment judgmentObj : getLegalcase().getEglcJudgments()) {
            if (!DateUtils.compareDates(getDisposalDate(), judgmentObj
                    .getOrderDate())) {
                errors.add(new ValidationError(LcmsConstants.DISPOSAL_DATE,
                        "disposalDate.greaterthan.judgementDate"));
            }
            for (Judgmentimpl judgementImpl : judgmentObj
                    .getEglcJudgmentimpls()) {
                if (!DateUtils.compareDates(getDisposalDate(), judgementImpl
                        .getDateofcompliance())) {
                    errors.add(new ValidationError(LcmsConstants.DISPOSAL_DATE,
                            "disposalDate.greaterthan.judgementImplDate"));
                }
            }
        }

        return errors;
    }
}
