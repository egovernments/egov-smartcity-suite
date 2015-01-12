package org.egov.lib.rjbac.user.ejb.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
import org.egov.lib.rjbac.user.ejb.api.UserService;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;

public class UserManagerBeanTest {

	private UserService userBean;
	private Session session;
	private SessionFactory sessionFactory;
	private Query query;
	private User user;

	@Before
	public void runBefore() throws Exception {
		this.userBean = new UserServiceImpl();
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
	}

	@Test
	public void testEjbCreate() throws Exception {
		assertTrue(true);
	}

	@Test
	public void testCreateUser() throws Exception {
		this.userBean.createUser(this.user);
		assertNotNull(this.user);
	}

	@Test
	public void testUpdateUser() {
		this.userBean.updateUser(this.user);
		assertNotNull(this.user);
	}

	@Test
	public void testGetValidRoles() {
		final Set roles = this.userBean.getValidRoles(1, new Date());
		assertNotNull(roles);
	}

	@Test
	public void testGetAllRolesForUser() {
		final Set roles = this.userBean.getAllRolesForUser("name");
		assertNotNull(roles);
	}

	@Test
	public void testGetUserByID() {
		when(this.session.get(any(Class.class), anyInt()))
				.thenReturn(this.user);
		final User user = this.userBean.getUserByID(1);
		assertNotNull(user);
	}

	@Test
	public void testGetUserByName() {
		List userList = new ArrayList();
		userList.add(this.user);
		when(this.query.list()).thenReturn(userList);
		userList = this.userBean.getUserByName("userName");
		assertEquals(1, userList.size());
	}

	@Test
	public void testGetUserByUserName() {
		when(this.query.uniqueResult()).thenReturn(this.user);
		final User user = this.userBean.getUserByUserName("userName");
		assertNotNull(user);
	}

	@Test
	public void testGetUserDetailByID() {
		when(this.session.get(any(Class.class), anyInt())).thenReturn(
				new UserDetailImpl());
		final UserDetail userDetail = this.userBean.getUserDetailByID(1);
		assertNotNull(userDetail);
	}

	@Test
	public void testGetUserDetByUserName() {
		final List userList = new ArrayList();
		UserDetail detail = new UserDetailImpl();
		userList.add(detail);
		when(this.query.list()).thenReturn(userList);
		detail = this.userBean.getUserDetByUserName("userName");
		assertNotNull(detail);
	}

	@Test
	public void testGetAllJurisdictionsForUser() {
		final Set data = this.userBean.getAllJurisdictionsForUser(1);
		assertNotNull(data);
	}

	@Test
	public void testGetJurisdictionsForUser() {
		final Set data = this.userBean.getJurisdictionsForUser(1, new Date());
		assertNotNull(data);
	}

	@Test
	public void testRemoveUserInteger() {
		when(this.session.get(any(Class.class), anyInt()))
				.thenReturn(this.user);
		this.userBean.removeUser(1);
		assertTrue(true);
	}

	@Test
	public void testRemoveUserUser() {
		when(this.session.get(any(Class.class), anyInt()))
				.thenReturn(this.user);
		this.userBean.removeUser(this.user);
		assertTrue(true);
	}

	@Test
	public void testGetJurisdictionValueByBndryIdAndUserId() {
		when(this.query.uniqueResult()).thenReturn(new JurisdictionValues());
		final JurisdictionValues juri = this.userBean
				.getJurisdictionValueByBndryIdAndUserId(1, 1);
		assertNotNull(juri);
	}

	@Test
	public void testGetJurisdictionByBndryTypeIdAndUserId() {
		when(this.query.uniqueResult()).thenReturn(new Jurisdiction());
		final Jurisdiction juri = this.userBean
				.getJurisdictionByBndryTypeIdAndUserId(1, 1);
		assertNotNull(juri);
	}

	@Test
	public void testGetUserJurisdictions() {
		final Set data = this.userBean.getUserJurisdictions(1);
		assertNotNull(data);
	}

	@Test
	public void testGetAllUsersForJurisdictionType() {
		final List names = new ArrayList();
		names.add("myname");
		when(this.query.iterate()).thenReturn(names.iterator());
		final Set bndry = new HashSet();
		when(this.user.getAllJurisdictionsForLevel(any(BoundaryType.class)))
				.thenReturn(bndry);
		when(this.query.uniqueResult()).thenReturn(this.user);
		final Map data = this.userBean.getAllUsersForJurisdictionType(
				new BoundaryTypeImpl(), 1);
		assertNotNull(data);
	}

	@Test
	public void testGetAllUsersForJurisdictionTypeFullResolve() {
		final List names = new ArrayList();
		names.add("myname");
		when(this.query.iterate()).thenReturn(names.iterator());
		final Set bndry = new HashSet();
		when(this.user.getAllJurisdictionsForLevel(any(BoundaryType.class)))
				.thenReturn(bndry);
		when(this.query.uniqueResult()).thenReturn(this.user);
		final Map data = this.userBean
				.getAllUsersForJurisdictionTypeFullResolve(
						new BoundaryTypeImpl(), 1);
		assertNotNull(data);
	}

	@Test
	public void testGetAllUsersForJurisdictionFullResolve() {
		final List names = new ArrayList();
		names.add("myname");
		when(this.query.iterate()).thenReturn(names.iterator());
		final Set bndry = new HashSet();
		when(this.user.getAllJurisdictionsForLevel(any(BoundaryType.class)))
				.thenReturn(bndry);
		when(this.query.uniqueResult()).thenReturn(this.user);
		final List bndryTypes = new ArrayList();
		bndryTypes.add(new BoundaryTypeImpl());
		final Map data = this.userBean.getAllUsersForJurisdictionFullResolve(
				bndryTypes, 1);
		assertNotNull(data);
	}

	@Test
	public void testGetUsersByDepartment() {
		final List l = this.userBean.getUsersByDepartment(new DepartmentImpl());
		assertNotNull(l);
	}

	@Test
	public void testFindIpAddress() {
		final List l = this.userBean.findIpAddress("localhost");
		assertNotNull(l);
	}

	@Test
	public void testGetAllUsers() {
		final List l = this.userBean.getAllUsers();
		assertNotNull(l);
	}

	@Test
	public void testGetAllUsersByRole() {
		List l = new ArrayList();
		l.add(new UserRole());
		l = this.userBean.getAllUsersByRole(l, 1, new Date());
		assertNotNull(l);
	}

	@Test
	public void testGetAllUsersByNameLike() {
		List l = new ArrayList();
		l.add(new UserImpl());
		l = this.userBean.getAllUserByUserNameLike("some");
		assertNotNull(l);
	}

}
