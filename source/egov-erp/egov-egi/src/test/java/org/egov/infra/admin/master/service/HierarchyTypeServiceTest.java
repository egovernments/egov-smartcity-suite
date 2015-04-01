package org.egov.infra.admin.master.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;
import java.util.List;

import org.egov.infra.admin.master.entity.HierarchyType;
import org.egov.infra.admin.master.repository.HierarchyTypeRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

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
