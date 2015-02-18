package org.egov.pgr.web.contract;

import org.egov.search.domain.Filter;
import org.egov.search.domain.Filters;

import java.util.ArrayList;
import java.util.List;

import static org.egov.search.domain.Filter.queryStringFilter;

public class ComplaintSearchRequest {
    private String searchText;
    private String complaintNumber;

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public void setComplaintNumber(String complaintNumber) {
        this.complaintNumber = complaintNumber;
    }

    public Filters searchFilters() {
        List<Filter> andFilters = new ArrayList<>();
        andFilters.add(queryStringFilter("searchable.crn", complaintNumber));
        return Filters.withAndFilters(andFilters);
    }


    public String searchQuery() {
        return searchText;
    }
}
