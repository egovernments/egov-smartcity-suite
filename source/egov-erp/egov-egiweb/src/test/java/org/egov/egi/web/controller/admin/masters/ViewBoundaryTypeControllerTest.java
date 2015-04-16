package org.egov.egi.web.controller.admin.masters;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.egov.egi.web.controller.AbstractContextControllerTest;
import org.egov.infra.admin.master.service.BoundaryTypeService;
import org.egov.infra.web.controller.admin.masters.boundarytype.ViewBoundaryTypeController;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;

public class ViewBoundaryTypeControllerTest extends AbstractContextControllerTest<ViewBoundaryTypeController> {

    private MockMvc mockMvc;
    
    @Mock
    private BoundaryTypeService boundaryTypeService;
    
    @Before
    public void before() {
        mockMvc = mvcBuilder.build();
    }

    @Override
    protected ViewBoundaryTypeController initController() {
        initMocks(this);
        return new ViewBoundaryTypeController(boundaryTypeService);
    }
    
    @Test
    public void getViewBoundaryTypeResult() throws Exception {
            Long id = 1l;
            this.mockMvc.perform(get("/boundarytype/view/"+id))
                            .andExpect(view().name("boundaryType-view"))
                            .andExpect(status().isOk());

    }
    
}
