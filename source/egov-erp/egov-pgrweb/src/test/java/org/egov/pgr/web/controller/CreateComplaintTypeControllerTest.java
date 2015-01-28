package org.egov.pgr.web.controller;

import org.egov.builder.entities.DepartmentBuilder;
import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.dept.ejb.api.DepartmentService;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.service.ComplaintTypeService;
import org.egov.pgr.web.formatter.DepartmentFormatter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CreateComplaintTypeControllerTest extends AbstractContextControllerTest<CreateComplaintTypeController> {

    @Mock
    private DepartmentService departmentService;
    @Mock
    private ComplaintTypeService complaintTypeService;
    private MockMvc mockMvc;

    @Override
    protected CreateComplaintTypeController initController() {
        initMocks(this);

        return new CreateComplaintTypeController(departmentService, complaintTypeService);
    }

    @Before
    public void before() {
        FormattingConversionService conversionService = new FormattingConversionService();
        conversionService.addFormatter(new DepartmentFormatter(departmentService));
        mvcBuilder.setConversionService(conversionService);
        mockMvc = mvcBuilder.build();

        Department department = new DepartmentBuilder().withCode("DC").build();
        when(departmentService.getDepartmentByCode(anyString())).thenReturn(department);
    }

    @Test
    public void shouldResolveComplaintTypeCreationPage() throws Exception {
        this.mockMvc.perform(get("/complaint-type"))
                .andExpect(view().name("complaint-type"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldAddDepartmentsAsModelAttribute() throws Exception {
        Department department1 = new DepartmentBuilder().withName("dep1").build();
        Department department2 = new DepartmentBuilder().withName("dep2").build();

        List<Department> departments = Arrays.asList(department1, department2);
        when(departmentService.getAllDepartments()).thenReturn(departments);

        this.mockMvc.perform(get("/complaint-type"))
                .andExpect(view().name("complaint-type"))
                .andExpect(model().attribute("departments", departments))
                .andExpect(status().isOk());

        verify(departmentService).getAllDepartments();
    }

    @Test
    public void shouldCreateNewComplaintType() throws Exception {
        this.mockMvc.perform(post("/complaint-type")
                .param("name", "new-complaint-type")
                .param("department", "DC"))
                .andExpect(model().hasNoErrors())
                .andExpect(redirectedUrl("/complaint-type"));

        ArgumentCaptor<ComplaintType> argumentCaptor = ArgumentCaptor.forClass(ComplaintType.class);
        verify(complaintTypeService).createComplaintType(argumentCaptor.capture());

        ComplaintType createdComplaintType = argumentCaptor.getValue();
        assertTrue(createdComplaintType.isNew());
        assertEquals("new-complaint-type", createdComplaintType.getName());
    }

    @Test
    public void shouldValidateComplaintTypeWhileCreating() throws Exception {
        this.mockMvc.perform(post("/complaint-type"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("complaintType", "name"))
                .andExpect(model().attributeHasFieldErrors("complaintType", "department"))
                .andExpect(view().name("complaint-type"));

        verify(complaintTypeService, never()).createComplaintType(any(ComplaintType.class));
    }
}