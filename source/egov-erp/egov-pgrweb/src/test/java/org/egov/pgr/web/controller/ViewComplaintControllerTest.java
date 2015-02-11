package org.egov.pgr.web.controller;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.egov.pgr.service.ComplaintService;
import org.egov.pgr.web.controller.complaint.ViewComplaintController;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;

public class ViewComplaintControllerTest extends AbstractContextControllerTest<ViewComplaintController> {

	private MockMvc mockMvc;
	public Long complaintId;

	@Mock
	private ComplaintService complaintService;

	@Before
	public void before() {
        mockMvc = mvcBuilder.build();
        
	}

	@Override
	protected ViewComplaintController initController() {
		initMocks(this);
		return new ViewComplaintController(complaintService);

	}

	@Test
	public void getViewComplaintResult() throws Exception {
		complaintId = 1L;
		this.mockMvc.perform(get("/view-complaint")
				.param("complaintId",String.valueOf(complaintId)))
				.andExpect(view().name("view-complaint"))
				.andExpect(status().isOk());

	}

}
