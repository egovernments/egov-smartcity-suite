package org.egov.lib.rrbac.authrule;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;

import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryType;
import org.egov.lib.admbndry.HeirarchyType;
import org.junit.Test;
import org.mockito.Mockito;

public class RulesDefinitionTest {

	@Test
	public void testJurisCompare() {
		final RulesDefinition ruleDefinition = new RulesDefinition();
		final Boundary userBoundary = Mockito.mock(Boundary.class);
		final Boundary objectBoundary = Mockito.mock(Boundary.class);
		final BoundaryType boundaryType = Mockito.mock(BoundaryType.class);
		final HeirarchyType heirarchyType = Mockito.mock(HeirarchyType.class);
		Mockito.when(heirarchyType.getCode()).thenReturn("ADMIN");
		Mockito.when(boundaryType.getHeirarchyType()).thenReturn(heirarchyType);
		Mockito.when(boundaryType.getHeirarchy()).thenReturn((short) 1);
		Mockito.when(userBoundary.getBoundaryType()).thenReturn(boundaryType);

		final BoundaryType boundaryType1 = Mockito.mock(BoundaryType.class);
		final HeirarchyType heirarchyType1 = Mockito.mock(HeirarchyType.class);
		Mockito.when(heirarchyType1.getCode()).thenReturn("ADMINN");
		Mockito.when(boundaryType1.getHeirarchyType()).thenReturn(
				heirarchyType1);
		Mockito.when(boundaryType1.getHeirarchy()).thenReturn((short) 2);
		Mockito.when(objectBoundary.getBoundaryType())
				.thenReturn(boundaryType1);

		boolean condition = ruleDefinition.jurisCompare(userBoundary,
				objectBoundary);
		assertFalse(condition);

		Mockito.when(heirarchyType1.getCode()).thenReturn("ADMIN");
		condition = ruleDefinition.jurisCompare(userBoundary, objectBoundary);
		assertTrue(condition);
		condition = ruleDefinition.jurisCompare(objectBoundary, objectBoundary);
		assertTrue(condition);
		Mockito.when(boundaryType.getHeirarchy()).thenReturn((short) 2);
		condition = ruleDefinition.jurisCompare(userBoundary, objectBoundary);
		assertFalse(condition);
		Mockito.when(boundaryType1.getHeirarchy()).thenReturn((short) 3);
		HashSet<Boundary> childrens = new HashSet<Boundary>();
		Mockito.when(userBoundary.getChildren()).thenReturn(childrens);
		condition = ruleDefinition.jurisCompare(userBoundary, objectBoundary);
		assertFalse(condition);
		childrens.add(objectBoundary);
		Mockito.when(userBoundary.getChildren()).thenReturn(childrens);
		condition = ruleDefinition.jurisCompare(userBoundary, objectBoundary);
		assertTrue(condition);

		childrens = new HashSet<Boundary>();
		childrens.add(objectBoundary);
		Mockito.when(objectBoundary.getChildren()).thenReturn(childrens);
		Mockito.when(userBoundary.getChildren()).thenReturn(childrens);
		Mockito.when(boundaryType1.getHeirarchy()).thenReturn((short) 4);
		condition = ruleDefinition.jurisCompare(userBoundary, objectBoundary);
		assertTrue(condition);

	}

}
