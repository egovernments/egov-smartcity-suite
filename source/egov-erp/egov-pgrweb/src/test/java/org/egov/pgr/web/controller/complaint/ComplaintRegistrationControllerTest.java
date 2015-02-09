package org.egov.pgr.web.controller.complaint;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.egov.pgr.service.ComplaintService;
import org.egov.pgr.web.controller.AbstractContextControllerTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;

public class ComplaintRegistrationControllerTest extends AbstractContextControllerTest<ComplaintRegistrationController>{

	@Mock
	private ComplaintService complaintService;
	
	private MockMvc mockMvc;
	
	@Override
	protected ComplaintRegistrationController initController() {
		initMocks(this);
		return new ComplaintRegistrationController(complaintService);
	}
	
	@Before
	public void beforeTest() {
		mockMvc = mvcBuilder.build();
	}

	@Test
	public void assertRegistrationPageViewReturns() throws Exception {
		this.mockMvc.perform(get("/complaint/register")).
		andExpect(view().name("complaint/registration")).
		andExpect(status().isOk());
	}
}
