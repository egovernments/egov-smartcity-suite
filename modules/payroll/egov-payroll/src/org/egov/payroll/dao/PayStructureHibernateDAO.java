/*
 * PayStructureHibernateDAO.java Created on Aug 29, 2007
 *
 * Copyright 2007 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.egov.payroll.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.payroll.model.PayScaleHeader;
import org.egov.payroll.model.PayStructure;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * This Class implememets the PayScaleDetailsHibernateDAO for the Hibernate
 * specific Implementation
 * 
 * @author Lokesh
 * @version 2.00
 */

public class PayStructureHibernateDAO extends GenericHibernateDAO implements
PayStructureDAO {

	private static final Logger LOGGER = Logger.getLogger(PayStructureHibernateDAO.class) ;
	public PayStructureHibernateDAO(Class persistentClass, Session session) {
		super(persistentClass, session);
	}
	  public PayStructure getPayStructureById(Integer Id)
	  {
		  Query qry = getSession().createQuery("from PayStructure pay where pay.id =:Id");
	      qry.setInteger("Id", Id);
	      return (PayStructure)qry.uniqueResult();  
	  }
	  public List getPayStructureByEmp(Integer empId)
	{
			List payStructureList=new ArrayList();
	    	Query qry = getSession().createQuery("from PayStructure S where S.employee.idPersonalInformation =:empId ");
			qry.setInteger("empId", empId);
			payStructureList=qry.list();
			return payStructureList;
	}
	public PayStructure getCurrentPayStructureForEmp(Integer empid) {
		List<PayStructure> payStructures = new ArrayList<PayStructure>();
		//PayStructure payStructure;
    	Query qry = getSession().createQuery("from PayStructure S where effectiveFrom in (select max(stemp.effectiveFrom) from PayStructure stemp where stemp.effectiveFrom<=SYSDATE and stemp.employee.idPersonalInformation=:empId) and S.employee.idPersonalInformation =:empId ");
		qry.setInteger("empId", empid);
		payStructures = qry.list();
		if(!payStructures.isEmpty()){
			return payStructures.get(0);
		}
		return null;		
		//payStructure=(PayStructure)qry.uniqueResult();
	}
	public PayStructure getPayStructureForEmpByDate(Integer empid,Date date) {		
		//PayStructure payStructure;
		List<PayStructure> payStructures = new ArrayList<PayStructure>();
    	Query qry = getSession().createQuery("from PayStructure S where effectiveFrom in (select max(stemp.effectiveFrom) from PayStructure stemp where stemp.effectiveFrom<=:date and stemp.employee.idPersonalInformation=:empId) and S.employee.idPersonalInformation =:empId ");
		qry.setInteger("empId", empid);
		qry.setDate("date", date);
		payStructures = qry.list();
		if(!payStructures.isEmpty()){
			return payStructures.get(0);
		}
		return null;		
		//payStructure=(PayStructure)qry.uniqueResult();
		//return payStructure;
	}
	public String checkExistingPayStructureInPayslip(PayScaleHeader payScaleHeader)throws Exception{
		try{
			String check = null;
			List<PayStructure> paystrcList = new ArrayList<PayStructure>();
			Query qry = getSession().createQuery("select PS from PayStructure PS, EmpPayroll EMP, PersonalInformation PI " +
					"where EMP.employee.idPersonalInformation = PI.idPersonalInformation and " +
					"PS.employee.idPersonalInformation = PI.idPersonalInformation and " +
					"PS.payHeader =:payScaleHeader");
			qry.setParameter("payScaleHeader", payScaleHeader);
			paystrcList = qry.list();
			if(paystrcList.isEmpty()){
				check = "notExist";
			}
			else
			{
				check = "exist";
			}
			return check;
		}catch(Exception e){
			LOGGER.error(e.getMessage());
			throw e;
		}
	}
	
	
}
