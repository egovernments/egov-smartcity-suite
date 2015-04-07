package org.egov.pgr.web.controller.masters;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.List;

import org.egov.builder.entities.DepartmentBuilder;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.web.support.formatter.DepartmentFormatter;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.service.ComplaintTypeService;
import org.egov.pgr.web.controller.AbstractContextControllerTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

public class UpdateComplaintTypeControllerTest extends AbstractContextControllerTest<UpdateComplaintTypeController> {

    @Mock
    private DepartmentService departmentService;
    @Mock
    private ComplaintTypeService complaintTypeService;
    private MockMvc mockMvc;
    private Long complaintTypeId;

    @Override
    protected UpdateComplaintTypeController initController() {
        initMocks(this);

        return new UpdateComplaintTypeController(departmentService, complaintTypeService);
    }

    @Before
    public void before() {
        FormattingConversionService conversionService = new FormattingConversionService();
        conversionService.addFormatter(new DepartmentFormatter(departmentService));
        mvcBuilder.setConversionService(conversionService);
        mockMvc = mvcBuilder.build();

        Department department = new DepartmentBuilder().withCode("DC").build();
        when(departmentService.getDepartmentByCode(anyString())).thenReturn(department);

        ComplaintType complaintType = new ComplaintType();
        complaintType.setName("existing");
        complaintType.setDepartment(department);
        complaintTypeId = 22L;
        when(complaintTypeService.findBy(complaintTypeId)).thenReturn(complaintType);
    }

    @Test
    public void shouldRetrieveExistingComplaintTypeForUpdate() throws Exception {
        Department department1 = new DepartmentBuilder().withName("dep1").build();
        Department department2 = new DepartmentBuilder().withName("dep2").build();
        List<Department> departments = Arrays.asList(department1, department2);

        ComplaintType complaintType = new ComplaintType();
        complaintType.setName("existing");
        Long complaintTypeId = 22L;

        when(departmentService.getAllDepartments()).thenReturn(departments);

        MvcResult result = this.mockMvc.perform(get("/complaint-type/" + complaintTypeId))
                .andExpect(view().name("complaint-type"))
                .andExpect(model().attribute("departments", departments))
                .andExpect(model().attributeExists("complaintType"))
                .andReturn();

        ComplaintType existingComplaintType = (ComplaintType) result.getModelAndView().getModelMap().get("complaintType");
        assertEquals(complaintType.getName(), existingComplaintType.getName());
    }

    @Test
    public void shouldUpdateComplaintType() throws Exception {
        this.mockMvc.perform(post("/complaint-type/22")
                .param("name", "existing-complaint-type")
                .param("department", "DC"))
                .andExpect(model().hasNoErrors())
                .andExpect(redirectedUrl("/complaint-type"));

        ArgumentCaptor<ComplaintType> argumentCaptor = ArgumentCaptor.forClass(ComplaintType.class);
        verify(complaintTypeService).updateComplaintType(argumentCaptor.capture());

        ComplaintType createdComplaintType = argumentCaptor.getValue();
        assertEquals("existing-complaint-type", createdComplaintType.getName());
    }

    @Test
    public void shouldValidateComplaintTypeWhileUpdating() throws Exception {
        this.mockMvc.perform(post("/complaint-type/22")
                .param("name",""))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("complaintType", "name"))
                .andExpect(model().errorCount(1))
                .andExpect(view().name("complaint-type"));

        verify(complaintTypeService, never()).updateComplaintType(any(ComplaintType.class));
    }
}