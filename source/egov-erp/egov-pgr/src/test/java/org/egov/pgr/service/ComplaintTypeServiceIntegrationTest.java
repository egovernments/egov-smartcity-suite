package org.egov.pgr.service;

import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.dept.ejb.api.DepartmentService;
import org.egov.pgr.PGRAbstractSpringIntegrationTest;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.entity.ComplaintTypeBuilder;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Ignore
public class ComplaintTypeServiceIntegrationTest extends PGRAbstractSpringIntegrationTest {

    @Autowired
    private ComplaintTypeService complaintTypeService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void shouldCreateComplaintType() throws ClassNotFoundException {
        Department department = departmentService.getDepartmentByCode("NB");
        ComplaintType complaintType = new ComplaintTypeBuilder()
                .withName("test-complaint-type1")
                .withDepartment(department)
                .build();

        complaintTypeService.createComplaintType(complaintType);

        List<Map<String, Object>> results = jdbcTemplate.queryForList("select * from pgr_complainttype");
        Optional<Map<String, Object>> createdRow = results.stream().filter(row -> row.containsValue(complaintType.getName())).findFirst();
        assertTrue(createdRow.isPresent());

        int expectedDeptId = department.getId();
        int actualDeptId = ((BigDecimal) createdRow.get().get("dept_id")).intValue();
        assertEquals(expectedDeptId, actualDeptId);
    }

    @Test
    public void shouldFindComplaintTypeById() {
        Department department = departmentService.getDepartmentByCode("NB");

        ComplaintType complaintType = new ComplaintTypeBuilder()
                .withName("test-complaint-type2")
                .withDepartment(department)
                .build();
        complaintTypeService.createComplaintType(complaintType);

        ComplaintType existingComplaintType = complaintTypeService.findBy(complaintType.getId());
        assertEquals(complaintType.getName(), existingComplaintType.getName());
        assertEquals(complaintType.getDepartment(), existingComplaintType.getDepartment());
    }
}