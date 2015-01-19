package org.egov.pgrweb.controller;

import org.junit.Test;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


public class ComplaintTypeControllerTest extends AbstractContextControllerTest<ComplaintTypeController> {

    @Override
    protected ComplaintTypeController initController() {
        initMocks(this);

        return new ComplaintTypeController();
    }

    @Test
    public void shouldResolveComplaintTypeCreationPage() throws Exception {
        this.mockMvc.perform(get("/complaint-type"))
                .andExpect(view().name("complaint-type"))
                .andExpect(status().isOk());
    }
}