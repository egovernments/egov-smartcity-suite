/*
 * @(#)DepartmentDAO.java 3.0, 16 Jun, 2013 10:28:45 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.rjbac.dept.dao;

import java.util.Iterator;
import java.util.List;

import org.egov.exceptions.DuplicateElementException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infstr.dao.GenericDAO;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.infra.admin.master.entity.Department;
import org.egov.infra.admin.master.entity.Department;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DepartmentDAO extends GenericHibernateDAO implements GenericDAO {
    
    public DepartmentDAO(Class persistentClass, Session session) {
        super(persistentClass, session);
    }
    
    public DepartmentDAO() {
        this(Department.class,null);
    }
    
    public static final Logger LOGGER = LoggerFactory.getLogger(DepartmentDAO.class);

	@Override
	protected Session getCurrentSession() {
		return HibernateUtil.getCurrentSession();
	}

	public void createDepartment(final Department dept) throws DuplicateElementException {
		try {
			final Department checkDept = getDepartment(dept.getName());

			if (checkDept != null) {
				throw new DuplicateElementException("A department by " + dept.getName() + " already exists.");
			}

			getCurrentSession().save(dept);
			EgovMasterDataCaching.getInstance().removeFromCache("egi-department");
		} catch (final Exception e) {
			LOGGER.error("Exception occurred in createDepartment", e);
			throw new EGOVRuntimeException("Exception occurred in createDepartment", e);

		}

	}

	public void updateDepartment(final Department dept) {
		try {
			getCurrentSession().update(dept);
			EgovMasterDataCaching.getInstance().removeFromCache("egi-department");
		} catch (final Exception e) {
			LOGGER.error("Exception occurred in updateDepartment", e);
			throw new EGOVRuntimeException("Exception occurred in updateDepartment", e);

		}

	}

	public void removeDepartment(final Department dept) {
		try {
			getCurrentSession().delete(dept);
			EgovMasterDataCaching.getInstance().removeFromCache("egi-department");
		} catch (final Exception e) {
			LOGGER.error("Exception occurred in removeDepartment", e);
			throw new EGOVRuntimeException("Exception occurred in removeDepartment", e);
		}
	}

	public Department getDepartment(final Integer deptID) {
		try {
			return (Department) getCurrentSession().load(Department.class, deptID);
		} catch (final HibernateException e) {
			LOGGER.error("Exception occurred in getDepartment", e);
			throw new EGOVRuntimeException("Exception occurred in getDepartment", e);
		}
	}

	public Department getDepartment(final String deptName) {
		try {
			final Query qry = getCurrentSession().createQuery("FROM Department DI WHERE DI.deptName =:deptname");
			qry.setString("deptname", deptName);
			return (Department) qry.uniqueResult();
		} catch (final HibernateException e) {
			LOGGER.error("Exception occurred in getDepartment", e);
			throw new EGOVRuntimeException("Exception occurred in getDepartment", e);
		}
	}

	public List<Department> getAllDepartments() {
		try {
			return getCurrentSession().createQuery("FROM Department DI").list();
		} catch (final HibernateException e) {
			LOGGER.error("Exception occurred in getAllDepartments", e);
			throw new EGOVRuntimeException("Exception occurred in getAllDepartments", e);
		}
	}

	public List<User> getAllUsersByDept(final Department dept, final int topBoundaryID) {
		try {
			final Query qry = getCurrentSession().createQuery("FROM User UI where UI.department =:department and UI.topBoundaryID = :topBoundaryID");
			qry.setEntity("department", dept);
			qry.setInteger("topBoundaryID", topBoundaryID);
			return qry.list();
		} catch (final HibernateException e) {
			LOGGER.error("Exception occurred in getAllUsersByDept", e);
			throw new EGOVRuntimeException("Exception occurred in getAllUsersByDept", e);
		}

	}

	public void refresh(final Department dept) {
		try {
			getCurrentSession().refresh(dept);
		} catch (final HibernateException e) {
			LOGGER.error("Exception occurred in refresh", e);
			throw new EGOVRuntimeException("Exception occurred in refresh", e);
		}

	}

	public List<User> getAllUsersByDept(final Department dept, final List<Role> roleList, final int topBoundaryID) {
		try {
			Query qry = null;
			final Iterator<Role> it = roleList.iterator();
			while (it.hasNext()) {
				final Role role = it.next();
				qry = getCurrentSession().createQuery(" FROM User UI where UI.department =:department and UI.topBoundaryID = :topBoundaryID and :role in elements(UI.roles) ");
				qry.setEntity("department", dept);
				qry.setInteger("topBoundaryID", topBoundaryID);
				qry.setEntity("role", role);
			}
			return qry.list();

		} catch (final HibernateException e) {
			LOGGER.error("Exception occurred in getAllUsersByDept", e);
			throw new EGOVRuntimeException("Exception occurred in getAllUsersByDept", e);
		}
	}

	public Department getDepartmentById(final Long id) {
		try {
			final Query qry = getCurrentSession().createQuery("FROM Department DI WHERE DI.id =:id");
			qry.setLong("id", id);
			return (Department) qry.uniqueResult();
		} catch (final HibernateException e) {
			LOGGER.error("Error occurred while getting Department", e);
			throw new EGOVRuntimeException("Hibernate Exception : " + e.getMessage(), e);
		}
	}

	public Department getDepartmentByCode(final String deptCode) {
		try {
			final Query qry = getCurrentSession().createQuery("FROM Department DI WHERE DI.deptCode =:deptcode");
			qry.setString("deptcode", deptCode);
			return (Department) qry.uniqueResult();
		} catch (final HibernateException e) {
			LOGGER.error("Exception occurred in getDepartmentByCode", e);
			throw new EGOVRuntimeException("Exception occurred in getDepartmentByCode", e);
		}
	}
}
