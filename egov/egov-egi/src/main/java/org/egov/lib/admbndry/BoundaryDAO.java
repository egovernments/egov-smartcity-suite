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

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.entity.HierarchyType;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infstr.utils.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Deprecated
public class BoundaryDAO {
    public static final Logger LOGGER = LoggerFactory.getLogger(BoundaryDAO.class);

    public BoundaryDAO(final SessionFactory sessionFactory) {
    }

    private Session getSession() {
        return HibernateUtil.getCurrentSession();
    }

    public Boundary getBoundary(final Long bndryID) {
        try {
            final Query qry = getSession().createQuery(
                    "from Boundary BI where BI.id=:bndryID and BI.isHistory='N' " + " AND ("
                            + "(BI.toDate IS NULL AND BI.fromDate <= :currDate) " + "OR "
                            + "(BI.fromDate <= :currDate AND BI.toDate >= :currDate))");
            qry.setLong("bndryID", bndryID);
            qry.setDate("currDate", new Date());
            return (Boundary) qry.uniqueResult();
        } catch (final HibernateException e) {
            LOGGER.error("Error occurred in getBoundary", e);
            throw new ApplicationRuntimeException("Error occurred in getBoundary", e);
        }

    }

    public Boundary getAllBoundaryById(final Long bndryID) {
        try {
            final Query qry = getSession().createQuery("from Boundary BI where BI.id=:bndryID");
            qry.setLong("bndryID", bndryID);
            return (Boundary) qry.uniqueResult();
        } catch (final HibernateException e) {
            LOGGER.error("Error occurred in getBoundary", e);
            throw new ApplicationRuntimeException("Error occurred in getBoundary", e);
        }

    }

    public Boundary getBoundaryById(final Long bndryID) {
        try {
            final Query qry = getSession().createQuery("from Boundary BI where BI.id=:bndryID and BI.isHistory='N' ");
            qry.setLong("bndryID", bndryID);

            return (Boundary) qry.uniqueResult();
        } catch (final HibernateException e) {
            LOGGER.error("Error occurred in getBoundaryById", e);
            throw new ApplicationRuntimeException("Error occurred in getBoundaryById", e);
        }

    }

    public List getAllBoundariesByBndryTypeId(final Long bndryTypeId) {
        try {
            final Query qry = getSession().createQuery(
                    "from Boundary BI where BI.boundaryType.id=:bndryTypeId and BI.isHistory='N' order by BI.name");
            qry.setLong("bndryTypeId", bndryTypeId);

            return qry.list();
        } catch (final HibernateException e) {
            LOGGER.error("Error occurred in getAllBoundariesByBndryTypeId", e);
            throw new ApplicationRuntimeException("Error occurred in getAllBoundariesByBndryTypeId", e);
        }
    }

    public List getTopBoundaries(final HierarchyType heirarchyType) {
        try {
            final Query qry = getSession().createQuery(
                    "from Boundary BI where BI.boundaryType.hierarchy = :hierarchy "
                            + "and BI.boundaryType.heirarchyType = :heirarchyType " + " and BI.isHistory='N' AND ("
                            + "(BI.toDate IS NULL AND BI.fromDate <= :currDate) " + "OR "
                            + "(BI.fromDate <= :currDate AND BI.toDate >= :currDate))" + " order by BI.name");
            qry.setLong("hierarchy", 1l);
            qry.setEntity("heirarchyType", heirarchyType);
            qry.setDate("currDate", new Date());
            return qry.list();

        } catch (final HibernateException e) {
            LOGGER.error("Error occurred in getTopBoundary", e);
            throw new ApplicationRuntimeException("Error occurred in getTopBoundary", e);
        }

    }

    /**
     * returns set of parent boundary for the given child boundary
     *
     * @param childBoundary
     * @return
     */
    public Set getCrossHeirarchyParent(final Boundary childBoundary) {
        final Set parentBoundarySet = new HashSet();
        if (childBoundary == null)
            throw new ApplicationRuntimeException("Childbndry.object.null");
        else {
            final Query qry = getSession().createQuery(
                    "select CI.parent from CrossHierarchy CI where CI.child = :childBoundary");
            qry.setEntity("childBoundary", childBoundary);

            for (final Iterator iter = qry.iterate(); iter.hasNext();) {
                final Boundary bndryObj = (Boundary) iter.next();
                parentBoundarySet.add(bndryObj);

            }

        }
        return parentBoundarySet;
    }

