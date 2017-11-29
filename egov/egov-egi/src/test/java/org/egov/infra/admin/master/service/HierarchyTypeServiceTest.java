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

import org.egov.infra.admin.master.entity.HierarchyType;
import org.egov.infra.admin.master.repository.HierarchyTypeRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class HierarchyTypeServiceTest {

    @Mock
    private HierarchyTypeRepository hierarchyTypeRepository;

    private HierarchyTypeService hierarchyTypeService;

    private HierarchyType expectedHierarchyType;

    private HierarchyType expectedHierarchyType1;
    private HierarchyType expectedHierarchyType2;

    @Before
    public void before() {
        initMocks(this);

        hierarchyTypeService = new HierarchyTypeService(hierarchyTypeRepository);
        AspectJProxyFactory factory = new AspectJProxyFactory(hierarchyTypeService);
        hierarchyTypeService = factory.getProxy();
        expectedHierarchyType = new HierarchyType();
        expectedHierarchyType.setName("Test");
        expectedHierarchyType.setCode("TEST");

        expectedHierarchyType1 = new HierarchyType();
        expectedHierarchyType1.setName("Administration");
        expectedHierarchyType1.setCode("ADMIN");

        expectedHierarchyType2 = new HierarchyType();
        expectedHierarchyType2.setName("Location");
        expectedHierarchyType2.setCode("LOCATION");
    }

    @Test
    public void testCreateHierarchyType() {
        HierarchyType hierarchyType = new HierarchyType();
        hierarchyType.setName("Test One");
        hierarchyType.setCode("TEST_ONE");
        hierarchyTypeService.createHierarchyType(hierarchyType);
        verify(hierarchyTypeRepository).save(hierarchyType);
    }

    @Test
    public void testGetHierarchyTypeByName() {
        when(hierarchyTypeService.getHierarchyTypeByName("Test")).thenReturn(expectedHierarchyType);
        HierarchyType hierarchyType = hierarchyTypeService.getHierarchyTypeByName("Test");
        assertTrue(hierarchyType.getName().equalsIgnoreCase(expectedHierarchyType.getName()));
    }

    @Test
    public void testGetAllHierarchyTypes() {
        when(hierarchyTypeService.getAllHierarchyTypes()).thenReturn(
                Arrays.asList(expectedHierarchyType1, expectedHierarchyType2));
        List<HierarchyType> hierarchyTypes = hierarchyTypeService.getAllHierarchyTypes();
        assertEquals(2, hierarchyTypes.size());
        verify(hierarchyTypeRepository).findAll();
    }
}
