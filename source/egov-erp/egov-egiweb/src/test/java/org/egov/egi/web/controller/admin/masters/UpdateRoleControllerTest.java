package org.egov.egi.web.controller.admin.masters;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.egov.egi.web.controller.AbstractContextControllerTest;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.RoleService;
import org.egov.infra.security.utils.SecurityUtils;
import org.egov.infra.web.controller.admin.masters.role.UpdateRoleController;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

public class UpdateRoleControllerTest extends AbstractContextControllerTest<UpdateRoleController> {

	@Mock
    private RoleService roleService;
	
	@Mock
    private SecurityUtils securityUtils;
	@Mock
	private User user;
    private MockMvc mockMvc;
    

    @Override
    protected UpdateRoleController initController() {
        initMocks(this);

        return new UpdateRoleController(roleService);
    }

    @Before
    public void before() {
    	when(securityUtils.getCurrentUser()).thenReturn(user);
        mockMvc = mvcBuilder.build();
        Role role = new Role();
        role.setName("existing");
        when(roleService.getRoleByName(role.getName())).thenReturn(role);
    }

    @Test
    public void shouldRetrieveExistingRoleForUpdate() throws Exception {

    	Role role = new Role();
    	 role.setName("existing");


        MvcResult result = this.mockMvc.perform(get("/update-role/" + role.getName()))
                .andExpect(view().name("role-update"))
                .andExpect(model().attributeExists("role"))
                .andReturn();

        Role existingRole = (Role) result.getModelAndView().getModelMap().get("role");
        assertEquals(role.getName(), existingRole.getName());
    }

    @Test
    public void shouldUpdateRole() throws Exception {
        this.mockMvc.perform(post("/update-role/existing")
                .param("name", "existing-role"))
                .andExpect(model().hasNoErrors())
                .andExpect(redirectedUrl("/view-role/existing-role"));

        ArgumentCaptor<Role> argumentCaptor = ArgumentCaptor.forClass(Role.class);
        verify(roleService).update(argumentCaptor.capture());

        Role createdRole = argumentCaptor.getValue();
        assertEquals("existing-role", createdRole.getName());
    }
    @Test
    public void shouldValidateRoleWhileUpdating() throws Exception {
        this.mockMvc.perform(post("/update-role/existing")
                .param("name",""))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("role", "name"))
                .andExpect(model().errorCount(1))
                .andExpect(view().name("update-role/"));

        verify(roleService, never()).update(any(Role.class));
    }
   
}