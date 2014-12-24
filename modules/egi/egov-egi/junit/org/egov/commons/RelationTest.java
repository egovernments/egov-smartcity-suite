package org.egov.commons;

import java.util.Arrays;

import org.egov.infstr.junit.EgovHibernateTest;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Session;

public class RelationTest extends EgovHibernateTest {
	private EgiObjectFactory oFactory;
	private Session session;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		this.session = HibernateUtil.getCurrentSession();
		this.oFactory = new EgiObjectFactory(this.session);
	}

	public void testValidRelation() {
		this.oFactory.createRelation(1, "egov", "aaa", "bbb", "egov1");
		assertTrue(true);

	}

	public void testAddres1and2Length() {
		final char[] addr1Arr = new char[150];
		final char[] addr2Arr = new char[150];
		Arrays.fill(addr1Arr, 'a');
		Arrays.fill(addr2Arr, 'b');
		final String addr1 = new String(addr1Arr);
		final String addr2 = new String(addr2Arr);
		final int before = count(Relation.class);
		this.oFactory.createRelation(1, "egov", addr1, addr2, "egov1");
		assertEquals(before + 1, count(Relation.class));

	}

	public void testLargerAddres1and2Relation() {
		final char[] addr1Arr = new char[151];
		final char[] addr2Arr = new char[151];
		Arrays.fill(addr1Arr, 'a');
		Arrays.fill(addr2Arr, 'b');
		final String addr1 = new String(addr1Arr);
		final String addr2 = new String(addr2Arr);
		try {
			final int before = count(Relation.class);
			this.oFactory.createRelation(1, "egov", addr1, addr2, "egov1");
			assertEquals(before + 1, count(Relation.class));
			fail();
		} catch (final Exception e) {
			assertNotNull(
					"'Address1' and 'Adress2' are more than 150 chracters in table Relation ",
					e);
		}

	}
}
