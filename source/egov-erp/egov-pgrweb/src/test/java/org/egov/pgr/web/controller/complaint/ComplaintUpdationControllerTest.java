package org.egov.pgr.web.controller.complaint;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.egov.pgr.entity.Complaint;
import org.egov.pgr.service.ComplaintService;
import org.egov.pgr.service.ComplaintTypeService;
import org.egov.pgr.web.controller.AbstractContextControllerTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

public class ComplaintUpdationControllerTest extends AbstractContextControllerTest<ComplaintUpdationController> {
	@Mock
	private Complaint complaint ;
	@Mock
	private ComplaintService complaintService;
	
	private MockMvc mockMvc;
	@Mock
	ComplaintTypeService complaintTypeService;
	private Long id;
	
	@Override
	protected ComplaintUpdationController initController() {
		initMocks(this);
		return new ComplaintUpdationController(complaintService,complaintTypeService);
	}
	@Before
	public void before()
	{
		
		//FormattingConversionService conversionService = new FormattingConversionService();
       // conversionService.addFormatter(null);
       //mvcBuilder.setConversionService(conversionService);
		
		mockMvc=mvcBuilder.build();
		complaint=new Complaint();
		complaint.setDetails("Already Registered complaint");
		id=2L;
		
		when(complaintService.get(id)).thenReturn(complaint);
	}
	public void show() throws Exception
	{
		id=2L;
		
		MvcResult result = this.mockMvc.perform(get("/complaint-update/" + id))
                .andExpect(view().name("complaint-show"))
                .andExpect(model().attributeExists("complaint"))
                .andReturn();

        Complaint existing = (Complaint) result.getModelAndView().getModelMap().get("complaint");
        assertEquals(complaint.getDetails(), existing.getDetails());
        
        verify(complaintTypeService).findAll();
	}

}
