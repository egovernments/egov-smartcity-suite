package org.egov.egi.web.controller.admin.masters;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.egov.egi.web.controller.AbstractContextControllerTest;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.RoleService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.web.controller.admin.masters.role.CreateRoleController;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;

public class CreateRoleControllerTest extends AbstractContextControllerTest<CreateRoleController> {
	
	
	@Mock
    private SecurityUtils securityUtils;
	
    @Mock
    private RoleService roleService;
    
    @InjectMocks
    private CreateRoleController controller;
    
    @Mock
    private User user;

    private MockMvc mockMvc;


    @Override
    protected CreateRoleController initController() {
        initMocks(this);
        return new CreateRoleController(roleService);
    }

    @Before
    public void before() {
    	when(securityUtils.getCurrentUser()).thenReturn(user);
        mockMvc = mvcBuilder.build();
    }

    @Test
    public void shouldResolveRoleCreationPage() throws Exception {
        this.mockMvc.perform(get("/create-role"))
                .andExpect(view().name("role-form"))
                .andExpect(status().isOk());
    }


    @Test
    public void shouldCreateNewRole() throws Exception {
        this.mockMvc.perform(post("/create-role")
                .param("name", "new-role"))
                .andExpect(model().hasNoErrors())
                .andExpect(redirectedUrl("view-role/new-role"));

        ArgumentCaptor<Role> argumentCaptor = ArgumentCaptor.forClass(Role.class);
        verify(roleService).createRole(argumentCaptor.capture());

        Role createdRole = argumentCaptor.getValue();
        assertTrue(createdRole.isNew());
        assertEquals("new-role", createdRole.getName());
    }
    
    @Test
    public void shouldValidateRoleWhileCreating() throws Exception {
        this.mockMvc.perform(post("/create-role"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("role", "name"))
                .andExpect(view().name("role-form"));

        verify(roleService, never()).createRole(any(Role.class));
    }
  
    
}