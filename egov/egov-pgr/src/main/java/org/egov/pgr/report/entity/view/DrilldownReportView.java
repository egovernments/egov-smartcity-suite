/*
 * eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) <2017>  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 * 	Further, all user interfaces, including but not limited to citizen facing interfaces,
 *         Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *         derived works should carry eGovernments Foundation logo on the top right corner.
 *
 * 	For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 * 	For any further queries on attribution, including queries on brand guidelines,
 *         please contact contact@egovernments.org
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.pgr.report.entity.view;


import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

@Entity
@Immutable
@Table(name = "egpgr_mv_drilldown_report_view")
public class DrilldownReportView implements Serializable {

    private static final long serialVersionUID = 4573115438039131111L;
    @Id
    private String crn;

    private Long employeeId;

    private String employeeName;

    private Date employeeStartDate;

    private Long complainantId;

    private String complainantName;

    private Long complaintTypeId;

    private String complaintTypeName;

    private Date createdDate;

    private String complaintDetail;

    private String status;

    private String boundaryName;

    private String parentBoundary;

    private String department;

    private String employeePosition;

    private String locality;

    private Integer feedback;

    private Long registered;

    private Long inprocess;

    private Long completed;

    private Long reopened;

    private Long rejected;

    private Long withinSLA;

    private Long beyondSLA;

    private String isSLA;

    public DrilldownReportView(Long employeeId, String employeeName, Long registered, Long inprocess, Long completed
            , Long reopened, Long rejected, Long withinSLA, Long beyondSLA) {

        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.registered = registered;
        this.inprocess = inprocess;
        this.completed = completed;
        this.reopened = reopened;
        this.rejected = rejected;
        this.withinSLA = withinSLA;
        this.beyondSLA = beyondSLA;
    }

    public DrilldownReportView(String name, Long registered, Long inprocess, Long completed
            , Long rejected, Long reopened, Long withinSLA, Long beyondSLA) {

        this.complaintTypeName = name;
        this.registered = registered;
        this.inprocess = inprocess;
        this.completed = completed;
        this.rejected = rejected;
        this.reopened = reopened;
        this.withinSLA = withinSLA;
        this.beyondSLA = beyondSLA;
    }

    public DrilldownReportView(String complaintTypeName, Long complaintTypeId, Long registered, Long inprocess, Long completed
            , Long rejected, Long reopened, Long withinSLA, Long beyondSLA) {

        this.complaintTypeId = complaintTypeId;
        this.complaintTypeName = complaintTypeName;
        this.registered = registered;
        this.inprocess = inprocess;
        this.completed = completed;
        this.rejected = rejected;
        this.reopened = reopened;
        this.withinSLA = withinSLA;
        this.beyondSLA = beyondSLA;
    }

    public DrilldownReportView(Long complainantId, String crn, Date createdDate, String complainantName, String complaintDetail
            , String status, String boundaryName, Integer feedback, String isSLA) {

        this.complainantId = complainantId;
        this.crn = crn;
        this.createdDate = createdDate;
        this.complainantName = complainantName;
        this.complaintDetail = complaintDetail;
        this.status = status;
        this.boundaryName = boundaryName;
        this.feedback = feedback;
        this.isSLA = isSLA;

    }


    public DrilldownReportView(Long count) {

    }

    public DrilldownReportView() {
    }

    public String getCrn() {
        return crn;
    }

    public void setCrn(String crn) {
        this.crn = crn;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Date getEmployeeStartDate() {
        return employeeStartDate;
    }

    public void setEmployeeStartDate(Date employeeStartDate) {
        this.employeeStartDate = employeeStartDate;
    }

    public Long getComplainantId() {
        return complainantId;
    }

    public void setComplainantId(Long complainantId) {
        this.complainantId = complainantId;
    }

    public String getComplainantName() {
        return complainantName;
    }

    public void setComplainantName(String complainantName) {
        this.complainantName = complainantName;
    }

    public Long getComplaintTypeId() {
        return complaintTypeId;
    }

    public void setComplaintTypeId(Long complaintTypeId) {
        this.complaintTypeId = complaintTypeId;
    }

    public String getComplaintTypeName() {
        return complaintTypeName;
    }

    public void setComplaintTypeName(String complaintTypeName) {
        this.complaintTypeName = complaintTypeName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getComplaintDetail() {
        return complaintDetail;
    }

    public void setComplaintDetail(String complaintDetail) {
        this.complaintDetail = complaintDetail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBoundaryName() {
        return boundaryName;
    }

    public void setBoundaryName(String boundaryName) {
        this.boundaryName = boundaryName;
    }


    public String getParentBoundary() {
        return parentBoundary;
    }

    public void setParentBoundary(String parentBoundary) {
        this.parentBoundary = parentBoundary;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmployeePosition() {
        return employeePosition;
    }

    public void setEmployeePosition(String employeePosition) {
        this.employeePosition = employeePosition;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public Integer getFeedback() {
        return feedback;
    }

    public void setFeedback(Integer feedback) {
        this.feedback = feedback;
    }

    public Long getRegistered() {
        return registered;
    }

    public void setRegistered(Long registered) {
        this.registered = registered;
    }

    public Long getInprocess() {
        return inprocess;
    }

    public void setInprocess(Long inprocess) {
        this.inprocess = inprocess;
    }

    public Long getCompleted() {
        return completed;
    }

    public void setCompleted(Long completed) {
        this.completed = completed;
    }

    public Long getReopened() {
        return reopened;
    }

    public void setReopened(Long reopened) {
        this.reopened = reopened;
    }

    public Long getRejected() {
        return rejected;
    }

    public void setRejected(Long rejected) {
        this.rejected = rejected;
    }

    public Long getWithinSLA() {
        return withinSLA;
    }

    public void setWithinSLA(Long withinSLA) {
        this.withinSLA = withinSLA;
    }

    public Long getBeyondSLA() {
        return beyondSLA;
    }

    public void setBeyondSLA(Long beyondSLA) {
        this.beyondSLA = beyondSLA;
    }

    public String getIsSLA() {
        return isSLA;
    }

    public void setIsSLA(String isSLA) {
        this.isSLA = isSLA;
    }

    public BigInteger getTotal() {
        return BigInteger.valueOf(registered).add(BigInteger.valueOf(inprocess)).add(BigInteger.valueOf(completed)).
                add(BigInteger.valueOf(rejected)).add(BigInteger.valueOf(reopened));
    }
}
