package org.egov.lib.rrbac.model;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EntityTest {

	@Test
	public void testGetId() {
		final Entity entity = new Entity();
		entity.setName("name");
		try {
			entity.compareTo(entity);
			entity.compareTo(new Entity());
		} catch (final ClassCastException e) {
			assertTrue(true);
		}
		entity.equals(null);
		entity.equals(entity);
		entity.equals(new Object());
		final Entity entity2 = new Entity();
		entity2.setName("namdse");
		entity.equals(entity2);
		entity2.setName("name");
		entity.equals(entity2);
		entity.hashCode();

		entity.setId(1);
		entity.setUpdatedTime(null);

		entity.getId();
		entity.getName();
		entity.getUpdatedTime();

		assertTrue(true);
	}

}
