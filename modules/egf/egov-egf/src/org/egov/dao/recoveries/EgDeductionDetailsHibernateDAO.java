/*
 * EgDeductionDetailsHibernateDAO.java Created on Oct 5, 2007
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.dao.recoveries;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.egov.commons.EgPartytype;
import org.egov.commons.EgwTypeOfWork;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.model.recoveries.EgDeductionDetails;
import org.egov.model.recoveries.Recovery;
import org.hibernate.Query;
import org.hibernate.Session;
/**
 * @author Iliyaraja s
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EgDeductionDetailsHibernateDAO extends GenericHibernateDAO
{
	private Session session;
	
    public EgDeductionDetailsHibernateDAO(Class persistentClass, Session session)
    {
            super(persistentClass, session);
    }

    public List findByTds(Recovery tds)
	{
    	session = HibernateUtil.getCurrentSession();
	Query qry = session.createQuery("from EgDeductionDetails ded where ded.recovery=:tds order by ded.id");
	qry.setEntity("tds", tds);
	return qry.list();
	}
    
    public List<EgDeductionDetails> getEgDeductionDetailsFilterBy(Recovery tds, BigDecimal amount, String date, EgwTypeOfWork egwTypeOfWork, EgwTypeOfWork egwSubTypeOfWork)
	{    	
    	session = HibernateUtil.getCurrentSession();
    	 Query qry;
         StringBuffer qryStr = new StringBuffer();
         List<EgDeductionDetails> egDeductionDetailsList=null;
         qryStr.append("from EgDeductionDetails ed where ed.recovery=:tds ");
         qry = session.createQuery(qryStr.toString());    
         
         if(amount!=null)
         {
             qryStr.append(" and ((ed.lowlimit<=:amount and ed.highlimit>=:amount and ed.highlimit is not null) or (ed.lowlimit<=:amount and ed.highlimit is null)) ");
             qry = session.createQuery(qryStr.toString()) ;
         }
         if(date!=null && !date.equals(""))
         {
             qryStr.append(" and ((ed.datefrom<=:date and ed.dateto>=:date and ed.dateto is not null) or(ed.datefrom<=:date and ed.dateto is null))");
             qry = session.createQuery(qryStr.toString()) ;
         }
         if(egwTypeOfWork!=null )
         {
             qryStr.append(" and ed.workDocType =:egwTypeOfWork");
             qry = session.createQuery(qryStr.toString()) ;
         }
         if(egwSubTypeOfWork!=null)
         {
             qryStr.append("  and ed.workDocSubType =:egwSubTypeOfWork");
             qry = session.createQuery(qryStr.toString()) ;
         }
         qryStr.append(" order by id");
         qry = session.createQuery(qryStr.toString()) ;
         if(tds!=null)
             qry.setEntity("tds",tds);
         if(date!=null && !date.equals(""))
        	 qry.setString("date", date);
         if(amount!=null )
        	 qry.setBigDecimal("amount", amount);
         if(egwTypeOfWork!=null )
             qry.setEntity("egwTypeOfWork",egwTypeOfWork);
         if(egwSubTypeOfWork!=null )
             qry.setEntity("egwSubTypeOfWork",egwSubTypeOfWork);
        
         egDeductionDetailsList=qry.list();
         return egDeductionDetailsList;		
	}
    
    public EgDeductionDetails findEgDeductionDetailsForDeduAmt(Recovery recovery, EgPartytype egPartyType, EgPartytype egPartySubType, EgwTypeOfWork docType,  Date date) {
    	EgDeductionDetails egDeductionDetails = null;
    	session = HibernateUtil.getCurrentSession();
   	 	Query qry;
        StringBuffer qryStr = new StringBuffer();
        qryStr.append("from EgDeductionDetails ed where ed.recovery=:recovery ");

        if(null != egPartySubType)
        	qryStr.append(" and ed.egpartytype =:egPartySubType ");
        if(null != docType)
        	qryStr.append(" and ed.workDocType =:docType ");
        if(null != date)
        	qryStr.append(" and ((ed.datefrom <=:date and ed.dateto>=:date and ed.dateto is not null) or (ed.datefrom<=:date and ed.dateto is null)) ");
         
        qry = session.createQuery(qryStr.toString());
        
        if(null != recovery)
            qry.setEntity("recovery",recovery);
        if(null != egPartySubType)
        	qry.setEntity("egPartySubType",egPartySubType);
        if(null != docType)
        	qry.setEntity("docType",docType);
        if(null != date)
        	qry.setDate("date", date);    
       
        egDeductionDetails = (EgDeductionDetails) qry.uniqueResult();

        return egDeductionDetails;
    }
    
}

