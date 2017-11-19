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
package org.egov.egi.web.controller.admin.masters.department;

import org.egov.egi.web.controller.AbstractContextControllerTest;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.infra.web.controller.admin.masters.department.CreateDepartmentController;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;

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

/**
 * This Class is used to test Create Department Controller
 * 
 * @author subhash
 */
public class CreateDepartmentControllerTest extends
		AbstractContextControllerTest<CreateDepartmentController> {

	@Mock
	private DepartmentService departmentService;

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
		this.mockMvc.perform(get("/department/create")).andExpect(view().name("department-form"))
				.andExpect(status().isOk());
	}

	@Test
	public void shouldCreateNewDepartment() throws Exception {
		this.mockMvc
				.perform(
						post("/department/create").param("name", "testing")
								.param("code", "testing")).andExpect(model().hasNoErrors())
				.andExpect(redirectedUrl("/department/view/testing"));
		ArgumentCaptor<Department> argumentCaptor = ArgumentCaptor.forClass(Department.class);
		verify(departmentService).createDepartment(argumentCaptor.capture());
		Department createdDeprtment = argumentCaptor.getValue();
		assertTrue(createdDeprtment.isNew());
		assertEquals(createdDeprtment.getName(), "testing");
		assertEquals(createdDeprtment.getCode(), "testing");
	}

	@Test
	public void shouldValidateWhileCreating() throws Exception {
		this.mockMvc.perform(post("/department/create")).andExpect(model().hasErrors())
				.andExpect(model().attributeHasFieldErrors("department", "code"))
				.andExpect(view().name("department-form"));
		verify(departmentService, never()).createDepartment(any(Department.class));
	}
}
