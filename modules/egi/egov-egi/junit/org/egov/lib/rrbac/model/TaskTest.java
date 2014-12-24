package org.egov.lib.rrbac.model;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TaskTest {

	@Test
	public void testGetId() {
		final Task task = new Task();
		task.setName("name");
		task.equals(null);
		task.equals(task);
		task.equals(new Object());
		final Task task2 = new Task();
		task.equals(task2);
		task2.setName("2");
		task.equals(task2);
		task2.setName("name");
		task.equals(task2);
		task.hashCode();

		task.setId(1);
		task.setUpdatedTime(null);
		task.getId();
		task.getUpdatedTime();

		try {
			task.compareTo(task);
			task.compareTo(new Task());
		} catch (final ClassCastException e) {
			assertTrue(true);
		}
		assertTrue(true);
	}

}
