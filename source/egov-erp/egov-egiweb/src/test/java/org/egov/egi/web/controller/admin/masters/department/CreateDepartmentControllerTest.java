package org.egov.egi.web.controller.admin.masters.department;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.egov.egi.web.controller.AbstractContextControllerTest;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.web.controller.admin.masters.department.CreateDepartmentController;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author subhash
 */
public class CreateDepartmentControllerTest extends AbstractContextControllerTest<CreateDepartmentController> {

    @Mock
    DepartmentService departmentService;

    private MockMvc mockMvc;

    @Override
    protected CreateDepartmentController initController() {
        initMocks(this);
        return new CreateDepartmentController(departmentService);
    }

    @Before
    public void before() {
        mockMvc = mvcBuilder.build();
    }

    @Test
    public void shouldResolveDepartmentCreationForm() throws Exception {
        this.mockMvc.perform(get("/create-department")).andExpect(view().name("department-form"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldCreateNewDepartment() throws Exception {
        this.mockMvc.perform(post("/create-department").param("name", "testing").param("code", "testing"))
                .andExpect(model().hasNoErrors()).andExpect(redirectedUrl("create-department"));
        ArgumentCaptor<Department> argumentCaptor = ArgumentCaptor.forClass(Department.class);
        verify(departmentService).createDepartment(argumentCaptor.capture());
        Department createdDeprtment = argumentCaptor.getValue();
        assertTrue(createdDeprtment.isNew());
        assertEquals(createdDeprtment.getName(), "testing");
        assertEquals(createdDeprtment.getCode(), "testing");
    }

    @Test
    public void shouldValidateWhileCreating() throws Exception {
        this.mockMvc.perform(post("/create-department")).andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("department", "code"))
                .andExpect(view().name("department-form"));
        verify(departmentService, never()).createDepartment(any(Department.class));
    }
}
