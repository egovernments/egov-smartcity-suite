package org.egov.pgrweb.controller;

import org.egov.builder.entities.DepartmentBuilder;
import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.dept.ejb.api.DepartmentService;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.service.ComplaintTypeService;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class ComplaintTypeControllerTest extends AbstractContextControllerTest<ComplaintTypeController> {

    @Mock
    private DepartmentService departmentService;
    @Mock
    private ComplaintTypeService complaintTypeService;

    @Override
    protected ComplaintTypeController initController() {
        initMocks(this);

        return new ComplaintTypeController(departmentService, complaintTypeService);
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
                .param("name", "new-complaint-type"))
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
                .andExpect(view().name("complaint-type"));

        verify(complaintTypeService, never()).createComplaintType(any(ComplaintType.class));
    }
}