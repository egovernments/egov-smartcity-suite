package org.egov.lib.security.terminal.client;

import static org.junit.Assert.assertEquals;

import org.egov.lib.security.terminal.model.Location;
import org.junit.Test;

public class ObjComparatorTest {

	private final ObjComparator objComparator = new ObjComparator();

	@Test
	public void testCompare() {
		final Location loc = new Location();
		final Location loc2 = new Location();
		int i = this.objComparator.compare(loc, loc2);
		assertEquals(0, i);
		loc.setName("1");
		loc2.setName("1");
		i = this.objComparator.compare(loc, loc2);
		assertEquals(0, i);
		loc.setName("2");
		loc2.setName("1");
		i = this.objComparator.compare(loc, loc2);
		assertEquals(1, i);
		loc.setName("1");
		loc2.setName("2");
		i = this.objComparator.compare(loc, loc2);
		assertEquals(-1, i);
		loc.setName("12");
		loc2.setName("21");
		i = this.objComparator.compare(loc, loc2);
		assertEquals(-1, i);
		loc.setName(null);
		loc2.setName("-1");
		i = this.objComparator.compare(loc, loc2);
		assertEquals(45, i);
		loc.setName("-1");
		loc2.setName(null);
		i = this.objComparator.compare(loc, loc2);
		assertEquals(-45, i);
		loc.setName("1-1");
		loc2.setName("-1");
		i = this.objComparator.compare(loc, loc2);
		assertEquals(4, i);

	}

}
