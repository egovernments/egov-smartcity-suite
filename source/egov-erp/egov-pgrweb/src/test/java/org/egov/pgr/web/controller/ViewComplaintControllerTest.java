package org.egov.pgr.web.controller;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.egov.builder.entities.DepartmentBuilder;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.pgr.entity.Complainant;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.service.ComplaintService;
import org.egov.pgr.web.controller.complaint.ViewComplaintController;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;

public class ViewComplaintControllerTest extends AbstractContextControllerTest<ViewComplaintController> {

	private MockMvc mockMvc;
	public String crnNo;

	@Mock
	private ComplaintService complaintService;
	
	@Mock
        private DepartmentService departmentService;

	@Before
	public void before() {
        mockMvc = mvcBuilder.build();
        Department department = new DepartmentBuilder().withCode("DC").build();
        when(departmentService.getDepartmentByCode(anyString())).thenReturn(department);

        ComplaintType complaintType = new ComplaintType();
        complaintType.setName("existing");
        complaintType.setDepartment(department);
        
        Complainant complainant = new Complainant();
        complainant.setEmail("abc@gmail.com");
        complainant.setName("xyz");
        
        Complaint complaint = new Complaint();
        complaint.setComplaintType(complaintType);
        complaint.setComplainant(complainant);
        crnNo = "CRN-rmd1";
        when(complaintService.getComplaintByCrnNo(crnNo)).thenReturn(complaint);
        
	}

	@Override
	protected ViewComplaintController initController() {
		initMocks(this);
		return new ViewComplaintController(complaintService);

	}

	@Test
	public void getViewComplaintResult() throws Exception {
		this.mockMvc.perform(get("/view-complaint")
				.param("crnNo",String.valueOf(crnNo)))
				.andExpect(view().name("view-complaint"))
				.andExpect(status().isOk());

	}

}
