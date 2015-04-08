package org.egov.infra.admin.master.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import org.egov.builder.entities.DepartmentBuilder;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.repository.DepartmentRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

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
        verify(departmentRepository).findByName("test");
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
    public void shouldGetDepartmentById() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
            IllegalAccessException {
        final Field idField = department.getClass().getSuperclass().getSuperclass().getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(department, 1l);
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
        verify(departmentRepository).findAll();
        assertEquals(list.size(), 2);
    }
}
