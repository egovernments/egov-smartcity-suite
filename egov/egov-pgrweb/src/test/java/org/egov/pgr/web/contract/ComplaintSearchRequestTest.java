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
import org.egov.search.domain.RangeFilter;
import org.egov.search.domain.TermsStringFilter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.egov.infra.utils.DateUtils.endOfGivenDate;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ComplaintSearchRequestTest {

	private ComplaintSearchRequest request;
	public static final String SEARCH_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm";

	@Before
	public void before() {
		request = new ComplaintSearchRequest();
	}

	@Test
	public void shouldConstructFilters() {
		request.setComplaintNumber("CRN-123-XYZ");
		request.setSearchText("road");

		Filters filters = request.searchFilters();
		String searchQuery = request.searchQuery();

		assertThat(searchQuery, is("road"));
		assertThat(filters.getAndFilters().size(), is(1));

		Filter filter = filters.getAndFilters().get(0);
		assertThat(filter.field(), is("clauses.crn"));
		assertThat(filter, instanceOf(TermsStringFilter.class));
		//assertThat((((TermsStringFilter) filter).values()),is ("CRN-123".toArray));
	}

	@Test
	public void searchForNoOfDays() {
		request.setComplaintDate("today");

		Filters filters = request.searchFilters();
		DateTime today = endOfGivenDate(new DateTime());
		
		assertThat(filters.getAndFilters().size(), is(1));
		Filter filter = filters.getAndFilters().get(0);
		
		assertThat(filter.field(), is("common.createdDate"));
		assertThat(filter, instanceOf(RangeFilter.class));
		assertThat(((RangeFilter) filter).to(), is(today.toString(SEARCH_DATE_FORMAT)));
	}

	@Test
	public void searchForDateRange() {
		request.setFromDate("01/05/2015");
	//	request.setToDate("19/05/2015");
		
		Filters filters = request.searchFilters();
		assertThat(filters.getAndFilters().size(), is(1));
		
		Filter filter = filters.getAndFilters().get(0);
		assertThat(filter.field(), is("common.createdDate"));
		assertThat(filter, instanceOf(RangeFilter.class));
	}
}