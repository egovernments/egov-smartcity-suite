package org.egov.lcms.transactions.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.persistence.validator.annotation.DateFormat;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.utils.DateUtils;
import org.egov.infra.validation.exception.ValidationError;
import org.egov.pims.commons.Position;

public class LegalcaseDepartment {

    private Long id;
    @DateFormat(message = "invalid.fieldvalue.dateOfReceipt")
    private Date receiptOfPwr;
    private Legalcase legalcase;
    @Required(message = "case.dept.null")
    private Department department;
    @Required(message = "case.position.null")
    private Position position;
    private String positionAndEmpName;
    private boolean isPrimaryDepartment;
    private List<Reminder> legalcaseReminders = new ArrayList<Reminder>();
    @DateFormat(message = "invalid.fieldvalue.assignOnDate")
    private Date assignOn;

    public boolean getIsPrimaryDepartment() {
        return isPrimaryDepartment;
    }

    public void setIsPrimaryDepartment(boolean isPrimaryDepartment) {
        this.isPrimaryDepartment = isPrimaryDepartment;
    }

    public Date getReceiptOfPwr() {
        return receiptOfPwr;
    }

    public void setReceiptOfPwr(Date receiptOfPwr) {
        this.receiptOfPwr = receiptOfPwr;
    }

    public Legalcase getLegalcase() {
        return legalcase;
    }

    public void setLegalcase(Legalcase legalcase) {
        this.legalcase = legalcase;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<ValidationError>();
        if (legalcase != null
                && !DateUtils.compareDates(getReceiptOfPwr(), legalcase
                        .getCasedate())) {
            errors.add(new ValidationError("dateOfReceipt",
                    "dateOfReceipt.less.casedate"));
        }
        if (legalcase != null
                && !DateUtils.compareDates(getAssignOn(), legalcase.getCasedate()))
        {
            errors.add(new ValidationError("assignOnDate",
                    "assignOn.less.casedate"));
        }

        return errors;
    }

    public String getPositionAndEmpName() {
        return positionAndEmpName;
    }

    public void setPositionAndEmpName(String positionAndEmpName) {
        this.positionAndEmpName = positionAndEmpName;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public List<Reminder> getLegalcaseReminders() {
        return legalcaseReminders;
    }

    public void setLegalcaseReminders(List<Reminder> legalcaseReminders) {
        this.legalcaseReminders = legalcaseReminders;
    }

    public void addReminder(Reminder reminder) {
        getLegalcaseReminders().add(reminder);
    }

    public void removeReminder(Reminder reminder) {
        getLegalcaseReminders().remove(reminder);
    }

    public Date getAssignOn() {
        return assignOn;
    }

    public void setAssignOn(Date assignOn) {
        this.assignOn = assignOn;
    }

}
