/*
 * PayScaleHeaderHibernateDAO.java Created on Aug 29, 2007
 *
 * Copyright 2007 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.payroll.dao;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.payroll.model.PayScaleHeader;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * This Class implememets the PayScaleHeaderHibernateDAO for the Hibernate
 * specific Implementation
 * 
 * @author Lokesh
 * @version 2.00
 */

public class PayScaleHeaderHibernateDAO extends GenericHibernateDAO implements
		PayScaleHeaderDAO {
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.egov.infstr.utils.ejb.support.AbstractStatelessSessionBean#ejbCreate()
	 */
	private static final Logger LOGGER = Logger.getLogger(PayScaleHeaderHibernateDAO.class);

	public PayScaleHeaderHibernateDAO(Class persistentClass, Session session) {
		super(persistentClass, session);
	}
	public PayScaleHeader getPayScaleHeaderByName(String name)
    {
    	Query qry = getSession().createQuery("from PayScaleHeader h where h.name =:name ");
		qry.setString("name",name);
		return (PayScaleHeader)qry.uniqueResult();
    }
	public PayScaleHeader getPayScaleHeaderForEmp(Integer Id)
    {
    	Query qry = getSession().createQuery("select h from PayScaleHeader h,PayStructure e" +
    			" where h.id = e.payHeader.id and " +
    			" e.employee.idPersonalInformation = :Id ");
		qry.setInteger("Id",Id);
		
		return (PayScaleHeader)qry.uniqueResult();
    }
	public PayScaleHeader getPayScaleHeaderById(Integer Id)
	{
		 Query qry = getSession().createQuery("from PayScaleHeader payH where payH.id =:Id");
	      qry.setInteger("Id", Id);
	      return (PayScaleHeader)qry.uniqueResult();  
	}
	
	/*
	 * Return last payscale for employee
	 */
	public PayScaleHeader getLastPayscaleByEmp(Integer empId)throws Exception{
	
		try{
			PayScaleHeader payscaleHeader = null;
			Query qry = getSession().createQuery("select PH from PayScaleHeader PH,PayStructure PS where " +
						"PH.id = PS.payHeader.id and " +
						"PS.employee.idPersonalInformation = :empId and " +
						"PS.effectiveFrom in" +
						"(select max(PS1.effectiveFrom) from PayStructure PS1 where " +
						"PS1.employee.idPersonalInformation = :empId)");
			qry.setInteger("empId", empId);
			List<PayScaleHeader> payscaleHeaderList = qry.list();
			for(PayScaleHeader tempPayscaleHeader : payscaleHeaderList){
				payscaleHeader = tempPayscaleHeader;
				break;
			}
			return payscaleHeader;		
		}catch(Exception e){
			LOGGER.error(e.getMessage());
			throw e;
		}
	}
	@Deprecated
	public List getPayScaleByGradeAndEffectiveDate(Integer gradeId,Date empEffectiveDate)throws Exception
	{
		
		try
		{
			
			Query qry=getSession().createQuery("from PayScaleHeader ph where ph.gradeMstr.id=:gradeId " +
					"and ph.effectiveFrom<=:empEffectiveDate");
			
			qry.setInteger("gradeId", gradeId);
			qry.setDate("empEffectiveDate", empEffectiveDate);
			
			LOGGER.debug("Query for payscaleGradeEffDate"+qry);
			List<PayScaleHeader> payscaleHeaderList = qry.list();
			
			return payscaleHeaderList;
		}
		catch(Exception e){
			LOGGER.error(e.getMessage());
			throw e;
		}
			
	}
	
	public List getPayScaleByEffectiveDate(Date empEffectiveDate)throws Exception
	{
		try
		{
			Query qry=getSession().createQuery("from PayScaleHeader ph where ph.effectiveFrom<=:empEffectiveDate");
			qry.setDate("empEffectiveDate", empEffectiveDate);
			
			LOGGER.debug("Query for payscaleByEffDate"+qry);
			List<PayScaleHeader> payscaleHeaderList = qry.list();
			
			return payscaleHeaderList;
		}
		catch(Exception e){
			LOGGER.error(e.getMessage());
			throw e;
		}
			
	}
	
}
