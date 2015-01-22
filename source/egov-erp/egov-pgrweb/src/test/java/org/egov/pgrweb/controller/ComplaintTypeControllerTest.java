package org.egov.pgrweb.controller;

import org.egov.builder.entities.DepartmentBuilder;
import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.dept.ejb.api.DepartmentService;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class ComplaintTypeControllerTest extends AbstractContextControllerTest<ComplaintTypeController> {

    @Mock
    private DepartmentService departmentService;

    @Override
    protected ComplaintTypeController initController() {
        initMocks(this);

        return new ComplaintTypeController(departmentService);
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
}