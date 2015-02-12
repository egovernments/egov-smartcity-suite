/*
 * @(#)BoundaryDAO.java 3.0, 11 Jun, 2013 11:48:49 AM
 * Copyright 2013 eGovernments Foundation. All rights reserved. 
 * eGovernments PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.lib.admbndry;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.lib.admbndry.ejb.api.BoundaryTypeService;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class BoundaryDAO {
	public static final Logger LOGGER = LoggerFactory.getLogger(BoundaryDAO.class);
	private static final String WARD = "egi-ward";
	private BoundaryTypeService boundaryTypeService;

	private SessionFactory sessionFactory;

	public BoundaryDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private Session getSession() {
		return this.sessionFactory.getCurrentSession();
	}
	public void setBoundaryTypeService(final BoundaryTypeService boundaryTypeService) {
		this.boundaryTypeService = boundaryTypeService;
	}
	
	public BoundaryImpl load(Integer id)
	{
		return (BoundaryImpl)getSession().load(BoundaryImpl.class,id);
	}

	public Boundary createBoundary(final Boundary boundary) {
		try {
			getSession().save(boundary);
			EgovMasterDataCaching.getInstance().removeFromCache(WARD);

		} catch (final HibernateException e) {
			LOGGER.error("Error occurred in createBoundary", e);
			throw new EGOVRuntimeException("Error occurred in createBoundary", e);
		} catch (final Exception e) {
			LOGGER.error("Error occurred in createBoundary", e);
			throw new EGOVRuntimeException("Error occurred in createBoundary", e);
		}
		return boundary;

	}

	public void updateBoundary(final Boundary boundary) {
		try {
			getSession().saveOrUpdate(boundary);
			EgovMasterDataCaching.getInstance().removeFromCache(WARD);
		} catch (final HibernateException e) {
			LOGGER.error("Error occurred in updateBoundary", e);
			throw new EGOVRuntimeException("Error occurred in updateBoundary", e);
		} catch (final Exception e) {
			LOGGER.error("Error occurred in updateBoundary", e);
			throw new EGOVRuntimeException("Error occurred in updateBoundary", e);
		}

	}

	public void removeBoundary(final Boundary boundary) {
		try {
			getSession().delete(boundary);
			EgovMasterDataCaching.getInstance().removeFromCache(WARD);
		} catch (final HibernateException e) {
			LOGGER.error("Error occurred in updateBoundary", e);
			throw new EGOVRuntimeException("Error occurred in removeBoundary", e);
		} catch (final Exception e) {
			LOGGER.error("Error occurred in updateBoundary", e);
			throw new EGOVRuntimeException("Error occurred in removeBoundary", e);
		}

	}

	public Boundary getBoundary(final int bndryID) {
		try {
			final Query qry = getSession().createQuery("from BoundaryImpl BI where BI.id=:bndryID and BI.isHistory='N' " + " AND (" + "(BI.toDate IS NULL AND BI.fromDate <= :currDate) " + "OR " + "(BI.fromDate <= :currDate AND BI.toDate >= :currDate))");
			qry.setInteger("bndryID", bndryID);
			qry.setDate("currDate", new Date());
			return (Boundary) qry.uniqueResult();
		} catch (final HibernateException e) {
			LOGGER.error("Error occurred in getBoundary", e);
			throw new EGOVRuntimeException("Error occurred in getBoundary", e);
		}

	}

	public Boundary getAllBoundaryById(final int bndryID) {
		try {
			final Query qry = getSession().createQuery("from BoundaryImpl BI where BI.id=:bndryID");
			qry.setInteger("bndryID", bndryID);
			return (Boundary) qry.uniqueResult();
		} catch (final HibernateException e) {
			LOGGER.error("Error occurred in getBoundary", e);
			throw new EGOVRuntimeException("Error occurred in getBoundary", e);
		}

	}

	public Boundary getBoundaryById(final int bndryID) {
		try {
			final Query qry = getSession().createQuery("from BoundaryImpl BI where BI.id=:bndryID and BI.isHistory='N' ");
			qry.setInteger("bndryID", bndryID);

			return (Boundary) qry.uniqueResult();
		} catch (final HibernateException e) {
			LOGGER.error("Error occurred in getBoundaryById", e);
			throw new EGOVRuntimeException("Error occurred in getBoundaryById", e);
		}

	}

	public Boundary getBoundary(final short bndryNum, final BoundaryType bndryType, final int topLevelBoundaryID) {
		return getBoundaryByBndryNumAsBigInteger(new BigInteger(String.valueOf(bndryNum)), bndryType, topLevelBoundaryID);
	}

	public Boundary getBoundaryByBndryNumAsBigInteger(final BigInteger bndryNum, final BoundaryType bndryType, final int topLevelBoundaryID) {
		try {
			final Query qry = getSession().createQuery(
					"from BoundaryImpl BI where BI.boundaryNum = :boundaryNum " + "AND BI.boundaryType = :boundaryType " + "and BI.isHistory='N' " + " AND (" + "(BI.toDate IS NULL AND BI.fromDate <= :currDate) " + "OR "
							+ "(BI.fromDate <= :currDate AND BI.toDate >= :currDate))");
			qry.setBigInteger("boundaryNum", bndryNum);

			qry.setEntity("boundaryType", bndryType);
			qry.setDate("currDate", new Date());
			// Boundary bn = (Boundary)qry.uniqueResult();

			Boundary bn = null, finalBoundary = null, tempBoundary = null;
			for (final Iterator iter = qry.iterate(); iter.hasNext();) {
				bn = (Boundary) iter.next();
				tempBoundary = bn;
				if (bn.getParent() == null && bn.getId().equals(topLevelBoundaryID)) {
					return bn;
				}

				while (bn.getParent() != null) {
					final Boundary parent = bn.getParent();
					if (parent.getId().equals(topLevelBoundaryID)) {
						finalBoundary = tempBoundary;
						break;
					}
					bn = parent;
				}
			}

			return finalBoundary;
		} catch (final HibernateException e) {
			LOGGER.error("Error occurred in getBoundaryByBndryNumAsBigInteger", e);
			throw new EGOVRuntimeException("Error occurred in getBoundaryByBndryNumAsBigInteger", e);
		}

	}

	public List getAllBoundaries(final BoundaryType bndryType, final int topLevelBoundaryID) {
		try {
			final short hierarchyLevel = bndryType.getHeirarchy();

			final StringBuffer sbf = new StringBuffer(50);
			sbf.append("AND BI");
			for (int i = 1; i < hierarchyLevel; i++) {
				sbf.append(".parent");
			}

			if (hierarchyLevel == 1) {
				sbf.append(".id = :topLevelBoundaryID ");
			} else {
				sbf.append(" = :topLevelBoundaryID ");
			}

			final Query qry = getSession().createQuery(
					"from BoundaryImpl BI left join fetch BI.children where " + "BI.boundaryType = :boundaryType " + sbf.toString() + " " + " and BI.isHistory='N' AND (" + "(BI.toDate IS NULL AND BI.fromDate <= :currDate) " + "OR "
							+ "(BI.fromDate <= :currDate AND BI.toDate >= :currDate)) " + "order by BI.boundaryNum");
			// left join fetch BI.children
			qry.setEntity("boundaryType", bndryType);
			qry.setInteger("topLevelBoundaryID", topLevelBoundaryID);
			qry.setDate("currDate", new Date());
			final LinkedHashSet distinctResults = new LinkedHashSet(qry.list());
			final List retBoundaries = new ArrayList(distinctResults);

			return retBoundaries;
		} catch (final HibernateException e) {
			LOGGER.error("Error occurred in getAllBoundaries", e);
			throw new EGOVRuntimeException("Error occurred in getAllBoundaries", e);
		}
	}

	public List getAllBoundariesHistory(final BoundaryType bndryType, final int topLevelBoundaryID) {
		try {
			final short hierarchyLevel = bndryType.getHeirarchy();

			final StringBuffer sbf = new StringBuffer(50);
			sbf.append("AND BI");
			for (int i = 1; i < hierarchyLevel; i++) {
				sbf.append(".parent");
			}

			if (hierarchyLevel == 1) {
				sbf.append(".id = :topLevelBoundaryID ");
			} else {
				sbf.append(" = :topLevelBoundaryID ");
			}

			final Query qry = getSession().createQuery("from BoundaryImpl BI left join fetch BI.children where " + "BI.boundaryType = :boundaryType " + sbf.toString() + " " + " and BI.isHistory='N' order by BI.name");
			qry.setEntity("boundaryType", bndryType);
			qry.setInteger("topLevelBoundaryID", topLevelBoundaryID);
			final LinkedHashSet distinctResults = new LinkedHashSet(qry.list());
			final List retBoundaries = new ArrayList(distinctResults);

			return retBoundaries;
		} catch (final HibernateException e) {
			LOGGER.error("Error occurred in getAllBoundariesHistory", e);
			throw new EGOVRuntimeException("Error occurred in getAllBoundariesHistory", e);
		}
	}

	public List getAllBoundariesByBndryTypeId(final Integer bndryTypeId) {
		try {
			final Query qry = getSession().createQuery("from BoundaryImpl BI where BI.boundaryType.id=:bndryTypeId and BI.isHistory='N' order by BI.name");
			qry.setInteger("bndryTypeId", bndryTypeId);

			return qry.list();
		} catch (final HibernateException e) {
			LOGGER.error("Error occurred in getAllBoundariesByBndryTypeId", e);
			throw new EGOVRuntimeException("Error occurred in getAllBoundariesByBndryTypeId", e);
		}
	}

	public List getAllBoundaries(final short heirarchyLevel, final HeirarchyType heirarchyType, final short topLevelBoundaryID) {
		try {
			final Query qry = getSession().createQuery(
					"from BoundaryImpl BI where BI.boundaryType.hierarchy = :hierarchy " + "and BI.boundaryType.heirarchyType = :heirarchyType " + " and BI.isHistory='N' AND (" + "(BI.toDate IS NULL AND BI.fromDate <= :currDate) " + "OR "
							+ "(BI.fromDate <= :currDate AND BI.toDate >= :currDate)) " + " order by BI.name");

			qry.setShort("hierarchy", heirarchyLevel);
			qry.setEntity("heirarchyType", heirarchyType);
			qry.setDate("currDate", new Date());
			final List retBoundaries = new ArrayList();
			Boundary bn = null, tempBoundary = null;
			for (final Iterator iter = qry.iterate(); iter.hasNext();) {
				bn = (Boundary) iter.next();
				tempBoundary = bn;
				if (bn.getParent() == null && bn.getId().equals(Integer.valueOf(topLevelBoundaryID))) {
					retBoundaries.add(bn);
				}

				while (bn.getParent() != null) {
					final Boundary parent = bn.getParent();
					if (parent.getId().equals(Integer.valueOf(topLevelBoundaryID))) {
						// finalBoundary = tempBoundary;
						retBoundaries.add(tempBoundary);
						// break;
					}
					bn = parent;
				}
			}

			return retBoundaries;
		} catch (final HibernateException e) {
			LOGGER.error("Error occurred in getAllBoundaries", e);
			throw new EGOVRuntimeException("Error occurred in getAllBoundaries", e);
		}

	}

	public Boundary getBoundary(final short bndryNum, final short bndryTypeHeirarchyLevel, final HeirarchyType heirarchyType, final int topLevelBoundaryID) {
		return getBoundaryByBndryNumAsBigInteger(new BigInteger(String.valueOf(bndryNum)), bndryTypeHeirarchyLevel, heirarchyType, topLevelBoundaryID);
	}

	public Boundary getBoundaryByBndryNumAsBigInteger(final BigInteger bndryNum, final short bndryTypeHeirarchyLevel, final HeirarchyType heirarchyType, final int topLevelBoundaryID) {
		try {
			final Query qry = getSession().createQuery("from BoundaryImpl BI where BI.boundaryNum = :boundaryNum AND " + "BI.boundaryType.hierarchy = :hierarchy and BI.boundaryType.heirarchyType = :heirarchyType " + " and BI.isHistory='N' ");
			qry.setBigInteger("boundaryNum", bndryNum);

			qry.setShort("hierarchy", bndryTypeHeirarchyLevel);
			qry.setEntity("heirarchyType", heirarchyType);

			// Set retBoundaries = new HashSet();
			Boundary bn = null, finalBoundary = null, tempBoundary = null;
			for (final Iterator iter = qry.iterate(); iter.hasNext();) {
				bn = (Boundary) iter.next();
				tempBoundary = bn;
				if (bn.getParent() == null && bn.getId().equals(topLevelBoundaryID)) {
					return bn;
				}

				while (bn.getParent() != null) {
					final Boundary parent = bn.getParent();
					if (parent.getId().equals(topLevelBoundaryID)) {
						finalBoundary = tempBoundary;
						break;

					}
					bn = parent;

				}

			}

			return finalBoundary;
		} catch (final HibernateException e) {
			LOGGER.error("Error occurred in getBoundaryByBndryNumAsBigInteger", e);
			throw new EGOVRuntimeException("Error occurred in getBoundaryByBndryNumAsBigInteger", e);
		}

	}

	public Boundary getBoundary(final short bndryNum, final Integer parentBoundaryID, final BoundaryType boundaryType) {
		return getBoundaryByBndryNumAsBigInteger(new BigInteger(String.valueOf(bndryNum)), parentBoundaryID, boundaryType);
	}

	public Boundary getBoundaryByBndryNumAsBigInteger(final BigInteger bndryNum, final Integer parentBoundaryID, final BoundaryType boundaryType) {
		try {
			final Query qry = getSession().createQuery(
					"from BoundaryImpl BI where BI.boundaryNum = :boundaryNum " + "AND BI.parent = :parent and BI.boundaryType = :bndryType " + " and BI.isHistory='N' AND (" + "(BI.toDate IS NULL AND BI.fromDate <= :currDate) " + "OR "
							+ "(BI.fromDate <= :currDate AND BI.toDate >= :currDate))");
			qry.setBigInteger("boundaryNum", bndryNum);

			qry.setInteger("parent", parentBoundaryID);
			qry.setEntity("bndryType", boundaryType);
			qry.setDate("currDate", new Date());
			final Boundary bn = (Boundary) qry.uniqueResult();

			return bn;
		} catch (final HibernateException e) {
			LOGGER.error("Error occurred in getBoundaryByBndryNumAsBigInteger", e);
			throw new EGOVRuntimeException("Error occurred in getBoundaryByBndryNumAsBigInteger", e);
		}
	}

	public Boundary getTopBoundary(final String boundaryName, final HeirarchyType heirarchyType) {
		try {

			final BoundaryType bt = this.boundaryTypeService.getTopBoundaryType(heirarchyType);

			final Query qry = getSession().createQuery(
					"from BoundaryImpl BI where BI.name = :name and " + "BI.boundaryType.heirarchyType = :heirarchyType and BI.boundaryType = :bndryType " + " and BI.isHistory='N' AND (" + "(BI.toDate IS NULL AND BI.fromDate <= :currDate) " + "OR "
							+ "(BI.fromDate <= :currDate AND BI.toDate >= :currDate))");
			qry.setString("name", boundaryName);
			qry.setEntity("heirarchyType", heirarchyType);
			qry.setEntity("bndryType", bt);
			qry.setDate("currDate", new Date());
			// Set retBoundaries = new HashSet();

			final Boundary bn = (Boundary) qry.uniqueResult();

			return bn;

		} catch (final HibernateException e) {
			LOGGER.error("Error occurred in getTopBoundary", e);
			throw new EGOVRuntimeException("Error occurred in getTopBoundary", e);
		}
	}

	public List getTopBoundaries(final HeirarchyType heirarchyType) {
		try {
			final Query qry = getSession().createQuery(
					"from BoundaryImpl BI where BI.boundaryType.hierarchy = :hierarchy " + "and BI.boundaryType.heirarchyType = :heirarchyType " + " and BI.isHistory='N' AND (" + "(BI.toDate IS NULL AND BI.fromDate <= :currDate) " + "OR "
							+ "(BI.fromDate <= :currDate AND BI.toDate >= :currDate))" + " order by BI.name");
			final short hl = 1;
			qry.setShort("hierarchy", hl);
			qry.setEntity("heirarchyType", heirarchyType);
			qry.setDate("currDate", new Date());
			return qry.list();

		} catch (final HibernateException e) {
			LOGGER.error("Error occurred in getTopBoundary", e);
			throw new EGOVRuntimeException("Error occurred in getTopBoundary", e);
		}

	}

	public String getBoundaryNameForID(final Integer id) {
		try {
			final Query qry = getSession().createQuery(
					"Select BI.name from BoundaryImpl BI where BI.id = :id " + " and BI.isHistory='N' AND (" + "(BI.toDate IS NULL AND BI.fromDate <= :currDate) " + "OR " + "(BI.fromDate <= :currDate AND BI.toDate >= :currDate))");

			qry.setInteger("id", id);
			qry.setDate("currDate", new Date());
			return (String) qry.uniqueResult();

		} catch (final HibernateException e) {
			LOGGER.error("Error occurred in getBoundaryNameForID", e);
			throw new EGOVRuntimeException("Error occurred in getBoundaryNameForID", e);
		}

	}

	/**
	 * returns set of parent boundary for the given child boundary
	 * @param childBoundary
	 * @return
	 */
	public Set getCrossHeirarchyParent(final Boundary childBoundary) {
		final Set parentBoundarySet = new HashSet();
		if (childBoundary == null) {
			throw new EGOVRuntimeException("Childbndry.object.null");
		} else {
			final Query qry = getSession().createQuery("select CI.parent from CrossHeirarchyImpl CI where CI.child = :childBoundary");
			qry.setEntity("childBoundary", childBoundary);

			for (final Iterator iter = qry.iterate(); iter.hasNext();) {
				final Boundary bndryObj = (Boundary) iter.next();
				parentBoundarySet.add(bndryObj);

			}

		}
		return parentBoundarySet;
	}

	/**
	 * returns set of child boundary for the given parent boundary and childBoundaryType
	 * @param parentBoundary
	 * @return
	 */
	public Set getCrossHeirarchyChildren(final Boundary parentBoundary, final BoundaryType childBoundaryType) {
		final Set childBoundarySet = new HashSet();
		if (parentBoundary == null || childBoundaryType == null) {
			throw new EGOVRuntimeException("parentBoundary.childBoundaryType.object.null");
		} else {
			final Query qry = getSession().createQuery("select CI.child from CrossHeirarchyImpl CI, BoundaryImpl BI where CI.parent = :parentBoundary and CI.child=BI and BI.boundaryType = :childBoundaryType order by BI.name ");
			qry.setEntity("parentBoundary", parentBoundary);
			qry.setEntity("childBoundaryType", childBoundaryType);

			for (final Iterator iter = qry.iterate(); iter.hasNext();) {
				final Boundary bndryObj = (Boundary) iter.next();
				childBoundarySet.add(bndryObj);
			}

		}
		return childBoundarySet;

	}

	/**
	 * Used to get all the Child Boundary from a given Parent Boundary ID
	 * @param parentBoundaryId
	 * @return {@link List<Boundary>}
	 * @throws Exception
	 */
	public List<Boundary> getChildBoundaries(final String parentBoundaryId) throws Exception {

		List<Boundary> childBoundaries = null;

		try {

			String parentId = null;
			if (parentBoundaryId == null || parentBoundaryId.equals("")) {
				parentId = "IS NULL";
			} else {
				parentId = " = :parentBoundaryId";
			}

			final Query qry = getSession().createQuery("from BoundaryImpl BI where BI.parent " + parentId + " and BI.isHistory='N' AND ((BI.toDate IS NULL AND BI.fromDate <= :currDate) OR (BI.fromDate <= :currDate AND BI.toDate >= :currDate))");
			if (parentBoundaryId != null && !parentBoundaryId.equals("")) {
				qry.setInteger("parentBoundaryId", Integer.parseInt(parentBoundaryId));
			}
			qry.setDate("currDate", new Date());
			childBoundaries = qry.list();

		} catch (final Exception e) {
			throw e;
		}
		return childBoundaries;

	}

	/**
	 * Used to get all the Parent Boundary from a given Child Boundary ID
	 * @param childBoundaryId
	 * @return {@link List<Boundary>}
	 * @throws Exception
	 */
	public List<Boundary> getAllParentBoundaries(final String childBoundaryId) throws Exception {

		List<Boundary> parentBoundaries = null;

		try {

			final Query qry = getSession().createQuery(
					"from BoundaryImpl B1 where B1.parent in (select B2.parent from BoundaryImpl B2 where B2.id in (select B3.parent from BoundaryImpl B3 where B3.id = :childBoundaryId)) "
							+ " and B1.isHistory='N' AND ((B1.toDate IS NULL AND B1.fromDate <= :currDate) OR (B1.fromDate <= :currDate AND B1.toDate >= :currDate)) order by B1.name");

			qry.setInteger("childBoundaryId", Integer.parseInt(childBoundaryId));
			qry.setDate("currDate", new Date());
			parentBoundaries = qry.list();

		} catch (final Exception e) {
			throw e;
		}
		return parentBoundaries;

	}

	/**
	 * Returns the boundary associated with a boundary number, boundary type and hierarchy type. The boundary type and hierarchy type is not case sensitive.
	 * @param bndryNum
	 * @param boundaryType - Boundary type name eg. City, Ward
	 * @param hierarchyCode - Code of hierarchyType
	 * @return
	 */
	public Boundary getBoundary(final Integer bndryNum, final String boundaryType, final String hierarchyCode) {
		final Query qry = getSession().createQuery(
				"from BoundaryImpl BI where BI.boundaryNum = :boundaryNum" + " and BI.isHistory='N' AND " + " upper(BI.boundaryType.name)= :boundaryType AND " + " upper(BI.boundaryType.heirarchyType.code)= :hierarchyCode AND ("
						+ "(BI.toDate IS NULL AND BI.fromDate <= :currDate) OR (BI.fromDate <= :currDate AND BI.toDate >= :currDate)) ");
		qry.setBigInteger("boundaryNum", BigInteger.valueOf(bndryNum));
		qry.setString("boundaryType", boundaryType.toUpperCase());
		qry.setString("hierarchyCode", hierarchyCode.toUpperCase());
		qry.setDate("currDate", new Date());
		return (Boundary) qry.uniqueResult();
	}

	/**
	 * Used to get all the child Boundary from a given Boundary ID
	 * @param bndryTypeId
	 * @return {@link List<Boundary>}
	 * @throws Exception
	 */
	public List<BoundaryImpl> getAllchildBoundaries(final Integer bndryTypeId) {
		try {
			List<BoundaryImpl> childBoundaries = null;
			if (bndryTypeId != null && !bndryTypeId.equals("")) {
				final Query qry = getSession().createQuery("from BoundaryImpl BI where BI.parent is not null and BI.parent.id=:bndryTypeId and BI.isHistory='N' order by BI.name");
				qry.setInteger("bndryTypeId", bndryTypeId);
				childBoundaries = qry.list();
			}
			return childBoundaries;
		} catch (final HibernateException e) {
			LOGGER.error("Error occurred in getAllBoundariesByBndryTypeId", e);
			throw new EGOVRuntimeException("Error occurred in getAllBoundariesByBndryTypeId", e);
		}
	}
	
	public List<Boundary> findByNameLike(String name) {
	    return getSession().createCriteria(BoundaryImpl.class).add(Restrictions.ilike("bndryNameLocal", name,MatchMode.ANYWHERE)).addOrder(Order.asc("bndryNameLocal")).list();
	}
}
