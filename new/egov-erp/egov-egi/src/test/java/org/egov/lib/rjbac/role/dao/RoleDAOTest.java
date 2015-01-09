package org.egov.lib.rjbac.role.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import org.egov.exceptions.DuplicateElementException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rjbac.role.Role;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

public class RoleDAOTest {

	private Session session;
	private SessionFactory sessionFactory;
	private Query query;
	private Role role;
	private RoleDAO roleDao;

	@Before
	public void runBefore() throws Exception {
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
		this.roleDao = new RoleDAO();
	}

	@Test
	public void testCreateRole() throws Exception {
		when(this.query.uniqueResult()).thenReturn(this.role);
		try {
			this.roleDao.createRole(this.role);
		} catch (final DuplicateElementException e) {
			assertTrue(true);
		}
		when(this.query.uniqueResult()).thenReturn(null);
		Mockito.doThrow(new HibernateException("")).when(this.session)
				.save(this.role);
		try {
			this.roleDao.createRole(this.role);
		} catch (final EGOVRuntimeException e) {
			assertTrue(true);
		}
		runBefore();
		when(this.query.uniqueResult()).thenReturn(null);
		this.roleDao.createRole(this.role);
		assertTrue(true);
	}

	@Test
	public void testUpdateRole() {
		this.roleDao.updateRole(this.role);
		Mockito.doThrow(new HibernateException("")).when(this.session)
				.update(this.role);
		try {
			this.roleDao.updateRole(this.role);
		} catch (final EGOVRuntimeException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetRoleByID() {
		when(this.session.load(Matchers.any(Class.class), Matchers.anyInt()))
				.thenReturn(this.role);
		assertNotNull(this.roleDao.getRoleByID(1));
		when(this.session.load(Matchers.any(Class.class), Matchers.anyInt()))
				.thenThrow(new HibernateException(""));
		try {
			this.roleDao.getRoleByID(1);
		} catch (final EGOVRuntimeException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetAllRoles() {
		assertNotNull(this.roleDao.getAllRoles());
		when(this.query.list()).thenThrow(new HibernateException(""));
		try {
			this.roleDao.getAllRoles();
		} catch (final EGOVRuntimeException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetAllTopLevelRoles() {
		assertNotNull(this.roleDao.getAllTopLevelRoles());
		when(this.query.list()).thenThrow(new HibernateException(""));
		try {
			this.roleDao.getAllTopLevelRoles();
		} catch (final EGOVRuntimeException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testRemoveRole() {
		this.roleDao.removeRole(this.role);
		Mockito.doThrow(new HibernateException("")).when(this.session)
				.delete(this.role);
		try {
			this.roleDao.removeRole(this.role);
		} catch (final EGOVRuntimeException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetRoleByRoleName() {
		when(this.query.uniqueResult()).thenReturn(this.role);
		assertNotNull(this.roleDao.getRoleByRoleName("name"));
		when(this.query.uniqueResult()).thenThrow(new HibernateException(""));
		try {
			this.roleDao.getRoleByRoleName("name");
		} catch (final EGOVRuntimeException e) {
			assertTrue(true);
		}
	}

}
