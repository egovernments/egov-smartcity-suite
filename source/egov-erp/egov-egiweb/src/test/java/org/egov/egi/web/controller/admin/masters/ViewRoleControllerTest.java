package org.egov.egi.web.controller.admin.masters;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.egov.egi.web.controller.AbstractContextControllerTest;
import org.egov.infra.admin.master.service.RoleService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;

public class ViewRoleControllerTest extends AbstractContextControllerTest<ViewRoleController> {

	private MockMvc mockMvc;
	
	public String name;

	@Mock
	private RoleService roleService;

	@Before
	public void before() {
        mockMvc = mvcBuilder.build();
        
	}

	@Override
    protected ViewRoleController initController() {
		
        initMocks(this);

        return new ViewRoleController(roleService);
    }

	@Test
	public void getViewRoleResult() throws Exception {
		name = "existing-role";
		this.mockMvc.perform(get("/view-role/"+name))
				.andExpect(view().name("role-view"))
				.andExpect(status().isOk());

	}

}
