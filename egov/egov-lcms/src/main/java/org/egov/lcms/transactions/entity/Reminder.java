package org.egov.lcms.transactions.entity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.infra.validation.exception.ValidationError;
import org.egov.infstr.models.BaseModel;

public class Reminder extends BaseModel {

    private static final long serialVersionUID = 6066719749607410784L;
    private LegalcaseDepartment legalCaseDepartment;
    private String remarks;
    private Date date;

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public LegalcaseDepartment getLegalCaseDepartment() {
        return legalCaseDepartment;
    }

    public void setLegalCaseDepartment(LegalcaseDepartment legalCaseDepartment) {
        this.legalCaseDepartment = legalCaseDepartment;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFormattedDate() {
        return sdf.format(this.date);
    }

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<ValidationError>();
        if (legalCaseDepartment != null && legalCaseDepartment.getReceiptOfPwr() != null
                && (!getDate().before(legalCaseDepartment.getReceiptOfPwr()))) {
            errors.add(new ValidationError("date",
                    "date.less.receiptOfPwr", getFormattedDate(), sdf.format(legalCaseDepartment.getReceiptOfPwr())));
        }
        else if (legalCaseDepartment != null && legalCaseDepartment.getLegalcase().getCasedate() != null
                && (!getDate().after(legalCaseDepartment.getLegalcase().getCasedate()))) {
            errors.add(new ValidationError("date",
                    "date.greater.casedate", getFormattedDate(), sdf.format(legalCaseDepartment.getLegalcase().getCasedate())));
        }

        return errors;
    }

}
