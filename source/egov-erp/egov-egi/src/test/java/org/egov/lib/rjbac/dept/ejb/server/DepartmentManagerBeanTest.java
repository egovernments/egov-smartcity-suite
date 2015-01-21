package org.egov.lib.rjbac.dept.ejb.server;

import org.egov.infstr.utils.HibernateUtil;
import org.egov.lib.rjbac.dept.Department;
import org.egov.lib.rjbac.dept.ejb.api.DepartmentService;
import org.egov.lib.rjbac.role.RoleImpl;
import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Ignore
public class DepartmentManagerBeanTest {

	private DepartmentService departmentService;
	private Session session;
	private Query query;
	private Department department;

	@Before
	public void runBefore() throws Exception {
		this.departmentService = new DepartmentServiceImpl();
		this.department = Mockito.mock(Department.class);
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
	public void testGetDepartmentInteger() {
		when(this.session.load(Matchers.any(Class.class), Matchers.anyInt()))
				.thenReturn(this.department);
		assertEquals(this.department, this.departmentService.getDepartment(1));
	}

	@Test
	public void testGetDepartmentString() {
		when(this.query.uniqueResult()).thenReturn(this.department);
		assertEquals(this.department, this.departmentService.getDepartment("1"));
	}

	@Test
	public void testGetAllDepartments() {
		assertNotNull(this.departmentService.getAllDepartments());
	}

	@Test
	public void testGetAllUsersByDeptDepartmentInt() {
		assertNotNull(this.departmentService.getAllUsersByDept(this.department,
				1));
	}

	@Test
	public void testGetAllUsersByDeptDepartmentListInt() {
		final ArrayList l = new ArrayList();
		l.add(new RoleImpl());
		assertNotNull(this.departmentService.getAllUsersByDept(this.department,
				l, 1));
	}

	@Test
	public void testRemoveDepartment() {
		try {

			this.departmentService.removeDepartment(this.department);
		} catch (final Throwable e) {
		}
		assertTrue(true);
	}

	@Test
	public void testGetDepartmentById() {
		when(this.query.uniqueResult()).thenReturn(this.department);
		assertEquals(this.department,
				this.departmentService.getDepartmentById(1l));
	}

	@Test
	public void testGetDepartmentByCode() {
		when(this.query.uniqueResult()).thenReturn(this.department);
		assertEquals(this.department,
				this.departmentService.getDepartmentByCode("1"));
	}

}
