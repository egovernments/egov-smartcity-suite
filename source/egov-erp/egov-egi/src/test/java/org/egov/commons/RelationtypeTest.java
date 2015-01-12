package org.egov.commons;

/**
 * @author manikanta
 *
 */

import org.egov.infstr.junit.EgovHibernateTest;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.Session;
import org.junit.Ignore;

@Ignore
public class RelationtypeTest extends EgovHibernateTest {

	private EgiObjectFactory oFactory;
	private Session session;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		this.session = HibernateUtil.getCurrentSession();
		this.oFactory = new EgiObjectFactory(this.session);

	}

	/*
	 * public void testForValidRelationType() {
	 * 
	 * int before = count(Relationtype.class); Relationtype reltType =
	 * oFactory.createRelationtype("eGov", "1"); System.out.println("id is " +
	 * reltType.getId()); assertEquals(before + 1, count(Relationtype.class)); }
	 */

	public void testForUniqueCode() {
		try {
			this.oFactory.createRelationtype("eGov", "1");
			this.oFactory.createRelationtype("eGovUser", "1");
			this.session.flush();
			fail();
		} catch (final Exception e) {
			assertNotNull(
					"Unique Index 'REL_CODE' doesnot exists in table Relation ",
					e);
		}
	}

	public void testForUniqueName() {
		try {
			this.oFactory.createRelationtype("eGov", "1");
			this.oFactory.createRelationtype("eGov", "2");
			this.session.flush();
			fail();
		} catch (final Exception e) {
			assertNotNull(
					"Unique Index 'REL_NAME' does not exists in table 'Relation'",
					e);
		}

	}

	public void testForNameLength() {
		final String name = "RELATIONTYP";
		try {
			this.oFactory.createRelationtype(name, "1");
			this.session.flush();
			fail();
		} catch (final Exception e) {
			assertNotNull(
					"column  'Name' length is more than 10 in table 'Relation'",
					e);
		}
	}

}
