package org.egov.pgr.web.controller.complaint.citizen;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.egov.infra.utils.SecurityUtils;
import org.egov.lib.rjbac.user.User;
import org.egov.pgr.service.ComplaintService;
import org.egov.pgr.service.ComplaintTypeService;
import org.egov.pgr.web.controller.AbstractContextControllerTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;

public class CitizenComplaintRegistrationControllerTest extends AbstractContextControllerTest<CitizenComplaintRegistrationController> {

    @Mock
    private ComplaintService complaintService;
    @Mock
    private ComplaintTypeService complaintTypeService;
    @Mock
    private SecurityUtils securityUtils;
    @InjectMocks
    private CitizenComplaintRegistrationController controller;

    @Mock
    User user;

    MockMvc mockMvc;

    @Before
    public void before() {
        when(securityUtils.getCurrentUser()).thenReturn(user);
        mockMvc = mvcBuilder.build();
    }

    @Test
    public void assertCitizenRegistrationPageViewReturns() throws Exception {
        mockMvc.perform(get("/complaint/citizen/show-reg-form"))
                .andExpect(view().name("complaint/citizen/registration-form"))
                .andExpect(status().isOk());
    }

    @Test
    public void assertAnonymousRegistrationPageViewReturns() throws Exception {
        mockMvc.perform(get("/complaint/citizen/anonymous/show-reg-form"))
                .andExpect(view().name("complaint/citizen/anonymous-registration-form"))
                .andExpect(status().isOk());
    }
    
    @Override
    protected CitizenComplaintRegistrationController initController() {
        initMocks(this);
        return controller;
    }
}