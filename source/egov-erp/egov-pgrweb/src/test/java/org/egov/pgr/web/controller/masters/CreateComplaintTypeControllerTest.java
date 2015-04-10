package org.egov.pgr.web.controller.masters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.egov.builder.entities.DepartmentBuilder;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.service.ComplaintTypeService;
import org.egov.pgr.web.controller.AbstractContextControllerTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.Formatter;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.test.web.servlet.MockMvc;

public class CreateComplaintTypeControllerTest extends AbstractContextControllerTest<CreateComplaintTypeController> {

    @Mock
    private DepartmentService departmentService;
    @Mock
    private ComplaintTypeService complaintTypeService;
    
    @Autowired
    private ConversionService conversionService;
    
    
    private MockMvc mockMvc;

    @Override
    protected CreateComplaintTypeController initController() {
        initMocks(this);
        return new CreateComplaintTypeController(departmentService, complaintTypeService);
    }

    @Before
    public void before() throws Exception {
        final Department department = new DepartmentBuilder().withId(1).withCode("DC").build();
        when(departmentService.getDepartmentById(any(Long.class))).thenReturn(department);
        FormattingConversionService formatterService = new FormattingConversionService();
        formatterService.addFormatter(new Formatter<Department>() {

            @Override
            public String print(Department object, Locale locale) {
               return null;
            }

            @Override
            public Department parse(String text, Locale locale) throws ParseException {
                return department;
            }
        });
        mvcBuilder.setConversionService(formatterService);
        mockMvc = mvcBuilder.build();
      
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
                .param("department", "1"))
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