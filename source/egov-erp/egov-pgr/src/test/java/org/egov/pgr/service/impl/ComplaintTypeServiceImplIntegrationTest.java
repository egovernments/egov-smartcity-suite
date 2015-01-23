package org.egov.pgr.service.impl;

import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.lib.rjbac.dept.ejb.api.DepartmentService;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.service.ComplaintTypeService;
import org.egov.pgr.service.PGRAbstractSpringIntegrationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ComplaintTypeServiceImplIntegrationTest extends PGRAbstractSpringIntegrationTest {

    @Autowired
    private ComplaintTypeService complaintTypeService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void shouldCreateComplaintType() throws ClassNotFoundException {
        Department department = departmentService.getDepartmentByCode("NB");

        ComplaintType complaintType = new ComplaintType();
        complaintType.setName("test-complaint-type1");
        complaintType.setDepartment((DepartmentImpl) department);

        complaintTypeService.createComplaintType(complaintType);

        List<Map<String, Object>> results = jdbcTemplate.queryForList("select * from pgr_complainttype");
        Optional<Map<String, Object>> createdRow = results.stream().filter(row -> row.containsValue(complaintType.getName())).findFirst();
        assertTrue(createdRow.isPresent());

        int expectedDeptId = department.getId();
        int actualDeptId = ((BigDecimal) createdRow.get().get("dept_id")).intValue();
        assertEquals(expectedDeptId, actualDeptId);
    }
}