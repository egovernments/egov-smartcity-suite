/**
 * 
 */
package org.egov.commons.dao;

import org.apache.log4j.Logger;
import org.egov.commons.Accountdetailtype;
import org.egov.exceptions.EGOVException;
import org.egov.infstr.junit.EgovHibernateTest;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.SessionFactory;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.HibernateException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author msahoo
 * 
 */
@Ignore
public class AccountdetailtypeHibernateDAOTest extends EgovHibernateTest {
	private static final Logger LOGGER = Logger
			.getLogger(AccountdetailtypeHibernateDAOTest.class);
	private static PersistenceService<Accountdetailtype, Long> accountdetailtypeService;
	AccountdetailtypeHibernateDAO detailTypeDao;

	/**
	 * @throws java.lang.Exception
	 */
	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		this.detailTypeDao = new AccountdetailtypeHibernateDAO(
				Accountdetailtype.class, HibernateUtil.getCurrentSession());
	}

	@Test
	public void testGetDetailtypeforObject() throws EGOVException {
		final RelationTest relationTest = new RelationTest();
		createAccountdetailtype("RelationTest", "RelationTest", "RelationTest",
				"RelationTest_id");
		final Integer detailTypeId = this.detailTypeDao
				.getDetailtypeforObject(relationTest);
		assertNotNull(detailTypeId);
		System.out
				.println("Object Relation detailTypeId is := " + detailTypeId);
	}

	@Test
	public void testGetDetailtypeByPassingNullObj() throws EGOVException {
		try {
			this.detailTypeDao.getDetailtypeforObject(null);
			fail("should raise an  EGOVException");
		} catch (final EGOVException e) {
			assertEquals("The object supplied is null", e.getMessage());
		}

	}

	@Test
	public void testGetDetailtypeforObjectChkException() throws EGOVException {

		final Integer detailType = this.detailTypeDao
				.getDetailtypeforObject(new AccountdetailtypeHibernateDAOTest());
		assertNull(detailType);

	}

	protected Accountdetailtype createAccountdetailtype(final String name,
			final String desc, final String tableName,
			final String attributeName) throws HibernateException {
		final Accountdetailtype accountdetailtype = new Accountdetailtype();
		accountdetailtypeService = new PersistenceService<Accountdetailtype, Long>();
		accountdetailtypeService.setSessionFactory(new SessionFactory());
		accountdetailtypeService.setType(Accountdetailtype.class);
		accountdetailtype.setName(name);
		accountdetailtype.setDescription(desc);
		accountdetailtype.setTablename(tableName);
		accountdetailtype.setColumnname("id");
		accountdetailtype.setAttributename(attributeName);
		accountdetailtype.setNbroflevels(new BigDecimal(1));
		accountdetailtype.setIsactive(true);
		accountdetailtype.setCreated(new Date());
		accountdetailtype.setModifiedby(new Long(1));
		accountdetailtype.setLastmodified(new Date());
		accountdetailtypeService.persist(accountdetailtype);
		return accountdetailtype;
	}
}

class RelationTest {
	private String tablename = "RelationTest";

	public String getTablename() {
		return this.tablename;
	}

	public void setTablename(final String tablename) {
		this.tablename = tablename;
	}

}
