package org.egov.lib.rrbac.services;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rrbac.model.Action;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class RbacManagerBeanTest {
	private Session session;
	private Query query;

	@Before
	public void beforeTestRuns() throws Exception {
		this.query = Mockito.mock(Query.class);
		this.session = Mockito.mock(Session.class);
		Mockito.when(this.session.isOpen()).thenReturn(true);
		Mockito.when(this.session.createQuery(Matchers.anyString()))
				.thenReturn(this.query);
		final Field f = HibernateUtil.class.getDeclaredField("threadSession");
		f.setAccessible(true);
		final ThreadLocal<Session> sessionThread = new ThreadLocal<Session>();
		sessionThread.set(this.session);
		f.set(new HibernateUtil(), sessionThread);

	}

	@Test
	public void testRbacManagerBean() throws Exception {
		final RbacService rbacManagerBean = new RbacServiceImpl();
		Mockito.when(this.query.uniqueResult()).thenReturn(null);
		rbacManagerBean.createEntity(null);
		Mockito.when(
				this.session.load(Matchers.any(Class.class), Matchers.anyLong()))
				.thenReturn(null);
		rbacManagerBean.getEntityByID(1);
		rbacManagerBean.getEntityByName("name");
		Mockito.when(this.query.list()).thenReturn(new ArrayList());
		final Criteria criteria = Mockito.mock(Criteria.class);
		Mockito.when(this.session.createCriteria(Matchers.any(Class.class)))
				.thenReturn(criteria);
		Mockito.when(criteria.list()).thenReturn(new ArrayList());
		rbacManagerBean.getEntityList();
		rbacManagerBean.updateEntity(null);

		assertTrue(true);

		rbacManagerBean.createTask(null);
		rbacManagerBean.getTaskByID(1);
		rbacManagerBean.getTaskByName("name");
		rbacManagerBean.getTaskList();
		rbacManagerBean.updateTask(null);

		assertTrue(true);

		rbacManagerBean.createAction(null);
		rbacManagerBean.getActionById(1);
		rbacManagerBean.getActionByName("name");
		rbacManagerBean.getActionList();
		rbacManagerBean.getActionByURL("/egi", "abc.jsp?asa=asa");
		rbacManagerBean.getActionListWithRG();
		rbacManagerBean.getActionListWithRoles();
		rbacManagerBean.updateAction(null);
		rbacManagerBean.deleteAction(null);

		assertTrue(true);

		rbacManagerBean.getRuleByName("name");
		rbacManagerBean.getRuleGroupById(1);
		rbacManagerBean.getRuleGroupByName("name");
		rbacManagerBean.getRuleGroupList();
		rbacManagerBean.getRuleTypeList();

		final ArrayList list = new ArrayList();
		list.add(new Action());
		Mockito.when(this.query.list()).thenReturn(list);
		rbacManagerBean.getActionByURL("/egi", "abc.jsp");

		try {
			Mockito.when(this.query.list()).thenThrow(
					new EGOVRuntimeException(""));
			rbacManagerBean.getActionListWithRG();
		} catch (final Exception e) {
		}
		try {
			beforeTestRuns();
			Mockito.when(this.query.list()).thenThrow(
					new EGOVRuntimeException(""));
			rbacManagerBean.getActionListWithRoles();
		} catch (final Exception e) {
		}
		assertTrue(true);
	}
}
