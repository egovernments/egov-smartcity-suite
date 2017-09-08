package org.egov.eis.contract;

import java.util.Date;

public class EmployeeResponse {


    private String code;

    private Date fromDate;

    private Date toDate;

    private String positionName;

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(final Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(final Date toDate) {
        this.toDate = toDate;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(final String positionName) {
        this.positionName = positionName;
    }

}
