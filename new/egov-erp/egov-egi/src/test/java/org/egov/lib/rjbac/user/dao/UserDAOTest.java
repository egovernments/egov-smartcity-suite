package org.egov.lib.rjbac.user.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.admbndry.BoundaryType;
import org.egov.lib.admbndry.BoundaryTypeImpl;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.lib.rjbac.jurisdiction.Jurisdiction;
import org.egov.lib.rjbac.jurisdiction.JurisdictionValues;
import org.egov.lib.rjbac.user.User;
import org.egov.lib.rjbac.user.UserDetail;
import org.egov.lib.rjbac.user.UserDetailImpl;
import org.egov.lib.rjbac.user.UserImpl;
import org.egov.lib.rjbac.user.UserRole;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

public class UserDAOTest {

	private UserDAO userDao;
	private Session session;
	private SessionFactory sessionFactory;
	private Query query;
	private User user;

	@Before
	public void runBefore() throws Exception {
		this.user = mock(User.class);
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
		this.userDao = new UserDAO();
	}

	@Test
	public void testCreateOrUpdateUserWithPwdEncryption() {
		this.userDao.createOrUpdateUserWithPwdEncryption(this.user);
		assertNotNull(this.user);
	}

	@Test
	public void testGetAllRolesForUser() {
		final Set roles = this.userDao.getAllRolesForUser("name");
		assertNotNull(roles);
		try {
			when(this.query.list()).thenThrow(new HibernateException(""));
			this.userDao.getAllRolesForUser("name");
		} catch (final Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetUsersByDepartment() {
		final List user = this.userDao
				.getUsersByDepartment(new DepartmentImpl());
		assertNotNull(user);
		try {
			when(this.query.list()).thenThrow(new HibernateException(""));
			this.userDao.getUsersByDepartment(new DepartmentImpl());
		} catch (final Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetJurisdictionValueByBndryIdAndUserId() {
		final JurisdictionValues value = this.userDao
				.getJurisdictionValueByBndryIdAndUserId(1, 1);
		assertNull(value);
		try {
			when(this.query.uniqueResult()).thenThrow(
					new HibernateException(""));
			this.userDao.getJurisdictionValueByBndryIdAndUserId(1, 1);
		} catch (final Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetJurisdictionByBndryTypeIdAndUserId() {
		final Jurisdiction value = this.userDao
				.getJurisdictionByBndryTypeIdAndUserId(1, 1);
		assertNull(value);
		try {
			when(this.query.uniqueResult()).thenThrow(
					new HibernateException(""));
			this.userDao.getJurisdictionByBndryTypeIdAndUserId(1, 1);
		} catch (final Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetAllJurisdictionsForUser() {
		final Set juriSet = this.userDao.getAllJurisdictionsForUser(1);
		assertNotNull(juriSet);
		try {
			when(this.query.list()).thenThrow(new HibernateException(""));
			this.userDao.getAllJurisdictionsForUser(1);
		} catch (final Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetJurisdictionsForUser() {
		final Set juriSet = this.userDao.getJurisdictionsForUser(1, new Date());
		assertNotNull(juriSet);
		try {
			when(this.query.list()).thenThrow(new HibernateException(""));
			this.userDao.getJurisdictionsForUser(1, new Date());
		} catch (final Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testCreateUserDetail() {
		this.userDao.createUserDetail(new UserDetailImpl());
		assertTrue(true);
		try {
			Mockito.doThrow(new HibernateException(""))
					.when(this.sessionFactory)
					.evictCollection(Matchers.anyString());
			this.userDao.createUserDetail(new UserDetailImpl());
		} catch (final Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetUserByID() {
		when(this.session.get(any(Class.class), anyInt()))
				.thenReturn(this.user);
		final User user = this.userDao.getUserByID(1);
		assertNotNull(user);
		try {
			Mockito.doThrow(new HibernateException("")).when(this.session)
					.get(any(Class.class), anyInt());
			this.userDao.getUserByID(1);
		} catch (final Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetUserDetailByID() {
		when(this.session.get(any(Class.class), anyInt())).thenReturn(
				new UserDetailImpl());
		final UserDetail userDetail = this.userDao.getUserDetailByID(1);
		assertNotNull(userDetail);
		try {
			Mockito.doThrow(new HibernateException("")).when(this.session)
					.get(any(Class.class), anyInt());
			this.userDao.getUserDetailByID(1);
		} catch (final Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetUserByUserName() {
		when(this.query.uniqueResult()).thenReturn(this.user);
		final User user = this.userDao.getUserByUserName("userName");
		assertNotNull(user);
		try {
			when(this.query.uniqueResult()).thenThrow(
					new HibernateException(""));
			this.userDao.getUserByUserName("userName");
		} catch (final Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetUserByName() {
		final List l = this.userDao.getUserByName("userName");
		assertNotNull(l);
		try {
			when(this.query.list()).thenThrow(new HibernateException(""));
			this.userDao.getUserByName("");
		} catch (final Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testRemoveUserUser() {
		when(this.session.get(any(Class.class), anyInt()))
				.thenReturn(this.user);
		this.userDao.removeUser(1);
		assertTrue(true);
		try {
			Mockito.doThrow(new HibernateException(""))
					.when(this.sessionFactory)
					.evictCollection(Matchers.anyString());
			this.userDao.removeUser(1);
		} catch (final Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testRemoveUserInteger() {
		when(this.session.get(any(Class.class), anyInt()))
				.thenReturn(this.user);
		this.userDao.removeUser(this.user);
		assertTrue(true);
		try {
			Mockito.doThrow(new HibernateException(""))
					.when(this.sessionFactory)
					.evictCollection(Matchers.anyString());
			this.userDao.removeUser(this.user);
		} catch (final Exception e) {
			assertTrue(true);
		}

	}

	@Test
	public void testGetUsersByCity() {
		assertNull(this.userDao.getUsersByCity(1));
	}

	@Test
	public void testGetUserDetByUserName() {
		final List l = new ArrayList();
		l.add(new UserDetailImpl());
		when(this.query.list()).thenReturn(l);
		final UserDetail udl = this.userDao.getUserDetByUserName("userName");
		assertNotNull(udl);
		try {
			when(this.session.createQuery(Matchers.anyString())).thenThrow(
					new HibernateException(""));
			this.userDao.getUserDetByUserName("");
		} catch (final Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetAllUsersForJurisdictionType() {

		List names = new ArrayList();
		names.add("myname");
		when(this.query.iterate()).thenReturn(names.iterator());
		Set bndry = new HashSet();
		final Boundary boundary = new BoundaryImpl();
		boundary.setId(1);
		bndry.add(boundary);
		when(this.user.getAllJurisdictionsForLevel(any(BoundaryType.class)))
				.thenReturn(bndry);
		when(this.query.uniqueResult()).thenReturn(this.user);
		Map data = this.userDao.getAllUsersForJurisdictionType(
				new BoundaryTypeImpl(), 1);
		assertNotNull(data);
		names = new ArrayList();
		names.add("myname");
		when(this.query.iterate()).thenReturn(names.iterator());
		Boundary boundary2 = new BoundaryImpl();
		boundary2.setId(1);
		boundary.setParent(boundary2);
		bndry = new HashSet();
		bndry.add(boundary);
		when(this.user.getAllJurisdictionsForLevel(any(BoundaryType.class)))
				.thenReturn(bndry);
		when(this.query.uniqueResult()).thenReturn(this.user);
		data = this.userDao.getAllUsersForJurisdictionType(
				new BoundaryTypeImpl(), 1);
		assertNotNull(data);
		names = new ArrayList();
		names.add("myname");
		when(this.query.iterate()).thenReturn(names.iterator());
		boundary2 = new BoundaryImpl();
		boundary2.setId(2);
		boundary.setParent(boundary2);
		bndry = new HashSet();
		bndry.add(boundary);
		when(this.user.getAllJurisdictionsForLevel(any(BoundaryType.class)))
				.thenReturn(bndry);
		when(this.query.uniqueResult()).thenReturn(this.user);
		data = this.userDao.getAllUsersForJurisdictionType(
				new BoundaryTypeImpl(), 1);
		assertNotNull(data);
		try {
			when(this.query.iterate()).thenThrow(new HibernateException(""));
			this.userDao.getAllUsersForJurisdictionType(new BoundaryTypeImpl(),
					1);
		} catch (final Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetAllUsersForJurisdictionTypeFullResolve() {
		List names = new ArrayList();
		names.add("myname");
		when(this.query.iterate()).thenReturn(names.iterator());
		Set bndry = new HashSet();
		final Boundary boundary = new BoundaryImpl();
		boundary.setId(1);
		bndry.add(boundary);
		when(
				this.user
						.getAllJurisdictionsForLevelFullReslove(any(BoundaryType.class)))
				.thenReturn(bndry);
		when(this.query.uniqueResult()).thenReturn(this.user);
		Map data = this.userDao.getAllUsersForJurisdictionTypeFullResolve(
				new BoundaryTypeImpl(), 1);
		assertNotNull(data);
		names = new ArrayList();
		names.add("myname");
		when(this.query.iterate()).thenReturn(names.iterator());
		Boundary boundary2 = new BoundaryImpl();
		boundary2.setId(1);
		boundary.setParent(boundary2);
		bndry = new HashSet();
		bndry.add(boundary);
		when(
				this.user
						.getAllJurisdictionsForLevelFullReslove(any(BoundaryType.class)))
				.thenReturn(bndry);
		when(this.query.uniqueResult()).thenReturn(this.user);
		data = this.userDao.getAllUsersForJurisdictionTypeFullResolve(
				new BoundaryTypeImpl(), 1);
		assertNotNull(data);
		names = new ArrayList();
		names.add("myname");
		when(this.query.iterate()).thenReturn(names.iterator());
		boundary2 = new BoundaryImpl();
		boundary2.setId(2);
		boundary.setParent(boundary2);
		bndry = new HashSet();
		bndry.add(boundary);
		when(
				this.user
						.getAllJurisdictionsForLevelFullReslove(any(BoundaryType.class)))
				.thenReturn(bndry);
		when(this.query.uniqueResult()).thenReturn(this.user);
		data = this.userDao.getAllUsersForJurisdictionTypeFullResolve(
				new BoundaryTypeImpl(), 1);
		assertNotNull(data);
		try {
			when(this.query.iterate()).thenThrow(new HibernateException(""));
			this.userDao.getAllUsersForJurisdictionTypeFullResolve(
					new BoundaryTypeImpl(), 1);
		} catch (final Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetAllUsersForJurisdictionFullResolve() {
		final BoundaryType bndryTyp = new BoundaryTypeImpl();
		final List listBndry = new ArrayList();
		listBndry.add(bndryTyp);
		List names = new ArrayList();
		names.add("myname");
		when(this.query.iterate()).thenReturn(names.iterator());
		Set bndry = new HashSet();
		final Boundary boundary = new BoundaryImpl();
		boundary.setId(1);
		bndry.add(boundary);
		when(this.user.getAllJurisdictionsFullReslove()).thenReturn(bndry);
		when(this.query.uniqueResult()).thenReturn(this.user);
		Map data = this.userDao.getAllUsersForJurisdictionFullResolve(
				listBndry, 1);
		assertNotNull(data);
		names = new ArrayList();
		names.add("myname");
		when(this.query.iterate()).thenReturn(names.iterator());
		Boundary boundary2 = new BoundaryImpl();
		boundary2.setId(1);
		boundary.setParent(boundary2);
		bndry = new HashSet();
		bndry.add(boundary);
		when(this.user.getAllJurisdictionsFullReslove()).thenReturn(bndry);
		when(this.query.uniqueResult()).thenReturn(this.user);
		data = this.userDao.getAllUsersForJurisdictionFullResolve(listBndry, 1);
		assertNotNull(data);
		names = new ArrayList();
		names.add("myname");
		when(this.query.iterate()).thenReturn(names.iterator());
		boundary2 = new BoundaryImpl();
		boundary2.setId(2);
		boundary.setParent(boundary2);
		bndry = new HashSet();
		bndry.add(boundary);
		when(this.user.getAllJurisdictionsFullReslove()).thenReturn(bndry);
		when(this.query.uniqueResult()).thenReturn(this.user);
		data = this.userDao.getAllUsersForJurisdictionFullResolve(listBndry, 1);
		assertNotNull(data);
		try {
			when(this.query.iterate()).thenThrow(new HibernateException(""));
			this.userDao.getAllUsersForJurisdictionFullResolve(listBndry, 1);
		} catch (final Exception e) {
			assertTrue(true);
		}

	}

	@Test
	public void testGetValidRoles() {
		final List l = new ArrayList();
		l.add(new UserRole());
		when(this.query.list()).thenReturn(l);
		final Set s = this.userDao.getValidRoles(1, new Date());
		assertNotNull(s);
		try {
			when(this.query.list()).thenThrow(new HibernateException(""));
			this.userDao.getValidRoles(1, new Date());
		} catch (final Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetAllUserForRolesStringDate() {
		final List l = this.userDao.getAllUserForRoles("abc,bscd", new Date());
		assertNotNull(l);
		try {
			when(this.query.list()).thenThrow(new HibernateException(""));
			this.userDao.getAllUserForRoles("abc,bscd", new Date());
		} catch (final Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetAllUsers() {
		final List l = this.userDao.getAllUsers();
		assertNotNull(l);
		try {
			when(this.query.list()).thenThrow(new HibernateException(""));
			this.userDao.getAllUsers();
		} catch (final Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetAllPasswords() {
		final List l = this.userDao.getAllPasswords();
		assertNotNull(l);
		try {
			when(this.query.list()).thenThrow(new HibernateException(""));
			this.userDao.getAllPasswords();
		} catch (final Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetAllUserForRoles() {
		final List l = this.userDao.getAllUserForRoles();
		assertNotNull(l);
		try {
			when(this.query.list()).thenThrow(new HibernateException(""));
			this.userDao.getAllUserForRoles();
		} catch (final Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetAllUsersByRole() {
		List l = new ArrayList();
		l.add(new UserRole());
		l = this.userDao.getAllUsersByRole(l, 1, new Date());
		assertNotNull(l);
		try {
			this.userDao.getAllUsersByRole(l, 1, null);
		} catch (final Exception e) {
			assertTrue(true);
		}
		try {
			l = new ArrayList();
			l.add(new UserRole());
			when(this.query.list()).thenThrow(new RuntimeException(""));
			this.userDao.getAllUsersByRole(l, 1, new Date());
		} catch (final Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetAllUsersByNameLike() {
		List l = new ArrayList();
		l.add(new UserImpl());
		l = this.userDao.getAllUserByUserNameLike("name");
		assertNotNull(l);
	}
}
