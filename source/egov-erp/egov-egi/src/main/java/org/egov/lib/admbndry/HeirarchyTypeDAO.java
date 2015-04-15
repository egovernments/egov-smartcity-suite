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

import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.egov.exceptions.DuplicateElementException;
import org.egov.exceptions.EGOVRuntimeException;
import org.egov.exceptions.NoSuchObjectException;
import org.egov.exceptions.TooManyValuesException;
import org.egov.infra.admin.master.entity.HierarchyType;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HeirarchyTypeDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(HeirarchyTypeDAO.class);
    private SessionFactory sessionFactory;

    public HeirarchyTypeDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * @see org.egov.lib.admbndry.ejb.api.HeirarchyTypeManager#create(org.egov.infra.admin.master.entity.HierarchyType)
     */
    public void create(final HierarchyType heirarchyType) throws DuplicateElementException {
        try {
            getSession().save(heirarchyType);
        } catch (final HibernateException e) {
            LOGGER.error("Error occurred in create", e);
            throw new EGOVRuntimeException("Error occurred in create", e);
        }
    }

    /**
     * @see org.egov.lib.admbndry.ejb.api.HeirarchyTypeManager#update(org.egov.infra.admin.master.entity.HierarchyType)
     */
    public void update(final HierarchyType heirarchyType) throws NoSuchElementException {
        try {
            getSession().saveOrUpdate(heirarchyType);
        } catch (final HibernateException e) {
            LOGGER.error("Error occurred in update", e);
            throw new EGOVRuntimeException("Error occurred in update", e);
        }

    }

    /**
     * @see org.egov.lib.admbndry.ejb.api.HeirarchyTypeManager#remove(org.egov.infra.admin.master.entity.HierarchyType)
     */
    public void remove(final HierarchyType heirarchyType) throws NoSuchElementException {
        try {
            getSession().delete(heirarchyType);
        } catch (final HibernateException e) {
            LOGGER.error("Error occurred in remove", e);
            throw new EGOVRuntimeException("Error occurred in remove", e);
        }

    }

    /**
     * @see org.egov.lib.admbndry.ejb.api.HeirarchyTypeManager#getHeirarchyTypeByID(int)
     */
    public HierarchyType getHeirarchyTypeByID(final int heirarchyTypeId) {
        try {
            return (HierarchyType) getSession().load(HierarchyType.class, heirarchyTypeId);
        } catch (final HibernateException e) {
            LOGGER.error("Error occurred in getHeirarchyTypeByID", e);
            throw new EGOVRuntimeException("Error occurred in getHeirarchyTypeByID", e);
        }
    }

    /**
     * @see org.egov.lib.admbndry.ejb.api.HeirarchyTypeManager#getAllHeirarchyTypes()
     */
    public Set<HierarchyType> getAllHeirarchyTypes() {
        try {
            return new LinkedHashSet(getSession().createQuery("from HierarchyType HT order by code asc").list());
        } catch (final HibernateException e) {
            LOGGER.error("Error occurred in getAllHeirarchyTypes", e);
            // HibernateUtil.rollbackTransaction();
            throw new EGOVRuntimeException("Error occurred in getAllHeirarchyTypes", e);
        }
    }

    public HierarchyType getHierarchyTypeByName(final String name) throws NoSuchObjectException, TooManyValuesException {

        if (name == null) {
            throw new EGOVRuntimeException("heirarchyType.name.null");
        }

        try {
            HierarchyType heirarchyType = null;
            final Query qry = getSession().createQuery("from HierarchyType HT where trim(upper(HT.name)) =:name ");
            final String ucaseName = name.toUpperCase();
            qry.setString("name", ucaseName);
            final List<HierarchyType> heirarchyTypeList = qry.list();
            if (heirarchyTypeList.size() == 0) {
                throw new NoSuchObjectException("heirarchyType.object.notFound");
            }
            if (heirarchyTypeList.size() > 1) {
                throw new TooManyValuesException("heirarchyType.multiple.objectsFound");
            }
            if (heirarchyTypeList.size() == 1) {
                heirarchyType = heirarchyTypeList.get(0);
            }
            return heirarchyType;

        } catch (final Exception e) {
            LOGGER.error("Error occurred in getHierarchyTypeByName", e);
            throw new EGOVRuntimeException("system.error", e);
        }
    }

}
