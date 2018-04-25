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

package org.egov.api.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.egov.infra.utils.DateUtils.formatter;

public class ComplaintSearchRequest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ComplaintSearchRequest.class);
    private static final String INPUT_DATE_FORMAT = "yyyy-MM-dd";
    private static final String OUTPUT_DATE_FORMAT = "dd/MM/yyyy";
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
        if (isNotBlank(fromDate))
            try {
                this.fromDate = formatter(INPUT_DATE_FORMAT).print(formatter(OUTPUT_DATE_FORMAT).parseDateTime(fromDate));

            } catch (final Exception e) {
                LOGGER.warn("Invalid Date Format", e);
            }
    }

    public void setToDate(final String toDate) {
        if (null != toDate)
            try {
                this.toDate = formatter(INPUT_DATE_FORMAT).print(formatter(OUTPUT_DATE_FORMAT).parseDateTime(toDate));
            } catch (final Exception e) {
                LOGGER.warn("Invalid Date Format", e);
            }
    }

    public void setComplaintDepartment(final String complaintDepartment) {
        this.complaintDepartment = complaintDepartment;
    }

    public void setComplaintDate(final String complaintDate) {
        if (null != complaintDate) {
            final Calendar cal = Calendar.getInstance();
            complaintDateTo = formatter(INPUT_DATE_FORMAT).print(cal.getTimeInMillis());

            if (complaintDate.equalsIgnoreCase("today")) {
                complaintDateFrom = complaintDateTo;
            } else if (complaintDate.equalsIgnoreCase("lastsevendays")) {
                cal.add(Calendar.DATE, -6);
                complaintDateFrom = formatter(INPUT_DATE_FORMAT).print(cal.getTimeInMillis());
            } else if (complaintDate.equalsIgnoreCase("lastthirtydays")) {
                cal.add(Calendar.DATE, -29);
                complaintDateFrom = formatter(INPUT_DATE_FORMAT).print(cal.getTimeInMillis());
            } else if (complaintDate.equalsIgnoreCase("lastninetydays")) {
                cal.add(Calendar.DATE, -89);
                complaintDateFrom = formatter(INPUT_DATE_FORMAT).print(cal.getTimeInMillis());
            } else {
                complaintDateFrom = null;
                complaintDateTo = null;
            }
        }

    }

    public String searchQuery() {
        return searchText;
    }

}
