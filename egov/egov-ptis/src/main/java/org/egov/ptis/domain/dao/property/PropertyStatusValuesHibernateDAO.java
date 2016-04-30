/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency, 
 *    accountability and the service delivery of the government  organizations.
 * 
 *     Copyright (C) <2015>  eGovernments Foundation
 * 
 *     The updated version of eGov suite of products as by eGovernments Foundation 
 *     is available at http://www.egovernments.org
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or 
 *     http://www.gnu.org/licenses/gpl.html .
 * 
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 * 
 * 	1) All versions of this program, verbatim or modified must carry this 
 * 	   Legal Notice.
 * 
 * 	2) Any misrepresentation of the origin of the material is prohibited. It 
 * 	   is required that all modified versions of this material be marked in 
 * 	   reasonable ways as different from the original version.
 * 
 * 	3) This license does not grant any rights to any user of the program 
 * 	   with regards to rights under trademark law for use of the trade names 
 * 	   or trademarks of eGovernments Foundation.
 * 
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org
 ******************************************************************************/
package org.egov.ptis.domain.dao.property;

import org.egov.ptis.domain.entity.property.BasicProperty;
import org.egov.ptis.domain.entity.property.PropertyStatusValues;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Repository(value = "propertyStatusValuesDAO")
@Transactional(readOnly = true)
public class PropertyStatusValuesHibernateDAO implements PropertyStatusValuesDAO {

    @PersistenceContext
    private EntityManager entityManager;

    private Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Override
    public PropertyStatusValues getLatestPropertyStatusValuesByPropertyIdAndCode(String PropertyId,
            List Code) {
        Query qry = getCurrentSession()
                .createQuery(
                        "from PropertyStatusValues PSV "
                                + "left join fetch PSV.basicProperty BP "
                                + "left join fetch PSV.propertyStatus PS "
                                + "where PSV.isActive ='Y' and BP.upicNo =:PropertyId and PS.statusCode in (:Code) "
                                + "order by PSV.createdDate desc").setMaxResults(1);
        qry.setString("PropertyId", PropertyId);
        qry.setParameterList("Code", Code);
        return (PropertyStatusValues) qry.uniqueResult();
    }

    public PropertyStatusValues getLatestPropertyStatusValuesByPropertyIdAndreferenceNo(String PropertyId,
            String referenceNumber)
    {
        Query qry = getCurrentSession()
                .createQuery(
                        "from PropertyStatusValues PSV "
                                + " where PSV.basicProperty.upicNo =:PropertyId and PSV.referenceNo in (:referenceNumber) "
                                + " order by PSV.createdDate desc").setMaxResults(1);
        qry.setString("PropertyId", PropertyId);
        qry.setString("referenceNumber", referenceNumber);
        return (PropertyStatusValues) qry.uniqueResult();

    }

    @Override
    public List<PropertyStatusValues> getParentBasicPropsForChild(BasicProperty basicProperty) {
        List<PropertyStatusValues> propStatValueList = new ArrayList<PropertyStatusValues>();
        if (basicProperty != null) {
            Query qry = getCurrentSession()
                    .createQuery(
                            "from PropertyStatusValues PSV left join fetch PSV.propertyStatus PS where PSV.basicProperty =:BasicPropertyId and PS.statusCode = 'CREATE' and PSV.isActive='Y' ");
            qry.setString("BasicPropertyId", basicProperty.getId().toString());
            propStatValueList = qry.list();
        }
        return propStatValueList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<PropertyStatusValues> getPropertyStatusValuesByReferenceBasicProperty(BasicProperty basicProperty) {
        Query qry = getCurrentSession()
                .createQuery(
                        "from PropertyStatusValues PSV left join fetch PSV.propertyStatus PS where PSV.referenceBasicProperty.id =:BasicPropertyId and PS.statusCode = 'CREATE' and PSV.isActive='Y' ");
        qry.setParameter("BasicPropertyId", basicProperty.getId());
        return (List<PropertyStatusValues>) qry.list();
    }
    
    @Override
    public PropertyStatusValues findById(Integer id, boolean lock) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<PropertyStatusValues> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PropertyStatusValues create(PropertyStatusValues propertyStatusValues) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void delete(PropertyStatusValues propertyStatusValues) {
        // TODO Auto-generated method stub

    }

    @Override
    public PropertyStatusValues update(PropertyStatusValues propertyStatusValues) {
        // TODO Auto-generated method stub
        return null;
    }

}