    /**
     * returns set of child boundary for the given parent boundary and
     * childBoundaryType
     *
     * @param parentBoundary
     * @return
     */
    public Set getCrossHeirarchyChildren(final Boundary parentBoundary, final BoundaryType childBoundaryType) {
        final Set childBoundarySet = new HashSet();
        if (parentBoundary == null || childBoundaryType == null)
            throw new ApplicationRuntimeException("parentBoundary.childBoundaryType.object.null");
        else {
            final Query qry = getSession()
                    .createQuery(
                            "select CI.child from CrossHierarchy CI, Boundary BI where CI.parent = :parentBoundary and CI.child=BI and BI.boundaryType = :childBoundaryType order by BI.name ");
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
     *
     * @param parentBoundaryId
     * @return {@link List<Boundary>}
     * @throws Exception
     */
    public List<Boundary> getChildBoundaries(final String parentBoundaryId) throws Exception {

        List<Boundary> childBoundaries = null;

        try {

            String parentId = null;
            if (parentBoundaryId == null || parentBoundaryId.equals(""))
                parentId = "IS NULL";
            else
                parentId = " = :parentBoundaryId";

            final Query qry = getSession()
                    .createQuery(
                            "from Boundary BI where BI.parent "
                                    + parentId
                                    + " and BI.isHistory='N' AND ((BI.toDate IS NULL AND BI.fromDate <= :currDate) OR (BI.fromDate <= :currDate AND BI.toDate >= :currDate))");
            if (parentBoundaryId != null && !parentBoundaryId.equals(""))
                qry.setLong("parentBoundaryId", Long.valueOf(parentBoundaryId));
            qry.setDate("currDate", new Date());
            childBoundaries = qry.list();

        } catch (final Exception e) {
            throw e;
        }
        return childBoundaries;

    }

    /**
     * Returns the boundary associated with a boundary number, boundary type and
     * hierarchy type. The boundary type and hierarchy type is not case
     * sensitive.
     *
     * @param bndryNum
     * @param boundaryType
     *            - Boundary type name eg. City, Ward
     * @param hierarchyCode
     *            - Code of hierarchyType
     * @return
     */
    public Boundary getBoundary(final Long bndryNum, final String boundaryType, final String hierarchyCode) {
        final Query qry = getSession()
                .createQuery(
                        "from Boundary BI where BI.boundaryNum = :boundaryNum"
                                + " and BI.isHistory='N' AND "
                                + " upper(BI.boundaryType.name)= :boundaryType AND "
                                + " upper(BI.boundaryType.heirarchyType.code)= :hierarchyCode AND ("
                                + "(BI.toDate IS NULL AND BI.fromDate <= :currDate) OR (BI.fromDate <= :currDate AND BI.toDate >= :currDate)) ");
        qry.setLong("boundaryNum", bndryNum);
        qry.setString("boundaryType", boundaryType.toUpperCase());
        qry.setString("hierarchyCode", hierarchyCode.toUpperCase());
        qry.setDate("currDate", new Date());
        return (Boundary) qry.uniqueResult();
    }

    /**
     * Used to get all the child Boundary from a given Boundary ID
     *
     * @param bndryTypeId
     * @return {@link List<Boundary>}
     * @throws Exception
     */
    public List<Boundary> getAllchildBoundaries(final Long bndryTypeId) {
        try {
            List<Boundary> childBoundaries = null;
            if (bndryTypeId != null && !bndryTypeId.equals("")) {
                final Query qry = getSession()
                        .createQuery(
                                "from Boundary BI where BI.parent is not null and BI.parent.id=:bndryTypeId and BI.isHistory='N' order by BI.name");
                qry.setLong("bndryTypeId", bndryTypeId);
                childBoundaries = qry.list();
            }
            return childBoundaries;
        } catch (final HibernateException e) {
            LOGGER.error("Error occurred in getAllBoundariesByBndryTypeId", e);
            throw new ApplicationRuntimeException("Error occurred in getAllBoundariesByBndryTypeId", e);
        }
    }

    /**
     * @param boundaryId
     * @return
     * @throws Exception
     */
    public List<Boundary> getChildBoundaries(final Long boundaryId) throws Exception {

        List<Boundary> childBoundaries = null;

        try {

            String parentId = null;
            if (boundaryId == null)
                parentId = "IS NULL";
            else
                parentId = " = :parentBoundaryId";

            final Query qry = getSession()
                    .createQuery(
                            "from Boundary BI where BI.parent "
                                    + parentId
                                    + " and BI.isHistory='N' AND ((BI.toDate IS NULL AND BI.fromDate <= :currDate) OR (BI.fromDate <= :currDate AND BI.toDate >= :currDate))");
            if (boundaryId != null)
                qry.setLong("parentBoundaryId", boundaryId);
            qry.setDate("currDate", new Date());
            childBoundaries = qry.list();

        } catch (final Exception e) {
            throw e;
        }
        return childBoundaries;

    }

    /**
     * Alternative for getAllBoundariesByBndryTypeId(Long bndryTypeId) This API
     * returns all the boundaries with isHistory marked as 'N' as well as 'Y'.
     *
     * @param bndryTypeId
     * @return
     */
    public List getAllBoundariesInclgHxByBndryTypeId(final Long bndryTypeId) {
        try {
            final Query qry = getSession().createQuery(
                    "from Boundary BI where BI.boundaryType.id=:bndryTypeId order by BI.id");
            qry.setLong("bndryTypeId", bndryTypeId);
            return qry.list();
        } catch (final HibernateException e) {
            LOGGER.error("Error occurred in getHistAndNonHistBndriesByBndryTypeId", e);
            throw new ApplicationRuntimeException("Error occurred in getHistAndNonHistBndriesByBndryTypeId", e);
        }
    }

    /**
     * Alternative for getChildBoundaries(Long parentBoundaryId) and return all
     * the boundaries with isHistory marked as 'N' as well as 'Y'.
     *
     * @param parentBoundaryId
     * @return
     * @throws Exception
     *             getChildBoundariesForHxAndNonHxBndry
     */
    public List<Boundary> getChildBoundariesInclgHx(final Long parentBoundaryId) throws Exception {

        try {
            String parentId = null;
            if (parentBoundaryId == null)
                parentId = "IS NULL";
            else
                parentId = " = :parentBoundaryId";

            final Query qry = getSession()
                    .createQuery(
                            "from Boundary BI where BI.parent "
                                    + parentId
                                    + " AND "
                                    + "((BI.toDate IS NULL AND BI.fromDate <= :currDate) OR (BI.fromDate <= :currDate AND BI.toDate >= :currDate))");
            if (parentBoundaryId != null && !parentBoundaryId.equals(""))
                qry.setLong("parentBoundaryId", parentBoundaryId);
            qry.setDate("currDate", new Date());
            return qry.list();
        } catch (final Exception e) {
            LOGGER.error("Error occurred while getting Child Boundaries for Parent Boundary : " + parentBoundaryId, e);
            throw new ApplicationRuntimeException("Error occurred while getting Child Boundaries", e);
        }

    }

    /**
     * Alternative for getBoundaryById(Long bndryID) and return all the
     * boundaries with isHistory marked as 'N' as well as 'Y'.
     *
     * @param bndryID
     * @return
     */
    public Boundary getBoundaryInclgHxById(final Long bndryID) {
        try {
            final Query qry = getSession().createQuery("from Boundary BI where BI.id=:bndryID ");
            qry.setLong("bndryID", bndryID);
            return (Boundary) qry.uniqueResult();
        } catch (final HibernateException e) {
            LOGGER.error("Error occurred in getBoundaryforHxAndNonHxBndryById", e);
            throw new ApplicationRuntimeException("Error occurred in getBoundaryforHxAndNonHxBndryById", e);
        }
    }

}
