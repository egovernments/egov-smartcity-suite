package org.egov.pgr.web.controller.reports;

import java.util.Date;

public class ReportHelper {
private Date reportFromDate;
private Date reportToDate;
private String complaintStatus;
private String groupBy;
private String complaintMode;
private String complaintDateType;


public String getComplaintDateType() {
    return complaintDateType;
}
public void setComplaintDateType(String complaintDateType) {
    this.complaintDateType = complaintDateType;
}
public String getGroupBy() {
    return groupBy;
}
public void setGroupBy(String groupBy) {
    this.groupBy = groupBy;
}
public Date getReportFromDate() {
    return reportFromDate;
}
public void setReportFromDate(Date reportFromDate) {
    this.reportFromDate = reportFromDate;
}
public Date getReportToDate() {
    return reportToDate;
}
public void setReportToDate(Date reportToDate) {
    this.reportToDate = reportToDate;
}
public String getComplaintStatus() {
    return complaintStatus;
}
public void setComplaintStatus(String complaintStatus) {
    this.complaintStatus = complaintStatus;
}
public String getComplaintMode() {
    return complaintMode;
}
public void setComplaintMode(String complaintMode) {
    this.complaintMode = complaintMode;
}


}
