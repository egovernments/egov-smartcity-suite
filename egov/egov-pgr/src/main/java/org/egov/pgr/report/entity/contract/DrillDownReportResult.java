/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */

package org.egov.pgr.report.entity.contract;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class DrillDownReportResult {
    private String zoneName;
    private BigInteger registered = BigInteger.ZERO;
    private BigInteger inprocess = BigInteger.ZERO;
    private BigInteger Completed = BigInteger.ZERO;
    private BigInteger Rejected = BigInteger.ZERO;
    private BigInteger reopened = BigInteger.ZERO;
    private String complainttype;
    private String name;
    private String crn;
    private Date createddate;
    private String complaintname;
    private String details;
    private String status;
    private String boundaryname;
    private BigInteger complaintid;
    private BigDecimal complainttypeid;
    private BigInteger usrid;
    private BigInteger feedback;
    private BigInteger withinsla;
    private BigInteger beyondsla;
    private String issla;

    public BigDecimal getComplainttypeid() {
        return complainttypeid;
    }

    public void setComplainttypeid(final BigDecimal complainttypeid) {
        this.complainttypeid = complainttypeid;
    }

    public BigInteger getComplaintid() {
        return complaintid;
    }

    public void setComplaintid(final BigInteger complaintid) {
        this.complaintid = complaintid;
    }

    public String getCrn() {
        return crn;
    }

    public void setCrn(final String crn) {
        this.crn = crn;
    }

    public DateTime getCreateddate() {
        return null == this.createddate ? null : new DateTime(this.createddate);
    }

    public void setCreateddate(final Date createddate) {
        this.createddate = createddate;
    }

    public String getComplaintname() {
        return complaintname;
    }

    public void setComplaintname(final String complaintname) {
        this.complaintname = complaintname;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(final String details) {
        this.details = details;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getBoundaryname() {
        return boundaryname;
    }

    public void setBoundaryname(final String boundaryname) {
        this.boundaryname = boundaryname;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(final String zoneName) {
        this.zoneName = zoneName;
    }

    public BigInteger getRegistered() {
        return registered;
    }

    public void setRegistered(final BigInteger registered) {
        this.registered = registered;
    }

    public BigInteger getInprocess() {
        return inprocess;
    }

    public void setInprocess(final BigInteger inprocess) {
        this.inprocess = inprocess;
    }

    public BigInteger getCompleted() {
        return Completed;
    }

    public void setCompleted(final BigInteger completed) {
        Completed = completed;
    }

    public BigInteger getRejected() {
        return Rejected;
    }

    public void setRejected(final BigInteger rejected) {
        Rejected = rejected;
    }

    public String getComplainttype() {
        return complainttype;
    }

    public void setComplainttype(final String complainttype) {
        this.complainttype = complainttype;
    }

    public BigInteger getTotal() {
        return registered.add(inprocess).add(Completed).add(Rejected).add(reopened);
    }

    public void setTotal(final BigInteger total) {
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public BigInteger getReopened() {
        return reopened;
    }

    public void setReopened(final BigInteger reopened) {
        this.reopened = reopened;
    }

    public BigInteger getFeedback() {
        return feedback;
    }

    public void setFeedback(final BigInteger feedback) {
        this.feedback = feedback;
    }

    public BigInteger getWithinsla() {
        return withinsla;
    }

    public void setWithinsla(final BigInteger withinsla) {
        this.withinsla = withinsla;
    }

    public BigInteger getBeyondsla() {
        return beyondsla;
    }

    public void setBeyondsla(final BigInteger beyondsla) {
        this.beyondsla = beyondsla;
    }

    public String getIssla() {
        return issla;
    }

    public void setIssla(final String issla) {
        this.issla = issla;
    }

    public BigInteger getUsrid() {
        return usrid;
    }

    public void setUsrid(BigInteger usrid) {
        this.usrid = usrid;
    }

}
