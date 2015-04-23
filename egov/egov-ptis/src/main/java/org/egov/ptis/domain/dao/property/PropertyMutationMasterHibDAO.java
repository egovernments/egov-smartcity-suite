package org.egov.ptis.domain.dao.property;

import java.util.List;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.ptis.domain.entity.property.PropertyMutationMaster;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * This Class implememets the PropertyOccupationDAO for the Hibernate specific 
 * Implementation 
 * 
 * @author Neetu
 * @version 2.00
 */

public class PropertyMutationMasterHibDAO extends GenericHibernateDAO implements PropertyMutationMasterDAO
{
    /**
     * @param persistentClass
     * @param session
     */
    public PropertyMutationMasterHibDAO(Class persistentClass, Session session)
    {
        super(persistentClass, session);
    }
    //this method return list of ProperyMutationMaster objects based on type which is passed as parameter type
    public List getAllPropertyMutationMastersByType(String type){
    	Query qry = getCurrentSession().createQuery("from PropertyMutationMaster PM where upper(PM.type) = :type order by PM.orderId");
		qry.setString("type", type.toUpperCase());
		return qry.list();
    }
  //this method returns ProperyMutationMaster object based on code which is passed as a parameter 
	public PropertyMutationMaster getPropertyMutationMasterByCode(String code) {
		Query qry = getCurrentSession().createQuery("from PropertyMutationMaster PM where upper(PM.code) = :code");
		qry.setString("code", code.toUpperCase());
		return (PropertyMutationMaster)qry.uniqueResult();
	}
    
}