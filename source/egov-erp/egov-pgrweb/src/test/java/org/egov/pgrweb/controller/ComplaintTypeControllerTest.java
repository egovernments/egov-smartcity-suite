package org.egov.pgrweb.controller;

import org.junit.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ComplaintTypeControllerTest extends AbstractContextControllerTest<ComplaintTypeController> {

    @Override
    protected ComplaintTypeController initController() {
        return new ComplaintTypeController();
    }

    @Test
    public void shouldResolveComplaintTypeCreationPage() throws Exception {
        this.mockMvc.perform(get("/complaint-type"))
                .andExpect(content().string("complaint-type"))
                .andExpect(status().isOk());
    }
}