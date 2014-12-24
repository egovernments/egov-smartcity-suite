/*
 * SalaryCategoryMasterHibernateDAO.java.java Created on Aug 29, 2007
 *
 * Copyright 2007 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
 package org.egov.payroll.dao;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.infstr.utils.EgovMasterDataCaching;
import org.egov.payroll.model.SalaryCategoryMaster;
import org.hibernate.Query;
import org.hibernate.Session;


public class SalaryCategoryMasterHibernateDAO extends GenericHibernateDAO implements SalaryCategoryMasterDAO {

    public SalaryCategoryMasterHibernateDAO(Class persistentClass, Session session)
    {
        super(persistentClass, session);
    }
    
    public SalaryCategoryMaster getSalCategoryMaster(String name){
    	Query qry = getSession().createQuery("from SalaryCategoryMaster salCategory where salCategory.name =:name");        
        qry.setString("name",name);       
        return (SalaryCategoryMaster)qry.uniqueResult();
    }
    
    public List getCategorymasterByType(String type)
    {
        Query qry = getSession().createQuery("from SalaryCategoryMaster scm where scm.catType = :type");
        qry.setString("type", type);
        return qry.list();
    }

    public SalaryCategoryMaster getSalCategoryMasterCach(String name)throws Exception{
    	SalaryCategoryMaster scm = null;
    	List<SalaryCategoryMaster> categorys = EgovMasterDataCaching.getInstance().get("egp-category");
    	for(Iterator<SalaryCategoryMaster> it = categorys.iterator();it.hasNext();){
    		scm = it.next();
    		if(scm.getName().equals(name)){
    			
    			return scm;
    		}	
    	}   	
    	return scm;
    }
   
}

