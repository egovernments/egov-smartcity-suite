/*
 * TdsHibernateDAO.java Created on Feb 24, 2007
 *
 * Copyright 2005 eGovernments Foundation. All rights reserved.
 * EGOVERNMENTS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.egov.dao.recoveries;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.egov.commons.EgPartytype;
import org.egov.commons.EgwTypeOfWork;

import org.egov.infstr.ValidationException;
import org.egov.infstr.dao.GenericHibernateDAO;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.services.SessionFactory;
import org.egov.infstr.utils.HibernateUtil;
import org.egov.model.recoveries.Recovery;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * TODO Brief Description of the purpose of the class/interface
 *
 * @author Sathish
 * @version 1.00
 */
public class TdsHibernateDAO extends GenericHibernateDAO
{
	private final Logger LOGGER = Logger.getLogger(TdsHibernateDAO.class);
	public static final SimpleDateFormat DDMMYYYYFORMAT1 = new SimpleDateFormat("dd-MMM-yyyy",new Locale("en","IN"));
	private Session session;
	
        public TdsHibernateDAO(Class persistentClass, Session session)
        {
                super(persistentClass, session);
        }

        public Recovery findById(Long id, boolean lock) {
        	session = HibernateUtil.getCurrentSession();
        	Recovery recovery;
    		if (lock) {
    			recovery = (Recovery)session.load(getPersistentClass(), id, LockMode.UPGRADE);
    		} else {
    			recovery = (Recovery) session.load(getPersistentClass(), id);
    		}
    		return recovery;
    	}
        
        public List findByEstDate(String estimateDate)
        {
        	session = HibernateUtil.getCurrentSession();
            Query qry = session.createQuery("from Recovery tds where tds.isactive='1' and tds.effectivefrom<=:estimateDate order by upper(type)");
            qry.setString("estimateDate", estimateDate);
            return qry.list();
        }

        public Recovery getTdsByType(String type)
        {
        	session = HibernateUtil.getCurrentSession();
            Query qry = session.createQuery("from Recovery tds where upper(tds.type) =:type");
            qry.setString("type", type.toUpperCase().trim());
            return (Recovery)qry.uniqueResult();
        }

        public List getAllTds()
        {
        	session = HibernateUtil.getCurrentSession();
            Query qry = session.createQuery("from Recovery order by upper(type)");
            return qry.list();
        }

        public List<Recovery> getAllActiveTds()
        {
        	session = HibernateUtil.getCurrentSession();
        	Query qry = session.createQuery("from Recovery where isactive='1' and isEarning is null or isEarning='0' order by upper(type)");
            return qry.list();
        }

        public List<Recovery> getActiveTdsFilterBy(String estimateDate, BigDecimal estCost, EgPartytype egPartytype, EgwTypeOfWork egwTypeOfWork, EgwTypeOfWork egwSubTypeOfWork)
        {
        	 Query qry;
        	 session = HibernateUtil.getCurrentSession();
             StringBuffer qryStr = new StringBuffer();
             List<Recovery> tdsList=null;
             qryStr.append("from Recovery tds where tds.isactive='1' ");
             qry = session.createQuery(qryStr.toString()) ;

             if(egPartytype!=null )
             {
                 qryStr.append(" and tds.egPartytype=:egPartytype");
                 qry = session.createQuery(qryStr.toString()) ;
             }
             
             if(estCost!=null )
             {
                 qryStr.append(" and tds.id in (select ed.recovery.id from EgDeductionDetails ed where (ed.lowlimit<=:estCost and ed.highlimit>=:estCost and ed.highlimit is not null) or (ed.lowlimit<=:estCost and ed.highlimit is null)) ");
                 qry = session.createQuery(qryStr.toString()) ;
             }

             if(estimateDate!=null && !estimateDate.equals(""))
             {
                 qryStr.append(" and tds.id in (select ed.recovery.id from EgDeductionDetails ed where (ed.datefrom<=:estimateDate and ed.dateto>=:estimateDate and ed.dateto is not null) or(ed.datefrom<=:estimateDate and ed.dateto is null))");
                 qry = session.createQuery(qryStr.toString()) ;
             }

             if(egwTypeOfWork!=null )
             {
                 qryStr.append(" and tds.id in (select ed.recovery.id from EgDeductionDetails ed where ed.workDocType =:egwTypeOfWork)");
                 qry = session.createQuery(qryStr.toString()) ;
             }
             if(egwSubTypeOfWork!=null )
             {
                 qryStr.append("  and tds.id in (select ed.recovery.id from EgDeductionDetails ed where ed.workDocSubType =:egwSubTypeOfWork)");
                 qry = session.createQuery(qryStr.toString()) ;
             }

             qryStr.append(" order by upper(type)");
             qry = session.createQuery(qryStr.toString());

             if(estimateDate!=null && !estimateDate.equals(""))
            	 qry.setString("estimateDate", estimateDate);
             if(estCost!=null )
            	 qry.setBigDecimal("estCost", estCost);
             if(egPartytype!=null )
            	 qry.setEntity("egPartytype", egPartytype);
             if(egwTypeOfWork!=null )
                 qry.setEntity("egwTypeOfWork",egwTypeOfWork);
             if(egwSubTypeOfWork!=null)
                 qry.setEntity("egwSubTypeOfWork",egwSubTypeOfWork);

             tdsList=qry.list();
             return tdsList;
        }
        
