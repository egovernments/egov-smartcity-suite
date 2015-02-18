package org.egov.pgr.web.controller.complaint;

import org.egov.config.search.Index;
import org.egov.config.search.IndexType;
import org.egov.pgr.web.controller.AbstractContextControllerTest;
import org.egov.search.domain.Filter;
import org.egov.search.domain.Filters;
import org.egov.search.domain.Page;
import org.egov.search.domain.QueryStringFilter;
import org.egov.search.domain.SearchResult;
import org.egov.search.domain.Sort;
import org.egov.search.service.SearchService;
import org.egov.search.util.Classpath;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class ComplaintSearchControllerTest extends AbstractContextControllerTest<ComplaintSearchController> {

    private MockMvc mockMvc;
    @Mock
    private SearchService searchService;

    @Override
    protected ComplaintSearchController initController() {
        MockitoAnnotations.initMocks(this);
        return new ComplaintSearchController(searchService);
    }

    @Before
    public void before() {
        mockMvc = mvcBuilder.build();
    }

    @Test
    public void shouldRetrieveSearchPage() throws Exception {
        this.mockMvc.perform(get("/complaint/citizen/anonymous/search"))
                .andExpect(view().name("complaint-search"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldSearchForGivenRequest() throws Exception {
        when(searchService.search(anyList(), anyList(), anyString(), any(Filters.class), eq(Sort.NULL), eq(Page.NULL)))
                .thenReturn(SearchResult.from(Classpath.readAsString("complaintSearchControllerTest-searchResponse.json")));

        MvcResult mvcResult = this.mockMvc.perform(
                    post("/complaint/citizen/anonymous/search")
                        .param("searchText", "road")
                        .param("complaintNumber", "CRN123")
                    )
                .andExpect(status().isOk())
                .andReturn();

        ArgumentCaptor<Filters> filterCaptor =ArgumentCaptor.forClass(Filters.class);

        verify(searchService).search(eq(asList(Index.PGR.toString())),
                eq(asList(IndexType.COMPLAINT.toString())),
                eq("road"),
                filterCaptor.capture(),
                eq(Sort.NULL), eq(Page.NULL));

        Filters actualFilters = filterCaptor.getValue();
        Filter filter = actualFilters.getAndFilters().get(0);
        assertThat(filter.field(), is("searchable.crn"));
        assertThat(filter, instanceOf(QueryStringFilter.class));
        assertThat(((QueryStringFilter) filter).value(), is("CRN123"));
    }

}