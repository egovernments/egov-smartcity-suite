/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It
	   is required that all modified versions of this material be marked in
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program
	   with regards to rights under trademark law for use of the trade names
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.api.model;

import static org.egov.search.domain.Filter.queryStringFilter;
import static org.egov.search.domain.Filter.rangeFilter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.egov.search.domain.Filter;
import org.egov.search.domain.Filters;
import org.jboss.logging.Logger;

/**
 * 
 * @author nihalsheik
 *
 *  Note : this model alrady available in pgrweb, 
 *  Suggestion : we can move it to pgr module so that we can share from both
 *               api and pgrweb modules
 */
public class ComplaintSearchRequest {
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
	SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat dtft = new SimpleDateFormat("dd/MM/yyyy");

	private static final Logger logger = Logger.getLogger(ComplaintSearchRequest.class);

	public void setSearchText(final String searchText) {
		this.searchText = searchText;
	}

	public void setComplaintNumber(final String complaintNumber) {
		this.complaintNumber = complaintNumber;
	}

	public void setComplaintStatus(final String complaintStatus) {
		this.complaintStatus = complaintStatus;
	}

	public void setComplainantName(final String complainantName) {
		this.complainantName = complainantName;
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
		if (null != fromDate)
			try {
				if (logger.isDebugEnabled())
					logger.debug("Date Range From start.. :" + ft.format(dtft.parse(fromDate)));
				this.fromDate = ft.format(dtft.parse(fromDate));
			} catch (final ParseException e) {
				e.printStackTrace();
			}
	}

	public void setToDate(final String toDate) {
		if (null != toDate)
			try {
				if (logger.isDebugEnabled())
					logger.debug("Date Range Till .. :" + ft.format(dtft.parse(toDate)));
				this.toDate = ft.format(dtft.parse(toDate));
			} catch (final ParseException e) {
				e.printStackTrace();
			}
	}

	public void setComplaintDepartment(final String complaintDepartment) {
		this.complaintDepartment = complaintDepartment;
	}

	public void setComplaintDate(final String complaintDate) {
		if (null != complaintDate) {
			final Date today = new Date();
			final Calendar cal = Calendar.getInstance();
			if (logger.isDebugEnabled())
				logger.debug("String today date... " + ft.format(today));
			complaintDateTo = ft.format(today);

			if (complaintDate.equalsIgnoreCase("today")) {
				if (logger.isDebugEnabled())
					logger.debug("This is today selection");
				complaintDateFrom = complaintDateTo;
			} else if (complaintDate.equalsIgnoreCase("all")) {
				complaintDateFrom = null;
				complaintDateTo = null;
			} else if (complaintDate.equalsIgnoreCase("lastsevendays")) {
				cal.add(Calendar.DATE, -6);
				complaintDateFrom = ft.format(cal.getTime());
			} else if (complaintDate.equalsIgnoreCase("lastthirtydays")) {
				cal.add(Calendar.DATE, -29);
				complaintDateFrom = ft.format(cal.getTime());
			} else if (complaintDate.equalsIgnoreCase("lastninetydays")) {
				cal.add(Calendar.DATE, -89);
				complaintDateFrom = ft.format(cal.getTime());
			} else {
				if (logger.isDebugEnabled())
					logger.debug("Else section in date range");
				complaintDateFrom = null;
				complaintDateTo = null;
			}
		}

	}

	public Filters searchFilters() {
		final List<Filter> andFilters = new ArrayList<>();
		andFilters.add(queryStringFilter("searchable.crn", complaintNumber));
		andFilters.add(queryStringFilter("common.citizen.name", complainantName));
		andFilters.add(queryStringFilter("common.citizen.mobile", complainantPhoneNumber));
		andFilters.add(queryStringFilter("common.citizen.email", complainantEmail));
		andFilters.add(queryStringFilter("clauses.status.name", complaintStatus));
		andFilters.add(queryStringFilter("clauses.receivingMode", receivingCenter));
		andFilters.add(queryStringFilter("searchable.complaintType.name", complaintType));
		andFilters.add(rangeFilter("common.createdDate", complaintDateFrom, complaintDateTo));
		andFilters.add(rangeFilter("common.createdDate", fromDate, toDate));
		andFilters.add(queryStringFilter("searchable.complaintType.department.name", complaintDepartment));
		if (logger.isDebugEnabled())
			logger.debug("finished filters");
		return Filters.withAndFilters(andFilters);
	}

	public String searchQuery() {
		return searchText;
	}

}
