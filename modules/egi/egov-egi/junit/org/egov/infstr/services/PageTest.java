package org.egov.infstr.services;

import org.egov.infstr.junit.EgovHibernateTest;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rrbac.model.Action;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.junit.Before;
import org.junit.Test;

public class PageTest extends EgovHibernateTest {
	Page page;

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@Test
	public void testPageWithCriteria() {
		final Criteria c = HibernateUtil.getCurrentSession().createCriteria(
				Action.class);
		this.page = new Page(c, 1, 10);

		assertEquals(10, this.page.getPageSize().intValue());
		assertEquals(10, this.page.getList().size());
	}

	public void testPageWithCriteriaPage2() {
		final Criteria c = HibernateUtil.getCurrentSession().createCriteria(
				Action.class);
		this.page = new Page(c, 2, 10);

		assertEquals(10, this.page.getPageSize().intValue());
		assertEquals(10, this.page.getList().size());
	}

	public void testPageWithCriteriaWithNoPageSize() {
		final Criteria c = HibernateUtil.getCurrentSession().createCriteria(
				Action.class);
		this.page = new Page(c, 2, null);

		assertTrue(this.page.getPageSize().intValue() == -1);
		assertTrue(this.page.getList().size() > 10);
	}

	public void testPageWithCriteriaProjection() {
		final Criteria c = HibernateUtil.getCurrentSession().createCriteria(
				Action.class);

		c.add(Restrictions.isNotNull("url"));
		c.setProjection(Projections.groupProperty("url"));

		this.page = new Page(c, 1, 10);

		assertEquals(10, this.page.getPageSize().intValue());
		assertEquals(10, this.page.getList().size());
	}

	@Test
	public void testPageWithQuery() {
		final Query qry = HibernateUtil.getCurrentSession().createQuery(
				"from org.egov.lib.rrbac.model.Action");
		this.page = new Page(qry, 1, 10);

		assertEquals(10, this.page.getPageSize().intValue());
		assertEquals(10, this.page.getList().size());
	}

	public void testPageWithQueryPage2() {
		final Query qry = HibernateUtil.getCurrentSession().createQuery(
				"from org.egov.lib.rrbac.model.Action");
		this.page = new Page(qry, 2, 10);

		assertEquals(10, this.page.getPageSize().intValue());
		assertEquals(10, this.page.getList().size());
	}

	public void testPageWithQueryNoPageSize() {
		final Query qry = HibernateUtil.getCurrentSession().createQuery(
				"from org.egov.lib.rrbac.model.Action");
		this.page = new Page(qry, 2, null);

		assertTrue(this.page.getPageSize().intValue() == -1);
		assertTrue(this.page.getList().size() > 10);
	}
}
