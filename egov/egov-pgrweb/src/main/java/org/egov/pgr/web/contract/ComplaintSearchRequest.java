/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */

package org.egov.pgr.web.contract;

import org.joda.time.DateTime;

import static org.egov.infra.utils.DateUtils.TO_DEFAULT_DATE_FORMAT;
import static org.egov.infra.utils.DateUtils.endOfGivenDate;
import static org.egov.infra.utils.DateUtils.startOfGivenDate;

public class ComplaintSearchRequest {
    public static final String SEARCH_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm";

    private String searchText;
    private String complaintNumber;
    private String complainantName;
    private String complaintStatus;
    private String complainantPhoneNumber;
    private String complainantEmail;
    private String receivingCenter;
    private String complaintType;
    private String complaintDateFrom;
    private String complaintDateTo;
    private String fromDate;
    private String toDate;
    private String complaintDepartment;
    private String location;
    private String currentUlb;

    public void setSearchText(final String searchText) {
        this.searchText = searchText;
    }

    public void setComplaintNumber(final String complaintNumber) {
        this.complaintNumber = complaintNumber;
    }

    public void setCurrentUlb(final String currentUlb) {
        this.currentUlb = currentUlb;
    }

    public void setComplaintStatus(final String complaintStatus) {
        this.complaintStatus = complaintStatus;
    }

    public void setComplainantName(final String complainantName) {
        this.complainantName = complainantName;
    }

    public void setLocation(final String location) {
        this.location = location;
    }

    public void setComplainantPhoneNumber(final String phoneNumber) {
        complainantPhoneNumber = phoneNumber;
    }

    public void setComplainantEmail(final String email) {
        complainantEmail = email;
    }

    public void setReceivingCenter(final String receivingCenter) {
        this.receivingCenter = receivingCenter;
    }

    public void setComplaintType(final String complaintType) {
        this.complaintType = complaintType;
    }

    public void setFromDate(final String fromDate) {
        if (fromDate != null)
            this.fromDate = startOfGivenDate(TO_DEFAULT_DATE_FORMAT.parseDateTime(fromDate)).toString(SEARCH_DATE_FORMAT);
    }

    public void setToDate(final String toDate) {
        if (toDate != null)
            this.toDate = endOfGivenDate(TO_DEFAULT_DATE_FORMAT.parseDateTime(toDate)).toString(SEARCH_DATE_FORMAT);
    }

    public void setComplaintDepartment(final String complaintDepartment) {
        this.complaintDepartment = complaintDepartment;
    }

    public void setComplaintDate(final String complaintDate) {
        if (null != complaintDate) {
            DateTime currentDate = new DateTime();
            complaintDateTo = endOfGivenDate(currentDate).toString(SEARCH_DATE_FORMAT);
            if (complaintDate.equalsIgnoreCase("today")) {
                complaintDateFrom = currentDate.withTimeAtStartOfDay().toString(SEARCH_DATE_FORMAT);
            } else if (complaintDate.equalsIgnoreCase("all")) {
                complaintDateFrom = null;
                complaintDateTo = null;
            } else if (complaintDate.equalsIgnoreCase("lastsevendays")) {
                complaintDateFrom = currentDate.minusDays(7).toString(SEARCH_DATE_FORMAT);
            } else if (complaintDate.equalsIgnoreCase("lastthirtydays")) {
                complaintDateFrom = currentDate.minusDays(30).toString(SEARCH_DATE_FORMAT);
            } else if (complaintDate.equalsIgnoreCase("lastninetydays")) {
                complaintDateFrom = currentDate.minusDays(90).toString(SEARCH_DATE_FORMAT);
            } else {
                complaintDateFrom = null;
                complaintDateTo = null;
            }
        }

    }

}
