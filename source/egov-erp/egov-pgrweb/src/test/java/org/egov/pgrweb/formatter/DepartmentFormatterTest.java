package org.egov.pgrweb.formatter;

import org.egov.builder.entities.DepartmentBuilder;
import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.lib.rjbac.dept.ejb.api.DepartmentService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.text.ParseException;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DepartmentFormatterTest {

    private DepartmentFormatter departmentFormatter;
    @Mock
    private DepartmentService departmentService;

    @Before
    public void before() {
        initMocks(this);

        departmentFormatter = new DepartmentFormatter(departmentService);
    }

    @Test
    public void shouldConvertDepartmentIdToDepartment() throws ParseException {
        String departmentCode = "AS";
        Department department = new DepartmentBuilder().withName("AS Department").withCode(departmentCode).build();

        when(departmentService.getDepartmentByCode(departmentCode)).thenReturn(department);

        DepartmentImpl actualDepartment = departmentFormatter.parse(departmentCode, Locale.ENGLISH);

        assertNotNull(actualDepartment);
        assertEquals(department, actualDepartment);
        verify(departmentService).getDepartmentByCode(departmentCode);
    }
}