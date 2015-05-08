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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.exceptions.EGOVRuntimeException;
import org.egov.exceptions.NoSuchObjectException;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.entity.HierarchyType;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
@Deprecated
public class BoundaryTypeDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(BoundaryTypeDAO.class);

    private final SessionFactory sessionFactory;

    public BoundaryTypeDAO(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public BoundaryType getBoundaryType(final String bndryTypeName, final HierarchyType hierarchyType) {
        try {
            final Query qry = getSession().createQuery(
                    "from BoundaryType BT where upper(NAME) = upper(:name) and BT.hierarchyType = :hierarchyType");
            qry.setString("name", bndryTypeName);
            qry.setEntity("hierarchyType", hierarchyType);
            return (BoundaryType) qry.uniqueResult();
        } catch (final HibernateException e) {
            LOGGER.error("Exception occurred in getBoundaryType", e);
            throw new EGOVRuntimeException("Exception occurred in getBoundaryType", e);
        }
    }

    // It will return all the parent boundary as list for the input boundary id.
    // BoundaryList declared as global because recursive is used to get the
    // parent boundary.
    List<Boundary> boundaryList = new ArrayList<Boundary>();

    public List<Boundary> getParentBoundaryList(final Long boundaryId) throws NoSuchObjectException {
        try {

            final BoundaryDAO bndryDAO = new BoundaryDAO(sessionFactory);
            final Boundary bndry = bndryDAO.getBoundary(boundaryId);
            if (bndry != null) {
                boundaryList.add(bndry);
                if (bndry.getParent() != null)
                    boundaryList = getParentBoundaryList(bndry.getParent().getId());
            } else
                throw new NoSuchObjectException("bndry.Obj.null");

            return boundaryList;
        } catch (final Exception e) {
            LOGGER.error("Exception occurred in getParentBoundaryList", e);
            throw new EGOVRuntimeException("system.error", e);
        }
    }

    // It will return all the parent boundary Types as list for the input
    // heirarchy type.
    // Filtered boundary types, if their parent is null.
    public List<BoundaryType> getParentBoundaryTypeByHirarchy(final HierarchyType hierarchyType)
            throws EGOVRuntimeException {
        try {
            List<BoundaryType> boundaryTypeList = new ArrayList<BoundaryType>();

            if (hierarchyType != null) {
                final Query qry = getSession().createQuery(
                        "from BoundaryType BT where BT.parent is not null and  BT.hierarchyType = :hierarchyType");
                qry.setEntity("hierarchyType", hierarchyType);
                boundaryTypeList = qry.list();
            }
            return boundaryTypeList;

        } catch (final Exception e) {
            LOGGER.error("Exception occurred in getParentBoundaryTypeByHirarchy", e);
            throw new EGOVRuntimeException("system.error", e);
        }
    }

    /**
     * It will return all the parent boundary as map for the input heirarchy
     * type. First Getting Top level Boundary Type in a hierarchy(next to
     * "city"). Second loop getting all the boundaries for individual boundary
     * types. Returns Map which mapped to Boundary Type Name and List of
     * boundaries.
     */
    public Map<String, List<Boundary>> getSecondLevelBoundaryByPassingHeirarchy(final HierarchyType hierarchyType)
            throws EGOVRuntimeException {
        try {
            List<Boundary> boundaryList = new ArrayList<Boundary>();
            final Map<String, List<Boundary>> retSet = new HashMap<String, List<Boundary>>();
            String bryName = null;
            final Date currDate = new Date();
            // Getting Top level Boundary Type in a hierarchy.
            if (hierarchyType != null) {

                final Query qry = getSession().createQuery(
                        "from BoundaryType BT where BT.parent.parent is null and  BT.hierarchyType = :hierarchyType");
                qry.setEntity("hierarchyType", hierarchyType);
                final BoundaryType boundry = (BoundaryType) qry.uniqueResult();
                if (boundry != null) {
                    final Query qry1 = getSession().createQuery(
                            "from Boundary BI where " + "BI.boundaryType = :boundaryType "
                                    + " and BI.isHistory='N' AND ("
                                    + "(BI.toDate IS NULL AND BI.fromDate <= :currDate) " + "OR "
                                    + "(BI.fromDate <= :currDate AND BI.toDate >= :currDate)) "
                                    + "order by BI.boundaryNum");
                    qry1.setEntity("boundaryType", boundry);
                    qry1.setDate("currDate", currDate);
                    boundaryList = qry1.list();
                    for (final Boundary boundary : boundaryList)
                        bryName = boundary.getBoundaryType().getName();
                    if (bryName != null)
                        retSet.put(bryName, boundaryList);
                }
            }
            return retSet;

        } catch (final Exception e) {
            LOGGER.error("Exception occurred in getParentBoundaryTypeByHirarchy", e);
            throw new EGOVRuntimeException("system.error", e);
        }
    }
}
