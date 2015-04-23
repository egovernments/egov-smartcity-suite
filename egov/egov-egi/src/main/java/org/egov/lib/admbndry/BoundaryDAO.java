/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.lib.admbndry;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.entity.HierarchyType;
import org.egov.infra.admin.master.service.BoundaryTypeService;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BoundaryDAO {
	public static final Logger LOGGER = LoggerFactory.getLogger(BoundaryDAO.class);
	private static final String WARD = "egi-ward";
	private BoundaryTypeService boundaryTypeService;

	private SessionFactory sessionFactory;

	public BoundaryDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private Session getSession() {
		return HibernateUtil.getCurrentSession();
	}
	public void setBoundaryTypeService(final BoundaryTypeService boundaryTypeService) {
		this.boundaryTypeService = boundaryTypeService;
	}
	
	public Boundary load(Long id)
	{
		return (Boundary)getSession().load(Boundary.class,id);
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

	public Boundary getBoundary(final Long bndryID) {
		try {
			final Query qry = getSession().createQuery("from Boundary BI where BI.id=:bndryID and BI.isHistory='N' " + " AND (" + "(BI.toDate IS NULL AND BI.fromDate <= :currDate) " + "OR " + "(BI.fromDate <= :currDate AND BI.toDate >= :currDate))");
			qry.setLong("bndryID", bndryID);
			qry.setDate("currDate", new Date());
			return (Boundary) qry.uniqueResult();
		} catch (final HibernateException e) {
			LOGGER.error("Error occurred in getBoundary", e);
			throw new EGOVRuntimeException("Error occurred in getBoundary", e);
		}

	}

	public Boundary getAllBoundaryById(final Long bndryID) {
		try {
			final Query qry = getSession().createQuery("from Boundary BI where BI.id=:bndryID");
			qry.setLong("bndryID", bndryID);
			return (Boundary) qry.uniqueResult();
		} catch (final HibernateException e) {
			LOGGER.error("Error occurred in getBoundary", e);
			throw new EGOVRuntimeException("Error occurred in getBoundary", e);
		}

	}

	public Boundary getBoundaryById(final Long bndryID) {
		try {
			final Query qry = getSession().createQuery("from Boundary BI where BI.id=:bndryID and BI.isHistory='N' ");
			qry.setLong("bndryID", bndryID);

			return (Boundary) qry.uniqueResult();
		} catch (final HibernateException e) {
			LOGGER.error("Error occurred in getBoundaryById", e);
			throw new EGOVRuntimeException("Error occurred in getBoundaryById", e);
		}

	}

	public Boundary getBoundary(final Long bndryNum, final BoundaryType bndryType, final Long topLevelBoundaryID) {
		return getBoundaryByBndryNumAsBigInteger(new Long(String.valueOf(bndryNum)), bndryType, topLevelBoundaryID);
	}

	public Boundary getBoundaryByBndryNumAsBigInteger(final Long bndryNum, final BoundaryType bndryType, final Long topLevelBoundaryID) {
		try {
			final Query qry = getSession().createQuery(
					"from Boundary BI where BI.boundaryNum = :boundaryNum " + "AND BI.boundaryType = :boundaryType " + "and BI.isHistory='N' " + " AND (" + "(BI.toDate IS NULL AND BI.fromDate <= :currDate) " + "OR "
							+ "(BI.fromDate <= :currDate AND BI.toDate >= :currDate))");
			qry.setLong("boundaryNum", bndryNum);

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

	public List getAllBoundaries(final BoundaryType bndryType, final Long topLevelBoundaryID) {
		try {
			final Long hierarchyLevel = bndryType.getHierarchy();

			final StringBuffer sbf = new StringBuffer(50);
			sbf.append("AND BI");
			for (Long i = 1l; i < hierarchyLevel; i++) {
				sbf.append(".parent");
			}

			if (hierarchyLevel == 1) {
				sbf.append(".id = :topLevelBoundaryID ");
			} else {
				sbf.append(" = :topLevelBoundaryID ");
			}

			final Query qry = getSession().createQuery(
					"from Boundary BI left join fetch BI.children where " + "BI.boundaryType = :boundaryType " + sbf.toString() + " " + " and BI.isHistory='N' AND (" + "(BI.toDate IS NULL AND BI.fromDate <= :currDate) " + "OR "
							+ "(BI.fromDate <= :currDate AND BI.toDate >= :currDate)) " + "order by BI.boundaryNum");
			// left join fetch BI.children
			qry.setEntity("boundaryType", bndryType);
			qry.setLong("topLevelBoundaryID", topLevelBoundaryID);
			qry.setDate("currDate", new Date());
			final LinkedHashSet distinctResults = new LinkedHashSet(qry.list());
			final List retBoundaries = new ArrayList(distinctResults);

			return retBoundaries;
		} catch (final HibernateException e) {
			LOGGER.error("Error occurred in getAllBoundaries", e);
			throw new EGOVRuntimeException("Error occurred in getAllBoundaries", e);
		}
	}

	public List getAllBoundariesHistory(final BoundaryType bndryType, final Long topLevelBoundaryID) {
		try {
			final Long hierarchyLevel = bndryType.getHierarchy();

			final StringBuffer sbf = new StringBuffer(50);
			sbf.append("AND BI");
			for (Long i = 1l; i < hierarchyLevel; i++) {
				sbf.append(".parent");
			}

			if (hierarchyLevel == 1) {
				sbf.append(".id = :topLevelBoundaryID ");
			} else {
				sbf.append(" = :topLevelBoundaryID ");
			}

			final Query qry = getSession().createQuery("from Boundary BI left join fetch BI.children where " + "BI.boundaryType = :boundaryType " + sbf.toString() + " " + " and BI.isHistory='N' order by BI.name");
			qry.setEntity("boundaryType", bndryType);
			qry.setLong("topLevelBoundaryID", topLevelBoundaryID);
			final LinkedHashSet distinctResults = new LinkedHashSet(qry.list());
			final List retBoundaries = new ArrayList(distinctResults);

			return retBoundaries;
		} catch (final HibernateException e) {
			LOGGER.error("Error occurred in getAllBoundariesHistory", e);
			throw new EGOVRuntimeException("Error occurred in getAllBoundariesHistory", e);
		}
	}

	public List getAllBoundariesByBndryTypeId(final Long bndryTypeId) {
		try {
			final Query qry = getSession().createQuery("from Boundary BI where BI.boundaryType.id=:bndryTypeId and BI.isHistory='N' order by BI.name");
			qry.setLong("bndryTypeId", bndryTypeId);

			return qry.list();
		} catch (final HibernateException e) {
			LOGGER.error("Error occurred in getAllBoundariesByBndryTypeId", e);
			throw new EGOVRuntimeException("Error occurred in getAllBoundariesByBndryTypeId", e);
		}
	}

	public List getAllBoundaries(final Long heirarchyLevel, final HierarchyType heirarchyType, final Long topLevelBoundaryID) {
		try {
			final Query qry = getSession().createQuery(
					"from Boundary BI where BI.boundaryType.hierarchy = :hierarchy " + "and BI.boundaryType.heirarchyType = :heirarchyType " + " and BI.isHistory='N' AND (" + "(BI.toDate IS NULL AND BI.fromDate <= :currDate) " + "OR "
							+ "(BI.fromDate <= :currDate AND BI.toDate >= :currDate)) " + " order by BI.name");

			qry.setLong("hierarchy", heirarchyLevel);
			qry.setEntity("heirarchyType", heirarchyType);
			qry.setDate("currDate", new Date());
			final List retBoundaries = new ArrayList();
			Boundary bn = null, tempBoundary = null;
			for (final Iterator iter = qry.iterate(); iter.hasNext();) {
				bn = (Boundary) iter.next();
				tempBoundary = bn;
				if (bn.getParent() == null && bn.getId().equals(Long.valueOf(topLevelBoundaryID))) {
					retBoundaries.add(bn);
				}

				while (bn.getParent() != null) {
					final Boundary parent = bn.getParent();
					if (parent.getId().equals(Long.valueOf(topLevelBoundaryID))) {
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

	public Boundary getBoundary(final Long bndryNum, final Long bndryTypeHeirarchyLevel, final HierarchyType heirarchyType, final Long topLevelBoundaryID) {
		return getBoundaryByBndryNumAsBigInteger(new Long(String.valueOf(bndryNum)), bndryTypeHeirarchyLevel, heirarchyType, topLevelBoundaryID);
	}

	public Boundary getBoundaryByBndryNumAsBigInteger(final Long bndryNum, final Long bndryTypeHeirarchyLevel, final HierarchyType heirarchyType, final Long topLevelBoundaryID) {
		try {
			final Query qry = getSession().createQuery("from Boundary BI where BI.boundaryNum = :boundaryNum AND " + "BI.boundaryType.hierarchy = :hierarchy and BI.boundaryType.heirarchyType = :heirarchyType " + " and BI.isHistory='N' ");
			qry.setLong("boundaryNum", bndryNum);

			qry.setLong("hierarchy", bndryTypeHeirarchyLevel);
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

	public Boundary getBoundary(final Long bndryNum, final Long parentBoundaryID, final BoundaryType boundaryType) {
		return getBoundaryByBndryNumAsBigInteger(new Long(String.valueOf(bndryNum)), parentBoundaryID, boundaryType);
	}

	public Boundary getBoundaryByBndryNumAsBigInteger(final Long bndryNum, final Long parentBoundaryID, final BoundaryType boundaryType) {
		try {
			final Query qry = getSession().createQuery(
					"from Boundary BI where BI.boundaryNum = :boundaryNum " + "AND BI.parent = :parent and BI.boundaryType = :bndryType " + " and BI.isHistory='N' AND (" + "(BI.toDate IS NULL AND BI.fromDate <= :currDate) " + "OR "
							+ "(BI.fromDate <= :currDate AND BI.toDate >= :currDate))");
			qry.setLong("boundaryNum", bndryNum);

			qry.setLong("parent", parentBoundaryID);
			qry.setEntity("bndryType", boundaryType);
			qry.setDate("currDate", new Date());
			final Boundary bn = (Boundary) qry.uniqueResult();

			return bn;
		} catch (final HibernateException e) {
			LOGGER.error("Error occurred in getBoundaryByBndryNumAsBigInteger", e);
			throw new EGOVRuntimeException("Error occurred in getBoundaryByBndryNumAsBigInteger", e);
		}
	}

	/*public Boundary getTopBoundary(final String boundaryName, final HierarchyType heirarchyType) {
		try {

			final BoundaryType bt = this.boundaryTypeService.getTopBoundaryType(heirarchyType);

			final Query qry = getSession().createQuery(
					"from Boundary BI where BI.name = :name and " + "BI.boundaryType.heirarchyType = :heirarchyType and BI.boundaryType = :bndryType " + " and BI.isHistory='N' AND (" + "(BI.toDate IS NULL AND BI.fromDate <= :currDate) " + "OR "
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
*/
	public List getTopBoundaries(final HierarchyType heirarchyType) {
		try {
			final Query qry = getSession().createQuery(
					"from Boundary BI where BI.boundaryType.hierarchy = :hierarchy " + "and BI.boundaryType.heirarchyType = :heirarchyType " + " and BI.isHistory='N' AND (" + "(BI.toDate IS NULL AND BI.fromDate <= :currDate) " + "OR "
							+ "(BI.fromDate <= :currDate AND BI.toDate >= :currDate))" + " order by BI.name");
			qry.setLong("hierarchy", 1l);
			qry.setEntity("heirarchyType", heirarchyType);
			qry.setDate("currDate", new Date());
			return qry.list();

		} catch (final HibernateException e) {
			LOGGER.error("Error occurred in getTopBoundary", e);
			throw new EGOVRuntimeException("Error occurred in getTopBoundary", e);
		}

	}

	public String getBoundaryNameForID(final Long id) {
		try {
			final Query qry = getSession().createQuery(
					"Select BI.name from Boundary BI where BI.id = :id " + " and BI.isHistory='N' AND (" + "(BI.toDate IS NULL AND BI.fromDate <= :currDate) " + "OR " + "(BI.fromDate <= :currDate AND BI.toDate >= :currDate))");

			qry.setLong("id", id);
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
			final Query qry = getSession().createQuery("select CI.child from CrossHeirarchyImpl CI, Boundary BI where CI.parent = :parentBoundary and CI.child=BI and BI.boundaryType = :childBoundaryType order by BI.name ");
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

			final Query qry = getSession().createQuery("from Boundary BI where BI.parent " + parentId + " and BI.isHistory='N' AND ((BI.toDate IS NULL AND BI.fromDate <= :currDate) OR (BI.fromDate <= :currDate AND BI.toDate >= :currDate))");
			if (parentBoundaryId != null && !parentBoundaryId.equals("")) {
				qry.setLong("parentBoundaryId", Long.valueOf(parentBoundaryId));
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
					"from Boundary B1 where B1.parent in (select B2.parent from Boundary B2 where B2.id in (select B3.parent from Boundary B3 where B3.id = :childBoundaryId)) "
							+ " and B1.isHistory='N' AND ((B1.toDate IS NULL AND B1.fromDate <= :currDate) OR (B1.fromDate <= :currDate AND B1.toDate >= :currDate)) order by B1.name");

			qry.setLong("childBoundaryId", Long.valueOf(childBoundaryId));
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
	public Boundary getBoundary(final Long bndryNum, final String boundaryType, final String hierarchyCode) {
		final Query qry = getSession().createQuery(
				"from Boundary BI where BI.boundaryNum = :boundaryNum" + " and BI.isHistory='N' AND " + " upper(BI.boundaryType.name)= :boundaryType AND " + " upper(BI.boundaryType.heirarchyType.code)= :hierarchyCode AND ("
						+ "(BI.toDate IS NULL AND BI.fromDate <= :currDate) OR (BI.fromDate <= :currDate AND BI.toDate >= :currDate)) ");
		qry.setLong("boundaryNum", bndryNum);
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
	public List<Boundary> getAllchildBoundaries(final Long bndryTypeId) {
		try {
			List<Boundary> childBoundaries = null;
			if (bndryTypeId != null && !bndryTypeId.equals("")) {
				final Query qry = getSession().createQuery("from Boundary BI where BI.parent is not null and BI.parent.id=:bndryTypeId and BI.isHistory='N' order by BI.name");
				qry.setLong("bndryTypeId", bndryTypeId);
				childBoundaries = qry.list();
			}
			return childBoundaries;
		} catch (final HibernateException e) {
			LOGGER.error("Error occurred in getAllBoundariesByBndryTypeId", e);
			throw new EGOVRuntimeException("Error occurred in getAllBoundariesByBndryTypeId", e);
		}
	}

	
	public List<Boundary> findByNameLike(String name) {
	    return getSession().createCriteria(Boundary.class).add(Restrictions.ilike("bndryNameLocal", name,MatchMode.ANYWHERE)).addOrder(Order.asc("bndryNameLocal")).list();
	}

	/**
	 * 
	 * @param boundaryId
	 * @return
	 * @throws Exception
	 */
	public List<Boundary> getChildBoundaries(final Long boundaryId) throws Exception {

		List<Boundary> childBoundaries = null;

		try {

			String parentId = null;
			if (boundaryId == null ) {
				parentId = "IS NULL";
			} else {
				parentId = " = :parentBoundaryId";
			}

			final Query qry = getSession().createQuery("from Boundary BI where BI.parent " + parentId + " and BI.isHistory='N' AND ((BI.toDate IS NULL AND BI.fromDate <= :currDate) OR (BI.fromDate <= :currDate AND BI.toDate >= :currDate))");
			if (boundaryId != null ) {
				qry.setLong("parentBoundaryId",boundaryId);
			}
			qry.setDate("currDate", new Date());
			childBoundaries = qry.list();

		} catch (final Exception e) {
			throw e;
		}
		return childBoundaries;

	}
	
	/**
         * Alternative for getAllBoundariesByBndryTypeId(Long bndryTypeId)
         * This API returns all the boundaries with isHistory marked as 'N' as well as 'Y'.   
         * @param bndryTypeId
         * @return
         */
        public List getAllBoundariesInclgHxByBndryTypeId(Long bndryTypeId) {
                try {
                        Query qry = getSession().createQuery("from Boundary BI where BI.boundaryType.id=:bndryTypeId order by BI.id");
                        qry.setLong("bndryTypeId", bndryTypeId);                     
                        return (List) qry.list();
                } catch (HibernateException e) {
                        LOGGER.error("Error occurred in getHistAndNonHistBndriesByBndryTypeId", e);
                        throw new EGOVRuntimeException("Error occurred in getHistAndNonHistBndriesByBndryTypeId", e);
                }
        }
        
        /**
         * Alternative for getChildBoundaries(Long parentBoundaryId) and return all the boundaries with isHistory marked as 'N' as well as 'Y'.
         * @param parentBoundaryId
         * @return
         * @throws Exception
         * getChildBoundariesForHxAndNonHxBndry
         */
        public List<Boundary> getChildBoundariesInclgHx(Long parentBoundaryId) throws Exception {

                try {
                        String parentId = null;
                        if (parentBoundaryId == null)
                                parentId = "IS NULL";
                        else
                                parentId = " = :parentBoundaryId";

                        Query qry = getSession().createQuery("from Boundary BI where BI.parent " + parentId + " AND " +
                                        "((BI.toDate IS NULL AND BI.fromDate <= :currDate) OR (BI.fromDate <= :currDate AND BI.toDate >= :currDate))");
                        if (parentBoundaryId != null && !parentBoundaryId.equals(""))
                                qry.setLong("parentBoundaryId", parentBoundaryId);
                        qry.setDate("currDate", new Date());
                        return qry.list();
                } catch (Exception e) {
                        LOGGER.error("Error occurred while getting Child Boundaries for Parent Boundary : "+parentBoundaryId,e);
                        throw new EGOVRuntimeException("Error occurred while getting Child Boundaries",e);
                }

        }

        /**
         * Alternative for getBoundaryById(Long bndryID) and return all the boundaries with isHistory marked as 'N' as well as 'Y'.
         * @param bndryID
         * @return
         */
        public Boundary getBoundaryInclgHxById(Long bndryID) {
                try {
                        Query qry = getSession().createQuery("from Boundary BI where BI.id=:bndryID ");
                        qry.setLong("bndryID", bndryID);                     
                        return (Boundary) qry.uniqueResult();
                } catch (HibernateException e) {
                        LOGGER.error("Error occurred in getBoundaryforHxAndNonHxBndryById", e);
                        throw new EGOVRuntimeException("Error occurred in getBoundaryforHxAndNonHxBndryById", e);
                }
        }

}
