package org.egov.pgr.web.controller.complaint;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.egov.infra.utils.SecurityUtils;
import org.egov.lib.admbndry.BoundaryDAO;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.rjbac.role.Role;
import org.egov.lib.rjbac.role.RoleImpl;
import org.egov.lib.rjbac.role.dao.RoleDAO;
import org.egov.lib.rjbac.user.User;
import org.egov.pgr.entity.Complaint;
import org.egov.pgr.entity.ComplaintStatus;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.service.CommonService;
import org.egov.pgr.service.ComplaintService;
import org.egov.pgr.service.ComplaintStatusMappingService;
import org.egov.pgr.service.ComplaintStatusService;
import org.egov.pgr.service.ComplaintTypeService;
import org.egov.pgr.web.controller.AbstractContextControllerTest;
import org.egov.pgr.web.formatter.BoundaryImplFormatter;
import org.egov.pgr.web.formatter.ComplaintStatusFormatter;
import org.egov.pgr.web.formatter.ComplaintTypeFormatter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.util.NestedServletException;

public class ComplaintUpdationControllerTest extends AbstractContextControllerTest<ComplaintUpdationController> {
	@Mock
	private Complaint complaint ;
	@Mock
	private ComplaintService complaintService;
	@Mock
	private BoundaryDAO boundaryDAO;
	
	@Mock
	private CommonService commonService;  
	
	@Mock
	private ComplaintStatusService complaintStatusService;
	
	@Mock
	ComplaintStatusMappingService complaintStatusMappingService;
	
	@Mock
	private SmartValidator smartValidator; 
	
	@Autowired
	private SmartValidator validator; 
	
	
	private MockMvc mockMvc;
	@Mock
	ComplaintTypeService complaintTypeService;
	private Long id;
	private ComplaintType complaintType;
	
	@Mock
	Complaint	complaint1;
	
	@Mock
	BindingResult errors;
	
	@Mock
	private SecurityUtils securityUtils;
	
	@Mock
	RoleDAO roleDAO;  
	
	@Mock
    User user;

	
	@Override
	protected ComplaintUpdationController initController() {
		initMocks(this);
		
		ComplaintUpdationController complaintUpdationController = new ComplaintUpdationController
				(complaintService,complaintTypeService, commonService, complaintStatusMappingService, smartValidator, securityUtils, roleDAO);
		//when(complaintUpdationController.getStatus()).thenReturn(value, values);
		
		return complaintUpdationController; 
	}
	@Before
	public void before()
	{
		
		FormattingConversionService conversionService = new FormattingConversionService();
		conversionService.addFormatter(new BoundaryImplFormatter(boundaryDAO));
		conversionService.addFormatter(new ComplaintTypeFormatter(complaintTypeService));
		conversionService.addFormatter(new ComplaintStatusFormatter(complaintStatusService));
        mvcBuilder.setConversionService(conversionService);
		
		mockMvc=mvcBuilder.build();
		complaint=new Complaint();
		complaint.setDetails("Already Registered complaint");
		id=2L;
		when(complaintService.getComplaintById(id)).thenReturn(complaint);
		
		complaintType = new ComplaintType();
		List ctList=new ArrayList<ComplaintType>();
		ctList.add(complaintType);
		when(complaintTypeService.findAll()).thenReturn(ctList);
		
		List<ComplaintStatus> csList=new ArrayList<ComplaintStatus>();
		ComplaintStatus cs=new ComplaintStatus(); 
		
		when(securityUtils.getCurrentUser()).thenReturn(user);
		RoleImpl r=new RoleImpl();
		List<Role> roleList=new ArrayList<Role>();
		roleList.add(r);
		
		when(roleDAO.getRolesByUser(Mockito.anyInt())).
				thenReturn(roleList);  
		csList.add(cs);
		when(complaintStatusMappingService.getStatusByRoleAndCurrentStatus(roleList, cs)).thenReturn(csList);
	
	
	}
	
	
	@Test(expected=NestedServletException.class )
	public void editWithInvalidComplaintId() throws Exception  
	{
		complaint=new Complaint();
		complaintType.setLocationRequired(false);
		complaint.setComplaintType(complaintType);
		complaint.setDetails("Already Registered complaint"); 
		MvcResult result = this.mockMvc.perform(get("/complaint-update/1")).andReturn();
	}
	
