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
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
/*
 * EgRemittanceHibernateDAO.java Created on Oct 10, 2007
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.deduction.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.egov.commons.CFinancialYear;
import org.egov.commons.Fund;
import org.egov.deduction.model.EgRemittance;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.model.recoveries.Recovery;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;

/**
 * TODO Brief Description of the purpose of the class/interface
 * 
 * @author Sathish
 * @version 1.00   
 */
@Transactional(readOnly=true)
public class EgRemittanceHibernateDAO extends GenericHibernateDAO
{
	private final static  Logger LOGGER = Logger.getLogger(EgRemittanceHibernateDAO.class);
	public EgRemittanceHibernateDAO(Class persistentClass, Session session)
	{
		super(persistentClass, session);
	}	
	public List<EgRemittance> getEgRemittanceFilterBy(Fund fund, Recovery recovery, String month, CFinancialYear financialyear)
	{
		Query qry;
		StringBuffer qryStr = new StringBuffer();
		List<EgRemittance> egRemittanceList=null;
		qryStr.append("From EgRemittance rmt where rmt.voucherheader.type='Payment' and rmt.voucherheader.status=0");
		qry =HibernateUtil.getCurrentSession().createQuery(qryStr.toString()) ;
		if(fund!=null)
	    {
	    	qryStr.append(" and (rmt.fund = :fund)");
	    	qry =HibernateUtil.getCurrentSession().createQuery(qryStr.toString());
	    }
		if(recovery!=null)
	    {
	    	qryStr.append(" and (rmt.tds = :recovery)");
	    	qry =HibernateUtil.getCurrentSession().createQuery(qryStr.toString()) ;
	    }
	    if(month!=null)
	    {
	    	qryStr.append(" and (rmt.month = :month)");
	    	qry =HibernateUtil.getCurrentSession().createQuery(qryStr.toString()) ;
	    }
	    if(financialyear!=null)
	    {
	    	qryStr.append(" and (rmt.financialyear =:financialyear)");
	    	qry =HibernateUtil.getCurrentSession().createQuery(qryStr.toString()) ;
	    }
	    
	    qryStr.append(" order by upper(rmt.tds.type)");
	    qry =HibernateUtil.getCurrentSession().createQuery(qryStr.toString()) ;
	   
	    if(fund!=null)
	    	qry.setEntity("fund",fund);
	    if(recovery!=null)
	       	qry.setEntity("recovery",recovery);
	    if(month!=null)
	      	qry.setString("month",month);
	    if(financialyear!=null)
	      	qry.setEntity("financialyear",financialyear);
	  
	    if(LOGGER.isDebugEnabled())     LOGGER.debug("qryStr "+qryStr.toString());
	    egRemittanceList=qry.list();
        return egRemittanceList;
	}
}