        public List<Recovery> getAllTdsByPartyType(String partyType)
        {
            List<Recovery> tdses; 
            session = HibernateUtil.getCurrentSession();
        	Query qry = session.createQuery("from Recovery tds where upper(tds.egPartytype.code) =:partyType");
            qry.setString("partyType", partyType.toUpperCase().trim());
            tdses = qry.list();
            return tdses;
        }
        
        
        /**
    	 * @author manoranjan.
    	 * @description -This API returns the  List of recovery objects for party type Contractor.
    	 * @exception - ValidationException
    	 * @param asOndate - optional ,get the active recovery Objects based on supplied date.
    	 * @return listTds -List<Recovery> - list of tds objects.
    	 */
    	public List<Recovery> recoveryForPartyContractor(Date asOndate) throws ValidationException{
    		LOGGER.debug("EgovCommon | recoveryForPartyContractor | Start ");
    		LOGGER.debug("asONDate value received : "+ asOndate);
    		PersistenceService<Recovery, Integer> persistenceService = new PersistenceService<Recovery, Integer>();
    		persistenceService.setSessionFactory(new SessionFactory());
    		persistenceService.setType(Recovery.class);
    		StringBuffer recoveryQuery = new StringBuffer(400);
    		List<Recovery> listTds;
    		recoveryQuery.append("From Recovery where egPartytype.id in ( select id from EgPartytype where code=?) and isactive=true");
    		if(null != asOndate){
    			recoveryQuery.append(" and id in (select recovery.id from EgDeductionDetails where datefrom <= '").
    			append(DDMMYYYYFORMAT1.format(asOndate)).append("' AND dateto >='").
    			append(DDMMYYYYFORMAT1.format(asOndate)).append("')");
    		}
    		listTds= (List<Recovery>) persistenceService.findAllBy(recoveryQuery.toString(),"Contractor");
    		 LOGGER.debug("The size of recovery for party type Contractor is :"+ listTds.size());
    		LOGGER.debug("EgovCommon | recoveryForPartyContractor | End ");
    		return listTds;
    		
    	}
    	
    	public EgPartytype getPartytypeByCode(String code) {
        	session = HibernateUtil.getCurrentSession();
            Query qry = session.createQuery("from EgPartytype where code=:code");
            qry.setString("code", code.trim());
            return (EgPartytype) qry.uniqueResult();
    	}
    	
    	public EgwTypeOfWork getTypeOfWorkByCode(String code) {
    		session = HibernateUtil.getCurrentSession();
    		Query qry = session.createQuery("from EgwTypeOfWork where code=:code");
    		qry.setString("code", code.trim());
    		return (EgwTypeOfWork)qry.uniqueResult();
    	}
    	
    	public EgPartytype getSubPartytypeByCode(String code) {
        	session = HibernateUtil.getCurrentSession();
            Query qry = session.createQuery("from EgPartytype where code=:code and parentid is not null");
            qry.setString("code", code.trim());
            return (EgPartytype) qry.uniqueResult();
    		
    	}
    	
        public Recovery getTdsByTypeAndPartyType(String type, EgPartytype egPartytype) {
        	session = HibernateUtil.getCurrentSession();
            Query qry = session.createQuery("from Recovery tds where upper(tds.type) =:type and tds.egPartytype =:egPartytype");
            qry.setString("type", type.toUpperCase().trim());
            qry.setEntity("egPartytype", egPartytype);
            return (Recovery)qry.uniqueResult();
        }
    	
}