	@Test
	public void editWithComplaintTypeLocationRequiredFalse() throws Exception
	{
		complaint=new Complaint();
		complaintType.setLocationRequired(false);
		complaint.setComplaintType(complaintType);
		complaint.setDetails("Already Registered complaint");
		when(complaintService.getComplaintById(id)).thenReturn(complaint);
		
		MvcResult result = this.mockMvc.perform(get("/complaint-update/2"))
                .andExpect(view().name("complaint-edit"))
                .andExpect(model().attributeExists("complaint"))
                .andReturn();

        Complaint existing = (Complaint) result.getModelAndView().getModelMap().get("complaint");
        assertEquals(complaint.getDetails(), existing.getDetails());
     
	}
	
	@Test
	public void editWithComplaintTypeLocationRequiredTrue() throws Exception
	{
		complaint=new Complaint();
		complaintType.setLocationRequired(true);
		complaint.setComplaintType(complaintType);
		complaint.setDetails("Already Registered complaint");
		id=2L;
		when(complaintService.getComplaintById(id)).thenReturn(complaint);

		MvcResult result = this.mockMvc.perform(get("/complaint-update/2"))
				.andExpect(status().isOk())
                .andExpect(view().name("complaint-edit"))
                .andExpect(model().attributeExists("complaint"))
                .andReturn();

        Complaint existing = (Complaint) result.getModelAndView().getModelMap().get("complaint");
        assertEquals(complaint.getDetails(), existing.getDetails());
     
	}
	
	
	@Test
	public void editWithBoundary() throws Exception
	{
		complaint=new Complaint();
		complaintType.setLocationRequired(true);
		complaint.setComplaintType(complaintType);
		complaint.setDetails("Already Registered complaint");
		BoundaryImpl ward = new BoundaryImpl();
		ward.setId(1);
		BoundaryImpl zone = new BoundaryImpl();
		zone.setId(2);
		ward.setParent(zone);
		complaint.setLocation(ward);
		id=2L;
		when(complaintService.getComplaintById(id)).thenReturn(complaint);
		List<BoundaryImpl> wards=new ArrayList<BoundaryImpl>();
		when(commonService.getWards(ward.getId())).thenReturn(wards);  

		MvcResult result = this.mockMvc.perform(get("/complaint-update/2"))
				.andExpect(status().isOk())
                .andExpect(view().name("complaint-edit"))
                .andExpect(model().attributeExists("complaint"))
                .andReturn();

        Complaint existing = (Complaint) result.getModelAndView().getModelMap().get("complaint");
        assertEquals(complaint.getDetails(), existing.getDetails());
     
	}
	
	@Test
	public void updateWithoutComplaintType() throws Exception
	{
		ComplaintStatus cs1=new ComplaintStatus();
		complaint1.setStatus(cs1);
		complaintType.setLocationRequired(true);
		complaint1.setComplaintType(complaintType);
		complaint1.setDetails("Already Registered complaint");
		id=2L;
		when(complaintService.getComplaintById(id)).thenReturn(complaint1);
		when(complaint1.getId()).thenReturn(2L);
		  this.mockMvc.perform(post("/complaint-update/2")
	                .param("id", "2")
	                .param("complaintStatus", "2"))
	                .andDo(print())
	                 .andExpect(flash().attributeExists("message"))
	                .andExpect(status().is3xxRedirection())
	                .andReturn();  
     
	}
	@Test
	public void updateWithComplaintType() throws Exception
	{
	
		ComplaintStatus cs=new ComplaintStatus();
		cs.setName("Forwarded");
		complaint1.setStatus(cs);
		complaintType.setLocationRequired(true);
		complaint1.setComplaintType(complaintType);
		complaint1.setDetails("Already Registered complaint");
		id=2L;
		when(complaintService.getComplaintById(id)).thenReturn(complaint1);
		when(complaint1.getId()).thenReturn(2L);
		when(complaintTypeService.load(id)).thenReturn(complaintType);
		  this.mockMvc.perform(post("/complaint-update/2")
	                .param("id", "2")
	                .param("complaintStatus", "2")
	                .param("complaintType", "2"))
	                .andDo(print())
	                 .andExpect(flash().attributeExists("message"))
	                .andExpect(status().is3xxRedirection())
	                .andReturn();  
     
	} 
	
	
	
}