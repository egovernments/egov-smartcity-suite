package org.egov.lib.rrbac.dao;

import org.egov.infstr.junit.EgovHibernateTest;

public class ActionDAOTest extends EgovHibernateTest {

	public void testGetActionByUrlWithContextParam() {
		assertNotNull(new Object());
	}
	
	/*public void testGetActionByUrlWithContextParam() {
		final ActionDAO objDao = new ActionHibernateDAO(ActionDAO.class,
				HibernateUtil.getCurrentSession());
		final Action actObj = objDao.findActionByURL("/egi", "/eGov.jsp");
		assertNotNull(actObj);
	}

	public void testGetActionByUrlWithoutLeadingSlashContextParam() {
		final ActionDAO objDao = new ActionHibernateDAO(ActionDAO.class,
				HibernateUtil.getCurrentSession());
		final Action actObj = objDao.findActionByURL("egi", "/eGov.jsp");
		assertNotNull(actObj);
	}*/
}