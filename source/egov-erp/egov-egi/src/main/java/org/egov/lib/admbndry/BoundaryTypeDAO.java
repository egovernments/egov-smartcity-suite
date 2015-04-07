/*
 * @(#)BoundaryTypeDAO.java 3.0, 16 Jun, 2013 3:25:25 PM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.admbndry;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.exceptions.NoSuchObjectException;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.entity.HierarchyType;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoundaryTypeDAO {

	private static final Logger LOGGER = LoggerFactory.getLogger(BoundaryTypeDAO.class);

	private SessionFactory sessionFactory;

	public BoundaryTypeDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}
	public void createBoundaryType(final BoundaryType bndryType) {
		try {
			getSession().save(bndryType);
		} catch (final HibernateException e) {
			LOGGER.error("Exception occurred in createBoundaryType", e);
			throw new EGOVRuntimeException("Exception occurred in createBoundaryType", e);
		}
	}

	public void removeBoundaryType(final BoundaryType bndryType) {
		try {
			getSession().delete(bndryType);
		} catch (final HibernateException e) {
			LOGGER.error("Exception occurred in removeBoundaryType", e);
			throw new EGOVRuntimeException("Exception occurred in removeBoundaryType", e);
		} catch (final Exception e) {
			LOGGER.error("Exception occurred in removeBoundaryType", e);
			throw new EGOVRuntimeException("Exception occurred in removeBoundaryType", e);
		}
	}

	public void updateBoundaryType(final BoundaryType bndryType) {
		try {
			getSession().saveOrUpdate(bndryType);
		} catch (final HibernateException e) {
			LOGGER.error("Exception occurred in updateBoundaryType", e);
			throw new EGOVRuntimeException("Exception occurred in updateBoundaryType", e);
		} catch (final Exception se) {
			LOGGER.error("Exception occurred in updateBoundaryType", se);
			throw new EGOVRuntimeException("Exception occurred in updateBoundaryType", se);
		}
	}

	public BoundaryType getBoundaryType(final short heirarchylevel, final HierarchyType heirarchyType) {
		try {
			final Query qry = getSession().createQuery("from BoundaryTypeImpl BT left join fetch BT.childBoundaryTypes where BT.hierarchy = :hirchy and BT.heirarchyType = :heirarchyType");
			qry.setShort("hirchy", heirarchylevel);
			qry.setEntity("heirarchyType", heirarchyType);
			return (BoundaryType) qry.uniqueResult();
		} catch (final HibernateException e) {
			LOGGER.error("Exception occurred in getBoundaryType", e);
			throw new EGOVRuntimeException("Exception occurred in getBoundaryType", e);
		}
	}

	public BoundaryType getBoundaryType(final String bndryTypeName, final HierarchyType heirarchyType) {
		try {
			final Query qry = getSession().createQuery("from BoundaryTypeImpl BT where upper(NAME) = upper(:name) and BT.heirarchyType = :heirarchyType");
			qry.setString("name", bndryTypeName);
			qry.setEntity("heirarchyType", heirarchyType);
			return (BoundaryType) qry.uniqueResult();
		} catch (final HibernateException e) {
			LOGGER.error("Exception occurred in getBoundaryType", e);
			throw new EGOVRuntimeException("Exception occurred in getBoundaryType", e);
		}
	}

	public BoundaryType getBoundaryType(final Integer id) {
		try {
			final Query qry = getSession().createQuery("from BoundaryTypeImpl BT where id=:id");
			qry.setInteger("id", id);
			return (BoundaryType) qry.uniqueResult();
		} catch (final HibernateException e) {
			LOGGER.error("Exception occurred in getBoundaryType", e);
			throw new EGOVRuntimeException("Exception occurred in getBoundaryType", e);
		}
	}

	// It will return all the parent boundary as list for the input boundary id.
	// BoundaryList declared as global because recursive is used to get the parent boundary.
	List<Boundary> boundaryList = new ArrayList<Boundary>();

	public List<Boundary> getParentBoundaryList(final Integer boundaryId) throws NoSuchObjectException {
		try {

			final BoundaryDAO bndryDAO = new BoundaryDAO(sessionFactory);
			final Boundary bndry = bndryDAO.getBoundary(boundaryId);
			if (bndry != null) {
				this.boundaryList.add(bndry);
				if (bndry.getParent() != null) {
					this.boundaryList = getParentBoundaryList(bndry.getParent().getId());
				}
			} else {
				throw new NoSuchObjectException("bndry.Obj.null");

			}

			return this.boundaryList;
		} catch (final Exception e) {
			LOGGER.error("Exception occurred in getParentBoundaryList", e);
			throw new EGOVRuntimeException("system.error", e);
		}
	}

	// It will return all the parent boundary Types as list for the input heirarchy type.
	// Filtered boundary types, if their parent is null.
	public List<BoundaryType> getParentBoundaryTypeByHirarchy(final HierarchyType heirarchyType) throws EGOVRuntimeException {
		try {
			List<BoundaryType> boundaryTypeList = new ArrayList<BoundaryType>();

			if (heirarchyType != null) {
				final Query qry = getSession().createQuery("from BoundaryTypeImpl BT where BT.parent is not null and  BT.heirarchyType = :heirarchyType");
				qry.setEntity("heirarchyType", heirarchyType);
				boundaryTypeList = qry.list();
			}
			return boundaryTypeList;

		} catch (final Exception e) {
			LOGGER.error("Exception occurred in getParentBoundaryTypeByHirarchy", e);
			throw new EGOVRuntimeException("system.error", e);
		}
	}

	/**
	 * It will return all the parent boundary as map for the input heirarchy type. 
	 * First Getting Top level Boundary Type in a hierarchy(next to "city"). 
	 * Second loop getting all the boundaries for individual boundary types. 
	 * Returns Map which mapped to Boundary Type Name and List of boundaries.
	 */
	public Map<String, List<Boundary>> getSecondLevelBoundaryByPassingHeirarchy(final HierarchyType heirarchyType) throws EGOVRuntimeException {
		try {
			List<Boundary> boundaryList = new ArrayList<Boundary>();
			final Map<String, List<Boundary>> retSet = new HashMap<String, List<Boundary>>();
			String bryName = null;
			final Date currDate = new Date();
			// Getting Top level Boundary Type in a hierarchy.
			if (heirarchyType != null) {

				final Query qry = getSession().createQuery("from BoundaryTypeImpl BT where BT.parent.parent is null and  BT.heirarchyType = :heirarchyType");
				qry.setEntity("heirarchyType", heirarchyType);
				final BoundaryType boundry = (BoundaryType) qry.uniqueResult();
				if (boundry != null) {
					final Query qry1 = getSession().createQuery(
							"from BoundaryImpl BI where " + "BI.boundaryType = :boundaryType " + " and BI.isHistory='N' AND (" + "(BI.toDate IS NULL AND BI.fromDate <= :currDate) " + "OR " + "(BI.fromDate <= :currDate AND BI.toDate >= :currDate)) "
									+ "order by BI.boundaryNum");
					qry1.setEntity("boundaryType", boundry);
					qry1.setDate("currDate", currDate);
					boundaryList = qry1.list();
					for (final Boundary boundary : boundaryList) {
						bryName = boundary.getBoundaryType().getName();
					}
					if (bryName != null) {
						retSet.put(bryName, boundaryList);
					}
				}
			}
			return retSet;

		} catch (final Exception e) {
			LOGGER.error("Exception occurred in getParentBoundaryTypeByHirarchy", e);
			throw new EGOVRuntimeException("system.error", e);
		}
	}

	public Map<String, List<Boundary>> getBoundaryMapByPassingHirarchy(final HierarchyType heirarchyType) throws EGOVRuntimeException {
		try {
			String bryName = null;
			final Date currDate = new Date();
			List<BoundaryType> boundaryTypeList = new ArrayList<BoundaryType>();
			final Map<String, List<Boundary>> retSet = new HashMap<String, List<Boundary>>();
			if (heirarchyType != null) {
				final Query qry = getSession().createQuery("from BoundaryTypeImpl BT where BT.parent is not null and  BT.heirarchyType = :heirarchyType");
				qry.setEntity("heirarchyType", heirarchyType);
				boundaryTypeList = qry.list();

				for (final BoundaryType boundaryType : boundaryTypeList) {
					final Query qry1 = getSession().createQuery(
							"from BoundaryImpl BI where " + "BI.boundaryType = :boundaryType " + " and BI.isHistory='N' AND (" + "(BI.toDate IS NULL AND BI.fromDate <= :currDate) " + "OR " + "(BI.fromDate <= :currDate AND BI.toDate >= :currDate)) "
									+ "order by BI.boundaryNum");
					qry1.setEntity("boundaryType", boundaryType);
					qry1.setDate("currDate", currDate);
					this.boundaryList = qry1.list();
					for (final Boundary boundary : this.boundaryList) {
						bryName = boundary.getBoundaryType().getName();
					}
					if (bryName != null) {
						retSet.put(bryName, this.boundaryList);
					}
				}
			}
			return retSet;

		} catch (final Exception e) {
			LOGGER.error("Exception occurred in getParentBoundaryTypeByHirarchy", e);
			throw new EGOVRuntimeException("system.error", e);
		}
	}

}
