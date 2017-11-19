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
package org.egov.infra.admin.master.service;

import org.egov.builder.entities.DepartmentBuilder;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.repository.DepartmentRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author subhash
 */
public class DepartmentServiceTest {
    @Mock
    DepartmentRepository departmentRepository;
    DepartmentService departmentService;
    Department department;

    @Before
    public void before() {
        initMocks(this);
        departmentService = new DepartmentService(departmentRepository);
        department = new DepartmentBuilder().withName("test").withCode("test").build();
    }

    @Test
    public void shouldCreateDepartment() {
        departmentService.createDepartment(department);
        verify(departmentRepository).save(department);
    }

    @Test
    public void shouldGetDepartmentByName() {
        when(departmentService.getDepartmentByName(anyString())).thenReturn(department);
        Department expectedDepartment = departmentService.getDepartmentByName("test");
        verify(departmentRepository).findByNameUpperCase("test".toUpperCase());
        assertTrue(department.equals(expectedDepartment));
    }

    @Test
    public void shouldGetDepartmentByCode() {
        when(departmentService.getDepartmentByCode(anyString())).thenReturn(department);
        Department expectedDepartment = departmentService.getDepartmentByCode("test");
        verify(departmentRepository).findByCode("test");
        assertTrue(department.equals(expectedDepartment));
    }

    @Test
    public void shouldGetDepartmentById()  {
        department.setId(1l);
        when(departmentService.getDepartmentById(anyLong())).thenReturn(department);
        Department expectedDepartment = departmentService.getDepartmentById(1l);
        verify(departmentRepository).findOne(1l);
        assertEquals(expectedDepartment, department);
    }

    @Test
    public void shouldGetAllDepartments() {
        Department department1 = new DepartmentBuilder().withName("test1").withCode("test1").build();
        Department department2 = new DepartmentBuilder().withName("test2").withCode("test2").build();
        when(departmentService.getAllDepartments()).thenReturn(Arrays.asList(department1, department2));
        List<Department> list = departmentService.getAllDepartments();
        verify(departmentRepository).findAll(new Sort(Sort.Direction.ASC, "name"));
        assertEquals(list.size(), 2);
    }
}
