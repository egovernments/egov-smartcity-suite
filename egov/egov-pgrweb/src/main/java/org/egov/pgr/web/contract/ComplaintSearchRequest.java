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
package org.egov.pgr.web.contract;

import org.egov.search.domain.Filter;
import org.egov.search.domain.Filters;

import java.util.ArrayList;
import java.util.List;
import org.jboss.logging.Logger;
import static org.egov.search.domain.Filter.queryStringFilter;
import static org.egov.search.domain.Filter.rangeFilter;

public class ComplaintSearchRequest {
	private String searchText;
	private String complaintNumber;
	private String complainantName;
	private String complaintStatus;
	private String complainantPhoneNumber;
	private String complainantEmail;
	private String receivingCenter;
	private String complaintType;
	private String complaintDate;
	private String complaintDateFrom;
	private String complaintDateTo;

	private static final Logger logger = Logger
			.getLogger(ComplaintSearchRequest.class);

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public void setComplaintNumber(String complaintNumber) {
		this.complaintNumber = complaintNumber;
	}

	public void setComplaintStatus(String complaintStatus) {
		this.complaintStatus = complaintStatus;
	}

	public void setComplainantName(String complainantName) {
		this.complainantName = complainantName;
	}

	public void setComplainantPhoneNumber(String phoneNumber) {
		this.complainantPhoneNumber = phoneNumber;
	}

	public void setComplainantEmail(String email) {
		this.complainantEmail = email;
	}

	public void setReceivingCenter(String receivingCenter) {
		this.receivingCenter = receivingCenter;
	}

	public void setComplaintType(String complaintType) {
		this.complaintType = complaintType;
	}

	public void setComplaintDate(String complaintDate) {
		this.complaintDate = complaintDate;
		if (null != complaintDate) {
			if (complaintDate.equalsIgnoreCase("today")) {
				logger.info("This is today selection");
				complaintDateFrom = "2015-05-19";
				complaintDateTo = "2015-05-19";
			} else if (complaintDate.equalsIgnoreCase("all")) {
				complaintDateFrom = null;
				complaintDateTo = null;
			} else if (complaintDate.equalsIgnoreCase("lastsevendays")) {
				complaintDateFrom = "2015-05-19";
				complaintDateTo = "2015-05-19";
			} else if (complaintDate.equalsIgnoreCase("lastthirtydays")) {
				complaintDateFrom = "2015-05-19";
				complaintDateTo = "2015-05-19";
			} else if (complaintDate.equalsIgnoreCase("lastninetydays")) {
				complaintDateFrom = "2015-05-19";
				complaintDateTo = "2015-05-19";
			} else {
				logger.info("Else section in date range");
				complaintDateFrom = "2015-05-19";
				complaintDateTo = "2015-05-19";
			}
		}

	}

	public Filters searchFilters() {
		List<Filter> andFilters = new ArrayList<>();
		andFilters.add(queryStringFilter("searchable.crn", complaintNumber));
		andFilters
				.add(queryStringFilter("common.citizen.name", complainantName));
		andFilters.add(queryStringFilter("common.citizen.mobile",
				complainantPhoneNumber));
		andFilters.add(queryStringFilter("common.citizen.email",
				complainantEmail));
		andFilters
				.add(queryStringFilter("clauses.status.name", complaintStatus));
		andFilters.add(queryStringFilter("clauses.receivingMode",
				receivingCenter));
		andFilters.add(queryStringFilter("searchable.complaintType.name",
				complaintType));
		andFilters.add(rangeFilter("common.createdDate", complaintDateFrom,
				complaintDateTo));

		return Filters.withAndFilters(andFilters);
	}

	public String searchQuery() {
		return searchText;
	}
}
