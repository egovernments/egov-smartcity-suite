/*
 * SalaryCodesHibernateDAO.java Created on Aug 29, 2007
 *
 * Copyright 2007 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
 package org.egov.payroll.dao;


import java.util.ArrayList;
import java.util.List;

import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.payroll.model.SalaryCodes;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * This Class implememets the SalaryCodesDAO for the Hibernate specific
 * Implementation
 *
 * @author Lokesh
 * @version 2.00
 */

public class SalaryCodesHibernateDAO extends GenericHibernateDAO implements SalaryCodesDAO {


    public SalaryCodesHibernateDAO(Class persistentClass, Session session)
    {
        super(persistentClass, session);
    }
    public List getSalaryCodesByCategoryId(Integer id)
	{
    	List salaryCodesList=new ArrayList();
    	Query qry = getSession().createQuery("from SalaryCodes S where S.categoryMaster.id =:id order by S.orderId");
		qry.setInteger("id", id);
		salaryCodesList=qry.list();
		return salaryCodesList;
	}
    public List getSalaryCodesByOrderId()
	{
    	List salCodesList=new ArrayList();
    	Query qry = getSession().createQuery("from SalaryCodes S order by S.orderId ");
		salCodesList=qry.list();
		return salCodesList;
	}
    public SalaryCodes getSalaryCodesByHead(String head){
    	Query qry = getSession().createQuery("from SalaryCodes salaryCode where salaryCode.head =:head");
        qry.setString("head",head);
        return (SalaryCodes)qry.uniqueResult();
    }

    

    public List getAllSalarycodesByCategoryType(String type){
    	List salarycodes = getSession().createQuery("from SalaryCodes salarycode where" +
    						" salarycode.categoryMaster.catType='"+type+"' order by salarycode.head").list();
    	return salarycodes;

    }

    public List getAllSalaryCodesByTypeAsSortedByOrder(String type){
    	List salarycodes = getSession().createQuery("from SalaryCodes salarycode where" +
    						" salarycode.categoryMaster.catType='"+type+"' order by salarycode.orderId").list();
    	return salarycodes;

    }
    public List<SalaryCodes> getSalaryCodesByCategoryName(String categoryName){
    	List<SalaryCodes> salarycodes = new ArrayList<SalaryCodes>();
    	Query qry = getSession().createQuery("from SalaryCodes salarycode where salarycode.categoryMaster.name =:name" +
    										" order by salarycode.orderId");
    	qry.setString("name", categoryName);
    	salarycodes = qry.list();    	
    	return salarycodes;
    }
    
    public List<SalaryCodes> getSalaryCodesByCategoryNames(String categoryName1, String categoryName2){
    	List<SalaryCodes> salarycodes = new ArrayList<SalaryCodes>();
    	Query qry = getSession().createQuery("from SalaryCodes salarycode where salarycode.categoryMaster.name =:name1" +
    										" or salarycode.categoryMaster.name =:name2 order by salarycode.orderId");
    	qry.setString("name1", categoryName1);
    	qry.setString("name2", categoryName2);
    	salarycodes = qry.list();    	
    	return salarycodes;
    }
    
    public Long getMaxOrderSalarycode(){
    	Query qry = getSession().createQuery("select max(S.orderId) from SalaryCodes S");
    	return (Long)qry.uniqueResult();
    }
    
    public List<SalaryCodes> getAllSAlaryCodesSortedByOrder(){
    	List<SalaryCodes> salrycodes = new ArrayList<SalaryCodes>();
    	Query qry = getSession().createQuery("from SalaryCodes s order by s.orderId");
    	salrycodes = qry.list();    	
    	return salrycodes;
    }
    public List<SalaryCodes> getAllSalarycodesByCategoryId(Integer categoryId){
    	List<SalaryCodes> salarycodes = new ArrayList<SalaryCodes>();
    	Query qry = getSession().createQuery("from SalaryCodes salarycode where salarycode.categoryMaster.id =:id" +
    										" order by salarycode.orderId");
    	qry.setInteger("id", categoryId);
    	salarycodes = qry.list();    	
    	return salarycodes;
    }
}

