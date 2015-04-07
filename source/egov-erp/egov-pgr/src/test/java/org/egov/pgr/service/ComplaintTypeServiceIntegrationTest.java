package org.egov.pgr.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.pgr.PGRAbstractSpringIntegrationTest;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.entity.ComplaintTypeBuilder;
import org.egov.search.service.IndexService;
import org.junit.Before;
import org.junit.Test;
import org.kubek2k.springockito.annotations.ReplaceWithMock;
import org.kubek2k.springockito.annotations.SpringockitoContextLoader;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(
        loader = SpringockitoContextLoader.class,
        locations = {
                "classpath*:config/spring/applicationContext-properties.xml",
                "classpath*:config/spring/test-applicationContext-hibernate.xml",
                "classpath*:config/spring/applicationContext-search.xml",
                "classpath*:config/spring/applicationContext-egi.xml",
                "classpath*:config/spring/applicationContext-pgr.xml"
        })
public class ComplaintTypeServiceIntegrationTest extends PGRAbstractSpringIntegrationTest {

    @Autowired
    private ComplaintTypeService complaintTypeService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PersistenceContext
    EntityManager entityManager;
    
    @Autowired
    @ReplaceWithMock
    private IndexService indexService;
    private Department department;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        department = departmentService.getDepartmentByCode("HD");
    }

    @Test
    public void shouldCreateComplaintType() throws ClassNotFoundException {
        ComplaintType complaintType = new ComplaintTypeBuilder()
                .withName("test-complaint-type1")
                .withDepartment(department)
                .build();

        complaintTypeService.createComplaintType(complaintType);

        List<ComplaintType> results = entityManager.createQuery("SELECT ct FROM ComplaintType ct").getResultList();
        Optional<ComplaintType> createdRow = results.stream().filter(row -> row.getDepartment().getCode().equals("HD")).findFirst();
        assertTrue(createdRow.isPresent());

        int expectedDeptId = department.getId().intValue();
        int actualDeptId = createdRow.get().getDepartment().getId().intValue();
        assertEquals(expectedDeptId, actualDeptId);
    }

    @Test
    public void shouldFindComplaintTypeById() {
        ComplaintType complaintType = new ComplaintTypeBuilder()
                .withName("test-complaint-type2")
                .withDepartment(department)
                .build();
        complaintTypeService.createComplaintType(complaintType);

        ComplaintType existingComplaintType = complaintTypeService.findBy(complaintType.getId());
        assertEquals(complaintType.getName(), existingComplaintType.getName());
        assertEquals(complaintType.getDepartment(), existingComplaintType.getDepartment());
    }


    @Test
    public void shouldLoadAllComplaintTypes() {
        ComplaintType complaintType1 = new ComplaintTypeBuilder().withName("ctype1").withDepartment(department).build();
        ComplaintType complaintType2 = new ComplaintTypeBuilder().withName("ctype2").withDepartment(department).build();
        ComplaintType complaintType3 = new ComplaintTypeBuilder().withName("ctype3").withDepartment(department).build();

        complaintTypeService.createComplaintType(complaintType1);
        complaintTypeService.createComplaintType(complaintType2);
        complaintTypeService.createComplaintType(complaintType3);

        List<ComplaintType> complaintTypes = complaintTypeService.findAll();

        assertTrue(collectionContains(complaintTypes, "ctype1"));
        assertTrue(collectionContains(complaintTypes, "ctype2"));
        assertTrue(collectionContains(complaintTypes, "ctype3"));
    }

    private boolean collectionContains(List<ComplaintType> complaintTypes, String name) {
        return complaintTypes.stream().anyMatch(complaintType -> complaintType.getName().equals(name));
    }
}