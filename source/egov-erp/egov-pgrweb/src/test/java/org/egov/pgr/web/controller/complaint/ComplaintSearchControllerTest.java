package org.egov.pgr.web.controller.complaint;

import org.egov.pgr.web.controller.AbstractContextControllerTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class ComplaintSearchControllerTest extends AbstractContextControllerTest<ComplaintSearchController> {

    private MockMvc mockMvc;

    @Override
    protected ComplaintSearchController initController() {
        return new ComplaintSearchController();
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

}