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

package org.egov.works.milestone.entity;

import java.util.Date;

public class SearchRequestMilestone {

    private Long department;
    private Date milestoneFromDate;
    private Date milestoneToDate;
    private String workIdentificationNumber;
    private Long typeOfWork;
    private Long subTypeOfWork;
    private String workOrderNumber;
    private String status;
    private Date trackMilestoneFromDate;
    private Date trackMilestoneToDate;
    private String contractor;

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getWorkIdentificationNumber() {
        return workIdentificationNumber;
    }

    public void setWorkIdentificationNumber(final String workIdentificationNumber) {
        this.workIdentificationNumber = workIdentificationNumber;
    }

    public void setDepartment(final Long department) {
        this.department = department;
    }

    public Long getDepartment() {
        return department;
    }

    public String getWorkOrderNumber() {
        return workOrderNumber;
    }

    public void setWorkOrderNumber(final String workOrderNumber) {
        this.workOrderNumber = workOrderNumber;
    }

    public Date getMilestoneFromDate() {
        return milestoneFromDate;
    }

    public void setMilestoneFromDate(final Date milestoneFromDate) {
        this.milestoneFromDate = milestoneFromDate;
    }

    public Date getMilestoneToDate() {
        return milestoneToDate;
    }

    public void setMilestoneToDate(final Date milestoneToDate) {
        this.milestoneToDate = milestoneToDate;
    }

    public Long getTypeOfWork() {
        return typeOfWork;
    }

    public void setTypeOfWork(final Long typeOfWork) {
        this.typeOfWork = typeOfWork;
    }

    public Long getSubTypeOfWork() {
        return subTypeOfWork;
    }

    public void setSubTypeOfWork(final Long subTypeOfWork) {
        this.subTypeOfWork = subTypeOfWork;
    }

    public Date getTrackMilestoneFromDate() {
        return trackMilestoneFromDate;
    }

    public void setTrackMilestoneFromDate(final Date trackMilestoneFromDate) {
        this.trackMilestoneFromDate = trackMilestoneFromDate;
    }

    public Date getTrackMilestoneToDate() {
        return trackMilestoneToDate;
    }

    public void setTrackMilestoneToDate(final Date trackMilestoneToDate) {
        this.trackMilestoneToDate = trackMilestoneToDate;
    }

    public String getContractor() {
        return contractor;
    }

    public void setContractor(final String contractor) {
        this.contractor = contractor;
    }
}
