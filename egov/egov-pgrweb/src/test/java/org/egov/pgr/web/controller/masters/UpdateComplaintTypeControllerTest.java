/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */
package org.egov.pgr.web.controller.masters;

import org.egov.builder.entities.DepartmentBuilder;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.service.ComplaintTypeCategoryService;
import org.egov.pgr.service.ComplaintTypeService;
import org.egov.pgr.web.controller.AbstractContextControllerTest;
import org.egov.pgr.web.controller.masters.type.UpdateComplaintTypeController;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.format.Formatter;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class UpdateComplaintTypeControllerTest extends AbstractContextControllerTest<UpdateComplaintTypeController> {

    @Mock
    private DepartmentService departmentService;
    @Mock
    private ComplaintTypeService complaintTypeService;
    @Mock
    private ComplaintTypeCategoryService complaintTypeCategoryService;

    private MockMvc mockMvc;
    private String code;

    @InjectMocks
    private UpdateComplaintTypeController updateComplaintTypeController;
    @Override
    protected UpdateComplaintTypeController initController() {
        initMocks(this);
        return updateComplaintTypeController;
    }

    @Before
    public void before() throws Exception {
        final Department department = new DepartmentBuilder().withId(1l).withCode("DC").build();
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
        code = "existing";
        ComplaintType complaintType = new ComplaintType();
        complaintType.setDepartment(department);
        complaintType.setCode("existing");
        complaintType.setSlaHours(24);
        when(complaintTypeService.findByCode(code)).thenReturn(complaintType);
    }

    @Test
    public void shouldRetrieveExistingComplaintTypeForUpdate() throws Exception {
        Department department1 = new DepartmentBuilder().withName("dep1").build();
        Department department2 = new DepartmentBuilder().withName("dep2").build();
        List<Department> departments = Arrays.asList(department1, department2);

        ComplaintType complaintType = new ComplaintType();
        complaintType.setCode("existing");
        complaintType.setSlaHours(24);
        code = "existing";
        when(departmentService.getAllDepartments()).thenReturn(departments);

        MvcResult result = this.mockMvc.perform(get("/complainttype/update/"+code))
                .andExpect(view().name("complaint-type"))
                .andExpect(model().attribute("departments", departments))
                .andExpect(model().attributeExists("complaintType"))
                .andReturn();

        ComplaintType existingComplaintType = (ComplaintType) result.getModelAndView().getModelMap().get("complaintType");
        assertEquals(complaintType.getName(), existingComplaintType.getName());
    }

    @Test
    public void shouldUpdateComplaintType() throws Exception {
        ComplaintType complaintType = new ComplaintType();
        complaintType.setCode("existing");
        complaintType.setSlaHours(24);
        when(complaintTypeService.updateComplaintType(any(ComplaintType.class))).thenReturn(complaintType);
        this.mockMvc.perform(post("/complainttype/update/existing")
                .param("name", "existing-complaint-type").param("code", "Test"))
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:/complainttype/view/existing"));

        ArgumentCaptor<ComplaintType> argumentCaptor = ArgumentCaptor.forClass(ComplaintType.class);
        verify(complaintTypeService).updateComplaintType(argumentCaptor.capture());

        ComplaintType createdComplaintType = argumentCaptor.getValue();
        assertEquals("existing-complaint-type", createdComplaintType.getName());
    }

    @Test
    public void shouldValidateComplaintTypeWhileUpdating() throws Exception {
        this.mockMvc.perform(post("/complainttype/update/existing")
                .param("name","").param("code", "Test"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("complaintType", "name"))
                .andExpect(model().errorCount(1))
                .andExpect(view().name("complaint-type"));

        verify(complaintTypeService, never()).updateComplaintType(any(ComplaintType.class));
    }
}