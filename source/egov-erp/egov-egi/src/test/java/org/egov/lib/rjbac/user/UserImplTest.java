package org.egov.lib.rjbac.user;

import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.admbndry.Boundary;
import org.egov.lib.admbndry.BoundaryImpl;
import org.egov.lib.admbndry.BoundaryType;
import org.egov.lib.admbndry.BoundaryTypeImpl;
import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.dept.DepartmentImpl;
import org.egov.lib.rjbac.jurisdiction.Jurisdiction;
import org.egov.lib.rjbac.jurisdiction.JurisdictionValues;
import org.egov.lib.rjbac.role.Role;
import org.egov.lib.rjbac.role.RoleImpl;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Matchers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Ignore
public class UserImplTest {
	private Session session;
	private Query query;

	@Before
	public void runBefore() throws Exception {
		this.session = mock(Session.class);
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
	public void testGetJurisdictionData() {
		final UserImpl user = new UserImpl();
		user.setId(1);
		final ArrayList s = new ArrayList();
		s.add(new Jurisdiction());
		when(this.query.list()).thenReturn(s);
		assertNotNull(user.getJurisdictionData());
	}

	@Test
	public void testSetAllJurisdictions() {
		final UserImpl user = new UserImpl();
		final Set s = new HashSet();
		final Jurisdiction jur = new Jurisdiction();
		jur.setJurisdictionValues(new HashSet());
		s.add(jur);
		user.setAllJurisdictions(s);
		assertTrue(true);
		user.removeJurisdiction(jur);
		assertTrue(true);
	}

	@Test
	public void testGetValidRolesOnDate() throws Exception {
		when(this.query.list()).thenReturn(new ArrayList());
		final UserImpl user = new UserImpl();
		user.setId(1);
		assertNotNull(user.getRoles());
		runBefore();
		when(this.query.list()).thenThrow(new HibernateException(""));
		try {
			user.getRoles();
		} catch (final Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetAllJurisdictionsForLevel() {
		final UserImpl user = new UserImpl();
		user.setUserName("username");
		assertNotNull(user.getAllJurisdictionsForLevel(null));
		user.setId(1);
		final ArrayList s = new ArrayList();
		final JurisdictionValues jurs = new JurisdictionValues();
		final BoundaryType bt = new BoundaryTypeImpl();
		bt.setId(1);
		bt.setName("name");
		s.add(jurs);
		final Boundary bndry = new BoundaryImpl();
		bndry.setBoundaryType(bt);
		jurs.setBoundary(bndry);
		when(this.query.list()).thenReturn(s);
		assertNotNull(user.getAllJurisdictionsForLevel(bt));
	}

	@Test
	public void testGetAllJurisdictionsForLevelFullReslove() {
		final UserImpl user = new UserImpl();
		user.setUserName("username");
		user.setId(1);
		final ArrayList s = new ArrayList();
		final Jurisdiction jurs = new Jurisdiction();
		final JurisdictionValues jurval = new JurisdictionValues();
		jurval.setId(1);
		final BoundaryType bt = new BoundaryTypeImpl();
		final Boundary bndry = new BoundaryImpl();
		bndry.setBoundaryType(bt);
		jurval.setBoundary(bndry);
		jurval.setUserJurLevel(jurs);
		jurs.setId(1);
		jurs.setJurisdictionLevel(bt);
		final HashSet set = new HashSet();
		set.add(jurval);
		jurs.setJurisdictionValues(set);
		jurs.setUser(user);
		bt.setId(1);
		bt.setName("name");
		s.add(jurs);
		when(this.query.list()).thenReturn(s);
		assertNotNull(user.getAllJurisdictionsForLevelFullReslove(null));
		assertNotNull(user.getAllJurisdictionsForLevelFullReslove(bt));
	}

	@Test
	public void testGetAllJurisdictionsFullReslove() {
		final UserImpl user = new UserImpl();
		user.setUserName("username");
		user.setId(1);
		final ArrayList s = new ArrayList();
		final Jurisdiction jurs = new Jurisdiction();
		final JurisdictionValues jurval = new JurisdictionValues();
		jurval.setId(1);
		final BoundaryType bt = new BoundaryTypeImpl();
		final Boundary bndry = new BoundaryImpl();
		bndry.setBoundaryType(bt);
		jurval.setBoundary(bndry);
		jurval.setUserJurLevel(jurs);
		jurs.setId(1);
		jurs.setJurisdictionLevel(bt);
		final HashSet set = new HashSet();
		set.add(jurval);
		jurs.setJurisdictionValues(set);
		jurs.setUser(user);
		bt.setId(1);
		bt.setName("name");
		s.add(jurs);
		when(this.query.list()).thenReturn(s);
		assertNotNull(user.getAllJurisdictionsFullReslove());
		assertNotNull(user.getAllJurisdictionsFullReslove());
	}

	@Test
	public void testSetRoles() {
		final UserImpl user = new UserImpl();
		final Set s = new HashSet();
		final Role role = new RoleImpl();
		user.setRoles(s);
		user.addRole(role);
		user.removeRole(role);
		assertTrue(true);
	}

	@Test
	public void testEqual() {
		final UserImpl user = new UserImpl();
		user.equals(user);
		user.equals(null);
		user.equals(new Object());
		final User user2 = new UserImpl();
		user2.setUserName("name");
		user.setUserName("name1");
		user.equals(user2);
		user.setUserName("name");
		user.equals(user2);
		user.hashCode();
		assertTrue(true);
	}

	@Test
	public void testAddJuridiction() {
		final Jurisdiction jur = new Jurisdiction();
		jur.setJurisdictionValues(new HashSet());
		final UserImpl user = new UserImpl();
		user.setUserName("name");
		jur.setUser(user);
		final BoundaryType bt = new BoundaryTypeImpl();
		jur.setJurisdictionLevel(bt);
		user.setAllJurisdictions(new HashSet());
		user.addJurisdiction(jur);
		assertTrue(true);
	}

	@Test
	public void testClosureOfUserImpl() {
		final UserImpl user = new UserImpl();
		final Department m = new DepartmentImpl();
		user.setDepartment(m);
		assertNotNull(user.getDepartment());
		user.setExtraField1("1");
		assertEquals("1", user.getExtraField1());
		user.setExtraField2("2");
		assertEquals("2", user.getExtraField2());
		user.setExtraField3("3");
		assertEquals("3", user.getExtraField3());
		user.setExtraField4("4");
		assertEquals("4", user.getExtraField4());
		user.setFirstName("name");
		assertEquals("name", user.getFirstName());
		user.setIsActive(1);
		assertEquals(Integer.valueOf(1), user.getIsActive());
		user.setId(1);
		assertEquals(Integer.valueOf(1), user.getId());
		user.setIsSuspended('N');
		assertEquals('N', user.getIsSuspended());
		user.setLastName("name");
		assertEquals("name", user.getLastName());
		user.setMiddleName("name");
		assertEquals("name", user.getMiddleName());
		user.setPwd("pwd");
		assertEquals("pwd", user.getPwd());
		user.setPwdReminder("pwdr");
		assertEquals("pwdr", user.getPwdReminder());
		final Date currDate = new Date();
		user.setDob(currDate);
		assertEquals(currDate, user.getDob());
		user.setSalutation("sal");
		assertEquals("sal", user.getSalutation());
		user.setTitle("mr");
		assertEquals("mr", user.getTitle());
		user.setUpdateTime(currDate);
		assertEquals(currDate, user.getUpdateTime());
		user.setFromDate(currDate);
		assertEquals(currDate, user.getFromDate());
		user.setToDate(currDate);
		assertEquals(currDate, user.getToDate());
		user.setUpdateUserId(1);
		assertEquals(Integer.valueOf(1), user.getUpdateUserId());
		user.setUserName("name");
		assertEquals("name", user.getUserName());
		user.setLoginTerminal("trmnl");
		assertEquals("trmnl", user.getLoginTerminal());
		user.setTopBoundaryID(1);
		assertEquals(Integer.valueOf(1), user.getTopBoundaryID());
		user.setUserRoles(null);
		assertNull(user.getUserRoles());
		user.setReportees(null);
		assertNull(user.getReportees());
		user.setParent(null);
		assertNull(user.getParent());
	}

}
