package org.egov.pgr.web.contract;

import org.egov.search.domain.Filter;
import org.egov.search.domain.Filters;
import org.egov.search.domain.QueryStringFilter;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ComplaintSearchRequestTest {

    private ComplaintSearchRequest request;

    @Before
    public void before() {
        request = new ComplaintSearchRequest();
    }

    @Test
    public void shouldConstructFilters() {
        request.setComplaintNumber("CRN123");
        request.setSearchText("road");

        Filters filters = request.searchFilters();
        String searchQuery = request.searchQuery();

        assertThat(searchQuery, is("road"));
        assertThat(filters.getAndFilters().size(), is(1));

        Filter filter = filters.getAndFilters().get(0);
        assertThat(filter.field(), is("searchable.crn"));
        assertThat(filter, instanceOf(QueryStringFilter.class));
        assertThat(((QueryStringFilter) filter).value(), is("CRN123"));
    }

}