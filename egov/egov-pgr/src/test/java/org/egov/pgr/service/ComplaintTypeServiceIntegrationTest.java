/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
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
 */
package org.egov.pgr.service;

import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.service.DepartmentService;
import org.egov.pgr.PGRAbstractSpringIntegrationTest;
import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.entity.ComplaintTypeBuilder;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.kubek2k.springockito.annotations.SpringockitoContextLoader;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@ContextConfiguration(
        loader = SpringockitoContextLoader.class,
        locations = {
                "classpath*:config/spring/applicationContext-properties.xml",
                "classpath*:config/spring/test-applicationContext-hibernate.xml",
                "classpath*:config/spring/applicationContext-egi.xml",
                "classpath*:config/spring/applicationContext-pgr.xml"
        })
@Ignore
public class ComplaintTypeServiceIntegrationTest extends PGRAbstractSpringIntegrationTest {

    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    private ComplaintTypeService complaintTypeService;
    @Autowired
    private DepartmentService departmentService;
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
        complaintType.setCode("test-code");
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

        complaintType1.setCode("code1");
        complaintType2.setCode("code2");
        complaintType3.setCode("code3");
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

