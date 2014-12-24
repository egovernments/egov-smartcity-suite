package org.egov.lib.rjbac.role.ejb.server;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rjbac.role.Role;
import org.egov.lib.rjbac.role.ejb.api.RoleService;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;

public class RoleManagerBeanTest {

	private RoleService roleManager;
	private Session session;
	private SessionFactory sessionFactory;
	private Query query;
	private Role role;

	@Before
	public void runBefore() throws Exception {
		this.roleManager = new RoleServiceImpl();
		this.role = mock(Role.class);
		this.session = mock(Session.class);
		this.sessionFactory = mock(SessionFactory.class);
		when(this.session.getSessionFactory()).thenReturn(this.sessionFactory);
		this.query = mock(Query.class);
		when(this.session.createQuery(Matchers.anyString())).thenReturn(
				this.query);
		when(this.session.isOpen()).thenReturn(true);
		final Field f = HibernateUtil.class.getDeclaredField("threadSession");
		f.setAccessible(true);
		final ThreadLocal<Session> sessionThread = new ThreadLocal<Session>();
		sessionThread.set(this.session);
		f.set(new HibernateUtil(), sessionThread);
	}

	@Test
	public void testCreateRole() throws Exception {
		when(this.query.uniqueResult()).thenReturn(null);
		this.roleManager.createRole(this.role);
		assertTrue(true);
	}

	@Test
	public void testUpdateRole() {
		this.roleManager.updateRole(this.role);
		assertTrue(true);
	}

	@Test
	public void testGetRoleByRoleName() {
		when(this.query.uniqueResult()).thenReturn(this.role);
		assertNotNull(this.roleManager.getRoleByRoleName("roleName"));
	}

	@Test
	public void testRemoveRole() {
		this.roleManager.removeRole(this.role);
		assertTrue(true);
	}

	@Test
	public void testGetAllRoles() {
		assertNotNull(this.roleManager.getAllRoles());
	}

	@Test
	public void testGetAllTopLevelRoles() {
		assertNotNull(this.roleManager.getAllTopLevelRoles());
	}

	@Test
	public void testGetRole() {
		when(this.session.load(Matchers.any(Class.class), Matchers.anyInt()))
				.thenReturn(this.role);
		assertNotNull(this.roleManager.getRole(1));
	}

}
