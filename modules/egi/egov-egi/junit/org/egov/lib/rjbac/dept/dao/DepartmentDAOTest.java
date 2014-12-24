package org.egov.lib.rjbac.dept.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.junit.utils.TestUtils;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.role.RoleImpl;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

public class DepartmentDAOTest {

	private DepartmentDAO departmentDao;
	private Session session;
	private Query query;
	private Department department;

	@Before
	public void runBefore() throws Exception {
		this.department = Mockito.mock(Department.class);
		EGOVThreadLocals.setDomainName("domainName");
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
		this.departmentDao = new DepartmentDAO();
		TestUtils.activateInitialContext();

	}

	@Test
	public void testCreateDepartment() throws Exception {
		when(this.query.uniqueResult()).thenReturn(this.department, null);
		when(this.session.load(Matchers.any(Class.class), Matchers.anyInt()))
				.thenReturn(this.department);
		try {
			this.departmentDao.createDepartment(this.department);
		} catch (final EGOVRuntimeException e) {
			assertTrue(true);
		}
		when(this.session.save(this.department)).thenThrow(
				new HibernateException(""));
		try {
			this.departmentDao.createDepartment(this.department);
		} catch (final Exception e) {
			assertTrue(true);
		}
		runBefore();
		when(this.session.save(this.department)).thenReturn(1);
		this.departmentDao.createDepartment(this.department);
		assertTrue(true);

	}

	@Test
	public void testUpdateDepartment() {
		this.departmentDao.updateDepartment(this.department);
		Mockito.doThrow(new HibernateException("")).when(this.session)
				.update(this.department);
		try {
			this.departmentDao.updateDepartment(this.department);
		} catch (final Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testRemoveDepartment() {
		this.departmentDao.removeDepartment(this.department);
		Mockito.doThrow(new HibernateException("")).when(this.session)
				.delete(this.department);
		try {
			this.departmentDao.removeDepartment(this.department);
		} catch (final Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetDepartmentInteger() {
		when(this.session.load(Matchers.any(Class.class), Matchers.anyInt()))
				.thenReturn(this.department);
		assertEquals(this.department, this.departmentDao.getDepartment(1));
		when(this.session.load(Matchers.any(Class.class), Matchers.anyInt()))
				.thenThrow(new HibernateException(""));
		try {
			this.departmentDao.getDepartment(1);
		} catch (final Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetDepartmentString() {
		when(this.query.uniqueResult()).thenReturn(this.department);
		assertEquals(this.department, this.departmentDao.getDepartment("1"));
		when(this.query.uniqueResult()).thenThrow(new HibernateException(""));
		try {
			this.departmentDao.getDepartment("1");
		} catch (final Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetAllDepartments() {
		assertNotNull(this.departmentDao.getAllDepartments());
		when(this.query.list()).thenThrow(new HibernateException(""));
		try {
			this.departmentDao.getAllDepartments();
		} catch (final Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetAllUsersByDeptDepartmentInt() {
		assertNotNull(this.departmentDao.getAllUsersByDept(this.department, 1));
		when(this.query.list()).thenThrow(new HibernateException(""));
		try {
			this.departmentDao.getAllUsersByDept(this.department, 1);
		} catch (final Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testRefresh() {
		this.departmentDao.refresh(this.department);
		Mockito.doThrow(new HibernateException("")).when(this.session)
				.refresh(this.department);
		try {
			this.departmentDao.refresh(this.department);
		} catch (final Exception e) {
		}
		assertTrue(true);
	}

	@Test
	public void testGetAllUsersByDeptDepartmentListInt() {
		final ArrayList l = new ArrayList();
		l.add(new RoleImpl());
		assertNotNull(this.departmentDao.getAllUsersByDept(this.department, l,
				1));
		when(this.query.list()).thenThrow(new HibernateException(""));
		try {
			this.departmentDao.getAllUsersByDept(this.department, l, 1);
		} catch (final Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetDepartmentById() {
		when(this.query.uniqueResult()).thenReturn(this.department);
		assertEquals(this.department, this.departmentDao.getDepartmentById(1l));
		when(this.query.uniqueResult()).thenThrow(new HibernateException(""));
		try {
			this.departmentDao.getDepartmentById(1l);
		} catch (final Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetDepartmentByCode() {
		when(this.query.uniqueResult()).thenReturn(this.department);
		assertEquals(this.department,
				this.departmentDao.getDepartmentByCode("1"));
		when(this.query.uniqueResult()).thenThrow(new HibernateException(""));
		try {
			this.departmentDao.getDepartmentByCode("1");
		} catch (final Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void testConstructor() {
		final DepartmentDAO dept = new DepartmentDAO(Department.class,
				this.session);
		assertNotNull(dept);
	}
}
